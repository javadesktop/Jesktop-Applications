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

import java.awt.*;
import java.util.*;

import xbrowser.doc.*;

public interface XContentView
{
	public XDocument createNewDocument();

    public void closeCurrentDocument();
    public void closeAll(boolean inform);

    public void tileCascade();
    public void tileHorizontal();
    public void tileVertical();
    public void minimizeAll();
    public void restoreAll();
    public boolean supportsTiling();

    public void activateDocument(XDocument doc);

    public Component getComponent();
    public void setTitleFor(XDocument doc, String title);

    public boolean hasDocument();
    public Iterator getDocuments();
    public void setDocuments(Iterator docs);

    public void setContentTabPlacement(int place);
}
