/**
 * Driving adapters: the ways something outside calls into the Users context.
 *
 * <p>There is no {@code events} leaf here, and that absence is the design. Users consumes no events
 * from anywhere: the dependency between the contexts runs in one direction only, which is what keeps
 * Users independently buildable and leaves the extraction path open.
 */
package io.github.agechev.workitem.users.impl.adapters.in;
