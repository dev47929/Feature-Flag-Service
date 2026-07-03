<div align="center">

# 🚩 Feature Flag Service

**A robust, production-ready feature flag management system with a modern React dashboard.**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=java&logoColor=white)](https://www.java.com/)
[![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=white)](https://react.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4-06B6D4?logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-auth-000000?logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

**Control feature rollouts, manage user-specific overrides, and ship with confidence.**

</div>

---

## ✨ Features

| Area | Capabilities |
|---|---|
| **🧩 Flag Management** | Create, read, update, and delete feature flags with full metadata |
| **🎯 Smart Evaluation** | Evaluate flags per-user with percentage-based rollouts and override support |
| **👤 User Overrides** | Force-enable or force-disable a flag for specific users |
| **🔐 Auth & Security** | JWT-based authentication with BCrypt password hashing |
| **🌍 Multi-Environment** | Separate flag configurations per environment (dev, prod, etc.) |
| **📊 Percentage Rollouts** | Gradually roll out features to a percentage of users |
| **⚛️ React Dashboard** | Modern UI with Tailwind CSS, demo mode, and filterable flag list |
| **⚡ RESTful API** | Clean, consistent HTTP API with JSON request/response bodies |
| **🗄️ PostgreSQL** | Persistent storage with JPA/Hibernate ORM |

---

## 🏗️ Tech Stack

### Backend

| Layer | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 4.0.6, Spring MVC, Spring Data JPA, Spring Security |
| **Database** | PostgreSQL |
| **Auth** | JWT (jjwt 0.12.6) + BCrypt |
| **Build** | Maven (with wrapper) |
| **Mapping** | ModelMapper 3.1.1 |
| **Boilerplate** | Lombok |

### Frontend

| Layer | Technology |
|---|---|
| **Language** | JavaScript (JSX) |
| **Framework** | React 19 |
| **Bundler** | Vite 8 |
| **Styling** | Tailwind CSS 4 |
| **Auth** | JWT stored in localStorage |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- PostgreSQL running on `localhost:5432`
- Node.js 22+
- Maven (or use the included `./mvnw` wrapper)

### Database Setup

```sql
CREATE DATABASE flagHub;
```

### Run the Backend

```bash
# Development
cd demo
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Production
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

Or build and run:

```bash
./mvnw clean package -DskipTests
java -jar target/feature-flag-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### Run the Frontend

```bash
cd frontend/feature-flag-ui
npm install
npm run dev
```

The dev server starts at `http://localhost:5173` and proxies API requests to `http://localhost:8080`.

### Run Tests

```bash
cd demo && ./mvnw test
```

### Docker (Frontend)

```bash
cd frontend/feature-flag-ui
docker build -t feature-flag-ui .
docker run -p 3000:80 feature-flag-ui
```

---

## 📡 API Reference

### 🔐 Authentication

#### Register

Creates a new user and returns a JWT token.

```http
POST /auth/register
```

**Request body:**

```json
{
  "username": "johndoe",
  "password": "securePass123",
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Response `200 OK`:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response `400 Bad Request`:**

```
Username already exists
```

---

#### Login

Authenticates with credentials and returns a JWT token.

```http
POST /auth/auth/login
```

**Request body:**

```json
{
  "username": "johndoe",
  "password": "securePass123"
}
```

**Response `200 OK`:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1
}
```

---

#### Signup

Alternative registration endpoint.

```http
POST /auth/auth/signup
```

**Request body:**

```json
{
  "username": "johndoe",
  "password": "securePass123",
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Response `200 OK`:**

```json
{
  "username": "johndoe",
  "email": "john@example.com"
}
```

---

### 🚩 Flags

#### List All Flags

```http
GET /flags
```

**Response `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "new-checkout-flow",
    "description": "Enable the redesigned checkout experience",
    "enabled": false,
    "rolloutPercentage": 25,
    "environment": "dev",
    "createdAt": "2026-06-17T12:00:00"
  }
]
```

---

#### Evaluate a Flag

Evaluates whether a flag is enabled for a given user, respecting overrides and rollout percentages.

```http
GET /flags/evaluate?flagName=new-checkout-flow&userId=user123
```

| Parameter | Type | Required | Description |
|---|---|---|---|
| `flagName` | string | ✅ | The name of the flag to evaluate |
| `userId` | string | ❌ | The user identifier for override & rollout evaluation |

**Response `200 OK`:**

```json
{
  "flagName": "new-checkout-flow",
  "enabled": true,
  "userId": "user123",
  "reason": "Rollout percentage"
}
```

Possible `reason` values:

| Reason | Meaning |
|---|---|
| `Flag found` | Flag exists and is enabled, no restrictions apply |
| `Flag disabled` | Flag is globally disabled |
| `Flag not found` | No flag matches the name and current environment |
| `Rollout percentage` | Evaluated against the percentage-based rollout |
| `Rollout percentage (no user)` | Rollout < 100% but no userId provided |
| `Override` | A user-level override was applied |

---

#### Create a Flag

```http
POST /flags
```

**Headers:** `Authorization: Bearer <token>`

**Request body:**

```json
{
  "name": "new-checkout-flow",
  "description": "Enable the redesigned checkout experience",
  "enabled": false,
  "rolloutPercentage": 25,
  "environment": "dev"
}
```

**Response `200 OK`:**

```json
{
  "id": 1,
  "name": "new-checkout-flow",
  "description": "Enable the redesigned checkout experience",
  "enabled": false,
  "rolloutPercentage": 25,
  "environment": "dev",
  "createdAt": "2026-06-17T12:00:00"
}
```

---

#### Update a Flag

```http
PATCH /flags/{id}
```

**Headers:** `Authorization: Bearer <token>`

**Request body:**

```json
{
  "name": "new-checkout-flow-v2",
  "description": "Updated checkout experience",
  "enabled": true,
  "rolloutPercentage": 50,
  "environment": "prod"
}
```

**Response `200 OK`:**
Returns the updated flag object.

---

#### Delete a Flag

```http
DELETE /flags/{id}
```

**Headers:** `Authorization: Bearer <token>`

**Response `204 No Content`**

---

### 👤 Overrides

#### Add User Override

Force-enable or force-disable a flag for a specific user.

```http
POST /flags/{id}/overrides
```

**Headers:** `Authorization: Bearer <token>`

**Request body:**

```json
{
  "userId": "user123",
  "enabled": true
}
```

**Response `200 OK`**

---

#### Remove User Override

```http
DELETE /flags/{id}/overrides/{userId}
```

**Headers:** `Authorization: Bearer <token>`

**Response `204 No Content`**

---

## 🧠 Flag Evaluation Logic

The evaluation engine follows a precise decision tree when determining if a flag is active:

```
                 ┌─────────────────────────────────┐
                 │  Evaluate(flagName, userId)      │
                 └────────────────┬────────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────┐
                    │  Flag exists in current  │──── No ──→ Reason: "Flag not found" → false
                    │  environment?            │
                    └─────────────────────────┘
                              │ Yes
                              ▼
                    ┌─────────────────────────┐
                    │  User-level override    │──── Yes ──→ Reason: "Override" → override.enabled
                    │  exists for this flag?  │
                    └─────────────────────────┘
                              │ No
                              ▼
                    ┌─────────────────────────┐
                    │  Is flag globally       │──── No ──→ Reason: "Flag disabled" → false
                    │  enabled?               │
                    └─────────────────────────┘
                              │ Yes
                              ▼
                    ┌─────────────────────────────────────┐
                    │  rolloutPercentage < 100 AND        │
                    │  rolloutPercentage != null?          │
                    └────────────────┬────────────────────┘
                        ┌───────────┴───────────┐
                      Yes                       No
                        │                        │
                        ▼                        ▼
             ┌─────────────────────┐    ┌──────────────────┐
             │  userId provided?   │    │  Reason: "Flag   │
             │  ┌─── Yes ───┐ No  │    │  enabled" → true  │
             │  ▼          ▼      │    └──────────────────┘
             │ hash % 100   false │
             │ < rollout?         │
             │ ┌─ Yes ─┐ No      │
             │ ▼       ▼         │
             │ true    false      │
             └─────────────────────┘
