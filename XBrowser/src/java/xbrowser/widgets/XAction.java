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
package xbrowser.widgets;

import java.util.*;
import javax.swing.*;

public abstract class XAction extends AbstractAction
{
	public XAction(Object owner, String action_key, KeyStroke accel)
	{
	String mnemonic_str = getValue(owner, "Action."+action_key+".mnemonic");
	int mnemonic = (mnemonic_str==null) ? -1 : mnemonic_str.charAt(0);

		putValue(Action.NAME, getValue(owner, "Action."+action_key+".name"));

		putValue(Action.SMALL_ICON, XComponentBuilder.getInstance().buildImageIcon(owner, "Action."+action_key+".icon"));
		putValue(Action.SHORT_DESCRIPTION, getValue(owner, "Action."+action_key+".tooltip"));
		putValue(XAction.MNEMONIC, new Integer(mnemonic));
		putValue(XAction.KEY_STROKE, accel);
	}

	private String getValue(Object owner, String key)
	{
		try
		{
			return XComponentBuilder.getInstance().getProperty(owner, key);
		}
		catch( MissingResourceException e )
		{
			return null;
		}
	}

// Attributes:
	public static final String MNEMONIC = "mnemonic";
	public static final String KEY_STROKE = "keystroke";
}
