# The Agent Workflow

This is the authoritative process for building any feature in this project. It is
**phase-gated**: each phase has an entry condition that must be satisfied before the
phase may begin. Skipping a gate is a process violation.

```
┌──────────────┐   gate    ┌──────────────┐         ┌──────────────┐
│ 1. SPECIFY   │──spec OK──▶│ 2. DESIGN    │────────▶│ 3. IMPLEMENT │
│ what-and-why │           │ how/          │         │ how/ + skills│
└──────────────┘           └──────────────┘         └──────┬───────┘
       ▲                                                    │
       │                                                    ▼
       │  loop back to the                          ┌──────────────┐
       │  phase that owns                  fail ◀────│ 4. VERIFY    │
       │  the failure                               │ verification/│
       │                                            └──────┬───────┘
       │                                                   │ pass
       │                                                   ▼
       │                                            ┌──────────────┐
       └────────────────────────────────────────── │ 5. DELIVER   │
                                                    │ branch/PR    │
                                                    └──────────────┘
```

---

## Phase 1 — SPECIFY (what & why)

**Goal:** know exactly what to build and why it matters, before touching code.

**Do:**
1. Read [`what-and-why/authoring-guide.md`](./what-and-why/authoring-guide.md).
2. Copy [`what-and-why/feature-template.md`](./what-and-why/feature-template.md) to
   `what-and-why/features/<feature-name>/feature.md`.
3. Fill in: problem & business value, user stories, requirements, acceptance criteria,
   scope (in/out), and open questions.
4. Resolve open questions **with the user**. Ask, don't assume.

**Exit gate (BLOCKING):** the feature spec is complete *and the user has explicitly
approved it*. Until then, **do not design or write any production code.** If the user
says "just build it", reply with the draft spec and ask for sign-off first.

---

## Phase 2 — DESIGN (how)

**Goal:** decide the technical approach within the established conventions.

**Do:**
1. Read [`how/README.md`](./how/README.md) and the specific `how/` docs that the feature
   touches (e.g. networking, caching, camera).
2. Identify which **skills** apply (data layer, MVI, navigation, DI, etc.) and follow them.
3. Identify the modules to create or change per
   [`how/02-architecture-and-modules.md`](./how/02-architecture-and-modules.md).
4. Note the design decisions in the feature spec's "Technical notes" section.

**Exit gate:** the affected modules, data flow, and screens are written down and consistent
with `how/` and the skills. No new architecture is invented without recording why.

---

## Phase 3 — IMPLEMENT

**Goal:** write the code, following the design and the skills.

**Do:**
1. Implement domain → data → presentation, one module/layer at a time.
2. Follow the matching skill for every layer (e.g. `android-presentation-mvi` for ViewModels).
3. Write tests **as you go**, not after — see [`how/`](./how/README.md) and the
   `android-testing` skill.
4. Keep commits small and logical from the start (you'll formalise them in Phase 5).

**Exit gate:** the feature is functionally complete and self-tested locally.

---

## Phase 4 — VERIFY (self-review)

**Goal:** prove the work is correct, performant, and ready — before involving anyone else.

**Do:** run [`verification/self-review-checklist.md`](./verification/self-review-checklist.md)
top to bottom. It covers:
- Build & static analysis — [`01`](./verification/01-build-and-static-analysis.md)
- Tests & coverage — [`02`](./verification/02-testing-and-coverage.md)
- UI — [`03`](./verification/03-ui-verification.md)
- Performance — [`04`](./verification/04-performance.md)
- Behaviour — [`05`](./verification/05-behaviour.md)

**Exit gate:** every checklist item passes. Any failure → **loop back** (see below).

---

## Phase 5 — DELIVER (branch, commits, PR, self-review)

**Goal:** ship the change in a reviewable, well-structured form.

**Do:** follow [`verification/06-delivery-and-pr.md`](./verification/06-delivery-and-pr.md):
1. Create a feature branch (`feature/<feature-name>`).
2. Split the work into small, digestible, conventional commits.
3. Open a PR with a description that links the feature spec.
4. **Self-review** the PR: read your own diff and leave comments on non-obvious choices,
   trade-offs, and follow-ups.

**Exit gate:** PR is open, CI is green, self-review comments are posted.

---

## Phase 6 — LOOP BACK (on failure)

When a verification check fails, return to the phase that **owns** the failure — do not
patch over it:

| Failing check | Loop back to | Because |
|---|---|---|
| Acceptance criteria not met / wrong behaviour | **Phase 1 (SPECIFY)** then 3 | The spec or your understanding of it is wrong. |
| Architecture/convention violation, wrong module, style | **Phase 2 (DESIGN)** then 3 | The approach is off; redesign before re-coding. |
| Bug, failing test, build error, low coverage | **Phase 3 (IMPLEMENT)** | The design is fine; the code isn't. |

After fixing, re-run Phase 4 from the top. Repeat until the gate is green, then proceed
to Phase 5.

---

## Definition of Done

A feature is **done** only when:

- [ ] An approved feature spec exists in `what-and-why/features/<feature>/`.
- [ ] The implementation follows the relevant `how/` docs and skills.
- [ ] Every item in the self-review checklist passes.
- [ ] All acceptance criteria in the feature spec are demonstrably met.
- [ ] A PR is open, CI is green, and a self-review has been posted.
