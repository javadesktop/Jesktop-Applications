
/**
 * Title:        JFileManager<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins<p>
 * Company:      <p>
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */
package jfilemanager;

import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import jfilemanager.*;
import java.util.*;

/**
 * Diese Klasse implementiert das Kopieren von beliebig vielen Dateien.
 * Die Dateien und das Zielverzeichnis werden über den Konstruktor übergeben.
 * Eine nachträgliche Veränderung des Zielverzeichnisses ist möglich.
 *
 * Das Kopieren von Verzeichnissen ist in dieser Version noch nicht enthalten!
 *
 * @see FileOperationsWindow
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: CopyFileWindow.java,v 1.1.1.1 2002-01-11 08:48:51 paul-h Exp $ 
 */

public class CopyFileWindow extends FileOperationsWindow {
    private Vector files;
    private FileTable destinationFileTable;
    private String destinationPath;
    private File destinationFile;
    private int ueberschreiben=0;
    private int fileCount=0;
    private LanguageData ld;

    public CopyFileWindow(Vector selectedFiles, String destinationPath, FileTable destinationFileTable, LanguageData ld) {
        super(ld.get(ld.L_COPY_DIALOG));
        this.ld=ld;
        if(OK) {
            files = new Vector();
            files = selectedFiles;

            this.destinationFileTable=destinationFileTable;
            this.destinationPath=destinationPath;
            copy.setText(" "+ld.get(ld.L_COPY_DIALOG_COPY)+" : ");
            copyTo.setText(" "+ld.get(ld.L_COPY_DIALOG_COPYTO)+" : ");

            if(files.size()==1) {
                copyFile.setText(((java.io.File)files.elementAt(0)).getName());
            } else {
                copyFile.setText("*");
            }

            copyFileTo.setText(destinationPath);

            cancelButton.setText(ld.get(ld.L_CANCEL));
            okButton.addActionListener(new java.awt.event.ActionListener() {

                                           public void actionPerformed(ActionEvent e) {
                                               copyFile();
                                           }
                                       }
                                      );
        }
    }

    /**
     * Kopieren der File-Objekte im Vector <code>files</code)
     */

    public void copyFile() {
        for(int i=0;i<files.size();i++) {

            if(copyFileTo.getText().endsWith(System.getProperty("file.separator"))) {
                destinationFile = new File(copyFileTo.getText()+((java.io.File)files.elementAt(i)).getName());
            } else {
                destinationFile = new File(copyFileTo.getText()+System.getProperty("file.separator")+((java.io.File)files.elementAt(i)).getName());
            }

            try {
                if(destinationFile.createNewFile()) {
                    ueberschreiben=0;
                } else {
                    ueberschreiben=JOptionPane.showConfirmDialog(mainPanel, ld.get(ld.L_FILE2) +" "+ destinationFile.getPath() + " "+ld.get(ld.L_FILE_EXISTS_OVERWRITE),ld.get(ld.L_FILE_EXISTS),JOptionPane.YES_NO_OPTION);
                }
            } catch(IOException io) {}



            if(ueberschreiben==0) {
                try {
                    if(destinationFile.canWrite()){
                      int filesize=(int)((java.io.File)files.elementAt(i)).length();
                      byte[] data = new byte[filesize];
                      DataInputStream in = new DataInputStream(new FileInputStream((java.io.File)files.elementAt(i)));
                      in.readFully(data);
                      in.close();
                      DataOutputStream out = new DataOutputStream(new FileOutputStream(destinationFile));
                      out.write(data);
                      out.close();
                      progressList.append(((java.io.File)files.elementAt(i)).getName()+"........."+ld.get(ld.L_DONE));
                      fileCount++;
                    }else{
                      JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILE2)+" "+((java.io.File)files.elementAt(i)).getName()+" "+ld.get(ld.L_COPY_ERROR)+" "+ld.get(ld.L_NO_WRITE_PERMISSION)+".",ld.get(ld.L_ERROR),JOptionPane.ERROR_MESSAGE);
                      }
                } catch(IOException io) {
                    JOptionPane.showMessageDialog(mainPanel, " "+ld.get(ld.L_FILE2)+" "+((java.io.File)files.elementAt(i)).getName()+" "+ld.get(ld.L_COPY_ERROR), ld.get(ld.L_ERROR),JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        // FileTable neu erstellen
        destinationFileTable.Model.changeFileTable(destinationFile.getParentFile());

        if(fileCount > 0) {
            JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILES_COPIED), ld.get(ld.L_DONE),JOptionPane.INFORMATION_MESSAGE );
        }
        super.closeWin();
        this.dispose();
    }

    public void changeLanguage(LanguageData l) {
       copy.setText(" "+ld.get(ld.L_COPY_DIALOG_COPY)+" : ");
       copyTo.setText(" "+ld.get(ld.L_COPY_DIALOG_COPYTO)+" : ");
       cancelButton.setText(ld.get(ld.L_CANCEL));
    }


}
