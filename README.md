# Spring Boot REST API (CRUD Operation)

A RESTful API built with Spring Boot for managing products. This API provides endpoints for creating, reading, updating, and deleting product information.

## Features

- CRUD operations for products
- Category-based product filtering
- Price-based product filtering
- Name-based product search
- Global exception handling
- Input validation
- MySQL database integration

## Technologies

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL
- Maven
- Jakarta Validation

## Prerequisites

- JDK 21 or later
- Maven 3.9+
- MySQL Server

## Database Configuration

The application is configured to connect to a MySQL database. Update the following properties in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=MYSQL DATABASE URL
spring.datasource.username=DATABASE USERNAME
spring.datasource.password=DATABASE PASSWORD
```

## Building and Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```bash
   ./mvnw clean install
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on port 8080.

## API Endpoints

### Products

- GET `/api/products` - Get all products
- GET `/api/products/{id}` - Get a specific product by ID
- GET `/api/products/category/{categoryName}` - Get products by category
- GET `/api/products/price/{maxPrice}` - Get products with price less than or equal to maxPrice
- GET `/api/products/search?name={name}` - Search products by name
- POST `/api/products` - Create a new product
- PUT `/api/products/{id}` - Update an existing product
- DELETE `/api/products/{id}` - Delete a product

### Request and Response Samples (with validation)

#### Product object structure
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product Description",
  "price": 99.99,
  "category": "Category Name"
}
```

Note:
- For create (`POST`), omit the `id` field in the request body. The server generates it.
- For update (`PUT`), the `id` comes from the path parameter; you still send the other fields in the body.

---

#### GET /api/products
Request:
```bash
curl -X GET http://localhost:8080/api/products
```
Response 200 OK:
```json
[
  {
    "id": 1,
    "name": "iPhone 15",
    "description": "Latest Apple smartphone",
    "price": 999.99,
    "category": "Electronics"
  },
  {
    "id": 2,
    "name": "Office Chair",
    "description": "Ergonomic mesh chair",
    "price": 199.0,
    "category": "Furniture"
  }
]
```

---

#### GET /api/products/{id}
Request:
```bash
curl -X GET http://localhost:8080/api/products/1
```
Response 200 OK:
```json
{
  "id": 1,
  "name": "iPhone 15",
  "description": "Latest Apple smartphone",
  "price": 999.99,
  "category": "Electronics"
}
```
Response 404 Not Found (when id does not exist):
```json
{
  "timestamp": "2025-11-30T00:17:00.000+00:00",
  "message": "Product not found for this id :: 999",
  "path": "uri=/api/products/999"
}
```

---

#### GET /api/products/category/{categoryName}
Request:
```bash
curl -X GET http://localhost:8080/api/products/category/Electronics
```
Response 200 OK:
```json
[
  {
    "id": 1,
    "name": "iPhone 15",
    "description": "Latest Apple smartphone",
    "price": 999.99,
    "category": "Electronics"
  }
]
```

---

#### GET /api/products/price/{maxPrice}
Request:
```bash
curl -X GET http://localhost:8080/api/products/price/250
```
Response 200 OK:
```json
[
  {
    "id": 2,
    "name": "Office Chair",
    "description": "Ergonomic mesh chair",
    "price": 199.0,
    "category": "Furniture"
  }
]
```

---

#### GET /api/products/search?name={name}
Request:
```bash
curl -G http://localhost:8080/api/products/search --data-urlencode "name=iphone"
```
Response 200 OK:
```json
[
  {
    "id": 1,
    "name": "iPhone 15",
    "description": "Latest Apple smartphone",
    "price": 999.99,
    "category": "Electronics"
  }
]
```

---

#### POST /api/products (create)
Request:
```bash
curl -X POST http://localhost:8080/api/products \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Mechanical Keyboard",
    "description": "Hot-swappable RGB keyboard",
    "price": 129.99,
    "category": "Electronics"
  }'
```
Response 201 Created:
```json
{
  "id": 3,
  "name": "Mechanical Keyboard",
  "description": "Hot-swappable RGB keyboard",
  "price": 129.99,
  "category": "Electronics"
}
```
Validation error 400 Bad Request (blank name, negative price):
```bash
curl -X POST http://localhost:8080/api/products \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "",
    "description": "Some description",
    "price": -10,
    "category": "Electronics"
  }'
```
Response 400 Bad Request:
```json
{
  "name": "Product name is required",
  "price": "Product price must be positive"
}
```

---

#### PUT /api/products/{id} (update)
Request:
```bash
curl -X PUT http://localhost:8080/api/products/3 \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Mechanical Keyboard (V2)",
    "description": "Gasket mount, PBT keycaps",
    "price": 149.99,
    "category": "Electronics"
  }'
