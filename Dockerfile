# syntax=docker/dockerfile:1
# Multi-stage Dockerfile for Spring Boot application

# Build stage
FROM gradle:9.3-jdk25 AS build

WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies (cached layer)
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application (skip tests for faster build, run them in CI)
RUN gradle build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

# Install curl for healthchecks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN groupadd --system --gid 1000 spring && \
    useradd spring --uid 1000 --gid 1000 --create-home --shell /bin/bash

USER 1000:1000

# Copy the built JAR from build stage
COPY --chown=spring:spring --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Healthcheck with start-period matching app startup time
# start-period: 35s matches observed startup time (~33-35s)
# interval: 3s checks every 3 seconds to detect healthy state quickly
# timeout: 3s for each healthcheck request
# retries: 3 consecutive failures mark container unhealthy
HEALTHCHECK --start-period=35s --interval=3s --timeout=3s --retries=3 \
  CMD /usr/bin/curl -f http://localhost:8080/java/up || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
