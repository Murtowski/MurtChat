# KMP Specifications

This directory is the **single source of truth** for how this Kotlin Multiplatform
mobile app is designed, built, verified, and shipped. It exists so that Claude Code
and any other agent can deliver features that are correct, consistent, and useful to
real users — without re-deciding architecture every time.

Read this file first. It tells you **what to read, when, and in what order.**

---

## The three pillars

| Folder | Question it answers | When you read it |
|---|---|---|
| [`what-and-why/`](./what-and-why/README.md) | **What** are we building and **why**? | Before any code. Defines the feature, its value, and its acceptance criteria. |
| [`how/`](./how/README.md) | **How** do we build it? | While designing and implementing. Code style, architecture, platform conventions. |
| [`verification/`](./verification/README.md) | Is it **done and correct**? | After implementing, before shipping. Self-review, tests, build, delivery. |

These three pillars map directly onto the workflow below. You must not skip a pillar,
and you must not start a later pillar until the earlier one passes its gate.

---

## Relationship to the `/skills` folder

The repository root also contains a [`skills/`](../skills) folder. Those skills are the
**detailed, code-level conventions** (data layer, MVI presentation, Koin DI, navigation,
testing, etc.). This `specs/` folder is the **layer above**: it defines the *process*
(what to build, in what order, and how to prove it's done) and links out to the skills
for the low-level "how".

Rule of thumb:

- **A skill tells you how to write one kind of code** (e.g. "how to write a ViewModel").
- **A spec tells you what to build, why, and how to know you're finished.**

Where a `how/` document and a skill overlap, **the skill is authoritative for code-level
detail** and the spec links to it. Never duplicate skill content here — link to it.

---

## The agent workflow (read [`WORKFLOW.md`](./WORKFLOW.md) for the full version)

Every feature follows the same six phases. Each phase has an **entry gate** — do not
begin a phase until the previous gate is green.

```
1. SPECIFY   → write the feature spec      (what-and-why/)   ──GATE: spec approved──┐
2. DESIGN    → choose the how               (how/)                                   │
3. IMPLEMENT → write the code               (how/ + skills/)                         │
4. VERIFY    → self-review                  (verification/)  ──GATE: all checks pass─┤
5. DELIVER   → branch, commits, PR, review  (verification/06-delivery-and-pr.md)     │
6. LOOP BACK → on failure, return to 1/2/3  (per the failing check)  ────────────────┘
```

**Hard rule — Phase 1 is blocking.** Do not write any production code until a feature
spec exists in `what-and-why/features/<feature>/feature.md` and the user has approved it.
If the user asks you to "just build X", first produce the feature spec and get sign-off.

---

## Directory map

```
specs/
├── README.md                        ← you are here (index)
├── WORKFLOW.md                      ← the 6-phase workflow in detail
│
├── what-and-why/                    ← PILLAR 1: the feature specs
│   ├── README.md                    ← purpose + the Phase-1 gate
│   ├── authoring-guide.md           ← how to write requirements, AC, user stories
│   ├── feature-template.md          ← copy this to start a new feature
│   └── features/
│       ├── README.md                ← how features are organised
│       └── example-photo-capture/
│           └── feature.md           ← a fully worked example
│
├── how/                             ← PILLAR 2: code conventions & designs
│   ├── README.md
│   ├── 01-kotlin-style.md
│   ├── 02-architecture-and-modules.md
│   ├── 03-api-and-networking.md     ← incl. authentication session
│   ├── 04-data-storage-and-caching.md ← incl. image download/cache, storage mgmt
│   ├── 05-device-and-platform.md    ← camera, settings, device compatibility
│   └── 06-security.md
│
└── verification/                    ← PILLAR 3: self-review & delivery
    ├── README.md
    ├── self-review-checklist.md     ← the one-page checklist the agent runs
    ├── 01-build-and-static-analysis.md
    ├── 02-testing-and-coverage.md
    ├── 03-ui-verification.md
    ├── 04-performance.md
    ├── 05-behaviour.md
    └── 06-delivery-and-pr.md         ← branch, commits, PR, self-review
```

---

## Quick start for an agent

When the user asks for a new feature:

1. Open [`what-and-why/authoring-guide.md`](./what-and-why/authoring-guide.md) and
   [`what-and-why/feature-template.md`](./what-and-why/feature-template.md).
2. Create `what-and-why/features/<feature-name>/feature.md` and fill it in **with the user**.
3. **Stop and get approval.** This is the gate.
4. Once approved, read the relevant [`how/`](./how/README.md) docs and the matching skills,
   then implement.
5. Run the [`verification/self-review-checklist.md`](./verification/self-review-checklist.md).
6. If green, follow [`verification/06-delivery-and-pr.md`](./verification/06-delivery-and-pr.md).
   If red, loop back to the phase that owns the failure.
