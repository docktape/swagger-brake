# Troubleshooting

## Swagger 2.0 Support and Limitations

### Overview
swagger-brake supports both **Swagger 2.0** and **OpenAPI 3.x** specifications. When you provide a Swagger 2.0 file, it is automatically detected and converted to OpenAPI 3.0.x format during parsing. This conversion is handled transparently by the underlying swagger-parser library.

### Version Detection
swagger-brake correctly identifies Swagger 2.0 specifications by:
- Detecting the `swagger: "2.0"` field in YAML files
- Detecting the `"swagger": "2.0"` field in JSON files
- Reporting the version as "Swagger 2.0 (converted to OpenAPI 3.0.x)" in logs and reports

### What Breaking Changes Are Detected
The following changes in Swagger 2.0 files are correctly detected as breaking changes:
- ✅ **Schema changes** - modifications to object definitions, properties, types
- ✅ **Parameter changes** - added required parameters, deleted parameters, type changes
- ✅ **Response changes** - deleted responses, schema modifications
- ✅ **Path deletions** - removed API endpoints
- ✅ **Required field changes** - making fields required, removing required fields
- ✅ **Enum value deletions** - removing enum values
- ✅ **Type constraints** - maxLength, minLength, maximum, minimum, etc.

### Known Limitations
Due to the automatic conversion from Swagger 2.0 to OpenAPI 3.0.x, the following Swagger 2.0-specific structural changes **cannot be detected**:

- ❌ **basePath changes** - modifications to the `basePath` field (converted to `servers` array)
- ❌ **host changes** - modifications to the `host` field (converted to `servers` array)
- ❌ **Path-level parameter reorganization** - Swagger 2.0 allows parameters at path level, which are duplicated to operations during conversion
- ❌ **produces/consumes simplifications** - These are converted to more verbose `content` objects in v3

**Important**: While these structural changes aren't detected, the **semantic meaning** of your API contract is preserved. All functional breaking changes (schema modifications, parameter changes, endpoint deletions) are properly detected.

### Example Usage
```bash
# Works with both Swagger 2.0 and OpenAPI 3.x files
$ java -jar swagger-brake.jar --old-api=swagger-v2.yaml --new-api=swagger-v2-updated.yaml
Loading old API from swagger-v2.yaml
Successfully loaded APIs
Detected Swagger 2.0 specification, auto-converted to OpenAPI 3.0.x
Starting the check for breaking API changes
...
```

### Technical Details
The conversion process:
1. swagger-brake detects Swagger 2.0 by reading the file header
2. The swagger-parser library automatically converts v2 to OpenAPI 3.0.x format
3. Breaking change detection runs on the converted OpenAPI 3.0.x models
4. Version information is preserved and reported correctly

This approach ensures maximum compatibility while maintaining accurate breaking change detection for the vast majority of use cases.

## Debugging Logging Issues

### SafeSwaggerSerializer Metrics
When troubleshooting circular reference issues or understanding how deep object serialization is occurring in logs, you can enable optional metrics logging.

**Environment Variable**: `SWAGGER_BRAKE_ENABLE_METRICS_LOGGING=true`

When enabled, swagger-brake will output metrics showing:
* Number of circular references detected during serialization
* Maximum depth reached during object serialization
* Statistics on how objects are being serialized

This is particularly useful when:
* Debugging StackOverflowError issues with complex schemas
* Tuning the `--max-log-serialization-depth` parameter (see [CLI configuration](../cli/README.md#configuring-log-serialization-depth))
* Understanding performance impacts of deeply nested object logging

Example usage:
```bash
$ export SWAGGER_BRAKE_ENABLE_METRICS_LOGGING=true
$ java -jar swagger-brake.jar --old-api=swagger.yaml --new-api=swagger2.yaml
```

The metrics will appear in the log output, helping you understand serialization behavior without modifying code.

## CLI file reporting doesn't work
If you encounter the following message during an execution
```text
No file reporting has been done since output file path is not set
```
It means you have configured file typed reporting like JSON or HTML but you did not pass the
`--output-path` argument so swagger-brake doesn't know where to store the reports. 

Relevant section: [Customizing reporting](../cli/cli-interface.md#customizing-reporting)

