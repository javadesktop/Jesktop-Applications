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
package xbrowser.content;

import xbrowser.*;
import xbrowser.content.event.*;
import xbrowser.content.view.*;

public final class XContentFactory
{
	public static XContentView createContent(int type, XContentListener listener)
	{
        switch( type )
        {
			case XProjectConstants.DESKTOP_PANE_CONTENT:
		        return new XContentDesktop(listener);

			case XProjectConstants.TABBED_PANE_CONTENT:
				return new XContentTab(listener);

			case XProjectConstants.WORKBOOK_CONTENT:
				return new XContentWorkbook(listener);

			default:
				return null;
		}
	}
}
