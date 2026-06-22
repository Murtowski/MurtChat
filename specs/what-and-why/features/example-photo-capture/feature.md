# Feature: Receipt Photo Capture

> **This is a worked example.** It shows the standard every real `feature.md` must meet.
> Imitate its structure and level of precision. Authoring rules: `../../authoring-guide.md`.

| Field | Value |
|---|---|
| **Status** | Approved (example) |
| **Owner** | Piotr |
| **Created** | 2026-06-22 |
| **Last updated** | 2026-06-22 |
| **Related skills** | android-presentation-mvi, android-data-layer, android-navigation, android-testing |
| **Related how/ docs** | how/04-data-storage-and-caching.md, how/05-device-and-platform.md, how/06-security.md |

---

## 1. Problem & business value

**Problem:** Travellers create expenses in the app but cannot attach a receipt at the moment
of spending. They keep paper receipts, lose them, and submit reports without proof — which get
rejected, generating rework for them and support tickets for finance.

**Business value / success metric:** Let users photograph and attach a receipt in under
10 seconds, at the point of expense. Success = ≥ 40% of expenses created with an attached
photo within one month of launch, and a ≥ 25% reduction in "missing receipt" support tickets.

---

## 2. User stories

### US-1: Attach a receipt while creating an expense

> As a **traveller submitting an expense**, I want to **photograph a receipt and attach it to
> the expense**, so that **I don't have to keep paper receipts and my report is accepted first
> time**.

### US-2: Fix a bad photo before saving

> As a **traveller who just took a blurry photo**, I want to **retake or remove the photo
> before saving**, so that **the attached receipt is legible and usable by finance**.

### US-3: Understand a blocked camera

> As a **first-time user who denied camera access**, I want to **understand why the app needs
> the camera and how to enable it**, so that **I'm not stuck and can complete my task**.

---

## 3. Requirements

### Functional

- **FR-1** The user can open the device camera from the expense-edit screen via an "Add receipt" action.
- **FR-2** After capture, a thumbnail of the photo is displayed, attached to the expense.
- **FR-3** The user can retake or remove an attached photo before saving the expense.
- **FR-4** The captured photo is stored locally and associated with the expense's id.
- **FR-5** If camera permission is denied, the app shows a rationale and a button that opens
  system Settings; expense editing remains usable.

### Non-functional (reference how/ docs)

- **NFR-1** Captured photos are compressed to ≤ 1.5 MB before storage (see `how/04-data-storage-and-caching.md`).
- **NFR-2** Photos are stored/cached locally but **never auto-uploaded**; upload is a separate
  explicit user action (see `how/04-data-storage-and-caching.md`).
- **NFR-3** Capture-to-thumbnail completes in < 2 s on a mid-range device (see `verification/04-performance.md`).
- **NFR-4** The local image is stored in app-private storage and is not world-readable
  (see `how/06-security.md`).

### Constraints

- **C-1** Supported on Android 8.0+ and iOS 15+ (see `how/05-device-and-platform.md`).
- **C-2** Must function with no network connection (capture + local store are offline).

---

## 4. Acceptance criteria

```
Scenario: Capture and attach a receipt (happy path)
  Given I am editing an expense and camera permission is granted
  When  I tap "Add receipt" and take a photo
  Then  a thumbnail of the photo appears attached to the expense
  And   the photo is stored in app-private storage, compressed to <= 1.5 MB
  And   the stored photo is linked to this expense's id

Scenario: Retake before saving
  Given I have just captured a receipt photo
  When  I tap "Retake"
  Then  the previous photo is discarded and the camera reopens

Scenario: Remove an attached photo
  Given I have an attached receipt photo on an unsaved expense
  When  I tap "Remove"
  Then  the thumbnail disappears and no photo is associated with the expense

Scenario: Camera permission denied
  Given camera permission is denied
  When  I tap "Add receipt"
  Then  I see an explanation of why the camera is needed
  And   I am offered a button that opens the system Settings
  And   I can continue editing the expense without the app crashing

Scenario: Photo never auto-uploads
  Given I have attached a receipt photo and I am on cellular data
  When  I save the expense
  Then  the photo is persisted locally
  And   no network upload of the photo occurs until I explicitly tap "Upload"

Scenario: Offline capture
  Given the device has no network connection
  When  I capture and attach a receipt and save the expense
  Then  the expense and its photo are saved locally without error
```

**Traceability:** US-1 → FR-1, FR-2, FR-4, NFR-1/2/4 → Scenarios 1, 5, 6 · US-2 → FR-3 →
Scenarios 2, 3 · US-3 → FR-5 → Scenario 4.

---

## 5. Scope

**In scope:** capturing a single photo per expense, compressing it, local storage, thumbnail
display, retake/remove, permission handling.

**Out of scope (explicitly):** in-app cropping/rotation, multiple photos per expense, OCR of
amounts, the upload/sync mechanism itself (separate feature), gallery import.

---

## 6. Technical notes (DESIGN phase)

- **Modules:** `feature:receipt:domain` (Receipt model, error type, `ReceiptRepository`
  interface), `feature:receipt:data` (Room entity, local data source, image compressor),
  `feature:receipt:presentation` (MVI ViewModel + Compose screen). Camera + permissions via
  `expect/actual` per `how/05`.
- **Data flow:** Compose screen → `Action` → ViewModel → `ReceiptRepository` →
  local data source → Room. State exposed as `StateFlow`; one-time camera launch via `Event`.
- **Screens / navigation:** reached from the expense-edit screen via a Nav3 destination key
  (`android-navigation` skill).

---

## 7. Open questions

- **Q-1** Max photo dimension before compression? — owner: Piotr — resolved: longest edge 2048 px.
- **Q-2** Where does upload live? — resolved: separate `offline-sync` feature, out of scope here.

---

## 8. Done checklist

- [ ] All six acceptance scenarios pass as automated tests (ViewModel + Compose UI).
- [ ] Implementation follows `how/04`, `how/05`, `how/06` and the listed skills.
- [ ] `verification/self-review-checklist.md` fully green.
- [ ] PR opened, CI green, self-review posted.
