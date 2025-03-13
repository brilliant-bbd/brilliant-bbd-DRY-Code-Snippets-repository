output "vpc_id" {
  description = "ID of the VPC being used"
  value       = local.vpc_id
}

output "vpc_cidr" {
  description = "CIDR block of the VPC being used"
  value       = local.vpc_cidr
}

output "subnet_ids" {
  description = "List of subnet IDs available for use"
  value       = local.final_subnet_ids
}

output "app_security_group_id" {
  description = "ID of the application tier security group"
  value       = aws_security_group.app_sg.id
}

output "db_security_group_id" {
  description = "ID of the database tier security group"
  value       = aws_security_group.db_sg.id
}