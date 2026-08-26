plugins {
    id("workitem.java-conventions")
}

// No project dependencies, by design. The outbox takes an opaque record and
// knows nothing about either context. The day this block is non-empty, the
// shared kernel is back. See ADR-0007.