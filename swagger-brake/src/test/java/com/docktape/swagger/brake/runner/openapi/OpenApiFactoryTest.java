package com.docktape.swagger.brake.runner.openapi;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import io.swagger.v3.oas.models.OpenAPI;

class OpenApiFactoryTest {
    private OpenApiFactory factory;
    
    @BeforeEach
    void setUp() {
        factory = new OpenApiFactory();
    }

    @Test
    void testSpecProblemInvalid() {
        // given - completely unknown invalid spec file/json
        String path = "swaggers/spec-problem.json";

        // when/then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
            factory.fromFile(path)
        );

        assertTrue(exception.getMessage().contains("API cannot be loaded"));
    }

    @Test
    void testFromFile_V2_Yaml_DetectedAsV2Converted() throws IOException {
        // given
        String path = new ClassPathResource("swaggers/v2/test-v2.yaml").getFile().getAbsolutePath();
        
        // when
        OpenAPI result = factory.fromFile(path);
        
        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getExtensions()),
                () -> assertEquals("V2_CONVERTED", result.getExtensions().get(OpenApiFactory.X_SWAGGER_BRAKE_ORIGINAL_VERSION))
        );

        // Verify version is correctly identified
        OpenApiVersion version = OpenApiVersion.fromOpenApi(result);
        assertEquals(OpenApiVersion.V2_CONVERTED, version);
    }
    
    @Test
    void testFromFile_V2_Json_DetectedAsV2Converted() throws IOException {
        // given
        String path = new ClassPathResource("swaggers/v2/test-v2.json").getFile().getAbsolutePath();
        
        // when
        OpenAPI result = factory.fromFile(path);
        
        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getExtensions()),
                () -> assertEquals("V2_CONVERTED", result.getExtensions().get(OpenApiFactory.X_SWAGGER_BRAKE_ORIGINAL_VERSION))
        );
        
        // Verify version is correctly identified
        OpenApiVersion version = OpenApiVersion.fromOpenApi(result);
        assertEquals(OpenApiVersion.V2_CONVERTED, version);
    }
    
    @Test
    void testFromFile_V2_ExistingPetstore_DetectedAsV2Converted() throws IOException {
        // given - use one of the existing v2 test files
        String path = new ClassPathResource("swaggers/v2/nobreakingchange/petstore.yaml").getFile().getAbsolutePath();
        
        // when
        OpenAPI result = factory.fromFile(path);
        
        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getExtensions()),
                () -> assertEquals("V2_CONVERTED", result.getExtensions().get(OpenApiFactory.X_SWAGGER_BRAKE_ORIGINAL_VERSION))
        );
        
        // Verify version is correctly identified
        OpenApiVersion version = OpenApiVersion.fromOpenApi(result);
        assertAll(
                () -> assertEquals(OpenApiVersion.V2_CONVERTED, version),
                () -> assertTrue(version.isV2Converted())
        );
    }
    
    @Test
    void testFromFile_V3_Yaml_DetectedAsV3() throws IOException {
        // given
        String path = new ClassPathResource("swaggers/v3/test-v3.yaml").getFile().getAbsolutePath();
        
        // when
        OpenAPI result = factory.fromFile(path);
        
        // then
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.getExtensions()),
                () -> assertEquals("V3_0_X", result.getExtensions().get(OpenApiFactory.X_SWAGGER_BRAKE_ORIGINAL_VERSION))
        );
        
        // Verify version is correctly identified
        OpenApiVersion version = OpenApiVersion.fromOpenApi(result);
        assertAll(
                () -> assertEquals(OpenApiVersion.V3_0_X, version),
                () -> assertFalse(version.isV2Converted())
        );
    }
    
    @Test
    void testFromFile_NonExistentFile_ThrowsException() {
        // given
        String path = "/nonexistent/path/to/swagger.yaml";
        
        // when/then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
            factory.fromFile(path)
        );
        
        assertTrue(exception.getMessage().contains("API cannot be loaded"));
    }
    
    @Test
    void testFromFile_V2_ParserConvertsToV3() throws IOException {
        // given
        String path = new ClassPathResource("swaggers/v2/test-v2.yaml").getFile().getAbsolutePath();
        
        // when
        OpenAPI result = factory.fromFile(path);
        
        // then - parser auto-converts, so openapi field is 3.0.x
        assertAll(
                () -> assertNotNull(result.getOpenapi()),
                () -> assertTrue(result.getOpenapi().startsWith("3.0"))
        );
        
        // But extension shows original was v2
        assertEquals("V2_CONVERTED", result.getExtensions().get(OpenApiFactory.X_SWAGGER_BRAKE_ORIGINAL_VERSION));
    }
    
    @Test
    void testFromFile_V2_HasComponents() throws IOException {
        // given - v2 has "definitions", parser converts to "components.schemas"
        String path = new ClassPathResource("swaggers/v2/nobreakingchange/petstore.yaml").getFile().getAbsolutePath();
        
        // when
        OpenAPI result = factory.fromFile(path);
        
        // then - verify conversion happened
        assertAll(
                () -> assertNotNull(result.getComponents()),
                () -> assertNotNull(result.getComponents().getSchemas()),
                () -> assertFalse(result.getComponents().getSchemas().isEmpty())
        );
        
        // Verify Pet schema exists (was in definitions in v2)
        assertTrue(result.getComponents().getSchemas().containsKey("Pet"));
    }
}
