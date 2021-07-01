package qiwi;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeFormat {
    public static String formatTime(String oldFormat, String newFormat, String stringToParse) throws ParseException {
        SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat);
        SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat);

        java.util.Date date = oldDateFormat.parse(stringToParse);

        return newDateFormat.format(date);
    }
}
