package com.docktape.swagger.brake.core.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;

class SafeSwaggerSerializerTest {
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

    @AfterEach
    void tearDown() {
        // Ensure ThreadLocal is cleaned up (should be done automatically by serializer)
        System.clearProperty("SWAGGER_BRAKE_ENABLE_METRICS_LOGGING");
    }

    @Test
    void testSerializeNullReturnsNullString() {
        assertEquals("null", serializer.serialize(null));
    }

    @Test
    void testSerializeString() {
        String input = "test string";
        assertEquals("test string", serializer.serialize(input));
    }

    @Test
    void testSerializeNumber() {
        assertAll(
                () -> assertEquals("42", serializer.serialize(42)),
                () -> assertEquals("3.14", serializer.serialize(3.14))
        );
    }

    @Test
    void testSerializeBoolean() {
        assertAll(
                () -> assertEquals("true", serializer.serialize(true)),
                () -> assertEquals("false", serializer.serialize(false))
        );
    }

    @Test
    void testSerializeSimpleSchema() {
        Schema<?> schema = new Schema<>();
        schema.setName("TestSchema");
        schema.setType("object");
        
        String result = serializer.serialize(schema);

        assertAll(
                () -> assertTrue(result.contains("Schema")),
                () -> assertTrue(result.contains("name=TestSchema")),
                () -> assertTrue(result.contains("type=object"))
        );
    }

    @Test
    void testSerializeSchemaWithCircularReference() {
        Schema<?> businessSchema = new Schema<>();
        businessSchema.setName("Business");
        businessSchema.setType("object");
        
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
        
        // This should not throw StackOverflowError
        String result = serializer.serialize(businessSchema);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("Schema")),
                () -> assertTrue(result.contains("Business") || result.contains("CIRCULAR"))
        );
    }

    @Test
    void testSerializeParameter() {
        Parameter parameter = new Parameter();
        parameter.setName("testParam");
        parameter.setIn("query");
        parameter.setRequired(true);
        
        String result = serializer.serialize(parameter);

        assertAll(
                () -> assertTrue(result.contains("Parameter")),
                () -> assertTrue(result.contains("name=testParam")),
                () -> assertTrue(result.contains("in=query"))
        );
    }

    @Test
    void testSerializeApiResponse() {
        ApiResponse response = new ApiResponse();
        response.setDescription("Test response");
        
        String result = serializer.serialize(response);

        assertAll(
                () -> assertTrue(result.contains("ApiResponse")),
                () -> assertTrue(result.contains("description=Test response"))
        );
    }

    @Test
    void testSerializeCollection() {
        List<String> list = Arrays.asList("item1", "item2", "item3");
        
        String result = serializer.serialize(list);

        assertAll(
                () -> assertTrue(result.contains("item1")),
                () -> assertTrue(result.contains("item2")),
                () -> assertTrue(result.contains("item3"))
        );
    }

    @Test
    void testSerializeMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        String result = serializer.serialize(map);

        assertAll(
                () -> assertTrue(result.contains("key1")),
                () -> assertTrue(result.contains("value1")),
                () -> assertTrue(result.contains("key2")),
                () -> assertTrue(result.contains("value2"))
        );
    }

    @Test
    void testDepthLimitingWithDefaultDepth() {
        // Default depth is 3
        Schema<?> level1 = new Schema<>();
        level1.setName("Level1");
        
        Schema<?> level2 = new Schema<>();
        level2.setName("Level2");
        
        Schema<?> level3 = new Schema<>();
        level3.setName("Level3");
        
        Schema<?> level4 = new Schema<>();
        level4.setName("Level4");
        
        Map<String, Schema> props1 = new HashMap<>();
        props1.put("child", level2);
        level1.setProperties(props1);
        
        Map<String, Schema> props2 = new HashMap<>();
        props2.put("child", level3);
        level2.setProperties(props2);
        
        Map<String, Schema> props3 = new HashMap<>();
        props3.put("child", level4);
        level3.setProperties(props3);
        
        String result = serializer.serialize(level1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("Level1")),
                // Level4 should be truncated due to depth limit
                () -> assertTrue(result.contains("MAX_DEPTH") || !result.contains("Level4"))
        );
    }

    @Test
    void testDepthLimitingWithCustomDepth() {
        checkerOptions.setMaxLogSerializationDepth(2);
        
        Schema<?> level1 = new Schema<>();
        level1.setName("Level1");
        
        Schema<?> level2 = new Schema<>();
        level2.setName("Level2");
        
        Schema<?> level3 = new Schema<>();
        level3.setName("Level3");
        
        Map<String, Schema> props1 = new HashMap<>();
        props1.put("child", level2);
        level1.setProperties(props1);
        
        Map<String, Schema> props2 = new HashMap<>();
        props2.put("child", level3);
        level2.setProperties(props2);
        
        String result = serializer.serialize(level1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("Level1")),
                // Level3 should be truncated due to depth limit of 2
                () -> assertTrue(result.contains("MAX_DEPTH") || !result.contains("Level3"))
        );
    }

    @Test
    void testThreadLocalIsolation() throws InterruptedException {
        Schema<?> schema1 = new Schema<>();
        schema1.setName("Schema1");
        
        Schema<?> schema2 = new Schema<>();
        schema2.setName("Schema2");
        
        final String[] thread1Result = new String[1];
        final String[] thread2Result = new String[1];
        
        Thread t1 = new Thread(() -> {
            thread1Result[0] = serializer.serialize(schema1);
        });
        
        Thread t2 = new Thread(() -> {
            thread2Result[0] = serializer.serialize(schema2);
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertAll(
                () -> assertTrue(thread1Result[0].contains("Schema1")),
                () -> assertTrue(thread2Result[0].contains("Schema2")),
                () -> assertNotEquals(thread1Result[0], thread2Result[0])
        );
    }

    @Test
    void testSerializeWithMetricsEnabled() {
        System.setProperty("SWAGGER_BRAKE_ENABLE_METRICS_LOGGING", "true");
        
        Schema<?> schema = new Schema<>();
        schema.setName("TestSchema");
        
        // Should not throw exception even with metrics enabled
        String result = serializer.serialize(schema);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("Schema"))
        );
    }

    @Test
    void testMultipleSerializationsReuseContext() {
        Schema<?> schema1 = new Schema<>();
        schema1.setName("Schema1");
        
        Schema<?> schema2 = new Schema<>();
        schema2.setName("Schema2");
        
        String result1 = serializer.serialize(schema1);
        String result2 = serializer.serialize(schema2);
        
        assertAll(
                () -> assertNotNull(result1),
                () -> assertNotNull(result2),
                () -> assertTrue(result1.contains("Schema1")),
                () -> assertTrue(result2.contains("Schema2"))
        );
    }
}
