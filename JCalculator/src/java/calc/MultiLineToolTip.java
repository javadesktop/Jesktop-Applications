package calc;

// Written by Nilesh Raghuvnashi, no copyright
// File: MultiLineToolTip.java

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.metal.*;

/**A ToolTip capable of displaying text in multiple lines.*/
public class MultiLineToolTip extends JToolTip {


	/**Constructs a ToolTip capable of displaying text in multiple lines.
	 */
  public MultiLineToolTip() {
    setUI(new MultiLineToolTipUI());
  }
}

/**The L&F for a multiple line ToolTip.*/
class MultiLineToolTipUI extends MetalToolTipUI {
	/**Holds the strings to be displayed on separate lines.*/
  private String[] strs;
  /**The maximum width of the ToolTip.*/
  private int maxWidth = 0;


	/**Paints the tool tip.
	 *
	 * @param   g  The graphics component for painting this icon.
	 * @param   c  Used for getting properties useful for painting, e.g. the foreground or background color.
	 */
  public void paint(Graphics g, JComponent c) {
  	/**Encapsulates information about the rendering of the font for the ToolTip.*/
    FontMetrics metrics = c.getFontMetrics(g.getFont());
    /**The dimension of the ToolTip.*/
    Dimension size = c.getSize();
    g.setColor(c.getBackground());
    g.fillRect(0, 0, size.width, size.height);
    g.setColor(c.getForeground());
    if (null != strs) {
      for (int i=0;i<strs.length;i++) {
        g.drawString(strs[i], 3, (metrics.getHeight()) * (i+1));
      }
    }
  }


	/**Returns the preferred size of the ToolTip.
	 *
	 *
	 * @param   c  Used for getting properties useful for painting, e.g. the foreground or background color.
	 * @return  The preferred size of the ToolTip.
	 */
  public Dimension getPreferredSize(JComponent c) {
  	/**Encapsulates information about the rendering of the font for the ToolTip.*/
    FontMetrics metrics = c.getFontMetrics(c.getFont());
    /**The text to be displayed in the ToolTip.*/
    String tipText = ((JToolTip)c).getTipText();
    if (null == tipText) {
      tipText = "";
    }
    /**Read the text for the tooltip.*/
    BufferedReader br = new BufferedReader(new StringReader(tipText));
    String line;
    int maxWidth = 0;
    /**Holds the lines to be displayed in the ToolTip separately.*/
    Vector v = new Vector();
    try {
      while ((line = br.readLine()) != null) {
      	/**The width required to display the line.*/
        int width = SwingUtilities.computeStringWidth(metrics,line);
        maxWidth = (maxWidth < width) ? width : maxWidth;
        v.addElement(line);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    /**The number of lines in the ToolTip.*/
    int lines = v.size();
    if (1 > lines) {
      strs = null;
      lines = 1;
    } else {
      strs = new String[lines];
      int i=0;
      for (Enumeration e = v.elements(); e.hasMoreElements() ;i++) {
        strs[i] = (String)e.nextElement();
      }
    }
    /**The height of the ToolTip.*/
    int height = metrics.getHeight() * lines;
    this.maxWidth = maxWidth;
    return new Dimension(maxWidth + 6, height + 4);
  }
}