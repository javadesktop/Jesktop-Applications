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
package xbrowser.renderer.event;

import java.applet.*;
import java.awt.print.*;

public interface XRendererListener
{
	public void hyperlinkEntered(String hyper_link);
	public void hyperlinkExited(String hyper_link);

    public void pageLoadingProgress(long read_size, long entire_size);
    public void pageLoadingFinished(Exception e);
    public void pageLoadingStarted();
    public void pageLoadingStopped();

    public void pageAddedToHistory(String url);

	public void pagePrintingStarted();
	public void pagePrintingFinished(PrinterException e);
    public void renderingPage(int page_index);
    public void renderingFinished();

    public void showAppletStatus(String status);
    public void showAppletLifeCycle(Applet applet, int status);

// Attributes:
	public final static int STARTING_APPLET_INIT = 100;
	public final static int STARTING_APPLET_START = 101;
	public final static int STARTING_APPLET_STOP = 102;
	public final static int STARTING_APPLET_DESTROY = 103;
	public final static int STARTING_APPLET_LOAD = 104;

	public final static int APPLET_INIT_SUCCESS = 105;
	public final static int APPLET_START_SUCCESS = 106;
	public final static int APPLET_STOP_SUCCESS = 107;
	public final static int APPLET_DESTROY_SUCCESS = 108;
	public final static int APPLET_LOAD_SUCCESS = 109;

	public final static int APPLET_INIT_FAILED = 110;
	public final static int APPLET_START_FAILED = 111;
	public final static int APPLET_STOP_FAILED = 112;
	public final static int APPLET_DESTROY_FAILED = 113;
	public final static int APPLET_LOAD_FAILED = 114;
}
