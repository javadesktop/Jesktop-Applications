/**
 * Jipe.java - Main Class for Jipe.  Copyright (c) 1996-2000 Steve Lawson.  E-Mail
 * steve@e-i-s.co.uk Latest version found at http://e-i-s.co.uk/jipe
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
 *  along with this program; if not, write to the free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.jesktop.jipe;

import java.awt.*;
import java.awt.event.*;

//import java.util.List;    //DnD
//import java.util.Iterator;  //Dnd
//import java.awt.dnd.*;    //Dnd
//import java.awt.datatransfer.*;//Dnd

import java.util.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.*;

import org.gjt.sp.jedit.syntax.*;
import org.gjt.sp.jedit.textarea.*;

import org.jesktop.frimble.*;


/**
 * The main Jipe application class.
 *
 * history:
 * 12 July 2000 mgh: in displayFindDialog(), added correct curly braces for if not null check
 * 12 July 2000 mgh: renamed Find() method to displayFindDialog()
 */
public class Jipe extends JPanel implements ActionListener, FrimbleAware  //,DropTargetListener

{
    JipeCompiler compiler;
    PropertiesManager props;
    JipeConsole jipeconsole;
    JipeMethodFinder speedbar;
    IDEOptions optiondialog;
    JavaOptions optionJavadialog;
    JipeProjectManager project;
    Find find;
    FindInFiles findin;
    StatusBar statusBar;
    JarFileManager jarFileManager;
    JFileChooser fileChooser;
    HelpConsole helpConsole;

    String lnfName = null;
    File CURRENTDIR;

    Container container;
    JMenu window, recentFiles, recentProjects;
    protected Vector children = null;
    protected Editor activeChild = null;

    JTree directorytree;


    TreeSelectionModel dirselectionModel;

    public JList compileErrorPanel,filesearchlistPanel;
    public DefaultListModel compilerErrors,filesearchModel;

    private Properties jipeProperties;
    private Hashtable commands;
    private JMenuBar menubar;

    private JToolBar toolbar,toolbar1,toolbar2;
    JTabbedPane tabbedPane;
    JTabbedPane tabbedPane2;
    JTabbedPane tabbedPane3;
    public Frimble frimble;

    JPopupMenu popup, popup2;
    JSplitPane sp,sp2,sp3;
    JPanel panel;
    protected int window_num = 0;
    JMenuItem windowitem,rprojectitem,rfileitem;
    JCheckBoxMenuItem view;
    String textcache = "";
    int saveorquitoption = 0;
    protected int Buttonswitch = 0;
    static int screenHeight;
    private String[] rfiles;
    private String[] rprojects;

    static String ABOUTMSG =
        "      Jipe Version 0.93\n      ---------------------------\n"+
        "Written by Steve Lawson \nemail: steve@e-i-s.co.uk \n   Visit : e-i-s.co.uk\\jipe \n"+
        "\n  Copyright 1996-2000.";
    static String gplLicence =
        "/*\n *  <one line to give the program's name and a brief idea of what it does.>\n"+
        " *  Copyright (C) 20yy  <name of author>\n *\n *  This program is free software;"+
        " you can redistribute it and/or modify\n *  it under the terms of the GNU General "+
        "Public License as published by\n *  the Free Software Foundation; either version 2 of"+
        " the License, or\n *  (at your option) any later version.\n *\n *  This program is "+
        "distributed in the hope that it will be useful,\n *  but WITHOUT ANY WARRANTY; without"+
        " even the implied warranty of\n *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE."+
        "  See the\n *  GNU General Public License for more details.\n *\n *  You should have"+
        " received a copy of the GNU General Public License\n *  along with this program; if not, "+
        "write to the Free Software\n *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.\n */ ";

    /**
     * Suffix applied to the key used in resource file lookups for an image.
     */
    public static final String imageSuffix = "Image";

    /**
     * Suffix applied to the key used in resource file lookups for a label.
     */
    public static final String labelSuffix = "Label";

    /**
     * Suffix applied to the key used in resource file lookups for tooltip
     * text.
     */
    public static final String tipSuffix = "Tooltip";

  public void setFrimble(Frimble frimble){
      this.frimble = frimble;
    }

    private URL getResourceURL(String rscName) {
    java.net.URLClassLoader cl = (java.net.URLClassLoader) this.getClass().getClassLoader();
      return cl.findResource(rscName);
  }

