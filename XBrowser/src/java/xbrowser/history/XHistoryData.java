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

import java.awt.*;
import java.util.*;
import java.beans.*;
import java.awt.datatransfer.*;

import xbrowser.*;
import xbrowser.history.event.*;

public class XHistoryData
{
	public XHistoryData(String location)
	{
		this.location = location;
		propChangeSupport = new PropertyChangeSupport(this);
	}

	public static XHistoryData buildNewHistoryData(String location, String title)
	{
	Date now = new Date();
	long exp_milsec = MILLISECONDS_PER_DAY * XProjectConfig.getInstance().getHistoryExpirationDays();
	Date expiration = new Date(now.getTime()+exp_milsec);
	XHistoryData new_history = new XHistoryData(location);

		new_history.setTitle(title);
		new_history.setFirstVisited(now);
		new_history.setLastVisited(now);
		new_history.setExpiration(expiration);
		new_history.setVisitCount(1);

		return new_history;
	}

	public void visit(XHistoryData histroy_data)
	{
		setExpiration( histroy_data.getExpiration() );
		setTitle( histroy_data.getTitle() );
		setLastVisited( histroy_data.getLastVisited() );
		setVisitCount( visitCount+1 );
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		propChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propChangeSupport.removePropertyChangeListener(listener);
	}

	public String toString()
	{
		return ("Title=" + title + " Location=" + location + " firstVisited=" + firstVisited + " lastVisited=" + lastVisited + " expiration=" + expiration + " visitCount=" + visitCount);
	}

	public boolean equals(Object obj)
	{
		if( !(obj instanceof XHistoryData) )
			return false;

		return( ((XHistoryData)obj).getLocation().equals(location) );
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String new_title)
	{
	String old_title = title;

		title = new_title;
		propChangeSupport.firePropertyChange("Title", old_title, new_title);
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String new_loc)
	{
	String old_loc = location;

		location = new_loc;
		propChangeSupport.firePropertyChange("Location", old_loc, new_loc);
	}

	public Date getFirstVisited()
	{
		return firstVisited;
	}

	public void setFirstVisited(Date first_visited)
	{
	Date old_first_visited = firstVisited;

		firstVisited = first_visited;
		propChangeSupport.firePropertyChange("FirstVisited", old_first_visited, firstVisited);
	}

	public Date getLastVisited()
	{
		return lastVisited;
	}

	public void setLastVisited(Date last_visited)
	{
	Date old_last_visited = lastVisited;

		lastVisited = last_visited;
		propChangeSupport.firePropertyChange("LastVisited", old_last_visited, lastVisited);
	}

	public Date getExpiration()
	{
		return expiration;
	}

	public void setExpiration(Date new_exp)
	{
	Date old_expiration = expiration;

		expiration = new_exp;
		propChangeSupport.firePropertyChange("Expiration", old_expiration, expiration);
	}

	public int getVisitCount()
	{
		return visitCount;
	}

	public void setVisitCount(int visit_count)
	{
	int old_visit_count = visitCount;

		visitCount = visit_count;
		propChangeSupport.firePropertyChange("VisitCount", old_visit_count, visitCount);
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
	StringSelection str_sel = new StringSelection(location);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str_sel,str_sel);
	}

// Attributes:
	private static final long MILLISECONDS_PER_DAY = 86400000;

	private String					title = "";
	private String					location = "";
	private Date					firstVisited = null;
	private Date					lastVisited = null;
	private Date					expiration = null;
	private int						visitCount = 0;
	private PropertyChangeSupport   propChangeSupport = null;
}
