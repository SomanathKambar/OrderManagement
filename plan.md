Title: OrderManagement Execution Plan (AI-iterable)

Purpose
- Single source of truth for implementation plan, status, and prompts.
- Any AI can resume from this file, picking only unfinished items.
- Enforces minimal-token interaction via short, structured prompts.

How To Use
1) Work phase-by-phase in order. Do not skip.
2) For each task:
   - Use the provided PROMPT template ID (T0..T7).
   - Paste only the minimal code snippet(s) needed.
   - Update the task’s Status to DONE with a concise Completion Log.
3) Never modify DONE items. Only edit TODO items.
4) Keep logs short (<= 4 lines per task). No extraneous prose.

Legend
- Status: TODO | IN_PROGRESS | DONE
- Priority: P0 (critical), P1 (high), P2 (normal)

Prompt Templates (Low-token)
- T0 REVIEW
  Role: Senior architect. Task: Review the provided snippet/file.
  Constraints: <= 10 bullets; each <= 15 words; no prose; code only if required.
  Input: [paste minimal code or description]
  Output: Bullet-only findings or steps.

- T1 CHANGE_FILE (diff-only)
  Role: Senior engineer. Task: Edit file at [path] to [intent].
  Constraints: Output unified diff only. No explanations. Keep patch minimal. Max 200 lines.
  Input: Current file content between --- markers.
  ---
  [file or section]
  ---
  Output: diff starting with --- and +++ headers.

- T2 CREATE_FILE
  Role: Senior engineer. Task: Create file [path] to [intent].
  Constraints: Provide complete file content only. Max 300 lines. No explanations.
  Output: Full file content.

- T3 MIGRATION_SQL (Flyway)
  Role: DBA. Task: Generate Flyway migration V[seq]__[desc].sql for [intent].
  Constraints: Include guards and indexes. Max 150 lines. No prose.
  Output: SQL only.

- T4 TEST_FILE
  Role: Test engineer. Task: Add tests for [class/method] covering [cases].
  Constraints: JUnit5, minimal mocks, deterministic. Max 200 lines. No prose.
  Input: [paste class/method]
  Output: Test class only.

- T5 OPENAPI_EDIT
  Role: API architect. Task: Update OpenAPI to [intent].
  Constraints: Output diff or full file. No prose. Max 200 lines.
  Input: Current file between --- markers.
  Output: Diff or full file.

- T6 POLICY_DOC
  Role: Tech writer. Task: Write concise section for [topic].
  Constraints: <= 200 words, bullets preferred. No fluff. Output text only.

- T7 K8S_MANIFEST
  Role: Platform engineer. Task: Provide Kubernetes YAML for [intent].
  Constraints: Minimal viable manifests. One file. Max 200 lines. No prose.
  Output: YAML only.

Global Acceptance Gates
- Build passes: mvn -q -DskipTests=false test
- Tests cover domain transitions, mappers, and integration happy-paths
- Actuator health and metrics reachable in dev profile
- OpenAPI served and valid

Commit and Verification Workflow (always follow)
1) Verify build locally: mvn -q -DskipTests=false test
2) If build succeeds, stage changes: git add -A
3) Create production-grade commit message:
   feat(api): standardize error envelope with requestId and reason codes
   - Include JIRA/issue if applicable
   - Mention modules/files touched and rationale
4) Commit: git commit -m "<message>"
5) Optional: run application locally to sanity check endpoints
6) Push when all phase tasks pass acceptance gates

PHASES AND TASKS

Phase 0: Baseline and Inventory (P0)
Objective: establish current state, risks, priorities.
Tasks:
0.1 Module inventory and build config review
- Files: pom.xml (root and modules)
- Prompt: T0 REVIEW
- Status: DONE
- Completion Log:
- Modules: order-mgmt-common, order-mgmt-ordering, order-mgmt-delivery, order-mgmt-app.
- Java 17; Spring Boot 3.4.0; Spring Cloud 2023.0.0; MapStruct 1.5.5; Lombok 1.18.30.
- Aggregator POM packaging; dependencyManagement includes mapstruct, springdoc-openapi, jjwt libs.
- Compiler plugin config for MapStruct/Lombok processors; annotation processing configured.

