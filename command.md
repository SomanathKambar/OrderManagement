# Order Management Project Commands

This document contains all necessary commands to manage the Order Management project (Angular, React, and Spring Boot).

## 1. Prerequisites
Ensure you have the following installed:
- **Java 17+**
- **Node.js (LTS)**
- **Docker & Docker Compose**

## 2. Infrastructure (Docker)
The project uses Docker for dependencies like PostgreSQL, Redis, Kafka, Zookeeper, and MailHog.

### Start Infrastructure
```bash
docker-compose up -d
```
*   `-d` runs the containers in detached mode (background).

### Stop Infrastructure
```bash
docker-compose down
```

### View Logs
```bash
docker-compose logs -f
```

## 3. Backend (Spring Boot)
The backend is a multi-module Maven project. The main application is in `order-mgmt-app`.

### Build the Project
```bash
./mvnw clean install
```
*   Skips tests (optional): `./mvnw clean install -DskipTests`

### Run the Application
```bash
./mvnw spring-boot:run -pl order-mgmt-app
```
*   This runs the Spring Boot application from the `order-mgmt-app` module.
*   **Pause/Stop:** Press `Ctrl + C` in the terminal.
*   **Restart:** Run the command again.

### Kafka Configuration (Manual Trigger)
Currently, the Kafka consumer is set to **manual startup** (`autoStartup = "false"`) to prevent connection errors when Kafka is not running.

**To Enable Kafka Consumer:**
1.  Open `order-mgmt-app/src/main/java/com/example/ordermanagement/infrastructure/kafka/OrderEventKafkaConsumer.java`
2.  Change `@KafkaListener(..., autoStartup = "false")` to `@KafkaListener(...)` (remove the `autoStartup` parameter or set to `"true"`).
3.  Restart the backend application.

## 4. Frontend (Nx Workspace)
The frontend applications are managed by Nx.

### Install Dependencies
```bash
npm install
```

### Run Angular App (oms-angular)
```bash
npx nx serve oms-angular
```
*   **Access:** Usually at `http://localhost:4200`
*   **Stop:** Press `Ctrl + C`.

### Run React App (oms-react)
```bash
npx nx serve oms-react
```
*   **Access:** Usually at `http://localhost:4200` (or another port if Angular is running, check console output).
*   **Stop:** Press `Ctrl + C`.

## 5. Lifecycle Management

| Action | Command / Instruction |
| :--- | :--- |
| **Start All** | 1. `docker-compose up -d`<br>2. `./mvnw spring-boot:run -pl order-mgmt-app`<br>3. `npx nx serve oms-angular` (in new terminal)<br>4. `npx nx serve oms-react` (in new terminal) |
| **Track Logs** | **Backend:** Terminal where you ran `mvnw`<br>**Docker:** `docker-compose logs -f`<br>**Frontend:** Terminal where you ran `nx serve` |
| **Pause** | **Services:** `docker-compose pause`<br>**Apps:** No native "pause" for dev servers, use Stop. |
| **Resume** | **Services:** `docker-compose unpause`<br>**Apps:** Run the start command again. |
| **Stop (Finish)** | 1. `Ctrl + C` in all app terminals.<br>2. `docker-compose down` |
| **Restart** | Stop (Ctrl + C) and run the start command again. |

## 6. Troubleshooting

- **Kafka Connection Refused:**
    - Ensure Docker containers are running: `docker ps`
    - If you don't need Kafka, keep `autoStartup = "false"` in `OrderEventKafkaConsumer.java`.
- **Port Conflicts:**
    - Check if ports 8080 (Boot), 4200 (Frontend), 5432 (Postgres), 6379 (Redis) are free.
