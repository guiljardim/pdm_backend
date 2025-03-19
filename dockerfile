# Etapa de construção
FROM gradle:7.6.1-jdk17 AS builder
WORKDIR /app
COPY . .

# Garante que o build falhe se houver erro
RUN gradle clean build -x test --no-daemon

# Renomeia para um nome fixo e copia diretamente para a próxima etapa
RUN cp build/libs/fdm-all.jar /app/app.jar

# Etapa final para rodar o app
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/app.jar app.jar

EXPOSE 8080
ENV PORT=8080

CMD ["java", "-jar", "app.jar"]
