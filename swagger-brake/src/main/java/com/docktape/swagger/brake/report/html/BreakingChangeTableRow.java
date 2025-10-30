package com.docktape.swagger.brake.report.html;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.docktape.swagger.brake.core.BreakingChange;
import lombok.Data;

@Data
public class BreakingChangeTableRow {
    @JsonUnwrapped
    private BreakingChange breakingChange;
    private String message;
    private String ruleCode;

    /**
     * Constructs a table row for reporting purposes.
     * @param breakingChange the breaking change
     */
    public BreakingChangeTableRow(BreakingChange breakingChange) {
        this.breakingChange = breakingChange;
        this.message = breakingChange.getMessage();
        this.ruleCode = breakingChange.getRuleCode();
    }
}
