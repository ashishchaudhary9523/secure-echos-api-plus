# =========================
# 1. Builder stage
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (cached in Docker layers)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline -B

# Copy source code and build the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# =========================
# 2. Runtime stage
# =========================
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/target/eslite-0.0.1-SNAPSHOT.jar .

# Expose default Spring Boot port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "/app/eslite-0.0.1-SNAPSHOT.jar"]