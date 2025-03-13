variable "application_name" {
  description = "Name of the Elastic Beanstalk application"
  type        = string
}

variable "environment" {
  description = "Environment (dev, staging, prod)"
  type        = string
}

variable "solution_stack_name" {
  description = "Solution stack name for the Elastic Beanstalk environment"
  type        = string
  default     = "64bit Amazon Linux 2023 v4.4.4 running Docker"
}

variable "tier" {
  description = "Elastic Beanstalk environment tier"
  type        = string
  default     = "WebServer"
}

variable "vpc_id" {
  description = "ID of the VPC to deploy the Elastic Beanstalk environment"
  type        = string
}

variable "subnet_ids" {
  description = "List of subnet IDs for the Elastic Beanstalk environment"
  type        = list(string)
}

variable "security_group_id" {
  description = "Security group ID for the Elastic Beanstalk instances"
  type        = string
}

variable "instance_type" {
  description = "Instance type for the Elastic Beanstalk instances"
  type        = string
  default     = "t3.micro"
}

variable "min_instances" {
  description = "Minimum number of instances in the Auto Scaling group"
  type        = number
  default     = 1
}

variable "max_instances" {
  description = "Maximum number of instances in the Auto Scaling group"
  type        = number
  default     = 2
}

variable "environment_variables" {
  description = "Environment variables for the Elastic Beanstalk environment"
  type        = map(string)
  default     = {}
}

variable "load_balancer_type" {
  description = "Type of load balancer for the Elastic Beanstalk environment"
  type        = string
  default     = "application"
}

variable "db_endpoint" {
  description = "RDS endpoint for database connection"
  type        = string
  default     = ""
}

variable "db_port" {
  description = "RDS port for database connection"
  type        = string
  default     = ""
}

variable "db_name" {
  description = "Database name for database connection"
  type        = string
  default     = ""
}

variable "db_username" {
  description = "Database username for database connection"
  type        = string
  default     = ""
}

variable "db_password" {
  description = "Database password for database connection"
  type        = string
  default     = ""
  sensitive   = true
}