
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.util;



import java.util.Vector;
import java.util.Properties;
import java.util.Enumeration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * Thisa is a simple string replacement class. I am planning to add
 * some more methods to it.
 */
public class MyStringUtils {

    public final static char SEPARATOR = '\n';

    /**
     * this is string replacement fuction
     */
    public static String replaceString(String source, String oldStr, String newStr) {

        StringBuffer sb = new StringBuffer();
        int sind = 0;
        int cind = 0;

        while ((cind = source.indexOf(oldStr, sind)) != -1) {
            sb.append(source.substring(sind, cind));
            sb.append(newStr);

            sind = cind + oldStr.length();
        }

        sb.append(source.substring(sind));

        return sb.toString();
    }

    /**
     * This method is used to insert HTML block dynamically
     *
     * @param source the HTML code to be processes
     * @param bReplaceNl if true '\n' will be replaced by <br>
     * @param bReplaceTag if true '<' will be replaced by &lt; and
     *                          '>' will be replaced by &gt;
     * @param bReplaceQuote if true '\"' will be replaced by &quot;
     */
    public static String formatHtml(String source, boolean bReplaceNl, boolean bReplaceTag,
                                    boolean bReplaceQuote) {

        StringBuffer sb = new StringBuffer();
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char c = source.charAt(i);

            switch (c) {

            case '\"' :
                if (bReplaceQuote) {
                    sb.append("&quot;");
                } else {
                    sb.append(c);
                }
                break;

            case '<' :
                if (bReplaceTag) {
                    sb.append("&lt;");
                } else {
                    sb.append(c);
                }
                break;

            case '>' :
                if (bReplaceTag) {
                    sb.append("&gt;");
                } else {
                    sb.append(c);
                }
                break;

            case '\n' :
                if (bReplaceNl) {
                    if (bReplaceTag) {
                        sb.append("&lt;br&gt;");
                    } else {
                        sb.append("<br>");
                    }
                } else {
                    sb.append(c);
                }
                break;

            case '\r' :
                break;

            case '&' :
                sb.append("&amp;");
                break;

            default :
                sb.append(c);
                break;
            }
        }

        return sb.toString();
    }

    /**
     * create properties from key and value vectors. It also maintains the
     * sequence of data. Hashtable does not maintain it.
     */
    public static Properties createProperty(Vector key, Vector val, String seq) {

        int sz = (key.size() < val.size()) ? key.size() : val.size();
        Properties prop = new Properties();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < sz; i++) {
            prop.setProperty(key.elementAt(i).toString(), val.elementAt(i).toString());
            sb.append(key.elementAt(i)).append(SEPARATOR);
        }

        if (seq != null) {
            prop.setProperty(seq, sb.toString());
        }

        return prop;
    }

    /**
     * get string from property
     */
    public static String getStringFromProperty(Properties prop) {

        if (prop == null) {
            return null;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(256);

            prop.store(baos, null);
            baos.close();

            return baos.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * get property from string
     */
    public static Properties getPropertyFromString(String propStr) {

        if ((propStr != null) && (!propStr.equals(""))) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(propStr.getBytes());
                Properties prop = new Properties();

                prop.load(bais);
                bais.close();

                return prop;
            } catch (Exception ex) {}
        }

        return null;
    }
}
