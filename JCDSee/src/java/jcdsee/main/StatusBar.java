
package jcdsee.main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import jcdsee.folderbrowser.*;
import jcdsee.imagebrowser.*;
import jcdsee.util.*;

/** A GUI statusbar
 */
public class StatusBar extends JPanel implements DirectoryEventListener, ImageSelectionListener {
  
  private JLabel[] labels = new JLabel[5];
  private static final double[] WEIGHTS = { 0.3, 0.3, 0.3, 0.3, 0.8 };
  
  /** Constructor
   */
  public StatusBar() {
    super();

    // Set layout
    setLayout( new GridBagLayout() );

    Color c = this.getBackground();
    Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
						    c.brighter(),
						    c,
						    c.darker(),
						    c);
  
    for( int i = 0; i < labels.length; i++ ) {
      labels[i] = new JLabel(" ");
      labels[i].setBorder( border ); 
      
      add( labels[i], 
	   new GridBagConstraints(i, 0, 1, 1, WEIGHTS[i], 0.0,
				  GridBagConstraints.NORTHWEST, 
				  GridBagConstraints.HORIZONTAL, 
				  new Insets(2, 2, 2, 2), 0, 0 ));
    }
  }

  /** Called when the directory is changed
   * @param e the directory event
   */
  public void directoryChanged(DirectoryEvent e) {
    File dir = e.getDir();

    if (dir.canRead()) {
      labels[0].setText("Images: " + FileInfo.printImageSizes( dir ) );
      labels[1].setText(FileInfo.printInfo( dir ) );
    }
  }

  /** Called when an image is selected
   * @param e the image selection event
   */ 
  public void imageSelected( ImageSelectionEvent e ) {
    ImageInfo info = e.getInfo();
    
    labels[2].setText( info.getFile().getName() );
    labels[3].setText( FileInfo.printInfo( info.getFile() ) );
    labels[4].setText( info.printDimension() );
  }
}
