
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.util;



import java.io.*;

import java.awt.*;

import java.util.*;

import java.text.*;


/**
 * Class MyProperties
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MyProperties extends Properties {

    /**
     * constructors
     */
    public MyProperties() {}

    /**
     * Constructor MyProperties
     *
     *
     * @param prop
     *
     */
    public MyProperties(Properties prop) {
        super(prop);
    }

    /**
     * Constructor MyProperties
     *
     *
     * @param fl
     *
     * @throws IOException
     *
     */
    public MyProperties(File fl) throws IOException {

        FileInputStream fis = new FileInputStream(fl);

        load(fis);
        fis.close();
    }

    /**
     * Constructor MyProperties
     *
     *
     * @param is
     *
     * @throws IOException
     *
     */
    public MyProperties(InputStream is) throws IOException {
        load(is);
    }

    /**
     * get boolean value
     */
    public boolean getBoolean(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        return Boolean.getBoolean(str);
    }

    /**
     * Method getBoolean
     *
     *
     * @param str
     * @param bol
     *
     * @return
     *
     */
    public boolean getBoolean(String str, boolean bol) {

        try {
            return getBoolean(str);
        } catch (MyPropertiesException ex) {
            return bol;
        }
    }

    /**
     * get integer value
     */
    public int getInteger(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getInteger
     *
     *
     * @param str
     * @param intVal
     *
     * @return
     *
     */
    public int getInteger(String str, int intVal) {

        try {
            return getInteger(str);
        } catch (MyPropertiesException ex) {
            return intVal;
        }
    }

    /**
     * get long value
     */
    public long getLong(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getLong
     *
     *
     * @param str
     * @param val
     *
     * @return
     *
     */
    public long getLong(String str, long val) {

        try {
            return getLong(str);
        } catch (MyPropertiesException ex) {
            return val;
        }
    }

    /**
     * get double value
     */
    public double getDouble(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getDouble
     *
     *
     * @param str
     * @param doubleVal
     *
     * @return
     *
     */
    public double getDouble(String str, double doubleVal) {

        try {
            return getDouble(str);
        } catch (MyPropertiesException ex) {
            return doubleVal;
        }
    }

    /**
     * get file object
     */
    public File getFile(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        return new File(str);
    }

    /**
     * Method getFile
     *
     *
     * @param str
     * @param fl
     *
     * @return
     *
     */
    public File getFile(String str, File fl) {

        try {
            return getFile(str);
        } catch (MyPropertiesException ex) {
            return fl;
        }
    }

    /**
     * get color property
     */
    public Color getColor(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            return new Color(Integer.parseInt(str, 16));
        } catch (NumberFormatException ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getColor
     *
     *
     * @param str
     * @param val
     *
     * @return
     *
     */
    public Color getColor(String str, Color val) {

        try {
            return getColor(str);
        } catch (MyPropertiesException ex) {
            return val;
        }
    }

    /**
     * get dimension property
     */
    public Dimension getDimension(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            StringTokenizer st = new StringTokenizer(str, ",");
            int width = Integer.parseInt(st.nextToken());
            int height = Integer.parseInt(st.nextToken());

            return new Dimension(width, height);
        } catch (Exception ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getDimension
     *
     *
     * @param str
     * @param val
     *
     * @return
     *
     */
    public Dimension getDimension(String str, Dimension val) {

        try {
            return getDimension(str);
        } catch (MyPropertiesException ex) {
            return val;
        }
    }

    /**
     * get font property
     */
    public Font getFont(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            StringTokenizer st = new StringTokenizer(str, ",");
            String name = st.nextToken();
            int style = Integer.parseInt(st.nextToken());
            int size = Integer.parseInt(st.nextToken());

            return new Font(name, style, size);
        } catch (Exception ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getFont
     *
     *
     * @param str
     * @param ft
     *
     * @return
     *
     */
    public Font getFont(String str, Font ft) {

        try {
            return getFont(str);
        } catch (MyPropertiesException ex) {
            return ft;
        }
    }

    /**
     * get insets
     */
    public Insets getInsets(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            StringTokenizer st = new StringTokenizer(str, ",");
            int top = Integer.parseInt(st.nextToken());
            int left = Integer.parseInt(st.nextToken());
            int bottom = Integer.parseInt(st.nextToken());
            int right = Integer.parseInt(st.nextToken());

            return new Insets(top, left, bottom, right);
        } catch (Exception ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getInsets
     *
     *
     * @param str
     * @param ins
     *
     * @return
     *
     */
    public Insets getInsets(String str, Insets ins) {

        try {
            return getInsets(str);
        } catch (MyPropertiesException ex) {
            return ins;
        }
    }

    /**
     * get class object
     */
    public Class getClass(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            return Class.forName(str);
        } catch (ClassNotFoundException ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getClass
     *
     *
     * @param str
     * @param cls
     *
     * @return
     *
     */
    public Class getClass(String str, Class cls) {

        try {
            return getClass(str);
        } catch (MyPropertiesException ex) {
            return cls;
        }
    }

    /**
     * get timezone
     */
    public TimeZone getTimeZone(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        return TimeZone.getTimeZone(str);
    }

    /**
     * Method getTimeZone
     *
     *
     * @param str
     * @param tz
     *
     * @return
     *
     */
    public TimeZone getTimeZone(String str, TimeZone tz) {

        try {
            return getTimeZone(str);
        } catch (MyPropertiesException ex) {
            return tz;
        }
    }

    /**
     * get date format
     */
    public SimpleDateFormat getDateFormat(String str) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        return new SimpleDateFormat(str);
    }

    /**
     * Method getDateFormat
     *
     *
     * @param str
     * @param fmt
     *
     * @return
     *
     */
    public SimpleDateFormat getDateFormat(String str, SimpleDateFormat fmt) {

        try {
            return getDateFormat(str);
        } catch (MyPropertiesException ex) {
            return fmt;
        }
    }

    /**
     * get date object
     */
    public Date getDate(String str, DateFormat fmt) throws MyPropertiesException {

        str = getProperty(str);

        if (str == null) {
            throw new MyPropertiesException(str + " : not found");
        }

        try {
            return fmt.parse(str);
        } catch (ParseException ex) {
            throw new MyPropertiesException(ex);
        }
    }

    /**
     * Method getDate
     *
     *
     * @param str
     * @param fmt
     * @param dt
     *
     * @return
     *
     */
    public Date getDate(String str, DateFormat fmt, Date dt) {

        try {
            return getDate(str, fmt);
        } catch (MyPropertiesException ex) {
            return dt;
        }
    }

    /**
     * set boolean property
     */
    public void setProperty(String key, boolean val) {
        setProperty(key, String.valueOf(val));
    }

    /**
     * set integer property
     */
    public void setProperty(String key, int val) {
        setProperty(key, String.valueOf(val));
    }

    /**
     * set double property
     */
    public void setProperty(String key, double val) {
        setProperty(key, String.valueOf(val));
    }

    /**
     * set float property
     */
    public void setProperty(String key, float val) {
        setProperty(key, String.valueOf(val));
    }

    /**
     * set long property
     */
    public void setProperty(String key, long val) {
        setProperty(key, String.valueOf(val));
    }

    /**
     * set file property
     */
    public void setProperty(String key, File val) {
        setProperty(key, val.getAbsolutePath());
    }

    /**
     * set color property
     */
    public void setProperty(String key, Color val) {

        String str = Integer.toHexString(val.getRGB());

        setProperty(key, str);
    }

    /**
     * set dimension property
     */
    public void setProperty(String key, Dimension val) {

        String str = String.valueOf(val.width) + ',' + String.valueOf(val.height);

        setProperty(key, str);
    }

    /**
     * set font property
     */
    public void setProperty(String key, Font val) {

        String str = val.getName() + ',' + val.getStyle() + ',' + val.getSize();

        setProperty(key, str);
    }

    /**
     * set insets property
     */
    public void setProperty(String key, Insets val) {

        String str = String.valueOf(val.top) + ',' + String.valueOf(val.left) + ','
                     + String.valueOf(val.bottom) + ',' + String.valueOf(val.right);

        setProperty(key, str);
    }

    /**
     * set dateformat property
     */
    public void setProperty(String key, SimpleDateFormat val) {
        setProperty(key, val.toPattern());
    }

    /**
     * set timezone
     */
    public void setProperty(String key, TimeZone val) {
        setProperty(key, val.getID());
    }

    /**
     * set date property
     */
    public void setProperty(String key, Date val, DateFormat fmt) {
        setProperty(key, fmt.format(val));
    }

    /**
     * set class property
     */
    public void setProperty(String key, Class val) {
        setProperty(key, val.getName());
    }
}
