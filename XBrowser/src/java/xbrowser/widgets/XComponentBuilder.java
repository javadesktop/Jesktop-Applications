/****************************************************************
*              XBrowser  -  eXtended web Browser                *
*                                                               *
*           Copyright (c) 2000-2001  Armond Avanes              *
*     Refer to ReadMe & License files for more information      *
*                                                               *
*                                                               *
*                      By: Armond Avanes                        *
*          Armond@Neda.net     &    ArnoldX@MailCity.com        *
*          http://www.geocities.com/xa_arnold/index.html        *
*****************************************************************/
package xbrowser.widgets;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.image.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.beans.*;

import xbrowser.*;

public final class XComponentBuilder
{
	public static XComponentBuilder getInstance()
	{
		if( instance==null )
			instance = new XComponentBuilder();

		return instance;
	}

	private XComponentBuilder()
	{
		try
		{
			resourceBundle = ResourceBundle.getBundle(XProjectConstants.PRODUCT_NAME);
		}
		catch( Exception e )
		{
			System.out.println("An error occured while trying to load the properties file!");
			e.printStackTrace();
		}
	}

	private String getClassName(Object obj)
	{
	String package_name = obj.getClass().getPackage().getName();
	String class_name = obj.getClass().getName().substring(package_name.length()+1);

		return class_name;
	}

	public String getProperty(Object owner, String key)
	{
		return resourceBundle.getString(getClassName(owner)+"."+key);
	}

	public String getProperty(Object owner, String key, String arg)
	{
	String property = getProperty(owner, key);
	Object[] arguments = {arg};
	String result = java.text.MessageFormat.format(property, arguments);

		return result;
	}

	public String getProperty(Object owner, String key, String arg1, String arg2)
	{
	String property = getProperty(owner, key);
	Object[] arguments = {arg1, arg2};
	String result = java.text.MessageFormat.format(property, arguments);

		return result;
	}

	public JFileChooser getLoadSaveFileChooser()
	{
		if( loadSaveFileChooser==null )
		{
		XFileFilter webFilter = new XFileFilter(new String[] {"htm", "html"}, getProperty(this, "XFileChooser.WebDescription"));

			loadSaveFileChooser = new JFileChooser();

		    loadSaveFileChooser.addChoosableFileFilter(webFilter);
		    loadSaveFileChooser.addChoosableFileFilter(new XFileFilter("txt", getProperty(this, "XFileChooser.TextDescription")));
		    loadSaveFileChooser.addChoosableFileFilter(new XFileFilter("rtf", getProperty(this, "XFileChooser.RichTextDescription")));
		    loadSaveFileChooser.setFileFilter(webFilter);
		}

		return loadSaveFileChooser;
	}

	public JFileChooser getImportExportFileChooser()
	{
		if( importExportFileChooser==null )
			importExportFileChooser = new JFileChooser();

		return importExportFileChooser;
	}

	public ImageIcon buildImageIcon(Object owner, String icon_key)
	{
	    if( (icon_key==null) || (icon_key.trim().equals("")) )
	        return null;

	java.net.URL url = null;

		try
		{
		String rsc = resourceBundle.getString(getClassName(owner)+"."+icon_key);

			url = ((java.net.URLClassLoader) XComponentBuilder.class.getClassLoader()).getResource(rsc);
		}
		catch( MissingResourceException e )
		{
		}

		return( (url!=null) ? new ImageIcon(url) : null );
	}

	public JLabel buildLabel(Object owner, String key)
	{
		return( new JLabel(getProperty(owner, key)) );
	}

	public JLabel buildLabel(Object owner, String key, int align)
	{
		return( new JLabel(getProperty(owner, key),align) );
	}

	public JCheckBox buildCheckBox(Object owner, String key)
	{
		return( new JCheckBox(getProperty(owner, key)) );
	}

	public JCheckBox buildCheckBox(Object owner, String key, boolean selected)
	{
		return( new JCheckBox(getProperty(owner, key), selected) );
	}

	public JRadioButton buildRadioButton(Object owner, String key)
	{
		return( new JRadioButton(getProperty(owner, key)) );
	}

	public JRadioButton buildRadioButton(Object owner, String key, String arg1, String arg2)
	{
		return( new JRadioButton(getProperty(owner, key, arg1, arg2)) );
	}

	public JButton buildButton(XAction action)
    {
	JButton btn = (JButton)initiateAbstractButton(new JButton(), action);

		btn.setMargin(new Insets(3,3,3,3));
		return btn;
    }

/*	public JButton buildSignButton(XAction action)
    {
	JButton btn = (JButton)initiateAbstractButton(new JButton(), action);
    Border border = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),BorderFactory.createLoweredBevelBorder());

        btn.setBorder( BorderFactory.createCompoundBorder(border,BorderFactory.createRaisedBevelBorder()) );
		btn.setFocusPainted(false);

		return btn;
    }*/

