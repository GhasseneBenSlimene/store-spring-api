# CI/CD

This project uses GitHub Actions for both quality checks and deployment.

## Workflow file

- `.github/workflows/ci-cd.yml`

## Triggers

- Pull request to `main` -> CI only
- Push on `main` -> CI + CD deploy

## Pipeline stages

### 1) CI

1. Checkout repository
2. Setup Java 17 (Temurin)
3. Maven compile
4. Maven tests
5. Maven package (`target/app.jar`)
6. Upload `app.jar` as workflow artifact

### 2) CD (main branch only)

1. Download `app.jar` artifact
2. Connect to Azure VM over SSH
3. Copy `app.jar` to `~/store-api/app.jar`
4. Restart service: `sudo systemctl restart store-api`
5. Verify service state: `sudo systemctl is-active store-api`

## Required GitHub secrets

- `VM_HOST` (example: `4.223.74.14`)
- `VM_USER` (example: `ghassene`)
- `VM_SSH_KEY` (private SSH key content)

## Local equivalent for CI checks

```bash
./mvnw clean compile
./mvnw test
./mvnw -DskipTests package
```

## Goal

- Keep build and tests green before merge
- Deploy latest `main` automatically to the public VM
