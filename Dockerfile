# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-slim
WORKDIR /app

# Install X11 and required dependencies for Swing
RUN apt-get update && apt-get install -y \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxext6 \
    libxft2 \
    x11-apps \
    && rm -rf /var/lib/apt/lists/*

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Environment variables
ENV DISPLAY=:0
ENV SPRING_PROFILES_ACTIVE=docker

# Expose port
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]