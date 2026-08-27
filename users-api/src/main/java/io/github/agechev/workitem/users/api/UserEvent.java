package io.github.agechev.workitem.users.api;

import java.time.Instant;

/**
 * A fact that has already happened to a user, and that other contexts are allowed to know about.
 *
 * <p>Users' events live in the published contract rather than in {@code users.impl.domain} because
 * they are consumed outside the context: Work Items listens for {@code UserDeactivated}. A type
 * under {@code impl} would be unreachable to that listener, and {@code users-api} cannot borrow a
 * marker from {@code users-impl} without inverting the dependency the module split exists to create.
 *
 * <p>This is the deliberate asymmetry with {@code WorkItemEvent}, which is internal because nothing
 * outside Work Items consumes it.
 */
public interface UserEvent {

    Instant occurredAt();
}
