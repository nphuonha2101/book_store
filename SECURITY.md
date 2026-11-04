# Security Policy

## Reporting a vulnerability

Please do **not** open a public GitHub issue for security problems.
Report vulnerabilities privately through the repository's **Security →
Advisories → Report a vulnerability** page, or contact the maintainers
directly.

Include reproduction steps, affected versions and the potential impact. We aim
to acknowledge reports within 5 business days.

## Supported versions

Only the latest commit on the default branch receives security fixes.

## Deployment hardening

- Run behind a TLS-terminating reverse proxy and set
  `SESSION_COOKIE_SECURE=true`.
- Generate a unique `JWT_SECRET` with `openssl rand -base64 48`; rotate it if it
  may have leaked.
- Restrict `CORS_ALLOWED_ORIGINS` to trusted storefront origins.
- Keep MySQL and Redis on a private network; never expose them publicly.
- Store `FIREBASE_CREDENTIALS_PATH`, PayOS keys and SMTP credentials as secrets.
- Use `SPRING_PROFILES_ACTIVE=prod` (schema validation, secure cookies, no SQL
  logging) and manage schema migrations instead of `ddl-auto=update`.
- Expose only `/actuator/health` and `/actuator/info`.
