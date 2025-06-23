FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build
COPY ../ .

WORKDIR /build/main-service
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /build/main-service/target/*.jar ./app.jar
COPY --from=builder /build/main-service/src/main/resources/application-docker.yml ./application.yml

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]