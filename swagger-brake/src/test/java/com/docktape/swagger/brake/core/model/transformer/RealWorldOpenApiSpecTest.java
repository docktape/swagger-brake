package com.docktape.swagger.brake.core.model.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.util.SafeSwaggerSerializer;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersion;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersionContext;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Test to validate that the SafeSwaggerSerializer correctly handles
 * real-world OpenAPI specifications with circular references, preventing
 * StackOverflowError crashes that occurred in production.
 *
 * <p>
 * This test uses the obfuscated safe-v3*.json specs which contains the
 * Circular reference pattern that previously caused crashes.
 */
@Slf4j
class RealWorldOpenApiSpecTest {
    private static final String TEST_SPEC_PATH = "/swaggers/v3/safe-serialize/safe-v3.json";
    private static final String TEST_SPEC_PATH_SECOND = "/swaggers/v3/safe-serialize/safe-v3-second.json";
    
    private SchemaTransformer schemaTransformer;
    private CheckerOptionsProvider checkerOptionsProvider;
    private SafeSwaggerSerializer safeSwaggerSerializer;

    @BeforeEach
    void setUp() {
        checkerOptionsProvider = Mockito.mock(CheckerOptionsProvider.class);
        CheckerOptions checkerOptions = new CheckerOptions();
        checkerOptions.setMaxLogSerializationDepth(3); // Default depth
        when(checkerOptionsProvider.get()).thenReturn(checkerOptions);
        
        safeSwaggerSerializer = new SafeSwaggerSerializer(checkerOptionsProvider);
        TypeRefNameResolver typeRefNameResolver = new TypeRefNameResolver();
        TypeResolver typeResolver = new TypeResolver(checkerOptionsProvider);
        
        schemaTransformer = new SchemaTransformer(typeRefNameResolver, typeResolver, safeSwaggerSerializer, checkerOptionsProvider);
    }

    /**
     * Provides test spec paths for parameterized tests.
     */
    static Stream<String> specPaths() {
        return Stream.of(TEST_SPEC_PATH, TEST_SPEC_PATH_SECOND);
    }

