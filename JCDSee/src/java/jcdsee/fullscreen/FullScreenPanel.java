package jcdsee.fullscreen;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import jcdsee.settings.*;
import jcdsee.imagebrowser.*;
import jcdsee.util.*;
import jcdsee.main.*;
import jcdsee.folderbrowser.*;

/**
 * Provides a fullscreen view panel of a picture with
 * zooming capability.
 */
public class FullScreenPanel extends JPanel implements ImageSelectionListener, SettingsListener, DirectoryEventListener {

  // how much to decrease alpha in fade effect
  private static final int ALPHA_DECREMENT = 5;
  
  private ImageInfo info;
  private Image image;

  // how much we should zoom
  private int zoomFactor;

  private JLabel imageLabel;
  
  // timers
  private Timer timer;

  // the alpha value used in zoomLabel
  private int alpha;

  private JLabel zoomLabel;

  private MainFrame mainFrame;

  private JPopupMenu popupMenu;
  private JMenuItem toggleSlideShowItem;

  /** Constructor
   */
  public FullScreenPanel(MainFrame main) {
    super();
    
    this.mainFrame = main;

    // listen for show event ==> load current image
    this.addComponentListener( new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
	// return current picture to original size
	if (image != null) {
	  zoomFactor = 0;
	  loadImage(false);
	}

	if (image == null && info != null) {
	  loadImage(true);
	  // we should show the file name
	  zoomLabel.setText(info.getFile().getName());

	}
      }
    });

    // zero zoom...
    zoomFactor = 0;

    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    imageLabel.setHorizontalTextPosition(JLabel.CENTER);
    imageLabel.setVerticalAlignment(JLabel.CENTER);
    imageLabel.setVerticalTextPosition(JLabel.CENTER);

    zoomLabel = new JLabel();
    zoomLabel.setFont(new Font("Verdane", Font.BOLD, 24));

    // center the zoomLabel on top of the imageLabel
    zoomLabel.setAlignmentX(0.5f);
    zoomLabel.setAlignmentY(0.5f);
    
    // timer for displaying zoom info
    timer = new Timer(Settings.getFullScreenZoomEffectDuration()/
		      (255/ALPHA_DECREMENT), 
		      new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
	if (alpha < 0) {
	  timer.stop();
	  // text is now totally transparent
	  alpha = 0;
	}
	// draw faded label
	Color c = Settings.getFullScreenZoomEffectColor();
	zoomLabel.setForeground(new Color(c.getRed(), 
					  c.getGreen(),
					  c.getBlue(),
					  alpha));
	alpha -= ALPHA_DECREMENT;
      }
    });

    // create a popupmenu
    popupMenu = new JPopupMenu();
    
    JMenuItem nextItem = new JMenuItem("Next");
    nextItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	// change picture to next
	mainFrame.nextImage();
      }
    });
    
    JMenuItem previousItem = new JMenuItem("Previous");
    previousItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
 	// change picture to next
	mainFrame.prevImage();
      }
    });

    JMenuItem zoomInItem = new JMenuItem("Zoom in");
    zoomInItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	// zoom in full screen 
	zoomIn();
      }
    });
    
    JMenuItem zoomOutItem = new JMenuItem("Zoom out");
    zoomOutItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	// zoom in full screen 
	zoomOut();
      }
    });
    
    JMenuItem zoomToNormalItem = new JMenuItem("Zoom to normal");
    zoomToNormalItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	// zoom to normal (100%)
	zoomToNormal();
      }
    });    

    toggleSlideShowItem = new JMenuItem( "Start slideshow" );
    toggleSlideShowItem.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	// switch slideshow off and on
	mainFrame.toggleSlideShow(); 
      }
    });  
    
    popupMenu.add(nextItem);
    popupMenu.add(previousItem);
    popupMenu.addSeparator();
    popupMenu.add(zoomInItem);
    popupMenu.add(zoomOutItem);
    popupMenu.add(zoomToNormalItem);
    popupMenu.addSeparator();
    popupMenu.add(toggleSlideShowItem);
    
    popupMenu.addPopupMenuListener( new PopupMenuListener() {
      
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	toggleSlideShowItem.setText(
	   mainFrame.isSlideShowRunning() ? "Stop slideshow" : "Start slideshow" );
      }
      
      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
      
      public void popupMenuCanceled(PopupMenuEvent e) { }
    });
    
    // add a mouse listener for the popup menu
    this.addMouseListener(new MouseAdapter() {
      
      // show menu when mouse button is pressed
      public void mousePressed(MouseEvent e) {
	if (e.isPopupTrigger()) {
	  Point relPoint = ((Component)e.getSource()).getLocationOnScreen();
	  popupMenu.show( (Component)e.getSource(), e.getX(), e.getY() );
	}
      }
    });
    
    // used to place components on top of each other
    final OverlayLayout overlay = new OverlayLayout(this);
    this.setLayout(overlay);
    
    // special ScollPane that listens on up/down. left/right
    // home/end, page up/page down to scroll
    PanningScrollPane psp = new PanningScrollPane(imageLabel);
    
    // let this panel listen for panning events
    this.addKeyListener(psp);
    
    this.add(zoomLabel);
    this.add(psp);
    
    // listen for settings changed events
    Settings.addSettingsListener(this);

    // set the current settings
    this.updateSettings();
  }

  /** Part of the ImageSelectionListener interface,
   * called when a new image file is selected.
   */
  public void imageSelected(ImageSelectionEvent e) {
    // store the new picture info
    info = e.getInfo();
    // image should be reloaded
    image = null;
    // start from normal zoom
    zoomFactor = 0;
    // load picture if we are visible
    if (this.isShowing()) { 
      loadImage(true);
      // we should show the file name
      zoomLabel.setText(info.getFile().getName());
    }      
  }

  // load & scale a picture
  private void loadImage(boolean showZoomInfo) {
    try {
      // load the image if this is the first time
      if (image == null) 
	image = (new ImageIcon(info.getFile().getPath())).getImage();
      
      if (image != null) {
	// Determine the scale.
	double scale = calculateScale();
	
	imageLabel.setIcon(new ImageIcon(ImageToolkit.scaleImage(image, scale)));
	
	if (showZoomInfo && Settings.getFullScreenZoomEffectEnabled()) {
	  showMessage("Zoom " + (int)(scale*100) + "%");
	}
      }
    } catch(OutOfMemoryError E) { 
      showMessage("No more memory");
      // restore zoom to previous level
      zoomFactor--;
    } catch(Exception E) {}
  }

  private void showMessage(String mesg) {
    // display the zoom factor
    // opaque -> transparent
    alpha = 255;
    zoomLabel.setText(mesg);
    timer.start(); 
  }

  // calculates scale factor based on zoom factor
  private double calculateScale() {
    if (zoomFactor >= -3) 
      return 0.25*(4 + zoomFactor);
    else
      return 0.25*Math.exp(3+(double)zoomFactor);
  }

  /** Called when a setting is changed
   * part of the SettingsListener 
   * @param e the setting change event
   */
  public void settingsChanged(SettingsEvent e) {
    // update all settings
    updateSettings();
  }

  // updates the settings
  private void updateSettings() {
    // recalculate the fade time
    timer.setDelay(Settings.getFullScreenZoomEffectDuration()
		   /(255/ALPHA_DECREMENT));
    // set backgroundcolour
    this.setBackground(Settings.getFullScreenBackground());
  }

  /** Called when the directory is changed... remove image!
   * Part of the DirectoryEventListener interface
   * @param e the DirectoryEvent
   * @return void
   */
  public synchronized void directoryChanged(DirectoryEvent e) {
    // no file selected
    info = null;
    // image should be reloaded
    image = null;
    // start from normal zoom
    zoomFactor = 0;
    // remove picture
    imageLabel.setIcon(null);
  }
  
  /**
   * Zooms in on the selected image
   */
  public void zoomIn() {
    zoomFactor++;
    loadImage(true);
  }

  /**
   * Zoom out on the selected image
   */
  public void zoomOut() {
    // if less than 1% don't allow further zoom
    if ((int)(calculateScale()*100d) > 1) 
      zoomFactor--;
    loadImage(true);
  }

  /**
   * Zoom to normal picture ratio
   */
  public void zoomToNormal() {
    zoomFactor = 0;
    loadImage(false);
  }

}
  

