# Stage 1: Build application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY task-management/pom.xml .
COPY task-management/src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
