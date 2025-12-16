# Copilot / AI agent instructions — spring-boot-crud-rest-api

Purpose: Help an AI coding agent be productive quickly in this repository. This document is intentionally concise and action-oriented; reference the files listed below for examples.

## Big picture (what this project is)
- Single-module Spring Boot 3.4.5 REST API (Java 21) that manages `Product` entities.
- Architecture: HTTP layer -> `controller` -> `service` -> `repository` (Spring Data JPA) -> MySQL DB.
- Data flow example: `ProductController#createProduct` -> `ProductService#saveProduct` -> `ProductRepository.save`.

## Key files and what they show (quick map)
- `src/main/java/.../controller/ProductController.java` — REST endpoints, request/response shapes, validation usage.
- `src/main/java/.../service/ProductService.java` — business layer (thin wrapper around repository).
- `src/main/java/.../repository/ProductRepository.java` — JPA repository with derived query methods (e.g., `findByCategory`, `findByNameContainingIgnoreCase`).
- `src/main/java/.../model/Product.java` — entity fields and validation (`@NotBlank`, `@Positive`).
- `src/main/java/.../exception/GlobalExceptionHandler.java` — how errors are returned (400 map field->message, 404 with timestamp/message/path, 500 with generic message).
- `src/main/resources/application.properties` — DB connection overrides (environment variables names and defaults).
- `Dockerfile` — multi-stage build and copy-of-jar flow (build args: `BASE_JDK_IMAGE`, `BASE_JRE_IMAGE`, `JAR_FILE`).
- `docker-compose.yml` — local dev stack (service names: `mysqldb`, `app`) and DB credentials used in compose.

## How to build, run, and test (explicit commands)
- Build: `./mvnw clean install` (or `./mvnw -DskipTests package` to skip tests).
- Run locally: `./mvnw spring-boot:run` — app starts on port 8080.
- Run tests: `./mvnw test` (note: only a basic context test exists).
- Docker (local): `docker compose up --build` (uses `docker-compose.yml` to start MySQL and the app).
- Debugging tips: SQL is visible (`spring.jpa.show-sql=true` in `application.properties`). To reproduce DB behavior use the compose stack which sets `DB_HOST=mysqldb`.

## Environment variables used by the app
- DB_HOST (default `localhost` or `mysqldb` when using compose)
- DB_NAME (default `productdb`)
- DB_USER (default `root` or `ifte` in compose)
- DB_PASSWORD (default `1234`)
These variables are consumed via `application.properties` placeholders.

## Patterns & conventions an agent should follow
- Use Spring's standard controller->service->repository layering. Controllers perform validation (`@Valid`), services do lookups & changes, and repositories use method-name queries.
- Prefer reusing `ResourceNotFoundException` for 404s (see `ProductController#getProductById`).
- Validation errors return a `Map<String,String>` of field -> message (see `GlobalExceptionHandler#handleValidationExceptions`), so tests should assert that shape.
- Repository queries: Add derived methods in `ProductRepository` (e.g., `findByXyz...`) before writing custom @Query methods.
- Update semantics: `PUT /api/products/{id}` loads the entity, mutates fields explicitly, then saves (do not replace with a blind save of the request body unless you preserve `id`).

## What to test (practical test targets)
- Controller validation paths (missing name / negative price -> 400 map response).
- Not-found behavior for `GET/PUT/DELETE` returns 404 with the same message structure.
- Repository query behaviors: `findByCategory`, `findByPriceLessThanEqual`, `findByNameContainingIgnoreCase` (case-insensitive name search).

## Small PR guidance for contributors
- Add unit/integration tests for bug fixes or new endpoints (tests currently minimal).
- Update `README.md` with any environment or run-command changes.
- If adding DB schema-breaking changes, include a lightweight migration plan (or seed data) for local `docker compose` runs.

## Examples (copyable) — interact with API
- Get all products: `curl -X GET http://localhost:8080/api/products`
- Create: `curl -X POST http://localhost:8080/api/products -H 'Content-Type: application/json' -d '{"name":"X","price":10.0,"category":"C"}'`

---
If anything above is unclear or you want this expanded into an `AGENT.md` or CI checks, tell me where you want more detail (examples to add: integration test snippets, GitHub Actions workflow examples, or a standard PR checklist).