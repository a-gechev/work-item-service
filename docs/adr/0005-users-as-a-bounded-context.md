# ADR-0005: A separate Users context over a shared user model

- **Status:** Accepted
- **Date:** 2026-08-24

## Context

The service needs to know who a work item is assigned to, who created it, and whether the
person acting on it has the standing to do so. The obvious approach — one `User` entity that
every part of the system references — is also how a codebase acquires a forty-field class that
nobody dares change, because each area of the system adds the attributes it happens to need.

Two facts constrain the design:

1. **The service validates JWTs but does not issue them.** The identity provider is external and
   knows nothing about this application's notion of standing. The token carries `sub` — who the
   caller is — and nothing else this system can trust as authoritative.
2. **Work Items must be able to hold an assignee reference indefinitely.** If a person's record
   can disappear, every historical work item acquires a dangling reference and every read path
   acquires a null check.

The question is therefore not "should users live in their own module" but "is there a distinct
model here with rules only it can enforce" — and if so, which way the dependency points.

## Options considered

1. **One shared `User` entity in `shared-kernel`.** Simplest to write. Both contexts reference the
   same class, so there is nothing to resolve and no API to design.
2. **No Users context at all — trust the token.** Work Items stores an opaque `AssigneeId` taken
   from the JWT `sub` and never resolves it to anything. Zero coupling.
3. **A separate Users context with a published API**, depended on in one direction only.

## Decision

Users is a bounded context in its own right, split across two Gradle modules: `users-api`
(identifiers, the `UserApi` interface, the `UserSummary` value object, published events) and
`users-impl` (persistence, application services, REST adapter). Work Items depends on
`users-api` and nothing else; Users depends on nothing. The dependency graph between contexts is
acyclic and enforced by ArchUnit, not convention.

Users is the sole authority on:

- identity — `UserId`, email, display name, and the creation of user records
- standing — `role` (`MEMBER` | `ADMIN`), which the token cannot supply
- lifecycle — `ACTIVE` / `DEACTIVATED`, with deactivation implemented as a status change
- the guarantee that a `UserId`, once issued, resolves forever

and enforces these invariants:

- no hard delete; an issued `UserId` always resolves
- email is globally unique and permanent — a deactivated user's address is never released for
  reuse. Releasing it would let a later account inherit the departed person's identity in every
  external system keyed on email, and would make an audit trail ambiguous about who acted
- the last remaining `ADMIN` cannot be deactivated
- deactivation is idempotent; reactivation restores the existing record and cannot conflict on
  email, because the address was never released
- display name is non-blank

The email rule is checked in the domain and reported as a domain error. A unique index backs it
as a last line of defence against a race, but the application must never rely on catching a
persistence exception to enforce a business rule — that leaks an infrastructure failure mode
into the application layer and produces an error message written by Hibernate.

Authorisation is deliberately split. Users owns *standing*; Work Items decides *what that standing
permits against a particular work item*, because that decision depends on the item's own status
and ownership — facts Users must never know.

Cross-context communication uses two channels: a synchronous `UserApi.resolve` / `resolveAll`
for questions Work Items needs answered now, and a `UserDeactivated` domain event published
through the outbox, which Work Items consumes to unassign the deactivated person's open items.

## Consequences

**Positive**

- Users is independently buildable and testable. Its entire suite runs with the Work Items module
  absent from the classpath — a claim verifiable in seconds.
- Work Items compiles and unit-tests against `users-api` alone, with `UserApi` stubbed. No
  database, no Spring, no Users implementation on the test classpath.
- The resolve-forever guarantee removes an entire class of problem from Work Items: an assignee
  reference is always resolvable, so no null checks and no foreign-key coupling between contexts.
- The extraction path stays open. Users can become its own service; `UserApi` becomes an HTTP
  client behind the same interface and no domain code changes.
- Two contexts publish events rather than one, so the outbox is a mechanism the system genuinely
  needs rather than a pattern demonstrated in isolation.
- Splitting `users-api` from `users-impl` applies "depend on the interface, not the
  implementation" at module granularity — the same discipline OSGi enforces at runtime through
  exported versus private packages, applied here at build time.

**Negative**

- Work Items cannot be built entirely alone; `users-api` must be present. The cost is small — that
  module is interfaces and value objects with no framework dependencies — but it is real.
- `UserSummary` duplicates a subset of the Users model. When a field is added to one, a deliberate
  decision is required about the other. That friction is the price of the boundary, and it is the
  point rather than a defect.
- Batch resolution (`resolveAll`) exists to avoid an N+1 across the module boundary. It is API
  surface added in anticipation of a problem, which is a small violation of YAGNI accepted
  knowingly: the alternative is discovering it under load and changing a published contract.
- The eventual-consistency window is real. Between a user's deactivation committing and the
  outbox poller delivering `UserDeactivated`, Work Items still shows them as assigned. Acceptable
  here; it would not be if assignment carried an access-control meaning.

## What we gave up

**Option 1 — the shared `User` entity — would have been considerably less work**, and at two
contexts and one developer, the forty-field failure mode is years away and may never arrive. The
honest argument for it is that a shared kernel type is the correct choice when both sides genuinely
mean the same thing, and premature separation imposes mapping code and a published contract for no
present benefit. It was rejected because the two sides do *not* mean the same thing: Work Items
means "an assignee — an identifier and a name to render", Users means "an account with standing and
a lifecycle". Sharing one class would have forced Work Items to carry role and status fields it has
no business reading, which is precisely how the boundary erodes.

**Option 2 — trusting the token and holding an opaque id — is the stronger rejected option** and
deserves the more careful answer. It has genuinely zero coupling, it needs no API design, and for a
system whose only use of identity is stamping an id onto a record, it is correct. It was rejected
because two of the system's rules cannot be expressed under it. There is nowhere to enforce that the
last admin cannot be deactivated, because there is no admin concept the system owns. And there is
nowhere to react to a person leaving, because nothing in the system knows that a person can leave —
their open work items would remain assigned to an identifier that no longer means anything. Choosing
option 3 buys the ability to hold those two invariants, and costs a published contract between the
two modules. If either invariant were dropped, option 2 would become the better design.