    public Jipe()
    {
    setLayout(new BorderLayout());
        container = this;
        props=new PropertiesManager(this);

        // Force SwingSet to come up in the Cross Platform L&F
        /*try
        {
            if (props.LOOKANDFEEL.startsWith("Metal"))
                lnfName = "javax.swing.plaf.metal.MetalLookAndFeel";
            else if (props.LOOKANDFEEL.startsWith("Window"))
                lnfName ="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            else if (props.LOOKANDFEEL.startsWith("CDE/Motif"))
                lnfName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            else
                lnfName = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lnfName);
        }
        catch (Exception exc)
        {
            System.err.println("Error loading L&F: " + exc);
        }*/
        try
        {
            jipeProperties = new Properties();
            String userdir = System.getProperty("user.home");
            userdir = userdir + System.getProperty("file.separator") +
                      "jipecontrol.properties";
            File file = new File(userdir);
            if (file.exists())
                jipeProperties.load(new FileInputStream(userdir));
            else
            {
                jipeProperties.load(getResourceURL("net/jesktop/jipe/resources/jipecontrol.properties").openStream());
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        CURRENTDIR=new File(System.getProperty("user.dir"));
        tabbedPane = new JTabbedPane();
        tabbedPane2 = new JTabbedPane();
        tabbedPane3 = new JTabbedPane();
        speedbar=new JipeMethodFinder(this);
        sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, (new JScrollPane
                            (speedbar.results)));
        sp.setOneTouchExpandable(true);

        compileErrorPanel = new JList(compilerErrors = new DefaultListModel());
        compileErrorPanel.setVisibleRowCount(10);
        compileErrorPanel.setFont(new Font(props.FONTSTYLE, 0, props.FONTSIZE));
        compiler = new JipeCompiler(this);

        filesearchlistPanel= new JList();
        filesearchModel= new DefaultListModel();
        filesearchlistPanel.setVisibleRowCount(10);
        filesearchlistPanel.setFont(new Font(props.FONTSTYLE, 0, props.FONTSIZE));

        sp3 = new JSplitPane(
                  JSplitPane.VERTICAL_SPLIT, tabbedPane2, tabbedPane3);
        sp3.setOneTouchExpandable(true);
        sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, sp3);
        recentFiles=new JMenu ("Recent Files");
        recentProjects=new JMenu ("Recent Projects");

        showLeftPanel();
        showBottomPanel();
        statusBar = new StatusBar();
        showToolBars();
        popup = createPopupMenu("popup");
        popup2 = createPopupMenu("popup2");
        children = new Vector(4, 4);
        tabbedPane.setFont(new Font("dialog", Font.PLAIN, 11));
        tabbedPane2.setFont(new Font("dialog", Font.PLAIN, 11));
        tabbedPane3.setFont(new Font("dialog", Font.PLAIN, 11));
        jipeconsole=new JipeConsole(this);
        tabbedPane3.addTab("Console", new JScrollPane(jipeconsole.jipeConsole));
        tabbedPane3.addTab("Compile", new JScrollPane(compileErrorPanel));
        tabbedPane3.addTab("Search",new JScrollPane(filesearchlistPanel));
        tabbedPane3.setTabPlacement(JTabbedPane.BOTTOM);

//        tabbedPane2.setDropTarget(new DropTarget(this,this));

        tabbedPane2.addChangeListener(new ChangeListener() {

                                          public void stateChanged(ChangeEvent e)
                                          {
                                              JTabbedPane tp = (JTabbedPane)e.getSource();
                                              int index = tp.getSelectedIndex();
                                              if (index >= 0)
                                              {
                                                  String s = tp.getTitleAt(index);
                                                  GetWindow(new File(s));
                                              }
                                          }
                                      });

        tabbedPane2.addMouseListener(new MouseAdapter() {
                                         public void mouseClicked(MouseEvent me) {
                                             if ((me.getModifiers() & me.BUTTON3_MASK) != 0)popup2.show(tabbedPane2, me.getX(), me.getY());
                                         }
                                     });

        setBounds(props.XPOS,props.YPOS,props.XSIZE,props.YSIZE);

        sp.setDividerLocation(props.DIV1);

//        setVisible(true);


//        frimble.setIconImage(JipeIcon());

    }
    public void showStatusBar(){
    if(props.SHOWSTATUSBAR==true) this.add(statusBar, BorderLayout.SOUTH);
    else this.remove(statusBar);
  setVisible(true);
    }
    public void showToolBars(){
      if(panel!=null) this.remove(panel);
      panel=new JPanel();
        menubar = createMenubar();
        if(props.SHOWTOOLBAR1==true && props.SHOWTOOLBAR2==true)panel.setLayout(new GridLayout(3, 1));
        else if(props.SHOWTOOLBAR1==true || props.SHOWTOOLBAR2==true)panel.setLayout(new GridLayout(2, 1));
        else panel.setLayout(new GridLayout(1, 1));
        panel.add(menubar);
        if(props.SHOWTOOLBAR1==true)panel.add(createToolbar(1));
        if(props.SHOWTOOLBAR2==true)panel.add(createToolbar(2));
        this.add(panel, BorderLayout.NORTH);
        this.add(sp2, BorderLayout.CENTER);
        showStatusBar();
        setVisible(true);
    }

    public void showLeftPanel(){

      if(props.SHOWLEFTPANEL==false){
          sp2.setDividerSize(0);
          sp2.setOneTouchExpandable(false);

          sp2.setDividerLocation(0);
          sp2.remove(sp);
          sp2.resetToPreferredSizes();
        }
        else {
          sp2.setDividerSize(10);
          sp2.setOneTouchExpandable(true);
          sp2.setLeftComponent(sp);
          sp2.setDividerLocation(props.DIV2);
          sp2.resetToPreferredSizes();
        }
  }

