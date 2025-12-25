# Order Management Service

A backend service being built to simulate a real-world food & grocery delivery order lifecycle similar to large-scale logistics platforms.

---

## Project Vision

Modern delivery platforms handle millions of orders across hundreds of cities.  
This service is designed to simulate such systems by implementing:

- Order lifecycle management
- City-based order routing
- Delivery partner assignment
- State validation & reliability mechanisms
- Scalable and maintainable REST APIs
---



### Dependencies
Component                    | Why it exists 
Spring Boot Web              | Provides REST API foundation 
Spring Data JPA              | Enables ORM-based persistence layer  
H2 Database                  | Lightweight in-memory database for local development 
Lombok                       | Reduces boilerplate for cleaner domain code 
application.yml              | Central configuration control

spring-boot-starter-web      | Provides REST capabilities and embedded Tomcat
spring-boot-starter-data-jpa | Enables JPA repositories and ORM
h2                           | Lightweight in-memory DB for development
lombok                       | Generates getters, setters, constructors, builders

---

## How to Run

```bash
./mvnw spring-boot:run

