/*
 * JPad.java Sat Jan 20 22:25:59 GMT+05:30 2001
 *
 * Software Designed and Developed by HariKrishna
 * Can be contacted at hari_8@yahoo.com
 *
 * The Author grants you ('Licensee') a non-exclusive, royalty free,
 * license to use, modify and redistribute this software in source and
 * binary code form, provided that this license notice appear on all
 * copies of this software.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR 'AS IS' WITHOUT A WARRANTY OF
 * ANY KIND. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE SOFTWARE
 * OR ITS DERIVATIVES. IN NO EVENT THE AUTHOR WILL BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.
 *
 * The Author encourages wide distribution of this software for personal or
 * commercial use, provided the above license notice remains intact.
 */

/*
 *
 * Convered to Jesktop by Paul Hammant, Feb 2001.
 *
 */


package net.jesktop.jpad;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.undo.*;
import org.jesktop.frimble.*;


public class JPad extends JPanel implements Runnable, FrimbleAware{

//---> member variables <-------------------
  private Color bakcolor,forcolor;
   private Container con;
   private ClassLoader cl;
   private static Cursor curdef = new Cursor(Cursor.DEFAULT_CURSOR),curwait = new Cursor(Cursor.WAIT_CURSOR);
   private static File fullpath,oldfullpath;
   private Font txtfont;
   private ImageIcon imgico,imgtip;
   private JCheckBox chksaveset,chkmachcase,chktip,chkwholeword;
   private JComboBox cmbfontname,cmbfontstyle,cmbfontsize,cmbtabs;
  private JDialog finddlg;
  private JEditorPane html;
   private JFrame frmhlp;
   private JLabel labbakcol,labforcol,labstatus;
   private JMenuBar menubar;
   private JMenu menufile,menuedit,menusearch,menuhelp;
   private JMenuItem item;
   private JOptionPane opt;
   private JPanel hlptotal;
   private JPopupMenu popmenu;
   private JProgressBar probar;
   private JRadioButton radjavalandf, radmotiflandf, radwinlandf, radmaclandf;
   private JScrollPane scrpane,scrhlppane;
   private JTextArea txtarea;
   private JTextField txtfind;
   private PropertyResourceBundle resrc;
   private static String fname,txtsaved = "",searchtxt;
   private static String fontnames[];
   private Thread thread,hintthread;
   private Toolkit tool;
   private UndoHandler undohand;
   private UndoManager undo;
   private Vector list;
   private static int optres,bakrgb,forrgb,optfindres,findpos,originalpos,htmlpos,tabsize;
   private boolean saveset = false,tip = true;
   private volatile boolean busy = false;
   private Frimble frimble;

//---> JPad Constructor with String argument <-------------------
   public JPad() {
   }

    private URL getResourceURL(String rscName) {
    java.net.URLClassLoader cl = (java.net.URLClassLoader) this.getClass().getClassLoader();
      return cl.findResource(rscName);
  }

