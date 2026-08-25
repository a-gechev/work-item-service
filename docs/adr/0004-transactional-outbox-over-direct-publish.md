# ADR-0004: Transactional outbox over publishing events directly

- **Status:** Proposed
- **Date:** 2026-09-__

## Context

Domain events must not be observable when the transaction that produced them rolls back. Publishing
directly from application code — inside or immediately after the transaction — creates a window in
which the event escapes and the state change does not survive, or the reverse: the state commits
and the publish fails silently. Either way the system's observers see a history that never happened.

An outbox writes the event into the same database transaction as the state change and publishes it
afterwards from a separate poller, moving the problem from "two systems must agree" to "one
transaction, then at-least-once delivery".

## Options considered

<!-- Fill in during Week 4, once the rollback test exists and passes. -->

## Decision

<!-- Week 4. -->

## Consequences

<!-- Week 4. -->

## What we gave up

<!-- Week 4. -->

---

**Prompts to answer in the sections above — not here:**

- At-least-once delivery means consumers can see duplicates. What is the idempotency story? This is
  the standard follow-up question and the one most candidates fail — do not hand-wave it.
- Why a polling publisher rather than change-data-capture? What would you switch to at scale?
- Describe the rollback test in one sentence. Doing that cleanly is enough to carry a whole
  interview on this ADR.
