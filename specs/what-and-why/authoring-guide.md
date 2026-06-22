# Authoring Guide — How to Write a Feature Spec

This is the **instruction set for the agent** on how to produce a feature specification
that is correct, testable, and tied to a real business need. Follow it whenever you create
or revise a file under `features/`.

A good feature spec makes three things unambiguous:

1. **Why** we are building this (the problem and its value).
2. **What** the user can do once it exists (user stories).
3. **When** we can call it done (requirements + acceptance criteria).

> **Golden rule:** every requirement traces to a user story, and every user story traces to
> a business reason. If a requirement serves no story, cut it. If a story serves no business
> reason, question it.

---

## 1. Start with the problem and the business value

Before features, write the **problem statement**: who has the problem, what the problem is,
and what it costs them today. Then state the **business value**: what success looks like and,
where possible, how it is measured.

Write it as prose, plainly:

> *Travellers using our app currently cannot capture a receipt photo at the point of expense,
> so they lose receipts and abandon expense reports. We will let them photograph and attach a
> receipt in under 10 seconds. Success = 40% of expenses created with an attached photo within
> one month, and a measurable drop in "missing receipt" support tickets.*

If you cannot state the value, you are not ready to write requirements. Ask the user.

---

## 2. User stories — what should be contained

A user story describes a capability from the **user's** point of view. Use the canonical form:

```
As a <type of user>,
I want <to perform some action / achieve some goal>,
so that <I get some benefit / business value>.
```

The **"so that"** clause is mandatory — it is the link back to value. A story without a
benefit is a feature looking for a justification.

Each user story must contain:

- **A role** — a specific user type (`first-time user`, `returning traveller`, `admin`), not
  a vague "user". Different roles have different needs.
- **A goal** — the concrete thing they want to accomplish, phrased as an outcome, not a UI
  mechanism. ("attach a receipt", not "tap the camera button").
- **A benefit** — why they want it; the value they get.
- **Acceptance criteria** — see §4. Criteria are attached *to the story*, not floating loose.

Keep stories **INVEST**:

| Letter | Means | Check |
|---|---|---|
| **I**ndependent | Can be built without depending on another unfinished story. |
| **N**egotiable | Describes intent, not a rigid implementation. |
| **V**aluable | Delivers value to a user or the business. |
| **E**stimable | Small enough that you can reason about effort. |
| **S**mall | Fits comfortably in one feature; split if it sprawls. |
| **T**estable | You can write a pass/fail check for it (the acceptance criteria). |

**Good story:**
> As a traveller submitting an expense, I want to photograph a receipt and attach it to the
> expense, so that I don't have to keep paper receipts and my report is accepted first time.

**Poor story (no role, no benefit, describes UI):**
> User taps the camera icon and a photo is saved.

Split large stories. "Manage receipts" is an epic; "capture a receipt", "view a receipt",
"delete a receipt" are stories.

---

## 3. Requirements — what should be contained

Requirements are the **specific, verifiable statements** of what the system must do or honour.
Group them into:

- **Functional requirements** — what the system *does* (behaviour, rules, data).
- **Non-functional requirements (NFRs)** — qualities it must have: performance, security,
  accessibility, offline behaviour, device support. Reference the relevant `how/` docs rather
  than re-specifying them (e.g. "encrypt the cached image per `how/06-security.md`").
