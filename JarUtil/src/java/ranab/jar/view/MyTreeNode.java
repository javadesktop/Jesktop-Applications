
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar.view;



import java.text.*;

import java.util.*;
import java.util.zip.*;

import javax.swing.tree.*;


/**
 * Class MyTreeNode
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MyTreeNode extends Vector {

    private String mstAbsPath;
    private String mstName;
    private MyTreeNode mParent;
    private ZipEntry mEntry;

    /**
     * constructor
     */
    public MyTreeNode(MyTreeNode parent, String fileName, ZipEntry entry) {

        mstName = fileName;
        mParent = parent;
        mEntry = entry;

        if (parent == null) {
            mstAbsPath = mstName;
        } else {
            mstAbsPath = parent.getAbsolutePath() + '/' + mstName;

            parent.add(this);
        }
    }

    /**
     * get node
     */
    public MyTreeNode getNode(String nodeAbsName) {

        int index = indexOf(nodeAbsName);

        if (index == -1) {
            return null;
        }

        return (MyTreeNode) get(index);
    }

    /**
     * index of a particular string node
     */
    public int indexOf(String nodeAbsName) {

        for (int i = 0; i < size(); i++) {
            MyTreeNode node = (MyTreeNode) get(i);

            if (node.mstAbsPath.equals(nodeAbsName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Method indexOf
     *
     *
     * @param cnode
     *
     * @return
     *
     */
    public int indexOf(MyTreeNode cnode) {

        for (int i = 0; i < size(); i++) {
            MyTreeNode node = (MyTreeNode) get(i);

            if (node.mstAbsPath.equals(cnode.mstAbsPath)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * clear all nodes
     */
    public void clear() {

        Iterator listIt = listIterator(0);

        while (listIt.hasNext()) {
            MyTreeNode nd = (MyTreeNode) listIt.next();

            nd.clear();
        }

        super.clear();
    }

    /**
     * get parent
     */
    public MyTreeNode getParent() {
        return mParent;
    }

    /**
     * is a folder?
     */
    public boolean isLeaf() {

        if (mEntry == null) {
            return false;
        }

        return (size() == 0) && (!mEntry.isDirectory());
    }

    /**
     * get entry - if not a leaf returns null
     */
    public ZipEntry getEntry() {

        if (isLeaf()) {
            return mEntry;
        }

        return null;
    }

    /**
     * get absolute path
     */
    public String getAbsolutePath() {
        return mstAbsPath;
    }

    /**
     * Check equality
     */
    public boolean equals(Object obj) {

        // check object
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ranab.jar.view.MyTreeNode)) {
            return false;
        }

        MyTreeNode nd = (MyTreeNode) obj;

        return mstAbsPath.equals(nd.mstAbsPath);
    }

    /**
     * get the directory name
     */
    public String toString() {
        return mstName;
    }
}
