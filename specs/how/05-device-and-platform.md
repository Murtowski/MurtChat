# 05 — Device & Platform

Covers **camera access**, **user settings**, runtime **permissions**, and **device
compatibility** across the KMP targets. Platform code is reached through `expect`/`actual`
behind domain interfaces, so `commonMain` and tests stay platform-agnostic
(`android-module-structure` skill).

## The expect/actual rule

Any capability that differs per platform — camera, permissions, secure storage, settings deep
links, file pickers — is modeled as:

1. A **domain interface** in `feature:<name>:domain` or `core:domain` (pure Kotlin).
2. An **`expect`** declaration (or the interface itself) in `commonMain`.
3. **`actual`** implementations in `androidMain` (CameraX, Activity Result APIs, etc.) and
   `iosMain` (AVFoundation, UIImagePickerController, etc.).
4. Wiring via Koin.

Common code and ViewModels depend only on the interface, so they're testable with a fake.

## Camera access

"Camera Access" = capturing a photo (and, if a feature needs it, video) through the device
camera, with correct permission handling.

- **Abstraction:** define a domain interface (e.g. `CameraController` / `ImageCapture`) that
  returns a `Result<CapturedImage, CameraError>`. Implement per platform:
  - **Android:** CameraX or an `ACTION_IMAGE_CAPTURE` intent via the Activity Result API;
    declare `<uses-feature android:name="android.hardware.camera" android:required="false"/>`
    so camera-less devices can still install.
  - **iOS:** `AVCaptureSession` / `UIImagePickerController`; `NSCameraUsageDescription` in the
    Info.plist with a clear, honest reason string.
- **Permission first:** request camera permission at the moment of use, not on launch. Handle
  three outcomes — granted, denied, permanently denied — per the permissions section below.
- **Output handling:** captured images are compressed and stored locally per
  `04-data-storage-and-caching.md`; they are **not auto-uploaded**.
- **Graceful absence:** if there's no camera or permission is refused, the feature degrades
  gracefully (e.g. offer gallery import or skip), never crashes.

## Permissions

- Request **just-in-time**, tied to the user action that needs it, with a rationale shown
  *before* the system dialog when the platform recommends it.
- Model permission state in the domain (`Granted` / `Denied` / `PermanentlyDenied`) via an
  interface so ViewModels can react and tests can fake it.
- On **permanently denied**, show an explanation and a button that deep-links to the app's
  system settings (see User Settings). Never spam the system dialog.
- Declare every permission in `AndroidManifest.xml` / Info.plist with the minimal set needed;
  justify each in code review. No "just in case" permissions.

## User Settings

"User Settings" = user-controllable app preferences and the screen that exposes them.

- **Storage:** persist with **DataStore** (Preferences for flags, Proto/serialized object for a
  typed settings model) behind a `SettingsRepository` domain interface. Never block the main
  thread; expose settings as a `Flow` the UI observes (MVI state).
- **Typical settings:** theme (system/light/dark), notifications opt-in, data-saver / "only
  download images on Wi-Fi", language, and a **Storage section** showing cache size with a
  **Clear cache** action (wired to `04-data-storage-and-caching.md`).
- **System deep links:** provide actions to open OS-level settings (app permissions,
  notification settings) via the platform `actual` (Android `Intent`, iOS
  `UIApplication.openSettingsURLString`).
- **Account:** sign-out lives here and must clear session + user-scoped caches
  (`03-api-and-networking.md`, `04`).
- Defaults are sensible and privacy-preserving (e.g. analytics opt-in respects consent —
  see `06-security.md`).

## Device compatibility

"Device Compatibility" = the range of devices/OS versions the app must run on well.

- **Supported OS:** state the floor per platform in the project (e.g. **Android 8.0 / API 26+**
  and **iOS 15+**) and set `minSdk` / deployment target accordingly. Each feature spec restates
  any tighter constraint (e.g. a capability needing API 29+) and provides a fallback.
- **Form factors:** support the range of phone screen sizes and densities; layouts are
  responsive (Compose adaptive layouts), not pixel-fixed. Tablet/foldable support is per the
  product's scope — state it in the feature spec.
- **Capability checks:** never assume hardware. Check for camera, biometrics, etc. at runtime
  and degrade gracefully when absent (see Camera access).
- **Configuration changes:** survive rotation, dark mode, font scaling, and process death —
  UI state via `SavedStateHandle`, durable state via Room/DataStore.
- **Accessibility is part of compatibility:** content descriptions, touch target sizes, dynamic
  type / font scaling, and sufficient contrast (`android-compose-ui` skill).
- **Localization & RTL:** externalize strings; support RTL layouts where the product ships RTL
  locales.
- **Verification:** device/OS matrix and adaptive-layout checks are part of
  `verification/03-ui-verification.md` and `verification/05-behaviour.md`.

## Rules

- No platform API is called from `commonMain` except through an `expect`/interface.
- Every permission and capability has a denied/absent path that's specified and tested.
- Minimum OS and any per-feature device constraints are written in the feature spec.
