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
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;
import java.beans.*;
import java.util.*;
import java.awt.*;
import javax.swing.plaf.*;
import javax.swing.tree.*;

import xbrowser.bookmark.*;
import xbrowser.bookmark.event.*;

public class XBookmarkFolderComboBox extends JComboBox
{
	public XBookmarkFolderComboBox()
	{
		super();
		setModel(new XTreeToListModel(new DefaultTreeModel(new XMutableTreeNode(XBookmarkManager.getInstance()))));
		setRenderer(new XListEntryRenderer());
	}

	public XBookmarkFolder getSelectedBookmark()
	{
		return ((XTreeToListModel)getModel()).getSelectedBookmark();
	}

	private class XMutableTreeNode extends DefaultMutableTreeNode implements PropertyChangeListener
	{
		public XMutableTreeNode(XBookmarkFolder bm_folder)
		{
			super(bm_folder);
			bm_folder.addPropertyChangeListener(this);
		}

	    public boolean isLeaf()
	    {
			return false;
		}

		public void propertyChange(PropertyChangeEvent evt)
		{
			XBookmarkFolderComboBox.this.repaint();
		}

		public void removingFromParent()
		{
			((XBookmarkFolder)getUserObject()).removePropertyChangeListener(this);

		Enumeration enum = children();

			while( enum.hasMoreElements() )
				((XMutableTreeNode)enum.nextElement()).removingFromParent();
		}
	}

	private class XTreeToListModel extends AbstractListModel implements ComboBoxModel, TreeModelListener, XBookmarkFolderListener
	{
		public XTreeToListModel(TreeModel aTreeModel)
		{
			source = aTreeModel;
			aTreeModel.addTreeModelListener(this);
			setRenderer(new XListEntryRenderer());

			XBookmarkManager.getInstance().addBookmarkFolderListener(this);
		}

		public void bookmarkAdded(XAbstractBookmark new_abs_bm, XBookmarkFolder parent)
		{
			if( !(new_abs_bm instanceof XBookmarkFolder) )
				return;

		XMutableTreeNode parent_node = getBookmarkNode(parent,(XMutableTreeNode)source.getRoot());

			if( parent_node==null )
				return;

		Enumeration enum = parent_node.children();
		XAbstractBookmark abs_bm = null;
		int index = 0;

			while( enum.hasMoreElements() )
			{
				abs_bm = (XAbstractBookmark)((DefaultMutableTreeNode)enum.nextElement()).getUserObject();

				if( comparator.compare(new_abs_bm, abs_bm)<0 )
					break;

				index++;
			}

			((DefaultTreeModel)source).insertNodeInto(new XMutableTreeNode((XBookmarkFolder)new_abs_bm), parent_node, index);

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
			if( !(abs_bm instanceof XBookmarkFolder) )
				return;

		XMutableTreeNode node = getBookmarkNode((XBookmarkFolder)abs_bm,(XMutableTreeNode)source.getRoot());

			node.removingFromParent();
			((DefaultTreeModel)source).removeNodeFromParent(node);
		}

		public void personalFolderChanged(XBookmarkFolder old_folder, XBookmarkFolder new_folder)
		{
			XBookmarkFolderComboBox.this.repaint();
		}

		public void clearBookmarks()
		{
			///???!!!
		}

		private XMutableTreeNode getBookmarkNode(XBookmarkFolder bm_folder, XMutableTreeNode base_node)
		{
			if( bm_folder==null )
				return( base_node );
			else
			{
			Enumeration enum = base_node.breadthFirstEnumeration();
			XMutableTreeNode node;

				while( enum.hasMoreElements() )
				{
					node = (XMutableTreeNode)enum.nextElement();
					if( node.getUserObject()==bm_folder )
						return node;
				}

				return null;
			}
		}

		public void setSelectedItem(Object anObject)
		{
			if( anObject==null )
			{
				if( getSize()>0 )
					currentValue = cache.elementAt(0);
				else
					currentValue = null;

				fireContentsChanged(this, -1, -1);
			}
			else if( anObject instanceof XListEntry )
			{
				currentValue = anObject;
				fireContentsChanged(this, -1, -1);
			}
			else
			{
			int size = getSize();

				for( int i=0; i<size; i++ )
				{
					if( ((DefaultMutableTreeNode)((XListEntry)cache.elementAt(i)).object()).getUserObject()==anObject )
					{
						currentValue = cache.elementAt(i);
						fireContentsChanged(this, -1, -1);
						return;
					}
				}
			}
		}

		public Object getSelectedItem()
		{
			return currentValue;
		}

