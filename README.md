# Real-Time Vehicle Telemetry Platform 🏎️

This project demonstrates a real-time data ingestion and processing pipeline designed to handle a high-velocity stream of vehicle telemetry data. It simulates a fleet of vehicles sending data every second, which is then ingested, processed, and stored for real-time analysis and dashboarding.

This project is an excellent showcase of modern data engineering principles, using a microservices architecture, asynchronous messaging, in-memory caching, and containerization.

## Architecture Overview

The system follows a classic producer-consumer pattern, decoupled by a message broker.

### Telemetry Producer
- A Spring Boot application that simulates multiple vehicles.
- Generates mock telemetry data (vehicle ID, location, speed, fuel level) and sends it to a Kafka topic at a high frequency.

### Kafka
- Acts as the central nervous system of our pipeline.
- Ingests the high-throughput data stream from the producer, providing durability and back-pressure capabilities.

### Telemetry Consumer
- Another Spring Boot application that subscribes to the Kafka topic.
- Consumes the telemetry messages, processes them, and stores the latest status for each vehicle in a Redis cache.

### Redis
- An in-memory data store used to maintain the real-time state of each vehicle.
- Ideal for powering a live dashboard that needs up-to-the-second vehicle information without querying a slower, disk-based database.

### Docker & Docker Compose
- The entire infrastructure (Producer, Consumer, Kafka, Zookeeper, Redis) is containerized.
- Allows for a consistent, isolated, and easily reproducible development environment.

## Technologies Used

- **Backend:** Java 17, Spring Boot 3
- **Messaging:** Apache Kafka
- **Cache/In-Memory DB:** Redis
- **Containerization:** Docker, Docker Compose
- **Build Tool:** Gradle

## Getting Started

### Prerequisites

- Java 17 or later
- Gradle
- Docker and Docker Compose

### Running the Application

1. Clone the repository:

```bash
git clone <repository-url>
cd real-time-data-pipeline
```

2. Build the Spring Boot applications:

Navigate to the `telemetry-producer` and `telemetry-consumer` directories and run:

```bash
gradle build -x test
```

This will create the executable JAR files.

3. Launch the entire stack using Docker Compose:

From the root directory of the project, run:

```bash
docker-compose up --build -d
```

This command will:
- Build the Docker images for the producer and consumer services.
- Start containers for Kafka, Zookeeper, Redis, and our two applications.

4. Verify the pipeline is working:

- Check the producer logs to see data being sent:

```bash
docker-compose logs -f producer
```

- Check the consumer logs to see data being received:

```bash
docker-compose logs -f consumer
```

- Connect to Redis to see the stored data:

```bash
docker-compose exec redis redis-cli
```

Inside `redis-cli`, check for vehicle data:

```bash
KEYS vehicle:*
GET vehicle:1
```

5. Shut down the application:

```bash
docker-compose down
```

