# Changelog

## 2.7.0-SNAPSHOT
### Migration Notes
**No breaking changes—default behavior unchanged.** 

The new `--strict-validation` option provides flexibility when customer specs are missing the `type` field on schemas. Use `--strict-validation=false` to allow default types (object/null) for comparison instead of failing. Default is `true`, which preserves current behavior.

### New Features
* Added `--strict-validation` CLI option (default: `true`) to handle specs missing type field by allowing default types (object/null) for comparison. This gives consumers flexibility to scan valid OpenAPI 3.1.x specs even when type fields are missing. See [CLI documentation](../cli/README.md#strict-validation-mode) for details.
* Added OpenAPI 3.1.x specification support as a natural extension to the library:
  * Type array support (e.g., `["string", "null"]`) in addition to OpenAPI 3.0.x single type strings
  * Version-aware nullable handling: OpenAPI 3.0.x uses `nullable: true` flag, while 3.1.x uses type arrays with `"null"`
  * Automatic version detection via `OpenApiVersion` enum and `OpenApiVersionContext`
  * Union type detection with warnings for multiple non-null types
  * Full backward compatibility with Swagger 2.0 and OpenAPI 3.0.x specifications
* Added `--max-log-serialization-depth` CLI option (and `maxLogSerializationDepth` configuration) to control depth of object serialization in logs (1-20 range, default: 3). See [Configuration documentation](../configuration/README.md#configuring-log-serialization-depth) for details.

### Bug Fixes
* Fixed StackOverflowError when processing OpenAPI 3.1.x schemas with circular references (e.g., Business → AuctionNumber → Business)

### Enhancements
* Implemented `SafeSwaggerSerializer` with ThreadLocal-based cycle detection and depth limiting for safe logging of swagger-parser objects. Optional metrics logging available via `SWAGGER_BRAKE_ENABLE_METRICS_LOGGING=true` environment variable (see [Troubleshooting](../troubleshooting/README.md#debugging-logging-issues)).
* Added version-aware type resolution with `OpenApiVersionContext` to differentiate between OpenAPI 3.0.x and 3.1.x specifications
* Enhanced nullable type detection supporting both OpenAPI 3.0.x (`nullable` flag) and 3.1.x (type arrays with `"null"`)

### Build
* Added Checkstyle RegexpSingleline rule to prevent future usage of `toString()` on swagger-parser objects that could cause StackOverflowError
* Added JaCoCo coverage verification for OpenAPI 3.1.x related classes (80% coverage requirement)

### Testing
* Added comprehensive integration test suite for OpenAPI 3.1.x features (type arrays, version detection, circular references)
* Added backward compatibility regression test suite ensuring Swagger 2.0 and OpenAPI 3.0.x continue to work correctly
* Spec v2 Validation/RequestParam, updated test files to reflect https://github.com/OAI/OpenAPI-Specification/blob/main/versions/2.0.md#parameter-object

## 2.5.0-SNAPSHOT
TBD

## 2.4.0
### CLI and Core
* [#107](https://github.com/docktape/swagger-brake/issues/107) Include version number into report and filename
* [#97](https://github.com/docktape/swagger-brake/issues/97) False negatives when removing deprecated properties from models
* [#96](https://github.com/docktape/swagger-brake/issues/96) Swagger brake cli jar in maven repository
* [#82](https://github.com/docktape/swagger-brake/issues/82) JUnit 4 migration to JUnit 5
* [#65](https://github.com/docktape/swagger-brake/issues/65) Schema class attributeRequiredMap.get(attrToSearchFor); returning null

CLI download from Maven Central: [swagger-brake-cli-2.4.0-cli.jar](https://repo1.maven.org/maven2/io/docktape/swagger-brake-cli/2.4.0/swagger-brake-cli-2.4.0-cli.jar)

CLI download from GitHub: [swagger-brake-cli-2.4.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/2.4.0/swagger-brake-cli-2.4.0-cli.jar)

### Maven plugin
* [#22](https://github.com/docktape/swagger-brake-maven-plugin/issues/22) Upgrade Swagger Brake to 2.3.0
* [#21](https://github.com/docktape/swagger-brake-maven-plugin/issues/21) Add support for war files

### Gradle plugin
* [#22](https://github.com/docktape/swagger-brake-gradle/issues/22) Upgrade Swagger Brake to 2.3.0

Released at 2021-12-09

## 2.3.0
### CLI and Core
* [#61](https://github.com/docktape/swagger-brake/issues/61) Add support for war files
* [#54](https://github.com/docktape/swagger-brake/issues/54) Required fields in nested definitions are being reported as breaking when they are not

CLI download: [swagger-brake-2.3.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/2.3.0/swagger-brake-2.3.0-cli.jar)

### Maven plugin
* [#22](https://github.com/docktape/swagger-brake-maven-plugin/issues/22) Upgrade Swagger Brake to 2.3.0
* [#21](https://github.com/docktape/swagger-brake-maven-plugin/issues/21) Add support for war files

### Gradle plugin
* [#22](https://github.com/docktape/swagger-brake-gradle/issues/22) Upgrade Swagger Brake to 2.3.0

Released at 2021-12-09

## 2.2.0
### CLI and Core
* [#44](https://github.com/docktape/swagger-brake/issues/44) Support for Array minItems/maxItems/uniqueItems constraints
* [#43](https://github.com/docktape/swagger-brake/issues/43) Support for String minLength/maxLength constraints
* [#42](https://github.com/docktape/swagger-brake/issues/42) swagger-brake not reporting violation in breaking changes | MIN/MAX validation | POST object mandatory field validation

CLI download: [swagger-brake-2.2.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/2.2.0/swagger-brake-2.2.0-cli.jar)

### Maven plugin
* [#20](https://github.com/docktape/swagger-brake-maven-plugin/issues/20) Upgrade Swagger Brake to 2.2.0

### Gradle plugin
* [#20](https://github.com/docktape/swagger-brake-gradle/issues/20) Upgrade Swagger Brake to 2.2.0

Released at 2020-12-21

## 2.1.0 
### CLI and Core
* [#41](https://github.com/docktape/swagger-brake/issues/41) Recursive schema throws NPE
* [#40](https://github.com/docktape/swagger-brake/issues/40) old-api should take precedence over maven configuration
* [#39](https://github.com/docktape/swagger-brake/issues/39) Support Nexus based maven metadata
* [#36](https://github.com/docktape/swagger-brake/issues/36) Add route path exclusion parameter support

CLI download: [swagger-brake-2.1.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/2.1.0/swagger-brake-2.1.0-cli.jar)

### Maven plugin
* [#18](https://github.com/docktape/swagger-brake-gradle/issues/18) Upgrade Swagger Brake to 2.1.0

### Gradle plugin
* [#19](https://github.com/docktape/swagger-brake-gradle/issues/19) Upgrade Swagger Brake to 2.1.0

Released at 2020-09-06

## 2.0.0
### CLI and Core
* [#35](https://github.com/docktape/swagger-brake/issues/35) Switch from JAXB to Jackson XMLMapper to support Java 11+
* [#29](https://github.com/docktape/swagger-brake/issues/29) allOf in Swagger contract results in NPE
* [#28](https://github.com/docktape/swagger-brake/issues/28) Null output for formData parameter
* [#26](https://github.com/docktape/swagger-brake/issues/26) SNAPSHOT releases should be optional
* OpenAPI V3 oneOf, anyOf schema support

CLI download: [swagger-brake-2.0.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/2.0.0/swagger-brake-2.0.0-cli.jar)

### Maven plugin
* [#16](https://github.com/docktape/swagger-brake-maven-plugin/issues/16) Upgrade Swagger Brake to 2.0.0
* [#14](https://github.com/docktape/swagger-brake-maven-plugin/issues/14) Support for the oldApi parameter
* [#6](https://github.com/docktape/swagger-brake-maven-plugin/issues/6) Support multiple output formats

### Gradle plugin
* [#18](https://github.com/docktape/swagger-brake-gradle/issues/18) Upgrade Swagger Brake to 2.0.0
* [#9](https://github.com/docktape/swagger-brake-gradle/issues/9) Support multiple output formats

Released at 2020-07-22

## 1.1.0
### CLI and Core
* [#27](https://github.com/docktape/swagger-brake/issues/27) Exitcode for CLI when API has breaking changes

CLI download: [swagger-brake-1.1.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/1.1.0/swagger-brake-1.1.0-cli.jar)

### Gradle plugin
* [#17](https://github.com/docktape/swagger-brake-gradle/issues/17) Support for the oldApi parameter
* [#15](https://github.com/docktape/swagger-brake-gradle/issues/15) Upgrade Swagger Brake to 1.1.0

Released at 2020-04-15

## 1.0.0
### CLI and Core
* [#25](https://github.com/docktape/swagger-brake/issues/25) Upgrade jackson-databind from 2.9.7 to 2.10.3
* [#24](https://github.com/docktape/swagger-brake/issues/24) Change in type of response attribute is not detected
* [#22](https://github.com/docktape/swagger-brake/issues/22) Extend README with link to release page for easier access
* [#19](https://github.com/docktape/swagger-brake/issues/19) Support for beta API
* [#18](https://github.com/docktape/swagger-brake/issues/18) Allow configuring the filename scanned from maven

CLI download: [swagger-brake-1.0.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/1.0.0/swagger-brake-1.0.0-cli.jar)

### Gradle plugin
* [#14](https://github.com/docktape/swagger-brake-gradle/issues/14) Upgrade Swagger Brake to 1.0.0
* [#13](https://github.com/docktape/swagger-brake-gradle/issues/13) Support for beta APIs
* [#12](https://github.com/docktape/swagger-brake-gradle/issues/12) Support for api filename configuration

Released at 2020-03-15

## 0.3.0
### CLI and Core
* [#17](https://github.com/docktape/swagger-brake/issues/17) Add ability to allow removal of deprecated APIs

CLI download: [swagger-brake-0.3.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/0.3.0/swagger-brake-0.3.0-cli.jar)

### Maven plugin
* [#8](https://github.com/docktape/swagger-brake-maven-plugin/issues/8) Upgrade Swagger Brake to 0.3.0
* [#7](https://github.com/docktape/swagger-brake-maven-plugin/issues/7) Fail on breaking changes

### Gradle plugin
* [#11](https://github.com/docktape/swagger-brake-gradle/issues/11) Upgrade Swagger Brake to 0.3.0
* [#10](https://github.com/docktape/swagger-brake-gradle/issues/10) Fail on breaking changes

Released at 2019-03-31

## 0.2.1
### CLI and Core
* [#16](https://github.com/docktape/swagger-brake/issues/16) Can you provide a simple entrypoint to compare 2 API instances
* [#15](https://github.com/docktape/swagger-brake/issues/15) The attempt to fix recursive schemas causes NPE
* [#14](https://github.com/docktape/swagger-brake/issues/14) Shouldn't removing a request body type be a breaking change?
* [#12](https://github.com/docktape/swagger-brake/issues/12) Don't disable all logging
* [#11](https://github.com/docktape/swagger-brake/issues/11) Starter should return results

CLI download: [swagger-brake-0.2.1-cli.jar](https://github.com/docktape/swagger-brake/releases/download/0.2.1/swagger-brake-0.2.1-cli.jar)

### Maven plugin
* [#5](https://github.com/docktape/swagger-brake-maven-plugin/issues/5) Upgrade Swagger Brake to 0.2.1

### Gradle plugin
* [#8](https://github.com/docktape/swagger-brake-gradle/issues/8) Upgrade Swagger Brake to 0.2.1

Released at 2018-12-19

## 0.2.0
### CLI and Core
* [#9](https://github.com/docktape/swagger-brake/issues/9) Add support for $ref to all types that allow reference objects
* [#8](https://github.com/docktape/swagger-brake/issues/8) Stack overflow on recursive schemas
* [#7](https://github.com/docktape/swagger-brake/issues/7) Grammar error on ResponseTypeChangedBreakingChange
* [#5](https://github.com/docktape/swagger-brake/issues/5) Document usage as gradle/maven plugin
* [#4](https://github.com/docktape/swagger-brake/issues/4) Authentication support for Maven repositories
* [#3](https://github.com/docktape/swagger-brake/issues/3) Fix 8.14 checkstyle version usage
* [#2](https://github.com/docktape/swagger-brake/issues/2) JSON reporter is not providing any information about report location
* [#1](https://github.com/docktape/swagger-brake/issues/1) Ability to use more than one reporter

CLI download: [swagger-brake-0.2.0-cli.jar](https://github.com/docktape/swagger-brake/releases/download/0.2.0/swagger-brake-0.2.0-cli.jar)

Released at 2018-12-10