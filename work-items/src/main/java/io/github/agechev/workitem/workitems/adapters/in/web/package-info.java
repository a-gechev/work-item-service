/**
 * REST controllers and their request and response models.
 *
 * <p>The only package in this context that knows HTTP exists. The token is validated in {@code app};
 * the acting {@code UserId} is mapped from the {@code sub} claim here and passed inwards as an
 * explicit argument. Role comes from the Users context, never from the token.
 */
package io.github.agechev.workitem.workitems.adapters.in.web;
