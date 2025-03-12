# Terraform Workflow Makefile with Safe Destroy Commands

# Variables
BACKEND_DIR = terraform/global/s3-backend
ENV_DIR = terraform/environments/dev
BACKEND_CONFIG = backend.tfvars
VAR_FILE = terraform.tfvars

.PHONY: init-backend apply-backend init-env plan-env apply-env all destroy-env destroy-backend destroy-all safe-destroy-backend

# Initialize and apply S3 backend
init-backend:
	cd $(BACKEND_DIR) && terraform init

apply-backend:
	cd $(BACKEND_DIR) && terraform apply

# Initialize, plan and apply environment
init-env:
	cd $(ENV_DIR) && terraform init -backend-config=$(BACKEND_CONFIG)

plan-env:
	cd $(ENV_DIR) && terraform plan -var-file=$(VAR_FILE)

apply-env:
	cd $(ENV_DIR) && terraform apply -var-file=$(VAR_FILE)

# Run the complete workflow
all: init-backend apply-backend init-env plan-env apply-env

# Just initialize everything
init-all: init-backend init-env

# Plan and apply everything (assumes already initialized)
deploy-all: apply-backend plan-env apply-env

# Destroy commands
destroy-env:
	cd $(ENV_DIR) && terraform destroy -var-file=$(VAR_FILE)

# Regular destroy backend (will fail with prevent_destroy=true)
destroy-backend:
	cd $(BACKEND_DIR) && terraform destroy

# Safe destroy backend - destroys everything EXCEPT the S3 bucket
safe-destroy-backend:
	cd $(BACKEND_DIR) && terraform destroy -target=aws_dynamodb_table.terraform_locks \
	-target=aws_s3_bucket_versioning.terraform_state_versioning \
	-target=aws_s3_bucket_server_side_encryption_configuration.terraform_state_encryption \
	-target=aws_s3_bucket_public_access_block.terraform_state_public_access

# Destroy everything (environment first, then backend)
destroy-all: destroy-env safe-destroy-backend
	@echo "\n==================================================================="
	@echo "NOTICE: The S3 state bucket was NOT destroyed due to prevent_destroy"
	@echo "If you really want to destroy it, you must:"
	@echo "1. Change prevent_destroy = true to false in the bucket resource"
	@echo "2. Run: cd $(BACKEND_DIR) && terraform apply"
	@echo "3. Run: cd $(BACKEND_DIR) && terraform destroy"
	@echo "==================================================================="

# Confirm and destroy everything (with prompts for confirmation)
destroy-with-confirm:
	@echo "Are you sure you want to destroy the environment? [y/N]" && read ans && [ $${ans:-N} = y ]
	@$(MAKE) destroy-env
	@echo "Are you sure you want to destroy backend resources (except the protected S3 bucket)? [y/N]" && read ans && [ $${ans:-N} = y ]
	@$(MAKE) safe-destroy-backend
	@echo "\n==================================================================="
	@echo "NOTICE: The S3 state bucket was NOT destroyed due to prevent_destroy"
	@echo "If you really want to destroy it, you must:"
	@echo "1. Change prevent_destroy = true to false in the bucket resource"
	@echo "2. Run: cd $(BACKEND_DIR) && terraform apply"
	@echo "3. Run: cd $(BACKEND_DIR) && terraform destroy"
	@echo "==================================================================="