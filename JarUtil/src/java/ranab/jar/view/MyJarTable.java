
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar.view;



import java.util.Vector;
import java.util.Date;
import java.util.Collections;
import java.util.zip.ZipEntry;

import java.text.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import ranab.gui.MyGuiUtil;

import ranab.jar.*;


/**
 * This is the table model to display Jar file.
 */
public class MyJarTable extends AbstractTableModel implements MyJarObserver, MyJarUI {

    private final static SimpleDateFormat mFormatter = new SimpleDateFormat("dd,MMM,yy HH:mm");
    public final static MyZipComparator[] mHeader = { new MyZipNameComparator(),
                                                      new MyZipSizeComparator(),
                                                      new MyZipCompressedSizeComparator(),
                                                      new MyZipModificationTimeComparator(),
                                                      new MyZipPathComparator() };
    private Vector mTabelData = null;
    private Component mParent;
    private JTable mjTable;
    private JPanel mjTopPane;
    private JComboBox mjAscCombo;
    private JComboBox mjDesCombo;
    private boolean mbIsActive;

    /**
     * initialize the table model
     */
    public MyJarTable(Component parent) {

        mTabelData = new Vector();
        mParent = parent;
        mbIsActive = false;

        initComponents();
    }

    /**
     * init UI components
     */
    private void initComponents() {

        mjTable = new JTable(this);

        mjTable.setColumnSelectionAllowed(false);
        mjTable.setBackground(Color.white);

        JScrollPane scrollPane = new JScrollPane(mjTable,
                                                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mjTopPane = new JPanel();

        mjTopPane.setLayout(new BorderLayout());
        mjTopPane.add(scrollPane, BorderLayout.CENTER);
        mjTopPane.add(getComboPanel(), BorderLayout.NORTH);
    }

    /**
     * create sort combo panel
     */
    private JPanel getComboPanel() {

        JPanel ascPanel = new JPanel();
        JLabel ascLabel = new JLabel("Ascending Sorting");

        ascLabel.setForeground(Color.black);
        ascLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ascLabel.setFont(new Font("Serif", 1, 14));

        mjAscCombo = new JComboBox(MyJarTable.mHeader);

        mjAscCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                MyZipComparator comp = (MyZipComparator) mjAscCombo.getSelectedItem();

                sortColumn(comp, true);
            }
        });
        ascPanel.add(ascLabel);
        ascPanel.add(mjAscCombo);

        JPanel desPanel = new JPanel();
        JLabel desLabel = new JLabel("Descending Sorting");

        desLabel.setForeground(Color.black);
        desLabel.setHorizontalAlignment(SwingConstants.CENTER);
        desLabel.setFont(new Font("Serif", 1, 14));

        mjDesCombo = new JComboBox(MyJarTable.mHeader);

        mjDesCombo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                MyZipComparator comp = (MyZipComparator) mjDesCombo.getSelectedItem();

                sortColumn(comp, false);
            }
        });
        desPanel.add(desLabel);
        desPanel.add(mjDesCombo);

        JPanel topPane = new JPanel();

        topPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPane.add(ascPanel);
        topPane.add(desPanel);

        return topPane;
    }

    // TableModel implementation

    /**
     * get the number of columns
     */
    public int getColumnCount() {
        return mHeader.length;
    }

    /**
     * get column name
     */
    public String getColumnName(int index) {
        return mHeader[index].getHeaderName();
    }

    /**
     * get column class
     */
    public Class getColumnClass(int columnIndex) {

        switch (columnIndex) {

        case 0 :
            return String.class;

        case 1 :
            return Long.class;

        case 2 :
            return Long.class;

        case 3 :
            return String.class;

        case 4 :
            return String.class;

        default :
            return String.class;
        }
    }

    /**
     * cell is not editable in this table
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * get the number of rows
     */
    public int getRowCount() {
        return mTabelData.size();
    }

    /**
     * get cell value
     */
    public Object getValueAt(int rowIndex, int columnIndex) {

        ZipEntry ze = (ZipEntry) mTabelData.elementAt(rowIndex);

        switch (columnIndex) {

        case 0 :
            return MyZipNameComparator.getName(ze);

        case 1 :
            return new Long(ze.getSize());

        case 2 :
            return new Long(ze.getCompressedSize());

        case 3 :
            return mFormatter.format(new Date(ze.getTime()));

        case 4 :
            return MyZipPathComparator.getPath(ze);

        default :
            return null;
        }
    }

    // MyJarObserver implementation

    /**
     * start viewing - clear the old data
     */
    public void start() {

        int sz = mTabelData.size();

        if (sz != 0) {
            mTabelData.clear();
            fireTableRowsDeleted(0, sz - 1);
        }
    }

    /**
     * set count - ignore
     */
    public void setCount(int count) {}

    /**
     * next entry found
     */
    public void setNext(ZipEntry je) {

        int row = mTabelData.size();

        if (!je.isDirectory()) {
            mTabelData.add(je);
            fireTableRowsInserted(row, row);
        }
    }

    /**
     * error messgae
     */
    public void setError(String errMsg) {

        if (isActive()) {
            MyGuiUtil.showErrorMessage(mParent, errMsg);
        }
    }

    /**
     * end viewing - ignore
     */
    public void end() {}

    /**
 * update component UI
 */
    public void updateLnF() {
        SwingUtilities.updateComponentTreeUI(mjTopPane);
    }

    /**
     * get the scroll pane that contains the table
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
     * returns the selected zip entries
     */
    public ZipEntry[] getSelectedEntries() {

        int indices[] = mjTable.getSelectedRows();

        if (indices.length == 0) {
            return null;
        }

        ZipEntry entries[] = new ZipEntry[indices.length];

        for (int i = 0; i < entries.length; i++) {
            entries[i] = (ZipEntry) mTabelData.elementAt(indices[i]);
        }

        return entries;
    }

    /**
     * sort column
     */
    public void sortColumn(MyZipComparator comp, boolean asc) {

        if (comp == null) {
            return;
        }

        MyZipComparator finalComp = null;

        if (asc) {
            finalComp = comp;
        } else {
            finalComp = new MyZipReverseComparator(comp);
        }

        Collections.sort(mTabelData, finalComp);
        fireTableDataChanged();
    }
}
