package com.docktape.swagger.brake.report.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class DirectoryCreator {
    public void create(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }
}
