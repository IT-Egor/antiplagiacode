FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build
COPY pom.xml .
COPY checkstyle.xml .
COPY main-service/ ./main-service

WORKDIR /build/main-service
RUN mvn -B dependency:go-offline -Dmaven.test.skip=true
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /build/main-service/target/*.jar ./app.jar
COPY --from=builder /build/main-service/src/main/resources/application-docker.yml ./application.yml

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]