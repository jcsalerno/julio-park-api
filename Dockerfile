FROM ubuntu:latest as build

# Instalar dependências
RUN apt-get update && apt-get install -y openjdk-21-jdk maven

# Definir diretório de trabalho
WORKDIR /build

# Copiar os arquivos do projeto
COPY . /build

# Garantir permissão de execução do script mvnw
RUN chmod +x mvnw

# Construir a aplicação
RUN ./mvnw clean package -DskipTests

# Copiar o arquivo JAR para o contêiner final
FROM openjdk:21-jdk-slim
COPY --from=build /build/target/api-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta
EXPOSE 8080

# Executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
