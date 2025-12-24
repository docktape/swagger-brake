package com.docktape.swagger.brake.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.docktape.swagger.brake.core.CheckerOptionsProvider;

import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Safe serialization utility for logging Swagger/OpenAPI objects without triggering StackOverflowError
 * with circular references. Uses ThreadLocal-based cycle detection and configurable depth limiting.
 *
 * <p>Format: Type + key identifying information (e.g., "Schema[name=Business]", "Parameter[name=userId]")
 *
 * <p>Metrics logging can be enabled via environment variable: SWAGGER_BRAKE_ENABLE_METRICS_LOGGING=true
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SafeSwaggerSerializer {
    private final CheckerOptionsProvider checkerOptionsProvider;

    private static final ThreadLocal<SerializationContext> CONTEXT = ThreadLocal.withInitial(SerializationContext::new);
    private static final boolean METRICS_ENABLED = "true".equalsIgnoreCase(System.getenv("SWAGGER_BRAKE_ENABLE_METRICS_LOGGING"));

    // Metrics tracking (only if enabled)
    private static final AtomicInteger totalSerializations = new AtomicInteger(0);
    private static final AtomicInteger cyclesDetected = new AtomicInteger(0);
    private static final AtomicInteger depthLimitReached = new AtomicInteger(0);

    /**
     * Serialization context for tracking cycles and depth per thread.
     */
    private static class SerializationContext {
        final Set<Object> seenObjects = Collections.newSetFromMap(new IdentityHashMap<>());
        int currentDepth = 0;

        void reset() {
            seenObjects.clear();
            currentDepth = 0;
        }
    }

    /**
     * Safely serializes a Swagger/OpenAPI object for logging purposes.
     *
     * @param obj the object to serialize (can be null)
     * @return a safe string representation
     */
    public String serialize(Object obj) {
        if (METRICS_ENABLED) {
            totalSerializations.incrementAndGet();
        }

        SerializationContext context = CONTEXT.get();
        boolean isTopLevelCall = context.currentDepth == 0;
        if (isTopLevelCall) {
            context.reset();
        }

        try {
            int maxDepth = getMaxDepth();
            return serializeInternal(obj, context, maxDepth);
        } finally {
            if (isTopLevelCall) {
                context.reset();
                CONTEXT.remove();
            }
        }
    }

    /**
     * Internal serialization with cycle detection and depth limiting.
     */
    private String serializeInternal(Object obj, SerializationContext context, int maxDepth) {
        // Handle null
        if (obj == null) {
            return "null";
        }

        // Handle primitives and simple types
        if (isPrimitiveOrSimple(obj)) {
            return obj.toString();
        }

        // Check depth limit
        if (context.currentDepth >= maxDepth) {
            if (METRICS_ENABLED) {
                depthLimitReached.incrementAndGet();
            }
            return String.format("[MAX_DEPTH:%d]", maxDepth);
        }

        // Check for cycles
        if (context.seenObjects.contains(obj)) {
            if (METRICS_ENABLED) {
                cyclesDetected.incrementAndGet();
            }
            return "[CIRCULAR_REF]";
        }

        // Mark as seen
        context.seenObjects.add(obj);
        context.currentDepth++;

        try {
            return formatObject(obj, context, maxDepth);
        } finally {
            context.currentDepth--;
            // Note: We keep object in seenObjects until full serialization completes (handled in reset())
        }
    }

    /**
     * Formats the object based on its type.
     */
    private String formatObject(Object obj, SerializationContext context, int maxDepth) {
        // Handle Schema objects
        if (obj instanceof Schema<?> schema) {
            return formatSchema(schema);
        }

        // Handle Parameter objects
        if (obj instanceof Parameter param) {
            return formatParameter(param);
        }

        // Handle ApiResponse objects
        if (obj instanceof ApiResponse response) {
            return formatApiResponse(response);
        }

        // Handle Collections
        if (obj instanceof Collection<?> collection) {
            return formatCollection(collection, context, maxDepth);
        }

        // Handle Maps
        if (obj instanceof Map<?, ?> map) {
            return formatMap(map, context, maxDepth);
        }

        // Fallback: class name (should not be null at this point)
        Class<?> objClass = obj.getClass();
        return objClass.getSimpleName() + "[...]";
    }

    private String formatSchema(Schema<?> schema) {
        StringBuilder sb = new StringBuilder("Schema[");
        
        // Add type
        if (schema.getType() != null) {
            sb.append("type=").append(schema.getType());
        }

        // Add name if available
        if (schema.getName() != null) {
            if (sb.length() > 7) {
                sb.append(", ");
            }
            sb.append("name=").append(schema.getName());
        }

        // Add $ref if available
        if (schema.get$ref() != null) {
            if (sb.length() > 7) {
                sb.append(", ");
            }
            sb.append("$ref=").append(schema.get$ref());
        }

        // Add title if available and name not present
        if (schema.getTitle() != null && schema.getName() == null) {
            if (sb.length() > 7) {
                sb.append(", ");
            }
            sb.append("title=").append(schema.getTitle());
        }

        sb.append("]");
        return sb.toString();
    }

    private String formatParameter(Parameter param) {
        StringBuilder sb = new StringBuilder("Parameter[");
        
        if (param.getName() != null) {
            sb.append("name=").append(param.getName());
        }

        if (param.getIn() != null) {
            if (sb.length() > 10) {
                sb.append(", ");
            }
            sb.append("in=").append(param.getIn());
        }

        sb.append("]");
        return sb.toString();
    }

    private String formatApiResponse(ApiResponse response) {
        StringBuilder sb = new StringBuilder("ApiResponse[");
        
        if (response.getDescription() != null) {
            String desc = response.getDescription();
            if (desc.length() > 50) {
                desc = desc.substring(0, 47) + "...";
            }
            sb.append("description=").append(desc);
        }

        sb.append("]");
        return sb.toString();
    }

    private String formatCollection(Collection<?> collection, SerializationContext context, int maxDepth) {
        if (collection.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        int count = 0;
        int limit = 3; // Show first 3 elements

        for (Object item : collection) {
            if (count > 0) {
                sb.append(", ");
            }
            if (count >= limit) {
                sb.append("... +").append(collection.size() - limit).append(" more");
                break;
            }
            sb.append(serializeInternal(item, context, maxDepth));
            count++;
        }

        sb.append("]");
        return sb.toString();
    }

    private String formatMap(Map<?, ?> map, SerializationContext context, int maxDepth) {
        if (map.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        int count = 0;
        int limit = 3; // Show first 3 entries

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (count > 0) {
                sb.append(", ");
            }
            if (count >= limit) {
                sb.append("... +").append(map.size() - limit).append(" more");
                break;
            }
            sb.append(serializeInternal(entry.getKey(), context, maxDepth));
            sb.append("=");
            sb.append(serializeInternal(entry.getValue(), context, maxDepth));
            count++;
        }

        sb.append("}");
        return sb.toString();
    }

    private boolean isPrimitiveOrSimple(Object obj) {
        return obj instanceof String
            || obj instanceof Number
            || obj instanceof Boolean
            || obj instanceof Character
            || obj instanceof Enum;
    }

    private int getMaxDepth() {
        try {
            return checkerOptionsProvider.get().getMaxLogSerializationDepth();
        } catch (RuntimeException e) {
            // CheckerOptions not set yet (e.g., during early initialization)
            // Fall back to default
            log.debug("CheckerOptions not available yet, using default max depth: 3");
            return 3;
        }
    }

    /**
     * Returns current metrics. Only meaningful if SWAGGER_BRAKE_ENABLE_METRICS_LOGGING=true.
     */
    public static String getMetrics() {
        if (!METRICS_ENABLED) {
            return "Metrics not enabled. Set SWAGGER_BRAKE_ENABLE_METRICS_LOGGING=true";
        }
        return String.format("SafeSwaggerSerializer Metrics - Total: %d, Cycles: %d, DepthLimits: %d",
            totalSerializations.get(), cyclesDetected.get(), depthLimitReached.get());
    }

    /**
     * Resets metrics. For testing purposes.
     */
    public static void resetMetrics() {
        totalSerializations.set(0);
        cyclesDetected.set(0);
        depthLimitReached.set(0);
    }
}
