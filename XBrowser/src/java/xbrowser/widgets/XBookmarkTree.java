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
import javax.swing.tree.*;
import javax.swing.event.*;
import java.beans.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.*;
import java.util.*;

import xbrowser.*;
import xbrowser.doc.*;
import xbrowser.screen.*;
import xbrowser.util.*;
import xbrowser.bookmark.*;
import xbrowser.bookmark.event.*;

public class XBookmarkTree extends JTree implements DragGestureListener
{
	public XBookmarkTree()
	{
		customizeTree();
	  	registerListeners();

	    bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new AddToBookmarksAction()) );
	    bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new SetAsPersonalFolderAction()) );
		bookmarkFolderPopup.addSeparator();
	    bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new OpenAllBookmarksAction()) );
	    bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new NewBookmarkAction()) );
	    bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new NewBookmarkFolderAction()) );
		bookmarkFolderPopup.addSeparator();
		bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new ImportBookmarksAction()) );
		bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new ExportBookmarksAction()) );
		bookmarkFolderPopup.addSeparator();
		bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new CopyAction()) );
		bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new DeleteAction()) );
		bookmarkFolderPopup.addSeparator();
		bookmarkFolderPopup.add( XComponentBuilder.getInstance().buildMenuItem(new PropertiesAction()) );

	    bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new OpenBookmarkAction()) );
	    bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new OpenBookmarkInNewPageAction()) );
		bookmarkPopup.addSeparator();
		bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new CopyAction()) );
		bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new DeleteAction()) );
		bookmarkPopup.addSeparator();
		bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new PropertiesAction()) );

		add(bookmarkPopup);
		add(bookmarkFolderPopup);
	}

	private void customizeTree()
	{
	XTreeCellRenderer renderer = new XTreeCellRenderer();

		setModel( new XTreeModel(new XMutableTreeNode(XBookmarkManager.getInstance())) );
		setCellRenderer(renderer);
    	getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	putClientProperty("JTree.lineStyle", "Angled");
    	//setToggleClickCount(1);
    	setScrollsOnExpand(true);

    	setEditable(false);
    	//setEditable(true);
    	//setCellEditor( new DefaultTreeCellEditor(this, renderer, new XCellEditor()) );
	}

	private void registerListeners()
	{
	  	DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

		ToolTipManager.sharedInstance().registerComponent(this);

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				if( e.getClickCount()==2 )
				{
				TreePath tree_path = getPathForLocation(e.getX(),e.getY());

					if( tree_path!=null && isPathSelected(tree_path) )
					{
					int path_len = tree_path.getPath().length;
					XMutableTreeNode tree_node = (XMutableTreeNode)tree_path.getPath()[path_len-1];

						if( tree_node instanceof XMutableTreeNode )
							((XMutableTreeNode)tree_node).handleDoubleClick();
					}
				}

				showPopup(e);
			}

		    public void mouseReleased(MouseEvent e)
		    {
				showPopup(e);
			}

			public void mousePressed(MouseEvent e)
			{
				showPopup(e);
			}

			private void showPopup(MouseEvent e)
			{
      			if( e.isPopupTrigger() )
      			{
				TreePath tree_path = getPathForLocation(e.getX(),e.getY());

					if( tree_path!=null )
					{
					int path_len = tree_path.getPath().length;
					XMutableTreeNode tree_node = (XMutableTreeNode)tree_path.getPath()[path_len-1];

						setSelectionPath(tree_path);

						if( ((XMutableTreeNode)tree_node).isLeaf() )
							bookmarkPopup.show(XBookmarkTree.this,e.getX(),e.getY());
						else
							bookmarkFolderPopup.show(XBookmarkTree.this,e.getX(),e.getY());
					}
				}
			}
		});
	}

	public void dragGestureRecognized(DragGestureEvent e)
	{
	TreePath tree_path = getSelectionPath();

		if( tree_path==null )
			return;

	XMutableTreeNode tree_node = (XMutableTreeNode)tree_path.getPath()[tree_path.getPath().length-1];
	XAbstractBookmark abs_bm = (XAbstractBookmark)tree_node.getUserObject();

		if( tree_node.getUserObject() instanceof XAbstractBookmark )
		{
		XObjectSelection obj_sel = new XObjectSelection( tree_node.getUserObject() );

			e.startDrag(DragSource.DefaultCopyDrop, obj_sel, new DragSourceListener() {
				public void dragDropEnd(DragSourceDropEvent e) {}
				public void dragEnter(DragSourceDragEvent e) {}
				public void dragExit(DragSourceEvent e) {}
				public void dragOver(DragSourceDragEvent e) {}
				public void dropActionChanged(DragSourceDragEvent e) {}
			});
		}
	}

	private XAbstractBookmark getSelectedBookmark()
	{
		return( getBookmark(getSelectionPath()) );
	}

	private XAbstractBookmark getBookmark(TreePath path)
	{
	Object user_obj = ((XMutableTreeNode)path.getLastPathComponent()).getUserObject();

		if( user_obj instanceof XAbstractBookmark )
			return( (XAbstractBookmark)user_obj );
		else
			return null;
	}

	public String getToolTipText(MouseEvent ev)
	{
		if( ev==null )
			return null;

	TreePath path = getPathForLocation(ev.getX(), ev.getY());

		if( path!=null )
		{
		XAbstractBookmark bookmark = getBookmark(path);

			if( bookmark instanceof XBookmark )
				return ((XBookmark)bookmark).getHRef();
			else if( bookmark instanceof XBookmarkFolder )
				return ((XBookmarkFolder)bookmark).getTitle();
		}
		return null;
	}

	private void newBookmark()
	{
	TreePath tree_path = getSelectionPath();
	XMutableTreeNode tree_node = (XMutableTreeNode)tree_path.getPath()[tree_path.getPath().length-1];
	XBookmark bm = new XBookmark(XProjectConstants.NEW_BOOKMARK_HREF);
	XBookmarkFolder parent = null;

		bm.setTitle(XProjectConstants.NEW_BOOKMARK_TITLE);
		bm.setDescription(XProjectConstants.NEW_BOOKMARK_DESCRIPTION);

		if( !tree_node.isLeaf() )
			parent = (XBookmarkFolder)tree_node.getUserObject();

		XBookmarkPropertiesLayout.getInstance(bm, parent).setVisible(true);
	}
