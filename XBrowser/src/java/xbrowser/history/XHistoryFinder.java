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

import xbrowser.history.event.*;

public class XHistoryFinder implements XHistoryListener
{
	public XHistoryFinder()
	{
		XHistoryManager.getInstance().addHistoryListener(this);
	}

	public void historyAdded(XHistoryData history_data)
	{
		checkHistoryData(history_data);
	}

	public void historyRemoved(XHistoryData history_data)
	{
		if( doesFitInCriteria(history_data) )
		{
			for( int i=0; i<historyListeners.size(); i++ )
				((XHistoryListener)historyListeners.get(i)).historyRemoved(history_data);
		}
	}

	public void clearHistory()
	{
		for( int i=0; i<historyListeners.size(); i++ )
			((XHistoryListener)historyListeners.get(i)).clearHistory();
	}

	public void setFindPhrase(String find_phrase)
	{
		findPhrase = find_phrase;
	}

	public void setSearchUrls(boolean search_urls)
	{
		searchUrls = search_urls;
	}

	public void setSearchTitles(boolean search_titles)
	{
		searchTitles = search_titles;
	}

	public void setMatchCase(boolean match_case)
	{
		matchCase = match_case;
	}

	public void setSearchVisitDate(boolean search_visit_date)
	{
		searchVisitDate = search_visit_date;
	}

	public void setVisitDate1(Date visit_date)
	{
		visitDate1 = visit_date;
	}

	public void setVisitDate2(Date visit_date)
	{
		visitDate2 = visit_date;
	}

	public void setVisitDateRange(int visit_date_range)
	{
		visitDateRange = visit_date_range;
	}

	public void find()
	{
	int i;

		for( i=0; i<historyListeners.size(); i++ )
			((XHistoryListener)historyListeners.get(i)).clearHistory();

	int size = XHistoryManager.getInstance().getHistorySize();

		for( i=0; i<size; i++ )
			checkHistoryData( XHistoryManager.getInstance().getHistoryDataAt(i));
	}

	private boolean doesFitInCriteria(XHistoryData hd)
	{
		if( findPhrase==null )
			return false;

		if( !searchUrls && !searchTitles )
			return false;

		if( searchVisitDate )
		{
			if( visitDateRange==AFTER_DATE && visitDate1!=null )
			{
				if( !hd.getLastVisited().after(visitDate1) )
					return false;
			}
			else if( visitDateRange==BEFORE_DATE && visitDate1!=null )
			{
				if( !hd.getLastVisited().before(visitDate1) )
					return false;
			}
			else if( visitDateRange==BETWEEN_DATES && visitDate1!=null && visitDate2!=null )
			{
				if( !hd.getLastVisited().after(visitDate1) || !hd.getLastVisited().before(visitDate2) )
					return false;
			}
		}

	boolean found = false;

		if( findPhrase.trim().equals("") )
			return true;

		if( searchUrls )
		{
			if( matchCase )
			{
				if( hd.getLocation().indexOf(findPhrase)!=-1 )
					found = true;
			}
			else
			{
				if( hd.getLocation().toLowerCase().indexOf(findPhrase.toLowerCase())!=-1 )
					found = true;
			}
		}

		if( searchTitles && !found )
		{
			if( matchCase )
			{
				if( hd.getTitle().indexOf(findPhrase)!=-1 )
					found = true;
			}
			else
			{
				if( hd.getTitle().toLowerCase().indexOf(findPhrase.toLowerCase())!=-1 )
					found = true;
			}
		}

		return found;
	}

	private void checkHistoryData(XHistoryData hd)
	{
		if( doesFitInCriteria(hd) )
		{
			for( int i=0; i<historyListeners.size(); i++ )
				((XHistoryListener)historyListeners.get(i)).historyAdded(hd);
		}
	}

	public void addHistoryListener(XHistoryListener listener)
	{
		if( !historyListeners.contains(listener) )
			historyListeners.add(listener);
	}

	public void removeHistoryListener(XHistoryListener listener)
	{
		historyListeners.remove(listener);
	}

// Attributes:
    private LinkedList historyListeners = new LinkedList();

	public static final int AFTER_DATE = 20;
	public static final int BEFORE_DATE = 21;
	public static final int BETWEEN_DATES = 22;

    private String findPhrase = null;
    private boolean searchUrls = false;
    private boolean searchTitles = false;
    private boolean matchCase = false;

    private boolean searchVisitDate = false;
    private Date visitDate1 = null;
    private Date visitDate2 = null;
    private int visitDateRange = BEFORE_DATE;
}
