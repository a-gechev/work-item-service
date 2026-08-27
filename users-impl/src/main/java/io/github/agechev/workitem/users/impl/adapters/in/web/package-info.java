/**
 * REST controllers for admin provisioning and user lifecycle, and their request and response models.
 *
 * <p>The only package in this context that knows HTTP exists. This service validates tokens and does
 * not issue them: there is no registration, login or password endpoint here, by decision rather than
 * by omission. See ADR-0005 and {@code docs/not-doing.md}.
 */
package io.github.agechev.workitem.users.impl.adapters.in.web;
