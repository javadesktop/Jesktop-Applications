
package jcdsee.imagebrowser;

import javax.swing.*;
import java.awt.*;

import java.io.*;

import jcdsee.util.*;

/**
 * A class that encapsulates an image and related information.
 */
public class ImageInfo {
  private File imageFile;
  private Dimension dim;
  private Image image;
  
  private String fileName;
  private String fileSize;
  private String fileDim;
  
  private InfoButton button;
  
  /** Constructor
   * @param file the image file to open
   */
  public ImageInfo( File file ) { 
    imageFile = file;
    
    fileName = imageFile.getName();
    fileSize = FileInfo.printSize( imageFile );
    fileDim = "";
    
    button = new InfoButton( fileName, this );
    button.setVerticalAlignment(SwingConstants.BOTTOM);
    button.setVerticalTextPosition(SwingConstants.BOTTOM);
    button.setHorizontalTextPosition(SwingConstants.CENTER);
    button.setToolTipText(fileName + " - " + fileSize);

    button.setFont(new Font(button.getFont().getName(),
			    Font.PLAIN,
			    10));
  }
  
  /** Sets the dimension of the image
   * @param d the new dimension
   * @return void
   */
  public void setDimension( Dimension d ) {
    dim = d;
    fileDim = "" + dim.width + "x" + dim.height;
  }
  
  /** Sets the image 
   * @param i the new image
   * @return void
   */
  public void setImage( Image i ) { 
    image = i; 
    button.setIcon( new ImageIcon( image ) );
  }
  
  /** Returns the file associated with the image
   * @return a File object
   */
  public File getFile() { return imageFile; }

  /** Gets the dimension of the image
   * @return the dimension of the image
   */
  public Dimension getDimension() { return dim; }

  /** Returns the image
   * @return the image
   */
  public Image getImage() { return image; }

  /** Pretty prints the image dimension
   * @return a String with the image dimension
   */ 
  public String printDimension() { return fileDim; }

  /** Returns this ImageInfo as an array of Strings
   * that can be inserted into the table
   * @return an array with String
   */
  public String[] getRow() {
    return new String[] { fileName, fileSize, fileDim };
  }
  
  /** Returns the thumbnail button associated with the image
   * @return a InfoButton object
   */
  public InfoButton getButton() { return button; }
}
