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

public class XBookmarkComparator implements Comparator
{
	public XBookmarkComparator(int sort_col, boolean sort_asc)
	{
		sortedCol = sort_col;
		sortAsc = sort_asc;
	}

	public int compare(Object o1, Object o2)
	{
		if( !(o1 instanceof XAbstractBookmark) || !(o2 instanceof XAbstractBookmark) )
			return 0;

	XAbstractBookmark   bm1 = (XAbstractBookmark) o1;
	XAbstractBookmark   bm2 = (XAbstractBookmark) o2;
	int					result = 0;
	String str1,str2;

		switch( sortedCol )
		{
			case 0:		// bookmark type (bookmark or bookmarkfolder)
				if( bm1.getClass().equals(bm2.getClass()) )
					result = bm1.getTitle().compareToIgnoreCase(bm2.getTitle()); // Then compare the titles!
				else if( bm1 instanceof XBookmarkFolder )
					result = -1;
				else
					result = 1;
				break;

			case 1:		// title
				result = bm1.getTitle().compareToIgnoreCase(bm2.getTitle());
				break;

			case 2:		// location
				str1 = (bm1 instanceof XBookmark) ? ((XBookmark)bm1).getHRef() : "";
				str2 = (bm2 instanceof XBookmark) ? ((XBookmark)bm2).getHRef() : "";

				result = str1.compareToIgnoreCase(str2);
				break;

			case 3:		// description
				result = bm1.getDescription().compareTo(bm2.getDescription());
				break;
		}

		if( !sortAsc )
			result = -result;

		return result;
	}

	public boolean equals(Object obj)
	{
		if( obj instanceof XBookmarkComparator )
		{
		XBookmarkComparator compObj = (XBookmarkComparator) obj;

			return (compObj.sortedCol == sortedCol) && (compObj.sortAsc == sortAsc);
		}

		return false;
	}

// Attributes:
	private int		sortedCol;
	private boolean   sortAsc;
}
