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
package xbrowser.bookmark.event;

import xbrowser.bookmark.*;

public interface XBookmarkFolderListener
{
	public void bookmarkAdded(XAbstractBookmark bookmark, XBookmarkFolder parent);
	public void bookmarkRemoved(XAbstractBookmark bookmark, XBookmarkFolder parent);
	public void personalFolderChanged(XBookmarkFolder old_folder, XBookmarkFolder new_folder);
	public void clearBookmarks();
}
