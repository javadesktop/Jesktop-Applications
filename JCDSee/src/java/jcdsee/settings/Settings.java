package jcdsee.settings;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

import java.util.*;
import java.io.*;


/** Stores all settings for the application as static member variables. */
public class Settings {

  // Listeners
  private final static EventListenerList listeners = 
   new EventListenerList();

  // Properties
  private final static DefaultSettings properties = new DefaultSettings();
  
  // Property strings
  public final static String PROPERTY_THUMBNAIL_DIMENSION = 
    "Thumbnail dimension";

  public final static String PROPERTY_STARTUP_VIEW = 
    "Startup view";  

  public final static String PROPERTY_STARTUP_REMEMBER_POSITION =
    "Remember position";

  public final static String PROPERTY_STARTUP_MAXIMIZE_WINDOW = 
    "Maximize window";  

  public final static String PROPERTY_FULLSCREEN_BACKGROUND =
    "Fullscreen background";
  
  public final static String PROPERTY_FULLSCREEN_ZOOMEFFECT_ENABLED = 
    "Zoom effect enabled";  

  public final static String PROPERTY_FULLSCREEN_ZOOMEFFECT_COLOR = 
    "Zoom effect color";  

  public final static String PROPERTY_FULLSCREEN_ZOOMEFFECT_DURATION = 
    "Zoom effect duration";  

  public final static String PROPERTY_SLIDESHOW_WRAP_ENABLED = 
    "Slideshow wrap enabled";

  public final static String PROPERTY_SLIDESHOW_TIME = 
    "Slideshow time";

  public final static String PROPERTY_LAST_SIZE =
    "Last size";

  public final static String PROPERTY_LAST_LOCATION = 
    "Last location";

  public final static String PROPERTY_HORIZONTAL_SPLITPANE = 
    "Horizontal splitpane";

  public final static String PROPERTY_VERTICAL_SPLITPANE = 
    "Vertical splitpane";
  
  
  private final static String JCDSEE_PROPERTY_FILE = ".jcdsee";

  /** Load from property file */
  public static void loadSettings() {    
    File settingsFile = getSettingsFile();
    
    // Check if file exists
    if( settingsFile.exists() ) {
      try {
	properties.load( new FileInputStream( settingsFile ) );
      
	// fire to listeners
	fireSettingsChanged( new SettingsEvent( null ) );
      }
      catch( Exception e ) { }
    }
  }
  
  /** Store settings to property file */
  public static void saveSettings() {
    File settingsFile = getSettingsFile();
    
    try {
      properties.store( new FileOutputStream( settingsFile ), null );
    }
    catch( Exception e ) { }
  }
  
  /** Get thumbnail size. */
  public static Dimension getThumbnailDimension() {
    return stringToDimension( properties.getProperty( PROPERTY_THUMBNAIL_DIMENSION ) );
  }
  
  /** Set thumbnail size. */
  public static void setThumbnailDimension( Dimension dim ) {
    properties.setProperty( PROPERTY_THUMBNAIL_DIMENSION, dimensionToString( dim ) );
  }

  public static String getStartupView() {
    return properties.getProperty( PROPERTY_STARTUP_VIEW );
  }
  
  public static void setStartupView( String view ) {
    properties.setProperty( PROPERTY_STARTUP_VIEW, view ); 
  }

  public static boolean getStartupRememberPosition() {
    return stringToBoolean( 
      properties.getProperty( PROPERTY_STARTUP_REMEMBER_POSITION ) );
  }

  public static void setStartupRememberPosition( boolean value ) {
    properties.setProperty( PROPERTY_STARTUP_REMEMBER_POSITION,
			    booleanToString( value ) );
  }

  public static boolean getStartupMaximizeWindow() {
    return stringToBoolean( 
      properties.getProperty( PROPERTY_STARTUP_MAXIMIZE_WINDOW ) );
  }

  public static void setStartupMaximizeWindow( boolean value ) {
    properties.setProperty( PROPERTY_STARTUP_MAXIMIZE_WINDOW,
			    booleanToString( value ) );
  }

  public static Color getFullScreenBackground() {
    return stringToColor( properties.getProperty( PROPERTY_FULLSCREEN_BACKGROUND ) ); 
  }
  
  public static void setFullScreenBackground( Color color ) {
    properties.setProperty( PROPERTY_FULLSCREEN_BACKGROUND, colorToString( color ) );
  }

  public static boolean getFullScreenZoomEffectEnabled() {
    return stringToBoolean( 
      properties.getProperty( PROPERTY_FULLSCREEN_ZOOMEFFECT_ENABLED ) );
  }

