# API Testing Guide (logical order)

This guide lets you test the application end-to-end in a simple and structured way.

## 1) Preparation

- Start the app:

```bash
docker compose up -d --build
```

- Base URL: `http://localhost:18080`
- Swagger UI: `http://localhost:18080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:18080/v3/api-docs`

## 2) Smoke test (API online)

### 2.1 Products (public)

```bash
curl http://localhost:18080/products
```

Expected result: HTTP 200 + product list.

---

## 3) Authentication

## 3.1 Create a user (public)

```bash
curl -X POST http://localhost:18080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test.user@example.com",
    "password": "password123"
  }'
```

Save the returned `id`.

### 3.2 Login

```bash
curl -X POST http://localhost:18080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.user@example.com",
    "password": "password123"
  }'
```

Expected result: JSON `{ "token": "..." }`

Save the token in `<ACCESS_TOKEN>`.

### 3.3 Current user

```bash
curl http://localhost:18080/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

## 4) Cart flow

### 4.1 Create a cart

```bash
curl -X POST http://localhost:18080/carts
```

Save cart `id` in `<CART_ID>`.

### 4.2 Add a product to cart

```bash
curl -X POST http://localhost:18080/carts/<CART_ID>/items \
  -H "Content-Type: application/json" \
  -d '{ "productId": 1 }'
```

### 4.3 Read cart

```bash
curl http://localhost:18080/carts/<CART_ID>
```

### 4.4 Update quantity

```bash
curl -X PUT http://localhost:18080/carts/<CART_ID>/items/1 \
  -H "Content-Type: application/json" \
  -d '{ "quantity": 2 }'
```

### 4.5 Remove an item

```bash
curl -X DELETE http://localhost:18080/carts/<CART_ID>/items/1
```

### 4.6 Clear cart

```bash
curl -X DELETE http://localhost:18080/carts/<CART_ID>/items
```

---

## 5) Checkout + orders

### 5.1 Re-add a product to cart

```bash
curl -X POST http://localhost:18080/carts/<CART_ID>/items \
  -H "Content-Type: application/json" \
  -d '{ "productId": 1 }'
```

### 5.2 Start checkout (auth required)

```bash
curl -X POST http://localhost:18080/checkout \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{ "cartId": "<CART_ID>" }'
```

Expected result: `orderId` + `checkoutUrl`.

If Stripe is not configured with a valid **test** secret key (`sk_test_...`), this call can fail with `500`.

### 5.3 Read user orders

```bash
curl http://localhost:18080/orders \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 5.4 Read a single order

```bash
curl http://localhost:18080/orders/<ORDER_ID> \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

## 6) User endpoints (auth)

### 6.1 User details

```bash
curl http://localhost:18080/users/<USER_ID> \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 6.2 Update profile

```bash
curl -X PUT http://localhost:18080/users/<USER_ID> \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{ "name": "Updated Name", "email": "test.user@example.com" }'
```

### 6.3 Change password

```bash
curl -X POST http://localhost:18080/users/<USER_ID>/change-password \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{ "oldPassword": "password123", "newPassword": "newpassword123" }'
```

---

## 7) Admin endpoints (optional)

Requires a token with `ADMIN` role.

```bash
curl http://localhost:18080/admin/hello \
  -H "Authorization: Bearer <ADMIN_ACCESS_TOKEN>"
```

## 8) Quick security summary

- Public: `POST /users`, `POST /auth/login`, `POST /auth/refresh`, `GET /products/**`, `POST /checkout/webhook`, Swagger.
- Auth required: most other routes (`/auth/me`, `/orders`, `/checkout`, `/users/**`, etc.).
- Admin required: `/admin/**`, `POST/PUT/DELETE /products/**`.

## 9) Troubleshooting

- Check containers:

```bash
docker compose ps
docker compose logs -f api
```

- Stop cleanly:

```bash
docker compose down
```
