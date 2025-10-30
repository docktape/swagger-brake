package com.docktape.swagger.brake.report;

import com.docktape.swagger.brake.runner.OutputFormat;

public interface CheckableReporter extends Reporter {
    boolean canReport(OutputFormat format);
}
