
package jcdsee.main;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.net.*;
import java.awt.*;
import java.awt.event.*;

import jcdsee.folderbrowser.*;
import jcdsee.imagebrowser.*;
import jcdsee.fullscreen.*;
import jcdsee.settings.*;
import jcdsee.preview.*;
import jcdsee.help.*;
import jcdsee.resources.*;
import org.jesktop.frimble.*;

/** The main frame of JCDSee
 */
public class MainFrame extends JFrimble {

  public final static String FULLSCREENPANEL = "Fullscreen View";
  public final static String BROWSEVIEWPANEL = "Browse View";

  // Menu bar
  private JMenuBar menuBar;

  // Tool bar
  private JToolBar toolBar;

  // Status bar
  private StatusBar statusBar;

  // Help dialog
  HelpDialog helpDialog;

  // View options button group
  private ButtonGroup viewButtonGroup;

  // All panels
  private JPanel centerPanel;

  private FullScreenPanel fullScreenPanel;

  private JSplitPane horizontalSplit;
  private JSplitPane verticalSplit;

  private FolderBrowser folderBrowser;
  private ImageBrowser imageViewPanel;
  private PreviewPanel previewPanel;

  // which view mode are we in?
  private String mode;

  // used to change the text of the slideshow
  // start / stop menu.
  private JMenuItem toggleSlideShowItem;

  private SlideShowTrigger slideShowTrigger;

  private JMenuItem zoomInItem;
  private JMenuItem zoomOutItem;
  private JMenuItem zoomToNormalItem;

  private JMenuItem fullScreenItem;

  private JMenu navMenu;

