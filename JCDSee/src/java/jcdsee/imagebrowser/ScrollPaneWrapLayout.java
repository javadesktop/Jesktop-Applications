package jcdsee.imagebrowser;

import java.awt.*;

/**
 * A flowlayout useful in scrollpanes.
 * This layout ensures that components 
 * extends in the y-direction, instead of 
 * expanding in the x-direction.
 */
public class ScrollPaneWrapLayout extends FlowLayout {
  
  /**
   * Empty constructor
   */
  public ScrollPaneWrapLayout() {
    super();
  }
  
  /** Constructs a new Flow Layout with the specified alignment and 
   * a default 5-unit horizontal and vertical gap. The value of the 
   * alignment argument must be one of FlowLayout.LEFT, FlowLayout.RIGHT, 
   * or FlowLayout.CENTER.
   * @param align - the alignment value
   */
  public ScrollPaneWrapLayout(int align) {
    super(align);
  }
  
  /** Creates a new flow layout manager with the indicated alignment and 
   * the indicated horizontal and vertical gaps. The value of the alignment 
   * argument must be one of FlowLayout.LEFT, FlowLayout.RIGHT, or
   * FlowLayout.CENTER.
   * @param align - the alignment value.
   * @param hgap - the horizontal gap between components.
   * 2param vgap - the vertical gap between components.
   */
  public ScrollPaneWrapLayout(int align, int hgap, int vgap) {
    super(align, hgap, vgap);
  }
  
  /** Overridden preferred size function that ensures that 
   *  ensures that components extends in the y-direction, instead of 
   * expanding in the x-direction when placed for in a scrollpane.
   * @param target the container to be layed out
   */
  public Dimension preferredLayoutSize( Container target ) {

    if( target.getParent() == null ) 
      return super.preferredLayoutSize( target );
    
    int maxWidth = target.getParent().getParent().getWidth();

    int count = target.getComponentCount();

    if( count == 0 )
      return new Dimension( 0, 0 );

    int width = target.getComponent(0).getWidth();

    Component[] compArray = target.getComponents();

    int j = 0;
    int rowSumW;
    int rowSumH;
    int maxW = 0;
    int maxH = 0;

    while (j < compArray.length) {
      rowSumW = compArray[j].getWidth();
      rowSumH = compArray[j].getHeight();

      while ( (++j < compArray.length) &&
	      (rowSumW + compArray[j].getWidth() < maxWidth)  ) {
	rowSumW += compArray[j].getWidth();
	rowSumH = Math.max(rowSumH, compArray[j].getHeight());
      }
      
      maxW = Math.max(maxW, rowSumW);
      maxH += rowSumH;
    }

    return new Dimension( maxW, maxH );
  }
}
