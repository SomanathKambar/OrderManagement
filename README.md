# Order Management System (OMS) Monorepo

This is the unified repository for the Order Management System, containing both backend services and frontend applications.

## 📂 Project Structure

| Directory | Description |
| :--- | :--- |
| `backend/` | **Spring Boot** Modular Monolith containing core business logic (Ordering, Delivery). |
| `apps/oms-angular/` | **Angular** frontend application (Admin/Operations Dashboard). |
| `apps/oms-react/` | **React** frontend application (Consumer/Analytics). |
| `cli/` | **Python** CLI tool for management and quick testing. |

## 🚀 Quick Start

### 1. Backend Service
To start the core API server:

```bash
cd backend
./mvnw spring-boot:run -pl order-mgmt-app
```
*See [backend/README.md](./backend/README.md) for detailed documentation.*

### 2. Frontend Applications

**Prerequisites:** Node.js (v18+)

Install dependencies:
```bash
npm install
```

Start **Angular** App:
```bash
npx nx serve oms-angular
```

Start **React** App:
```bash
npx nx serve oms-react
```

### 3. Docker Infrastructure
Start required databases (PostgreSQL, Redis) and tools:
```bash
docker compose up -d
```

## 🛠️ Development Tools

- **NX**: Used for managing the frontend workspace.
- **Maven**: Used for the Java backend.
- **Docker Compose**: Orchestrates local infrastructure.

## 🤝 Contribution
Please refer to the README in each specific module/app for contribution guidelines tailored to that technology.