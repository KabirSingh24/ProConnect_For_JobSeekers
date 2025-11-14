# Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper + settings first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (caches layers)
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Set port for container
ENV PORT=8080
EXPOSE 8080

# Run the built jar
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
