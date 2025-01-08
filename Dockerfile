# Etapa 1: Build
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y

WORKDIR /build
COPY . .

RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim

# Copiar o JAR gerado para o contêiner de produção
COPY --from=build /build/target/api-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta
EXPOSE 8080

# Executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
