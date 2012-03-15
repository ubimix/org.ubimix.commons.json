/**
 * 
 */
package org.webreformatter.commons.json.ext;

import org.webreformatter.commons.json.JsonValue;
import org.webreformatter.commons.json.JsonValue.IJsonValueFactory;

/**
 * @author kotelnikov
 */
public class FormattedDate implements Comparable<FormattedDate> {

    public static final IJsonValueFactory<FormattedDate> FACTORY = new IJsonValueFactory<FormattedDate>() {
        public FormattedDate newValue(Object object) {
            String str = JsonValue.STRING_FACTORY.newValue(object);
            return new FormattedDate(str);
        }
    };

    private static void append(StringBuilder buf, int value) {
        append(buf, value, 2);
    }

    private static void append(StringBuilder buf, int value, int len) {
        String str = Integer.toString(value);
        for (int i = str.length(); i < len; i++) {
            buf.append('0');
        }
        buf.append(str);
    }

    protected String fDate = "1900-01-01T00:00:00Z";

    public FormattedDate(
        int year,
        int month,
        int day,
        int hour,
        int min,
        int sec) {
        StringBuilder buf = new StringBuilder();
        append(buf, year);
        buf.append("-");
        append(buf, month);
        buf.append("-");
        append(buf, day);
        buf.append("T");
        append(buf, hour);
        buf.append(":");
        append(buf, min);
        buf.append(":");
        append(buf, sec);
        buf.append("Z");
        fDate = buf.toString();
        checkAll();
    }

    /**
     * @param formattedDate
     */
    public FormattedDate(String formattedDate) {
        fDate = formattedDate;
        checkAll();
    }

    // Check validity of field values
    private void checkAll() {
        getYear();
        getMonth();
        getDay();
        getHour();
        getMinutes();
        getSeconds();
    }

    private void checkRange(String name, int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalStateException(name
                + " field should be in the range ["
                + min
                + ".."
                + max
                + "]");
        }
    }

    public int compareTo(FormattedDate o) {
        return fDate.compareTo(o.fDate);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FormattedDate)) {
            return false;
        }
        FormattedDate o = (FormattedDate) obj;
        return fDate.equals(o.fDate);
    }

    /**
     * @return the formatted date
     */
    public final String getDate() {
        return fDate;
    }

    /**
     * @return the current day of the month (values in the range [1..31])
     */
    public int getDay() {
        int day = Integer.parseInt(fDate.substring(8, 10));
        checkRange("Date", day, 1, 31);
        return day;
    }

    /**
     * @return the hour of the day (values in the range [0..23])
     */
    public int getHour() {
        int hour = Integer.parseInt(fDate.substring(11, 13));
        checkRange("Hour", hour, 0, 23);
        return hour;
    }

    /**
     * @return the minutes (values in the range [0..59])
     */
    public int getMinutes() {
        int min = Integer.parseInt(fDate.substring(14, 16));
        checkRange("Minutes", min, 0, 59);
        return min;
    }

    /**
     * @return the current month (values in the range [1..12])
     */
    public int getMonth() {
        int month = Integer.parseInt(fDate.substring(5, 7));
        checkRange("Month", month, 1, 12);
        return month;
    }

    /**
     * @return the seconds (values in the range [0..59])
     */
    public int getSeconds() {
        int sec = Integer.parseInt(fDate.substring(17, 19));
        checkRange("Seconds", sec, 0, 59);
        return sec;
    }

    /**
     * @return the current year (ex: 2011, 1923, etc)
     */
    public int getYear() {
        int year = Integer.parseInt(fDate.substring(0, 4));
        checkRange("Year", year, 0, 999999);
        return year;
    }

    @Override
    public final int hashCode() {
        return fDate.hashCode();
    }

    /**
     * @return the current day of the month (values in the range [1..31])
     */
    public void setDay(int date) {
        setValue("Day", date, 8, 10, 1, 31);
    }

    /**
     * Sets a new hour of the day (values in the range [0..23])
     * 
     * @param hour
     */
    public void setHour(int hour) {
        setValue("Hour", hour, 11, 13, 0, 23);
    }

    /**
     * Sets minutes (values in the range [0..59])
     */
    public void setMinutes(int value) {
        setValue("Minutes", value, 14, 16, 0, 59);
    }

    /**
     * Sets month values (in the range [1..12])
     */
    public void setMonth(int month) {
        setValue("Months", month, 5, 7, 1, 12);
    }

    /**
     * Sets seconds (values in the range [0..59])
     */
    public void setSeconds(int sec) {
        setValue("Months", sec, 17, 19, 0, 59);
    }

    private void setValue(
        String name,
        int value,
        int start,
        int stop,
        int min,
        int max) {
        checkRange(name, value, min, max);
        StringBuilder buf = new StringBuilder();
        buf.append(fDate.substring(0, start));
        int len = stop - start;
        String str = "" + value;
        for (int i = str.length(); i < len; i++) {
            buf.append('0');
        }
        buf.append(str);
        buf.append(fDate.substring(stop, fDate.length()));
        fDate = buf.toString();
    }

    /**
     * Sets the current year (ex: 2011, 1923, etc)
     */
    public void setYear(int year) {
        setValue("Year", year, 0, 4, 0, 999999);
    }

    @Override
    public final String toString() {
        return fDate;
    }
}
