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

public final class XProxyServer
{
	public XProxyServer()
	{
		useProxy = false;
		proxyAddress = "";
		proxyPort = "";
	}

	public boolean getUseProxy()
    {
    	return useProxy;
    }

	public void setUseProxy(boolean use)
    {
    	useProxy = use;
    }

	public String getProxyAddress()
    {
    	return proxyAddress;
    }

	public void setProxyAddress(String address)
    {
    	proxyAddress = address.trim();
    }

	public String getProxyPort()
    {
    	return proxyPort;
    }

	public void setProxyPort(String port)
    {
    	proxyPort = port.trim();
    }

	public boolean equals(Object obj)
	{
		if( !(obj instanceof XProxyServer) )
			return false;

	XProxyServer proxy = (XProxyServer)obj;

		return( useProxy==proxy.getUseProxy() && proxyAddress.equals(proxy.getProxyAddress()) && proxyPort.equals(proxy.getProxyPort()) );
	}

// Attributes:
    private boolean useProxy;
    private String proxyAddress;
    private String proxyPort;
}
