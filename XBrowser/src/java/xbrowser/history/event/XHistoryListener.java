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
package xbrowser.history.event;

import xbrowser.history.*;

public interface XHistoryListener
{
	public void historyAdded(XHistoryData history_data);
	public void historyRemoved(XHistoryData history_data);
	public void clearHistory();
}
