/*
 * Textpad.java
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

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import javax.swing.text.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.*;
import org.jesktop.frimble.*;
import net.jesktop.textpad.TextpadUtils;


/**
 * Main class of Textpad
 * @author  Dr.M.L.Noone
 * @version  1.1 2000/11/6
 * @author  Paul Hammant (forked 29th Nov 2000)
 */
public class Textpad extends JPanel implements FrimbleAware { // ContentViewer{

    private static boolean MAINED = false;
    private TextpadUtils tpUtils;

  /**
   * Creates a new Textpad.
   */
  public Textpad() {
    super(true);
  }

  public void setFrimble(Frimble frimble) {
    this.frimble = frimble;
    tpUtils = new TextpadUtils(frimble);
    startTextpad();
    postContruction(null);
  }

  private void startTextpad() {

    setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    setLayout(new BorderLayout());

    editor = createEditor();
    editor.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

    editor.getDocument().addUndoableEditListener(undoHandler);
    editor.setDropTarget(new TpDropHandler());


    editor.addMouseListener(new TpMouseListener());

    // install the command table
    commands = new Hashtable();

    Action[] actions = getActions();

    for (int i = 0; i < actions.length; i++) {
      Action a = actions[i];
      commands.put(a.getValue(Action.NAME), a);
    }

    JScrollPane scroller = new JScrollPane();

    JViewport port = scroller.getViewport();

    port.add(editor);

    homeDir = new File("."); //System.getProperty("user.home"+File.separator+".Textpad")
    if (!homeDir.exists() && !homeDir.mkdir()) System.err.println("Unable to create home directory");

    menubar = createMenubar();
    add("North", menubar);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add("North",createToolbar());
    panel.add("Center", scroller);

    JPanel sp = new JPanel();
    statusLabel=new JLabel(getResourceString(newFile),JLabel.CENTER);
    sp.setLayout(new GridLayout(1,1));
    sp.add(statusLabel);
    panel.add("South",sp);

    add("Center", panel);

    currentFileName="Untitled";
    URL url=getResource("iconImage");
    if (url!=null) iconImage=Toolkit.getDefaultToolkit().getImage(url);
    frimble.setIconImage(iconImage);



  }


  /**
   * The main method of Textpad application.<p>
   * If the first command line argument is a file, it will be opened
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    MAINED = true;
    try {
      String vers = System.getProperty("java.version");
      if (vers.compareTo("1.1.2") < 0) {
        System.out.println("!!!WARNING: Swing must be run with a " +
        "1.1.2 or higher version VM!!!");
      }
      Textpad tp=new Textpad();
      Frimble frimble = JFrameFrimble.createJFrameFrimble(new JFrame());
      tp.setFrimble(frimble);
      frimble.getContentPane().setLayout(new BorderLayout());
      frimble.getContentPane().add("Center", tp);
      tp.postContruction(args);

    } catch (Throwable t) {
      System.out.println("uncaught exception: " + t);
      t.printStackTrace();
    }
  }

    private void postContruction(String[] args) {

      frimble.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      frimble.setTitle(resources.getString(title)+" - "+resources.getString(newFile));
      frimble.setBackground(Color.lightGray);
      frimble.addFrimbleListener(new AppCloser());
      frimble.pack();

      File f =null;
      if (args!=null && args.length>0) f = new File(args[0]);
      getPrefs(f);    // the frame is shown in the getPrefs method.

  }
  public String[] getViewableMimeTypes() {
    return new String[] {"text/plain"};
  }
  public void viewContent(String url, InputStream is, String mimeType) {
  }
  public void viewContent(File file, String mimeType, boolean thumbNail) {
    getPrefs(file);
  }

       /**
  *  Creates an editor for Textpad
  */
  private JTextArea createEditor() {
    return new JTextArea();
  }

        /**
  *  Creates a menu item
  *  @param cmd     Idenfifier for the command
  *  @param   addActlis   true if the item  needs  the TpActionHamdler
  *  @param   parent    identifier for the parent menu
  */
  private JMenuItem createMenuItem(String cmd, boolean addActLis, String parent) {
    String lstr = getResourceString(cmd + labelSuffix);
    if (lstr==null) lstr=cmd;

    String astr = getResourceString(cmd + actionSuffix);
    if (astr == null)   {
      if (addActLis) astr=parent+'-'+cmd;
      else astr = cmd;
    }
    Action a = getAction(astr);
    KeyStroke accel = (KeyStroke) accelTable.get(cmd);

    JMenuItem mi = new JMenuItem(lstr);
    mi.setHorizontalTextPosition(JButton.RIGHT);

        if (a != null) {
      mi.addActionListener(a);
      a.addPropertyChangeListener(createActionChangeListener(mi));
      mi.setEnabled(a.isEnabled());
    } else
    if (addActLis) {
      ButtonGroup bg = tpUtils.getButtonGroup(parent.charAt(0),cmd.charAt(0));
      if (bg!=null) {
        mi = new JRadioButtonMenuItem(lstr);
        bg.add(mi);
      }
      mi.addActionListener(new TpActionListener());
    }
    else {
      mi.setEnabled(false);
    }
    if (accel!=null) mi.setAccelerator(accel);

    URL url = getResource(cmd + imageSuffix);
    if (url != null) mi.setIcon(new ImageIcon(url));
    mi.setActionCommand(astr);
    return mi;
  }

       /**
  *  Creates a toolbar
  *  @see #createToolbarButton
  */
  private Component createToolbar() {
    toolbar = new JToolBar();
    String[] toolKeys = tokenize(getResourceString("toolbar"));
    for (int i = 0; i < toolKeys.length; i++) {
      if (toolKeys[i].equals("-")) {
        toolbar.add(Box.createHorizontalStrut(10));
      } else {
        toolbar.add(createTool(toolKeys[i]));
      }
    }
    toolbar.add(Box.createHorizontalGlue());
    return toolbar;
  }

