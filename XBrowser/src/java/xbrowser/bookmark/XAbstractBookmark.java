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
import java.beans.*;

public abstract class XAbstractBookmark
{
	public XAbstractBookmark()
	{
		creationDate = new Date();
		modificationDate = new Date();

		propChangeSupport = new PropertyChangeSupport(this);
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
		return getTitle();
	}

	public void setTitle(String new_title)
	{
	String old_title = title;

		title = new_title;
		propChangeSupport.firePropertyChange("Title",old_title,new_title);
	}

	public String getTitle()
	{
		return title;
	}

	public void setDescription(String new_description)
	{
	String old_description = description;

		description = new_description;
		propChangeSupport.firePropertyChange("Description",old_description,new_description);
	}

	public String getDescription()
	{
		return description;
	}

	public void setParent(XBookmarkFolder new_parent)
	{
	XBookmarkFolder old_parent = parent;

		parent = new_parent;
		propChangeSupport.firePropertyChange("Parent",old_parent,new_parent);
	}

	public XBookmarkFolder getParent()
	{
		return parent;
	}

	public void setCreationDate(Date new_date)
	{
	Date old_date = creationDate;

		creationDate = new_date;
		propChangeSupport.firePropertyChange("CreationDate",old_date,new_date);
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setModificationDate(Date new_date)
	{
	Date old_date = modificationDate;

		modificationDate = new_date;
		propChangeSupport.firePropertyChange("ModificationDate",old_date,new_date);
	}

	public Date getModificationDate()
	{
		return modificationDate;
	}

	public boolean isUnderPersonalFolder()
	{
		if( parent!=null )
			return( parent.isPersonalFolder() ? true : parent.isUnderPersonalFolder() );
		else
			return false;
	}

	public abstract void open();
	public abstract void copy();

// Attributes:
	protected String title = "";
	protected String description = "";
	protected XBookmarkFolder parent = null;
	protected Date creationDate = null;
	protected Date modificationDate = null;

	protected PropertyChangeSupport propChangeSupport = null;
}
