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
import java.beans.*;
import java.awt.event.*;

import xbrowser.bookmark.*;

class XBookmarkMenuItem extends JMenuItem implements ActionListener, PropertyChangeListener
{
	XBookmarkMenuItem(XBookmark bookmark)
	{
		super(bookmark.getTitle());

		setIcon( XComponentBuilder.getInstance().buildImageIcon(this, "image.Bookmark") );
		setToolTipText(bookmark.getHRef());

		this.bookmark = bookmark;
		if( this.bookmark!=null )
			this.bookmark.addPropertyChangeListener(this);

		addActionListener(this);
	}

	public void removingFromParent()
	{
		if( bookmark!=null )
			bookmark.removePropertyChangeListener(this);
	}

	public XBookmark getBookmark()
	{
		return bookmark;
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
	String prop_name = evt.getPropertyName();
	Object new_value = evt.getNewValue();

		if( prop_name.equals("Title") )
			setText( (String)new_value );
		else if( prop_name.equals("HRef") )
			setToolTipText( (String)new_value );
	}

	public void actionPerformed(ActionEvent e)
	{
		bookmark.openInSamePage();
	}
/*
	protected void processMouseEvent(MouseEvent e)
	{
		if( e.isPopupTrigger() )
		{
		JPopupMenu popup = new JPopupMenu();

			popup.add("aaaa");
			popup.add("bbbb");
			popup.add("cccc");
			popup.add("dddd");
			popup.add("eeee");

			popup.show(this, e.getX(), e.getY());
		}
		else
			super.processMouseEvent(e);
	}
*/
// Attributes:
	private XBookmark bookmark = null;
}
