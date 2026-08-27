/**
 * The transactional outbox.
 *
 * <p>Takes an opaque record — aggregate type, aggregate id, event type, payload, timestamp — and
 * knows nothing about either bounded context. The day this module depends on one of them, the shared
 * kernel is back; see ADR-0007.
 *
 * <p>It exists because recording that something happened and acting on it have to be separately
 * committable. Sending mail inside the state-changing transaction fails either way round: the
 * transaction rolls back after the mail has gone, or a mail failure takes a valid state change down
 * with it.
 *
 * <p>Only {@code ..adapters.out..} may depend on this package — ArchUnit Rule 6. Gradle cannot
 * express that: its dependency graph has module granularity, so the build can only say that
 * {@code work-items} as a whole may see {@code outbox}. The test is the enforcement.
 */
package io.github.agechev.workitem.outbox;
