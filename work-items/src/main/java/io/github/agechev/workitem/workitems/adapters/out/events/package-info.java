/**
 * The outbox-backed implementation of {@code WorkItemEventPublisher}.
 *
 * <p>This is the only package in the Work Items context permitted to depend on the {@code outbox}
 * module — ArchUnit Rule 6. Gradle cannot express that constraint, because its dependency graph has
 * module granularity and {@code work-items} as a whole must be able to see {@code outbox}. The test
 * is the enforcement.
 *
 * <p>Swapping the outbox for a real broker replaces this package and nothing else.
 */
package io.github.agechev.workitem.workitems.adapters.out.events;
