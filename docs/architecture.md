# Architecture

## High-level diagram

```text
Client (Web/Mobile)
       |
       v
Spring Boot API (Controllers)
       |
       v
Service Layer (Business Rules)
       |
       v
Repository Layer (JPA)
       |
       v
MySQL + Flyway Migrations
```

## Modules

- `auth`: login, refresh token, current user
- `users`: user CRUD and password change
- `products`: catalog and product management
- `carts`: cart lifecycle and cart items
- `orders`: order read APIs
- `payments`: checkout + Stripe webhook
- `common`: shared security rules, exception handling, OpenAPI config

## Request flow

1. HTTP request hits a controller endpoint
2. Spring Security evaluates route rules and JWT (if required)
3. Controller delegates to service layer
4. Service applies business rules and loads/saves data through repositories
5. Repository accesses MySQL (schema managed by Flyway)
6. Controller returns JSON response with HTTP status

## Security model (summary)

- Public:
  - `POST /auth/login`
  - `POST /auth/refresh`
  - `GET /products/**`
  - `POST /checkout/webhook`
  - Swagger endpoints
- Protected:
  - all other endpoints unless explicitly permitted
