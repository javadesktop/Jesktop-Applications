
package jcdsee.folderbrowser;

import java.util.*;

/**
 * The interface implemented by Objects listening for
 * directory change events
 */
public interface DirectoryEventListener extends EventListener {
   
  /**
   * Function called when the directory is changed
   * @param e The directory event object
   * @return void
   */ 
    public void directoryChanged(DirectoryEvent e);
}
    
