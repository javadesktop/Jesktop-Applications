
package jcdsee.resources;

import java.awt.*;
import javax.swing.*;
import java.net.*;

/**
 * Resources used in the program
 */
public class Resources {

  /** JCDSee logo */
  public final static ImageIcon LOGOTYPE = getImageIcon("images/logo.jpg");

  /** URL to help index */
  public final static URL MANUAL_INDEX =
    Resources.class.getResource("../help/html/index.html");

  /** Computer icon */
  public static final ImageIcon ICON_COMPUTER = getImageIcon("images/computer.gif");

  /** Computer disk icon */
  public static final ImageIcon ICON_DISK = getImageIcon("images/disk.gif");

  /** Closed folder icon */
  public static final ImageIcon ICON_FOLDER = getImageIcon("images/closedfolder.gif");

  /** Open folder icon */
  public static final ImageIcon ICON_EXPANDEDFOLDER = getImageIcon("images/openfolder.gif");

  /** Thumbnail icon */
  public static final ImageIcon ICON_THUMBNAIL = getImageIcon("images/thumb.gif");

  /** Thumbnail icon */
  public static final ImageIcon ICON_LIST = getImageIcon("images/list.gif");

  /** Fullscreen icon */
  public static final ImageIcon ICON_FULLSCREEN = getImageIcon("images/fullscreen.gif");

  /** Settings icon */
  public static final ImageIcon ICON_SETTINGS = getImageIcon("images/settings.gif");

  /** Help icon */
  public static final ImageIcon ICON_HELP = getImageIcon("images/help.gif");

  private static ImageIcon getImageIcon(String file) {
    URL url = ((URLClassLoader) Resources.class.getClassLoader()).getResource(file);
    System.out.println("URL=" + url);
    return new ImageIcon(url);
  }

}
