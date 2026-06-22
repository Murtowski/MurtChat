# What & Why — Feature Specifications

This pillar answers two questions before a single line of code is written:

- **What** are we building?
- **Why** does it matter to users and the business?

If you cannot answer both clearly, you are not ready to build. The purpose of this folder
is to make sure that **what we build is the right thing**, that it is **useful to users**,
and that it **satisfies a real business need** — and that everyone agrees on this *before*
implementation starts.

---

## The Phase-1 gate (blocking)

> **No production code may be written until a feature spec exists here and the user has
> approved it.**

This is the most important rule in the whole spec system. A wrong line of code is cheap to
fix; a wrong *feature* wastes days. The feature spec is how we catch "wrong feature" early.

If the user says "just build X", your first deliverable is the feature spec for X, not the
code. Draft it, ask the open questions, and wait for approval.

---

## What lives here

| File | Purpose |
|---|---|
| [`authoring-guide.md`](./authoring-guide.md) | **The instruction for the agent.** How to write requirements, acceptance criteria, and user stories. Read this before authoring any spec. |
| [`feature-template.md`](./feature-template.md) | Copy-paste skeleton. Every feature spec starts from this. |
| [`features/`](./features/README.md) | One folder per feature. Contains the actual specs. |
| [`features/example-photo-capture/feature.md`](./features/example-photo-capture/feature.md) | A fully worked example to imitate. |

---

## How to create a new feature spec

1. Choose a short, kebab-case `<feature-name>` (e.g. `photo-capture`, `offline-sync`).
2. Create the folder `features/<feature-name>/`.
3. Copy [`feature-template.md`](./feature-template.md) into it as `feature.md`.
4. Fill every section, following [`authoring-guide.md`](./authoring-guide.md).
5. Mark unresolved decisions under **Open questions** and resolve them with the user.
6. Get explicit approval. Only then proceed to the `how/` pillar.

A feature spec is a living document: if requirements change during build, update the spec
first, then the code.
