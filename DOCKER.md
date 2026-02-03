# 🐳 Docker Setup Guide

## Quick Start (1 Commande)

### Linux/Mac

```bash
./start-docker.sh
```

### Windows

```bash
start-docker.bat
```

**C'est tout!** L'API démarre automatiquement sur `http://localhost:8080`

---

## Manuel: Démarrage Détaillé

### Prérequis

- Docker Desktop (https://www.docker.com/products/docker-desktop)
- Docker Compose (inclus dans Docker Desktop)

### 1️⃣ Configuration

```bash
# Copier le fichier de configuration
cp .env.example .env

# Éditer .env avec vos clés Stripe et JWT
nano .env  # ou votre éditeur préféré
```

### 2️⃣ Démarrer les Services

```bash
# Démarrer uniquement la base de données
docker-compose up database -d

# Démarrer l'API et la base de données
docker-compose up -d

# Logs en temps réel
docker-compose logs -f api
```

### 3️⃣ Accéder à l'Application

```
API:           http://localhost:8080
Swagger UI:    http://localhost:8080/swagger-ui.html
Health Check:  http://localhost:8080/actuator/health
Database:      localhost:3306
```

---

## Commandes Utiles

### 🚀 Démarrage/Arrêt

```bash
# Démarrer
docker-compose up -d

# Arrêter
docker-compose down

# Redémarrer
docker-compose restart

# Remise à zéro (supprime les données)
docker-compose down -v
```

### 📊 Monitoring

```bash
# Voir les logs de l'API
docker-compose logs -f api

# Voir les logs de la base de données
docker-compose logs -f database

# État des services
docker-compose ps

# Statistiques d'utilisation
docker stats
```

### 🔧 Maintenance

```bash
# Reconstruire l'image Docker
docker-compose build

# Reconstruire et démarrer
docker-compose up --build -d

# Nettoyer les volumes
docker-compose down -v

# Entrer dans le conteneur API
docker-compose exec api /bin/bash

# Entrer dans la base de données MySQL
docker-compose exec database mysql -u store_user -p store_api
```

### 🧪 Tests dans Docker

```bash
# Lancer les tests
docker-compose exec api ./mvnw test

# Générer le rapport de couverture
docker-compose exec api ./mvnw test jacoco:report
```

---

## Structure Docker Compose

```yaml
Services:
├── api (Spring Boot)
│   ├── Port: 8080
│   ├── Image: ghassenebenslimene/store-api:latest
│   └── Volumes: logs/
│
└── database (MySQL 8.0)
    ├── Port: 3306
    ├── Database: store_api
    └── Volumes: mysql_data/
```

---

## Variables d'Environnement

### Dans `.env`:

```env
# Database
DB_NAME=store_api
DB_USER=store_user
DB_PASSWORD=MyPassword!
DB_ROOT_PASSWORD=root

# JWT
JWT_SECRET=your-secret-key-32-chars-minimum

# Stripe
STRIPE_SECRET_KEY=sk_test_xxx
STRIPE_WEBHOOK_SECRET_KEY=whsec_xxx
```

### Variables API

```env
SPRING_PROFILES_ACTIVE=prod
JAVA_OPTS=-Xmx512m -Xms256m
```

---

## Troubleshooting

### ❌ Port 8080 already in use

```bash
# Trouver le process
lsof -i :8080

# Ou utiliser un port différent
docker-compose -f docker-compose.yml -f docker-compose.override.yml up -d
```

### ❌ Database connection refused

```bash
# Vérifier que MySQL est prêt
docker-compose logs database

# Vérifier la connectivité
docker-compose exec api nc -zv database 3306
```

### ❌ Tests échouent dans Docker

```bash
# Vérifier les variables d'environnement
docker-compose exec api printenv | grep SPRING

# Vérifier la base de données
docker-compose exec database mysql -u root -p -e "SHOW DATABASES;"
```

---

## Performance

### Optimisations pour Production

```dockerfile
# Multi-stage build: réduit la taille de l'image
# Image finale: ~150-200MB (vs ~500MB avec tous les sources)
```

### Limites de Ressources

```yaml
# Dans docker-compose.yml
services:
  api:
    deploy:
      resources:
        limits:
          cpus: "1"
          memory: 512M
        reservations:
          cpus: "0.5"
          memory: 256M
```

---

## Docker Hub

### Publier l'Image

```bash
# Login
docker login

# Tag
docker tag ghassenebenslimene/store-api:latest ghassenebenslimene/store-api:v1.0.0

# Push
docker push ghassenebenslimene/store-api:latest
docker push ghassenebenslimene/store-api:v1.0.0
```

### Tirer une Image

```bash
docker pull ghassenebenslimene/store-api:latest
```

---

## Développement vs Production

### Dev Profile

```yaml
spring:
  jpa:
    show-sql: true
  datasource:
    url: jdbc:mysql://localhost:3306/store_api_dev
```

### Prod Profile

```yaml
spring:
  jpa:
    show-sql: false
  datasource:
    url: jdbc:mysql://database:3306/store_api
```

### Changer le Profil

```bash
# Dev
SPRING_PROFILES_ACTIVE=dev docker-compose up -d

# Prod (défaut)
SPRING_PROFILES_ACTIVE=prod docker-compose up -d
```

---

## Documents Connexes

- [README.md](README.md) - Configuration générale
- [.github/workflows/ci-cd.yml](.github/workflows/ci-cd.yml) - CI/CD Pipeline
- [Makefile](Makefile) - Commandes automatisées
