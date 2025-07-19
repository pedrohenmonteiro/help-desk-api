FROM openjdk:21-jdk
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# docker container rm $(dock ps -a -q) -> Remove todos os containers stopados