# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests

# Copy source code and build jar
COPY src src
RUN mvn clean package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/app.jar app.jar

# API port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
