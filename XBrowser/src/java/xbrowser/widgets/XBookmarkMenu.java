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

import javax.swing.event.*;
import javax.swing.*;
import java.beans.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import xbrowser.*;
import xbrowser.doc.*;
import xbrowser.screen.*;
import xbrowser.bookmark.*;
import xbrowser.bookmark.event.*;

public class XBookmarkMenu extends JMenu implements XBookmarkFolderListener
{
	public XBookmarkMenu()
	{
		setText( XComponentBuilder.getInstance().getProperty(this, "Title") );

	    add( XComponentBuilder.getInstance().buildMenuItem(addToBookmarksAction) );
	    add( XComponentBuilder.getInstance().buildMenuItem(newBookmarkAction) );
	    add( XComponentBuilder.getInstance().buildMenuItem(newBookmarkFolderAction) );
		add( XComponentBuilder.getInstance().buildMenuItem(deleteBookmarkFolderAction) );
	    add( XComponentBuilder.getInstance().buildMenuItem(new SearchBookmarksAction()) );
		addSeparator();
	    add( XComponentBuilder.getInstance().buildMenuItem(new ImportBookmarksAction()) );
	    add( XComponentBuilder.getInstance().buildMenuItem(new ExportBookmarksAction()) );
		addSeparator();

		XBookmarkManager.getInstance().addBookmarkFolderListener(this);
		bookmarkFolder = XBookmarkManager.getInstance();
	}

	public XBookmarkFolder getBookmarkFolder()
	{
		return bookmarkFolder;
	}

	private int computeBookmarkPosition(XAbstractBookmark new_abs_bm, JMenu folder_menu)
	{
	Component[] comps = folder_menu.getMenuComponents();
	XAbstractBookmark abs_bm = null;
	int i;

		for( i=0; i<comps.length; i++ )
		{
			if( comps[i] instanceof XBookmarkMenuItem )
				abs_bm = ((XBookmarkMenuItem)comps[i]).getBookmark();
			else if( comps[i] instanceof XBookmarkFolderMenu )
				abs_bm = ((XBookmarkFolderMenu)comps[i]).getBookmarkFolder();

			if( comparator.compare(new_abs_bm, abs_bm)<0 )
				break;
		}

		return i;
	}

	public void bookmarkAdded(XAbstractBookmark new_abs_bm, XBookmarkFolder parent)
	{
	JMenu folder_menu = getBookmarkMenu(parent,this);

		if( folder_menu==null )
			return;

		if( new_abs_bm instanceof XBookmark )
			folder_menu.insert( new XBookmarkMenuItem((XBookmark)new_abs_bm), computeBookmarkPosition(new_abs_bm, folder_menu));
		else if( new_abs_bm instanceof XBookmarkFolder )
		{
		XBookmarkFolderMenu menu = new XBookmarkFolderMenu((XBookmarkFolder)new_abs_bm);

			menu.add( XComponentBuilder.getInstance().buildMenuItem(addToBookmarksAction) );
			menu.add( XComponentBuilder.getInstance().buildMenuItem(setAsPersonalFolderAction) );
			menu.add( XComponentBuilder.getInstance().buildMenuItem(newBookmarkAction) );
			menu.add( XComponentBuilder.getInstance().buildMenuItem(newBookmarkFolderAction) );
			menu.add( XComponentBuilder.getInstance().buildMenuItem(deleteBookmarkFolderAction) );
			menu.addSeparator();

			folder_menu.insert(menu, computeBookmarkPosition(new_abs_bm, folder_menu));
		}

		if( new_abs_bm instanceof XBookmarkFolder )
		{
		XBookmarkFolder folder = (XBookmarkFolder)new_abs_bm;
		Iterator it = folder.getBookmarks();

			while( it.hasNext() )
				bookmarkAdded( (XAbstractBookmark)it.next(), folder);
		}
	}