```

**Rollout hashing:** Uses `Math.abs(userId.hashCode()) % 100` for consistent, deterministic user assignment.

---

## 📁 Project Structure

```
├── demo/                                          # Spring Boot backend
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   └── src/
│       ├── main/java/com/dev/featureflag/
│       │   ├── FeatureFlagApplication.java
│       │   ├── config/
│       │   │   ├── AllConfig.java
│       │   │   ├── CustomUserDetailsService.java
│       │   │   ├── JwtAuthFilter.java
│       │   │   ├── JwtService.java
│       │   │   └── SecurityConfig.java
│       │   ├── controller/
│       │   │   ├── AdminAuthController.java
│       │   │   ├── FlagController.java
│       │   │   └── UserController.java
│       │   ├── dto/
│       │   │   ├── auth/
│       │   │   │   ├── LoginReqDto.java
│       │   │   │   ├── LoginResponseDto.java
│       │   │   │   ├── SignupReqDto.java
│       │   │   │   └── SignupResponseDTO.java
│       │   │   ├── EvaluationResponse.java
│       │   │   ├── FlagRequest.java
│       │   │   ├── FlagResponse.java
│       │   │   └── OverrideRequest.java
│       │   ├── entity/
│       │   │   ├── Flag.java
│       │   │   ├── Override.java
│       │   │   └── User.java
│       │   ├── repository/
│       │   │   ├── FlagRepository.java
│       │   │   ├── OverrideRepository.java
│       │   │   └── UserRepo.java
│       │   └── service/
│       │       ├── AuthService.java
│       │       ├── EvaluationService.java
│       │       └── FlagService.java
│       └── resources/
│           ├── application-dev.properties
│           └── application-prod.properties
│
└── frontend/feature-flag-ui/                     # React dashboard
    ├── Dockerfile
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── main.jsx
        ├── index.css
        ├── App.jsx
        ├── context/
        │   └── AuthContext.jsx
        └── components/
            ├── FilterBar.jsx
            ├── FlagCard.jsx
            ├── FlagList.jsx
            ├── LoginPage.jsx
            └── SignupPage.jsx