	public JButton buildSignButton(XAction action)
    {
	JButton btn = (JButton)initiateAbstractButton(new JButton(), action);

        btn.setBorder( new EmptyBorder(0, 0, 0, 0) );
		btn.setFocusPainted(false);

		return btn;
    }

	public XPopupButton buildPopupButton(XAction action)
	{
		return( new XPopupButton(action) );
	}

	public JButton buildToolbarButton(XAction action)
	{
	XToolbarButton btn = (XToolbarButton)initiateAbstractButton(new XToolbarButton(), action);

    	buildFlatLook(btn, -1, -1);
		btn.setHorizontalTextPosition(JButton.CENTER);
		btn.setVerticalTextPosition(JButton.BOTTOM);

		return buildBnWLook(btn, (ImageIcon)btn.getRealIcon());
	}

	public JButton buildFlatButton(XAction action, int width, int height)
	{
	JButton btn = (JButton)initiateAbstractButton(new JButton(), action);

    	return buildFlatLook(btn, width, height);
	}

    public JButton buildFlatLook(JButton btn, int width, int height)
    {
		return XFlatLook.buildFlatLook(btn, width, height);
    }

    public JButton buildBnWLook(JButton btn, ImageIcon icon)
    {
		return XBnWLook.buildBnWLook(btn, icon);
	}

    public JButton buildBnWLook(JButton btn)
    {
		return XBnWLook.buildBnWLook(btn);
	}

	public JMenu buildMenu(Object owner, String key)
	{
	JMenu menu = new JMenu( XComponentBuilder.getInstance().getProperty(owner, "Menu."+key+".name") );
	ImageIcon icon = buildImageIcon(owner, "Menu."+key+".icon");

        if( icon!=null )
            menu.setIcon(icon);

	String mnemonic_str = getProperty(owner, "Menu."+key+".mnemonic");

        if( mnemonic_str!=null )
            menu.setMnemonic( mnemonic_str.charAt(0) );

		return menu;
	}

	public JMenuItem buildMenuItem(XAction action)
	{
	JMenuItem menu_item = (JMenuItem)initiateAbstractButton(new JMenuItem(), action);
	KeyStroke accel = (KeyStroke)action.getValue(XAction.KEY_STROKE);

		if( accel!=null )
		    menu_item.setAccelerator(accel);

		return menu_item;
	}

