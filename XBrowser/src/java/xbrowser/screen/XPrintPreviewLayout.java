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
import java.util.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.event.*;

import xbrowser.*;
import xbrowser.widgets.*;
import xbrowser.widgets.event.*;
import xbrowser.doc.*;

public class XPrintPreviewLayout extends XDialog implements XScaleListener
{
    public XPrintPreviewLayout(XDocument doc)
    {
        super(true);

		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );
        setSize(600, 400);
        document = doc;

	JToolBar tlbMain = new JToolBar();

        tlbMain.setFloatable(false);
		tlbMain.add( XComponentBuilder.getInstance().buildToolbarButton(new ZoomInAction()) );
		tlbMain.add( XComponentBuilder.getInstance().buildToolbarButton(new ZoomOutAction()) );
		tlbMain.add( XComponentBuilder.getInstance().buildToolbarButton(new PrintAction()) );
		tlbMain.add( XComponentBuilder.getInstance().buildToolbarButton(new CloseAction()) );

        tlbMain.addSeparator();
        tlbMain.add(cmbScale);
        getContentPane().add(tlbMain, BorderLayout.NORTH);

        previewContainer = new XPreviewContainer(this);
        getContentPane().add(new JScrollPane(previewContainer), BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				document.getRenderer().stopPrinting();
			}
		});

        new Thread() {
            public void run()
            {
                XPrintPreviewLayout.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try
                {
                    document.getRenderer().loadPreviews(previewContainer, 10);
                }
                catch( Exception e )
                {
				String title = XComponentBuilder.getInstance().getProperty(XPrintPreviewLayout.this, "Error");

        	        JOptionPane.showMessageDialog(XPrintPreviewLayout.this, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
                }
                XPrintPreviewLayout.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }.start();

        setVisible(true);
    }

    public void scaleChanged(final int new_scale)
    {
		new Thread() {
			public void run()
			{
				XPrintPreviewLayout.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				document.getRenderer().changePreviewScale(previewContainer, new_scale);
				XPrintPreviewLayout.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}.start();
	}

    public void zoomedIn()
    {
	int new_scale = cmbScale.getCurrentScale()*2;

		cmbScale.getEditor().setItem(new_scale+" %");
		scaleChanged(new_scale);
	}

    public void zoomedOut()
    {
	int new_scale = (int)cmbScale.getCurrentScale()/2;

		if( new_scale<1 )
			new_scale = 1;

		cmbScale.getEditor().setItem(new_scale+" %");
		scaleChanged(new_scale);
	}

    private class XScaleComboBox extends JComboBox implements ActionListener
    {
		public XScaleComboBox(XScaleListener scale_listener)
		{
			super( new String[]{ "10 %", "25 %", "50 %", "75 %", "100 %" } );

			scaleListener = scale_listener;

	        addActionListener(this);
	        getEditor().addActionListener(this);
			setMaximumSize(getPreferredSize());
			setEditable(true);
		}

        public int getCurrentScale()
        {
		String str = getEditor().getItem().toString();

			if( str.endsWith("%") )
				str = str.substring(0, str.length()-1);
			str = str.trim();

		int cur_scale;

			try
			{
				cur_scale = (int)Integer.parseInt(str);
				if( cur_scale<1 )
					cur_scale = 1;
			}
			catch( Exception e )
			{
				str = getSelectedItem().toString();

				if( str.endsWith("%") )
					str = str.substring(0, str.length()-1);
				str = str.trim();

				try
				{
					cur_scale = (int)Integer.parseInt(str);
				}
				catch( Exception ex )
				{
					cur_scale = 1;
				}
			}

			return cur_scale;
		}

        public void actionPerformed(ActionEvent e)
	    {
		int cur_scale = getCurrentScale();

			getEditor().setItem(cur_scale+" %");
			scaleListener.scaleChanged(cur_scale);
        }

	// Attributes:
		private XScaleListener scaleListener;
	}

    private class ZoomInAction extends XAction
    {
        public ZoomInAction()
        {
            super(XPrintPreviewLayout.this, "ZoomIn", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			previewContainer.setZoomMode(XProjectConstants.ZOOM_IN);
			zoomedIn();
        }
    }

    private class ZoomOutAction extends XAction
    {
        public ZoomOutAction()
        {
            super(XPrintPreviewLayout.this, "ZoomOut", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			previewContainer.setZoomMode(XProjectConstants.ZOOM_OUT);
			zoomedOut();
        }
    }

    private class PrintAction extends XAction
    {
        public PrintAction()
        {
            super(XPrintPreviewLayout.this, "Print", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    document.printDocumentImmediately();
		    setVisible(false);
		    dispose();
        }
    }

    private class CloseAction extends XAction
    {
        public CloseAction()
        {
            super(XPrintPreviewLayout.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			document.getRenderer().stopPrinting();
            setVisible(false);
            dispose();
        }
    }

// Attributes:
    private int pageWidth;
    private int pageHeight;
    private XDocument document;
    private XPreviewContainer previewContainer;
    private XScaleComboBox cmbScale = new XScaleComboBox(this);
}
