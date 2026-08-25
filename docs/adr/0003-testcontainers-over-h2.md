# ADR-0003: Testcontainers over an in-memory H2 database

- **Status:** Proposed
- **Date:** 2026-09-__

## Context

Integration tests need a database. H2 starts in milliseconds and needs no Docker daemon;
Testcontainers starts a real PostgreSQL instance per test run, costing seconds of startup and a
Docker dependency in CI.

The tests that matter most in this project are precisely the ones where the two databases diverge:
optimistic-locking conflict behaviour, transactional rollback semantics around the outbox, and any
Postgres-specific SQL or type behaviour the schema relies on.

## Options considered

<!-- Fill in during Week 3–4, once the first divergence has actually been hit. -->

## Decision

<!-- Week 3–4. -->

## Consequences

<!-- Week 3–4. -->

## What we gave up

<!-- Week 3–4. -->

---

**Prompts to answer in the sections above — not here:**

- Name one concrete behaviour where H2 in Postgres-compatibility mode would have passed a test that
  real Postgres fails, or vice versa. A specific example is required, not the general claim — find
  one while building, it is the strongest version of this answer.
- What did Testcontainers cost in CI wall-clock time? Actual numbers.
- Where do plain unit tests with no database remain, and why is that the right call there?
