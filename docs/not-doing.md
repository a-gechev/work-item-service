# Not doing (and why)

Scope was fixed before implementation began. This list exists so the omissions read as
deliberate, not as gaps someone forgot to fill. Each entry: what was cut, and the one-line
reason.

## Whole capabilities

- **Notifications bounded context** — not built. Work item events are written to the outbox; a
  notification service would consume them. _The seam exists for a reason that holds whether or not
  the consumer is ever built:_ sending mail inside the state-changing transaction is the failure
  the outbox pattern is for. Commit them together and either the transaction rolls back after the
  mail has already gone out, or a mail failure takes a valid state change down with it. Recording
  the fact and acting on the fact have to be separately committable. A third bounded context is out
  of scope here, and the pattern is demonstrated by the seam rather than by the feature.
- **Recurring tasks / timezone handling** — real complexity, but orthogonal to the two things
  this project is meant to prove (module boundaries, dependency-graph algorithm).
- **Metrics dashboards, distributed tracing** — operational maturity that doesn't test the
  skills this project targets (design, DSA, testing discipline).

## Infrastructure

- **Actual message broker** — in-process event dispatch is enough to prove the outbox pattern.
  _The seam is named:_ each context declares its own event-publisher port in its application
  layer (`WorkItemEventPublisher`, `UserEventPublisher`), and an adapter in `adapters/out`
  implements that port against the `outbox` module. Swapping in RabbitMQ or Kafka replaces one
  adapter class per context and no domain or application code. See ADR-0007.
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
  this system does not otherwise justify. _The seam is named:_ `WorkItemApi.openItemCountFor(assigneeId)`
  answers the counting half today with no cross-context dependency. It counts items in any status
  other than `FINISHED`, so whoever imposes a limit later chooses the threshold.
- **Fine-grained permissions (`canEdit`, `canDelete`) as user attributes** — authorisation over a
  work item depends on the work item's own state and the requester's relationship to it, so it is
  decided in Work Items, next to the resource. Users owns *standing* (`MEMBER` / `ADMIN`); Work
  Items owns *what that standing permits here*. See ADR-0005.

## Inside the Work Items context

- **Work item ownership** — not modelled. A work item has an assignee and no owner. In the domain
  this describes an owner is real and distinct from the assignee: accountable for the item, versus
  currently doing it. The field itself is cheap; the behaviour that would make it mean anything is
  not — a second authorisation path over every operation, and a second recipient in every
  notification. An owner attribute with no behaviour attached is vocabulary without design.

## Scope freeze

Scope was frozen before the first line of implementation code and has not been widened since.
Features that arrived after the freeze were written down here instead of built. Nothing on this
list is a discovery made late — each entry is a decision made with the alternative understood.

_Updated as decisions are made — entries added here should never quietly disappear from the plan._
