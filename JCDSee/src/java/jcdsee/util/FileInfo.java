
package jcdsee.util;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * A class that encapsulates file info
 */
public class FileInfo {
  
  protected static boolean isImageFile( String file ) {
    String str = (new String(file)).toLowerCase();
    return str.endsWith(".jpg") || str.endsWith(".gif");
  }
  
  /** Returns all readable image (jpg, gif) files in
   * a directory.
   * @param folder the directory to look in
   * @return an array with File objects
   */
  public static File[] getImages( File folder ) {
    if( !folder.isDirectory() )
      return null;

    return folder.listFiles( new FileFilter() {
      public boolean accept( File pathname ) {
	return ( pathname.isFile() && 
		 isImageFile(pathname.getName()) &&
		 pathname.canRead());
      }
    });
  }
  
  /** Returns the total size of all the images in
   * in a directory.
   * @param folder the directory to look in
   * @return the total size 
   */
  public static long getImageSizes( File folder ) {
    long size = 0;
    File[] files = getImages( folder );
    
    for( int i = 0; i < files.length; i++ ) {
      size += files[i].length();
    }
    
    return size;
  }

  /** Pretty printer for file size
   * @param file the file whos size is to be printed
   * @return a formatted string with the size
   */
  public static String printSize( File file ) {
    return printSize( file.length() );
  }

  /**
   * Pretty printer for total image size in a directory
   * @param file the file whos size is to be printed
   * @return a formatted string with the size
   */
  public static String printImageSizes( File folder ) {
    return printSize( getImageSizes( folder ) );
  }

  /** Pretty printer for file size
   * @param size the file size
   * @return a formatted string with the size
   */ 
  public static String printSize( long size ) {

    if( size < 1024 )
      return "" + size + " bytes";
    else if( size < 1024*1024 ) {
      double kbsize = (double)size / 1024d;
      DecimalFormat form = new DecimalFormat( ".##" );
      
      return form.format(kbsize).toString() + " kb";
    }
    else {
      double mbsize = (double)size / (1024d*1024d);
      DecimalFormat form = new DecimalFormat( ".##" );
      
      return form.format(mbsize).toString() + " mb";
    }
  }

  /** Pretty prints file size and last modified
   * attributes.
   * @param file the file to print info about
   * @return a formatted string
   */
  public static String printInfo( File file ) {
    DateFormat df = DateFormat.getDateInstance();

    return printSize( file ) + " - " + 
      df.format( new Date (file.lastModified()) );  
  }
  
}
     
