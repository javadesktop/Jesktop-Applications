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

import java.io.*;
import java.net.*;
import java.util.*;

public final class XUtil
{
	public static XUtil getInstance()
    {
		if( instance==null )
			instance = new XUtil();

        return instance;
    }

	private XUtil()
	{
	}

	public Object loadObject(String class_name, String resource) throws Exception
	{
		if( resource==null || resource.trim().equals("") )
			return Class.forName(class_name).newInstance();
		else
		{
			try
			{
				return loadObjectFromResource(class_name, resource);
			}
			catch( Exception e )
			{
				return Class.forName(class_name).newInstance();
			}
		}
	}

	private Object loadObjectFromResource(String class_name, String resource) throws Exception
	{
	LinkedList url_list = new LinkedList();
	StringTokenizer tokenizer = new StringTokenizer(resource, ",;");

		while( tokenizer.hasMoreTokens() )
		{
			try
			{
				url_list.add( new File(tokenizer.nextToken()).toURL() );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}

	ClassLoader class_loader = new URLClassLoader( (URL[])url_list.toArray(new URL[url_list.size()]) );
	Class renderer_class = Class.forName(class_name, true, class_loader);

		return renderer_class.newInstance();
	}

	public void checkForResourceExistence(String filename)
	{
	File file = new File(filename);

		if( file.exists() )
			return;

	URL url = XUtil.class.getClassLoader().getResource(filename);

		if( url==null )
			return;

	InputStream in_stream = null;
	OutputStream out_stream = null;

		try
		{
		int b;

			in_stream = url.openStream();
			out_stream = new FileOutputStream(file);

			while( (b=in_stream.read())!=-1 )
				out_stream.write(b);

		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				in_stream.close();
				out_stream.close();
			}
			catch( Exception ex2 )
			{
			}
		}
	}

// Attributes:
	private static XUtil instance = null;
}
