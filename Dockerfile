FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-21-jdk maven

WORKDIR /build

COPY . .

RUN ls -l  # Para verificar se o mvnw foi copiado corretamente
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /build/target/park-api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
