package com.docktape.swagger.brake.report.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileWriter {
    /**
     * Writes the content to a file identified by filePath.
     * @param filePath the path to the file.
     * @param content the content to be written into the file.
     */
    public void write(String filePath, String content) {
        try {
            log.debug("File path {}", filePath);
            Path path = Paths.get(filePath);
            Files.write(path, Collections.singleton(content));
        } catch (IOException e) {
            throw new RuntimeException("Error happened while writing the output file", e);
        }
    }
}
