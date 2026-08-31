/**
 * JPA entities, repositories, and the mapping between them and the Users domain model.
 *
 * <p>The only package in this context that knows what a table is. It also owns the uniqueness
 * constraint that backs the email invariant — the rule is stated in the domain and defended in the
 * database, and both are needed.
 */
package io.github.agechev.workitem.users.impl.adapters.out.persistence;
