package io.github.agechev.workitem.workitems.domain;

import java.time.Instant;

/**
 * A fact that has already happened inside the Work Items context.
 *
 * <p>Events are <em>returned</em> by aggregate methods and published by the application layer in the
 * same transaction as the state change they describe. Nothing in this package publishes anything.
 *
 * <p>{@code occurredAt} is declared here rather than only on each concrete event so that the
 * publisher port and the outbox adapter can write any event without knowing its type. That is the
 * whole reason this interface is not empty.
 */
public interface WorkItemEvent {

    Instant occurredAt();
}
