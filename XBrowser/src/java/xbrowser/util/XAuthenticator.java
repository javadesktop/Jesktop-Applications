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

import java.net.*;

public class XAuthenticator extends Authenticator
{
	public static XAuthenticator getInstance()
	{
		if( instance==null )
			instance = new XAuthenticator();

		return instance;
	}

	private XAuthenticator()
	{
		Authenticator.setDefault(this);
	}

	public void setAuthenticationInfo(String user, String pass)
	{
		passAuth = new PasswordAuthentication(user, pass.toCharArray());
	}

	protected PasswordAuthentication getPasswordAuthentication()
	{
		return passAuth;
	}

// Attributes:
	private static XAuthenticator instance = null;
	private PasswordAuthentication passAuth = null;
}
