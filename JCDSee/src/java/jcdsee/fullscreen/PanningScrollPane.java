package jcdsee.fullscreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A scollpane that that listens on up/down. left/right
 * home/end, page up/page down to scroll
 */
public class PanningScrollPane extends JScrollPane implements KeyListener {

  private static final int DEFAULT_PANNING = 10;

  private int panSize = DEFAULT_PANNING;

  /** Constructor
   * @param view the component to place the scroolpane around
   */
  public PanningScrollPane(Component view) {
    super(view);
  }
  
  /** Constructor
   * @param view the component to place the scroolpane around
   * @param panSize the maount in pixles to pan
   */
  public PanningScrollPane(Component view, int panSize) {
    super(view);

    this.panSize = panSize;
  }  
  
  /** Part of the KeyListener interface
   * @param e the key event
   */
  public void keyPressed(KeyEvent e) {
    JScrollBar vScrollBar = this.getVerticalScrollBar();
    JScrollBar hScrollBar = this.getHorizontalScrollBar();
    switch (e.getKeyCode()) {
    case KeyEvent.VK_DOWN:
      // scroll downwards
      panValue(vScrollBar, panSize);
      break;
    case KeyEvent.VK_UP:
      // scroll downwards
       panValue(vScrollBar, -panSize);
      break;
    case KeyEvent.VK_LEFT:
      // scroll downwards
      panValue(hScrollBar, -panSize);
      break;
    case KeyEvent.VK_RIGHT:
      // scroll downwards
      panValue(hScrollBar, panSize);
      break;
    case KeyEvent.VK_HOME:
      if( vScrollBar != null )
	vScrollBar.setValue( vScrollBar.getMinimum());
      break;
    case KeyEvent.VK_END:
      if( vScrollBar != null )
	vScrollBar.setValue( vScrollBar.getMaximum());
      break;
    case KeyEvent.VK_PAGE_UP:
      // scroll downwards blockwise
      if (hScrollBar != null)
	panValue(vScrollBar, -vScrollBar.getBlockIncrement(-1));
      break;
    case KeyEvent.VK_PAGE_DOWN:
      // scroll downwards blockwise
      if (hScrollBar != null)
	panValue(vScrollBar, vScrollBar.getBlockIncrement(1));
      break; 
    default:
    }
  }

  private void panValue(JScrollBar scrollBar, int val) {
    if (scrollBar != null) {
      int currVal = scrollBar.getValue();
      int max     = scrollBar.getMaximum();
      int min     = scrollBar.getMinimum();

      int newVal = currVal + val > max ? max : currVal + val;
      newVal     = newVal < min ? min : newVal;

      scrollBar.setValues(newVal,
			  scrollBar.getVisibleAmount(),
			  min,
			  max);
    }
  }

  public void keyTyped(KeyEvent e) {}
  public void keyReleased(KeyEvent e){}
}
