/**
 * The Users context's published contract, and the only part of Users another context may see.
 *
 * <p>Interfaces, summaries and events. Depends on nothing: anything this package referenced would
 * become part of the contract. ArchUnit Rule 2 makes the reachability real — nothing outside
 * {@code ..users..} may touch {@code ..users.impl..}.
 *
 * <p>Since there is no shared kernel (ADR-0007), this package <em>is</em> the integration point
 * between the two contexts. A published contract with one owner is a stricter relationship than a
 * jointly owned module.
 */
package io.github.agechev.workitem.users.api;
