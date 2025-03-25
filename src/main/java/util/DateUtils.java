package util;

import java.util.Date;

public class DateUtils {

    public static String formatDate(Date date) {
        if (date == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static Date parseDate(String dateText) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse(dateText);
        } catch (java.text.ParseException e) {
            return null;
        }
    }
}