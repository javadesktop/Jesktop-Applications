
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
public class MyZipCompressedSizeComparator extends MyZipComparator {

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
        long diff = z1.getCompressedSize() - z2.getCompressedSize();

        if (diff < 0) {
            return -1;
        } else if (diff > 0) {
            return 1;
        }

        return 0;
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

        return z1.getCompressedSize() == z2.getCompressedSize();
    }

    /**
     * get header name
     */
    public String getHeaderName() {
        return "Compressed Size";
    }
}
