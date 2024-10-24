package com.begcode.monolith.util;

public class MobileNumberValidator {

    // 中国大陆手机号码正则表达式
    private static final String CHINA_MOBILE_NUMBER_REGEX = "^1[3-9]\\d{9}$";

    public static boolean isValidChinaMobileNumber(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        return number.matches(CHINA_MOBILE_NUMBER_REGEX);
    }
}
