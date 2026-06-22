<!--
  FEATURE SPEC TEMPLATE
  Copy this file to: what-and-why/features/<feature-name>/feature.md
  Fill every section. Delete the guidance comments (<!-- ... -->) as you go.
  Authoring rules: ../authoring-guide.md
-->

# Feature: <Feature Name>

| Field | Value |
|---|---|
| **Status** | Draft / In review / Approved / Built / Shipped |
| **Owner** | <name> |
| **Created** | <YYYY-MM-DD> |
| **Last updated** | <YYYY-MM-DD> |
| **Related skills** | <e.g. android-presentation-mvi, android-data-layer> |
| **Related how/ docs** | <e.g. how/04-data-storage-and-caching.md> |

---

## 1. Problem & business value

<!-- Who has the problem, what it is, what it costs today. Then: what success looks like,
     and how it is measured (a metric if possible). See authoring-guide §1. -->

**Problem:**

**Business value / success metric:**

---

## 2. User stories

<!-- One block per story. Role + goal + benefit. INVEST. See authoring-guide §2. -->

### US-1: <short title>

> As a **<role>**, I want **<goal>**, so that **<benefit>**.

### US-2: <short title>

> As a **<role>**, I want **<goal>**, so that **<benefit>**.

---

## 3. Requirements

<!-- Atomic, verifiable, traceable, implementation-agnostic. See authoring-guide §3. -->

### Functional

- **FR-1** <statement>
- **FR-2** <statement>

### Non-functional (reference how/ docs, don't re-specify)

- **NFR-1** <statement> (see `how/<doc>.md`)
- **NFR-2** <statement> (see `how/<doc>.md`)

### Constraints

- **C-1** <platform / regulatory / dependency limit>

---

## 4. Acceptance criteria

<!-- Given/When/Then. Happy path + every failure path. Each maps to a test.
     See authoring-guide §4. -->

```
Scenario: <happy path name>
  Given <context>
  When  <action>
  Then  <observable outcome>

Scenario: <failure / edge case name>
  Given <context>
  When  <action>
  Then  <observable outcome>
```

**Traceability:** US-1 → FR-1, FR-2 → Scenarios 1, 2; US-2 → FR-3 → Scenario 3 …

---

## 5. Scope

**In scope:**

**Out of scope (explicitly):**

---

## 6. Technical notes (filled during Phase 2 — DESIGN)

<!-- Modules to add/change, data flow, screens, key skills applied. Keep it brief;
     detailed conventions live in how/ and skills/. -->

- **Modules:**
- **Data flow:**
- **Screens / navigation:**

---

## 7. Open questions

<!-- Numbered, with owner. Resolve all blocking ones before approval. -->

- **Q-1** <question> — owner: <name> — status: open/resolved

---

## 8. Done checklist

- [ ] All acceptance scenarios implemented and passing as tests.
- [ ] Implementation follows the referenced `how/` docs and skills.
- [ ] `verification/self-review-checklist.md` is fully green.
- [ ] PR opened, CI green, self-review posted (`verification/06-delivery-and-pr.md`).
