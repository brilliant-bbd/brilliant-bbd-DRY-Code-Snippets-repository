# Provider configuration
provider "aws" {
  region = var.aws_region
}

# S3 bucket for Elastic Beanstalk artifacts
resource "aws_s3_bucket" "eb_bucket" {
  bucket = var.eb_bucket_name
}

# Elastic Beanstalk application
resource "aws_elastic_beanstalk_application" "dry_code_snippets_app" {
  name        = var.application_name
  description = "DRY Code Snippets Repository API"
}

# Create a new VPC
resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name = "dry-code-snippets-vpc"
  }
}

# Create public subnets in different AZs
resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true

  tags = {
    Name = "dry-code-snippets-public-a"
  }
}

resource "aws_subnet" "public_b" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.aws_region}b"
  map_public_ip_on_launch = true

  tags = {
    Name = "dry-code-snippets-public-b"
  }
}

# Create private subnets for RDS
resource "aws_subnet" "private_a" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "${var.aws_region}a"

  tags = {
    Name = "dry-code-snippets-private-a"
  }
}

resource "aws_subnet" "private_b" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "${var.aws_region}b"

  tags = {
    Name = "dry-code-snippets-private-b"
  }
}

# Create Internet Gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "dry-code-snippets-igw"
  }
}

# Create route table for public subnets
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "dry-code-snippets-public-rt"
  }
}

# Associate route table with public subnets
resource "aws_route_table_association" "public_a" {
  subnet_id      = aws_subnet.public_a.id
  route_table_id = aws_route_table.public.id
}

resource "aws_route_table_association" "public_b" {
  subnet_id      = aws_subnet.public_b.id
  route_table_id = aws_route_table.public.id
}

# Create DB subnet group
resource "aws_db_subnet_group" "default" {
  name       = "dry-code-snippets-subnet-group"
  subnet_ids = [aws_subnet.private_a.id, aws_subnet.private_b.id]

  tags = {
    Name = "dry-code-snippets-db-subnet-group"
  }
}

# Create security group for RDS
resource "aws_security_group" "rds_sg" {
  name        = "dry-code-snippets-rds-sg"
  description = "Security group for PostgreSQL RDS"
  vpc_id      = aws_vpc.main.id
  
  # Allow PostgreSQL traffic from Elastic Beanstalk
  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    description = "PostgreSQL traffic from within VPC"
    cidr_blocks = [aws_vpc.main.cidr_block]
  }
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "dry-code-snippets-rds-sg"
  }
}

# Create security group for Elastic Beanstalk
resource "aws_security_group" "eb_sg" {
  name        = "dry-code-snippets-eb-sg"
  description = "Security group for Elastic Beanstalk"
  vpc_id      = aws_vpc.main.id
  
  # Allow HTTP
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  # Allow HTTPS
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  # Allow outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "dry-code-snippets-eb-sg"
  }
}

# PostgreSQL RDS instance
resource "aws_db_instance" "postgres" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  instance_class       = "db.t3.micro"
  identifier           = var.db_identifier
  db_name              = var.db_name
  username             = var.db_username
  password             = var.db_password
  publicly_accessible  = false
  skip_final_snapshot  = true
 
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.default.name
}

# IAM role for Elastic Beanstalk service
resource "aws_iam_role" "beanstalk_service_role" {
  name = "beanstalk-service-role"
  
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
}

# Attach AWS managed policy for Elastic Beanstalk
resource "aws_iam_role_policy_attachment" "beanstalk_service" {
  role       = aws_iam_role.beanstalk_service_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSElasticBeanstalkService"
}

# IAM role for EC2 instances
resource "aws_iam_role" "beanstalk_ec2_role" {
  name = "beanstalk-ec2-role"
  
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
}

# Attach AWS managed policy for EC2 role
resource "aws_iam_role_policy_attachment" "beanstalk_ec2_web" {
  role       = aws_iam_role.beanstalk_ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier"
}

# Create instance profile
resource "aws_iam_instance_profile" "beanstalk_ec2_profile" {
  name = "beanstalk-ec2-profile"
  role = aws_iam_role.beanstalk_ec2_role.name
}

# Elastic Beanstalk environment
resource "aws_elastic_beanstalk_environment" "dry_code_snippets_env" {
  name                = var.environment_name
  application         = aws_elastic_beanstalk_application.dry_code_snippets_app.name
  solution_stack_name = "64bit Amazon Linux 2 v3.7.12 running Corretto 17"

  # EC2 settings
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = "t3.micro"
  }
  
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.beanstalk_ec2_profile.name
  }
  
  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_sg.id
  }
  
  # VPC settings
  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = aws_vpc.main.id
  }
  
  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value     = "${aws_subnet.public_a.id},${aws_subnet.public_b.id}"
  }
  
  # Auto Scaling settings
  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MinSize"
    value     = "1"
  }
  
  setting {
    namespace = "aws:autoscaling:asg"
    name      = "MaxSize"
    value     = "2"
  }
  
  # Environment variables
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "SPRING_PROFILES_ACTIVE"
    value     = "prod"
  }
  
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_URL"
    value     = "jdbc:postgresql://${aws_db_instance.postgres.endpoint}/${var.db_name}"
  }
  
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_USERNAME"
    value     = var.db_username
  }
  
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "DB_PASSWORD"
    value     = var.db_password
  }
  
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "GOOGLE_CLIENT_ID"
    value     = var.google_client_id
  }
  
  setting {
    namespace = "aws:elasticbeanstalk:application:environment"
    name      = "GOOGLE_CLIENT_SECRET"
    value     = var.google_client_secret
  }

}