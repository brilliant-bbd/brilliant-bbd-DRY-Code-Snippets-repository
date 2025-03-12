#!/bin/bash
set -e

# Get script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"

# Default values
ENVIRONMENT="dev"
ACTION="plan"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  key="$1"
  case $key in
    -e|--environment)
      ENVIRONMENT="$2"
      shift 2
      ;;
    -a|--action)
      ACTION="$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# Check if environment exists
ENV_DIR="$PROJECT_ROOT/terraform/environments/$ENVIRONMENT"
if [ ! -d "$ENV_DIR" ]; then
  echo "Error: Environment directory '$ENVIRONMENT' not found in terraform/environments"
  exit 1
fi

# Read backend configuration from tfvars file if exists
BACKEND_VARS_FILE="$ENV_DIR/backend.tfvars"
if [ -f "$BACKEND_VARS_FILE" ]; then
  source <(grep -v '^#' "$BACKEND_VARS_FILE" | sed 's/^/-e /')
  BUCKET_NAME=$(grep 'bucket' "$BACKEND_VARS_FILE" | cut -d '=' -f 2 | tr -d ' "')
  KEY_NAME=$(grep 'key' "$BACKEND_VARS_FILE" | cut -d '=' -f 2 | tr -d ' "')
  REGION=$(grep 'region' "$BACKEND_VARS_FILE" | cut -d '=' -f 2 | tr -d ' "')
  DYNAMODB_TABLE=$(grep 'dynamodb_table' "$BACKEND_VARS_FILE" | cut -d '=' -f 2 | tr -d ' "')
else
  echo "Warning: Backend configuration file not found at $BACKEND_VARS_FILE"
  echo "Will use default backend or prompt for configuration"
fi

# Change to environment directory
cd "$ENV_DIR"
echo "Working in directory: $(pwd)"

# Initialize Terraform if needed
if [ ! -d ".terraform" ]; then
  echo "Initializing Terraform..."
  if [ -n "$BUCKET_NAME" ] && [ -n "$KEY_NAME" ] && [ -n "$REGION" ] && [ -n "$DYNAMODB_TABLE" ]; then
    terraform init \
      -backend-config="bucket=$BUCKET_NAME" \
      -backend-config="key=$KEY_NAME" \
      -backend-config="region=$REGION" \
      -backend-config="dynamodb_table=$DYNAMODB_TABLE" \
      -backend-config="encrypt=true"
  else
    terraform init
  fi
fi

# Perform requested action
case $ACTION in
  plan)
    echo "Planning Terraform changes for environment: $ENVIRONMENT"
    terraform plan -var-file=terraform.tfvars -out=tfplan
    ;;
  apply)
    echo "Applying Terraform changes for environment: $ENVIRONMENT"
    if [ -f tfplan ]; then
      terraform apply tfplan
    else
      echo "Creating plan first..."
      terraform plan -var-file=terraform.tfvars -out=tfplan
      terraform apply tfplan
    fi
    ;;
  destroy)
    echo "WARNING: You are about to destroy resources in environment: $ENVIRONMENT"
    read -p "Are you sure you want to continue? (yes/no) " answer
    if [ "$answer" != "yes" ]; then
      echo "Aborting..."
      exit 0
    fi
    terraform plan -destroy -var-file=terraform.tfvars -out=tfplan
    terraform apply tfplan
    ;;
  *)
    echo "Unknown action: $ACTION"
    echo "Available actions: plan, apply, destroy"
    exit 1
    ;;
esac

echo "Done!"