  public void showBottomPanel(){
      if(props.SHOWBOTTOMPANEL==false){
          sp3.setDividerSize(0);
          sp3.setOneTouchExpandable(false);

          sp3.setDividerLocation(0);
          sp3.remove(tabbedPane3);
          sp3.resetToPreferredSizes();
        }
        else {
          sp3.setDividerSize(10);
          sp3.setOneTouchExpandable(true);
          sp3.setBottomComponent(tabbedPane3);
          sp3.setDividerLocation(props.DIV3);
          sp3.resetToPreferredSizes();
        }
  }
    ////////////////// Start of DnD Code /////////////////////
/*    public void dragEnter (DropTargetDragEvent evt) { }
    public void dragOver (DropTargetDragEvent evt) { }
    public void dragExit (DropTargetEvent evt) { }
    public void dragScroll (DropTargetDragEvent evt) { }
    public void dropActionChanged (DropTargetDragEvent evt) { }

    public void drop (DropTargetDropEvent evt)
    {
        DataFlavor[] flavors = evt.getCurrentDataFlavors();
        if (flavors == null)
            return;

        boolean dropCompleted = false;
        for (int i = flavors.length - 1; i >= 0; i--)
        {
            if (flavors[i].isFlavorJavaFileListType())
            {
                evt.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = evt.getTransferable();
                try
                {
                    Iterator iterator = ((List) transferable.getTransferData(flavors[0])).iterator();
                    while (iterator.hasNext())
                    {
                        loadChild( "Open",((File) iterator.next()));
                    }
                    dropCompleted = true;
                } catch (Exception e) { }
            }
        }
        evt.dropComplete(dropCompleted);
    }*/
    ////////////////// End of DnD Code /////////////////////


    public Image JipeIcon(){
        Image imageIcon=getToolkit().getImage(getResourceURL("net/jesktop/jipe/resources/jipeicon.gif"));
        return imageIcon;
    }

    /**
     * Converts a string to a keystroke.
     *
     * The string should be of the form <i>modifiers</i>+<i>shortcut</i> where
     * <i>modifiers</i> is any combination of A for Alt, C for Control, S for
     * Shift or M for Meta, and <i>shortcut</i> is either a single character,
     * or a keycode name from the <code>KeyEvent</code> class, without the
     * <code>VK_</code> prefix.
     *
     * @param keyStroke A string description of the key stroke
     */
    public static KeyStroke parseKeyStroke(String keyStroke)
    {
        if (keyStroke == null)
            return null;
        int modifiers = 0;
        int ch = '\0';
        int index = keyStroke.indexOf('+');
        if (index != -1)
        {
            for (int i = 0; i < index; i++)
            {
                switch (Character.toUpperCase(keyStroke.charAt(i)))
                {
                case 'A':
                    modifiers |= InputEvent.ALT_MASK;
                    break;
                case 'C':
                    modifiers |= InputEvent.CTRL_MASK;
                    break;
                case 'M':
                    modifiers |= InputEvent.META_MASK;
                    break;
                case 'S':
                    modifiers |= InputEvent.SHIFT_MASK;
                    break;
                }
            }
        }
        String key = keyStroke.substring(index + 1);
        if (key.length() == 1)
            ch = Character.toUpperCase(key.charAt(0));
        else if (key.length() == 0)
        {
            System.err.println("Invalid key stroke: " + keyStroke);
            return null;
        }
        else
        {
            try
            {
                ch = KeyEvent.class.getField("VK_".concat(key)).getInt(null);
            }
            catch (Exception e)
            {
                System.err.println("Invalid key stroke: " + keyStroke);
                return null;
            }
        }
        return KeyStroke.getKeyStroke(ch, modifiers);
    }

    /**
     * Create the menubar for the app.
     *
     * By default this pulls the definition of the menu from the associated
     * resource file.
     */
    protected JMenuBar createMenubar()
    {
        JMenuBar mb = new JMenuBar();
        String[] menuKeys = tokenize(jipeProperties.getProperty("menulist"));
        for (int i = 0; i < menuKeys.length; i++)
        {
            JMenu m = createMenu(menuKeys[i]);
            if (m != null)
            {
                m.setFont(new Font("dialog", Font.PLAIN, 11));
                mb.add(m);
            }
        }
        return mb;
    }

    /**
     * Create a menu for the app.
     *
     * By default this pulls the definition of the menu from the associated
     * resource file.
     */
    protected JMenu createMenu(String key)
    {
        String[] itemKeys = tokenize(jipeProperties.getProperty(key));
        JMenu menu = new JMenu(jipeProperties.getProperty(key + "Label"));
        if(jipeProperties.getProperty(key + "Label").equals("Window"))window=menu;

        menu.setMnemonic(jipeProperties.getProperty(key + "Label").charAt(0));
        for (int i = 0; i < itemKeys.length; i++)
        {
            if (itemKeys[i].equals("-"))
            {
                menu.addSeparator();
            }
            else
            {
                if(key.startsWith("view")){
                  boolean state=true;
                  if(itemKeys[i].equals("showleftpanelItem"))state=props.SHOWLEFTPANEL;
                  if(itemKeys[i].equals("showbottompanelItem"))state=props.SHOWBOTTOMPANEL;
                  if(itemKeys[i].equals("showToolBar1Item"))state=props.SHOWTOOLBAR1;
                  if(itemKeys[i].equals("showToolBar2Item"))state=props.SHOWTOOLBAR2;
                  if(itemKeys[i].equals("showStatusBarItem"))state=props.SHOWSTATUSBAR;
                    JMenuItem mi = createCheckBoxMenuItem(itemKeys[i], state);
                    menu.add(mi);
                }
               else if(itemKeys[i].startsWith("recentFiles"))menu.add(createRecentFilesList());
               else if(itemKeys[i].startsWith("recentProjects"))menu.add(createRecentProjectsList());
             //   menu.add(recentFiles);
             //   menu.add(recentProjects);
                else
                {
                    JMenuItem mi = createMenuItem(itemKeys[i]);
                    menu.add(mi);
                }
            }
        }
        return menu;
    }

