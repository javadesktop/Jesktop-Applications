
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar.view;



import java.awt.Component;

import java.util.zip.ZipEntry;

import javax.swing.JPanel;


/**
 * Interface MyJarUI
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version * $Revision: 1.1.1.1 $
 */
public interface MyJarUI {

    /**
     * Method updateLnF
     *
     *
     */
    public void updateLnF();

    /**
     * Method getPanel
     *
     *
     * @return
     *
     */
    public JPanel getPanel();

    /**
     * Method getSelectedEntries
     *
     *
     * @return
     *
     */
    public ZipEntry[] getSelectedEntries();

    /**
     * Method isActive
     *
     *
     * @return
     *
     */
    public boolean isActive();

    /**
     * Method setActive
     *
     *
     * @param b
     *
     */
    public void setActive(boolean b);
}
