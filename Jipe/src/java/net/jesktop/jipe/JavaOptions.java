/**
 * JavaOptions.java - Sets properties within the Jipe Environment.  Copyright
 * (c) 1996-2000 Steve Lawson.  E-Mail steve@e-i-s.co.uk Latest version can be
 * found at http://e-i-s.co.uk/jipe
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
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


public class JavaOptions extends JDialog implements ActionListener
{

    Jipe jipe;
    private static Properties p;
    private String title = "Java Options";

    JTextField jTextField1 = new JTextField();
    JTextField jTextField2 = new JTextField();
    JTextField jTextField3 = new JTextField();
    JTextField jTextField4 = new JTextField();
    JTextField jTextField5 = new JTextField();
    JTextField jTextField6 = new JTextField();
    JTextField jTextField7 = new JTextField();
    JTextField jTextField8 = new JTextField();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    JButton jButton3 = new JButton();
    JButton jButton4 = new JButton();
    JButton jButton5 = new JButton();
    JButton jButton6 = new JButton();
    JButton jButton7 = new JButton();
    JButton jButton8 = new JButton();

    private JCheckBox[] checkBoxes = {
        new JCheckBox("g"),
        new JCheckBox("O"),
        new JCheckBox("deprecation"),
        new JCheckBox("nowarn"),
        new JCheckBox("verbose"),
        new JCheckBox("nowrite"),
    };
    public JavaOptions(Jipe parent){
        jipe=parent;
        try
        {
            p = new Properties();
            String userdir = System.getProperty("user.home");
            userdir = userdir + System.getProperty("file.separator") +
                      "jipe.properties";
            File file = new File(userdir);
            if (file.exists())
                p.load(new FileInputStream(userdir));
            else
            {
                InputStream is = getClass().getResourceAsStream(
                                     "resources/jipe.properties");
                p.load(is);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        JPanel messagePanel = new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3=new JPanel();

        messagePanel.setBorder(
            BorderFactory.createTitledBorder("Javac Options:"));

        panel2.setBorder(
            BorderFactory.createTitledBorder(""));
        panel3.setBorder(
            BorderFactory.createTitledBorder(""));

        if(p.getProperty("OPTIONSTRING").indexOf("-g")!=-1)checkBoxes[0]=new JCheckBox("g",true);
        else checkBoxes[0]=new JCheckBox("g");
        if(p.getProperty("OPTIONSTRING").indexOf("-O")!=-1)checkBoxes[1]=new JCheckBox("O",true);
        else checkBoxes[1]=new JCheckBox("o");
        if(p.getProperty("OPTIONSTRING").indexOf("-deprecation")!=-1)checkBoxes[2]=new JCheckBox("deprecation",true);
        else checkBoxes[2]=new JCheckBox("deprecation");
        if(p.getProperty("OPTIONSTRING").indexOf("-nowarn")!=-1)checkBoxes[3]=new JCheckBox("nowarn",true);
        else checkBoxes[3]=new JCheckBox("nowarn");
        if(p.getProperty("OPTIONSTRING").indexOf("-verbose")!=-1)checkBoxes[4]=new JCheckBox("verbose",true);
        else checkBoxes[4]=new JCheckBox("verbose");
        if(p.getProperty("OPTIONSTRING").indexOf("-nowrite")!=-1)checkBoxes[5]=new JCheckBox("nowrite",true);
        else checkBoxes[5]=new JCheckBox("nowrite");

        messagePanel.setLayout(new GridLayout(0,3,2,0));
        for(int i=0; i < checkBoxes.length; ++i){
            messagePanel.add(checkBoxes[i]);
        }

        JPanel panel2a=new JPanel();
        JPanel panel2b=new JPanel();
        JPanel panel2c=new JPanel();
        JPanel panel2d=new JPanel();
        JPanel panel2e=new JPanel();
        panel2.setLayout(new GridLayout(0,1,3,0));
        //        panel2a.setLayout(new FlowLayout(FlowLayout.LEFT));
        //        panel2b.setLayout(new FlowLayout(FlowLayout.LEFT));
        //        panel2c.setLayout(new FlowLayout(FlowLayout.LEFT));
        jButton1.setText("Browse");
        jButton2.setText("Browse");
        jButton3.setText("Browse");
        jButton6.setText("Browse");
        jButton7.setText("Browse");
        jButton8.setText("Browse");
        panel2a.add(new JLabel("Compiler  "));
        panel2a.add(jTextField1);
        jTextField1.setColumns(20);
        panel2a.add(jButton1);
        panel2b.add(new JLabel("Browser    "));
        panel2b.add(jTextField2);
        jTextField2.setColumns(20);
        panel2b.add(jButton2);
        panel2c.add(new JLabel("Interpreter "));
        panel2c.add(jTextField3);
        jTextField3.setColumns(20);
        panel2c.add(jButton3);
        panel2d.add(new JLabel("Debugger "));
        panel2d.add(jTextField6);
        jTextField6.setColumns(20);
        panel2d.add(jButton6);

        panel2e.add(new JLabel("Decompiler "));
        panel2e.add(jTextField8);
        jTextField8.setColumns(20);
        panel2e.add(jButton8);



        panel2.add(panel2a);
        panel2.add(panel2b);
        panel2.add(panel2c);
        panel2.add(panel2d);
        panel2.add(panel2e);
        JPanel panel3a=new JPanel();
        JPanel panel3b=new JPanel();
        JPanel panel3c=new JPanel();
        panel3.setLayout(new GridLayout(0,1,3,0));
        //        panel3a.setLayout(new FlowLayout(FlowLayout.LEFT));
        //        panel3b.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel3a.add(new JLabel("Class Paths      "));
        panel3a.add(jTextField4);
        jTextField4.setColumns(25);

        panel3c.add(new JLabel("JavaDocs "));
        panel3c.add(jTextField7);
        jTextField7.setColumns(20);
        panel3c.add(jButton7);

        panel3b.add(new JLabel("Output Directory"));
        panel3b.add(jTextField5);
        jTextField5.setColumns(25);
        panel3.add(panel3a);
        panel3.add(panel3b);
        panel3.add(panel3c);

        JTabbedPane tp=new JTabbedPane();
        tp.add(panel2,"Directories");
        tp.addTab("User Paths",panel3);
        tp.addTab("Options",messagePanel);

        jTextField1.setText(p.getProperty("COMPILER"));
        jTextField2.setText(p.getProperty("BROWSER"));
        jTextField3.setText(p.getProperty("JAVA"));
        jTextField6.setText(p.getProperty("DEBUG"));
        jTextField8.setText(p.getProperty("DECOMPILER"));
        jTextField7.setText(p.getProperty("JAVADOCS"));
        jTextField4.setText(p.getProperty("CLASSPATH"));
        jTextField5.setText(p.getProperty("OUTPUTDIR"));

        jButton1.addActionListener(this);
        jButton2.addActionListener(this);
        jButton3.addActionListener(this);
        jButton4.addActionListener(this);
        jButton5.addActionListener(this);
        jButton6.addActionListener(this);
        jButton7.addActionListener(this);
        jButton8.addActionListener(this);



        final JOptionPane pane = new JOptionPane(
                                     tp, // message
                                     JOptionPane.PLAIN_MESSAGE, // messageType
                                     JOptionPane.OK_CANCEL_OPTION); // optionType

        JDialog dialog = pane.createDialog(
                             this, // parentComponent
                             title); // title

        dialog.setResizable(false);
        dialog.pack();
        dialog.show();

        Integer value=(Integer)pane.getValue();
        if(value.intValue()==JOptionPane.OK_OPTION)UpdateReferences();
    }

    /**
     * Respond to users requests
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == jButton1)
            jTextField1.setText(BrowseName("Compiler", jTextField1.getText()));
        else if (source == jButton2)
            jTextField2.setText(BrowseName("Browser", jTextField2.getText()));
        else if (source == jButton3)
            jTextField3.setText(BrowseName(
                                    "Interpreter", jTextField3.getText()));
        else if (source == jButton6)
            jTextField6.setText(BrowseName(
                                    "Debugger", jTextField6.getText()));
        else if (source == jButton7)
            jTextField7.setText(BrowseName(
                                    "Java Docs", jTextField7.getText()));
        else if (source == jButton8)
            jTextField8.setText(BrowseName(
                                    "Decompiler", jTextField8.getText()));
    }
    String BrowseName(String title, String field)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Browse for " + title);
        int selected = fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file != null && selected == JFileChooser.APPROVE_OPTION)
            return (file.getPath());
        else
            return field;
    }

    public void UpdateReferences(){

        jipe.props.JAVA=  jTextField3.getText();
        jipe.props.BROWSER= jTextField2.getText();
        jipe.props.COMPILER= jTextField1.getText();
        jipe.props.DEBUG= jTextField6.getText();
        jipe.props.DECOMPILER= jTextField8.getText();
        jipe.props.JAVADOCS= jTextField7.getText();
        jipe.props.CLASSPATH= jTextField4.getText();
        jipe.props.OUTPUTDIR= jTextField5.getText();
        jipe.props.JAVAOPTIONSTRING=optionString();
        jipe.props.SaveOptions();
    }
    String optionString(){
        String optionstring="";
        if(checkBoxes[0].isSelected())optionstring+="-g ";
        if(checkBoxes[1].isSelected())optionstring+="-O ";
        if(checkBoxes[2].isSelected())optionstring+="-deprecation ";
        if(checkBoxes[3].isSelected())optionstring+="-nowarn ";
        if(checkBoxes[4].isSelected())optionstring+="-verbose ";
        if(checkBoxes[5].isSelected())optionstring+="-nowrite ";
        return optionstring;
    }
}