```
Response 200 OK:
```json
{
  "id": 3,
  "name": "Mechanical Keyboard (V2)",
  "description": "Gasket mount, PBT keycaps",
  "price": 149.99,
  "category": "Electronics"
}
```
Validation error 400 Bad Request (same structure as POST):
```json
{
  "name": "Product name is required",
  "price": "Product price must be positive"
}
```
Not found 404 (when id does not exist):
```json
{
  "timestamp": "2025-11-30T00:17:00.000+00:00",
  "message": "Product not found for this id :: 999",
  "path": "uri=/api/products/999"
}
```

---

#### DELETE /api/products/{id}
Request:
```bash
curl -X DELETE http://localhost:8080/api/products/3
```
Response 200 OK:
```json
{
  "deleted": true
}
```
Not found 404:
```json
{
  "timestamp": "2025-11-30T00:17:00.000+00:00",
  "message": "Product not found for this id :: 999",
  "path": "uri=/api/products/999"
}
```

---

### Error handling summary
The API uses a global exception handler that returns structured responses:
- 404 Not Found for missing resources:
```json
{
  "timestamp": "2025-11-30T00:17:00.000+00:00",
  "message": "Product not found for this id :: 999",
  "path": "uri=/api/products/999"
}
```
- 400 Bad Request for validation errors (map of `field` -> `message`):
```json
{
  "name": "Product name is required",
  "price": "Product price must be positive"
}
```
- 500 Internal Server Error (unexpected errors):
```json
{
  "timestamp": "2025-11-30T00:17:00.000+00:00",
  "message": "An unexpected error occurred",
  "details": "<exception message>"
}
```

### Validation rules
- `name`: must not be blank
- `price`: must be positive (greater than 0)

## Development

The project uses the following folder structure:
```
src/main/java/
    └── com.learnwithiftekhar.spring_boot_rest_api/
        ├── controller/      # REST controllers
        ├── model/          # Data models
        ├── repository/     # Data access layer
        ├── service/        # Business logic
        └── exception/      # Exception handling
```

## Containerization (Docker & Docker Compose)

This project ships with a production-friendly multi-stage `Dockerfile` and a `docker-compose.yml` for running the API with a MySQL database.

### Prerequisites
- Docker 24+
- Docker Compose v2 (usually available as `docker compose`)

### Dockerfile overview
Multi-stage build using Temurin JDK/JRE 21 on Alpine:
- Stage 1 (dependencies): caches Maven dependencies
- Stage 2 (builder): packages the app into a fat JAR
- Stage 3 (runtime): copies the JAR into a small JRE image and runs it on port 8080

Build arguments you can override:
- `BASE_JDK_IMAGE` (default: `eclipse-temurin:21-jdk-alpine`)
- `BASE_JRE_IMAGE` (default: `eclipse-temurin:21-jre-alpine`)
- `JAR_FILE` (default: `*.jar` — the packaged jar in `target/`)

Ports:
- Exposes `8080` inside the container.

### Build a local image
```bash
docker build -t product-service:latest .
```

Optional (override base images):
```bash
docker build \
  --build-arg BASE_JDK_IMAGE=eclipse-temurin:21-jdk \
  --build-arg BASE_JRE_IMAGE=eclipse-temurin:21-jre \
  -t product-service:latest .
```

### Run the container against an existing MySQL
If you already have MySQL running on your host, point the app to it via env vars. The app reads:
- `DB_HOST` (default: `localhost`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: `1234`)

```bash
docker run --name product-service \
  -e DB_HOST=host.docker.internal \
  -e DB_USER=root \
  -e DB_PASSWORD=1234 \
  -p 8080:8080 \
  product-service:latest
```
Note: on Linux you might need to use your host IP instead of `host.docker.internal`.

Verify:
```bash
curl http://localhost:8080/api/products
```

### Using Docker Compose (app + MySQL)
The included `docker-compose.yml` defines two services: `mysqldb` (MySQL 8) and `app` (this API).

Key points:
- MySQL service creates database `productdb` with user `ifte` and password `1234` (see `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`).
- The API service exposes port `8080` and depends on MySQL health.
- Application uses env vars `DB_HOST`, `DB_USER`, `DB_PASSWORD` to connect.

Important: ensure the API's `DB_PASSWORD` matches MySQL's `MYSQL_PASSWORD`.
In the provided compose, MySQL uses `MYSQL_PASSWORD=1234`, so set `DB_PASSWORD=1234` as well.

Start services:
```bash
docker compose up -d
```

Check status and logs:
```bash
docker compose ps
docker compose logs -f app
```

Quick test after startup:
```bash
curl http://localhost:8080/api/products
```

Stop and remove containers (data is kept in the named volume `mysql-data`):
```bash
docker compose down
```

Full cleanup (also removes the MySQL named volume!):
```bash
docker compose down -v
```

### Environment configuration mapping
The Spring datasource is configured in `src/main/resources/application.properties` as:
```properties
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:3306/productdb
spring.datasource.username=${DB_USER:root}
spring.datasource.password=${DB_PASSWORD:1234}
```
- When running with Compose, `DB_HOST=mysqldb` (the service name) so the app can reach MySQL by DNS within the compose network.
- Credentials must match what MySQL is initialized with.

### Troubleshooting
- Port already in use: change host ports (`8080:8080` or `3306:3306`) in `docker-compose.yml` or stop the conflicting service.
- First startup may take a bit until MySQL is healthy; the app waits via `depends_on.conditions` but you may still see one reconnection attempt in logs.
- Authentication failures: ensure `DB_USER`/`DB_PASSWORD` match `MYSQL_USER`/`MYSQL_PASSWORD`. With the provided config, both should be `ifte` and `1234` respectively.
- Connecting from container to host DB on Linux: replace `host.docker.internal` with your host IP (e.g., `172.17.0.1`).


## Contact

- LinkedIn: https://www.linkedin.com/in/hossain-md-iftekhar
- YouTube: https://www.youtube.com/@learnWithIfte
