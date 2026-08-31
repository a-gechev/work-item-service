package io.github.agechev.workitem.workitems.domain;

import io.github.agechev.workitem.workitems.application.WorkItemEventPublisher;

/**
 * Deliberate violation of Rule 1 (Layer): a domain class reaching into {@code ..application..}.
 * Exists only to make {@code LayerRuleTest} fail once its rule body is written. Delete this class
 * after the red run has been observed.
 */
final class RuleOneLayerViolation {

    private final WorkItemEventPublisher publisher;

    RuleOneLayerViolation(WorkItemEventPublisher publisher) {
        this.publisher = publisher;
    }
}