```

---

## 🖥️ Frontend Overview

The React dashboard provides:

- **Authentication** — Login and signup pages with JWT token persistence in localStorage
- **Flag Dashboard** — Lists all feature flags with environment tags, rollout percentages, and enabled/disabled status
- **Filters** — Search by name, filter by environment, filter by enabled/disabled status
- **Demo Mode** — Offline demo with 9 sample flags to explore the UI without a running backend
- **Responsive Design** — Built with Tailwind CSS for a clean, responsive layout

---

## 🔧 Configuration

### Database

Default connection to PostgreSQL on `localhost:5432`:

| Property | Value |
|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/flagHub` |
| `spring.datasource.username` | `postgres` |
| `spring.datasource.password` | `Password@12345` |

### Profiles

| Profile | Active By | Environment Variable |
|---|---|---|
| `dev` | `--spring.profiles.active=dev` | `app.environment=dev` |
| `prod` | `--spring.profiles.active=prod` | `app.environment=prod` |

### JWT

| Property | Description |
|---|---|
| `jwt.secret.key` | HMAC secret key for token signing |
| Token TTL | 1 hour |
| Algorithm | HMAC-SHA |

### JPA / Hibernate

- `ddl-auto=update` — schema auto-updates on startup
- `show-sql=true` — logs all SQL statements (disable in production)

---

## 🗄️ Database Schema

### `flag` Table

| Column | Type | Constraints |
|---|---|---|
| `id` | `BIGINT` | PK, auto-increment |
| `name` | `VARCHAR` | |
| `description` | `VARCHAR` | |
| `enabled` | `BOOLEAN` | |
| `rollout_percentage` | `INTEGER` | nullable |
| `environment` | `VARCHAR` | |
| `created_at` | `TIMESTAMP` | |

### `user_details` Table

| Column | Type | Constraints |
|---|---|---|
| `id` | `BIGINT` | PK, auto-increment |
| `username` | `VARCHAR` | unique |
| `name` | `VARCHAR` | |
| `email` | `VARCHAR` | unique |
| `password` | `VARCHAR` | BCrypt-hashed |
| `created_at` | `TIMESTAMP` | |

### `override_flag` Table

| Column | Type | Constraints |
|---|---|---|
| `id` | `BIGINT` | PK, auto-increment |
| `flag_id` | `BIGINT` | FK → `flag.id` |
| `user_id` | `VARCHAR` | |
| `enabled` | `BOOLEAN` | |

---

## 📦 Build

### Backend

```bash
cd demo
./mvnw clean package              # Build (skips tests by default)
./mvnw clean package -DskipTests=false   # Build with tests
```

Output: `demo/target/feature-flag-0.0.1-SNAPSHOT.jar`

### Frontend

```bash
cd frontend/feature-flag-ui
npm run build                     # Outputs to dist/
npm run preview                   # Preview the production build
```

---

## 🧪 Testing

### Backend

```bash
cd demo && ./mvnw test
```

### Frontend

```bash
cd frontend/feature-flag-ui
npm run lint
npm run build
```

---

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

<div align="center">

Built with ❤️ using Spring Boot & React

</div>
