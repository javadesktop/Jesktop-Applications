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
import javax.swing.table.*;
import java.util.*;
import java.text.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;

import xbrowser.screen.*;
import xbrowser.bookmark.*;
import xbrowser.bookmark.event.*;

public class XBookmarkTable extends JTable
{
	public XBookmarkTable(XBookmarkFinder bookmark_finder)
	{
		customizeTable();
		registerListeners();

	    bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new GoToPageAction()) );
		bookmarkPopup.addSeparator();
	    bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new CopyAction()) );
	    bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new DeleteAction()) );
		bookmarkPopup.addSeparator();
	    bookmarkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new PropertiesAction()) );

		add(bookmarkPopup);

		bookmark_finder.addBookmarkListener(model);
	}

	private void customizeTable()
	{
	XBookmarkCellRenderer cell_renderer = new XBookmarkCellRenderer();
	XBookmarkHeaderRenderer header_renderer = new XBookmarkHeaderRenderer();
	TableColumn col;

		setModel(model);

		for( int i=0; i<getColumnCount(); i++ )
		{
			col = getColumnModel().getColumn(i);
			col.setPreferredWidth(columnWidths[i]);
			col.setCellRenderer(cell_renderer);
			col.setHeaderRenderer(header_renderer);
		}

		setRowHeight(23);
	}

	// JTable does not update its header properly!! maybe a bug!!
	public void updateUI()
	{
		super.updateUI();
		getTableHeader().updateUI();
	}

	private void registerListeners()
	{
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				showPopup(e);
			}

			public void mousePressed(MouseEvent e)
			{
				showPopup(e);
			}

			public void mouseClicked(MouseEvent e)
			{
				if( e.getClickCount()==2 )
				{
				int index = rowAtPoint( e.getPoint() );

					if( index!=-1 )
						model.getBookmark(index).open();
				}

				showPopup(e);
			}

			private void showPopup(MouseEvent e)
			{
				if( e.isPopupTrigger() )
				{
				int index = rowAtPoint( e.getPoint() );

					if( !isRowSelected(index) )
						getSelectionModel().setSelectionInterval(index,index);

					bookmarkPopup.show(XBookmarkTable.this, e.getX(), e.getY());
				}
			}
		});
	}

	private class XBookmarkCellRenderer extends DefaultTableCellRenderer//JLabel implements TableCellRenderer
	{
		public XBookmarkCellRenderer()
		{
			setHorizontalAlignment( JLabel.LEFT );
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		String label = "";
		ImageIcon icon = null;

			if( value instanceof Date )
			{
				label = dateLabel((Date)value);
				setHorizontalAlignment( JLabel.CENTER );
			}
			else if( value instanceof String )
			{
				label = (String)value;
				setHorizontalAlignment( JLabel.LEFT );
			}
			else if( value instanceof ImageIcon )
			{
				icon = (ImageIcon)value;
				setHorizontalAlignment( JLabel.CENTER );
			}

			setText(label);

			if( label.equals("") )
				setToolTipText(null);
			else
				setToolTipText(label);

			setIcon(icon);

			/*if( isSelected )
			{
			   setOpaque(true);
			   setBackground(SystemColor.textHighlight);
			   setForeground(SystemColor.textHighlightText);
			}
			else
			{
				setOpaque(false);
				setBackground(SystemColor.text);
				setForeground(SystemColor.textText);
			}*/

			return this;
		}

		private String dateLabel(Date date)
		{
		Date now = new Date();
		String label;
		long minutes_ago = ((now.getTime() - date.getTime()) / MILLISECONDS_PER_MINUTE);

			if( date.after(now) )
				label = df.format(date);
			else if (minutes_ago < 60)
				label = XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "MinutesAgo", ""+minutes_ago);
			else if (minutes_ago < MINUTES_PER_DAY)
				label = XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "HoursAgo", ""+(minutes_ago / 60));
			else if (minutes_ago < MINUTES_PER_WEEK)
				label = XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "DaysAgo", ""+(minutes_ago / MINUTES_PER_DAY));
			else
				label = df.format(date);

			return label;
		}

	// Attributes:
		private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

		private final static long MILLISECONDS_PER_MINUTE = 60000;
		private final static long MINUTES_PER_DAY = 1440;
		private final static long MINUTES_PER_WEEK = 10080;
	}

	private class XBookmarkHeaderRenderer extends JLabel implements TableCellRenderer
	{
		public XBookmarkHeaderRenderer()
		{
			setHorizontalAlignment( JLabel.CENTER );
			setHorizontalTextPosition( JLabel.LEFT );
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if( value!=null )
				setText( (String)value );
			else
				setText("");

    		if( getColumnModel().getColumn(column).getModelIndex()==sortedIndex )
				setIcon( sortAsc ? ICON_ASC_SORTED : ICON_DESC_SORTED );
			else
				setIcon(null);

			if( isSelected )
			{
				super.setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			}
			else
			{
				super.setForeground(table.getForeground());
				super.setBackground(table.getBackground());
			}

			setFont(table.getFont());

			if( hasFocus )
			{
				setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );

				if( table.isCellEditable(row, column) )
				{
					super.setForeground( UIManager.getColor("Table.focusCellForeground") );
					super.setBackground( UIManager.getColor("Table.focusCellBackground") );
				}
			}
			else
				setBorder(noFocusBorder);

		Color back = getBackground();
		boolean color_match = (back != null) && ( back.equals(table.getBackground()) ) && table.isOpaque();

			setOpaque(!color_match);

			return this;
		}

	// Attributes:
		private final ImageIcon ICON_ASC_SORTED = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTable.this, "image.AscendingSorted");
		private final ImageIcon ICON_DESC_SORTED = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTable.this, "image.DescendingSorted");

	    private Border noFocusBorder = BorderFactory.createRaisedBevelBorder();
	}

	private class XBookmarkTableModel extends AbstractTableModel implements XBookmarkFolderListener, PropertyChangeListener
	{
		public XBookmarkTableModel()
		{
			getTableHeader().addMouseListener( new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
				TableColumnModel colModel = getColumnModel();
				int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
				int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

					if( modelIndex<0 )
						return;

					if( sortedIndex==modelIndex )
						sortAsc = !sortAsc;
					else
						sortedIndex = modelIndex;

					getTableHeader().repaint();
					sort();
				}
			});

			columnNames = new String[] {
				"*" ,
				XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "Column.Title"),
				XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "Column.Location"),
				XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "Column.Description"),
				XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "Column.CreationDate"),
				XComponentBuilder.getInstance().getProperty(XBookmarkTable.this, "Column.ModificationDate"),
			};
		}

		private void sort()
		{
			Collections.sort(data, new XBookmarkComparator(sortedIndex, sortAsc));
			tableChanged( new TableModelEvent(XBookmarkTableModel.this) );
			repaint();
		}

		public Class getColumnClass(int c)
		{
			return getValueAt(0, c).getClass();
		}

		public int getColumnCount()
		{
			return columnNames.length;
		}

		public String getColumnName(int col)
		{
	    	return columnNames[col];
		}

		public int getRowCount()
		{
			return data.size();
		}

		public Object getValueAt(int row, int col)
		{
		XAbstractBookmark abs_bm = (XAbstractBookmark)data.get(row);

			switch( col )
			{
				case 0:
					if( abs_bm instanceof XBookmark )
						return ICON_BM;
					else
						return( ((XBookmarkFolder)abs_bm).isPersonalFolder() ? ICON_PERSONAL_BM_FOLDER : ICON_BM_FOLDER);
				case 1:
					return abs_bm.getTitle();
				case 2:
					return( (abs_bm instanceof XBookmark) ? ((XBookmark)abs_bm).getHRef() : "" );
				case 3:
					return abs_bm.getDescription();
				case 4:
					return abs_bm.getCreationDate();
				case 5:
					return abs_bm.getModificationDate();
			}

			return null;
		}

		public void propertyChange(PropertyChangeEvent evt)
		{
		int index = data.indexOf(evt.getSource());

			if( sortedIndex<0 )
				fireTableRowsUpdated(index, index);
			else
				sort();
		}

		public void bookmarkAdded(XAbstractBookmark bookmark, XBookmarkFolder parent)
		{
			data.add(0, bookmark);

			if( sortedIndex<0 )
				fireTableRowsInserted(0, 0);
			else
				sort();

			bookmark.addPropertyChangeListener(this);
		}

		public void bookmarkRemoved(XAbstractBookmark bookmark, XBookmarkFolder parent)
		{
		int index = data.indexOf(bookmark);

			if( index!=-1 )
			{
				data.remove(index);
				fireTableRowsDeleted(index, index);
				bookmark.removePropertyChangeListener(this);
			}

			if( bookmark instanceof XBookmarkFolder )
			{
			XBookmarkFolder folder = (XBookmarkFolder)bookmark;
			Iterator it = folder.getBookmarks();

				while( it.hasNext() )
					bookmarkRemoved( (XAbstractBookmark)it.next(), folder);
			}
		}

		public void personalFolderChanged(XBookmarkFolder old_folder, XBookmarkFolder new_folder)
		{
		int index = data.indexOf(old_folder);

			fireTableRowsUpdated(index, index);

			index = data.indexOf(new_folder);
			fireTableRowsUpdated(index, index);
		}

		public void clearBookmarks()
		{
		int size = data.size();

			if( size>0 )
			{
				for( int i=0; i<size; i++ )
					((XAbstractBookmark)data.get(i)).removePropertyChangeListener(this);

				data.clear();
				fireTableRowsDeleted(0, size-1);
			}
		}

		public XAbstractBookmark getBookmark(int row_index)
		{
			return (XAbstractBookmark) data.get(row_index);
		}

	// Attributes:
		private LinkedList data = new LinkedList();
		private String[] columnNames;

		private final ImageIcon ICON_BM = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTable.this, "image.Bookmark");
		private final ImageIcon ICON_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTable.this, "image.BookmarkFolder");
		private final ImageIcon ICON_PERSONAL_BM_FOLDER = XComponentBuilder.getInstance().buildImageIcon(XBookmarkTable.this, "image.PersonalBookmarkFolder");
	}

    private class GoToPageAction extends XAction
    {
        public GoToPageAction()
        {
            super(XBookmarkTable.this, "GoToPage", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_rows[] = getSelectedRows();

			for( int i=0; i<sel_rows.length; i++ )
				model.getBookmark(sel_rows[i]).open();
        }
    }

    private class CopyAction extends XAction
    {
        public CopyAction()
        {
            super(XBookmarkTable.this, "Copy", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_row = getSelectedRow();

			if( sel_row!=-1 )
				model.getBookmark(sel_row).copy();
        }
    }

    private class DeleteAction extends XAction
    {
        public DeleteAction()
        {
            super(XBookmarkTable.this, "Delete", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_rows[] = getSelectedRows();
		LinkedList list = new LinkedList();
		XAbstractBookmark abs_bm = null;

			for( int i=0; i<sel_rows.length; i++ )
				list.add( model.getBookmark(sel_rows[i]) );

		Iterator it = list.iterator();

			while( it.hasNext() )
			{
				abs_bm = (XAbstractBookmark)it.next();
				abs_bm.getParent().removeBookmark(abs_bm);
			}
        }
    }

    private class PropertiesAction extends XAction
    {
        public PropertiesAction()
        {
            super(XBookmarkTable.this, "Properties", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_row = getSelectedRow();

			if( sel_row!=-1 )
				XBookmarkPropertiesLayout.getInstance(model.getBookmark(sel_row), null).setVisible(true);
        }
    }

// Attributes:
	private XBookmarkTableModel model = new XBookmarkTableModel();
	private JPopupMenu bookmarkPopup = new XPopupMenu();
	private final int[]	columnWidths = { 30, 200, 200, 200, 120, 120 };

	private int sortedIndex = -1;
	private boolean sortAsc = true;
}
