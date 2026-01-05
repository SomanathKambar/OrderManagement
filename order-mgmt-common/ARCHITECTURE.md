# Common Module (Shared Kernel) Architecture

## Vibe
The **Common Module** provides the glue that binds the modular monolith together. it contains shared value objects, domain events, and cross-cutting concerns like security and idempotency filters.

## Core Responsibilities
- **Domain Events**: Defines the hierarchy of `OrderEvent` types used for inter-module communication.
- **Shared Objects**: Reusable domain objects like `Address` and `IdempotencyRecord`.
- **Global Exceptions**: Standardized error handling and `ErrorResponse` formats.
- **Infrastructure Infrastructure**: Filters (e.g., `IdempotencyFilter`) that apply to all incoming API requests.

## Key Boundaries
- Must remain "Thin". Logic here should be strictly shared and not contain module-specific business rules.
