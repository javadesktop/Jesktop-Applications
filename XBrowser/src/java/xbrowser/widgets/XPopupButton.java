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

import java.awt.*;
import java.beans.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import xbrowser.*;

public class XPopupButton extends JPanel implements MouseListener, PopupMenuListener, PropertyChangeListener
{
	XPopupButton(XAction main_action)
	{
		raisedBorder = XComponentBuilder.getInstance().buildRaisedBorder();
		loweredBorder = XComponentBuilder.getInstance().buildLoweredBorder();
		inactiveBorder = XComponentBuilder.getInstance().buildInactiveBorder();

		setLayout( new BorderLayout() );

		btnMain = new XButton();
		btnMain.setText((String)main_action.getValue(Action.NAME));
		btnMain.setIcon((Icon)main_action.getValue(Action.SMALL_ICON));
		decorateButton(main_action, btnMain);

		btnArrow = buildButton(new ArrowAction());

		updateSize();

	String tooltip = (String)main_action.getValue(Action.SHORT_DESCRIPTION);

        if( tooltip!=null && !tooltip.equals("") )
        {
			btnMain.setToolTipText(tooltip);
			btnArrow.setToolTipText(tooltip);
		}

        setEnabled(main_action.isEnabled());
        btnMain.setEnabled(main_action.isEnabled());
        btnArrow.setEnabled(main_action.isEnabled());

        buildBnWLook();

        main_action.addPropertyChangeListener( new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
			Object new_value = e.getNewValue();

				if( e.getPropertyName().equals("enabled") )
				{
				boolean bool_value = ((Boolean)new_value).booleanValue();

					XPopupButton.this.setEnabled(bool_value);
					btnMain.setEnabled(bool_value);
					btnArrow.setEnabled(bool_value);
				}
				else if( e.getPropertyName().equals(Action.NAME) )
					btnMain.setText( (new_value==null) ? null : new_value.toString() );
				else if( e.getPropertyName().equals(Action.SMALL_ICON) )
					btnMain.setIcon( (Icon)new_value );
				else if( e.getPropertyName().equals(Action.SHORT_DESCRIPTION) )
				{
				String str_value = (new_value==null) ? null : new_value.toString();

					btnMain.setToolTipText(str_value);
					btnArrow.setToolTipText(str_value);
				}
				else if( e.getPropertyName().equals(XAction.MNEMONIC) )
					btnMain.setMnemonic( (new_value==null) ? -1 : ((Integer)new_value).intValue() );
			}
		});

		btnArrow.addMouseListener(this);
		btnMain.addMouseListener(this);
		XProjectConfig.getInstance().addPropertyChangeListener("ToolBarStyle", this);

		add(btnMain, BorderLayout.CENTER);
		add(btnArrow, BorderLayout.EAST);
	}

	private void setComponentSize(JComponent comp, Dimension size)
	{
		comp.setMinimumSize(size);
		comp.setMaximumSize(size);
		comp.setPreferredSize(size);
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

		setComponentSize(btnMain, new Dimension(size.width,size.height));
		setComponentSize(btnArrow, new Dimension(ARROW_WIDTH,size.height));
		setComponentSize(this, new Dimension(size.width+ARROW_WIDTH,size.height));
	}

	private void buildBnWLook()
	{
	ImageIcon icon;

		icon = (ImageIcon)btnMain.getRealIcon();

		if( icon!=null )
		{
			mainRolloverIcon = icon;
			mainDefaultIcon = XComponentBuilder.getInstance().prepareBnWImage(icon.getImage());

			btnMain.setIcon(mainDefaultIcon);
		}

		icon = (ImageIcon)btnArrow.getIcon();

		if( icon!=null )
		{
			arrowRolloverIcon = icon;
			arrowDefaultIcon = XComponentBuilder.getInstance().prepareBnWImage(icon.getImage());

			btnArrow.setIcon(arrowDefaultIcon);
		}
	}

	private JButton buildButton(XAction action)
	{
	JButton btn = new JButton((String)action.getValue(Action.NAME),(Icon)action.getValue(Action.SMALL_ICON));

		return decorateButton(action, btn);
	}

	private JButton decorateButton(XAction action, JButton btn)
	{
		btn.setMargin(new Insets(1,1,1,1));
        btn.addActionListener(action);

		btn.setHorizontalTextPosition(JButton.CENTER);
		btn.setVerticalTextPosition(JButton.BOTTOM);

		btn.setBorder(inactiveBorder);
		btn.setFocusPainted(false);

        return btn;
	}

    public void updateUI()
    {
		super.updateUI();

        if( popup!=null )
        	SwingUtilities.updateComponentTreeUI(popup);
	}

	public JPopupMenu getPopupMenu()
	{
		return popup;
	}

	public void setPopupMenu(JPopupMenu popup)
	{
		if( this.popup!=null )
			this.popup.removePopupMenuListener(this);

		this.popup = popup;
		popup.addPopupMenuListener(this);
	}

	private boolean isPopupVisible()
	{
    	if( popup!=null )
    		return( popup.isVisible() );
    	else
    		return false;
	}

	public void popupMenuCanceled(PopupMenuEvent e)
	{
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
	{
		if( !isEnabled() || !mouseOver )
			btnArrow.setBorder(inactiveBorder);
		else
			btnArrow.setBorder( btnArrow.isSelected() ? loweredBorder : raisedBorder);

		btnMain.setBorder((isEnabled() && mouseOver) ? raisedBorder : inactiveBorder);

		btnArrow.setSelected(false);
		btnMain.setSelected(false);

		if( mouseOver )
		{
			if( mainRolloverIcon!=null )
				btnMain.setIcon(mainRolloverIcon);

			if( arrowRolloverIcon!=null )
				btnArrow.setIcon(arrowRolloverIcon);
		}
		else
		{
			if( mainDefaultIcon!=null )
				btnMain.setIcon(mainDefaultIcon);

			if( arrowDefaultIcon!=null )
				btnArrow.setIcon(arrowDefaultIcon);
		}
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	Point loc = ((Component)e.getSource()).getLocation();

		if( !contains(loc.x+e.getX(),loc.y+e.getY()) )
			return;

		mouseOver = true;

    	if( isPopupVisible() )
    		return;

	    if( isEnabled() )
	    {
			btnMain.setBorder(btnMain.isSelected() ? loweredBorder : raisedBorder);
		    btnArrow.setBorder( (btnMain.isSelected() || btnArrow.isSelected()) ? loweredBorder : raisedBorder);

			if( mainRolloverIcon!=null )
				btnMain.setIcon(mainRolloverIcon);

			if( arrowRolloverIcon!=null )
				btnArrow.setIcon(arrowRolloverIcon);
		}
		else
		{
	    	btnMain.setBorder(inactiveBorder);
	    	btnArrow.setBorder(inactiveBorder);
		}
	}

	public void mouseExited(MouseEvent e)
	{
	Point loc = ((Component)e.getSource()).getLocation();

		if( contains(loc.x+e.getX(),loc.y+e.getY()) )
			return;

		mouseOver = false;

    	if( isPopupVisible() )
    		return;

		btnMain.setBorder(inactiveBorder);
		btnArrow.setBorder(inactiveBorder);

	    if( isEnabled() )
	    {
			if( mainDefaultIcon!=null )
				btnMain.setIcon(mainDefaultIcon);

			if( arrowDefaultIcon!=null )
				btnArrow.setIcon(arrowDefaultIcon);
		}
	}

	public void mousePressed(MouseEvent e)
	{
	    if( isEnabled() )
	    {
			btnMain.setSelected( e.getSource()==btnMain );
			btnMain.setBorder( e.getSource()==btnMain ? loweredBorder : raisedBorder);

	    	btnArrow.setSelected(true);
		    btnArrow.setBorder(loweredBorder);
		}
	}

	public void mouseReleased(MouseEvent e)
	{
    	if( e.getSource()==btnArrow && btnArrow.contains(e.getX(), e.getY()) )
    		return;

	Border border = (isEnabled() && mouseOver) ? raisedBorder : inactiveBorder;

		btnArrow.setSelected(false);
		btnMain.setSelected(false);

		btnArrow.setBorder(border);
		btnMain.setBorder(border);
	}

	public void propertyChange(PropertyChangeEvent e)
	{
		btnMain.update();
		updateSize();
	}

	private class XButton extends JButton
	{
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

		public void update()
		{
			setText(myText);
			setIcon(myIcon);
		}

	// Attributes:
		private String myText = "";
		private Icon myIcon = null;
	}

    private class ArrowAction extends XAction
    {
        public ArrowAction()
        {
            super(XPopupButton.this, "Arrow", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			if( popup!=null )
				popup.show(btnMain,0,btnMain.getHeight());
        }
    }

// Attributes:
	private Border raisedBorder,loweredBorder,inactiveBorder;
	private JPopupMenu popup = null;
	private XButton btnMain;
	private JButton btnArrow;
	private boolean mouseOver = false;

	private Icon mainRolloverIcon = null;
	private Icon mainDefaultIcon = null;
	private Icon arrowRolloverIcon = null;
	private Icon arrowDefaultIcon = null;

	private final static int ARROW_WIDTH = 12;
}
