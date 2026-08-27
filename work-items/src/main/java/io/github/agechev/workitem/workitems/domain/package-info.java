/**
 * The Work Items domain: aggregates, value objects, the status lifecycle, and the invariants of the
 * dependency graph.
 *
 * <p>Depends on nothing. No Spring, no JPA, no Jakarta, no HTTP types, nothing from
 * {@code application} or {@code adapters}, and nothing from the {@code outbox} module. A method that
 * has caused something worth telling the world about <em>returns</em> the event; it never publishes
 * one.
 */
package io.github.agechev.workitem.workitems.domain;
