/**
 * IDEOptions.java - Sets properties within the Jipe Environment.  Copyright
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
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;


/** Class to display the Jipe Options dialog.
 *
 */
public class IDEOptions extends JDialog implements ActionListener
{

    /**      */
    Jipe jipe;
    /**      */
    private static Properties p;
    /**      */
    private String title = "IDE Options";

    //IDE Settings
    /**      */
    private JPanel messagePanel = new JPanel();
    /**      */
    private JPanel panel2=new JPanel();
    /**      */
    private JPanel panel2a=new JPanel();
    /**      */
    private JPanel panel2b=new JPanel();
    /**      */
    private JPanel panel2c=new JPanel();
    /**      */
    private JPanel panel3=new JPanel();
    /**      */
    private JPanel panel3a=new JPanel();
    /**      */
    private JPanel panel3b=new JPanel();
    /**      */
    private JPanel panel4=new JPanel();
    //    private String word1 = new String("Java");

    /**      */
    String word2 = new String();
    /**      */
    JComboBox jComboBox1= new JComboBox();
    /**      */
    JComboBox jComboBox2 = new JComboBox();
    /**      */
    JComboBox jComboBox3 = new JComboBox();
    /**      */
    JComboBox jComboBox4 = new JComboBox();
    /**      */
    JComboBox jComboBox5 = new JComboBox();
    /**      */
    JComboBox jComboBox6 = new JComboBox();
    /**      */
    JComboBox jComboBox7 = new JComboBox();
    /**      */
    JComboBox jComboBox8 = new JComboBox();
    /**      */
    JButton jButton1 = new JButton("Change");
    /**      */
    JCheckBox jCheckBox1;
    // Console Settings
    /**      */
    private JPanel panel5=new JPanel();
    /**      */
    private JPanel panel5a=new JPanel();
    /**      */
    private JPanel panel5b=new JPanel();


    /**      */
    JButton bcolor=new JButton("Background Color");
    /**      */
    JButton fcolor=new JButton("Foreground Color");


