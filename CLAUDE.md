# swagger-brake

A tool for detecting breaking changes in Swagger/OpenAPI contracts. Compares two API specifications and reports breaking changes that could affect API consumers. Includes a core library and a standalone CLI tool.

## Tech Stack

- Language: Java 21
- Build: Gradle (multi-project)
- Frameworks: Spring Context (DI), Jackson (JSON/YAML parsing), Swagger Parser
- Testing: JUnit 5, AssertJ, Mockito
- Quality: Checkstyle, SpotBugs, Lombok
- Packaging: Shadow JAR (fat jar for CLI)

## Commands

```bash
# Build everything (including fat CLI jar)
./gradlew clean build shadowJar

# Run tests
./gradlew test

# Run checks (Checkstyle + SpotBugs)
./gradlew check

# Build without tests
./gradlew clean build -x test
```

## Project Structure

- `swagger-brake/` - Core library: breaking change detection logic, rule implementations
- `swagger-brake-cli/` - CLI wrapper that uses the core library and produces a standalone fat jar
- `swagger-brake/src/main/java/` - Main source: rules, parsers, diff engine
- `swagger-brake/src/test/` - Unit tests

## Key Notes

- The core library and CLI are separate Gradle subprojects; changes to rules go in `swagger-brake/`
- The CLI fat jar is produced by the Shadow plugin and is the primary distribution artifact
- Lombok is used extensively — ensure annotation processing is enabled in your IDE
- Checkstyle config is in `config/checkstyle/`
