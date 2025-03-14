variable "project_name" {
  description = "Name of the project, used for resource naming"
  type        = string
  default     = "dry-code-snippets-tfoject"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC if a new one needs to be created"
  type        = string
  default     = "172.31.0.0/16"
}

variable "required_subnet_count" {
  description = "Minimum number of subnets required"
  type        = number
  default     = 2
}