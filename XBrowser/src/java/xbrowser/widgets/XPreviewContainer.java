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

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import xbrowser.*;
import xbrowser.widgets.event.*;

public class XPreviewContainer extends JPanel
{
    public XPreviewContainer(XScaleListener scale_listener)
    {
		scaleListener = scale_listener;

	Image zoom_in_img = XComponentBuilder.getInstance().buildImageIcon(this, "image.ZoomIn").getImage();
	Image zoom_out_img = XComponentBuilder.getInstance().buildImageIcon(this, "image.ZoomOut").getImage();

		zoomInCursor = Toolkit.getDefaultToolkit().createCustomCursor(zoom_in_img, new Point(0,0), "ZoomIn");
		zoomOutCursor = Toolkit.getDefaultToolkit().createCustomCursor(zoom_out_img, new Point(0,0), "ZoomOut");

        addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				xDifference = e.getX();
				yDifference = e.getY();
			}

			public void mouseReleased(MouseEvent e)
			{
				setCursor( (zoomMode==XProjectConstants.ZOOM_IN) ? zoomInCursor : zoomOutCursor );
			}

			public void mouseClicked(MouseEvent e)
			{
				if( zoomMode==XProjectConstants.ZOOM_IN )
					scaleListener.zoomedIn();
				else if( zoomMode==XProjectConstants.ZOOM_OUT )
					scaleListener.zoomedOut();
			}

			public void mouseEntered(MouseEvent e)
			{
				setCursor( (zoomMode==XProjectConstants.ZOOM_IN) ? zoomInCursor : zoomOutCursor );
			}

			public void mouseExited(MouseEvent e)
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

        addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e)
			{
			Container c = getParent();

				if( c instanceof JViewport )
				{
				JViewport jv = (JViewport) c;
				Point p = jv.getViewPosition();
				int new_x = p.x - (e.getX() - xDifference);
				int new_y = p.y - (e.getY() - yDifference);
				int max_x = getWidth() - jv.getWidth();
				int max_y = getHeight() - jv.getHeight();

					if (new_x < 0)
						new_x = 0;

					if (new_x > max_x)
						new_x = max_x;

					if (new_y < 0)
						new_y = 0;

					if (new_y > max_y)
						new_y = max_y;

					jv.setViewPosition(new Point(new_x, new_y));
				}
			}
		});
    }

	public void setZoomMode(int zoom)
	{
		zoomMode = zoom;
	}

	public int getZoomMode()
	{
		return zoomMode;
	}

	public Dimension getPreferredSize()
	{
	int n = getComponentCount();

		if (n == 0)
			return new Dimension(H_GAP, V_GAP);

	Component comp = getComponent(0);
	Dimension dc = comp.getPreferredSize();
	int w = dc.width;
	int h = dc.height;

	Dimension dp = getParent().getSize();
	int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);
	int nRow = n/nCol;

		if (nRow*nCol < n)
			nRow++;

	int ww = nCol*(w+H_GAP) + H_GAP;
	int hh = nRow*(h+V_GAP) + V_GAP;
	Insets ins = getInsets();

		return new Dimension(ww+ins.left+ins.right,hh+ins.top+ins.bottom);
	}

	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}

	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	public void doLayout()
	{
	Insets ins = getInsets();
	int x = ins.left + H_GAP;
	int y = ins.top + V_GAP;
	int n = getComponentCount();

		if( n==0 )
			return;

	Dimension dc = getComponent(0).getPreferredSize();
	int w = dc.width;
	int h = dc.height;

	Dimension dp = getParent().getSize();
	int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);
	int nRow = n/nCol;

		if( nRow*nCol<n )
			nRow++;

	int index = 0;

		for( int i=0; i<nRow; i++ )
		{
			for( int j = 0; j<nCol; j++ )
			{
				if( index>=n )
					return;

				getComponent(index++).setBounds(x, y, w, h);
				x += w + H_GAP;
			}

			y += h + V_GAP;
			x = ins.left + H_GAP;
		}
	}

// Attributes:
	private int H_GAP = 16;
	private int V_GAP = 10;
    private int xDifference, yDifference;
    private XScaleListener scaleListener;

    private Cursor zoomInCursor, zoomOutCursor;
    private int zoomMode = XProjectConstants.ZOOM_IN;
}
