# ADR-0006: The dependency graph as a single consistency boundary

- **Status:** Proposed
- **Date:** 2026-09-__

## Context

Work items depend on other work items, and that dependency structure must stay acyclic: a cycle
means every item in the loop waits on another item in the loop permanently, which is not a slow
system but an impossible one. Acyclicity is therefore a domain invariant, not a performance
concern.

The difficulty is that the invariant does not fit inside a single work item. Adding an edge
A → B is a valid operation as far as either item is concerned; whether it closes a cycle is a
property of the *whole* graph. A `WorkItem` aggregate that owns its own outgoing edges can enforce
nothing useful about them, because the fact that invalidates the edge lives arbitrarily far away.

That forces a choice about the consistency boundary. Either the boundary is the entire graph —
with the transaction scope and contention that implies — or it is smaller and the invariant holds
only under a weaker guarantee, with concurrent edge insertions able to race into a cycle that
neither insertion could see on its own.

Two further decisions hang off the same structure and are recorded here rather than separately,
because they are not independent of it: where the acyclicity check runs, and which traversal
answers the ordering queries.

Constraints that are real: one developer, a single PostgreSQL instance, one transaction boundary
(ADR-0001), optimistic locking via `@Version` already in use, and no requirement for high write
concurrency in this system's actual usage.

## Options considered

<!-- Fill in during Week 3, after implementing it and feeling where it binds. -->

## Decision

<!-- Week 3. -->

## Consequences

<!-- Week 3. -->

## What we gave up

<!-- Week 3. -->

---

**Questions this ADR must answer — in the sections above, not here:**

**Aggregate boundary**

- Who owns a dependency edge: the `WorkItem` aggregate, a separate `DependencyGraph` aggregate, or
  a standalone entity with no root?
- What is locked when an edge is added, and what happens when two edges are added concurrently that
  are individually safe but jointly form a cycle? Name the actual mechanism — pessimistic lock,
  optimistic version on a graph root, serializable isolation, or an accepted race with a repair.
- If the boundary is the whole graph, what does that cost under concurrent writes, and why is that
  acceptable at this system's scale?

**Where the check runs**

- Acyclicity is enforced on write rather than checked on read. State why: the invariant holds at all
  times, so `plan()` cannot fail at read time — the constraint on one operation is what makes the
  other total.
- The check is incremental. Since the graph was acyclic before the insert, adding A → B can only
  create a cycle if B already reaches A, so it is one reachability search rather than full
  detection. Same worst case, much smaller typical case. Say what that exploits.

**Algorithm**

- Kahn's over DFS. The defensible reason is that Kahn's intermediate state is a domain concept —
  each frontier is one `startable()` wave — while DFS's is a call stack. Stack-depth safety on a
  long chain is true but secondary; do not lead with it.
- Topological order is not unique. How was it tested? (Property assertion: for every edge u → v,
  index(u) < index(v) — not an exact expected list.)
- Complexity is O(V + E) for both traversals. What was done to keep the traversed subgraph small,
  and what did that do to the N+1 problem?

**Read model**

- `startable()` computed on read, or an "unblocked" flag maintained on write? Give the choice and
  what the rejected option would have cost — derived state that can drift, versus a traversal on
  every call.
