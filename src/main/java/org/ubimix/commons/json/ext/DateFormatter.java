package org.ubimix.commons.json.ext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author kotelnikov
 */
public class DateFormatter {

    // "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ssZ",
        Locale.UK);

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static FormattedDate formatDate(Date date) {
        FormattedDate result = null;
        if (date != null) {
            String str = DATE_FORMAT.format(date);
            result = new FormattedDate(str);
        }
        return result;
    }

    public static FormattedDate formatDate(long date) {
        return formatDate(new Date(date));
    }

    public static Date getDateTime(FormattedDate date) {
        try {
            Date d = DATE_FORMAT.parse(date.toString());
            return d;
        } catch (Throwable t) {
            return null;
        }
    }

    public static long getDays(int count) {
        int sec = 1000;
        int min = 60 * sec;
        int hour = 60 * min;
        int day = 24 * hour;
        long result = day;
        result *= count;
        return result;
    }

    public static long getNextDate(FormattedDate prevDate, long delta) {
        long time = prevDate != null ? getTime(prevDate) : now();
        time += delta;
        return time;
    }

    public static FormattedDate getNextFormattedDate(
        FormattedDate prevDate,
        long delta) {
        long time = getNextDate(prevDate, delta);
        return formatDate(time);
    }

    public static long getTime(FormattedDate date) {
        Date d = getDateTime(date);
        return d != null ? d.getTime() : 0;
    }

    protected static long now() {
        return System.currentTimeMillis();
    }
}