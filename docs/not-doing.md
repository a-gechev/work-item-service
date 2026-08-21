# Not doing (and why)

Scope was fixed before implementation began. This list exists so the omissions read as
deliberate, not as gaps someone forgot to fill. Each entry: what was cut, and the one-line
reason.

- **Notifications bounded context** — out of scope; the outbox already demonstrates reliable
  event publishing, a third context wouldn't add a new architectural lesson.
- **Recurring tasks / timezone handling** — real complexity, but orthogonal to the two things
  this project is meant to prove (module boundaries, dependency-graph algorithm).
- **Actual message broker** — in-process event dispatch via the outbox is enough to prove the
  pattern; swapping in Kafka/RabbitMQ later is a contained change, not a redesign.
- **Users & Access as a full bounded context** — a simple auth stub is sufficient; building
  real auth would spend a week proving nothing new architecturally.
- **Metrics dashboards, distributed tracing** — operational maturity that doesn't test the
  skills this project targets (design, DSA, testing discipline).

_Updated as decisions are made — entries added here should never quietly disappear from the plan._
