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
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;

import xbrowser.*;
import xbrowser.util.*;
import xbrowser.widgets.*;
import xbrowser.history.*;

public class XOptionsLayout extends XDialog
{
	public static XOptionsLayout getInstance()
	{
		if( instance==null )
			instance = new XOptionsLayout();

		return instance;
	}

	public static XOptionsLayout getInstance(String current_url)
	{
		if( instance==null )
			instance = new XOptionsLayout();

		instance.currentURL = current_url;
        instance.loadOptions();

		return instance;
	}

	private XOptionsLayout()
	{
	    super(true);

		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );
		getContentPane().setLayout(new BorderLayout(5,5));

	JPanel pnl_center = new JPanel( new BorderLayout(5,5) );
	JScrollPane scr_options = new JScrollPane(new XOptionsTree());

        scr_options.setPreferredSize( new Dimension(170,400) );
		pnlOptions.setBorder( BorderFactory.createEtchedBorder() );

		pnl_center.add(scr_options, BorderLayout.WEST);
		pnl_center.add(pnlOptions, BorderLayout.CENTER);

		getContentPane().add(pnl_center,BorderLayout.CENTER);
		getContentPane().add(getButtonsPanel(),BorderLayout.SOUTH);

        registerListeners();

        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setSize( new Dimension(570,400) );
        setResizable(false);
	}

	private void registerListeners()
	{
        chkUseHttpProxy.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				txfHttpProxyAddress.setEnabled(chkUseHttpProxy.isSelected());
				txfHttpProxyPort.setEnabled(chkUseHttpProxy.isSelected());
				lblHttpType.setEnabled(chkUseHttpProxy.isSelected());
				lblHttpColon.setEnabled(chkUseHttpProxy.isSelected());
			}
		});

        chkUseFtpProxy.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				txfFtpProxyAddress.setEnabled(chkUseFtpProxy.isSelected());
				txfFtpProxyPort.setEnabled(chkUseFtpProxy.isSelected());
				lblFtpType.setEnabled(chkUseFtpProxy.isSelected());
				lblFtpColon.setEnabled(chkUseFtpProxy.isSelected());
			}
		});

        chkUseSocksProxy.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				txfSocksProxyAddress.setEnabled(chkUseSocksProxy.isSelected());
				txfSocksProxyPort.setEnabled(chkUseSocksProxy.isSelected());
				lblSocksType.setEnabled(chkUseSocksProxy.isSelected());
				lblSocksColon.setEnabled(chkUseSocksProxy.isSelected());
			}
		});

        chkUseGopherProxy.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				txfGopherProxyAddress.setEnabled(chkUseGopherProxy.isSelected());
				txfGopherProxyPort.setEnabled(chkUseGopherProxy.isSelected());
				lblGopherType.setEnabled(chkUseGopherProxy.isSelected());
				lblGopherColon.setEnabled(chkUseGopherProxy.isSelected());
			}
		});

        cmbLNF.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
			XLookAndFeel lnf = (XLookAndFeel)cmbLNF.getSelectedItem();

				if( cmbTheme.getItemCount()>0 )// a workaround for Bug # ?? (calling removeAllItems() on an empty combobox causes exception)
					cmbTheme.removeAllItems();

				lblTheme.setEnabled(lnf.hasTheme());
				cmbTheme.setEnabled(lnf.hasTheme());

				if( lnf.hasTheme() )
				{
				Iterator themes = lnf.getThemes();

					while( themes.hasNext() )
						cmbTheme.addItem(themes.next());

					cmbTheme.setSelectedItem( lnf.getActiveTheme() );
				}
			}
		});

        cmbContentStyle.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				updateTabPlacement();
			}
		});

        chkDocumentSelector.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				cmbDocSelPlacement.setEnabled(chkDocumentSelector.isSelected());
			}
		});

        chkBookmarkPane.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				cmbBookmarkPlacement.setEnabled(chkBookmarkPane.isSelected());
			}
		});

        chkHistoryPane.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				cmbHistoryPlacement.setEnabled(chkHistoryPane.isSelected());
			}
		});
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

    private JComponent getGeneralPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl_home_page = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);
		pnl_home_page.setBorder( BorderFactory.createTitledBorder(XComponentBuilder.getInstance().getProperty(this, "HomePage")) );

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "HomePageDescription"),pnl_home_page,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Address"),pnl_home_page,gridbag,constraints,1,0);
		addToContainer(txfHomeAddress,pnl_home_page,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(XComponentBuilder.getInstance().buildButton(new CurrentURLAction()),pnl_home_page,gridbag,constraints,1,1);
		addToContainer(XComponentBuilder.getInstance().buildButton(new DefaultURLAction()),pnl_home_page,gridbag,constraints,1,1);
		addToContainer(XComponentBuilder.getInstance().buildButton(new BlankURLAction()),pnl_home_page,gridbag,constraints,1,1);
		addToContainer(XComponentBuilder.getInstance().buildButton(new BrowseAction()),pnl_home_page,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(chkStartsWithHomePage,pnl_home_page,gridbag,constraints,GridBagConstraints.REMAINDER,1);

    	gridbag = new GridBagLayout();
    	constraints = new GridBagConstraints();

    JPanel pnl_history = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);
		pnl_history.setBorder( BorderFactory.createTitledBorder(XComponentBuilder.getInstance().getProperty(this, "History")) );

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "HistoryDescription"),pnl_history,gridbag,constraints,GridBagConstraints.REMAINDER,0);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "HistoryDays"),pnl_history,gridbag,constraints,3,0);
		addToContainer(txfExpireDays,pnl_history,gridbag,constraints,GridBagConstraints.RELATIVE,0);
		addToContainer(XComponentBuilder.getInstance().buildButton(new ClearHistoryAction()),pnl_history,gridbag,constraints,GridBagConstraints.REMAINDER,0);

    	gridbag = new GridBagLayout();
    	constraints = new GridBagConstraints();

    JPanel pnl_general = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);

		addToContainer(pnl_home_page,pnl_general,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(pnl_history,pnl_general,gridbag,constraints,GridBagConstraints.REMAINDER,1);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weighty = 1;
		addToContainer(Box.createGlue(),pnl_general,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl_general;
	}

    private JComponent getViewPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl_view = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,2,5,2);

		cmbTabPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Bottom"));
		cmbTabPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Left"));
		cmbTabPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Right"));
		cmbTabPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Top"));

		cmbContentStyle.addItem(XComponentBuilder.getInstance().getProperty(this, "DesktopPane"));
		cmbContentStyle.addItem(XComponentBuilder.getInstance().getProperty(this, "TabbedPane"));
		cmbContentStyle.addItem(XComponentBuilder.getInstance().getProperty(this, "Workbook"));

		cmbToolBarStyle.addItem(XComponentBuilder.getInstance().getProperty(this, "IconOnlyStyle"));
		cmbToolBarStyle.addItem(XComponentBuilder.getInstance().getProperty(this, "TextOnlyStyle"));
		cmbToolBarStyle.addItem(XComponentBuilder.getInstance().getProperty(this, "TextIconStyle"));

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "LNF"),pnl_view,gridbag,constraints,1,1);
		addToContainer(cmbLNF,pnl_view,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_view,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(lblTheme,pnl_view,gridbag,constraints,1,1);
		addToContainer(cmbTheme,pnl_view,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_view,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "ContentStyle"),pnl_view,gridbag,constraints,1,1);
		addToContainer(cmbContentStyle,pnl_view,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_view,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(lblTabPlacement,pnl_view,gridbag,constraints,1,1);
		addToContainer(cmbTabPlacement,pnl_view,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_view,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "ToolBarStyle"),pnl_view,gridbag,constraints,1,1);
		addToContainer(cmbToolBarStyle,pnl_view,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_view,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Renderer"),pnl_view,gridbag,constraints,1,1);
		addToContainer(cmbRenderer,pnl_view,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_view,gridbag,constraints,GridBagConstraints.REMAINDER,1);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weighty = 1;
		addToContainer(Box.createGlue(),pnl_view,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl_view;
	}

    private JComponent getToolPanesPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl_panes = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,2,5,2);

		cmbDocSelPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page1"));
		cmbDocSelPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page2"));
		cmbDocSelPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page3"));

		cmbBookmarkPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page1"));
		cmbBookmarkPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page2"));
		cmbBookmarkPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page3"));

		cmbHistoryPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page1"));
		cmbHistoryPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page2"));
		cmbHistoryPlacement.addItem(XComponentBuilder.getInstance().getProperty(this, "Page3"));

		addToContainer(chkDocumentSelector,pnl_panes,gridbag,constraints,1,1);
		addToContainer(cmbDocSelPlacement,pnl_panes,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkBookmarkPane,pnl_panes,gridbag,constraints,1,1);
		addToContainer(cmbBookmarkPlacement,pnl_panes,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkHistoryPane,pnl_panes,gridbag,constraints,1,1);
		addToContainer(cmbHistoryPlacement,pnl_panes,gridbag,constraints,1,1);
		addToContainer(Box.createGlue(),pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkNavigationToolBar,pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(chkLocationToolBar,pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkPersonalToolBar,pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(chkStatusBar,pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weighty = 1;
		addToContainer(Box.createGlue(),pnl_panes,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl_panes;
	}

	private JComponent getDomainPage()
	{
    JPanel pnl_domain = new JPanel( new BorderLayout() );
    JPanel pnl_north = new JPanel();
    Iterator domain_completions = XProjectConfig.getInstance().getDomainCompletions();
    XDomainCompletionUI domain_ui;

		pnl_north.setLayout( new BoxLayout(pnl_north, BoxLayout.Y_AXIS) );

		pnl_north.add( buildOneLinePanel(XComponentBuilder.getInstance().buildLabel(this, "DomainCompletionDescription1")) );
		pnl_north.add( buildOneLinePanel(XComponentBuilder.getInstance().buildLabel(this, "DomainCompletionDescription2")) );
		pnl_north.add(Box.createVerticalStrut(10));

		while( domain_completions.hasNext() )
		{
			domain_ui = new XDomainCompletionUI( (XDomainCompletion)domain_completions.next() );
			domainCompletions.add(domain_ui);
			pnl_north.add(domain_ui);
		}

		pnl_north.add(Box.createVerticalStrut(10));
		pnl_north.add( buildOneLinePanel(XComponentBuilder.getInstance().buildButton(new DefaultDomainCompletionAction())) );

		pnl_domain.add(BorderLayout.NORTH, pnl_north);

		return pnl_domain;
	}

	private JPanel buildOneLinePanel(JComponent comp)
	{
	JPanel pnl = new JPanel( new BorderLayout() );

		pnl.add(BorderLayout.WEST, comp);
		pnl.add(BorderLayout.CENTER, Box.createGlue());

		return pnl;
	}

	private JComponent getConnectionPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl_connection = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);

		addToContainer(getConnectionPage1(),pnl_connection,gridbag,constraints,GridBagConstraints.REMAINDER,1);

        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weighty = 1;
		addToContainer(getConnectionPage2(),pnl_connection,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl_connection;
	}

    private JComponent getConnectionPage1()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl_connection = new JPanel(gridbag);
	char valid_chars[] = {'0','1','2','3','4','5','6','7','8','9'};

	    txfHttpProxyPort.setDocument( new XLimitedDocument(valid_chars) );
	    txfFtpProxyPort.setDocument( new XLimitedDocument(valid_chars) );
	    txfSocksProxyPort.setDocument( new XLimitedDocument(valid_chars) );
	    txfGopherProxyPort.setDocument( new XLimitedDocument(valid_chars) );
	    txfExpireDays.setDocument( new XLimitedDocument(valid_chars) );

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(2,2,2,2);
		pnl_connection.setBorder( BorderFactory.createEtchedBorder() );

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "UseProxy"),pnl_connection,gridbag,constraints,2,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "ProxyType"),pnl_connection,gridbag,constraints,2,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "ProxyAddress"),pnl_connection,gridbag,constraints,5,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "ProxyPort"),pnl_connection,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkUseHttpProxy,pnl_connection,gridbag,constraints,2,1);
		addToContainer(lblHttpType,pnl_connection,gridbag,constraints,2,1);
		addToContainer(txfHttpProxyAddress,pnl_connection,gridbag,constraints,4,1);
		addToContainer(lblHttpColon,pnl_connection,gridbag,constraints,1,0);
		addToContainer(txfHttpProxyPort,pnl_connection,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkUseFtpProxy,pnl_connection,gridbag,constraints,2,1);
		addToContainer(lblFtpType,pnl_connection,gridbag,constraints,2,1);
		addToContainer(txfFtpProxyAddress,pnl_connection,gridbag,constraints,4,1);
		addToContainer(lblFtpColon,pnl_connection,gridbag,constraints,1,0);
		addToContainer(txfFtpProxyPort,pnl_connection,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkUseGopherProxy,pnl_connection,gridbag,constraints,2,1);
		addToContainer(lblGopherType,pnl_connection,gridbag,constraints,2,1);
		addToContainer(txfGopherProxyAddress,pnl_connection,gridbag,constraints,4,1);
		addToContainer(lblGopherColon,pnl_connection,gridbag,constraints,1,0);
		addToContainer(txfGopherProxyPort,pnl_connection,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkUseSocksProxy,pnl_connection,gridbag,constraints,2,1);
		addToContainer(lblSocksType,pnl_connection,gridbag,constraints,2,1);
		addToContainer(txfSocksProxyAddress,pnl_connection,gridbag,constraints,4,1);
		addToContainer(lblSocksColon,pnl_connection,gridbag,constraints,1,0);
		addToContainer(txfSocksProxyPort,pnl_connection,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl_connection;
	}

    private JComponent getConnectionPage2()
    {
	JPanel pnl_connection = new JPanel( new BorderLayout() );
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
	JPanel pnl_north = new JPanel(gridbag);
	JPanel pnl_center = new JPanel( new BorderLayout() );

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "UserName"),pnl_north,gridbag,constraints,1,0);
		addToContainer(txfUserName,pnl_north,gridbag,constraints,2,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Password"),pnl_north,gridbag,constraints,1,0);
		addToContainer(txfPassword,pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		pnl_north.setBorder( new TitledBorder(XComponentBuilder.getInstance().getProperty(this, "AuthInfo")) );

		pnl_center.add(BorderLayout.NORTH, XComponentBuilder.getInstance().buildLabel(this, "ExceptionDescription"));
		pnl_center.add(BorderLayout.CENTER, new JScrollPane(txaExceptions, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		pnl_center.add(BorderLayout.SOUTH, XComponentBuilder.getInstance().buildLabel(this, "SeparatingEntries"));
		pnl_center.setBorder( new TitledBorder(XComponentBuilder.getInstance().getProperty(this, "Exception")) );

		pnl_connection.add(BorderLayout.NORTH, pnl_north);
		pnl_connection.add(BorderLayout.CENTER, pnl_center);
		pnl_connection.setBorder( BorderFactory.createEtchedBorder() );

		return pnl_connection;
	}

    private JPanel getButtonsPanel()
    {
    JPanel pnl_main = new JPanel();
    JPanel pnl_buttons = new JPanel();
	JButton btn_ok = XComponentBuilder.getInstance().buildButton(new OKAction());

        pnl_main.setLayout(new FlowLayout());
        pnl_buttons.setLayout(new GridLayout(1,2,15,5));
        pnl_buttons.add(btn_ok);
        pnl_buttons.add(XComponentBuilder.getInstance().buildButton(new CancelAction()));
        pnl_main.add(pnl_buttons);

        getRootPane().setDefaultButton(btn_ok);

        return pnl_main;
	}

    private void updateTabPlacement()
    {
		lblTabPlacement.setEnabled(cmbContentStyle.getSelectedIndex()!=0);
		cmbTabPlacement.setEnabled(cmbContentStyle.getSelectedIndex()!=0);
	}

    public void applyOptions()
    {
    XLookAndFeel lnf = (XLookAndFeel)cmbLNF.getSelectedItem();
    XProxyServer proxy;
    XViewTool tool;
    int index;

        proxy = new XProxyServer();
        proxy.setUseProxy( chkUseHttpProxy.isSelected() );
        proxy.setProxyAddress( txfHttpProxyAddress.getText() );
        proxy.setProxyPort( txfHttpProxyPort.getText() );
        XProjectConfig.getInstance().setHttpProxy(proxy);

        proxy = new XProxyServer();
        proxy.setUseProxy( chkUseFtpProxy.isSelected() );
        proxy.setProxyAddress( txfFtpProxyAddress.getText() );
        proxy.setProxyPort( txfFtpProxyPort.getText() );
        XProjectConfig.getInstance().setFtpProxy(proxy);

        proxy = new XProxyServer();
        proxy.setUseProxy( chkUseSocksProxy.isSelected() );
        proxy.setProxyAddress( txfSocksProxyAddress.getText() );
        proxy.setProxyPort( txfSocksProxyPort.getText() );
        XProjectConfig.getInstance().setSocksProxy(proxy);

        proxy = new XProxyServer();
        proxy.setUseProxy( chkUseGopherProxy.isSelected() );
        proxy.setProxyAddress( txfGopherProxyAddress.getText() );
        proxy.setProxyPort( txfGopherProxyPort.getText() );
        XProjectConfig.getInstance().setGopherProxy(proxy);

        XProjectConfig.getInstance().setUserName( txfUserName.getText() );
        XProjectConfig.getInstance().setPassword( new String(txfPassword.getPassword()) );
        XProjectConfig.getInstance().setProxyExceptions( txaExceptions.getText() );

        tool = new XViewTool();
        tool.setVisible( chkDocumentSelector.isSelected() );
		index = cmbDocSelPlacement.getSelectedIndex();
        if( index==0 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE1 );
        else if( index==1 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE2 );
        else if( index==2 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE3 );
        XProjectConfig.getInstance().setDocumentSelector(tool);

        tool = new XViewTool();
        tool.setVisible( chkBookmarkPane.isSelected() );
		index = cmbBookmarkPlacement.getSelectedIndex();
        if( index==0 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE1 );
        else if( index==1 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE2 );
        else if( index==2 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE3 );
        XProjectConfig.getInstance().setBookmarkPane(tool);

        tool = new XViewTool();
        tool.setVisible( chkHistoryPane.isSelected() );
		index = cmbHistoryPlacement.getSelectedIndex();
        if( index==0 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE1 );
        else if( index==1 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE2 );
        else if( index==2 )
        	tool.setPlacement( XProjectConstants.TOOLS_PAGE3 );
        XProjectConfig.getInstance().setHistoryPane(tool);

        XProjectConfig.getInstance().setHomeURL(txfHomeAddress.getText());
        XProjectConfig.getInstance().setStartsWithHomePage(chkStartsWithHomePage.isSelected());

       	try
       	{
       		XProjectConfig.getInstance().setHistoryExpirationDays( (new Integer(txfExpireDays.getText())).intValue() );
		}
		catch( Exception e )
		{
       		XProjectConfig.getInstance().setHistoryExpirationDays(0);
		}

    	index = cmbContentStyle.getSelectedIndex();
        if( index==0 )
            XProjectConfig.getInstance().setContentStyle(XProjectConstants.DESKTOP_PANE_CONTENT);
        else if( index==1 )
            XProjectConfig.getInstance().setContentStyle(XProjectConstants.TABBED_PANE_CONTENT);
        else if( index==2 )
            XProjectConfig.getInstance().setContentStyle(XProjectConstants.WORKBOOK_CONTENT);

    	index = cmbTabPlacement.getSelectedIndex();
        if( index==0 )
    	    XProjectConfig.getInstance().setContentTabPlacement(XProjectConstants.BOTTOM);
        else if( index==1 )
    	    XProjectConfig.getInstance().setContentTabPlacement(XProjectConstants.LEFT);
        else if( index==2 )
    	    XProjectConfig.getInstance().setContentTabPlacement(XProjectConstants.RIGHT);
        else if( index==3 )
    	    XProjectConfig.getInstance().setContentTabPlacement(XProjectConstants.TOP);

    	index = cmbToolBarStyle.getSelectedIndex();
        if( index==0 )
    	    XProjectConfig.getInstance().setToolBarStyle(XProjectConstants.TOOLBAR_STYLE_ICON_ONLY);
        else if( index==1 )
    	    XProjectConfig.getInstance().setToolBarStyle(XProjectConstants.TOOLBAR_STYLE_TEXT_ONLY);
        else if( index==2 )
    	    XProjectConfig.getInstance().setToolBarStyle(XProjectConstants.TOOLBAR_STYLE_TEXT_ICON);

        XProjectConfig.getInstance().setActiveRenderer( (XRendererObject)cmbRenderer.getSelectedItem() );

        if( lnf.hasTheme() )
        	lnf.setActiveTheme( (XTheme)cmbTheme.getSelectedItem() );
        XProjectConfig.getInstance().setActiveLNF(lnf);

        XProjectConfig.getInstance().setNavigationToolBar(chkNavigationToolBar.isSelected());
        XProjectConfig.getInstance().setPersonalToolBar(chkPersonalToolBar.isSelected());
        XProjectConfig.getInstance().setLocationToolBar(chkLocationToolBar.isSelected());
        XProjectConfig.getInstance().setStatusBar(chkStatusBar.isSelected());

    Iterator it = domainCompletions.iterator();

		while( it.hasNext() )
			((XDomainCompletionUI)it.next()).applyChanges();
	}

    public void loadOptions()
    {
    int place = XProjectConfig.getInstance().getContentTabPlacement();
    int style = XProjectConfig.getInstance().getContentStyle();
    XProxyServer proxy;
    XViewTool tool;

        proxy = XProjectConfig.getInstance().getHttpProxy();
        chkUseHttpProxy.setSelected(proxy.getUseProxy());
        txfHttpProxyAddress.setText(proxy.getProxyAddress());
        txfHttpProxyPort.setText(proxy.getProxyPort());

        proxy = XProjectConfig.getInstance().getFtpProxy();
        chkUseFtpProxy.setSelected(proxy.getUseProxy());
        txfFtpProxyAddress.setText(proxy.getProxyAddress());
        txfFtpProxyPort.setText(proxy.getProxyPort());

        proxy = XProjectConfig.getInstance().getSocksProxy();
        chkUseSocksProxy.setSelected(proxy.getUseProxy());
        txfSocksProxyAddress.setText(proxy.getProxyAddress());
        txfSocksProxyPort.setText(proxy.getProxyPort());

        proxy = XProjectConfig.getInstance().getGopherProxy();
        chkUseGopherProxy.setSelected(proxy.getUseProxy());
        txfGopherProxyAddress.setText(proxy.getProxyAddress());
        txfGopherProxyPort.setText(proxy.getProxyPort());

        txfUserName.setText(XProjectConfig.getInstance().getUserName());
        txfPassword.setText(XProjectConfig.getInstance().getPassword());
        txaExceptions.setText(XProjectConfig.getInstance().getProxyExceptions());

        tool = XProjectConfig.getInstance().getDocumentSelector();
        chkDocumentSelector.setSelected(tool.isVisible());
        if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE1 )
            cmbDocSelPlacement.setSelectedIndex(0);
        else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE2 )
            cmbDocSelPlacement.setSelectedIndex(1);
        else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE3 )
            cmbDocSelPlacement.setSelectedIndex(2);

        tool = XProjectConfig.getInstance().getBookmarkPane();
        chkBookmarkPane.setSelected(tool.isVisible());
        if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE1 )
            cmbBookmarkPlacement.setSelectedIndex(0);
        else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE2 )
            cmbBookmarkPlacement.setSelectedIndex(1);
        else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE3 )
            cmbBookmarkPlacement.setSelectedIndex(2);

        tool = XProjectConfig.getInstance().getHistoryPane();
        chkHistoryPane.setSelected(tool.isVisible());
        if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE1 )
            cmbHistoryPlacement.setSelectedIndex(0);
        else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE2 )
            cmbHistoryPlacement.setSelectedIndex(1);
        else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE3 )
            cmbHistoryPlacement.setSelectedIndex(2);

        txfHomeAddress.setText(XProjectConfig.getInstance().getHomeURL());
        chkStartsWithHomePage.setSelected(XProjectConfig.getInstance().getStartsWithHomePage());

       	txfExpireDays.setText(""+XProjectConfig.getInstance().getHistoryExpirationDays());

        if( style==XProjectConstants.DESKTOP_PANE_CONTENT )
            cmbContentStyle.setSelectedIndex(0);
        else if( style==XProjectConstants.TABBED_PANE_CONTENT )
            cmbContentStyle.setSelectedIndex(1);
        else if( style==XProjectConstants.WORKBOOK_CONTENT )
            cmbContentStyle.setSelectedIndex(2);

        if( place==XProjectConstants.BOTTOM )
            cmbTabPlacement.setSelectedIndex(0);
        else if( place==XProjectConstants.LEFT )
            cmbTabPlacement.setSelectedIndex(1);
        else if( place==XProjectConstants.RIGHT )
            cmbTabPlacement.setSelectedIndex(2);
        else if( place==XProjectConstants.TOP )
            cmbTabPlacement.setSelectedIndex(3);

    	style = XProjectConfig.getInstance().getToolBarStyle();
        if( style==XProjectConstants.TOOLBAR_STYLE_ICON_ONLY )
            cmbToolBarStyle.setSelectedIndex(0);
        else if( style==XProjectConstants.TOOLBAR_STYLE_TEXT_ONLY )
            cmbToolBarStyle.setSelectedIndex(1);
        else if( style==XProjectConstants.TOOLBAR_STYLE_TEXT_ICON )
            cmbToolBarStyle.setSelectedIndex(2);

    	if( cmbLNF.getItemCount()==0 )
    	{
		Iterator lnfs = XProjectConfig.getInstance().getLookAndFeels();

			while( lnfs.hasNext() )
				cmbLNF.addItem( lnfs.next() );
		}
        cmbLNF.setSelectedItem( XProjectConfig.getInstance().getActiveLNF() );

    	if( cmbRenderer.getItemCount()==0 )
    	{
		Iterator renderers = XProjectConfig.getInstance().getRenderers();

			while( renderers.hasNext() )
				cmbRenderer.addItem( renderers.next() );
		}
        cmbRenderer.setSelectedItem( XProjectConfig.getInstance().getActiveRenderer() );

		updateTabPlacement();

        chkNavigationToolBar.setSelected( XProjectConfig.getInstance().hasNavigationToolBar() );
        chkPersonalToolBar.setSelected( XProjectConfig.getInstance().hasPersonalToolBar() );
        chkLocationToolBar.setSelected( XProjectConfig.getInstance().hasLocationToolBar() );
        chkStatusBar.setSelected( XProjectConfig.getInstance().hasStatusBar() );

    Iterator it = domainCompletions.iterator();

		while( it.hasNext() )
			((XDomainCompletionUI)it.next()).load();
    }

	private class XOptionsTree extends JTree
	{
		public XOptionsTree()
		{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(XProjectConstants.PRODUCT_NAME);
		DefaultTreeModel tree_model = new DefaultTreeModel(root);

			setModel(tree_model);
			setCellRenderer( new XTreeCellRenderer() );
			getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			putClientProperty("JTree.lineStyle", "Angled");
			setEditable(false);

			getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e)
				{
				TreePath tree_path = e.getNewLeadSelectionPath();

					if( tree_path!=null )
					{
					TreeNode tree_node = (TreeNode)tree_path.getPath()[tree_path.getPath().length-1];

						if( tree_node instanceof XMutableTreeNode )
							((XMutableTreeNode)tree_node).handleSelection();
						else
							pnlOptions.removeAll();

						pnlOptions.revalidate();
						pnlOptions.repaint();
					}
				}
			});

			addTreeWillExpandListener( new TreeWillExpandListener() {
				public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException
				{
					throw new ExpandVetoException(e);
				}

				public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException
				{
				}
			});

		JComponent comp;

			comp = getGeneralPage();
			tree_model.insertNodeInto(new XMutableTreeNode("General", "image.General", comp), root, 0);
			pages.add(comp);

			comp = getViewPage();
			tree_model.insertNodeInto(new XMutableTreeNode("View", "image.View", comp), root, 1);
			pages.add(comp);

			comp = getToolPanesPage();
			tree_model.insertNodeInto(new XMutableTreeNode("ToolPanes", "image.ToolPanes", comp), root, 2);
			pages.add(comp);

			comp = getDomainPage();
			tree_model.insertNodeInto(new XMutableTreeNode("DomainCompletion", "image.Domain", comp), root, 3);
			pages.add(comp);

			comp = getConnectionPage();
			tree_model.insertNodeInto(new XMutableTreeNode("Connection", "image.Connection", comp), root, 4);
			pages.add(comp);

			expandRow(0);
			setSelectionRow(1);
		}

		public void updateUI()
		{
			super.updateUI();

			if( pages!=null )
			{
			Iterator it = pages.iterator();

				while( it.hasNext() )
					SwingUtilities.updateComponentTreeUI( (JComponent)it.next() );
			}
		}

		private class XTreeCellRenderer extends DefaultTreeCellRenderer
		{
			public Icon getLeafIcon()
			{
				if( currentNode==null )
					return super.getLeafIcon();
				else
					return currentNode.getIcon();
			}

			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
			{
				if( value instanceof XMutableTreeNode )
					currentNode = (XMutableTreeNode)value;
				else
					currentNode = null;

				return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			}

		// Attributes:
			private XMutableTreeNode currentNode = null;
		}

		private class XMutableTreeNode extends DefaultMutableTreeNode
		{
			public XMutableTreeNode(String name_key, String icon_key, JComponent comp)
			{
				super( XComponentBuilder.getInstance().getProperty(XOptionsLayout.this, name_key) );
				this.icon = XComponentBuilder.getInstance().buildImageIcon(XOptionsLayout.this, icon_key);
				this.comp = comp;
			}

			public void handleSelection()
			{
				pnlOptions.removeAll();
				pnlOptions.add(comp, BorderLayout.CENTER);
			}

			public boolean isLeaf()
			{
				return true;
			}

			public ImageIcon getIcon()
			{
				return icon;
			}

		// Attributes:
			private ImageIcon icon;
			private JComponent comp;
		}

	// Attributes:
		private LinkedList pages = new LinkedList();
	}

    private class OKAction extends XAction
    {
        public OKAction()
        {
            super(XOptionsLayout.this, "Ok", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
		    applyOptions();
        }
    }

    private class CancelAction extends XAction
    {
        public CancelAction()
        {
            super(XOptionsLayout.this, "Cancel", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
        }
    }

    private class CurrentURLAction extends XAction
    {
        public CurrentURLAction()
        {
            super(XOptionsLayout.this, "UseCurrent", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    txfHomeAddress.setText(currentURL);
        }
    }

    private class DefaultURLAction extends XAction
    {
        public DefaultURLAction()
        {
            super(XOptionsLayout.this, "UseDefault", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    txfHomeAddress.setText(XProjectConstants.DEFAULT_HOME_URL);
        }
    }

    private class BlankURLAction extends XAction
    {
        public BlankURLAction()
        {
            super(XOptionsLayout.this, "UseBlank", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    txfHomeAddress.setText("");
        }
    }

    private class BrowseAction extends XAction
    {
        public BrowseAction()
        {
            super(XOptionsLayout.this, "Browse", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		JFileChooser file_chooser = XComponentBuilder.getInstance().getLoadSaveFileChooser();

			if( file_chooser.showOpenDialog(XOptionsLayout.this)==JFileChooser.APPROVE_OPTION )
			{
			File selected_file = file_chooser.getSelectedFile();

				if( (selected_file!=null) && (selected_file.exists()) )
				{
					try
					{
						txfHomeAddress.setText(selected_file.toURL().toString());
					}
					catch( Exception ex )
					{
						txfHomeAddress.setText(selected_file.toString());
					}
					//txfHomeAddress.setText("file:"+System.getProperty("file.separator")+selected_file.toString());
				}
			}
        }
    }

    private class ClearHistoryAction extends XAction
    {
        public ClearHistoryAction()
        {
            super(XOptionsLayout.this, "ClearHistory", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XHistoryManager.getInstance().removeAllHistory();
        }
    }

    private class DefaultDomainCompletionAction extends XAction
    {
        public DefaultDomainCompletionAction()
        {
            super(XOptionsLayout.this, "DefaultDomainCompletion", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		Iterator it = domainCompletions.iterator();

			while( it.hasNext() )
				((XDomainCompletionUI)it.next()).loadDefaults();
        }
    }

// Attributes:
	private static XOptionsLayout instance = null;
    private String currentURL = "";

    private JTextField txfHomeAddress = new JTextField(20);
    private JComboBox cmbLNF = new JComboBox();
    private JComboBox cmbContentStyle = new JComboBox();
    private JComboBox cmbRenderer = new JComboBox();
    private JLabel lblTheme = XComponentBuilder.getInstance().buildLabel(this, "Theme");
    private JComboBox cmbTheme = new JComboBox();
    private JCheckBox chkStartsWithHomePage = XComponentBuilder.getInstance().buildCheckBox(this, "StartHomePage");
    private JLabel lblTabPlacement = XComponentBuilder.getInstance().buildLabel(this, "TabPlacement");
    private JComboBox cmbTabPlacement = new JComboBox();
    private JComboBox cmbToolBarStyle = new JComboBox();

    private JCheckBox chkDocumentSelector = XComponentBuilder.getInstance().buildCheckBox(this, "DocumentSelector");
    private JCheckBox chkBookmarkPane = XComponentBuilder.getInstance().buildCheckBox(this, "BookmarkPane");
    private JCheckBox chkHistoryPane = XComponentBuilder.getInstance().buildCheckBox(this, "HistoryPane");
    private JComboBox cmbDocSelPlacement = new JComboBox();
    private JComboBox cmbBookmarkPlacement = new JComboBox();
    private JComboBox cmbHistoryPlacement = new JComboBox();
    private JCheckBox chkNavigationToolBar = XComponentBuilder.getInstance().buildCheckBox(this, "NavigationToolbar");
    private JCheckBox chkLocationToolBar = XComponentBuilder.getInstance().buildCheckBox(this, "LocationToolbar");
    private JCheckBox chkPersonalToolBar = XComponentBuilder.getInstance().buildCheckBox(this, "PersonalToolbar");
    private JCheckBox chkStatusBar = XComponentBuilder.getInstance().buildCheckBox(this, "Statusbar");

    private JCheckBox chkUseHttpProxy = new JCheckBox();
    private JTextField txfHttpProxyAddress = new JTextField(20);
    private JTextField txfHttpProxyPort = new JTextField(5);
    private JLabel lblHttpType = new JLabel("HTTP");
    private JLabel lblHttpColon = new JLabel(":");
    private JCheckBox chkUseFtpProxy = new JCheckBox();
    private JTextField txfFtpProxyAddress = new JTextField(20);
    private JTextField txfFtpProxyPort = new JTextField(5);
    private JLabel lblFtpType = new JLabel("FTP");
    private JLabel lblFtpColon = new JLabel(":");
    private JCheckBox chkUseSocksProxy = new JCheckBox();
    private JTextField txfSocksProxyAddress = new JTextField(20);
    private JTextField txfSocksProxyPort = new JTextField(5);
    private JLabel lblSocksType = new JLabel("Socks");
    private JLabel lblSocksColon = new JLabel(":");
    private JCheckBox chkUseGopherProxy = new JCheckBox();
    private JTextField txfGopherProxyAddress = new JTextField(20);
    private JTextField txfGopherProxyPort = new JTextField(5);
    private JLabel lblGopherType = new JLabel("Gopher");
    private JLabel lblGopherColon = new JLabel(":");

    private JTextField txfExpireDays = new JTextField(4);
    private JTextField txfUserName = new JTextField(7);
    private JPasswordField txfPassword = new JPasswordField(7);
    private JTextArea txaExceptions = new JTextArea();

    private JPanel pnlOptions = new JPanel( new BorderLayout() );

    private LinkedList domainCompletions = new LinkedList();
}
