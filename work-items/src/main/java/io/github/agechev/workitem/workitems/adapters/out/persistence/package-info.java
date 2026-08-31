/**
 * JPA entities, Spring Data repositories, and the mapping between them and the domain model.
 *
 * <p>The only package in this context that knows what a table is. The domain model is not the
 * persistence model; where they diverge, this package absorbs the difference rather than leaking an
 * annotation inwards.
 */
package io.github.agechev.workitem.workitems.adapters.out.persistence;
