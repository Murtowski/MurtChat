# 03 — UI Verification

**Question: is the UI fine?** Verify every screen state, accessibility, and adaptivity. UI
patterns are authoritative in the **`android-compose-ui`** skill.

## States — all must be reachable and correct

For each screen touched, verify every state renders and is reachable:

- **Loading** — placeholder/skeleton, no spinner-of-death; no blank flash.
- **Empty** — a clear empty state, not a blank screen.
- **Error** — the typed error mapped to a readable `UiText`, with a retry where sensible.
- **Success/content** — matches the design and the acceptance criteria.

Drive these via `ComposeTestRule`/Robot tests by feeding the corresponding MVI `State`
(`android-testing` skill) — the screen is a pure function of state, so each state is directly
testable.

## Snapshot testing

- Use **Paparazzi** snapshot tests for key composables and each significant state. Run
  `./gradlew verifyPaparazzi`; review every diff — an intended change updates the golden, an
  unintended diff is a regression.
- Snapshot in light **and** dark theme, and at a large font scale, for primary screens.

## Accessibility

- Every interactive/non-text element conveying meaning has a `contentDescription` (or is
  explicitly decorative).
- Touch targets ≥ 48 dp; sufficient color contrast.
- Content scales with **dynamic type / font scaling** without clipping or overlap.
- Logical focus/traversal order; TalkBack/VoiceOver can complete the core flow.

## Adaptivity & configuration

- Layouts adapt across phone sizes/densities (and tablet/foldable if in scope) — no fixed pixel
  layouts (`how/05-device-and-platform.md`).
- Correct under **rotation**, **dark mode**, **RTL**, and **process death** (state restored).
- No clipped text or overflow at the largest supported font scale.

## Manual visual pass

For nuances tools can't catch, do a manual pass on an emulator/device and note results in the
PR: spacing/alignment vs. design, animation smoothness, and that the happy-path flow feels right.

## Checklist

- [ ] Loading/empty/error/success all render and are reachable.
- [ ] Paparazzi snapshots updated and diffs are intentional (light + dark).
- [ ] Accessibility: descriptions, target sizes, contrast, dynamic type, traversal.
- [ ] Adapts to size/rotation/dark mode/RTL; survives process death.

Any failure → loop back to **IMPLEMENT** (or **DESIGN** if the UI structure is wrong).
