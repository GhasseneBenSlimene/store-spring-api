# Stage 1: Build avec Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml et télécharge les dépendances (couche réutilisable)
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests

# Copy le code source et compile
COPY src src
RUN mvn clean package -DskipTests -q

# Stage 2: Runtime (image finale minimale)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR du stage 1
COPY --from=builder /app/target/store-*.jar app.jar

# Infos de l'image
LABEL author="GhasseneBenSlimene"
LABEL description="E-commerce API - Spring Boot 3.4.1"
LABEL version="1.0.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Expose le port
EXPOSE 8080

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run l'app
ENTRYPOINT ["java", "-jar", "app.jar"]
