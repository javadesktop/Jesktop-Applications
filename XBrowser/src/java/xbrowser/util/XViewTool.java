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
package xbrowser.util;

import xbrowser.*;

public final class XViewTool
{
	public XViewTool()
	{
    	visible = true;
    	placement = XProjectConstants.TOOLS_PAGE1;
	}

	public XViewTool(int placement)
	{
    	visible = true;
    	this.placement = placement;
	}

	public boolean isVisible()
    {
    	return visible;
    }

	public void setVisible(boolean b)
    {
    	visible = b;
    }

	public int getPlacement()
    {
    	return placement;
    }

	public void setPlacement(int placement)
    {
    	this.placement = placement;
    }

	public boolean equals(Object obj)
	{
		if( !(obj instanceof XViewTool) )
			return false;

	XViewTool tool = (XViewTool)obj;

		return( visible==tool.isVisible() && placement==tool.getPlacement() );
	}

// Attributes:
    private boolean visible;
    private int placement;
}
