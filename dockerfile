FROM gradle:7.6.1-jdk17 as builder
WORKDIR /app
COPY . .
RUN gradle shadowJar --no-daemon

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*-all.jar app.jar
EXPOSE 8080
ENV PORT=8080
CMD ["java", "-jar", "app.jar"]