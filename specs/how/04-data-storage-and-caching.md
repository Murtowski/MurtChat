# 04 — Data, Storage & Caching

Covers **data caching**, **storage management**, and **image download & caching (no
auto-upload)**. Code-level data patterns: **`android-data-layer`** skill (authoritative).

## What is data caching in this app?

Caching means keeping a local copy of remote data so the app is fast, works offline, and
avoids redundant network calls. The policy:

- **Room is the single source of truth** for cacheable domain data. The flow is: fetch from
  network → persist to Room → expose a Room `Flow` to the ViewModel. The ViewModel **never**
  observes network responses directly. This is the offline-first pattern in the
  `android-data-layer` skill — apply it whenever a feature needs offline support or repeat reads.
- **Transient / ephemeral cache** (e.g. an in-memory map for a session) is allowed only inside
  a data source and must be invalidated on logout.
- **Cache validity:** each cache defines a freshness policy (TTL or explicit invalidation).
  Stale-while-revalidate is preferred — show cached data immediately, refresh in the background,
  emit the update through the Room `Flow`.
- **Invalidation triggers:** logout/session end (clear all user-scoped caches — see
  `03-api-and-networking.md`), explicit pull-to-refresh, and server-driven changes.
- **No caching of secrets.** Tokens and sensitive material go to secure storage, never Room or
  plain DataStore (`06-security.md`).

### Storage technology choice

| Need | Use |
|---|---|
| Structured/relational app data, offline source of truth | **Room** (`:core:database` or feature `data`) |
| Small key-value preferences / flags | **DataStore (Preferences)** |
| Typed user settings object | **DataStore (Proto)** or a serialized object (see `05`) |
| Binary blobs (images, files) | App-private **file storage**, referenced by path in Room |
| Secrets / tokens | **Secure storage** (`06-security.md`) — never the above |

Room migrations: prefer `autoMigrations`; use manual `Migration` objects for complex schema
changes (`android-data-layer` skill).

## Storage management

The app is a guest on the user's device. Manage space deliberately.

- **App-private by default.** Persist files in app-private/internal storage so they're sandboxed
  and removed on uninstall. Use shared/external storage only when the user explicitly exports.
- **Reference, don't embed.** Store large blobs as files; keep only the file path/uri and
  metadata in Room.
- **Budget the cache.** Image/file caches have a maximum size (e.g. an LRU cap such as 250 MB or
  a fraction of free space). When over budget, evict least-recently-used entries.
- **Clear on these events:** logout (user-scoped data), "Clear cache" in settings (see `05`),
  and low-storage conditions where the platform signals pressure.
- **Report usage.** Provide a way for User Settings to show cache size and to clear it (`05`).
- **Never block the main thread** for I/O — file and DB work runs on `Dispatchers.IO`
  (inject the dispatcher where the class is unit-tested, per the `android-testing` skill).

## Images: download & caching (do not auto-upload)

Images are **downloaded and cached**, never uploaded automatically. Uploading is always a
separate, explicit, user-initiated action.

### Downloading & display

- Use **Coil** (Compose `AsyncImage` / KMP Coil) for loading and decoding remote images. It
  provides memory + disk caching, lifecycle awareness, downsampling, and placeholders out of
  the box — do not hand-roll image loading.
- Request images at display size (let Coil downsample); never decode a full-resolution image
  into a thumbnail-sized `Composable`.
- Show placeholder, error, and loading states; map load failures to `UiText`.

### Caching policy for images

- **Memory cache** for on-screen images; **disk cache** (app-private) for recently viewed,
  both bounded (Coil's `MemoryCache`/`DiskCache` builders with explicit size caps).
- Disk image cache participates in the storage budget above and is cleared by "Clear cache".
- Cache key includes any transformation (size, crop) so variants don't collide.

### Locally captured images (e.g. camera, see `05`)

- Compress before storage to the agreed budget (e.g. ≤ 1.5 MB, longest edge ≤ 2048 px);
  compression runs off-main on `Dispatchers.IO`.
- Store in app-private files; keep the path + metadata in Room, linked to the owning entity.
- **No automatic upload.** A captured/cached image stays local until the user explicitly
  triggers an upload action. Never upload over cellular or in the background without consent.
- Upload, when it exists, is its own feature spec with its own consent and retry rules
  (e.g. WorkManager-backed), and is out of scope for image *caching*.

## Rules

- ViewModels observe Room/DataStore `Flow`s, never raw network or file I/O.
- Every cache has a defined eviction and invalidation policy — no unbounded growth.
- All persistence goes through a domain interface so it can be faked in tests.
- User-scoped data is wiped on logout; nothing sensitive is left in plain caches.
