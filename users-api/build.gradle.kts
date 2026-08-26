plugins {
    id("workitem.java-conventions")
}

// No dependencies, by design. This is the published contract between contexts;
// anything it depended on would become part of that contract. See ADR-0007.