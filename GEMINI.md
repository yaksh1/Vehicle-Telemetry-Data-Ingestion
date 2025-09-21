# Project Overview

This project is a real-time vehicle telemetry platform. It consists of two main services: a `telemetry-producer` and a `telemetry-consumer`.

-   The `telemetry-producer` is a Spring Boot application that simulates vehicle telemetry data and sends it to a Kafka topic.
-   The `telemetry-consumer` is another Spring Boot application that consumes the data from the Kafka topic, processes it, and stores it in a Redis cache.

The entire system is containerized using Docker and orchestrated with Docker Compose.

## Building and Running

The project can be built and run using the following commands:

1.  **Build the applications:**

    Navigate to the `telemetry-producer` and `telemetry-consumer` directories and run:

    ```bash
    ./gradlew build -x test
    ```

2.  **Run the application:**

    From the root directory of the project, run:

    ```bash
    docker-compose up --build -d
    ```

## Redis Data Structure and CLI Commands

The telemetry data for each vehicle is stored in Redis as a Hash.

-   **Key:** `vehicle:<vehicleId>` (e.g., `vehicle:vehicle-1`)
-   **Data Structure:** Hash

**Example Hash Fields:**

-   `vehicleId`
-   `latitude`
-   `longitude`
-   `speed`
-   `fuelLevel`

**Redis CLI Commands:**

1.  **Connect to Redis:**

    ```bash
    docker-compose exec redis redis-cli
    ```

2.  **List all vehicle keys:**

    ```bash
    KEYS vehicle:*
    ```

3.  **Get all data for a specific vehicle:**

    ```bash
    HGETALL vehicle:<vehicleId>
    ```

    (e.g., `HGETALL vehicle:vehicle-1`)

## Development Conventions

-   The project uses Java 17 and Spring Boot 3.
-   Dependencies are managed with Gradle.
-   The code uses Lombok for boilerplate code reduction.
-   The project follows a microservices architecture, with services communicating through a Kafka message broker.
-   The entire infrastructure is containerized with Docker, and the development environment is defined in the `docker-compose.yml` file.