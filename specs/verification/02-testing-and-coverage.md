# 02 — Testing & Coverage

**Questions: does it contain test cases? What's the test coverage?** Code-level testing
patterns are authoritative in the **`android-testing`** skill (JUnit5, Kotest, Turbine,
`UnconfinedTestDispatcher`, fakes, `ComposeTestRule`, Robot pattern, Paparazzi).

## Test cases — what must exist

Map tests back to the feature spec. **Every acceptance scenario** (`what-and-why`) should have a
test that proves it — happy path *and* every failure path.

- **ViewModels:** unit-tested with Turbine over `StateFlow`/events, using **fakes** (not mocks)
  for repositories. Cover state transitions, events, and error mapping.
- **Domain/use cases:** unit-tested where logic is non-trivial.
- **Data layer:** test mappers and any non-trivial data source/repository logic; use Ktor
  `MockEngine` for network and an in-memory/Room test DB for persistence (integration tests
  where interactions are non-trivial).
- **UI:** `ComposeTestRule` tests for critical flows and each screen state; use the **Robot
  pattern** for screens with 3+ cases or shared setup.
- **Snapshots:** Paparazzi snapshot tests for key composables/states (`03-ui-verification.md`).

Prefer fakes over mocks — they catch more real bugs. Don't test trivial getters or the
framework itself.

## Running tests

```bash
./gradlew test                 # JVM/common unit tests
./gradlew connectedCheck       # instrumented/Compose UI tests (device/emulator)
./gradlew verifyPaparazzi      # snapshot tests
```

All must pass. A flaky test is a failing test — fix or quarantine with an issue, don't ignore.

## Coverage

Measured with **Kover** (Kotlin coverage) or JaCoCo, reported in CI.

```bash
./gradlew koverHtmlReport koverVerify
```

Thresholds (on **new/changed** code; enforce via `koverVerify`):

| Scope | Minimum line/branch coverage |
|---|---|
| New/changed code overall | **≥ 80%** |
| ViewModels and domain logic | **≥ 90%** |
| Data layer (mappers, repos) | **≥ 80%** |

Coverage is a floor, not a goal — 100% coverage of trivial code proves nothing. The real bar is:
**are the acceptance scenarios and failure paths exercised?** Use coverage to find *untested
branches*, especially error handling.

## Checklist

- [ ] Every acceptance scenario has a corresponding test.
- [ ] Failure/edge paths (permission denied, offline, empty, error) are tested.
- [ ] Fakes used for repositories; `MockEngine` for network; test DB for persistence.
- [ ] `test`, `connectedCheck`, and snapshot tests all pass; no flaky tests.
- [ ] `koverVerify` passes the thresholds above.

Any failure → loop back to **IMPLEMENT** (or **SPECIFY** if a missing test reveals a missing/
ambiguous acceptance criterion).
