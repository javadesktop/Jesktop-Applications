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

import xbrowser.*;
import xbrowser.history.event.*;

public final class XHistoryManager
{
	public static XHistoryManager getInstance()
    {
		if( instance==null )
			instance = new XHistoryManager();

        return instance;
    }

	private XHistoryManager()
	{
    	serializer = new XHistorySerializer();
	}

	public void load()
	{
		try
		{
			serializer.loadHistory(XProjectConstants.CONFIG_DIR+historyFileName,this);
		}
		catch( Exception e )
		{
			System.out.println("An error occured on loading the history!");

			removeAllHistory();
			//save();
		}
	}

	public void save()
	{
		try
		{
			serializer.saveHistory(XProjectConstants.CONFIG_DIR+historyFileName,this);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void updateHistoryData(XHistoryData history_data)
	{
	int index = history.indexOf(history_data);

		if( index!=-1 )
			((XHistoryData)history.get(index)).setTitle(history_data.getTitle());
	}

	public void addHistoryData(XHistoryData history_data)
	{
	int index = history.indexOf(history_data);

		if( index!=-1 )
			((XHistoryData)history.get(index)).visit(history_data);
		else if( history_data.getExpiration().after(new Date()) )
		{
			history.add(history_data);
			for( int i=0; i<historyListeners.size(); i++ )
				((XHistoryListener)historyListeners.get(i)).historyAdded(history_data);
		}
	}

	public void removeHistoryData(XHistoryData history_data)
	{
		if( !history.contains(history_data) )
			return;

		history.remove(history_data);
		for( int i=0; i<historyListeners.size(); i++ )
			((XHistoryListener)historyListeners.get(i)).historyRemoved(history_data);
	}

	public void removeAllHistory()
	{
		history.clear();

		for( int i=0; i<historyListeners.size(); i++ )
			((XHistoryListener)historyListeners.get(i)).clearHistory();
	}

	public Iterator getHistory()
	{
		return history.iterator();
	}

	public int getHistorySize()
	{
		return history.size();
	}

	public XHistoryData getHistoryDataAt(int index)
	{
		return (XHistoryData)history.get(index);
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
    private final String historyFileName = "XHistory.xml";

    private static XHistoryManager instance = null;
    private XHistorySerializer serializer = null;

    private LinkedList historyListeners = new LinkedList();
    private LinkedList history = new LinkedList();
}
