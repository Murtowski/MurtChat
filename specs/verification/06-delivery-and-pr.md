# 06 — Delivery: Branch, Commits, PR & Self-Review

Phase 5. Run **only after** [`self-review-checklist.md`](./self-review-checklist.md) is fully
green. This turns finished, verified work into a reviewable pull request.

## 1. Feature branch

Branch off the default branch (e.g. `main`) with a conventional name:

```
feature/<feature-name>        # e.g. feature/receipt-photo-capture
fix/<short-description>        # for bug fixes
chore/<short-description>      # tooling/build/non-feature
```

- One feature per branch. Keep it rebased on the latest default branch.
- Branch name matches the feature folder under `what-and-why/features/`.

## 2. Digestible commits

Split the work into **small, logical, self-contained commits** — each one builds and tells one
part of the story. Don't dump the whole feature in a single commit.

Use **Conventional Commits**:

```
<type>(<scope>): <imperative summary>

<body: what & why, not how>

Refs: what-and-why/features/<feature>/feature.md
```

- `type` ∈ `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `perf`, `build`.
- `scope` = the feature/module (e.g. `receipt`, `core-data`).
- Suggested ordering, one commit each: domain models/interfaces → data layer (+tests) →
  presentation/ViewModel (+tests) → UI/Compose (+snapshots) → DI wiring → navigation.
- Every commit compiles and its tests pass — so the PR is reviewable commit by commit.
- Reference the feature spec in the body for traceability.

## 3. Open the pull request

PR description must include:

- **Summary** — what and why, linking the `feature.md`.
- **Acceptance criteria** — the scenario list with ✅/⬜ status.
- **Self-review results** — paste the outcome of each `self-review-checklist.md` section
  (build, tests + coverage %, UI/snapshots, performance numbers, behaviour).
- **Screenshots / screen recording** for UI changes (light + dark).
- **Out of scope / follow-ups** — anything deliberately deferred.
- Link the issue/ticket if one exists.

Keep PRs small. If the diff is large, consider stacking PRs per layer.

## 4. Self-review (comment your own PR)

Before requesting human review, **read your own diff in the PR UI and leave comments** — this is
a required step, not optional. Comment on:

- **Non-obvious decisions** and trade-offs ("used a repository here because two sources are
  coordinated — see `android-data-layer`").
- **Deviations** from a `how/` doc or skill, with justification.
- **Risk areas** the reviewer should focus on (concurrency, migrations, permissions).
- **Known limitations / follow-ups**, each linked to an issue.
- Anything you'd ask a teammate about — answer it pre-emptively.

Then re-read the diff as a critic: dead code, leftover logs, missing tests, naming, error
handling. Fix what you find before requesting review.

## 5. CI gate

CI runs the same checks as the self-review (build, static analysis, tests, coverage, snapshots).
**CI must be green.** A red CI is a blocker — fix and push; never merge over red.

## Checklist

- [ ] `feature/<name>` branch matching the spec folder.
- [ ] Small conventional commits; each builds and passes tests; spec referenced.
- [ ] PR opened with summary, AC status, self-review results, and screenshots.
- [ ] Self-review comments posted on non-obvious choices, deviations, and follow-ups.
- [ ] CI green.

When this is done, the feature meets the **Definition of Done** in
[`WORKFLOW.md`](../WORKFLOW.md).
