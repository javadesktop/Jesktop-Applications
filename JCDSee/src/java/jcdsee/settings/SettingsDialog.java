package jcdsee.settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.jesktop.frimble.*;

/** The dialog with settings
 */
public class SettingsDialog extends JDialog implements ActionListener {

  private JButton okButton;
  private JButton saveButton;
  private JButton cancelButton;
  private ThumbnailSettings thumbnailSize;
  private StartupSettings startup;
  private FullScreenSettings fullscreen;
  private SlideShowSettings slideshow;

  /** Constructor
   * @param owner the owner frame
   */
  public SettingsDialog(JFrimble owner) {
    super(owner.getOwnerFrame(), "Settings", true);
    this.setLocationRelativeTo( owner.getContentPane() );

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

    okButton = new JButton("OK");
    saveButton = new JButton("Save");
    cancelButton = new JButton("Cancel");

    buttonPanel.add(okButton);
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    okButton.addActionListener(this);
    saveButton.addActionListener(this);
    cancelButton.addActionListener(this);

    thumbnailSize = new ThumbnailSettings();
    startup = new StartupSettings();

    fullscreen = new FullScreenSettings();

    slideshow = new SlideShowSettings();

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Thumbnail size", thumbnailSize);
    tabbedPane.addTab("Startup", startup );
    tabbedPane.addTab("Fullscreen", fullscreen );
    tabbedPane.addTab("Slideshow", slideshow );

    JPanel cp = (JPanel)this.getContentPane();

    cp.add(tabbedPane, BorderLayout.CENTER);
    cp.add(buttonPanel, BorderLayout.SOUTH);

    this.setSize( new Dimension(this.getPreferredSize().width + 10,
        this.getPreferredSize().height + 10) );
  }

  /** Used internally to listen for button events
   */
  public void actionPerformed(ActionEvent ev) {
    JButton button = (JButton)ev.getSource();

    // Set settings if not cancel button
    if( button != cancelButton ) {
      setAllSettings();

      if( button == saveButton )
  storeSettings();
    }

    dispose();
  }

  private void setAllSettings() {
    Settings.setThumbnailDimension( thumbnailSize.getSelectedDimension() );

    Settings.setStartupView( startup.getSelectedView() );
    Settings.setStartupRememberPosition( startup.getRememberPosition() );
    Settings.setStartupMaximizeWindow( startup.getMaximizeWindow() );

    Settings.setFullScreenBackground( fullscreen.getBackgroundColor() );
    Settings.setFullScreenZoomEffectEnabled( fullscreen.getZoomEffectEnabled() );
    Settings.setFullScreenZoomEffectColor( fullscreen.getZoomEffectColor() );
    Settings.setFullScreenZoomEffectDuration( fullscreen.getZoomEffectDuration() );

    Settings.setSlideShowWrapEnabled( slideshow.getSlideShowWrapEnabled() );
    Settings.setSlideShowTime( slideshow.getSlideShowTime() );

    // fire to listeners
    Settings.fireSettingsChanged( new SettingsEvent( this,
                 thumbnailSize.isSizeChanged() ) );
  }

  private void storeSettings() {
    Settings.saveSettings();
  }
}

