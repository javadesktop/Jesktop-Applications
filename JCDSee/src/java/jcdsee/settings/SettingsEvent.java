package jcdsee.settings;

import java.util.*;

/**
 * The event that is generated when a setting is changed
 */
public class SettingsEvent extends EventObject {

  private boolean thumbSizeChanged = false;
  
  /** Constructor
   * @param source the Object generating the event
   */
  public SettingsEvent(Object source) {
    super(source);
  }

  /** Constructor
   * @param source the Object generating the event
   * @param thumbSizeChanged was the thumbnail size changed?
   */ 
  public SettingsEvent(Object source, boolean thumbSizeChanged) {
    super(source);
    this.thumbSizeChanged = thumbSizeChanged;
  }

  /** Returns true if the thumbnail size was changed
   * @return true if thumbnail size was changed
   */
  public boolean isThumbnailSizeChanged() {
    return thumbSizeChanged;
  }

}
