
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.util.Comparator;


/**
 * interface of all the zip entry comparators
 */
public abstract class MyZipComparator implements Comparator {

    /**
     * Method getHeaderName
     *
     *
     * @return
     *
     */
    public abstract String getHeaderName();

    /**
     * Method toString
     *
     *
     * @return
     *
     */
    public String toString() {
        return getHeaderName();
    }
}
