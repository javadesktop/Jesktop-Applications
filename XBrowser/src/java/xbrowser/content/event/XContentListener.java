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
package xbrowser.content.event;

import java.awt.*;
import xbrowser.doc.*;

public interface XContentListener
{
    public void showPopupMenu(Component invoker, int x, int y);
    public void documentActivated(XDocument doc);
    public void documentAdded(XDocument doc);
    public void documentClosed(XDocument doc);
}
