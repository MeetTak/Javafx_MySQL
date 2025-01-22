## Stage 1: Build the application
#FROM maven:3.9.9-eclipse-temurin-21 AS build
#
#WORKDIR /app
#
## Copy the Maven wrapper and pom.xml
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
## Ensure the Maven wrapper script is executable
#RUN chmod +x mvnw
#
## Download dependencies
#RUN ./mvnw dependency:go-offline -B
#
## Copy the source code
#COPY src ./src
#
## Package the application
#RUN ./mvnw package -DskipTests -B && \
#    cp target/*.jar target/app.jar
#
## Stage 2: Create the final runtime image
#FROM eclipse-temurin:21-jre-jammy AS runtime
#
#WORKDIR /app
#
## Copy the JAR file from the build stage
#COPY --from=build /app/target/app.jar app.jar
#
## Expose the application port
#EXPOSE 8080
#
## Define the command to run the application
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:latest



WORKDIR /app



COPY pom.xml .



RUN mvn dependency:go-offline



COPY src ./src



RUN mvn package
