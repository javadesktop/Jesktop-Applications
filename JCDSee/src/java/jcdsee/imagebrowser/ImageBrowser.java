package jcdsee.imagebrowser;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.io.*;

import jcdsee.util.*;
import jcdsee.folderbrowser.*;
import jcdsee.settings.*;

/** Class that contains a table and thumbnail panel for browsing
 * images. Contains various function for loading next/previous image.
 */
public class ImageBrowser extends JPanel implements DirectoryEventListener, ImageLoaderListener, SettingsListener {

  /*
   * Constants
   */
  /** A zero dimension constant */
  public final static Dimension ZERO_DIM = new Dimension( 0, 0 );

  /** Constants for thumbnail mode */
  public final static String THUMBNAIL_MODE = "Thumbnail view";

  /**  Constants for list mode */
  public final static String LIST_MODE = "List mode";

  /** Padding to accomodate the image name in ImageInfo button */
  public final static int IMAGE_INFO_PADDING = 20;

  /*
   * Variables
   */
  private JTable table;
  private ImageTableModel model;

  private Vector images;

  private ThumbPanel thumbPanel;

  private int currentRowLoaded;

  private ImageLoaderThread thread;

  private EventListenerList listeners;

  private String mode;

  private ListSelectionListener tableSelectionListener;

  private File currentDirectory = null;

