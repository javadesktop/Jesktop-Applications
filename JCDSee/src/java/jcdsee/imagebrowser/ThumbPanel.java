
package jcdsee.imagebrowser;

import javax.swing.*;
import java.awt.*;

import jcdsee.settings.*;

/** An extended JPanel that implements the Scollable interface.
 * This class is needed by a scollpane with ScrollPaneWrapLayout 
 * in order that the horizontal panel size should follow the 
 * scrollpanes viewport size. The vertical panel size however does 
 * not follow the viewport size.
 */
public class ThumbPanel extends JPanel implements Scrollable {

  private Dimension preferredViewportSize;

  /** Constructor
   */
  public ThumbPanel() {
    super();
  }
  
  //
  // Implementing the Scrollable interface
  //
  
  /** Returns the preferred size of the viewport for a view component. 
   * For example the preferredSize of a JList component is the size 
   * required to acommodate all of the cells in its list however the value 
   * of preferredScrollableViewportSize is the size required for 
   * JList.getVisibleRowCount() rows. A component without any properties 
   * that would effect the viewport size should just return 
   * getPreferredSize() here.
   * @return The preferredSize of a JViewport whose view is this Scrollable.
   */
  public Dimension getPreferredScrollableViewportSize() {
    return this.getPreferredSize(); 
  }
  
  /** Returns the logical unit increment, i.e one thumbnail height
   * also adds the padding used in ImageBrowser.
   * @param visibleRect - The view area visible within the viewport
   * @param orientation - Either SwingConstants.VERTICAL or 
   * SwingConstants.HORIZONTAL.
   * @param direction - Less than zero to scroll up/left, greater than zero 
   * for down/right.
   */
  public int getScrollableUnitIncrement(Rectangle visibleRect, 
					int orientation,
					int direction) {
    if (orientation == SwingConstants.VERTICAL) 
      return (Settings.getThumbnailDimension().width + 
	ImageBrowser.IMAGE_INFO_PADDING)/2;
    else 
      return (Settings.getThumbnailDimension().height + 
	ImageBrowser.IMAGE_INFO_PADDING)/2;
  }
  
  /** Returns the logical block increment, in our case twice
   * the size of a thumbnail. Also adds the padding used in ImageBrowser.
   * @param visibleRect - The view area visible within the viewport
   * @param orientation - Either SwingConstants.VERTICAL or 
   * SwingConstants.HORIZONTAL.
   * @param direction - Less than zero to scroll up/left, greater than zero 
   * for down/right.
   */
  public int getScrollableBlockIncrement(Rectangle visibleRect, 
					 int orientation,
					 int direction) {
    if (orientation == SwingConstants.VERTICAL) 
      return Settings.getThumbnailDimension().width +
		ImageBrowser.IMAGE_INFO_PADDING;
		
    else 
      return Settings.getThumbnailDimension().height +
		ImageBrowser.IMAGE_INFO_PADDING;
  }
  
  /** Always returns true to indicate that the viewport
   * width determines the panels width
   * @return always true
   */
  public boolean getScrollableTracksViewportWidth() {
    return true;
  }
  
  /** Always returns false to indicate that the viewport
   * height does not determines the panels height. This will
   * cause the panel to expand in the horizontal direction
   * @return always false
   */
  public boolean getScrollableTracksViewportHeight() {
    return false;
  }
}


  
