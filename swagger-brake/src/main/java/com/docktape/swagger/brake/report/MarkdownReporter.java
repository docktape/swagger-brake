package com.docktape.swagger.brake.report;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.report.file.DirectoryCreator;
import com.docktape.swagger.brake.report.file.FileWriter;
import com.docktape.swagger.brake.runner.OutputFormat;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class MarkdownReporter extends AbstractFileReporter implements CheckableReporter {
    private static final String DEFAULT_FILENAME = "swagger-brake.md";
    private static final String VERSIONED_FILENAME_TEMPLATE = "swagger-brake-%s.md";

    public MarkdownReporter(FileWriter fileWriter, DirectoryCreator directoryCreator) {
        super(fileWriter, directoryCreator);
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
        StringBuilder sb = new StringBuilder();
        sb.append("# swagger-brake Report\n\n");
        appendSection(sb, "Breaking Changes", breakingChanges);
        sb.append("\n");
        appendSection(sb, "Ignored Breaking Changes", ignoredBreakingChanges);
        return sb.toString();
    }

    private void appendSection(StringBuilder sb, String title, Collection<BreakingChange> breakingChanges) {
        sb.append("## ").append(title).append(" (").append(breakingChanges.size()).append(")\n");
        sb.append("| Rule | Message |\n");
        sb.append("|------|---------|\n");
        for (BreakingChange bc : breakingChanges) {
            sb.append("| ").append(escape(bc.getRuleCode())).append(" | ").append(escape(bc.getMessage())).append(" |\n");
        }
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "\\|");
    }

    @Override
    public boolean canReport(OutputFormat format) {
        return OutputFormat.MARKDOWN.equals(format);
    }
}
