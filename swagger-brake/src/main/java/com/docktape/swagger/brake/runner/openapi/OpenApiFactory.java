package com.docktape.swagger.brake.runner.openapi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * The class is responsible for loading an OpenAPI definition.
 */
@Component
@Slf4j
public class OpenApiFactory {
    private static final Pattern SWAGGER_2_YAML_PATTERN = Pattern.compile("swagger:\\s*[\"']?2\\.0");
    private static final Pattern SWAGGER_2_JSON_PATTERN = Pattern.compile("\"swagger\"\\s*:\\s*\"2\\.0\"");
    private static final int MAX_LINES_TO_SCAN = 100;
    
    /**
     * Extension key used to store the original detected version in the OpenAPI object.
     * This is necessary because swagger-parser auto-converts Swagger 2.0 to OpenAPI 3.0.x,
     * losing the original version information.
     */
    public static final String X_SWAGGER_BRAKE_ORIGINAL_VERSION = "x-swagger-brake-original-version";
    
    /**
     * The method loads an OpenAPI definition from the file system and detects its version.
     * @param path the path of the definition file. It can be a relative path or an absolute one as well.
     * @return the {@link OpenAPI} object instance representing the definition
     * @throws IllegalStateException in case any error happens or the version is unsupported
     */
    public OpenAPI fromFile(String path) {
        try {
            // Detect if this is a Swagger 2.0 file before parsing
            boolean isV2Source = detectV2SourceFile(path);
            
            OpenAPI loadedApi = loadV3Api(path);
            if (loadedApi == null) {
                String errorMsg = isV2Source 
                    ? "Failed to convert Swagger 2.0 specification from path " + path
                    : "API cannot be loaded from path " + path;
                throw new IllegalStateException(errorMsg);
            }
            
            // Determine version based on source detection or parsed version
            OpenApiVersion version;
            if (isV2Source) {
                version = OpenApiVersion.V2_CONVERTED;
                log.info("Detected Swagger 2.0 specification, auto-converted to OpenAPI 3.0.x from: {}", path);
                // Store the original version as an extension so Checker can retrieve it
                loadedApi.addExtension(X_SWAGGER_BRAKE_ORIGINAL_VERSION, version.name());
            } else {
                // Detect and validate OpenAPI version from parsed object
                String versionString = loadedApi.getOpenapi();
                log.info("Detected OpenAPI version string: {}", versionString);
                if (versionString == null || versionString.trim().isEmpty()) {
                    log.error("OpenAPI version field is missing in file: {}", path);
                    throw new IllegalStateException("OpenAPI version field is missing in " + path);
                }
                
                // Parse version and fail-fast if unsupported - this will throw if version is invalid
                version = OpenApiVersion.fromString(versionString);
                log.info("Successfully loaded OpenAPI {} specification from: {}", version, path);
                // Store the detected version as an extension
                loadedApi.addExtension(X_SWAGGER_BRAKE_ORIGINAL_VERSION, version.name());
            }
            
            return loadedApi;
        } catch (IllegalArgumentException e) {
            // Re-throw version validation errors with context
            throw new IllegalStateException("Invalid OpenAPI specification in " + path + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalStateException("API cannot be loaded from path " + path, e);
        }
    }

    /**
     * Detects if a file is a Swagger 2.0 specification by reading the file header.
     * Checks for the presence of "swagger: \"2.0\"" (YAML) or "\"swagger\": \"2.0\"" (JSON).
     * 
     * @param path the path to the specification file
     * @return true if the file appears to be Swagger 2.0, false otherwise
     */
    private boolean detectV2SourceFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            int linesRead = 0;
            
            while ((line = reader.readLine()) != null && linesRead < MAX_LINES_TO_SCAN) {
                linesRead++;
                
                // Check for YAML format: swagger: "2.0" or swagger: 2.0 or swagger: '2.0'
                if (SWAGGER_2_YAML_PATTERN.matcher(line).find()) {
                    log.debug("Detected Swagger 2.0 YAML format in file: {}", path);
                    return true;
                }
                
                // Check for JSON format: "swagger": "2.0"
                if (SWAGGER_2_JSON_PATTERN.matcher(line).find()) {
                    log.debug("Detected Swagger 2.0 JSON format in file: {}", path);
                    return true;
                }
            }
            
            log.debug("No Swagger 2.0 signature detected in first {} lines of file: {}", linesRead, path);
            return false;
        } catch (IOException e) {
            log.warn("Could not read file to detect version: {}. Assuming OpenAPI 3.x.", path, e);
            return false;
        }
    }

    private OpenAPI loadV3Api(String path) {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolveFully(true);
        return new OpenAPIV3Parser().read(path, null, parseOptions);
    }
}
