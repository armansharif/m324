package com.dam.commons.utils;


import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss z");
    static final DateTimeFormatter SIMPLE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss");

    public static String getTimeNow() {
        return ZonedDateTime.now(ZoneId.of("Asia/Tehran")).format(FORMATTER);
    }

    public static String getLocalTimeNow() {
        return LocalDateTime.now().format(SIMPLE_FORMATTER);
    }

    public static Timestamp getTimestampNow() {
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static String convertTimestamp(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.format(SIMPLE_FORMATTER);
    }

    public static Timestamp convertTimeString(String time) {
        LocalDateTime from = LocalDateTime.from(SIMPLE_FORMATTER.parse(time));
        return Timestamp.valueOf(from);
    }

    public static boolean isNotNull(String s) {
        return !isNull(s);
    }

    public static boolean isNull(String s) {
        return s == null || s.equals("") || s.equalsIgnoreCase("null");
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNull(Object obj) {
        if (obj == null)
            return true;
        String str = obj.toString();
        return isNull(str);
    }

    public static boolean isEqual(String s1, String s2) {
        return (!isNull(s1) && !isNull(s2) && s1.equals(s2));
    }

    public static String getWhereSimple(){
        return  " where 1=1";
    }

    public static Field getDeclaredField(Class className, String fieldName) {
        Field field = null;
        while (className != null && field == null) {
            try {
                field = className.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
            }
            className = className.getSuperclass();
        }
        return field;
    }
}
