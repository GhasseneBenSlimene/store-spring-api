# Store API Starter — Open-Source Spring Boot E-commerce Backend

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/compose/)
[![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?logo=swagger&logoColor=black)](http://localhost:18080/swagger-ui.html)
![CI](https://img.shields.io/badge/CI-enabled-brightgreen)
![Tests](https://img.shields.io/badge/tests-automated-brightgreen)

Store API is a complete e-commerce backend built with **Java 17** and **Spring Boot 3**: JWT authentication, cart and order management, Stripe integration, Flyway migrations, and OpenAPI documentation.

The project is designed as an **open-source, production-ready backend foundation** for anyone who wants to build their own e-commerce website.

## Why this project

- Modular and maintainable architecture (`auth`, `users`, `products`, `carts`, `orders`, `payments`)
- Robust security with Spring Security + JWT (public/private routes)
- Versioned database schema with Flyway
- Reproducible local runtime with Docker Compose
- Continuous quality checks through GitHub Actions (build + tests)

---

## Quick Start

### 1) Start the application

#### Option A — Without `.env` (fastest)

```bash
docker compose up -d --build
```

#### Option B — With custom `.env`

```bash
cp .env.example .env
# optional: edit .env with your values
docker compose up -d --build
```

Need help with variables? See [Environment variables](#environment-variables).

### 2) Verify that the app is running

- Swagger UI: http://localhost:18080/swagger-ui.html
- OpenAPI JSON: http://localhost:18080/v3/api-docs

### 3) Test APIs in a clear order

- Follow the end-to-end guide: [docs/api-testing-guide.md](docs/api-testing-guide.md)
- Or start with Swagger UI: http://localhost:18080/swagger-ui.html

### 4) Stop

```bash
docker compose down
```

---

## Core features

- JWT authentication (login, refresh token, current user endpoint)
- Product catalog with filtering
- Cart management (create, add, update, remove)
- Stripe checkout + webhook handling

---

## Tech stack

- Java 17, Spring Boot 3.4.1
- Spring Security, JWT
- MySQL 8, Flyway, JPA/Hibernate
- Docker, Docker Compose
- Maven, JUnit

---

## Documentation

- Docker: [docs/docker.md](docs/docker.md)
- CI/CD: [docs/ci-cd.md](docs/ci-cd.md)
- Architecture: [docs/architecture.md](docs/architecture.md)
- Full API testing guide: [docs/api-testing-guide.md](docs/api-testing-guide.md)

---

## Environment variables

- The project is testable without `.env` (demo values)
- To customize configuration: copy [.env.example](.env.example) to `.env`
- For real usage: replace all `change_me_*` values
- Default host ports: `API_HOST_PORT=18080` and `DB_HOST_PORT=13306`
