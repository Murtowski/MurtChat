# 01 — Kotlin Code Style

The baseline is the **official Kotlin coding conventions** and the **Android Kotlin style
guide**, enforced automatically. This document records the project-specific rules and the
ones worth calling out. Style is enforced by tooling (see `verification/01-build-and-static-analysis.md`),
not by reviewer memory.

## Formatting & tooling

- **Formatter:** ktlint (via Spotless or the ktlint Gradle plugin), official Kotlin style.
- **Static analysis:** detekt, with the agreed ruleset checked into the repo.
- **Indent** 4 spaces, no tabs. **Max line length** 120.
- `./gradlew spotlessApply detekt` must be clean before any commit. CI fails on violations.
- Configure `editorconfig` so IDE and CLI agree.

## Naming

| Element | Convention | Example |
|---|---|---|
| Class / interface / object | UpperCamelCase | `ReceiptRepository` |
| Function / property / parameter | lowerCamelCase | `loadReceipts()` |
| Constant (`const`/top-level `val`) | UPPER_SNAKE_CASE | `MAX_IMAGE_BYTES` |
| Composable function | UpperCamelCase, noun-ish | `ReceiptListScreen` |
| Implementation class | named for what makes it unique, **never** `*Impl` | `RoomReceiptDataSource` |
| Test function | backtick sentence | `` `denied permission emits rationale event` `` |
| Module | kebab-case path | `:feature:receipt:data` |

- No Hungarian notation, no `m`/`s` prefixes.
- Boolean names read as predicates: `isLoading`, `hasReceipt`, `canRetake`.
- Don't suffix interfaces with `I` or impls with `Impl` — see the `android-data-layer` skill.

## Language idioms

- **Immutability first:** prefer `val`; use `data class` for state; expose read-only
  collections (`List`, not `MutableList`) across boundaries.
- **Expression bodies** for one-liners; block bodies when there's branching.
- **Exhaustive `when`** on sealed types — no `else` branch, so new cases force a compile error.
- **Sealed interfaces/classes** for closed sets (UI `Action`, `Event`, error types).
- **Null safety:** avoid `!!`. Model absence with nullable types or sealed states, not
  sentinel values. Use `?.`, `?:`, `requireNotNull` with a message at trust boundaries.
- **Scope functions:** use `let`/`run`/`apply`/`also`/`with` for readability, not to build
  deep nesting. If it's hard to read, extract a function.
- **Extension functions** for mappers and small helpers (e.g. `fun ReceiptDto.toReceipt()`),
  kept next to the type they extend, per the `android-data-layer` skill.
- **Data/UI separation:** never let a DTO or Room entity cross into domain or UI; map at the
  boundary (`android-data-layer` skill).

## Coroutines & Flow hygiene

- **Structured concurrency:** launch from a scoped owner (`viewModelScope`); never `GlobalScope`.
- **Suspend, don't block:** no blocking calls on the main dispatcher. Inject a
  `CoroutineDispatcher` only when a class dispatches off-main *and* is unit-tested
  (see the `android-testing` skill).
- **Expose `StateFlow` for state and a `Channel`/`Flow` for one-time events**, per the
  `android-presentation-mvi` skill — never expose `MutableStateFlow`.
- **Cancellation-aware:** respect `isActive`; don't swallow `CancellationException`.
- Use `Result<T, E>` for expected failures; reserve exceptions for truly exceptional cases.

## Comments & docs

- Code should read without comments; comment **why**, not **what**.
- KDoc public APIs of `core:*` modules and any non-obvious domain rule.
- TODOs must reference a tracking issue: `// TODO(#123): ...`.

## Files & structure

- One top-level declaration per file in general; group a sealed hierarchy in one file.
- File name matches the primary declaration.
- Package by feature-then-layer, mirroring the module path (`android-module-structure` skill).
