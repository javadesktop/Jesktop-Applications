/**
 * FindInFiles.java - FindinFiles class for use with the Jipe Project.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package net.jesktop.jipe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

import org.gjt.sp.jedit.syntax.*;
import org.gjt.sp.jedit.textarea.*;

import org.jesktop.frimble.*;


public class FindInFiles extends JDialog implements ActionListener


{
    Jipe jipe;
    JPanel jPanel = new JPanel();
    JButton find_next = new JButton("   Find    ");
    JButton cancel = new JButton(" Cancel");
    JTextField find = new JTextField();
    JLabel jLabel1 = new JLabel("Find Text");
    JLabel jLabel2 = new JLabel("In Types");
    JLabel jLabel3 = new JLabel("Directory");
    JCheckBox matchcase = new JCheckBox("Match Case");
    JCheckBox include = new JCheckBox("Search subdirectories");
    JTextField jTypes = new JTextField();
    JTextField jDirectory = new JTextField();
    JButton browseButton = new JButton("Browse");
    JFileChooser chooseDirectory;
    int count;

    Vector filePathStrings; //for storing the filepaths
    Vector fileLinesVector; //for storing the lines where the string was found
    JEditTextArea editor;

    protected static FindInFiles makeFindInFiles(Jipe jipe, Frimble frimble) {
    Component contained = frimble.getFrimbleContained();
    if (contained instanceof JFrame) {
      return new FindInFiles(jipe, (JFrame) contained);
    } else {
      return new FindInFiles(jipe, null);
    }
  }


    public FindInFiles( Jipe master, JFrame frame)
    {
        super(frame, "Find in files", false);
        jipe = master;

        jipe.filesearchlistPanel.setListData((Vector)null);
        jipe.filesearchlistPanel.removeAll();
        jipe.filesearchlistPanel.setModel(jipe.filesearchModel);
        JPanel jPanela = new JPanel();
        JPanel jPanelb = new JPanel();
        JPanel jPanelc = new JPanel();
        JPanel jPaneld = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 4, 0));
        jPanela.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanelb.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanelc.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPaneld.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanela.add(jLabel1);
        jPanela.add(find);
        jPanela.add(find_next);
        jPanelb.add(jLabel2);
        jPanelb.add(jTypes);
        jPanelb.add(cancel);
        jPanelc.add(jLabel3);
        jPanelc.add(jDirectory);
        jPanelc.add(browseButton);
        jPaneld.add(matchcase);
        jPaneld.add(include);

        find.setColumns(14);
        jTypes.setColumns(14);
        jDirectory.setColumns(14);

        jPanel.setBorder(BorderFactory.createTitledBorder(""));

        jPanel.add(jPanela);
        jPanel.add(jPanelb);
        jPanel.add(jPanelc);
        jPanel.add(jPaneld);
        jTypes.setText(".java");

        getContentPane().add(jPanel);

        find_next.addActionListener(this);
        cancel.addActionListener(this);
        browseButton.addActionListener(this);

        setResizable(false);
        pack();
    }

    public void actionPerformed(ActionEvent event)
    {
        Object source = event.getSource();
        if (source == cancel)
        {
            setVisible(false);
        }

        // Find the Text in the Files/Folders
        if (source == find_next)
            find();

        // Browse for the starting directory
        if (source == browseButton)
            browse();

    }

    //First get a listing of the files, then search within them

    public void find()
    {
        StringParser sp = new StringParser(); //necessary only for Windows OS
        getFiles(sp.ModifyPath(jDirectory.getText()),jTypes.getText()); //gets a Vector with file path strings
        jipe.filesearchModel.removeAllElements();
        //Now search within these files
        fileLinesVector = new Vector();   //for storing the search results
        Enumeration en = filePathStrings.elements();
        while(en.hasMoreElements()){
            try{
                count = 0;
                String searchFile = new String(en.nextElement().toString());
                FileReader f = new FileReader(searchFile);
                LineNumberReader fileStream = new LineNumberReader(f);
                int c;
                StringBuffer line = new StringBuffer("(1) : ");
                while((c = fileStream.read())!= -1){
                    line.append((char)c);
                    if (c=='\n'){
                        compare(find.getText(),new String(line),searchFile);
                        line = new StringBuffer("("+(fileStream.getLineNumber()+1)+") : ");
                    }
                }
            }
            catch(FileNotFoundException e1){
                System.out.println("File not found :"+ e1.toString());
            }
            catch(IOException e1){
                System.out.println("Error opening stream :"+ e1.toString());
            }
        }
        if (fileLinesVector.isEmpty())fileLinesVector.addElement(" Found string 0 times");
        Enumeration en1 = fileLinesVector.elements();
        jipe.tabbedPane3.setSelectedIndex(2);
        while (en1.hasMoreElements()){
            Object obj1 = en1.nextElement();
            String str=obj1.toString();
            jipe.filesearchModel.addElement(str.trim());
            MouseListener ml = new MouseAdapter() { //listen for mouse double click in the Search panel
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() >= 2) {
                  String selected = new String(jipe.filesearchlistPanel.getSelectedValue().toString());
                  int filepath_end_index = selected.indexOf("(");  //get the filepath
                  String filepath = new String(selected.substring(0,filepath_end_index-1));
                  try{
                  File file_to_open = new File(filepath);
                  openFile(file_to_open,selected);  // open the file and go to the line
                  }catch (Exception exc){
                  System.out.println("Could not open the file!");
                  }
           }
        }
      };
  jipe.filesearchlistPanel.addMouseListener(ml);

        }

    }

    //gets the linenumber from the string passed to it

    public String getLineNumber(String line){
      char ch =' ';
      int charcount = 0;
      int maxlength = line.length();
      StringBuffer tempnum = new StringBuffer();
      StringBuffer temp = new StringBuffer(line.substring(line.indexOf("(")+1,line.length()));
    do{
      tempnum.append(temp.charAt(charcount));
        ++charcount;
        ch = temp.charAt(charcount);
         }while (ch!=')'&& charcount<= maxlength);
      return tempnum.toString();
    }


    //if the file  is not open, opens it. Also goes to the selected line

    public void openFile(File xfile, String selectedstring){
      if (xfile != null){
        if (jipe.GetWindow(xfile) == false)
                jipe.addChild(jipe, xfile.getName(), xfile);
        }
        String linenum = getLineNumber(selectedstring);
      Integer ilinenum = new Integer(linenum);
      int linenumber = ilinenum.intValue();
      jipe.activeChild.gotoLine(linenumber-1);
    }


    public void getFiles(String start,String extn){
        if (include.isSelected()){            //if you have to search the subdirectories too
            ListAllFiles listAll = new ListAllFiles(start,extn);
            filePathStrings = listAll.filevector;
        }
        else{
            try{                    // else simply get the File array within directory
                File listDirectory = new File(start);
          //      File[] filelist = listDirectory.listFiles(new ExtensionFilter(extn)); //Had to remove - not JDK1.1 compatible. SL
                String[] filelist = listDirectory.list(new ExtensionFilter(extn));
                filePathStrings = new Vector();
                for (int i=0;i<filelist.length;++i){
                    filePathStrings.addElement(listDirectory.getPath()+System.getProperty("file.separator")+filelist[i]);  // Had to alter to accomodate above change. SL
                }
            }
            catch(Exception e){
                System.out.println("Sorry, such a file does not exist");
            }
        }
    }

    // See if the search string is an element of the read line

    public void compare(String findstring, String fileline, String sourceFile){
        String fnd,target;
        if (matchcase.isSelected()){
            target = fileline.substring(fileline.indexOf(":"));
            fnd = findstring;
        }
        else{
            target = fileline.substring(fileline.indexOf(":")).toLowerCase();
            fnd = findstring.toLowerCase();
        }
        if (target.indexOf(fnd)!= -1){
//            if (count == 0) fileLinesVector.addElement("Found string in " + sourceFile +"\n");
      fileLinesVector.addElement(sourceFile+" "+removeWhitespace(fileline));
            count++;
        }
    }

    public void browse()
    {
        chooseDirectory = new JFileChooser(System.getProperty("java.home"));
        chooseDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooseDirectory.setDialogTitle("Choose directory");
        int selected = chooseDirectory.showOpenDialog(this);
        File file = chooseDirectory.getSelectedFile();
        if (file != null && selected == JFileChooser.APPROVE_OPTION)
            jDirectory.setText(file.getPath());
        else
            jDirectory.setText(System.getProperty("java.home"));
    }

    //removes the whitespace from the strings before displaying them in the
    //Search panel

    public String removeWhitespace(String spacedstring){
      String tokeep = spacedstring.substring(0,spacedstring.indexOf(":")+1);
      String totest = spacedstring.substring(spacedstring.indexOf(":")+1);
      boolean linestarted = false;
      char testchar = ' ';
      int linestart = 0;
      while ((!linestarted) && (linestart <= totest.length())){
        testchar = totest.charAt(linestart);
        if (!Character.isWhitespace(testchar))
          linestarted = true;
        ++linestart;
      }
      String toreturn = tokeep + " " + totest.substring(linestart-1);
      return toreturn;
    }
}

