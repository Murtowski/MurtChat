# 01 — Build & Static Analysis

**Question: is the Gradle build fine?** Plus formatting, linting, and dependency hygiene.

## Build

Run and read the output — don't assume:

```bash
./gradlew clean build
```

- Must complete with **no errors** and **no new warnings**. Treat new warnings as failures.
- All KMP targets compile (Android + iOS framework, plus any others the project enables).
- `./gradlew assembleDebug` (Android) and the iOS framework link both succeed.
- Configuration-cache and build-cache friendly — no broken or non-reproducible tasks.

## Formatting & static analysis

```bash
./gradlew spotlessCheck    # or ktlintCheck — formatting
./gradlew detekt           # static analysis
./gradlew lint             # Android lint
```

- All must be **clean**. If `spotlessCheck` fails, run `spotlessApply`, review the diff, recommit.
- detekt runs against the repo's committed ruleset; do not weaken rules to pass — fix the code or
  justify a scoped suppression in the PR.
- Android `lint` issues at the configured severity (e.g. error on `FatalError`) block the build.

## Dependencies & secrets

- Every dependency resolves from the **version catalog** (`libs.versions.toml`); no hardcoded
  versions (`how/02-architecture-and-modules.md`).
- No new transitive vulnerability flagged by dependency scanning; a flagged vulnerable dependency
  is build-blocking (`how/06-security.md`).
- No secret or credential committed (`how/06-security.md`). Grep the diff for keys/tokens.

## What "fine" means

- [ ] `clean build` green, no new warnings.
- [ ] `spotlessCheck`/`detekt`/`lint` all clean.
- [ ] All deps via catalog; no flagged vulnerabilities.
- [ ] No secrets in the diff.

Any failure here → loop back to **IMPLEMENT** (or **DESIGN** if it's a structural/convention
violation).
