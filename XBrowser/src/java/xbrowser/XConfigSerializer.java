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
import java.util.*;
import javax.swing.plaf.*;
import org.w3c.dom.*;

import xbrowser.util.*;

// the following line is for IBM parser
//import com.ibm.xml.parser.Parser;

// the following 4 lines are for Sun parser
import com.sun.xml.parser.Resolver;
import com.sun.xml.tree.*;
import org.xml.sax.*;

public final class XConfigSerializer
{
	public void loadConfiguration(String file_name,XProjectConfig config) throws Exception
	{
	Node node = XMLManager.findNode(XMLManager.readDocument(file_name),"xbrowserconfig").getNextSibling().getFirstChild();

		config.setStartsWithHomePage(false);

		config.setNavigationToolBar(false);
		config.setPersonalToolBar(false);
		config.setLocationToolBar(false);
		config.setStatusBar(false);

		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("homeurl") )
					config.setHomeURL( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("contentstyle") )
					loadContentStyle(node, config);
				else if( node.getNodeName().equals("contenttab") )
					loadContentTab(node, config);
				else if( node.getNodeName().equals("documentselector") )
					config.setDocumentSelector( getViewTool(node) );
				else if( node.getNodeName().equals("bookmarkpane") )
					config.setBookmarkPane( getViewTool(node) );
				else if( node.getNodeName().equals("historypane") )
					config.setHistoryPane( getViewTool(node) );
				else if( node.getNodeName().equals("startswithhomepage") )
					config.setStartsWithHomePage(true);
				else if( node.getNodeName().equals("navigationtoolbar") )
					config.setNavigationToolBar(true);
				else if( node.getNodeName().equals("personaltoolbar") )
					config.setPersonalToolBar(true);
				else if( node.getNodeName().equals("locationtoolbar") )
					config.setLocationToolBar(true);
				else if( node.getNodeName().equals("statusbar") )
					config.setStatusBar(true);
				else if( node.getNodeName().equals("toolbarstyle") )
				{
				String toolbar_tyle = XMLManager.getNodeAttribute(node, "style");

					if( toolbar_tyle.equals("icononly") )
						config.setToolBarStyle(XProjectConstants.TOOLBAR_STYLE_ICON_ONLY);
					else if( toolbar_tyle.equals("textonly") )
						config.setToolBarStyle(XProjectConstants.TOOLBAR_STYLE_TEXT_ONLY);
					else if( toolbar_tyle.equals("texticon") )
						config.setToolBarStyle(XProjectConstants.TOOLBAR_STYLE_TEXT_ICON);
				}
				else if( node.getNodeName().equals("historyexpirationdays") )
				{
				int expire = 0;

					try
					{
						expire = (new Integer(XMLManager.getNodeValue(node))).intValue();
					}
					catch( Exception e )
					{
					}

					config.setHistoryExpirationDays(expire);
				}
				else if( node.getNodeName().equals("proxyservers") )
					loadProxyServers(node, config);
				else if( node.getNodeName().equals("proxyexceptions") )
					config.setProxyExceptions( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("authentication") )
					loadAuthenticationInfo(node, config);
				else if( node.getNodeName().equals("lookandfeels") )
					loadLookAndFeels(node, config);
				else if( node.getNodeName().equals("domaincompletions") )
					loadDomainCompletions(node, config);
				else if( node.getNodeName().equals("renderers") )
					loadRenderers(node, config);
			}

			node = node.getNextSibling();
		}
	}

	private void loadContentTab(Node node,XProjectConfig config) throws DOMException
	{
	String tab_place = XMLManager.getNodeAttribute(node,"place");

		if( tab_place.equals("bottom") )
			config.setContentTabPlacement( XProjectConstants.BOTTOM );
		else if( tab_place.equals("top") )
			config.setContentTabPlacement( XProjectConstants.TOP );
		else if( tab_place.equals("right") )
			config.setContentTabPlacement( XProjectConstants.RIGHT );
		else if( tab_place.equals("left") )
			config.setContentTabPlacement( XProjectConstants.LEFT );
	}

	private void loadContentStyle(Node node,XProjectConfig config) throws DOMException
	{
	String tab_place = XMLManager.getNodeAttribute(node,"name");

		if( tab_place.equals("desktoppane") )
			config.setContentStyle( XProjectConstants.DESKTOP_PANE_CONTENT );
		else if( tab_place.equals("tabbedpane") )
			config.setContentStyle( XProjectConstants.TABBED_PANE_CONTENT );
		else if( tab_place.equals("workbook") )
			config.setContentStyle( XProjectConstants.WORKBOOK_CONTENT );
	}

	private void loadProxyServers(Node node,XProjectConfig config) throws DOMException
	{
		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("httpproxy") )
					config.setHttpProxy( getProxy(node) );
				else if( node.getNodeName().equals("ftpproxy") )
					config.setFtpProxy( getProxy(node) );
				else if( node.getNodeName().equals("gopherproxy") )
					config.setGopherProxy( getProxy(node) );
				else if( node.getNodeName().equals("socksproxy") )
					config.setSocksProxy( getProxy(node) );
			}

			node = node.getNextSibling();
		}
	}

	private void loadAuthenticationInfo(Node node,XProjectConfig config) throws DOMException
	{
		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("username") )
					config.setUserName( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("password") )
					config.setPassword( decode(XMLManager.getNodeValue(node)) );
			}

			node = node.getNextSibling();
		}
	}

	// Just a simple decryption!! for not revealing the password in the configuration file
	private String decode(String str)
	{
	StringBuffer buf = new StringBuffer();
	int ch;

		for( int i=0; i<str.length(); i++ )
		{
			ch = str.charAt(i);
			ch -= 33;
			buf.append((char)ch);
		}

		return buf.toString();
	}

	private XViewTool getViewTool(Node node) throws DOMException
	{
	XViewTool tool = new XViewTool();

		tool.setVisible( XMLManager.getNodeAttribute(node,"visible").equals("true") );

	String placement = XMLManager.getNodeAttribute(node,"placement");

		if( placement.equals("page1") )
			tool.setPlacement( XProjectConstants.TOOLS_PAGE1 );
		else if( placement.equals("page2") )
			tool.setPlacement( XProjectConstants.TOOLS_PAGE2 );
		else if( placement.equals("page3") )
			tool.setPlacement( XProjectConstants.TOOLS_PAGE3 );

		return tool;
	}

	private XProxyServer getProxy(Node node) throws DOMException
	{
	XProxyServer proxy = new XProxyServer();

		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("useproxy") )
					proxy.setUseProxy(true);
				else if( node.getNodeName().equals("proxyaddress") )
					proxy.setProxyAddress( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("proxyport") )
					proxy.setProxyPort( XMLManager.getNodeValue(node) );
			}

			node = node.getNextSibling();
		}

		return proxy;
	}

	private void loadRenderers(Node node, XProjectConfig config) throws DOMException
	{
		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("renderer") )
				{
				XRendererObject renderer = new XRendererObject(XMLManager.getNodeAttribute(node,"name"), XMLManager.getNodeAttribute(node,"package"), XMLManager.getNodeAttribute(node,"resource"));

					config.addRenderer(renderer);
				}
				else if( node.getNodeName().equals("activerenderer") )
				{
				String active_renderer = XMLManager.getNodeAttribute(node,"name");
				Iterator renderers = config.getRenderers();
				XRendererObject renderer;

					while( renderers.hasNext() )
					{
						renderer = (XRendererObject)renderers.next();
						if( renderer.getName().equals(active_renderer) )
						{
							config.setActiveRenderer(renderer);
							break;
						}
					}
				}
			}

			node = node.getNextSibling();
		}
	}

	private void loadDomainCompletions(Node node, XProjectConfig config) throws DOMException
	{
		node = node.getFirstChild();
		while( node!=null )
		{
			if( (node instanceof Element) && node.getNodeName().equals("domaincompletion") )
				loadDomainCompletion(node, config);

			node = node.getNextSibling();
		}
	}

	private void loadDomainCompletion(Node node, XProjectConfig config) throws DOMException
	{
	boolean alt = XMLManager.getNodeAttribute(node,"alt").equals("true");
	boolean ctrl = XMLManager.getNodeAttribute(node,"ctrl").equals("true");
	boolean shft = XMLManager.getNodeAttribute(node,"shft").equals("true");
	XDomainCompletion domain_completion = new XDomainCompletion(alt, ctrl, shft);

		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("defaultprefix") )
					domain_completion.setDefaultPrefix( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("defaultpostfix") )
					domain_completion.setDefaultPostfix( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("prefix") )
					domain_completion.setPrefix( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("postfix") )
					domain_completion.setPostfix( XMLManager.getNodeValue(node) );
			}

			node = node.getNextSibling();
		}

    	config.addDomainCompletion(domain_completion);
	}

	private void loadLookAndFeels(Node node, XProjectConfig config) throws DOMException
	{
		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("lookandfeel") )
					loadLookAndFeel(node,config);
				else if( node.getNodeName().equals("activelnf") )
				{
				String active_lnf = XMLManager.getNodeAttribute(node,"name");
				Iterator lnfs = config.getLookAndFeels();
				XLookAndFeel lnf;

					while( lnfs.hasNext() )
					{
						lnf = (XLookAndFeel)lnfs.next();
						if( lnf.getName().equals(active_lnf) )
						{
							config.setActiveLNF(lnf);
							break;
						}
					}
				}
			}

			node = node.getNextSibling();
		}
	}

	private void loadLookAndFeel(Node node,XProjectConfig config) throws DOMException
	{
	XLookAndFeel lnf = new XLookAndFeel(XMLManager.getNodeAttribute(node,"name"), XMLManager.getNodeAttribute(node,"package"), XMLManager.getNodeAttribute(node,"resource"));

    	config.addLookAndFeel(lnf);

		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("theme") )
					loadTheme(node,lnf);
				else if( node.getNodeName().equals("activetheme") )
				{
				String active_theme = XMLManager.getNodeAttribute(node,"name");
				Iterator themes = lnf.getThemes();
				XTheme theme;

					while( themes.hasNext() )
					{
						theme = (XTheme)themes.next();

						if( theme.getName().equals(active_theme) )
						{
							lnf.setActiveTheme(theme);
							break;
						}
					}
				}
			}

			node = node.getNextSibling();
		}
	}

	private void loadTheme(Node node,XLookAndFeel lnf) throws DOMException
	{
	XTheme theme = new XTheme(XMLManager.getNodeAttribute(node,"name"));
	Node color;

    	lnf.addTheme(theme);

		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("primary1") )
					theme.setPrimary1( buildColor(XMLManager.findNode(node,"color")) );
				else if( node.getNodeName().equals("primary2") )
					theme.setPrimary2( buildColor(XMLManager.findNode(node,"color")) );
				else if( node.getNodeName().equals("primary3") )
					theme.setPrimary3( buildColor(XMLManager.findNode(node,"color")) );
				else if( node.getNodeName().equals("secondary1") )
					theme.setSecondary1( buildColor(XMLManager.findNode(node,"color")) );
				else if( node.getNodeName().equals("secondary2") )
					theme.setSecondary2( buildColor(XMLManager.findNode(node,"color")) );
				else if( node.getNodeName().equals("secondary3") )
					theme.setSecondary3( buildColor(XMLManager.findNode(node,"color")) );
				else if( node.getNodeName().equals("black") )
					theme.setBlack( buildColor(XMLManager.findNode(node,"color")) );
				else if( node.getNodeName().equals("white") )
					theme.setWhite( buildColor(XMLManager.findNode(node,"color")) );
			}

			node = node.getNextSibling();
		}
	}

	private ColorUIResource buildColor(Node node) throws DOMException
	{
	int red = 0;
	int green = 0;
	int blue = 0;

        try
        {
	        red = new Integer(XMLManager.getNodeAttribute(node,"red")).intValue();
		}
		catch( Exception e )
		{
		}

        try
        {
	        green = new Integer(XMLManager.getNodeAttribute(node,"green")).intValue();
		}
		catch( Exception e )
		{
		}

        try
        {
	        blue = new Integer(XMLManager.getNodeAttribute(node,"blue")).intValue();
		}
		catch( Exception e )
		{
		}

		return( new ColorUIResource(red,green,blue) );
	}

	public void saveConfiguration(String file_name, XProjectConfig config) throws Exception
	{
	XmlDocument xml_doc = new XmlDocument();
	File dtd_file = new File( (new File(file_name)).getParentFile()+System.getProperty("file.separator")+XProjectConstants.PRODUCT_NAME+".dtd" );
	OutputStream out = new FileOutputStream(file_name);
	ElementNode root = (ElementNode)xml_doc.createElement("xbrowserconfig");
	ElementNode node, node2;

		if( !dtd_file.exists() )
		{
		FileWriter writer = new FileWriter(dtd_file);

			writer.write(dtdContent);
			writer.close();
		}

		xml_doc.appendChild(root);
		xml_doc.setDoctype(null, XProjectConstants.PRODUCT_NAME+".dtd", null);

		//// Saving HomeURL
		XMLManager.addDataNodeTo(xml_doc, root, "homeurl", config.getHomeURL());
		//// HomeURL Saved

		//// Saving Content Properties
		node = (ElementNode)xml_doc.createElement("contentstyle");
		root.appendChild(node);

		switch( config.getContentStyle() )
		{
			case XProjectConstants.DESKTOP_PANE_CONTENT:
				node.setAttribute("name","desktoppane");
				break;

			case XProjectConstants.TABBED_PANE_CONTENT:
				node.setAttribute("name","tabbedpane");
				break;

			case XProjectConstants.WORKBOOK_CONTENT:
				node.setAttribute("name","workbook");
				break;
		}

		node = (ElementNode)xml_doc.createElement("contenttab");
		root.appendChild(node);

		switch( config.getContentTabPlacement() )
		{
			case XProjectConstants.BOTTOM:
				node.setAttribute("place","bottom");
				break;

			case XProjectConstants.TOP:
				node.setAttribute("place","top");
				break;

			case XProjectConstants.RIGHT:
				node.setAttribute("place","right");
				break;

			case XProjectConstants.LEFT:
				node.setAttribute("place","left");
				break;
		}
		//// Content Properties Saved

		saveViewTool(config.getDocumentSelector(),"documentselector",xml_doc,root);
		saveViewTool(config.getBookmarkPane(),"bookmarkpane",xml_doc,root);
		saveViewTool(config.getHistoryPane(),"historypane",xml_doc,root);

		if( config.getStartsWithHomePage() )
			root.appendChild( xml_doc.createElement("startswithhomepage") );

		if( config.hasNavigationToolBar() )
			root.appendChild( xml_doc.createElement("navigationtoolbar") );

		if( config.hasPersonalToolBar() )
			root.appendChild( xml_doc.createElement("personaltoolbar") );

		if( config.hasLocationToolBar() )
			root.appendChild( xml_doc.createElement("locationtoolbar") );

		if( config.hasStatusBar() )
			root.appendChild( xml_doc.createElement("statusbar") );

		node = (ElementNode)xml_doc.createElement("toolbarstyle");
		root.appendChild(node);

		if( config.getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_ICON_ONLY )
			node.setAttribute("style","icononly");
		else if( config.getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_TEXT_ONLY )
			node.setAttribute("style","textonly");
		else if( config.getToolBarStyle()==XProjectConstants.TOOLBAR_STYLE_TEXT_ICON )
			node.setAttribute("style","texticon");

		//// Saving HistoryExpirationDays
		XMLManager.addDataNodeTo(xml_doc, root, "historyexpirationdays", ""+config.getHistoryExpirationDays());
		//// HistoryExpirationDays Saved

		//// Saving Proxy Properties
		node = (ElementNode)xml_doc.createElement("proxyservers");
		root.appendChild(node);

		saveProxy("httpproxy", config.getHttpProxy(),xml_doc,node);
		saveProxy("ftpproxy", config.getFtpProxy(),xml_doc,node);
		saveProxy("gopherproxy", config.getGopherProxy(),xml_doc,node);
		saveProxy("socksproxy", config.getSocksProxy(),xml_doc,node);
		//// Proxy Properties Saved

		//// Saving Proxy Exceptions
		if( !config.getProxyExceptions().equals("") )
			XMLManager.addDataNodeTo(xml_doc, root, "proxyexceptions", config.getProxyExceptions());
		//// Proxy Exceptions Saved

		//// Saving Authentication Info.
		if( !config.getUserName().equals("") || !config.getPassword().equals("") )
		{
			node = (ElementNode)xml_doc.createElement("authentication");
			root.appendChild(node);

			XMLManager.addDataNodeTo(xml_doc, node, "username", config.getUserName());
			XMLManager.addDataNodeTo(xml_doc, node, "password", config.getPassword());
		}
		//// Authentication Info. Saved

		//// Saving Domain Completions
	Iterator domain_completions = config.getDomainCompletions();

		node = (ElementNode)xml_doc.createElement("domaincompletions");
		root.appendChild(node);

		while( domain_completions.hasNext() )
			saveDomainCompletion( (XDomainCompletion)domain_completions.next(), xml_doc, node);
		//// Domain Completions Saved

		//// Saving Renderers
	Iterator renderers = config.getRenderers();

		node = (ElementNode)xml_doc.createElement("renderers");
		root.appendChild(node);

		while( renderers.hasNext() )
			saveRenderer( (XRendererObject)renderers.next(), xml_doc, node);

		node2 = (ElementNode)xml_doc.createElement("activerenderer");
		node2.setAttribute("name", config.getActiveRenderer().getName());
		node.appendChild(node2);
		//// Renderers Saved

		//// Saving Look&Feel Properties
	Iterator lnfs = config.getLookAndFeels();

		node = (ElementNode)xml_doc.createElement("lookandfeels");
		root.appendChild(node);

		while( lnfs.hasNext() )
			saveLookAndFeel( (XLookAndFeel)lnfs.next(), xml_doc, node);

		node2 = (ElementNode)xml_doc.createElement("activelnf");
		node2.setAttribute("name",config.getActiveLNF().getName());
		node.appendChild(node2);
		//// Look&Feel Properties Saved

		root.normalize();
		xml_doc.write(out);
		out.flush();
		out.close();
	}

	// Just a simple encryption!! for not revealing the password in the configuration file
	private String encode(String str)
	{
	StringBuffer buf = new StringBuffer();
	int ch;

		for( int i=0; i<str.length(); i++ )
		{
			ch = str.charAt(i);
			ch += 33;
			buf.append((char)ch);
		}

		return buf.toString();
	}

	private void saveViewTool(XViewTool tool, String name, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node;

		node = (ElementNode)xml_doc.createElement(name);
		node.setAttribute("visible",""+tool.isVisible());

		if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE1 )
			node.setAttribute("placement","page1");
		else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE2 )
			node.setAttribute("placement","page2");
		else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE3 )
			node.setAttribute("placement","page3");

		parent_node.appendChild(node);
	}

	private void saveProxy(String new_node_name, XProxyServer proxy, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement(new_node_name);

		parent_node.appendChild(node);

		if( proxy.getUseProxy() )
			node.appendChild( xml_doc.createElement("useproxy") );

		XMLManager.addDataNodeTo(xml_doc, node, "proxyaddress", proxy.getProxyAddress());
		XMLManager.addDataNodeTo(xml_doc, node, "proxyport", proxy.getProxyPort());
	}

	private void saveDomainCompletion(XDomainCompletion domain_completion, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement("domaincompletion");

		node.setAttribute("alt", ""+domain_completion.getAltStatus());
		node.setAttribute("ctrl", ""+domain_completion.getCtrlStatus());
		node.setAttribute("shft", ""+domain_completion.getShiftStatus());
		parent_node.appendChild(node);

		XMLManager.addDataNodeTo(xml_doc, node, "defaultprefix", ""+domain_completion.getDefaultPrefix());
		XMLManager.addDataNodeTo(xml_doc, node, "defaultpostfix", ""+domain_completion.getDefaultPostfix());
		XMLManager.addDataNodeTo(xml_doc, node, "prefix", ""+domain_completion.getPrefix());
		XMLManager.addDataNodeTo(xml_doc, node, "postfix", ""+domain_completion.getPostfix());
	}

	private void saveRenderer(XRendererObject renderer, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement("renderer");

		node.setAttribute("name", renderer.getName());
		node.setAttribute("package", renderer.getPackageName());
		if( renderer.getResource()!=null && !renderer.getResource().trim().equals("") )
			node.setAttribute("resource", renderer.getResource());

		parent_node.appendChild(node);
	}

	private void saveLookAndFeel(XLookAndFeel lnf, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement("lookandfeel");

		node.setAttribute("name", lnf.getName());
		node.setAttribute("package", lnf.getPackageName());
		if( lnf.getResource()!=null && !lnf.getResource().trim().equals("") )
			node.setAttribute("resource", lnf.getResource());

		parent_node.appendChild(node);

		//// Saving Theme Properties
		if( lnf.hasTheme() )
		{
		Iterator themes = lnf.getThemes();
		ElementNode node2;

			while( themes.hasNext() )
				saveTheme( (XTheme)themes.next(), xml_doc, node);

			node2 = (ElementNode)xml_doc.createElement("activetheme");
			node2.setAttribute("name",lnf.getActiveTheme().getName());
			node.appendChild(node2);
		}
		//// Theme Properties Saved
	}

	private void saveTheme(XTheme theme, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement("theme");

		node.setAttribute("name",theme.getName());
		parent_node.appendChild(node);

		if( theme.shouldSavePrimary1() )
			saveColor("primary1", theme.getPrimary1(), xml_doc, node);

		if( theme.shouldSavePrimary2() )
			saveColor("primary2", theme.getPrimary2(), xml_doc, node);

		if( theme.shouldSavePrimary3() )
			saveColor("primary3", theme.getPrimary3(), xml_doc, node);

		if( theme.shouldSaveSecondary1() )
			saveColor("secondary1", theme.getSecondary1(), xml_doc, node);

		if( theme.shouldSaveSecondary2() )
			saveColor("secondary2", theme.getSecondary2(), xml_doc, node);

		if( theme.shouldSaveSecondary3() )
			saveColor("secondary3", theme.getSecondary3(), xml_doc, node);

		if( theme.shouldSaveBlack() )
			saveColor("black", theme.getBlack(), xml_doc, node);

		if( theme.shouldSaveWhite() )
			saveColor("white", theme.getWhite(), xml_doc, node);
	}

	private void saveColor(String color_name, ColorUIResource color, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement(color_name);
	ElementNode node2 = (ElementNode)xml_doc.createElement("color");

		parent_node.appendChild(node);

		node2.setAttribute("red",""+color.getRed());
		node2.setAttribute("green",""+color.getGreen());
		node2.setAttribute("blue",""+color.getBlue());
		node.appendChild(node2);
	}

