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
package xbrowser.bookmark.io;

import java.io.*;
import java.util.*;

import xbrowser.bookmark.*;

public class XBookmarkIESerializer extends XBookmarkSerializer
{
	XBookmarkIESerializer()
	{
	}

	public String getName()
	{
		return "Internet Explorer";
	}

	public String getFormat()
	{
		return "url";
	}

	public boolean hasSingleFileStructure()
	{
		return false;
	}

	public void importFrom(String file_name, XBookmarkFolder root_folder) throws Exception
	{
	File file = new File(file_name);

		if( !file.isDirectory() )
			throw new Exception("Couldn't find any folder for importing bookmarks as IE format!");

	File[] files = file.listFiles(ieFileFilter);

		for( int i=0; i<files.length; i++ )
		{
			if( files[i].isFile() )
				loadBookmark(files[i], root_folder);
			else
				loadBookmarkFolder(files[i], root_folder);
		}
	}

	private void loadBookmarkFolder(File file, XBookmarkFolder parent)
	{
	XBookmarkFolder bm_folder = new XBookmarkFolder();

		bm_folder.setTitle( file.getName() );
		//bm_folder.setDescription("Imported from IE");
		parent.addBookmark(bm_folder);

	File[] files = file.listFiles(ieFileFilter);

		for( int i=0; i<files.length; i++ )
		{
			if( files[i].isFile() )
				loadBookmark(files[i], bm_folder);
			else
				loadBookmarkFolder(files[i], bm_folder);
		}
	}

	private void loadBookmark(File file, XBookmarkFolder parent)
	{
		try
		{
		XBookmark bookmark = new XBookmark( getHRefFromFile(file) );
		String file_name = file.getName();
		int dot_index = file_name.lastIndexOf('.');

			if( dot_index!=-1 )
				file_name = file_name.substring(0, dot_index);

			bookmark.setTitle(file_name);
			//bookmark.setDescription("Imported from IE");
			parent.addBookmark(bookmark);
		}
		catch( Exception e )
		{
		}
	}

	private String getHRefFromFile(File file) throws Exception
	{
	RandomAccessFile rand_file = null;

		try
		{
		String str = "";

			rand_file = new RandomAccessFile(file, "r");

			while( !str.toUpperCase().startsWith("URL=") )
				str = rand_file.readLine();

			return str.substring(4);
		}
		finally
		{
			if( rand_file!=null )
				rand_file.close();
		}
	}

	public void exportTo(String file_name, XBookmarkFolder root_folder) throws Exception
	{
	File file = new File(file_name);

		if( !file.isDirectory() )
			throw new Exception("Couldn't find any folder to export bookmarks as IE format!");

	Iterator bookmarks = root_folder.getBookmarks();

		while( bookmarks.hasNext() )
			saveBookmarksImpl( (XAbstractBookmark)bookmarks.next(), file);
	}

	private void saveBookmarksImpl(XAbstractBookmark abs_bm, File file)
	{
		try
		{
			if( abs_bm instanceof XBookmark )
				saveBookmark( (XBookmark)abs_bm, file);
			else if( abs_bm instanceof XBookmarkFolder )
				saveBookmarkFolder( (XBookmarkFolder)abs_bm, file);
		}
		catch( Exception e )
		{
		}
	}

	private void saveBookmark(XBookmark bookmark, File file) throws Exception
	{
	File bm_file = new File(file, fixFileName(bookmark.getTitle())+".url");

		if( bm_file.exists() )
			bm_file.delete();

		bm_file.createNewFile();

	RandomAccessFile rand_file = new RandomAccessFile(bm_file,"rw");

		rand_file.writeUTF("[InternetShortcut]\n");
		rand_file.writeUTF("URL="+bookmark.getHRef()+"\n");
		rand_file.writeUTF("Modified=\n");
		rand_file.close();
	}

	private void saveBookmarkFolder(XBookmarkFolder bm_folder, File file) throws Exception
	{
	File bm_folder_file = new File(file, fixFileName(bm_folder.getTitle())+"/");

		bm_folder_file.mkdir();

	Iterator bookmarks = bm_folder.getBookmarks();

		while( bookmarks.hasNext() )
			saveBookmarksImpl( (XAbstractBookmark)bookmarks.next(), bm_folder_file);
	}

	private String fixFileName(String file_name)
	{
	StringBuffer str = new StringBuffer();
	char ch;

		for( int i=0; i<file_name.length(); i++ )
		{
			ch = file_name.charAt(i);

			if( ch=='\\' || ch=='/' || ch==':' || ch=='*' || ch=='?' ||
				ch=='\"' || ch=='<' || ch=='>' || ch=='|' )
				str.append('_');
			else
				str.append(ch);
		}

		return str.toString();
	}

	private class XIEFileFilter implements FileFilter
	{
		public boolean accept(File pathname)
		{
			if( pathname.isFile() )
				return( pathname.toString().endsWith(".url") );
			else
				return true;
		}
	}

// Attributes:
	private FileFilter ieFileFilter = new XIEFileFilter();
}
