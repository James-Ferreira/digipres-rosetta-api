FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests -e

FROM openjdk:21
WORKDIR /app

COPY --from=build /app/target/rosetta-api-1.0-SNAPSHOT.jar .
COPY --from=build /app/target/libs ./libs

CMD ["java", "-cp", "rosetta-api-1.0-SNAPSHOT.jar:libs/*", "com/api/Launcher"]
