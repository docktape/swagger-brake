package com.docktape.swagger.brake.core.util;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

/**
 * Demo test to show the actual log output of SafeSwaggerSerializer
 * with various scenarios including circular references.
 */
@Slf4j
class SafeSwaggerSerializerLogOutputDemo {
    private CheckerOptionsProvider checkerOptionsProvider;
    private CheckerOptions checkerOptions;
    private SafeSwaggerSerializer serializer;

    @BeforeEach
    void setUp() {
        checkerOptionsProvider = Mockito.mock(CheckerOptionsProvider.class);
        checkerOptions = new CheckerOptions();
        when(checkerOptionsProvider.get()).thenReturn(checkerOptions);
        serializer = new SafeSwaggerSerializer(checkerOptionsProvider);
    }

    @Test
    void demoSimpleSchema() {
        log.info("\n=== Demo: Simple Schema ===");
        
        Schema<?> schema = new Schema<>();
        schema.setName("Pet");
        schema.setType("object");
        schema.setDescription("A pet in the pet store");
        
        String output = serializer.serialize(schema);
        log.debug("Output: {}", output);

        assertNotNull(output);
        assertTrue(output.contains("Pet"));
    }

    @Test
    void demoCircularReference_BusinessAuctionNumber() {
        log.debug("\n=== Demo: Circular Reference (Business -> AuctionNumber -> Business) ===");
        
        // Simulate the real-world scenario
        Schema<?> businessSchema = new Schema<>();
        businessSchema.setName("Business");
        businessSchema.setType("object");
        businessSchema.setDescription("Business entity");
        
        Schema<?> auctionNumberSchema = new Schema<>();
        auctionNumberSchema.setName("AuctionNumber");
        auctionNumberSchema.setType("string");
        auctionNumberSchema.setDescription("Auction number");
        
        // Create circular reference
        Map<String, Schema> businessProperties = new HashMap<>();
        businessProperties.put("auctionNumber", auctionNumberSchema);
        businessProperties.put("name", new Schema<>().type("string"));
        businessSchema.setProperties(businessProperties);
        
        Map<String, Schema> auctionNumberProperties = new HashMap<>();
        auctionNumberProperties.put("business", businessSchema);
        auctionNumberProperties.put("number", new Schema<>().type("string"));
        auctionNumberSchema.setProperties(auctionNumberProperties);

        log.debug("Before serialization - this would have caused StackOverflowError with toString()");
        String output = serializer.serialize(businessSchema);
        log.debug("After serialization - SUCCESS! No StackOverflowError");
        log.debug("Output: {}", output);
        
        assertNotNull(output);
        assertFalse(output.isEmpty());
    }

    @Test
    void demoSelfReferencingSchema() {
        log.debug("\n=== Demo: Self-Referencing Schema ===");
        
        Schema<?> treeNode = new Schema<>();
        treeNode.setName("TreeNode");
        treeNode.setType("object");
        
        Map<String, Schema> properties = new HashMap<>();
        properties.put("value", new Schema<>().type("string"));
        properties.put("parent", treeNode);  // Self-reference
        treeNode.setProperties(properties);
        
        String output = serializer.serialize(treeNode);
        log.debug("Output: {}", output);
        
        assertNotNull(output);
        assertTrue(output.contains("TreeNode"));
    }

    @Test
    void demoDepthLimiting() {
        log.debug("\n=== Demo: Depth Limiting (depth=2) ===");
        
        checkerOptions.setMaxLogSerializationDepth(2);
        
        Schema<?> level1 = new Schema<>();
        level1.setName("Level1");
        level1.setType("object");
        
        Schema<?> level2 = new Schema<>();
        level2.setName("Level2");
        level2.setType("object");
        
        Schema<?> level3 = new Schema<>();
        level3.setName("Level3");
        level3.setType("object");
        
        Schema<?> level4 = new Schema<>();
        level4.setName("Level4");
        level4.setType("object");
        
        Map<String, Schema> props1 = new HashMap<>();
        props1.put("child", level2);
        level1.setProperties(props1);
        
        Map<String, Schema> props2 = new HashMap<>();
        props2.put("child", level3);
        level2.setProperties(props2);
        
        Map<String, Schema> props3 = new HashMap<>();
        props3.put("child", level4);
        level3.setProperties(props3);
        
        String output = serializer.serialize(level1);
        log.debug("Output with depth limit 2: {}", output);
        log.debug("Note: Deeper levels are truncated to prevent excessive output");
        
        assertNotNull(output);
    }

    @Test
    void demoParameter() {
        log.debug("\n=== Demo: Parameter Serialization ===");
        
        Parameter param = new Parameter();
        param.setName("petId");
        param.setIn("path");
        param.setRequired(true);
        param.setDescription("ID of pet to fetch");
        
        String output = serializer.serialize(param);
        log.debug("Output: {}", output);
        
        assertNotNull(output);
        assertTrue(output.contains("petId"));
    }

    @Test
    void demoComparisonWithDefaultDepth() {
        log.debug("\n=== Demo: Comparing Different Depth Settings ===");
        
        Schema<?> root = createNestedSchema(5);
        
        // Default depth (3)
        log.debug("With default depth (3):");
        String output1 = serializer.serialize(root);
        log.debug(output1);
        
        // Depth 1 (minimal)
        checkerOptions.setMaxLogSerializationDepth(1);
        log.debug("\nWith depth 1 (minimal):");
        String output2 = serializer.serialize(root);
        log.debug(output2);
        
        // Depth 10 (detailed)
        checkerOptions.setMaxLogSerializationDepth(10);
        log.debug("\nWith depth 10 (detailed):");
        String output3 = serializer.serialize(root);
        log.debug(output3);
        
        assertNotNull(output1);
        assertNotNull(output2);
        assertNotNull(output3);
        
        // More detailed output should be longer (when no circular refs)
        log.debug("\nOutput lengths: depth-1= {}, depth-3={} , depth-10={}",
                output2.length(), output1.length(), output3.length());
    }

    private Schema<?> createNestedSchema(int depth) {
        Schema<?> schema = new Schema<>();
        schema.setName("Level" + depth);
        schema.setType("object");
        
        if (depth > 1) {
            Map<String, Schema> properties = new HashMap<>();
            properties.put("child", createNestedSchema(depth - 1));
            schema.setProperties(properties);
        }
        
        return schema;
    }
}
