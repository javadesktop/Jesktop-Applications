/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package net.jesktop.apps.decorators.oyoaha;



import java.io.*;


/**
 *
 * @author <a href="mailto:Paul_Hammant@yahoo.com">Paul Hammant</a> May 2001.
 * @version V1.0
 */
public class Config implements Serializable {
    String oyoahaConfigFile = "";
    
    public boolean equals( Object o ) {
	    if (o instanceof Config) {
			return ((Config) o).oyoahaConfigFile.equals(oyoahaConfigFile);
		}
		return false;
    }
}
