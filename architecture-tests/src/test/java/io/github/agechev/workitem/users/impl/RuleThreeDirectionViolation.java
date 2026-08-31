package io.github.agechev.workitem.users.impl;

import io.github.agechev.workitem.workitems.domain.WorkItemEvent;

/**
 * Deliberate violation of Rule 3 (Direction): a class under {@code ..users..} reaching into
 * {@code ..workitems..}. Exists only to make {@code DirectionRuleTest} fail once its rule body is
 * written.
 *
 * <p>Filed under {@code architecture-tests} rather than a real {@code users} module: today,
 * neither {@code users-api} nor {@code users-impl} has {@code work-items} on its compile
 * classpath, so this dependency cannot be produced from real production source without a
 * build-graph change. This is a split-package stand-in, scoped to the one module built to not be
 * real production code. Delete this class after the red run has been observed.
 */
final class RuleThreeDirectionViolation {

    private final WorkItemEvent event;

    RuleThreeDirectionViolation(WorkItemEvent event) {
        this.event = event;
    }
}
