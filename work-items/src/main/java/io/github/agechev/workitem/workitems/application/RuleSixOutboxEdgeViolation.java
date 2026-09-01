package io.github.agechev.workitem.workitems.application;

import io.github.agechev.workitem.outbox.OutboxWriter;

/**
 * Deliberate violation of Rule 6 (Infrastructure only at the edge): a class in
 * {@code ..application..} reaching into {@code ..outbox..} directly, instead of only from
 * {@code adapters/out}. Exists only to give the new CI workflow something to fail on for the
 * PR-check demo. Delete this class once the red run has been observed and screenshotted.
 */
final class RuleSixOutboxEdgeViolation {

    private final OutboxWriter writer;

    RuleSixOutboxEdgeViolation(OutboxWriter writer) {
        this.writer = writer;
    }
}
