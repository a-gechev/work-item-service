package io.github.agechev.workitem.users.impl.adapters.out.events;

import io.github.agechev.workitem.outbox.OutboxWriter;

/**
 * Deliberate violation of Rule 5 (No cycles): this half is the legitimate shape an outbound
 * adapter will eventually have — real production code, depending on the real {@code OutboxWriter}.
 * It becomes a cycle only when paired with {@code RuleFiveOutboxToUsersViolation}, the illegitimate
 * reverse edge. Exists only to make {@code NoCyclesRuleTest} fail once its rule body is written.
 * Delete this class after the red run has been observed.
 */
final class RuleFiveUsersToOutboxViolation {

    private final OutboxWriter writer;

    RuleFiveUsersToOutboxViolation(OutboxWriter writer) {
        this.writer = writer;
    }
}