  public void setFrimble(Frimble frimble){
      this.frimble = frimble;
      String str = "";
      con = this;
      con.setLayout(new BorderLayout());
      tool = getToolkit();
      imgico = new ImageIcon(getResourceURL("resources/JPad.jpg"));
      imgtip = new ImageIcon(getResourceURL("resources/JPadTip.jpg"));
      tabsize = 8;
      try {
    try {
          resrc = new PropertyResourceBundle(new FileInputStream("JPad.properties"));
      } catch (FileNotFoundException fnfe) {
        resrc = new PropertyResourceBundle(getResourceURL("JPad.properties").openStream());
    }
        txtfont = new Font(resrc.getString("FontFamily"),Integer.parseInt(resrc.getString("FontStyle")),Integer.parseInt(resrc.getString("FontSize")));
         bakrgb = Integer.parseInt(resrc.getString("BackgroundColor"));
         forrgb = Integer.parseInt(resrc.getString("ForegroundColor"));
          String ui = resrc.getString("LookAndFeel").trim();
          if(ui.equals("CDE/Motif"))
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
         else if(ui.equals("Windows"))
             UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
         else if(ui.equals("Macintosh"))
             UIManager.setLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel");
         else
             UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        tabsize = Integer.parseInt(resrc.getString("TabSize"));
        tip = new Boolean(resrc.getString("ShowTips")).booleanValue();
       saveset = true;
       } catch(MissingResourceException exp) {
          System.err.println("Unable to set the custom seetings\n" + exp);
          tool.beep();
          JOptionPane.showMessageDialog(JPad.this,"Unable to read the saved JPad custom settings","JPad",JOptionPane.INFORMATION_MESSAGE);
       } catch(Exception exp) {
          System.err.println("Unable to set the Look and feel seetings\n" + exp);
          tool.beep();
          JOptionPane.showMessageDialog(JPad.this,"Unable to read the saved JPad custom settings","JPad",JOptionPane.INFORMATION_MESSAGE);
       }

//---> Start Menu bar <-------------------
      menubar = new JMenuBar();

      //---> Start File Menu <-------------------
      menufile = new JMenu("File");
      menufile.setMnemonic('F');
      item = new JMenuItem("New",'N');
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            fileNew();
         }
      });
      menufile.add(item);
      item = new JMenuItem("Open...",'O');
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            fileOpen();
         }
      });
      menufile.add(item);
      item = new JMenuItem("Save",'S');
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            fileSave();
         }
      });
      menufile.add(item);
      item = new JMenuItem("Save As...",'A');
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            fileSaveAs();
         }
      });
      menufile.add(item);
      menufile.addSeparator();
      item = new JMenuItem("Print...",'P');
      item.setEnabled(false);
    item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            filePrint();
            return;
         }
      });
      menufile.add(item);
      menufile.addSeparator();
      item = new JMenuItem("Exit",'x');
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            fileExit();
         }
      });
      menufile.add(item);
      menubar.add(menufile);

      //---> Start Edit Menu <-------------------
      menuedit = new JMenu("Edit");
      menuedit.setMnemonic('E');
      item = new JMenuItem("Undo",'U');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            try {
              if(undo.canUndo())
                undo.undo();
            }catch(Exception e) {
            System.err.println(e);
           }
            return;
         }
      });
      menuedit.add(item);
      item = new JMenuItem("Redo",'R');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            try {
              if(undo.canRedo())
                undo.redo();
            }catch(Exception e) {
            System.err.println(e);
           }
            return;
         }
      });
      menuedit.add(item);
      menuedit.addSeparator();
      item = new JMenuItem("Cut",'t');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.cut();
            return;
         }
      });
      menuedit.add(item);
      item = new JMenuItem("Copy",'C');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.copy();
            return;
         }
      });
      menuedit.add(item);
      item = new JMenuItem("Paste",'P');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.paste();
            return;
         }
      });
      menuedit.add(item);
      item = new JMenuItem("Delete",'D');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
      item.setEnabled(false);
      menuedit.add(item);
      menuedit.addSeparator();
      item = new JMenuItem("Select All",'S');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.selectAll();
            return;
         }
      });
      menuedit.add(item);
      item = new JMenuItem("Time/Date",'i');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            int pos = txtarea.getCaretPosition();
            String bef = txtarea.getText().substring(0,pos);
            bef += new Date();
            bef += txtarea.getText().substring(pos);
            txtarea.setText(bef);
            txtarea.setCaretPosition(pos+34);
            return;
         }
      });
      menuedit.add(item);
      menuedit.addSeparator();
      final JCheckBoxMenuItem radmenu = new JCheckBoxMenuItem("Word wrap",true);
      radmenu.setMnemonic('W');
      radmenu.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            if(radmenu.isSelected())
               txtarea.setLineWrap(true);
            else
               txtarea.setLineWrap(false);
            return;
         }
      });
      menuedit.add(radmenu);
      item = new JMenuItem("Options...",'O');
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            editOptions();
            return;
         }
      });
      menuedit.add(item);
      menubar.add(menuedit);

      //---> Start Search Menu <-------------------
      menusearch = new JMenu("Search");
      menusearch.setMnemonic('S');
      item = new JMenuItem("Find...",'F');
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            searchFind();
            return;
         }
      });
      menusearch.add(item);
      item = new JMenuItem("Find Next",'N');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,0));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            if(searchtxt != null)
               findString();
            else
               searchFind();
            return;
         }
      });
      menusearch.add(item);
      menubar.add(menusearch);

      //---> Start Help Menu <-------------------
      menuhelp = new JMenu("Help");
      menuhelp.setMnemonic('H');
      item = new JMenuItem("Help Topics",'H');
      item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            helpTopics();
            return;
         }
      });
      menuhelp.add(item);
      menuhelp.addSeparator();
      item = new JMenuItem("About JPad",'A');
      menuhelp.add(item);
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
          busy = true;
          JPad.this.setCursor(curwait);
           labstatus.setText("e-mail: hari_8@yahoo.com");
            String s = "<html><font FACE = \"Verdana, Arial, Sans-Serif\" size=5 color=#F97022><b>JPad</b></font> v1.0</html>";
            String s1 = "<html><font FACE = \"Verdana, Arial, Sans-Serif\" size=-1>The cross - platform ASCII editor completely written using Java(TM)2.</font></html>";
            String s2 = "<html><font FACE = \"Verdana, Arial, Sans-Serif\" size=-1>Software Designed and Developed by HariKrishna.</font></html>";
            String s3 = "<html><font FACE = \"Verdana, Arial, Sans-Serif\" size=-1>For more information e-mail: <b>hari_8@yahoo.com</b></font></html>";
            String s4 = "<html><font FACE = \"Verdana, Arial, Sans-Serif\" size=-1>This program is a freeware and may be redistributed as such.</font></html>";
            JPanel panabout = new JPanel(new GridLayout(0,1));
            JLabel lababout = new JLabel(s1);
            panabout.add(lababout);
            lababout = new JLabel(s2);
            panabout.add(lababout);
            lababout = new JLabel(s3);
            panabout.add(lababout);
            lababout = new JLabel(s4);
            panabout.add(lababout);
            JOptionPane optabout = new JOptionPane();
            optabout.setOptionType(JOptionPane.DEFAULT_OPTION);
            optabout.setIcon(imgico);
            optabout.setMessage(s);
            optabout.add(panabout,1);
            JDialog optaboutdlg = optabout.createDialog(JPad.this,"About JPad");
            optaboutdlg.setResizable(false);
            JPad.this.setCursor(curdef);
            busy = false;
            optaboutdlg.show();
            labstatus.setText("Ready");
            return;
         }
      });
      menubar.add(menuhelp);
      //---> Menu Bar complete <-------------------

//---> Start Creating TextArea <-------------------
      txtarea = new JTextArea();
      txtarea.setLineWrap(true);
      txtarea.setWrapStyleWord(true);
      createPopMenu();
      if(saveset == true) {
         txtarea.setBackground(new Color(bakrgb));
         txtarea.setForeground(new Color(forrgb));
         txtarea.setCaretColor(new Color(forrgb));
         txtarea.setFont(txtfont);
      }
      else {
        txtarea.setBackground(new Color(-6710785));
        txtarea.setForeground(new Color(-1));
        txtarea.setCaretColor(new Color(-1));
     }
     txtarea.setTabSize(tabsize);
      txtarea.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent me) {
          if(!busy)
            JPad.this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
         else
          JPad.this.setCursor(curwait);
          return;
        }
        public void mouseExited(MouseEvent me) {
          JPad.this.setCursor(curdef);
          return;
        }
        public void mousePressed(MouseEvent me) {
          if(me.isPopupTrigger())
            popmenu.show(txtarea,me.getX(),me.getY());
          return;
       }
       public void mouseReleased(MouseEvent me) {
        if(me.isPopupTrigger())
            popmenu.show(txtarea,me.getX(),me.getY());
          return;
       }
    });
    txtarea.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent me) {
        if(!busy)
            JPad.this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
         else
          JPad.this.setCursor(curwait);
        return;
      }
    });
      scrpane = new JScrollPane(txtarea);
      scrpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
      JPanel pantmp = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
      labstatus = new JLabel("Welcome to JPad",JLabel.LEFT);
      pantmp.add(labstatus);
      probar = new JProgressBar(0,100);
      probar.setVisible(false);
      pantmp.add(probar);
      con.add(scrpane,BorderLayout.CENTER);
      con.add(pantmp,BorderLayout.SOUTH);
      frimble.pack();

//---> Start Frame <-------------------
      frimble.setIconImage(imgico.getImage());
      fname = "Untitled";
      setCursor(curdef);
      frimble.setTitle(fname + " - JPad");
      frimble.setJMenuBar(menubar);
      setSize(640,480);
      setLocation((tool.getScreenSize().width - getSize().width) / 2, (tool.getScreenSize().height- getSize().height)/ 2);
      frimble.setVisible(true);
    thread = new Thread(this);
    undo = new UndoManager();
    undohand = new UndoHandler();
      txtarea.getDocument().addUndoableEditListener(undohand);
      setFile(str);
      frimble.addFrimbleListener(new FrimbleAdapter() {
         public void frimbleClosing(FrimbleEvent fe) {
            fileExit();
            return;
         }
      });
   }

