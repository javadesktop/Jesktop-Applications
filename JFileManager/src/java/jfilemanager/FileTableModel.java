/**
 * Title:        JFileManager<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins<p>
 * Company:      <p>
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */
package jfilemanager;

import javax.swing.table.*;
import javax.swing.ImageIcon;
import java.io.*;
import java.util.Vector;

/**
 * Diese Klasse verwaltet die Daten, die in der FileTable dargestellt werden.
 *
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: FileTableModel.java,v 1.1.1.1 2002-01-11 08:48:56 paul-h Exp $ 
 */

public class FileTableModel extends AbstractTableModel implements LanguageAware {
    protected File dir;
    protected String[] filenames;
    protected String[] columnNames;
    protected Class[] columnClasses=new Class[]{ImageIcon.class, String.class, Long.class};

    DetailView dv;

    /**
    * Der Konstruktor erhält als Parameter das Home-Verzeichnis des Benutzers.
    *
    */
    public FileTableModel(File dir, DetailView dv, LanguageData ld) {
        this.dir=dir;
//        LanguageData ld = new LanguageData();
        this.columnNames = new String[]{"", ld.get(LanguageData.L_NAME), ld.get(LanguageData.L_SIZE)};
        this.filenames=dir.list();
        this.dv = dv;
        this.fireTableDataChanged();
    }

    /**
     * Ein anderes Verzeichnis anzeigen
     */
    public void changeFileTable(File dir) {
        this.dir=dir;
        if(dir.exists()) {
            this.filenames=dir.list();
            this.fireTableDataChanged();
        }
    }

    /**
     * Liefert die ausgewählte Datei
     * @return die ausgewählte Datei
     */
    public File getSelectedFile(int row) {
        File f = new File(dir,filenames[row]);
        return f;
    }

    /**
     * Anzeigen der Dateiinformationen
     */
    public void showFileOrDirectoryInfo(int row) {
        File f = new File(dir,filenames[row]);
        dv.showInfo(f);
    }

    /**
     * In das, durch Doppelklick, angewählte Verzeichnis wechseln
     */
    public void changeDirectoryFileTable(int row) {
        File f= new File(dir,filenames[row]);
        if(f.exists() && f.isDirectory() && f.canRead()) {
            this.dir=f;
            this.filenames=dir.list();
        }
        this.fireTableDataChanged();
    }

    /**
     * Liefert die Spaltenanzahl
     * @return Spaltenanzahl
     */
    public int getColumnCount() {
        return 3;
    }
    /**
     * Liefert die Zeilenanzahl
     * @return Zeilenanzahl
     */
    public int getRowCount() {
        return filenames.length;
    }
    /**
     * Liefert die Spaltenüberschrift
     * @return Spaltenüberschrift
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int col) {
        return columnClasses[col];
    }
    /**
     * Liefert die Daten der ausgewählten Zelle
     * @return Zellendaten
     */
    public Object getValueAt(int row, int col) {
        File f=new File(dir, filenames[row]);
        switch(col) {
        case 0:
            return ExtendedFile.getIcon(f);
        case 1:
            return filenames[row];
        case 2:
            return new Long(f.length());
        default:
            return null;
        }
    }


  	public void changeLanguage(LanguageData l) {
			columnNames[1] = l.get(l.L_NAME);
   		columnNames[2] = l.get(l.L_SIZE);
      this.fireTableStructureChanged();
   	}

}
