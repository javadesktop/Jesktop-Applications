
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/

/*
 * MyJarUtil.java
 *
 */
package ranab.jar;



import java.util.zip.ZipEntry;
import java.util.jar.JarFile;

import java.io.File;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import ranab.gui.*;

import ranab.jar.view.*;

import org.jesktop.frimble.*;


/**
 * @author  Rana Bhattacharyya
 * @version
 */
public class MyJarUtil extends JPanel implements FrimbleAware {

    private final static String SPLASH_IMG = "images/jar.gif";
    private final static String APP_NAME = "JarUtil";
    private JMenuBar mjMenuBar;
    private JMenu mjFileMenu;
    private JMenu mjNewMenu;
    private JMenuItem mjNewJarMenu;
    private JMenuItem mjNewZipMenu;
    private JMenuItem mjOpenMenu;
    private JMenuItem mjAddMenu;
    private JMenuItem mjCloseMenu;
    private JMenu mjExtractMenu;
    private JMenuItem mjExtractAllMenu;
    private JMenuItem mjExtractSelectMenu;
    private JSeparator mjExitSeparator;
    private JMenuItem mjExitMenu;
    private JMenu mjCustomizeMenu;
    private JMenu mjLookFeelMenu;
    private JCheckBoxMenuItem mjMetalMenu;
    private JCheckBoxMenuItem mjWindowsMenu;
    private JCheckBoxMenuItem mjMotifMenu;
    private JMenu mjViewMenu;
    private JCheckBoxMenuItem mjTableViewMenu;
    private JCheckBoxMenuItem mjTreeViewMenu;
    private JMenu mjHelpMenu;
    private JMenuItem mjAboutMenu;
    private JPanel mjTopPane;
    private MyJarTable mJarTable;
    private MyJarTree mJarTree;
    private File mCurrentOpenFile;
    private MyCompressor mCompressor;
    private MyAddFileChooser mFileChooser;
    private Frimble frimble;
    private String zipFile;

    /**
     * Creates new form JFrame
     */
    public MyJarUtil() {
        this.setLayout(new BorderLayout());
    }