  /** Constructor
   */
  public ImageBrowser() {

    // create the scrollbar used in thumbpanel
    // so that thumbpanel can use it
    thumbPanel = new ThumbPanel();
    
    thumbPanel.setLayout( new ScrollPaneWrapLayout(FlowLayout.LEFT, 0, 0) {
      public Dimension minimumLayoutSize(Container target) {
	return ZERO_DIM;
      }      
    });
    
    thumbPanel.setOpaque( false );
 
    table = new JTable();
    model = new ImageTableModel();
    table.setModel( model );
    
    listeners = new EventListenerList();

    // listen for selection events in the table
    tableSelectionListener =  new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
	if (!e.getValueIsAdjusting()) {
	  int index = table.getSelectionModel().getMinSelectionIndex();
	  fireImageSelected( new ImageSelectionEvent( ImageBrowser.this,
						      model.getRow(index ) ));
	}
      }
    };

    table.getSelectionModel().addListSelectionListener( tableSelectionListener );
    
    table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

    // We want to listenen for setting changes
    Settings.addSettingsListener( this );

    this.setLayout( new CardLayout() );
    
    this.add( new JScrollPane(thumbPanel), THUMBNAIL_MODE );
    this.add( new JScrollPane(table), LIST_MODE );

    mode = THUMBNAIL_MODE;
    changeMode(THUMBNAIL_MODE);

    images = new Vector();
  }
    
  /** Overridden requestFocus method  
   * Called by classes that wish to give focus to the ImageBrowser
   * @return void
   */
  public void requestFocus() {
    if (mode.equals(THUMBNAIL_MODE)) 
      setSelectedImage( getSelectedImage() );
    else
      table.requestFocus();
  }
    
  // determines which image is selected
  private int getSelectedImage() {
    for (int i=0; i < images.size(); i++) {
      if ( ((ImageInfo)images.elementAt(i)).getButton().isSelected() )
	return i;
    }
    return -1;
  }

  /** Called by classes that want to set a thumbnail selected
   * @param index the thumbnail index to select
   * @return void
   */
  public void setSelectedImage(int index) {
    if( (index >= 0) && (index < images.size()) ) {
      InfoButton info = ((ImageInfo)images.elementAt(index)).getButton();
      
      info.doClick();
      if( info.isShowing() )
	info.requestFocus();
    }
  }

  /** Selects the next image image
   * @wrap should be wrap if the end is reached?
   * @return void
   */
  public void selectNextImage(boolean wrap) {
    int index = getSelectedImage();
    if (index >= 0) {
      if (wrap)
	index = (index + 1) % images.size();
      else 
	index = (index == images.size() - 1) ? index : index + 1;
    }
    setSelectedImage( index );
  }

  /** Select the previous image
   * @wrap should be wrap if the end is reached?
   * @return void
   */
  public void selectPreviousImage(boolean wrap) {
    int index = getSelectedImage();
    if (index >= 0) {
      if (wrap)
	index = (index == 0) ? images.size() - 1 : index - 1;
      else 
	index = (index == 0) ? index : index - 1;
    }
    setSelectedImage( index );
  }

  /** Check if the selected image is the last one in the list */
  public boolean isLastImage() {
    return getSelectedImage() == images.size() - 1;
  }

  /** Changes the ImageBrowser mode, table <-> thumbnail
   * @param mode the mode to flip to <br>
   *  Can be: <br>
   *  THUMBNAIL_MODE <br>
   *  LIST_MODE 
   * @return void
   */
  public void changeMode( String mode ) {
      if (!this.mode.equals(mode)) {
	((CardLayout)this.getLayout()).show( this, mode );
	table.revalidate();
      }
      this.mode = mode;
  }

  /** Called when the directory is changed in the folderbrowser.
   * Part of the DirectoryEventListener interface.
   * @return void
   */
  public synchronized void directoryChanged(DirectoryEvent e) {
    // Kill image loading thread if active
    if( thread != null )
      thread.kill();

    // check that we can read the directory...
    if (!e.getDir().canRead()) 
      return;

    // Clear panels and vector
    thumbPanel.removeAll();
    model.removeAll();
    images.removeAllElements();
    
    currentRowLoaded = 0;
    
    // Get all images in directory
    currentDirectory = e.getDir();
    File[] imageFiles = FileInfo.getImages( e.getDir() );
    images.ensureCapacity( imageFiles.length );
    
    ButtonGroup group = new ButtonGroup();

    // Create listeners
    ActionListener actionListener = new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
	fireImageSelected( new ImageSelectionEvent( ImageBrowser.this,
						    ((InfoButton)e.getSource()).getInfo()));
      }
    };

    KeyListener keyListener = new KeyAdapter() {
      public void keyPressed( KeyEvent ev ) {
	
	switch( ev.getKeyCode() ) {
	  
	case KeyEvent.VK_LEFT:
	case KeyEvent.VK_UP:
	  // Previous image
	  selectPreviousImage(false);
	  break;
	  
	case KeyEvent.VK_RIGHT:
	case KeyEvent.VK_DOWN:
	  // Next image
	  selectNextImage(false);
	  break;
	}
      }
    };

    // Add buttons to panel and rows to table
    for( int i = 0; i < imageFiles.length; i++ ) {
      ImageInfo info = new ImageInfo( imageFiles[i] );
      
      InfoButton button = info.getButton();
      
      button.addActionListener( actionListener );

      button.addKeyListener( keyListener );
      
      int w = Settings.getThumbnailDimension().width + IMAGE_INFO_PADDING;
      int h = Settings.getThumbnailDimension().height + IMAGE_INFO_PADDING;
      
      button.setMinimumSize( new Dimension( w, h ) );
      button.setPreferredSize( new Dimension( w, h ) );
      button.setMaximumSize( new Dimension( w, h ) );
      group.add( button );
      
      thumbPanel.add( button );
      model.addRow( info );
      
      images.add( info );
    }
    
    // Start image loading thread
    thread = new ImageLoaderThread( images, this );
    thread.start();
  }

  /** Called when a setting is changed in settings dialog
   * Part of SettingsListener interface
   * @return void
   */
  public void settingsChanged( SettingsEvent e ) {
    if( currentDirectory != null && e.isThumbnailSizeChanged() ) {
      int selected = getSelectedImage();
      directoryChanged( new DirectoryEvent( this, currentDirectory ) );
      setSelectedImage( selected );
    }
  }
    
  /** Called by the image loading thread when a image
   * has been loaded and scaled to a thumbnail. When
   * this ocurrs it's name entered into the table.
   * @return void
   */ 
  public void imageLoaded( ImageInfo info ) {
    model.setValueAt( info.printDimension(), currentRowLoaded, 2 );
    currentRowLoaded++;
  }

  /** Adds an image selection listener, i.e a listener that is
   * notified when a new image has been selected.
   * @param ImageSelectionListener the listener to be added
   * @return void
   */
  public void addImageSelectionListener(ImageSelectionListener l) {
    listeners.add(ImageSelectionListener.class, l);
  }
  
  /** Removes an image selection listener, i.e a listener that is
   * notified when a new image has been selected.
   * @param ImageSelectionListener the listener to be added
   * @return void
   */   
  public void removeImageSelectionListener(ImageSelectionListener l) {
    listeners.remove(ImageSelectionListener.class, l);
  }
    
  // notifies listeners that a image was selected
  protected void fireImageSelected(ImageSelectionEvent e) {
    // Sync table and buttons
    e.getInfo().getButton().setSelected(true);
    
    // Remove selection listener on table (and then add it again when selection is done)
    table.getSelectionModel().removeListSelectionListener( tableSelectionListener );

    int index = getSelectedImage();

    table.setRowSelectionInterval( index, index );

    table.getSelectionModel().addListSelectionListener( tableSelectionListener );  


    // Guaranteed to return a non-null array
    Object[] audience = listeners.getListenerList();
    
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = audience.length-2; i>=0; i-=2) {
      if (audience[i] == ImageSelectionListener.class) {
	((ImageSelectionListener)audience[i+1]).imageSelected(e);
      }              
    }
  }         
    
  // a table model for the table, makes cells noneditable
  // and stores ImageInfo objects with every entry
  protected class ImageTableModel extends DefaultTableModel {

    Vector rows;

    public ImageTableModel() {
      super( new String[] { "Name", "Size", "Dimension" }, 0 );
      rows = new Vector();
    }
    
    public boolean isCellEditable(int row, int col) { 
      return false; 
    }

    public void addRow(ImageInfo info) {
      super.addRow( info.getRow() );
      rows.add( info );
    }

    public void removeAll() {
      super.setNumRows(0);
      rows.clear();
    }
    
    public ImageInfo getRow( int index ) {
      return (ImageInfo)rows.elementAt( index );
    }
  }
}

