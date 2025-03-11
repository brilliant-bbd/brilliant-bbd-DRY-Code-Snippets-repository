output "vpc_id" {
  description = "ID of the VPC"
  value       = module.networking.vpc_id
}

output "subnet_ids" {
  description = "List of subnet IDs"
  value       = module.networking.subnet_ids
}

output "db_instance_endpoint" {
  description = "Connection endpoint of the RDS instance"
  value       = module.database.db_instance_endpoint
}

output "db_name" {
  description = "Name of the database"
  value       = module.database.db_name
}

# Can be used by scripts to connect to the database
output "db_connection_string" {
  description = "PostgreSQL connection string (without credentials)"
  value       = "postgresql://${module.database.db_instance_endpoint}/${module.database.db_name}"
  sensitive   = false  
}