       /**
  *  Routed call to createToolbarButton()
  *  @see #createToolbarButton
  */
  private Component createTool(String key) {
    return createToolbarButton(key);
  }

       /**
  *  Creates a toolbar button.<p>
  *  Uses <a href =#getResource(java.lang.String)>getResource()</a> to load the image.<p>
  *  Uses <a href =#getResourceString(java.lang.String)>getResourceString()</a> to load the tool tip.<p>
  *  @param key identifier for the action
  */
  private JButton createToolbarButton(String key) {

    URL url = getResource(key + imageSuffix);

    JButton b = new JButton(new ImageIcon(url)) {
      public float getAlignmentY() { return 0.5f; }
    };
    b.setRequestFocusEnabled(false);
    b.setMargin(new Insets(1,1,1,1));
    String astr = getResourceString(key + actionSuffix);
    if (astr == null) {
      astr = key;
    }
    Action a = getAction(astr);

    if (a != null) {
      b.setActionCommand(astr);
      b.addActionListener(a);
      a.addPropertyChangeListener(createActionChangeListener(b));
      b.setEnabled(a.isEnabled());
    } else {
      b.setEnabled(false);
    }

    String tip = getResourceString(key + tipSuffix);
    if (tip != null) {
      b.setToolTipText(tip);
    }

    return b;
  }


       /**
  *  Utility function which converts a space delimited String to an array of Strings
  *  @param input the String to tokenize
  */
  private String[] tokenize(String input) {
    Vector v = new Vector();
    StringTokenizer t = new StringTokenizer(input);
    while (t.hasMoreTokens())
      v.addElement(t.nextToken());
      String cmd[];
      cmd = new String[v.size()];
      v.copyInto(cmd);
      return cmd;
  }

       private JMenuBar createMenubar() {
    JMenuItem mi;
    JMenuBar mb = new JMenuBar();

    String[] menuKeys ={"file","edit","view","presets","search","help"};
    for (int i = 0; i < menuKeys.length; i++) {
      JMenu m = createMenu(menuKeys[i]);
      if (m != null) {
        mb.add(m);
      }
    }
    return mb;
  }

       /**
  *  Creates a menu
  *  @param key the String ideentifier for the menu
  */
  private JMenu createMenu(String key) {
    char id=key.charAt(0);
    final JMenu menu = new JMenu(getResourceString(key + labelSuffix));
    menu.setMnemonic(menu.getText().charAt(0));
    if (id=='p'){
      presetMenu=menu;
      getPresets();
      return presetMenu;
    }
    boolean addActLis=false;
    String af = getResourceString(key+actionFlagSuffix);
    if (af!=null && af.charAt(0)=='1') addActLis=true;
    String[] itemKeys = tpUtils.getMenuKeys(id);
    for (int i = 0; i < itemKeys.length; i++) {
      switch (itemKeys[i].charAt(0)) {
        case '-' :
          menu.addSeparator(); break;
        case '*' :
          JMenu m = createMenu(itemKeys[i].substring(1));
          if (m!=null) menu.add(m);
          break;
        default:
        JMenuItem mi = createMenuItem(itemKeys[i],addActLis,key);
        menu.add(mi);
      }
    }

    return menu;
  }


  /**
  * Creates a property changed listener for the actions
  * @param b the MenuItem implementing the action
  */
  private PropertyChangeListener createActionChangeListener(AbstractButton b) {
    return new ActionChangedListener(b);
  }


       /**
  * Gets a url from a resource string
  * @param key the string key to the url in the resource bundle
  * @see java.lang.Class#getResource
  */
  private URL getResource(String key) {
    String name = getResourceString(key);
    if (name != null) {
      URL url = this.getClass().getResource(name);
      return url;
    }
    return null;
  }


       /**
  * gets a resource string from the resource bundle
  * @param nm name of the resource to fetch
  */
  private String getResourceString(String nm) {
    String str;
    try {
      str = resources.getString(nm);
    } catch (MissingResourceException mre) {
      str = null;
    }
    return str;
  }


       /**
  * fetches the presets from the  Textpad.presets file
  * @see mln.textpad.TextpadUtils#loadHashtable
  */
  private void getPresets() {
    SwingUtilities.invokeLater(new Runnable() {
      //System.out.println("reading presets");
      public void run()
      { presetTable=new Hashtable();
        tpUtils.loadHashtable(presetTable,new File(homeDir,"Textpad.presets"));
        updatePresets();
      }
    });
  }

       /**
  * Updates the presets Hashtable and the presets menu
  */
  private void updatePresets() {
    presetMenu.removeAll();
    for (java.util.Enumeration en = presetTable.keys();en.hasMoreElements();)  {
      String presetName = (String) en.nextElement();
      JMenuItem mi = new JMenuItem(presetName);
      mi.setActionCommand("preset-"+presetName);
      mi.addActionListener(new TpActionListener());
      presetMenu.add(mi);
    }
    String[] itemKeys = tpUtils.getMenuKeys('p');
    for (int i = 0; i < itemKeys.length; i++) {
      switch (itemKeys[i].charAt(0)) {
        case '-' :
          presetMenu.addSeparator(); break;
        default:
        JMenuItem mi = createMenuItem(itemKeys[i],true,"presets");
        presetMenu.add(mi);
      }
    }

  }

