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

import java.io.*;
import java.util.*;

import xbrowser.*;
import xbrowser.widgets.*;
import xbrowser.bookmark.io.*;
import xbrowser.bookmark.event.*;

public final class XBookmarkManager extends XBookmarkFolder
{
	public static XBookmarkManager getInstance()
    {
		if( instance==null )
			instance = new XBookmarkManager();

        return instance;
    }

	private XBookmarkManager()
	{
		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );
	}

	private void loadDefaults()
	{
	XBookmarkFolder xbrowser_folder = new XBookmarkFolder();
	XBookmark bm;

		xbrowser_folder.setTitle("XBrowser");
		xbrowser_folder.setDescription("XBrowser on the Internet");

		bm = new XBookmark("http://java.sun.com");
		bm.setTitle("java.sun.com");
		bm.setDescription("Java's mother land ;-)");
		addBookmark(bm);

		bm = new XBookmark("http://www.geocities.com/xa_arnold/index.html");
		bm.setTitle("Java! Java! Java!");
		xbrowser_folder.addBookmark(bm);

		bm = new XBookmark("http://www.geocities.com/xa_arnold/XBrowser.html");
		bm.setTitle("XBrowser");
		xbrowser_folder.addBookmark(bm);

		addBookmark(xbrowser_folder);
	}

	public void load()
	{
		try
		{
			XBookmarkSerializer.getDefaultSerializer().importFrom(XProjectConstants.CONFIG_DIR+bookmarkFileName, this);
		}
		catch( Exception e )
		{
			System.out.println("An error occured on loading the bookmarks!");

			removeAllBookmarks();
			loadDefaults();
			XProjectConfig.getInstance().setPersonalToolBar(false);

			//save();
		}
	}

	public void save()
	{
		try
		{
			XBookmarkSerializer.getDefaultSerializer().exportTo(XProjectConstants.CONFIG_DIR+bookmarkFileName, this);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void addBookmarkFolderListener(XBookmarkFolderListener listener)
	{
		if( !bookmarkFolderListeners.contains(listener) )
			bookmarkFolderListeners.add(listener);
	}

	public void removeBookmarkFolderListener(XBookmarkFolderListener listener)
	{
		bookmarkFolderListeners.remove(listener);
	}

	void bookmarkAdded(XAbstractBookmark bookmark, XBookmarkFolder parent)
	{
	XBookmarkFolderListener listener;

		for( int i=0; i<bookmarkFolderListeners.size(); i++ )
		{
			listener = (XBookmarkFolderListener)bookmarkFolderListeners.get(i);
			listener.bookmarkAdded(bookmark,parent);
		}
	}

	void bookmarkRemoved(XAbstractBookmark bookmark, XBookmarkFolder parent)
	{
	XBookmarkFolderListener listener;

		for( int i=0; i<bookmarkFolderListeners.size(); i++ )
		{
			listener = (XBookmarkFolderListener)bookmarkFolderListeners.get(i);
			listener.bookmarkRemoved(bookmark,parent);
		}
	}

	void setPersonalFolder(XBookmarkFolder folder)
	{
		if( personalFolder==folder )
			return;

	XBookmarkFolder old_personal_folder = personalFolder;
	XBookmarkFolderListener listener;

		personalFolder = folder;

		for( int i=0; i<bookmarkFolderListeners.size(); i++ )
		{
			listener = (XBookmarkFolderListener)bookmarkFolderListeners.get(i);
			listener.personalFolderChanged(old_personal_folder,personalFolder);
		}
	}

	XBookmarkFolder getPersonalFolder()
	{
		return personalFolder;
	}

// Attributes:
    private final String bookmarkFileName = "XBookmark.xml";

    private static XBookmarkManager instance = null;

    private LinkedList bookmarkFolderListeners = new LinkedList();
    private XBookmarkFolder personalFolder = null;
}
