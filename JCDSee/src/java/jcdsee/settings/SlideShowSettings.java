package jcdsee.settings;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/** GUI for slideshow settings
 */
public class SlideShowSettings extends JPanel {

  private final static String[] sliderOptionNames = { "1 s.", "2 s.", "3 s.",
    "4 s.", "5 s.", "6 s.", "7 s.", "8 s.", "9 s.", "10 s.", "15 s.", 
    "20 s.", "25 s.", "30 s.", "35 s.", "40 s.", "45 s.", "50 s.", "55 s.", 
    "1 min.", "2 min.", "5 min.", "10 min.", "15 min.", "20 min.", "30 min.",
    "45 min.", "1 h.", "2 h." };

  private final static int[] sliderOptionValues = { 1000, 2000, 3000,
    4000, 5000, 6000, 7000, 8000, 9000, 10000, 15000, 20000,
    25000, 30000, 35000, 40000, 45000, 50000, 55000, 60000,
    120000, 300000, 600000, 900000, 1200000, 1800000,
    2700000, 3600000, 7200000 };
  
  private JCheckBox wrapCheckBox;
  private JSlider timeSlider;
  private JLabel  timeLabel;

  /** Constructor
   */
  public SlideShowSettings() {
    super( new GridBagLayout() );
    
    timeSlider = new JSlider( JSlider.HORIZONTAL, 
			      0, 
			      (sliderOptionValues.length - 1), 
			      Settings.getSlideShowTime() );
    
    wrapCheckBox = new JCheckBox( "Wrap around",
				  Settings.getSlideShowWrapEnabled() );
    

    timeLabel = new JLabel( sliderOptionNames[Settings.getSlideShowTime()] );

    this.add( wrapCheckBox, 
	      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.25,
				     GridBagConstraints.CENTER, 
				     GridBagConstraints.NONE, 
				     new Insets(5, 5, 5, 5), 0, 0) );
    
    this.add( new JLabel("Time between slides:"),
	      new GridBagConstraints(0, 1, 1, 1, 1.0, 0.25,
				     GridBagConstraints.CENTER, 
				     GridBagConstraints.NONE, 
				     new Insets(5, 5, 5, 5), 0, 0) );
    
    this.add( timeSlider,
	      new GridBagConstraints(0, 2, 1, 1, 1.0, 0.25,
				     GridBagConstraints.CENTER, 
				     GridBagConstraints.BOTH, 
				     new Insets(5, 5, 5, 5), 0, 0) );

    this.add( timeLabel,
	      new GridBagConstraints(0, 3, 1, 1, 1.0, 0.25,
				     GridBagConstraints.NORTH, 
				     GridBagConstraints.NONE, 
				     new Insets(5, 5, 5, 5), 0, 0) );
    

    // Add listener
    timeSlider.addChangeListener( new ChangeListener() {
      public void stateChanged( ChangeEvent ev ) {
 	timeLabel.setText( sliderOptionNames[timeSlider.getValue()] );
      }
    });
    
  }

  /** Converts the value returned by getSlideShowTime() to
   * milliseconds
   * @param index the index returned by getSlideShowTime()
   * @return the time in milliseconds
   */
  public final static int convertToMilliSeconds( int index ) {
    return sliderOptionValues[index];
  }

  /** Returns true if the slideshow is enabled
   * @return true if the slide show is enabled
   */
  public boolean getSlideShowWrapEnabled() {
    return wrapCheckBox.isSelected();
  }

  /** Returns an internal index that can be converted
   * by calling convertToMilliSeconds()
   * @return an internal index
   */
  public int getSlideShowTime() {
    return timeSlider.getValue();
  }
}
