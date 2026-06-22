# Self-Review Checklist

The single checklist the agent runs at Phase 4, top to bottom. **Every box must be ticked
before opening a PR.** Each section links to the detailed doc. Run the real commands; paste the
results into the PR (`06-delivery-and-pr.md`).

## 0. Spec alignment
- [ ] An approved `feature.md` exists for this work.
- [ ] Every **acceptance scenario** in the spec is implemented and has a corresponding test
      (`05-behaviour.md`, `02-testing-and-coverage.md`).
- [ ] In-scope only — nothing out-of-scope was built; no scope creep.

## 1. Build & static analysis → [`01`](./01-build-and-static-analysis.md)
- [ ] `./gradlew build` succeeds for all targets (no errors, no new warnings).
- [ ] `./gradlew spotlessCheck detekt lint` is clean.
- [ ] No new dependency added without a version-catalog entry; no flagged vulnerable deps.
- [ ] No secrets committed.

## 2. Tests & coverage → [`02`](./02-testing-and-coverage.md)
- [ ] Unit tests exist for every new/changed ViewModel and non-trivial domain/data logic.
- [ ] Failure paths from the acceptance criteria are tested (not just the happy path).
- [ ] `./gradlew test` (and relevant connected/UI tests) all pass.
- [ ] Coverage meets the threshold in `02` (≥ 80% on new/changed code; ViewModels/domain ≥ 90%).

## 3. UI → [`03`](./03-ui-verification.md)
- [ ] Loading / empty / error / success states all render and are reachable.
- [ ] Paparazzi (or screenshot) snapshots updated and reviewed; diffs intentional.
- [ ] Accessibility: content descriptions, touch targets, dynamic type, contrast.
- [ ] Dark mode, rotation, and font-scaling look correct; layouts adapt across screen sizes.

## 4. Performance → [`04`](./04-performance.md)
- [ ] No jank in lists/animations; no main-thread I/O or blocking calls.
- [ ] Image loading downsamples; caches are bounded; the NFR timings in the spec are met.
- [ ] No leaked coroutines/resources; no unbounded growth.

## 5. Behaviour → [`05`](./05-behaviour.md)
- [ ] The app does what each acceptance scenario says, on a real/emulated device.
- [ ] Permission-denied, offline, and edge cases behave gracefully (no crash, clear messaging).
- [ ] Session/logout clears tokens and user-scoped caches.

## 6. Code quality
- [ ] Follows `how/01-kotlin-style.md` and the relevant skills (data layer, MVI, DI, nav).
- [ ] Architecture/dependency rules honored; no cross-feature dependency; typed errors used.
- [ ] No dead code, no stray `TODO` without an issue, no debug logging of sensitive data.

## 7. Delivery → [`06`](./06-delivery-and-pr.md)
- [ ] Feature branch created; work split into small, conventional commits.
- [ ] PR opened with description linking the feature spec and listing checklist results.
- [ ] Self-review comments posted on non-obvious decisions and trade-offs.
- [ ] CI is green.

---

**Result:** if any box is unticked → **loop back** (see [`README`](./README.md) and
[`WORKFLOW.md`](../WORKFLOW.md) §6). If all green → proceed to deliver.
