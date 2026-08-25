# ADR-0002: PostgreSQL over MongoDB

- **Status:** Proposed
- **Date:** 2026-09-__

## Context

The core data is highly relational: work items reference other work items as dependencies, and
the central query — "what can I start right now?" — is a traversal over that reference graph.
Status transitions must be atomic and must not interleave incorrectly under concurrent updates,
which makes multi-row transactional guarantees a functional requirement rather than a preference.
Schema is known in advance and stable; there is no ingestion of heterogeneous documents.

## Options considered

<!-- Fill in during Week 3, once the schema exists and the constraints have actually bitten. -->

## Decision

<!-- Week 3. -->

## Consequences

<!-- Week 3. -->

## What we gave up

<!-- Week 3. -->

---

**Prompts to answer in the sections above — not here:**

- What specifically would MongoDB have made *easier*? If the answer is "nothing", the alternative
  was not considered seriously, and an interviewer will find that out.
- Where did the relational choice cost you? Name a concrete place in your own schema.
- Optimistic locking via `@Version` — what does the database actually guarantee here, and what does
  Hibernate add on top?