	public void bookmarkRemoved(XAbstractBookmark abs_bm_to_remove, XBookmarkFolder parent)
	{
	JMenu folder_menu = getBookmarkMenu(parent,this);

		if( folder_menu==null )
			return;

		if( abs_bm_to_remove instanceof XBookmarkFolder )
		{
		XBookmarkFolder folder = (XBookmarkFolder)abs_bm_to_remove;
		Iterator it = folder.getBookmarks();

			while( it.hasNext() )
				bookmarkRemoved( (XAbstractBookmark)it.next(), folder);
		}

	Component[] comps = folder_menu.getMenuComponents();
	XAbstractBookmark abs_bm = null;

		for( int i=0; i<comps.length; i++ )
		{
			if( comps[i] instanceof XBookmarkMenuItem )
				abs_bm = ((XBookmarkMenuItem)comps[i]).getBookmark();
			else if( comps[i] instanceof XBookmarkFolderMenu )
				abs_bm = ((XBookmarkFolderMenu)comps[i]).getBookmarkFolder();

			if( abs_bm==abs_bm_to_remove )
			{
				if( comps[i] instanceof XBookmarkMenuItem )
					((XBookmarkMenuItem)comps[i]).removingFromParent();
				else if( comps[i] instanceof XBookmarkFolderMenu )
					((XBookmarkFolderMenu)comps[i]).removingFromParent();

				folder_menu.remove(i);
				break;
			}
		}
	}

	public void personalFolderChanged(XBookmarkFolder old_folder, XBookmarkFolder new_folder)
	{
	XBookmarkFolderMenu old_folder_menu = (XBookmarkFolderMenu)getBookmarkMenu(old_folder,this);
	XBookmarkFolderMenu new_folder_menu = (XBookmarkFolderMenu)getBookmarkMenu(new_folder,this);

		if( old_folder_menu!=null )
			old_folder_menu.setPersonal(false);

		if( new_folder_menu!=null )
			new_folder_menu.setPersonal(true);
	}

	public void clearBookmarks()
	{
		///???!!!
	}

	private JMenu getBookmarkMenu(XBookmarkFolder bookmark_folder, JMenu base_menu)
	{
		if( bookmark_folder==bookmarkFolder )
			return this;
		else
		{
		Component[] components = base_menu.getMenuComponents();
		JMenu menu;

			for( int i=0; i<components.length; i++ )
			{
				if( components[i] instanceof XBookmarkFolderMenu )
				{
					menu = (JMenu)components[i];
					if( ((XBookmarkFolderMenu)menu).getBookmarkFolder()==bookmark_folder )
						return menu;
					else
					{
						menu = getBookmarkMenu(bookmark_folder,menu);
						if( menu!=null )
							return menu;
					}
				}
			}

			return null;
		}
	}

	private void newBookmark(JMenu parent)
	{
	XBookmark bm = new XBookmark(XProjectConstants.NEW_BOOKMARK_HREF);
	XBookmarkFolder parent_folder = null;

		bm.setTitle(XProjectConstants.NEW_BOOKMARK_TITLE);
		bm.setDescription(XProjectConstants.NEW_BOOKMARK_DESCRIPTION);

		if( parent instanceof XBookmarkMenu )
			parent_folder = ((XBookmarkMenu)parent).getBookmarkFolder();
		else if( parent instanceof XBookmarkFolderMenu )
			parent_folder = ((XBookmarkFolderMenu)parent).getBookmarkFolder();

		XBookmarkPropertiesLayout.getInstance(bm, parent_folder).setVisible(true);
	}