// Attributes:
	private String dtdContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+"\n"+
								"\n"+
								"<!ELEMENT xbrowserconfig (homeurl?, contentstyle?, contenttab, documentselector, bookmarkpane, historypane, startswithhomepage?, navigationtoolbar?, personaltoolbar?, locationtoolbar?, statusbar?, toolbarstyle, historyexpirationdays, proxyservers, proxyexceptions?, authentication?, domaincompletions, renderers, lookandfeels)>"+"\n"+
								"\n"+
								"<!ELEMENT homeurl (#PCDATA)>"+"\n"+
								"\n"+
								"<!ELEMENT contentstyle EMPTY>"+"\n"+
								"<!ELEMENT contenttab EMPTY>"+"\n"+
								"\n"+
								"<!ELEMENT documentselector EMPTY>"+"\n"+
								"<!ELEMENT bookmarkpane EMPTY>"+"\n"+
								"<!ELEMENT historypane EMPTY>"+"\n"+
								"\n"+
								"<!ELEMENT navigationtoolbar EMPTY>"+"\n"+
								"<!ELEMENT personaltoolbar EMPTY>"+"\n"+
								"<!ELEMENT locationtoolbar EMPTY>"+"\n"+
								"<!ELEMENT statusbar EMPTY>"+"\n"+
								"<!ELEMENT toolbarstyle EMPTY>"+"\n"+
								"\n"+
								"<!ELEMENT startswithhomepage EMPTY>"+"\n"+
								"\n"+
								"<!ELEMENT historyexpirationdays (#PCDATA)>"+"\n"+
								"\n"+
								"<!ELEMENT proxyservers (httpproxy,ftpproxy,gopherproxy,socksproxy)>"+"\n"+
								"<!ELEMENT httpproxy (useproxy?,proxyaddress,proxyport)>"+"\n"+
								"<!ELEMENT ftpproxy (useproxy?,proxyaddress,proxyport)>"+"\n"+
								"<!ELEMENT gopherproxy (useproxy?,proxyaddress,proxyport)>"+"\n"+
								"<!ELEMENT socksproxy (useproxy?,proxyaddress,proxyport)>"+"\n"+
								"<!ELEMENT useproxy EMPTY>"+"\n"+
								"<!ELEMENT proxyaddress (#PCDATA)>"+"\n"+
								"<!ELEMENT proxyport (#PCDATA)>"+"\n"+
								"\n"+
								"<!ELEMENT proxyexceptions (#PCDATA)>"+"\n"+
								"\n"+
								"<!ELEMENT authentication (username,password)>"+"\n"+
								"<!ELEMENT username (#PCDATA)>"+"\n"+
								"<!ELEMENT password (#PCDATA)>"+"\n"+
								"\n"+
								"<!ELEMENT domaincompletions (domaincompletion*)>"+"\n"+
								"<!ELEMENT domaincompletion (defaultprefix, defaultpostfix, prefix, postfix)>"+"\n"+
								"<!ELEMENT defaultprefix (#PCDATA)>"+"\n"+
								"<!ELEMENT defaultpostfix (#PCDATA)>"+"\n"+
								"<!ELEMENT prefix (#PCDATA)>"+"\n"+
								"<!ELEMENT postfix (#PCDATA)>"+"\n"+
								"\n"+
								"<!ELEMENT renderers (renderer+,activerenderer)>"+"\n"+
								"<!ELEMENT renderer EMPTY>"+"\n"+
								"<!ELEMENT activerenderer EMPTY>"+"\n"+
								"\n"+
								"<!ELEMENT lookandfeels (lookandfeel+,activelnf)>"+"\n"+
								"<!ELEMENT activelnf EMPTY>"+"\n"+
								"<!ELEMENT lookandfeel (theme*,activetheme?)>"+"\n"+
								"<!ELEMENT activetheme EMPTY>"+"\n"+
								"<!ELEMENT theme (primary1?,primary2?,primary3?,secondary1?,secondary2?,secondary3?,black?,white?)>"+"\n"+
								"<!ELEMENT primary1 (color)>"+"\n"+
								"<!ELEMENT primary2 (color)>"+"\n"+
								"<!ELEMENT primary3 (color)>"+"\n"+
								"<!ELEMENT secondary1 (color)>"+"\n"+
								"<!ELEMENT secondary2 (color)>"+"\n"+
								"<!ELEMENT secondary3 (color)>"+"\n"+
								"<!ELEMENT black (color)>"+"\n"+
								"<!ELEMENT white (color)>"+"\n"+
								"<!ELEMENT color EMPTY>"+"\n"+
								"\n"+
								"<!ATTLIST contentstyle name (desktoppane|tabbedpane|workbook) #REQUIRED>"+"\n"+
								"<!ATTLIST contenttab place (top|bottom|left|right) #REQUIRED>"+"\n"+
								"\n"+
								"<!ATTLIST documentselector placement (page1|page2|page3) #REQUIRED>"+"\n"+
								"<!ATTLIST documentselector visible (true|false) #REQUIRED>"+"\n"+
								"\n"+
								"<!ATTLIST bookmarkpane placement (page1|page2|page3) #REQUIRED>"+"\n"+
								"<!ATTLIST bookmarkpane visible (true|false) #REQUIRED>"+"\n"+
								"\n"+
								"<!ATTLIST historypane placement (page1|page2|page3) #REQUIRED>"+"\n"+
								"<!ATTLIST historypane visible (true|false) #REQUIRED>"+"\n"+
								"\n"+
								"<!ATTLIST toolbarstyle style (icononly|textonly|texticon) #REQUIRED>"+"\n"+
								"\n"+
								"<!ATTLIST domaincompletion alt (true|false) #REQUIRED>"+"\n"+
								"<!ATTLIST domaincompletion ctrl (true|false) #REQUIRED>"+"\n"+
								"<!ATTLIST domaincompletion shft (true|false) #REQUIRED>"+"\n"+
								"\n"+
								"<!ATTLIST renderer name CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST renderer package CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST renderer resource CDATA #IMPLIED>"+"\n"+
								"<!ATTLIST activerenderer name CDATA #REQUIRED>"+"\n"+
								"\n"+
								"<!ATTLIST lookandfeel name CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST lookandfeel package CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST lookandfeel resource CDATA #IMPLIED>"+"\n"+
								"<!ATTLIST activelnf name CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST theme name CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST activetheme name CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST color red CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST color green CDATA #REQUIRED>"+"\n"+
								"<!ATTLIST color blue CDATA #REQUIRED>";
}
