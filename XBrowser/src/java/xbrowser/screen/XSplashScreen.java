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
package xbrowser.screen;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import xbrowser.widgets.*;
import xbrowser.*;

public class XSplashScreen extends JWindow
{
    public XSplashScreen(String status)
    {
        super(new Frame());

  JLabel lbl = new JLabel(XComponentBuilder.getInstance().buildImageIcon(this, "image.Splash"));
  JPanel pnl = new JPanel( new BorderLayout() );
  JPanel pnl_south = new JPanel( new FlowLayout() );

        lbl.setVerticalTextPosition(JLabel.BOTTOM);
        lbl.setHorizontalTextPosition(JLabel.CENTER);

  String title = XComponentBuilder.getInstance().getProperty(this, "Title", XProjectConstants.PRODUCT_NAME+" "+XProjectConstants.PRODUCT_VERSION, XProjectConstants.PRODUCT_AUTHOR);

        lbl.setText(title);

        lblStatus = new JLabel(status);
        lblStatus.setSize(lblStatus.getWidth(), 150);
        pnl_south.add(lblStatus);

        pnl.add(BorderLayout.CENTER, lbl);
        pnl.add(BorderLayout.SOUTH, pnl_south);
        pnl.setBorder( BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),BorderFactory.createLoweredBevelBorder()) );

        getContentPane().add(pnl);
        pack();
    }

    public void setVisible(boolean b)
    {
      if( b==true )
        {
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = getSize();

          setLocation( (screen_size.width-size.width)/2, (screen_size.height-size.height)/2 );
        }

        super.setVisible(b);
    }

    public void setStatus(String status)
    {
    lblStatus.setText(status);
//    System.out.println("status2=" + status + " wi " + lblStatus.getWidth() + " hi " + lblStatus.getHeight());
  //  System.out.println("       =" + lblStatus.getBounds());

    lblStatus.invalidate();
    this.dispatchEvent(new PaintEvent(this,PaintEvent.PAINT_FIRST, new Rectangle(0,0)));
    Rectangle rect = lblStatus.getBounds();
    lblStatus.paintImmediately(rect);

  }

// Attributes:
  private JLabel lblStatus = null;
}