    private class AddToBookmarksAction extends XAction
    {
        public AddToBookmarksAction()
        {
            super(XBookmarkMenu.this, "AddBookmark", KeyStroke.getKeyStroke(KeyEvent.VK_D,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		JMenu parent = (JMenu) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
		XDocument active_doc = XBrowser.getBrowser().getActiveDocument();

			if( active_doc!=null )
			{
			XBookmark bm = new XBookmark(active_doc.getPageCompletePath());

				bm.setTitle(active_doc.getPageTitle());

				if( parent instanceof XBookmarkMenu )
					(((XBookmarkMenu)parent).getBookmarkFolder()).addBookmark(bm);
				else if( parent instanceof XBookmarkFolderMenu )
					(((XBookmarkFolderMenu)parent).getBookmarkFolder()).addBookmark(bm);
			}
			else
				newBookmark(parent);
        }
    }

    private class DeleteBookmarkFolderAction extends XAction
    {
        public DeleteBookmarkFolderAction()
        {
            super(XBookmarkMenu.this, "DeleteFolder", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		JMenu parent = (JMenu) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();

			if( parent instanceof XBookmarkMenu )
				(((XBookmarkMenu)parent).getBookmarkFolder()).removeAllBookmarks();
			else if( parent instanceof XBookmarkFolderMenu )
			{
			XBookmarkFolder folder = ((XBookmarkFolderMenu)parent).getBookmarkFolder();

				folder.getParent().removeBookmark(folder);
			}
        }
    }

    private class NewBookmarkAction extends XAction
    {
        public NewBookmarkAction()
        {
            super(XBookmarkMenu.this, "NewBookmark", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		JMenu parent = (JMenu) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();

			newBookmark(parent);
        }
    }

    private class NewBookmarkFolderAction extends XAction
    {
        public NewBookmarkFolderAction()
        {
            super(XBookmarkMenu.this, "NewBookmarkFolder", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		JMenu parent = (JMenu) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();
		XBookmarkFolder bm_folder = new XBookmarkFolder();
		XBookmarkFolder parent_folder = null;

			bm_folder.setTitle(XProjectConstants.NEW_BOOKMARK_FOLDER_TITLE);
			bm_folder.setDescription(XProjectConstants.NEW_BOOKMARK_FOLDER_DESCRIPTION);

			if( parent instanceof XBookmarkMenu )
				parent_folder = ((XBookmarkMenu)parent).getBookmarkFolder();
			else if( parent instanceof XBookmarkFolderMenu )
				parent_folder = ((XBookmarkFolderMenu)parent).getBookmarkFolder();

			XBookmarkPropertiesLayout.getInstance(bm_folder, parent_folder).setVisible(true);
        }
    }

    private class SearchBookmarksAction extends XAction
    {
        public SearchBookmarksAction()
        {
            super(XBookmarkMenu.this, "SearchBookmarks", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	        XFindLayout2.getInstance(XFindLayout2.SEARCH_BOOKMARKS).setVisible(true);
        }
    }

    private class SetAsPersonalFolderAction extends XAction
    {
        public SetAsPersonalFolderAction()
        {
            super(XBookmarkMenu.this, "SetAsPersonalFolder", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		JMenu parent = (JMenu) ((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker();

			if( parent instanceof XBookmarkFolderMenu )
			{
			XBookmarkFolder folder = ((XBookmarkFolderMenu)parent).getBookmarkFolder();

				folder.setPersonalFolder(true);
			}
        }
    }

    private class ImportBookmarksAction extends XAction
    {
        public ImportBookmarksAction()
        {
            super(XBookmarkMenu.this, "ImportBookmarks", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XImportExportBookmarksLayout.getInstance(XImportExportBookmarksLayout.IMPORT_BOOKMARKS, null).setVisible(true);
        }
    }

    private class ExportBookmarksAction extends XAction
    {
        public ExportBookmarksAction()
        {
            super(XBookmarkMenu.this, "ExportBookmarks", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XImportExportBookmarksLayout.getInstance(XImportExportBookmarksLayout.EXPORT_BOOKMARKS, null).setVisible(true);
        }
    }

// Attributes:
	private XAction addToBookmarksAction = new AddToBookmarksAction();
	private XAction deleteBookmarkFolderAction = new DeleteBookmarkFolderAction();
	private XAction newBookmarkAction = new NewBookmarkAction();
	private XAction newBookmarkFolderAction = new NewBookmarkFolderAction();
	private XAction setAsPersonalFolderAction = new SetAsPersonalFolderAction();

	private XBookmarkFolder bookmarkFolder = null;
	private XBookmarkComparator comparator = new XBookmarkComparator(0, true);
}
