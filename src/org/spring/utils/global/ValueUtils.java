package org.spring.utils.global;

import java.util.regex.Pattern;

public class ValueUtils {
    /**
     * 将字符串转换成正确类型的内容
     */
    public static Object stringConvert(String value){
        if (isInteger(value)){
            return Integer.valueOf(value);
        }else if (isDouble(value)){
            return Double.valueOf(value);
        }else if (isBoolean(value)){
            return Boolean.valueOf(value);
        }
        return value;
    }

    public static boolean isInteger(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }

    public static boolean isBoolean(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(true|false)$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str).matches();
    }
}
