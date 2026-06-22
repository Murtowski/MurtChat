# 06 — Security & Privacy

"Security" here = protecting user data, credentials, and the app's integrity. These rules are
mandatory; security requirements in a feature spec reference this document.

## Secrets & configuration

- **No secrets in source or VCS.** API keys, client secrets, and base URLs that are sensitive
  go in `local.properties` (git-ignored) and are surfaced via `BuildConfig` (Android) /
  `BuildKonfig` (KMP) — never hardcoded, never committed (`android-module-structure` skill).
- CI injects secrets from the CI secret store, not from the repo.
- Don't ship debug-only secrets or endpoints in release builds.

## Credential & token storage

- **Tokens and credentials live in platform secure storage**, never plain DataStore /
  SharedPreferences / Room:
  - **Android:** EncryptedSharedPreferences or DataStore encrypted with a key from the
    **Android Keystore**.
  - **iOS:** the **Keychain**.
- Access secure storage only through a domain interface (e.g. `SessionStorage`) so it can be
  faked in tests and so no UI/business code touches platform crypto directly.
- Clear all tokens and session-scoped data on logout, refresh failure, or expiry
  (`03-api-and-networking.md`).

## Network security

- **HTTPS only.** No cleartext traffic — set `android:usesCleartextTraffic="false"` and use ATS
  on iOS. Reject plaintext endpoints.
- Consider **certificate pinning** for high-value endpoints (auth, payments); document it in the
  feature spec when applied.
- Never log tokens, full auth headers, request/response bodies containing PII, or secrets.
  Logging is at the network boundary only and is scrubbed (`03-api-and-networking.md`).

## Data-at-rest

- Persist only what's needed. Sensitive cached data is encrypted or kept in secure storage.
- App-private storage by default; nothing sensitive in world-readable/shared locations
  (`04-data-storage-and-caching.md`).
- Wipe user-scoped data on logout.

## Input & content safety

- Validate and sanitize all external input (API responses, deep links, file content) at the
  boundary; model invalid input as typed errors (`android-error-handling` skill), don't trust
  the wire.
- Validate deep-link parameters before acting on them.

## Privacy, consent & analytics

- Collect the minimum data necessary; request permissions just-in-time (`05`).
- **Analytics/crash reporting respects consent.** Firebase Analytics/Crashlytics is isolated in
  `:core:analytics`; nothing outside imports `com.google.firebase.*` (the `observability`
  skill). Never send PII or secrets as event params/user properties. Honor the user's
  analytics opt-out from User Settings (`05`).
- Provide the privacy disclosures the platforms require (Android Data Safety, iOS Privacy
  Manifest / nutrition labels) consistent with what the app actually collects.

## Build & platform hardening

- Release builds: enable code shrinking/obfuscation (R8/ProGuard) and strip logs.
- Don't disable platform security features (no `allowBackup` of sensitive data, no debuggable
  release builds).
- Keep dependencies current; treat a flagged vulnerable dependency as a build-blocking issue
  (`verification/01-build-and-static-analysis.md`).

## Security checklist (per feature)

- [ ] No secret added to source or committed config.
- [ ] Any credential/token uses secure storage via a domain interface.
- [ ] All network calls are HTTPS; sensitive endpoints considered for pinning.
- [ ] No PII/secret in logs or analytics.
- [ ] External input validated at the boundary.
- [ ] User-scoped/sensitive data is cleared on logout.
- [ ] Consent honored for analytics and any data collection.
