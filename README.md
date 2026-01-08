# Order Management System (OMS) Monorepo

This is a unified monorepo for a Swiggy-class Order Management and Logistics platform. It contains a high-scale modular monolith backend and multiple specialized frontend applications.

## 📂 Project Structure

| Directory | Type | Technology | Purpose |
| :--- | :--- | :--- | :--- |
| **`backend/`** | Backend | Java 17, Spring Boot | Core business logic, state machine, and API. |
| **`apps/oms-angular/`** | Frontend | Angular 18+, Signals | Admin & Operations dashboard for order management. |
| **`apps/oms-react/`** | Frontend | React 18, XState | Customer-facing analytics and order wizard. |
| **`cli/`** | Tool | Python 3 | Developer CLI for system health and manual triggers. |
| **`docker-compose.yml`** | Infra | Docker | Local infrastructure (Postgres, Redis, Kafka, MailHog). |

---

## 🚀 Quick Start

### 1. Prerequisites
- **Java 17+** & **Maven**
- **Node.js v18+** & **NPM**
- **Docker** & **Docker Compose**
- **Python 3** (for CLI)

### 2. Infrastructure Setup
Spin up the required databases and message brokers:
```bash
docker compose up -d
```

### 3. Backend Execution
```bash
cd backend
./mvnw spring-boot:run -pl order-mgmt-app
```
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Actuator Health**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

### 4. Frontend Execution
Install dependencies first (from root):
```bash
npm install
```

Run **Angular Dashboard**:
```bash
npx nx serve oms-angular
```

Run **React Client**:
```bash
npx nx serve oms-react
```

---

## 🧪 Testing & Quality

### Backend Tests
Run unit and integration tests (uses H2/TestContainers):
```bash
cd backend
./mvnw test
```

### Frontend Tests
Run Vitest suites for the applications:
```bash
npx nx test oms-angular
npx nx test oms-react
```

### Linting
```bash
npx nx lint oms-angular
npx nx lint oms-react
```

---

## 🛠️ Debugging Guide

### Backend Debugging
- **IDE**: Open the `backend/` folder in IntelliJ IDEA or VS Code.
- **Remote Debug**: Run with `-Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"`.
- **Logs**: Check `backend/target/logs` (if configured) or console output.

### Frontend Debugging
- Use **Chrome DevTools** (F12).
- For Angular: Use **Angular DevTools** extension.
- For React: Use **React Developer Tools** and **XState Viz**.

---

## 📦 Publishing & Deployment

### Docker Images
Build the backend production image:
```bash
cd backend
docker build -t oms-backend:latest .
```

### Frontend Production Build
```bash
npx nx build oms-angular --prod
npx nx build oms-react --prod
```
The artifacts will be generated in the `dist/` directory.

---
*Maintained by the Platform Engineering Team.*
