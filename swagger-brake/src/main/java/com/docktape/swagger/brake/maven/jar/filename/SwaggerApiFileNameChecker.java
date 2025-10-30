package com.docktape.swagger.brake.maven.jar.filename;

import org.springframework.core.Ordered;

class SwaggerApiFileNameChecker implements ApiFileNameChecker {
    @Override
    public boolean isApiFile(String fileName) {
        return fileName.endsWith("swagger.json")
            || fileName.endsWith("swagger.yml")
            || fileName.endsWith("swagger.yaml");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
