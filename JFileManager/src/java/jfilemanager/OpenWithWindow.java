package jfilemanager;

/**
 * Title:        JFileManager
 * Description:
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins
 * Company:
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */
import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.Dimension;
import java.io.*;
import jfilemanager.FileTable;
import javax.swing.border.*;
import java.util.*;
import javax.swing.JFileChooser;

/**
 * Diese Klasse erstellt einen Dialog zum Öffnen der in der FileTable ausgewählten Datei.
 * Die letzte zehn zum Öffnen ausgewählten Programme werden in der Datei progList.dat gespeichert und
 * stehen beim nächsten Programmstart wieder zur Verfügung.
 *
 * @version $Id: OpenWithWindow.java,v 1.1.1.1 2002-01-11 08:49:03 paul-h Exp $
 *
 * @see FileOperationsWindow
 * @author Kai Benjamins
 * @author Thorsten Jens
 */

 public class OpenWithWindow extends FileOperationsWindow{
    private final static File baseDir = new File(System.getProperty("user.home") +
                                        System.getProperty("file.separator") +
                                        ".jfilemanager" + System.getProperty("file.separator"));
    private JPanel labelPanel, textFieldPanel;
    private JLabel headLine;
    private JComboBox programPath;
    private File file;
    private Vector comboList;
    private File progList;
    private JFileChooser fc;
    private JButton fcButton;
    private String[] cmd = new String[2];
    private boolean fcUsed;
    private LanguageData ld;
    private int itemCount=0;

  public OpenWithWindow(Vector selectedFile, LanguageData ld) {
    super(ld.get(ld.L_OPEN_WITH)+"...");
    this.ld=ld;
    if(OK){
    boolean fcUsed = false;
    progList = new File(baseDir,"progList.data");
    comboList = new Vector();
    //comboList.setSize(10);
    if(!progList.exists()){
        saveList(comboList);
    }else{
        itemCount=0;
        loadList();
        }

    file = (java.io.File)selectedFile.elementAt(0);
    mainPanel.remove(contentPanel);
    mainPanel.remove(progressPanel);

    labelPanel = new JPanel(new BorderLayout());
    textFieldPanel = new JPanel(new BorderLayout());

    fcButton = new JButton(ld.get(ld.L_BROWSE));
    headLine = new JLabel(ld.get(ld.L_FILE3)+" "+file.getName()+" "+ld.get(ld.L_OPEN_WITH)+":");
    programPath = new JComboBox(comboList);
    programPath.setEditable(true);

    labelPanel.add(headLine,BorderLayout.NORTH);
    textFieldPanel.add(programPath,BorderLayout.NORTH);
    textFieldPanel.add(fcButton,BorderLayout.SOUTH);

    mainPanel.add(labelPanel,BorderLayout.NORTH);
    mainPanel.add(textFieldPanel, BorderLayout.CENTER);

    cancelButton.setText(ld.get(ld.L_CANCEL));
    okButton.addActionListener(new java.awt.event.ActionListener() {
                                             public void actionPerformed(ActionEvent e) {
                                                 execProg();
                                             }
                                         }
                                      );

    fcButton.addActionListener(new java.awt.event.ActionListener() {
                                             public void actionPerformed(ActionEvent e) {
                                                 showFileChooser();
                                             }
                                         }
                                      );

    this.setSize(new Dimension(330,145));
    }
  }

  private void saveList(Vector list){
    try{
      PrintWriter out = new PrintWriter(new FileWriter(progList));
      for(int i=0;i<list.size();i++){
        out.println(list.elementAt(i));
      }
      out.close();
    }catch(IOException io){}
  }

  private void loadList(){
    String line;
    int i=0;
    try{
      BufferedReader in = new BufferedReader(new FileReader(progList));
      while((line=in.readLine())!=null){
        comboList.add(line);
        i++;
        itemCount++;
      }
      if(itemCount>=10)
        itemCount=0;
      in.close();

    }catch(IOException io){}
  }


  private void showFileChooser(){
    fcUsed = false;
    fc = new JFileChooser();
    fc.setDialogTitle(ld.get(ld.L_FILE3)+" "+file.getName()+" "+ld.get(ld.L_OPEN_WITH)+"...");
    int ret =  this.showOpenDialog(fc);
    if(ret == JFileChooser.APPROVE_OPTION){
      cmd[0]=fc.getSelectedFile().getPath();
      fcUsed=true;
      execProg();
    }
  }


  private void execProg(){
    boolean itemExists=false;
    try{
     if(!fcUsed){
      cmd[0]= (String)programPath.getSelectedItem();
     }
     cmd[1]= file.getPath();
    }catch(NullPointerException npe){}

    if(cmd[0]!=null){
      try{
        Runtime runProg = Runtime.getRuntime();
        runProg.exec(cmd);
        if(itemCount<10){
          for(int i=0;i<comboList.size();i++){
            if(((String)comboList.elementAt(i)).equals(cmd[0])){
              itemExists=true;
              }
            }
        //System.out.println(itemExists);
        if(itemExists!=true){
           //System.out.println(itemCount);
           if(progList.length()!=0){
              comboList.add((itemCount-1),cmd[0]);
            }else{
              comboList.add(cmd[0]);
            }
            saveList(comboList);
           }
          }
    }catch(IOException io){
      JOptionPane.showMessageDialog(mainPanel, ld.get(ld.L_FILE2)+" "+file.getName()+" "+ld.get(ld.L_EXEC_ERROR), "Fehler!",JOptionPane.ERROR_MESSAGE);
      }
    super.closeWin();
    this.dispose();
    }
  }

  public void changeLanguage(LanguageData l) {
    //super(ld.get(ld.L_OPEN_WITH)+"...");
    fcButton.setText(ld.get(ld.L_BROWSE));
    headLine.setText(ld.get(ld.L_FILE3)+" "+file.getName()+" "+ld.get(ld.L_OPEN_WITH)+":");
    cancelButton.setText(ld.get(ld.L_CANCEL));
    }
  }
