
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.util.Date;
import java.util.zip.ZipEntry;


/**
 * compare two zip entries based on the file name
 */
public class MyZipModificationTimeComparator extends MyZipComparator {

    /**
     * Method compare
     *
     *
     * @param o1
     * @param o2
     *
     * @return
     *
     */
    public int compare(Object o1, Object o2) {

        if (!((o1 instanceof java.util.zip.ZipEntry) && (o1 instanceof java.util.zip.ZipEntry))) {
            throw new IllegalArgumentException("Not a ZipEntry object.");
        }

        ZipEntry z1 = (ZipEntry) o1;
        ZipEntry z2 = (ZipEntry) o2;
        Date dt1 = new Date(z1.getTime());
        Date dt2 = new Date(z2.getTime());

        return dt1.compareTo(dt2);
    }

    /**
     * Method equals
     *
     *
     * @param o1
     * @param o2
     *
     * @return
     *
     */
    public boolean equals(Object o1, Object o2) {

        if (!((o1 instanceof java.util.zip.ZipEntry) && (o1 instanceof java.util.zip.ZipEntry))) {
            throw new IllegalArgumentException("Not a ZipEntry object.");
        }

        ZipEntry z1 = (ZipEntry) o1;
        ZipEntry z2 = (ZipEntry) o2;

        return z1.getTime() == z2.getTime();
    }

    /**
     * get header name
     */
    public String getHeaderName() {
        return "Modification Time";
    }
}
