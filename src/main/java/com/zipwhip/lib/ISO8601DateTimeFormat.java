package com.zipwhip.lib;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Format and parse an ISO 8601 DateTimeFormat used in XML documents.
 * This lexical representation is the ISO 8601
 * extended format CCYY-MM-DDThh:mm:ss
 * where "CC" represents the century, "YY" the year, "MM" the month
 * and "DD" the day,
 * preceded by an optional leading "-" sign to indicate a negative number.
 * If the sign is omitted, "+" is assumed.
 * The letter "T" is the date/time separator
 * and "hh", "mm", "ss" represent hour, minute and second respectively.
 * This representation may be immediately followed by a "Z" to indicate
 * Coordinated Universal Time (UTC) or, to indicate the time zone,
 * i.e. the difference between the local time and Coordinated Universal Time,
 * immediately followed by a sign, + or -,
 * followed by the difference from UTC represented as hh:mm.
 */
public class ISO8601DateTimeFormat extends DateFormat {

    /**
     * Construct a new ISO8601DateTimeFormat using the default time zone.
     */
    public ISO8601DateTimeFormat() {
        setCalendar(Calendar.getInstance());
    }

    /**
     * Construct a new ISO8601DateTimeFormat using a specific time zone.
     *
     * @param tz The time zone used to format and parse the date.
     */
    public ISO8601DateTimeFormat(TimeZone tz) {
        setCalendar(Calendar.getInstance(tz));
    }

    /**
     * @see DateFormat#parse(String, java.text.ParsePosition)
     */
    public Date parse(String text, ParsePosition pos) {

        int i = pos.getIndex();

        try {
            int year = Integer.valueOf(text.substring(i, i + 4)).intValue();
            i += 4;

            if (text.charAt(i) != '-') {
                throw new NumberFormatException();
            }
            i++;

            int month = Integer.valueOf(text.substring(i, i + 2)).intValue() - 1;
            i += 2;

            if (text.charAt(i) != '-') {
                throw new NumberFormatException();
            }
            i++;

            int day = Integer.valueOf(text.substring(i, i + 2)).intValue();
            i += 2;

            if (text.charAt(i) != 'T') {
                throw new NumberFormatException();
            }
            i++;

            int hour = Integer.valueOf(text.substring(i, i + 2)).intValue();
            i += 2;

            if (text.charAt(i) != ':') {
                throw new NumberFormatException();
            }
            i++;

            int mins = Integer.valueOf(text.substring(i, i + 2)).intValue();
            i += 2;

            int secs = 0;
            if (i < text.length() && text.charAt(i) == ':') {
                // handle seconds flexible
                i++;

                secs = Integer.valueOf(text.substring(i, i + 2)).intValue();
                i += 2;
            }

            calendar.set(year, month, day, hour, mins, secs);
            calendar.set(Calendar.MILLISECOND, 0); // no parts of a second

            i = parseTZ(i, text);

        } catch (NumberFormatException ex) {
            pos.setErrorIndex(i);
            return null;
        } catch (IndexOutOfBoundsException ex) {
            pos.setErrorIndex(i);
            return null;
        } finally {
            pos.setIndex(i);
        }

        return calendar.getTime();
    }

    /**
     * Parse the time zone.
     *
     * @param i    The position to start parsing.
     * @param text The text to parse.
     * @return The position after parsing has finished.
     */
    protected final int parseTZ(int i, String text) {
        if (i < text.length()) {
            // check and handle the zone/dst offset
            int offset = 0;
            if (text.charAt(i) == 'Z') {
                offset = 0;
                i++;
            } else {
                int sign = 1;
                if (text.charAt(i) == '-') {
                    sign = -1;
                } else if (text.charAt(i) != '+') {
                    throw new NumberFormatException();
                }
                i++;

                int offsetHour = Integer.valueOf(text.substring(i, i + 2)).intValue();
                i += 2;

                if (text.charAt(i) != ':') {
                    throw new NumberFormatException();
                }
                i++;

                int offsetMin = Integer.valueOf(text.substring(i, i + 2)).intValue();
                i += 2;
                offset = ((offsetHour * 60) + offsetMin) * 60000 * sign;
            }
            int offsetCal =
                    calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);

            calendar.add(Calendar.MILLISECOND, offsetCal - offset);
        }
        return i;
    }

    /**
     * @see DateFormat#format(Date, StringBuffer, java.text.FieldPosition)
     */
    public StringBuffer format(
            Date date,
            StringBuffer sbuf,
            FieldPosition fieldPosition) {

        calendar.setTime(date);

        writeCCYYMM(sbuf);

        sbuf.append('T');

        writehhmmss(sbuf);

        writeTZ(sbuf);

        return sbuf;
    }

    /**
     * Write the time zone string.
     *
     * @param sbuf The buffer to append the time zone.
     */
    protected final void writeTZ(StringBuffer sbuf) {
        int offset =
                calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
        if (offset == 0) {
            sbuf.append('Z');
        } else {
            int offsetHour = offset / 3600000;
            int offsetMin = (offset % 3600000) / 60000;
            if (offset >= 0) {
                sbuf.append('+');
            } else {
                sbuf.append('-');
                offsetHour = 0 - offsetHour;
                offsetMin = 0 - offsetMin;
            }
            appendInt(sbuf, offsetHour, 2);
            sbuf.append(':');
            appendInt(sbuf, offsetMin, 2);
        }
    }

    /**
     * Write hour, minutes, and seconds.
     *
     * @param sbuf The buffer to append the string.
     */
    protected final void writehhmmss(StringBuffer sbuf) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        appendInt(sbuf, hour, 2);
        sbuf.append(':');

        int mins = calendar.get(Calendar.MINUTE);
        appendInt(sbuf, mins, 2);
        sbuf.append(':');

        int secs = calendar.get(Calendar.SECOND);
        appendInt(sbuf, secs, 2);
    }

    /**
     * Write century, year, and months.
     *
     * @param sbuf The buffer to append the string.
     */
    protected final void writeCCYYMM(StringBuffer sbuf) {
        int year = calendar.get(Calendar.YEAR);
        appendInt(sbuf, year, 4);

        String month;
        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                month = "-01-";
                break;
            case Calendar.FEBRUARY:
                month = "-02-";
                break;
            case Calendar.MARCH:
                month = "-03-";
                break;
            case Calendar.APRIL:
                month = "-04-";
                break;
            case Calendar.MAY:
                month = "-05-";
                break;
            case Calendar.JUNE:
                month = "-06-";
                break;
            case Calendar.JULY:
                month = "-07-";
                break;
            case Calendar.AUGUST:
                month = "-08-";
                break;
            case Calendar.SEPTEMBER:
                month = "-09-";
                break;
            case Calendar.OCTOBER:
                month = "-10-";
                break;
            case Calendar.NOVEMBER:
                month = "-11-";
                break;
            case Calendar.DECEMBER:
                month = "-12-";
                break;
            default:
                month = "-NA-";
                break;
        }
        sbuf.append(month);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        appendInt(sbuf, day, 2);
    }

    /**
     * Write an integer value with leading zeros.
     *
     * @param buf    The buffer to append the string.
     * @param value  The value to write.
     * @param length The length of the string to write.
     */
    protected final void appendInt(StringBuffer buf, int value, int length) {
        int len1 = buf.length();
        buf.append(value);
        int len2 = buf.length();
        for (int i = len2; i < len1 + length; ++i) {
            buf.insert(len1, '0');
        }
    }
}
