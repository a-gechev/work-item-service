package io.github.agechev.workitem.workitems.application;

import io.github.agechev.workitem.outbox.OutboxWriter;

/**
 * Deliberate violation of Rule 6 (Infrastructure only at the edge): a class in
 * {@code ..application..} reaching into {@code ..outbox..} directly, instead of only from
 * {@code adapters/out}. Exists only to make {@code OutboxEdgeRuleTest} fail once its rule body is
 * written. Delete this class after the red run has been observed.
 */
final class RuleSixOutboxEdgeViolation {

    private final OutboxWriter writer;

    RuleSixOutboxEdgeViolation(OutboxWriter writer) {
        this.writer = writer;
    }
}
