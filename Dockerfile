# Use an official Maven image as the base image for the build stage
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project to the working directory
COPY . .

# Build the Maven project
RUN mvn package

# Use an official OpenJDK image as the base image for the runtime
FROM eclipse-temurin:21-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]