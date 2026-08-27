/**
 * Work Items use cases, and the ports they need.
 *
 * <p>Orchestrates the domain: load the aggregate, call it, persist the result, publish the events it
 * returned — all in one transaction. May depend on {@code domain}; may not depend on
 * {@code adapters}, on any framework, or on the {@code outbox} module. The acting user arrives as an
 * explicit parameter, because nothing here may read a security context.
 */
package io.github.agechev.workitem.workitems.application;
