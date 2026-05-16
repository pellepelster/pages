# Kotlin Project — Claude Code Guide

## Build System

- Use **Gradle with Kotlin DSL** (`build.gradle.kts`) exclusively — no Groovy DSL.
- Keep dependency versions in `gradle/libs.versions.toml` (version catalog).
- Run builds via `./gradlew` wrapper — never a system-installed Gradle.

```bash
./gradlew build          # compile + test
./gradlew test           # tests only
./gradlew check          # tests + linting
./gradlew run            # run application (if applicable)
```

## Code Style

- Enforce **ktlint** for formatting: `./gradlew ktlintCheck` / `./gradlew ktlintFormat`.
- Enforce **detekt** for static analysis: `./gradlew detekt`.
- Follow the [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html).
- 4-space indentation, no trailing whitespace, newline at EOF.
- Max line length: 120 characters.

## Project Structure

```
src/
  main/kotlin/com/example/
    domain/          # pure business logic, no framework deps
    application/     # use cases / services
    infrastructure/  # DB, HTTP, external systems
    api/             # entry points (REST controllers, CLI, etc.)
  test/kotlin/com/example/
    unit/            # fast, isolated unit tests
    integration/     # tests requiring real infrastructure
  testFixtures/kotlin/ # shared test helpers and builders
```

## Kotlin Idioms

- Prefer **data classes** for value objects and DTOs.
- Use **sealed classes** for algebraic types and result types — not exceptions for control flow.
- Prefer **`val`** over `var`; treat mutability as a red flag that needs justification.
- Use **extension functions** to add behavior to existing types rather than utility classes.
- Use **`object`** declarations for singletons and companion objects for factory methods.
- Use **`require()` / `check()` / `error()`** for precondition checks instead of throwing raw exceptions.
- Avoid nullable types (`T?`) at domain boundaries — convert to `Result<T>` or sealed types early.
- Prefer **`when` expressions** over chains of `if/else if`.

## Null Safety

- Never use `!!` in production code — treat it as a bug.
- Use `?.let`, `?:`, `requireNotNull()`, or safe-cast `as?` instead.
- Annotate Java interop boundaries with `@NonNull` / `@Nullable` where needed.

## Coroutines

- Use **structured concurrency**: always launch inside a `CoroutineScope`, never `GlobalScope`.
- Prefer `suspend` functions over callbacks or `Future`/`CompletableFuture`.
- Use `Dispatchers.IO` for blocking I/O; `Dispatchers.Default` for CPU-bound work.
- Use `Flow` for reactive streams — prefer cold flows; use `SharedFlow`/`StateFlow` only when you need hot behavior.
- Test coroutines with `kotlinx-coroutines-test` and `runTest`.

## Testing

- **JUnit 5** as the test runner; **Kotest assertions** (`shouldBe`, `shouldThrow`) for readability.
- **MockK** for mocking — never Mockito in Kotlin code.
- Test class naming: `FooTest` for unit, `FooIntegrationTest` for integration.
- One public `@Test` function per behavior — descriptive names in backtick strings are encouraged.
- Keep unit tests free of I/O and Spring context loading.
- Use `@TestContainers` with Testcontainers for integration tests requiring real databases.

```kotlin
class OrderServiceTest {
    @Test
    fun `placing an order with insufficient stock throws InsufficientStockException`() { ... }
}
```

## Dependencies to Prefer

| Purpose | Library                                                                                |
|---|----------------------------------------------------------------------------------------|
| HTTP client | Ktor Client                                                                            |
| HTTP server | Ktor Server                                                                            |
| Serialization | `kotlinx.serialization`                                                                |
| DB access | Exposed                                                                                |
| Logging | io.github.oshai:kotlin-logging-jvm (https://github.com/oshai/kotlin-logging) + Logback |
| Testing assertions | Kotest assertions                                                                      |
| Mocking | MockK                                                                                  |
| Containers in tests | Testcontainers                                                                         |

## Logging

```kotlin
private val log = KotlinLogging.logger {}

// structured — no string interpolation at the call site
log.info { "Order placed orderId=$orderId customerId=$customerId" }
```

- Never use `System.out.println` or `e.printStackTrace()`.
- Log at `DEBUG` for internal state, `INFO` for domain events, `WARN` for recoverable anomalies, `ERROR` only for actionable failures.

## Error Handling

- Use `Result<T>` or a domain-specific sealed type for expected failures.
- Reserve exceptions for truly unexpected conditions.
- Never swallow exceptions silently — at minimum log them.

## CI Checklist

Before merging, the following must pass:

- [ ] `./gradlew ktlintCheck`
- [ ] `./gradlew detekt`
- [ ] `./gradlew test`
- [ ] `./gradlew integrationTest` (if applicable)
- [ ] No `!!` operator in diff
- [ ] No `GlobalScope` in diff
- [ ] No new `TODO` / `FIXME` without a linked issue
