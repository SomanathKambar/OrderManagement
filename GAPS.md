# Alignment Gap Analysis: One Vibe Coding (2026 Theme)

This document tracks the alignment of the **OrderManagement** project with the "One Vibe Coding" principles: AI-first planning, extreme modularity, and agent-friendly observability.

## 🎯 Current Status
- **Architecture**: Modular Monolith (Strong Alignment)
- **Patterns**: DDD, Outbox, Idempotency (Strong Alignment)
- **Observability**: Standard Actuator (Needs semantic upgrade)
- **Safety**: Basic Integration Tests (Needs 90%+ coverage)

## 🚩 Identified Gaps
1. **Semantic Context**: Lack of module-level "vibe" docs for AI agents to understand boundaries.
2. **Observability for Agents**: Logs lack "Reason Codes" and natural language explanations of state failures.
3. **Safety Nets**: Coverage gap in complex domain state transitions.
4. **Developer Control Plane**: No agent-friendly CLI to reset, seed, or simulate events.

## 🚀 Execution Plan

### Phase 1: Semantic Foundation (The "Context" Vibe) - 🟢 IN PROGRESS
- [ ] Create `ARCHITECTURE.md` for core modules (`ordering`, `delivery`, `common`).
- [ ] Implement Structured Logging with Reason Codes.
- [ ] Generate a consolidated `CONTEXT.md` for LLM context injection.

### Phase 2: Safety & Feedback Loops (The "Safe" Vibe)
- [ ] Target 90%+ Unit Test coverage for Domain Models.
- [ ] Implement Contract Testing between modules.
- [ ] Add "Dry Run" mode to state-changing APIs.

### Phase 3: Agentic Interfaces (The "Autonomous" Vibe)
- [ ] Create `oms-cli` for agent-triggered maintenance.
- [ ] Upgrade to Java 21 (Virtual Threads & Record Patterns) to simplify code logic.
- [ ] Automated LLM-migration validation scripts.