0.2 Runtime config secrets and profiles review
- Files: order-mgmt-app/src/main/resources/application-*.yml
- Prompt: T0 REVIEW
- Status: DONE
- Completion Log:
- Profiles: base with dev active; dev uses H2, prod uses Postgres/Redis.
- Secrets via env placeholders (DB_*, REDIS_*); no raw secrets committed.
- Actuator exposed: health, metrics, info, prometheus; springdoc paths configured.

0.3 DB schema and migration coverage review
- Files: V1__init_schema.sql and entities
- Prompt: T0 REVIEW
- Status: DONE
- Completion Log:
- Tables: orders, order_items, delivery_partners, idempotency_records.
- FKs: order_items.order_id -> orders.id; others missing.
- Missing: outbox table, indexes, unique constraints on business keys.
- Timestamps present; no retention; no event/versioning columns.

0.4 Top 10 risks list
- Prompt: T0 REVIEW (description input)
- Status: DONE
- Completion Log:
- No outbox table; event delivery not guaranteed.
- Kafka DLQ/retry/backoff not configured.
- Public API lacks /api/v1 versioning.
- Incomplete security: JWT/RBAC config not enforced.
- Observability gaps: correlationId, tracing missing.
- Idempotency records lack TTL/cleanup.
- No DB indexes on hot columns; potential scans.
- Missing event schema version and eventId for idempotency.
- Limited tests for aggregates, Kafka, migrations.
- Active profile defaults to dev; prod hardening needed.

Phase 1: Public API Readiness (P0)
Objective: consistent /api/v1, error envelope, idempotency, pagination.
Tasks:
1.0 Verify build and commit (Phase 1 changes)
- Files: repo-wide
- Prompt: T6 POLICY_DOC (commit message skeleton)
- Acceptance: build passes; commit created
- Status: TODO
- Completion Log:

1.1 Prefix controllers with /api/v1
- Files: OrderController.java, DeliveryPartnerController.java
- Prompt: T1 CHANGE_FILE
- Acceptance: All public endpoints under /api/v1
- Status: DONE
- Completion Log:
- Both controllers already prefixed: /api/v1/orders and /api/v1/delivery-partners.
- No code changes required; verified current annotations and paths.

1.2 Standard error envelope and mapping
- Files: GlobalExceptionHandler.java, ErrorResponse.java
- Prompt: T1 CHANGE_FILE
- Acceptance: {traceId, code, message, details}
- Status: DONE
- Completion Log:
- GlobalExceptionHandler adds requestId from X-Correlation-Id; consistent reasonCode.
- Error envelope fields included across handlers; generic and specific exceptions updated.

1.3 Idempotency header policy doc
- Files: README.md
- Prompt: T6 POLICY_DOC
- Acceptance: Header usage and retention documented
- Status: DONE
- Completion Log:
- Added README section covering header, scope, behavior, retention, errors, response headers, security.

1.4 Pagination/filter/sort conventions
- Files: controllers and DTOs
- Prompt: T6 POLICY_DOC (spec) then T1 CHANGE_FILE (apply)
- Acceptance: Consistent params across list endpoints
- Status: TODO
- Completion Log:

1.5 OpenAPI security, headers, version
- Files: OpenApiConfig.java
- Prompt: T5 OPENAPI_EDIT
- Acceptance: JWT scheme, Idempotency-Key header, version
- Status: TODO
- Completion Log:

Phase 2: Observability and SRE (P0)
Objective: health, metrics, tracing, structured logs, correlation IDs.
Tasks:
2.1 Enable actuator endpoints and Prometheus
- Files: application-*.yml, pom.xml
- Prompt: T1 CHANGE_FILE
- Acceptance: /actuator/health, /actuator/metrics
- Status: TODO
- Completion Log:

2.2 Logging filter with correlationId
- Files: new common web LoggingFilter
- Prompt: T2 CREATE_FILE
- Acceptance: JSON logs with correlationId
- Status: TODO
- Completion Log:

2.3 Business metrics counters
- Files: service layers
- Prompt: T1 CHANGE_FILE
- Acceptance: counters for orders and delivery lifecycle
- Status: TODO
- Completion Log:

2.4 Tracing (OpenTelemetry) config docs
- Files: README.md
- Prompt: T6 POLICY_DOC
- Acceptance: setup for dev/prod documented
- Status: TODO
- Completion Log:

