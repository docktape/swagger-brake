package com.docktape.swagger.brake.maven.jar.filename;

import org.springframework.core.Ordered;

public interface ApiFileNameChecker extends Ordered {
    boolean isApiFile(String fileName);
}
