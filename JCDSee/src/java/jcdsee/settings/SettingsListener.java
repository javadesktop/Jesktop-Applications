package jcdsee.settings;

import java.util.*;

/** the interface that setting listeners implement
 */
public interface SettingsListener extends EventListener {
  
  /** Constructor
   * @param e the settinng event
   */
  public void settingsChanged(SettingsEvent e);
}