  public static void setFullScreenZoomEffectEnabled(boolean value) {
    properties.setProperty( PROPERTY_FULLSCREEN_ZOOMEFFECT_ENABLED,
			    booleanToString( value ) );
  }

  public static Color getFullScreenZoomEffectColor() {
    return stringToColor( properties.getProperty( 
      PROPERTY_FULLSCREEN_ZOOMEFFECT_COLOR ) ); 
  }

  public static void setFullScreenZoomEffectColor(Color color) {
    properties.setProperty( PROPERTY_FULLSCREEN_ZOOMEFFECT_COLOR, 
			    colorToString( color ) );
  }
  
  public static int getFullScreenZoomEffectDuration() {
    return Integer.parseInt( properties.getProperty( 
      PROPERTY_FULLSCREEN_ZOOMEFFECT_DURATION ) ); 
  }

  public static void setFullScreenZoomEffectDuration(int value) {
    properties.setProperty( PROPERTY_FULLSCREEN_ZOOMEFFECT_DURATION, 
			    "" + value );
  } 

  public static boolean getSlideShowWrapEnabled() {
    return stringToBoolean( properties.getProperty( 
      PROPERTY_SLIDESHOW_WRAP_ENABLED ) );
  }

  public static void setSlideShowWrapEnabled(boolean value) {
    properties.setProperty( PROPERTY_SLIDESHOW_WRAP_ENABLED,
			    booleanToString( value ) );
  }

  public static int getSlideShowTime() {
    return Integer.parseInt( properties.getProperty( 
      PROPERTY_SLIDESHOW_TIME ) );
  }
  
  public static void setSlideShowTime(int value) {
    properties.setProperty( PROPERTY_SLIDESHOW_TIME,
			    "" + value );
  }

  public static Point getLastLocation() {
    Dimension dim = stringToDimension( properties.getProperty( 
      PROPERTY_LAST_LOCATION ) );
    return new Point( dim.width, dim.height );
  }

  public static void setLastLocation( Point p ) {
    properties.setProperty( PROPERTY_LAST_LOCATION,
      dimensionToString( new Dimension( p.x, p.y ) ) );
  }

  public static Dimension getLastSize() {
    return stringToDimension( properties.getProperty(
      PROPERTY_LAST_SIZE ) );
  }

  public static void setLastSize( Dimension size ) {
    properties.setProperty( PROPERTY_LAST_SIZE,
			    dimensionToString( size ) );
  }
  
  public static void setHorizontalSplitPane(double prop) {
    properties.setProperty( PROPERTY_HORIZONTAL_SPLITPANE,
			    (new Double(prop)).toString() );
  }

  public static double getHorizontalSplitPane() {
    double res = 0d;
    try {
      res = Double.parseDouble( properties.getProperty(
		 PROPERTY_HORIZONTAL_SPLITPANE ) );
    } catch(NumberFormatException E) {}
    return res;
  }

  public static void setVerticalSplitPane(double prop) {
    properties.setProperty( PROPERTY_VERTICAL_SPLITPANE,
			    (new Double(prop)).toString() );
  }
  
  public static double getVerticalSplitPane() {
    double res = 0d;
    try {
      res = Double.parseDouble( properties.getProperty(
		 PROPERTY_VERTICAL_SPLITPANE) );
    } catch(NumberFormatException E) {}
    return res;
  }

  /** Adds a settings listener
   * @param l the listener
   */
  public static void addSettingsListener(SettingsListener l) {
    listeners.add(SettingsListener.class, l);
  }

  /** Removes a settings listener
   * @param l the listener
   */
  public static void removeSettingsListener(SettingsListener l) {
    listeners.remove(SettingsListener.class, l);
  }
  
