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
package xbrowser.history;

import java.util.*;

public class XHistoryComparator implements Comparator
{
	public XHistoryComparator(int sortCol, boolean sortAsc)
	{
		this.sortedCol = sortCol;
		this.sortAsc = sortAsc;
	}

	public int compare(Object o1, Object o2)
	{
		if( !(o1 instanceof XHistoryData) || !(o2 instanceof XHistoryData) )
			return 0;

	XHistoryData   hd1 = (XHistoryData) o1;
	XHistoryData   hd2 = (XHistoryData) o2;
	int			result = 0;

		switch( sortedCol )
		{
			case 0:		// title
				result = hd1.getTitle().compareToIgnoreCase(hd2.getTitle());
				break;

			case 1:		// location
				result = hd1.getLocation().compareToIgnoreCase(hd2.getLocation());
				break;

			case 2:		// firstVisited
				result = hd1.getFirstVisited().compareTo(hd2.getFirstVisited());
				break;

			case 3:		// lastVisited
				result = hd1.getLastVisited().compareTo(hd2.getLastVisited());
				break;

			case 4:		// expiration
				result = hd1.getExpiration().compareTo(hd2.getExpiration());
				break;

			case 5:		// visitCount
				result = hd1.getVisitCount() < hd2.getVisitCount() ? -1 : (hd1.getVisitCount() > hd2.getVisitCount() ? 1 : 0);
				break;
		}

		if( !sortAsc )
			result = -result;

		return result;
	}

	public boolean equals(Object obj)
	{
		if( obj instanceof XHistoryComparator )
		{
		XHistoryComparator compObj = (XHistoryComparator) obj;

			return (compObj.sortedCol == sortedCol) && (compObj.sortAsc == sortAsc);
		}

		return false;
	}

// Attributes:
	private int		sortedCol;
	private boolean   sortAsc;
}
