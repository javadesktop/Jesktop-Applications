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
package xbrowser.bookmark;

import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.*;

import xbrowser.*;

public class XBookmark extends XAbstractBookmark
{
	public XBookmark(String href)
	{
		setHRef(href);
	}

	public void setHRef(String new_href)
	{
	String old_href = href;

		href = new_href;
		propChangeSupport.firePropertyChange("HRef",old_href,new_href);
	}

	public String getHRef()
	{
		return href;
	}

	public void openInSamePage()
	{
		XBrowser.getBrowser().openInActiveDocument(this);
	}

	public void open()
	{
		XBrowser.getBrowser().openInNewDocument(this);
	}

	public void copy()
	{
	StringSelection str_sel = new StringSelection(href);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str_sel,str_sel);
	}

// Attributes:
	private String href = "";
}
