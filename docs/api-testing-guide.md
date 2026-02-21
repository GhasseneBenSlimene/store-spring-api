# API Testing Guide (ordre logique)

Ce guide permet de tester l’application de bout en bout, de manière simple et structurée.

## 1) Préparation

- Démarrer l’app:

```bash
docker compose up -d --build
```

- Base URL: `http://localhost:18080`
- Swagger UI: `http://localhost:18080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:18080/v3/api-docs`

## 2) Smoke test (API en ligne)

### 2.1 Produits (public)

```bash
curl http://localhost:18080/products
```

Résultat attendu: HTTP 200 + liste de produits.

---

## 3) Authentification

## 3.1 Créer un utilisateur (public)

```bash
curl -X POST http://localhost:18080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test.user@example.com",
    "password": "password123"
  }'
```

Garde l’`id` retourné.

### 3.2 Login

```bash
curl -X POST http://localhost:18080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.user@example.com",
    "password": "password123"
  }'
```

Résultat attendu: JSON `{ "token": "..." }`

Sauvegarde le token dans `<ACCESS_TOKEN>`.

### 3.3 Utilisateur courant

```bash
curl http://localhost:18080/auth/me \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

## 4) Parcours panier

### 4.1 Créer un panier

```bash
curl -X POST http://localhost:18080/carts
```

Sauvegarde `id` du panier dans `<CART_ID>`.

### 4.2 Ajouter un produit au panier

```bash
curl -X POST http://localhost:18080/carts/<CART_ID>/items \
  -H "Content-Type: application/json" \
  -d '{ "productId": 1 }'
```

### 4.3 Lire le panier

```bash
curl http://localhost:18080/carts/<CART_ID>
```

### 4.4 Modifier la quantité

```bash
curl -X PUT http://localhost:18080/carts/<CART_ID>/items/1 \
  -H "Content-Type: application/json" \
  -d '{ "quantity": 2 }'
```

### 4.5 Supprimer un item

```bash
curl -X DELETE http://localhost:18080/carts/<CART_ID>/items/1
```

### 4.6 Vider le panier

```bash
curl -X DELETE http://localhost:18080/carts/<CART_ID>/items
```

---

## 5) Checkout + commandes

### 5.1 Re-ajouter un produit au panier

```bash
curl -X POST http://localhost:18080/carts/<CART_ID>/items \
  -H "Content-Type: application/json" \
  -d '{ "productId": 1 }'
```

### 5.2 Lancer le checkout (auth requis)

```bash
curl -X POST http://localhost:18080/checkout \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{ "cartId": "<CART_ID>" }'
```

Résultat attendu: `orderId` + `checkoutUrl`.

### 5.3 Lire les commandes de l’utilisateur

```bash
curl http://localhost:18080/orders \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 5.4 Lire une commande

```bash
curl http://localhost:18080/orders/<ORDER_ID> \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

---

## 6) Endpoints utilisateur (auth)

### 6.1 Détail utilisateur

```bash
curl http://localhost:18080/users/<USER_ID> \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### 6.2 Mise à jour profil

```bash
curl -X PUT http://localhost:18080/users/<USER_ID> \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{ "name": "Updated Name", "email": "test.user@example.com" }'
```

### 6.3 Changer mot de passe

```bash
curl -X POST http://localhost:18080/users/<USER_ID>/change-password \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{ "oldPassword": "password123", "newPassword": "newpassword123" }'
```

---

## 7) Endpoints admin (optionnel)

Nécessite un token avec rôle `ADMIN`.

```bash
curl http://localhost:18080/admin/hello \
  -H "Authorization: Bearer <ADMIN_ACCESS_TOKEN>"
```

## 8) Résumé sécurité rapide

- Public: `POST /users`, `POST /auth/login`, `POST /auth/refresh`, `GET /products/**`, `POST /checkout/webhook`, Swagger.
- Auth requis: la plupart des autres routes (`/auth/me`, `/orders`, `/checkout`, `/users/**`, etc.).
- Admin requis: `/admin/**`, `POST/PUT/DELETE /products/**`.

## 9) En cas de problème

- Vérifier les conteneurs:

```bash
docker compose ps
docker compose logs -f api
```

- Arrêter proprement:

```bash
docker compose down
```
