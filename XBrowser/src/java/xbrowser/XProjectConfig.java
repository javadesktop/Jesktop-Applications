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

import java.util.*;
import java.beans.*;

import xbrowser.util.*;
import xbrowser.renderer.*;

public final class XProjectConfig implements PropertyChangeListener
{
	public static XProjectConfig getInstance()
    {
		if( instance==null )
			instance = new XProjectConfig();

        return instance;
    }

	private XProjectConfig()
	{
		propChangeSupport = new PropertyChangeSupport(this);
    	serializer = new XConfigSerializer();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		propChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String property_name, PropertyChangeListener listener)
	{
		propChangeSupport.addPropertyChangeListener(property_name, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propChangeSupport.removePropertyChangeListener(listener);
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
	String prop_name = evt.getPropertyName();

		if( prop_name.equals("ActiveTheme") )
			propChangeSupport.firePropertyChange("ActiveTheme",evt.getOldValue(),evt.getNewValue());
	}

	private void loadDefaults()
	{
		setHomeURL(XProjectConstants.DEFAULT_HOME_URL);
		setContentStyle(XProjectConstants.WORKBOOK_CONTENT);
		setDocumentSelector( new XViewTool() );
		setBookmarkPane( new XViewTool() );
		setHistoryPane( new XViewTool(XProjectConstants.TOOLS_PAGE2) );
		setNavigationToolBar(true);
		setPersonalToolBar(false);
		setLocationToolBar(true);
		setStatusBar(true);
		setStartsWithHomePage(true);
		setContentTabPlacement(XProjectConstants.BOTTOM);
		setHttpProxy( new XProxyServer() );
		setFtpProxy( new XProxyServer() );
		setSocksProxy( new XProxyServer() );
		setGopherProxy( new XProxyServer() );
		setProxyExceptions("");
		setHistoryExpirationDays(10);
		setUserName("");
		setPassword("");
		setToolBarStyle(XProjectConstants.TOOLBAR_STYLE_TEXT_ICON);
	}

	public void load()
	{
		loadDefaults();
		checkForNeccessaryResources();

		try
		{
			serializer.loadConfiguration(XProjectConstants.CONFIG_DIR+configFileName,this);
		}
		catch( Exception e )
		{
			System.out.println("An error occured on loading the configuration! Using default configuration...");
			//e.printStackTrace();

			lookAndFeels.clear();
			domainCompletions.clear();
			loadDefaults();

		XLookAndFeel lnf = new XLookAndFeel(XProjectConstants.CROSSPLATFORM_LNF,"*");

			lnf.addTheme( new XTheme(XProjectConstants.CROSSPLATFORM_LNF_DEFAULT_THEME) );
			addLookAndFeel(lnf);
			addLookAndFeel( new XLookAndFeel(XProjectConstants.NATIVE_LNF,"*") );

			setActiveLNF(lnf);

		XDomainCompletion domain_completion;

			domain_completion = new XDomainCompletion(true, false, false);
			domain_completion.setDefaultPrefix("www.");
			domain_completion.setDefaultPostfix(".net");
			domain_completion.setPrefix("www.");
			domain_completion.setPostfix(".net");
			domainCompletions.add(domain_completion);

			domain_completion = new XDomainCompletion(false, true, false);
			domain_completion.setDefaultPrefix("www.");
			domain_completion.setDefaultPostfix(".com");
			domain_completion.setPrefix("www.");
			domain_completion.setPostfix(".com");
			domainCompletions.add(domain_completion);

			domain_completion = new XDomainCompletion(false, false, true);
			domain_completion.setDefaultPrefix("www.");
			domain_completion.setDefaultPostfix(".edu");
			domain_completion.setPrefix("www.");
			domain_completion.setPostfix(".edu");
			domainCompletions.add(domain_completion);

			domain_completion = new XDomainCompletion(false, true, true);
			domain_completion.setDefaultPrefix("www.");
			domain_completion.setDefaultPostfix(".gov");
			domain_completion.setPrefix("www.");
			domain_completion.setPostfix(".gov");
			domainCompletions.add(domain_completion);

		XRendererObject renderer = new XRendererObject(XProjectConstants.DEFAULT_RENDERER_NAME, XProjectConstants.DEFAULT_RENDERER_PACKAGE);

			addRenderer(renderer);
			setActiveRenderer(renderer);

			//save();
		}
	}

	private void checkForNeccessaryResources()
	{
		XUtil.getInstance().checkForResourceExistence(XProjectConstants.CONFIG_DIR+XProjectConstants.PRODUCT_NAME+".xml");
		XUtil.getInstance().checkForResourceExistence(XProjectConstants.CONFIG_DIR+XProjectConstants.PRODUCT_NAME+".dtd");
	}

	public void save()
	{
		try
		{
			serializer.saveConfiguration(XProjectConstants.CONFIG_DIR+configFileName,this);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void addLookAndFeel(XLookAndFeel lnf)
	{
		lookAndFeels.add(lnf);
		Collections.sort(lookAndFeels);
		propChangeSupport.firePropertyChange("LookAndFeels", null, null);
	}

	public void removeLookAndFeel(XLookAndFeel lnf)
	{
		lookAndFeels.remove(lnf);
		propChangeSupport.firePropertyChange("LookAndFeels", null, null);
	}

	public Iterator getLookAndFeels()
	{
		return lookAndFeels.iterator();
	}

	public XLookAndFeel getActiveLNF()
    {
    	return activeLNF;
    }

	public void setActiveLNF(XLookAndFeel lnf)
    {
	XLookAndFeel old_lnf = activeLNF;

    	if( activeLNF!=null )
    		activeLNF.removePropertyChangeListener(this);

    	activeLNF = lnf;

    	if( activeLNF!=null )
	   		activeLNF.addPropertyChangeListener(this);

		propChangeSupport.firePropertyChange("ActiveLNF",old_lnf,activeLNF);
    }

	public void addRenderer(XRendererObject renderer)
	{
		renderers.add(renderer);
		Collections.sort(renderers);
		propChangeSupport.firePropertyChange("Renderers", null, null);
	}

	public void removeRenderer(XRendererObject renderer)
	{
		renderers.remove(renderer);
		propChangeSupport.firePropertyChange("Renderers", null, null);
	}

	public Iterator getRenderers()
	{
		return renderers.iterator();
	}

	public XRendererObject getActiveRenderer()
    {
    	return activeRenderer;
    }

	public void setActiveRenderer(XRendererObject renderer)
    {
	XRendererObject old_renderer = activeRenderer;

    	activeRenderer = renderer;

		propChangeSupport.firePropertyChange("ActiveRenderer", old_renderer, activeRenderer);
    }

	public boolean getStartsWithHomePage()
    {
    	return startsWithHomePage;
    }

	public void setStartsWithHomePage(boolean b)
    {
	boolean old_start = startsWithHomePage;

    	startsWithHomePage = b;
		propChangeSupport.firePropertyChange("StartsWithHomePage",old_start,startsWithHomePage);
    }

	public boolean hasNavigationToolBar()
    {
    	return navigationToolBar;
    }

	public void setNavigationToolBar(boolean b)
    {
	boolean old_navigation = navigationToolBar;

    	navigationToolBar = b;
		propChangeSupport.firePropertyChange("NavigationToolBar",old_navigation,navigationToolBar);
    }

	public boolean hasPersonalToolBar()
    {
    	return personalToolBar;
    }

	public void setPersonalToolBar(boolean b)
    {
	boolean old_personal = personalToolBar;

    	personalToolBar = b;
		propChangeSupport.firePropertyChange("PersonalToolBar",old_personal,personalToolBar);
    }

	public boolean hasLocationToolBar()
    {
    	return locationToolBar;
    }

	public void setLocationToolBar(boolean b)
    {
	boolean old_location = locationToolBar;

    	locationToolBar = b;
		propChangeSupport.firePropertyChange("LocationToolBar",old_location,locationToolBar);
    }

	public boolean hasStatusBar()
    {
    	return statusBar;
    }

	public void setStatusBar(boolean b)
    {
	boolean old_status = statusBar;

    	statusBar = b;
		propChangeSupport.firePropertyChange("StatusBar",old_status,statusBar);
    }

	public XViewTool getBookmarkPane()
    {
    	return bookmarkPane;
    }

	public void setBookmarkPane(XViewTool view_tool)
    {
	XViewTool old_bookmark_pane = bookmarkPane;

    	bookmarkPane = view_tool;
		propChangeSupport.firePropertyChange("BookmarkPane",old_bookmark_pane,bookmarkPane);
    }

	public XViewTool getHistoryPane()
    {
    	return historyPane;
    }

	public void setHistoryPane(XViewTool view_tool)
    {
	XViewTool old_history_pane = historyPane;

    	historyPane = view_tool;
		propChangeSupport.firePropertyChange("HistoryPane",old_history_pane,historyPane);
    }

	public XViewTool getDocumentSelector()
    {
    	return documentSelector;
    }

	public void setDocumentSelector(XViewTool view_tool)
    {
	XViewTool old_doc_sel = documentSelector;

    	documentSelector = view_tool;
		propChangeSupport.firePropertyChange("DocumentSelector",old_doc_sel,documentSelector);
    }

	public int getContentStyle()
    {
    	return contentStyle;
    }

	public void setContentStyle(int style)
    {
	int old_style = contentStyle;

    	contentStyle = style;
		propChangeSupport.firePropertyChange("ContentStyle",old_style,contentStyle);
    }

	public int getContentTabPlacement()
    {
    	return contentTabPlacement;
    }

	public void setContentTabPlacement(int place)
    {
	int old_placement = contentTabPlacement;

    	contentTabPlacement = place;
		propChangeSupport.firePropertyChange("ContentTabPlacement",old_placement,contentTabPlacement);
    }

	public String getHomeURL()
    {
    	return homeURL;
    }

	public void setHomeURL(String url)
    {
	String old_home = homeURL;

    	homeURL = url.trim();
		propChangeSupport.firePropertyChange("HomeURL",old_home,homeURL);
    }

	public void setHttpProxy(XProxyServer http_proxy)
	{
	XProxyServer old_http_proxy = httpProxy;

		httpProxy = http_proxy;
		propChangeSupport.firePropertyChange("HttpProxy",old_http_proxy,httpProxy);
	}

	public XProxyServer getHttpProxy()
	{
		return httpProxy;
	}

	public void setFtpProxy(XProxyServer ftp_proxy)
	{
	XProxyServer old_ftp_proxy = ftpProxy;

		ftpProxy = ftp_proxy;
		propChangeSupport.firePropertyChange("FtpProxy",old_ftp_proxy,ftpProxy);
	}

	public XProxyServer getFtpProxy()
	{
		return ftpProxy;
	}

	public void setSocksProxy(XProxyServer socks_proxy)
	{
	XProxyServer old_socks_proxy = socksProxy;

		socksProxy = socks_proxy;
		propChangeSupport.firePropertyChange("SocksProxy",old_socks_proxy,socksProxy);
	}

	public XProxyServer getSocksProxy()
	{
		return socksProxy;
	}

	public void setGopherProxy(XProxyServer gopher_proxy)
	{
	XProxyServer old_gopher_proxy = gopherProxy;

		gopherProxy = gopher_proxy;
		propChangeSupport.firePropertyChange("GopherProxy",old_gopher_proxy,gopherProxy);
	}

	public XProxyServer getGopherProxy()
	{
		return gopherProxy;
	}

	public int getHistoryExpirationDays()
	{
		return historyExpirationDays;
	}

	public void setHistoryExpirationDays(int expire)
	{
	int old_expire_days = historyExpirationDays;

		historyExpirationDays = expire;
		propChangeSupport.firePropertyChange("HistoryExpirationDays",old_expire_days,historyExpirationDays);
	}

	public String getUserName()
    {
    	return userName;
    }

	public void setUserName(String user_name)
    {
	String old_user = userName;

    	userName = user_name.trim();
		propChangeSupport.firePropertyChange("UserName",old_user,userName);
    }

	public String getPassword()
    {
    	return password;
    }

	public void setPassword(String pass)
    {
	String old_pass = password;

    	password = pass.trim();
		propChangeSupport.firePropertyChange("Password",old_pass,password);
    }

	public String getProxyExceptions()
    {
    	return proxyExceptions;
    }

	public void setProxyExceptions(String proxy_exceptions)
    {
	String old_exceptions = proxyExceptions;

    	proxyExceptions = proxy_exceptions.trim();
		propChangeSupport.firePropertyChange("ProxyExceptions",old_exceptions,proxyExceptions);
    }

	public void addDomainCompletion(XDomainCompletion domain_completion)
	{
		domainCompletions.add(domain_completion);
		propChangeSupport.firePropertyChange("DomainCompletions",null,null);
	}

	public void removeDomainCompletion(XDomainCompletion domain_completion)
	{
		domainCompletions.remove(domain_completion);
		propChangeSupport.firePropertyChange("DomainCompletions",null,null);
	}

	public XDomainCompletion getDomainCompletion(boolean alt, boolean ctrl, boolean shft)
	{
	Iterator it = getDomainCompletions();
	XDomainCompletion domain_completion;

		while( it.hasNext() )
		{
			domain_completion = (XDomainCompletion)it.next();
			if( (domain_completion.getAltStatus()==alt) &&
				(domain_completion.getCtrlStatus()==ctrl) &&
				(domain_completion.getShiftStatus()==shft) )
				return domain_completion;
		}

		return null;
	}

	public Iterator getDomainCompletions()
	{
		return domainCompletions.iterator();
	}

	public int getToolBarStyle()
    {
    	return toolBarStyle;
    }

	public void setToolBarStyle(int style)
    {
	int old_style = toolBarStyle;

    	toolBarStyle = style;
		propChangeSupport.firePropertyChange("ToolBarStyle",old_style,toolBarStyle);
    }

// Attributes:
    private final static String configFileName = XProjectConstants.PRODUCT_NAME+".xml";
	private PropertyChangeSupport propChangeSupport = null;
    private static XProjectConfig instance = null;
    private XConfigSerializer serializer = null;

    private LinkedList lookAndFeels = new LinkedList();
    private XLookAndFeel activeLNF;

    private LinkedList renderers = new LinkedList();
    private XRendererObject activeRenderer;

    private LinkedList domainCompletions = new LinkedList();

	private String userName = "";
	private String password = "";

    private int toolBarStyle = -1;

    private String homeURL;
    private int contentStyle;
    private int contentTabPlacement;

    private XViewTool documentSelector;
    private XViewTool bookmarkPane;
    private XViewTool historyPane;

    private boolean startsWithHomePage;

    private XProxyServer httpProxy;
    private XProxyServer ftpProxy;
    private XProxyServer socksProxy;
    private XProxyServer gopherProxy;
    private String proxyExceptions;

    private int historyExpirationDays;

    private boolean navigationToolBar;
    private boolean personalToolBar;
    private boolean locationToolBar;
    private boolean statusBar;
}
