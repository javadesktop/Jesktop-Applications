
/*****************************************************************************
 * Copyright (C) Jesktop Organization. All rights reserved.                  *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the jesktop-bsd-license.html file.                                        *
 *****************************************************************************/
package org.jesktop.config;



/**
 * Interface PersistableConfig
 *
 *
 * @author Paul Hammant <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
 * @version * $Revision: 1.1.1.1 $
 */
public interface PersistableConfig {

    /**
     * Method put
     *
     *
     * @param key
     * @param obj
     *
     */
    void put(String key, Object obj);

    /**
     * Method get
     *
     *
     * @param key
     *
     * @return
     *
     */
    Object get(String key);
}
