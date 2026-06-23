package com.docktape.swagger.brake.report;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.docktape.swagger.brake.core.ApiInfo;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.report.file.DirectoryCreator;
import com.docktape.swagger.brake.report.file.FileWriter;
import com.docktape.swagger.brake.runner.OutputFormat;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
class JUnitReporter extends AbstractFileReporter implements CheckableReporter {
    private static final String DEFAULT_FILENAME = "swagger-brake-junit.xml";
    private static final String VERSIONED_FILENAME_TEMPLATE = "swagger-brake-%s-junit.xml";

    public JUnitReporter(FileWriter fileWriter, DirectoryCreator directoryCreator) {
        super(fileWriter, directoryCreator);
    }

    @Override
    protected String getFilename(ApiInfo apiInfo) {
        if (apiInfo != null && isNotBlank(apiInfo.getVersion())) {
            return VERSIONED_FILENAME_TEMPLATE.formatted(apiInfo.getVersion());
        }
        return DEFAULT_FILENAME;
    }

    @Override
    protected String toFileContent(Collection<BreakingChange> breakingChanges, Collection<BreakingChange> ignoredBreakingChanges, ApiInfo apiInfo) {
        int total = breakingChanges.size();
        int failures = total;
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<testsuites name=\"swagger-brake\">\n");
        sb.append("    <testsuite name=\"BreakingChanges\" tests=\"").append(total).append("\" failures=\"").append(failures).append("\">\n");
        for (BreakingChange bc : breakingChanges) {
            String name = escapeXml(bc.getRuleCode() + ": " + bc.getMessage());
            String message = escapeXml(bc.getMessage());
            sb.append("        <testcase name=\"").append(name).append("\" classname=\"swagger-brake\">\n");
            sb.append("            <failure message=\"").append(message).append("\" type=\"BreakingChange\"/>\n");
            sb.append("        </testcase>\n");
        }
        sb.append("    </testsuite>\n");
        sb.append("</testsuites>\n");
        return sb.toString();
    }

    @Override
    public boolean canReport(OutputFormat format) {
        return OutputFormat.JUNIT.equals(format);
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }
        return value
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }
}
