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
import java.awt.event.*;
import java.awt.geom.*;

public class XProgressBar extends JComponent implements ActionListener
{
	public XProgressBar()
    {
        timer = new Timer(sleepTime,this);
        setValue(min);
    }

    public Dimension getPreferredSize()
    {
        return( new Dimension(150,super.getSize().height) );
    }

    public void actionPerformed(ActionEvent e)
    {
		if( direction==FORWARD )
		{
	       	if( value<max )
            	setValue(value+1);
            else
            {
            	direction = BACKWARD;
                setValue(value-1);
            }
        }
        else
        {
        	if( value>min )
            	setValue(value-1);
            else
            {
            	direction = FORWARD;
                setValue(value+1);
            }
        }
    }

    private void setValue(int new_val)
    {
        if( new_val>max )
			value = max;
        else if( new_val<min )
			value = min;
		else
			value = new_val;

        repaint();
    }

	protected void paintComponent(Graphics g)
    {
    Graphics2D g2 = (Graphics2D) g;
    Dimension size = getPreferredSize();
    int current_value = value*(size.width/(max-min));
    Color first_color = UIManager.getColor("ProgressBar.background");
    Color second_color = UIManager.getColor("ProgressBar.foreground");

        g2.setColor( second_color );
        //g2.drawRect(0,0,size.width,size.height);

        if( direction==FORWARD )
        {
	        g2.setPaint( new GradientPaint(0, 0, first_color, current_value, size.height, second_color) );
        	g2.fill(new Rectangle2D.Double(0, 0, current_value, size.height));
        }
        else
        {
	        g2.setPaint( new GradientPaint(current_value, 0, second_color, size.width, size.height, first_color) );
        	g2.fill(new Rectangle2D.Double(current_value, 0, size.width, size.height));
        }
    }

   	public void start()
    {
		if( !timer.isRunning() )
    		timer.start();
    }

	public void stop()
    {
		if( timer.isRunning() )
		{
			timer.stop();
			direction = FORWARD;
			setValue(min);
		}
    }


// Attributes:
	private static final int FORWARD = 1;
	private static final int BACKWARD = 2;

    private Timer timer = null;
    private int min = 0;
    private int max = 50;
    private int value = 0;
    private int sleepTime = 50;
    private int direction = FORWARD;
}
