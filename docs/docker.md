# Docker Guide

## Host ports (default)

- API: `18080` -> container `8080`
- MySQL: `13306` -> container `3306`

These ports avoid common conflicts with services already running locally.

## Start

### Option A — without `.env`

```bash
docker compose up -d --build
```

### Option B — with `.env`

```bash
cp .env.example .env
# edit variables if needed
docker compose up -d --build
```

## Verify

```bash
docker compose ps
docker compose logs -f api
```

- API: http://localhost:18080
- Swagger UI: http://localhost:18080/swagger-ui.html
- OpenAPI: http://localhost:18080/v3/api-docs
- MySQL host: `localhost:13306`

## Stop

```bash
docker compose down
```

Reset DB (optional):

```bash
docker compose down -v
```
