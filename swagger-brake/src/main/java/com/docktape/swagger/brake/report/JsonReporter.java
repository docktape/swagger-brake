package com.docktape.swagger.brake.report;

import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.report.file.DirectoryCreator;
import com.docktape.swagger.brake.report.file.FileWriter;
import com.docktape.swagger.brake.report.json.JsonConverter;
import com.docktape.swagger.brake.runner.OutputFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class JsonReporter extends AbstractFileReporter implements CheckableReporter {
    private static final String DEFAULT_FILENAME = "swagger-brake.json";
    private static final String VERSIONED_FILENAME_TEMPLATE = "swagger-brake-%s.json";
    private final JsonConverter jsonConverter;

    public JsonReporter(FileWriter fileWriter, DirectoryCreator directoryCreator, JsonConverter jsonConverter) {
        super(fileWriter, directoryCreator);
        this.jsonConverter = jsonConverter;
    }

    @Override
    protected String getFilename(ApiInfo apiInfo) {
        String result = DEFAULT_FILENAME;
        if (apiInfo != null && isNotBlank(apiInfo.getVersion())) {
            result = VERSIONED_FILENAME_TEMPLATE.formatted(apiInfo.getVersion());
        }
        return result;
    }

    @Override
    protected String toFileContent(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, ApiInfo apiInfo) {
        Map<String, List<BreakingChange>> nameMapping = breakingChanges.stream().collect(groupingBy(BreakingChange::getRuleCode));
        Map<String, List<BreakingChange>> ignoredNameMapping = ignoredBreakingChanges.stream().collect(groupingBy(BreakingChange::getRuleCode));
        return jsonConverter.convert(new JsonContent(apiInfo, nameMapping, ignoredNameMapping));
    }

    @Override
    public boolean canReport(OutputFormat format) {
        return OutputFormat.JSON.equals(format);
    }

    @RequiredArgsConstructor
    @Getter
    static class JsonContent {
        private final ApiInfo info;
        private final Map<String, List<BreakingChange>> breakingChanges;
        private final Map<String, List<BreakingChange>> ignoredBreakingChanges;
    }
}
