package jcdsee.imagebrowser;

import javax.swing.*;

/** A the button class that is used in the 
 * imagebrowser table
 */
public class InfoButton extends JToggleButton {
  private ImageInfo info;

  /**
   * Constructor
   * @param caption the name of the image
   * @param an ImageInfo associated with the image
   */
  public InfoButton(String caption, ImageInfo info) {
    super(caption);
    this.info = info;
    this.resetKeyboardActions();
  }

  /**
   * Returns the ImageInfo associated with this button
   * @return ImageInfo the ImageInfo
   */
  public ImageInfo getInfo() {
    return info;
  }
}
