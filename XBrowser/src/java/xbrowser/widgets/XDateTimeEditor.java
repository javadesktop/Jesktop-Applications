/****************************************************************
*              XBrowser  -  eXtended web Browser                *
*                                                               *
*           Copyright (c) 2000-2001  Armond Avanes              *
*     Refer to ReadMe & License files for more information      *
*                                                               *
*                                                               *
*                      By: David M. Karr                        *
*****************************************************************/
package xbrowser.widgets;

import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class XDateTimeEditor extends JPanel
{
	public XDateTimeEditor()
	{
		timeOrDateType = DATETIME;
		lengthStyle = DateFormat.SHORT;
		init();
	}

	public XDateTimeEditor(int type)
	{
		timeOrDateType = type;
		lengthStyle = DateFormat.FULL;
		init();
	}

	public XDateTimeEditor(int type, int length_style)
	{
		timeOrDateType = type;
		lengthStyle = length_style;
		init();
	}

	private void init()
	{
		setLayout(new BorderLayout());
		textField = new JTextField();
		textField.setDocument(new XDateTimeDocument());		// BUG FIX
		spinner = new XSpinner();
		spinner.getIncrementButton().addActionListener(upAction);
		spinner.getDecrementButton().addActionListener(downAction);
		add(textField, BorderLayout.CENTER);
		add(spinner, BorderLayout.EAST);

		textField.addCaretListener( new CaretListener() {
			public void caretUpdate(CaretEvent e)
			{
				setCurField();
			}
		});

		setupKeymap();
		reinit();
	}

	protected class XDateTimeDocument extends PlainDocument
	{
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
		{
			if (settingDateText)
				super.insertString(offset, str, a);
		}
	}		// BUG FIX

	public int getTimeOrDateType()
	{
		return timeOrDateType;
	}

	public void setTimeOrDateType(int timeOrDateType)
	{
		timeOrDateType = timeOrDateType;
		reinit();
	}

	public int getLengthStyle()
	{
		return lengthStyle;
	}

	public void setLengthStyle(int lengthStyle)
	{
		lengthStyle = lengthStyle;
		reinit();
	}

	public Date getDate()
	{
		return (lastDate);
	}

	// public void setDate(Date date)
	// {
	// lastDate = date;
	// calendar.setTime(lastDate);
	// textField.setText(dateFormat.format(lastDate));
	// getFieldPositions();
	// }

	public void setDate(Date date)
	{
		lastDate = date;
		calendar.setTime(lastDate);
		settingDateText = true;
		textField.setText(dateFormat.format(lastDate));
		settingDateText = false;
		getFieldPositions();
	}		// BUG FIX

	private int getFieldBeginIndex(int fieldNum)
	{
		for (Iterator iter = fieldPositions.iterator(); iter.hasNext(); )
		{
			FieldPosition   fieldPos = (FieldPosition) iter.next();

			if (fieldPos.getField() == fieldNum)
				return fieldPos.getBeginIndex();
		}

		return -1;
	}

	private FieldPosition getFieldPosition(int fieldNum)
	{
		for (Iterator iter = fieldPositions.iterator(); iter.hasNext(); )
		{
			FieldPosition   fieldPosition = (FieldPosition) iter.next();

			if (fieldPosition.getField() == fieldNum)
				return fieldPosition;
		}

		return null;
	}

	private void reinit()
	{
		setupFormat();
		setDate(lastDate);
		textField.setCaretPosition(0);
		setCurField();
		repaint();
	}

	protected void setupFormat()
	{
		switch( timeOrDateType )
		{
			case TIME:
				dateFormat = DateFormat.getTimeInstance(lengthStyle);
				break;
			case DATE:
				dateFormat = DateFormat.getDateInstance(lengthStyle);
				break;
			case DATETIME:
				dateFormat = DateFormat.getDateTimeInstance(lengthStyle, lengthStyle);
				break;
		}
	}

	protected class UpDownAction extends AbstractAction
	{
		public UpDownAction(int direction, String name)
		{
			super(name);
			m_direction = direction;
		}

		public void actionPerformed(ActionEvent evt)
		{
			if( !XDateTimeEditor.this.isEnabled() )
				return;

			boolean dateSet = true;

			switch (curField)
			{
				case DateFormat.AM_PM_FIELD:
					lastDate.setTime(lastDate.getTime() + (m_direction * 12 * ONE_HOUR));
					break;
				case DateFormat.DATE_FIELD:
				case DateFormat.DAY_OF_WEEK_FIELD:
				case DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD:
				case DateFormat.DAY_OF_YEAR_FIELD:
					lastDate.setTime(lastDate.getTime() + (m_direction * ONE_DAY));
					break;
				case DateFormat.ERA_FIELD:
					dateSet = false;
					break;
				case DateFormat.HOUR0_FIELD:
				case DateFormat.HOUR1_FIELD:
				case DateFormat.HOUR_OF_DAY0_FIELD:
				case DateFormat.HOUR_OF_DAY1_FIELD:
					lastDate.setTime(lastDate.getTime() + (m_direction * ONE_HOUR));
					break;
				case DateFormat.MILLISECOND_FIELD:
					lastDate.setTime(lastDate.getTime() + (m_direction * 1));
					break;
				case DateFormat.MINUTE_FIELD:
					lastDate.setTime(lastDate.getTime() + (m_direction * ONE_MINUTE));
					break;
				case DateFormat.MONTH_FIELD:
					calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + m_direction);
					lastDate = calendar.getTime();
					break;
				case DateFormat.SECOND_FIELD:
					lastDate.setTime(lastDate.getTime() + (m_direction * ONE_SECOND));
					break;
				case DateFormat.WEEK_OF_MONTH_FIELD:
					calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + m_direction);
					lastDate = calendar.getTime();
					break;
				case DateFormat.WEEK_OF_YEAR_FIELD:
					calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + m_direction);
					lastDate = calendar.getTime();
					break;
				case DateFormat.YEAR_FIELD:
					calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + m_direction);
					lastDate = calendar.getTime();
					break;
				default:
					dateSet = false;
			}

			if( dateSet )
			{
			int fieldId = curField;

				setDate(lastDate);

				textField.setCaretPosition(getFieldPosition(fieldId).getBeginIndex());
				textField.requestFocus();
				repaint();
			}
		}

	// attributes:
		private int m_direction;		// +1 = up; -1 = down
	}

	protected class BackwardAction extends TextAction
	{
		BackwardAction(String name)
		{
			super(name);
		}

		public void actionPerformed(ActionEvent e)
		{
		JTextComponent  target = getTextComponent(e);

			if (target != null)
			{
				int dot = target.getCaretPosition();

				if (dot > 0)
				{
				FieldPosition   position = getPrevField(dot);

					if (position != null)
						target.setCaretPosition(position.getBeginIndex());
					else
					{
						position = getFirstField();
						if (position != null)
							target.setCaretPosition(position.getBeginIndex());
					}
				}
				else
					target.getToolkit().beep();

				target.getCaret().setMagicCaretPosition(null);
			}
		}
	}

	protected class ForwardAction extends TextAction
	{
		ForwardAction(String name)
		{
			super(name);
		}

		public void actionPerformed(ActionEvent e)
		{
		JTextComponent  target = getTextComponent(e);

			if (target != null)
			{
			FieldPosition   position = getNextField(target.getCaretPosition());

				if (position != null)
					target.setCaretPosition(position.getBeginIndex());
				else
				{
					position = getLastField();
					if (position != null)
						target.setCaretPosition(position.getBeginIndex());
				}

				target.getCaret().setMagicCaretPosition(null);
			}
		}
	}

	protected class BeginAction extends TextAction
	{
		BeginAction(String name)
		{
			super(name);
		}

		public void actionPerformed(ActionEvent e)
		{
		JTextComponent  target = getTextComponent(e);

			if (target != null)
			{
			FieldPosition   position = getFirstField();

				if (position != null)
					target.setCaretPosition(position.getBeginIndex());
			}
		}
	}

	protected class EndAction extends TextAction
	{
		EndAction(String name)
		{
			super(name);
		}

		public void actionPerformed(ActionEvent e)
		{
		JTextComponent  target = getTextComponent(e);

			if (target != null)
			{
			FieldPosition   position = getLastField();

				if (position != null)
					target.setCaretPosition(position.getBeginIndex());
			}
		}
	}

	protected void setupKeymap()
	{
	Keymap  keymap = textField.addKeymap("XDateTimeKeymap", null);

		keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), upAction);
		keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), downAction);
		keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), new BackwardAction(DefaultEditorKit.backwardAction));
		keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), new ForwardAction(DefaultEditorKit.forwardAction));
		keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), new BeginAction(DefaultEditorKit.beginAction));
		keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), new EndAction(DefaultEditorKit.endAction));

		keymap.setDefaultAction(null);
		textField.setKeymap(keymap);
	}

	private void getFieldPositions()
	{
		fieldPositions.clear();

		for (int ctr = 0; ctr < fieldTypes.length; ++ctr)
		{
		int				fieldId = fieldTypes[ctr];
		FieldPosition   fieldPosition = new FieldPosition(fieldId);
		StringBuffer	formattedField = new StringBuffer();

			dateFormat.format(lastDate, formattedField, fieldPosition);
			if (fieldPosition.getEndIndex() > 0)
				fieldPositions.add(fieldPosition);
		}

		fieldPositions.trimToSize();
		Collections.sort(fieldPositions, new Comparator() {
			public int compare(Object o1, Object o2)
			{
				return (((FieldPosition) o1).getBeginIndex() - ((FieldPosition) o2).getBeginIndex());
			}
		});
	}

	private FieldPosition getField(int caretLoc)
	{
		for (Iterator iter = fieldPositions.iterator(); iter.hasNext(); )
		{
			FieldPosition   chkFieldPosition = (FieldPosition) iter.next();

			if ((chkFieldPosition.getBeginIndex() <= caretLoc) && (chkFieldPosition.getEndIndex() > caretLoc))
				return chkFieldPosition;
		}

		return null;
	}

	private FieldPosition getPrevField(int caretLoc)
	{
		for (int ctr = fieldPositions.size() - 1; ctr > -1; --ctr)
		{
			FieldPosition   chkFieldPosition = (FieldPosition) fieldPositions.get(ctr);

			if (chkFieldPosition.getEndIndex() <= caretLoc)
				return chkFieldPosition;
		}

		return null;
	}

	private FieldPosition getNextField(int caretLoc)
	{
		for (Iterator iter = fieldPositions.iterator(); iter.hasNext(); )
		{
		FieldPosition   chkFieldPosition = (FieldPosition) iter.next();

			if (chkFieldPosition.getBeginIndex() > caretLoc)
				return chkFieldPosition;
		}

		return null;
	}

	private FieldPosition getFirstField()
	{
		try
		{
			return( (FieldPosition)fieldPositions.get(0) );
		}
		catch( Exception ex )
		{
		}

		return null;
	}

	private FieldPosition getLastField()
	{
		try
		{
			return( (FieldPosition)fieldPositions.get(fieldPositions.size()-1) );
		}
		catch (NoSuchElementException ex)
		{
		}

		return null;
	}

	private void setCurField()
	{
	FieldPosition   fieldPosition = getField(textField.getCaretPosition());

		if (fieldPosition != null)
		{
			if (textField.getCaretPosition() != fieldPosition.getBeginIndex())
				textField.setCaretPosition(fieldPosition.getBeginIndex());
		}
		else
		{
			fieldPosition = getPrevField(textField.getCaretPosition());
			if (fieldPosition != null)
				textField.setCaretPosition(fieldPosition.getBeginIndex());
			else
			{
				fieldPosition = getFirstField();
				if (fieldPosition != null)
					textField.setCaretPosition(fieldPosition.getBeginIndex());
			}
		}

		if (fieldPosition != null)
			curField = fieldPosition.getField();
		else
			curField = -1;
	}

	public void setEnabled(boolean enable)
	{
		textField.setEnabled(enable);
		spinner.setEnabled(enable);
	}

	public boolean isEnabled()
	{
		return( textField.isEnabled() && spinner.isEnabled() );
	}

