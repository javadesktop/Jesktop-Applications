
/*
 * @(#)TextpadUtils.java- Utility cklass for  Textpad
 * Copyright (C) 20000, 2001, 2002 Dr.M.L.Noone
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  http://www.gnu.org/copyleft/gpl.html
 *
 */

package net.jesktop.textpad;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import org.jesktop.frimble.*;

import net.jesktop.textpad.Textpad;

/**
*  Bundle of utility functions for Textpad
*  @author Dr.M.L.Noone
*/
public final class TextpadUtils {

  private Frimble frimble;


  protected TextpadUtils(Frimble frimble) {
    this.frimble = frimble;
  }


/**
  * Saves a Hashtable to a file
  */
  public final void saveHashtable(Hashtable H,File f) {
    try {
      PrintWriter out  = new PrintWriter(new BufferedWriter(new FileWriter(f)),true);
      for (java.util.Enumeration en = H.keys();en.hasMoreElements();)  {
        String key = (String) en.nextElement();
        out.println(key+'='+H.get(key));

      }
      out.close();
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}


  }


/**
  * Loads a Hashtable from a file
  */
  public final void loadHashtable(Hashtable toLoad, File data) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(data));
      String inline = new String();
      while ((inline = in.readLine()) !=null) {
        try {
          int sep = inline.indexOf('=');
          String key = new String(inline.substring(0,sep));
          toLoad.put (key , inline.substring(sep+1));
        } catch(StringIndexOutOfBoundsException e) {}
      }
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }

  /**
  * Gets a Color
  * @param s color name
        */
  public final Color getColor(String s){
    switch (s.charAt(0)) {
      case 'W':
        return Color.white;
      case 'B':
        if (s.charAt(2)=='a') return Color.black; else return Color.blue;
      case 'G':
        if (s.charAt(2)=='a') return Color.gray; else return Color.green;
      case 'R':
        return Color.red;
      case 'C':
        return Color.cyan;
      case 'M':
        return Color.magenta;
      case 'Y':
        return Color.yellow;
      case 'L':
        return Color.lightGray;
      case 'D':
        return Color.darkGray;
      case 'P':
        return Color.pink;
      case 'O':
        return Color.orange;
      default :
        return null;
    }

  }


/**
  * Gets the menu keys for a menu
  * from its id
.
  * @param id The char identifying the menu.
        */
  public final String[] getMenuKeys(char id) {

    switch(id) {
      case 'f': String[] s={"new","open","reload","-","save","save_as","-","add_to_presets","add_dir_to_presets","-","exit"}; return s;
      case 'e': String[] s1={"undo","redo","-","cut","copy","paste","-","select_all"}; return s1;
      case 'v': String[] s2={"*jfont","*background","*text","*lnf"}; return s2;
      case 'j': String[] s3={"Courier","Helvetica","TimesRoman","-","12","13","14","15","16","17","18","19","20"}; return s3;
      case 'b': String[] s4={"White","Black","Gray","Red","Green","Blue","Cyan","Magenta","Yellow","Light Gray","Dark Gray","Pink","Orange"}; return s4;
      case 't': String[] s5={"Black","White","Gray","Red","Green","Blue","Cyan","Magenta","Yellow","Light Gray","Dark Gray","Pink","Orange"}; return s5;
      case 'l': {
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        int l =info.length;
        String[] lnfs = new String[l];
        for (int i=0;i<l;i++ ) {
          lnfs[i] = info[i].getName();
        }
        return lnfs;
      }
      case 'p' : String[] s6={"-","editps","update"}; return s6;
      case 's' : String[] s7={"find","find_next","goto_line"}; return s7;
      case 'h' : String[] s8={"usage","about"}; return s8;
    }
    return null;
  }