    /**
     * This is the hook through which all menu items are created.
     *
     * It registers the result with the menuitem hashtable so that it can be
     * fetched with getMenuItem().
     *
     * @see #getMenuItem
     */
    protected JMenuItem createMenuItem(String cmd)
    {
        JMenuItem mi = new JMenuItem(jipeProperties.getProperty(cmd + labelSuffix));
        KeyStroke keyStroke = parseKeyStroke(jipeProperties.getProperty(cmd + "Key"));
        mi.setFont(new Font("dialog", Font.PLAIN, 11));
        mi.setAccelerator(keyStroke);
        URL url = getResource(cmd + imageSuffix);
        System.out.println("url2="+cmd + imageSuffix);
        if (url == null)url = getResource("blankItemImage");
        mi.setHorizontalTextPosition(JButton.RIGHT);
        System.out.println("url="+url);
        mi.setIcon(new ImageIcon(url));
        mi.addActionListener(this);
        mi.setActionCommand(cmd);
        return mi;
    }

    /**
     * This is the hook through which all CheckBox menu items are created.
     *
     * It registers the result with the menuitem hashtable so that it can be
     * fetched with getMenuItem().
     *
     * @see #getMenuItem
     */
    protected JMenuItem createCheckBoxMenuItem(String cmd, boolean state)
    {

        view = new JCheckBoxMenuItem(jipeProperties.getProperty(cmd + labelSuffix), state);
        KeyStroke keyStroke = parseKeyStroke(jipeProperties.getProperty(cmd + "Key"));
        view.setFont(new Font("dialog", Font.PLAIN, 11));
        view.setAccelerator(keyStroke);
        URL url = getResource(cmd + imageSuffix);
        if (url == null)url = getResource("blankItemImage");
        view.setHorizontalTextPosition(JButton.RIGHT);
        view.setIcon(new ImageIcon(url));
        view.addActionListener(this);
        view.setActionCommand(cmd);
        return view;
    }

  protected JMenu createRecentFilesList(){
                  recentFiles.removeAll();
                  rfiles=tokenize(props.RFILES);
                  recentFiles.setFont(new Font("dialog", Font.PLAIN, 11));
                  for(int j=0;j<rfiles.length;j++){
          rfileitem=new JMenuItem(rfiles[j]);
                  rfileitem.setFont(new Font("dialog", Font.PLAIN, 11));
                  recentFiles.add(rfileitem);
                  rfileitem.addActionListener(this);
              rfileitem.setActionCommand("recent_files_"+rfiles[j]);

                }
                return recentFiles;

    }
    protected JMenu createRecentProjectsList(){
                  recentProjects.removeAll();
                  rprojects=tokenize(props.RPROJECTS);
                  recentProjects.setFont(new Font("dialog", Font.PLAIN, 11));
                  for(int j=0;j<rprojects.length;j++){
                  rprojectitem=new JMenuItem(rprojects[j]);
                  rprojectitem.setFont(new Font("dialog", Font.PLAIN, 11));
                  recentProjects.add(rprojectitem);
                  rprojectitem.addActionListener(this);
              rprojectitem.setActionCommand("recent_projects_"+rprojects[j]);

                }
                return recentProjects;
    }

    protected URL getResource(String key)
    {
        String name = jipeProperties.getProperty(key);
        System.out.println("key " + key);
        if (name != null)
        {
			System.out.println("name " + name);
            URL url = getResourceURL("net/jesktop/jipe/" + name);
            return url;
        }
        return null;
    }
    protected Container getToolbar()
    {
        return toolbar;

    }
    protected JMenuBar getMenubar()
    {
        return menubar;
    }

    /**
     * Create the toolbar.
     *
     * By default this reads the resource file for the definition of the
     * toolbar.
     */
    private Component createToolbar(int toolrow)
    {
        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.putClientProperty("JToolBar.isRollover",Boolean.TRUE);
        String[] toolKeys = tokenize(jipeProperties.getProperty("toolbar" + toolrow));
        for (int i = 0; i < toolKeys.length; i++)
        {
            if (toolKeys[i].equals("-"))toolbar.add(Box.createHorizontalStrut(5));
            else toolbar.add(createTool(toolKeys[i]));
        }
        return toolbar;
    }

    /**
     * Hook through which every toolbar item is created.
     */
    protected Component createTool(String key)
    {
        return createToolbarButton(key);
    }

