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
package xbrowser.doc;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.print.*;

import xbrowser.widgets.*;
import xbrowser.renderer.*;
import xbrowser.doc.event.*;

public interface XDocument
{
	public Component getComponent();
	public XRenderer getRenderer();

    public void showURL(String url);
    public void closingDocument();

    public void addDocumentListener(XDocumentListener listener);
    public void removeDocumentListener(XDocumentListener listener);

    public void showPageSource();

    public String getPageTitle();
    public String getPageCompletePath();

    public void printDocument();
    public void printDocumentImmediately();
}