Phase 3: Security Hardening (P0)
Objective: OAuth2/JWT, RBAC, CORS, validation, secrets.
Tasks:
3.1 Resource server security config
- Files: new SecurityConfig
- Prompt: T2 CREATE_FILE
- Acceptance: role/scope checks pass tests
- Status: TODO
- Completion Log:

3.2 CORS policy
- Files: WebConfig.java
- Prompt: T1 CHANGE_FILE
- Acceptance: restricted origins via properties
- Status: TODO
- Completion Log:

3.3 DTO validation and error mapping
- Files: DTOs, GlobalExceptionHandler
- Prompt: T1 CHANGE_FILE
- Acceptance: 400 with validation details
- Status: TODO
- Completion Log:

3.4 Secrets via env and .env.sample
- Files: application-*.yml, new .env.sample
- Prompt: T1 CHANGE_FILE, T2 CREATE_FILE
- Acceptance: no secrets in repo
- Status: TODO
- Completion Log:

Phase 4: Data and Migrations (P0)
Objective: complete schema, indexes, retention jobs.
Tasks:
4.1 Index migrations
- Files: db/migration/V2__indexes.sql
- Prompt: T3 MIGRATION_SQL
- Acceptance: indexes for orders/outbox/idempotency
- Status: TODO
- Completion Log:

4.2 Outbox/idempotency cleanup job
- Files: new scheduler class, props
- Prompt: T2 CREATE_FILE
- Acceptance: retention configurable
- Status: TODO
- Completion Log:

4.3 Repository tests for indexed queries
- Files: tests
- Prompt: T4 TEST_FILE
- Acceptance: plans use indexes
- Status: TODO
- Completion Log:

Phase 5: Eventing Robustness (P0)
Objective: exactly-once, DLQ, event versioning.
Tasks:
5.1 Event base fields (id, occurredAt, version)
- Files: common event classes
- Prompt: T1 CHANGE_FILE
- Acceptance: serialization includes fields
- Status: TODO
- Completion Log:

5.2 Kafka consumer idempotency and DLQ
- Files: OrderEventKafkaConsumer.java
- Prompt: T1 CHANGE_FILE
- Acceptance: dedupe by eventId; DLQ configured
- Status: TODO
- Completion Log:

5.3 Retry/backoff policy doc
- Files: README.md
- Prompt: T6 POLICY_DOC
- Acceptance: clear retry and DLQ policy
- Status: TODO
- Completion Log:

Phase 6: Resilience Patterns (P1)
Objective: timeouts, retries, circuit breakers, bulkheads.
Tasks:
6.1 Apply Resilience4j to external calls
- Files: payment/notification/delivery services
- Prompt: T1 CHANGE_FILE
- Acceptance: graceful degradation; metrics exposed
- Status: TODO
- Completion Log:

6.2 Config defaults in yml
- Files: application-*.yml
- Prompt: T1 CHANGE_FILE
- Acceptance: sane defaults, overrides via env
- Status: TODO
- Completion Log:

Phase 7: Payments and Refunds (P1)
Objective: provider integration, webhooks, reconciliation.
Tasks:
7.1 Payment provider adapter and abstraction
- Files: payment module
- Prompt: T2 CREATE_FILE
- Acceptance: sandbox flows pass
- Status: TODO
- Completion Log:

7.2 Payment webhook endpoint
- Files: new controller
- Prompt: T2 CREATE_FILE
- Acceptance: signature validation and idempotency
- Status: TODO
- Completion Log:

7.3 Order state transitions alignment
- Files: Order aggregate, services
- Prompt: T1 CHANGE_FILE, T4 TEST_FILE
- Acceptance: consistent PaymentStatus transitions
- Status: TODO
- Completion Log:

Phase 8: Delivery Assignment (P1)
Objective: pluggable strategies, telemetry, ops override.
Tasks:
8.1 Strategy interface + implementations
- Files: delivery/service/strategy
- Prompt: T2 CREATE_FILE
- Acceptance: selectable via config
- Status: TODO
- Completion Log:

8.2 Assignment metrics
- Files: delivery service
- Prompt: T1 CHANGE_FILE
- Acceptance: latency and success metrics
- Status: TODO
- Completion Log:

8.3 Ops reassignment endpoint
- Files: DeliveryPartnerController.java
- Prompt: T1 CHANGE_FILE
- Acceptance: RBAC protected
- Status: TODO
- Completion Log:

Phase 9: Notifications (P1)
Objective: templates, providers, preferences.
Tasks:
9.1 Notification abstraction + provider adapters
- Files: notification module
- Prompt: T2 CREATE_FILE
- Acceptance: reliable, idempotent sends
- Status: TODO
- Completion Log:

9.2 Templates for key events
- Files: templates store
- Prompt: T2 CREATE_FILE
- Acceptance: customizable templates
- Status: TODO
- Completion Log:

9.3 Preferences model and enforcement
- Files: domain + APIs
- Prompt: T2 CREATE_FILE, T1 CHANGE_FILE
- Acceptance: opt-out respected
- Status: TODO
- Completion Log:

Phase 10: Real-time Updates (P2)
Objective: SSE/WebSockets for order status.
Tasks:
10.1 SSE endpoint
- Files: new controller
- Prompt: T2 CREATE_FILE
- Acceptance: secure stream by orderId
- Status: TODO
- Completion Log:

10.2 Kafka-to-SSE bridge
- Files: event listener
- Prompt: T2 CREATE_FILE
- Acceptance: backpressure friendly
- Status: TODO
- Completion Log:

Phase 11: Ops Console (P2)
Objective: operational dashboards and controls.
Tasks:
11.1 Admin UI pages
- Files: Thymeleaf or SPA scaffolding
- Prompt: T2 CREATE_FILE
- Acceptance: pipeline view
- Status: TODO
- Completion Log:

11.2 Admin actions (reassign, refund approve, replay DLQ)
- Files: controllers/services
- Prompt: T1 CHANGE_FILE
- Acceptance: audited and RBAC
- Status: TODO
- Completion Log:

Phase 12: Compliance and Privacy (P2)
Objective: audit logging, PII handling, DSR.
Tasks:
12.1 Audit trail for critical actions
- Files: common audit module
- Prompt: T2 CREATE_FILE
- Acceptance: persist audit records
- Status: TODO
- Completion Log:

12.2 Data classification and masking in logs
- Files: logging filter and mappers
- Prompt: T1 CHANGE_FILE
- Acceptance: no PII in logs
- Status: TODO
- Completion Log:

12.3 Data subject requests workflow
- Files: controllers/services
- Prompt: T2 CREATE_FILE
- Acceptance: export/delete flows
- Status: TODO
- Completion Log:

Phase 13: Performance and Scalability (P2)
Objective: indices, load tests, Kafka tuning.
Tasks:
13.1 Additional index migrations
- Files: Flyway V[seq]
- Prompt: T3 MIGRATION_SQL
- Acceptance: target queries optimized
- Status: TODO
- Completion Log:

13.2 Load test scripts
- Files: perf/ (Gatling or JMeter)
- Prompt: T2 CREATE_FILE
- Acceptance: meets SLOs
- Status: TODO
- Completion Log:

Phase 14: CI/CD and Release (P1)
Objective: automated build/test/scan/release.
Tasks:
14.1 CI workflow YAML
- Files: .github/workflows/build.yml
- Prompt: T2 CREATE_FILE
- Acceptance: build+tests+coverage+scan
- Status: TODO
- Completion Log:

14.2 Dockerfile hardening
- Files: Dockerfile
- Prompt: T1 CHANGE_FILE
- Acceptance: multi-stage, non-root
- Status: TODO
- Completion Log:

14.3 Release policy doc
- Files: README.md
- Prompt: T6 POLICY_DOC
- Acceptance: versioning and changelog
- Status: TODO
- Completion Log:

Phase 15: Documentation and Onboarding (P1)
Objective: frictionless developer experience.
Tasks:
15.1 Quickstart with docker-compose
- Files: docker-compose.yml, README.md
- Prompt: T1 CHANGE_FILE, T6 POLICY_DOC
- Acceptance: run in <15 minutes
- Status: TODO
- Completion Log:

15.2 API usage snippets and Postman collection
- Files: README.md, postman/collection.json
- Prompt: T6 POLICY_DOC, T2 CREATE_FILE
- Acceptance: copy-paste ready
- Status: TODO
- Completion Log:

Review Cadence
- After finishing a phase, run T0 REVIEW on diff summary to confirm scope.
- Keep this plan.md updated; do not remove tasks; only change Status and add Completion Logs.
