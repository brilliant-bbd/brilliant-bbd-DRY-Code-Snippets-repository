# output "elastic_beanstalk_url" {
#   description = "URL of the Elastic Beanstalk environment"
#   value       = aws_elastic_beanstalk_environment.dry_code_snippets_env.endpoint_url
# }

output "rds_endpoint" {
  description = "Endpoint of the RDS PostgreSQL instance"
  value       = aws_db_instance.postgres.endpoint
}

output "rds_port" {
  description = "Port of the RDS PostgreSQL instance"
  value       = aws_db_instance.postgres.port
}

# output "eb_app_name" {
#   description = "Name of the Elastic Beanstalk application"
#   value       = aws_elastic_beanstalk_application.dry_code_snippets_app.name
# }

# output "eb_env_name" {
#   description = "Name of the Elastic Beanstalk environment"
#   value       = aws_elastic_beanstalk_environment.dry_code_snippets_env.name
# }