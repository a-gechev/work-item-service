# ADR-0007: No shared kernel; contexts integrate through published API modules

- **Status:** Accepted
- **Date:** 2026-08-25
- **Amends:** ADR-0001, which specified "one Gradle module per bounded context plus a
  `shared-kernel`". The rest of ADR-0001 stands; only the module-layout clause is superseded here.

## Context

ADR-0001 committed to a modular monolith with one module per bounded context plus a `shared-kernel`.
The shared kernel was specified before there was anything to put in it, on the assumption that two
contexts would eventually need common domain types.

The first thing proposed for it was the transactional outbox (ADR-0004), and the architecture
diagram drew both domain layers depending on it. That exposed two problems at once.

The first is a layering problem. The outbox is infrastructure: a table, a repository and a polling
publisher. A domain layer that depends on it cannot satisfy the rule that the domain depends on no
framework, and an aggregate that writes to an outbox cannot be unit tested without one.

The second is a problem with the shared kernel itself. In Evans' formulation a Shared Kernel is a
subset of the *domain model* that two contexts agree to share and to change only by mutual consent.
It is explicitly a compromise, chosen when the cost of duplication exceeds the cost of coupling. The
outbox is not domain model, so placing it there would have made `shared-kernel` a module for
"anything more than one context happens to need" — which is the shared mutable dependency ADR-0001
warned about, arriving by a different route.

Removing the outbox raised the question the module had never been asked: what else belongs in it?
Running through the model, nothing does. `UserId` is owned by Users and published in `users-api`.
`WorkItemId` is owned by Work Items. There are no value objects both contexts reason about. A shared
`AggregateRoot` supertype is scaffolding rather than domain model, and sharing it would couple two
contexts through inheritance to save a few lines.

The remaining candidates were the ones the outbox needs: a `DomainEvent` marker interface and a
`DomainEventPublisher` port. Those are real, but they are not necessarily *shared* — each context can
own its own.

## Options considered

1. **Keep `shared-kernel`, holding a `DomainEvent` marker and one `DomainEventPublisher` port.**
   The port is a genuine contract both contexts hold, the module stays small and honestly populated,
   and there is exactly one interface for the outbox to implement.

2. **Keep `shared-kernel`, move only the outbox implementation out of it.** The smallest possible
   change. Leaves a module whose contents are whatever did not fit elsewhere.

3. **No shared kernel.** Each context declares its own event-publisher port in its application layer
   and its own event type. An outbound adapter in each context implements that port against a
   standalone `outbox` infrastructure module, which knows nothing about either context.

## Decision

Option 3. The `shared-kernel` module is removed from the build.

The modules are `work-items`, `users-api`, `users-impl`, `outbox` and `app`.

Domain events leave the domain as return values: an aggregate method returns what happened and never
performs the side effect itself. The application layer declares the port — `WorkItemEventPublisher`,
`UserEventPublisher` — and hands events to it inside the same transaction as the state change. An
adapter in `adapters/out` implements that port against `OutboxWriter`, which takes an opaque record
of aggregate type, aggregate id, event type, serialised payload and timestamp.

Consequently the contexts depend on the `outbox` module only from `adapters/out`. Neither `domain`
nor `application` may reference it, and an ArchUnit rule enforces that alongside the existing
framework-independence rule.

Work Items and Users integrate through `users-api` alone: a published contract with a single owner.
Users decides what is in it; Work Items consumes it and has no say.

## Consequences

**Positive**

- The domain layer depends on nothing at all, so its framework-independence rule is trivially true
  rather than narrowly true, and its unit tests need no test doubles for event publishing.
- There is no jointly owned module. The only cross-context coupling is a published API with one
  owner, which is a weaker and better-defined relationship than a shared kernel.
- The outbox is swappable exactly as `docs/not-doing.md` claims: replacing it with a broker changes
  one adapter class per context and no domain or application code.
- A module that exists only in case it is needed later cannot quietly accumulate contents. Removing
  it removes that pressure permanently rather than promising to resist it.

**Negative**

- The event-publisher port is duplicated — two small interfaces that are unlikely ever to diverge.
  Duplication is real and someone will ask about it.
- Both contexts' outbound adapters depend on the `outbox` module, so that module is now a supplier
  to both. It is one-directional and contains no domain concepts, but it is the place to watch: if
  domain vocabulary ever appears in it, it has become a shared kernel under another name.
- If a genuinely shared domain concept appears later, it will need a new home and this decision will
  have to be revisited. That is the intended trade — pay the cost when the need is demonstrated,
  not in advance.

## What we gave up

Option 1 is a reasonable design and would have been the conventional choice. A single
`DomainEventPublisher` port is a real contract rather than an incidental one; sharing it means one
interface for the outbox to implement instead of two, no duplication to explain, and a
`shared-kernel` whose contents are defensible. The strongest form of the argument is that a shared
kernel of two pure interfaces is so small that the coupling is theoretical, while the duplication it
avoids is concrete.

The reason to decline is that the coupling is not theoretical, it is deferred. A module that exists
attracts contents, and a module named `shared-kernel` attracts anything more than one context needs
— which is precisely how the outbox came to be proposed for it. The duplication being avoided is two
three-line interfaces. Trading a jointly owned module for six lines is not a trade worth making, and
declining it costs nothing that can be pointed at.
