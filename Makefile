# Define variables
TF_VARS_EXAMPLE_PATH=terraform/environments/dev/terraform.tfvars.example
TF_VARS_PATH=terraform/environments/dev/terraform.tfvars
SCRIPT_PATH=scripts/run-migrations.sh

# 1. Create folder structure
create-folders:
	mkdir -p terraform/modules/{networking,database}
	mkdir -p terraform/environments/dev
	mkdir -p flyway/{sql,conf}
	mkdir -p scripts
	mkdir -p .github/workflows

# 2. Copy files to respective locations
copy-files:
	# Add your copy commands here if any, for now, it's just a placeholder
	@echo "Files copied to respective locations (implement as needed)."

# 3. Make scripts executable
make-scripts-executable:
	chmod +x $(SCRIPT_PATH)

# 4. Create terraform.tfvars from the example
create-terraform-vars:
	cp terraform/environments/dev/terraform.tfvars.example terraform/environments/dev/terraform.tfvars
	@echo "Please edit $(TF_VARS_PATH) with your preferred values."


# 5. Initialize Terraform
terraform-init:
	cd terraform/environments/dev && terraform init

# 6. Run Terraform plan to verify configuration
terraform-plan:
	cd terraform/environments/dev && terraform plan

# 7. Apply the Terraform configuration
terraform-apply:
	cd terraform/environments/dev && terraform apply

# 8. Run database migrations
run-migrations:
	bash "$(SCRIPT_PATH)"

# Combined target for running everything
all: create-folders copy-files make-scripts-executable create-terraform-vars terraform-init terraform-plan terraform-apply run-migrations
	@echo "All steps completed successfully."
