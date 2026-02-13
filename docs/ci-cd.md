# CI/CD

This project uses a simple GitHub Actions pipeline to keep feedback fast and reliable.

## Workflow file

- `.github/workflows/ci-cd.yml`

## Triggers

- Push on `main`
- Pull request to `main`

## What is executed

1. Checkout repository
2. Setup Java 17 (Temurin)
3. Maven compile
4. Maven tests

Equivalent local commands:

```bash
./mvnw clean compile
./mvnw test
```

PowerShell:

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd test
```

## Goal

- Ensure every change compiles
- Ensure regression tests pass before merge
