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
import java.awt.*;

public class XDesktopPane extends JDesktopPane
{
    public void addInternalFrame(JInternalFrame frame)
    {
		frame.setBounds( getNextCascadeBounds() );

		add(frame);
        frame.setVisible(true);
        try
        {
        	frame.setSelected(true);
        }
        catch( Exception e )
        {
        }
	}

    private Rectangle getNextCascadeBounds()
    {
	int w = (int)(getSize().width*0.7);
	int h = (int)(0.7*w);
	int new_x = row*20 + column*80;
	int new_y = row*20;

		if( new_x+w>getSize().width )
		{
			row = 0;
			column = 0;

			new_x = 0;
			new_y = 0;
		}
		else if( new_y+h>getSize().height )
		{
			row = 0;
			column++;

			new_x = row*20 + column*80;
			new_y = row*20;
		}

		row++;

		return( new Rectangle(new_x,new_y,w,h) );
	}

    public void remove(int index)
    {
		super.remove(index);

        if( getAllFrames().length==0 )
        {
			row = 0;
			column = 0;
		}
	}

	private void tileFour()
	{
	int win_width = (int)(getSize().width/2.0);
	int win_height = (int)((getSize().height-30)/2.0);
    JInternalFrame[] frames = getAllFrames();
	int count = 0;

		for( int j=0; j<frames.length; j++ )
		{
		JInternalFrame cwin = frames[j];

			if( !cwin.isIcon() )
			{
				cwin.setSize(new Dimension(win_width,win_height));
				switch (count)
				{
					case 0:
						cwin.setLocation(0,0);
						break;
					case 1:
						cwin.setLocation(win_width,0);
						break;
					case 2:
						cwin.setLocation(0,win_height);
						break;
					case 3:
						cwin.setLocation(win_width,win_height);
						break;
					default:
						break;
				}

				count++;
			}
		}
	}

	public void tileCascade()
	{
    JInternalFrame[] frames = getAllFrames();

		row = 0;
		column = 0;

		for( int j=0; j<frames.length; j++ )
		{
		JInternalFrame cwin = frames[j];

			if( !cwin.isIcon() )
			{
				cwin.setBounds( getNextCascadeBounds() );
				cwin.toFront();
			}
		}
	}

	public void tileHorizontal()
	{
    JInternalFrame[] frames = getAllFrames();
	int win_count = 0;

		for( int i=0; i<frames.length; i++ )
		{
		JInternalFrame cwin = frames[i];

			if( !cwin.isIcon() )
				win_count++;
		}

		if( win_count==4 )
			tileFour();
		else if( win_count>0 )
		{
		int win_width = getSize().width;
		int win_height = (getSize().height-30)/win_count;
		int y_pos = 0;

			for( int j=0; j<frames.length; j++ )
			{
			JInternalFrame cwin = frames[j];

				if( !cwin.isIcon() )
				{
					cwin.setSize(new Dimension(win_width,win_height));
					cwin.setLocation(0,y_pos);
					y_pos += win_height;
				}
			}
		}
	}

	public void tileVertical()
	{
    JInternalFrame[] frames = getAllFrames();
	int win_count = 0;

		for( int i=0; i<frames.length; i++ )
		{
		JInternalFrame cwin = frames[i];

			if( !cwin.isIcon() )
				win_count++;
		}

		if( win_count==4 )
			tileFour();
		else if( win_count>0 )
		{
		int win_width = getSize().width/win_count;
		int win_height = getSize().height-30;
		int x_pos = 0;

			for( int j=0; j<frames.length; j++ )
			{
			JInternalFrame cwin = frames[j];

				if( !cwin.isIcon() )
				{
					cwin.setSize(new Dimension(win_width,win_height));
					cwin.setLocation(x_pos,0);
					x_pos += win_width;
				}
			}
		}
	}

    public void minimizeAll()
    {
    JInternalFrame[] frames = getAllFrames();

		for( int j=0; j<frames.length; j++ )
		{
		JInternalFrame cwin = frames[j];

			if( !cwin.isIcon() )
			{
				try
				{
					cwin.setIcon(true);
				}
				catch( Exception e )
				{
				}
			}
		}
	}

    public void restoreAll()
    {
    JInternalFrame[] frames = getAllFrames();

		for( int j=0; j<frames.length; j++ )
		{
		JInternalFrame cwin = frames[j];

			if( cwin.isIcon() )
			{
				try
				{
					cwin.setIcon(false);
				}
				catch( Exception e )
				{
				}
			}
		}
	}

// Attributes:
	private int row = 0;
	private int column = 0;
}
