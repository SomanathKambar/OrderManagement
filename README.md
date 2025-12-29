# Order Management System (OMS)

A Modular Monolith application for managing orders and delivery partners, built with Spring Boot 3.4+ and Java 17/21.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
  - [Orders](#orders-api)
  - [Delivery Partners](#delivery-partners-api)
- [Configuration](#configuration)

## Features

-   **Order Creation**: Create new orders with details like customer information, order items, and delivery address.
-   **Order Status Management**: Update the status of orders (e.g., PENDING, PROCESSING, DELIVERED, CANCELLED).
-   **Delivery Partner Management**: Register and manage delivery partners, including their availability status.
-   **Order Assignment**: Assign orders to available delivery partners using pluggable strategies (e.g., City-Based, Nearest-First).
-   **Domain-Driven Design**: Clear separation between domain models, entities, DTOs, and services.
-   **RESTful API**: Exposes a comprehensive set of REST endpoints for all core functionalities.
-   **In-memory Database**: Uses H2 database for easy setup and development.

## Tech Stack

-   **Java**: Version 17
-   **Spring Boot**: Version 3.2.0
-   **Maven**: Build automation tool
-   **Spring Data JPA**: For data persistence and repository abstraction.
-   **Hibernate**: JPA implementation.
-   **H2 Database**: In-memory database for development and testing.
-   **Lombok**: To reduce boilerplate code (e.g., getters, setters, constructors).
-   **JUnit 5 & Mockito**: For unit and integration testing.

## Architecture

This system follows a **Modular Monolith** architecture, separating concerns by domain (`ordering`, `delivery`, `identity`) while keeping deployment simple.

- **Modules**:
  - `modules.ordering`: Handles order lifecycle (Create, Update, Cancel).
  - `modules.delivery`: Manages delivery partners and assignments.
  - `common`: Shared domain objects and exceptions.
- **Data Isolation**: Each module uses its own database schema (logically).
- **Communication**: Sync for queries, Async events for side effects (planned).

## Prerequisites

- Java 17 or 21
- Docker (optional, for Postgres/Redis)
- Maven (wrapper included)

## Getting Started

1. **Start Infrastructure (Optional)**
   If you have Docker, start the database and tools:
   ```bash
   docker compose up -d
   ```
   *If you don't have Docker, the application will fallback to H2 (In-Memory Database) automatically.*

2. **Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the Application**
   - API Base URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Documentation

### Orders API

**Base URL**: `/api/v1/orders`

#### 1. Create a New Order
*   **Method**: `POST /api/v1/orders`
*   **Body**:
    ```json
    {
      "customerId": "cust_123",
      "customerName": "John Doe",
      "restaurantId": "rest_456",
      "restaurantName": "Burger King",
      "orderType": "FOOD",
      "deliveryAddress": {
        "streetAddress": "123 Main St",
        "city": "New York",
        "state": "NY",
        "postalCode": "10001",
        "country": "USA"
      },
      "restaurantAddress": {
        "streetAddress": "456 Market St",
        "city": "New York",
        "state": "NY",
        "postalCode": "10002",
        "country": "USA"
      },
      "items": [
        {
          "itemId": "item_1",
          "itemName": "Burger",
          "quantity": 2,
          "unitPrice": 5.99
        }
      ],
      "specialInstructions": "Ring doorbell"
    }
    ```
*   **Response**: `201 Created` with `OrderResponse`

#### 2. Get Order by ID
*   **Method**: `GET /api/v1/orders/{id}`
*   **Response**: `200 OK`

#### 3. Search Orders
*   **Method**: `GET /api/v1/orders`
*   **Query Params**:
    *   `customerId` (optional)
    *   `restaurantId` (optional)
    *   `status` (optional)
    *   `city` (optional)
    *   `page` (default 0)
    *   `size` (default 20)

#### 4. Update Order Status
*   **Method**: `PUT /api/v1/orders/{id}/status`
*   **Body**:
    ```json
    {
      "status": "PREPARING"
    }
    ```

#### 5. Cancel Order
*   **Method**: `DELETE /api/v1/orders/{id}?reason=MindChanged`

---

### Delivery Partners API

**Base URL**: `/api/v1/delivery-partners`

#### 1. Create Delivery Partner
*   **Method**: `POST /api/v1/delivery-partners`
*   **Body**:
    ```json
    {
      "name": "Jane Smith",
      "phoneNumber": "+1234567890",
      "email": "jane@example.com",
      "currentLocation": {
        "city": "New York",
        "country": "USA"
      },
      "vehicleType": "BIKE",
      "vehicleNumber": "NY-1234"
    }
    ```

#### 2. Get All Partners
*   **Method**: `GET /api/v1/delivery-partners`

#### 3. Get Partner by ID
*   **Method**: `GET /api/v1/delivery-partners/{id}`

#### 4. Find Available Partners
*   **Method**: `GET /api/v1/delivery-partners/available/{city}`
*   **Query Params**: `strategy` (default `CITY_BASED`)

#### 5. Assign Partner to Order
*   **Method**: `PUT /api/v1/delivery-partners/{partnerId}/assign/{orderId}`

#### 6. Update Partner Status
*   **Method**: `PUT /api/v1/delivery-partners/{id}/status/{status}`
*   **Example**: `/api/v1/delivery-partners/1/status/BUSY`

#### 7. Get Partners by Status
*   **Method**: `GET /api/v1/delivery-partners/status/{status}`

## Configuration

The application uses `application.yml` for configuration.
- **Database**: Postgres (Prod) or H2 (Dev fallback).
- **Redis**: Disabled by default in dev if not found.