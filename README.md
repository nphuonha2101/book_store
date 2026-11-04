# Book Store API

REST API and admin portal for an online bookstore. It powers the Next.js
storefront and also serves a Thymeleaf-based admin dashboard.

## Stack

| Layer | Technology |
| --- | --- |
| Runtime | Java 21 |
| Framework | Spring Boot 3.5.16 (Spring Framework 6, Spring Security 6) |
| Persistence | Spring Data JPA + MySQL 8 |
| Cache / session | Redis 7 (Spring Cache, Spring Session) |
| Admin UI | Thymeleaf + layout dialect + Bootstrap |
| Auth | JWT (HS256) for the storefront, form login + session for admins, Google OAuth2 |
| Integrations | Firebase Admin (optional), PayOS (optional), Vosk speech-to-text (optional) |
| Build | Maven Wrapper |

## Quick start with Docker

```bash
cp .env.example .env
# Generate a real signing key and put it in .env:
openssl rand -base64 48
docker compose up --build
```

- API: <http://localhost:8080>
- Health: <http://localhost:8080/actuator/health>
- Admin: <http://localhost:8080/admin/login>

MySQL and Redis are provisioned by Compose with health checks and persistent
volumes. `JPA_DDL_AUTO=update` is the default for first-run convenience; switch
to `validate` once schema migrations are managed externally.

### Demo data (seed)

`APP_SEED_ENABLED=true` loads an idempotent demo catalogue on an empty database:
6 categories, 15 books with galleries, 3 sliders, 3 ribbons, 3 vouchers, demo
customers (addresses, cart, wishlist, reviews) and an admin account.

| Role | Email | Password |
| --- | --- | --- |
| Customer | `demo@npbookstore.local` | `Demo@123456` |
| Customer | `reviewer@npbookstore.local` | `Review@123456` |
| Admin | `admin@npbookstore.local` | `Admin@123456` |

The seed runs by default in the `dev` profile and in Compose. Set
`APP_SEED_ENABLED=false` (and override `SEED_*` values) for real production
deployments.

## Local development

Prerequisites: Java 21, MySQL 8, Redis 7.

```bash
# Start dependencies (or use docker compose up mysql redis)
export JWT_SECRET="$(openssl rand -base64 48)"
./mvnw spring-boot:run
```

The default profile is `dev` and reads `application-dev.yml` (verbose SQL,
Thymeleaf cache off, localhost frontend). Create a database before starting:

```sql
CREATE DATABASE book_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## Configuration

Configuration is environment-first (`application.yml`). Important variables:

| Variable | Default | Description |
| --- | --- | --- |
| `PORT` | `8080` | HTTP port |
| `DB_URL` | local MySQL URL | JDBC URL |
| `DB_USERNAME` / `DB_PASSWORD` | `root` / empty | Database credentials |
| `REDIS_HOST` / `REDIS_PORT` | `localhost` / `6379` | Redis connection |
| `JWT_SECRET` | dev-only value in `dev` | **Required in production.** Base64, >= 32 bytes |
| `JWT_EXPIRATION_MS` | `86400000` | Token lifetime (24h) |
| `FRONTEND_URL` | `http://localhost:3000` | OAuth2 redirect + PayOS return URL |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:5173` | Comma-separated allowed origins |
| `MAIL_*` | Gmail SMTP | Password-reset mail |
| `FIREBASE_CREDENTIALS_PATH` | empty | `classpath:`/`file:` service account; integrations skipped when empty |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | `not-configured` | Google OAuth2 login |
| `PAYOS_CLIENT_ID` / `PAYOS_API_KEY` / `PAYOS_CHECKSUM_KEY` | empty | PayOS payments |
| `VOSK_MODEL_PATH` | `models/vosk-model-small-vn-0.4` | Filesystem path to the Vosk model |
| `APP_SEED_ENABLED` | `false` (`true` in dev/Compose) | Load the demo catalogue/accounts on an empty database |
| `SEED_ADMIN_EMAIL` / `SEED_ADMIN_PASSWORD` | `admin@npbookstore.local` / `Admin@123456` | Demo admin credentials |
| `SEED_USER_EMAIL` / `SEED_USER_PASSWORD` | `demo@npbookstore.local` / `Demo@123456` | Demo customer credentials |

Production profile (`SPRING_PROFILES_ACTIVE=prod`): `ddl-auto=validate`,
Thymeleaf cache on, secure session cookie, no SQL logging.

## Tests

```bash
./mvnw test
```

Tests run against an in-memory H2 database (`test` profile) and do not need
MySQL or Redis. They cover application context startup and JWT generation,
validation and key-strength checks.

## API overview

All endpoints are under `/api/v1`.

| Group | Base path | Auth |
| --- | --- | --- |
| Auth | `/login`, `/register`, `/forgot-password`, `/auth/me`, `/auth/update` | public / JWT |
| Books | `/books/all`, `/books/{id}`, `/books/search`, `/books/filter`, `/books/suggest` | public |
| Categories / sliders / ribbons | `/categories`, `/sliders`, `/ribbons` | public |
| Cart | `/auth/carts/**` | JWT |
| Wishlist | `/wishlists/**` | public |
| Addresses | `/addresses/**` | public (owner scoped by payload) |
| Orders | `/auth/orders/**` | JWT + ownership check |
| Vouchers | `/auth/vouchers/**` | JWT |
| Notifications / FCM | `/auth/notifications/**`, `/auth/fcm/**` | JWT |
| Reviews | `/reviews/**` | public / JWT |
| Payment webhook | `/webhook/payos` | public callback |

Responses use a consistent envelope:

```json
{ "success": true, "message": "...", "statusCode": 200, "data": {}, "pagination": null }
```

## Security and production notes

- JWT secret is validated at startup (Base64, minimum 256 bits) and invalid or
  expired tokens are rejected with a JSON `401` instead of a server error.
- CORS is centralised in the security filter chain and driven by
  `CORS_ALLOWED_ORIGINS`.
- Admin routes use session authentication with CSRF protection enabled.
- Actuator exposes only `health` and `info`; all other actuator endpoints are denied.
- Order detail, cancellation and failure callbacks verify that the order belongs
  to the authenticated user.
- Firebase, PayOS, mail and Vosk are optional: missing credentials disable the
  feature instead of preventing startup.
- Run behind a TLS-terminating proxy, set `SESSION_COOKIE_SECURE=true`, rotate
  `JWT_SECRET`, and restrict database/Redis network access.
- The bundled Vosk model is large (~50 MB). It is copied to `/app/models` in the
  Docker image rather than being embedded in the jar.

### Upgrade notes

- Java 17 → **21**.
- Spring Boot 3.3.5 → **3.5.16** (latest 3.5 line).
- Firebase Admin 9.4.3 → **9.11.0**, jjwt 0.12.6 → **0.13.0**,
  Thymeleaf layout dialect 3.3 → **3.4**, MySQL driver managed by Boot.
- Removed obsolete `thymeleaf-extras-java8time` and the standalone validation
  API (replaced by `spring-boot-starter-validation`).
- Configuration moved from an uncommitted `application.properties` to a
  committed, environment-driven `application.yml` + profiles.
- Dockerfile is now a multi-stage build running the packaged jar as a non-root
  user; Compose includes MySQL and Redis.

## License

MIT
