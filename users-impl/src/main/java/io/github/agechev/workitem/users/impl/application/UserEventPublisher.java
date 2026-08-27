package io.github.agechev.workitem.users.impl.application;

import io.github.agechev.workitem.users.api.UserEvent;

/**
 * The port through which the Users context publishes what has happened.
 *
 * <p>Declared here, implemented in {@code adapters.out.events} against the {@code outbox} module, so
 * that the state change and the record of the event commit together.
 *
 * <p>Deliberately a separate interface from {@code WorkItemEventPublisher} rather than one shared
 * port. A shared port would need a module both contexts own, which is the shared kernel ADR-0007
 * removed.
 */
public interface UserEventPublisher {

    void publish(UserEvent event);
}