//---> end of setFrimble <-------------------

   private void setFile(String str) {
      if(str.length() == 0)
         fileNew();
      else {
         fullpath = new File(str);
         thread.start();
      }
      fontnames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
      HintThread hthread = new HintThread();


   }


//---> createPopMenu Method <--------------
  protected void createPopMenu() {
    popmenu = new JPopupMenu();
      item = new JMenuItem("Undo");
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            try {
              if(undo.canUndo())
                undo.undo();
           }catch(Exception e) {
            System.err.println(e);
           }
            return;
         }
      });
      popmenu.add(item);
      popmenu.addSeparator();
      item = new JMenuItem("Cut");
     item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.cut();
            return;
         }
      });
      popmenu.add(item);
      item = new JMenuItem("Copy");
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.copy();
            return;
         }
      });
      popmenu.add(item);
      item = new JMenuItem("Paste");
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.paste();
            return;
         }
      });
      popmenu.add(item);
      item = new JMenuItem("Select All");
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            txtarea.selectAll();
            return;
         }
      });
      popmenu.add(item);
      popmenu.addSeparator();
      item = new JMenuItem("Options...");
      item.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            editOptions();
            return;
         }
      });
      popmenu.add(item);
      popmenu.pack();
      popmenu.setVisible(false);
      popmenu.setBorderPainted(true);
    return;
  }

//---> implementing the run Method <------
   public void run() {
      probar.setValue(0);
     if(!(busy = checkFile()))
      return;
      JPad.this.setCursor(curwait);
      try {
        BufferedReader br = new BufferedReader(new FileReader(fullpath));
        txtarea.setText("");
        labstatus.setText("Reading...");
        probar.setVisible(true);
        int val = 0,len = (int)fullpath.length();
        StringBuffer sb = new StringBuffer(len);
        String var = "";
        if((var = br.readLine()) != null) {
        sb.append(var);
          val += var.length();
          probar.setValue((val*100)/len);
          while((var = br.readLine()) != null) {
          sb.append("\n" + var);
          val += var.length();
          probar.setValue((val*100)/len);
          }
       }
        br.close();
        txtarea.getDocument().insertString(0,sb.toString(),null);
        txtsaved = txtsaved.intern();
        txtsaved = txtarea.getText();
        txtarea.setCaretPosition(0);
        fname = fullpath.getName();
        if(!fname.startsWith(".") && fname.lastIndexOf('.') != -1)
          fname = fname.substring(0,fname.lastIndexOf('.'));
        if(!fullpath.canWrite()) {
          fname += " [read-only]";
          labstatus.setText("Ready [read-only]");
       }
       else
        labstatus.setText("Ready");
      frimble.setTitle(fname + " - JPad");
      undo.discardAllEdits();
    }catch(FileNotFoundException fnfe) {
      thread.yield();
        tool.beep();
        System.err.println(fnfe);
        JOptionPane.showMessageDialog(JPad.this,fullpath.getAbsolutePath() + " cannot access.\nThe file may be used by another process.","JPad",JOptionPane.WARNING_MESSAGE);
      }catch(IOException ioe) {
        thread.yield();
        tool.beep();
        System.err.println(ioe);
        JOptionPane.showMessageDialog(JPad.this,"Unable to read from the specified drive.","JPad",JOptionPane.WARNING_MESSAGE);
      }catch(Exception exp) {
        thread.yield();
        tool.beep();
        System.err.println(exp);
        JOptionPane.showMessageDialog(JPad.this,exp,"JPad",JOptionPane.WARNING_MESSAGE);
      }
      probar.setVisible(false);
      undo.discardAllEdits();
      JPad.this.setCursor(curdef);
      busy = false;
      return;
   }

//---> checkFile Method <-----------------
  protected boolean checkFile() {
    if(!fullpath.exists()) {
        tool.beep();
        JOptionPane.showMessageDialog(JPad.this,fullpath.getAbsolutePath() + " not found.","JPad",JOptionPane.INFORMATION_MESSAGE);
     }
      else if(!fullpath.canRead()) {
        tool.beep();
        JOptionPane.showMessageDialog(JPad.this,"Access to " + fullpath.getAbsolutePath() + " denied.\nPlease check the file read permissions.","JPad",JOptionPane.INFORMATION_MESSAGE);
      }
      else if(fullpath.length() > 262144) {
        tool.beep();
        JOptionPane.showMessageDialog(JPad.this,fullpath.getName() + " is too large to open with JPad.","JPad",JOptionPane.INFORMATION_MESSAGE);
      }
      else
      return true;
    fullpath = oldfullpath;
     return false;
  }

