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

public class XAnimator extends JLabel
{
    public XAnimator(int delay, String frames_base_name, int frames_count)
    {
		this.delay = delay;
		framesCount = frames_count;
		frames = new Icon[framesCount];

        for( int i=1; i<=framesCount; i++ )
            frames[i-1] = XComponentBuilder.getInstance().buildImageIcon(this, "image."+frames_base_name+i);

        timer = new Timer(delay, new ActionListener() {
		    public void actionPerformed(ActionEvent e)
		    {
				frameNumber++;
				setIcon( frames[frameNumber%framesCount] );
			}
		});
        timer.setInitialDelay(0);
        timer.setCoalesce(true);

		setIcon(frames[0]);
		//setBorder( BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),BorderFactory.createLoweredBevelBorder()) );

		addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent e)
			{
				startAnimation();
			}

			public void mouseExited(MouseEvent e)
			{
				stopAnimation();

				while( (frameNumber%framesCount)!=0 )
				{
					frameNumber++;
					setIcon( frames[frameNumber%framesCount] );
				}
			}
		});
	}

    private synchronized void startAnimation()
    {
		if( !timer.isRunning() )
			timer.start();
    }

    private synchronized void stopAnimation()
    {
        if( timer.isRunning() )
            timer.stop();
    }

// Attributes:
    private int frameNumber = 0;
    private int delay;
    private int framesCount;
    private Timer timer;
    private Icon frames[];
}
