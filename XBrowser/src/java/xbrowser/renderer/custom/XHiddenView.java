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

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class XHiddenView extends ComponentView
{
	public XHiddenView(Element elem)
	{
		super(elem);

	Dimension size = new Dimension(0,0);

		container = new JPanel();

		container.setSize(size);
		container.setPreferredSize(size);
		container.setMinimumSize(size);
		container.setMaximumSize(size);
	}

	protected Component createComponent()
	{
		return container;
	}

// Attributes:
	private JPanel	container = null;
}
