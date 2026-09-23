# url-shortner
A small Java URL-shortening service (Javalin + SQLite), built as a QA
Elective project to demonstrate ISO/IEC 25010 quality characteristics
through an automated CI/CD pipeline.


## Features

- `POST /api/shorten` — validate a long URL and return a Base62 short code
  (reuses the existing code if the URL was already shortened).
- `GET /{code}` — 302 redirect to the original URL, and increment its click
  count.
- URL validation rejects malformed input and disallowed schemes
  (`javascript:`, `file:`, etc.) before anything is persisted.
- SQLite persistence behind a repository interface, with an in-memory fake
  for fast unit testing.

## Tech stack

- **Language/build:** Java 21, Maven
- **Web framework:** Javalin 6, Jackson (with `JavaTimeModule`)
- **Persistence:** SQLite via JDBC (`sqlite-jdbc`), raw `PreparedStatement`
- **Testing:** JUnit 5, REST-assured/OkHttp, Javalin test tools
- **Quality gates:** JaCoCo (coverage), PITest (mutation testing),
  SpotBugs + PMD (static analysis), OWASP Dependency-Check (vulnerability
  scanning)
- **Packaging:** Maven Shade (fat jar), Docker (`eclipse-temurin:21-jre`)
- **CI/CD:** GitHub Actions (`CI.yml`)

## Architecture

```
UrlController (HTTP)
      │
      ▼
ShortenerService (business rules: validate, dedupe, encode, resolve)
      │              │
      ▼              ▼
UrlValidator    UrlRepository (interface)
                      │
        ┌─────────────┴─────────────┐
        ▼                           ▼
SqliteUrlRepository          FakeUrlRepository
   (JDBC / SQLite)             (in-memory, tests only)
```

`Base62Encoder` is a standalone, dependency-free utility used by
`ShortenerService` to turn a generated database id into a short code.

## Getting started

### Prerequisites

- JDK 21
- Maven 3.8+

### Build

```bash
mvn -B compile
```

### Run tests

```bash
mvn -B test
```

### Run the service locally

```bash
mvn -B package -DskipTests
java -jar target/url-shortener.jar
```

The service starts on `http://localhost:7000` and creates/uses a local
`url-shortener.db` SQLite file.

### Example requests

```bash
curl -X POST http://localhost:7000/api/shorten \
  -H "Content-Type: application/json" \
  -d '{"url":"https://example.com"}'
# -> 201 { "id": 1, "shortCode": "1", "longUrl": "https://example.com", ... }

curl -i http://localhost:7000/1
# -> 302 redirect to https://example.com
```

### Run with Docker

```bash
mvn -B package -DskipTests
docker build -t url-shortener .
docker run -p 7000:7000 url-shortener
```

## Testing layers

| Layer | Location | What it covers |
|---|---|---|
| Unit | `encoder/Base62EncoderTest`, `validation/UrlValidatorTest` | Pure logic, no I/O |
| Service (fake repo) | `service/ShortenerServiceTest` | Business rules against `FakeUrlRepository` |
| Integration | `repository/SqliteUrlRepositoryTest` | Real SQLite (temp file) persistence |
| HTTP / black-box | `controller/UrlControllerTest` | Full request/response cycle via a live Javalin instance |

## CI/CD pipeline (`CI.yml`)

Runs on every push/PR to `main`:

1. **build** — compile
2. **test** — run unit + integration tests, upload Surefire report
3. **quality** — SpotBugs, PMD, JaCoCo coverage check (≥ 80% line coverage)
4. **mutation** — PITest mutation testing (≥ 70% mutation score), upload
   report
5. **security** — OWASP Dependency-Check, upload report
6. **perf** — performance test profile
7. **package** — build the shaded jar and Docker image, push to
   `ghcr.io` on `main`

WTC-QMUUJRVA
