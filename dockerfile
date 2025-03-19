FROM gradle:7.6.1-jdk17

WORKDIR /app
COPY . .

RUN gradle build -x test

RUN find build/classes/kotlin/main -type f | sort

EXPOSE 8080
ENV PORT=8080

CMD ["sh", "-c", "java -cp build/classes/kotlin/main ApplicationKt"]