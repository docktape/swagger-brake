package com.docktape.swagger.brake.runner.openapi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * Enum representing supported OpenAPI specification versions.
 */
public enum OpenApiVersion {
    /**
     * OpenAPI 3.0.x versions (3.0.0, 3.0.1, 3.0.2, 3.0.3, etc.).
     */
    V3_0_X,
    
    /**
     * OpenAPI 3.1.x versions (3.1.0, 3.1.1, etc.).
     */
    V3_1_X,
    
    /**
     * Swagger 2.0 specification that was auto-converted to OpenAPI 3.0.x during parsing.
     * The swagger-parser library automatically converts Swagger 2.0 specs to OpenAPI 3.0.x format.
     */
    V2_CONVERTED,
    
    /**
     * Unsupported or unknown version.
     */
    UNSUPPORTED;
    
    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+).*$");
    
    /**
     * Extracts the OpenAPI version from an OpenAPI object.
     * First checks for the x-swagger-brake-original-version extension (which stores the detected version),
     * then falls back to parsing the openapi field.
     * 
     * @param openApi the OpenAPI object
     * @return the corresponding OpenApiVersion enum value
     * @throws IllegalArgumentException if the version cannot be determined or is unsupported
     */
    public static OpenApiVersion fromOpenApi(OpenAPI openApi) {
        if (openApi == null) {
            throw new IllegalArgumentException("OpenAPI object cannot be null");
        }
        
        // Check for stored original version in extensions (set by OpenApiFactory)
        if (openApi.getExtensions() != null) {
            Object storedVersion = openApi.getExtensions().get("x-swagger-brake-original-version");
            if (storedVersion != null) {
                try {
                    return OpenApiVersion.valueOf(storedVersion.toString());
                } catch (IllegalArgumentException e) {
                    // Fall through to parsing the openapi field
                }
            }
        }
        
        // Fall back to parsing the openapi field
        String versionString = openApi.getOpenapi();
        return fromString(versionString);
    }
    
    /**
     * Parses an OpenAPI version string and returns the corresponding enum value.
     * 
     * @param versionString the version string from the OpenAPI spec (e.g., "3.0.0", "3.1.0")
     * @return the corresponding OpenApiVersion enum value
     * @throws IllegalArgumentException if the version is null, empty, malformed, or unsupported
     */
    public static OpenApiVersion fromString(String versionString) {
        if (versionString == null || versionString.trim().isEmpty()) {
            throw new IllegalArgumentException("OpenAPI version string cannot be null or empty");
        }
        
        Matcher matcher = VERSION_PATTERN.matcher(versionString.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Malformed OpenAPI version string: " + versionString);
        }
        
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        
        if (major == 3 && minor == 0) {
            return V3_0_X;
        } else if (major == 3 && minor == 1) {
            return V3_1_X;
        } else {
            throw new IllegalArgumentException(
                "Unsupported OpenAPI version: " + versionString + ". Only versions 3.0.x and 3.1.x are supported."
            );
        }
    }
    
    /**
     * Checks if this version is OpenAPI 3.0.x.
     * 
     * @return true if this is V3_0_X, false otherwise
     */
    public boolean is3_0() {
        return this == V3_0_X;
    }
    
    /**
     * Checks if this version is OpenAPI 3.1.x.
     * 
     * @return true if this is V3_1_X, false otherwise
     */
    public boolean is3_1() {
        return this == V3_1_X;
    }
    
    /**
     * Checks if this version is a Swagger 2.0 spec that was auto-converted to OpenAPI 3.0.x.
     * 
     * @return true if this is V2_CONVERTED, false otherwise
     */
    public boolean isV2Converted() {
        return this == V2_CONVERTED;
    }
    
    @Override
    public String toString() {
        return switch (this) {
            case V3_0_X -> "OpenAPI 3.0.x";
            case V3_1_X -> "OpenAPI 3.1.x";
            case V2_CONVERTED -> "Swagger 2.0 (converted to OpenAPI 3.0.x)";
            case UNSUPPORTED -> "Unsupported version";
            default -> "Unknown version";
        };
    }
}
