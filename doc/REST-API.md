# Calculator REST API

This document explains how to run the REST API and how to call its endpoints.

## Run the application

From the project root:

```bash
# Run with Maven
mvn spring-boot:run

# Or build and run the JAR
mvn clean package -DskipTests
java -jar target/calculator-cucumber-0.5.0.jar

# Or run the main class in your IDE: CalculatorRestApplication
```

By default the server listens on port 8080.

## Base URLs

- Base: `http://localhost:8080`
- Evaluate endpoint: `POST /api/v1/evaluate` → `http://localhost:8080/api/v1/evaluate`
- OpenAPI JSON: `/v3/api-docs` → `http://localhost:8080/v3/api-docs`
- Swagger UI: `/swagger-ui/index.html` → `http://localhost:8080/swagger-ui/index.html`

## Evaluate endpoint

POST `/api/v1/evaluate`

- Content-Type: `application/json`
- Body: JSON object containing an `ast` field describing the expression to evaluate.
- Response: JSON `{ "result": <number> }` on success or `{ "error": "..." }` on bad request.

Example AST format (JSON):

```json
{
  "ast": {
    "type": "operation",
    "op": "+",
    "args": [
      { "type": "number", "value": 1 },
      { "type": "operation", "op": "*", "args": [
        { "type": "number", "value": 2 },
        { "type": "number", "value": 3 }
      ] }
    ]
  }
}
```

Example curl:

```bash
curl -s -X POST http://localhost:8080/api/v1/evaluate \
  -H "Content-Type: application/json" \
  -d '{"ast":{"type":"operation","op":"+","args":[{"type":"number","value":1},{"type":"operation","op":"*","args":[{"type":"number","value":2},{"type":"number","value":3}]}]}}'
```

Expected response:

```json
{"result":7}
```

## Errors and status codes

- `200 OK`: evaluation successful
- `400 Bad Request`: malformed AST or illegal construction (mapped from `IllegalConstruction`)
- `415 Unsupported Media Type`: when request is not `application/json`
- `500 Internal Server Error`: unexpected server error
