# Elastic Beanstalk Application
resource "aws_elastic_beanstalk_application" "app" {
  name        = var.application_name
  description = "Elastic Beanstalk Application for ${var.application_name}"

  tags = {
    Name        = var.application_name
    Environment = var.environment
  }
}

# Elastic Beanstalk Environment
resource "aws_elastic_beanstalk_environment" "env" {
  name                = "${var.application_name}-${var.environment}"
  application         = aws_elastic_beanstalk_application.app.name
  solution_stack_name = var.solution_stack_name
  tier                = var.tier

  # Set up VPC and subnets
  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = var.vpc_id
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = join(",", var.subnet_ids)
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "AssociatePublicIpAddress"
    value     = "true"
  }
  
  # Set up EC2 instances
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = var.instance_type
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = var.security_group_id
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.eb_instance_profile.name
  }

  # Set up Auto Scaling
  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MinSize"
    value     = var.min_instances
  }

  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = var.max_instances
  }

  # Set up Environment variables
  dynamic "setting" {
    for_each = var.environment_variables
    content {
      namespace = "aws:elasticbeanstalk:application:environment"
      name      = setting.key
      value     = setting.value
    }
  }

  # Set up Health monitoring
  setting {
    namespace = "aws:elasticbeanstalk:healthreporting:system"
    name      = "SystemType"
    value     = "enhanced"
  }

  # Set up Load balancer
  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "LoadBalancerType"
    value     = var.load_balancer_type
  }

  # Set up database connection if needed
  dynamic "setting" {
    for_each = var.db_endpoint != "" ? [1] : []
    content {
      namespace = "aws:elasticbeanstalk:application:environment"
      name      = "RDS_HOSTNAME"
      value     = var.db_endpoint
    }
  }

  dynamic "setting" {
    for_each = var.db_port != "" ? [1] : []
    content {
      namespace = "aws:elasticbeanstalk:application:environment"
      name      = "RDS_PORT"
      value     = var.db_port
    }
  }

  dynamic "setting" {
    for_each = var.db_name != "" ? [1] : []
    content {
      namespace = "aws:elasticbeanstalk:application:environment"
      name      = "RDS_DB_NAME"
      value     = var.db_name
    }
  }

  dynamic "setting" {
    for_each = var.db_username != "" ? [1] : []
    content {
      namespace = "aws:elasticbeanstalk:application:environment"
      name      = "RDS_USERNAME"
      value     = var.db_username
    }
  }

  dynamic "setting" {
    for_each = var.db_password != "" ? [1] : []
    content {
      namespace = "aws:elasticbeanstalk:application:environment"
      name      = "RDS_PASSWORD"
      value     = var.db_password
    }
  }

  tags = {
    Name        = "${var.application_name}-${var.environment}"
    Environment = var.environment
  }
}

# IAM role and instance profile for Elastic Beanstalk
resource "aws_iam_role" "eb_service_role" {
  name = "${var.application_name}-${var.environment}-eb-service-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "elasticbeanstalk.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name        = "${var.application_name}-${var.environment}-eb-service-role"
    Environment = var.environment
  }
}

resource "aws_iam_role_policy_attachment" "eb_service_role_policy" {
  role       = aws_iam_role.eb_service_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSElasticBeanstalkService"
}

resource "aws_iam_role" "eb_instance_role" {
  name = "${var.application_name}-${var.environment}-eb-instance-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name        = "${var.application_name}-${var.environment}-eb-instance-role"
    Environment = var.environment
  }
}

resource "aws_iam_role_policy_attachment" "eb_instance_role_policy" {
  role       = aws_iam_role.eb_instance_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier"
}

resource "aws_iam_instance_profile" "eb_instance_profile" {
  name = "${var.application_name}-${var.environment}-eb-instance-profile"
  role = aws_iam_role.eb_instance_role.name
}