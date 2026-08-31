package io.github.agechev.workitem.outbox;

import io.github.agechev.workitem.workitems.application.WorkItemEventPublisher;

/**
 * Deliberate violation of Rule 5 (No cycles): the illegitimate half of the cycle — {@code outbox}
 * reaching back into {@code workitems}, which it must never do (ADR-0007: the outbox knows nothing
 * about either context). Paired with {@code RuleFiveWorkitemsToOutboxViolation}, the legitimate
 * forward edge, to complete a real two-node cycle between the {@code workitems} and {@code outbox}
 * slices.
 *
 * <p>Test-scoped, filed under {@code architecture-tests}: {@code outbox} has no real dependency on
 * {@code workitems} to exploit, and a real placement would need a build-graph change. Exists only
 * to make {@code NoCyclesRuleTest} fail once its rule body is written. Delete this class after the
 * red run has been observed.
 */
final class RuleFiveOutboxToWorkitemsViolation {

    private final WorkItemEventPublisher publisher;

    RuleFiveOutboxToWorkitemsViolation(WorkItemEventPublisher publisher) {
        this.publisher = publisher;
    }
}
