# Project rules for Claude Code

This is a **Kotlin Multiplatform** mobile app built with **specification-driven
development**. Read and obey the specs in [`specs/`](./specs/README.md) before doing any work.

## Start here, every time

1. Read [`specs/README.md`](./specs/README.md) — the index.
2. Read [`specs/WORKFLOW.md`](./specs/WORKFLOW.md) — the mandatory 6-phase workflow.

## Non-negotiable rules

- **No code before an approved spec.** For any new feature, first create
  `specs/what-and-why/features/<feature>/feature.md` and get the user's sign-off.
  Phase 1 is a blocking gate (see `WORKFLOW.md`).
- **Follow the conventions in [`specs/how/`](./specs/how/README.md)** and the detailed
  [`skills/`](./skills) — the skills are authoritative for code-level patterns
  (data layer, MVI, Koin, navigation, testing, etc.).
- **Self-review before shipping.** Run
  [`specs/verification/self-review-checklist.md`](./specs/verification/self-review-checklist.md)
  and only open a PR once every check is green.
- **On verification failure, loop back** to the phase that owns the failure — don't paper
  over it (see `WORKFLOW.md` §6).

## Tech stack (see specs/how + skills for detail)

Clean Architecture (presentation → domain ← data) · MVI presentation · Koin DI ·
Jetpack Compose / Compose Multiplatform · Ktor client · Room · DataStore ·
Jetpack Navigation 3 (type-safe) · Coil · KotlinX Serialization · Coroutines/Flow ·
Firebase Analytics + Crashlytics · Gradle version catalogs + convention plugins ·
JUnit5 / Kotest / Turbine / ComposeTestRule / Paparazzi.
