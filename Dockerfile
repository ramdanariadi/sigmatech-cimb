FROM maven:3.8.3-jdk-11-slim as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11
RUN groupadd grocerry && useradd grocerry -g grocerry
USER grocerry:grocerry
ARG JAR_FILE=/home/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]