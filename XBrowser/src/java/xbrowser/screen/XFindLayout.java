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
import javax.swing.event.*;
import javax.swing.border.*;

import xbrowser.*;
import xbrowser.doc.*;
import xbrowser.widgets.*;

public class XFindLayout extends XDialog
{
	public static XFindLayout getInstance()
	{
		if( instance==null )
			instance = new XFindLayout();

		return instance;
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

	private XFindLayout()
	{
	    super(false);

		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );
    	btnFind = XComponentBuilder.getInstance().buildButton(new FindAction());

	JPanel pnl_east_north = new JPanel( new GridLayout(2,1,5,10) );
	JPanel pnl_east = new JPanel( new BorderLayout() );

		getContentPane().setLayout( new BorderLayout() );

		pnl_east_north.add( btnFind );
		pnl_east_north.add( XComponentBuilder.getInstance().buildButton(new CloseAction()) );

		pnl_east.add(pnl_east_north, BorderLayout.NORTH);

		getContentPane().add(getDocumentPage(), BorderLayout.CENTER);
		getContentPane().add(pnl_east, BorderLayout.EAST);

        registerListeners();
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        pack();
        setResizable(false);

        getRootPane().setDefaultButton(btnFind);
	}

    private void registerListeners()
    {
        txfDocumentFindPhrase.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				btnFind.doClick();
			}
		});
	}

    private JPanel getDocumentPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);

		cmbDirection.addItem( XComponentBuilder.getInstance().getProperty(this, "UpDirection") );
		cmbDirection.addItem( XComponentBuilder.getInstance().getProperty(this, "DownDirection") );
		cmbDirection.setSelectedIndex(1);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "FindSubject"),pnl,gridbag,constraints,1,0);
		addToContainer(txfDocumentFindPhrase,pnl,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(chkMatchWholeWord,pnl,gridbag,constraints,2,1);
		addToContainer(chkMatchCase,pnl,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Direction"),pnl,gridbag,constraints,1,0);
		addToContainer(cmbDirection,pnl,gridbag,constraints,1,1);
		addToContainer(Box.createHorizontalGlue(),pnl,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl;
	}

    public void findInDocument()
    {
    XDocument document = XBrowser.getBrowser().getActiveDocument();

    	if( document==null )
    	{
		String message = XComponentBuilder.getInstance().getProperty(this, "NoActiveDocument");
		String title = XComponentBuilder.getInstance().getProperty(this, "Warning");

	        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
			return;
		}

    	try
    	{
    		document.getRenderer().findNext(txfDocumentFindPhrase.getText(), chkMatchCase.isSelected(), chkMatchWholeWord.isSelected(), cmbDirection.getSelectedItem().equals("Up"));
		}
		catch( Exception e )
		{
		String title = XComponentBuilder.getInstance().getProperty(this, "Warning");

	        JOptionPane.showMessageDialog(this, e.getMessage(), title, JOptionPane.WARNING_MESSAGE);
		}
    }

    private class FindAction extends XAction
    {
        public FindAction()
        {
            super(XFindLayout.this, "Find", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			findInDocument();
        }
    }

    private class CloseAction extends XAction
    {
        public CloseAction()
        {
            super(XFindLayout.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
        }
    }

// Attributes:
	private static XFindLayout instance = null;

    private JTextField txfDocumentFindPhrase = new JTextField(20);
    private JCheckBox chkMatchWholeWord = XComponentBuilder.getInstance().buildCheckBox(this, "WholeWord");
    private JCheckBox chkMatchCase = XComponentBuilder.getInstance().buildCheckBox(this, "CaseSensitive");
    private JComboBox cmbDirection = new JComboBox();
    private JButton btnFind;
}
