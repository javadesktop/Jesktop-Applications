
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.util;



import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

import java.text.DateFormat;
import java.text.ParseException;


/**
 * this is a timezone conversion utility class
 */
public class MyDateUtils {

    /**
     * Get the TimeZone specific string
     */
    public static String getString(Date dt, DateFormat df, TimeZone to) {

        df.setTimeZone(to);

        return df.format(dt);
    }

    /**
     * Get the TimeZone specific calendar
     */
    public static Calendar getCalendar(Date dt, TimeZone to) {

        Calendar cal = Calendar.getInstance(to);

        cal.setTime(dt);

        return cal;
    }

    /**
     * Get date object
     */
    public static Date getDate(String str, DateFormat df, TimeZone from)
            throws java.text.ParseException {

        df.setTimeZone(from);

        return df.parse(str);
    }
}
