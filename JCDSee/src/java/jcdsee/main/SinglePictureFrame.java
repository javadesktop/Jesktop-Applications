package jcdsee.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import jcdsee.fullscreen.PanningScrollPane;
import org.jesktop.frimble.*;

/**
 * Provides a panel for a single image
 */
public class SinglePictureFrame extends JFrimble {

  /** Constructor
   * @param image the image to be displayed
   */
  public SinglePictureFrame(ImageIcon image, String fileName) {
    super();
    this.getContentPane().setLayout(new BorderLayout());

    PanningScrollPane psp = new PanningScrollPane(new JLabel(image));
    this.addKeyListener(psp);

    this.getContentPane().add(psp, BorderLayout.CENTER);
    this.pack();
    this.setSize(this.getPreferredSize());

    // center frame
    Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameDimension = this.getSize();
    this.setLocation( (screenDimension.width-frameDimension.width)/2,
          (screenDimension.height-frameDimension.height)/2 );

    this.setTitle(fileName);
    this.setVisible(true);
  }
}



