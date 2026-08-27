/**
 * Everything behind the Users contract: domain, use cases and adapters.
 *
 * <p>Nothing outside {@code ..users..} may reference this package or anything under it — ArchUnit
 * Rule 2. The {@code impl} segment is not decoration; it is the thing the rule matches on.
 *
 * <p>Layered hexagonally, on the same terms as Work Items.
 */
package io.github.agechev.workitem.users.impl;
