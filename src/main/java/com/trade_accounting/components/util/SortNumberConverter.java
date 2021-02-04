package com.trade_accounting.components.util;

public class SortNumberConverter {

    private SortNumberConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static String convert(String sortNumber) {
        int number = sortNumber.length();
        switch (number) {
            case 1:
                return "0000" + sortNumber;
            case 2:
                return "000" + sortNumber;
            case 3:
                return "00" + sortNumber;
            case 4:
                return "0" + sortNumber;
            default:
                return sortNumber;
        }
    }

}
