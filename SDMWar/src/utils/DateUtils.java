package utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date jsonDateToJavaDate(String jsonDate) {
        int year = Integer.parseInt(jsonDate.substring(0,4));
        int month  = Integer.parseInt(jsonDate.substring(5,7)) - 1;
        int date = Integer.parseInt(jsonDate.substring(8,10));
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, date);

        return calendar.getTime();
    }

    public static String javaDateToJsonDate(Date javaDate) {
        Calendar cal =  Calendar.getInstance();

        cal.setTime(javaDate);
        String year = Integer.toString(cal.get(Calendar.YEAR));
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String date = Integer.toString(cal.get(Calendar.DATE));

        return date + "/" + month + "/" +year;
    }
}

