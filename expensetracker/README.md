# Expense Tracker API

Spring Boot expense tracker API with:

- `/api/v1/users` CRUD
- `/api/v1/expenses` CRUD
- `X-User-Id` header-based expense scoping
- seeded mock users and expenses
- OpenAPI and Actuator endpoints

## Base URLs

- API base path: `/api/v1`
- OpenAPI JSON: `/api/v1/openapi`
- Swagger UI: `/api/v1/swagger-ui`
- Actuator health: `/api/v1/actuator/health`
- Actuator info: `/api/v1/actuator/info`

## Mock Users

The app seeds these users on startup:

- `11111111-1111-1111-1111-111111111111` - Alex Johnson
- `22222222-2222-2222-2222-222222222222` - Priya Sharma
- `33333333-3333-3333-3333-333333333333` - Marcus Lee

Use one of those IDs in the `X-User-Id` header for all expense endpoints.

Example:

```bash
curl -H "X-User-Id: 11111111-1111-1111-1111-111111111111" \
  http://localhost:8080/api/v1/expenses
```

## Local Run

Compile:

```bash
./gradlew compileJava
```

Run:

```bash
./gradlew bootRun
```

## Docker Run

Build the image:

```bash
docker build -t expense-tracker-api .
```

Run the container:

```bash
docker run --rm -p 8080:8080 expense-tracker-api
```

## Vercel

This repository is **not directly deployable to Vercel as-is**.

Why:

- this project is a long-running Spring Boot server
- Vercel runs serverless functions, not general Java app servers
- Vercel does not support deploying Docker images directly

If you still need the current official Vercel CLI deployment commands for a Vercel-compatible app, they are:

```bash
vercel --prod
```

Or for a prebuilt deployment:

```bash
vercel build
vercel deploy --prebuilt --prod
```

For this backend, use a platform that supports long-running Java services, such as:

- Render
- Railway
- Fly.io
- AWS ECS
- Google Cloud Run

## Main Endpoints

### Users

- `GET /api/v1/users`
- `GET /api/v1/users/{userId}`
- `POST /api/v1/users`
- `PUT /api/v1/users/{userId}`
- `DELETE /api/v1/users/{userId}`

### Expenses

All expense endpoints require `X-User-Id`.

- `GET /api/v1/expenses`
- `GET /api/v1/expenses/{expenseId}`
- `POST /api/v1/expenses`
- `PUT /api/v1/expenses/{expenseId}`
- `DELETE /api/v1/expenses/{expenseId}`

Optional header for create:

- `Idempotency-Key`

## Production Notes

- default storage is file-backed H2
- actuator exposure is limited to `health`, `info`, and `metrics`
- the Docker image runs as a non-root user
- CORS is configurable with `APP_CORS_ALLOWED_ORIGIN_PATTERNS`

## References

- Vercel deploy docs: https://vercel.com/docs/cli/deploy
- Vercel build docs: https://vercel.com/docs/cli/build
- Vercel runtimes docs: https://vercel.com/docs/functions/runtimes
- Vercel Docker support note: https://vercel.com/guides/does-vercel-support-docker-deployments
