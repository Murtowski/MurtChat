# 04 — Performance

**Question: what's the performance?** Verify the app is responsive and resource-thrifty, and
that the NFR timings written in the feature spec are met.

## Responsiveness

- **No main-thread I/O or blocking calls.** All disk/DB/network work is on `Dispatchers.IO`
  (inject the dispatcher where the class is unit-tested — `android-testing` skill).
- **Smooth scrolling/animation (target 60 fps).** `LazyColumn`/`LazyRow` use stable `key`s and
  stable item types; avoid unnecessary recomposition (`android-compose-ui` skill — stability,
  `@Stable`, `derivedStateOf`, avoiding lambda allocation in hot paths).
- **No jank** on the critical flow. Inspect recomposition counts in debug; check the macro/micro
  flow with the Android Studio profiler or a Macrobenchmark for hot screens where it matters.

## Startup & memory

- App startup isn't blocked by heavy work; defer non-critical init.
- **Images downsample** to display size via Coil; memory and disk caches are **bounded**
  (`how/04-data-storage-and-caching.md`). No full-res decode into small views.
- No memory leaks: no leaked `Activity`/`Context`, no coroutine outliving its scope; collectors
  are lifecycle-aware.

## Data & network efficiency

- Reads come from the Room/DataStore cache where the offline-first pattern applies; no redundant
  network calls (`how/04`).
- Pagination for large lists; no loading an entire dataset into memory.
- Caches have eviction policies; storage usage stays within budget (`how/04`).

## Meeting the spec's NFRs

- Each performance NFR in the feature spec (e.g. "capture-to-thumbnail < 2 s") is measured and
  met. Record the measurement (device + number) in the PR.

## How to verify

- Build a profileable release-like build; use the **Android Studio Profiler** (CPU, memory) and
  **Layout Inspector**/recomposition counts. Add a **Macrobenchmark** for any flow with a strict
  NFR.
- Watch Logcat for skipped frames / ANR warnings during the core flow.

## Checklist

- [ ] No main-thread I/O or blocking calls.
- [ ] Lists/animations are smooth; recomposition is controlled.
- [ ] Images downsample; memory/disk caches bounded; no leaks.
- [ ] Spec performance NFRs measured and met (numbers recorded in the PR).

Any failure → loop back to **IMPLEMENT** (or **DESIGN** if the approach is structurally slow).
