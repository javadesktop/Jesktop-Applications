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
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import xbrowser.*;
import xbrowser.doc.*;

public class XOpenDocumentsMenu extends JMenu
{
	public XOpenDocumentsMenu()
	{
		setText( XComponentBuilder.getInstance().getProperty(this, "Title") );
		setIcon( XComponentBuilder.getInstance().buildImageIcon(this, "image.Document") );
		//setMnemonic('o');
	}

	public void activateDocument(XDocument doc)
	{
	XDocumentMenuItem menu_item = (XDocumentMenuItem)doc_MenuItem.get(doc);

		if( menu_item!=null && !menu_item.isSelected() )
			menu_item.setSelected(true);
	}

	public void removeDocument(XDocument doc)
	{
	XDocumentMenuItem menu_item = (XDocumentMenuItem)doc_MenuItem.remove(doc);

		if( menu_item!=null )
		{
			remove(menu_item);
			btnDocGroup.remove(menu_item);
			menu_item.setDocument(null);
		}
	}

	public void addDocument(XDocument doc)
	{
	XDocumentMenuItem menu_item = new XDocumentMenuItem(doc);

		add(menu_item);
		btnDocGroup.add(menu_item);
		menu_item.setSelected(true);

		doc_MenuItem.put(doc,menu_item);
	}

	private class XDocumentMenuItem extends JRadioButtonMenuItem implements ActionListener
	{
		public XDocumentMenuItem(XDocument doc)
		{
			setDocument(doc);
			addActionListener(this);
		}

		public void setDocument(XDocument doc)
		{
			this.doc = doc;
		}

		/*protected void processMouseEvent(MouseEvent e)
		{
			if( e.isPopupTrigger() )
			{
				browserContent.activateDocument(doc);
				XBrowser.getBrowser().showPopupMenu(this,null,e.getX(),e.getY());
			}
			else
				super.processMouseEvent(e);
		}*/

		public String getText()
		{
			if( doc!=null )
				return doc.getPageTitle();
			else
				return "";
		}

		public void actionPerformed(ActionEvent e)
		{
			if( doc!=null )
				XBrowser.getBrowser().activateDocument(doc);
		}

	// Attribute:
		private XDocument doc = null;
	}

// Attributes:
	private ButtonGroup btnDocGroup = new ButtonGroup();
	private Map doc_MenuItem = new HashMap();
}

