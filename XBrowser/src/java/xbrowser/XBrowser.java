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
package xbrowser;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;

import xbrowser.util.*;
import xbrowser.screen.*;
import xbrowser.widgets.*;

import org.jesktop.frimble.*;
import org.jesktop.appsupport.*;

public class XBrowser implements PropertyChangeListener, ContentViewer
{
	public XBrowser(XBrowserLayout browser_layout)
	{
	File config_dir = new File(XProjectConstants.CONFIG_DIR);

		if( !config_dir.exists() )
			config_dir.mkdirs();

		XProjectConfig.getInstance().addPropertyChangeListener(this);

  	XSplashScreen splash = new XSplashScreen( XComponentBuilder.getInstance().getProperty(this, "StartInit", XProjectConstants.PRODUCT_NAME) );

        splash.setVisible(true);

        browser = browser_layout;
        browser.init(splash);
		if( !XProjectConfig.getInstance().getHomeURL().equals("") && XProjectConfig.getInstance().getStartsWithHomePage() )
			browser.showInActiveDocument(XProjectConfig.getInstance().getHomeURL());

    	splash.setVisible(false);
	}

	public static XIBrowser getBrowser()
	{
		return browser;
	}

	public static JFrimble getMainFrame()
	{
		return browser;
	}

	public void viewContent(URL url, boolean thumbNail)
	{
		getBrowser().showInNewDocument(url.toString());
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
	String prop_name = evt.getPropertyName();
	Object new_value = evt.getNewValue();

		if( prop_name.equals("ActiveLNF") || prop_name.equals("ActiveTheme") )
		{/*
			try
			{
				XProjectConfig.getInstance().getActiveLNF().activate();
			}
			catch( Exception e )
			{
				e.printStackTrace();

			//String message = XComponentBuilder.getInstance().getProperty(this, "LNFError");
			//String title = XComponentBuilder.getInstance().getProperty(this, "Warning");

				JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
			}*/
		}
		else if( prop_name.equals("HttpProxy") )
		{
		XProxyServer proxy = (XProxyServer)new_value;

			if( proxy.getUseProxy() )
			{
				//System.getProperties().put("http.proxySet", "true");
				System.getProperties().put("http.proxyHost", proxy.getProxyAddress());
				System.getProperties().put("http.proxyPort", proxy.getProxyPort());
			}
			else
			{
				//System.getProperties().put("http.proxySet", "false");
				System.getProperties().remove("http.proxyHost");
				System.getProperties().remove("http.proxyPort");
			}
		}
		else if( prop_name.equals("FtpProxy") )
		{
		XProxyServer proxy = (XProxyServer)new_value;

			if( proxy.getUseProxy() )
			{
				//System.getProperties().put("ftpProxySet", "true");
				System.getProperties().put("ftp.proxyHost", proxy.getProxyAddress());
				System.getProperties().put("ftp.proxyPort", proxy.getProxyPort());
			}
			else
			{
				//System.getProperties().put("ftpProxySet", "false");
				System.getProperties().remove("ftp.proxyHost");
				System.getProperties().remove("ftp.proxyPort");
			}
		}
		else if( prop_name.equals("SocksProxy") )
		{
		XProxyServer proxy = (XProxyServer)new_value;

			if( proxy.getUseProxy() )
			{
				//System.getProperties().put("socksProxySet", "true");
				System.getProperties().put("socksProxyHost", proxy.getProxyAddress());
				System.getProperties().put("socksProxyPort", proxy.getProxyPort());
			}
			else
			{
				//System.getProperties().put("socksProxySet", "false");
				System.getProperties().remove("socksProxyHost");
				System.getProperties().remove("socksProxyPort");
			}
		}
		else if( prop_name.equals("GopherProxy") )
		{
		XProxyServer proxy = (XProxyServer)new_value;

			if( proxy.getUseProxy() )
			{
				System.getProperties().put("gopherProxySet", "true");
				System.getProperties().put("gopherProxyHost", proxy.getProxyAddress());
				System.getProperties().put("gopherProxyPort", proxy.getProxyPort());
			}
			else
			{
				System.getProperties().put("gopherProxySet", "false");
				System.getProperties().remove("gopherProxyHost");
				System.getProperties().remove("gopherProxyPort");
			}
		}
		else if( prop_name.equals("ProxyExceptions") )
		{
		String exceptions = new_value.toString().replace(';','|');

			if( !exceptions.equals("") )
				System.getProperties().put("http.nonProxyHosts", exceptions);
			else
				System.getProperties().remove("http.nonProxyHosts");
		}
		else if( prop_name.equals("UserName") || prop_name.equals("Password") )
			XAuthenticator.getInstance().setAuthenticationInfo(XProjectConfig.getInstance().getUserName(), XProjectConfig.getInstance().getPassword());
	}

// Attributes:
	private static XBrowserLayout browser = null;
}
