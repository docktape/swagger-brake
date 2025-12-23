package com.docktape.swagger.brake.runner.openapi;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;

class OpenApiVersionTest {
    
    @Test
    void testFromString_ValidV3_0_0() {
        OpenApiVersion version = OpenApiVersion.fromString("3.0.0");

        assertAll(
                () -> assertEquals(OpenApiVersion.V3_0_X, version),
                () -> assertTrue(version.is3_0()),
                () -> assertFalse(version.is3_1())
        );
    }
    
    @Test
    void testFromString_ValidV3_0_3() {
        OpenApiVersion version = OpenApiVersion.fromString("3.0.3");

        assertAll(
                () -> assertEquals(OpenApiVersion.V3_0_X, version),
                () -> assertTrue(version.is3_0())
        );
    }
    
    @Test
    void testFromString_ValidV3_1_0() {
        OpenApiVersion version = OpenApiVersion.fromString("3.1.0");

        assertAll(
                () -> assertEquals(OpenApiVersion.V3_1_X, version),
                () -> assertTrue(version.is3_1()),
                () -> assertFalse(version.is3_0())
        );
    }
    
    @Test
    void testFromString_ValidV3_1_1() {
        OpenApiVersion version = OpenApiVersion.fromString("3.1.1");

        assertEquals(OpenApiVersion.V3_1_X, version);
    }
    
    @Test
    void testFromString_NullThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            OpenApiVersion.fromString(null)
        );

        assertTrue(exception.getMessage().contains("cannot be null or empty"));
    }
    
    @Test
    void testFromString_EmptyThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            OpenApiVersion.fromString("")
        );
        assertTrue(exception.getMessage().contains("cannot be null or empty"));
    }
    
    @Test
    void testFromString_MalformedThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            OpenApiVersion.fromString("3.0")
        );
        assertTrue(exception.getMessage().contains("Malformed"));
    }
    
    @Test
    void testFromString_UnsupportedVersion2() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            OpenApiVersion.fromString("2.0.0")
        );
        assertTrue(exception.getMessage().contains("Unsupported"));
    }
    
    @Test
    void testFromString_UnsupportedVersion3_2() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            OpenApiVersion.fromString("3.2.0")
        );
        assertTrue(exception.getMessage().contains("Unsupported"));
    }
    
    @Test
    void testFromString_UnsupportedVersion4() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            OpenApiVersion.fromString("4.0.0")
        );
        assertTrue(exception.getMessage().contains("Unsupported"));
    }
    
    @Test
    void testV2Converted_EnumValue() {
        assertSame(OpenApiVersion.V2_CONVERTED, OpenApiVersion.valueOf("V2_CONVERTED"));
    }
    
    @Test
    void testIsV2Converted_True() {
        assertTrue(OpenApiVersion.V2_CONVERTED.isV2Converted());
    }
    
    @Test
    void testIsV2Converted_False() {
        assertAll(
                () -> assertFalse(OpenApiVersion.V3_0_X.isV2Converted()),
                () -> assertFalse(OpenApiVersion.V3_1_X.isV2Converted()),
                () -> assertFalse(OpenApiVersion.UNSUPPORTED.isV2Converted())
        );
    }
    
    @Test
    void testToString_V2Converted() {
        String result = OpenApiVersion.V2_CONVERTED.toString();
        
        assertAll(
                () -> assertTrue(result.contains("Swagger 2.0")),
                () -> assertTrue(result.contains("converted"))
        );
    }
    
    @Test
    void testToString_V3_0_X() {
        String result = OpenApiVersion.V3_0_X.toString();
        
        assertTrue(result.contains("OpenAPI 3.0"));
    }
    
    @Test
    void testToString_V3_1_X() {
        String result = OpenApiVersion.V3_1_X.toString();
        
        assertTrue(result.contains("OpenAPI 3.1"));
    }
    
    @Test
    void testFromOpenApi_WithV2ConvertedExtension() {
        OpenAPI openApi = new OpenAPI();
        openApi.setOpenapi("3.0.0"); // Parser sets this after conversion
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("x-swagger-brake-original-version", "V2_CONVERTED");
        openApi.setExtensions(extensions);
        
        OpenApiVersion version = OpenApiVersion.fromOpenApi(openApi);
        
        assertEquals(OpenApiVersion.V2_CONVERTED, version);
    }
    
    @Test
    void testFromOpenApi_WithV3_0Extension() {
        OpenAPI openApi = new OpenAPI();
        openApi.setOpenapi("3.0.0");
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("x-swagger-brake-original-version", "V3_0_X");
        openApi.setExtensions(extensions);
        
        OpenApiVersion version = OpenApiVersion.fromOpenApi(openApi);
        
        assertEquals(OpenApiVersion.V3_0_X, version);
    }
    
    @Test
    void testFromOpenApi_WithoutExtension_FallbackToOpenApiField() {
        OpenAPI openApi = new OpenAPI();
        openApi.setOpenapi("3.1.0");
        
        OpenApiVersion version = OpenApiVersion.fromOpenApi(openApi);
        
        assertEquals(OpenApiVersion.V3_1_X, version);
    }
    
    @Test
    void testFromOpenApi_NullThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            OpenApiVersion.fromOpenApi(null)
        );
        
        assertTrue(exception.getMessage().contains("cannot be null"));
    }
    
    @Test
    void testFromOpenApi_InvalidExtensionValue_FallbackToOpenApiField() {
        OpenAPI openApi = new OpenAPI();
        openApi.setOpenapi("3.0.0");
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("x-swagger-brake-original-version", "INVALID_VERSION");
        openApi.setExtensions(extensions);
        
        OpenApiVersion version = OpenApiVersion.fromOpenApi(openApi);
        
        // Should fall back to parsing the openapi field
        assertEquals(OpenApiVersion.V3_0_X, version);
    }
}
