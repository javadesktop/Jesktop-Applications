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

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public class XSpinner extends JPanel
{
	public XSpinner()
	{
		createComponents();
	}

	public XSpinner(int orientation)
	{
		if( orientation!=SwingConstants.VERTICAL && orientation!=SwingConstants.HORIZONTAL )
			throw new IllegalArgumentException();

		orientation = orientation;
		createComponents();
	}

	public void setEnabled(boolean enable)
	{
		btnIncrement.setEnabled(enable);
		btnDecrement.setEnabled(enable);
	}

	public boolean isEnabled()
	{
		return( btnIncrement.isEnabled() && btnDecrement.isEnabled() );
	}

	protected void createComponents()
	{
		if( orientation==SwingConstants.VERTICAL )
		{
			setLayout(new GridLayout(2, 1));
			btnIncrement = new BasicArrowButton(SwingConstants.NORTH);
			btnDecrement = new BasicArrowButton(SwingConstants.SOUTH);
		}
		else if( orientation==SwingConstants.HORIZONTAL )
		{
			setLayout(new GridLayout(1, 2));
			btnIncrement = new BasicArrowButton(SwingConstants.EAST);
			btnDecrement = new BasicArrowButton(SwingConstants.WEST);
		}

		add(btnDecrement);
		add(btnIncrement);

    Dimension size = getPreferredSize();

        size.height = 25;
        setPreferredSize(size);
	}

	public JButton getIncrementButton()
	{
		return btnIncrement;
	}

	public JButton getDecrementButton()
	{
		return btnDecrement;
	}

// Attributes:
	private int					orientation = SwingConstants.VERTICAL;
	private BasicArrowButton	btnIncrement;
	private BasicArrowButton	btnDecrement;
}
