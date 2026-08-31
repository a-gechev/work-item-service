package io.github.agechev.workitem.outbox;

import java.time.Instant;

/**
 * Takes an opaque record of what happened; knows nothing about either bounded context.
 *
 * <p>The five parameters are the record ADR-0007 describes: aggregate type, aggregate id, event
 * type, serialised payload, timestamp. No {@code OutboxEntry} or context event type appears in this
 * signature on purpose — the day domain vocabulary reaches this module, the shared kernel ADR-0007
 * removed is back under another name.
 *
 * <p>Only {@code ..adapters.out..} in either context may call this — ArchUnit Rule 6. Gradle cannot
 * say that; its dependency graph has module granularity, so the build can only say that a whole
 * module may see {@code outbox}. The test is the enforcement.
 */
public interface OutboxWriter {

    void write(String aggregateType, String aggregateId, String eventType, String payload, Instant occurredAt);
}
