FROM maven:3.8.6-amazoncorretto-17 AS build
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src/
RUN mvn package -DskipTests

#RUN stage
FROM openjdk:17-alpine
ARG JAR_FILE=/build/target/*.jar/
COPY --from=build build/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]