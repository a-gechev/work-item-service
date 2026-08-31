/**
 * The outbox-backed implementation of {@code UserEventPublisher}.
 *
 * <p>The only package in the Users context permitted to depend on the {@code outbox} module —
 * ArchUnit Rule 6. Swapping the outbox for a real broker replaces this package and nothing else.
 */
package io.github.agechev.workitem.users.impl.adapters.out.events;
