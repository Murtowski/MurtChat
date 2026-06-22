# 03 — API, Networking & Authentication Session

Covers how the app talks to the backend, how API contracts are documented, and **how the
authentication session is handled.** Code-level patterns: **`android-data-layer`** and
**`android-error-handling`** skills (authoritative).

## HTTP client (Ktor)

- A single `HttpClient` is configured once in `core:data` via `HttpClientFactory.create(engine)`,
  with the engine injected so tests can swap a `MockEngine`. See the `android-data-layer` skill
  for the canonical factory.
- Installed plugins: `ContentNegotiation` (KotlinX `json`), `Auth` (bearer), `Logging`,
  `DefaultRequest` (base URL, JSON content type), and a sensible `HttpTimeout`.
- Inject `HttpClient` via Koin. For KMP use the platform default engine.
- Every call goes through the `safeCall` / typed `get`/`post`/`delete` helpers so call sites
  return `Result<T, DataError.Network>` and never throw (see `android-error-handling`).

## API documentation (contract-first)

Each backend integration is documented so the agent never guesses payloads.

- **Source of truth:** the backend's OpenAPI/Swagger spec. Link it in the feature spec's
  *Related how/ docs* or the data module's README.
- For each endpoint used, record in the feature's `data` module (a short `API.md` or KDoc):
  method + path, request DTO, response DTO, auth requirement, pagination, and the error codes
  and what they mean.
- **DTOs mirror the wire format exactly** (`@Serializable`, `@SerialName` for mismatched
  names). DTOs live in `data`; they are mapped to domain models at the boundary and **never**
  cross into domain or UI (`android-data-layer` skill).
- Version the API base path (`/v1/...`); breaking changes get a new path, not a silent change.

## Authentication session

The session is the user's authenticated state and the tokens that back it. Handle it as
follows:

### Tokens & refresh

- Use the Ktor **`Auth` / `bearer`** plugin. `loadTokens` reads the current access/refresh
  tokens; `refreshTokens` calls the refresh endpoint and persists new tokens. This makes 401
  handling and token refresh automatic for all calls.
- **Storage:** tokens live in encrypted/secure storage, not plain DataStore or SharedPreferences
  in clear text — see `06-security.md`. Token read/write is wrapped behind a domain interface
  (e.g. `SessionStorage`) so it can be faked in tests.
- **Single refresh:** ensure concurrent 401s trigger only one refresh (the `Auth` plugin
  coordinates this); never refresh per-call in data sources.

### Session state & lifecycle

- Expose session state through a domain-level `SessionManager` interface in `core:domain`,
  emitting a `Flow<SessionState>` (`Authenticated` / `Unauthenticated` / `Expired`). UI and
  navigation observe it; data sources never observe it directly.
- **Login:** on success, persist tokens and any minimal user identity needed for the session;
  emit `Authenticated`.
- **Logout / expiry / refresh failure:** clear all tokens and session-scoped caches
  (see `04-data-storage-and-caching.md`), emit `Unauthenticated`, and route to the auth flow
  via a navigation `Event` (`android-navigation` skill). Never leave stale tokens.
- **Process death:** session is reconstructed from secure storage on startup; transient UI
  state via `SavedStateHandle` (`android-presentation-mvi` skill).

### Rules

- No screen reads tokens directly — only the data layer and `SessionManager` touch them.
- Auth errors are typed (`DataError.Network.UNAUTHORIZED`, etc.) and mapped to `UiText`, never
  shown as raw exceptions.
- Biometric/PIN re-auth, if required, is a separate feature spec; it gates *access to* the
  session, it does not replace token handling.

## Networking rules

- All network results are `Result<T, DataError.Network>`; map errors to `UiText` in presentation.
- Respect connectivity: surface offline state from the data layer; pair with the offline-first
  pattern where the feature requires it (`04-data-storage-and-caching.md`).
- No business logic in data sources — they fetch, map, and return. Coordination of multiple
  sources is a repository's job (`android-data-layer` skill).
- Log at the network boundary only; never log tokens, bodies with PII, or full auth headers.
