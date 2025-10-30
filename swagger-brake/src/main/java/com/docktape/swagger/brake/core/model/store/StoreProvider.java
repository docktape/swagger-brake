package com.docktape.swagger.brake.core.model.store;

public class StoreProvider {
    private static final ThreadLocal<SchemaStore> schemaStore = new ThreadLocal<>();
    private static final ThreadLocal<ParameterStore> parameterStore = new ThreadLocal<>();
    private static final ThreadLocal<ResponseStore> responseStore = new ThreadLocal<>();

    public static void setSchemaStore(SchemaStore store) {
        schemaStore.set(store);
    }

    public static void setParameterStore(ParameterStore store) {
        parameterStore.set(store);
    }

    public static void setResponseStore(ResponseStore store) {
        responseStore.set(store);
    }

    public static SchemaStore provideSchemaStore() {
        return schemaStore.get();
    }

    public static ParameterStore provideParameterStore() {
        return parameterStore.get();
    }

    public static ResponseStore provideResponseStore() {
        return responseStore.get();
    }

    /**
     * Clears all the threadlocal stores.
     */
    public static void clear() {
        schemaStore.remove();
        parameterStore.remove();
        responseStore.remove();
    }
}