       /**
  * Fetches the preferences from the Textpad.ini file.<p>
  * Also opens any file passed on from <a href = #main(java.lang.String[])>main()</a>
  * @param f The file to open after setting prefs, passed on from main()
  * @see mln.textpad.TextpadUtils#loadHashtable
    */
  private void getPrefs(final File file) {

    if (prefTable==null) prefTable=new Hashtable();
    //PH
    SwingUtilities.invokeLater(new Runnable() {
      public void run()
      {

        Dimension presetdm = new Dimension();
        tpUtils.loadDefaultPrefTable(prefTable);
        tpUtils.loadHashtable(prefTable,new File(homeDir,"Textpad.ini"));
        try {
          String bgCol = (String)prefTable.get("bgcolor");
          String txCol =(String)prefTable.get("textcolor");

          setFont();
          setBg(bgCol);
          setText(txCol);

          tpUtils.selectRadioItem('b', bgCol);
          tpUtils.selectRadioItem('t', txCol);
          tpUtils.selectRadioItem('j',(String)prefTable.get("fontstyle"));
          tpUtils.selectRadioItem('j',(String)prefTable.get("fontsize"));
          tpUtils.selectRadioItem('l',(String)prefTable.get("lnf"));

          presetdm = new Dimension(
              Integer.parseInt((String)prefTable.get("width")),
              Integer.parseInt((String)prefTable.get("height"))
              );

        } catch (Exception e) {}

        setLNF();
        frimble.setSize(presetdm);
        frimble.show();
        Dimension realdm = frimble.getSize();
        dmcor = new Dimension(realdm.width-presetdm.width,realdm.height-presetdm.height);
        revalidate();
        if (file!=null) commonOpenProc(file);
        //appFrame.getContentPane().repaint();
      }
    });
  }

       /**
  * Saves the presets to the  Textpad.presets file
  * @see mln.textpad.TextpadUtils#saveHashtable
  */
  private void savePresets() {
    tpUtils.saveHashtable(presetTable,new File(homeDir,"Textpad.presets"));
  }

       /**
  * Ssaves the preferences to the  Textpad.ini file
  * @see mln.textpad.TextpadUtils#saveHashtable
  */
  private void savePrefs() {
    Dimension d = frimble.getSize();
    prefTable.put("width",Integer.toString(d.width-dmcor.width));
    prefTable.put("height",Integer.toString(d.height-dmcor.height));
    tpUtils.saveHashtable(prefTable,new File(homeDir,"Textpad.ini"));
  }

