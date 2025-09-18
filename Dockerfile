
FROM openjdk:17 as builder

WORKDIR /app

COPY . .

WORKDIR /app/web/Finance

RUN ./mvnw clean install -DskipTests


FROM openjdk:17-slim
WORKDIR /app

COPY --from=builder /app/web/Finance/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]