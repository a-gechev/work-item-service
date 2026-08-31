package io.github.agechev.workitem.workitems.application;

import java.security.Principal;

/**
 * Deliberate violation of Rule 4 (No ambient identity): a class in {@code ..application..}
 * reading a security context instead of taking the acting user as an explicit parameter. Exists
 * only to make {@code AmbientIdentityRuleTest} fail once its rule body is written. Delete this
 * class after the red run has been observed.
 */
final class RuleFourAmbientIdentityViolation {

    private final Principal actor;

    RuleFourAmbientIdentityViolation(Principal actor) {
        this.actor = actor;
    }
}
