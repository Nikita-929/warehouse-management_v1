# Installation Guide — Warehouse Management Desktop

## System Requirements
- Windows 10 or 11 (64‑bit)
- Disk space: ~300 MB
- No internet needed after install

## What to Distribute
- Preferred: `desktop/dist-out/Warehouse Management Setup 1.0.0.exe`
- Portable (no installer): `desktop/dist-out/win-unpacked/` (run `Warehouse Management.exe`)

## Dependencies
- If the installer bundles a Java runtime: no external dependencies.
- If not bundled: install Java 17 (JDK/JRE) and ensure `java` is on `PATH`.

## Installation Steps (Installer)
- Double‑click `Warehouse Management Setup 1.0.0.exe`.
- Follow prompts (per‑user install). No admin required.
- Launch the app from Start Menu or Desktop shortcut.

## First Run
- Data file created at `C:\Users\<User>\.warehouse\warehouse.db`.
- Backend log at `C:\Users\<User>\.warehouse\backend.log`.

## Offline Operation
- The UI and API communicate over `127.0.0.1`. No internet access is required.
- If corporate security restricts loopback, allow local connections for the app.

## Verifying Health
- Open `http://127.0.0.1:<port>/api/status` in a browser.
- You should see `backendUp=true`, `dbConnectOk=true`, and the DB path.

## Uninstall
- Use “Add or Remove Programs” to remove “Warehouse Management”.
- Data persists unless you delete `C:\Users\<User>\.warehouse\warehouse.db`.

## Troubleshooting
- Installer blocked: allow via Windows Defender SmartScreen.
- App fails to start backend:
  - Ensure Java 17 is installed, or rebuild with bundled runtime.
- Save/Upload errors:
  - The app shows the backend’s specific message (validation or parse); check `backend.log`.
- Packaging locks:
  - Use a clean output directory (`dist-out`) or stop any running instance before packing.

## Optional: Zero‑Dependency Installer
- Bundle Java runtime via jlink and include `backend/target/runtime` in the installer resources.
- Verify `win-unpacked\resources\backend\runtime\bin\java.exe` exists after packaging.

