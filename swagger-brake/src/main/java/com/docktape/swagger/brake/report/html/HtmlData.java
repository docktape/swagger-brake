package com.docktape.swagger.brake.report.html;

import java.util.Collection;

import lombok.Data;

@Data
public class HtmlData {
    private Collection<BreakingChangeTableRow> breakingChanges;
    private Collection<BreakingChangeTableRow> ignoredBreakingChanges;
}
