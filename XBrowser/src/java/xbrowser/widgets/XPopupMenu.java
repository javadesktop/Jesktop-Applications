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
import java.awt.*;

public class XPopupMenu extends JPopupMenu
{
	public void show(Component invoker, int x, int y)
	{
	Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension size = getPreferredSize();
	Point invoker_abs_loc = invoker.getLocationOnScreen();

		if( x+invoker_abs_loc.x+size.width>=screen_size.width )
			x = screen_size.width - size.width - invoker_abs_loc.x - 10;

		if( y+invoker_abs_loc.y+size.height>=screen_size.height )
			y = screen_size.height - size.height - invoker_abs_loc.y - 10;

		super.show(invoker,x,y);
	}
}