// Attributes:
	public final static long	ONE_SECOND = 1000;
	public final static long	ONE_MINUTE = 60 * ONE_SECOND;
	public final static long	ONE_HOUR = 60 * ONE_MINUTE;
	public final static long	ONE_DAY = 24 * ONE_HOUR;
	public final static long	ONE_WEEK = 7 * ONE_DAY;
	public final static int		TIME = 0;
	public final static int		DATE = 1;
	public final static int		DATETIME = 2;

	private int					timeOrDateType;
	private int					lengthStyle;
	private DateFormat			dateFormat;
	private Calendar			calendar = Calendar.getInstance();
	private ArrayList			fieldPositions = new ArrayList();
	private Date				lastDate = new Date();
	private int					curField = -1;
	private JTextField			textField;
	private XSpinner			spinner;
	private AbstractAction		upAction = new UpDownAction(1, "up");
	private AbstractAction		downAction = new UpDownAction(-1, "down");
	private boolean				settingDateText = false;		// BUG FIX
	private int[]				fieldTypes =
	{
		DateFormat.ERA_FIELD, DateFormat.YEAR_FIELD, DateFormat.MONTH_FIELD, DateFormat.DATE_FIELD, DateFormat.HOUR_OF_DAY1_FIELD, DateFormat.HOUR_OF_DAY0_FIELD, DateFormat.MINUTE_FIELD,
		DateFormat.SECOND_FIELD, DateFormat.MILLISECOND_FIELD, DateFormat.DAY_OF_WEEK_FIELD, DateFormat.DAY_OF_YEAR_FIELD, DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD, DateFormat.WEEK_OF_YEAR_FIELD,
		DateFormat.WEEK_OF_MONTH_FIELD, DateFormat.AM_PM_FIELD, DateFormat.HOUR1_FIELD, DateFormat.HOUR0_FIELD
	};
}
