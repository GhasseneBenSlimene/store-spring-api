# 🛍️ E-Commerce API - Store

![CI/CD](https://github.com/GhasseneBenSlimene/store-api/actions/workflows/ci-cd.yml/badge.svg)
![Tests](https://img.shields.io/badge/tests-17%20passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-33%25-orange)
![Java](https://img.shields.io/badge/java-17-orange)
![Spring Boot](https://img.shields.io/badge/springboot-3.4.1-green)
![MySQL](https://img.shields.io/badge/mysql-8.0-blue)
![Docker](https://img.shields.io/badge/docker-multi--stage-blue)

Plateforme e-commerce moderne construite avec **Spring Boot 3.4.1** et **Java 17**, avec authentification JWT, paiements Stripe, et infrastructure de production prête avec Docker & GitHub Actions.

---

## 🚀 Quick Start

### Avec Docker (1 Commande)
```bash
# Linux/Mac
./start-docker.sh

# Windows
start-docker.bat
```

**Ou manuel:**
```bash
docker-compose up -d
# API: http://localhost:8080
```

### Sans Docker (Local Development)
```bash
# Build
./mvnw clean install

# Run
./mvnw spring-boot:run

# Tests
./mvnw test
```

---

## 📋 Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Docker & Deployment](#docker--deployment)
- [CI/CD Pipeline](#cicd-pipeline)
- [Contributing](#contributing)

---

## ✨ Features

### 🔐 Authentication & Security
- ✅ JWT-based authentication (jjwt 0.12.x)
- ✅ Role-based access control (User, Admin)
- ✅ Password encryption with Spring Security
- ✅ Refresh token mechanism
- ✅ CORS configuration

### 💳 E-Commerce Functionality
- ✅ Product management with categories
- ✅ Shopping cart system
- ✅ Order management with status tracking
- ✅ Order items with pricing
- ✅ Stripe payment integration
- ✅ Payment webhook handling

### 🧪 Quality Assurance
- ✅ **17 unit & integration tests (100% passing)**
- ✅ **33% code coverage** (auth, checkout, orders)
- ✅ JaCoCo coverage reporting
- ✅ Automated linting (SpotBugs, Checkstyle, PMD)
- ✅ Security scanning (Dependency-Check)

### 🔄 DevOps & Deployment
- ✅ Docker multi-stage build
- ✅ Docker Compose orchestration
- ✅ GitHub Actions CI/CD pipeline
- ✅ Automated testing on push
- ✅ Docker image publishing

### 📊 Data Management
- ✅ MySQL 8.0 database
- ✅ Flyway migrations (version control)
- ✅ JPA/Hibernate ORM
- ✅ Entity relationships & validations

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│         Client / Frontend               │
└────────────────┬────────────────────────┘
                 │ HTTP/REST
┌────────────────▼────────────────────────┐
│      Spring Boot API (Port 8080)        │
│  ┌──────────────────────────────────┐   │
│  │  Controllers (Request Handlers)  │   │
│  └──────────────┬───────────────────┘   │
│                 │                       │
│  ┌──────────────▼───────────────────┐   │
│  │   Services (Business Logic)      │   │
│  │  - AuthService                   │   │
│  │  - CheckoutService               │   │
│  │  - OrderService                  │   │
│  │  - ProductService                │   │
│  └──────────────┬───────────────────┘   │
│                 │                       │
│  ┌──────────────▼───────────────────┐   │
│  │  Repositories (Data Access)      │   │
│  │  - JPA/Hibernate                 │   │
│  └──────────────┬───────────────────┘   │
└────────────────┼────────────────────────┘
                 │
    ┌────────────┼────────────┐
    │            │            │
┌───▼──┐   ┌───▼──┐   ┌───▼──────┐
│MySQL │   │JWT   │   │ Stripe   │
│ DB   │   │Keys  │   │ Gateway  │
└──────┘   └──────┘   └──────────┘
```

### Request Flow
```
Request → Filter/Security → Controller → Service → Repository → Database → Response
```

---

## 💻 Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 17 (LTS) |
| **Framework** | Spring Boot | 3.4.1 |
| **Database** | MySQL | 8.0 |
| **ORM** | JPA/Hibernate | 6.6.4 |
| **auth** | JWT (jjwt) | 0.12.x |
| **Payment** | Stripe Java SDK | 29.0.0 |
| **Mapping** | MapStruct | 1.6.3 |
| **Testing** | JUnit 5 + Mockito | 5.x |
| **Coverage** | JaCoCo | 0.8.12 |
| **Build** | Maven | 3.9 |
| **Containerization** | Docker | 29.2.1 |
| **Orchestration** | Docker Compose | 5.0.2 |
| **CI/CD** | GitHub Actions | Latest |

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.9+
- MySQL 8.0 (or Docker)
- Git

### Installation

#### 1️⃣ Clone the Repository
```bash
git clone https://github.com/GhasseneBenSlimene/store-api.git
cd spring-api-finished
```

#### 2️⃣ Configuration
```bash
# Copy environment template
cp .env.example .env

# Edit .env with your settings
nano .env
```

**Required settings:**
```env
DB_NAME=store_api
DB_USER=store_user
DB_PASSWORD=MyPassword!
JWT_SECRET=your-32-char-secret-key-here
STRIPE_SECRET_KEY=sk_test_xxx
STRIPE_WEBHOOK_SECRET_KEY=whsec_xxx
```

#### 3️⃣ Build
```bash
./mvnw clean install
```

#### 4️⃣ Run
```bash
# Dev mode
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Production
./mvnw spring-boot:run
```

**API running on:**
- Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health: `http://localhost:8080/actuator/health`

---

## 📚 API Endpoints

### Authentication
```
POST   /api/auth/login              - User login
POST   /api/auth/refresh            - Refresh access token
GET    /api/auth/me                 - Get current user
```

### Products
```
GET    /api/products                - List all products
GET    /api/products/{id}           - Get product details
GET    /api/categories              - List categories
```

### Shopping Cart
```
POST   /api/carts                   - Create cart
GET    /api/carts/{id}              - Get cart
POST   /api/carts/{id}/items        - Add item to cart
DELETE /api/carts/{id}/items/{itemId} - Remove item
```

### Checkout & Orders
```
POST   /api/checkout                - Initiate payment
POST   /api/checkout/webhook        - Stripe webhook
GET    /api/orders                  - List user orders
GET    /api/orders/{id}             - Get order details
```

### Users
```
GET    /api/users/{id}              - Get user profile
PUT    /api/users/{id}              - Update profile
POST   /api/users                   - Register new user
```

---

## 🧪 Testing

### Run All Tests
```bash
./mvnw test
```

### Test Reports
```bash
# Generate coverage report
./mvnw test jacoco:report

# Open: target/site/jacoco/index.html
```

### Run Specific Test
```bash
# Single test
./mvnw test -Dtest=AuthServiceTest#testLoginWithValidCredentials

# Class
./mvnw test -Dtest=AuthServiceTest

# Package
./mvnw test -Dtest=com.ghassenebenslimene.store.auth.*
```

### Test Structure
```
src/test/
├── java/
│   └── com/ghassenebenslimene/store/
│       ├── auth/
│       │   ├── AuthServiceTest.java        (5 unit tests)
│       │   └── AuthServiceIntegrationTest.java (1 integration test)
│       ├── payments/
│       │   ├── CheckoutServiceTest.java    (4 unit tests)
│       │   └── CheckoutServiceIntegrationTest.java (1 integration test)
│       └── orders/
│           ├── OrderServiceTest.java       (3 unit tests)
│           └── OrderServiceIntegrationTest.java (2 integration tests)
└── resources/
    └── application-test.yaml          (H2 test database)
```

**Total: 17 tests, 100% passing** ✅

---

## 🐳 Docker & Deployment

### Quick Start
```bash
# Start
docker-compose up -d

# Logs
docker-compose logs -f api

# Stop
docker-compose down
```

### Build Custom Image
```bash
docker build -t my-store-api:1.0 .
```

### Push to Registry
```bash
docker tag ghassenebenslimene/store-api:latest my-registry/store-api:latest
docker push my-registry/store-api:latest
```

For detailed Docker information: [DOCKER.md](DOCKER.md)

---

## 🔄 CI/CD Pipeline

### GitHub Actions Workflow

**Automatically runs on:**
- Push to `main` or `develop`
- Pull requests to `main` or `develop`

**Pipeline stages:**
1. ✅ **Build & Test** (2m)
   - Compile code
   - Run 17 tests
   - Generate coverage report
   
2. ✅ **Lint** (1m)
   - SpotBugs: potential bugs
   - Checkstyle: code style
   - PMD: code quality

3. ✅ **Security** (2m)
   - Dependency-Check: CVE scanning
   
4. ✅ **Docker** (3m - main only)
   - Build image
   - Push to Docker Hub

**Status badge:**
```markdown
![CI/CD](https://github.com/GhasseneBenSlimene/store-api/actions/workflows/ci-cd.yml/badge.svg)
```

For detailed CI/CD info: [CI-CD.md](CI-CD.md)

---

## 📦 Project Structure

```
spring-api-finished/
├── src/
│   ├── main/
│   │   ├── java/com/ghassenebenslimene/store/
│   │   │   ├── auth/               - Authentication logic
│   │   │   ├── products/           - Product management
│   │   │   ├── carts/              - Shopping cart
│   │   │   ├── orders/             - Order management
│   │   │   ├── payments/           - Stripe integration
│   │   │   ├── users/              - User management
│   │   │   └── common/             - Shared utilities
│   │   └── resources/
│   │       ├── application.yaml    - Main config
│   │       ├── application-dev.yaml - Dev profile
│   │       ├── application-prod.yaml - Prod profile
│   │       └── db/migration/       - Flyway migrations
│   └── test/
│       ├── java/                   - Test classes
│       └── resources/
│           └── application-test.yaml - Test config (H2)
├── .github/
│   └── workflows/
│       └── ci-cd.yml              - GitHub Actions pipeline
├── Dockerfile                     - Multi-stage Docker build
├── docker-compose.yml             - API + MySQL orchestration
├── pom.xml                        - Maven configuration
├── Makefile                       - Development commands
├── start-docker.sh                - Docker startup (Linux/Mac)
├── start-docker.bat               - Docker startup (Windows)
├── DOCKER.md                      - Docker documentation
├── CI-CD.md                       - CI/CD documentation
└── README.md                      - This file
```

---

## 🛠️ Development Commands

### Using Make (Linux/Mac)
```bash
make help              # Show all commands
make build             # Build project
make test              # Run tests
make coverage          # Generate coverage report
make docker-up         # Start Docker services
make docker-down       # Stop Docker services
make docker-logs       # View Docker logs
make clean-all         # Clean everything including Docker
```

### Manual Maven
```bash
./mvnw clean install   # Full build
./mvnw test            # Run tests
./mvnw spring-boot:run # Run app
./mvnw compile         # Just compile
```

---

## 📖 Documentation

- **[DOCKER.md](DOCKER.md)** - Complete Docker guide
- **[CI-CD.md](CI-CD.md)** - CI/CD pipeline details
- **[Makefile](Makefile)** - Available commands

---

## 🔐 Security Considerations

### JWT
- ✅ Bearer token authentication
- ✅ Token expiration (15m access, 7d refresh)
- ✅ Secret key rotation possible

### Database
- ✅ Parameterized queries (JPA prevents SQL injection)
- ✅ Password hashing with BCrypt
- ✅ User role-based authorization

### Stripe
- ✅ Webhook signature verification
- ✅ PCI-DSS compliance (no card storage)
- ✅ Secure key management

---

## 🚀 Deployment

### Production Checklist
- [ ] Update `.env` with production secrets
- [ ] Change `JWT_SECRET` to strong random value
- [ ] Add real Stripe keys
- [ ] Configure database backups
- [ ] Set up monitoring/logging
- [ ] Enable HTTPS
- [ ] Configure firewall

### Deploy with Docker
```bash
# Pull latest image
docker pull ghassenebenslimene/store-api:latest

# Run
docker run -d \
  --name store-api \
  -p 8080:80 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/store_api \
  -e SPRING_DATASOURCE_USERNAME=store_user \
  -e SPRING_DATASOURCE_PASSWORD=secure-password \
  ghassenebenslimene/store-api:latest
```

---

## 📊 Metrics & Monitoring

### Coverage
- Overall: **33%**
- Auth module: **66%** ✅
- Orders module: **51%** ✅
- Checkout module: **30%**

### Performance
- API startup: ~5 seconds
- Test suite: ~20 seconds
- Docker build: ~3 minutes

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

### Code Quality Standards
- All tests must pass: `./mvnw test`
- Coverage must not decrease
- Code must follow checkstyle rules
- No SpotBugs warnings

---

## 📝 License

MIT License - see LICENSE file for details

---

## 👨‍💻 Author

**Ghassene Ben Slimene**
- GitHub: [@GhasseneBenSlimene](https://github.com/GhasseneBenSlimene)
- Email: your-email@example.com

---

## 🙏 Acknowledgments

- Spring Boot team for excellent framework
- Stripe for payment integration
- OpenJDK for great Java runtime
- Docker for containerization
- GitHub for CI/CD capabilities

---

## 📞 Support

Need help? Check these resources:
- 📖 [DOCKER.md](DOCKER.md) - Docker issues
- 🔄 [CI-CD.md](CI-CD.md) - Pipeline issues
- 🐛 [GitHub Issues](https://github.com/GhasseneBenSlimene/store-api/issues) - Report bugs
- 💬 [GitHub Discussions](https://github.com/GhasseneBenSlimene/store-api/discussions) - Ask questions

---

**⭐ If you find this project useful, please star it on GitHub!**
