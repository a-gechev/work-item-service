# ADR-0008: Module-owned Spring configuration over wiring in the composition root

- **Status:** Accepted
- **Date:** 2026-08-31

## Context

Rule 2 (Context boundary) says nothing outside `..users..` may reach `..users.impl..`; a context is
reachable only through its published `api`. Writing that rule forces a question the diagram never
had to answer: something has to instantiate the implementation classes, and the obvious candidate is
the composition root in `app`.

If `app` is exempted, the exemption is not bounded by anything in the rule itself. The rule stops
saying "nothing reaches `users.impl`" and starts saying "nothing reaches `users.impl` except the one
module whose contents are defined by convention." That is a rule with a hole whose size is a matter
of taste, and rule 2's value is that it currently has none. So `app` is guarded, and the wiring has
to happen somewhere that is legally allowed to see `users.impl`.

The constraint is narrower than it first appears. Rule 2 is about **compile-time** coupling: what a
class names, and therefore what breaks when an internal signature changes. The Gradle edge
`app -> users-impl` is a classpath fact and stays — the classes must be on the classpath to be
instantiated at all. What must not exist is a line in `app` that names `UserServiceImpl`.

## Options considered

1. **Exempt `app` from rule 2.** Honest about what a composition root does, and matches how most
   Spring applications are written — explicit `@Bean` methods in a configuration class the root owns.
2. **Component scanning alone.** Implementation classes carry `@Service`, `@Repository`,
   `@RestController`; Spring instantiates them reflectively from the scanned root package. `app`
   names nothing, so rule 2 holds with no further design.
3. **Each context publishes its own `@Configuration` inside its own packages.** The wiring is
   explicit and lives where it is legally allowed to see the types it wires. `app` discovers it by
   component scan, never by name.
4. **Each context ships a Spring Boot auto-configuration.** An `@AutoConfiguration` class listed in
   the module's own `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
   This is what a Spring Boot starter does. It removes the requirement that a context's packages nest
   under the application's root package, and discovery is a declaration the module owns rather than a
   scan.

## Decision

Option 3. **Every context module owns its composition.** `io.github.agechev.workitem.users.impl.UsersConfiguration`
declares the beans of the Users context, naming impl types directly, which it may do because it
resides inside `..users..`. `UsersConfiguration` is the first instance because Users is the context
rule 2 names; Work Items and the outbox follow the same form.

`app` applies `@SpringBootApplication` at `io.github.agechev.workitem` and discovers those
configuration classes by component scan. It does not `@Import` them by class literal — that would be
a compile-time reference to a class inside `..users.impl..`. ArchUnit sees this: `@Import` is
`RUNTIME`-retained, so the class literal survives as an annotation member value in the bytecode,
which `dependOnClassesThat()` covers. It is not an access, so `accessClassesThat()` would miss it —
rule 2 must be phrased the way rule 1 already is.

The configuration class sits at the context's `impl` root, not in `domain`, `application` or an
adapter package. `domain` bans frameworks outright and `application` is declared to depend on no
framework; the config class is neither business logic nor an adapter to a specific technology, but
the module's composition surface, and its `package-info` says so.

Two build changes make this real. `users-impl` gains **`spring-context`, not a Boot starter** — the
module needs `@Configuration` and `@Bean`, and has no business booting anything:

```kotlin
implementation(platform(libs.spring.boot.bom))
implementation("org.springframework:spring-context")
```

And `app` declares `runtimeOnly(project(":users-impl"))` and `runtimeOnly(project(":work-items"))`
rather than `implementation`. The distinction this ADR draws between a classpath fact and a
compile-time reference is one Gradle can express directly, so it is expressed there: the classes are
present to be instantiated, and javac cannot see them. Rule 2 gains a second, independent enforcer
and the ArchUnit rule becomes the backstop rather than the only guard. `users-api` stays
`implementation` — `app` will name `UserId` at the security edge.

`app` therefore contains the Spring Boot entry point and configuration that is genuinely
application-wide, and no per-context wiring at all.

