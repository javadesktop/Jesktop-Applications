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
package xbrowser.renderer.custom;

import javax.swing.text.html.*;
import javax.swing.text.*;
import javax.swing.*;
import java.applet.*;
import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class XHTMLEditorKit extends HTMLEditorKit
{
	public Object clone()
	{
		return new XHTMLEditorKit();
	}

	public void install(JEditorPane c)
	{
		myEditor = c;
		super.install(c);
	}

	public void deinstall(JEditorPane c)
	{
		if( myEditor==c )
			myEditor = null;
		super.deinstall(c);
	}

	public ViewFactory getViewFactory()
	{
		return myViewFactory;
	}

	public void destroyAllApplets()
	{
	Enumeration enum = applets.elements();

		while( enum.hasMoreElements() )
			((XAppletView)enum.nextElement()).destroyApplet();

		applets.removeAllElements();
	}

	private class XHTMLFactory extends HTMLEditorKit.HTMLFactory
	{
		public View create(Element elem)
		{
		Object o = elem.getAttributes().getAttribute(StyleConstants.NameAttribute);

			if( o instanceof HTML.Tag )
			{
			HTML.Tag kind = (HTML.Tag) o;

				if( kind == HTML.Tag.APPLET )
				{
					if( insideAppletTag )
					{
						insideAppletTag = false;
						return new XHiddenView(elem);
					}
					else
					{
					XAppletView applet_view;

						insideAppletTag = true;
						applet_view = new XAppletView(elem, appletContext);
						applets.add(applet_view);
						return applet_view;
					}
				}
				else if( kind == HTML.Tag.CONTENT && insideAppletTag )
					return new XHiddenView(elem);
			}

			return super.create(elem);
		}

	// Attributes:
		private boolean insideAppletTag = false;
	}

	public class XAppletContext implements AppletContext
	{
		public Applet getApplet(String name)
		{
		Enumeration enum = applets.elements();
		Applet applet = null;

			while( enum.hasMoreElements() )
			{
				applet = ((XAppletView)enum.nextElement()).getApplet();
				if( applet.getName().equals(name) )
					return applet;
			}

			return null;
		}

		public Enumeration getApplets()
		{
			return applets.elements();
		}

		public Image getImage(URL url)
		{
			return Toolkit.getDefaultToolkit().createImage(url);
		}

		public AudioClip getAudioClip(URL url)
		{
			return Applet.newAudioClip(url);
		}

		public void showDocument(URL url)
		{
			showDocument(url, "_self");
		}

		public void showDocument(URL url, String target)
		{
			if ( myEditor instanceof XCustomRenderer )
				((XCustomRenderer)myEditor).showDocument(url, target);
		}

		public void showStatus(String status)
		{
			if( myEditor instanceof XCustomRenderer )
				((XCustomRenderer)myEditor).showAppletStatusInternal(status);
		}

		void showAppletLifeCycle(Applet applet, int status)
		{
			if( myEditor instanceof XCustomRenderer )
				((XCustomRenderer)myEditor).showAppletLifeCycle(applet, status);
		}
	}

// Attributes:
	private ViewFactory 	myViewFactory = new XHTMLFactory();
	private XAppletContext	appletContext = new XAppletContext();
	private Vector			applets = new Vector();

	private JEditorPane		myEditor = null;
}
