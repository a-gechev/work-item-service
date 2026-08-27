/**
 * Listeners for events published by other contexts.
 *
 * <p>Today that is {@code UserDeactivated} from Users, which unassigns that user's open items —
 * the eventual half of a rule whose synchronous half is the assignment invariant in the domain.
 *
 * <p>Delivery is at-least-once, so a listener here must be idempotent: seeing the same event twice
 * must be indistinguishable from seeing it once. See ADR-0004.
 */
package io.github.agechev.workitem.workitems.adapters.in.events;
