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
import javax.swing.border.*;

public class XBnWLook extends MouseAdapter
{
	static JButton buildBnWLook(JButton btn)
	{
		return buildBnWLook(btn, (ImageIcon)btn.getIcon());
	}

	static JButton buildBnWLook(JButton btn, ImageIcon icon)
	{
		new XBnWLook(btn, icon);
		return btn;
	}

	private XBnWLook(JButton btn, ImageIcon icon)
	{
		if( icon!=null )
		{
			rolloverIcon = icon;
			defaultIcon = XComponentBuilder.getInstance().prepareBnWImage(icon.getImage());

			btn.setIcon(defaultIcon);
			btn.addMouseListener(this);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
	JButton btn = (JButton)e.getSource();

	    if( btn.isEnabled() && rolloverIcon!=null )
			btn.setIcon(rolloverIcon);
	}

	public void mouseExited(MouseEvent e)
	{
	JButton btn = (JButton)e.getSource();

	    if( btn.isEnabled() && defaultIcon!=null )
			btn.setIcon(defaultIcon);
	}

// Attributes:
	private Icon defaultIcon = null;
	private Icon rolloverIcon = null;
}
