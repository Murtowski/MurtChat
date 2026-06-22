# How — Code Conventions & Designs

This pillar answers **how** we build. It is the design layer between the feature spec
(*what/why*) and the actual code. Read the relevant documents here during Phase 2 (DESIGN)
and Phase 3 (IMPLEMENT).

## How this relates to `skills/`

The repository's [`skills/`](../../skills) folder holds the **code-level patterns** —
how to write a ViewModel, a data source, a Koin module, a navigation entry, a test. Those
skills are **authoritative for code-level detail.** This `how/` folder is a thinner layer
that:

- indexes which skill governs which concern,
- records **cross-cutting platform conventions** that aren't owned by a single skill
  (authentication session, image caching, storage, camera, settings, device support, security),
- and states the project-wide design rules an agent must honour.

When a topic is covered by a skill, **follow the skill** and use the `how/` doc only for the
project-specific decisions layered on top.

## Index

| Doc | Covers | Primary skills |
|---|---|---|
| [`01-kotlin-style.md`](./01-kotlin-style.md) | Kotlin style, naming, formatting, idioms, immutability, coroutines hygiene | all |
| [`02-architecture-and-modules.md`](./02-architecture-and-modules.md) | Clean Architecture, MVI, modules, DI, navigation | android-module-structure, android-presentation-mvi, android-di-koin, android-navigation, android-error-handling |
| [`03-api-and-networking.md`](./03-api-and-networking.md) | API documentation, Ktor client, **authentication session**, error mapping | android-data-layer, android-error-handling |
| [`04-data-storage-and-caching.md`](./04-data-storage-and-caching.md) | **Data caching**, **storage management**, **image download/cache (no auto-upload)**, offline-first | android-data-layer |
| [`05-device-and-platform.md`](./05-device-and-platform.md) | **Camera access**, **user settings**, permissions, **device compatibility**, expect/actual | android-module-structure |
| [`06-security.md`](./06-security.md) | **Security**, token storage, secrets, encryption, privacy | android-data-layer, observability |

## Design rules that always apply

1. **Clean Architecture direction:** `presentation → domain ← data`. Domain depends on nothing.
2. **Features never depend on features.** Share via `core:*` modules.
3. **Typed errors everywhere:** use the `Result<T, E>` / `DataError` model from the
   `android-error-handling` skill — never throw across layer boundaries for control flow.
4. **The UI is dumb:** all state and logic live in the ViewModel/domain/data, per the
   `android-compose-ui` and `android-presentation-mvi` skills.
5. **No hardcoded versions or secrets:** version catalogs for deps; `local.properties` /
   `BuildKonfig` for secrets (see `06-security.md`).
6. **Everything testable:** every data source/repository used by a ViewModel has a domain
   interface so it can be faked (see the `android-testing` skill).
