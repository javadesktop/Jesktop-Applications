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
import xbrowser.history.*;
import xbrowser.history.event.*;

public class XHistoryTable extends JTable
{
	public XHistoryTable(XHistoryManager history_manager)
	{
		init();
		history_manager.addHistoryListener(model);
	}

	public XHistoryTable(XHistoryFinder history_finder)
	{
		init();
		history_finder.addHistoryListener(model);
	}

	private void init()
	{
		customizeTable();
		registerListeners();

		goToPageAction.setEnabled(false);
		addToBookmarksAction.setEnabled(false);
		copyAction.setEnabled(false);
		deleteAction.setEnabled(false);

	    historyPopup.add( XComponentBuilder.getInstance().buildMenuItem(goToPageAction) );
		historyPopup.addSeparator();
	    historyPopup.add( XComponentBuilder.getInstance().buildMenuItem(addToBookmarksAction) );
		historyPopup.addSeparator();
	    historyPopup.add( XComponentBuilder.getInstance().buildMenuItem(copyAction) );
	    historyPopup.add( XComponentBuilder.getInstance().buildMenuItem(deleteAction) );

		add(historyPopup);
	}

	private void customizeTable()
	{
	XHistoryCellRenderer cell_renderer = new XHistoryCellRenderer();
	XHistoryHeaderRenderer header_renderer = new XHistoryHeaderRenderer();
	TableColumn col;

		setModel(model);

		for( int i=0; i<getColumnCount(); i++ )
		{
			col = getColumnModel().getColumn(i);
			col.setPreferredWidth(columnWidths[i]);
			col.setCellRenderer(cell_renderer);
			col.setHeaderRenderer(header_renderer);
		}

		setRowHeight(20);
	}

	// JTable is not update its header properly!! maybe a bug!!
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
						model.getHistoryData(index).open();
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

					historyPopup.show(XHistoryTable.this, e.getX(), e.getY());
				}
			}
		});

		getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e)
			{
				goToPageAction.setEnabled( getSelectedRow()!=-1 );
				addToBookmarksAction.setEnabled( getSelectedRow()!=-1 );
				copyAction.setEnabled( getSelectedRow()!=-1 );
				deleteAction.setEnabled( getSelectedRow()!=-1 );
			}
		});
