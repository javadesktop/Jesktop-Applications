
/**
 * Title:        JFileManager<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins<p>
 * Company:      <p>
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */
package jfilemanager;

import java.util.Vector;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JButton;

/**
 * In einem eigenen Thread wird nach Dateien gesucht. Dies hat u.a. zum Vorteil,
 * dass das Userinterface zwischendurch benutzbar bleibt.
 *
 * @see FileSearchDialog
 * @author Thorsten Jens
 * @version $Id: FileSearchThread.java,v 1.1.1.1 2002-01-11 08:48:55 paul-h Exp $
 *
 */
public class FileSearchThread extends Thread {

  Vector result;
  JList list;
  String searchString;
  Component parent;
  File startDir;
  JLabel statusBar;
  LanguageData ld;
  JButton jb;
  private boolean keepRunning;
  private int recdepth = 0;

  /** Die maximale Verzeichnis-Rekursions-Tiefe (=40) */
  public final static int MAX_DEPTH = 40;

	public final static int THREAD_STOP_MAGIC = 0xdeadbeef;


  /**
   * Der Konstruktor erhält einige Variablen, die zur Suche und zur Ausgabe der
   * Meldungen gebraucht werden.
   *
   * @param p Parent-Component (für JOptionPane)
   * @param l JList zur Ausgabe des Suchergebnisses
   * @param r Vector, der das Ergebnis enthält
   * @param s Suchstring aus dem JTextField von {@link FileSearchDialog}
   * @param sD Startverzeichnis, unterhalb <code>sD</code> wird gesucht.
   * @param sB JLabel, zur Statusanzeige
   * @param ld LanguageData, für Meldungstexte
   * @param jb JButton, der nach der Beendigung wieder enabled wird
   *
   */
  public FileSearchThread(Component p, JList l, Vector r, String s, File sD,
    JLabel sB, LanguageData ld, JButton jb) {
    result = r;
    list = l;
    searchString = s;
    parent = p;
    startDir = sD;
    this.ld = ld;
    statusBar = sB;
    this.jb = jb;
    this.setName("FileSearchThread for " + s + " in " + startDir.getAbsolutePath());
    keepRunning = false;
  }


  /**
   * Diese Klasse startet den Thread. Mit den im Konstruktor übergebenen Variablen
   * füttert sie private Klassen, die nach den Dateien suchen.
   *
   *
   */
  public void run() {
    result = findFileMain(startDir, searchString, result);
   	if (list != null && result != null && result.size() != 0) {
    	  list.setListData(result);
       	statusBar.setText(result.size() + " " + ld.get(ld.L_FILES_FOUND));
   	} else {
     	  statusBar.setText(ld.get(ld.L_ERROR));
   	}
    jb.setEnabled(true);
  }

    private Vector findFileMain(File startDir, String searchString, Vector result) {
        searchString = searchString.toLowerCase().trim();

        if (searchString.startsWith("*") && searchString.endsWith("*")) {
          	if (searchString.equals("*")) {
             	result = findAllFiles(startDir, result, recdepth);
           	} else {
            	searchString = searchString.substring(1, searchString.length() - 1);
            	result = findFileAny(startDir, searchString, result, recdepth);
            }
        } else if (searchString.startsWith("*")) {
            searchString = searchString.substring(1, searchString.length());
            result =  findFileRest(startDir, searchString, result, recdepth);
        } else if (searchString.endsWith("*")) {
            searchString = searchString.substring(0, searchString.length() - 1);
            result = findFileStart(startDir, searchString, result, recdepth);
        } else {
            result =  findFileExact(startDir, searchString, result, recdepth);
        }
        return result;
    }


