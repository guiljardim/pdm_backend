FROM gradle:7.6.1-jdk17 as builder

WORKDIR /app
COPY . .

RUN echo "Main-Class: org.example.ApplicationKt" > MANIFEST.MF
RUN gradle -q build -x test

RUN mkdir -p build/libs/dependencies
RUN cd build/libs/dependencies && jar -xf ../fdm-*.jar
RUN cd build/libs && jar -cvfm app.jar ../../MANIFEST.MF -C dependencies .

FROM openjdk:17-slim

WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar

EXPOSE 8080
ENV PORT=8080

CMD ["java", "-jar", "app.jar"]