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

import java.util.*;

import xbrowser.bookmark.event.*;

public class XBookmarkFinder implements XBookmarkFolderListener
{
	public XBookmarkFinder()
	{
		XBookmarkManager.getInstance().addBookmarkFolderListener(this);
	}

	private boolean isUnderBaseFolder(XBookmarkFolder parent)
	{
	boolean result = false;
	XBookmarkFolder bm_folder = parent;

		while( bm_folder!=null )
		{
			if( bm_folder==baseFolder )
			{
				result = true;
				break;
			}

			bm_folder = bm_folder.getParent();
		}

		return result;
	}

	public void bookmarkAdded(XAbstractBookmark abs_bm, XBookmarkFolder parent)
	{
		if( isUnderBaseFolder(parent) )
			checkAbstractBookmark(abs_bm);
	}

	public void bookmarkRemoved(XAbstractBookmark abs_bm, XBookmarkFolder parent)
	{
	boolean does_fit = false;

		if( abs_bm instanceof XBookmark )
			does_fit = doesFitInBookmarkCriteria( (XBookmark)abs_bm );
		else if( abs_bm instanceof XBookmarkFolder )
			does_fit = doesFitInBookmarkFolderCriteria( (XBookmarkFolder)abs_bm );

		if( does_fit && isUnderBaseFolder(parent) )
		{
			for( int i=0; i<bookmarkListeners.size(); i++ )
				((XBookmarkFolderListener)bookmarkListeners.get(i)).bookmarkRemoved(abs_bm, null);
		}
	}

	public void personalFolderChanged(XBookmarkFolder old_folder, XBookmarkFolder new_folder)
	{
	}

	public void clearBookmarks()
	{
		for( int i=0; i<bookmarkListeners.size(); i++ )
			((XBookmarkFolderListener)bookmarkListeners.get(i)).clearBookmarks();
	}

	public void setFindPhrase(String find_phrase)
	{
		findPhrase = find_phrase;
	}

	public void setBaseFolder(XBookmarkFolder base_folder)
	{
		baseFolder = base_folder;
	}

	public void setSearchSubFolders(boolean search_sub_folders)
	{
		searchSubFolders = search_sub_folders;
	}

	public void setSearchUrls(boolean search_urls)
	{
		searchUrls = search_urls;
	}

	public void setSearchTitles(boolean search_titles)
	{
		searchTitles = search_titles;
	}

	public void setSearchDescriptions(boolean search_descriptions)
	{
		searchDescriptions = search_descriptions;
	}

	public void setMatchCase(boolean match_case)
	{
		matchCase = match_case;
	}

	public void find()
	{
	int i;

		for( i=0; i<bookmarkListeners.size(); i++ )
			((XBookmarkFolderListener)bookmarkListeners.get(i)).clearBookmarks();

	int size = baseFolder.getBookmarkCount();

		for( i=0; i<size; i++ )
			checkAbstractBookmark( baseFolder.getBookmarkAt(i) );
	}

	private void checkAbstractBookmark(XAbstractBookmark abs_bm)
	{
		if( abs_bm instanceof XBookmark )
			checkBookmark( (XBookmark)abs_bm );
		else if( abs_bm instanceof XBookmarkFolder )
			checkBookmarkFolder( (XBookmarkFolder)abs_bm );
	}

	private boolean doesFitInBookmarkCriteria(XBookmark bm)
	{
		if( findPhrase==null )
			return false;

		if( !searchUrls && !searchTitles && !searchDescriptions )
			return false;

	boolean found = false;

		if( findPhrase.trim().equals("") )
			return true;

		if( searchUrls )
		{
			if( matchCase )
			{
				if( bm.getHRef().indexOf(findPhrase)!=-1 )
					found = true;
			}
			else
			{
				if( bm.getHRef().toLowerCase().indexOf(findPhrase.toLowerCase())!=-1 )
					found = true;
			}
		}

		if( searchTitles && !found )
		{
			if( matchCase )
			{
				if( bm.getTitle().indexOf(findPhrase)!=-1 )
					found = true;
			}
			else
			{
				if( bm.getTitle().toLowerCase().indexOf(findPhrase.toLowerCase())!=-1 )
					found = true;
			}
		}

		if( searchDescriptions && !found )
		{
			if( matchCase )
			{
				if( bm.getDescription().indexOf(findPhrase)!=-1 )
					found = true;
			}
			else
			{
				if( bm.getDescription().toLowerCase().indexOf(findPhrase.toLowerCase())!=-1 )
					found = true;
			}
		}

		return found;
	}

	private void checkBookmark(XBookmark bm)
	{
		if( doesFitInBookmarkCriteria(bm) )
		{
			for( int i=0; i<bookmarkListeners.size(); i++ )
				((XBookmarkFolderListener)bookmarkListeners.get(i)).bookmarkAdded(bm, null);
		}
	}

	private boolean doesFitInBookmarkFolderCriteria(XBookmarkFolder bm_folder)
	{
		if( findPhrase==null )
			return false;

		if( !searchTitles && !searchDescriptions )
			return false;

	boolean found = false;

		if( findPhrase.trim().equals("") )
			return true;

		if( searchTitles )
		{
			if( matchCase )
			{
				if( bm_folder.getTitle().indexOf(findPhrase)!=-1 )
					found = true;
			}
			else
			{
				if( bm_folder.getTitle().toLowerCase().indexOf(findPhrase.toLowerCase())!=-1 )
					found = true;
			}
		}

		if( searchDescriptions && !found )
		{
			if( matchCase )
			{
				if( bm_folder.getDescription().indexOf(findPhrase)!=-1 )
					found = true;
			}
			else
			{
				if( bm_folder.getDescription().toLowerCase().indexOf(findPhrase.toLowerCase())!=-1 )
					found = true;
			}
		}

		return found;
	}

	private void checkBookmarkFolder(XBookmarkFolder bm_folder)
	{
		if( doesFitInBookmarkFolderCriteria(bm_folder) )
		{
			for( int i=0; i<bookmarkListeners.size(); i++ )
				((XBookmarkFolderListener)bookmarkListeners.get(i)).bookmarkAdded(bm_folder, null);
		}

		if( searchSubFolders )
		{
		int size = bm_folder.getBookmarkCount();

			for( int i=0; i<size; i++ )
				checkAbstractBookmark( bm_folder.getBookmarkAt(i) );
		}
	}

	public void addBookmarkListener(XBookmarkFolderListener listener)
	{
		if( !bookmarkListeners.contains(listener) )
			bookmarkListeners.add(listener);
	}

	public void removeBookmarkListener(XBookmarkFolderListener listener)
	{
		bookmarkListeners.remove(listener);
	}

// Attributes:
    private LinkedList bookmarkListeners = new LinkedList();

	private String findPhrase = null;
	private boolean searchSubFolders = false;
	private boolean searchUrls = false;
	private boolean searchTitles = false;
	private boolean searchDescriptions = false;
    private boolean matchCase = false;

	private XBookmarkFolder baseFolder = null;
}
