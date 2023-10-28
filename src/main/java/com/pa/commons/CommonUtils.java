package com.pa.commons;

import java.util.Objects;

public class CommonUtils {

    public static final String normalizeString(String s) {
        return s.replace('ی', 'ي').replace('ک', 'ك');
    }
    public static final boolean isNull(String str) {
        return str == null || "".equals(str) || "null".equals(str) || str.trim().isEmpty();
    }

    public static final boolean isNull(Object obj) {
        if (obj == null)
            return true;
        return isNull(obj.toString());
    }

    public static final boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * mansour: Checks if two strings are the same
     */
    public static final boolean isEqual(String toCompareStr, String constStr) {
        return (!isNull(toCompareStr) && !isNull(constStr) &&
                normalizeString(constStr).equals(normalizeString(toCompareStr)));
    }

    public static final boolean isEqual(Object obj, String constStr) {
        if (obj == null)
            return false;
        String str = obj.toString();
        return (isEqual(str, constStr));
    }

    public static final boolean isEqual(Object obj1, Object obj2) {
        return (isNull(obj1) && isNull(obj2)) || (obj1 != null && obj1.equals(obj2));
    }

    public static final String getString(Object object) {
        if (object != null) {
            return object.toString();
        } else {
            return "";
        }
    }

    public static final String getStringValue(Object columnValue) {
        if (isNull(columnValue)) {
            return "";
        } else {

            return columnValue.toString();
        }
    }


    public static String fixFarsiNumbers(String s) {
        s = s.replace((char) (1776), (char) (48));              // 0
        s = s.replace((char) (1777), (char) (49));              // 1
        s = s.replace((char) (1778), (char) (50));              // 2
        s = s.replace((char) (1779), (char) (51));              // 3
        s = s.replace((char) (1780), (char) (52));              // 4
        s = s.replace((char) (1781), (char) (53));              // 5
        s = s.replace((char) (1782), (char) (54));              // 6
        s = s.replace((char) (1783), (char) (55));              // 7
        s = s.replace((char) (1784), (char) (56));              // 8
        s = s.replace((char) (1785), (char) (57));              // 9
        return s;
    }

}
