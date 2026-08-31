package io.github.agechev.workitem;

import io.github.agechev.workitem.users.impl.application.UserEventPublisher;

/**
 * Deliberate violation of Rule 2 (Context boundary): a class outside {@code ..users..} reaching
 * into {@code ..users.impl..} directly instead of through {@code users.api}. Exists only to make
 * {@code ContextBoundaryRuleTest} fail once its rule body is written. Delete this class after the
 * red run has been observed.
 */
final class RuleTwoContextBoundaryViolation {

    private final UserEventPublisher publisher;

    RuleTwoContextBoundaryViolation(UserEventPublisher publisher) {
        this.publisher = publisher;
    }
}
