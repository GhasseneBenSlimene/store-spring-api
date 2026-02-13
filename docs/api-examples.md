# API Examples

Base URL: `http://localhost:8080`

## 1) Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "your-password"
  }'
```

Expected: returns access token JSON and sets `refreshToken` cookie.

## 2) Get current user (authenticated)

```bash
curl http://localhost:8080/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

## 3) List products (public)

```bash
curl http://localhost:8080/products
```

Optional filter by category:

```bash
curl "http://localhost:8080/products?categoryId=1"
```

## 4) Create cart

```bash
curl -X POST http://localhost:8080/carts
```

Expected: returns a cart object with an `id` (UUID).

## 5) Add product to cart

```bash
curl -X POST http://localhost:8080/carts/<CART_ID>/items \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1
  }'
```

## 6) Checkout

```bash
curl -X POST http://localhost:8080/checkout \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "cartId": "<CART_UUID>"
  }'
```

Expected: returns checkout session data.

## OpenAPI / Swagger

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