- **Constraints** — platform, regulatory, or dependency limits ("must work on Android 8+ and
  iOS 15+", "must not upload over cellular without consent").

Write requirements so each is:

- **Atomic** — one testable statement per requirement. Split "and"s.
- **Unambiguous** — no "fast", "easy", "intuitive". Quantify: "thumbnail appears within 300 ms".
- **Verifiable** — there is an objective way to check it (this is what acceptance criteria do).
- **Traceable** — give each an ID (`FR-1`, `NFR-1`) so acceptance criteria and commits can
  reference it.
- **Implementation-agnostic** — say *what*, not *how*. The "how" belongs in the `how/` pillar.

### Example requirements (for the receipt-capture feature)

> **FR-1** The user can open the device camera from the expense-edit screen.
> **FR-2** After capture, a thumbnail of the photo is shown attached to the expense.
> **FR-3** The user can retake or remove an attached photo before saving the expense.
> **FR-4** Photos are stored locally and associated with the expense's id.
> **FR-5** If the camera permission is denied, the app shows an explanation and a path to
>         Settings; it never crashes or silently fails.
> **NFR-1** A captured photo is compressed to ≤ 1.5 MB before storage (see `how/04-data-storage-and-caching.md`).
> **NFR-2** Captured images are downloaded/cached but **never auto-uploaded**; upload is a
>          separate, explicit user action (see `how/04`).
> **NFR-3** The capture-to-thumbnail flow completes in < 2 s on a mid-range device
>          (see `verification/04-performance.md`).
> **C-1** Supported on Android 8.0+ and iOS 15+ (see `how/05-device-and-platform.md`).

Note how each requirement is testable, references the conventions for the "how", and avoids
prescribing implementation.

---

## 4. Acceptance criteria — how to write them

Acceptance criteria (AC) define **the conditions under which a story is accepted as done**.
They are the contract between intent and implementation, and they become your tests.

Use the **Given / When / Then** (Gherkin) format — one scenario per behaviour:

```
Scenario: <short name>
  Given <the starting context / preconditions>
  When  <the action the user takes>
  Then  <the observable, verifiable outcome>
```

Rules for writing AC:

- **Cover the happy path first**, then the important alternatives and every failure path
  (permission denied, no network, empty state, large input, cancellation).
- **Be observable.** "Then" must describe something a user or a test can see — UI state, a
  stored value, a navigation, an emitted event — never an internal implementation detail.
- **One outcome per scenario.** If you need "and also", it's usually a second scenario.
- **Quantify** timings, sizes, and limits; reuse the numbers from the requirements.
- **Make them map to tests.** Each scenario should be realisable as a ViewModel unit test,
  a Compose UI test, or an integration test (see the `android-testing` skill and
  `verification/02-testing-and-coverage.md`).

### Worked acceptance criteria (receipt capture)

```
Scenario: Capture and attach a receipt (happy path)
  Given I am editing an expense and camera permission is granted
  When  I tap "Add receipt" and take a photo
  Then  a thumbnail of the photo appears attached to the expense
  And   the photo is stored locally and linked to this expense's id

Scenario: Retake before saving
  Given I have just captured a receipt photo
  When  I tap "Retake"
  Then  the previous photo is discarded and the camera reopens

Scenario: Camera permission denied
  Given camera permission is denied
  When  I tap "Add receipt"
  Then  I see an explanation of why the permission is needed
  And   I am offered a button that opens the system Settings
  And   the app remains usable (no crash, expense editing continues)

Scenario: Photo never auto-uploads
  Given I have attached a receipt photo and I am on cellular data
  When  I save the expense
  Then  the photo is persisted locally
  And   no network upload of the photo occurs until I explicitly choose to upload
```

A story is **not done** until every one of its acceptance scenarios passes as an automated
test (where feasible) or a documented manual check (where not).

---

## 5. Scope — say what's out, too

Explicitly list what is **out of scope** for this feature. This prevents scope creep and
sets the boundary for verification. "Editing/cropping the photo" and "OCR of receipt totals"
might be out of scope for v1 — say so.

---

## 6. Open questions

Capture every unknown as a numbered open question with an owner. Do **not** guess past an
open question that changes behaviour — ask the user. Resolve all blocking questions before
the spec is approved.

---

## Quality bar — before you call a spec ready

- [ ] Problem statement and business value are explicit and, where possible, measurable.
- [ ] Every user story has a role, a goal, and a "so that" benefit, and is INVEST-shaped.
- [ ] Requirements are atomic, unambiguous, verifiable, traceable (IDs), and implementation-agnostic.
- [ ] NFRs reference the relevant `how/` docs instead of re-specifying them.
- [ ] Every story has Given/When/Then acceptance criteria covering happy path **and** failures.
- [ ] In-scope and out-of-scope are both stated.
- [ ] All blocking open questions are resolved and the user has approved the spec.
