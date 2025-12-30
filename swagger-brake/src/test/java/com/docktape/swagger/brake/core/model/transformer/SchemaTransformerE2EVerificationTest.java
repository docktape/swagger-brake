package com.docktape.swagger.brake.core.model.transformer;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.util.SafeSwaggerSerializer;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersion;
import com.docktape.swagger.brake.runner.openapi.OpenApiVersionContext;

/**
 * End-to-end verification test that simulates the actual SchemaTransformer
 * usage with circular references and verifies the log output is safe.
 */
@Slf4j
class SchemaTransformerE2EVerificationTest {
    private SchemaTransformer schemaTransformer;
    private CheckerOptionsProvider checkerOptionsProvider;
    private SafeSwaggerSerializer safeSwaggerSerializer;

    @BeforeEach
    void setUp() {
        checkerOptionsProvider = Mockito.mock(CheckerOptionsProvider.class);
        CheckerOptions checkerOptions = new CheckerOptions();
        when(checkerOptionsProvider.get()).thenReturn(checkerOptions);
        
        safeSwaggerSerializer = new SafeSwaggerSerializer(checkerOptionsProvider);
        TypeRefNameResolver typeRefNameResolver = new TypeRefNameResolver();
        TypeResolver typeResolver = new TypeResolver(checkerOptionsProvider);
        
        schemaTransformer = new SchemaTransformer(typeRefNameResolver, typeResolver, safeSwaggerSerializer, checkerOptionsProvider);
    }

    @AfterEach
    void tearDown() {
        // Cleanup handled by try-with-resources in tests
    }

    @Test
    void verifyNoStackOverflowError_WithRealWorldBusinessAuctionNumberScenario() {
        log.debug("\n╔═══════════════════════════════════════════════════════════════╗");
        log.debug("║  E2E Verification: Business-AuctionNumber Circular Reference  ║");
        log.debug("╚═══════════════════════════════════════════════════════════════╝");
        
        // Use OpenAPI 3.1.x context to trigger the code path that logs schemas
        try (OpenApiVersionContext context = new OpenApiVersionContext(OpenApiVersion.V3_1_X)) {
            // Create the exact scenario from the bug report
            io.swagger.v3.oas.models.media.Schema<?> businessSchema = new io.swagger.v3.oas.models.media.Schema<>();
            businessSchema.setName("Business");
            businessSchema.setType("object");
            businessSchema.set$ref("#/components/schemas/Business");
            
            io.swagger.v3.oas.models.media.Schema<?> auctionNumberSchema = new io.swagger.v3.oas.models.media.Schema<>();
            auctionNumberSchema.setName("AuctionNumber");
            auctionNumberSchema.setType("object");
            auctionNumberSchema.set$ref("#/components/schemas/AuctionNumber");
            
            // Create circular reference: Business -> auctionNumber -> Business
            Map<String, io.swagger.v3.oas.models.media.Schema> businessProperties = new HashMap<>();
            businessProperties.put("auctionNumber", auctionNumberSchema);
            businessProperties.put("name", new io.swagger.v3.oas.models.media.Schema<>().type("string"));
            businessProperties.put("id", new io.swagger.v3.oas.models.media.Schema<>().type("integer"));
            businessSchema.setProperties(businessProperties);
            
            Map<String, io.swagger.v3.oas.models.media.Schema> auctionNumberProperties = new HashMap<>();
            auctionNumberProperties.put("business", businessSchema);
            auctionNumberProperties.put("number", new io.swagger.v3.oas.models.media.Schema<>().type("string"));
            auctionNumberSchema.setProperties(auctionNumberProperties);
            
            log.debug("\n✓ Created circular schema structure: Business → AuctionNumber → Business");
            log.debug("✓ OpenAPI Version: {}", OpenApiVersion.V3_1_X);
            
            // Test safe serialization directly
            log.debug("\n--- Testing SafeSwaggerSerializer ---");
            String serialized = safeSwaggerSerializer.serialize(businessSchema);
            log.debug("Serialized output: {}", serialized);
            assertNotNull(serialized, "Serialization should not return null");
            assertFalse(serialized.isEmpty(), "Serialization should produce output");
            
            // This would have thrown StackOverflowError before the fix
            log.debug("\n--- Testing SchemaTransformer.transform() ---");
            log.debug("This internally calls safeSwaggerSerializer.serialize() at line 114");
            log.debug("Before fix: Would cause StackOverflowError");
            log.debug("After fix: Should complete successfully...");
            
            assertDoesNotThrow(() -> {
                // Call transform - the key is that it doesn't throw StackOverflowError
                // (result may be null due to missing data, but that's not what we're testing)
                Schema result = schemaTransformer.transform(businessSchema);
                log.debug("✓ Transform completed successfully - No StackOverflowError!");
                log.debug("✓ Result: {}",
                        (result != null ? result.getClass().getSimpleName() : "null (expected due to test data)"));
            }, "SchemaTransformer should handle circular references without StackOverflowError");
            
            log.debug("\n╔═══════════════════════════════════════════════════════════════╗");
            log.debug("║  ✓ VERIFICATION PASSED - Fix is working correctly!            ║");
            log.debug("╚═══════════════════════════════════════════════════════════════╝\n");
        }
    }

