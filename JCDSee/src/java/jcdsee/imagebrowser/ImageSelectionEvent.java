package jcdsee.imagebrowser;

import java.util.*;
import java.io.*;

/** The event that is issued when an image
 * is selected.
 */
public class ImageSelectionEvent extends EventObject {
  protected ImageInfo info;
  
  /** Constructor
   * @param obj the class creating the event
   * @param info the image that was selected
   */
  public ImageSelectionEvent(Object obj, ImageInfo info) {
    super(obj);
    this.info = info;
  }
  
  /** Returns the ImageInfo object
   * @return an ImageInfo object
   */
  public ImageInfo getInfo() { return info; }
}
