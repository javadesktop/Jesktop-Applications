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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import xbrowser.*;

import org.jesktop.frimble.*;

public class XDialog extends JDialog implements PropertyChangeListener
{
	public XDialog(boolean modal)
	{
		super(XBrowser.getMainFrame().getOwnerFrame(), "", modal);
		XProjectConfig.getInstance().addPropertyChangeListener(this);

		doEscapeCloseSupport();
	}

	protected void doEscapeCloseSupport()
	{
		getRootPane().registerKeyboardAction( new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent)
			{
				setVisible(false);
			}
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	/*public void setVisible(boolean b)
	{
		if (b == true)
		{
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = getSize();

			setLocation((screen_size.width - size.width) / 2, (screen_size.height - size.height) / 2);
		}

		super.setVisible(b);
	}*/

	public void propertyChange(PropertyChangeEvent evt)
	{
	String prop_name = evt.getPropertyName();

		//if( prop_name.equals("ActiveLNF") || prop_name.equals("ActiveTheme") )
		//if( prop_name.equals("lookAndFeel") )
		//	updateLookAndFeel();
	}

	protected void updateLookAndFeel()
	{
    	//SwingUtilities.updateComponentTreeUI(this);
	}
}
