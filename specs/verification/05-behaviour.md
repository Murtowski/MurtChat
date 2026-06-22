# 05 — Behaviour

**Question: does the app behave correctly?** This is the end-to-end check that the running app
does what the **acceptance criteria** in the feature spec say — including every failure path.

## Drive verification from the acceptance criteria

The feature spec's Given/When/Then scenarios are the script. For each scenario:

1. Set up the **Given** (state, permissions, connectivity).
2. Perform the **When** (the user action).
3. Confirm the **Then** (the observable outcome) on a real or emulated device.

Prefer automating each scenario as a Compose/E2E test (`02-testing-and-coverage.md`,
`android-testing` Robot pattern). For anything not automated, do it manually and record the
result in the PR.

## Cross-cutting behaviours to confirm

- **Permissions:** granted, denied, and permanently-denied paths all behave per
  `how/05-device-and-platform.md` — rationale shown, Settings deep-link offered, no crash.
- **Connectivity:** offline and flaky-network behaviour is graceful; offline-first reads work;
  errors map to readable `UiText` (`how/03`, `how/04`).
- **Session:** login establishes the session; logout/expiry clears tokens **and** user-scoped
  caches and routes to auth (`how/03`, `how/06`).
- **Images:** captured/cached images stay local and are **never auto-uploaded** (`how/04`).
- **Lifecycle:** rotation, backgrounding, and process death preserve the right state; one-time
  events (navigation, snackbars) don't re-fire after recreation (`android-presentation-mvi`).
- **Navigation:** back stack behaves; deep links (if any) validate parameters (`how/06`).
- **Edge cases:** empty input, very large input, rapid taps/double submission, cancellation.

## No regressions

- Run the existing test suite; confirm related existing flows still work.
- Confirm no analytics/crash regressions and that consent is still honored (`how/06`).

## Checklist

- [ ] Every acceptance scenario verified (automated where feasible, else manual + recorded).
- [ ] Permission-denied / offline / error paths behave gracefully, no crash.
- [ ] Session, image-no-upload, and lifecycle behaviours all correct.
- [ ] Edge cases handled; no regressions in existing flows.

Any failure → loop back: wrong outcome vs. spec → **SPECIFY** then **IMPLEMENT**; correct intent
but buggy → **IMPLEMENT**.
