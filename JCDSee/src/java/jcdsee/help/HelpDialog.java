package jcdsee.help;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

import java.net.*;
import java.io.IOException;
import org.jesktop.frimble.*;

/**
 * Displays a help-browser dialog using simple html-based help pages.
 * The dialog provides a Back, Next, Index button, and a Web-address
 * input field.
 *
 */
public class HelpDialog extends JDialog {

    // URL:s
    private URL indexURL = null;

    private WebScrollPane WebPane = new WebScrollPane();
    private JTextField WebAddField = new JTextField();

    /**
     * Create a new help browser dialog
     *
     * @param     frame  the frame
     * @param     title  string with the dialog title
     * @param     modal  is dialog modal
     */
    public HelpDialog(JFrimble frame, String title, boolean modal) {
  super(frame.getOwnerFrame(), title, modal);

  // Layout
  JPanel ButtonPanel = new JPanel();
  ButtonPanel.setBorder(BorderFactory.createEtchedBorder());
  ButtonPanel.setMinimumSize(new Dimension(350, 70));
  ButtonPanel.setPreferredSize(new Dimension(350, 70));
  ButtonPanel.setLayout(new GridBagLayout());

  JButton IndexButton = new JButton();
  IndexButton.setActionCommand("IndexButton");
  IndexButton.setText("Index");
  IndexButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    IndexButton_actionPerformed(e);
      }
  });

  JButton BackButton = new JButton();
  BackButton.setActionCommand("BackButton");
  BackButton.setText("Back");
  BackButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    BackButton_actionPerformed(e);
      }
  });

  JButton ForwardButton = new JButton();
  ForwardButton.setActionCommand("ForwardButton");
  ForwardButton.setText("Forward");
  ForwardButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    ForwardButton_actionPerformed(e);
      }
  });

  JButton closeButton = new JButton();
  closeButton.setActionCommand("CloseButton");
  closeButton.setText("Close");
  closeButton.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      closeButton_actionPerformed(e);
    }
  });


  WebAddField.setMinimumSize(new Dimension(200, 21));
  WebAddField.setPreferredSize(new Dimension(220, 30));
  WebAddField.setToolTipText("");
  WebAddField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
    WebAddField_actionPerformed(e);
      }
  });

  JLabel AddressLabel = new JLabel();
  AddressLabel.setToolTipText("");
  AddressLabel.setText("Address:");

  WebPane.setMinimumSize(new Dimension(400, 300));
  WebPane.setPreferredSize(new Dimension(400, 300));

  this.getContentPane().add(ButtonPanel, BorderLayout.NORTH);

  ButtonPanel.add(IndexButton,
      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST,
                 GridBagConstraints.NONE,
                 new Insets(5, 5, 5, 5), 0, 0));
  ButtonPanel.add(BackButton,
      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST,
                 GridBagConstraints.NONE,
                 new Insets(5, 5, 5, 5), 0, 0));
  ButtonPanel.add(ForwardButton,
      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST,
                 GridBagConstraints.NONE,
                 new Insets(5, 5, 5, 5), 0, 0));
  ButtonPanel.add(closeButton,
      new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST,
                 GridBagConstraints.NONE,
                 new Insets(5, 5, 5, 5), 0, 0));

  ButtonPanel.add(WebAddField,
      new GridBagConstraints(1, 1, 3, 1, 1.0, 0.0,
                 GridBagConstraints.WEST,
                 GridBagConstraints.HORIZONTAL,
                 new Insets(5, 5, 5, 5), 0, 0));

  ButtonPanel.add(AddressLabel,
      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST,
                 GridBagConstraints.HORIZONTAL,
                 new Insets(5, 5, 5, 5), 0, 0));

  this.getContentPane().add(WebPane, BorderLayout.CENTER);

  this.setSize(new Dimension(640,505));

  // center dialog in middle of screen

    }

    /**
     * Create a new help browser dialog
     *
     */
    public HelpDialog() {
  this(null, "", false);
    }

    // -- Public methods

    /**
     * Sets the index URL.
     *
     * @param     url    a String pointing to the index page
     */
    public void setIndexURL( String url ) throws MalformedURLException {
  setIndexURL( new URL( url ) );
    }

    /**
     * Sets the index URL.
     *
     * @param     url    an URL pointing to the index page
     */
    public void setIndexURL( URL url ) {
  indexURL = url;
    }

    /**
     * Go to a specific web-page. The current page is added to history.
     *
     * @param     url    a String pointing to the page
     */
    public void gotoPage( String url ) {
  try {
      WebPane.gotoPage( url );
      WebAddField.setText( WebPane.getPage().toString() );
  }
  catch( MalformedURLException e1 ) {
      displayErrorInvalidURL( url );
  }
  catch( IOException e2 ) {
      displayErrorOpeningPage( url );
  }
    }

    /**
     * Go to a specific web-page. The current page is added to history.
     *
     * @param     url    an URL pointing to the page
     */
    public void gotoPage( URL url ) {
  try {
      WebPane.gotoPage( url );
      WebAddField.setText( WebPane.getPage().toString() );
  }
  catch( IOException e ) {
      displayErrorOpeningPage( url.toString() );
  }
    }

    // -- Private methods

    private void displayErrorOpeningPage( String url ) {
  JOptionPane.showMessageDialog( this,
               "Could not open page: " + url,
               "Error",
               JOptionPane.ERROR_MESSAGE);
    }

    private void displayErrorInvalidURL( String url ) {
  JOptionPane.showMessageDialog( this,
               "Invalid URL: " + url,
               "Error",
               JOptionPane.ERROR_MESSAGE);
    }


    // -- Events

    private void IndexButton_actionPerformed(ActionEvent e) {
  // Index button pressed - go to index page
  if( indexURL != null )
      gotoPage( indexURL );
    }

    void BackButton_actionPerformed(ActionEvent e) {
  try {
      WebPane.prevPage();
      WebAddField.setText( WebPane.getPage().toString() );
  }
  catch(IOException except ) { }
    }

    void ForwardButton_actionPerformed(ActionEvent e) {
  try {
      WebPane.nextPage();
      WebAddField.setText( WebPane.getPage().toString() );
  }
  catch(IOException except ) { }
    }

    void WebAddField_actionPerformed(ActionEvent e) {
  // Pressed ENTER
  gotoPage( WebAddField.getText() );
    }

    void closeButton_actionPerformed(ActionEvent e) {
      this.dispose();
    }

  /**
   * Centers this <code>BaseDialog</code> on screen.
   */
    public void centerOnScreen() {
      Dimension screenDimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameDimension = getSize();
      setLocation( (screenDimension.width-frameDimension.width)/2,
       (screenDimension.height-frameDimension.height)/2 );
    }
}










