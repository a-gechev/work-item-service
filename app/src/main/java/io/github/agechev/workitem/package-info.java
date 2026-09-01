/**
 * The composition root.
 *
 * <p>The only module that knows the whole dependency graph. It does not wire implementations to
 * ports itself: each context publishes its own {@code @Configuration} inside its own packages
 * (ADR-0008), and this module discovers them by component scan, never by naming a type across a
 * context boundary. The contexts themselves never learn which implementation they were given, or
 * that HTTP and tokens exist.
 *
 * <p>The bounded contexts live in the packages below this one, one per context, alongside the
 * {@code outbox} they publish through.
 */
package io.github.agechev.workitem;
