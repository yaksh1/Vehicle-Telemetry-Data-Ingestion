# Stage 1: Build the application using a Gradle image that includes JDK 17
# Using the standard Debian-based image instead of Alpine to avoid potential SSL/network issues.
FROM gradle:8.5-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files first. This allows us to use ./gradlew
COPY gradlew .
COPY gradle ./gradle

# Make the Gradle wrapper executable. This is a common requirement in Linux-based containers.
RUN chmod +x ./gradlew

# Copy the build configuration files. This is a Docker optimization.
# If these files don't change, Docker will use the cached dependency layer.
COPY build.gradle .
COPY settings.gradle .

# Download all the project dependencies using the wrapper
# Using --no-daemon is a good practice for CI/CD environments and Docker builds
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the source code
COPY src ./src

# Build the application and create the executable jar, skipping tests.
RUN ./gradlew build --no-daemon -x test


# Stage 2: Create the final, lightweight production image
# We use a slim OpenJDK image which is much smaller than the Gradle image.
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the executable JAR from the 'build' stage's build/libs directory
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8081

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