    private void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }

    /**
     * Method setFrimble
     *
     *
     * @param frimble
     *
     */
    public void setFrimble(Frimble frimble) {

        this.frimble = frimble;

        // show splash window
        //JWindow splashWin = MyGuiUtil.createSplashWindow(SPLASH_IMG);
        //if (splashWin != null) {
        //    splashWin.setVisible(true);
        //}
        initComponents();
        frimble.pack();
        setSize(new Dimension(700, 600));
        MyGuiUtil.setLocation(this);

        mCurrentOpenFile = null;
        mCompressor = null;

        setAppStatus();

        // hide splash window
        //if (splashWin != null) {
        //    splashWin.setVisible(false);
        //    splashWin.dispose();
        //}
        setVisible(true);

        if (zipFile != null) {
            mCurrentOpenFile = new File(zipFile);

            openFile();
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {

        mjMenuBar = new JMenuBar();
        mjFileMenu = new JMenu("File");
        mjNewMenu = new JMenu("New");
        mjNewJarMenu = new JMenuItem("Jar");
        mjNewZipMenu = new JMenuItem("Zip");
        mjOpenMenu = new JMenuItem("Open");
        mjAddMenu = new JMenuItem("Add");
        mjCloseMenu = new JMenuItem("Close");
        mjExtractMenu = new JMenu("Extract");
        mjExtractAllMenu = new JMenuItem("All");
        mjExtractSelectMenu = new JMenuItem("Selection");
        mjExitSeparator = new JSeparator();
        mjExitMenu = new JMenuItem("Exit");
        mjCustomizeMenu = new JMenu("Customize");
        mjLookFeelMenu = new JMenu("Look & Feel");
        mjMetalMenu = new JCheckBoxMenuItem("Metal", true);
        mjWindowsMenu = new JCheckBoxMenuItem("Windows", false);
        mjMotifMenu = new JCheckBoxMenuItem("Motif", false);
        mjViewMenu = new JMenu("View");
        mjTableViewMenu = new JCheckBoxMenuItem("Table", true);
        mjTreeViewMenu = new JCheckBoxMenuItem("Tree", false);
        mjHelpMenu = new JMenu("Help");
        mjAboutMenu = new JMenuItem("About");
        mFileChooser = new MyAddFileChooser();

        // new file menu
        mjNewJarMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjNewJarMenuActionPerformed(evt);
            }
        });
        mjNewMenu.add(mjNewJarMenu);
        mjNewZipMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjNewZipMenuActionPerformed(evt);
            }
        });
        mjNewMenu.add(mjNewZipMenu);
        mjFileMenu.add(mjNewMenu);

        // open menu
        mjOpenMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjOpenMenuActionPerformed(evt);
            }
        });
        mjFileMenu.add(mjOpenMenu);

        // add menu
        mjAddMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjAddMenuActionPerformed(evt);
            }
        });
        mjFileMenu.add(mjAddMenu);

        // close menu
        mjCloseMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjCloseMenuActionPerformed(evt);
            }
        });
        mjFileMenu.add(mjCloseMenu);

        // extract menu
        mjExtractAllMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjExtractAllMenuActionPerformed(evt);
            }
        });
        mjExtractMenu.add(mjExtractAllMenu);
        mjExtractSelectMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjExtractSelectMenuActionPerformed(evt);
            }
        });
        mjExtractMenu.add(mjExtractSelectMenu);
        mjFileMenu.add(mjExtractMenu);
        mjFileMenu.add(mjExitSeparator);

        // exit menu
        mjExitMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjExitMenuActionPerformed(evt);
            }
        });
        mjFileMenu.add(mjExitMenu);
        mjMenuBar.add(mjFileMenu);

        // look and feel menu
        mjMetalMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjMetalMenuActionPerformed(evt);
            }
        });
        mjLookFeelMenu.add(mjMetalMenu);
        mjWindowsMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjWindowsMenuActionPerformed(evt);
            }
        });
        mjLookFeelMenu.add(mjWindowsMenu);
        mjMotifMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjMotifMenuActionPerformed(evt);
            }
        });
        mjLookFeelMenu.add(mjMotifMenu);
        mjCustomizeMenu.add(mjLookFeelMenu);

        // view menu
        mjTableViewMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjTableViewMenuActionPerformed(evt);
            }
        });
        mjViewMenu.add(mjTableViewMenu);
        mjTreeViewMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjTreeViewMenuActionPerformed(evt);
            }
        });
        mjViewMenu.add(mjTreeViewMenu);
        mjCustomizeMenu.add(mjViewMenu);
        mjMenuBar.add(mjCustomizeMenu);

        // help menu
        mjAboutMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                mjAboutMenuActionPerformed(evt);
            }
        });
        mjHelpMenu.add(mjAboutMenu);
        mjMenuBar.add(mjHelpMenu);

        // draw table pane
        mjTopPane = new JPanel();

        mjTopPane.setLayout(new BorderLayout());

        mJarTable = new MyJarTable(mjTopPane);

        mJarTable.setActive(true);

        mJarTree = new MyJarTree(mjTopPane);

        mJarTree.setActive(false);
        mjTopPane.setBackground(Color.white);
        mjTopPane.add(mJarTable.getPanel(), BorderLayout.CENTER);
        this.add(mjTopPane, BorderLayout.CENTER);
        frimble.setJMenuBar(mjMenuBar);
    }

    ///// all menu handlers /////
    // new zip menu handle
    private void mjNewZipMenuActionPerformed(ActionEvent evt) {

        // get filename
        String newZip = MyGuiUtil.getFileName(this);

        if (newZip == null) {
            return;
        }

        // if exists get confirmation
        File zipFile = new File(newZip);

        if (zipFile.exists()) {
            boolean yes = MyGuiUtil.getConfirmation(this,
                                                    "Do you want to overwrite " + zipFile + "?");

            if (!yes) {
                return;
            }
        }

        // open compressor
        try {
            mCompressor = new MyZipCompressor(zipFile);

            mCompressor.addObserver(mJarTable);
            mCompressor.addObserver(mJarTree);
            mCompressor.open();

            mCurrentOpenFile = null;
        } catch (Exception ex) {
            mCompressor = null;
        }

        setAppStatus();
    }

    // new jar menu handle
    private void mjNewJarMenuActionPerformed(ActionEvent evt) {

        // get filename
        String newJar = MyGuiUtil.getFileName(this);

        if (newJar == null) {
            return;
        }

        // if exists get confirmation
        File jarFile = new File(newJar);

        if (jarFile.exists()) {
            boolean yes = MyGuiUtil.getConfirmation(this,
                                                    "Do you want to overwrite " + jarFile + "?");

            if (!yes) {
                return;
            }
        }

        // open compresor
        try {
            mCompressor = new MyJarCompressor(new File(newJar));

            mCompressor.addObserver(mJarTable);
            mCompressor.addObserver(mJarTree);
            mCompressor.open();

            mCurrentOpenFile = null;
        } catch (Exception ex) {
            mCompressor = null;
        }

        setAppStatus();
    }

    // open menu handler
    private void mjOpenMenuActionPerformed(ActionEvent evt) {

        String zipFile = MyGuiUtil.getFileName(this);

        if (zipFile == null) {
            return;
        }

        mCurrentOpenFile = new File(zipFile);
        mCompressor = null;

        openFile();
    }

    // add menu handler
    private void mjAddMenuActionPerformed(ActionEvent evt) {

        File selectedFile = mFileChooser.getFileName(this);

        if (selectedFile == null) {
            return;
        }

        boolean recursive = mFileChooser.isRecursive();
        boolean pathInfo = mFileChooser.isPathInfo();
        int level = mFileChooser.getCompressionLevel();

        mCompressor.addFile(selectedFile, recursive, pathInfo, level);
    }

    // close menu handle
    private void mjCloseMenuActionPerformed(ActionEvent evt) {

        mCompressor.close();

        mCurrentOpenFile = new File(mCompressor.toString());
        mCompressor = null;

        setAppStatus();
    }

    // extract all menu handler
    private void mjExtractAllMenuActionPerformed(ActionEvent evt) {

        String dir = MyGuiUtil.getDirName(this);

        if (dir == null) {
            return;
        }

        File zipDir = new File(dir);
        MyJarExtractor extr = new MyJarAllExtractor(mCurrentOpenFile, zipDir);

        new MyExtractorDialog((frimble.getFrimbleContained() instanceof JFrame)
                              ? (JFrame) frimble.getFrimbleContained() : null, extr);
    }

    // extract selection menu handler
    private void mjExtractSelectMenuActionPerformed(ActionEvent evt) {

        MyJarUI currUI = null;

        if (mJarTable.isActive()) {
            currUI = mJarTable;
        } else {
            currUI = mJarTree;
        }

        ZipEntry ze[] = currUI.getSelectedEntries();

        if (ze == null) {
            return;
        }

        String dirName = MyGuiUtil.getDirName(this);

        if (dirName == null) {
            return;
        }

        File dirFile = new File(dirName);
        MyJarExtractor extr = new MyJarSelectiveExtractor(ze, mCurrentOpenFile, dirFile);

        new MyExtractorDialog((frimble.getFrimbleContained() instanceof JFrame)
                              ? (JFrame) frimble.getFrimbleContained() : null, extr);
    }

    // exit menu handles
    private void mjExitMenuActionPerformed(ActionEvent evt) {

        if (MyGuiUtil.getConfirmation(this, "Do you really want to exit?")) {
            frimble.setVisible(false);
            frimble.dispose();

            //System.exit(0);
        }
    }

    // windows L&F menu handle
    private void mjWindowsMenuActionPerformed(ActionEvent evt) {

        mjMetalMenu.setState(false);
        mjWindowsMenu.setState(true);
        mjMotifMenu.setState(false);
        menuLfHandle("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }

    // metal L&F menu handle
    private void mjMetalMenuActionPerformed(ActionEvent evt) {

        mjMetalMenu.setState(true);
        mjWindowsMenu.setState(false);
        mjMotifMenu.setState(false);
        menuLfHandle("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");
    }

    // motif L&F menu handle
    private void mjMotifMenuActionPerformed(ActionEvent evt) {

        mjMetalMenu.setState(false);
        mjWindowsMenu.setState(false);
        mjMotifMenu.setState(true);
        menuLfHandle("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    }

    // table miew menu handle
    private void mjTableViewMenuActionPerformed(ActionEvent evt) {

        mjTableViewMenu.setState(true);
        mjTreeViewMenu.setState(false);

        if (!mJarTable.isActive()) {
            changePanel(mJarTable.getPanel());
        }

        mJarTable.setActive(true);
        mJarTree.setActive(false);
    }

    // tree view menu handle
    private void mjTreeViewMenuActionPerformed(ActionEvent evt) {

        mjTableViewMenu.setState(false);
        mjTreeViewMenu.setState(true);

        if (!mJarTree.isActive()) {
            changePanel(mJarTree.getPanel());
        }

        mJarTable.setActive(false);
        mJarTree.setActive(true);
    }

    /**
     * about menu handler
     */
    private void mjAboutMenuActionPerformed(ActionEvent evt) {

        MyAboutDialog abtDlg =
            new MyAboutDialog((frimble.getFrimbleContained() instanceof JFrame)
                              ? (JFrame) frimble.getFrimbleContained() : null, true);

        abtDlg.setImage(SPLASH_IMG);
        abtDlg.setText("rana_b@yahoo.com");
        abtDlg.setButtonText("Close");
        abtDlg.setForegroundColor(Color.blue);
        abtDlg.display();
    }

    /**
     * open jar/zip file
     */
    private void openFile() {

        MyJarViewer jarView = new MyJarViewer(mCurrentOpenFile);

        jarView.addObserver(mJarTable);
        jarView.addObserver(mJarTree);
        jarView.view();
        setAppStatus();
    }

    /**
     * set look and feel
     */
    private void menuLfHandle(String name, String lnfName) {

        try {
            UIManager.setLookAndFeel(lnfName);
            SwingUtilities.updateComponentTreeUI(this);
            MyGuiUtil.updateLnF();
            mJarTable.updateLnF();
            mJarTree.updateLnF();
        } catch (ClassNotFoundException ex) {
            MyGuiUtil.showErrorMessage(this, "Class " + lnfName + " not found.");
        } catch (InstantiationException ex) {
            MyGuiUtil.showErrorMessage(this, "Cannot instantiate " + lnfName);
        } catch (UnsupportedLookAndFeelException ex) {
            MyGuiUtil.showErrorMessage(this, "Unsupported Look & Feel " + name);
        } catch (Exception ex) {
            MyGuiUtil.showErrorMessage(this, ex.getLocalizedMessage());
        }
    }

    /*
     * Handle window closing event.
     */

    /*   protected void processWindowEvent(WindowEvent e) {

           int id = e.getID();
           if (id == WindowEvent.WINDOW_CLOSING) {
               if ( !MyGuiUtil.getConfirmation(this, "Do you really want to exit?") ) {
                   return;
               }

               super.processWindowEvent(e);
               frimble.setVisible(false);
               frimble.dispose();
               //System.exit(0);
           } else {
               super.processWindowEvent(e);
           }
       }*/

    /**
     * set menu status
     */
    private void setAppStatus() {

        // set window title
        if (mCurrentOpenFile != null) {
            frimble.setTitle(APP_NAME + " - " + mCurrentOpenFile.getAbsolutePath());
        } else if (mCompressor != null) {
            frimble.setTitle(APP_NAME + " - " + mCompressor.toString());
        } else {
            frimble.setTitle(APP_NAME);
        }

        // set menu status
        if (mCurrentOpenFile != null) {
            mjNewJarMenu.setEnabled(true);
            mjNewZipMenu.setEnabled(true);
            mjOpenMenu.setEnabled(true);
            mjAddMenu.setEnabled(false);
            mjCloseMenu.setEnabled(false);
            mjExtractAllMenu.setEnabled(true);
            mjExtractSelectMenu.setEnabled(true);
        } else if (mCompressor != null) {
            mjNewJarMenu.setEnabled(false);
            mjNewZipMenu.setEnabled(false);
            mjOpenMenu.setEnabled(false);
            mjAddMenu.setEnabled(true);
            mjCloseMenu.setEnabled(true);
            mjExtractAllMenu.setEnabled(false);
            mjExtractSelectMenu.setEnabled(false);
        } else {
            mjNewJarMenu.setEnabled(true);
            mjNewZipMenu.setEnabled(true);
            mjOpenMenu.setEnabled(true);
            mjAddMenu.setEnabled(false);
            mjCloseMenu.setEnabled(false);
            mjExtractAllMenu.setEnabled(false);
            mjExtractSelectMenu.setEnabled(false);
        }
    }

    /**
     * change panel
     */
    private void changePanel(JPanel currPane) {

        mjTopPane.removeAll();
        mjTopPane.add(currPane, BorderLayout.CENTER);
        mjTopPane.validate();
        mjTopPane.repaint();
    }

    // program starting point

    /**
     * Method main
     *
     *
     * @param args
     *
     */
    public static void main(String args[]) {

        String fileStr = null;

        if (args.length > 0) {
            fileStr = args[0];
        }

        Frimble frimble = JFrameFrimble.createJFrameFrimble(new JFrame());
        MyJarUtil mj = new MyJarUtil();

        mj.setZipFile(fileStr);
        mj.setFrimble(frimble);
        frimble.setVisible(true);
    }
}