//---> fileNew Method <-------------------
   protected void fileNew() {
      if(!txtarea.getText().equals(txtsaved)) {
        tool.beep();
         int res = JOptionPane.showConfirmDialog(JPad.this,"The text in the " + fname + " file has changed.\nDo want to save the changes?","JPad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
         if(res == JOptionPane.CANCEL_OPTION)
            return;
         else if(res == JOptionPane.YES_OPTION)
            fileSave();
      }
      undo.discardAllEdits();
      fname = "Untitled";
      fullpath = null;
      frimble.setTitle(fname + " - JPad");
      txtarea.setText("");
      txtsaved = "";
      return;
   }

//---> fileOpen Method <-------------------
   protected void fileOpen() {
      if(!txtarea.getText().equals(txtsaved)) {
        tool.beep();
         int res = JOptionPane.showConfirmDialog(JPad.this,"The text in the " + fname + " file has changed.\nDo want to save the changes?","JPad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
         if(res == JOptionPane.CANCEL_OPTION)
            return;
         else if(res == JOptionPane.YES_OPTION)
            fileSave();
      }
      busy = true;
      JPad.this.setCursor(curwait);
      labstatus.setText("Open file");
      JFileChooser choose = new JFileChooser();
      JPadFileView fileview = new JPadFileView(cl);
      choose.setFileView(fileview);
      JPadFileFilter ff = new JPadFileFilter(new String []{"jxt","txt"}, "All Text Files");
      choose.addChoosableFileFilter(ff);
      choose.setFileFilter(ff);
      if(fullpath != null)
        choose.setCurrentDirectory(fullpath);
     else
      choose.setCurrentDirectory(new File(System.getProperty("user.dir")));
      int res = choose.showOpenDialog(this);
      choose.setVisible(false);
      if(res == JFileChooser.APPROVE_OPTION) {
        oldfullpath = fullpath;
         fullpath = choose.getSelectedFile();
         thread = new Thread(this);
        thread.start();
      }
      else {
        labstatus.setText("Ready");
        JPad.this.setCursor(curdef);
        busy = false;
     }
      return;
   }

//---> fileSave Method <-------------------
   protected void fileSave() {
      if(fullpath == null) {
         fileSaveAs();
         return;
      }
      if(!fullpath.canWrite()) {
        tool.beep();
        JOptionPane.showMessageDialog(JPad.this,"Access to " + fullpath.getAbsolutePath() + " denied.\nPlease check the file write permissions.","JPad",JOptionPane.INFORMATION_MESSAGE);
        return;
     }
     if(txtarea.getText().equals(txtsaved))
      return;
     busy = true;
      JPad.this.setCursor(curwait);
      labstatus.setText("Saving...");
      try {
        FileOutputStream fos = new FileOutputStream(fullpath);
        txtsaved = txtarea.getText();
        fos.write(txtsaved.getBytes());
        fos.close();
     }catch(FileNotFoundException fnfe) {
        tool.beep();
        System.err.println(fnfe);
        JOptionPane.showMessageDialog(JPad.this,fullpath.getAbsolutePath() + " cannot access.\nThe file may be used by another process.","JPad",JOptionPane.WARNING_MESSAGE);
      }catch(IOException ioe) {
        tool.beep();
        System.err.println(ioe);
        JOptionPane.showMessageDialog(JPad.this,"Unable to write to the specified drive.","JPad",JOptionPane.WARNING_MESSAGE);
      }catch(Exception exp) {
        tool.beep();
        System.err.println(exp);
        JOptionPane.showMessageDialog(JPad.this,exp,"JPad",JOptionPane.WARNING_MESSAGE);
      }
      labstatus.setText("Ready");
      labstatus.setIcon(null);
      JPad.this.setCursor(curdef);
      busy = false;
      return;
   }

//---> fileSaveAs Method <-------------------
   protected void fileSaveAs() {
    busy = true;
    JPad.this.setCursor(curwait);
      JFileChooser choose = new JFileChooser();
      JPadFileView fileview = new JPadFileView(cl);
      choose.setFileView(fileview);
      JPadFileFilter ff = new JPadFileFilter(new String("jxt"), "Text File");
      choose.addChoosableFileFilter(ff);
      choose.setFileFilter(ff);
      if(fullpath != null)
        choose.setCurrentDirectory(fullpath);
     else
      choose.setCurrentDirectory(new File(System.getProperty("user.dir")));
      choose.setApproveButtonMnemonic('S');
      choose.setApproveButtonToolTipText("Save the selected file");
      labstatus.setText("Save As file");
      int res = choose.showDialog(this,"Save As");
      if(res == JFileChooser.APPROVE_OPTION) {
        oldfullpath = fullpath;
         fullpath = choose.getSelectedFile();
      }
      else {
        labstatus.setText("Ready");
        labstatus.setIcon(null);
        JPad.this.setCursor(curdef);
        busy = false;
         return;
      }
      fname = fullpath.getName();
      if(!fname.startsWith(".") && fname.lastIndexOf('.') != -1)
        fname = fname.substring(0,fname.lastIndexOf('.'));
     else if(!fname.startsWith("."))
         fullpath = new File(fullpath.getAbsolutePath() + ".jxt");
      try {
        if(fullpath.exists()) {
          tool.beep();
           res = JOptionPane.showConfirmDialog(JPad.this,fullpath.getAbsolutePath() + " already exists.\nDo want to replace it?","JPad",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
           if(res == JOptionPane.NO_OPTION) {
            fullpath = oldfullpath;
             labstatus.setText("Ready");
            JPad.this.setCursor(curdef);
          busy = false;
              return;
           }
       }
       else
        fullpath.createNewFile();
      if(!fullpath.canWrite()) {
        tool.beep();
          JOptionPane.showMessageDialog(JPad.this,"Access to " + fullpath.getAbsolutePath() + " denied.\nPlease check the file permissons.","JPad",JOptionPane.INFORMATION_MESSAGE);
            fullpath = oldfullpath;
       }
       else {
         labstatus.setText("Saving...");
          FileOutputStream fos = new FileOutputStream(fullpath.getAbsolutePath());
          txtsaved = txtarea.getText();
        fos.write(txtsaved.getBytes());
          fos.close();
          frimble.setTitle(fname + " - JPad");
        }
      }catch(FileNotFoundException fnfe) {
        tool.beep();
        System.err.println(fnfe);
        JOptionPane.showMessageDialog(JPad.this,fullpath.getAbsolutePath() + " cannot access.\nThe file may be used by another process.","JPad",JOptionPane.WARNING_MESSAGE);
      }catch(IOException ioe) {
        tool.beep();
        System.err.println(ioe);
        JOptionPane.showMessageDialog(JPad.this,"Unable to write to the specified drive.","JPad",JOptionPane.WARNING_MESSAGE);
      }catch(Exception exp) {
        tool.beep();
        System.err.println(exp);
        JOptionPane.showMessageDialog(JPad.this,exp,"JPad",JOptionPane.WARNING_MESSAGE);
      }
      oldfullpath = fullpath;
      labstatus.setText("Ready");
      JPad.this.setCursor(curdef);
      busy = false;
      return;
   }

//---> filePrint Method <-------------------
   protected void filePrint() {
    try {
      PrinterJob pjob = PrinterJob.getPrinterJob();
       pjob.printDialog();
       pjob.print();
     }catch(Exception exp) {}
      return;
   }

//---> fileExit Method <-------------------
   protected void fileExit() {
      if(thread.isAlive() == false && !txtarea.getText().equals(txtsaved)) {
        tool.beep();
         int res = JOptionPane.showConfirmDialog(JPad.this,"The text in the " + fname + " file has changed.\nDo want to save the changes?","JPad",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
         if(res == JOptionPane.YES_OPTION)
            fileSave();
      }
      frimble.dispose();
      return;
   }

//---> editOptions Method <-------------------
   protected void editOptions() {

    busy = true;
    JPad.this.setCursor(curwait);
    labstatus.setText("Options");
    TitledBorder titled = null;
    Font thisfont = this.getFont();
    //---> Starting the Font Panel <----------
    JPanel panfontleft = new JPanel(new GridLayout(0,1,5,5));
    JLabel labfont = new JLabel("Font Family");
    panfontleft.add(labfont);
    cmbfontname = new JComboBox(fontnames);
    cmbfontname.setSelectedItem(txtarea.getFont().getName());
    panfontleft.add(cmbfontname);
    JPanel panfontright = new JPanel(new GridLayout(0,2,5,5));
    labfont = new JLabel("Font Style");
    panfontright.add(labfont);
    labfont = new JLabel("Font Size");
    panfontright.add(labfont);
    cmbfontstyle = new JComboBox(new String[]{"Regular","Bold","Italic","Bold Italic"});
    cmbfontstyle.setSelectedIndex(txtarea.getFont().getStyle());
    panfontright.add(cmbfontstyle);
    cmbfontsize = new JComboBox();
    for(int i=8;i<25;i+=2)
      cmbfontsize.addItem(String.valueOf(i));
    for(int i=28;i<65;i+=4)
      cmbfontsize.addItem(String.valueOf(i));
    cmbfontsize.addItem(String.valueOf(72));
    cmbfontsize.setEditable(true);
    cmbfontsize.setSelectedItem(String.valueOf(txtarea.getFont().getSize()));
    panfontright.add(cmbfontsize);
    JPanel panfont = new JPanel(new BorderLayout());
    panfont.add(panfontleft,BorderLayout.WEST);
    panfont.add(panfontright,BorderLayout.EAST);
    titled = new TitledBorder(" Font ");
    titled.setTitleFont(new Font(thisfont.getName(),Font.BOLD,thisfont.getSize()));
    panfont.setBorder(titled);

      //---> Starting the Color Panel <----------
      JPanel pancolor = new JPanel();
      labbakcol = new JLabel("Background",new ColoredSquare(txtarea.getBackground()),JLabel.RIGHT);
      JButton butbakchange = new JButton("Change...");
      butbakchange.setMnemonic('B');
      butbakchange.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            getBackColor();
            return;
         }
      });
      labforcol = new JLabel("Foreground",new ColoredSquare(txtarea.getForeground()),JLabel.RIGHT);
      JButton butforchange = new JButton("Change...");
      butforchange.setMnemonic('R');
      butforchange.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            getForeColor();
            return;
         }
      });
      pancolor.add(labbakcol);
      pancolor.add(butbakchange);
      pancolor.add(labforcol);
      pancolor.add(butforchange);
      titled = new TitledBorder(" Colors ");
    titled.setTitleFont(new Font(thisfont.getName(),Font.BOLD,thisfont.getSize()));
      pancolor.setBorder(titled);

      //---> Starting the Look And Feel Panel <----------
      JPanel panlandf = new JPanel(new GridLayout(0,2));
      ButtonGroup grplandf = new ButtonGroup();
      radjavalandf = new JRadioButton("Java Style Look and Feel",true);
      radjavalandf.setMnemonic('J');
      grplandf.add(radjavalandf);
      radmotiflandf = new JRadioButton("Motif Style Look and Feel");
      radmotiflandf.setMnemonic('M');
      grplandf.add(radmotiflandf);
      radwinlandf = new JRadioButton("Windows Style Look and Feel");
      radwinlandf.setMnemonic('W');
      grplandf.add(radwinlandf);
      radmaclandf = new JRadioButton("Macintosh Style Look and Feel");
      radmaclandf.setMnemonic('A');
      radmaclandf.setEnabled(false);
      grplandf.add(radmaclandf);
      panlandf.add(radjavalandf);
      panlandf.add(radmaclandf);
      panlandf.add(radmotiflandf);
      panlandf.add(radwinlandf);
      if(UIManager.getLookAndFeel().getName().equals("CDE/Motif"))
         radmotiflandf.setSelected(true);
      else if(UIManager.getLookAndFeel().getName().equals("Windows"))
         radwinlandf.setSelected(true);
      else if(UIManager.getLookAndFeel().getName().equals("Macintosh"))
         radmaclandf.setSelected(true);
      else
         radjavalandf.setSelected(true);
      titled = new TitledBorder(" Look and Feel ");
    titled.setTitleFont(new Font(thisfont.getName(),Font.BOLD,thisfont.getSize()));
      panlandf.setBorder(titled);

    //---> Starting the Tabs And Tips Panel <----------
      JPanel pantip = new JPanel(new GridLayout(0,1));
      JPanel pantmp = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
      labfont = new JLabel("Tab Size");
      pantmp.add(labfont);
      String[] tab = new String[32];
      for(int i=0;i<32;i++)
        tab[i] = String.valueOf(i+1);
      cmbtabs = new JComboBox(tab);
      cmbtabs.setSelectedIndex(txtarea.getTabSize() - 1);
      pantmp.add(cmbtabs);
      pantip.add(pantmp);
      chktip = new JCheckBox("Don't show tips on status bar next time",!tip);
      chktip.setMnemonic('D');
      pantip.add(chktip);
      titled = new TitledBorder(" Tabs And Tips ");
    titled.setTitleFont(new Font(thisfont.getName(),Font.BOLD,thisfont.getSize()));
      pantip.setBorder(titled);

      JPanel pansaveset = new JPanel(new GridLayout(0,1));
      chksaveset = new JCheckBox("Save these settings",false);
      chksaveset.setMnemonic('S');
      pansaveset.add(chksaveset);

      //---> Constructing the Options Dialog Box <----------
      opt = new JOptionPane();
      opt.setOptionType(JOptionPane.OK_CANCEL_OPTION);
      opt.setMessage("");
      opt.add(panfont,1);
      opt.add(pancolor,2);
      opt.add(panlandf,3);
      opt.add(pantip,4);
      opt.add(pansaveset,5);
      JDialog optdlg = opt.createDialog(JPad.this,"Options");
      optdlg.setResizable(false);
      JPad.this.setCursor(curdef);
      busy = false;
      optdlg.show();

      //---> Processing the Options Dialog OK Event <----------
      optdlg.addWindowListener(new WindowAdapter() {
         public void windowClosed(WindowEvent we) {
            try {
              optres = Integer.parseInt(String.valueOf(opt.getValue()));
            } catch(Exception exp) {}
            setOptions();
            return;
         }
      });
      return;
   }

