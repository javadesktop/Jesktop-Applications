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
package xbrowser.doc.view;

import java.io.*;
import java.awt.*;
import java.awt.print.*;
import java.applet.*;
import java.net.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.event.*;

import xbrowser.*;
import xbrowser.doc.*;
import xbrowser.doc.event.*;
import xbrowser.widgets.*;
import xbrowser.renderer.*;
import xbrowser.renderer.event.*;

public class XDocumentView extends JPanel implements XDocument, XRendererListener
{
	public XDocumentView()
	{
		try
		{
        	myRenderer = XProjectConfig.getInstance().getActiveRenderer().buildRenderer();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

        scrPageContent = new JScrollPane(myRenderer.getComponent());

        setLayout(new BorderLayout());

		add(scrPageContent, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);

		myRenderer.addRendererListener(this);

		myRenderer.getComponent().addMouseListener( new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				if( e.isPopupTrigger() )
					XBrowser.getBrowser().showPopupMenu(myRenderer.getComponent(), myRenderer.getCurrentFocusedLink(), e.getX(), e.getY());
			}
		});
    }

	public XRenderer getRenderer()
	{
		return myRenderer;
	}

    ///////////// XRendererListener ///////////
    public void pageLoadingProgress(long read_size, long entire_size)
    {
	String message = XComponentBuilder.getInstance().getProperty(this, "LoadingProgress", myRenderer.getPageCompletePath(), ""+(read_size/1000));

        statusBar.setStatusMessage(message);
    }

    public void pageLoadingStopped()
    {
		reset();
    }

    public void pageLoadingStarted()
    {
		removeSourceDisplayingFacility();
    	statusBar.setStatusMessage(XComponentBuilder.getInstance().getProperty(this, "OpeningPage", myRenderer.getPageCompletePath()));
		statusBar.startProgress();

	    listener.documentLoadingStarted(this);
	}

    public void pageLoadingFinished(Exception e)
    {
		reset();

		if( e==null )
		    listener.documentLoadingFinished(this);
		else
		{
		String message = XComponentBuilder.getInstance().getProperty(XDocumentView.this, "LoadingError", myRenderer.getPageCompletePath());
		String title = XComponentBuilder.getInstance().getProperty(XDocumentView.this, "Error");

			JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
		}
	}

    public void pageAddedToHistory(String url)
    {
    	listener.pageAddedToDocumentHistory(this, url);
	}

	public void pagePrintingStarted()
	{
		setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
	}

	public void pagePrintingFinished(PrinterException e)
	{
		statusBar.resetStatus();
		setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) );

		if( e==null )
			JOptionPane.showMessageDialog(this, XComponentBuilder.getInstance().getProperty(this, "PrintCompleted"), XComponentBuilder.getInstance().getProperty(this, "Information"), JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, XComponentBuilder.getInstance().getProperty(this, "PrintError"), XComponentBuilder.getInstance().getProperty(this, "Warning"), JOptionPane.WARNING_MESSAGE);
	}

    public void renderingPage(int page_index)
    {
	String message = XComponentBuilder.getInstance().getProperty(this, "Rendering", ""+(page_index+1));

        statusBar.setStatusMessage(message);
	}

    public void renderingFinished()
    {
		statusBar.resetStatus();
		setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR) );
	}

    public void showAppletStatus(String status)
    {
		if( status==null || status.trim().equals("") )
			statusBar.resetStatus();
		else
        	statusBar.setStatusMessage(status);
	}

    public void showAppletLifeCycle(Applet applet, int status)
    {
	String msg_key = "";

		switch( status )
		{
			case STARTING_APPLET_INIT:
				msg_key = "StartingAppletInit";
				break;

			case STARTING_APPLET_START:
				msg_key = "StartingAppletStart";
				break;

			case STARTING_APPLET_STOP:
				msg_key = "StartingAppletStop";
				break;

			case STARTING_APPLET_DESTROY:
				msg_key = "StartingAppletDestroy";
				break;

			case STARTING_APPLET_LOAD:
				msg_key = "StartingAppletLoad";
				break;

			case APPLET_INIT_SUCCESS:
				msg_key = "AppletInitSuccess";
				break;

			case APPLET_START_SUCCESS:
				msg_key = "AppletStartSuccess";
				break;

			case APPLET_STOP_SUCCESS:
				msg_key = "AppletStopSuccess";
				break;

			case APPLET_DESTROY_SUCCESS:
				msg_key = "AppletDestroySuccess";
				break;

			case APPLET_LOAD_SUCCESS:
				msg_key = "AppletLoadSuccess";
				break;

			case APPLET_INIT_FAILED:
				msg_key = "AppletInitFailed";
				break;

			case APPLET_START_FAILED:
				msg_key = "AppletStartFailed";
				break;

			case APPLET_STOP_FAILED:
				msg_key = "AppletStopFailed";
				break;

			case APPLET_DESTROY_FAILED:
				msg_key = "AppletDestroyFailed";
				break;

			case APPLET_LOAD_FAILED:
				msg_key = "AppletLoadFailed";
				break;

			default:
				return;
		}

    	statusBar.setStatusMessage(XComponentBuilder.getInstance().getProperty(this, msg_key));
	}

	public void hyperlinkEntered(String hyper_link)
	{
    	statusBar.setStatusMessage(XComponentBuilder.getInstance().getProperty(this, "GotoLink", hyper_link));
	}

	public void hyperlinkExited(String hyper_link)
	{
        statusBar.resetStatus();
	}
    ///////////// XRendererListener ///////////

	public Component getComponent()
	{
		return this;
	}

    public void addDocumentListener(XDocumentListener listener)
    {
		this.listener = listener;
	}

    public void removeDocumentListener(XDocumentListener listener)
    {
		if( this.listener==listener )
			listener = null;
	}

    public void showURL(String url)
	{
		myRenderer.showURL(url);
	}

    public void closingDocument()
	{
		myRenderer.destroying();
	}

    public String getPageTitle()
	{
		return myRenderer.getPageTitle();
	}

	public String getPageCompletePath()
	{
		return myRenderer.getPageCompletePath();
	}

	private void reset()
    {
        statusBar.resetStatus();
		statusBar.stopProgress();
	}

    public String toString()
    {
        return myRenderer.getPageTitle();
    }

    private void removeSourceDisplayingFacility()
    {
		if( tabPane!=null && tabPane.isAncestorOf(scrPageContent) )
		{
			tabPane.setSelectedComponent(scrPageContent);
			remove(tabPane);
			tabPane.removeAll();
			sourceContent.setText("");
			add(scrPageContent, BorderLayout.CENTER);
		}
	}

    private void prepareSourceDisplayingFacility()
    {
		if( tabPane==null )
		{
			tabPane = new JTabbedPane();
			sourceContent = new JTextArea();
			sourceContent.setEditable(false);
			scrSourceContent = new JScrollPane(sourceContent);

			tabPane.add(XComponentBuilder.getInstance().getProperty(this, "PageTab"),scrPageContent);
			tabPane.add(XComponentBuilder.getInstance().getProperty(this, "SourceTab"),scrSourceContent);

			remove(scrPageContent);
			add(tabPane, BorderLayout.CENTER);
		}
		else
		{
			if( !isAncestorOf(tabPane) )
			{
				remove(scrPageContent);
				add(tabPane, BorderLayout.CENTER);
			}

			if( tabPane.indexOfComponent(scrPageContent)==-1 )
			{
				tabPane.add(XComponentBuilder.getInstance().getProperty(this, "PageTab"),scrPageContent);
				tabPane.add(XComponentBuilder.getInstance().getProperty(this, "SourceTab"),scrSourceContent);
			}
		}

		tabPane.setSelectedComponent(scrSourceContent);
	}

    public void showPageSource()
    {
		if( !myRenderer.hasSource() )
			return;

		statusBar.setStatusMessage(XComponentBuilder.getInstance().getProperty(this, "DisplayingSource"));
		statusBar.startProgress();

		prepareSourceDisplayingFacility();

		try
		{
			sourceContent.setText( myRenderer.getSource() );
		}
		catch( Exception e )
		{
			sourceContent.setText("");
		}

		sourceContent.setCaretPosition(0);

		reset();
    }

    public void printDocument()
    {
        new Thread()
        {
            public void run()
            {
                myRenderer.startPrinting(false);
            }
        }.start();
    }

    public void printDocumentImmediately()
    {
        new Thread()
        {
            public void run()
            {
                myRenderer.startPrinting(true);
            }
        }.start();
    }

// Attributes:
	private XDocumentListener listener = null;

	private XRenderer myRenderer = null;
	private JScrollPane scrPageContent = null;
	private XStatusBar statusBar = new XStatusBar(true,false);
	private JTabbedPane tabPane = null;
	private JTextArea sourceContent = null;
	private JScrollPane scrSourceContent = null;
}
