# Docker Guide

## Ports hôte (par défaut)

- API: `18080` -> conteneur `8080`
- MySQL: `13306` -> conteneur `3306`

Ces ports évitent les conflits fréquents avec des services déjà installés localement.

## Démarrer

### Option A — sans `.env`

```bash
docker compose up -d --build
```

### Option B — avec `.env`

```bash
cp .env.example .env
# modifie les variables si besoin
docker compose up -d --build
```

## Vérifier

```bash
docker compose ps
docker compose logs -f api
```

- API: http://localhost:18080
- Swagger UI: http://localhost:18080/swagger-ui.html
- OpenAPI: http://localhost:18080/v3/api-docs
- MySQL host: `localhost:13306`

## Arrêter

```bash
docker compose down
```

Reset DB (optionnel):

```bash
docker compose down -v
```
