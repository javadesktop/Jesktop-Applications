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

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import xbrowser.*;
import xbrowser.doc.*;
import xbrowser.widgets.*;

public class XJavaConsole extends XFrame
{
	public static XJavaConsole getInstance()
	{
		if( instance==null )
			instance = new XJavaConsole();

		return instance;
	}

	private XJavaConsole()
	{
		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );

		txaConsole.setEditable(false);

		getContentPane().setLayout( new BorderLayout() );
		getContentPane().add(new JScrollPane(txaConsole), BorderLayout.CENTER);
		getContentPane().add(getButtonsPanel(),BorderLayout.SOUTH);

        setIconImage( XComponentBuilder.getInstance().buildImageIcon(this, "image.FrameIcon").getImage() );
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();

		try
		{
		PrintStream output = new PrintStream( new XOutputStream() );

			System.setOut(output);
			System.setErr(output);
		}
		catch( Exception e )
		{
			System.out.println("Error on redirecting the streams !!");
		}

        setSize( new Dimension(500,350) );
	}

    private JPanel getButtonsPanel()
    {
	JPanel pnl_main = new JPanel(new FlowLayout());
	JPanel pnl = new JPanel(new GridLayout(1,3,20,0));
	JButton btn_close = XComponentBuilder.getInstance().buildButton(new CloseAction());

		pnl.add(XComponentBuilder.getInstance().buildButton(new ClearAction()));
		pnl.add(XComponentBuilder.getInstance().buildButton(new CopyAction()));
		pnl.add(btn_close);

        getRootPane().setDefaultButton(btn_close);

        pnl_main.add(pnl);
        return pnl_main;
	}

    private class ClearAction extends XAction
    {
        public ClearAction()
        {
            super(XJavaConsole.this, "Clear", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			txaConsole.setText("");
        }
    }

    private class CopyAction extends XAction
    {
        public CopyAction()
        {
            super(XJavaConsole.this, "Copy", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	        txaConsole.copy();
        }
    }

    private class CloseAction extends XAction
    {
        public CloseAction()
        {
            super(XJavaConsole.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
        }
    }

	private class XOutputStream extends OutputStream
	{
		public synchronized void write(int b) throws IOException
		{
			txaConsole.append(""+((char)b));
		}
	}

// Attributes:
	private static XJavaConsole instance = null;

	private JTextArea txaConsole = new JTextArea(20,10);
}
