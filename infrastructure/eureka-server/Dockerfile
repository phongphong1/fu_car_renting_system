FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

ARG SERVICE_PATH

WORKDIR /build

COPY pom.xml .

COPY ${SERVICE_PATH}/pom.xml level1/level2/
COPY ${SERVICE_PATH}/src level1/level2/src

WORKDIR /build/level1/level2

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /build/level1/level2/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]