/*
		model.addTableModelListener( new TableModelListener() {
			public void tableChanged(TableModelEvent e)
			{
				goToPageAction.setEnabled( getSelectedRow()!=-1 );
				addToBookmarksAction.setEnabled( getSelectedRow()!=-1 );
				copyAction.setEnabled( getSelectedRow()!=-1 );
				deleteAction.setEnabled( getSelectedRow()!=-1 );
			}
		});*/
	}

	private class XHistoryCellRenderer extends DefaultTableCellRenderer//JLabel implements TableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		String label = "";

			if( value instanceof Date )
			{
				label = dateLabel((Date) value);
				setHorizontalAlignment( JLabel.CENTER );
			}
			else if (value instanceof String)
			{
				label = (String) value;
				setHorizontalAlignment( JLabel.LEFT );
			}
			else if (value instanceof Integer)
			{
				label = "" + value;
				setHorizontalAlignment( JLabel.CENTER );
			}

			setText(label);

			if( label.equals("") )
				setToolTipText(null);
			else
				setToolTipText(label);

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
				label = XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "MinutesAgo", ""+minutes_ago);
			else if (minutes_ago < MINUTES_PER_DAY)
				label = XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "HoursAgo", ""+(minutes_ago / 60));
			else if (minutes_ago < MINUTES_PER_WEEK)
				label = XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "DaysAgo", ""+(minutes_ago / MINUTES_PER_DAY));
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

	private class XHistoryHeaderRenderer extends JLabel implements TableCellRenderer
	{
		public XHistoryHeaderRenderer()
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
				setIcon( sortAsc ? ascSortedIcon : descSortedIcon );
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
		private ImageIcon ascSortedIcon = XComponentBuilder.getInstance().buildImageIcon(XHistoryTable.this, "image.AscendingSorted");
		private ImageIcon descSortedIcon = XComponentBuilder.getInstance().buildImageIcon(XHistoryTable.this, "image.DescendingSorted");

	    private Border noFocusBorder = BorderFactory.createRaisedBevelBorder();
	}

	private class XHistoryTableModel extends AbstractTableModel implements XHistoryListener, PropertyChangeListener
	{
		public XHistoryTableModel()
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
				XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "Column.Title"),
				XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "Column.Location"),
				XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "Column.FirstVisited"),
				XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "Column.LastVisited"),
				XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "Column.Expiration"),
				XComponentBuilder.getInstance().getProperty(XHistoryTable.this, "Column.VisitCount")
			};
		}

		private void sort()
		{
			Collections.sort(data, new XHistoryComparator(sortedIndex, sortAsc));
			tableChanged( new TableModelEvent(XHistoryTableModel.this) );
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
		XHistoryData record = (XHistoryData) data.get(row);

			if( col==0 )
				return record.getTitle();
			else if( col==1 )
				return record.getLocation();
			else if( col==2 )
				return record.getFirstVisited();
			else if( col==3 )
				return record.getLastVisited();
			else if( col==4 )
				return record.getExpiration();
			else if( col==5 )
				return new Integer(record.getVisitCount());
			else
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

		public void historyAdded(XHistoryData history_data)
		{
			data.add(0, history_data);

			if( sortedIndex<0 )
				fireTableRowsInserted(0, 0);
			else
				sort();

			history_data.addPropertyChangeListener(this);
		}

		public void historyRemoved(XHistoryData history_data)
		{
		int index = data.indexOf(history_data);

			if( index!=-1 )
			{
				data.remove(index);
				fireTableRowsDeleted(index, index);
				history_data.removePropertyChangeListener(this);
			}
		}

		public void clearHistory()
		{
		int size = data.size();

			if( size>0 )
			{
				for( int i=0; i<size; i++ )
					((XHistoryData)data.get(i)).removePropertyChangeListener(this);

				data.clear();
				fireTableRowsDeleted(0, size-1);
			}
		}

		public XHistoryData getHistoryData(int row_index)
		{
			return (XHistoryData) data.get(row_index);
		}

	// Attributes:
		private String[] columnNames;
		private LinkedList data = new LinkedList();
	}

    private class GoToPageAction extends XAction
    {
        public GoToPageAction()
        {
            super(XHistoryTable.this, "GoToPage", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_rows[] = getSelectedRows();

			for( int i=0; i<sel_rows.length; i++ )
				model.getHistoryData(sel_rows[i]).open();
        }
    }

    private class AddToBookmarksAction extends XAction
    {
        public AddToBookmarksAction()
        {
            super(XHistoryTable.this, "AddToBookmarks" ,null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_row = getSelectedRow();

			if( sel_row!=-1 )
			{
			XHistoryData history_data = model.getHistoryData(sel_row);
			XBookmark bm = new XBookmark(history_data.getLocation());

				bm.setTitle(history_data.getTitle());
				XBookmarkPropertiesLayout.getInstance(bm,null).setVisible(true);
			}
        }
    }

    private class CopyAction extends XAction
    {
        public CopyAction()
        {
            super(XHistoryTable.this, "Copy" ,null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_row = getSelectedRow();

			if( sel_row!=-1 )
				model.getHistoryData(sel_row).copy();
        }
    }

    private class DeleteAction extends XAction
    {
        public DeleteAction()
        {
            super(XHistoryTable.this, "Delete" ,null);
        }

        public void actionPerformed(ActionEvent e)
        {
		int sel_rows[] = getSelectedRows();
		LinkedList list = new LinkedList();

			for( int i=0; i<sel_rows.length; i++ )
				list.add( model.getHistoryData(sel_rows[i]) );

		Iterator it = list.iterator();

			while( it.hasNext() )
				XHistoryManager.getInstance().removeHistoryData( (XHistoryData)it.next() );
        }
    }

// Attributes:
	private XHistoryTableModel model = new XHistoryTableModel();
	private JPopupMenu historyPopup = new XPopupMenu();
	private final int[]	columnWidths = { 200, 200, 80, 80, 80, 80 };

	private int sortedIndex = -1;
	private boolean sortAsc = true;

	public final XAction goToPageAction = new GoToPageAction();
	public final XAction addToBookmarksAction = new AddToBookmarksAction();
	public final XAction copyAction = new CopyAction();
	public final XAction deleteAction = new DeleteAction();
}
