
package jcdsee.util;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/** Provides various image manipulation functions
 */
public class ImageToolkit {
  
  /** Scales an image
   * @param inImage the image to be scaled
   * @maxSize the desired max size in any direction
   * @return the scaled image
   */
  public static Image scaleImage( Image inImage, int maxSize ) {
    
    // Get the image from a file.
    double imHeight = (double)inImage.getHeight(null);
    double imWidth = (double)inImage.getWidth(null);
		    
    // Determine the scale.
    double scale = (double)maxSize/imHeight;
    if (imWidth > imHeight) 
      scale = (double)maxSize/imWidth;

    // If the image is smaller than 
    // the desired image size,
    // don't bother scaling.
    if (scale < 1.0d) {
    
      // Determine size of new image. 
      // One of them should equal maxDim.
      int scaledW = (int)(scale*imWidth);
      int scaledH = (int)(scale*imHeight);

      // Check if zero
      if( scaledW == 0 )
	scaledW = 1;
      
      if( scaledH == 0 )
	scaledH = 1;
      
      // Create an image buffer in 
      // which to paint on.
      BufferedImage outImage = 
	new BufferedImage(scaledW, scaledH,BufferedImage.TYPE_INT_RGB);
      
      // Set the scale.
      AffineTransform tx = new AffineTransform();
      tx.scale(scale, scale);
      
      Graphics2D g2d = outImage.createGraphics();
      g2d.drawImage(inImage, tx, null);
      g2d.dispose();
    
      return outImage;
    }
    else
      return inImage;
  }
  
  /** Scales an image
   * @param inImage the image to be scaled
   * @xMax the desired max size in the x-direction
   * @yMax the desired max size in the y-direction
   * @return the scaled image
   */
  public static Image scaleImage( Image inImage, int xMax, int yMax) {
    // Get the image from a file.
    double imHeight = (double)inImage.getHeight(null);
    double imWidth = (double)inImage.getWidth(null);
		    
    // Determine the scale.

    double fY = yMax/imHeight;
    double fX = xMax/imWidth;

    double scale = fY < fX ? fY : fX; 

    // If the image is smaller than 
    // the desired image size,
    // don't bother scaling.
    if (scale < 1.0d) {
    
      // Determine size of new image. 
      // One of them should equal maxDim.
      int scaledW = (int)(scale*imWidth);
      int scaledH = (int)(scale*imHeight);

      // Check if zero
      if( scaledW == 0 )
	scaledW = 1;
      
      if( scaledH == 0 )
	scaledH = 1;
      
      // Create an image buffer in 
      // which to paint on.
      BufferedImage outImage = 
	new BufferedImage(scaledW, scaledH,BufferedImage.TYPE_INT_RGB);
      
      // Set the scale.
      AffineTransform tx = new AffineTransform();
      tx.scale(scale, scale);
      
      Graphics2D g2d = outImage.createGraphics();
      g2d.drawImage(inImage, tx, null);
      g2d.dispose();
    
      return outImage;
    }
    else
      return inImage;
  }

  /** Scales an image. Maintains gif transparency.
   * @param inImage the image to be scaled
   * @xMax the desired max size in the x-direction
   * @yMax the desired max size in the y-direction
   * @return the scaled image
   */
  public static Image scaleImageUgly( Image inImage, int xMax, int yMax) {
    // Get the image from a file.
    double imHeight = (double)inImage.getHeight(null);
    double imWidth = (double)inImage.getWidth(null);
    
    // Determine the scale.
    double fY = yMax/imHeight;
    double fX = xMax/imWidth;

    double scale = fY < fX ? fY : fX; 

    // If the image is smaller than 
    // the desired image size,
    // don't bother scaling.
    if (scale < 1.0d) {
    
      // Determine size of new image. 
      // One of them should equal maxDim.
      int scaledW = (int)(scale*imWidth);
      int scaledH = (int)(scale*imHeight);

      // Check if zero
      if( scaledW == 0 )
	scaledW = 1;
      
      if( scaledH == 0 )
	scaledH = 1;
      
      // Create an image buffer in 
      // which to paint on.
      Image outImage = inImage.getScaledInstance(scaledW,
						 scaledH,
						 Image.SCALE_DEFAULT);
      return outImage;
    }
    else
      return inImage;
  }
  
  /** Scales an image.
   * @param inImage the image to be scaled
   * @param scale ratio with which to scale
   * @return the scaled image
   */
  public static Image scaleImage( Image image, double scale) {
    // Get the image from a file.
    double imHeight = (double)image.getHeight(null);
    double imWidth  = (double)image.getWidth(null);
    
    // don't scale if original size
    if (scale == 1.0d) 
      return image;
    
    // Determine size of new image. 
    // One of them should equal maxDim.
    int scaledW = (int)(scale*imWidth);
    int scaledH = (int)(scale*imHeight);
    
    // Check if zero
    if( scaledW == 0 )
      scaledW = 1;
    
    if( scaledH == 0 )
      scaledH = 1;
      
    // Create an image buffer in 
    // which to paint on.
    BufferedImage outImage = 
      new BufferedImage(scaledW, scaledH,BufferedImage.TYPE_INT_RGB);
      
    // Set the scale.
    AffineTransform tx = new AffineTransform();
    tx.scale(scale, scale);
      
    Graphics2D g2d = outImage.createGraphics();
    g2d.drawImage(image, tx, null);
    g2d.dispose();

    return outImage;
  }
}
