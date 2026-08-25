# ADR-0001: Modular monolith over microservices

- **Status:** Accepted
- **Date:** 2026-08-23

## Context

This service has two bounded contexts (Work Items, Users), one developer, and a fixed five-week
build window. There is no scaling requirement, no independent release cadence between contexts,
and no team boundary that a service boundary would mirror.

The engineering question the project exists to answer is whether module boundaries can be held
without a distributed runtime enforcing them. Splitting into services would answer a different
question — one about operations — and would answer it shallowly at this scale.

## Options considered

1. **Microservices, one per context.** Independently deployable, physically enforced boundaries,
   and the currently fashionable answer.
2. **Single-module Spring Boot application.** Fastest to build; boundaries exist only as package
   names and hold only as long as discipline does.
3. **Modular monolith.** One deployable, multiple build modules, boundaries enforced by build-time
   rules.

## Decision

A single deployable unit, built as multiple Gradle modules: one per bounded context plus a
`shared-kernel`. A module may reference another only through its published `api` package.
This is enforced by ArchUnit tests that run in CI and fail the build on violation. Cross-context
communication uses in-process domain events published through a transactional outbox.

## Consequences

**Positive**

- Boundary violations fail the build rather than surviving code review. The rule is executable.
- One database and one transaction boundary, which is what makes the outbox rollback test
  meaningful and provable.
- The extraction path stays open: a module whose only outward dependency is another module's API
  package can be lifted into its own service without rewriting its internals.
- No operational cost — no service discovery, no distributed tracing, no partial-failure handling —
  for capabilities this system does not need.

**Negative**

- No independent deployability; any change redeploys everything.
- Compile-time coupling through `shared-kernel`: a change there rebuilds every module, so it must
  be kept genuinely minimal or it becomes a shared mutable dependency by another name.
- Nothing here demonstrates operating a distributed system. Partially mitigated by the outbox,
  which addresses the correctness problem distribution creates, but not the operational one.

## What we gave up

Microservices would have demonstrated familiarity with service discovery, network partition
handling and independent deployment pipelines — all things employers ask about. The reason to
decline is honesty about scale: at two contexts and one developer, a service split produces
distributed-systems overhead with no distributed-systems benefit, and building it would demonstrate
a willingness to adopt an architecture without justifying it. That is the opposite of what this
repository is meant to show.
