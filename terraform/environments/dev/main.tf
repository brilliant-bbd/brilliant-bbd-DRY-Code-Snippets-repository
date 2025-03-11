# Provider configuration
provider "aws" {
  region = var.aws_region
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