//---> Set the Options <---------------
   protected void setOptions() {

         if(optres == JOptionPane.CANCEL_OPTION) {
          labstatus.setText("Ready");
            return;
         }
      //---> Setting the Look And Feel <-----------
      busy = true;
      JPad.this.setCursor(curwait);
         try {
           if(radmotiflandf.isSelected())
              UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
           else if(radwinlandf.isSelected())
              UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
           else if(radmaclandf.isSelected())
              UIManager.setLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel");
           else
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
         } catch(Exception exp) {
            System.err.println("Unable to Change the Look and Feel " + exp);
            JOptionPane.showMessageDialog(JPad.this,"Unable to change Look and Feel.\n" + exp,"JPad",JOptionPane.INFORMATION_MESSAGE);
         }
        //---> Setting the Colors <-----------
         if(bakcolor != null)
            txtarea.setBackground(bakcolor);
         if(forcolor != null) {
            txtarea.setForeground(forcolor);
            txtarea.setCaretColor(forcolor);
         }
      //---> Setting the Font <-----------
      String fontname = fontnames[cmbfontname.getSelectedIndex()];
      int fontstyle = cmbfontstyle.getSelectedIndex();
      int fontsize = 12;
      try {
        fontsize = Integer.parseInt((String)cmbfontsize.getSelectedItem());
      }catch(Exception exp) {}
      txtarea.setFont(new Font(fontname,fontstyle,fontsize));

      //---> Setting the TabSize <-----------
      tabsize = cmbtabs.getSelectedIndex() + 1;
      txtarea.setTabSize(tabsize);

         SwingUtilities.updateComponentTreeUI(JPad.this);
         SwingUtilities.updateComponentTreeUI(popmenu);
         if(frmhlp != null)
           SwingUtilities.updateComponentTreeUI(frmhlp);
         if(chksaveset.isSelected()) {
            try {
              PrintWriter pw = new PrintWriter(new FileOutputStream("JPad.properties"),true);
              pw.println("# JPad.properties");
              pw.println("#");
              pw.println("# Resource strings for JPad");
              pw.println("");
              pw.println("# Font Style");
              pw.println("FontFamily=" + fontname);
              pw.println("FontStyle=" + fontstyle);
              pw.println("FontSize=" + fontsize);
              pw.println("");
              pw.println("# Colors");
              pw.println("BackgroundColor=" + txtarea.getBackground().getRGB());
              pw.println("ForegroundColor=" + txtarea.getForeground().getRGB());
              pw.println("");
              pw.println("# Look and Feel");
              pw.println("LookAndFeel=" + UIManager.getLookAndFeel().getName());
              pw.println("");
              pw.println("# Tab Size");
              pw.println("TabSize=" + tabsize);
              pw.println("");
              pw.println("# Tips");
              pw.println("ShowTips=" + !chktip.isSelected());
              pw.close();
            } catch(Exception exp) {
              tool.beep();
               System.err.println("Unable to save the settings\n" + exp);
               JOptionPane.showMessageDialog(JPad.this,"Unable to save the settings.","JPad",JOptionPane.INFORMATION_MESSAGE);
            }
         }
         labstatus.setText("Ready");
         JPad.this.setCursor(curdef);
         busy = false;
         return;
   }

