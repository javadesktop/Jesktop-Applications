package jcdsee.settings;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/** GUI for fullscreen settings
 */
public class FullScreenSettings extends JPanel {
  
  private ColorButton backgroundColorButton;
  
  private JCheckBox zoomEffectEnabledBox;
  private ColorButton zoomEffectColorButton;
  
  private JLabel zoomEffectDurationLabel;
  private JSlider zoomEffectDurationSlider;
  
  /** Constructor
   */
  public FullScreenSettings() {
    super( new GridBagLayout() );
    
    Color backgroundColor = Settings.getFullScreenBackground();
    boolean zoomEffectEnabled = Settings.getFullScreenZoomEffectEnabled();
    Color zoomEffectColor = Settings.getFullScreenZoomEffectColor();
    int zoomEffectDuration = Settings.getFullScreenZoomEffectDuration();
    
    // Create background settings
    backgroundColorButton  = new ColorButton( backgroundColor, 
					      new Dimension( 10, 10 ) );

    JPanel backgroundPanel = new JPanel( new GridBagLayout() );
    backgroundPanel.setBorder( BorderFactory.createTitledBorder(
      BorderFactory.createEtchedBorder(),
      "Background color",
      TitledBorder.LEFT,
      TitledBorder.TOP ) );

    backgroundPanel.add( new JLabel("Select color:"), 
			 new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5,
						GridBagConstraints.EAST, 
						GridBagConstraints.NONE, 
						new Insets(5, 5, 5, 5), 0, 0) );

    backgroundPanel.add( backgroundColorButton, 
			 new GridBagConstraints(1, 0, 1, 1, 0.5, 0.5,
						GridBagConstraints.WEST, 
						GridBagConstraints.NONE, 
						new Insets(5, 5, 5, 5), 0, 0) );
            
    JPanel zoomEffectPanel = new JPanel( new GridBagLayout() );
    zoomEffectPanel.setBorder( BorderFactory.createTitledBorder(
      BorderFactory.createEtchedBorder(),
      "Zoom effect",
      TitledBorder.LEFT,
      TitledBorder.TOP ) );


    // Create zoom effect settings
    zoomEffectEnabledBox = new JCheckBox( "Enabled", zoomEffectEnabled );
    zoomEffectColorButton = new ColorButton( zoomEffectColor, 
					 new Dimension( 10, 10 ) );
    zoomEffectDurationLabel = new JLabel( "" + zoomEffectDuration + " ms" );
    zoomEffectDurationSlider = new JSlider( JSlider.HORIZONTAL,
					    60,
					    1000,
					    zoomEffectDuration );
    
    zoomEffectPanel.add( zoomEffectEnabledBox, 
			 new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
						GridBagConstraints.EAST, 
						GridBagConstraints.NONE, 
						new Insets(5, 5, 5, 5), 0, 0) );
    
    zoomEffectPanel.add( new JLabel("Color:"), 
			 new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, 
						GridBagConstraints.NONE, 
						new Insets(5, 5, 5, 5), 0, 0) );
    
    zoomEffectPanel.add( zoomEffectColorButton, 
			 new GridBagConstraints(2, 0, 1, 1, 0.5, 0.25,
						GridBagConstraints.WEST, 
						GridBagConstraints.NONE, 
						new Insets(5, 5, 5, 5), 0, 0) );
    
    zoomEffectPanel.add( new JLabel("Duration:"),
			 new GridBagConstraints(0, 1, 3, 1, 1.0, 0.25,
						GridBagConstraints.CENTER, 
						GridBagConstraints.NONE, 
						new Insets(5, 5, 5, 5), 0, 0) );

    zoomEffectPanel.add( zoomEffectDurationLabel,
			 new GridBagConstraints(0, 3, 3, 1, 1.0, 0.25,
						GridBagConstraints.SOUTH, 
						GridBagConstraints.NONE, 
						new Insets(5, 5, 5, 5), 0, 0) );

    zoomEffectPanel.add( zoomEffectDurationSlider,
			 new GridBagConstraints(0, 2, 3, 1, 1.0, 0.25,
						GridBagConstraints.CENTER, 
						GridBagConstraints.BOTH, 
						new Insets(5, 5, 5, 5), 0, 0) );



    // Add panels
    this.add( backgroundPanel,
	      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.3,
				     GridBagConstraints.NORTH, 
				     GridBagConstraints.BOTH, 
				     new Insets(5, 5, 5, 5), 0, 0) );
    
    this.add( zoomEffectPanel,
	      new GridBagConstraints(0, 1, 1, 1, 1.0, 0.7,
				     GridBagConstraints.SOUTH, 
				     GridBagConstraints.BOTH, 
				     new Insets(5, 5, 5, 5), 0, 0) );
    

    // Add all listeners
    backgroundColorButton.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent ev ) {
	Color selected = JColorChooser.showDialog( FullScreenSettings.this,
          "Select fullscreen background color",
           backgroundColorButton.getColor() );

	if( selected != null )
	  backgroundColorButton.setColor( selected );
      }
    });

    zoomEffectColorButton.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent ev ) {
	Color selected = JColorChooser.showDialog( FullScreenSettings.this,
          "Select zoom effect color",
           zoomEffectColorButton.getColor() );

	if( selected != null )
	  zoomEffectColorButton.setColor( selected );
      }
    });

    zoomEffectDurationSlider.addChangeListener( new ChangeListener() {
      public void stateChanged( ChangeEvent ev ) {
 	zoomEffectDurationLabel.setText( "" + zoomEffectDurationSlider.getValue() + 
					 " ms" );
      }
    });
  }
  
  /** Returns the selected background color
   * @return the selected color
   */
  public Color getBackgroundColor() {
    return backgroundColorButton.getColor();
  }
   
  /**
   * Returns if the zoom effect is enabled
   * @return true id zoom effect is enabled
   */
  public boolean getZoomEffectEnabled() {
    return zoomEffectEnabledBox.isSelected();
  }

  /**
   * Returns the zoom effect color
   * @return zoom effect Color
   */
  public Color getZoomEffectColor() {
    return zoomEffectColorButton.getColor();
  }
  
  /**
   * Get the zoom effect duration 
   * @return the zoom effect duration in milliseconds
   */
  public int getZoomEffectDuration() {
    return zoomEffectDurationSlider.getValue();
  }
  
}

// a button with a colour icon
class ColorButton extends JButton {

  ColorIcon icon;

  public ColorButton( Color color, Dimension size ) {
    super();

    icon = new ColorIcon( color, size );

    this.setIcon( icon );
    this.setMargin( new Insets(0, 0, 0, 0) );
  }

  public Color getColor() { return icon.getColor(); }
  
  public void setColor( Color color ) { 
    icon.setColor( color );
    repaint();
  }
  
  class ColorIcon implements Icon {
    
    private Dimension size;
    private Color color;
    
    public ColorIcon( Color color, Dimension size ) {
      this.color = color;
      this.size = size;
    }
    
    public Color getColor() { return color; }

    public void setColor( Color color ) { this.color = color; }
    
    public int getIconWidth() { return size.width; }
    
    public int getIconHeight() { return size.height; }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
      g.setColor( color );
      g.fill3DRect( x, y, getIconWidth(), getIconHeight(), true );
    }

  }
}
