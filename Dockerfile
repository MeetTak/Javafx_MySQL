# Use the official Maven image as the base image
FROM maven:3.9.4-amazoncorretto-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and any other necessary files to the container
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Copy the entire project to the container
COPY . .

# Build the project
RUN mvn package -DskipTests

# Use a smaller base image for the final stage
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/db_app-1.0-SNAPSHOT.jar .

# Set the command to run your application
CMD ["java", "-jar", "db_app-1.0-SNAPSHOT.jar"]