# Local Development

## Prerequisites

- Java 17+
- Git
- Docker Desktop (optional, recommended)

## Clone

```bash
git clone https://github.com/GhasseneBenSlimene/store-api.git
cd spring-api-finished
```

## Run with Docker (recommended)

```bash
docker compose up -d --build
```

## Run without Docker

Start your MySQL instance, then provide DB/JWT/Stripe environment variables.

```bash
./mvnw clean install
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

## Tests

```bash
./mvnw test
```

PowerShell:

```powershell
.\mvnw.cmd test
```

## URLs

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health