		public XBookmarkFolder getSelectedBookmark()
		{
			if( currentValue!=null )
				return( (XBookmarkFolder)((DefaultMutableTreeNode)((XListEntry)currentValue).object()).getUserObject() );
			else
				return null;
		}

		public int getSize()
		{
			validate();
			return cache.size();
		}

		public Object getElementAt(int index)
		{
			return cache.elementAt(index);
		}

		public void treeNodesChanged(TreeModelEvent e)
		{
			invalid = true;
		}

		public void treeNodesInserted(TreeModelEvent e)
		{
			invalid = true;
		}

		public void treeNodesRemoved(TreeModelEvent e)
		{
			invalid = true;
		}

		public void treeStructureChanged(TreeModelEvent e)
		{
			invalid = true;
		}

		private void validate()
		{
			if (invalid)
			{
				cache = new Vector();
				cacheTree(source.getRoot(), 0);
				if (cache.size() > 0)
					currentValue = cache.elementAt(0);
				invalid = false;
				fireContentsChanged(this, 0, 0);
			}
		}

		private void cacheTree(Object anObject, int level)
		{
			if (source.isLeaf(anObject))
				addListEntry(anObject, level, false);
			else
			{
				int		c = source.getChildCount(anObject);
				int		i;
				Object  child;

				addListEntry(anObject, level, true);
				level++;
				for (i = 0; i < c; i++)
				{
					child = source.getChild(anObject, i);
					cacheTree(child, level);
				}
				level--;
			}
		}

		private void addListEntry(Object anObject, int level, boolean isNode)
		{
			cache.addElement(new XListEntry(anObject, level, isNode));
		}

	// Attributes:
		private TreeModel   source;
		private boolean		invalid = true;
		private Object		currentValue;
		private Vector		cache = new Vector();
	}

	private class XListEntry
	{
		public XListEntry(Object anObject, int aLevel, boolean isNode)
		{
			object = anObject;
			level = aLevel;
			this.isNode = isNode;
		}

		public Object object()
		{
			return object;
		}

		public int level()
		{
			return level;
		}

		public boolean isNode()
		{
			return isNode;
		}

	// Attributes:
		private Object  object;
		private int		level;
		private boolean isNode;
	}

	private class XListEntryRenderer extends JLabel implements ListCellRenderer
	{
		public XListEntryRenderer()
		{
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList listbox, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
		XListEntry   listEntry = (XListEntry) value;

			if( listEntry!=null )
			{
				setText(listEntry.object().toString());
				if( listEntry.isNode() )
				{
				XBookmarkFolder bm_folder = (XBookmarkFolder)((DefaultMutableTreeNode)listEntry.object()).getUserObject();

					if( isSelected )
						setIcon( bm_folder.isPersonalFolder() ? ICON_PERSONAL_OPEN_BM_FOLDER : ICON_OPEN_BM_FOLDER);
					else
						setIcon( bm_folder.isPersonalFolder() ? ICON_PERSONAL_CLOSED_BM_FOLDER : ICON_CLOSED_BM_FOLDER);
				}
				else
					setIcon(ICON_BM);

				if( UIManager.getLookAndFeel().getName().equals("CDE/Motif") )
				{
					if (index == -1)
						setOpaque(false);
					else
						setOpaque(true);
				}
				else
					setOpaque(true);

				if( index!=-1 )
					setBorder(new EmptyBorder(0, OFFSET * listEntry.level(), 0, 0));
				else
					setBorder(emptyBorder);

				if( isSelected )
				{
					setBackground(UIManager.getColor("ComboBox.selectionBackground"));
					setForeground(UIManager.getColor("ComboBox.selectionForeground"));
				}
				else
				{
					setBackground(UIManager.getColor("ComboBox.background"));
					setForeground(UIManager.getColor("ComboBox.foreground"));
				}
			}
			else
				setText("");

			return this;
		}

	// Attributes:
		private final ImageIcon ICON_BM = XComponentBuilder.getInstance().buildImageIcon(XBookmarkFolderComboBox.this, "image.Bookmark");
		private final ImageIcon ICON_OPEN_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkFolderComboBox.this, "image.OpenBookmarkFolder");
		private final ImageIcon ICON_PERSONAL_OPEN_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkFolderComboBox.this, "image.PersonalOpenBookmarkFolder");
		private final ImageIcon ICON_CLOSED_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkFolderComboBox.this, "image.ClosedBookmarkFolder");
		private final ImageIcon ICON_PERSONAL_CLOSED_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkFolderComboBox.this, "image.PersonalClosedBookmarkFolder");
	}

// Attributes:
	private static Border   emptyBorder = new EmptyBorder(0, 0, 0, 0);
	private static final int OFFSET = 16;

	private XBookmarkComparator comparator = new XBookmarkComparator(0, true);
}
