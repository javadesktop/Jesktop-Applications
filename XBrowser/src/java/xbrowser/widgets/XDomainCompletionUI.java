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

import xbrowser.util.*;

public class XDomainCompletionUI extends JPanel
{
	public XDomainCompletionUI(XDomainCompletion domain_completion)
	{
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();

		setLayout(gridbag);
		domainCompletion = domain_completion;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,0,0,0);

		addToContainer(chkAlt,this,gridbag,constraints,1,1);
		addToContainer(chkCtrl,this,gridbag,constraints,1,1);
		addToContainer(chkShft,this,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),this,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(txfPrefix,this,gridbag,constraints,1,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "CompletionAddrss", SwingConstants.CENTER),this,gridbag,constraints,1,0);
		addToContainer(txfPostfix,this,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		setBorder( BorderFactory.createTitledBorder("") );
		load();
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

	public void load()
	{
		chkAlt.setSelected( domainCompletion.getAltStatus() );
		chkCtrl.setSelected( domainCompletion.getCtrlStatus() );
		chkShft.setSelected( domainCompletion.getShiftStatus() );

		txfPrefix.setText( domainCompletion.getPrefix() );
		txfPostfix.setText( domainCompletion.getPostfix() );
	}

	public void loadDefaults()
	{
		txfPrefix.setText( domainCompletion.getDefaultPrefix() );
		txfPostfix.setText( domainCompletion.getDefaultPostfix() );
	}

	public void applyChanges()
	{
		domainCompletion.setAltStatus( chkAlt.isSelected() );
		domainCompletion.setCtrlStatus( chkCtrl.isSelected() );
		domainCompletion.setShiftStatus( chkShft.isSelected() );

		domainCompletion.setPrefix( txfPrefix.getText() );
		domainCompletion.setPostfix( txfPostfix.getText() );
	}

// Attributes:
	private XDomainCompletion domainCompletion;
	private JCheckBox chkAlt = XComponentBuilder.getInstance().buildCheckBox(this, "Alt");
	private JCheckBox chkCtrl = XComponentBuilder.getInstance().buildCheckBox(this, "Ctrl");
	private JCheckBox chkShft = XComponentBuilder.getInstance().buildCheckBox(this, "Shft");

    private JTextField txfPrefix = new JTextField(5);
    private JTextField txfPostfix = new JTextField(5);
}
