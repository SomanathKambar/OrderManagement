# PLATFORM VISION & ENGINEERING PROMPT
## Order & Logistics Platform (Swiggy-Class Consumer Backend)

### Mission
Upgrade the existing Kotlin Spring Boot Order Management service into a production-grade consumer commerce platform capable of handling long-lived distributed order workflows, real-world failures, retries, compensation, and logistics orchestration — similar to Swiggy, Zomato, Uber Eats class systems.

This system must behave as a reliable financial transaction platform, not just a CRUD API.

### Core Engineering Principles
- **Orders are workflows, not rows in a table**
- **All operations must be retry-safe and idempotent**
- **Every state change must be validated, persisted, observable and auditable**
- **Failure must lead to deterministic recovery or compensation**
- **No API may cause silent corruption of order state**

### Order Lifecycle (Source of Truth)
#### Order States
- `INITIATED`
- `PENDING_PAYMENT`
- `PAID`
- `CONFIRMED`
- `PREPARING`
- `READY_FOR_PICKUP`
- `PICKED_UP`
- `IN_TRANSIT`
- `DELIVERED`
- `CANCELLED`
- `FAILED`
- `REFUNDED`

#### Legal Transitions
- `INITIATED` → `PENDING_PAYMENT`
- `PENDING_PAYMENT` → `PAID` | `FAILED` | `CANCELLED`
- `PAID` → `CONFIRMED` | `CANCELLED`
- `CONFIRMED` → `PREPARING` | `CANCELLED`
- `PREPARING` → `READY_FOR_PICKUP`
- `READY_FOR_PICKUP` → `PICKED_UP`
- `PICKED_UP` → `IN_TRANSIT`
- `IN_TRANSIT` → `DELIVERED` | `FAILED`
- `FAILED` → `REFUNDED`
- `CANCELLED` → `REFUNDED`

**No API may bypass this transition graph.**

### API Governance Rules
All mutating APIs must:
- Require `Idempotency-Key`
- Validate business rules
- Use transactional persistence
- Emit domain events
- Produce structured logs and metrics

Illegal transitions must return `409 Conflict`.

### Domain Events (Mandatory)
Every valid state change must emit a domain event:
- `OrderInitiatedEvent`
- `PaymentPendingEvent`
- `PaymentCompletedEvent`
- `OrderConfirmedEvent`
- `OrderPreparingEvent`
- `OrderReadyEvent`
- `OrderPickedUpEvent`
- `OrderInTransitEvent`
- `OrderDeliveredEvent`
- `OrderFailedEvent`
- `OrderCancelledEvent`
- `OrderRefundedEvent`

These events are the contract for future Kafka / event-stream pipelines.

### Compensation & Financial Safety
Refunds are mandatory compensation flows.

Refund is allowed only when:
`state == FAILED` or `CANCELLED`

Refund must be a separate transactional workflow.

### Observability & Reliability
This platform must expose:
- **Health**: `/health`
- **Metrics**: `/metrics` (Prometheus)
- **Logs**: Structured logs with `orderId`
- **Errors**: Global error catalog

### Data Governance
- Versioned DB migrations
- Environment profiles (dev/prod)
- Strict DTO / Domain / Entity separation
- Testable service layer abstractions

### Delivery Partner Platform
Partner assignment is a first-class workflow:
- Only allowed in `READY_FOR_PICKUP`
- Emits pickup & transit events
- Must be idempotent and transactional

### Containerization & CI/CD Readiness
- Dockerfile mandatory
- Health checks mandatory
- Ready for Kubernetes deployment

### Engineering Success Criteria
This platform must demonstrate:
- Deterministic order workflows
- Retry-safe APIs
- Fault-tolerant pipelines
- Compensation flows
- Event-driven architecture readiness
- Production observability

### Final Directive
**Treat this service as if it were managing real money, real riders, and real food deliveries.**
Reliability and correctness are non-negotiable.
