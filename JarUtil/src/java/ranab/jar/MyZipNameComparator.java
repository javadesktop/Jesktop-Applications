
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.util.zip.ZipEntry;


/**
 * compare two zip entries based on the file name
 */
public class MyZipNameComparator extends MyZipComparator {

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

        return getName(z1).compareTo(getName(z2));
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

        return getName(z1).equals(getName(z2));
    }

    /**
     * get the ZipEntry file name
     */
    public static String getName(ZipEntry entry) {

        String fullName = entry.getName();
        int index = fullName.lastIndexOf('/');

        if (index == -1) {
            return fullName;
        } else if (index == (fullName.length() - 1)) {
            return "";
        }

        return fullName.substring(index + 1);
    }

    /**
     * get header name
     */
    public String getHeaderName() {
        return "Name";
    }
}
