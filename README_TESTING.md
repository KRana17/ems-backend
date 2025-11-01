Testing notes — Employee Management System (ems-backend)

Purpose
- Document how tests run locally in this project, why an in-memory H2 DB is used for tests, and how to run and troubleshoot tests.

Key files (test-time)
- src/test/resources/application.properties
  - Contains: spring.profiles.active=test
  - This activates the `test` profile when running tests (so Spring will also load application-test.properties).

- src/test/resources/application-test.properties
  - Contains the H2 in-memory database configuration used by tests (jdbc:h2:mem:testdb, ddl-auto=create-drop, dialect H2).
  - This prevents tests that load the Spring context from attempting to connect to a local MySQL instance.

Why tests used H2
- Spring Boot loads resources from `src/test/resources` on the test classpath. With `spring.profiles.active=test` set for tests, Spring will read `application-test.properties` and use the H2 settings.
- Some tests (e.g., Mockito-only tests or slice tests annotated with `@WebMvcTest`) do not start the JPA/DB layer at all; only `@SpringBootTest` or `@DataJpaTest` will attempt to initialize JPA and therefore need the DB config.

Which tests require a DB vs. which do not
- Don't hit the DB:
  - Mockito/unit tests (pure JUnit + Mockito, no Spring context)
  - @WebMvcTest slice tests (only web layer; repositories are mocked)
- Will initialize a DB (and therefore use H2 in tests):
  - @SpringBootTest (loads full Spring context)
  - @DataJpaTest (repository/JPA slice)

How to run tests
- Run the full test suite (uses test resources):
```bash
./gradlew test --no-daemon
```
- Run a single test class (fast, avoids unrelated failures):
```bash
./gradlew test --tests "com.example.ems.impl.DepartmentServiceImplTest"
```
- Run a single test method:
```bash
./gradlew test --tests "com.example.ems.impl.DepartmentServiceImplTest.testCreateDepartment"
```
- Run tests with more Gradle logging if troubleshooting:
```bash
./gradlew test --info
```

Troubleshooting: MySQL connection errors during tests
- Symptom: stack trace shows a JDBCConnectionException, CommunicationsException, or messages about failing to open JDBC connection when running tests.
- Quick checks:
  1) Ensure `src/test/resources/application.properties` exists and contains `spring.profiles.active=test`.
  2) Ensure `src/test/resources/application-test.properties` exists and contains H2 config. If you accidentally placed it elsewhere (e.g., `src/test/test/application-test.properties`), move it:
```bash
mv src/test/test/application-test.properties src/test/resources/application-test.properties
```
  3) Remove stray test folder if you want to clean up:
```bash
rm -rf src/test/test
```
  4) Verify the H2 dependency is available on the test classpath (in `build.gradle`). It's currently declared as:
```gradle
runtimeOnly 'com.h2database:h2'
```
If you want to make intent explicit, change to `testRuntimeOnly 'com.h2database:h2'` (optional).

If a test still tries to use MySQL
- Check if any tests set an active profile explicitly with `@ActiveProfiles` (none in the repo by default). If a test sets `spring.profiles.active=dev` or the environment overrides the profile, that could force MySQL.
- Check for environment variables (SPRING_DATASOURCE_URL, SPRING_PROFILES_ACTIVE) that override test properties in your shell/CI.

Recommendations (optional changes you may consider)
- Keep the `test` profile + `application-test.properties` for an embedded H2 database to provide reliable, fast tests.
- Consider updating `build.gradle` to use `testRuntimeOnly 'com.h2database:h2'` to restrict H2 to tests only.
- Delete the stray `src/test/test` folder to avoid confusion.
- If you prefer per-test control, annotate test classes with `@ActiveProfiles("test")` instead of a global `spring.profiles.active` in `src/test/resources/application.properties`.

Why we left code unchanged
- Per your request (Plan B), no runtime or dependency changes were applied to the project; only documentation was added to explain the current configuration and troubleshooting steps.

If you want me to apply the optional changes (remove stray folder, change dependency scope in `build.gradle`, or rename/annotate profiles), tell me which items and I'll make the changes and run the test-suite.

PR branch: feature/test-h2-config
