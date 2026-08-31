# Working notes for automated assistants

A modular monolith whose architectural boundaries are to be enforced by tests rather than by review.
`README.md` holds the design, `docs/adr/` holds why each choice was made and what was given up, and
`docs/not-doing.md` holds what was cut. Those three are the sources of record — with the corrections
under *Known drift* below — and this file is a summary and a working agreement.

**Where the repo actually is:** the skeleton compiles, has no behaviour, and enforces nothing yet.
The `architecture-tests` module exists and applies `archunit-junit5` from the catalog, but the only
test in it is a throwaway probe confirming the toolchain resolves — see *Unverified* below. Writing
the first six rules is the current work; until they exist and have each been seen to fail, every
boundary in this document is a convention, not a constraint.

## Build

```bash
./gradlew build
```

Java 21 toolchain, Gradle 8.14.3 wrapper. The build is the only source of truth about whether
something works — run it before reporting anything as done.

For the red/green loop, run the one test rather than the whole build:

```bash
./gradlew :architecture-tests:test --tests '*<RuleName>*'
```

## Modules, and the edges that must not move

| Module | May depend on | Note |
|---|---|---|
| `work-items` | `users-api`, `outbox` | `implementation`, never `api` |
| `users-impl` | `users-api`, `outbox` | same |
| `users-api` | **nothing** | a published contract; anything it depends on becomes part of that contract |
| `outbox` | **nothing** | takes an opaque record; a dependency here recreates the shared kernel |
| `app` | all of the above | composition root, and the only Spring-aware module |
| `architecture-tests` | all of the above | test-only, and a leaf — see below |

There is no `shared-kernel` module. One was specified in ADR-0001 and removed in ADR-0007. Do not
create one, and do not add a dependency that would make one necessary — raise it instead.

`architecture-tests` is the exception that proves that rule, and the distinction is worth being able
to state: it depends on every module, but **nothing depends on it**. It is a leaf, it contains no
domain types, and it ships in no artifact. A shared kernel is a module that others depend *on*, which
is what makes it jointly owned and hard to change. Inverting the arrow removes the coupling entirely.
The rules need a vantage point that can see the whole graph; that is the only thing this module is
for.

## Packages

```
io.github.agechev.workitem
├── workitems              Work Items context
│   ├── domain             no framework, no infrastructure, no outward dependency at all
│   ├── application        use cases and ports; may see domain only
│   └── adapters/{in/{web,events}, out/{persistence,events}}
├── users
│   ├── api                the only part of Users that anything else may reference
│   └── impl               domain, application, adapters/{in/web, out/{persistence,events}}
└── outbox                 reachable only from ..adapters.out..
```

Users has no `adapters/in/events` on purpose — it consumes no events; the dependency between contexts
runs one way only. Every package has a `package-info.java` (23 of them) stating what belongs in it
**and what may not**. Read it before adding a class there — they are the rules drafted in prose, and
the test names should map onto them.

Those files are documentation and nothing more: javac emits `package-info.class` only when the
package declaration carries an annotation, so a javadoc-only `package-info.java` compiles to nothing
and does **not** put an empty package on the classpath. That is the whole reason for the class-count
assertion below.

## The six rules

**`README.md`, under "The six rules", is the single source of truth. They are not restated here.**

Refer to them by number. The numbering is stable and is cited from the README, from the
`package-info.java` of each package a rule protects, and from the test that enforces it —
renumbering means editing all three.

Two things govern how they are written:

- **A rule that has never been seen to fail is a comment.** Commit each rule with a deliberate
  violation, watch the build go red, then delete the violation. Six rules, six red runs, no
  exceptions.
- **Phrase every rule from the guarded side, not the protected target.**
  `noClasses().that().resideInAnyPackage("..domain..", "..application..")
  .should().dependOnClassesThat().resideInAPackage("..outbox..")` checks 4 classes today;
  `classes().that().resideInAPackage("..outbox..").should().onlyBeAccessed()...` checks none,
  because `outbox` has no compiled class. Same rule, and only one of them is evidence.
  `failOnEmptyShould` stays at its default — a rule that checks nothing should go red.
  **Rule 5 names its slices instead of counting them:** the slice pattern is scoped explicitly
  to `workitems`, `users` and `outbox` rather than a wildcard, because a wildcard also catches
  `architecture-tests`'s own test classes under `..architecture..` as a spurious extra slice.
  Naming the three replaces the count-based workaround this file used to describe here — written
  when `outbox` held no compiled class, so the count was 2 rather than 3. `outbox` now holds
  `OutboxWriter` (Rule 6), so there is no longer a count to hardcode or a slice to wait on.

**Verified:** ArchUnit 1.5.0 resolves and runs against JUnit 6.1.3 here — confirmed by a throwaway
probe rule in `architecture-tests`, no catalog change needed. That probe is not one of the six and
carries no numbering; it stays only until the first real rule makes it redundant.

## Non-negotiables

- Domain events are **returned** by aggregate methods, never published from inside one.
- No ambient identity. The acting `UserId` is an explicit parameter; nothing in `domain` or
  `application` reads a security context.
- Scope is frozen. Something that looks missing was probably cut on purpose — check
  `docs/not-doing.md` first, and add an entry there rather than widening scope.
- Every pattern must survive "why?" three times, or be cut. Deleting a layer beats adding one.

## Working agreement

**The domain model and the architecture rules are written by hand by the repository author.** An
ArchUnit rule is both a test and a rule, so the line runs through the middle of the test file:

| Assistant | Author |
|---|---|
| The `architecture-tests` module, its build file, the test convention, the catalog wiring | The rule expressions themselves |
| Test class shells with named, empty test methods | What each rule asserts, and its name |
| The deliberate violation classes, written to a stated design | Whether a violation is the right one |
| Running the build and explaining failures | Deciding what a failure means |
| Build files, boilerplate, documentation written to a specified structure | The domain model, entire |

Propose before writing anything that changes a module edge, adds a dependency, or introduces a
pattern. A version-catalog entry is a decision, not a detail.

## Known drift in the documents

Parked deliberately for the Week 5 legibility pass. Do not fix them mid-track, and do not trust them:

- The README says boundaries are "enforced by ArchUnit tests that fail the build". Not yet true.
- The README tech stack says **Spring Boot 3.x**; the catalog pins **4.1.1**, with reasons.
- The README's Running section promises `docker compose up`; there is no compose file.
- The README module diagram shows no inbound web adapter for `users-impl`, though
  `users.impl.adapters.in.web` exists.
- ADRs 0002, 0003, 0004 and 0006 are `Status: Proposed` with placeholder dates. Accepted ADRs (0001,
  0005, 0007) are binding; proposed ones record a question, not a decision.

## Commits

Conventional prefixes: `feat`, `docs`, `build`, `chore`, and `test` for the rules. One concern per
commit — structure, documentation and build changes go separately.
