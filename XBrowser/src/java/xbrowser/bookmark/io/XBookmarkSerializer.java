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
package xbrowser.bookmark.io;

import java.text.*;
import java.util.*;

import xbrowser.bookmark.*;

public abstract class XBookmarkSerializer
{
	public abstract void importFrom(String file_name, XBookmarkFolder root_folder) throws Exception;
	public abstract void exportTo(String file_name, XBookmarkFolder root_folder) throws Exception;
	public abstract String getName();
	public abstract String getFormat();
	public abstract boolean hasSingleFileStructure();

	public static XBookmarkSerializer getDefaultSerializer()
	{
		return defaultSerializer;
	}

	public static Iterator getAvailableSerializers()
	{
		return serilizers.iterator();
	}

// Attributes:
	protected DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

	private static XBookmarkSerializer defaultSerializer;
	private static List serilizers;

	static
	{
		serilizers = new LinkedList();

		defaultSerializer = new XBookmarkDefaultSerializer();
		serilizers.add(defaultSerializer);

		serilizers.add(new XBookmarkIESerializer());
		serilizers.add(new XBookmarkNetscapeSerializer());
		serilizers.add(new XBookmarkOperaSerializer());
	}
}
