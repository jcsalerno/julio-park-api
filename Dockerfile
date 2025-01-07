# Stage 1: Build the application with Java 21
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk maven -y

WORKDIR /build
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Check the target directory
RUN ls -l /build/target

# Stage 2: Run the application
FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /build/target/park-api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
