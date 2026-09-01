# Architecture Decision Records

Short records of the decisions that shaped this project, each stating what was given up and
not only what was chosen. Written on the day the decision was made, not reconstructed
afterwards — the commit dates should match the ADR dates.

| ADR | Decision | Status |
|---|---|---|
| 0001 | Modular monolith over microservices | Accepted — module layout amended by 0007 |
| 0002 | PostgreSQL over MongoDB | Proposed |
| 0003 | Testcontainers over an in-memory H2 database | Proposed |
| 0004 | Transactional outbox over publishing events directly | Proposed |
| 0005 | A separate Users context over a shared user model | Accepted |
| 0006 | The dependency graph as a single consistency boundary | Proposed |
| 0007 | No shared kernel; contexts integrate through published API modules | Accepted |
| 0008 | Module-owned Spring configuration over wiring in the composition root | Accepted |

Each ADR follows: context → options considered → decision → consequences → what we gave up.

The final section is not standard in most ADR formats. It is here on purpose: if the strongest
argument for the rejected option cannot be written down honestly, the alternative was not
understood well enough to have been rejected.