    Vector findAllFiles(File startDir, Vector retVal, int recdepth) {
      	File[] allFiles = startDir.listFiles();
        statusBar.setText(startDir.getPath());
        Thread.yield();

       	recdepth++;
//        printUpdate(startDir, recdepth);
        if (recdepth < MAX_DEPTH && retVal != null) {
					for (int i = 0; i < allFiles.length; i++) {
						if (allFiles[i].isDirectory() && allFiles[i].canRead()) {
							retVal = findAllFiles(allFiles[i], retVal, recdepth);
       			} else {
            	retVal.addElement(allFiles[i].getPath());
            }
       		}
        } else {
          if (retVal != null)
          	terminateThread();
           	return null;
        }
        return retVal;
    }


    Vector findFileExact(File startDir, String str, Vector retVal, int recdepth) {
        File[] allFiles = startDir.listFiles();
        statusBar.setText(startDir.getPath());
        Thread.yield();

        recdepth++;
//        printUpdate(startDir, recdepth);
        if (recdepth < MAX_DEPTH && retVal != null) {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isDirectory() && allFiles[i].canRead()) {
                    retVal = findFileExact(allFiles[i], str, retVal, recdepth);
                } else {
                    if (str.equalsIgnoreCase(allFiles[i].getName())) {
                        retVal.addElement(allFiles[i].getPath());
                    }
                }
            }
        } else {
            if (retVal != null)
                terminateThread();
            return null;
        }
        return retVal;
    }


    private Vector findFileRest(File startDir, String rest, Vector retVal, int recdepth) {
        File[] allFiles = startDir.listFiles();
        statusBar.setText(startDir.getPath());
        Thread.yield();

        recdepth++;
//        printUpdate(startDir, recdepth);
        if (recdepth < MAX_DEPTH && retVal != null) {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isDirectory() && allFiles[i].canRead()) {
                    retVal = findFileRest(allFiles[i], rest, retVal, recdepth);
                } else {
                    if (allFiles[i].getName().toLowerCase().endsWith(rest)) {
                        retVal.addElement(allFiles[i].getPath());
                    }
                }
            }
        } else {
            if (retVal != null)
                terminateThread();
            return null;
        }
        return retVal;
    }


    private Vector findFileAny(File startDir, String str, Vector retVal, int recdepth) {
        File[] allFiles = startDir.listFiles();
        statusBar.setText(startDir.getPath());
        Thread.yield();

        recdepth++;
//        printUpdate(startDir, recdepth);
        if (recdepth < MAX_DEPTH && retVal != null) {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isDirectory() && allFiles[i].canRead()) {
                    retVal = findFileAny(allFiles[i], str, retVal, recdepth);
                } else {
                    if (allFiles[i].getName().toLowerCase().indexOf(str) >= 0) {
                        retVal.addElement(allFiles[i].getPath());
                    }
                }
            }
        } else {
            if (retVal != null)
                terminateThread();
            return null;
        }
        return retVal;
    }


    private Vector findFileStart(File startDir, String str, Vector retVal, int recdepth) {
        File[] allFiles = startDir.listFiles();
        statusBar.setText(startDir.getPath());
        Thread.yield();

        recdepth++;
//        printUpdate(startDir, recdepth);
        if (recdepth < MAX_DEPTH && retVal != null) {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isDirectory() && allFiles[i].canRead()) {
                    retVal = findFileStart(allFiles[i], str, retVal, recdepth);
                } else {
                    if (allFiles[i].getName().toLowerCase().startsWith(str)) {
                        retVal.addElement(allFiles[i].getPath());
                    }
                }
            }
        } else {
            if (retVal != null)
                terminateThread();
            return null;
        }
        return retVal;
    }


    public void pleaseStop() {
			recdepth = THREAD_STOP_MAGIC;
   		result = null;
    }


    private void terminateThread() {
      switch(recdepth) {
				case THREAD_STOP_MAGIC: break;
        default: JOptionPane.showMessageDialog(parent,
                                      "The maximum directory depth has been reached."
                                      + System.getProperty("line.separator")
                                      + "There is probably a symlink loop in your directory tree."
                                      + System.getProperty("line.separator")
                                      + "Not all matching files could be found.",
                                      "ERROR",
                                      JOptionPane.ERROR_MESSAGE);
                  break;
			}
    }



}
