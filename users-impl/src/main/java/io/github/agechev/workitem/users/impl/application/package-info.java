/**
 * Users use cases, and the ports they need.
 *
 * <p>Provisioning, deactivation, reactivation and the queries {@code users.api} publishes. Depends on
 * {@code domain} and {@code users.api}; never on {@code adapters}, a framework, or the {@code outbox}
 * module. The acting user arrives as an explicit parameter.
 */
package io.github.agechev.workitem.users.impl.application;
