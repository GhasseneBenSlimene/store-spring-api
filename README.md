# Store API — Spring Boot E-commerce Backend

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/compose/)
[![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?logo=swagger&logoColor=black)](http://localhost:18080/swagger-ui.html)
![CI](https://img.shields.io/badge/CI-enabled-brightgreen)
![Tests](https://img.shields.io/badge/tests-automated-brightgreen)

Store API est un backend e-commerce complet construit avec **Java 17** et **Spring Boot 3** : authentification JWT, gestion du panier et des commandes, intégration Stripe, migrations Flyway et documentation OpenAPI.

Le projet est pensé comme une **base backend open source, prête à l’emploi**, pour toute personne souhaitant lancer son propre site e-commerce.

## Pourquoi ce projet

- Architecture modulaire et maintenable (`auth`, `users`, `products`, `carts`, `orders`, `payments`)
- Sécurité robuste avec Spring Security + JWT (routes publiques/privées)
- Schéma de base de données versionné avec Flyway
- Exécution locale reproductible avec Docker Compose
- Qualité continue via GitHub Actions (build + tests)

---

## Quick Start

### Option A — Sans `.env` (le plus rapide)

```bash
docker compose up -d --build
```

### Option B — Avec `.env` personnalisé

```bash
cp .env.example .env
# édite .env avec tes valeurs
docker compose up -d --build
```

### Vérifier

- API: http://localhost:18080
- Swagger UI: http://localhost:18080/swagger-ui.html
- OpenAPI JSON: http://localhost:18080/v3/api-docs

### Arrêter

```bash
docker compose down
```

---

## Fonctionnalités clés

- Authentification JWT (login, refresh token, endpoint utilisateur courant)
- Catalogue produits avec filtrage
- Gestion du panier (création, ajout, mise à jour, suppression)
- Checkout Stripe + gestion du webhook

---

## Stack Technique

- Java 17, Spring Boot 3.4.1
- Spring Security, JWT
- MySQL 8, Flyway, JPA/Hibernate
- Docker, Docker Compose
- Maven, JUnit

---

## Tests

```bash
./mvnw test
```

PowerShell:

```powershell
.\mvnw.cmd test
```

---

## Documentation

- Docker: [docs/docker.md](docs/docker.md)
- CI/CD: [docs/ci-cd.md](docs/ci-cd.md)
- Architecture: [docs/architecture.md](docs/architecture.md)
- Guide de test complet: [docs/api-testing-guide.md](docs/api-testing-guide.md)

---

## Variables d'environnement

- Le projet est testable sans `.env` (valeurs de démonstration)
- Pour personnaliser la configuration : copie [.env.example](.env.example) vers `.env`
- Pour un usage réel : remplace toutes les valeurs `change_me_*`
- Ports hôte par défaut : `API_HOST_PORT=18080` et `DB_HOST_PORT=13306`