       /**
  * Sets the title bar and status to the current file
        */
  private void updateDisplayedFileName() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (openedFile!=null) {
          frimble.setTitle(getResourceString(title)+" - "+currentFileName);
          statusLabel.setText(openedFile.toString());
        }
        else {
          frimble.setTitle(getResourceString(title)+" - "+getResourceString(newFile));
          statusLabel.setText(getResourceString(newFile));
        }
      }
    });
  }


       /**
  * Pops up a JOptionPane
  * @param message the message to display
  * @param  type      JOptionPane message type
  * @param  options   array of options
  */
  private int popOptions(String message, int type,final Object[] options) {
    final JOptionPane pane = new JOptionPane(message,type);
    pane.setOptions(options); // Configure
    String fileDialogTitle=new String();
    switch(type) {
      case JOptionPane.WARNING_MESSAGE : fileDialogTitle = "Textpad - warning!"; break;
      case JOptionPane.QUESTION_MESSAGE: fileDialogTitle = "Textpad - choice:"; break;
    }

    if (options instanceof JButton[]) {
      for (int i=0;i<options.length;i++) {
        final int t=i;
        ((JButton)options[i]).addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            pane.setValue(options[t]);
          }
        });
      }
    }
    JDialog dialog = pane.createDialog(frimble.getFrimbleContained(),fileDialogTitle);
    dialog.show();
    Object selectedValue = pane.getValue();
    if(selectedValue == null)
      return options.length;
      for(int counter = 0, maxCounter = options.length;
      counter < maxCounter; counter++) {
        if(options[counter].equals(selectedValue))
          return counter;
      }
      return 2;
  }

       /**
  * Pops up a standard file save reminder
  * @return The chioce made by the user, as <p> 0=Save Changes, 1=Discard Changes, 2=Cancel.
  */
  private int fileChangedWarning() {
    String[] optionStrings={"Save Changes","Don't Save","Cancel"};
    JButton [] options = new JButton[3];
    for (int i=0;i<3;i++) {
      options[i]=new JButton(optionStrings[i]);
      options[i].setMnemonic(optionStrings[i].charAt(0));
    }
    return popOptions("The file "+currentFileName+" has unsaved changes.",JOptionPane.WARNING_MESSAGE,options);
  }

       /**
  *   Calls the file changed warning and does the nedful
  *   @return true if a decision either to save or to discard has been made
  */
  private boolean standardSaveCheck() {
    if (!changed) return true;
    switch (fileChangedWarning()) {
        case 0:
          if(openedFile==null) {if (!saveAsFileBox()) return false;}
           else {if (!saveCurrentFile()) return false;}
        case 1:  return true;
        default: return false;
      }
  }

       /**
  *   Shows the file chooser dialog in save mode
  *   and starts the file saver thread.
  *   @see Textpad.FileSaver
  */
  private boolean saveAsFileBox() {
    int retval = frimble.showSaveDialog(fileDialog); // TODO PH
    frimble.getContentPane().repaint();
    if ( retval != JFileChooser.APPROVE_OPTION) return false;
    File f = fileDialog.getSelectedFile();
    if (f.exists()) {
      String few = getResourceString("replaceFileWarning");
      if (few==null) few = " - this file aleady exists";
      few = f.getName()+few;
      String [] feOpts={"Replace","Cancel"};
      if(popOptions(few,JOptionPane.WARNING_MESSAGE,feOpts)>0) return false;
    }
    FileSaver saver = new FileSaver(f, editor.getDocument());
    if (!saver.canWrite()) {
      if (saveAsFileBox()) return true;
      return false;
    }
    saver.start();
    openedFile=f;
    currentFileName=f.getName();
    updateDisplayedFileName();
    updateActions();
    return true;

  }


  private void openFileBox() {
    int returnVal = frimble.showOpenDialog(fileDialog);
    frimble.getContentPane().repaint();
    if (returnVal != JFileChooser.APPROVE_OPTION) return;
    openFile(fileDialog.getSelectedFile());
  }


  /**
  *   Saves the current file.
  *   @return true if saved
  */
  private boolean saveCurrentFile() {
    Document Doc = editor.getDocument();
    FileSaver saver = new FileSaver(openedFile, editor.getDocument());
    if (!saver.canWrite()) {
      if (saveAsFileBox()) return true;
      return false;
    }
    saver.start();
    updateDisplayedFileName();
    return true;

  }


  /**
  * Starts the file loader
  * @param  f the File to open
  * @see Textpad.FileLoader
  */
  private void openFile(File f) {
    if  (! (f.exists() && f.canRead()) )
      {
        JOptionPane.showMessageDialog(frimble.getFrimbleContained(),
          "Textpad cannot read the file - access denied.",
          "Textpad - error!",
          JOptionPane.ERROR_MESSAGE);
        return;
      }
    clearEditor();
    openedFile=f;
    currentFileName=f.getName();
    frimble.setTitle(getResourceString(title)+" - "+currentFileName);
    Thread loader = new FileLoader(f, editor.getDocument());
    loader.start();
  }


       /**
  *   Clears the editor and resets the actions
  *   @see #clearEditor
  */
  private void clearDocument() {
    clearEditor();
    changed=false;
    openedFile=null;
    updateActions();
    currentFileName=getResourceString(newFile);
    editor.getDocument().addUndoableEditListener(undoHandler);
    updateDisplayedFileName();
  }


       /**
  *  Common file open procedure for presets and drops.<p>
  *  Handles both files and directories.
  */
  private boolean commonOpenProc(File f) {
    if (f.isDirectory()) {
      if (openedFile!=null || changed) {
        String[] pdOpts = {"Save to","Open"};
        if (popOptions(f.toString(),JOptionPane.QUESTION_MESSAGE,pdOpts) == 0) {
          fileDialog.setCurrentDirectory(f);
          saveAsFileBox();
          return true;
        }
      }
      if (standardSaveCheck()) {
             fileDialog.setCurrentDirectory(f);
             openFileBox();
             return true;
            }
    }
    else {

      if (standardSaveCheck()) { openFile(f);
              fileDialog.setCurrentDirectory(f);
              return true;
      }
    }
    return false;
  }


       /**
  * Resets the actions.<p>
        * This will cann the corresponding PropertyChangedListener to
  * disable the menuitems and buttons
  */
  private void updateActions() {
    aReloadAction.update();
    aFindAction.update();
    aFindNextAction.update();
    aGotoLineAction.update();
    anAddToPresetsAction.update();
    anAddDirToPresetsAction.update();
  }


       /**
  * Clears the editor and removes undos.
  */
  private void clearEditor() {
    Document oldDoc = editor.getDocument();
    if(oldDoc != null)
      oldDoc.removeUndoableEditListener(undoHandler);
    undo.discardAllEdits();
    undoAction.update();
    redoAction.update();
    editor.setDocument(new PlainDocument());
    searchString=null;
    searchIndex=0;
  }


       /** Default find next*/
  private void findNext() {findNext(true);}


       /**
  * Finds the next occurence of the search string in the document.
  * @param select True if the search string is to be selected in the editor.
  */
  private void findNext(boolean select) {
      searchString = searchField.getText();
      if (searchString==null) return;
      int sl=searchString.length();
      Document doc = editor.getDocument();
      int dl = doc.getLength();
      if (dl<sl) return;
      int step = Math.min(255,dl-searchIndex-1);
      int i=step;
      boolean once =false;
      while (true) {
        try{
          String text=doc.getText(searchIndex,i);
          //System.out.println(i);
          if (matchCase) {
            int s = text.indexOf(searchString);
            //System.out.println(s);
            if (s > -1) {
              searchIndex+=s;
              break;
            }

          }
          else {
            text=text.toLowerCase();
            searchString=searchString.toLowerCase();
            int s = text.indexOf(searchString);
            //System.out.println(s);
            if (s > -1) {
              searchIndex+=s;
              break;
            }

          }

          int rm = dl-searchIndex;
          if ((i+=step) > rm) {
            if (!once) { once=true; i = rm; continue;}
            if (!select) return;
            int ret = JOptionPane.showConfirmDialog(
                     searchDialog,
                                                    "Restart from top?",
                    "Textpad - choice" ,
                   JOptionPane.YES_NO_OPTION,
                 JOptionPane.QUESTION_MESSAGE);

            if (ret == JOptionPane.YES_OPTION)
            {searchIndex=0;
            findNext();}
            return;
          }

        } catch (BadLocationException ex)
        {
          //System.out.println(ex);
          //System.out.println("si,i,dl="+searchIndex+' '+i+' '+dl);
          return;
        }
      }
      if (select) {
      editor.setCaretPosition(searchIndex);
      editor.moveCaretPosition(searchIndex+sl);
      }
      searchIndex+=sl+1;
      return;
    }

       /**
  * Replaces the searchString with the replaceString
  */
  private void replaceNext() {
    if (searchField.getText()==null) return;
    if (searchString==null) { findNext(); return; }
    String replaceString = replaceField.getText();
    String selt = editor.getSelectedText();
    if (selt!=null && selt.equalsIgnoreCase(searchString)) {
      editor.replaceSelection(replaceString);
    }
  }


       /**
  * Replaces all occurences of the search String betweenn st and end with the replace String.
  * @param st Start index.
  * @param end End index.
  */
  private void replaceIndex(int st, int end) {
    searchString=searchField.getText();
    if (searchString==null) return;
    int sl = searchString.length();
    if (sl<1) return;
    searchIndex = st;
    int replct = 0;
    String replaceString = replaceField.getText();
    while (searchIndex<end-sl) {
       int pst = searchIndex;
       findNext(false);
       if (searchIndex<=pst) break;
       editor.replaceRange(replaceString,searchIndex-(sl+1),searchIndex-1);
       replct++;
    }
    JOptionPane.showMessageDialog(searchDialog,
                                             "Made "+Integer.toString(replct)+" change(s)");
  }


       /** Save presets, preferences and exit*/

  private void quit() {
    savePresets();
    savePrefs();
    if (MAINED) {
      System.exit(0);
    }
    frimble.dispose();
    frimble = null;
  }

  private void setFont() {
    Font f=new Font( (String)prefTable.get("fontstyle") , Font.PLAIN , Integer.parseInt((String)prefTable.get("fontsize")) );
    editor.setFont(f);
  }


       /** Stores the font to the prefTable and calls setFont()*/

  private void setFont(String hint) {
    switch (hint.charAt(0)) {
      case 'C' :  case 'T' : case 'H' :  prefTable.put("fontstyle",hint); break;
      default  :   prefTable.put("fontsize",hint); break;
    }
    setFont();
  }


       /**  Sets the look and feel.<p>
  *   The file chooser dialog is also initialized here
  *   to avaoid conflict with the l&f.
  */
  private void setLNF() {

    String name = (String)prefTable.get("lnf");
    try     {
      UIManager.LookAndFeelInfo[]  lnfs = UIManager.getInstalledLookAndFeels();
      for (int i = 0;i< lnfs.length;i++ ) {
        if (lnfs[i].getName().equals(name)) {
          UIManager.setLookAndFeel(lnfs[i].getClassName());
          SwingUtilities.updateComponentTreeUI(frimble.getFrimbleContained());
          if (fileDialog==null) {
            fileDialog=new JFileChooser();
            fileDialog.setFileHidingEnabled(false);
          }
          else SwingUtilities.updateComponentTreeUI(fileDialog);
          if (searchDialog!=null) SwingUtilities.updateComponentTreeUI(searchDialog);
          if (tpPop!=null) SwingUtilities.updateComponentTreeUI(tpPop);
          break;
        }

      }
    }
    catch (UnsupportedLookAndFeelException e) {setLNF("Metal");}
    catch (IllegalAccessException e) {setLNF("Metal");}
    catch (InstantiationException e) {setLNF("Metal");}
    catch (ClassNotFoundException e) {setLNF("Metal");}
    // catch the break.
    if (fileDialog==null) {
            fileDialog=new JFileChooser();
            fileDialog.setFileHidingEnabled(false);
          }
  }

  private void setLNF(String name) {
    prefTable.put("lnf",name);
    setLNF();
  }

  private void setBg(String s){
    prefTable.put("bgcolor",s);
    editor.setBackground(tpUtils.getColor(s));
  }


  /** Sets the text and cursor color*/
  private void setText(String s){
    prefTable.put("textcolor",s);
    Color c=tpUtils.getColor(s);
    editor.setForeground(c);
    editor.setCaretColor(c);
  }

  /** Returns the action identified by cmd*/
  private Action getAction(String cmd) {
    return (Action) commands.get(cmd);
  }

       /** Returns all supported acions*/
  private Action[] getActions() {
    return TextAction.augmentList(editor.getActions(), defaultActions);
        }


  // private memnbers

        private String currentFileName;

  private File openedFile;

  private boolean safeToSave;

  /** Becomes true when the users changes the document after a load or save*/
  private boolean changed;

  private JTextArea editor;

  private JMenuBar menubar;

  private JMenu presetMenu;

  private JToolBar toolbar;

  private JLabel statusLabel;

  private JPanel status;

  private File homeDir;

  private Image iconImage;

  /** Popup menu for Textpad*/
  private JPopupMenu tpPop;

  private Frimble frimble;

  /** Needed to correct the dimensions while saving the frame size. */
  private Dimension dmcor;

  private Hashtable commands;

  private Hashtable presetTable;

  /** Contains user preferences */
  private Hashtable prefTable;

  /** Contains keyboard accelerators */
  private static Hashtable accelTable;

  private static ResourceBundle resources;

  /** Fetch the ResourceBundle and <a href=#accelTable>accelTable</a>*/
  static {
    try {

      resources = ResourceBundle.getBundle("net.jesktop.textpad.resources.Textpad",
      Locale.getDefault());

    } catch (MissingResourceException mre) {
      System.err.println("net.jesktop.textpad.resources.Textpad/Textpad.properties not found");
      System.exit(1);
    }

    accelTable=TextpadUtils.buildAccelTable();

  }


  private JFileChooser fileDialog;


  private static String newFile = "newfile";

  private static String title = "Title";

  // Search and Replace Dialog Items
  private JTextField searchField = new JTextField(30);
  private JTextField replaceField = new JTextField(30);
  private JDialog searchDialog ;
  /** String to search for */
  private String searchString;
  /** True if searches are case sensitive.*/
  private boolean matchCase;
  /** Index of last search.*/
  private int searchIndex;

  private UndoableEditListener undoHandler = new UndoHandler();

  private UndoManager undo = new UndoManager();


  // suffixes for the resources
  private static String imageSuffix = "Image";
  private static String labelSuffix = "Label";
  private static String actionSuffix = "Action";
  private static String actionFlagSuffix = "Enabled";
  private static String tipSuffix = "Tooltip";
  // action identifiers
  private static String openAction = "open";
  private static String reloadAction = "reload";
  private static String newAction  = "new";
  private static String saveAction = "save";
  private static String save_asAction = "save_as";
  private static String add_to_presetsAction = "add_to_presets";
  private static String add_dir_to_presetsAction = "add_dir_to_presets";
  private static String findAction = "find";
  private static String find_nextAction = "find_next";
  private static String goto_lineAction = "goto_line";
  private static String exitAction = "exit";
  private static String usageAction = "usage";
  private static String aboutAction = "about";

  // --- action implementations -----------------------------------

  private UndoAction undoAction = new UndoAction();
  private RedoAction redoAction = new RedoAction();
  private ReloadAction aReloadAction = new ReloadAction();
  private FindAction aFindAction = new FindAction();
  private FindNextAction aFindNextAction = new FindNextAction();
  private AddToPresetsAction anAddToPresetsAction = new AddToPresetsAction();
  private AddDirToPresetsAction anAddDirToPresetsAction = new AddDirToPresetsAction();
  private GotoLineAction aGotoLineAction = new GotoLineAction();

       /**
  * The array of actions implememnted in Textpad<p>
  *
  */
  private Action[] defaultActions = {
    new NewAction(),
    new OpenAction(),
    aReloadAction,
    new ExitAction(),
    new SaveAction(),
    new SaveAsAction(),
    anAddToPresetsAction,
    anAddDirToPresetsAction,
    aFindAction ,
    aFindNextAction,
    aGotoLineAction,
    new UsageAction(),
    new AboutAction(),
    undoAction,
    redoAction
  };


  private final  class UndoAction extends AbstractAction {

    public UndoAction() {
      super("Undo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        undo.undo();
      } catch (CannotUndoException ex) {

      }
      update();
      redoAction.update();
    }

    public void update() {
      if(undo.canUndo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getUndoPresentationName());
      }
      else {
        setEnabled(false);
        putValue(Action.NAME, "Undo");
      }
    }
  }

  private final class RedoAction extends AbstractAction {
    public RedoAction() {
      super("Redo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        undo.redo();
      } catch (CannotRedoException ex) {

      }
      update();
      undoAction.update();
    }

    public void update() {
      if(undo.canRedo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getRedoPresentationName());
      }
      else {
        setEnabled(false);
        putValue(Action.NAME, "Redo");
      }
    }
  }

  private final class NewAction extends AbstractAction {

    NewAction() {
      super(newAction);
    }

    NewAction(String nm) {
      super(nm);
    }

    public void actionPerformed(ActionEvent e) {
      if (standardSaveCheck()) clearDocument();
      frimble.getContentPane().repaint();
    }
  }


  private final class OpenAction extends AbstractAction {

    OpenAction() {
      super(openAction);
    }

    public void actionPerformed(ActionEvent e) {
      if (standardSaveCheck()) openFileBox();
    }
  }


  /**
   * Relaods the current file from disk.
   */
  private final class ReloadAction extends AbstractAction {

    ReloadAction() {
      super(reloadAction);
      setEnabled(false);
    }

    /**Shows a warning before reloading*/
    public void actionPerformed(ActionEvent e) {
      if (openedFile==null) return;
      String [] rlOpts = {"Continue","Cancel"};
      if (changed) {
        String rlm=getResourceString("reloadWarning");
        if (rlm==null) rlm="Changes made since last save will be lost!";
        if (popOptions(rlm,JOptionPane.WARNING_MESSAGE,rlOpts)>0) return;
      }
      openFile(openedFile);
    }

    public void update() {
       setEnabled(openedFile!=null);
    }
  }


  private final  class SaveAction extends SaveAsAction {

    SaveAction() {
      super(saveAction);
    }

    public void actionPerformed(ActionEvent e) {
      if (openedFile==null) {
        super.actionPerformed(e);
        return;
      }
      saveCurrentFile();
    }
  }



  private  class SaveAsAction extends AbstractAction {

    SaveAsAction() {
      super(save_asAction);
    }
    SaveAsAction(String nm) {
      super(nm);
    }
    public void actionPerformed(ActionEvent e) {
      saveAsFileBox();
    }
  }


  /**
   * Exits after the <a href=#standardSaveCheck()>standardSaveCheck</a>
   */
  private final class ExitAction extends AbstractAction {

    ExitAction() {
      super(exitAction);
    }

    public void actionPerformed(ActionEvent e) {
      if (standardSaveCheck()) quit();
    }
  }


  /** Adds the file to the Presets Hashtable */

  private class AddToPresetsAction extends AbstractAction {
    AddToPresetsAction() {
      super(add_to_presetsAction);
      setEnabled(false);
    }
    AddToPresetsAction(String nm) {
      super(nm);
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      add(openedFile,currentFileName);
    }

    protected void add(File presetF, String name) {
      if (presetF==null) return;
      presetTable.put(name,presetF.toString());
      updatePresets();
    }
    public void update() {
      setEnabled(openedFile!=null);
    }
  }


  /** Adds the directory to the Presets Hashtable */

  private final class AddDirToPresetsAction extends AddToPresetsAction {
    AddDirToPresetsAction() {
      super(add_dir_to_presetsAction);
    }
    public void actionPerformed(ActionEvent e) {
      File currentDir=openedFile.getParentFile();
      String name = currentDir.getName();
      String pdi=getResourceString("dirIndicator");
      //System.out.println(pdi);
      if (pdi!=null) name=name.concat(pdi);
      add(currentDir,name);
    }
  }


  /** Opens the search and find dialog */

  private class FindAction extends AbstractAction {
    FindAction() {
      super (findAction);
      setEnabled(false);
    }
    FindAction(String nm) {
      super (nm);
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      editor.requestFocus();
      if (searchDialog==null) {
        TpSearchHandler sl = new TpSearchHandler();
        searchDialog = tpUtils.getSearchDialog(sl,sl,searchField,replaceField);
      }
      searchDialog.show();
      searchField.requestFocus();
      if (searchString!=null) searchField.selectAll();
      else  searchField.setText(null);
      replaceField.setText(null);

    }
    public void update() {
      setEnabled(openedFile!=null || changed);
    }
  }


       /**
  * Call <a href=#findNext()>findNext()</a> after setting the search index to
  * the caret position.
  */
  private final class FindNextAction extends FindAction{

    FindNextAction() {
      super (find_nextAction);
    }

    public void actionPerformed(ActionEvent e) {
      if (searchString == null) super.actionPerformed(e);
      else {
        searchIndex = editor.getCaretPosition();
        findNext();
      }
    }

  }

  private final class GotoLineAction extends FindAction {
    GotoLineAction() {
      super (goto_lineAction);
    }
    public void actionPerformed(ActionEvent e) {
      try {
        int LineNo = Integer.parseInt(JOptionPane.showInputDialog(frimble.getFrimbleContained(),"Go to line:"))-1;
        editor.setCaretPosition(editor.getLineStartOffset(LineNo));
        editor.requestFocus();
      } catch (Exception ex) {}
    }

  }


  /**
   * Shows the instructions in the Textpad.usage file,
   * which is in the Textpad.jar.<p>
   * The file is read using the <a href=Textpad.FileLoader.html>FileLoader<a>
   */
  private final class UsageAction extends AbstractAction {
    UsageAction() {
      super (usageAction);
    }
    public void actionPerformed(ActionEvent e) {


      final JDialog hd = new JDialog (frimble.getOwnerFrame(),"Textpad - "+getResourceString("usageLabel"));


      final Container fr=hd.getContentPane();


      fr.setLayout(new BorderLayout());

      JPanel usagePanel = new JPanel();

      JButton okb = new JButton("Ok");

      okb.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            hd.dispose();
          }
        });
      usagePanel.add(okb);
      fr.add("South",usagePanel);

      usagePanel = new JPanel();

      usagePanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      usagePanel.setLayout(new GridLayout(1,1));

      final JTextArea usageArea = new JTextArea();

      usageArea.setEditable(false);

      JScrollPane scroller = new JScrollPane();

      JViewport port = scroller.getViewport();

      port.add(usageArea);

      scroller.setMinimumSize(new Dimension(520,420));
      scroller.setPreferredSize(new Dimension(520,420));
      usagePanel.add(scroller);

      fr.add("Center",usagePanel);

      URL url = getResource("helpFile");

      //System.out.println(url);

      Thread loader = new FileLoader(url,usageArea.getDocument());
      loader.start();


      usageArea.setFont(new Font("Courier",Font.PLAIN,12));

      hd.setSize(536, 440);
      hd.setLocationRelativeTo(frimble.getFrimbleContained());
      hd.show();
      frimble.getContentPane().repaint();
    }
  }


  /**Shows the about dialog, uses the TextpadUtils showAbout() action*/

  private final class AboutAction extends AbstractAction {
    AboutAction() {
      super (aboutAction);
    }
    public void actionPerformed(ActionEvent e) {
      tpUtils.showAbout(new ImageIcon(iconImage));
    }
  }


  /** Thread to load a file or url.*/

  private final class FileLoader extends Thread {

    /** Loads a file*/
    FileLoader(File f, Document doc) {
      this.f = f;
      setPriority(4);
      this.doc = doc;
      try {
        in =new BufferedReader(new FileReader(f));
      }
      catch (FileNotFoundException e) {}
      status = true;
    }

      /** Loads a url*/
    FileLoader(URL url, Document doc) {
      setPriority(4);
      this.doc = doc;
      try {
        in = new BufferedReader(new InputStreamReader(url.openStream()));
      }
      catch (IOException e) {}
      status = false;
    }


    public void run() {
      try {
        StringBuffer data = new StringBuffer();
        if (status) {
          statusLabel.setText("Loading "+f);
          data.ensureCapacity((int)f.length());
        }
        String inline = new String();
        while ((inline = in.readLine()) !=null)
          {
          data.append(inline);
          data.append('\n');
        }
        in.close();
        doc.insertString(0,data.toString(),null);

        if (status)
        {
          doc.addUndoableEditListener(undoHandler);
          changed=false;
          updateActions();
          statusLabel.setText(f.toString());
          if (!f.canWrite()) {
            JOptionPane.showMessageDialog(frimble.getFrimbleContained(),"The file appears to be read only.","Textpad - warning!",JOptionPane.WARNING_MESSAGE);
            return;
          }
        }
      }
      catch (IOException e) {
        System.err.println(e.toString());
      }
      catch (BadLocationException e) {
        System.err.println(e.getMessage());
      }
    }


    /** Common reader for file/url to open*/
    private BufferedReader in;

    /**True if the file name is to be displayed in the status bar*/
    private boolean status;

    /**The document to load the file into*/
    private Document doc;

    /**The file to open - null if a url is opened*/
    private File f;
  }



  /** Thread to save a file.*/

  private final class FileSaver extends Thread {

    FileWriter fr;

    FileSaver(File f, Document doc) {
      setPriority(4);
      this.f = f;
      this.doc = doc;
    }

    public boolean canWrite() {
      try {
        fr = new FileWriter(f);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(frimble.getFrimbleContained(),
          "Textpad is unable to write to the file - access denied.",
          "Textpad - error!",
          JOptionPane.ERROR_MESSAGE);
        return false;
      }
      return true;
    }

         /**
    * Tokenizes the lines using a StringTokenizer
    * and prints them using the PrintWriter's println method
    */
    public void run() {
      try {
        PrintWriter out  = new PrintWriter(new BufferedWriter(fr),true);
        StringTokenizer st = new StringTokenizer(doc.getText(0,doc.getLength()),"\n",true);
        while (st.hasMoreTokens())
          {
           String outln = st.nextToken();
                 if (outln.equals("\n")) out.println();
                 else out.print(outln);
          }
        out.close();
        changed = false;
      }
      catch (BadLocationException e) {
        System.err.println(e.getMessage());
      }
    }


    /**The document to load the file into*/
    private Document doc;

    /**The file to save*/
    private File f;
  }


  /**Implements enable/disable for the action implementing Buttons and MenuItems*/

  private final class ActionChangedListener implements PropertyChangeListener {

    AbstractButton worker;
    boolean menu;

    ActionChangedListener(AbstractButton b) {
      super();
      this.worker = b;
      if (b instanceof JMenuItem) menu=true;
      else menu=false;
    }

    public void propertyChange(PropertyChangeEvent e) {
      String propertyName = e.getPropertyName();
      if (propertyName.equals(Action.NAME)) {
        String text = (String) e.getNewValue();
        if (menu) worker.setText(text);
        else  worker.setToolTipText(text);
      } else if (propertyName.equals("enabled")) {
        Boolean enabledState = (Boolean) e.getNewValue();
        worker.setEnabled(enabledState.booleanValue());
      }
    }
  }

  /**
   * Listens to undoable edits and adds them to the undo manager
   * The <a href =#changed>changed</a> variable is set to true at the
   * first edit.
   */
  private final class UndoHandler implements UndoableEditListener {

    public void undoableEditHappened(UndoableEditEvent e) {
      if (!changed) {
        changed=true;
        frimble.setTitle(frimble.getTitle()+'*');
        updateActions();
      }
      undo.addEdit(e.getEdit());
      undoAction.update();
      redoAction.update();
    }
  }


  /** Implements actions in the view menu*/

  private final class TpActionListener implements ActionListener {
    private TpActionListener() {}
    public void actionPerformed(ActionEvent e) {
      String ac=e.getActionCommand();
      int i=ac.indexOf('-');
      if (i==-1) return;
      char menu=ac.charAt(0);
      String command=ac.substring(i+1);

      switch (menu) {
        case 'b' : setBg(command);return;
        case 't':  setText(command);return;
        case 'j':  setFont(command);return;
        case 'l':  setLNF(command);return;
        case 'p':
          String fs=(String) presetTable.get(command);
          if (fs!=null) {
            File f=new File(fs);
            if (f.exists()) {
              commonOpenProc(f);
            }
          }
          else if (command.equals("editps")) {
            savePresets();
            File f=new File(homeDir,"Textpad.presets");
            if (f.exists()) {
              if(standardSaveCheck()) openFile(f);
              //fileDialog.setCurrentDirectory(f);
            }
          }
          else if (command.equals("update")) {
            getPresets();
          }
          return;
      }
    }
  }


  /** Handles search and replace events*/

  private final class TpSearchHandler implements ActionListener, ItemListener {
    public void actionPerformed(ActionEvent e) {
      String comm = e.getActionCommand() ;
      //System.out.println(comm);
      if ( e.getSource()==searchField || comm.equals("Find Next") )
            {
            searchIndex = editor.getCaretPosition();
            findNext();
            return;
            }
      if ( e.getSource()==replaceField || comm.equals("Replace") ) {replaceNext();return;}
      if ( comm.equals("Selection") ) { replaceIndex(editor.getSelectionStart(),editor.getSelectionEnd());return;}
      if ( comm.equals("All") ) {replaceIndex(0,editor.getDocument().getLength()-1);return;}
      if ( comm.equals("Restart") ) {searchIndex=0;findNext();return;}
      if ( comm.equals("Close") )   {searchDialog.dispose();return;}
    }
    public void itemStateChanged(ItemEvent  e) {
      if (e.getStateChange()==ItemEvent.SELECTED) matchCase=true;
      else matchCase=false;
    }

  }


  /**Closes the application after a <a href=#standardSaveCheck()>standardSaveCheck</a>*/

  private final class AppCloser extends FrimbleAdapter {
    private AppCloser() {}
    public void frimbleClosing(WindowEvent e) {
      if (standardSaveCheck())
        quit();
    }
  }

        /**
   * Handles drag and drop
   * @see java.awt.dnd.DropTarget
   * @see java.awt.datatransfer.Transferable
   */
  private final class TpDropHandler extends DropTarget {

    TpDropHandler() {
      super();
    }

    public void dragEnter(DropTargetDragEvent e) {
      DataFlavor[] dataFlavors = e.getCurrentDataFlavors();
      for (int i = 0; i < dataFlavors.length; i++) {
        if (DataFlavor.javaFileListFlavor.equals(dataFlavors[i])) {
          e.acceptDrag(DnDConstants.ACTION_COPY);
          return;
        }
      }
      e.rejectDrag();
    }

    public void drop(DropTargetDropEvent e) {

      DropTargetContext targetContext = e.getDropTargetContext();
      boolean outcome = false;

      if ((e.getSourceActions() & DnDConstants.ACTION_COPY) != 0)
        e.acceptDrop(DnDConstants.ACTION_COPY);
        else {
          e.rejectDrop();
          return;
        }

      Transferable t  = e.getTransferable();
      File  f = null;

      try {
        java.util.List files = (java.util.List) (t.getTransferData(DataFlavor.javaFileListFlavor));
        f = (File) files.get(0);
      }
      catch (IOException ex) {}
      catch (UnsupportedFlavorException ex) {}
      if (f != null && f.exists() ) {
        if (commonOpenProc(f)) outcome = true;
      }
      targetContext.dropComplete(outcome);
    }


  }


  /**Listens for mouse clicks and shows the popup menu when the right mouse buton is pressed.*/

  private final class TpMouseListener extends MouseAdapter {
    TpMouseListener() {
      super();
    }

    public void mousePressed(MouseEvent e) {
      if ( (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 ) return;

      if (tpPop ==null) buildPop();
      tpPop.show(editor,e.getX(),e.getY());
    }

    private void buildPop() {
      tpPop = new JPopupMenu();
      String[] itemKeys = tpUtils.getMenuKeys('e');
      for (int i = 0; i < itemKeys.length; i++) {
      switch (itemKeys[i].charAt(0)) {
        case '-' :
          tpPop.addSeparator(); break;
        default:
        JMenuItem mi = createMenuItem(itemKeys[i],false,"edit");
        tpPop.add(mi);
      }

    }

    }

  }

}
