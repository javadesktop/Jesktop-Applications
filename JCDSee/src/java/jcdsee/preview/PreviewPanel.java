package jcdsee.preview;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import jcdsee.util.*;
import jcdsee.imagebrowser.*;
import jcdsee.folderbrowser.*;
import jcdsee.resources.*;

/** Provides a preview panel of an image 
 */
public class PreviewPanel extends JPanel implements ImageSelectionListener, DirectoryEventListener {
  
  private JLabel label;
  private ImageInfo info;

  /** Constructor
   */
  public PreviewPanel() {
    super( new BorderLayout() );
    
    info = null;
    label = new JLabel();
    label.setVerticalAlignment( SwingConstants.CENTER );
    label.setHorizontalAlignment( SwingConstants.CENTER );
    
    this.add( label, BorderLayout.CENTER );

    this.addComponentListener( new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
	loadIcon();
      }
    });
    
  }
  
  /** Called when an image is selected, i.e the preview 
   * should update. Part of the ImageSelectionListener
   * interface.
   */ 
  public void imageSelected(ImageSelectionEvent e) {
    info = e.getInfo();
    loadIcon();
  }
  
  /**
   * Called when the directory is changed. Remove the
   * image and show the JCDSee logo...
   * Part of the DirectoryEventListener interface
   */
  public void directoryChanged(DirectoryEvent e) {
    info = null;
    loadIcon();
  }

  // load the preview image
  private void loadIcon() {
    Image image;
    
    if (info != null) {
      ImageIcon icon = new ImageIcon(info.getFile().getPath());
      image = ImageToolkit.scaleImage(icon.getImage(), 
				      Math.abs(this.getWidth() - 2), 
				      Math.abs(this.getHeight() - 2));
    } else {
      // SetLogotype label
      image = ImageToolkit.scaleImageUgly(Resources.LOGOTYPE.getImage(),
					  Math.abs(this.getWidth() - 2), 
					  Math.abs(this.getHeight() - 2));
    }
    label.setIcon(new ImageIcon(image));
    label.setMinimumSize( ImageBrowser.ZERO_DIM );
  }
  
}
