package jcdsee.settings;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ThumbnailSettings extends JPanel {

  public final static int MAX_SIZE = 150;
  public final static int MIN_SIZE = 10;

  private final Dimension originalSize = Settings.getThumbnailDimension();

  private final JSlider widthSlider = 
    new JSlider( JSlider.HORIZONTAL, 
		 MIN_SIZE, 
		 MAX_SIZE, 
		 originalSize.width );

  private final JSlider heightSlider = 
    new JSlider( JSlider.VERTICAL, 
		 MIN_SIZE, 
		 MAX_SIZE, 
		 originalSize.height );
  
  private final DimensionPanel dimensionPanel = new DimensionPanel();
  
  private SizeIcon sizeIcon = new SizeIcon();
  private final JLabel sizeIconLabel = new JLabel( sizeIcon );
  
  public ThumbnailSettings() {
    // Use boder layout
    super(new BorderLayout());
    
    // Flip the slider
    heightSlider.setInverted( true );

    // Add components
    this.add( widthSlider, BorderLayout.NORTH );
    this.add( heightSlider, BorderLayout.WEST );

    this.add( sizeIconLabel, BorderLayout.CENTER );

    this.add( dimensionPanel, BorderLayout.SOUTH );

    // Add listeners
    Updater updater = new Updater();
    widthSlider.addChangeListener( updater );
    heightSlider.addChangeListener( updater );

    dimensionPanel.addActionListener( new ActionListener() {
      public void actionPerformed( ActionEvent ev ) {
	int width = dimensionPanel.getWidthText();
	int height = dimensionPanel.getHeightText();

	width = (width < MIN_SIZE) ? MIN_SIZE : width;
	width = (width > MAX_SIZE) ? MAX_SIZE : width;

	height = (height < MIN_SIZE) ? MIN_SIZE : height;
	height = (height > MAX_SIZE) ? MAX_SIZE : height;
	
	widthSlider.setValue( width );
	heightSlider.setValue( height );
      }
    });
  }
  
  public Dimension getSelectedDimension() {
    return new Dimension( widthSlider.getValue(),
			  heightSlider.getValue() );
  }
  
  public boolean isSizeChanged() {
    return !getSelectedDimension().equals(originalSize);
  }
  
  // Sizeable Icon class
  class SizeIcon implements Icon {
    public int getIconWidth() { return widthSlider.getValue(); }
    
    public int getIconHeight() { return heightSlider.getValue(); }
    
    public void paintIcon( Component c, Graphics g, int x, int y ) {
      g.fill3DRect( x, y, getIconWidth(), getIconHeight(), true );
    }
  }
  
  class Updater implements ChangeListener {
    public void stateChanged( ChangeEvent ev ) {
      sizeIconLabel.repaint();
      if( ev.getSource() == widthSlider ) 
	dimensionPanel.setWidthText( widthSlider.getValue() );
      else if( ev.getSource() == heightSlider ) 
	dimensionPanel.setHeightText( heightSlider.getValue() );
    }
  }
  
  class DimensionPanel extends JPanel implements ActionListener, FocusListener {
    
    private WholeNumberField widthField = new WholeNumberField( 
        Settings.getThumbnailDimension().width, 3 );
    
    private WholeNumberField heightField = new WholeNumberField( 
	Settings.getThumbnailDimension().height, 3 );
    
    private ActionListener alist = null;    

    public DimensionPanel() {
      super( new FlowLayout( FlowLayout.CENTER, 5, 5 ) );
      
      // Add components
      this.add( widthField );
      this.add( new JLabel("x") );
      this.add( heightField );
      this.add( new JLabel("pixels") );
      
      // Add listener
      widthField.addActionListener( this );
      widthField.addFocusListener( this );
      heightField.addActionListener( this );
      heightField.addFocusListener( this );
    }

    public void setWidthText( int value ) {
      widthField.setText( "" + value );
    }
    
    public void setHeightText( int value ) {
      heightField.setText( "" + value );
    }

    public int getWidthText() {
      String text = widthField.getText();
      if( text.equals("") )
	return 0;
      else {
	try {
	  return Integer.parseInt( text );
	}
	catch( NumberFormatException e ) {
	  return Integer.MAX_VALUE;
	}
      }
    }
    
    public int getHeightText() {
      String text = heightField.getText();
      if( text.equals("") )
	return 0;
      else {
	try {
	  return Integer.parseInt( text );
	}
	catch( NumberFormatException e ) {
	  return Integer.MAX_VALUE;
	}
      }
    }
    
    public void actionPerformed( ActionEvent ev ) {
      fireActionEvent( new ActionEvent( this, 
					ActionEvent.ACTION_PERFORMED, 
					this.toString()) );
    }
    
    public void focusGained( FocusEvent ev ) { }

    public void focusLost( FocusEvent ev ) {
      fireActionEvent( new ActionEvent( this, 
					ActionEvent.ACTION_PERFORMED, 
					this.toString()) );
    }

    public void addActionListener( ActionListener listener ) {
      alist = AWTEventMulticaster.add(alist, listener);
    }
    
    public void removeActionListener( ActionListener listener ) {
      alist = AWTEventMulticaster.remove(alist, listener);
    }
    
    public void fireActionEvent( ActionEvent ev ) {
      if(alist != null) {
	alist.actionPerformed( ev );
      }
    }
  }
}
  
