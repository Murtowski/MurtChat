# Features

One folder per feature. Each folder is the home for everything that defines *what* that
feature is and *why* it exists.

## Layout

```
features/
├── <feature-name>/
│   ├── feature.md        ← the spec (from ../feature-template.md)
│   └── assets/           ← optional: mockups, diagrams, sample payloads
└── example-photo-capture/
    └── feature.md        ← worked example — read this to see the standard
```

## Conventions

- **Folder name** = the feature, kebab-case: `photo-capture`, `offline-sync`, `expense-list`.
- The spec file is always named **`feature.md`**.
- Keep one feature per folder. If a "feature" grows several independent stories that each
  ship on their own, it's an epic — split it into multiple feature folders.
- The spec is **living**: when scope changes during build, update `feature.md` first.

## Creating a new one

1. `mkdir features/<feature-name>`
2. Copy [`../feature-template.md`](../feature-template.md) → `features/<feature-name>/feature.md`
3. Follow [`../authoring-guide.md`](../authoring-guide.md).
4. Get user approval before any code (the Phase-1 gate).
