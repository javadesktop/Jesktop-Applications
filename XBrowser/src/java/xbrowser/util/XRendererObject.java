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

import xbrowser.renderer.*;

public final class XRendererObject implements Comparable
{
	public XRendererObject(String name, String package_name)
	{
		this(name, package_name, null);
	}

	public XRendererObject(String name, String package_name, String resource)
	{
		this.name = name;
		this.packageName = package_name;
		this.resource = resource;
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
		if( !(obj instanceof XRendererObject) )
			return -1;

	XRendererObject renderer_obj = (XRendererObject)obj;

		return( name.compareTo(renderer_obj.getName()) );
	}

	public boolean equals(Object obj)
	{
		if( !(obj instanceof XRendererObject) )
			return false;

	XRendererObject renderer_obj = (XRendererObject)obj;

		if( !name.equals(renderer_obj.getName()) || !packageName.equals(renderer_obj.getPackageName()) || !resource.equals(renderer_obj.getResource()) )
			return false;

		return true;
	}

	public XRenderer buildRenderer() throws Exception
	{
		return( (XRenderer)XUtil.getInstance().loadObject(packageName, resource) );
	}

// Attributes:
	private String name;
	private String resource;
	private String packageName;
}
