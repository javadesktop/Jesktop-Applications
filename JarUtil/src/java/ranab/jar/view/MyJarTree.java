
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar.view;



import java.util.*;
import java.util.zip.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import ranab.jar.*;

import ranab.gui.MyGuiUtil;


/**
 * Class MyJarTree
 *
 *
 * @author Rana Bhattacharyya <rana_b@yahoo.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MyJarTree implements TreeModel, MyJarObserver, MyJarUI {

    private MyTreeNode mRoot;
    private TreeModelEvent mRootEvent;
    private Vector mListenrList;
    private Component mParent;
    private JTree mjTree;
    private JScrollPane mjScrollPane;
    private JPanel mjTopPane;
    private boolean mbIsActive;

    /**
     * Constructor MyJarTree
     *
     *
     * @param comp
     *
     */
    public MyJarTree(Component comp) {

        mListenrList = new Vector();
        mParent = comp;
        mbIsActive = false;

        initComponents();
    }

    private void initComponents() {

        mRoot = new MyTreeNode(null, ".", null);
        mRootEvent = new TreeModelEvent(this, new TreePath(mRoot));
        mjTree = new JTree(this);

        mjTree.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(mjTree,
                                                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mjTopPane = new JPanel();

        mjTopPane.setLayout(new BorderLayout());
        mjTopPane.add(scrollPane);
    }

    /**
     * initialize tree
     */
    public void start() {
        mRoot.clear();
        structureChanged();
    }

    /**
     * add new entry
     */
    public void setNext(ZipEntry entry) {

        String name = entry.getName();
        StringTokenizer st = new StringTokenizer(name, "/");
        MyTreeNode parent = mRoot;
        String currAbsPath = mRoot.getAbsolutePath();

        while (st.hasMoreTokens()) {
            String tok = st.nextToken();

            currAbsPath += '/' + tok;

            MyTreeNode thisNode = parent.getNode(currAbsPath);

            if (thisNode == null) {
                thisNode = new MyTreeNode(parent, tok, entry);

                structureChanged();
            }

            parent = thisNode;
        }
    }

    /**
     * set count
     */
    public void setCount(int count) {}

    /**
     * jar file processing end
     */
    public void end() {
        structureChanged();
        mjTree.expandPath(mRootEvent.getTreePath());
    }

    /**
 * update component UI
 */
    public void updateLnF() {
        SwingUtilities.updateComponentTreeUI(mjTopPane);
    }

    /**
 * get the scroll pane that contains the tree
 */
    public JPanel getPanel() {
        return mjTopPane;
    }

    /**
     * is active
     */
    public boolean isActive() {
        return mbIsActive;
    }

    /**
     * set enable tag
     */
    public void setActive(boolean b) {
        mbIsActive = b;
    }

    /**
     * get selected entries
     */
    public ZipEntry[] getSelectedEntries() {

        TreePath selectedItems[] = mjTree.getSelectionPaths();

        if ((selectedItems == null) || (selectedItems.length == 0)) {
            return null;
        }

        Vector vec = new Vector();

        for (int i = 0; i < selectedItems.length; i++) {
            MyTreeNode node = (MyTreeNode) selectedItems[i].getLastPathComponent();

            if (node.isLeaf()) {
                ZipEntry entry = node.getEntry();

                if (!vec.contains(entry)) {
                    vec.add(entry);
                }
            } else {
                getLeaves(node, vec);
            }
        }

        if (vec.size() == 0) {
            return null;
        }

        ZipEntry[] entries = new ZipEntry[vec.size()];

        for (int i = 0; i < entries.length; i++) {
            entries[i] = (ZipEntry) vec.get(i);
        }

        return entries;
    }

    /**
     * get children entries
     */
    private void getLeaves(MyTreeNode node, Vector vec) {

        // check size
        if ((node == null) || (node.size() == 0)) {
            return;
        }

        // check zipentry
        for (int i = 0; i < node.size(); i++) {
            MyTreeNode child = (MyTreeNode) node.elementAt(i);

            if (child.isLeaf()) {
                ZipEntry entry = child.getEntry();

                if (!vec.contains(entry)) {
                    vec.add(entry);
                }
            } else {
                getLeaves(child, vec);
            }
        }
    }

    /**
     * send structure change event
     */
    private void structureChanged() {

        int sz = mListenrList.size();

        for (int i = sz - 1; i >= 0; i--) {
            ((TreeModelListener) mListenrList.elementAt(i)).treeStructureChanged(mRootEvent);
        }
    }

    /**
     * set error message
     */
    public void setError(String errMsg) {

        if (isActive()) {
            MyGuiUtil.showErrorMessage(mParent, errMsg);
        }
    }

    /**
 * add a listener
 */
    public void addTreeModelListener(TreeModelListener l) {
        mListenrList.add(l);
    }

    /**
 * remove a listener
 */
    public void removeTreeModelListener(TreeModelListener l) {
        mListenrList.remove(l);
    }

    /**
     * object changed. In our case it is not possible - so
     * igmore it.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {}

    /**
     * get child at
     */
    public Object getChild(Object parent, int index) {
        return ((MyTreeNode) parent).get(index);
    }

    /**
     * get child count
     */
    public int getChildCount(Object parent) {
        return ((MyTreeNode) parent).size();
    }

    /**
     * get root node
     */
    public Object getRoot() {
        return mRoot;
    }

    /**
     * is a leaf
     */
    public boolean isLeaf(Object node) {
        return ((MyTreeNode) node).isLeaf();
    }

    /**
     * get index of child
     */
    public int getIndexOfChild(Object parent, Object child) {
        return ((MyTreeNode) parent).indexOf(child);
    }
}
