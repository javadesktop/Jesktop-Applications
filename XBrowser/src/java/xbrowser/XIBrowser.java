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
package xbrowser;

import java.awt.*;

import xbrowser.bookmark.*;
import xbrowser.history.*;
import xbrowser.doc.*;

public interface XIBrowser
{
    public void openInActiveDocument(XBookmark bookmark);
    public void openInNewDocument(XBookmark bookmark);

    public void openInActiveDocument(XHistoryData history_data);
    public void openInNewDocument(XHistoryData history_data);

    public void showInActiveDocument(String url);
	public void showInNewDocument(String url);

	public XDocument getActiveDocument();
	public void activateDocument(XDocument doc);
	public void showPopupMenu(Component invoker, String hyper_link, int x, int y);
}
