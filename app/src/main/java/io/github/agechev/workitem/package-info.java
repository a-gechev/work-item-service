/**
 * The composition root.
 *
 * <p>The only module that knows the whole dependency graph, and the only place Spring is allowed to
 * appear outside an adapter. It wires implementations to the ports the contexts declare; the
 * contexts themselves never learn which implementation they were given, or that HTTP and tokens
 * exist.
 *
 * <p>The bounded contexts live in the packages below this one, one per context, alongside the
 * {@code outbox} they publish through.
 */
package io.github.agechev.workitem;