/**
  * Builds a hashtable of KeyStroke shortcuts for the menus.
  * @return Hashtable of Keyboard shortcuts
  */
  public static final Hashtable buildAccelTable() {
    Hashtable accelTable = new Hashtable();
    accelTable.put("new",KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
    accelTable.put("open",KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    accelTable.put("reload",KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
    accelTable.put("save",KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    accelTable.put("save_as",KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
    accelTable.put("cut",KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    accelTable.put("copy",KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    accelTable.put("paste",KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    accelTable.put("add_to_presets",KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
    accelTable.put("add_dir_to_presets",KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
    accelTable.put("exit",KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
    accelTable.put("undo",KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    accelTable.put("redo",KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
    accelTable.put("select_all",KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
    accelTable.put("find",KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
    accelTable.put("find_next",KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
    accelTable.put("goto_line",KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
    accelTable.put("usage",KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
    return accelTable;
  }



  /**Loads the default preferencesinto prefTable*/

  public final void loadDefaultPrefTable(Hashtable prefTable) {
    prefTable.put("fontstyle","TimesRoman");
    prefTable.put("fontsize","12");
    prefTable.put("bgcolor","Cyan");
    prefTable.put("textcolor","Black");
    prefTable.put("width","580");
    prefTable.put("height","480");
    prefTable.put("lnf",UIManager.getInstalledLookAndFeels()[0].getName());

  }


       /**
  * Creates the search and find dialog.
  * @param il ItemListener for item events in the dialog
  * @param al ActionListener for item events in the dialog
  * @param searchField "Search" TextField.
  * @param replaceField "Replace" TextField.
  * @return The search and find JDialog
  */
  public final JDialog getSearchDialog(ItemListener il, ActionListener al, JTextField searchField, JTextField replaceField) {
      final JDialog jd = new JDialog(frimble.getOwnerFrame(), "Textpad - Find", false);
      JPanel fr = new JPanel();
      fr.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      fr.setLayout(new BoxLayout(fr,BoxLayout.Y_AXIS));

      KeyAdapter ka = new KeyAdapter(){
        public void keyPressed(KeyEvent e) {
          if(e.getKeyCode()==KeyEvent.VK_ESCAPE) jd.dispose();
      } };
      searchField.addActionListener(al);
      searchField.addKeyListener(ka);
      replaceField.addActionListener(al);
      replaceField.addKeyListener(ka);

      JPanel tp = new JPanel();
      tp.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      tp.setLayout(new GridLayout(4,1,0,5));
      JLabel ql = new JLabel("Find:",JLabel.LEFT);
      tp.add(ql);
      tp.add(searchField);
      ql = new JLabel("Replace with:",JLabel.LEFT);
      tp.add(ql);
      tp.add(replaceField);
      fr.add(tp);

      tp = new JPanel();
      JCheckBox matchcase = new JCheckBox("Match Case",false);
      matchcase.addItemListener(il);
      matchcase.addKeyListener(ka);
      tp.add(matchcase);
      fr.add(tp);


      tp = new JPanel();
      tp.setLayout(new GridLayout(1,3,6,2));
      final String[] b = {("Find Next"),("Replace"),("Restart")};
      final char[] m = {'F','R','E',};
                        for (int i=0; i<3; i++) {
        Panel ttp = new Panel();
        JButton t = new JButton(b[i]);
        t.setMnemonic(m[i]);
        t.addActionListener(al);
        t.addKeyListener(ka);
        tp.add(t);
      }
      fr.add(tp);

      tp = new JPanel();
      tp.setLayout(new GridLayout(1,1));
      ql = new JLabel("Replace:",JLabel.LEFT);
      tp.add(ql);
      fr.add(tp);

      final String[] b1 = {("Selection"),("All"),("Close")};
      final char[] m1 = {'S','A','C'};
      tp = new JPanel();
      tp.setLayout(new GridLayout(1,3,6,2));
      for (int i=0; i<3; i++) {
        JButton t = new JButton(b1[i]);
        t.setMnemonic(m1[i]);
        t.addActionListener(al);
        t.addKeyListener(ka);
              tp.add(t);
      }
      fr.add(tp);

      jd.getContentPane().add(fr);
      jd.pack();
      jd.setSize(380,264);
      jd.setLocationRelativeTo(frimble.getFrimbleContained());
      return jd;
  }


  /**
  * Shows the about message
  *  @param icon The Textpad icon
  */
  public void showAbout(Icon ic) {
    final JPanel aboutPanel=new JPanel();
    final Font f= new Font("Dialog",Font.BOLD,12);
    final Color c =new Color(0x60679F);
    aboutPanel.setLayout(new GridLayout(3,1));
    JPanel tp = new JPanel();
    JLabel infol = new JLabel("Textpad 1.1 by Dr.M.L.Noone");
    infol.setForeground(c);
    infol.setFont(f);
    tp.add(infol);
    aboutPanel.add(tp);
    tp=new JPanel();
    JTextField info=new JTextField(" mln@gjt.org ");
    info.setForeground(c);
    info.setFont(f);
    info.setEditable(false);
    tp.add(info);
    aboutPanel.add(tp);
    tp=new JPanel();
    info=new JTextField(" http://www.gjt.org/~mln/ ");
    info.setForeground(c);
    info.setFont(f);
    info.setEditable(false);
    tp.add(info);
    aboutPanel.add(tp);
    JOptionPane.showMessageDialog(frimble.getFrimbleContained(),
          aboutPanel,
          "Textpad - about",
          JOptionPane.INFORMATION_MESSAGE,
        ic);

  }

/** ButtonGroup for Font Style menu */
  private ButtonGroup fStyle = new ButtonGroup();

/** ButtonGroup for Font Size  menu */
  private ButtonGroup fSize = new ButtonGroup();

/** ButtonGroup for Background menu */
  private ButtonGroup bgCol = new ButtonGroup();

/** ButtonGroup for text menu */
  private ButtonGroup txCol = new ButtonGroup();

/** ButtonGroup for Background Look and Feel menu*/
  private ButtonGroup lnf = new ButtonGroup();

  /*
*
  * Gets the botton group for the radio button menu item identified by parent and cmd
  *
@param parent id of the parent menu.
  * @param cmd id of the command implemented by the button.

        */


/**
  * Gets the botton group for the radio button menu item identified by parent and cmd
  *
@param parent char identifying the parent menu.
  * @param cmd  char identifying the command implemented by the button.
  * @return the ButtonGroup containg the radio button.
  */
  public final ButtonGroup getButtonGroup(char parent, char cmd) {
    switch (parent) {
      case ('j') :
        if ( cmd < 'A' ) return fSize;
        else return fStyle;
      case ('b') : return bgCol;
      case ('t')  : return txCol;
      case ('l')  : return lnf;
      default    : return null;
    }
  }


/**
  * Selects the radio button menu item identified by parent and cmd
  *


@param parent id of the parent menu.
  * @param cmd id of the command implemented by the button.
  */
  public final void selectRadioItem(char parent,String cmd) {
    ButtonGroup bGp = getButtonGroup(parent,cmd.charAt(0));
    int i=0;
    for (java.util.Enumeration en = bGp.getElements();en.hasMoreElements();)  {
      AbstractButton jr = (AbstractButton) en.nextElement();
      if (jr.getText().equals(cmd)) {
        jr.setSelected(true);
        return;
      }
    }

  }

}
