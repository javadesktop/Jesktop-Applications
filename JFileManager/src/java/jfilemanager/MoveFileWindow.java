package jfilemanager;

/**
 * Title:        JFileManager
 * Description:
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins
 * Company:
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */

import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import jfilemanager.*;
import java.util.*;

/**
 * Diese Klasse implementiert das Verschieben von beliebig vielen Dateien und das Umbenennen
 * einer einzelnen Datei.
 * Die Dateien und das Zielverzeichnis werden über den Konstruktor übergeben.
 * Eine nachträgliche Veränderung des Zielverzeichnisses ist möglich.
 *
 * Das Verschieben von Verzeichnissen ist in dieser Version noch nicht enthalten!
 *
 * @see FileOperationsWindow
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: MoveFileWindow.java,v 1.1.1.1 2002-01-11 08:49:03 paul-h Exp $
 */

public class MoveFileWindow extends FileOperationsWindow {
    Vector files;
    File destinationFile;
    FileTable sourceFileTable, destinationFileTable;
    String destinationPath;
    int fileCount=0;
    int sepCount=0;
    int ueberschreiben=0;
    private LanguageData ld;

    public MoveFileWindow(Vector selectedFiles, String destinationPath, FileTable sourceFileTable, FileTable destinationFileTable,LanguageData ld) {
        super(ld.get(ld.L_MOVE_DIALOG));
        this.ld = ld;
        if(OK) {
            files = new Vector();
            files = selectedFiles;

            this.sourceFileTable = sourceFileTable;
            this.destinationFileTable = destinationFileTable;
            this.destinationPath=destinationPath;
            copy.setText(" "+ld.get(ld.L_MOVE_DIALOG_MOVE)+" : ");
            copyTo.setText(" "+ld.get(ld.L_MOVE_DIALOG_MOVETO)+" : ");

            if(files.size()==1) {
                copyFile.setText(((java.io.File)files.elementAt(0)).getName());
            } else {
                copyFile.setText("*");
            }

            copyFileTo.setText(destinationPath);
            cancelButton.setText(ld.get(ld.L_CANCEL));
            okButton.addActionListener(new java.awt.event.ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                               moveFile();
                                           }
                                       }
                                      );
        }
    }

    /**
     * Verschieben der File-Objekte im Vector <code>files</code>.
     * Enthält der Vector nur ein Objekt und ist im <code>TextField copyFileTo</code> ein
     * Dateiname angegeben, wird das File-Objekt umbenannt.
     */

    public void moveFile() {
        for(int i=0;i<files.size();i++) {
            for(int j=0;j<copyFileTo.getText().length();j++) {
                if(copyFileTo.getText().substring(j,j+1).equals(System.getProperty("file.separator"))) {
                    sepCount++;
                }
            }

            if(sepCount==0 && files.size() > 1) {
                JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_RENAME_ERROR), ld.get(ld.L_ERROR),JOptionPane.ERROR_MESSAGE);
                super.closeWin();
                this.dispose();
                break;
            }

            if(sepCount > 0 || files.size() > 1) {
                if(copyFileTo.getText().endsWith(System.getProperty("file.separator"))) {
                    destinationFile = new File(copyFileTo.getText()+((java.io.File)files.elementAt(i)).getName());
                } else {
                    destinationFile = new File(copyFileTo.getText()+System.getProperty("file.separator")+((java.io.File)files.elementAt(i)).getName());
                }
            } else {
                destinationFile = new File(((java.io.File)files.elementAt(i)).getParentFile()+System.getProperty("file.separator")+copyFileTo.getText());
            }

            if(destinationFile.exists()) {
                ueberschreiben=JOptionPane.showConfirmDialog(mainPanel,ld.get(ld.L_FILE2) +" " + destinationFile.getPath() + " "+ld.get(ld.L_FILE_EXISTS_OVERWRITE),ld.get(ld.L_FILE_EXISTS),JOptionPane.YES_NO_OPTION);
                /*if(ueberschreiben==0) {
                    destinationFile.delete();
                }*/
            }


            if(ueberschreiben==0) {
                try {
                    if(((java.io.File)files.elementAt(i)).exists() && ((java.io.File)files.elementAt(i)).isFile() &&((java.io.File)files.elementAt(i)).canRead()) {
                        if(destinationFile.canWrite()){
                          ((java.io.File)files.elementAt(i)).renameTo(destinationFile);
                          progressList.append(((java.io.File)files.elementAt(i)).getName()+"........."+ld.get(ld.L_DONE));
                          fileCount++;
                        }else{
                          JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILE2)+" "+((java.io.File)files.elementAt(i)).getName()+" "+ld.get(ld.L_MOVE_ERROR)+" "+ld.get(ld.L_NO_WRITE_PERMISSION)+".",ld.get(ld.L_ERROR),JOptionPane.ERROR_MESSAGE);
                          }

                    }
                } catch(Exception io) {
                    JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILE2)+" "+((java.io.File)files.elementAt(i)).getName()+" "+ld.get(ld.L_MOVE_ERROR),ld.get(ld.L_ERROR),JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        //FileTables neu erstellen
        sourceFileTable.Model.changeFileTable(((java.io.File)files.elementAt(0)).getParentFile());
        destinationFileTable.Model.changeFileTable(destinationFile.getParentFile());

        if(fileCount > 0) {
            JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILES_MOVED), ld.get(ld.L_DONE),JOptionPane.INFORMATION_MESSAGE);
        }
        super.closeWin();
        this.dispose();
    }
    public void changeLanguage(LanguageData l) {
      copy.setText(" "+ld.get(ld.L_MOVE_DIALOG_MOVE)+" : ");
      copyTo.setText(" "+ld.get(ld.L_MOVE_DIALOG_MOVETO)+" : ");
      cancelButton.setText(ld.get(ld.L_CANCEL));
    }


    }
