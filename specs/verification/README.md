# Verification — Self-Review & Delivery

This pillar answers: **is it done and correct?** It defines how the agent verifies its own
output before involving a human, and how it ships once verification passes. This is Phase 4
(VERIFY) and Phase 5 (DELIVER) of the [workflow](../WORKFLOW.md).

## The verification gate

> The agent must run [`self-review-checklist.md`](./self-review-checklist.md) and get **every
> item green** before opening a PR. A red item is a blocker, not a footnote.

## What's here

| Doc | Verifies |
|---|---|
| [`self-review-checklist.md`](./self-review-checklist.md) | The one-page checklist the agent runs end to end. |
| [`01-build-and-static-analysis.md`](./01-build-and-static-analysis.md) | Is the **Gradle build** fine? Lint, detekt, format. |
| [`02-testing-and-coverage.md`](./02-testing-and-coverage.md) | Are there **test cases**? What's the **coverage**? |
| [`03-ui-verification.md`](./03-ui-verification.md) | Is the **UI** fine? Snapshots, states, accessibility. |
| [`04-performance.md`](./04-performance.md) | What's the **performance**? |
| [`05-behaviour.md`](./05-behaviour.md) | Does the **app behave** per the acceptance criteria? |
| [`06-delivery-and-pr.md`](./06-delivery-and-pr.md) | Branch, **commits**, **PR**, **self-review**. |

## Loop-back on failure

When a check fails, return to the phase that **owns** the failure (see
[`WORKFLOW.md`](../WORKFLOW.md) §6), fix it, and re-run the whole checklist:

- **Wrong behaviour / unmet acceptance criteria** → back to **SPECIFY** (re-read the spec),
  then implement.
- **Convention/architecture/style violation** → back to **DESIGN**, then implement.
- **Bug / failing test / build error / low coverage** → back to **IMPLEMENT**.

Only when the checklist is fully green do you proceed to delivery.

## How the agent runs verification

Prefer running the real tooling in the sandbox/CI and reading the output, not asserting from
memory. Each doc lists the concrete commands. Where a check can't be automated (e.g. a visual
nuance), document the manual check and its result in the PR.
