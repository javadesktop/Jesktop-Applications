
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
 * Interface MyJarObserver
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version * $Revision: 1.1.1.1 $
 */
public interface MyJarObserver {

    /**
     * Method start
     *
     *
     */
    public void start();

    /**
     * Method setCount
     *
     *
     * @param count
     *
     */
    public void setCount(int count);

    /**
     * Method setNext
     *
     *
     * @param je
     *
     */
    public void setNext(ZipEntry je);

    /**
     * Method setError
     *
     *
     * @param errMsg
     *
     */
    public void setError(String errMsg);

    /**
     * Method end
     *
     *
     */
    public void end();
}
