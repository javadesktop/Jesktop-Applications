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
package xbrowser;

import java.awt.*;
import javax.swing.*;

public final class XProjectConstants
{
    public final static String PRODUCT_NAME = "XBrowser";
    public final static String PRODUCT_VERSION = "v3.3";
    public final static String PRODUCT_HOME_PAGE = "http://www.geocities.com/xa_arnold/XBrowser.html";
    public final static String PRODUCT_AUTHOR = "Armond Avanes";
    public final static String AUTHOR_PRIMARY_EMAIL = "Armond@Neda.net";
    public final static String AUTHOR_SECONDARY_EMAIL = "ArnoldX@MailCity.com";
    public final static String AUTHOR_HOME_PAGE = "http://www.geocities.com/xa_arnold/index.html";

    //public final static String CONFIG_DIR = "config"+System.getProperty("file.separator");
    public final static String CONFIG_DIR = "config/";

    public final static String DEFAULT_HOME_URL = "http://java.sun.com/";
    public final static String UNTITLED_DOCUMENT = "untitled.html";
    public final static String EMPTY_DOCUMENT = "< ... >";

	public final static String NATIVE_LNF = "Native";
	public final static String CROSSPLATFORM_LNF = "CrossPlatform (Java-Metal)";
	public final static String CROSSPLATFORM_LNF_DEFAULT_THEME = "Steel";

	public final static String DEFAULT_RENDERER_NAME = "Custom Renderer";
	public final static String DEFAULT_RENDERER_PACKAGE = "xbrowser.renderer.custom.XEditorPane";

    public final static String DEFAULT_PROTOCOL = "http://";

    public final static String NEW_BOOKMARK_TITLE = "New Bookmark";
    public final static String NEW_BOOKMARK_HREF = "http://";
    public final static String NEW_BOOKMARK_DESCRIPTION = "";

    public final static String NEW_BOOKMARK_FOLDER_TITLE = "New Folder";
    public final static String NEW_BOOKMARK_FOLDER_DESCRIPTION = "";

    public final static int TOOLBAR_STYLE_ICON_ONLY = 80;
    public final static int TOOLBAR_STYLE_TEXT_ONLY = 81;
    public final static int TOOLBAR_STYLE_TEXT_ICON = 82;

    public final static Dimension TOOLBAR_ICON_ONLY_SIZE = new Dimension(30,30);
    public final static Dimension TOOLBAR_TEXT_ONLY_SIZE = new Dimension(70,25);
    public final static Dimension TOOLBAR_TEXT_ICON_SIZE = new Dimension(70,50);

    public final static int ZOOM_IN = 70;
    public final static int ZOOM_OUT = 71;

    public final static int DESKTOP_PANE_CONTENT = 1;
    public final static int TABBED_PANE_CONTENT = 2;
    public final static int WORKBOOK_CONTENT = 3;
    public final static int FIRST_CONTENT = DESKTOP_PANE_CONTENT;
    public final static int LAST_CONTENT = WORKBOOK_CONTENT;

    public final static int TOOLS_PAGE1 = 50;
    public final static int TOOLS_PAGE2 = 51;
    public final static int TOOLS_PAGE3 = 52;
    public final static int FIRST_TOOLS_PAGE = TOOLS_PAGE1;
    public final static int LAST_TOOLS_PAGE = TOOLS_PAGE3;

    public final static int TOP = SwingConstants.TOP;
    public final static int BOTTOM = SwingConstants.BOTTOM;
    public final static int LEFT = SwingConstants.LEFT;
    public final static int RIGHT = SwingConstants.RIGHT;
    public final static int FIRST_PLACEMENT = TOP;
    public final static int LAST_PLACEMENT = RIGHT;
}
