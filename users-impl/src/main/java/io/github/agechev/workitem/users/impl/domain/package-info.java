/**
 * The Users domain: identity, standing, lifecycle, and the invariants over them — email uniqueness,
 * the last remaining admin, idempotent deactivation.
 *
 * <p>No framework, no infrastructure, no dependency on {@code application} or {@code adapters}. It
 * may reference {@code users.api}, which is a dependency-free contract rather than infrastructure,
 * and its aggregate methods <em>return</em> {@code UserEvent} instances rather than publishing them.
 */
package io.github.agechev.workitem.users.impl.domain;
