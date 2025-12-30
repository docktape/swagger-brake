package com.docktape.swagger.brake.core.model.transformer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.util.SafeSwaggerSerializer;

import io.swagger.v3.oas.models.media.Schema;

/**
 * Integration test to verify that circular references in OpenAPI schemas
 * do not cause StackOverflowError. This simulates the real-world scenario
 * where Business schema references AuctionNumber schema, which references
 * back to Business schema.
 */
class CircularReferenceIntegrationTest {

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

    @Test
    void testCircularReference_BusinessToAuctionNumber_NoStackOverflowError() {
        // Create Business schema
        Schema<?> businessSchema = new Schema<>();
        businessSchema.setName("Business");
        businessSchema.setType("object");
        
        // Create AuctionNumber schema
        Schema<?> auctionNumberSchema = new Schema<>();
        auctionNumberSchema.setName("AuctionNumber");
        auctionNumberSchema.setType("object");
        
        // Create circular reference: Business -> AuctionNumber -> Business
        Map<String, Schema> businessProperties = new HashMap<>();
        businessProperties.put("auctionNumber", auctionNumberSchema);
        businessSchema.setProperties(businessProperties);
        
        Map<String, Schema> auctionNumberProperties = new HashMap<>();
        auctionNumberProperties.put("business", businessSchema);
        auctionNumberSchema.setProperties(auctionNumberProperties);
        
        // This should NOT throw StackOverflowError
        assertDoesNotThrow(() -> {
            String serialized = safeSwaggerSerializer.serialize(businessSchema);
            assertNotNull(serialized, "Serialized output should not be null");
            assertTrue(serialized.contains("Schema"), "Should contain Schema type");
        }, "Circular reference should not cause StackOverflowError");
    }

    @Test
    void testDeepCircularReference_MultipleNestingLevels() {
        // Create a more complex scenario with deeper nesting
        Schema<?> level1 = new Schema<>();
        level1.setName("Level1");
        level1.setType("object");
        
        Schema<?> level2 = new Schema<>();
        level2.setName("Level2");
        level2.setType("object");
        
        Schema<?> level3 = new Schema<>();
        level3.setName("Level3");
        level3.setType("object");
        
        // Create nested structure with circular reference back to level1
        Map<String, Schema> props1 = new HashMap<>();
        props1.put("child", level2);
        level1.setProperties(props1);
        
        Map<String, Schema> props2 = new HashMap<>();
        props2.put("child", level3);
        level2.setProperties(props2);
        
        Map<String, Schema> props3 = new HashMap<>();
        props3.put("backToRoot", level1);
        level3.setProperties(props3);
        
        // Should handle deep circular references without StackOverflowError
        assertDoesNotThrow(() -> {
            String serialized = safeSwaggerSerializer.serialize(level1);
            assertNotNull(serialized, "Serialized output should not be null");
        }, "Deep circular reference should not cause StackOverflowError");
    }

    @Test
    void testSelfReferencingSchema() {
        // Create a schema that references itself
        Schema<?> selfRefSchema = new Schema<>();
        selfRefSchema.setName("SelfReferencing");
        selfRefSchema.setType("object");
        
        Map<String, Schema> properties = new HashMap<>();
        properties.put("self", selfRefSchema);
        selfRefSchema.setProperties(properties);
        
        // Self-referencing should not cause StackOverflowError
        assertDoesNotThrow(() -> {
            String serialized = safeSwaggerSerializer.serialize(selfRefSchema);
            assertNotNull(serialized, "Serialized output should not be null");
        }, "Self-referencing schema should not cause StackOverflowError");
    }

    @Test
    void testCircularReferenceWithCustomDepth() {
        // Test with custom depth setting
        CheckerOptions customOptions = new CheckerOptions();
        customOptions.setMaxLogSerializationDepth(2);
        when(checkerOptionsProvider.get()).thenReturn(customOptions);
        
        Schema<?> schema1 = new Schema<>();
        schema1.setName("Schema1");
        schema1.setType("object");
        
        Schema<?> schema2 = new Schema<>();
        schema2.setName("Schema2");
        schema2.setType("object");
        
        Map<String, Schema> props1 = new HashMap<>();
        props1.put("ref", schema2);
        schema1.setProperties(props1);
        
        Map<String, Schema> props2 = new HashMap<>();
        props2.put("ref", schema1);
        schema2.setProperties(props2);
        
        // Should respect custom depth limit and not throw StackOverflowError
        assertDoesNotThrow(() -> {
            String serialized = safeSwaggerSerializer.serialize(schema1);
            assertNotNull(serialized, "Serialized output should not be null");
            assertTrue(serialized.contains("Schema"), "Should contain Schema type");
        }, "Custom depth setting should prevent StackOverflowError");
    }

    @Test
    void testMultipleCircularPaths() {
        // Create schema with multiple circular paths
        Schema<?> root = new Schema<>();
        root.setName("Root");
        root.setType("object");
        
        Schema<?> branchA = new Schema<>();
        branchA.setName("BranchA");
        branchA.setType("object");
        
        Schema<?> branchB = new Schema<>();
        branchB.setName("BranchB");
        branchB.setType("object");
        
        // Both branches reference back to root
        Map<String, Schema> rootProps = new HashMap<>();
        rootProps.put("branchA", branchA);
        rootProps.put("branchB", branchB);
        root.setProperties(rootProps);
        
        Map<String, Schema> branchAProps = new HashMap<>();
        branchAProps.put("backToRoot", root);
        branchA.setProperties(branchAProps);
        
        Map<String, Schema> branchBProps = new HashMap<>();
        branchBProps.put("backToRoot", root);
        branchB.setProperties(branchBProps);
        
        // Multiple circular paths should be handled
        assertDoesNotThrow(() -> {
            String serialized = safeSwaggerSerializer.serialize(root);
            assertNotNull(serialized, "Serialized output should not be null");
        }, "Multiple circular paths should not cause StackOverflowError");
    }
}
