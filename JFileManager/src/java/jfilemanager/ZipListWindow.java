
/**
 * Title:        JFileManager<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins<p>
 * Company:      <p>
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */
package jfilemanager;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.util.zip.*;
import java.util.jar.*;
import java.io.*;
import java.awt.BorderLayout;

/**
 * Ein Fenster, das die Einträge einer <b>.zip</b>- oder <b>.jar</b>-Datei als JTable anzeigt.
 * @author Thorsten Jens
 * @version $Id: ZipListWindow.java,v 1.1.1.1 2002-01-11 08:49:04 paul-h Exp $
 */
public class ZipListWindow extends FileContentWindow {

    private JTable table;
    private JScrollPane scrollPane;
    private Vector columnNames;


    public ZipListWindow(File f) {
        super("ZIP+JARViewer: " + f.toString());
        if (OK) {
            columnNames = new Vector(3);
            LanguageData ld = new LanguageData();
            columnNames.add(ld.get(LanguageData.L_NAME));
            columnNames.add(ld.get(LanguageData.L_SIZE));
            table = new JTable(populateListZIP(f), columnNames);
            statusBar.setText(table.getRowCount() + " " + ld.get(LanguageData.L_ENTRIES));
            scrollPane = new JScrollPane(table);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            this.setSize(scrollPane.getPreferredSize());
            this.setVisible(true);
        }
    }


    private Vector populateListZIP(File f) {
        Vector ret = new Vector();
        try {
            ZipFile zf = new ZipFile(f);
            for (java.util.Enumeration e = zf.entries(); e.hasMoreElements();) {
                ZipEntry ze = (ZipEntry)e.nextElement();
                Vector tmp = new Vector();
                tmp.add(ze.getName());
                tmp.add(new Long(ze.getSize()));
                ret.add(tmp);
            }
        } catch(IOException ioe) {
            System.err.println("Brarps!");
            return null;
        }
        return ret;
    }


    private Vector populateListJAR(File f) {
        Vector ret = new Vector();
        try {
            ZipFile zf = new JarFile(f);
            for (java.util.Enumeration e = zf.entries(); e.hasMoreElements();) {
                ZipEntry ze = (ZipEntry)e.nextElement();
                Vector tmp = new Vector();
                tmp.add(ze.getName());
                tmp.add(new Long(ze.getSize()));
                ret.add(tmp);
            }
        } catch(IOException ioe) {
            System.err.println("Brarps!");
            return null;
        }
        return ret;
    }

}
