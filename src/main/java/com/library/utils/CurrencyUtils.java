package com.library.utils;

import java.text.DecimalFormat;

public class CurrencyUtils {
    public static String doubleToVND(double value) {
        String patternVND = ",###₫";
        DecimalFormat decimalFormat = new DecimalFormat(patternVND);
        return decimalFormat.format(value);
    }
}
