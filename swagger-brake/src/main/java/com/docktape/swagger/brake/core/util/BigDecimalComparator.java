package com.docktape.swagger.brake.core.util;

import java.math.BigDecimal;

public class BigDecimalComparator {
    public static boolean isLessThan(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0;
    }

    public static boolean isEqual(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) == 0;
    }

    public static boolean isGreaterThan(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0;
    }
}
