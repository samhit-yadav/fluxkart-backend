# Use an official Maven image to build the Spring Boot app
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK image to run the app
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/fluxkart-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "fluxkart-0.0.1-SNAPSHOT.jar"]
