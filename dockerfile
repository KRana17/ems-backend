# Stage 1: Builder
FROM gradle:8.12.1-jdk17 AS builder
WORKDIR /app

# Add build arguments for configurable build
ARG PROFILE=docker
ARG APP_VERSION=3.0

# Set environment variables during build
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

COPY . .
RUN gradle build --no-daemon -Pversion=${APP_VERSION}

# Debug: List the contents of the build directory
RUN ls -l /app/build/libs/

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Pass build arguments to runtime image
ARG APP_VERSION=3.0
ENV SPRING_PROFILES_ACTIVE=default

# Install bash, wget, and other necessary tools
RUN apk add --no-cache bash wget curl

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/ems-backend-${APP_VERSION}.jar app.jar

# Copy wait-for-it.sh script
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080

# Use wait-for-it.sh in the entrypoint with bash
ENTRYPOINT ["/bin/bash", "-c", "/wait-for-it.sh db:3306 -- java -jar app.jar"]
