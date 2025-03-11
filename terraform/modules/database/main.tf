# Provider configuration
provider "aws" {
  region = var.aws_region
}

# Check if a default VPC exists
data "aws_vpcs" "default_vpc_check" {
  filter {
    name   = "isDefault"
    values = ["true"]
  }
}

locals {
  # Check if default VPC exists
  default_vpc_exists = length(data.aws_vpcs.default_vpc_check.ids) > 0
}

# Get the default VPC if it exists
data "aws_vpc" "default" {
  count   = local.default_vpc_exists ? 1 : 0
  default = true
}

# Create a default VPC if it doesn't exist
resource "aws_vpc" "main" {
  count                = local.default_vpc_exists ? 0 : 1
  cidr_block           = "172.31.0.0/16"  # Common default VPC CIDR
  enable_dns_support   = true
  enable_dns_hostnames = true
  
  tags = {
    Name = "dry-code-snippets-vpc"
  }
}

# Determine which VPC ID to use
locals {
  vpc_id = local.default_vpc_exists ? data.aws_vpc.default[0].id : aws_vpc.main[0].id
  vpc_cidr = local.default_vpc_exists ? data.aws_vpc.default[0].cidr_block : aws_vpc.main[0].cidr_block
}

# Get available AZs
data "aws_availability_zones" "available_zones" {}

# Fetch existing default subnets if default VPC exists
data "aws_subnets" "default" {
  count = local.default_vpc_exists ? 1 : 0
  filter {
    name   = "vpc-id"
    values = [local.vpc_id]
  }
  filter {
    name   = "default-for-az"
    values = ["true"]
  }
}

# Get the details of all default subnets if they exist
data "aws_subnet" "default_subnets" {
  for_each = local.default_vpc_exists ? toset(data.aws_subnets.default[0].ids) : toset([])
  id       = each.value
}

# Calculate subnet-related values
locals {
  # Map of subnets by AZ (if default VPC exists)
  subnet_by_az = local.default_vpc_exists ? {
    for id, subnet in data.aws_subnet.default_subnets : 
    subnet.availability_zone => subnet.id
  } : {}
  
  # List of AZs that have default subnets
  available_azs = keys(local.subnet_by_az)
  
  # Number of AZs with existing subnets
  existing_subnet_count = length(local.available_azs)
  
  # We need at least 2 subnets for RDS
  subnets_needed = max(0, 2 - local.existing_subnet_count)
  
  # Choose AZs for new subnets if we need to create any
  new_subnet_azs = slice(data.aws_availability_zones.available_zones.names, 
                          local.existing_subnet_count, 
                          local.existing_subnet_count + local.subnets_needed)
                         
  # Get existing subnet IDs if any
  existing_subnet_ids = [
    for az in local.available_azs : local.subnet_by_az[az]
  ]
}

# Create subnets if needed (either because no default VPC or not enough default subnets)
resource "aws_subnet" "additional_subnet" {
  count             = local.subnets_needed
  vpc_id            = local.vpc_id
  # Create non-overlapping CIDR blocks - adjust if needed
  cidr_block        = cidrsubnet(local.vpc_cidr, 8, count.index + local.existing_subnet_count)
  availability_zone = local.new_subnet_azs[count.index]
  
  tags = {
    Name = "dry-code-snippets-subnet-${count.index + 1}"
  }
}

# If we created a new VPC, we need an internet gateway
resource "aws_internet_gateway" "igw" {
  count  = local.default_vpc_exists ? 0 : 1
  vpc_id = aws_vpc.main[0].id
  
  tags = {
    Name = "dry-code-snippets-igw"
  }
}

# Create a route table for new VPC
resource "aws_route_table" "main" {
  count  = local.default_vpc_exists ? 0 : 1
  vpc_id = aws_vpc.main[0].id
  
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw[0].id
  }
  
  tags = {
    Name = "dry-code-snippets-route-table"
  }
}

# Associate route table with subnets if we created a new VPC
resource "aws_route_table_association" "subnet_association" {
  count          = local.default_vpc_exists ? 0 : local.subnets_needed
  subnet_id      = aws_subnet.additional_subnet[count.index].id
  route_table_id = aws_route_table.main[0].id
}

# Final list of subnet IDs combining existing and new subnets
locals {
  final_subnet_ids = concat(local.existing_subnet_ids, aws_subnet.additional_subnet[*].id)
}

# Create security group for Elastic Beanstalk
resource "aws_security_group" "eb_sg" {
  name        = "dry-code-snippets-eb-sg"
  description = "Security group for Elastic Beanstalk"
  vpc_id      = local.vpc_id
  
  # Allow HTTP
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  # Allow HTTPS
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  # Allow outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "dry-code-snippets-eb-sg"
  }
}

# Create security group for RDS
resource "aws_security_group" "rds_sg" {
  name        = "dry-code-snippets-rds-sg"
  description = "Security group for PostgreSQL RDS"
  vpc_id      = local.vpc_id
  
  # Allow PostgreSQL traffic from Elastic Beanstalk
  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    description     = "PostgreSQL traffic from within VPC"
    security_groups = [aws_security_group.eb_sg.id]
  }
  
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "dry-code-snippets-rds-sg"
  }
}

# Create the database subnet group
resource "aws_db_subnet_group" "rds_subnet_group" {
  name        = "dry-code-snippets-rds-subnet-group"
  description = "Subnet group for PostgreSQL RDS"
  subnet_ids  = local.final_subnet_ids

  tags = {
    Name = "dry-code-snippets-rds-subnet-group"
  }
}

# PostgreSQL RDS instance
resource "aws_db_instance" "postgres" {
  engine                 = "postgres"
  engine_version         = "14.7"
  multi_az               = false
  identifier             = var.db_identifier
  db_name                = var.db_name
  username               = var.db_username
  password               = var.db_password
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name 
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  # Let AWS choose the best AZ with available IPs
  availability_zone      = null
  skip_final_snapshot    = true
}