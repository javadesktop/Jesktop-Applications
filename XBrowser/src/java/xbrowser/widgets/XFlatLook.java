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
import java.awt.image.*;
import java.awt.color.*;
import javax.swing.*;
import javax.swing.border.*;

public class XFlatLook extends MouseAdapter
{
	static JButton buildFlatLook(JButton btn, int width, int height)
	{
		new XFlatLook(btn, width, height);
		return btn;
	}

	private XFlatLook(JButton btn, int width, int height)
	{
		raisedBorder = XComponentBuilder.getInstance().buildRaisedBorder();
		loweredBorder = XComponentBuilder.getInstance().buildLoweredBorder();
		inactiveBorder = XComponentBuilder.getInstance().buildInactiveBorder();

		btn.setMargin(new Insets(1,1,1,1));

	Dimension size;

	    if( width==-1 || height==-1 )
			size = btn.getPreferredSize();
		else
			size = new Dimension(width,height);

		btn.setMinimumSize(size);
		btn.setMaximumSize(size);
		btn.setPreferredSize(size);

		btn.setBorder(inactiveBorder);
		btn.setFocusPainted(false);

		btn.addMouseListener(this);
	}

	public void mousePressed(MouseEvent e)
	{
	JButton btn = (JButton)e.getSource();

	    if( btn.isEnabled() )
	    {
			btn.setSelected(true);
		    btn.setBorder(loweredBorder);
		}
	}

	public void mouseReleased(MouseEvent e)
	{
	JButton btn = (JButton)e.getSource();

		btn.setSelected(false);
	    btn.setBorder((btn.isEnabled() && btn.contains(e.getX(),e.getY())) ? raisedBorder : inactiveBorder);
	}

	public void mouseEntered(MouseEvent e)
	{
	JButton btn = (JButton)e.getSource();

	    if( btn.isEnabled() )
    		btn.setBorder(btn.isSelected() ? loweredBorder : raisedBorder);
		else
	    	btn.setBorder(inactiveBorder);
	}

	public void mouseExited(MouseEvent e)
	{
	JButton btn = (JButton)e.getSource();

	    btn.setBorder(inactiveBorder);
	}

// Attributes:
	private Border raisedBorder,loweredBorder,inactiveBorder;
}
