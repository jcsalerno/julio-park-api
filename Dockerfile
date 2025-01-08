# Etapa 1: Build
FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-21-jdk maven

WORKDIR /build
COPY . .

RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /build/target/api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