//---> getBackColor Method <------------
   protected void getBackColor() {
      bakcolor = JColorChooser.showDialog(this,"Choose Background Color",txtarea.getBackground());
      if(bakcolor != null)
         labbakcol.setIcon(new ColoredSquare(bakcolor));
      return;
   }

//---> getForeColor Method <------------
   protected void getForeColor() {
      forcolor = JColorChooser.showDialog(this,"Choose Foreground Color",txtarea.getForeground());
      if(forcolor != null)
         labforcol.setIcon(new ColoredSquare(forcolor));
      return;
   }

//---> runwin Method <---------------
  protected void runWin() {

    return;
  }

//---> searchFind Method <------------
   protected void searchFind() {

    busy = true;
    JPad.this.setCursor(curwait);
      //---> Starting the Find Panel <----------
      JPanel panfind = new JPanel(new GridLayout(0,1));
      if(txtarea.getSelectedText() != null)
         searchtxt = txtarea.getSelectedText();
      txtfind = new JTextField(searchtxt,10);
      txtfind.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0));
      txtfind.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent ke) {
          if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
          if(txtfind.getText().equals(""))
            return;
          searchtxt = txtfind.getText();
              findpos = txtarea.getCaretPosition();
             originalpos = txtarea.getCaretPosition();
              findString();
           }
          return;
       }
    });
      chkwholeword = new JCheckBox("Match Whole word only",false);
      chkwholeword.setMnemonic('W');
      chkmachcase = new JCheckBox("Match Case",false);
      chkmachcase.setMnemonic('C');
      panfind.add(txtfind);
      panfind.add(chkwholeword);
      panfind.add(chkmachcase);
      JPanel panfindbut = new JPanel(new GridLayout(0,1,5,5));
      JButton butfind = new JButton("Find Next");
      butfind.setMnemonic('N');
      butfind.setDefaultCapable(true);
      butfind.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          if(txtfind.getText().equals(""))
          return;
            searchtxt = txtfind.getText();
            findpos = txtarea.getCaretPosition();
            originalpos = txtarea.getCaretPosition();
            findString();
          return;
       }
    });
    butfind.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
          if(txtfind.getText().equals(""))
            return;
          searchtxt = txtfind.getText();
              findpos = txtarea.getCaretPosition();
             originalpos = txtarea.getCaretPosition();
              findString();
           }
        return;
      }
    });
    panfindbut.add(butfind);
    JButton butfindcancel = new JButton("Cancel");
    butfindcancel.setMnemonic(KeyEvent.VK_ESCAPE);
      butfindcancel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          finddlgClose();
          return;
       }
    });
    butfindcancel.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent ke) {
        if(ke.getKeyCode() == KeyEvent.VK_ENTER)
          finddlgClose();
        return;
      }
    });
    panfindbut.add(butfindcancel);
    JPanel panfindtot = new JPanel();
    panfindtot.add(panfind);
    panfindtot.add(panfindbut);
    TitledBorder titled = new TitledBorder(" Find What ");
    Font thisfont = this.getFont();
    titled.setTitleFont(new Font(thisfont.getName(),Font.BOLD,thisfont.getSize()));
      panfindtot.setBorder(titled);

   //---> Constructing the Find Dialog Box <----------
      finddlg = new JDialog(frimble.getOwnerFrame(), "Find", false);
      finddlg.setResizable(false);
      finddlg.setModal(false);
      Container findcon = finddlg.getContentPane();
      findcon.setLayout(new BorderLayout());
      findcon.add(panfindtot,BorderLayout.CENTER);
      finddlg.pack();
      Dimension me = finddlg.getSize();
      Dimension screen = finddlg.getToolkit().getScreenSize();
      int newX = (screen.width - me.width) / 2;
      int newY = (screen.height- me.height)/ 2;
      finddlg.setLocation(newX, newY);
      finddlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        finddlgClose();
        return;
      }
    });
    finddlg.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent ke) {
          if(ke.getKeyCode() == ke.VK_ESCAPE)
            finddlg.dispose();
         return;
      }
      });
      JPad.this.setCursor(curdef);
      busy = false;
     finddlg.show();
   }
