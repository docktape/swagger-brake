package com.docktape.swagger.brake.runner.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiVersionContextTest {
    
    @Test
    void testContextSetAndGet() {
        try (OpenApiVersionContext context = new OpenApiVersionContext(OpenApiVersion.V3_1_X)) {
            OpenApiVersion version = OpenApiVersionContext.getCurrentVersion();
            assertEquals(OpenApiVersion.V3_1_X, version);
        }
    }
    
    @Test
    void testContextClearedAfterClose() {
        try (OpenApiVersionContext context = new OpenApiVersionContext(OpenApiVersion.V3_0_X)) {
            assertNotNull(OpenApiVersionContext.getCurrentVersion());
        }
        // After try-with-resources closes, context should be cleared
        assertNull(OpenApiVersionContext.getCurrentVersion());
    }
    
    @Test
    void testContextIsolatedAcrossThreads() throws InterruptedException {
        try (OpenApiVersionContext context = new OpenApiVersionContext(OpenApiVersion.V3_0_X)) {
            assertEquals(OpenApiVersion.V3_0_X, OpenApiVersionContext.getCurrentVersion());
            
            // Create a new thread that should not see this context
            Thread thread = new Thread(() -> {
                assertNull(OpenApiVersionContext.getCurrentVersion());
                
                // Set a different version in the new thread
                try (OpenApiVersionContext threadContext = new OpenApiVersionContext(OpenApiVersion.V3_1_X)) {
                    assertEquals(OpenApiVersion.V3_1_X, OpenApiVersionContext.getCurrentVersion());
                }
                assertNull(OpenApiVersionContext.getCurrentVersion());
            });
            
            thread.start();
            thread.join();
            
            // Original thread should still have its context
            assertEquals(OpenApiVersion.V3_0_X, OpenApiVersionContext.getCurrentVersion());
        }
    }
    
    @Test
    void testGetCurrentVersion_ReturnsNullWhenNotSet() {
        assertNull(OpenApiVersionContext.getCurrentVersion());
    }
}
