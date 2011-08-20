package com.zipwhip.lib;

import org.apache.log4j.Logger;

import java.text.Format;
import java.text.ParseException;
import java.util.Date;

public class JsonParserUtil {

    private static Logger logger = Logger.getLogger(JsonParserUtil.class);
    //    private static final String Z_STRING = "Z";
//    private static final String GMT_TIME_ZONE = "+0000";
//    private static final Format GRAILS_DATE_FORMATTER = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;
    private static final Format GRAILS_DATE_FORMATTER = new ISO8601DateTimeFormat();
//    private static final Format GRAILS_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
//    private static final Format GRAILS_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    /**
     * Utility class --> private constructor
     */
    private JsonParserUtil() {
    }

    /**
     * Takes an object and returns a java.util.Date.
     * Supported objects are String (format: yyyy-MM-ddTHH:mm:ssZ)
     * and Long (date in milli seconds)
     * The time zone for the input date string is considered to be in GMT by
     * default (+0000)
     * Returns null in case of an exception
     *
     * @param date - Standard Grails Formatted String Date
     * @return Date - java.util.Date object
     */
    public static Date getDate(Object date) {
        //Check for null
        if (date == null) {
            return null;
        }

        //Call the right parser method depending on the input datatype
        if (date instanceof String) {
            return getDate(date.toString());
        } else if (date instanceof Long) {
            long longDate = (Long) date;
            return getDate(longDate);
        } else if (date instanceof Date) { // we need to handle the backwards compatible case
            return (Date) date;
        }

        //TODO: Printing to the command line instead of throwing an exception due to the way groovy handles exeptions...
        logger.debug("Unsupported datatype! " + date.getClass().getName() + " . Cannot parse the supplied dataype.");
        return null;
    }

    /**
     * Takes a string date in the grails default format (yyyy-MM-ddTHH:mm:ssZ)
     * and returns a java.util.Date
     * The time zone for the input date string is considered to be in GMT by
     * default (+0000)
     * Returns null in case of an exception
     *
     * @param stringDate - Standard Grails Formatted String Date
     * @return Date - java.util.Date
     */
    public static Date getDate(String stringDate) {
        //Check for null, string length
        if (stringDate == null || stringDate.length() <= 0) {
            return null;
        }

//        2010-03-06T04:16:24+00:00
//        2010-03-06T04:16:24
//        if (stringDate == null || stringDate.length() != 20) {
//            //throw new Exception("Invalid date format for date " + stringDate + ". Date should be in the format [yyyy-MM-ddTHH:mm:ssZ]. Example: [2010-02-04T18:13:04Z]");
//            //TODO: Printing to the command line instead of throwing an exception due to the way groovy handles exeptions...
//            logger.debug("Invalid date format for date " + stringDate + ". Date should be in the format [yyyy-MM-ddTHH:mm:ssZ]. Example: [2010-02-04T18:13:04Z]");
//            return null;
//        }

        //Parse and return date
        try {
            synchronized (GRAILS_DATE_FORMATTER) {
                return (Date) GRAILS_DATE_FORMATTER.parseObject(stringDate.toUpperCase());
            }
//            return (Date) GRAILS_DATE_FORMATTER.parseObject(stringDate.toUpperCase().replaceFirst(Z_STRING, GMT_TIME_ZONE));
        } catch (ParseException pex) {
            //TODO: Printing to the command line instead of throwing an exception due to the way groovy handles exeptions...
            pex.printStackTrace();
            return null;
        }
    }

    /**
     * Takes a long date in the grails default format and returns a
     * java.util.Date
     *
     * @param milliDate - date in milli seconds
     * @return Date - java.util.Date
     */
    public static Date getDate(long milliDate) {
        return new Date(milliDate);
    }
}