    @Test
    void verifyLogOutputWithDifferentDepthSettings() {
        log.debug("\n╔═══════════════════════════════════════════════════════════════╗");
        log.debug("║  Verification: Log Output with Different Depth Settings       ║");
        log.debug("╚═══════════════════════════════════════════════════════════════╝");
        
        io.swagger.v3.oas.models.media.Schema<?> schema = new io.swagger.v3.oas.models.media.Schema<>();
        schema.setName("TestSchema");
        schema.setType("object");
        
        // Test with different depth settings
        int[] depths = {1, 3, 5, 10, 20};
        
        for (int depth : depths) {
            CheckerOptions options = new CheckerOptions();
            options.setMaxLogSerializationDepth(depth);
            when(checkerOptionsProvider.get()).thenReturn(options);
            
            String output = safeSwaggerSerializer.serialize(schema);
            log.debug("Depth {}: {}", depth, output);
            
            assertNotNull(output);
            assertFalse(output.isEmpty());
        }
        
        log.debug("\n✓ All depth settings working correctly");
    }

    @Test
    void verifyConfigurationValidation() {
        log.debug("\n╔═══════════════════════════════════════════════════════════════╗");
        log.debug("║  Verification: Configuration Validation                       ║");
        log.debug("╚═══════════════════════════════════════════════════════════════╝");
        
        CheckerOptions options = new CheckerOptions();
        
        // Test valid values
        log.debug("\nTesting valid depth values:");
        for (int i : new int[]{1, 3, 10, 20}) {
            final int depth = i;
            assertDoesNotThrow(() -> options.setMaxLogSerializationDepth(depth),
                "Depth " + depth + " should be valid");
            log.debug("✓ Depth {} - Valid", depth);
        }
        
        // Test invalid values
        log.debug("\nTesting invalid depth values (should throw exception):");
        for (int i : new int[]{0, -1, 21, 100}) {
            final int depth = i;
            Exception ex = assertThrows(IllegalArgumentException.class,
                () -> options.setMaxLogSerializationDepth(depth),
                "Depth " + depth + " should throw IllegalArgumentException");
            log.debug("✓ Depth {} - Correctly rejected: {}", depth, ex.getMessage());
        }
        
        log.debug("\n✓ Configuration validation working correctly");
    }

    @Test
    void verifyThreadSafety() throws InterruptedException {
        log.debug("\n╔═══════════════════════════════════════════════════════════════╗");
        log.debug("║  Verification: Thread Safety                                  ║");
        log.debug("╚═══════════════════════════════════════════════════════════════╝");
        
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final boolean[] success = new boolean[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                try {
                    io.swagger.v3.oas.models.media.Schema<?> schema = new io.swagger.v3.oas.models.media.Schema<>();
                    schema.setName("Schema" + threadId);
                    schema.setType("object");
                    
                    // Create self-reference
                    Map<String, io.swagger.v3.oas.models.media.Schema> props = new HashMap<>();
                    props.put("self", schema);
                    schema.setProperties(props);
                    
                    String output = safeSwaggerSerializer.serialize(schema);
                    success[threadId] = output != null && !output.isEmpty();
                    
                    if (success[threadId]) {
                        log.debug("Thread {} ✓ Success: {}", threadId, output);
                    }
                } catch (Exception e) {
                    log.error("Thread {} ✗ Failed: {}", threadId, e.getMessage());
                    success[threadId] = false;
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Verify all succeeded
        for (int i = 0; i < threadCount; i++) {
            assertTrue(success[i], "Thread " + i + " should have succeeded");
        }
        
        log.debug("\n✓ All {} threads completed successfully - Thread safety verified", threadCount);
    }
}
