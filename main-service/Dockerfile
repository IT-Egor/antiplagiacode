FROM eclipse-temurin:21-jre-jammy
VOLUME /tmp
ARG JAR_FILE=target/*.jar
ARG JAVA_PROPS=src/main/resources/application-docker.yml
COPY ${JAR_FILE} app.jar
COPY ${JAVA_PROPS} application.yml
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]