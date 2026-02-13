# Docker Guide

## Prerequisites

- Docker Desktop running
- Docker Compose available (`docker compose`)

## Start

### Option A: without `.env` (demo values)

```bash
docker compose up -d --build
```

### Option B: with custom `.env`

```bash
cp .env.example .env
# edit .env
docker compose up -d --build
```

## Verify

```bash
docker compose ps
docker compose logs -f api
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs

## Stop

```bash
docker compose down
```

Reset database volume:

```bash
docker compose down -v
```

## Useful commands

```bash
# Rebuild API image
docker compose build api

# Follow DB logs
docker compose logs -f database

# Open shell in API container
docker compose exec api sh
```

## Troubleshooting

### Port already in use (8080 or 3306)

- Stop local service using the port, then re-run `docker compose up -d`.

### API cannot connect to DB

```bash
docker compose logs database
docker compose logs api
```

Wait until MySQL healthcheck is green, then restart API:

```bash
docker compose restart api
```
