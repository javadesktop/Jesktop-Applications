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
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import xbrowser.*;
import xbrowser.doc.*;

public class XDocumentList extends JList implements ListSelectionListener
{
	public XDocumentList()
	{
		super( new DefaultListModel() );

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel( new DefaultListSelectionModel() );
		ToolTipManager.sharedInstance().registerComponent(this);

		registerListeners();
	}

	private void registerListeners()
	{
		addMouseListener( new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				if( e.isPopupTrigger() )
				{
				int index = locationToIndex(e.getPoint());

					if( index!=-1 )
					{
						setSelectedIndex(index);
						XBrowser.getBrowser().showPopupMenu(XDocumentList.this,null,e.getX(),e.getY());
					}
				}
			}
		});

		addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent e)
	{
	XDocument doc = (XDocument)getSelectedValue();

		if( doc!=null )
			XBrowser.getBrowser().activateDocument(doc);
	}

	public String getToolTipText(MouseEvent e)
	{
		if( e==null )
			return null;

	int index = locationToIndex(e.getPoint());

		if( index!=-1 )
		{
		XDocument doc = (XDocument)getModel().getElementAt(index);
		String path = doc.getPageCompletePath();

			return( path.equals("") ? null : path );
		}
		else
			return null;
	}

	public void removeDocument(XDocument doc)
	{
		((DefaultListModel)getModel()).removeElement(doc);
	}

	public void addDocument(XDocument doc)
	{
		((DefaultListModel)getModel()).addElement(doc);
		setSelectedValue(doc,true);
	}
}