//---> finddlgClose Method <----------
  protected void finddlgClose() {
    finddlg.hide();
    finddlg.dispose();
    return;
  }
//---> findString Method <------------
   protected void findString() {
      String txt = txtarea.getText();
      if(searchtxt.length() == 1 && chkmachcase.isSelected()) {
        for(int i=findpos;i<=txt.length() - 2;i++) {
            if(txt.charAt(i) == searchtxt.charAt(0)) {
               txtarea.setCaretPosition(i);
               txtarea.setSelectionStart(i);
               txtarea.setSelectionEnd(i+searchtxt.length());
               findpos = i+searchtxt.length();
            return;
            }
         }
      }
      else if(searchtxt.length() == 1) {
        char chs = searchtxt.charAt(0);
        if(chs >= 'a' && chs<= 'z')
          chs -= 32;
        for(int i=findpos;i<=txt.length() - 2;i++) {
          char cht = txt.charAt(i);
          if(cht >= 'a' && cht<= 'z')
            cht -= 32;
            if(cht == chs) {
               txtarea.setCaretPosition(i);
               txtarea.setSelectionStart(i);
               txtarea.setSelectionEnd(i+searchtxt.length());
               findpos = i+searchtxt.length();
            return;
            }
         }
      }
      else {
        try {
          if(chkwholeword.isSelected() && chkmachcase.isSelected()) {
            int pos = txt.indexOf(" " + searchtxt + " ",findpos);
            if(pos == -1)
              pos = txt.indexOf(" " + searchtxt + "\t",findpos);
            if(pos == -1)
              pos = txt.indexOf(" " + searchtxt + "\n",findpos);
           if(pos == -1)
              pos = txt.indexOf("\t" + searchtxt + " ",findpos);
           if(pos == -1)
              pos = txt.indexOf("\t" + searchtxt + "\t",findpos);
           if(pos == -1)
              pos = txt.indexOf("\t" + searchtxt + "\n",findpos);
             if(pos != -1) {
              pos += 1;
              txtarea.setCaretPosition(pos);
                txtarea.setSelectionStart(pos);
                txtarea.setSelectionEnd(pos+searchtxt.length());
                findpos = pos+searchtxt.length();
                return;
             }
          }
          else if(chkwholeword.isSelected() && !chkmachcase.isSelected()) {
            for(int i=findpos;i<=(txt.length() - searchtxt.length());i++) {
            if(txt.regionMatches(true,i," " + searchtxt + " ",0,searchtxt.length()+2)) {
               txtarea.setCaretPosition(i+1);
                   txtarea.setSelectionStart(i+1);
                   txtarea.setSelectionEnd(i+1+searchtxt.length());
                   findpos = i+1+searchtxt.length();
                   return;
            }
          }
          for(int i=findpos;i<=(txt.length() - searchtxt.length());i++) {
            if(txt.regionMatches(true,i," " + searchtxt + "\t",0,searchtxt.length()+2)) {
               txtarea.setCaretPosition(i+1);
                   txtarea.setSelectionStart(i+1);
                   txtarea.setSelectionEnd(i+1+searchtxt.length());
                   findpos = i+1+searchtxt.length();
                   return;
            }
          }
          for(int i=findpos;i<=(txt.length() - searchtxt.length());i++) {
            if(txt.regionMatches(true,i," " + searchtxt + "\n",0,searchtxt.length()+2)) {
               txtarea.setCaretPosition(i+1);
                   txtarea.setSelectionStart(i+1);
                   txtarea.setSelectionEnd(i+1+searchtxt.length());
                   findpos = i+1+searchtxt.length();
                   return;
            }
          }
          for(int i=findpos;i<=(txt.length() - searchtxt.length());i++) {
            if(txt.regionMatches(true,i,"\t" + searchtxt + "\t",0,searchtxt.length()+2)) {
               txtarea.setCaretPosition(i+1);
                   txtarea.setSelectionStart(i+1);
                   txtarea.setSelectionEnd(i+1+searchtxt.length());
                   findpos = i+1+searchtxt.length();
                   return;
            }
          }
          for(int i=findpos;i<=(txt.length() - searchtxt.length());i++) {
            if(txt.regionMatches(true,i,"\t" + searchtxt + "\n",0,searchtxt.length()+2)) {
               txtarea.setCaretPosition(i+1);
                   txtarea.setSelectionStart(i+1);
                   txtarea.setSelectionEnd(i+1+searchtxt.length());
                   findpos = i+1+searchtxt.length();
                   return;
            }
          }
         }
          else if(chkmachcase.isSelected()) {
            int pos = txt.indexOf(searchtxt,findpos);
             if(pos != -1) {
              txtarea.setCaretPosition(pos);
                txtarea.setSelectionStart(pos);
                txtarea.setSelectionEnd(pos+searchtxt.length());
                findpos = pos+searchtxt.length();
                return;
             }
          }
          else {
             for(int i=findpos;i<=(txt.length() - searchtxt.length());i++) {
                if(txt.substring(i,i + searchtxt.length()).equalsIgnoreCase(searchtxt)) {
                   txtarea.setCaretPosition(i);
                   txtarea.setSelectionStart(i);
                   txtarea.setSelectionEnd(i+searchtxt.length());
                   findpos = i+searchtxt.length();
                   return;
                }
             }
          }
        } catch(Exception exp) {
          System.err.println("Error in find " + exp);
       }
     }
      JOptionPane.showMessageDialog(JPad.this,"JPad has finished searching the file.","JPad Find",JOptionPane.INFORMATION_MESSAGE);
      findpos = 0;
      txtarea.setCaretPosition(originalpos);
      return;
   }

