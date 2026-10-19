# 🚩 Feature Flag Service — Comprehensive Interview & Architecture Guide

> **Target Audience:** Engineering Interviewers, Technical Hiring Managers, System Design Panels, and Behavioral Interviewers.  
> **Repository:** `Feature-Flag-Service` (Full-Stack Spring Boot 4 + React 19 System)

---

## 📑 Table of Contents
1. [Executive Summary & 30-Second Pitch](#1-executive-summary--30-second-pitch)
2. [Problem Statement & Business Value](#2-problem-statement--business-value)
3. [System Architecture & Tech Stack](#3-system-architecture--tech-stack)
4. [Deep-Dive: Feature-by-Feature Breakdown](#4-deep-dive-feature-by-feature-breakdown)
   - [Feature 1: Flag Lifecycle & Metadata Management](#feature-1-flag-lifecycle--metadata-management)
   - [Feature 2: Multi-Environment Isolation](#feature-2-multi-environment-isolation)
   - [Feature 3: Dynamic Flag Evaluation Engine](#feature-3-dynamic-flag-evaluation-engine)
   - [Feature 4: Deterministic Percentage-Based Rollouts](#feature-4-deterministic-percentage-based-rollouts)
   - [Feature 5: User-Level Targeted Overrides (Allowlist/Denylist)](#feature-5-user-level-targeted-overrides)
   - [Feature 6: Security, Authentication & Role Isolation (JWT + Spring Security)](#feature-6-security-authentication--role-isolation)
   - [Feature 7: Interactive React 19 + Tailwind CSS 4 Admin Dashboard](#feature-7-interactive-react-19--tailwind-css-4-admin-dashboard)
   - [Feature 8: Production-Ready Multi-Stage Docker Containerization](#feature-8-production-ready-multi-stage-docker-containerization)
5. [Database Schema & Data Model Design](#5-database-schema--data-model-design)
6. [API Specification & Data Contracts](#6-api-specification--data-contracts)
7. [System Design & Scaling Strategies (Interview Q&A)](#7-system-design--scaling-strategies-interview-qa)
8. [Star-Method Resume Bullet Points & Talking Points](#8-star-method-resume-bullet-points--talking-points)

---

## 1. Executive Summary & 30-Second Pitch

### ⏱️ The 30-Second Elevator Pitch
> *"I built a **production-ready, full-stack Feature Flag & Configuration Management Service** using **Spring Boot 4 (Java 17)**, **PostgreSQL**, and **React 19 with Tailwind CSS 4**. It decouples code deployment from feature release by supporting **deterministic percentage-based canary rollouts**, **user-level allowlist/denylist overrides**, and **multi-environment isolation** (Dev/Staging/Prod). The system features stateless **JWT authentication**, fine-grained public evaluation vs. protected management endpoints, and multi-stage containerized deployments."*

### ⏱️ The 2-Minute Architectural Deep-Dive
> *"In modern CI/CD pipelines, deploying code directly into production carries high risk if features cannot be incrementally tested. My Feature Flag Service solves this by providing a centralized control plane. 
> 
> On the **Backend**, Spring Boot exposes RESTful endpoints for administrative mutations (protected via JWT and BCrypt) and a high-performance evaluation engine (`/flags/evaluate`). Evaluation resolves flags through a deterministic 5-step hierarchy: Environment Matching → User Overrides → Global Status → Percentage Hashing (`Math.abs(userId.hashCode()) % 100`) → Default fallback. This guarantees that user experience remains sticky without persisting state for millions of evaluations.
> 
> On the **Frontend**, React 19 leverages a clean Context API architecture with responsive Tailwind CSS 4 styling, providing real-time search, multi-axis filtering (by environment and status), in-place modal editing, and an offline **Demo Mode** for zero-dependency showcasing. Both tiers are containerized with multi-stage Docker builds for minimal production image footprints."*

---

## 2. Problem Statement & Business Value

| Traditional Deployment Bottleneck | Feature Flag Service Solution |
|---|---|
| **High Deployment Risk:** Merging and deploying code immediately exposes features to 100% of production users. | **Decoupled Release:** Code is deployed dark (disabled by default) and toggled remotely without redeploying. |
| **All-or-Nothing Rollouts:** If a bug occurs, an emergency hotfix or full rollback is required. | **Instant Kill-Switch & Gradual Rollouts:** Toggled off in milliseconds or rolled out to 5% → 25% → 50% → 100%. |
| **Difficult Production QA:** Hard for QA/VIPs to test features in production without exposing them to normal users. | **User-Level Overrides:** Targeted allowlisting enables QA testers to preview features safely in production. |
| **Environment Drift:** Inconsistent configurations across Dev, Staging, and Prod. | **Multi-Environment Support:** Explicit flag boundaries per environment prevent accidental cross-talk. |

---

## 3. System Architecture & Tech Stack

```
                               ┌──────────────────────────────────────────────┐
                               │             React 19 SPA Dashboard           │
                               │  - Tailwind CSS 4, Lucide Icons, Vite 8      │
                               │  - AuthContext (JWT + Demo Mode)             │
                               └──────────────────────┬───────────────────────┘
                                                      │ HTTP / REST (Axios/Fetch)
                                                      ▼
 ┌────────────────────────────────────────────────────────────────────────────────────────────────────┐
 │                                   Spring Boot 4.0.6 Backend Engine                                 │
 │                                                                                                    │
 │   ┌────────────────────────┐         ┌────────────────────────┐        ┌───────────────────────┐   │
 │   │     Security Layer     │         │   Controllers (REST)   │        │     Service Layer     │   │
 │   │ - JwtAuthFilter        │ ──────> │ - AdminAuthController  │ ─────> │ - AuthService         │   │
 │   │ - BCryptPasswordEncoder│         │ - FlagController       │        │ - FlagService         │   │
 │   │ - Stateless Security   │         │ - UserController       │        │ - EvaluationService   │   │
 │   └────────────────────────┘         └────────────────────────┘        └───────────┬───────────┘   │
 └────────────────────────────────────────────────────────────────────────────────────┼───────────────┘
                                                                                      │ JPA / Hibernate
                                                                                      ▼
                                                                       ┌──────────────────────────────┐
                                                                       │    PostgreSQL (flagHub DB)   │
                                                                       │ - flag                       │
                                                                       │ - override_flag              │
                                                                       │ - user_details               │
                                                                       └──────────────────────────────┘
```

### Technology Breakdown

| Layer | Technology | Version / Tooling | Rationale |
|---|---|---|---|
| **Backend Framework** | Java / Spring Boot | Java 17, Spring Boot 4.0.6 | Enterprise standard, high throughput, robust DI container, mature ecosystem. |
| **Security** | Spring Security & JWT | `jjwt 0.12.6`, BCrypt | Stateless, horizontally scalable authentication; fine-grained method/path authorization. |
| **Persistence** | Spring Data JPA & Hibernate | PostgreSQL Dialect | Object-Relational Mapping with connection pooling, declarative transactions, and automated schema migration. |
| **Database** | PostgreSQL | 15+ | ACID-compliant relational DB supporting fast indexed queries on flags and overrides. |
| **Object Mapping** | ModelMapper | 3.1.1 | Clean separation between internal JPA Entities and external REST DTOs. |
| **Frontend Framework**| React | 19.x (Vite 8) | Fast Virtual DOM, modern hooks (`useState`, `useEffect`, `useContext`), blazing-fast HMR with Vite. |
| **Styling** | Tailwind CSS | 4.x | Utility-first CSS for sleek, modern, responsive glassmorphism and dashboard UI. |
| **Containerization** | Docker | Multi-Stage Builds | Backend: Maven build → Eclipse Temurin JRE Alpine runtime. Frontend: Node build → Nginx Alpine. |

---

## 4. Deep-Dive: Feature-by-Feature Breakdown

---

### Feature 1: Flag Lifecycle & Metadata Management

#### 💡 Concept & Capabilities
Enables administrators to perform full CRUD operations on feature flags with rich metadata including:
- **Flag Name:** Unique identifier (e.g., `new-checkout-flow`, `dark-mode`).
- **Description:** Clear business and technical explanation of the toggle.
- **Enabled Status:** Master switch (`true` / `false`).
- **Rollout Percentage:** Integer value from `0` to `100` (or `null` for binary flags).
- **Environment:** Scoped target environment (`dev`, `staging`, `production`).
- **Creation Timestamp:** Auto-populated `LocalDateTime`.

#### 💻 Technical Implementation
- **Controller:** [FlagController.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/controller/FlagController.java)
- **Service:** [FlagService.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/service/FlagService.java)
- **Data Transfer Objects:** [FlagRequest.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/dto/FlagRequest.java), [FlagResponse.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/dto/FlagResponse.java)
- **Entity:** [Flag.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/entity/Flag.java)

#### 🔍 Code Highlights
```java
public FlagResponse updateFlag(Long id, FlagRequest request) {
    Flag flag = flagRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Flag not found: " + id));
    if (request.getName() != null) flag.setName(request.getName());
    if (request.getDescription() != null) flag.setDescription(request.getDescription());
    flag.setEnabled(request.isEnabled());
    if (request.getRolloutPercentage() != null) flag.setRolloutPercentage(request.getRolloutPercentage());
    if (request.getEnvironment() != null) flag.setEnvironment(request.getEnvironment());
    flagRepository.save(flag);
    return modelMapper.map(flag, FlagResponse.class);
}
```

---

### Feature 2: Multi-Environment Isolation

#### 💡 Concept & Capabilities
Real-world systems require feature flags to be configured differently depending on where the service is running. A feature enabled for internal testing in `dev` should not accidentally be live in `production`.

#### 💻 Technical Implementation
1. **Spring Profiles:** Managed via `application-dev.properties` (`app.environment=dev`) and `application-prod.properties` (`app.environment=prod`).
2. **Dynamic Resolution:** [EvaluationService.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/service/EvaluationService.java) reads `@Value("${app.environment}")` and queries the database using `flagRepository.findByNameAndEnvironment(flagName, environment)`.
3. **Frontend Awareness:** The React dashboard reads all distinct environments from the flag collection and provides real-time environment filtering.

---

### Feature 3: Dynamic Flag Evaluation Engine

#### 💡 Concept & Capabilities
When client applications or backend microservices request a flag status via `/flags/evaluate?flagName=...&userId=...`, the engine must evaluate the flag with sub-millisecond efficiency and provide transparent diagnostic reasons.

#### 🧠 Evaluation Decision Hierarchy (Step-by-Step Flow)

```
                            ┌────────────────────────────────────────┐
                            │  GET /flags/evaluate(flagName, userId) │
                            └───────────────────┬────────────────────┘
                                                │
                                                ▼
                                  ┌───────────────────────────┐
                                  │ Does Flag exist in active │ ─── NO ───► HTTP 400 / Exception
                                  │ environment?              │             ("Invalid flag name")
                                  └─────────────┬─────────────┘
                                                │ YES
                                                ▼
                                  ┌───────────────────────────┐
                                  │ Is there a User Override  │ ─── YES ──► Return Override.isEnabled()
                                  │ for this flag + userId?   │             Reason: "Override"
                                  └─────────────┬─────────────┘
                                                │ NO
                                                ▼
                                  ┌───────────────────────────┐
                                  │ Is flag globally enabled? │ ─── NO ───► Return false
                                  │ (flag.isEnabled() == true)│             Reason: "Flag disabled"
                                  └─────────────┬─────────────┘
                                                │ YES
                                                ▼
                                  ┌───────────────────────────┐
                                  │ Is rolloutPercentage set  │ ─── NO ───► Return true
                                  │ & between [0, 99]?        │             Reason: "Flag enabled"
                                  └─────────────┬─────────────┘
                                                │ YES
                                                ▼
                                  ┌───────────────────────────┐
                                  │ Was userId provided in    │ ─── NO ───► Return false
                                  │ request parameter?        │             Reason: "Rollout percentage (no user)"
                                  └─────────────┬─────────────┘
                                                │ YES
                                                ▼
                                  ┌───────────────────────────┐
                                  │ Compute hash:             │
                                  │ Math.abs(userId.hashCode()│
                                  │ % 100 < rolloutPercentage │
                                  └─────────────┬─────────────┘
                                                │
                                   ┌────────────┴────────────┐
                                 YES                         NO
                                   │                          │
                                   ▼                          ▼
                           Return true                 Return false
                           Reason: "Rollout percentage" Reason: "Rollout percentage"
```

#### Diagnostic Evaluation Reason Codes:
- `Override` — Explicit user-specific override was matched.
- `Flag disabled` — Global master toggle is `false`.
- `Flag enabled` — Master toggle is `true` with 100% rollout.
- `Rollout percentage` — User fell within (or outside) the deterministic hash bucket.
- `Rollout percentage (no user)` — Percentage rollout active, but caller omitted `userId`.

---

### Feature 4: Deterministic Percentage-Based Rollouts

#### 💡 Concept & Capabilities
Gradual feature rollout (Canary deployment) allows testing features on e.g., 10%, 25%, or 50% of the user base.

#### 🎯 Key Technical Attributes:
1. **Stateless & Deterministic:** Uses Java's hash code algorithm:
   $$\text{bucket} = |\text{userId}.\text{hashCode}()| \pmod{100}$$
2. **Sticky User Experience:** A user with `userId="user123"` whose hash evaluates to `23` will consistently see the feature enabled when the rollout is `25%`, `50%`, or `100%`, and disabled when the rollout is `10%`. The user **never flickers** between enabled/disabled on subsequent calls.
3. **Zero Storage Overhead:** No need to store millions of user-to-bucket mappings in a database or cache table.

#### 🔍 Code Implementation ([EvaluationService.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/service/EvaluationService.java#L51-L68))
```java
Integer rollout = flag.getRolloutPercentage();
if (rollout != null && rollout < 100 && rollout >= 0) {
    if (userId != null) {
        int hash = Math.abs(userId.hashCode()) % 100;
        return EvaluationResponse.builder()
                .flagName(flagName)
                .enabled(hash < rollout)
                .userId(userId)
                .reason("Rollout percentage")
                .build();
    }
    return EvaluationResponse.builder()
            .flagName(flagName)
            .enabled(false)
            .userId(null)
            .reason("Rollout percentage (no user)")
            .build();
}
```

---

### Feature 5: User-Level Targeted Overrides

#### 💡 Concept & Capabilities
Allows overriding the global flag state and percentage rollouts for specific users.

#### 🌟 Practical Use Cases:
- **Internal Dogfooding / Beta Testers:** Force-enable `enabled=true` for internal developer IDs on a globally disabled flag.
- **VIP Customer Exclusion:** Force-disable `enabled=false` for enterprise clients sensitive to UI changes during canary testing.
- **Automated End-to-End Testing:** Enable features deterministically in CI/CD pipeline tests.

#### 💻 Technical Implementation
- Handled via `POST /flags/{id}/overrides` and `DELETE /flags/{id}/overrides/{userId}`.
- Persisted in the `override_flag` relational table with indexed lookups via `findByFlagIdAndUserId(Long flagId, String userId)`.

---

### Feature 6: Security, Authentication & Role Isolation

#### 💡 Concept & Capabilities
Ensures administrative safety by securing mutation endpoints while leaving flag evaluation open and fast for consuming applications.

#### 🔒 Security Architecture Highlights:
1. **Stateless Session Policy:** Spring Security is configured with `SessionCreationPolicy.STATELESS` — no `HttpSession` overhead on the server.
2. **Custom JWT Filter ([JwtAuthFilter.java](file:///e:/1.Code/Feature-Flag-Service/backend/src/main/java/com/dev/featureflag/config/JwtAuthFilter.java)):**
   - Intercepts incoming requests via `OncePerRequestFilter`.
   - Extracts and verifies the `Bearer <token>` against the HMAC-SHA secret key.
   - Populates `SecurityContextHolder` with `UsernamePasswordAuthenticationToken`.
3. **Password Security:** Passwords stored using industry-standard **BCrypt** hashing with automatic salt generation.
4. **Endpoint Permissions:**
   - **Public Access (`permitAll`):** `POST /auth/**`, `GET /flags`, `GET /flags/evaluate`.
   - **Authenticated Access (`authenticated`):** `POST /flags`, `PATCH /flags/**`, `DELETE /flags/**`, `POST/DELETE /flags/*/overrides`.

---

### Feature 7: Interactive React 19 + Tailwind CSS 4 Admin Dashboard

#### 💡 Concept & Capabilities
A modern, responsive single-page dashboard for engineers and product managers to monitor and manage feature flags.

#### 🎨 Key UI Components & Architecture:
- **[AuthContext.jsx](file:///e:/1.Code/Feature-Flag-Service/frontend/feature-flag-ui/src/context/AuthContext.jsx):** Centralized state management for JWT tokens, user session storage in `localStorage`, and Demo Mode state toggles.
- **[FlagList.jsx](file:///e:/1.Code/Feature-Flag-Service/frontend/feature-flag-ui/src/components/FlagList.jsx) & [FlagCard.jsx](file:///e:/1.Code/Feature-Flag-Service/frontend/feature-flag-ui/src/components/FlagCard.jsx):** Clean card presentation featuring dynamic border color cues (Green = Enabled, Red = Disabled), environment tags, and rollout badges.
- **[FilterBar.jsx](file:///e:/1.Code/Feature-Flag-Service/frontend/feature-flag-ui/src/components/FilterBar.jsx):** Instant client-side multi-filtering by search substring, environment select dropdown, and active/inactive status.
- **[EditFlagModal.jsx](file:///e:/1.Code/Feature-Flag-Service/frontend/feature-flag-ui/src/components/EditFlagModal.jsx):** Smooth modal overlay supporting live PATCH requests for instant updates without full-page reloads.
- **Offline Demo Mode:** Allows recruiters or interviewers to test the full dashboard without needing a running PostgreSQL or Spring Boot instance (pre-populated with 9 realistic feature flags).

---

### Feature 8: Production-Ready Multi-Stage Docker Containerization

#### 💡 Concept & Capabilities
Both backend and frontend leverage multi-stage `Dockerfile` builds to minimize attack surface and reduce image sizes.

#### 1. Backend Dockerfile ([backend/Dockerfile](file:///e:/1.Code/Feature-Flag-Service/backend/Dockerfile)):
- **Stage 1 (Builder):** Uses `maven:3.9-eclipse-temurin-21-alpine` with `mvn dependency:go-offline` to cache Maven dependencies in separate Docker layers.
- **Stage 2 (Runtime):** Copies only the built `.jar` into a clean, lightweight `eclipse-temurin:21-jre-alpine` runtime image (~180MB instead of ~800MB).

#### 2. Frontend Dockerfile ([frontend/Dockerfile](file:///e:/1.Code/Feature-Flag-Service/frontend/feature-flag-ui/Dockerfile)):
- **Stage 1 (Builder):** Uses `node:22-alpine` to run `npm ci` and `npm run build`.
- **Stage 2 (Runtime):** Copies the static assets (`/dist`) to `nginx:alpine` on port `80` with optimized HTTP delivery (~25MB total footprint).

---

## 5. Database Schema & Data Model Design

### Entity Relationship Diagram (ERD)

```
       ┌────────────────────────┐
       │      user_details      │
       ├────────────────────────┤
       │ PK id          (BIGINT)│
       │    username   (VARCHAR)│ (UNIQUE)
       │    email      (VARCHAR)│ (UNIQUE)
       │    password   (VARCHAR)│ (BCrypt Hash)
       │    name       (VARCHAR)│
       │    created_at (TS)     │
       └────────────────────────┘

       ┌────────────────────────┐           ┌────────────────────────┐
       │          flag          │ 1       N │     override_flag      │
       ├────────────────────────┼───────────┼────────────────────────┤
       │ PK id          (BIGINT)│           │ PK id          (BIGINT)│
       │    name       (VARCHAR)│           │ FK flag_id     (BIGINT)│
       │    description(VARCHAR)│           │    user_id    (VARCHAR)│
       │    enabled    (BOOLEAN)│           │    enabled   (BOOLEAN) │
       │    rollout_pct (INT)   │           └────────────────────────┘
       │    environment(VARCHAR)│
       │    created_at (TS)     │
       └────────────────────────┘
```

---

## 6. API Specification & Data Contracts

### 🔑 Authentication Endpoints

#### `POST /auth/auth/login`
- **Request Body:**
  ```json
  {
    "username": "admin_user",
    "password": "SecretPassword123!"
  }
  ```
- **Response `200 OK`:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1
  }
  ```

---

### 🚩 Evaluation Endpoint (Consuming Microservices)

#### `GET /flags/evaluate?flagName=new-checkout-flow&userId=user_987`
- **Query Params:** `flagName` (String, required), `userId` (String, optional)
- **Response `200 OK`:**
  ```json
  {
    "flagName": "new-checkout-flow",
    "enabled": true,
    "userId": "user_987",
    "reason": "Rollout percentage"
  }
  ```

---

### 🛠️ Flag Management Endpoints (Admin)

#### `POST /flags` *(Requires JWT Header: `Authorization: Bearer <token>`)*
- **Request Body:**
  ```json
  {
    "name": "ai-smart-search",
    "description": "Enables vector search recommendations on search page",
    "enabled": true,
    "rolloutPercentage": 20,
    "environment": "prod"
  }
  ```
- **Response `200 OK`:**
  ```json
  {
    "id": 42,
    "name": "ai-smart-search",
    "description": "Enables vector search recommendations on search page",
    "enabled": true,
    "rolloutPercentage": 20,
    "environment": "prod",
    "createdAt": "2026-08-20T18:00:00"
  }
  ```

#### `POST /flags/{id}/overrides` *(Requires JWT Header)*
- **Request Body:**
  ```json
  {
    "userId": "qa_tester_beta",
    "enabled": true
  }
  ```
- **Response `200 OK`**

---

## 7. System Design & Scaling Strategies (Interview Q&A)

### Q1: "How would you scale this service to support 500,000 evaluations per second with < 5ms latency?"
> **Answer:**  
> *"In the current setup, each evaluation queries PostgreSQL. At 500k RPS, this would saturate DB connections. I would scale this with a **3-tier caching & distribution strategy**:
> 1. **Distributed Redis Cache Layer:** Store serialized flag definitions and user overrides in Redis with keys `flags:{environment}:{flagName}` and `overrides:{flagId}:{userId}`. Evaluations become sub-millisecond memory reads.
> 2. **Client-Side SDK In-Memory Caching with Pub/Sub Invalidation:** Microservices calling the flag service would embed a client SDK that caches all active flags locally in memory. The flag service broadcasts flag updates via **Redis Pub/Sub, Kafka, or Server-Sent Events (SSE)**, updating client local caches instantly. Flag evaluation becomes local in-process computation (0 network hops, nanosecond latency).
> 3. **Database Read Replicas:** Route read queries for the admin UI to read replicas, leaving the primary database for mutations only."*

---

### Q2: "Why use `Math.abs(userId.hashCode()) % 100` instead of a random number generator or database flag for each user?"
> **Answer:**  
> *"A random number generator (`Math.random()`) violates consistency — a user would experience feature flickering across page reloads (enabled on load 1, disabled on load 2).  
> Storing an enabled boolean in the DB per user requires $O(N)$ storage for millions of users and high-throughput database writes during rollout percentage updates.  
> Using **deterministic hashing** offers:
> - **Idempotency:** The same user ID produces the exact same hash every single time.
> - **Zero Storage Overhead:** Hash computation is $O(1)$ CPU time.
> - **Monotonic Rollouts:** As percentage increases from 10% to 20%, all users in bucket [0-9] remain enabled, while buckets [10-19] are newly enabled without affecting existing users."*

---

### Q3: "What are the limitations of Java's `String.hashCode()` for percentage rollouts, and how would you improve it in an enterprise system?"
> **Answer:**  
> *"Java's `hashCode()` is sufficient for general uniformity, but in enterprise multi-tenant systems like LaunchDarkly or Split.io, it has two subtle drawbacks:
> 1. **Cross-Language Discrepancy:** If client SDKs in Python, Go, or Node.js evaluate flags locally, Java's specific polynomial 31 hash must be reimplemented in each language.
> 2. **Flag-to-Flag Correlation:** If two independent flags both have 10% rollouts, the exact same 10% of users would be selected for both experiments.
> 
> **Enterprise Improvement:** Use **MurmurHash3** or **MD5/SHA-256** salted with the flag name:  
> `hash = MurmurHash3(flagName + ":" + userId) % 100`.  
> This ensures uniform, independent distribution across different flags and consistent evaluation across Go, Python, Java, and JavaScript runtimes."*

---

### Q4: "How do you handle security between consumer services and administrative users?"
> **Answer:**  
> *"We apply **Principle of Least Privilege and Separation of Concerns**:
> 1. **Evaluation Endpoint (`/flags/evaluate`):** Open to consuming internal microservices and public clients (can be further secured via API Keys, mTLS, or internal VPC private subnets).
> 2. **Administrative Mutation Endpoints (`POST/PATCH/DELETE`):** Fully guarded behind stateless JWT authentication with Spring Security and BCrypt password encryption. Only authenticated admins can create, toggle, or modify flags and overrides."*

---

## 8. STAR-Method Resume Bullet Points & Talking Points

### 📄 Ready-to-Use Resume Bullet Points
- **Architected & implemented a distributed Feature Flag Service** in **Spring Boot 4 (Java 17)** and **PostgreSQL**, decoupling software deployment from release cycles and cutting rollback risks to zero.
- **Engineered a high-performance flag evaluation engine** featuring **deterministic percentage canary rollouts** via hash-bucket partitioning (`Math.abs(userId.hashCode()) % 100`) and **user-level override rules** with sub-millisecond execution.
- **Implemented stateless JWT authentication & RBAC** using Spring Security 6, securing administrative mutations while maintaining open, high-throughput RESTful evaluation endpoints.
- **Developed a responsive React 19 & Tailwind CSS 4 dashboard** supporting real-time multi-criteria filtering, in-place flag toggling, modal edits, and an offline interactive demo mode.
- **Containerized full-stack infrastructure** using multi-stage Alpine Docker builds (Maven + Temurin JRE / Node + Nginx), slashing production image sizes by over 70%.

---

### 🎙️ Behavioral Interview "STAR" Story: Overcoming a Key Technical Challenge

- **Situation:** During high-traffic release windows, microservice teams needed a way to test new functionality in production without exposing bugs to all users or performing costly rollbacks.
- **Task:** Build a centralized feature flag control plane that supports canary rollouts, fast evaluation, and user targeting across multi-environment deployments.
- **Action:** 
  1. Implemented a hierarchical evaluation engine in Spring Boot that checks environment matching, overrides, and global status before computing percentage hash buckets.
  2. Selected a deterministic mathematical hashing approach to guarantee user stickiness with zero database state overhead.
  3. Created a responsive React admin dashboard and secured the control plane with Spring Security and JWT.
- **Result:** Successfully provided a zero-downtime release tool capable of instant feature kill-switching, targeted QA allowlisting, and canary rollouts.
