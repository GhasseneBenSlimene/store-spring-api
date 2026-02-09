# 📋 Installation & Setup Guide

## Table of Contents
1. [Local Development Setup](#local-development-setup)
2. [Docker Setup](#docker-setup)
3. [GitHub Setup](#github-setup)
4. [CI/CD Configuration](#cicd-configuration)
5. [Deployment](#deployment)

---

## Local Development Setup

### Step 1: Prerequisites
```bash
# Check Java version (should be 17+)
java -version

# Check Maven
./mvnw -v

# Check Git
git --version
```

### Step 2: Clone & Configure
```bash
# Clone the repository
git clone https://github.com/GhasseneBenSlimene/store-api.git
cd store-api

# Create environment file
cp .env.example .env

# Edit with your values
nano .env  # or use your editor
```

### Step 3: Build & Run
```bash
# Download dependencies
./mvnw clean install

# Run tests
./mvnw test

# Start the application
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**App is running:**
```
🚀 http://localhost:8080
📚 Swagger: http://localhost:8080/swagger-ui.html
❤️ Health: http://localhost:8080/actuator/health
```

---

## Docker Setup

### Prerequisites
- Docker Desktop installed and running
- Docker Compose (included in Docker Desktop)

### Step 1: Prepare Environment
```bash
# Copy environment
cp .env.example .env

# Edit .env (set your Stripe keys, JWT secret, etc.)
nano .env
```

### Step 2: Start Services
```bash
# Option 1: Run script
./start-docker.sh              # Linux/Mac
start-docker.bat               # Windows

# Option 2: Manual start
docker-compose up -d

# Option 3: Rebuild from scratch
docker-compose down -v
docker-compose build
docker-compose up -d
```

### Step 3: Verify
```bash
# Check containers
docker-compose ps

# View logs
docker-compose logs -f api

# Test API
curl http://localhost:8080/actuator/health

# MySQL connection
docker-compose exec database mysql -u store_user -p -e "USE store_api; SHOW TABLES;"
```

### Common Commands
```bash
# Stop
docker-compose down

# Stop and remove volumes (reset database)
docker-compose down -v

# View logs
docker-compose logs -f

# Execute command in API container
docker-compose exec api ./mvnw test
```

---

## GitHub Setup

### Step 1: Push to GitHub
```bash
# Initialize git (if not already done)
git init

# Add files
git add .

# Commit
git commit -m "Initial commit: e-commerce API with Docker & CI/CD"

# Add remote
git remote add origin https://github.com/YOUR_USERNAME/store-api.git

# Push
git branch -M main
git push -u origin main
```

### Step 2: Configure Repository Settings
1. Go to `Settings` → `General`
   - Default branch: `main`
   - Allow merge commits: Enable
   - Allow squash merging: Enable

2. Go to `Settings` → `Actions` → `General`
   - Check "Allow all actions and reusable workflows"

3. Go to `Settings` → `Branches`
   - Add branch protection rule for `main`
   - Require PR reviews: 1
   - Require status checks: Yes

---

## CI/CD Configuration

### Step 1: Add Docker Hub Credentials
1. Go to `Settings` → `Secrets and variables` → `Actions`
2. Click `New repository secret`

Add these secrets:

#### Docker Hub Authentication
| Name | Value |
|------|-------|
| `DOCKER_USERNAME` | Your Docker Hub username |
| `DOCKER_PASSWORD` | Docker Hub access token (not password) |

**How to get Docker Hub token:**
1. Goto https://hub.docker.com/settings/security
2. Click "New access token"
3. Copy the token

#### Codecov (Optional)
| Name | Value |
|------|-------|
| `CODECOV_TOKEN` | Token from codecov.io |

**How to get Codecov token:**
1. Sign in at https://codecov.io/
2. Go to your repo settings
3. Copy the token

### Step 2: Verify Workflow
1. Go to `Actions` tab
2. Click on `CI/CD Pipeline`
3. Should show green checkmark ✅

---

## Deployment

### Option 1: Docker Hub
```bash
# Build image
docker build -t yourusername/store-api:latest .

# Tag with version
docker tag yourusername/store-api:latest yourusername/store-api:v1.0.0

# Login
docker login

# Push
docker push yourusername/store-api:latest
docker push yourusername/store-api:v1.0.0
```

### Option 2: AWS ECS
```bash
# Install AWS CLI
pip install awscli

# Configure credentials
aws configure

# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR_ACCOUNT.dkr.ecr.us-east-1.amazonaws.com

# Tag and push
docker tag store-api:latest YOUR_ACCOUNT.dkr.ecr.us-east-1.amazonaws.com/store-api:latest
docker push YOUR_ACCOUNT.dkr.ecr.us-east-1.amazonaws.com/store-api:latest

# ECS update task
aws ecs update-service --cluster store-cluster --service store-service --force-new-deployment
```

### Option 3: Kubernetes
```bash
# Create deployment
kubectl create deployment store-api \
  --image=yourusername/store-api:latest \
  --port=8080

# Create service
kubectl expose deployment store-api \
  --type=LoadBalancer \
  --port=80 \
  --target-port=8080

# Check
kubectl get pods
kubectl get services
```

### Option 4: Traditional Server
```bash
# Build JAR
./mvnw clean package

# SCP to server
scp target/store-*.jar user@server:/opt/apps/

# SSH into server
ssh user@server

# Run with systemd
sudo systemctl start store-api
sudo journalctl -f -u store-api
```

---

## Production Checklist

### Security
- [ ] Change `JWT_SECRET` to strong random value
- [ ] Update `STRIPE_SECRET_KEY` and `STRIPE_WEBHOOK_SECRET_KEY`
- [ ] Configure HTTPS/TLS certificates
- [ ] Set up firewall rules
- [ ] Enable database backups
- [ ] Rotate sensitive keys regularly

### Database
- [ ] Set up proper MySQL backups
- [ ] Enable slow query logging
- [ ] Configure connection pooling
- [ ] Set up monitoring/alerting
- [ ] Test recovery procedure

### Monitoring & Logging
- [ ] Set up application monitoring (NewRelic, DataDog, etc.)
- [ ] Configure centralized logging (ELK, Splunk, etc.)
- [ ] Set up alerts for errors and performance
- [ ] Monitor disk/memory usage
- [ ] Track API performance metrics

### Code Quality
- [ ] All tests passing
- [ ] Code coverage > 80% for critical paths
- [ ] No SpotBugs warnings
- [ ] No code style violations
- [ ] Security scan passed

### Infrastructure
- [ ] Database replication for HA
- [ ] Load balancer configuration
- [ ] Auto-scaling policies
- [ ] Disaster recovery plan
- [ ] Incident response plan

---

## Troubleshooting

### Build Issues
```bash
# Clean cache
./mvnw clean

# Clear local repository
rm -rf ~/.m2/repository/com/ghassenebenslimene

# Rebuild
./mvnw clean install
```

### Docker Issues
```bash
# Docker daemon not running
# Solution: Start Docker Desktop or Docker daemon

# Port already in use
lsof -i :8080
kill -9 <PID>

# Permission denied
sudo usermod -aG docker $USER
newgrp docker
```

### Database Issues
```bash
# MySQL won't start
docker-compose logs database

# Connection refused
docker-compose exec database mysql -u root -p -e "SHOW PROCESSLIST;"

# Reset database
docker-compose down -v
docker-compose up -d database
```

### Tests Failing
```bash
# Run single test
./mvnw test -Dtest=AuthServiceTest#testLoginWithValidCredentials

# With debug output
./mvnw test -X

# Skip tests
./mvnw package -DskipTests
```

---

## Environment Profiles

### Development (`dev`)
```yaml
# Lower security, detailed logging, local database
SPRING_PROFILES_ACTIVE=dev
SPRING_JPA_SHOW_SQL=true
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/store_api_dev
```

### Production (`prod`)
```yaml
# Higher security, minimal logging, remote database
SPRING_PROFILES_ACTIVE=prod
SPRING_JPA_SHOW_SQL=false
SPRING_DATASOURCE_URL=jdbc:mysql://database-host:3306/store_api
```

### Test (`test`)
```yaml
# H2 in-memory database, test-specific config
SPRING_PROFILES_ACTIVE=test
SPRING_DATASOURCE_URL=jdbc:h2:mem:store
```

---

## Next Steps

1. **Customize** - Update branding, add features
2. **Test** - Run full test suite locally
3. **Deploy** - Push to production following checklist
4. **Monitor** - Set up monitoring and alerting
5. **Scale** - Add load balancing, caching, etc.

---

## Support & Resources

- **Documentation:** [README.md](README.md), [DOCKER.md](DOCKER.md), [CI-CD.md](CI-CD.md)
- **Spring Boot:** https://spring.io/
- **Docker:** https://docker.com/
- **GitHub Actions:** https://docs.github.com/en/actions
- **Issues:** Create GitHub issue for bugs

---

**⭐ Good luck with your project!**
