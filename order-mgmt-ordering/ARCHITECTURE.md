# Ordering Module Architecture

## Vibe
The **Ordering Module** is the "Source of Truth" for the lifecycle of any order. It manages state transitions, financial calculations, and emits domain events that drive the rest of the system.

## Core Responsibilities
- **State Governance**: Enforces a strict DAG (Directed Acyclic Graph) for order states (e.g., INITIATED -> PAID -> CONFIRMED).
- **Financial Integrity**: Calculates totals, taxes, and handles refund logic.
- **Idempotency**: Ensures that retried requests (with the same `Idempotency-Key`) do not result in duplicate orders or transitions.
- **Event Sourcing (Outbox)**: Persists `OrderEvent` records in the same transaction as state changes to ensure "At-Least-Once" delivery.

## Key Boundaries
- Does NOT handle delivery logistics (only tracks `deliveryPartnerId`).
- Does NOT handle identity/authentication (receives `customerId`).
- Communicates with other modules primarily via the Shared Kernel (`common`).
