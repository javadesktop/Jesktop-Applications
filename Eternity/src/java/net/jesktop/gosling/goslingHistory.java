/*
goslingHistory.java
Copyright (C) 2001 Dave Seaton
dave@goose24.org
http://www.goose24.org

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package net.jesktop.gosling;

import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Implements a history for the gosling help system.
 *
 * @author Dave Seaton
 * @version 1.0
 */
public class goslingHistory
{
	private Vector history;
	private int current;
	private boolean forw;
	private boolean back;

	private Vector listeners;

	/**
	 * Constructs a new gosling history and specifies the first <code>URL</code> in the history.
	 *
	 * @param start the first page in the history
	 */
	public goslingHistory( URL start )
	{
		history = new Vector();
		history.addElement( start );
		current = 0;
		forw = false;
		back = false;
	}

	/**
	 * Clears the history of all but the starting document.
	 */
	public void clear()
	{
		Object start = history.elementAt( 0 );

		history = new Vector();
		history.addElement( start );
		current = 0;
		forw = false;
		back = false;
		fireHistoryEvent();
	}

	/**
	 * Adds a new page to the history and fires a <code>HistoryEvent</code> if the
	 * history status has changed.
	 *
	 * @param newPage the new page to add to the history
	 */
	public void visit( URL newPage )
	{
		if( !forw )
		{
			history.addElement( newPage );
			++current;
		}
		else
		{
			for( int i = current + 1; i < history.size(); ++i )
				history.removeElementAt( i );

			history.addElement( newPage );
			++current;
		}

		if( !back || forw )
		{
			back = true;
			forw = false;
			fireHistoryEvent();
		}
	}

	/**
	 * Returns the previous <code>URL</code> in the history and fires a <code>HistoryEvent</code> if the
	 * history status has changed.  Returns <code>null</code> if there is no previous page in the history.
	 *
	 * @return the previous <code>URL</code> in the history
	 */
	public URL goBack()
	{
		if( !back )
			return null;

		URL page = (URL) history.elementAt( --current );

		if( current == 0 )
		{
			back = false;
			forw = true;
			fireHistoryEvent();
		}
		else if( !forw )
		{
			forw = true;
			fireHistoryEvent();
		}

		return page;
	}

	/**
	 * Returns the next <code>URL</code> in the history and fires a <code>HistoryEvent</code> if the
	 * history status has changed.  Returns <code>null</code> if there is no previous page in the history.
	 *
	 * @return the next <code>URL</code> in the history
	 */
	public URL goForward()
	{
		if( !forw )
			return null;

		URL page = (URL) history.elementAt( ++current );

		if( current == history.size() - 1 )
		{
			forw = false;
			back = true;
			fireHistoryEvent();
		}
		else if( !back )
		{
			back = true;
			fireHistoryEvent();
		}

		return page;
	}

	/**
	 * Adds a <code>HistoryEventListener</code> to the listener list.
	 *
	 * @param listener a <code>HistoryEventListener</code>
	 */
	public synchronized void addHistoryEventListener( HistoryEventListener listener )
	{
		if( listeners == null )
			listeners = new Vector();
		listeners.addElement( listener );
	}


	/**
	 * Removes a <code>HistoryEventListener</code> from the listener list.
	 *
	 * @param listener a <code>HistoryEventListener</code>
	 */
	public synchronized void removeHistoryEventListener( HistoryEventListener listener )
	{
		if( listeners == null )
			listeners = new Vector();
		listeners.removeElement( listener );
	}

	/**
	 * Fires a <code>HistoryEvent</code> to registered listeners notifying them of a change in the history
	 * status.
	 */
	protected void fireHistoryEvent()
	{
		if( listeners != null && ! listeners.isEmpty())
		{
			HistoryEvent event = new HistoryEvent( this, back, forw );
			Vector listenersCopy;

			synchronized( this )
			{
				listenersCopy = (Vector) listeners.clone();
			}

			Enumeration e = listenersCopy.elements();
			while( e.hasMoreElements())
				((HistoryEventListener) e.nextElement()).historyStatusChanged( event );
		}
	}
}