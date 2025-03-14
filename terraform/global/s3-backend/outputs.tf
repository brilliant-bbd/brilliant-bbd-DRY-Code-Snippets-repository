output "s3_bucket_id" {
  value       = aws_s3_bucket.terraform_state.id
  description = "The ID of the S3 bucket for storing Terraform state"
}
 
output "s3_bucket_arn" {
  value       = aws_s3_bucket.terraform_state.arn
  description = "The ARN of the S3 bucket for storing Terraform state"
}
 
output "dynamodb_table_name" {
  value       = aws_dynamodb_table.terraform_locks.name
  description = "The name of the DynamoDB table for state locking"
}