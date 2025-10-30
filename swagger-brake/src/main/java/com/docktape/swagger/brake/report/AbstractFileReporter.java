package com.docktape.swagger.brake.report;

import com.docktape.swagger.brake.core.ApiInfo;
import java.io.File;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.report.file.DirectoryCreator;
import com.docktape.swagger.brake.report.file.FileWriter;
import com.docktape.swagger.brake.runner.Options;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractFileReporter implements Reporter {
    private final FileWriter fileWriter;
    private final DirectoryCreator directoryCreator;

    @Override
    public void report(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, Options options, ApiInfo apiInfo) {
        if (StringUtils.isBlank(options.getOutputFilePath())) {
            log.warn("No file reporting has been done since output file path is not set");
            return;
        }
        String json = toFileContent(breakingChanges, ignoredBreakingChanges, apiInfo);
        try {
            directoryCreator.create(options.getOutputFilePath());
            String filePath = options.getOutputFilePath() + File.separator + getFilename(apiInfo);
            fileWriter.write(filePath, json);
            log.info("Report can be found at {}", filePath);
        } catch (Exception e) {
            throw new RuntimeException("An exception occurred while writing the report file", e);
        }
    }

    protected abstract String getFilename(ApiInfo apiInfo);

    protected abstract String toFileContent(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, ApiInfo apiInfo);
}
