package utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date jsonDateToJavaDate(String jsonDate) {
        int year = Integer.parseInt(jsonDate.substring(0,3));
        int month  = Integer.parseInt(jsonDate.substring(5,6));
        int date = Integer.parseInt(jsonDate.substring(8,9));
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, date);

        return calendar.getTime();
    }
}
