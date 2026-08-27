package io.github.agechev.workitem.workitems.application;

import io.github.agechev.workitem.workitems.domain.WorkItemEvent;

/**
 * The port through which this context publishes what has happened.
 *
 * <p>Declared here, implemented in {@code adapters.out.events} against the {@code outbox} module. The
 * application layer therefore commits the state change and the record of the event together, without
 * knowing that an outbox — or a broker, later — exists.
 *
 * <p>Each context declares its own copy of this port rather than sharing one. Two three-line
 * interfaces are cheaper than a module both contexts own; see ADR-0007.
 */
public interface WorkItemEventPublisher {

    void publish(WorkItemEvent event);
}