//---> helpTopics Method <-------------------
   public void helpTopics() {
    if(frmhlp != null) {
      frmhlp.setVisible(true);
       frmhlp.setState(frmhlp.NORMAL);
       return;
    }
    busy = true;
    JPad.this.setCursor(curwait);
      frmhlp = new JFrame("JPad Help");
      Container hlpcon = frmhlp.getContentPane();
      hlpcon.setLayout(new BorderLayout());
      frmhlp.setIconImage(imgico.getImage());
      htmlpos = 0;
      list = new Vector();
      JPanel panbut = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
      JButton buttmp = new JButton("Contents",new ImageIcon(getResourceURL("resources/JPadHelp16.jpg")));
      buttmp.setFocusPainted(false);
      buttmp.setMnemonic('C');
      buttmp.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          try {
            URL u = (URL)list.elementAt(0);
          html.setPage(u);
          list.add(u);
          htmlpos++;
        }catch(Exception e) {}
          return;
        }
      });
      panbut.add(buttmp);
      buttmp = new JButton("Back",new ImageIcon(getResourceURL("resources/JPadBack16.jpg")));
      buttmp.setMnemonic('B');
      buttmp.setFocusPainted(false);
      buttmp.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          try {
            if(htmlpos > 0)
            html.setPage((URL)list.elementAt(--htmlpos));
        }catch(Exception e) {}
          return;
        }
      });
      panbut.add(buttmp);
      buttmp = new JButton("Forward",new ImageIcon(getResourceURL("resources/JPadForward16.jpg")));
      buttmp.setMnemonic('F');
      buttmp.setFocusPainted(false);
      buttmp.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          try {
          if(htmlpos < (list.size() - 1))
            html.setPage((URL)list.elementAt(++htmlpos));
        }catch(Exception e) {}
          return;
        }
      });
      panbut.add(buttmp);
      buttmp = new JButton("Close",new ImageIcon(getResourceURL("resources/JPadClose16.jpg")));
      buttmp.setFocusPainted(false);
      buttmp.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          frmhlp.setVisible(false);
          return;
        }
      });
      panbut.add(buttmp);
      panbut.setBorder(new BevelBorder(BevelBorder.RAISED));
      try {
        URL url = getResourceURL("resources/JPadHlp.html");
        list.add(url);
        html = new JEditorPane(url);
        html.setEditable(false);
        html.addHyperlinkListener(createHyperLinkListener());
     }catch(Exception exp) {
      frmhlp = null;
        tool.beep();
        System.err.println(exp);
        JOptionPane.showMessageDialog(frmhlp,"Unable to read the help file.","JPad",JOptionPane.WARNING_MESSAGE);
        JPad.this.setCursor(curdef);
        return;
      }
      scrhlppane = new JScrollPane(html);
      hlpcon.add(panbut,BorderLayout.NORTH);
      hlpcon.add(scrhlppane,BorderLayout.CENTER);
      frmhlp.pack();
      frmhlp.setSize(500,380);
      frmhlp.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent ke) {
          if(ke.getKeyCode() == KeyEvent.VK_ESCAPE)
            frmhlp.setVisible(false);
          return;
       }
    });
    JPad.this.setCursor(curdef);
    busy = false;
      frmhlp.setVisible(true);
      return;
   }

//---> createHyperLinkListener Method <-----------
  protected HyperlinkListener createHyperLinkListener() {
    return new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          if (e instanceof HTMLFrameHyperlinkEvent) {
            ((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent)e);
          }
          else {
            try {
              URL u = e.getURL();
              html.setPage(u);
              htmlpos++;
              for(int i=htmlpos;i<list.size();i++)
                list.removeElementAt(i);
              list.add(u);
            } catch (IOException ioe) {
              System.err.println("IOE: " + ioe);
              JOptionPane.showMessageDialog(JPad.this,ioe,"JPad",JOptionPane.WARNING_MESSAGE);
            }
          }
        }
      }
    };
    }

//---> JPad main Method <-------------------
  public static void main(String [] ar){
    Frimble frimble =JFrameFrimble.createJFrameFrimble(new JFrame("JPad"));

    frimble.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frimble.getContentPane().setLayout(new BorderLayout());

    JPad  pad=new JPad();
    pad.setLayout(new BorderLayout());


    frimble.getContentPane().add(pad, BorderLayout.CENTER);
    pad.setFrimble(frimble);
    frimble.pack();

        if(ar.length == 0)
           pad.setFile("");
        else {
        if(ar[0].equals("-h") || ar[0].equals("--help"))
          System.out.println("Usage: java -jar JPad.jar [filname]" +
            "\nOnly one file name can be given at command line.\n");
      else
           pad.setFile(ar[0]);
        }


    frimble.setVisible(true);

  }

//---> Inner class UndoHandler <---------
  private class UndoHandler implements UndoableEditListener {
    public void undoableEditHappened(UndoableEditEvent uee) {
      undo.addEdit(uee.getEdit());
      return;
    }
  }

//---> Inner class HintThread <---------
  private class HintThread implements Runnable {
    private String hints[];
    private java.util.Timer timer;
    private int hintpos = 0;

    public HintThread() {
      hints = new String[] {
      "Press F1, for help",
      "Use Edit->Options to change the appearance of JPad",
      "Press F3, to find the next ocuurence of string",
      "Right click to popup Edit menu",
      "Press F5, to insert date",
      "Please e-mail your suggestions or bugs to hari_8@yahoo.com",
      "JPad Homepage: 'http://www.geocities.com/hari_8/'"
      };
      hintthread = new Thread(this);
      hintthread.start();
    }
    public void run() {
      try {
        Thread.sleep(8000);
        if(!tip) {
          labstatus.setText("Ready");
          return;
        }
        while(true) {
          if(busy) {
            hintthread.yield();
            Thread.sleep(8000);
            continue;
          }
          if(hintpos > hints.length - 1)
            hintpos = 0;
          labstatus.setIcon(imgtip);
          labstatus.setText(hints[hintpos]);
          hintpos++;
          Thread.sleep(3500);
          labstatus.setIcon(null);
          labstatus.setText("Ready");
          Thread.sleep(8000);
        }
      }catch(Exception e) {
        System.err.println("Error in HintThread: " + e);
      }
      return;
    }
  }


//---> Inner class ColoredSquare <---------
   private class ColoredSquare implements Icon {
      Color color;
      public ColoredSquare(Color c) {
         this.color = c;
      }
      public void paintIcon(Component c, Graphics g, int x, int y) {
         Color oldColor = g.getColor();
         g.setColor(color);
         g.fill3DRect(x,y,getIconWidth(), getIconHeight(), true);
         g.setColor(oldColor);
      }
      public int getIconWidth() {
        return 20;
     }
      public int getIconHeight() {
        return 20;
     }
   }
}