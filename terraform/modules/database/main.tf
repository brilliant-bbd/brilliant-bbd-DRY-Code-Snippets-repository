# database module:

# Create the database subnet group
resource "aws_db_subnet_group" "rds_subnet_group" {
  name        = "${var.project_name}-rds-subnet-group"
  description = "Subnet group for PostgreSQL RDS"
  subnet_ids  = var.subnet_ids

  tags = {
    Name = "${var.project_name}-rds-subnet-group"
  }
}

# PostgreSQL RDS instance
resource "aws_db_instance" "postgres" {
  engine                 = "postgres"
  engine_version         = var.db_engine_version
  multi_az               = var.multi_az
  identifier             = var.db_identifier
  db_name                = var.db_name
  username               = var.db_username
  password               = var.db_password
  instance_class         = var.db_instance_class
  allocated_storage      = var.allocated_storage
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = [var.db_security_group_id]
  availability_zone      = var.availability_zone
  skip_final_snapshot    = var.skip_final_snapshot
  publicly_accessible    = true

  tags = {
    Name        = "${var.project_name}-postgres"
    Environment = var.environment
  }
}