    @Test
    void testRealWorldSpec_WithCircularReferences_NoStackOverflowError() throws IOException {
        log.info("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
        log.info("║  Real-World OpenAPI Spec Test: safe-v3.json with Entity↔MarketplaceNumber       ║");
        log.info("╚═══════════════════════════════════════════════════════════════════════════════╝");
        
        // Load the real OpenAPI spec that contains circular references
        OpenAPI openApi = loadOpenApiSpec(TEST_SPEC_PATH);
        assertNotNull(openApi, "OpenAPI spec should be loaded successfully");
        
        log.info("\n✓ Loaded OpenAPI spec: " + openApi.getInfo().getTitle());
        log.info("✓ Version: " + openApi.getInfo().getVersion());
        
        // Get the schemas that have circular references
        Map<String, Schema> schemas = openApi.getComponents().getSchemas();
        assertNotNull(schemas, "Schemas should be present in the spec");
        
        Schema<?> entitySchema = schemas.get("Entity");
        Schema<?> marketplaceNumberSchema = schemas.get("MarketplaceNumber");

        assertAll(
                () -> assertNotNull(entitySchema, "Entity schema should be present"),
                () -> assertNotNull(marketplaceNumberSchema, "MarketplaceNumber schema should be present")
        );

        log.info("\n✓ Found Entity schema with properties: " + entitySchema.getProperties().keySet());
        log.info("✓ Found MarketplaceNumber schema with properties: " + marketplaceNumberSchema.getProperties().keySet());
        
        // Verify circular reference exists
        Schema<?> entityMarketplaceNumbers = (Schema<?>) entitySchema.getProperties().get("marketplaceNumbers");
        assertNotNull(entityMarketplaceNumbers, "Entity should have marketplaceNumbers property");
        log.info("\n✓ Verified circular reference: Entity → marketplaceNumbers (array of MarketplaceNumber)");
        
        Schema<?> marketplaceNumberEntity = (Schema<?>) marketplaceNumberSchema.getProperties().get("entity");
        assertNotNull(marketplaceNumberEntity, "MarketplaceNumber should have entity property");
        log.info("✓ Verified circular reference: MarketplaceNumber → entity (Entity)");
        
        // Test safe serialization with OpenAPI 3.1.x context (the version that triggered the bug)
        try (OpenApiVersionContext context = new OpenApiVersionContext(OpenApiVersion.V3_1_X)) {
            log.info("\n--- Testing SafeSwaggerSerializer with Entity schema ---");
            
            // This would have caused StackOverflowError before the fix
            assertDoesNotThrow(() -> {
                String serializedEntity = safeSwaggerSerializer.serialize(entitySchema);

                assertAll(
                        () -> assertNotNull(serializedEntity, "Entity serialization should not return null"),
                        () -> assertFalse(serializedEntity.isEmpty(), "Entity serialization should produce output")
                );
                log.info("✓ Entity schema serialized safely: " + truncate(serializedEntity, 100));
            }, "Entity schema serialization should not cause StackOverflowError");
            
            log.info("\n--- Testing SafeSwaggerSerializer with MarketplaceNumber schema ---");
            
            assertDoesNotThrow(() -> {
                String serializedMarketplace = safeSwaggerSerializer.serialize(marketplaceNumberSchema);
                assertAll(
                        () -> assertNotNull(serializedMarketplace, "MarketplaceNumber serialization should not return null"),
                        () -> assertFalse(serializedMarketplace.isEmpty(), "MarketplaceNumber serialization should produce output")
                );
                log.info("✓ MarketplaceNumber schema serialized safely: " + truncate(serializedMarketplace, 100));
            }, "MarketplaceNumber schema serialization should not cause StackOverflowError");
            
            log.info("\n--- Testing SchemaTransformer with circular references ---");
            
            // Test that SchemaTransformer can handle these schemas without StackOverflowError
            com.docktape.swagger.brake.core.model.Schema transformedEntity = assertDoesNotThrow(() -> {
                com.docktape.swagger.brake.core.model.Schema result = schemaTransformer.transform(entitySchema);
                log.info("✓ Entity schema transformation completed without StackOverflowError");
                return result;
            }, "Entity schema transformation should complete successfully");
            
            assertNotNull(transformedEntity, "Transformed Entity schema should not be null");
            log.info("✓ Transformed Entity schema: " + transformedEntity.getType());
            
            com.docktape.swagger.brake.core.model.Schema transformedMarketplaceNumber = assertDoesNotThrow(() -> {
                com.docktape.swagger.brake.core.model.Schema result = schemaTransformer.transform(marketplaceNumberSchema);
                log.info("✓ MarketplaceNumber schema transformation completed without StackOverflowError");
                return result;
            }, "MarketplaceNumber schema transformation should complete successfully");
            
            assertNotNull(transformedMarketplaceNumber, "Transformed MarketplaceNumber schema should not be null");
            log.info("✓ Transformed MarketplaceNumber schema: " + transformedMarketplaceNumber.getType());
            
            log.info("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
            log.info("║  ✓ ALL TESTS PASSED - Real-world spec with circular references handled!     ║");
            log.info("║  ✓ No StackOverflowError - Fix is working correctly in production scenario! ║");
            log.info("╚═══════════════════════════════════════════════════════════════════════════════╝\n");
        }
    }

    @ParameterizedTest
    @MethodSource("specPaths")
    void testAllSchemasInSpec_NoCrashes(String specPath) throws IOException {
        log.info("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
        log.info("║  Comprehensive Test: Process ALL schemas from " + specPath.substring(specPath.lastIndexOf('/') + 1) + "                  ║");
        log.info("╚═══════════════════════════════════════════════════════════════════════════════╝");
        
        OpenAPI openApi = loadOpenApiSpec(specPath);
        Map<String, Schema> schemas = openApi.getComponents().getSchemas();
        
        log.info("\n✓ Total schemas to process: " + schemas.size());
        
        try (OpenApiVersionContext context = new OpenApiVersionContext(OpenApiVersion.V3_1_X)) {
            int successCount = 0;
            
            for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
                String schemaName = entry.getKey();
                Schema<?> schema = entry.getValue();
                
                // Every schema should be serializable without crashes
                assertDoesNotThrow(() -> {
                    String serialized = safeSwaggerSerializer.serialize(schema);
                    assertNotNull(serialized, "Serialization of " + schemaName + " should not be null");
                }, "Schema " + schemaName + " should serialize without StackOverflowError");
                
                successCount++;
            }
            
            log.info("\n✓ Successfully processed all " + successCount + " schemas without any StackOverflowError");
            log.info("✓ SafeSwaggerSerializer is production-ready!");
        }
    }

    @ParameterizedTest
    @MethodSource("specPaths")
    void testWithDifferentDepthConfigurations(String specPath) throws IOException {
        log.info("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
        log.info("║  Test: Varying maxLogSerializationDepth with " + specPath.substring(specPath.lastIndexOf('/') + 1) + "                ║");
        log.info("╚═══════════════════════════════════════════════════════════════════════════════╝");
        
        OpenAPI openApi = loadOpenApiSpec(specPath);
        Schema<?> firstSchema = openApi.getComponents().getSchemas().values().iterator().next();
        
        int[] depths = {1, 2, 3, 5, 10, 20};
        
        try (OpenApiVersionContext context = new OpenApiVersionContext(OpenApiVersion.V3_1_X)) {
            for (int depth : depths) {
                CheckerOptions options = new CheckerOptions();
                options.setMaxLogSerializationDepth(depth);
                when(checkerOptionsProvider.get()).thenReturn(options);
                
                assertDoesNotThrow(() -> {
                    String output = safeSwaggerSerializer.serialize(firstSchema);
                    log.info("✓ Depth " + depth + ": " + truncate(output, 80));
                }, "Depth " + depth + " should work without StackOverflowError");
            }
            
            log.info("\n✓ All depth configurations handled circular references correctly");
        }
    }

    /**
     * Load the OpenAPI spec from test resources.
     * Note: safe-v3-second.json has complex circular references that cause StackOverflowError
     * in the OpenAPI parser's ResolverFully, so we disable resolveFully for it.
     */
    private OpenAPI loadOpenApiSpec(String specPath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(specPath)) {
            assertNotNull(inputStream, "Test spec file should exist: " + specPath);
            
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            
            ParseOptions parseOptions = new ParseOptions();
            parseOptions.setResolve(true);
            // Disable resolveFully for safe-v3-second.json as it has complex circular references
            // that cause StackOverflowError in the OpenAPI parser itself
            boolean isSecondSpec = specPath.contains("safe-v3-second");
            parseOptions.setResolveFully(!isSecondSpec);
            
            SwaggerParseResult result = new OpenAPIV3Parser().readContents(content, null, parseOptions);
            
            if (result.getMessages() != null && !result.getMessages().isEmpty()) {
                log.info("Parse messages: " + result.getMessages());
            }
            
            OpenAPI openApi = result.getOpenAPI();
            assertNotNull(openApi, "OpenAPI should be parsed successfully");
            
            return openApi;
        }
    }

    /**
     * Truncate string for display purposes.
     */
    private String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
}
