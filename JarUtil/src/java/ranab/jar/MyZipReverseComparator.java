
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
public class MyZipReverseComparator extends MyZipComparator {

    private MyZipComparator mOrigComp;

    /**
     * Constructor MyZipReverseComparator
     *
     *
     * @param comp
     *
     */
    public MyZipReverseComparator(MyZipComparator comp) {
        mOrigComp = comp;
    }

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
        return mOrigComp.compare(o2, o1);
    }

    /**
     * Method getHeaderName
     *
     *
     * @return
     *
     */
    public String getHeaderName() {
        return mOrigComp.getHeaderName();
    }
}
