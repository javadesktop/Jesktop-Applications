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
package xbrowser.util;

import xbrowser.*;

import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.*;

public class XLookAndFeel implements Comparable
{
	public XLookAndFeel(String name, String package_name)
	{
		this(name, package_name, null);
	}

	public XLookAndFeel(String name, String package_name, String resource)
	{
		this.name = name;
		this.packageName = package_name;
		this.resource = resource;

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

	public String getName()
	{
		return name;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public String getResource()
	{
		return resource;
	}

    public String toString()
	{
		return name;
	}

	public int compareTo(Object obj)
	{
		if( !(obj instanceof XLookAndFeel) )
			return -1;

	XLookAndFeel lnf = (XLookAndFeel)obj;

		return( name.compareTo(lnf.getName()) );
	}

	// I know I should compare all themes too in order to have a complete equals() method! But there is no need in this stage!!
	public boolean equals(Object obj)
	{
		if( !(obj instanceof XLookAndFeel) )
			return false;

	XLookAndFeel lnf = (XLookAndFeel)obj;

		if( !name.equals(lnf.getName()) || !packageName.equals(lnf.getPackageName()) || !resource.equals(lnf.getResource()) )
			return false;

		if( (activeTheme==null && lnf.getActiveTheme()!=null) || (activeTheme!=null && lnf.getActiveTheme()==null) )
			return false;

		if( activeTheme!=null )
			return( activeTheme.equals(lnf.getActiveTheme()) );
		else
			return true;
	}

	public boolean hasTheme()
	{
		return( !themes.isEmpty() );
	}

	public Iterator getThemes()
	{
		return themes.iterator();
	}

	public void addTheme(XTheme theme)
	{
		themes.add(theme);
		Collections.sort(themes);

		if( themes.size()==1 )
			activeTheme = theme;
	}

	public XTheme getActiveTheme()
	{
		return activeTheme;
	}

	public void setActiveTheme(XTheme theme)
	{
		if( themes.contains(theme) )
		{
		XTheme old_theme = activeTheme;

			activeTheme = theme;
			propChangeSupport.firePropertyChange("ActiveTheme", old_theme, activeTheme);
		}
		else
			throw new IllegalArgumentException("setActiveTheme: Theme not found!");
	}

	public void activate() throws Exception
	{
		if( activeTheme!=null )
	        activeTheme.activate();

       	if( name.equalsIgnoreCase(XProjectConstants.NATIVE_LNF) && packageName.equals("*") )
       		activateImpl(UIManager.getSystemLookAndFeelClassName(), resource);
       	else if( name.equalsIgnoreCase(XProjectConstants.CROSSPLATFORM_LNF) && packageName.equals("*") )
       		activateImpl(UIManager.getCrossPlatformLookAndFeelClassName(), resource);
       	else
       		activateImpl(packageName, resource);
	}

	private void activateImpl(String lnf_class_name, String lnf_resource) throws Exception
	{
	LookAndFeel lnf = (LookAndFeel)XUtil.getInstance().loadObject(lnf_class_name, lnf_resource);

   		UIManager.setLookAndFeel(lnf);
	}

// Attributes:
	private String name;
	private String packageName;
	private String resource;
	private XTheme activeTheme;
	private LinkedList themes = new LinkedList();
	private PropertyChangeSupport propChangeSupport = null;
}
