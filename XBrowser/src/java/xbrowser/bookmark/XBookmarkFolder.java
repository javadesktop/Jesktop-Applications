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
import java.util.*;
import java.io.*;

public class XBookmarkFolder extends XAbstractBookmark
{
	public void addBookmark(XAbstractBookmark bookmark)
	{
		if( bookmarks.contains(bookmark) )
			return;

		if( bookmark.getParent()!=null )
			bookmark.getParent().removeBookmark(bookmark);

		bookmarks.add(bookmark);
		bookmark.setParent(this);

		XBookmarkManager.getInstance().bookmarkAdded(bookmark,this);
	}

	public void removeBookmark(XAbstractBookmark bookmark)
	{
		if( !bookmarks.contains(bookmark) )
			return;

		bookmark.setParent(null);
		bookmarks.remove(bookmark);

		XBookmarkManager.getInstance().bookmarkRemoved(bookmark,this);
	}

	public void removeAllBookmarks()
	{
	int size = bookmarks.size();

		for( int i=0; i<size; i++ )
			removeBookmark( (XAbstractBookmark)bookmarks.get(0) );
	}

	public Iterator getBookmarks()
	{
		return bookmarks.iterator();
	}

	public int getBookmarkCount()
	{
		return bookmarks.size();
	}

	public XAbstractBookmark getBookmarkAt(int index)
	{
		return (XAbstractBookmark)bookmarks.get(index);
	}

	public void setPersonalFolder(boolean is_personal)
	{
		if( is_personal )
			XBookmarkManager.getInstance().setPersonalFolder(this);
		else
			XBookmarkManager.getInstance().setPersonalFolder(null);
	}

	public boolean isPersonalFolder()
	{
		return( XBookmarkManager.getInstance().getPersonalFolder()==this );
	}

	public void open()
	{
	Iterator it = getBookmarks();

		while( it.hasNext() )
			((XAbstractBookmark)it.next()).open();
	}

	public void copy()
	{
	StringSelection str_sel = new StringSelection(title);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str_sel,str_sel);
	}

// Attributes:
    private static XBookmarkFolder personalFolder = null;
    private LinkedList bookmarks = new LinkedList();
}
