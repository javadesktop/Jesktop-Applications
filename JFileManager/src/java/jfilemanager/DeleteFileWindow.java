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
 * Diese Klasse implementiert das Löschen von beliebig vielen Dateien.
 * Die Dateien werden über den Konstruktor übergeben.
 *
 * Das Löschen von Verzeichnissen ist in dieser Version noch nicht enthalten!
 *
 * @see FileOperationsWindow
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: DeleteFileWindow.java,v 1.1.1.1 2002-01-11 08:48:52 paul-h Exp $ 
 */


public class DeleteFileWindow extends FileOperationsWindow {
    private Vector files;
    private FileTable sourceFileTable;
    private LanguageData ld;
    private int fileCount=0;

    public DeleteFileWindow(Vector selectedFiles, FileTable sourceFileTable, LanguageData ld) {
        super(ld.get(ld.L_DELETE_DIALOG));
        this.ld=ld;
        if(OK) {
            files = new Vector();
            files = selectedFiles;
            this.sourceFileTable = sourceFileTable;

            copy.setText(" "+ld.get(ld.L_DELETE));

            contentFieldPanel.removeAll();
            contentLabelPanel.remove(copyTo);
            contentPanel.remove(contentFieldPanel);

            for(int i=0; i<files.size();i++) {
                progressList.append(((java.io.File)files.elementAt(i)).getName()+"\n");
            }

            cancelButton.setText(ld.get(ld.L_CANCEL));
            okButton.addActionListener(new java.awt.event.ActionListener() {

                                           public void actionPerformed(ActionEvent e) {
                                               deleteFile();
                                           }
                                       }
                                      );
        }
    }

    /**
     * Löschen der File-Objekte im Vector <code>files</code>
     */

    public void deleteFile() {
        for(int i=0;i<files.size();i++) {
          if(((java.io.File)files.elementAt(i)).canWrite()){
            try {
              ((java.io.File)files.elementAt(i)).delete();
              fileCount++;
            } catch(Exception e) {
                JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILE2)+" "+((java.io.File)files.elementAt(i)).getPath()+" "+ld.get(ld.L_DELETE_ERROR), ld.get(ld.L_ERROR),JOptionPane.ERROR_MESSAGE);
            }
          }else{
             JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILE2)+" "+((java.io.File)files.elementAt(i)).getName()+" "+ld.get(ld.L_DELETE_ERROR)+" "+ld.get(ld.L_NO_WRITE_PERMISSION)+".",ld.get(ld.L_ERROR),JOptionPane.ERROR_MESSAGE);
              }
        }
        //FileTable neu erstellen
        sourceFileTable.Model.changeFileTable(((java.io.File)files.elementAt(0)).getParentFile());
        if(fileCount>0){
          JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILES_DELETED), ld.get(ld.L_DONE), JOptionPane.INFORMATION_MESSAGE);
          }
        super.closeWin();
        this.dispose();
    }

    public void changeLanguage(LanguageData l) {
      //super(ld.get(ld.L_DELETE_DIALOG));
      copy.setText(" "+ld.get(ld.L_DELETE));
      cancelButton.setText(ld.get(ld.L_CANCEL));
    }


  }

