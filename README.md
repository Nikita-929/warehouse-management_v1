# Warehouse Management System (Desktop + Web)

A modern warehouse management system with a Java Spring Boot backend, React frontend, and an Electron-based desktop shell. Data is stored locally in SQLite for reliable, non‑volatile persistence.

## Overview
- Core features: Product management (CRUD), transactions with Excel upload, search/filter/autocomplete, reports.
- Single-machine deployment: The desktop app runs a local backend and serves the bundled React UI. No internet is required.
- Persistence: Data stored at `C:\Users\<User>\.warehouse\warehouse.db`.

## Tech Stack
- Backend: Spring Boot 3.2, Java 17, Hibernate/JPA, SQLite
- Frontend: React 18, React Bootstrap, Axios, React Router, React Toastify
- Desktop: Electron 29, electron-builder (NSIS installer)

## Repository Structure
- `backend/` Spring Boot application (REST API, SQLite persistence)
- `frontend/` React application (built and copied into backend static resources)
- `desktop/` Electron shell packaging the backend JAR and static UI

## Data Storage
- File: `C:\Users\<User>\.warehouse\warehouse.db`
- Config: `backend/src/main/resources/application.yml`
- Tables: `products`, `transactions`, `product_names`

## Runtime Workflow
- Desktop app starts the backend JAR on a free port (8080–8200) and loads the UI at `http://127.0.0.1:<port>`.
- Health and status:
  - `GET /api/health` → system healthy
  - `GET /api/status` → `{ port, backendUp, dbPath, dbExists, dbConnectOk, staticIndexExists }`
- Add Product:
  - Form submits to `POST /api/products` and shows success or backend validation errors.
- Upload Excel:
  - `POST /api/transactions/upload` parses `.xlsx/.xls`, supports Indian date formats, and persists valid rows.

## Key Implementation Notes
- SQLite driver does not implement JDBC generated keys. Hibernate is configured to avoid `getGeneratedKeys()` and batching:
  - `spring.jpa.properties.hibernate.jdbc.use_get_generated_keys=false`
  - `spring.jpa.properties.hibernate.jdbc.batch_size=0`
- Security:
  - CSRF disabled for API, all routes permitted for local desktop use.
- Frontend error handling:
  - Axios interceptor surfaces server error messages in UI toasts.
- Static asset serving:
  - React build copied to `backend/src/main/resources/static/` and served by Spring Boot.
- Electron startup:
  - IPv4 loopback (`127.0.0.1`) used consistently for health checks and UI loading.

## Development
Prerequisites
- Java 17 (JDK), Maven 3.9+
- Node.js 18+

Install & Run (dev)
- Backend: `cd backend && mvn spring-boot:run`
- Frontend: `cd frontend && npm install && npm start`
- Convenience scripts: `start-backend.bat`, `start-frontend.bat`

Build
- Backend JAR: `cd backend && mvn clean package -DskipTests`
- Frontend build → copy to backend static: `cd frontend && npm run build` then mirror `frontend/build` to `backend/src/main/resources/static`

## Desktop Packaging
- Electron pack: `cd desktop && npm run pack`
- Output:
  - Installer: `desktop/dist-out/Warehouse Management Setup 1.0.0.exe`
  - Unpacked: `desktop/dist-out/win-unpacked/`
- Bundled resources: backend JAR (and optionally Java runtime via jlink).

## Deployment/Distribution
- Share the installer `.exe` for a one‑click install.
- Data and logs are user‑home scoped (`.warehouse`).

## Troubleshooting
- check `GET /api/status` for health.
- If Excel upload fails, the UI shows the backend’s error (file type/size/parse/validation).
- If packaging complains about locked files, use a clean output directory (e.g., `dist-out`).

## Roadmap
- Bundle Java runtime by default to remove external dependency.
- Add automated tests and CI packaging.
- Extend reports and export options.

