
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.util;



/**
 * Class MyPropertiesException
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MyPropertiesException extends ranab.MyException {

    /**
     * Constructor MyPropertiesException
     *
     *
     */
    public MyPropertiesException() {
        super();
    }

    /**
     * Constructor MyPropertiesException
     *
     *
     * @param msg
     *
     */
    public MyPropertiesException(String msg) {
        super(msg);
    }

    /**
     * Constructor MyPropertiesException
     *
     *
     * @param ex
     *
     */
    public MyPropertiesException(Exception ex) {
        super(ex);
    }
}
