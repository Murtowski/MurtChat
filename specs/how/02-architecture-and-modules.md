# 02 — Architecture & Modules

This document is the **map**. The detailed rules live in skills; this records the project's
architectural decisions and points you to the authoritative skill for each layer.

## Architecture: Clean Architecture + MVI

```
        ┌─────────────────────────┐
        │     presentation        │  Compose UI + MVI ViewModel
        │  (State, Action, Event) │
        └───────────┬─────────────┘
                    │ depends on
        ┌───────────▼─────────────┐
        │        domain           │  Pure Kotlin: models, interfaces, errors, use cases
        │   (no framework deps)   │
        └───────────▲─────────────┘
                    │ depends on
        ┌───────────┴─────────────┐
        │         data            │  Repositories/data sources, DTOs, Room, Ktor, mappers
        └─────────────────────────┘
```

- **Direction:** `presentation → domain ← data`. Domain is innermost and depends on nothing
  but `core:domain`.
- **Presentation = MVI:** every screen has a single `State`, a sealed `Action`, a sealed
  `Event`, and a ViewModel exposing `StateFlow<State>` + a `Channel` of events. **Authoritative
  skill: `android-presentation-mvi`.** Compose specifics: **`android-compose-ui`.**
- **Domain:** models, repository/data-source **interfaces**, typed errors, and use cases when
  logic is non-trivial. Every dependency a ViewModel uses has a domain interface so it can be
  faked in tests.
- **Data:** implementations, DTOs, Room entities, mappers. **Authoritative skill:
  `android-data-layer`.**

## Modules

Feature-layered modularization — split by feature first, then by layer. **Authoritative skill:
`android-module-structure`.** Summary:

```
:app                            ← wires everything; the only module that knows all others
:build-logic                    ← Gradle convention plugins
:core:domain                    ← shared models, Result, Error types, shared interfaces
:core:data                      ← Ktor HttpClient factory, shared DB/data utilities
:core:presentation              ← ObserveAsEvents, UiText, shared UI logic
:core:design-system             ← Compose theme, colors, typography, reusable components
:core:database                  ← (optional) shared Room @Database, entities, DAOs, migrations
:core:analytics                 ← (optional) Firebase wrapper (observability skill)
:feature:<name>:domain
:feature:<name>:data
:feature:<name>:presentation
```

For navigation, features split into `:feature:<name>:api` (NavKeys) and `:impl`
(screens/ViewModels) — **authoritative skill: `android-navigation`.**

### Dependency rules (enforced)

| Layer | May depend on |
|---|---|
| `presentation` | own `domain`, `core:domain`, `core:presentation`, `core:design-system` |
| `data` | own `domain`, `core:domain`, `core:data` |
| `domain` | `core:domain` only — never `data` or `presentation` |
| `:app` | everything |

**Features never depend on other features.** Shared code goes to the right `core:*` module.

## Dependency injection — Koin

One Koin module per feature layer, assembled in `:app`; ViewModels injected with
`koinViewModel()`. **Authoritative skill: `android-di-koin`.**

## Error handling

Project-wide `Result<T, E>`, `DataError`, `EmptyResult`, and the `map`/`onSuccess`/`onFailure`
helpers. **Authoritative skill: `android-error-handling`.** Errors are typed and propagated;
the presentation layer maps them to `UiText` (`android-presentation-mvi`).

## Gradle & build

- **Version catalogs** (`libs.versions.toml`) for all versions — no hardcoded versions.
- **Convention plugins** in `:build-logic` for every non-trivial config (see the
  `android-module-structure` skill for the plugin list).
- Secrets via `local.properties` + `BuildConfig`/`BuildKonfig` (see `06-security.md`).

## KMP source sets

- `commonMain` holds everything shareable: domain, data logic, ViewModels, and (with Compose
  Multiplatform) shared UI.
- `androidMain` / `iosMain` hold platform implementations behind `expect`/`actual`
  (camera, permissions, secure storage, image pickers — see `05-device-and-platform.md`).
- Keep `expect`/`actual` surfaces small and behind a domain interface so common code and tests
  stay platform-agnostic.

## Adding a feature — module checklist

- [ ] Create `:feature:<name>:domain|data|presentation` (and `:api`/`:impl` if it navigates).
- [ ] Apply the right convention plugins.
- [ ] Define domain interfaces before implementations.
- [ ] No cross-feature dependency introduced.
- [ ] Shared logic promoted to a `core:*` module.
- [ ] Koin module added and assembled in `:app`.