  // fire settings change event to listeners
  protected static void fireSettingsChanged(SettingsEvent e) {
    // Guaranteed to return a non-null array
    Object[] audience = listeners.getListenerList();
    
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = audience.length-2; i>=0; i-=2) {
      if (audience[i] == SettingsListener.class) {
	((SettingsListener)audience[i+1]).settingsChanged(e);
      }
    }
  }         
  
  private static File getSettingsFile() {
    String homeDir = System.getProperty( "user.home" );
    if( !homeDir.endsWith( File.separator ) )
      homeDir += File.separator;
    
    return new File( homeDir + JCDSEE_PROPERTY_FILE );
  }

  private static String dimensionToString( Dimension dim ) {
    return dim.width + "," + dim.height;
  }

  private static Dimension stringToDimension( String str ) {
    StringTokenizer tokenizer = new StringTokenizer( str, "," );
    
    if( tokenizer.countTokens() == 2 )
      return new Dimension( Integer.parseInt( tokenizer.nextToken() ),
			    Integer.parseInt( tokenizer.nextToken() ) );
    
    return null;
  }

  private static Color stringToColor( String str ) {
    return Color.decode( str );
  }

  private static String colorToString( Color c ) {
    return (new Integer(c.getRGB())).toString();
  }

  private static boolean stringToBoolean( String str ) {
    return (Integer.parseInt(str) != 0 );
  }
  
  private static String booleanToString( boolean b ) {
    return b ? "1" : "0";
  }
  
}
    
// contains deafult values for all settings
class DefaultSettings extends Properties {
  
  private final static String DEFAULT_THUMBNAIL_DIMENSION = "80,80";
  private final static String DEFAULT_FULLSCREEN_BACKGROUND = 
    (new Integer(Color.black.getRGB())).toString();

  private final static String DEFAULT_STARTUP_VIEW =
    StartupSettings.STARTUP_VIEW_THUMBNAIL;

  private final static String DEFAULT_STARTUP_REMEMBER_POSITION = "0";
  private final static String DEFAULT_STARTUP_MAXIMIZE_WINDOW   = "0";

  private final static String DEFAULT_FULLSCREEN_ZOOMEFFECT_ENABLED = "1";

  public final static String DEFAULT_FULLSCREEN_ZOOMEFFECT_COLOR =
    (new Integer(Color.white.getRGB())).toString();
  
  public final static String DEFAULT_FULLSCREEN_ZOOMEFFECT_DURATION = "300";

  public final static String DEFAULT_SLIDESHOW_WRAP_ENABLED = "1";

  public final static String DEFAULT_SLIDESHOW_TIME = "4";

  public final static String DEFAULT_LAST_LOCATION = "0,0";
  
  public final static String DEFAULT_LAST_SIZE = "600,500"; 

  public final static String DEFAULT_VERTICAL_SPLITPANE = "0.4";
  
  public final static String DEFAULT_HORIZONTAL_SPLITPANE = "0.7";

  public DefaultSettings() {
    super();
      
    setProperty( Settings.PROPERTY_THUMBNAIL_DIMENSION,
		 DEFAULT_THUMBNAIL_DIMENSION );
    
    setProperty( Settings.PROPERTY_FULLSCREEN_BACKGROUND,
		 DEFAULT_FULLSCREEN_BACKGROUND );

    setProperty( Settings.PROPERTY_STARTUP_VIEW,
		 DEFAULT_STARTUP_VIEW );

    setProperty( Settings.PROPERTY_STARTUP_REMEMBER_POSITION,
		 DEFAULT_STARTUP_REMEMBER_POSITION );
    
    setProperty( Settings.PROPERTY_STARTUP_MAXIMIZE_WINDOW,
		 DEFAULT_STARTUP_MAXIMIZE_WINDOW );
    
    setProperty( Settings.PROPERTY_FULLSCREEN_ZOOMEFFECT_ENABLED,
		 DEFAULT_FULLSCREEN_ZOOMEFFECT_ENABLED );

    setProperty( Settings.PROPERTY_FULLSCREEN_ZOOMEFFECT_COLOR,
		 DEFAULT_FULLSCREEN_ZOOMEFFECT_COLOR );

    setProperty( Settings.PROPERTY_FULLSCREEN_ZOOMEFFECT_DURATION,
		 DEFAULT_FULLSCREEN_ZOOMEFFECT_DURATION );

    setProperty( Settings.PROPERTY_SLIDESHOW_WRAP_ENABLED,
		 DEFAULT_SLIDESHOW_WRAP_ENABLED );

    setProperty( Settings.PROPERTY_SLIDESHOW_TIME,
		 DEFAULT_SLIDESHOW_TIME );

    setProperty( Settings.PROPERTY_LAST_LOCATION,
		 DEFAULT_LAST_LOCATION );
    
    setProperty( Settings.PROPERTY_LAST_SIZE,
		 DEFAULT_LAST_SIZE );

    setProperty( Settings.PROPERTY_HORIZONTAL_SPLITPANE,
		 DEFAULT_HORIZONTAL_SPLITPANE );

    setProperty( Settings.PROPERTY_VERTICAL_SPLITPANE,
		 DEFAULT_VERTICAL_SPLITPANE );
  }

}