	public JCheckBoxMenuItem buildCheckBoxMenuItem(XAction action)
	{
	JCheckBoxMenuItem menu_item = (JCheckBoxMenuItem)initiateAbstractButton(new JCheckBoxMenuItem(), action);
	KeyStroke accel = (KeyStroke)action.getValue(XAction.KEY_STROKE);

		if( accel!=null )
		    menu_item.setAccelerator(accel);

		return menu_item;
	}
/*
	public JRadioButton buildRadioButton(XAction action)
	{
		return( (JRadioButton)initiateAbstractButton(new JRadioButton(), action) );
	}
*/
	private AbstractButton initiateAbstractButton(final AbstractButton abs_btn, XAction action)
	{
	String tooltip = (String)action.getValue(Action.SHORT_DESCRIPTION);
	Integer mnemonic_obj = (Integer)action.getValue(XAction.MNEMONIC);
	int mnemonic = (mnemonic_obj!=null) ? mnemonic_obj.intValue() : -1;

        abs_btn.setText( (String)action.getValue(Action.NAME) );
        abs_btn.setIcon( (Icon)action.getValue(Action.SMALL_ICON) );

        abs_btn.setEnabled(action.isEnabled());
        if( mnemonic!=-1 )
            abs_btn.setMnemonic(mnemonic);
        if( tooltip!=null && !tooltip.equals("") )
        	abs_btn.setToolTipText(tooltip);

        abs_btn.addActionListener(action);

        action.addPropertyChangeListener( new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
			Object new_value = e.getNewValue();

				if( e.getPropertyName().equals("enabled") )
					abs_btn.setEnabled( ((Boolean)new_value).booleanValue() );
				else if( e.getPropertyName().equals(Action.NAME) )
					abs_btn.setText( (new_value==null) ? null : new_value.toString() );
				else if( e.getPropertyName().equals(Action.SMALL_ICON) )
					abs_btn.setIcon( (Icon)new_value );
				else if( e.getPropertyName().equals(Action.SHORT_DESCRIPTION) )
					abs_btn.setToolTipText( (new_value==null) ? null : new_value.toString() );
				else if( e.getPropertyName().equals(XAction.MNEMONIC) )
					abs_btn.setMnemonic( (new_value==null) ? -1 : ((Integer)new_value).intValue() );
			}
		});

		return abs_btn;
	}

	public ImageIcon prepareBnWImage(Image img)
	{
	ImageProducer producer = new FilteredImageSource(img.getSource(), imageFilter);

		return( new ImageIcon(Toolkit.getDefaultToolkit().createImage(producer)) );
	}

	public Border buildRaisedBorder()
	{
		return new BevelBorder(BevelBorder.RAISED) {
			protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int width, int height)
			{
			Color oldColor = g.getColor();
			int h = height;
			int w = width;

				g.translate(x, y);

				g.setColor(getHighlightOuterColor(c));
				g.drawLine(0, 0, 0, h-1);
				g.drawLine(1, 0, w-1, 0);

				//g.setColor(getHighlightInnerColor(c));
				//g.drawLine(1, 1, 1, h-2);
				//g.drawLine(2, 1, w-2, 1);

				g.setColor(getShadowOuterColor(c));
				g.drawLine(1, h-1, w-1, h-1);
				g.drawLine(w-1, 1, w-1, h-2);

				//g.setColor(getShadowInnerColor(c));
				//g.drawLine(2, h-2, w-2, h-2);
				//g.drawLine(w-2, 2, w-2, h-3);

				g.translate(-x, -y);
				g.setColor(oldColor);
			}
		};
	}

	public Border buildLoweredBorder()
	{
		return new BevelBorder(BevelBorder.LOWERED) {
			protected void paintLoweredBevel(Component c, Graphics g, int x, int y,	int width, int height)
			{
			Color oldColor = g.getColor();
			int h = height;
			int w = width;

				g.translate(x, y);

				g.setColor(getShadowInnerColor(c));
				g.drawLine(0, 0, 0, h-1);
				g.drawLine(1, 0, w-1, 0);

				//g.setColor(getShadowOuterColor(c));
				//g.drawLine(1, 1, 1, h-2);
				//g.drawLine(2, 1, w-2, 1);

				g.setColor(getHighlightOuterColor(c));
				g.drawLine(1, h-1, w-1, h-1);
				g.drawLine(w-1, 1, w-1, h-2);

				//g.setColor(getHighlightInnerColor(c));
				//g.drawLine(2, h-2, w-2, h-2);
				//g.drawLine(w-2, 2, w-2, h-3);

				g.translate(-x, -y);
				g.setColor(oldColor);
			}
		};
	}

	public Border buildInactiveBorder()
	{
		return new EmptyBorder(1,1,1,1);
	}

	private class XDesaturateFilter extends RGBImageFilter
	{
		public XDesaturateFilter(int brightness)
		{
			brightnessAdjustment = brightness;
			canFilterIndexColorModel = false;
		}

		public int filterRGB(int x, int y, int rgb)
		{
		int a = rgb & 0xff000000;
		float r = (rgb & 0xff0000) >> 16;
		float g = (rgb & 0x00ff00) >> 8;
		float b = rgb & 0x0000ff;

			r *= CO_RedBandWeight;
			g *= CO_GreenBandWeight;
			b *= CO_BlueBandWeight;

		int gray = (int) Math.min( (r + g + b + brightnessAdjustment), 255 );

			return( a | (gray<<16) | (gray<<8) | gray );
		}

	// Attributes:
		private static final float CO_RedBandWeight = 0.2125f;
		private static final float CO_GreenBandWeight = 0.7154f;
		private static final float CO_BlueBandWeight = 0.0721f;
		private int brightnessAdjustment;
	}

	private class XToolbarButton extends JButton implements PropertyChangeListener
	{
		public XToolbarButton()
		{
			XProjectConfig.getInstance().addPropertyChangeListener("ToolBarStyle", this);
			updateSize();
		}

		public void setText(String text)
		{
			myText = text;

			if( XProjectConfig.getInstance().getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_TEXT_ONLY ||
				XProjectConfig.getInstance().getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_TEXT_ICON )
				super.setText(myText);
			else
				super.setText("");
		}

		public void setIcon(Icon icon)
		{
			myIcon = icon;

			if( XProjectConfig.getInstance().getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_ICON_ONLY ||
				XProjectConfig.getInstance().getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_TEXT_ICON )
				super.setIcon(myIcon);
			else
				super.setIcon(null);
		}

		public Icon getRealIcon()
		{
			return myIcon;
		}

		public void propertyChange(PropertyChangeEvent e)
		{
			updateSize();
			setText(myText);
			setIcon(myIcon);
		}

		private void updateSize()
		{
		Dimension size;

			if( XProjectConfig.getInstance().getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_ICON_ONLY )
				size = XProjectConstants.TOOLBAR_ICON_ONLY_SIZE;
			else if( XProjectConfig.getInstance().getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_TEXT_ONLY )
				size = XProjectConstants.TOOLBAR_TEXT_ONLY_SIZE;
			else if( XProjectConfig.getInstance().getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_TEXT_ICON )
				size = XProjectConstants.TOOLBAR_TEXT_ICON_SIZE;
			else
				return;

			setMinimumSize(size);
			setMaximumSize(size);
			setPreferredSize(size);
		}

	// Attributes:
		private String myText = "";
		private Icon myIcon = null;
	}

// Attributes:
	private static XComponentBuilder instance = null;
	private ResourceBundle resourceBundle = null;
    private JFileChooser loadSaveFileChooser = null;
    private JFileChooser importExportFileChooser = null;

    private XDesaturateFilter imageFilter = new XDesaturateFilter(20);
}
