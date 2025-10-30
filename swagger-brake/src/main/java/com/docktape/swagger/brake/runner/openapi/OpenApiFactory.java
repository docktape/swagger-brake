package com.docktape.swagger.brake.runner.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import org.springframework.stereotype.Component;

/**
 * The class is responsible for loading an OpenAPI definition.
 */
@Component
public class OpenApiFactory {
    /**
     * The method loads an OpenAPI definition from the file system.
     * @param path the path of the definition file. It can be a relative path or an absolute one as well.
     * @return the {@link OpenAPI} object instance representing the definition
     * @throws IllegalStateException in case any error happens
     */
    public OpenAPI fromFile(String path) {
        try {
            OpenAPI loadedApi = loadV3Api(path);
            if (loadedApi == null) {
                throw new IllegalStateException("API cannot be loaded from path " + path);
            }
            return loadedApi;
        } catch (Exception e) {
            throw new IllegalStateException("API cannot be loaded from path " + path, e);
        }
    }

    private OpenAPI loadV3Api(String path) {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolveFully(true);
        return new OpenAPIV3Parser().read(path, null, parseOptions);
    }
}