**What this changes in the repo:** `app`'s `package-info.java` and the module tables in `README.md`
and `CLAUDE.md` all described `app` as the module that wires implementations to ports. They are
corrected in the same commit, along with the build files above and the row in `docs/adr/README.md`.

## Consequences

**Positive**

- Rule 2 needs no exemption and no special case in its test expression. The rule a reader sees in the
  README is the rule that runs.
- Wiring is explicit and reviewable, and it travels with the module that owns it — which is what
  would have to be true anyway on the day a context is extracted into its own service.
- Declaring the module edge `runtimeOnly` means the compiler enforces the boundary too. A rule that
  two independent mechanisms enforce is harder to erode than one a single test guards.

**Negative**

- Each context module now depends on Spring for its configuration class. Rule 1 keeps that out of
  `domain`, but `users-impl` is no longer framework-free as a module.
- Discovery is by component scan, which is convention rather than declaration. Rename or move the
  package and the beans disappear silently; no compiler error results.
- The configuration class sits outside the layers rule 6 guards, so it may name `outbox` types that
  `application` may not. That is intended — wiring an adapter means naming what the adapter needs —
  but rule 6's guarded-side phrasing has a third package to account for, and its `because` string
  should say the `impl` root is deliberately outside it rather than accidentally.
- The configuration class must be public to be scanned, so `app` could name it. Only rule 2 and the
  `runtimeOnly` edge stop that.
- One more place to look when tracing where a bean comes from. `app` no longer answers that question
  by itself.

## What we gave up

Option 1 is the conventional choice and the strongest argument for it is that it is honest: a
composition root exists precisely to know about implementations, and hiding that knowledge behind
component scanning does not remove the dependency, it removes the checker's ability to see it. On
that reading, option 3 wins a green test rather than a better design.

The reason to decline is that the two dependencies are not the same dependency. `app -> users-impl`
at the classpath level is declared, one-directional and unavoidable. A `@Bean` method in `app` that
calls `new UserServiceImpl(repository, publisher)` is something else: it puts the constructor
signature of an internal class into a module every other module is downstream of, so an internal
refactor becomes an edit to the composition root.

`@Import(UsersConfiguration.class)` sits between the two, and it is worth being honest that it is
*not* the same harm: it names one class the module publishes on purpose and couples to no
constructor. It is banned anyway, and for a different reason. Rule 2 earns its keep by being a bright
line — nothing outside `..users..` names anything inside `..users.impl..`, with no cases. Admitting
`@Import` turns it into a rule with an allowance, and an allowance has to be adjudicated per class by
whoever writes the next one. The cost of the bright line is one package name that a scan resolves
instead; that is cheap enough that the exception is not worth opening.

The sharpest form of the charge is not option 1 but a variant of it:
`@ComponentScan(basePackages = "io.github.agechev.workitem.users.impl")` in `app`. ArchUnit reads
bytecode, so a package name in a string constant is invisible to it — that line would pass rule 2
while pointing straight at another context's internals. It is worth naming because it marks the limit
of what the rule can do. Rule 2 constrains what a class *names in its bytecode*, not what its author
*knows*; no static check constrains the latter. Option 3 is chosen because it needs no such string
anywhere, not because rule 2 would have caught one.

Option 4 is the mechanism Boot itself uses for this problem and the strongest technical alternative.
It was declined on semantics, not on effort: auto-configuration is designed for *optional*
configuration a consuming application may override — `@ConditionalOnMissingBean`, ordering through
`@AutoConfigureAfter`, user beans winning by default. None of that is true here. These beans are
mandatory, there is exactly one application, and there is nothing to back off from. Adopting the
starter contract for non-optional wiring would import override semantics that are dead weight and
would misdescribe what the module does. The one advantage it keeps over option 3 — a class name
listed in a file the module owns, rather than a scan — is smaller than it looks, because that name is
a string too: rename the class and the beans vanish just as silently.
