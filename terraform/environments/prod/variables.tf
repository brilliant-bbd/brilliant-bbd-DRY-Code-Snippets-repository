# General
variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "af-south-1"
}

variable "project_name" {
  description = "Name of the project, used for resource naming"
  type        = string
  default     = "terraform-project"
}

# VPC configuration
variable "vpc_cidr" {
  description = "CIDR block for the VPC if a new one needs to be created"
  type        = string
  default     = "172.31.0.0/16"
}

# Database configuration
variable "db_identifier" {
  description = "Identifier for the RDS instance"
  type        = string
}

variable "db_name" {
  description = "Name of the database to create"
  type        = string
}

variable "db_username" {
  description = "Username for the master DB user"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "Password for the master DB user"
  type        = string
  sensitive   = true
}

variable "db_instance_class" {
  description = "Instance class for the RDS instance"
  type        = string
  default     = "db.t3.micro"
}

variable "db_engine_version" {
  description = "Version of PostgreSQL to use"
  type        = string
  default     = "14.7"
}

variable "allocated_storage" {
  description = "Allocated storage in GB"
  type        = number
  default     = 20
}

variable "db_multi_az" {
  description = "Whether to enable Multi-AZ deployment"
  type        = bool
  default     = false
}

variable "db_skip_final_snapshot" {
  description = "Whether to skip the final snapshot when destroying the instance"
  type        = bool
  default     = true
}

variable "environment" {
  description = "Environment name (dev, test, prod)"
  type        = string
  default     = "dev"
}

variable "eb_solution_stack_name" {
  description = "Elastic Beanstalk solution stack name"
  type        = string
}

variable "eb_instance_type" {
  description = "EC2 instance type for Elastic Beanstalk environment"
  type        = string
  default     = "t3.micro"
}

variable "eb_min_instances" {
  description = "Minimum number of instances for Elastic Beanstalk auto scaling"
  type        = number
  default     = 1
}

variable "eb_max_instances" {
  description = "Maximum number of instances for Elastic Beanstalk auto scaling"
  type        = number
  default     = 2
}

variable "db_availability_zone" {
  description = "The AZ where the RDS instance will be created"
  type        = string
  default     = null  # Let AWS choose 
}

variable "required_subnet_count" {
  description = "The AZ where the RDS instance will be created"
  type        = number
  default     = 2  
}