## Use the official Maven image as the base image
#FROM maven:3.9.4-amazoncorretto-21 AS build
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the pom.xml and any other necessary files to the container
#COPY pom.xml .
#
## Download the project dependencies
#RUN mvn dependency:go-offline
#
## Copy the entire project to the container
#COPY . .
#
## Build the project
#RUN mvn package -DskipTests
#
## Use a smaller base image for the final stage
#FROM openjdk:21-jdk-slim
#
## Set the working directory inside the container
#WORKDIR /app
#
## Copy the built JAR file from the build stage
#COPY --from=build /app/target/db_app-1.0-SNAPSHOT.jar .
#
## Set the command to run your application
#CMD ["java", "-jar", "db_app-1.0-SNAPSHOT.jar"]

# Use the Eclipse Temurin JDK 21 as the base image
FROM eclipse-temurin:21-jdk-jammy

# Set the JavaFX version and download URL
ENV JAVAFX_VERSION=22
ENV JAVAFX_SDK_URL=https://gluonhq.com/download/javafx-${JAVAFX_VERSION}-mac

# Create a directory for the application
RUN mkdir /opt/app

# Download and extract JavaFX
RUN curl -L ${JAVAFX_SDK_URL} -o javafx-sdk.zip && \
    unzip javafx-sdk.zip && \
    rm javafx-sdk.zip && \
    mv javafx-sdk-${JAVAFX_VERSION} /opt/javafx

# Set the JavaFX library path
ENV PATH_TO_FX=/opt/javafx/lib

# Copy your application JAR into the container
COPY target/db_app-1.0-SNAPSHOT.jar /opt/app/db_app-1.0-SNAPSHOT.jar

# Expose the port your application runs on
#EXPOSE 8080

# Run the application with the JavaFX libraries
CMD ["java", "--module-path", "/opt/javafx/lib", "--add-modules", "javafx.controls", "-jar", "/opt/app/db_app-1.0-SNAPSHOT.jar"]