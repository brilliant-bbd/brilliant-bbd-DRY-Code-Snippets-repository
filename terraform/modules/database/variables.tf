variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "af-south-1"
}

variable "application_name" {
  description = "Name of the Elastic Beanstalk application"
  type        = string
  default     = "dry-code-snippets-api"
}

variable "environment_name" {
  description = "Name of the Elastic Beanstalk environment"
  type        = string
  default     = "dry-code-snippets-env"
}

variable "eb_bucket_name" {
  description = "Name of the S3 bucket for Elastic Beanstalk artifacts"
  type        = string
}

variable "db_identifier" {
  description = "Identifier for the RDS instance"
  type        = string
  default     = "dry-code-snippets-db"
}

variable "db_name" {
  description = "Name of the PostgreSQL database"
  type        = string
  default     = "drycodesnippetsdb"
}

variable "db_username" {
  description = "Username for the PostgreSQL database"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "Password for the PostgreSQL database"
  type        = string
  sensitive   = true
}

variable "google_client_id" {
  description = "Google OAuth client ID"
  type        = string
  sensitive   = true
}

variable "google_client_secret" {
  description = "Google OAuth client secret"
  type        = string
  sensitive   = true
}