  /** Constructor
   */
  public MainFrame() {
    super();

    // Load settings from file
    Settings.loadSettings();

    // Create Menu bar
    menuBar = new JMenuBar();

    // Create Tool bar
    toolBar = new JToolBar() {
      public boolean isFocusTraversable() { return false; } };

    // Create Status bar
    statusBar = new StatusBar();

    // Create help dialog and set indes
    helpDialog = new HelpDialog(this, "JCDSee help", false);
    helpDialog.setIndexURL(Resources.MANUAL_INDEX);

    // Create file menu
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('F');

    // Add file items
    JMenuItem fileSettings = new JMenuItem("Settings");
    fileSettings.setMnemonic('S');
    fileSettings.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  fileSettings_actionPerformed();
      }
    });

    JMenuItem fileExit = new JMenuItem("Exit");
    fileExit.setMnemonic('x');
    fileExit.setAccelerator(KeyStroke.getKeyStroke('Q', KeyEvent.ALT_MASK));
    fileExit.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  fileExit_actionPerformed();
      }
    });

    fileMenu.add( fileSettings );
    fileMenu.add( new JSeparator() );
    fileMenu.add( fileExit );

    // Create view menu
    JMenu viewMenu = new JMenu("View");
    viewMenu.setMnemonic('V');

    // Add view items
    JRadioButtonMenuItem viewThumbnail = new JRadioButtonMenuItem("Thumbnail view", true);
    viewThumbnail.setMnemonic('T');
    viewThumbnail.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  viewThumbnail_actionPerformed();
      }
    });
    viewThumbnail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

    JRadioButtonMenuItem viewList = new JRadioButtonMenuItem("List view", false);
    viewList.setMnemonic('L');
    viewList.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  viewList_actionPerformed();
      }
    });
    viewList.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));

    JRadioButtonMenuItem viewFullscreen = new JRadioButtonMenuItem("Fullscreen view", false);
    viewFullscreen.setMnemonic('F');
    //viewFullscreen.setEnabled(false);
    viewFullscreen.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  viewFullscreen_actionPerformed();
      }
    });
    viewFullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));

    viewButtonGroup = new ButtonGroup();
    viewButtonGroup.add( viewThumbnail );
    viewButtonGroup.add( viewList );
    viewButtonGroup.add( viewFullscreen );

    JCheckBoxMenuItem viewToolbar = new JCheckBoxMenuItem("Toolbar", true);
    viewToolbar.setMnemonic('O');
    viewToolbar.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  viewToolbar_actionPerformed();
      }
    });

    JCheckBoxMenuItem viewStatusbar = new JCheckBoxMenuItem("Statusbar", true);
    viewStatusbar.setMnemonic('S');
    viewStatusbar.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  viewStatusbar_actionPerformed();
      }
    });

    viewMenu.add( viewThumbnail );
    viewMenu.add( viewList );
    viewMenu.add( viewFullscreen );
    viewMenu.add( new JSeparator() );
    viewMenu.add( viewToolbar );
    viewMenu.add( viewStatusbar );

    // create Navigation menu
    navMenu = new JMenu("Navigation");

    JMenuItem nextItem = new JMenuItem("Next");
    nextItem.setMnemonic('N');
    nextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
    nextItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  // change picture to next
  imageViewPanel.selectNextImage(false);
      }
    });

    JMenuItem previousItem = new JMenuItem("Previous");
    previousItem.setMnemonic('P');
    previousItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,
                   0));
    previousItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  // change picture to next
  imageViewPanel.selectPreviousImage(false);
      }
    });

    fullScreenItem = new JMenuItem("Fullscreen");
    fullScreenItem.setMnemonic('F');
    fullScreenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
               0));
    fullScreenItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  // view in fullscreen view
  changeMode( FULLSCREENPANEL );
      }
    });

    zoomInItem = new JMenuItem("Zoom in");
    zoomInItem.setMnemonic('I');
    zoomInItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_ADD, 0 ));
    zoomInItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  // zoom in full screen
  fullScreenPanel.zoomIn();
      }
    });

    zoomOutItem = new JMenuItem("Zoom out");
    zoomOutItem.setMnemonic('O');
    zoomOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0));
    zoomOutItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  // zoom in full screen
  fullScreenPanel.zoomOut();
      }
    });

    zoomToNormalItem = new JMenuItem("Zoom to normal");
    zoomToNormalItem.setMnemonic('N');
    zoomToNormalItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  // zoom to normal (100%)
  fullScreenPanel.zoomToNormal();
      }
    });

    toggleSlideShowItem = new JMenuItem("Start slideshow");
    toggleSlideShowItem.setMnemonic('S');
    toggleSlideShowItem.setAccelerator(KeyStroke.getKeyStroke(
   KeyEvent.VK_F8, 0 ));
    toggleSlideShowItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  slideShowTrigger.toggleSlideShow();
      }
    });

    navMenu.add(nextItem);
    navMenu.add(previousItem);
    navMenu.add(fullScreenItem);
    navMenu.addSeparator();
    navMenu.add(zoomInItem);
    navMenu.add(zoomOutItem);
    navMenu.add(zoomToNormalItem);
    navMenu.addSeparator();
    navMenu.add(toggleSlideShowItem);

    // add listener to change slideshow text
    navMenu.addMenuListener(new MenuListener() {
      public void menuSelected(MenuEvent e) {
  updateNavMenu();
      }

      public void menuDeselected(MenuEvent e) { }
      public void menuCanceled(MenuEvent e) { }
    });

    // Create help menu
    JMenu helpMenu = new JMenu("Help");
    helpMenu.setMnemonic('H');

    // Add help items
    JMenuItem helpManual = new JMenuItem("Manual" );
    helpManual.setMnemonic('M');
    helpManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    helpManual.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  helpManual_actionPerformed();
      }
    });

    JMenuItem helpAbout = new JMenuItem("About");
    helpAbout.setMnemonic('A');
    helpAbout.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
  helpAbout_actionPerformed();
      }
    });

    helpMenu.add( helpManual );
    helpMenu.add( helpAbout );

    // Add all menus
    menuBar.add( fileMenu );
    menuBar.add( viewMenu );
    menuBar.add( navMenu );
    menuBar.add( helpMenu );

    this.setJMenuBar( menuBar );

    // Create toolbar
    toolBar = new JToolBar();

    JButton addedButton;

    toolBar.add( new AbstractAction( "", Resources.ICON_THUMBNAIL ) {
      public void actionPerformed(ActionEvent e) {
  viewThumbnail_actionPerformed();
      }
    }).setToolTipText("Thumnail view");

    toolBar.add( new AbstractAction( "", Resources.ICON_LIST ) {
      public void actionPerformed(ActionEvent e) {
  viewList_actionPerformed();
      }
    }).setToolTipText("List view");

    toolBar.add( new AbstractAction( "", Resources.ICON_FULLSCREEN ) {
      public void actionPerformed(ActionEvent e) {
  viewFullscreen_actionPerformed();
      }
    }).setToolTipText("Fullscreen view");

    toolBar.addSeparator();
    toolBar.add( new AbstractAction( "", Resources.ICON_SETTINGS ) {
      public void actionPerformed(ActionEvent e) {
  fileSettings_actionPerformed();
      }
    }).setToolTipText("Settings");

    toolBar.addSeparator();
    toolBar.add(new AbstractAction( "", Resources.ICON_HELP ) {
      public void actionPerformed(ActionEvent e) {
  helpManual_actionPerformed();
      }
    }).setToolTipText("Help");

    // Add window listener
    addFrimbleListener(new FrimbleAdapter() {
      public void frimbleClosing(FrimbleEvent e) {
  fileExit_actionPerformed();
      }
    });

    JComponent c;
    int i = 0;

    while( (c = (JComponent)toolBar.getComponentAtIndex(i++)) != null ) {
      c.setAlignmentY( JFrame.CENTER_ALIGNMENT );
    }

    // Create framework

    // Do layout
    imageViewPanel = new ImageBrowser();

    previewPanel = new PreviewPanel();

    // create fullscreen panel
    fullScreenPanel = new FullScreenPanel(this);

    folderBrowser = new FolderBrowser();
    folderBrowser.addDirectoryChangeListener( statusBar );
    folderBrowser.addDirectoryChangeListener( imageViewPanel );
    folderBrowser.addDirectoryChangeListener( previewPanel );
    folderBrowser.addDirectoryChangeListener( fullScreenPanel );

    // listen for image change events
    imageViewPanel.addImageSelectionListener( statusBar );
    imageViewPanel.addImageSelectionListener( previewPanel );

    verticalSplit = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
            folderBrowser,
            previewPanel );
    verticalSplit.setContinuousLayout(true);
    verticalSplit.invalidate();

    horizontalSplit = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
          verticalSplit,
          imageViewPanel);
    horizontalSplit.setContinuousLayout(true);
    horizontalSplit.invalidate();

    centerPanel = new JPanel( new CardLayout() );
    centerPanel.add( horizontalSplit, BROWSEVIEWPANEL );
    centerPanel.add( fullScreenPanel, FULLSCREENPANEL );

    this.getContentPane().add( toolBar, BorderLayout.NORTH );
    this.getContentPane().add( statusBar, BorderLayout.SOUTH );
    this.getContentPane().add( centerPanel, BorderLayout.CENTER );

    // listen for image change events
    imageViewPanel.addImageSelectionListener( statusBar );
    imageViewPanel.addImageSelectionListener( previewPanel );
    imageViewPanel.addImageSelectionListener( fullScreenPanel );

    // Create new slideshow trigger
    slideShowTrigger = new SlideShowTrigger();
    Settings.addSettingsListener( slideShowTrigger );

    // Process settings

    // Set frame size
    if( Settings.getStartupRememberPosition() ) {
      this.setSize( Settings.getLastSize() );
      this.setLocation( Settings.getLastLocation().x, Settings.getLastLocation().y );
    }
    else
      this.setSize( new Dimension( 600, 500 ) );

    // Startup mode
    if( Settings.getStartupView().equals(
    StartupSettings.STARTUP_VIEW_THUMBNAIL ) )
      imageViewPanel.changeMode( ImageBrowser.THUMBNAIL_MODE );
    else
      imageViewPanel.changeMode( ImageBrowser.LIST_MODE );

    // Set browser mode
    mode = BROWSEVIEWPANEL;
    changeMode(BROWSEVIEWPANEL);

    this.pack();

    // restore splitpane proportions
    if( Settings.getStartupRememberPosition() ) {
      verticalSplit.setDividerLocation( Settings.
          getVerticalSplitPane());
      horizontalSplit.setDividerLocation( Settings.
            getHorizontalSplitPane());
    } else {
      // Set splitpanes locations
      verticalSplit.setDividerLocation(0.7f);
      horizontalSplit.setDividerLocation(0.4f);
    }

    // Maximize
    if( Settings.getStartupMaximizeWindow() ) {
      System.out.println("hej");
      this.setLocation( 0,0 );
      this.setSize( Toolkit.getDefaultToolkit().getScreenSize() );
    }
    this.setVisible( true );
  }

  /** Select the next image
   */
  public void nextImage() {
    imageViewPanel.selectNextImage( false );
  }

  /** Selects the previous image
   */
  public void prevImage() {
    imageViewPanel.selectPreviousImage( false );
  }

  /** Returns true if the slide show is running
   * @param true if slideshow is running
   */
  public boolean isSlideShowRunning() {
    return slideShowTrigger.isSlideShowRunning();
  }

  /** Toggles the slideshow on & off
   */
  public void toggleSlideShow() {
    slideShowTrigger.toggleSlideShow();
  }

  // update the nav menu
  private void updateNavMenu() {
    // check slideshow text
    if (slideShowTrigger.isSlideShowRunning())
      toggleSlideShowItem.setText("Stop slideshow");
    else
      toggleSlideShowItem.setText("Start slideshow");

    // Check if zoom and fullscreen available
    boolean zoom = (mode.equals(FULLSCREENPANEL));

    zoomInItem.setEnabled( zoom );
    zoomOutItem.setEnabled( zoom );

    zoomToNormalItem.setEnabled( zoom );

    fullScreenItem.setEnabled( !zoom );
  }

  // used to restore focus from button in the toolbar
  private void restoreFocusFromToolbar() {
    // restore focus to previous view
      if (mode.equals(BROWSEVIEWPANEL))
  imageViewPanel.requestFocus();
      else
  fullScreenPanel.requestFocus();
  }

  private void changeMode( String mode ) {
    this.mode = mode;
    ((CardLayout)centerPanel.getLayout()).show( centerPanel, mode );
  }

  private void fileSettings_actionPerformed() {
    SettingsDialog settingsDialog = new SettingsDialog(this);
    settingsDialog.setVisible(true);
    settingsDialog.dispose();

    restoreFocusFromToolbar();
  }

  private void fileExit_actionPerformed() {
    // Save position info
    Settings.loadSettings();

    Settings.setLastLocation( this.getLocation() );
    Settings.setLastSize( this.getSize() );

    double vSpan = (double)verticalSplit.getMaximumDividerLocation();
    double vProp = (double)verticalSplit.getDividerLocation() / vSpan;
    Settings.setVerticalSplitPane(vProp);

    double hSpan = (double)horizontalSplit.getMaximumDividerLocation();
    double hProp = (double)horizontalSplit.getDividerLocation() / hSpan;
    Settings.setHorizontalSplitPane(hProp);

    Settings.saveSettings();

	this.dispose();
    //System.exit(0);
  }

  private void viewThumbnail_actionPerformed() {
    changeMode( BROWSEVIEWPANEL );
    imageViewPanel.changeMode( ImageBrowser.THUMBNAIL_MODE );
    imageViewPanel.requestFocus();
  }

  private void viewList_actionPerformed() {
    changeMode( BROWSEVIEWPANEL );
    imageViewPanel.changeMode( ImageBrowser.LIST_MODE );
    imageViewPanel.requestFocus();
  }

  private void viewFullscreen_actionPerformed() {
    changeMode( FULLSCREENPANEL );
    fullScreenPanel.requestFocus();
  }

  private void viewToolbar_actionPerformed() {
    toolBar.setVisible( !toolBar.isVisible() );
    ((JPanel)this.getContentPane()).revalidate();
  }

  private void viewStatusbar_actionPerformed() {
    statusBar.setVisible( !statusBar.isVisible() );
    ((JPanel)this.getContentPane()).revalidate();
  }

  private void helpManual_actionPerformed() {
    // show the manual
    if( Resources.MANUAL_INDEX != null ) {
      helpDialog.gotoPage(Resources.MANUAL_INDEX);
      helpDialog.centerOnScreen();
      helpDialog.setVisible(true);
    }
    restoreFocusFromToolbar();
  }

  private void helpAbout_actionPerformed() {
    JOptionPane.showMessageDialog(null,
          new String[] { "JCDSee 1.0",
             "Copyright 2000 Martin Lansler & Johan Karlsson",
             "paf-56@mdstud.chalmers.se"},
          "About JCDSee",
          JOptionPane.INFORMATION_MESSAGE,
          null);
  }

  /** The main method
   */
  public static void main( String[] args ) {
    // start images in new windows

    // create the killer thread
    KillerThread killer = new KillerThread();

    if (args.length > 0) {
      for (int i=0; i < args.length; i++) {
  ImageIcon image = new ImageIcon(args[i]);
  if (image != null) {
    SinglePictureFrame spf = new SinglePictureFrame(image, args[i]);
    spf.addFrimbleListener(killer);
    killer.addWindow();
  }
      }
    } else {
      try {
  (new MainFrame()).setTitle("JCDSee");
      } catch(RuntimeException rte) {}
      catch(Exception e) { }
    }
  }

  class SlideShowTrigger implements SettingsListener {

    private Timer slideShowTimer;

    public SlideShowTrigger() {

      // create the slideshow timer
      int time = SlideShowSettings.convertToMilliSeconds(
          Settings.getSlideShowTime());

      slideShowTimer = new Timer(time, new ActionListener() {
  public void actionPerformed(ActionEvent ev) {
    boolean wrap = Settings.getSlideShowWrapEnabled();

    // Check if last and not wrap
    if( !wrap && imageViewPanel.isLastImage() ) {
      slideShowTimer.stop();
      return;
    }

    // display the next image with wrap
    imageViewPanel.selectNextImage( wrap );
  }
      });
    }

    public void settingsChanged(SettingsEvent e) {
      // Update timer
      int time = SlideShowSettings.convertToMilliSeconds(
          Settings.getSlideShowTime());

      slideShowTimer.setInitialDelay(time);
      slideShowTimer.setDelay(time);
    }

    public void toggleSlideShow() {
      if (slideShowTimer.isRunning())
  slideShowTimer.stop();
      else
  slideShowTimer.start();
    }

    public boolean isSlideShowRunning() {
      return slideShowTimer.isRunning();
    }

  }

}
