package com.docktape.swagger.brake.integration.support;

import java.io.File;
import java.net.URISyntaxException;

public class ClasspathFileLoader {
    public static String getAbsolutePath(String classpathResourcePath) {
        try {
            return new File(ClasspathFileLoader.class.getClassLoader().getResource(classpathResourcePath).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unable to get absolute path", e);
        }
    }
}
