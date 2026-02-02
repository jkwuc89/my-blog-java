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

# Create non-root user for security
RUN groupadd --system --gid 1000 spring && \
    useradd spring --uid 1000 --gid 1000 --create-home --shell /bin/bash

USER 1000:1000

# Copy the built JAR from build stage
COPY --chown=spring:spring --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
