#!/bin/bash
set -e

# Get script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

# Default values
ENVIRONMENT="dev"
SKIP_TERRAFORM=false
SKIP_MIGRATIONS=false

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  key="$1"
  case $key in
    -e|--environment)
      ENVIRONMENT="$2"
      shift 2
      ;;
    --skip-terraform)
      SKIP_TERRAFORM=true
      shift
      ;;
    --skip-migrations)
      SKIP_MIGRATIONS=true
      shift
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

echo "========================================"
echo "Deploying to environment: $ENVIRONMENT"
echo "========================================"

# 1. Apply Terraform unless skipped
if [ "$SKIP_TERRAFORM" = false ]; then
  echo "Applying Terraform..."
  "$SCRIPT_DIR/apply-terraform.sh" --environment "$ENVIRONMENT" --action apply
  echo "Terraform apply completed."
else
  echo "Skipping Terraform apply."
fi

# Get database connection info from Terraform outputs
if [ "$SKIP_MIGRATIONS" = false ]; then
  echo "Getting database connection information..."
  cd "$PROJECT_ROOT/terraform/environments/$ENVIRONMENT"
  
  DB_ENDPOINT=$(terraform output -raw db_endpoint 2>/dev/null || echo "")
  DB_NAME=$(terraform output -raw db_name 2>/dev/null || echo "")
  DB_USERNAME=$(terraform output -raw db_username 2>/dev/null || echo "")
  DB_PASSWORD=$(terraform output -raw db_password 2>/dev/null || echo "")
  
  if [ -z "$DB_ENDPOINT" ] || [ -z "$DB_NAME" ] || [ -z "$DB_USERNAME" ] || [ -z "$DB_PASSWORD" ]; then
    echo "Error: Could not get database connection info from Terraform outputs"
    exit 1
  fi
  
  # 2. Run Flyway migrations
  echo "Running database migrations..."
  FLYWAY_CONFIG="$PROJECT_ROOT/flyway/conf/flyway-$ENVIRONMENT.conf"
  
  # Ensure the config file exists
  if [ ! -f "$FLYWAY_CONFIG" ]; then
    echo "Error: Flyway config file not found at $FLYWAY_CONFIG"
    exit 1
  fi
  
  # Run migrations
  "$SCRIPT_DIR/run-migrations.sh" -e "$ENVIRONMENT" -u "$DB_USERNAME" -p "$DB_PASSWORD" -h "$DB_ENDPOINT" -d "$DB_NAME"
  echo "Database migrations completed."
else
  echo "Skipping database migrations."
fi

echo "========================================"
echo "Deployment to $ENVIRONMENT completed!"
echo "========================================"