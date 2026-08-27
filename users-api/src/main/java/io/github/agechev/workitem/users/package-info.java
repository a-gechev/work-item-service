/**
 * The Users bounded context.
 *
 * <p>Owns user identity, standing ({@code MEMBER} / {@code ADMIN}) and lifecycle. It must never
 * learn that work items exist, who is assigned to what, or whether a user may act on anything.
 *
 * <p>The context is split across two Gradle modules that share this package: {@code users.api}, the
 * published contract, and {@code users.impl}, everything behind it. This parent package is declared
 * in {@code users-api} because that is the module with a single owner. Splitting a package across
 * two modules is fine on the classpath; it would not be under the Java Platform Module System, which
 * this project does not use.
 */
package io.github.agechev.workitem.users;
