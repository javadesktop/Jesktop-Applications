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
import java.awt.event.*;
import java.util.*;

import xbrowser.doc.*;
import xbrowser.renderer.*;

public class XBackForwardHistoryPopup extends JPopupMenu implements ActionListener
{
	public XBackForwardHistoryPopup(int history_type)
	{
		historyType = history_type;
	}

	public void setDocument(XDocument doc)
	{
		this.doc = doc;
		update();
	}

	public void update()
	{
		removeAll();

		if( doc==null )
			return;

	JMenuItem menu_item = null;
	Iterator it = null;

		if( historyType==XRenderer.FORWARD_HISTORY )
		{
			if( doc.getRenderer().hasForwardHistory() )
			{
				it = doc.getRenderer().getForwardHistory();

				while( it.hasNext() )
				{
					menu_item = new JMenuItem( (String)it.next() );
					menu_item.addActionListener(this);
					add(menu_item);
				}
			}
		}
		else
		{
			if( doc.getRenderer().hasBackwardHistory() )
			{
				it = doc.getRenderer().getBackwardHistory();

				while( it.hasNext() )
				{
					menu_item = new JMenuItem( (String)it.next() );
					menu_item.addActionListener(this);
					insert(menu_item, 0);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e)
	{
	int index = getComponentIndex((JMenuItem)e.getSource());

		if( historyType==XRenderer.BACKWARD_HISTORY)
			index = getComponentCount() - index - 1;

		doc.getRenderer().showPageFromHistory(index, historyType);
	}

// Attribute:
	private XDocument doc = null;
	private int historyType = XRenderer.BACKWARD_HISTORY;
}

