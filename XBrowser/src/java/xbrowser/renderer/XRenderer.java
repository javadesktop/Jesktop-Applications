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
package xbrowser.renderer;

import java.io.*;
import java.awt.*;
import java.util.*;

import xbrowser.renderer.event.*;
import xbrowser.widgets.*;

public interface XRenderer
{
	public Component getComponent();

    public void addRendererListener(XRendererListener renderer_listener);
    public void removeRendererListener(XRendererListener renderer_listener);

	public void showURL(String url);
	public String getPageTitle();

	public boolean hasSource();
	public String getSource();

	public void reload();
	public void refresh();

	public void selectAll();
	public void copy();

    public void stopRendering();
	public void destroying();

    public void saveContent(File dest_file);

    public void showNextPage();
    public void showPreviousPage();

    public boolean hasForwardHistory();
    public boolean hasBackwardHistory();

    public String getNextPagePath();
    public String getPreviousPagePath();

    public Iterator getForwardHistory();
    public Iterator getBackwardHistory();

	public void showPageFromHistory(int index, int history_type);

    public String getPageCompletePath();
    public String getCurrentFocusedLink();
    public String getCurrentURL();
    public Iterator getURLList();

    public void findNext(String find_phrase, boolean case_sensitive, boolean whole_word, boolean search_up) throws Exception;

    public void startPrinting(boolean print_immediately);
	public void stopPrinting();

    public void changePreviewScale(XPreviewContainer preview, int new_scale);
    public void loadPreviews(final XPreviewContainer preview, int scale) throws Exception;

// Attributes:
	public final static int BACKWARD_HISTORY = 30;
	public final static int FORWARD_HISTORY = 31;
}
