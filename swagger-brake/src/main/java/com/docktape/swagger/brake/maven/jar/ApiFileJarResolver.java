package com.docktape.swagger.brake.maven.jar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.jar.JarEntry;

import com.docktape.swagger.brake.maven.jar.filename.ApiFilenameCheckerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiFileJarResolver {
    private final JarScanner jarScanner;
    private final ApiFilenameCheckerFactory apiFilenameCheckerFactory;

    /**
     * Resolves the API file from a JAR identified by the parameter. If the resolution is successful,
     * a temporary file will be created just for the purpose of the execution.
     * @param parameter the {@link ApiFileResolverParameter} that controls the behavior.
     * @return the {@link File} instance pointing to the Swagger file.
     */
    public File resolve(ApiFileResolverParameter parameter) {
        try {
            File jarFile = parameter.getApiJar();
            log.debug("Attempting to resolve swagger file from external JAR {}", jarFile.getAbsolutePath());
            URL swaggerFile = findSwaggerFile(jarFile, parameter.getConfiguredApiFilename());
            String fileName = new File(swaggerFile.getFile()).getName();
            File destination = Files.createTempFile("swagger-brake", fileName).toFile();
            log.debug("Extracting swagger file from {} to {}", swaggerFile, destination.getAbsolutePath());
            FileCopyUtils.copy(swaggerFile.openConnection().getInputStream(), new FileOutputStream(destination));
            log.debug("Extraction done");
            return destination;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL findSwaggerFile(File jarFile, String configuredApiFilename) throws IOException {
        return jarScanner.find(jarFile, entry -> isSwaggerFile(entry, configuredApiFilename))
            .map(e -> getSwaggerFileUrl(jarFile, e))
            .orElseThrow(() -> new IllegalStateException("Swagger file is not present in the artifact"));
    }

    private URL getSwaggerFileUrl(File jarFile, JarEntry entry) {
        try {
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {jarFile.toURI().toURL()});
            return urlClassLoader.getResource(entry.getName());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSwaggerFile(JarEntry entry, String configuredApiFilename) {
        String entryName = entry.getName();
        return apiFilenameCheckerFactory.create(configuredApiFilename).isApiFile(entryName);
    }
}
