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
import java.util.*;
import java.text.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import xbrowser.*;
import xbrowser.widgets.*;
import xbrowser.bookmark.*;

public class XBookmarkPropertiesLayout extends XFrame
{
	public static XBookmarkPropertiesLayout getInstance()
	{
		return getInstance(null,null);
	}

	public static XBookmarkPropertiesLayout getInstance(XAbstractBookmark abs_bm, XBookmarkFolder new_parent)
	{
		if( instance==null )
			instance = new XBookmarkPropertiesLayout();

		instance.setBookmark(abs_bm, new_parent);
		return instance;
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

	private XBookmarkPropertiesLayout()
	{
		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();

		lblLocation.setVerticalAlignment(SwingConstants.TOP);
		lblLocation.setVerticalTextPosition(SwingConstants.TOP);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);
		getContentPane().setLayout(gridbag);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Name"),getContentPane(),gridbag,constraints,1,0);
		addToContainer(txfName,getContentPane(),gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(lblLocation,getContentPane(),gridbag,constraints,1,0);
		addToContainer(txfLocation,getContentPane(),gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Description"),getContentPane(),gridbag,constraints,1,0);
		addToContainer(new JScrollPane(txaDescription),getContentPane(),gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "ParentFolder"),getContentPane(),gridbag,constraints,1,0);
		addToContainer(cmbParent,getContentPane(),gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "CreationDate"),getContentPane(),gridbag,constraints,1,0);
		addToContainer(txfCreationDate,getContentPane(),gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "ModificationDate"),getContentPane(),gridbag,constraints,1,0);
		addToContainer(txfModificationDate,getContentPane(),gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(getButtonsPanel(),getContentPane(),gridbag,constraints,GridBagConstraints.REMAINDER,0);

		txfCreationDate.setEnabled(false);
		txfModificationDate.setEnabled(false);

        txfName.addFocusListener( new FocusAdapter() {
			public void focusGained(FocusEvent e)
			{
				txfName.selectAll();
			}
		});
        txfLocation.addFocusListener( new FocusAdapter() {
			public void focusGained(FocusEvent e)
			{
				txfLocation.selectAll();
			}
		});
        txaDescription.addFocusListener( new FocusAdapter() {
			public void focusGained(FocusEvent e)
			{
				txaDescription.selectAll();
			}
		});

        setIconImage( XComponentBuilder.getInstance().buildImageIcon(this, "image.FrameIcon").getImage() );
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();
        setResizable(false);
	}

    private void setBookmark(XAbstractBookmark abs_bm, XBookmarkFolder new_parent)
    {
        absBookmark = abs_bm;

        if( absBookmark!=null )
        {
			txfName.setText( absBookmark.getTitle() );
			txaDescription.setText( absBookmark.getDescription() );

			if( absBookmark.getParent()!=null )
				cmbParent.setSelectedItem( absBookmark.getParent() );
			else
				cmbParent.setSelectedItem( new_parent );

			txfCreationDate.setText( df.format(absBookmark.getCreationDate()) );
			txfModificationDate.setText( df.format(absBookmark.getModificationDate()) );
		}
		else
		{
			txfName.setText("");
			txaDescription.setText("");

			txfCreationDate.setText("");
			txfModificationDate.setText("");

			cmbParent.setSelectedItem( new_parent );
		}

		if( absBookmark instanceof XBookmark )
		{
			lblLocation.setEnabled(true);
			txfLocation.setEnabled(true);
	        txfLocation.setText( ((XBookmark)absBookmark).getHRef() );
		}
		else
		{
			lblLocation.setEnabled(false);
			txfLocation.setEnabled(false);
	        txfLocation.setText("");
		}
	}

    private JPanel getButtonsPanel()
    {
    JPanel pnl_main = new JPanel();
    JPanel pnl_buttons = new JPanel();
	final JButton btn_ok = XComponentBuilder.getInstance().buildButton(new OKAction());

        cmbParent.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				btn_ok.setEnabled( cmbParent.getSelectedBookmark()!=absBookmark );
			}
		});

        pnl_main.setLayout(new FlowLayout());
        pnl_buttons.setLayout(new GridLayout(1,2,15,5));
        pnl_buttons.add(btn_ok);
        pnl_buttons.add(XComponentBuilder.getInstance().buildButton(new CancelAction()));
        pnl_main.add(pnl_buttons);

        getRootPane().setDefaultButton(btn_ok);

        return pnl_main;
	}

    private class OKAction extends XAction
    {
        public OKAction()
        {
            super(XBookmarkPropertiesLayout.this, "Ok", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    absBookmark.setTitle( txfName.getText() );
		    absBookmark.setDescription( txaDescription.getText() );
			if( absBookmark instanceof XBookmark )
				((XBookmark)absBookmark).setHRef( txfLocation.getText() );
			absBookmark.setModificationDate( new Date() );

			cmbParent.getSelectedBookmark().addBookmark(absBookmark);
		    setVisible(false);
        }
    }

    private class CancelAction extends XAction
    {
        public CancelAction()
        {
            super(XBookmarkPropertiesLayout.this, "Cancel", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
        }
    }

// Attributes:
	private static XBookmarkPropertiesLayout instance = null;
	private XAbstractBookmark absBookmark = null;

	private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

	private JLabel lblLocation = XComponentBuilder.getInstance().buildLabel(this, "Location");
	private JTextField txfName = new JTextField(30);
	private JTextField txfLocation = new JTextField(30);
	private JTextArea txaDescription = new JTextArea(7,30);
	private JTextField txfCreationDate = new JTextField(30);
	private JTextField txfModificationDate = new JTextField(30);
	private XBookmarkFolderComboBox cmbParent = new XBookmarkFolderComboBox();
}