    /**      */
    public IDEOptions(Jipe parent){
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

        jCheckBox1=new JCheckBox("Auto Indent",jipe.props.AUTOINDENT);

        messagePanel.setBorder(
            BorderFactory.createTitledBorder(""));
        panel2.setBorder(
            BorderFactory.createTitledBorder(""));
        panel3.setBorder(
            BorderFactory.createTitledBorder(""));
        panel4.setBorder(
            BorderFactory.createTitledBorder(""));
        panel5.setBorder(
            BorderFactory.createTitledBorder(""));

        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));


        //        jComboBox1 = new FontComboBox();

        jComboBox1.addItem("Serif");
        jComboBox1.addItem("SansSerif");
        jComboBox1.addItem("Monospaced");
        jComboBox1.addItem("Dialog");
        jComboBox1.addItem("DialogInput");
        //        jComboBox1.addItem("Lucida Console");

        jComboBox2.addItem("8");
        jComboBox2.addItem("9");
        jComboBox2.addItem("10");
        jComboBox2.addItem("11");
        jComboBox2.addItem("12");
        jComboBox2.addItem("14");
        jComboBox2.addItem("16");
        jComboBox2.addItem("20");

        //       jComboBox5 = new FontComboBox();

        jComboBox5.addItem("Serif");
        jComboBox5.addItem("SansSerif");
        jComboBox5.addItem("Monospaced");
        jComboBox5.addItem("Dialog");
        jComboBox5.addItem("DialogInput");
        //        jComboBox5.addItem("Lucida Console");

        jComboBox6.addItem("8");
        jComboBox6.addItem("9");
        jComboBox6.addItem("10");
        jComboBox6.addItem("11");
        jComboBox6.addItem("12");
        jComboBox6.addItem("14");
        jComboBox6.addItem("16");
        jComboBox6.addItem("20");

        UIManager.LookAndFeelInfo[] tpLAF =
            UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < tpLAF.length; i++)
        {
            jComboBox3.addItem(tpLAF[i].getName().toString());
        }

        jComboBox4.addItem("4");
        jComboBox4.addItem("8");
        jComboBox4.addItem("12");
        panel2.setLayout(new GridLayout(0,1,4,0));
        panel2a.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel2b.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel2c.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel2a.add(new JLabel("Font"));
        panel2a.add(jComboBox1);
        panel2a.add(new JLabel("Size"));
        panel2a.add(jComboBox2);
        panel2b.add(new JLabel("Tab Spaces"));
        panel2b.add(jComboBox4);
        panel2b.add(jCheckBox1);
        panel2c.add(new JLabel("Look & Feel"));
        panel2c.add(jComboBox3);

        panel2.add(panel2a);
        panel2.add(panel2b);
        panel2.add(panel2c);
        jCheckBox1.addActionListener(this);

        panel5.setLayout(new GridLayout(0,1,1,0));
        panel5a.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel5a.add(new JLabel("Font"));
        panel5a.add(jComboBox5);
        panel5a.add(new JLabel("Size"));
        panel5a.add(jComboBox6);
        panel5b.add(bcolor);
        panel5b.add(fcolor);
        panel5.add(panel5a);
        panel5.add(panel5b);

        jComboBox7.addItem("Java");
        jComboBox7.addItem("C/C++");
        jComboBox7.addItem("HTML");
        jComboBox7.addItem("JScript");
        jComboBox7.addItem("JSP");

        jComboBox8.addItem("Block comment");
        jComboBox8.addItem("Line comment");
        jComboBox8.addItem("Keyword 1");
        jComboBox8.addItem("Keyword 2");
        jComboBox8.addItem("Classes");
        jComboBox8.addItem("String");
        jComboBox8.addItem("Keyword 3");
        jComboBox8.addItem("Label");

        panel3.setLayout(new GridLayout(0,1,3,0));
        panel3a.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel3b.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel3a.add(new JLabel("Language"));
        panel3a.add(jComboBox7);
        panel3b.add(new JLabel("Color group"));
        panel3b.add(jComboBox8);
        panel3b.add(jButton1);
        jComboBox7.addActionListener(this);
        jComboBox8.addActionListener(this);
        panel3.add(panel3a);
        panel3.add(panel3b);
        jButton1.addActionListener(this);
        jButton1.setBackground(new Color(jipe.props.JCOLOR[0]));
        jButton1.repaint();

        bcolor.setBackground(jipe.props.CONSOLEBCOLOUR);
        fcolor.setBackground(jipe.props.CONSOLEFCOLOUR);
        fcolor.addActionListener(this);
        bcolor.addActionListener(this);

        JTabbedPane tp=new JTabbedPane();

        tp.addTab("Editor",panel2);
        tp.addTab("Console",panel5);
        tp.add(messagePanel,"Preferences");
        tp.addTab("Syntax Colours",panel3);
        tp.addTab("Code Completion",panel4);

        final JOptionPane pane = new JOptionPane(
                                     tp, // message
                                     JOptionPane.PLAIN_MESSAGE, // messageType
                                     JOptionPane.OK_CANCEL_OPTION); // optionType

        //
        //  Display the currently set options in the pane.
        //

        String fontsize="8";
        String consolefontsize="8";
        fontsize=fontsize.valueOf(jipe.props.FONTSIZE).toString();
        consolefontsize=consolefontsize.valueOf(jipe.props.CONSOLEFONTSIZE).toString();
        String tabsize="4";
        tabsize=tabsize.valueOf(jipe.props.TABSIZE).toString();
        jComboBox1.setSelectedItem(jipe.props.FONTSTYLE);
        jComboBox2.setSelectedItem(fontsize);
        jComboBox3.setSelectedItem(jipe.props.LOOKANDFEEL);
        jComboBox4.setSelectedItem(tabsize);
        jComboBox5.setSelectedItem(jipe.props.CONSOLEFONT);
        jComboBox6.setSelectedItem(consolefontsize);
        jComboBox7.setSelectedItem("Java");

        //
        // Display the Pane
        //

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
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == bcolor)
        {
            Color color = JColorChooser.showDialog(IDEOptions.this, "Color Chooser", jipe.props.CONSOLEBCOLOUR);
            jipe.props.CONSOLEBCOLOUR=color;
        }
        else if (source == fcolor)
        {
            Color color = JColorChooser.showDialog(IDEOptions.this, "Color Chooser", jipe.props.CONSOLEFCOLOUR);
            jipe.props.CONSOLEFCOLOUR=color;
        }
        else if (source == jButton1){
            SelectColors(IDEOptions.this,jComboBox7.getSelectedItem().toString(),jComboBox8.getSelectedIndex());
        }
        else if (source == jCheckBox1){
          if(jCheckBox1.isSelected())jipe.props.AUTOINDENT=true;
          else jipe.props.AUTOINDENT=false;
        }
        else if (source == jComboBox7)
        {
            if (jComboBox7.getSelectedItem().toString().equals("Java"))
            {

                jComboBox8.removeAllItems();
                jComboBox8.addItem("Block comment");
                jComboBox8.addItem("Line comment");
                jComboBox8.addItem("Keyword 1");
                jComboBox8.addItem("Keyword 2");
                jComboBox8.addItem("Classes");
                jComboBox8.addItem("String");
                jComboBox8.addItem("Keyword 3");
                jComboBox8.addItem("Label");
                jButton1.setBackground(new Color(jipe.props.JCOLOR[jComboBox8.getSelectedIndex()]));
                jButton1.repaint();
            }
            else if (jComboBox7.getSelectedItem().toString().equals("C/C++"))
            {
                jComboBox8.removeAllItems();
                jComboBox8.addItem("Block comment");
                jComboBox8.addItem("Line comment");
                jComboBox8.addItem("Keyword 1");
                jComboBox8.addItem("Keyword 2");
                jComboBox8.addItem("Classes");
                jComboBox8.addItem("String");
                jComboBox8.addItem("Keyword 3");
                jComboBox8.addItem("Label");
                jButton1.setBackground(new Color(jipe.props.CCOLOR[jComboBox8.getSelectedIndex()]));
                jButton1.repaint();
            }
            else if (jComboBox7.getSelectedItem().toString().equals("HTML"))
            {
                jComboBox8.removeAllItems();
                jComboBox8.addItem("JS comment1");
                jComboBox8.addItem("JS comment2");
                jComboBox8.addItem("Tag");
                jComboBox8.addItem("Keywords");
                jComboBox8.addItem("JS keywords");
                jComboBox8.addItem("JS string");
                jComboBox8.addItem("JS literal");
                jComboBox8.addItem("HTML string");
                jButton1.setBackground(new Color(jipe.props.HCOLOR[jComboBox8.getSelectedIndex()]));
                jButton1.repaint();
            }
            else if (jComboBox7.getSelectedItem().toString().equals("JScript"))
            {
                jComboBox8.removeAllItems();
                jComboBox8.addItem("Keyword 1");
                jComboBox8.addItem("Keyword 2");
            }
            else if (jComboBox7.getSelectedItem().toString().equals("JSP"))
            {
                jComboBox8.removeAllItems();
                jComboBox8.addItem("Comment 1");
                jComboBox8.addItem("Comment 2");
                jComboBox8.addItem("Key 1");
                jComboBox8.addItem("Key 2");
                jComboBox8.addItem("Key 3");
                jComboBox8.addItem("String 1");
                jComboBox8.addItem("Jkey/String");
                jComboBox8.addItem("String 2");
            }
        }
        //  else if ((source == jComboBox8)&& (!(word1.equals(jComboBox7.getSelectedItem().toString()))))
        else if (source == jComboBox8)
            //word1 is required since on instantiation of IDEOptions two actionevents are sent from
            //jComboBox8. Figure out why from basic definition of event.
        {
            if (jComboBox8.getSelectedIndex()>=0)
                //when jComboBox7 is changed too two events are created by jComboBox8
                //First one has invalid index. (Why?)
            {
                if (jComboBox7.getSelectedItem().toString().equals("Java"))
                {
                    jButton1.setBackground(new Color(jipe.props.JCOLOR[jComboBox8.getSelectedIndex()]));
                    jButton1.repaint();
                }
                else if (jComboBox7.getSelectedItem().toString().equals("C/C++"))
                {
                    jButton1.setBackground(new Color(jipe.props.CCOLOR[jComboBox8.getSelectedIndex()]));
                    jButton1.repaint();
                }
                else if (jComboBox7.getSelectedItem().toString().equals("HTML"))
                {
                    jButton1.setBackground(new Color(jipe.props.HCOLOR[jComboBox8.getSelectedIndex()]));
                    jButton1.repaint();
                }
                else if (jComboBox7.getSelectedItem().toString().equals("JScript"))
                {
                    jButton1.setBackground(new Color(jipe.props.JSCOLOR[jComboBox8.getSelectedIndex()+3]));
                    jButton1.repaint();
                }
                else if (jComboBox7.getSelectedItem().toString().equals("JSP"))
                {
                    jButton1.setBackground(new Color(jipe.props.JSPCOLOR[jComboBox8.getSelectedIndex()]));
                    jButton1.repaint();
                }
                else if (jComboBox7.getSelectedItem()==null){}
            }
        }
    }


    /**
     * Changes the color associated with each type word
     *
     * @param cmp
     * @param itemName
     * @param elementindx
     */
    public void SelectColors(Component cmp,String itemName, int elementindx){
        try{
            if(itemName.equals("Java")){
                Color original=new Color(jipe.props.JCOLOR[elementindx]);
                Color color= JColorChooser.showDialog(cmp,"Color Chooser",original);
                jipe.props.JCOLOR[elementindx]=color.getRGB();
            }
            else if (itemName.equals("C/C++")){
                Color original=new Color(jipe.props.CCOLOR[elementindx]);
                Color color= JColorChooser.showDialog(cmp,"Color Chooser",original);
                jipe.props.CCOLOR[elementindx]=color.getRGB();
            }
            else if (itemName.equals("HTML")){
                Color original=new Color(jipe.props.HCOLOR[elementindx]);
                Color color= JColorChooser.showDialog(cmp,"Color Chooser",original);
                jipe.props.HCOLOR[elementindx]=color.getRGB();
            }
            else if (itemName.equals("JScript")){
                Color original=new Color(jipe.props.JSCOLOR[elementindx+3]);
                Color color= JColorChooser.showDialog(cmp,"Color Chooser",original);
                jipe.props.JSCOLOR[elementindx+3]=color.getRGB();
            }
            else if (itemName.equals("JSP")){
                Color original=new Color(jipe.props.JSPCOLOR[elementindx]);
                Color color= JColorChooser.showDialog(cmp,"Color Chooser",original);
                jipe.props.JSPCOLOR[elementindx]=color.getRGB();
            }
        }
        catch(NullPointerException ex){}
    }
    /** Updates jipe so that it can start using its new settings.
     *
     */
    public void UpdateReferences(){
        String lnfName = jComboBox3.getSelectedItem().toString();
        if (p.getProperty("LOOKFEEL") != lnfName)
        {

            jipe.props.LOOKANDFEEL=lnfName;
            // Force SwingSet to come up in the Cross Platform L&F
            /*
            try
            {
                if (lnfName.startsWith("Metal"))
                    lnfName = "javax.swing.plaf.metal.MetalLookAndFeel";
                else if (lnfName.startsWith("Window"))
                    lnfName =
                        "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
                else if (lnfName.startsWith("CDE/Motif"))
                    lnfName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
                else
                    lnfName = UIManager.getCrossPlatformLookAndFeelClassName();
                UIManager.setLookAndFeel(lnfName);
                SwingUtilities.updateComponentTreeUI(jipe.frimble.getContentPane());

            }
            catch (Exception exc)
            {
                System.err.println("Error loading L&F: " + exc);
            }
            */
        }
        int tabsize = Integer.parseInt(jComboBox4.getSelectedItem().toString());
        jipe.props.TABSIZE = tabsize;
        if(jCheckBox1.isSelected())jipe.props.AUTOINDENT = true;
          else jipe.props.AUTOINDENT = false;
        if (jComboBox1.getSelectedItem().toString() != jipe.props.FONTSTYLE || Integer.parseInt
                (jComboBox2.getSelectedItem().toString()) != jipe.props.FONTSIZE)
        {
            jipe.props.FONTSIZE = Integer.parseInt(
                                      jComboBox2.getSelectedItem().toString());
            jipe.props.FONTSTYLE = jComboBox1.getSelectedItem().toString();
            for (int counter = 0; counter < jipe.children.size(); counter++)
            {
                Editor child = (Editor)jipe.children.elementAt(counter);
                child.textarea.getPainter().setFont(new Font(
                                                        jipe.props.FONTSTYLE, 0, jipe.props.FONTSIZE));
                child.textarea.setText(child.textarea.getText());
            }
        }
        if (jComboBox5.getSelectedItem().toString() != jipe.props.CONSOLEFONT || Integer.parseInt
                (jComboBox6.getSelectedItem().toString()) != jipe.props.CONSOLEFONTSIZE)
        {
            jipe.props.CONSOLEFONTSIZE = Integer.parseInt(
                                             jComboBox6.getSelectedItem().toString());
            jipe.props.CONSOLEFONT = jComboBox5.getSelectedItem().toString();
            jipe.jipeconsole.jipeConsole.setFont(new Font(jipe.props.CONSOLEFONT, 0, jipe.props.CONSOLEFONTSIZE));
        }
        jipe.jipeconsole.jipeConsole.setForeground(jipe.props.CONSOLEFCOLOUR);
        jipe.jipeconsole.jipeConsole.setBackground(jipe.props.CONSOLEBCOLOUR);
        jipe.jipeconsole.jipeConsole.setCaretColor(jipe.props.CONSOLEFCOLOUR);
        jipe.props.SaveOptions();
    }
}
