
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import javax.swing.JButton;
import javax.swing.ImageIcon;

import ranab.gui.MyGuiUtil;


/**
 * This button component will be used as table header.
 * It has three states - ascending, descending, non-sorted.
 */
public class MyJarHeaderButton extends JButton {

    // button states
    public final static int ASCENDING = 0;
    public final static int DESCENDING = 1;
    public final static int NON_SORTED = 2;

    // image icons
    private final static ImageIcon TOP_IMG = MyGuiUtil.createImageIcon("images/top.gif");
    private final static ImageIcon BOTTOM_IMG = MyGuiUtil.createImageIcon("images/bottom.gif");
    private final static ImageIcon TRANS_IMG = MyGuiUtil.createImageIcon("images/trans.gif");
    private int miButtonState = NON_SORTED;
    private MyZipComparator mZipComparator = null;

    /**
     * instantiate button
     */
    public MyJarHeaderButton(MyZipComparator comp) {

        //super(comp.getHeaderName(), TRANS_IMG);
        super(comp.getHeaderName());

        setVerticalTextPosition(CENTER);
        setHorizontalTextPosition(LEFT);

        mZipComparator = comp;
    }

    /**
     * Constructor MyJarHeaderButton
     *
     *
     * @param comp
     * @param state
     *
     */
    public MyJarHeaderButton(MyZipComparator comp, int state) {

        //super(comp.getHeaderName(), TRANS_IMG);
        super(comp.getHeaderName());

        setState(state);
        setVerticalTextPosition(CENTER);
        setHorizontalTextPosition(LEFT);

        mZipComparator = comp;
    }

    /**
     * set state
     */
    public void setState(int state) {

        if ((state < ASCENDING) || (state > NON_SORTED)) {
            throw new IllegalArgumentException("Not a valid state : " + state);
        }

        if (miButtonState == state) {
            return;
        }

        miButtonState = state;

        switch (state) {

        case ASCENDING :

            //setIcon(TOP_IMG);
            break;

        case DESCENDING :

            //setIcon(BOTTOM_IMG);
            break;

        case NON_SORTED :

            //setIcon(TRANS_IMG);
            break;
        }
    }

    /**
     * next state
     */
    public void nextState() {
        setState(getNextState(miButtonState));
    }

    /**
     * get state
     */
    public int getState() {
        return miButtonState;
    }

    /**
     * get comparator
     */
    public MyZipComparator getComparator() {
        return mZipComparator;
    }

    /**
     * string representation
     */
    public String toString() {
        return mZipComparator.getHeaderName();
    }

    /**
     * get next state
     */
    public static int getNextState(int state) {

        switch (state) {

        case ASCENDING :
            return DESCENDING;

        case DESCENDING :
            return ASCENDING;

        case NON_SORTED :
            return ASCENDING;

        default :
            return NON_SORTED;
        }
    }
}
