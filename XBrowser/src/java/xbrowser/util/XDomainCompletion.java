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

public final class XDomainCompletion
{
	public XDomainCompletion(boolean alt, boolean ctrl, boolean shft)
	{
		altStatus = alt;
		ctrlStatus = ctrl;
		shiftStatus = shft;
	}

	public boolean getAltStatus()
    {
    	return altStatus;
    }

	public void setAltStatus(boolean alt)
    {
		altStatus = alt;
    }

	public boolean getCtrlStatus()
    {
    	return ctrlStatus;
    }

	public void setCtrlStatus(boolean ctrl)
    {
		ctrlStatus = ctrl;
    }

	public boolean getShiftStatus()
    {
    	return shiftStatus;
    }

	public void setShiftStatus(boolean shft)
    {
		shiftStatus = shft;
    }

	public String getDefaultPrefix()
	{
		return defaultPrefix;
	}

	public void setDefaultPrefix(String default_prefix)
	{
		defaultPrefix = default_prefix;
	}

	public String getDefaultPostfix()
	{
		return defaultPostfix;
	}

	public void setDefaultPostfix(String default_postfix)
	{
		defaultPostfix = default_postfix;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String new_prefix)
	{
		prefix = new_prefix;
	}

	public String getPostfix()
	{
		return postfix;
	}

	public void setPostfix(String new_postfix)
	{
		postfix = new_postfix;
	}

// Attributes:
    private boolean altStatus, ctrlStatus, shiftStatus;
    private String defaultPrefix = "";
    private String defaultPostfix = "";
    private String prefix = "";
    private String postfix = "";
}