/*
	private class XCellEditor extends DefaultCellEditor implements CellEditorListener
	{
		public XCellEditor()
		{
			super( new JTextField() );

			setClickCountToStart(1);
			addCellEditorListener(this);
		}

		public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row)
		{
			if( value instanceof XMutableTreeNode )
			{
			XMutableTreeNode node = (XMutableTreeNode)value;
			Object user_obj = node.getUserObject();

				if( user_obj instanceof XAbstractBookmark )
				{
					bookmarkBeingEdited = (XAbstractBookmark)user_obj;
					return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
				}
			}

			bookmarkBeingEdited = null;
			return null;
		}

	 	public Object getCellEditorValue()
	 	{
			return bookmarkBeingEdited;
		}

		public void editingCanceled(ChangeEvent e)
		{
		}

		public void editingStopped(ChangeEvent e)
		{
			if( bookmarkBeingEdited!=null )
			{
			CellEditor editor = (CellEditor)e.getSource();

				bookmarkBeingEdited.setTitle( (String)editor.getCellEditorValue() );
			}
		}

      // Attributes:
		private XAbstractBookmark bookmarkBeingEdited = null;
	}
*/
	private class XTreeCellRenderer extends DefaultTreeCellRenderer
	{
		public Icon getOpenIcon()
		{
			if( currentBookmark==null )
				return super.getOpenIcon();
			else
				return( ((XBookmarkFolder)currentBookmark).isPersonalFolder() ? ICON_PERSONAL_OPEN_BM_FOLDER : ICON_OPEN_BM_FOLDER);
		}

		public Icon getClosedIcon()
		{
			if( currentBookmark==null )
				return super.getOpenIcon();
			else
				return( ((XBookmarkFolder)currentBookmark).isPersonalFolder() ? ICON_PERSONAL_CLOSED_BM_FOLDER : ICON_CLOSED_BM_FOLDER);
		}

		public Icon getLeafIcon()
		{
			if( currentBookmark==null )
				return super.getLeafIcon();
			else
				return ICON_BM;
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			if( value instanceof XMutableTreeNode )
			{
			XMutableTreeNode node = (XMutableTreeNode)value;
			Object user_obj = node.getUserObject();

				if( user_obj instanceof XAbstractBookmark )
					currentBookmark = (XAbstractBookmark)user_obj;
				else
					currentBookmark = null;
			}

			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}

	// Attributes:
		private XAbstractBookmark currentBookmark = null;

		private final ImageIcon ICON_BM = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTree.this, "image.Bookmark");
		private final ImageIcon ICON_OPEN_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTree.this, "image.OpenBookmarkFolder");
		private final ImageIcon ICON_PERSONAL_OPEN_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTree.this, "image.PersonalOpenBookmarkFolder");
		private final ImageIcon ICON_CLOSED_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTree.this, "image.ClosedBookmarkFolder");
		private final ImageIcon ICON_PERSONAL_CLOSED_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTree.this, "image.PersonalClosedBookmarkFolder");
	}

	private class XTreeModel extends DefaultTreeModel implements XBookmarkFolderListener
	{
		public XTreeModel(TreeNode root)
		{
			super(root);
			XBookmarkManager.getInstance().addBookmarkFolderListener(this);
		}

		public void bookmarkAdded(XAbstractBookmark new_abs_bm, XBookmarkFolder parent)
		{
		XMutableTreeNode parent_node = getBookmarkNode(parent,(XMutableTreeNode)getRoot());

			if( parent_node==null )
				return;

		Enumeration enum = parent_node.children();
		XAbstractBookmark abs_bm = null;
		int index = 0;

			while( enum.hasMoreElements() )
			{
				abs_bm = (XAbstractBookmark)((XMutableTreeNode)enum.nextElement()).getUserObject();

				if( comparator.compare(new_abs_bm, abs_bm)<0 )
					break;

				index++;
			}

			insertNodeInto(new XMutableTreeNode(new_abs_bm), parent_node, index);

			if( new_abs_bm instanceof XBookmarkFolder )
			{
			XBookmarkFolder folder = (XBookmarkFolder)new_abs_bm;
			Iterator it = folder.getBookmarks();

				while( it.hasNext() )
					bookmarkAdded( (XAbstractBookmark)it.next(), folder);
			}
		}

		public void bookmarkRemoved(XAbstractBookmark abs_bm, XBookmarkFolder parent)
		{
		XMutableTreeNode node = getBookmarkNode(abs_bm,(XMutableTreeNode)getRoot());

			node.removingFromParent();
			removeNodeFromParent(node);
		}

		public void personalFolderChanged(XBookmarkFolder old_folder, XBookmarkFolder new_folder)
		{
			XBookmarkTree.this.repaint();
		}

		public void clearBookmarks()
		{
			// ????
			/*
		XMutableTreeNode root = (XMutableTreeNode)getRoot();
		Enumeration enum = root.children();

			while( enum.hasMoreElements() )
				removeNodeFromParent( (XMutableTreeNode)enum.nextElement() );
				*/
		}

		private XMutableTreeNode getBookmarkNode(XAbstractBookmark abs_bm, XMutableTreeNode base_node)
		{
			if( abs_bm==null )
				return( base_node );
			else
			{
			Enumeration enum = base_node.breadthFirstEnumeration();
			XMutableTreeNode node;

				while( enum.hasMoreElements() )
				{
					node = (XMutableTreeNode)enum.nextElement();
					if( node.getUserObject()==abs_bm )
						return node;
				}

				return null;
			}
		}

	// Attributes:
		private XBookmarkComparator comparator = new XBookmarkComparator(0, true);
	}

	private class XMutableTreeNode extends DefaultMutableTreeNode implements PropertyChangeListener
	{
		public XMutableTreeNode(XAbstractBookmark abs_bm)
		{
			super(abs_bm);
			abs_bm.addPropertyChangeListener(this);
		}

		public void propertyChange(PropertyChangeEvent evt)
		{
			((XTreeModel)getModel()).nodeStructureChanged(this);
		}

		public void handleDoubleClick()
		{
			if( getUserObject() instanceof XBookmark )
				((XBookmark)getUserObject()).open();
		}

	    public boolean isLeaf()
	    {
			return( getUserObject() instanceof XBookmark );
		}

		public void removingFromParent()
		{
			((XAbstractBookmark)getUserObject()).removePropertyChangeListener(this);

		Enumeration enum = children();

			while( enum.hasMoreElements() )
				((XMutableTreeNode)enum.nextElement()).removingFromParent();
		}
	}

    private class AddToBookmarksAction extends XAction
    {
        public AddToBookmarksAction()
        {
            super(XBookmarkTree.this, "AddBookmark", KeyStroke.getKeyStroke(KeyEvent.VK_D,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		XDocument active_doc = XBrowser.getBrowser().getActiveDocument();

			if( active_doc!=null )
			{
			XBookmark bm = new XBookmark(active_doc.getPageCompletePath());
			XAbstractBookmark sel_abs_bm = getSelectedBookmark();

				bm.setTitle(active_doc.getPageTitle());
				((XBookmarkFolder)sel_abs_bm).addBookmark(bm);
			}
			else
				newBookmark();
        }
    }

    private class OpenAllBookmarksAction extends XAction
    {
        public OpenAllBookmarksAction()
        {
            super(XBookmarkTree.this, "OpenAllBookmarks", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			((XBookmarkFolder)getSelectedBookmark()).open();
        }
    }

    private class NewBookmarkAction extends XAction
    {
        public NewBookmarkAction()
        {
            super(XBookmarkTree.this, "NewBookmark", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			newBookmark();
        }
    }

    private class NewBookmarkFolderAction extends XAction
    {
        public NewBookmarkFolderAction()
        {
            super(XBookmarkTree.this, "NewBookmarkFolder", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XBookmarkFolder bm_folder = new XBookmarkFolder();

			bm_folder.setTitle(XProjectConstants.NEW_BOOKMARK_FOLDER_TITLE);
			bm_folder.setDescription(XProjectConstants.NEW_BOOKMARK_FOLDER_DESCRIPTION);

			XBookmarkPropertiesLayout.getInstance(bm_folder, (XBookmarkFolder)getSelectedBookmark()).setVisible(true);
        }
    }

    private class DeleteAction extends XAction
    {
        public DeleteAction()
        {
            super(XBookmarkTree.this, "Delete", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XAbstractBookmark sel_abs_bm = getSelectedBookmark();

			if( sel_abs_bm.getParent()!=null )
				sel_abs_bm.getParent().removeBookmark(sel_abs_bm);
			else
				((XBookmarkFolder)sel_abs_bm).removeAllBookmarks();
        }
    }

    private class PropertiesAction extends XAction
    {
        public PropertiesAction()
        {
            super(XBookmarkTree.this, "Properties", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XAbstractBookmark sel_abs_bm = getSelectedBookmark();

			if( sel_abs_bm.getParent()!=null )
				XBookmarkPropertiesLayout.getInstance(sel_abs_bm, null).setVisible(true);
        }
    }

    private class OpenBookmarkAction extends XAction
    {
        public OpenBookmarkAction()
        {
            super(XBookmarkTree.this, "OpenBookmark", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XAbstractBookmark sel_abs_bm = getSelectedBookmark();

			if( sel_abs_bm instanceof XBookmark )
				((XBookmark)sel_abs_bm).openInSamePage();
        }
    }

    private class OpenBookmarkInNewPageAction extends XAction
    {
        public OpenBookmarkInNewPageAction()
        {
            super(XBookmarkTree.this, "OpenBookmarkInNewPage", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XAbstractBookmark sel_abs_bm = getSelectedBookmark();

			if( sel_abs_bm instanceof XBookmark )
				((XBookmark)sel_abs_bm).open();
        }
    }

    private class CopyAction extends XAction
    {
        public CopyAction()
        {
            super(XBookmarkTree.this, "Copy", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			getSelectedBookmark().copy();
        }
    }

    private class SetAsPersonalFolderAction extends XAction
    {
        public SetAsPersonalFolderAction()
        {
            super(XBookmarkTree.this, "SetAsPersonalFolder", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XAbstractBookmark sel_abs_bm = getSelectedBookmark();

			if( sel_abs_bm.getParent()!=null )
				((XBookmarkFolder)sel_abs_bm).setPersonalFolder(true);
        }
    }

    private class ImportBookmarksAction extends XAction
    {
        public ImportBookmarksAction()
        {
            super(XBookmarkTree.this, "ImportBookmarks", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XImportExportBookmarksLayout.getInstance(XImportExportBookmarksLayout.IMPORT_BOOKMARKS, (XBookmarkFolder)getSelectedBookmark()).setVisible(true);
        }
    }

    private class ExportBookmarksAction extends XAction
    {
        public ExportBookmarksAction()
        {
            super(XBookmarkTree.this, "ExportBookmarks", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XImportExportBookmarksLayout.getInstance(XImportExportBookmarksLayout.EXPORT_BOOKMARKS, (XBookmarkFolder)getSelectedBookmark()).setVisible(true);
        }
    }

// Attributes:
	private JPopupMenu bookmarkPopup = new XPopupMenu();
	private JPopupMenu bookmarkFolderPopup = new XPopupMenu();
}
