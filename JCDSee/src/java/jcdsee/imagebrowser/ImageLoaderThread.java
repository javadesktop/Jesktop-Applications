package jcdsee.imagebrowser;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import jcdsee.util.*;
import jcdsee.imagebrowser.*;
import jcdsee.settings.*;

/** Thread that loads the thumbnail images
 */
public class ImageLoaderThread extends Thread {
  
  private Vector images;
  private ImageLoaderListener listener;

  private boolean die;

  private Semaphore semaphore;

  /** Constuctor
   * @param images vector with ImageInfo objects
   * @param listener a listener that is notified when the image loading is done
   */
  public ImageLoaderThread( Vector images, ImageLoaderListener listener ) {
    this.images = images;
    this.listener = listener;

    die = false;

    semaphore = new Semaphore();
  }
  
  /**
   * Start the tread. part of the Thread interface.
   * @return void
   */
  public void run() {

    Dimension dim = Settings.getThumbnailDimension();

    for( Enumeration e = images.elements(); e.hasMoreElements(); ) {
      ImageInfo info = (ImageInfo)e.nextElement();

      if( die )
	break;

      // Load image from file
      Image image = (new ImageIcon( info.getFile().getPath() )).getImage();

      if( die )
	break;

      // Set dimension
      info.setDimension( new Dimension( image.getWidth(null), image.getHeight(null) ) );

      if( die )
	break;

      // Set thumbnail
      info.setImage( ImageToolkit.scaleImage( image, dim.width, dim.height ) );

      if( die )
	break;
      
      // Notify listener
      listener.imageLoaded( info );

      if( die )
	break;
    }
    
    // Release lock on semaphore
    semaphore.unlock();
  }

  /** Called if image loading should be terminated
   * prematurely. Terminates the tread 
   * and returns when thread has died.
   * @return void
   */
  public void kill() {
    
    die = true;
    
    // Wait for thread to terminate
    semaphore.lock();
  }


}
