package jcdsee.settings;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/** GUI for startup settings
 */
public class StartupSettings extends JPanel {

  public final static String STARTUP_VIEW_THUMBNAIL = "Thumbnail";
  public final static String STARTUP_VIEW_LIST      = "List";

  private JRadioButton thumbviewButton;
  private JRadioButton listviewButton;

  private JCheckBox rememberPositionBox;
  private JCheckBox maximizeWindowBox;
  
  /** Constructor
   */
  public StartupSettings() {
    super( new GridBagLayout() );
    
    String selectedView = Settings.getStartupView();
    boolean rememberPosition = Settings.getStartupRememberPosition();
    boolean maximizeWindow = Settings.getStartupMaximizeWindow();
    
    // Create view settings
    thumbviewButton = new JRadioButton( "Thumbnail view",
        (selectedView.equals( STARTUP_VIEW_THUMBNAIL ) ) );
    listviewButton = new JRadioButton( "List view",
        (selectedView.equals( STARTUP_VIEW_LIST ) ) );
    
    ButtonGroup viewButtonGroup = new ButtonGroup();
    viewButtonGroup.add( thumbviewButton );
    viewButtonGroup.add( listviewButton );
    
    JPanel viewPanel = new JPanel( new GridBagLayout() );
    viewPanel.setBorder( BorderFactory.createTitledBorder(
      BorderFactory.createEtchedBorder(),
      "View",
      TitledBorder.LEFT,
      TitledBorder.TOP ) );


    viewPanel.add( thumbviewButton, 
		   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					  GridBagConstraints.WEST, 
					  GridBagConstraints.NONE, 
					  new Insets(5, 5, 5, 5), 0, 0) );
    
    viewPanel.add( listviewButton, 
		   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					  GridBagConstraints.WEST, 
					  GridBagConstraints.NONE, 
					  new Insets(5, 5, 5, 5), 0, 0) );    
    
    // Create position settings
    rememberPositionBox = new JCheckBox( "Remember window position",
					 rememberPosition );
    
    maximizeWindowBox = new JCheckBox( "Maximize window",
				       maximizeWindow );
    
    JPanel positionPanel = new JPanel( new GridBagLayout() );
    positionPanel.setBorder( BorderFactory.createTitledBorder(
      BorderFactory.createEtchedBorder(),
      "Position",
      TitledBorder.LEFT,
      TitledBorder.TOP ) );

    positionPanel.add( rememberPositionBox, 
		       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					      GridBagConstraints.WEST, 
					      GridBagConstraints.NONE, 
					      new Insets(5, 5, 5, 5), 0, 0) );
    
    positionPanel.add( maximizeWindowBox, 
		       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					      GridBagConstraints.WEST, 
					      GridBagConstraints.NONE, 
					      new Insets(5, 5, 5, 5), 0, 0) );


    // Add panels
    this.add( viewPanel,
	      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5,
				     GridBagConstraints.NORTH, 
				     GridBagConstraints.BOTH, 
				     new Insets(5, 5, 5, 5), 0, 0) );
    
    this.add( positionPanel,
	      new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5,
				     GridBagConstraints.NORTH, 
				     GridBagConstraints.BOTH, 
				     new Insets(5, 5, 5, 5), 0, 0) );

  }
  
  /** Returns the selected view, can be either STARTUP_VIEW_THUMBNAIL or
   * STARTUP_VIEW_LIST.
   * @param view the selected view
   */
  public String getSelectedView() {
    if( thumbviewButton.isSelected() )
      return STARTUP_VIEW_THUMBNAIL;
    else
      return STARTUP_VIEW_LIST;
  }

  /** Returns true if the main windows position should be 
   * remembered.
   * @param true if the main window position should be remembered
   */
  public boolean getRememberPosition() {
    return rememberPositionBox.isSelected();
  }

  /** Returns true if the main window size should be 
   * remembered.
   * @param true if the main window size should be remembered
   */
  public boolean getMaximizeWindow() {
    return maximizeWindowBox.isSelected();
  }

}
