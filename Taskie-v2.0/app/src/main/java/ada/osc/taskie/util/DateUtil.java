package ada.osc.taskie.util;

import java.text.DateFormat;
import java.util.Date;

public class DateUtil {
    public static boolean isDateBeforeToday(Date date) {
        /*
         * DateFormat is needed for when the user picks the same date as today.
         * The before() method compares by milliseconds and will always say that
         * the selected date is before current date. But the format() method returns
         * a string based on the day, so by comparing their string representations,
         * we can conclude whether they are the same date or not.
         */
        DateFormat df = DateFormat.getDateInstance();
        return date.before(new Date()) && !df.format(date).equals(df.format(new Date()));
    }
}
