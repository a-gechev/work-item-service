# Not doing (and why)

Scope was fixed before implementation began. This list exists so the omissions read as
deliberate, not as gaps someone forgot to fill. Each entry: what was cut, and the one-line
reason.

## Whole capabilities

- **Notifications bounded context** — out of scope; the outbox already demonstrates reliable
  event publishing, a third context wouldn't add a new architectural lesson.
- **Recurring tasks / timezone handling** — real complexity, but orthogonal to the two things
  this project is meant to prove (module boundaries, dependency-graph algorithm).
- **Metrics dashboards, distributed tracing** — operational maturity that doesn't test the
  skills this project targets (design, DSA, testing discipline).

## Infrastructure

- **Actual message broker** — in-process event dispatch is enough to prove the outbox pattern.
  _The seam is named:_ the outbox publisher sits behind a `DomainEventPublisher` interface;
  swapping in RabbitMQ or Kafka replaces one implementation class and no domain code.
- **Token issuance, self-registration, password management** — this service *validates* JWTs,
  it does not issue them. Authentication is the identity provider's job, and drawing that line
  is itself the decision worth recording. See ADR-0005.

## Inside the Users context

The Users context is real and holds genuine invariants (see ADR-0005), but it was kept
deliberately small. What was considered and declined:

- **Profile attributes — position, team, contact details beyond email** — storage without rules.
  No behaviour in the system reads them, so they would grow the model without growing the design.
- **Organisational hierarchy (manager / subordinates)** — a second graph, and a tree at that.
  Cycles are impossible by construction, so there is no invariant to enforce and no interesting
  traversal to test. The work-item dependency DAG already demonstrates graph work, with a
  stronger invariant (acyclicity) and a real algorithm (cycle detection, topological ordering).
- **Per-user WIP limits and "is this person available for more work?"** — the *count* belongs to
  Work Items, the *limit* would belong to Users, and composing them needs an orchestration layer
  this system does not otherwise justify. _The seam is named:_ `WorkItemApi.activeCountFor(assigneeId)`
  answers the counting half today with no cross-context dependency.
- **Fine-grained permissions (`canEdit`, `canDelete`) as user attributes** — authorisation over a
  work item depends on the work item's own state and ownership, so it is decided in Work Items,
  next to the resource. Users owns *standing* (`MEMBER` / `ADMIN`); Work Items owns *what that
  standing permits here*. See ADR-0005.

## Scope freeze

Scope was frozen before the first line of implementation code and has not been widened since.
Features that arrived after the freeze were written down here instead of built. Nothing on this
list is a discovery made late — each entry is a decision made with the alternative understood.

_Updated as decisions are made — entries added here should never quietly disappear from the plan._
