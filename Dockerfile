# Use the Eclipse Temurin 21 (Java 21) base image
FROM eclipse-temurin:21-jdk

# Install Maven and OpenJFX libraries (if not already included)
RUN apt-get update && apt-get install -y \
    maven \
    openjfx \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy your project files into the container
COPY . /app

# Build the application (using Maven)
RUN mvn -f /app/pom.xml clean package -DskipTests

# Run the built JAR file (replace MyApp.jar with the actual final artifact name)
CMD ["java", "-jar", "/app/target/db_app-1.0-SNAPSHOT.jar"]