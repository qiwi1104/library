package qiwi;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeFormat {
    public static String formatTime(String oldFormat, String newFormat, String stringToParse) {
        SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat);
        SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat);

        java.util.Date date;

        try {
            date = oldDateFormat.parse(stringToParse);
        } catch (ParseException e) {
            date = Date.valueOf("1970-1-1");
            e.printStackTrace();
        }

        return newDateFormat.format(date);
    }
}
