
package jcdsee.folderbrowser;

import java.util.*;
import java.io.*;

/**
 * The event generated when a directory is changed in FolderBrowser
 */
public class DirectoryEvent extends EventObject {
  protected File dir;
  
  /**
   * Constructor
   * @param obj the source object
   * @param dir the directory changed to
   */
  public DirectoryEvent(Object obj, File dir) {
    super(obj);
    this.dir = dir;
  }
  
  /**
   * Returns the directory changed to
   * @return a File with the directory
   */
  public File getDir() { return dir; }
}
