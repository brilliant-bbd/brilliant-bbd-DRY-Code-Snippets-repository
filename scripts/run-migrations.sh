#!/bin/bash
# Script to run Flyway migrations based on environment and Terraform outputs

set -e

# Default values
ENVIRONMENT=${ENVIRONMENT:-dev}
TERRAFORM_DIR="./terraform/environments/${ENVIRONMENT}"

# Function to display usage
usage() {
  echo "Usage: $0 [options]"
  echo "Options:"
  echo "  -e, --environment ENV    Set environment (dev, staging, prod). Default: dev"
  echo "  -t, --terraform-dir DIR  Set Terraform directory. Default: ./terraform/environments/dev"
  echo "  -h, --help               Display this help message"
  exit 1
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case "$1" in
    -e|--environment)
      ENVIRONMENT="$2"
      TERRAFORM_DIR="./terraform/environments/${ENVIRONMENT}"
      shift 2
      ;;
    -t|--terraform-dir)
      TERRAFORM_DIR="$2"
      shift 2
      ;;
    -h|--help)
      usage
      ;;
    *)
      echo "Unknown option: $1"
      usage
      ;;
  esac
done

echo "Running migrations for environment: ${ENVIRONMENT}"
echo "Using Terraform directory: ${TERRAFORM_DIR}"

# Ensure we're in the project root directory
cd "$(dirname "$0")/.."

# Verify that the Terraform directory exists
if [ ! -d "${TERRAFORM_DIR}" ]; then
  echo "Error: Terraform directory does not exist: ${TERRAFORM_DIR}"
  exit 1
fi

# Get database connection details from Terraform outputs
echo "Retrieving database connection details from Terraform..."
DB_ENDPOINT=$(cd "${TERRAFORM_DIR}" && terraform output -raw db_instance_endpoint)
DB_NAME=$(cd "${TERRAFORM_DIR}" && terraform output -raw db_name)

# We need to get the sensitive values differently
echo "Please enter database username:"
read -r DB_USER
echo "Please enter database password:"
read -rs DB_PASSWORD

# Extract host and port from endpoint
DB_HOST=$(echo "${DB_ENDPOINT}" | cut -d':' -f1)
DB_PORT=$(echo "${DB_ENDPOINT}" | cut -d':' -f2)

# Create temporary Flyway configuration with connection details
TEMP_CONF=$(mktemp)
cat "flyway/conf/flyway.conf" > "${TEMP_CONF}"
cat "flyway/conf/flyway-${ENVIRONMENT}.conf" >> "${TEMP_CONF}"

# Ensure that each line is added separately to avoid concatenation issues
echo "flyway.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}" >> "${TEMP_CONF}"
echo "flyway.user=${DB_USER}" >> "${TEMP_CONF}"
echo "flyway.password=${DB_PASSWORD}" >> "${TEMP_CONF}"

# Development-specific settings, ensure these are also on their own lines
echo "flyway.cleanDisabled=false" >> "${TEMP_CONF}"
echo "flyway.outOfOrder=true" >> "${TEMP_CONF}"