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

import xbrowser.*;
import xbrowser.bookmark.*;

public class XBookmarkOperaSerializer extends XBookmarkSerializer
{
	XBookmarkOperaSerializer()
	{
	}

	public String getName()
	{
		return "Opera";
	}

	public String getFormat()
	{
		return "adr";
	}

	public boolean hasSingleFileStructure()
	{
		return true;
	}

	public void importFrom(String file_name, XBookmarkFolder root_folder) throws Exception
	{
	RandomAccessFile rand_file = new RandomAccessFile(file_name, "r");
	String line;

		while( (line=rand_file.readLine())!=null )
		{
			if( line.toUpperCase().startsWith("#FOLDER") )
				loadBookmarkFolder(rand_file, root_folder);
			else if( line.toUpperCase().startsWith("#URL") )
				loadBookmark(rand_file, root_folder);
		}

		rand_file.close();
	}

	private Date buildDate(String str)
	{
	Date date = null;

        try
        {
        	date = new Date( Long.parseLong(str+"000") );
		}
		catch( Exception e )
		{
			date = new Date();
		}

		return date;
	}

	private void loadBookmark(RandomAccessFile rand_file, XBookmarkFolder parent)
	{
		try
		{
		XBookmark bookmark = new XBookmark("");
		String line;

			while( (line=rand_file.readLine())!=null )
			{
				line = line.trim();

				if( line.equals("") )
					break;
				else if( line.toUpperCase().startsWith("NAME=") )
					bookmark.setTitle( line.substring(5) );
				else if( line.toUpperCase().startsWith("CREATED=") )
				{
					bookmark.setCreationDate( buildDate(line.substring(8)) );
					bookmark.setModificationDate( buildDate(line.substring(8)) );
				}
				else if( line.toUpperCase().startsWith("DESCRIPTION=") )
					bookmark.setDescription( line.substring(12) );
				else if( line.toUpperCase().startsWith("URL=") )
					bookmark.setHRef( line.substring(4) );
			}

			parent.addBookmark(bookmark);
		}
		catch( Exception e )
		{
		}
	}

	private void loadBookmarkFolder(RandomAccessFile rand_file, XBookmarkFolder parent)
	{
		try
		{
		XBookmarkFolder bm_folder = new XBookmarkFolder();
		boolean personal_Folder = false;
		String line;

			while( (line=rand_file.readLine())!=null )
			{
				line = line.trim();

				if( line.equals("") )
					break;
				else if( line.toUpperCase().startsWith("NAME=") )
					bm_folder.setTitle( line.substring(5) );
				else if( line.toUpperCase().startsWith("CREATED=") )
				{
					bm_folder.setCreationDate( buildDate(line.substring(8)) );
					bm_folder.setModificationDate( buildDate(line.substring(8)) );
				}
				else if( line.toUpperCase().startsWith("DESCRIPTION=") )
					bm_folder.setDescription( line.substring(12) );
				else if( line.toUpperCase().startsWith("ACTIVE FOLDER=YES") )
					personal_Folder = true;
			}

			parent.addBookmark(bm_folder);
			if( personal_Folder )
				bm_folder.setPersonalFolder(true);

			while( (line=rand_file.readLine())!=null )
			{
				line = line.trim();

				if( line.equals("-") )
					break;
				else if( line.toUpperCase().startsWith("#FOLDER") )
					loadBookmarkFolder(rand_file, bm_folder);
				else if( line.toUpperCase().startsWith("#URL") )
					loadBookmark(rand_file, bm_folder);
			}
		}
		catch( Exception e )
		{
		}
	}

	public void exportTo(String file_name, XBookmarkFolder root_folder) throws Exception
	{
	FileWriter file_writer = new FileWriter(file_name);

		file_writer.write("Opera Hotlist version 2.0\n\n");

	Iterator bookmarks = root_folder.getBookmarks();
	int i = 0;

		while( bookmarks.hasNext() )
		{
			saveBookmarksImpl( (XAbstractBookmark)bookmarks.next(), file_writer, i++);
			file_writer.write("\n");
		}

		try
		{
			file_writer.close();
		}
		catch( Exception e )
		{
		}
	}

	private String prepareDateForSaving(Date date)
	{
	String str = ""+date.getTime();

		str = str.substring(0, str.length()-3);
		return str;
	}

	private void saveBookmarksImpl(XAbstractBookmark abs_bm, FileWriter file_writer, int order)
	{
		try
		{
			if( abs_bm instanceof XBookmark )
				saveBookmark( (XBookmark)abs_bm, file_writer, order);
			else if( abs_bm instanceof XBookmarkFolder )
				saveBookmarkFolder( (XBookmarkFolder)abs_bm, file_writer, order);
		}
		catch( Exception e )
		{
		}
	}

	private void saveBookmark(XBookmark bookmark, FileWriter file_writer, int order) throws Exception
	{
		file_writer.write("#URL\n");
		file_writer.write("\tNAME="+bookmark.getTitle()+"\n");
		file_writer.write("\tURL="+bookmark.getHRef()+"\n");
		file_writer.write("\tCREATED="+prepareDateForSaving(bookmark.getCreationDate())+"\n");
		file_writer.write("\tVISITED=0\n");
		file_writer.write("\tORDER="+order+"\n");
		file_writer.write("\tDESCRIPTION="+bookmark.getDescription()+"\n");
		file_writer.write("\tSHORT NAME=\n");
	}

	private void saveBookmarkFolder(XBookmarkFolder bm_folder, FileWriter file_writer, int order) throws Exception
	{
		file_writer.write("#FOLDER\n");
		file_writer.write("\tNAME="+bm_folder.getTitle()+"\n");
		file_writer.write("\tCREATED="+prepareDateForSaving(bm_folder.getCreationDate())+"\n");
		file_writer.write("\tVISITED=0\n");
		file_writer.write("\tORDER="+order+"\n");

		if( bm_folder.isPersonalFolder() )
			file_writer.write("\tACTIVE FOLDER=YES\n");

		file_writer.write("\tDESCRIPTION="+bm_folder.getDescription()+"\n");
		file_writer.write("\tSHORT NAME=\n\n");

	Iterator bookmarks = bm_folder.getBookmarks();
	int i = 0;

		while( bookmarks.hasNext() )
		{
			saveBookmarksImpl( (XAbstractBookmark)bookmarks.next(), file_writer, i++);
			file_writer.write("\n");
		}

		file_writer.write("-");
	}
}
