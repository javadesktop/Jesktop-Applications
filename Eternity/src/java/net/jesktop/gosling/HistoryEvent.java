/*
HistoryEvent.java
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

import java.util.EventObject;

/**
 * Defines an event for reporting status changes in a <code>goslingHistory</code>.
 *
 * @author Dave Seaton
 * @version 1.0
 */
public class HistoryEvent extends EventObject
{
	private boolean backAvailable;
	private boolean forwardAvailable;

	/**
	 * Constructs a new <code>HistoryEvent</code>.
	 *
	 * @param source the source of the event
	 * @param backAvailable whether there is a document in the history before the current document
	 * @param forwardAvailable whether there is a document in the history after the current document
	 */
	public HistoryEvent( Object source, boolean backAvailable, boolean forwardAvailable )
	{
		super( source );
		this.backAvailable = backAvailable;
		this.forwardAvailable = forwardAvailable;
	}

	/**
	 * Returns <code>true</code> if there is a document in the history before the current document
	 *
	 * @return <code>true</code> if there is a document in the history before the current document
	 */
	public boolean isBackAvailable()
	{
		return backAvailable;
	}

	/**
	 * Returns <code>true</code> if there is a document in the history after the current document
	 *
	 * @return <code>true</code> if there is a document in the history after the current document
	 */
	public boolean isForwardAvailable()
	{
		return forwardAvailable;
	}
}