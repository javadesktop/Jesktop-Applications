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
package xbrowser.doc.event;

import java.awt.*;
import xbrowser.doc.*;

public interface XDocumentListener
{
    public void documentLoadingStarted(XDocument doc);
    public void documentLoadingFinished(XDocument doc);

    public void pageAddedToDocumentHistory(XDocument doc, String url);
}
