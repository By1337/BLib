package org.by1337.blib.util.number;

import java.text.DecimalFormat;

public class NumberFormatter {

    private static final double ONE_K = 1_000;
    private static final DecimalFormat df = new DecimalFormat("#.####");

    public static String formatNumberK(Double number) {
        if (number < ONE_K) return df.format(number);
        double d = number / ONE_K;
        int x = 1;
        while (d > ONE_K){
            d /= ONE_K;
            x++;
        }
        return df.format(d) + "K".repeat(x);
    }

    public static String formatNumberWithThousandsSeparator(Double raw, String integerSeparator, String thousandSeparator){
        return formatNumberWithThousandsSeparator(df.format(raw), integerSeparator, thousandSeparator);
    }
    public static String formatNumberWithThousandsSeparator(String raw, String integerSeparator, String thousandSeparator){
        StringBuilder formatted = new StringBuilder();
        String[] parts = raw.split("\\D");
        String integerPart = parts[0];
        String decimalPart = (parts.length == 2) ? integerSeparator + parts[1] : "";

        char[] integerDigits = integerPart.toCharArray();
        for (int i = integerDigits.length - 1, count = 0; i >= 0; i--, count++) {
            if (count > 0 && count % 3 == 0) {
                formatted.append(thousandSeparator);
            }
            formatted.append(integerDigits[i]);
        }
        return formatted.reverse() + decimalPart;
    }
}
