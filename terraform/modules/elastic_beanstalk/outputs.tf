output "elastic_beanstalk_application_name" {
  value       = aws_elastic_beanstalk_application.app.name
  description = "Name of the Elastic Beanstalk application"
}

output "elastic_beanstalk_environment_id" {
  value       = aws_elastic_beanstalk_environment.env.id
  description = "ID of the Elastic Beanstalk environment"
}

output "elastic_beanstalk_environment_name" {
  value       = aws_elastic_beanstalk_environment.env.name
  description = "Name of the Elastic Beanstalk environment"
}

output "elastic_beanstalk_endpoint" {
  value       = aws_elastic_beanstalk_environment.env.endpoint_url
  description = "URL of the Elastic Beanstalk environment"
}

output "elastic_beanstalk_instances_security_group_id" {
  value       = var.security_group_id
  description = "Security group ID used by Elastic Beanstalk instances"
}