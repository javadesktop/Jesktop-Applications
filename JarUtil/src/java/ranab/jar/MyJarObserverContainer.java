
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.util.*;
import java.util.zip.*;


/**
 * Class MyJarObserverContainer
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MyJarObserverContainer extends Vector implements MyJarObserver {

    /**
         * default constructor
         */
    public MyJarObserverContainer() {}

    /**
     * add observer
     */
    public void addObserver(MyJarObserver observer) {

        if (!contains(observer)) {
            add(observer);
        }
    }

    /**
     * remove observer
     */
    public void removeObserver(MyJarObserver observer) {

        if (contains(observer)) {
            remove(observer);
        }
    }

    /**
     * start observing
     */
    public void start() {

        for (int i = 0; i < size(); i++) {
            MyJarObserver obsr = (MyJarObserver) elementAt(i);

            obsr.start();
        }
    }

    /**
     * set count
     */
    public void setCount(int count) {

        for (int i = 0; i < size(); i++) {
            MyJarObserver obsr = (MyJarObserver) elementAt(i);

            obsr.setCount(count);
        }
    }

    /**
     * set next entry
     */
    public void setNext(ZipEntry je) {

        for (int i = 0; i < size(); i++) {
            MyJarObserver obsr = (MyJarObserver) elementAt(i);

            obsr.setNext(je);
        }
    }

    /**
     * set error message
     */
    public void setError(String errMsg) {

        for (int i = 0; i < size(); i++) {
            MyJarObserver obsr = (MyJarObserver) elementAt(i);

            obsr.setError(errMsg);
        }
    }

    /**
     * stop observing
     */
    public void end() {

        for (int i = 0; i < size(); i++) {
            MyJarObserver obsr = (MyJarObserver) elementAt(i);

            obsr.end();
        }
    }
}
