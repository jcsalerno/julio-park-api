# Etapa 1: Build
FROM ubuntu:latest as build

# Instalar dependências
RUN apt-get update && apt-get install -y openjdk-21-jdk maven

# Definir diretório de trabalho
WORKDIR /build

# Copiar o arquivo pom.xml
COPY pom.xml ./

# Copiar o restante dos arquivos do projeto
COPY . /build

# Garantir permissão de execução do script mvnw (caso queira usar)
RUN chmod +x mvnw

# Construir a aplicação
RUN mvn clean package -DskipTests

# Etapa 2: Produção
FROM openjdk:21-jdk-slim

# Copiar o JAR gerado para o contêiner de produção
COPY --from=build /build/target/api-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta
EXPOSE 8080

# Executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
