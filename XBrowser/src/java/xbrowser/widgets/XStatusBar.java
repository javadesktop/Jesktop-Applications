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
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;

public class XStatusBar extends JPanel implements ActionListener
{
	public XStatusBar(boolean show_progress, boolean show_date_time)
    {
    	setLayout( new BorderLayout() );

    	prgbarMain.setBorder(BorderFactory.createLoweredBevelBorder());
    	lblStatus.setBorder(BorderFactory.createLoweredBevelBorder());

    	lblTime.setBorder(BorderFactory.createLoweredBevelBorder());
    	lblDate.setBorder(BorderFactory.createLoweredBevelBorder());
	    pnlDateTime.add(lblTime, BorderLayout.EAST);
	    pnlDateTime.add(lblDate, BorderLayout.WEST);

	    add(lblStatus, BorderLayout.CENTER);

	    showDateTime(show_date_time);
	    showProgressBar(show_progress);
	    resetStatus();
    }

    public void resetStatus()
    {
    	setStatusMessage(initialStatus);
	}

    public void setStatusMessage(String str)
    {
    	lblStatus.setText(str);
	}

    public void showDateTime(boolean b)
    {
		if( b )
		{
		    add(pnlDateTime, BorderLayout.EAST);
		    startTimer();
		}
		else
		{
			remove(pnlDateTime);
		    stopTimer();
		}
	}

    public void showProgressBar(boolean b)
    {
		if( b )
		    add(prgbarMain, BorderLayout.WEST);
		else
			remove(prgbarMain);
	}

	public void startProgress()
	{
		prgbarMain.start();
	}

	public void stopProgress()
	{
		prgbarMain.stop();
	}

	public void startTimer()
	{
		timer.start();
	}

	public void stopTimer()
	{
		timer.stop();
	}

    public void actionPerformed(ActionEvent e)
	{
        lblTime.setText( timeFormatter.format(new Date()) );
        lblDate.setText( dateFormatter.format(new Date()) );
	}

// Attributes:
	private JLabel lblStatus = new JLabel();
	private JLabel lblTime = new JLabel();
	private JLabel lblDate = new JLabel();
	private XProgressBar prgbarMain = new XProgressBar();
	private JPanel pnlDateTime = new JPanel(new BorderLayout());

    private javax.swing.Timer timer = new javax.swing.Timer(1000,this);
    private DateFormat timeFormatter = new SimpleDateFormat("   h:mm:ss a   ");
    private DateFormat dateFormatter = new SimpleDateFormat("   EEE, MMM dd, yyyy   ");

    private String initialStatus = XComponentBuilder.getInstance().getProperty(this, "InitialStatus");
}
