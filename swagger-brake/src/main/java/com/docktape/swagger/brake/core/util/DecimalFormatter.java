package com.docktape.swagger.brake.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DecimalFormatter {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    static {
        DECIMAL_FORMAT.setMaximumFractionDigits(2);
        DECIMAL_FORMAT.setMinimumFractionDigits(0);
        DECIMAL_FORMAT.setGroupingUsed(false);
    }

    public static String format(BigDecimal bigDecimal) {
        return DECIMAL_FORMAT.format(bigDecimal);
    }
}