    /**
     * Create a button to go inside of the toolbar.
     *
     * By default this will load an image resource.  The image filename is
     * relative to the classpath (including the '.' directory if its a part of
     * the classpath), and may either be in a JAR file or a separate file.
     *
     * @param key The key in the resource file to serve as the basis of
     *            lookups.
     */
    protected JButton createToolbarButton(String key)
    {
        URL url = getResource(key + imageSuffix);
        JButton b = new JButton(new ImageIcon(url)) {

                        public float getAlignmentY()
                        {
                            return 0.5f;
                        }
                    };
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1, 1, 1, 1));
        b.setActionCommand(key);
        b.addActionListener(this);
        String tip = jipeProperties.getProperty(key + tipSuffix);
        if (tip != null)
        {
            b.setToolTipText(tip);
        }
        return b;
    }

    /**
     * Take the given string and chop it up into a series of strings on
     * whitespace boundries.
     *
     * This is useful for trying to get an array of strings out of the
     * resource file.
     */
    protected String[] tokenize(String input)
    {
        Vector v = new Vector();
        StringTokenizer t = new StringTokenizer(input,",");
        String[] cmd;
        while (t.hasMoreTokens())
            v.addElement(t.nextToken());
        cmd = new String[v.size()];
        for (int i = 0; i < cmd.length; i++)
            cmd[i] = (String)v.elementAt(i);
        return cmd;
    }

    /**
    * Create the popupMenu.
    *
    * By default this reads the resource file for the definition of the
    * toolbar.
    */
    private JPopupMenu createPopupMenu(String popupType)
    {
        JPopupMenu popUp = new JPopupMenu();
        String[] toolKeys = tokenize(jipeProperties.getProperty(popupType));
        for (int i = 0; i < toolKeys.length; i++)
        {
            if (toolKeys[i].equals("-"))
            {
                popUp.addSeparator();
            }
            else
            {
                popUp.add(createMenuItem(toolKeys[i]));
            }
        }
        return popUp;
    }

    /**
     * Respond to users requests
     */
    public void actionPerformed(ActionEvent e)
    {
        String source = e.getActionCommand();
        if(source.startsWith("recent_files_")){


          File file=new File(source.substring(13,source.length()));
          loadChild(file.getName(), file);
        }
        else if(source.startsWith("window_"))GetWindow(new File(source.substring(7,source.length())));
        else if(source.startsWith("recent_projects_"))
          {
            if(project!=null)CloseProject();
            project=new JipeProjectManager(this);
            project.OpenNewProject(new File(source.substring(16,source.length())));
        }
        else if (source.equals("opendocument"))
            loadChild("Load", null);
        else if (source.equals("newplain"))
            addChild(this, "Untitled", null);
        if (source.equals("quitItem"))
            close();
        else if (source.equals("newhtml"))
            addChild(this, "Html", null);
        else if (source.equals("newjava"))
            addChild(this, "Java", null);
        else if (project!=null && source.equals("addprojectItem"))
            project.addToProject();
        else if (project!=null && source.equals("removeprojectItem"))
            project.removeFromProject();
        else if (project!=null && source.equals("projectproperties"))
            project.ProjectProperties();
        else if(source.equals("clearrecentfilelistItem")){
          recentFiles.removeAll();
          props.RFILES="";
        }
        else if(source.equals("clearrecentprojectlistItem")){
          recentProjects.removeAll();
          props.RPROJECTS="";
        }
        else if (source.equals("jarFileManagerItem"))
        {
            jarFileManager = new JarFileManager(
                                 this, "Jipe JarFileManager");
        }
        else if (source.equals("classBrowserItem"))OpenClassBrowser();
        else if (source.equals("decompileItem"))DecompileClass();
        else if (source.equals("aboutItem"))
        {
            JOptionPane aboutPane = new JOptionPane();
            aboutPane.setFont(new Font("dialog", Font.PLAIN, 11));
            aboutPane.showMessageDialog(this, ABOUTMSG, "About Jipe", JOptionPane.INFORMATION_MESSAGE, new ImageIcon
                                        (getClass().getResource("resources/jipe.gif")));
        }
        else if (source.equals("helpItem"))
        {
            if (helpConsole == null)
                helpConsole = new HelpConsole(this);
        }
        else if (source.equals("javadocsItem"))
            OpenJavaDoc();
        else if (source.equals("closeallItem"))
            CloseAll();
        else if (project!=null && source.equals("closeprojectItem"))CloseProject();
        else if (source.equals("openproject")){
            if(project!=null)CloseProject();
            project=new JipeProjectManager(this);
            project.OpenNewProject();
        }
        else if (source.equals("newprojectItem")){
            if(project!=null)CloseProject();
            project=new JipeProjectManager(this);
            project.NewProject();
        }
        else if (project!=null && source.equals("saveprojectItem"))
            project.SaveProject();
        else if (source.equals("optionsItem")){
                    try{
            optiondialog = new IDEOptions(this);
                    } catch(Exception ex){}
        }

        else if (source.equals("optionsJavaItem")){
                    try{
            optionJavadialog = new JavaOptions(this);
                    } catch(Exception ex){}
        }
        else if (source.equals("compileallItem"))
            CompileAll();
        else if (source.equals("runScriptItem"))
            RunScript();
        else if (source.equals("debugItem"))
            RunCommandLine(props.DEBUG, "");

        else if (source.equals("debugappletItem"))
        {
            if((System.getProperty("file.separator")).equals("\\")){
                StringParser ps = new StringParser();
                RunCommandLine("appletviewer -debug",ps.makeAppletCommand(activeChild.file.getPath()));
            }
            else
                RunCommandLine("appletviewer -debug",activeChild.file.getPath());
        }
        else if(source.equals("codeCompleterItem")){
            JipeCodeCompleter codeCompleter= JipeCodeCompleter.makeJipeCodeCompleter(this,frimble,activeChild.textarea);
        }
        else if (source.equals("findinfilesItem"))getFindInFiles();
                else if(source.equals("showleftpanelItem")){
                  props.SHOWLEFTPANEL=invertBoolean(props.SHOWLEFTPANEL);
                  showLeftPanel();
                }
                else if(source.equals("showStatusBarItem")){
                  props.SHOWSTATUSBAR=invertBoolean(props.SHOWSTATUSBAR);
                  showStatusBar();
                }
                else if(source.equals("showbottompanelItem")){
                  props.SHOWBOTTOMPANEL=invertBoolean(props.SHOWBOTTOMPANEL);
                  showBottomPanel();
                }
                else if(source.equals("showToolBar1Item")){
                  props.SHOWTOOLBAR1=invertBoolean(props.SHOWTOOLBAR1);
                  showToolBars();
                }
                else if(source.equals("showToolBar2Item")){
                  props.SHOWTOOLBAR2=invertBoolean(props.SHOWTOOLBAR2);
                  showToolBars();
                }
        else if (source.equals("closeItem")){
            if(tabbedPane2.getTitleAt(tabbedPane2.getSelectedIndex()).equals("- Help -")){
                helpConsole.close();
                helpConsole=null;
            }
            else if (activeChild!=null)activeChild.close();
        }
        if (activeChild != null)
        {
            if (source.equals("findItem"))
                displayFindDialog();
            else if (source.equals("findNextItem"))
                FindNext();
            else if (source.equals("parseItem") && activeChild.file.getPath().endsWith
                     (".java"))
                speedbar.parse();
            else if (source.equals("gotoItem"))
                activeChild.Goto();
            else if (source.equals("saveItem"))
                activeChild.SaveFile(false);
            else if (source.equals("saveasItem"))
                activeChild.SaveFile(true);
            else if (source.equals("printItem"))
                Print();
            else if (source.equals("applicationItem"))
                RunCommandLine(props.JAVA, activeChild.getTitle());
            else if (source.equals("appletviewerItem")){
                if((System.getProperty("file.separator")).equals("\\")){
                    StringParser ps = new StringParser();
                    RunCommandLine(props.APPLETVIEWER,ps.makeAppletCommand(activeChild.file.getPath()));
                }
                else
                    RunCommandLine(props.APPLETVIEWER,activeChild.file.getPath());
            }
            else if (source.equals("compilerItem")){
                if((System.getProperty("file.separator")).equals("\\")){
                    StringParser ps = new StringParser();
                    RunCompiler(ps.ModifyPath(activeChild.file.getPath()));
                }
                else RunCompiler(activeChild.file.getPath());
            }
            else if (source.equals("browserItem"))
                RunCommandLine(props.BROWSER, activeChild.file.getPath());
            else if (source.equals("undoItem"))
                activeChild.undoAction();
            else if (source.equals("redoItem"))
                activeChild.redoAction();
            else if (source.equals("cutItem"))
                activeChild.textarea.cut();
            else if (source.equals("copyItem"))
                activeChild.textarea.copy();
            else if (source.equals("pasteItem"))
                activeChild.textarea.paste();
            else if (source.equals("selectItem"))
                activeChild.textarea.selectAll();
            else if (source.equals("deletelineItem")){
              DeleteLine deleteLine;
              deleteLine=new DeleteLine(activeChild.textarea);
            }
            else if (source.equals("lowercaseItem")){
              ToLowerCase toLowerCase;
              toLowerCase=new ToLowerCase(activeChild.textarea);
            }
            else if (source.equals("uppercaseItem")){
              ToUpperCase toUpperCase;
              toUpperCase=new ToUpperCase(activeChild.textarea);
            }
            else if (source.equals("commentItem")){
              SimpleComment simpleComment;
              simpleComment=new SimpleComment(activeChild.textarea);
            }
            else if (source.equals("boxcommentItem")){
              BoxComment boxComment;
              boxComment=new BoxComment(activeChild.textarea);
            }
            else if (source.equals("wingcommentItem")){
              WingComment wingComment;
              wingComment=new WingComment(activeChild.textarea);
            }
            else if( source.equals("removespacesItem")){
              RemoveSpaces removespaces;
              removespaces=new RemoveSpaces(activeChild.textarea);
            }
            else if( source.equals("indentleftItem")){
              LeftIndent leftIndent;
              leftIndent=new LeftIndent(activeChild.textarea);
            }
            else if( source.equals("indentrightItem")){
              RightIndent rightIndent;
              rightIndent=new RightIndent(activeChild.textarea);
            }
            else if (source.equals("indentItem"))
            {
                activeChild.indent();
                speedbar.parse();
                activeChild.textarea.setDirty();
                tabbedPane2.setForegroundAt(tabbedPane2.getSelectedIndex(), Color.red.darker());
            }
            else if (source.equals("insertDateItem"))
            {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat(
                                           "dd/MMM/yyyy - HH:mm:ss");
                activeChild.textarea.setSelectedText(sdf.format(date));
                activeChild.textarea.setDirty();
                tabbedPane2.setForegroundAt(tabbedPane2.getSelectedIndex(), Color.red.darker());
            }
            else if (source.equals("gplLicenceItem"))
            {
                activeChild.textarea.setSelectedText(gplLicence);
                activeChild.textarea.setDirty();
                tabbedPane2.setForegroundAt(tabbedPane2.getSelectedIndex(), Color.red.darker());
            }
        }
    }

    public boolean invertBoolean(boolean var){
      if(var==true)return false;
      else return true;
    }
    public void displayFindDialog()
    {
        if (find == null)
        {
            find =  Find.makeFind(frimble.getFrimbleContained(), activeChild.textarea);
        }
        if (find != null)
            Find.editor = activeChild.textarea;
        find.setVisible(true);
    }
    public void FindNext(){
        if(find==null){
          displayFindDialog();
        }
        else find.find();
    }
    public void getFindInFiles()
    {
        if (findin == null)
        {
            findin = FindInFiles.makeFindInFiles(this,frimble);
        }
        if (findin != null)
            findin.setVisible(true);
        findin.requestFocus();
    }
    public void close()
    {
        CloseAll();
        props.SaveOptions();
        if(children.isEmpty())System.exit(0);
    }
    public boolean GetWindow(File windowname)
    {
        for (int i = 0; i < window.getItemCount(); i++)
        {
            if (window.getItem(i) == null || children.isEmpty())
            {
                continue;
            }
            if (windowname.getName().equals(window.getItem(i).getText()))
            {
                if(windowname.getName().equals("- Help -")){
                    tabbedPane2.setSelectedIndex(tabbedPane2.indexOfTab("- Help -"));
                    statusBar.msgline.setText("Help Window");
                    activeChild=null;
                }
                else{
                    Editor child = (Editor)children.elementAt(i - 4);
                    if (child != null)
                    {
                        activeChild = child;
                        tabbedPane2.setSelectedIndex(tabbedPane2.indexOfTab(
                                                         activeChild.getTitle()));
                        statusBar.msgline.setText("Current Window : " +
                                                  activeChild.getTitle());
                        if (activeChild.getTitle().endsWith(".java"))
                            speedbar.parse();
                        else
                            speedbar.removeList();
                    }
                    statusBar.updateStatus(activeChild.textarea);
                    return true;
                }
            }
        }
        return false;
    }

    // This method used to update the details for an existing child

    public void addChild(Jipe parent, Editor child)
    {
        children.addElement((Object)child);
        statusBar.msgline.setText("Current Window : " + child.getTitle());
        window.add(windowitem = new JMenuItem(child.getTitle()));
        windowitem.setFont(new Font("dialog", Font.PLAIN, 11));
        windowitem.addActionListener(parent);
        windowitem.setActionCommand("window_"+child.getTitle());
        activeChild = child;
        if (child.getTitle().endsWith(".java"))
            speedbar.parse();
    }


    public void loadChild(String title, File importfile)
    {
        File file = importfile;
        if (title == "Load" || file != null)
        {
            if (title == "Load")file = getAddChildFileName("Open File");
            if (file != null)
            {
                if(fileChooser!=null)CURRENTDIR=fileChooser.getCurrentDirectory();
                if (GetWindow(file) == false)
                    addChild(this, file.getName(), file);
            }
        }
    }

    protected File getAddChildFileName(String title)
    {
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(CURRENTDIR);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle(title);
        fileChooser.addChoosableFileFilter(new TextFilter());
        fileChooser.addChoosableFileFilter(new JspFilter());
        fileChooser.addChoosableFileFilter(new HtmlFilter());
        fileChooser.addChoosableFileFilter(new ProjectFilter());
        fileChooser.addChoosableFileFilter(new CFilter());
        fileChooser.setFileFilter(new JavaFilter());
        int selected = fileChooser.showOpenDialog(container);
        if (selected == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        else if (selected == JFileChooser. CANCEL_OPTION)
            return null;
        return null;
    }

    public void addChild(Jipe parent, String title, File importfile)
    {
        Editor child;
        if (title == "Untitled" || title == "Html" || title == "Java")
        {
            child = new Editor(parent, title, null);
            if (title.equals("Html"))
                child.textarea.setText(
                    "\n<HTML>\n<HEAD>\n<TITLE> Your Title here</TITLE>\n</HEAD>\n<BODY>\n<H1>Your heading"+
                    " here</H1>\n<applet code=\"Your.class\" width=350 height=350>\n</applet>\n</BODY>\n</HTML>");
            title = title + window_num;
            child.setTitle(title);
            window_num++;
        }
        else if(title.endsWith(".project")){
            child = new Editor(parent, title, null);
            child.setTitle(title);
            child.textarea.setText("Project Details:\nAuthor:\nDate:\nProject Description\n\nNotes:");
            child.textarea.setDirty();
        }

        else
        {
            child = new Editor(parent, title, importfile);
            child.file = importfile;
            child.load(title);
            if(props.RFILES.indexOf(importfile.getAbsolutePath())<0){
              props.RFILES=importfile.getAbsolutePath()+","+props.RFILES;
            createRecentFilesList();
          }
        }
        child.setLocation(200, 0);
        child.setSize(440, screenHeight - 200);
        tabbedPane2.add(child.textarea, child.getTitle());
        tabbedPane2.setSelectedIndex(tabbedPane2.getTabCount() - 1);
        children.addElement((Object)child);
        statusBar.msgline.setText("Current Window : " + child.getTitle());
        window.add(windowitem = new JMenuItem(child.getTitle()));
        windowitem.setFont(new Font("dialog", Font.PLAIN, 11));
        windowitem.addActionListener(parent);
        windowitem.setActionCommand("window_"+child.getTitle());
        activeChild = child;
        activeChild.textarea.setCaretPosition(0);
        statusBar.updateStatus(activeChild.textarea);
        if (child.getTitle().endsWith(".java"))
            speedbar.parse();

    }

    public void Print()
    {
        String vers = System.getProperty("java.version");

        if (vers.compareTo("1.2") < 0)
        {
            //PrintManager print = new PrintManager(this, activeChild.textarea.getText());
        }
        else
        {
            Print print;
            print = new Print((PlainDocument)activeChild.textarea.getDocument(), new Font
                              (props.FONTSTYLE, 0, props.FONTSIZE));
        }

    }

    public void RunCommandLine(String nameOption, String nameExtension)
    {
        if ((nameOption.endsWith("java.exe") || nameOption.endsWith("java"))&&(nameExtension.endsWith(".java"))){

            nameExtension = nameExtension.substring(0,
                                                    nameExtension.length() - 5);
            if(props.OUTPUTDIR.trim().length()>=1)nameExtension="-classpath "+props.OUTPUTDIR+" " +nameExtension;
            else nameExtension="-classpath "+activeChild.file.getParent()+" "+nameExtension;

        }
        tabbedPane3.setSelectedIndex(0);
        System.out.println(nameOption+" "+nameExtension);
        try
        {
            jipeconsole.RunCommand(nameOption+" "+nameExtension);

        }
        catch (IOException e)
        {
            jipeconsole.jipeConsole.append(e+ System.getProperty("line.separator"));
        }
    }

    public void RunScript(){
        JFileChooser chooser=new JFileChooser();
        int state=chooser.showOpenDialog(null);
        File file=chooser.getSelectedFile();
        if(file!=null && state==JFileChooser.APPROVE_OPTION) {
            try
            {
                jipeconsole.RunCommand(file.getPath().toString());
            }
            catch (IOException e)
            {
                jipeconsole.jipeConsole.append(e+ System.getProperty("line.separator"));
            }
        }

    }
    protected void RunCompiler(String compileFile)
    {
        compilerErrors.removeAllElements();
        if(props.JAVAOPTIONSTRING.trim().length()>=1)compileFile=props.JAVAOPTIONSTRING+" " + compileFile;
        if(props.OUTPUTDIR.trim().length()>=1)compileFile="-d "+props.OUTPUTDIR+" " +compileFile;
        if(props.CLASSPATH.trim().length()>=1)compileFile="-classpath "+props.CLASSPATH+" " +compileFile;
        else compileFile="-classpath "+activeChild.file.getParent()+" "+compileFile;
        compiler.compile(props.COMPILER+ " ",compileFile );
    }
    protected void CompileAll()
    {
        String[] files = new File(System.getProperty("user.dir")).list();
        for (int i = 1; i < files.length; i++)
        {
            if (files[i].endsWith(".java"))
            {
                try
                {
                    jipeconsole.jipeConsole.append("Compiling:" + files[i]+ System.getProperty("line.separator"));
                    RunCompiler(files[i]);
                }
                catch (Exception err)
                {
                }
            }
            break;
        }
    }

    public void OpenClassBrowser(){
        ClassBrowser classBrowser=new ClassBrowser();
        classBrowser.setJipe(this);
        classBrowser.init(null);
        Frimble frimble2 = frimble.makeFrimble(classBrowser);
        frimble2.setTitle("Class Browser");
        frimble2.getFrimbleContained().setSize(new Dimension(700, 450));
        frimble2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frimble2.setIconImage(JipeIcon());
        frimble2.setVisible(true);
    }

    public void DecompileClass(){
        addChild(this, "Java", null);
        JipeDecompiler decompiler= JipeDecompiler.makeJipeDecompiler(this, frimble, activeChild.textarea);
    }
    void OpenJavaDoc()
    {
        String separator = System.getProperty("file.separator");
        try
        {
            Process ps = Runtime.getRuntime().exec(props.BROWSER + " " +props.JAVADOCS+"index.html");
        }
        catch (Exception err)
        {
            jipeconsole.jipeConsole.append("Error could not open browser " + err + System.getProperty("line.separator"));
        }
    }

    void CloseAll()
    {
        int count;
        if(helpConsole!=null){
            helpConsole.close();
            helpConsole=null;
        }
        while ((count = children.size()) > 0)
        {

            Editor child = (Editor)children.elementAt(0);
            if (child != null)
                child.close();
            if (count == children.size())
                break;
        }
    }
    void CloseProject(){
        project.CloseProject();
    }
    public static void main(String[] args)
    {
        Jipe window = new Jipe();

        System.out.println("Java version:"+System.getProperty("java.version"));
        System.out.println("OS Name:"+System.getProperty("os.name"));
        System.out.println("OS version:"+System.getProperty("os.version"));
        System.out.println("OS arch:"+System.getProperty("os.arch"));

        final Frimble frimble =JFrameFrimble.createJFrameFrimble(new JFrame("Jipe"));

        frimble.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frimble.addFrimbleListener(new FrimbleAdapter() {

           public void frimbleClosing(WindowEvent e) {
               frimble.dispose();
           }
        });

        frimble.getContentPane().setLayout(new BorderLayout());

        //window.setLayout(new BorderLayout());


        frimble.getContentPane().add(window, BorderLayout.CENTER);
        window.setFrimble(frimble);
        frimble.pack();

        frimble.setVisible(true);

        if (args.length > 0)
        {
            File file = new File(args[0]);
            if (file.getName().endsWith(".java") || file.getName().endsWith(
                        ".html") || file.getName().endsWith("htm"))
                window.loadChild(file.getName(), file);
            if (file.getName().endsWith(".prj"))
                window.project.OpenNewProject(file);
        }

    }
}

