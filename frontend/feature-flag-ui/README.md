# Feature Flag UI

React dashboard for the Feature Flag Service — view, filter, and explore feature flags.

## Setup

```bash
npm install
npm run dev
```

The dev server starts at `http://localhost:5173` and proxies API requests to `http://localhost:8080`.

## Build

```bash
npm run build    # Outputs to dist/
npm run preview  # Preview the production build
```

## Docker

```bash
docker build -t feature-flag-ui .
docker run -p 3000:80 feature-flag-ui
```

## Features

- **Authentication** — Login and signup with JWT persistence via localStorage
- **Flag Dashboard** — Lists all flags with environment tags, rollout %, and status badges
- **Filters** — Search by name, filter by environment or enabled/disabled status
- **Demo Mode** — Click "Demo Mode" on the login page to browse sample flags offline

## Stack

- React 19 + Vite 8
- Tailwind CSS 4
- No external UI libraries
