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
package xbrowser.screen;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import xbrowser.widgets.*;
import xbrowser.history.*;

public class XHistoryLayout extends XFrame
{
	public static XHistoryLayout getInstance()
	{
		if( instance==null )
			instance = new XHistoryLayout();

		return instance;
	}

	private XHistoryLayout()
	{
		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );

	XHistoryTable history_table = new XHistoryTable( XHistoryManager.getInstance() );

		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add(new JScrollPane(history_table), BorderLayout.CENTER);
		getContentPane().add(getButtonsPanel(history_table),BorderLayout.SOUTH);

        setIconImage( XComponentBuilder.getInstance().buildImageIcon(this, "image.FrameIcon").getImage() );
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();

    Dimension size = getSize();

        size.height = 350;
        setSize(size);
	}

    private JPanel getButtonsPanel(XHistoryTable history_table)
    {
	JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JButton btn_close = XComponentBuilder.getInstance().buildButton(new CloseAction());

		pnl.add(XComponentBuilder.getInstance().buildButton(history_table.goToPageAction));
		pnl.add(XComponentBuilder.getInstance().buildButton(history_table.addToBookmarksAction));
		pnl.add(XComponentBuilder.getInstance().buildButton(history_table.deleteAction));
		pnl.add(XComponentBuilder.getInstance().buildButton(new ClearHistoryAction()));
		pnl.add(XComponentBuilder.getInstance().buildButton(new SearchHistoryAction()));
		pnl.add(XComponentBuilder.getInstance().buildButton(history_table.copyAction));
		pnl.add(btn_close);

        getRootPane().setDefaultButton(btn_close);

        return pnl;
	}

    private class ClearHistoryAction extends XAction
    {
        public ClearHistoryAction()
        {
            super(XHistoryLayout.this, "ClearHistory", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XHistoryManager.getInstance().removeAllHistory();
        }
    }

    private class SearchHistoryAction extends XAction
    {
        public SearchHistoryAction()
        {
            super(XHistoryLayout.this, "SearchHistory", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	        XFindLayout2.getInstance(XFindLayout2.SEARCH_HISTORY).setVisible(true);
        }
    }

    private class CloseAction extends XAction
    {
        public CloseAction()
        {
            super(XHistoryLayout.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
        }
    }

// Attributes:
	private static XHistoryLayout instance = null;
}
