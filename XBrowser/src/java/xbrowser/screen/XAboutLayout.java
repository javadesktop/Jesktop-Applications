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
import javax.swing.border.*;

import xbrowser.*;
import xbrowser.widgets.*;

public class XAboutLayout extends XDialog
{
	public XAboutLayout()
	{
	    super(true);

		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );
		getContentPane().setLayout(new BorderLayout());

	JTabbedPane tab = new JTabbedPane();
	ImageIcon icon;
	String title;

		icon = XComponentBuilder.getInstance().buildImageIcon(this, "image.XBrowser");
		tab.addTab(XProjectConstants.PRODUCT_NAME, icon, getFirstPage());

		icon = XComponentBuilder.getInstance().buildImageIcon(this, "image.Contact");
		title = XComponentBuilder.getInstance().getProperty(this, "ContactInfo");
		tab.addTab(title, icon, getSecondPage());

		icon = XComponentBuilder.getInstance().buildImageIcon(this, "image.Acknowledgement");
		title = XComponentBuilder.getInstance().getProperty(this, "Acknowledge");
		tab.addTab(title, icon, getThirdPage());

		getContentPane().add(tab,BorderLayout.CENTER);

	JPanel pnl_south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JButton btn_ok = XComponentBuilder.getInstance().buildButton(new OKAction());

		pnl_south.add(btn_ok);
		getContentPane().add(pnl_south,BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btn_ok);

		pack();
		setResizable(false);
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

    private JPanel getFirstPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
	JPanel pnl_main = new JPanel( new BorderLayout() );
	JPanel pnl_center = new JPanel(gridbag);
	JPanel pnl_west = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JPanel pnl_east = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JLabel lbl_title = new JLabel(XProjectConstants.PRODUCT_NAME+" "+XProjectConstants.PRODUCT_VERSION, SwingConstants.CENTER);
	JLabel lbl_description = XComponentBuilder.getInstance().buildLabel(this, "Description", SwingConstants.CENTER);
	Font fnt = lbl_title.getFont();

		lbl_title.setFont( fnt.deriveFont(Font.BOLD,fnt.getSize()+6) );
		lbl_description.setFont( fnt.deriveFont(Font.BOLD,fnt.getSize()+3) );

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2,2,2,2);

		addToContainer(lbl_title,pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(" ", SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(lbl_description,pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);

		//addToContainer(new JLabel(" ", SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(XComponentBuilder.getInstance().buildImageIcon(this, "image.JarsRated")),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);

		addToContainer(new JLabel(XComponentBuilder.getInstance().getProperty(this, "Author", XProjectConstants.PRODUCT_AUTHOR), SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(" ", SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(XComponentBuilder.getInstance().getProperty(this, "Copyright", XProjectConstants.PRODUCT_AUTHOR), SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(XComponentBuilder.getInstance().getProperty(this, "IconCopyright", "1998 Dean S. Jones"), SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "AllRightsReserved", SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);

		pnl_west.add(new XAnimator(100,"Duke",10));
		pnl_east.add(new XAnimator(100,"Duke",10));

		pnl_main.add(pnl_center,BorderLayout.CENTER);
		pnl_main.add(pnl_west,BorderLayout.WEST);
		pnl_main.add(pnl_east,BorderLayout.EAST);

		return pnl_main;
	}

    private JPanel getSecondPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
	JPanel pnl_main = new JPanel( new BorderLayout() );
	JPanel pnl_center = new JPanel(gridbag);
	JPanel pnl_addresses = new JPanel( new GridLayout(3,1,0,5) );

        pnl_addresses.add(XComponentBuilder.getInstance().buildLabel(this, "AuthorEmails"));
        pnl_addresses.add(new JLabel(" -    "+XProjectConstants.AUTHOR_PRIMARY_EMAIL));
        pnl_addresses.add(new JLabel(" -    "+XProjectConstants.AUTHOR_SECONDARY_EMAIL));

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2,2,2,2);

        constraints.gridheight = 3;
		addToContainer(pnl_addresses,pnl_center,gridbag,constraints,GridBagConstraints.RELATIVE,1);
		addToContainer(new JLabel(XComponentBuilder.getInstance().buildImageIcon(this, "image.Contact2")),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
        constraints.gridheight = 1;
		addToContainer(new JLabel(" ", SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "AuthorHomePage"),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(XProjectConstants.AUTHOR_HOME_PAGE),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(" ", SwingConstants.CENTER),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(XComponentBuilder.getInstance().getProperty(this, "ProductWebPage", XProjectConstants.PRODUCT_NAME)),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(XProjectConstants.PRODUCT_HOME_PAGE),pnl_center,gridbag,constraints,GridBagConstraints.REMAINDER,0);

		pnl_main.add(pnl_center,BorderLayout.CENTER);

		return pnl_main;
	}

    private JPanel getThirdPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
	JPanel pnl_main = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);
        constraints.gridheight = 1;
        constraints.weighty = 1;

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "SpecialThanks"),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel(""),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(new JLabel("Bernd Schilling, Daniel Lemire, David Bernard,",SwingConstants.CENTER),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(new JLabel("Dean S. Jones, Dmitry Beransky, Jean-Baptiste Bugeaud,",SwingConstants.CENTER),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(new JLabel("Matthew Robinson, Sébastien Stormacq, Venko Stanev,",SwingConstants.CENTER),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(new JLabel("Vincent Estrada",SwingConstants.CENTER),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(new JLabel(""),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,0);

        constraints.insets = new Insets(0,0,0,0);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Thanks1", SwingConstants.CENTER),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Thanks2", SwingConstants.CENTER),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Thanks3", SwingConstants.CENTER),pnl_main,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl_main;
	}

    private class OKAction extends XAction
    {
        public OKAction()
        {
            super(XAboutLayout.this, "Ok", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
		    dispose();
        }
    }
}
