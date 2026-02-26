# Architecture — Recruiter Edition

This document presents the architecture in a way that is quick to scan for technical interviews and hiring reviews.

## 1) What this backend delivers

Store API is a modular Spring Boot e-commerce backend that supports:

- secure authentication with JWT (access token + refresh token)
- product catalog browsing
- cart lifecycle management
- checkout initiation through Stripe
- payment status synchronization via Stripe webhook
- order history retrieval for authenticated users

In short: it covers the full customer path from browsing to paid order confirmation.

## 2) Business flow in one view

1. Customer browses products (`GET /products`)
2. Customer creates/updates cart (`/carts/**`)
3. Customer authenticates (`/auth/login`, `/auth/refresh`, `/auth/me`)
4. Customer starts checkout (`POST /checkout`)
5. Backend creates an order and Stripe checkout session
6. Stripe webhook updates payment status on the order (`POST /checkout/webhook`)
7. Customer reads own orders (`GET /orders`, `GET /orders/{id}`)

## 3) High-level technical architecture

```text
Client (Web/Mobile)
                      |
                      v
Spring Boot REST API (Controllers)
                      |
                      v
Service Layer (Business Rules + Orchestration)
                      |
                      v
Repository Layer (Spring Data JPA)
                      |
                      v
MySQL (schema versioned by Flyway)

External Integration: Stripe (Checkout Session + Webhook)
```

## 4) Module breakdown

- `auth` - Login, refresh, current user - JWT generation/parsing and security filter integration
- `users` - Registration, profile update, password change - Role assignment (`USER`/`ADMIN`) and validation rules
- `products` - Product listing, filtering by category, product CRUD - Admin restrictions on write operations
- `carts` - Cart creation and item operations (add/update/remove/clear)
- `payments` - Checkout orchestration with Stripe - Webhook signature verification and payment outcome mapping
- `orders` - Order persistence and retrieval, scoped to current authenticated user
- `common` - Shared exception handling, OpenAPI setup, logging/security abstractions

## 5) Core request lifecycle

For a protected endpoint:

1. Request enters Spring Security filter chain
2. `JwtAuthenticationFilter` reads and validates `Authorization: Bearer <token>`
3. If valid, user identity + role are loaded into `SecurityContext`
4. Controller receives request and delegates to service
5. Service applies domain/business rules
6. Repositories perform data access
7. Controller returns JSON response with proper HTTP status

## 6) Security architecture

### Authentication model

- Access token is returned in response body after successful login
- Refresh token is stored in an HTTP-only secure cookie
- Refresh endpoint issues new access tokens without forcing full re-login

### Authorization model

- Default policy: `anyRequest().authenticated()`
- Public routes include: - `POST /users` - `POST /auth/login` - `POST /auth/refresh` - `GET /products/**` - `POST /checkout/webhook` - Swagger/OpenAPI routes
- Admin routes include: - `/admin/**` - `POST/PUT/DELETE /products/**`

### Security controls and behaviors

- Stateless API sessions (`SessionCreationPolicy.STATELESS`)
- CSRF disabled for token-based API style
- Centralized 401/403 handling through security config
- Password hashing via BCrypt

## 7) Data and persistence

- Primary database: MySQL
- ORM: Spring Data JPA / Hibernate
- Schema evolution: Flyway migrations (`V1` to `V5` currently)
- Integration testing database: H2 (test scope)

At checkout time:

- order is created from the cart
- payment session is created via Stripe
- cart is cleared when checkout session creation succeeds
- if payment session creation fails, order creation is rolled back logically (order removed)
- webhook events update order payment status (`PAID` / `FAILED`)

## 8) Integration architecture (Stripe)

The Stripe integration is intentionally abstracted behind `PaymentGateway`:

- `createCheckoutSession(order)` encapsulates external session creation
- `parseWebhookRequest(...)` validates signature and maps Stripe events

This abstraction improves testability and allows future gateway replacement with minimal impact to service logic.

## 9) API quality and DX

- OpenAPI/Swagger UI is enabled for exploration and contract visibility
- Validation with `spring-boot-starter-validation`
- Consistent exception-to-HTTP mapping in controllers and common handlers

## 10) Testing and CI confidence

### Test strategy in repository

- Unit and integration tests for key domains: - `auth` (`AuthServiceTest`, `AuthServiceIntegrationTest`) - `orders` (`OrderServiceTest`, `OrderServiceIntegrationTest`) - `payments` (`CheckoutServiceTest`, `CheckoutServiceIntegrationTest`) - app bootstrap (`StoreApplicationTests`)

### Continuous integration

GitHub Actions workflow (`.github/workflows/ci-cd.yml`) runs on push/PR to `main`:

1. Java 17 setup (Temurin)
2. Maven compile (`./mvnw -q clean compile`)
3. Maven tests (`./mvnw -q test`)

## 11) Deployability and runtime

- Local runtime: Docker + Docker Compose
- Production demo runtime: Azure Linux VM (no Docker), Java process managed by `systemd`
- Public exposure through `Nginx` reverse proxy (`80 -> 8080`)
- Environment-specific configuration through `application-*.yaml` and `.env`
- Database in production demo: local MySQL service on VM

See deployment runbook: [deployment-azure-vm.md](deployment-azure-vm.md)

## 12) Why this architecture is hiring-signal strong

- Clear layered design (controller/service/repository)
- Real security implementation (JWT + role-based authorization)
- Real external payment integration with webhook reconciliation
- Controlled database evolution (Flyway)
- Automated build/test gate in CI
- Documentation and API discoverability through Swagger

This is not a toy CRUD only project: it demonstrates end-to-end backend engineering concerns that are expected in production-oriented teams.
