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

import xbrowser.widgets.*;
import xbrowser.history.*;
import xbrowser.bookmark.*;

public class XFindLayout2 extends XFrame
{
	public static XFindLayout2 getInstance(int search_type)
	{
		if( instance==null )
			instance = new XFindLayout2();

		instance.setSearchType(search_type);

		return instance;
	}

	public static XFindLayout2 getInstance()
	{
		return getInstance(SEARCH_BOOKMARKS);
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

	private XFindLayout2()
	{
		setTitle( XComponentBuilder.getInstance().getProperty(this, "Title") );
        registerListeners();

    	btnFind = XComponentBuilder.getInstance().buildButton(new FindAction());

	ImageIcon icon;

		icon = XComponentBuilder.getInstance().buildImageIcon(this, "image.Bookmark");
		tabMain.addTab(XComponentBuilder.getInstance().getProperty(this, "BookmarkTab"), icon, getBookmarkPage());

		icon = XComponentBuilder.getInstance().buildImageIcon(this, "image.History");
		tabMain.addTab(XComponentBuilder.getInstance().getProperty(this, "HistoryTab"), icon, getHistoryPage());

	JPanel pnl_east_north = new JPanel( new GridLayout(3,1,0,10) );
	JPanel pnl_east = new JPanel( new BorderLayout() );

		getContentPane().setLayout( new BorderLayout(10,5) );

		pnl_east_north.add( Box.createVerticalGlue() );
		pnl_east_north.add( btnFind );
		pnl_east_north.add( XComponentBuilder.getInstance().buildButton(new CloseAction()) );

		pnl_east.add(pnl_east_north, BorderLayout.NORTH);

		getContentPane().add(tabMain, BorderLayout.CENTER);
		getContentPane().add(pnl_east, BorderLayout.EAST);

        setIconImage( XComponentBuilder.getInstance().buildImageIcon(this, "image.FrameIcon").getImage() );
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getRootPane().setDefaultButton(btnFind);

        pack();

    Dimension size = getSize();

        size.height = 350;
        setSize(size);
	}

	public void setSearchType(int search_type)
	{
		if( search_type==SEARCH_HISTORY )
			tabMain.setSelectedIndex(1);
		else if( search_type==SEARCH_BOOKMARKS )
			tabMain.setSelectedIndex(0);
	}

    private void registerListeners()
    {
        txfBookmarkFindPhrase.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				btnFind.doClick();
			}
		});

        txfHistoryFindPhrase.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				btnFind.doClick();
			}
		});

		cmbHistoryTime.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
    			edtHistoryVisitDate1.setEnabled( cmbHistoryTime.getSelectedIndex()!=0 );

    			lblAnd.setEnabled( cmbHistoryTime.getSelectedIndex()==3 ); //Between
    			edtHistoryVisitDate2.setEnabled( cmbHistoryTime.getSelectedIndex()==3 ); //Between
			}
		});
	}

    private JPanel getBookmarkPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl_north = new JPanel(gridbag);
    JPanel pnl = new JPanel( new BorderLayout() );
	XBookmarkTable bookmark_table = new XBookmarkTable(bookmarkFinder);

		bookmark_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "FindSubject"),pnl_north,gridbag,constraints,1,0);
		addToContainer(txfBookmarkFindPhrase,pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "BaseFolder"),pnl_north,gridbag,constraints,1,0);
		addToContainer(cmbBaseFolder,pnl_north,gridbag,constraints,GridBagConstraints.RELATIVE,1);
		addToContainer(chkSubFolder,pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkBookmarkMatchCase,pnl_north,gridbag,constraints,2,1);
		addToContainer(chkBookmarkURL,pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);
		addToContainer(chkBookmarkDescription,pnl_north,gridbag,constraints,2,1);
		addToContainer(chkBookmarkTitle,pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);

        pnl.add(pnl_north, BorderLayout.NORTH);
        pnl.add(new JScrollPane(bookmark_table), BorderLayout.CENTER);

		return pnl;
	}

    private JPanel getHistoryPage()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl_north = new JPanel(gridbag);
    JPanel pnl = new JPanel( new BorderLayout() );
	XHistoryTable history_table = new XHistoryTable(historyFinder);

		history_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);

		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "FindSubject"),pnl_north,gridbag,constraints,1,0);
		addToContainer(txfHistoryFindPhrase,pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		addToContainer(chkHistoryURL,pnl_north,gridbag,constraints,2,1);
		addToContainer(chkHistoryTitle,pnl_north,gridbag,constraints,2,1);
		addToContainer(chkHistoryMatchCase,pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);

        constraints.gridheight = 2;
		addToContainer(getLastVisitedPanel(),pnl_north,gridbag,constraints,GridBagConstraints.REMAINDER,1);

        pnl.add(pnl_north, BorderLayout.NORTH);
        pnl.add(new JScrollPane(history_table), BorderLayout.CENTER);

		return pnl;
	}

    private JPanel getLastVisitedPanel()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pnl = new JPanel(gridbag);

		pnl.setBorder( BorderFactory.createTitledBorder(XComponentBuilder.getInstance().getProperty(this, "VisitDate")) );

		cmbHistoryTime.addItem(" ");
		cmbHistoryTime.addItem(XComponentBuilder.getInstance().getProperty(this, "After"));
		cmbHistoryTime.addItem(XComponentBuilder.getInstance().getProperty(this, "Before"));
		cmbHistoryTime.addItem(XComponentBuilder.getInstance().getProperty(this, "Between"));

		cmbHistoryTime.setSelectedIndex(0);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);

		addToContainer(cmbHistoryTime,pnl,gridbag,constraints,1,1);
		addToContainer(edtHistoryVisitDate1,pnl,gridbag,constraints,1,1);
		addToContainer(lblAnd,pnl,gridbag,constraints,1,0);
		addToContainer(edtHistoryVisitDate2,pnl,gridbag,constraints,GridBagConstraints.REMAINDER,1);

		return pnl;
	}

    private class FindAction extends XAction
    {
        public FindAction()
        {
            super(XFindLayout2.this, "Find", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			if( tabMain.getSelectedIndex()==0 )
			{
				bookmarkFinder.setFindPhrase(txfBookmarkFindPhrase.getText());
				bookmarkFinder.setBaseFolder(cmbBaseFolder.getSelectedBookmark());
				bookmarkFinder.setSearchSubFolders(chkSubFolder.isSelected());
				bookmarkFinder.setSearchUrls(chkBookmarkURL.isSelected());
				bookmarkFinder.setSearchTitles(chkBookmarkTitle.isSelected());
				bookmarkFinder.setSearchDescriptions(chkBookmarkDescription.isSelected());
				bookmarkFinder.setMatchCase(chkBookmarkMatchCase.isSelected());

				bookmarkFinder.find();
			}
			else if( tabMain.getSelectedIndex()==1 )
			{
				historyFinder.setFindPhrase(txfHistoryFindPhrase.getText());
				historyFinder.setSearchUrls(chkHistoryURL.isSelected());
				historyFinder.setSearchTitles(chkHistoryTitle.isSelected());
				historyFinder.setMatchCase(chkHistoryMatchCase.isSelected());

				historyFinder.setSearchVisitDate(cmbHistoryTime.getSelectedIndex()!=0);
				historyFinder.setVisitDate1(edtHistoryVisitDate1.getDate());
				historyFinder.setVisitDate2(edtHistoryVisitDate2.getDate());

			int sel_index = cmbHistoryTime.getSelectedIndex();

				if( sel_index==1 )
					historyFinder.setVisitDateRange(XHistoryFinder.AFTER_DATE);
				else if( sel_index==2 )
					historyFinder.setVisitDateRange(XHistoryFinder.BEFORE_DATE);
				else if( sel_index==3 )
					historyFinder.setVisitDateRange(XHistoryFinder.BETWEEN_DATES);
				else
					historyFinder.setVisitDateRange(-1);

				historyFinder.find();
			}
        }
    }

    private class CloseAction extends XAction
    {
        public CloseAction()
        {
            super(XFindLayout2.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
        }
    }

// Attributes:
	public static final int SEARCH_HISTORY = 10;
	public static final int SEARCH_BOOKMARKS = 11;

	private static XFindLayout2 instance = null;
	private XHistoryFinder historyFinder = new XHistoryFinder();
	private XBookmarkFinder bookmarkFinder = new XBookmarkFinder();

	private XBookmarkFolderComboBox cmbBaseFolder = new XBookmarkFolderComboBox();

    private JCheckBox chkSubFolder = XComponentBuilder.getInstance().buildCheckBox(this, "SearchSubFolders", true);
    private JCheckBox chkBookmarkURL = XComponentBuilder.getInstance().buildCheckBox(this, "SearchURLs", true);
    private JCheckBox chkBookmarkDescription = XComponentBuilder.getInstance().buildCheckBox(this, "SearchDescriptions");
    private JCheckBox chkBookmarkTitle = XComponentBuilder.getInstance().buildCheckBox(this, "SearchTitles", true);
    private JCheckBox chkBookmarkMatchCase = XComponentBuilder.getInstance().buildCheckBox(this, "CaseSensitive");
    private JCheckBox chkHistoryURL = XComponentBuilder.getInstance().buildCheckBox(this, "SearchURLs", true);
    private JCheckBox chkHistoryTitle = XComponentBuilder.getInstance().buildCheckBox(this, "SearchTitles", true);
    private JCheckBox chkHistoryMatchCase = XComponentBuilder.getInstance().buildCheckBox(this, "CaseSensitive");

    private JComboBox cmbHistoryTime = new JComboBox();
    private JTabbedPane tabMain = new JTabbedPane();
    private JButton btnFind;

    private JTextField txfBookmarkFindPhrase = new JTextField(20);
    private JTextField txfHistoryFindPhrase = new JTextField(20);

    private JLabel lblAnd = XComponentBuilder.getInstance().buildLabel(this, "And");

    private XDateTimeEditor edtHistoryVisitDate1 = new XDateTimeEditor(XDateTimeEditor.DATETIME, java.text.DateFormat.MEDIUM);
    private XDateTimeEditor edtHistoryVisitDate2 = new XDateTimeEditor(XDateTimeEditor.DATETIME, java.text.DateFormat.MEDIUM);
}
