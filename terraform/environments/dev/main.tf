# Provider configuration
provider "aws" {
  region = var.aws_region
}

# Configure the S3 backend
terraform {
  backend "s3" {
     bucket         = "dry-code-snippets-bucket"
     key            = "terraform.tfstate"
     region         = "af-south-1"
     dynamodb_table = "terraform-state-locks"
     encrypt        = true
  }
}

# Networking module
module "networking" {
  source = "../../modules/networking"
  
  project_name        = var.project_name
  vpc_cidr            = var.vpc_cidr
  required_subnet_count = 2  # For RDS
}

# Database module
module "database" {
  source = "../../modules/database"
  
  project_name        = var.project_name
  environment         = "dev"
  db_identifier       = var.db_identifier
  db_name             = var.db_name
  db_username         = var.db_username
  db_password         = var.db_password
  db_instance_class   = var.db_instance_class
  db_engine_version   = var.db_engine_version
  allocated_storage   = var.allocated_storage
  multi_az            = var.db_multi_az
  skip_final_snapshot = var.db_skip_final_snapshot
  
  # Values from networking module
  subnet_ids          = module.networking.subnet_ids
  db_security_group_id = module.networking.db_security_group_id
}

# Elastic Beanstalk module
module "elastic_beanstalk" {
  source = "../../modules/elastic_beanstalk"
  
  application_name  = var.project_name
  environment       = var.environment
  solution_stack_name = var.eb_solution_stack_name
  
  vpc_id             = module.networking.vpc_id
  subnet_ids         = module.networking.subnet_ids
  security_group_id  = module.networking.app_security_group_id
  
  instance_type      = var.eb_instance_type
  min_instances      = var.eb_min_instances
  max_instances      = var.eb_max_instances
  
  # Connect to database
  db_endpoint        = module.database.db_instance_endpoint
  db_port            = "5432"
  db_name            = var.db_name
  db_username        = var.db_username
  db_password        = var.db_password
  
  # Additional environment variables
  environment_variables = {
    APP_ENV = var.environment
    FLYWAY_DB_URL = "jdbc:postgresql://${module.database.db_instance_endpoint}:5432/${var.db_name}"
  }
}
