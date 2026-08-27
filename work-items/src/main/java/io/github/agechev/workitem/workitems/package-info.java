/**
 * The Work Items bounded context.
 *
 * <p>Owns work item identity, the status lifecycle, and the dependency graph and its acyclicity.
 * Sees the Users context only through {@code users.api}; it must never reach {@code users.impl},
 * and must never learn how a user is authenticated or stored.
 *
 * <p>Layered hexagonally: {@code domain} knows nothing, {@code application} knows the domain,
 * {@code adapters} know the application. Dependencies point inwards, and that is enforced by
 * ArchUnit rather than by convention.
 */
package io.github.agechev.workitem.workitems;
