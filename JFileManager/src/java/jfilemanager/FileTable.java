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
import java.io.File;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.*;
/**
 * Diese Klasse sorgt für die graphische Darstellung der Daten in <code>FileTableModel</code>.
 *
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: FileTable.java,v 1.1.1.1 2002-01-11 08:48:56 paul-h Exp $ 
 */
public class FileTable extends JPanel implements LanguageAware {
    String path;
    JScrollPane fileScrollPane;
    File[] rootdirs;
    FileTableModel Model;
    TableColumn column;
    ListSelectionModel rowSM;
    JTable table;
    DetailView dv;
    int selectedRow;
    Vector selectedFiles;
    LanguageData ld;

    public FileTable(DetailView dv, LanguageData ld) {
        this.dv = dv;
        this.ld = ld;
        this.initializeFileTable();
    }

    /**
     * Die JTable initialisieren.
     * Zunächst wird das Home-Verzeichnis des Benutzers angezeigt .
     */
    public void initializeFileTable() {
        File dir=new File(System.getProperty("user.home"));
        Model=new FileTableModel(dir, this.dv, ld);
        table=new JTable(Model);
        selectedFiles=new Vector();

        table.getColumnModel().getColumn(0).setPreferredWidth(18);
        // 1000 -> Alle anderen werden zusammengestaucht, Dateiname erhält
        // maximale Breite
        table.getColumnModel().getColumn(1).setPreferredWidth(1000);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        fileScrollPane=new JScrollPane(table);
        this.setLayout(new BorderLayout());
        this.add(fileScrollPane,BorderLayout.CENTER);

        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
                                           public void valueChanged(ListSelectionEvent e) {
                                               selectedFiles.removeAllElements();
                                               if(e.getValueIsAdjusting()) {
                                                   return;
                                               }
                                               ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                                               if(lsm.isSelectionEmpty()) {
                                                   return;
                                               } else {
                                                   int min_index=lsm.getMinSelectionIndex();
                                                   int max_index=lsm.getMaxSelectionIndex();
                                                   for (int i=min_index; i<=max_index; i++) {
                                                       if(lsm.isSelectedIndex(i)) {
                                                           if(Model.getSelectedFile(i).isFile())
                                                               selectedFiles.addElement(Model.getSelectedFile(i));
                                                       }
                                                   }
                                               }
                                           }
                                       }
                                      );

        table.addMouseListener(new MouseAdapter() {
                                   public void mouseClicked(MouseEvent e) {
                                       if(e.getClickCount()==2) {
                                           dv.openExternalWindow();
                                           changeDirectory(table.rowAtPoint(e.getPoint()));
                                       } else {
                                           Model.showFileOrDirectoryInfo(table.rowAtPoint(e.getPoint()));
                                           selectedRow=table.rowAtPoint(e.getPoint());
                                       }
                                   }
                               }
                              );

    }

    /**
     * Setzt das Root-Verzeichnis
     * @params root Das neue Rootverzeichnis.
     *
     */
    public void setRootTable(String root) {
        File f = new File(root);
        if (f.canRead() && f.isDirectory()) {
            Model.changeFileTable(f);
        }
    }

    /**
     * @deprecated geändert auf String-Übergabe, wegen Inkompabilität mit Linux
     * @see <code>setRootTable(String root)</code>
     */
    public void setRootTable(int rootIndex) {
        File[] rootdirs=File.listRoots();
        if(rootdirs[rootIndex].canRead()) {
            Model.changeFileTable(rootdirs[rootIndex]);

        }
    }
    /**
     * Liefert den Pfad zum Home-Verzeichnis des aktuellen Benutzers
     * @return Pfad zum Home-Verzeichnis
     */
    public String returnHomePath() {
        String homePath = System.getProperty("user.home");
       // return homePath;
      if(System.getProperty("os.name").equals("Linux") || System.getProperty("os.name").equals("Solaris")) {
          //String homePath = System.getProperty("user.home");
          return homePath;
       }else {
	  return (homePath.substring(0,3));
       }
    }
                        /**
                         * Anzeige des Home-Verzeichnisses
                         */
                        public void setHomeTable() {
                            File homedir = new File(System.getProperty("user.home"));
                            Model.changeFileTable(homedir);
                        }
                        /**
                         * Wechsel in das angebene Verzeichnis
                         * @param Zeile
                         */
                        public void changeDirectory(int row) {
                            Model.changeDirectoryFileTable(row);
                        }

                        /**
                         * Liefert den aktuellen Pfad.
                         *
                         * @return der aktuelle Pfad
                         */
                        public String getActualPath() {
                            return Model.dir.getPath();
                        }

                        /**
                         * Liefert alle markierten Dateien
                         * @return markierte Dateien
                         */
                        public Vector getSelectedFiles() {
                            return selectedFiles;
                        }

                        /**
                         * Ein Verzeichnis zurück
                         */
                        public void setParentDirectoryTable() {
                            if(Model.dir.getParentFile()!= null)
                                Model.changeFileTable(Model.dir.getParentFile());
                        }

                        public void showFileContent() {
                            if((Model.getSelectedFile(selectedRow).canRead()) && (Model.getSelectedFile(selectedRow).isFile())) {
                                //      FileContentWindow showFile = new FileContentWindow(Model.getSelectedFile(selectedRow));
                                //						dv.openExternalWindow();
                            }

                    }
    public void changeLanguage(LanguageData l) {
      Model.changeLanguage(l);
    }
              }
