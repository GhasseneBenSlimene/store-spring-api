.PHONY: help build up down logs clean test lint docker-build docker-push

help:
	@echo "========================================"
	@echo "Store API - Development Commands"
	@echo "========================================"
	@echo ""
	@echo "Development:"
	@echo "  make dev              - Start in dev mode (with hot reload)"
	@echo "  make build            - Build the project"
	@echo "  make test             - Run all tests"
	@echo "  make coverage         - Generate coverage report"
	@echo "  make lint             - Run lint checks"
	@echo ""
	@echo "Docker:"
	@echo "  make docker-build     - Build Docker image"
	@echo "  make docker-up        - Start with docker-compose"
	@echo "  make docker-down      - Stop docker-compose"
	@echo "  make docker-logs      - View docker logs"
	@echo "  make docker-clean     - Remove containers and volumes"
	@echo ""
	@echo "Cleanup:"
	@echo "  make clean            - Clean build artifacts"
	@echo "  make clean-all        - Full cleanup (including Docker)"
	@echo ""

# Development
dev:
	./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

build:
	./mvnw clean package -DskipTests

test:
	./mvnw clean test

coverage:
	./mvnw test jacoco:report
	@echo "Coverage report: target/site/jacoco/index.html"

lint:
	./mvnw spotbugs:check checkstyle:check

# Docker
docker-build:
	docker build -t ghassenebenslimene/store-api:latest .

docker-up:
	docker-compose up -d
	@echo "✅ Application started!"
	@echo "API: http://localhost:8080"
	@echo "MySQL: localhost:3306"

docker-down:
	docker-compose down

docker-logs:
	docker-compose logs -f api

docker-clean:
	docker-compose down -v
	docker image rm ghassenebenslimene/store-api:latest 2>/dev/null || true

docker-restart:
	docker-compose restart

docker-rebuild:
	docker-compose down -v
	docker build -t ghassenebenslimene/store-api:latest .
	docker-compose up -d

# Cleanup
clean:
	./mvnw clean
	rm -rf target/

clean-all: docker-clean clean
	rm -rf .docker/
	find . -name "*.log" -delete

# Database
db-migrate:
	./mvnw flyway:migrate

db-info:
	./mvnw flyway:info
