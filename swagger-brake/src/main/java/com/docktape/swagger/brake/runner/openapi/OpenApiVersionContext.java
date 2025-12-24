package com.docktape.swagger.brake.runner.openapi;

import java.lang.ref.WeakReference;
import lombok.extern.slf4j.Slf4j;

/**
 * Thread-local context for tracking OpenAPI version during spec processing.
 * Uses WeakReference to allow garbage collection and implements AutoCloseable for cleanup.
 */
@Slf4j
public class OpenApiVersionContext implements AutoCloseable {
    private static final ThreadLocal<WeakReference<OpenApiVersion>> VERSION_CONTEXT = new ThreadLocal<>();
    
    private final OpenApiVersion version;
    
    /**
     * Creates a new version context and sets it for the current thread.
     * 
     * @param version the OpenAPI version to set
     */
    public OpenApiVersionContext(OpenApiVersion version) {
        this.version = version;
        VERSION_CONTEXT.set(new WeakReference<>(version));
        log.trace("OpenAPI version context set for thread {}: {}", Thread.currentThread().getName(), version);
    }
    
    /**
     * Gets the current OpenAPI version for this thread.
     * 
     * @return the current OpenAPI version, or null if not set or garbage collected
     */
    public static OpenApiVersion getCurrentVersion() {
        WeakReference<OpenApiVersion> ref = VERSION_CONTEXT.get();
        return ref != null ? ref.get() : null;
    }
    
    /**
     * Clears the version context for the current thread.
     * This method is automatically called when used with try-with-resources.
     */
    @Override
    public void close() {
        log.trace("Clearing OpenAPI version context for thread {}: {}", Thread.currentThread().getName(), version);
        VERSION_CONTEXT.remove();
    }
}
