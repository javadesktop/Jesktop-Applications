
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.util;



import java.util.*;


/**
 * Class MyQueue
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MyQueue extends LinkedList {

    /**
     * create an empty queue
     */
    public MyQueue() {}

    /**
     * get element
     */
    public Object get() {
        return removeFirst();
    }

    /**
     * put an object
     */
    public synchronized void put(Object obj) {
        addLast(obj);
    }

    /**
     * is empty
     */
    public boolean empty() {
        return size() == 0;
    }
}
