package jcdsee.imagebrowser;

import java.util.*;

/** The interface that image selection listeners implement.
 */
public interface ImageSelectionListener extends EventListener {
  
  /** This function is called when a new image is selected
   * @param e the image selection event
   */
  public void imageSelected(ImageSelectionEvent e);
}

