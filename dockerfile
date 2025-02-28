FROM gradle:8.12.1-jdk17 AS builder
WORKDIR /app

# Add build arguments for configurable build
ARG PROFILE=docker
ARG APP_VERSION=0.0.1-SNAPSHOT

# Set environment variables during build
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

COPY . .
RUN gradle build --no-daemon -Pversion=${APP_VERSION}

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Pass build arguments to runtime image
ARG PROFILE=default
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

COPY --from=builder /app/build/libs/ems-backend-${APP_VERSION}.jar app.jar
EXPOSE 8080

# Allow runtime environment variable overrides
ENTRYPOINT ["sh", "-c", "java -jar app.jar"]