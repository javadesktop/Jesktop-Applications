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
import java.net.*;
import java.util.*;
import java.beans.*;
import java.text.*;
import java.awt.event.*;
import java.awt.print.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.*;

import xbrowser.*;
import xbrowser.doc.*;
import xbrowser.doc.event.*;
import xbrowser.content.*;
import xbrowser.content.event.*;
import xbrowser.bookmark.*;
import xbrowser.history.*;
import xbrowser.renderer.*;
import xbrowser.util.*;
import xbrowser.widgets.*;
import xbrowser.widgets.event.*;

import org.jesktop.frimble.*;

public class XBrowserLayout extends JFrimble implements XIBrowser, XContentListener, XDocumentListener, PropertyChangeListener
{
  	public static void main(String[] args)
  	{
  	    /*System.out.println("Your locale : "+java.util.Locale.getDefault());
  	    System.out.println("Available locales :");

  	java.util.Locale locales[] = java.util.Locale.getAvailableLocales();

  		for( int i=0; i<locales.length; i++ )
  			System.out.print(locales[i]+" ,");*/

  	    /*if( findParameterPosition("-log", args)!=-1 )
  	    {
		    try
		    {
		        System.setOut( new PrintStream(new FileOutputStream("out.log",true)) );
		        System.setErr( new PrintStream(new FileOutputStream("err.log",true)) );
		    }
		    catch( Exception e )
		    {
		        System.out.println("Error on redirecting the streams !!");
		    }
        }*/

  	int lang_pos = findParameterPosition("-language", args);
  	int country_pos = findParameterPosition("-country", args);

  		if( lang_pos!=-1 || country_pos!=-1 )
  		{
		String lang = (lang_pos!=-1) ? args[lang_pos+1] : "";
		String country = (country_pos!=-1) ? args[country_pos+1] : "";

			Locale.setDefault( new java.util.Locale(lang, country) );
		}

  		new XBrowserLayout();
  	}

	private static int findParameterPosition(String param_name, String[] args)
	{
		if( args.length==0 )
			return -1;

		for( int i=0; i<args.length; i++ )
		{
			if( args[i].equalsIgnoreCase(param_name) )
				return i;
		}

		return -1;
	}

	public XBrowserLayout()
	{
  		new XBrowser(this);
	}

	public void init(XSplashScreen splash)
	{
	    setTitle(XProjectConstants.PRODUCT_NAME);
 		buildActions();

        splash.setStatus(XComponentBuilder.getInstance().getProperty(this, "InitToolPanes"));
    	buildDocumentSelector();
    	buildBookmarkPane();
    	buildHistoryPane();
    	XBookmarkPropertiesLayout.getInstance();
    	mnuBookmarkQuickFile = new XBookmarkMenu();
        cmbURL = new XURLComboBox();

		buildMainToolBar();
		buildLocationBar();
		statusBar = new XStatusBar(false,true);

        splash.setStatus(XComponentBuilder.getInstance().getProperty(this, "InitScreens"));
    	//XFindLayout.getInstance();
    	XFindLayout2.getInstance();
    	XHistoryLayout.getInstance();
    	//XOptionsLayout.getInstance();
    	XImportExportBookmarksLayout.getInstance();

        registerListeners();
        constructPopupMenus();

    	getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnlNorth, BorderLayout.NORTH);
	    getContentPane().add(pnlCenter, BorderLayout.CENTER);
	    setJMenuBar( constructMenuBar() );

		addDnDAbilityTo( lstDocumentSelector );
		addDnDAbilityTo( cmbURL.getEditor().getEditorComponent() );

        setIconImage( XComponentBuilder.getInstance().buildImageIcon(this, "image.FrameIcon").getImage() );
        setSize(windowSize);
    	setActiveDocument(null);

        splash.setStatus(XComponentBuilder.getInstance().getProperty(this, "LoadingConfig"));
		XProjectConfig.getInstance().load();
        splash.setStatus(XComponentBuilder.getInstance().getProperty(this, "LoadingBookmarks"));
		XBookmarkManager.getInstance().load();
        splash.setStatus(XComponentBuilder.getInstance().getProperty(this, "LoadingHistory"));
		XHistoryManager.getInstance().load();

		//applyResourceBundle(new XResource());

    	XJavaConsole.getInstance();
		setVisible(true);
	}

/*	private class XResource extends ListResourceBundle
	{
		public Object[][] getContents()
		{
			return contents;
		}

		private Object[][] contents = { {"Orientation", ComponentOrientation.RIGHT_TO_LEFT} };
	}*/

  	private void addDnDAbilityTo(Component comp)
	{
		new DropTarget(comp, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetListener() {
			public void drop(DropTargetDropEvent dtde)
			{
				try
				{
				DataFlavor flavor = XObjectSelection.localObjectFlavor;
				Transferable tr = dtde.getTransferable();

					if( tr.isDataFlavorSupported(flavor) && (tr.getTransferData(flavor) instanceof XAbstractBookmark) )
					{
					XAbstractBookmark abs_bm = (XAbstractBookmark)tr.getTransferData(flavor);

						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						abs_bm.open();
						dtde.dropComplete(true);
					}
					else
						dtde.rejectDrop();
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}

			public void dragEnter(DropTargetDragEvent e) {}
			public void dragExit(DropTargetEvent e) {}
			public void dragOver(DropTargetDragEvent e) {}
			public void dropActionChanged(DropTargetDragEvent e) {}
		});
	}

	private void buildDocumentSelector()
	{
		lstDocumentSelector = new XDocumentList();
		documentSelectorComponent = createToolViewPanel(new JScrollPane(lstDocumentSelector), "DocumentTab", new CloseDocumentSelectorAction());
	}

	private void buildBookmarkPane()
	{
		bookmarkPaneComponent = createToolViewPanel(new JScrollPane(new XBookmarkTree()), "BookmarkTab", new CloseBookmarkPaneAction());
	}

	private void buildHistoryPane()
	{
	XHistoryTable history_table = new XHistoryTable( XHistoryManager.getInstance() );

		history_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		historyPaneComponent = createToolViewPanel(new JScrollPane(history_table), "HistoryTab", new CloseHistoryPaneAction());
	}

    private JPanel createToolViewPanel(Component comp, String name_key, final XAction close_action)
    {
	final JPanel pnl = new JPanel( new BorderLayout(0,0) );
	JPanel pnl_north = new  JPanel( new BorderLayout(0,0) );

		pnl_north.add(XComponentBuilder.getInstance().buildLabel(this, name_key), BorderLayout.WEST);
		pnl_north.add(XComponentBuilder.getInstance().buildFlatButton(close_action, -1, -1), BorderLayout.EAST);

		pnl.add(pnl_north,BorderLayout.NORTH);
		pnl.add(comp,BorderLayout.CENTER);
		pnl.setBorder(BorderFactory.createTitledBorder(""));
		//pnl.setBorder(BorderFactory.createTitledBorder(XComponentBuilder.getInstance().getProperty(this, name_key)));

		/*pnl.addMouseListener( new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				showPopup(e);
			}

			public void mousePressed(MouseEvent e)
			{
				showPopup(e);
			}

			public void mouseClicked(MouseEvent e)
			{
				showPopup(e);
			}

			private void showPopup(MouseEvent e)
			{
      			if( e.isPopupTrigger() )
      			{
				JPopupMenu popup = new XPopupMenu();

				    popup.add( XComponentBuilder.getInstance().buildMenuItem(close_action) );
      				popup.show(pnl, e.getX(), e.getY());
				}
			}
		});*/

		return pnl;
	}

    private void buildLocationBar()
    {
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JButton btn_go = XComponentBuilder.getInstance().buildFlatButton(new GoAction(),30,25);

		btn_go = XComponentBuilder.getInstance().buildBnWLook(btn_go);

		pnlLocationBar = new JPanel(gridbag);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,0,0,10);
		addToContainer(XComponentBuilder.getInstance().buildFlatButton(new BookmarkQuickFileAction(),100,25),pnlLocationBar,gridbag,constraints,3,0);

        constraints.insets = new Insets(0,2,0,2);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "Address"),pnlLocationBar,gridbag,constraints,2,0);
		addToContainer(cmbURL,pnlLocationBar,gridbag,constraints,GridBagConstraints.RELATIVE,1);
		addToContainer(btn_go,pnlLocationBar,gridbag,constraints,GridBagConstraints.REMAINDER,0);
    	pnlLocationBar.setBorder( BorderFactory.createTitledBorder("") );
	}

    private void buildMainToolBar()
    {
	XPopupButton btn_back = XComponentBuilder.getInstance().buildPopupButton(backAction);
	XPopupButton btn_forward = XComponentBuilder.getInstance().buildPopupButton(forwardAction);
	Dimension separator_size = new Dimension(20,20);

		tlbNavigation = new JToolBar();

		mnuBackwardHistory = new XBackForwardHistoryPopup(XRenderer.BACKWARD_HISTORY);
		mnuForwardHistory = new XBackForwardHistoryPopup(XRenderer.FORWARD_HISTORY);

		btn_back.setPopupMenu(mnuBackwardHistory);
		btn_forward.setPopupMenu(mnuForwardHistory);

        tlbNavigation.setFloatable(false);

	    tlbNavigation.add(btn_back);
	    tlbNavigation.add(btn_forward);
		tlbNavigation.addSeparator(separator_size);

		tlbNavigation.add( XComponentBuilder.getInstance().buildToolbarButton(stopAction) );
		tlbNavigation.add( XComponentBuilder.getInstance().buildToolbarButton(reloadAction) );
		tlbNavigation.add( XComponentBuilder.getInstance().buildToolbarButton(homeAction) );
    	tlbNavigation.addSeparator(separator_size);

		tlbNavigation.add( XComponentBuilder.getInstance().buildToolbarButton(findAction) );
		tlbNavigation.add( XComponentBuilder.getInstance().buildToolbarButton(printAction) );
		tlbNavigation.add( XComponentBuilder.getInstance().buildToolbarButton(historyAction) );
    	tlbNavigation.addSeparator(separator_size);

		tlbNavigation.add( XComponentBuilder.getInstance().buildToolbarButton(newPageAction) );
	    //tlbNavigation.add( Box.createGlue() );
    	//tlbNavigation.add( XComponentBuilder.getInstance().buildSignButton(new XBrowserSignAction()) );
	}

    private void constructPopupMenus()
    {
		rightPopup = new XPopupMenu();
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(backAction) );
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(forwardAction) );
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(reloadAction) );
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(stopAction) );
		rightPopup.addSeparator();
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(addToBookmarksAction) );
		rightPopup.addSeparator();
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(pageSourceAction) );
		rightPopup.addSeparator();
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(saveAsAction) );
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(closeAction) );
		rightPopup.addSeparator();
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(selectAllAction) );
	    rightPopup.add( XComponentBuilder.getInstance().buildMenuItem(copyAction) );

		hyperLinkPopup = new XPopupMenu();
	    hyperLinkPopup.add( XComponentBuilder.getInstance().buildMenuItem(openInNewWindowAction) );
	    hyperLinkPopup.add( XComponentBuilder.getInstance().buildMenuItem(openInSameWindowAction) );
		hyperLinkPopup.addSeparator();
	    hyperLinkPopup.add( XComponentBuilder.getInstance().buildMenuItem(copyAction) );
	    hyperLinkPopup.add( XComponentBuilder.getInstance().buildMenuItem(new CopyHyperLinkAction()) );
		hyperLinkPopup.addSeparator();
	    hyperLinkPopup.add( XComponentBuilder.getInstance().buildMenuItem(addToBookmarksAction) );

		comboBoxPopup = new XPopupMenu();
	    comboBoxPopup.add( XComponentBuilder.getInstance().buildMenuItem(copyAction) );
	    comboBoxPopup.add( XComponentBuilder.getInstance().buildMenuItem(cutAction) );
	    comboBoxPopup.add( XComponentBuilder.getInstance().buildMenuItem(pasteAction) );
	    comboBoxPopup.add( XComponentBuilder.getInstance().buildMenuItem(selectAllAction) );
	}

    private JMenuBar constructMenuBar()
    {
    JMenu mnuFile = XComponentBuilder.getInstance().buildMenu(this, "File");
	JMenu mnuEdit = XComponentBuilder.getInstance().buildMenu(this, "Edit");
	JMenu mnuView = XComponentBuilder.getInstance().buildMenu(this, "View");
	JMenu mnuHistory = XComponentBuilder.getInstance().buildMenu(this, "History");
	JMenu mnuOptions = XComponentBuilder.getInstance().buildMenu(this, "Options");
	JMenu mnuWindow = XComponentBuilder.getInstance().buildMenu(this, "Window");
	JMenu mnuHelp = XComponentBuilder.getInstance().buildMenu(this, "Help");
  	JMenuBar mnubarMain = new JMenuBar();

		mnuOpenDocuments = new XOpenDocumentsMenu();

	    mnubarMain.add(mnuFile);
    	mnubarMain.add(mnuEdit);
	    mnubarMain.add(mnuView);
	    mnubarMain.add( new XBookmarkMenu() );
	    mnubarMain.add(mnuHistory);
	    mnubarMain.add(mnuOptions);
		mnubarMain.add(mnuWindow);
    	mnubarMain.add(mnuHelp);

	    mnuFile.add( XComponentBuilder.getInstance().buildMenuItem(newPageAction) );
	    mnuFile.add( XComponentBuilder.getInstance().buildMenuItem(new OpenFileAction()) );
	    mnuFile.add( XComponentBuilder.getInstance().buildMenuItem(new OpenURLAction()) );
	    mnuFile.add( XComponentBuilder.getInstance().buildMenuItem(saveAsAction) );
	    mnuFile.addSeparator();
	    mnuFile.add( XComponentBuilder.getInstance().buildMenuItem(printPreviewAction) );
	    mnuFile.add( XComponentBuilder.getInstance().buildMenuItem(printAction) );
	    mnuFile.addSeparator();
	    mnuFile.add( XComponentBuilder.getInstance().buildMenuItem(new ExitAction()) );

	    mnuEdit.add( XComponentBuilder.getInstance().buildMenuItem(cutAction) );
	    mnuEdit.add( XComponentBuilder.getInstance().buildMenuItem(copyAction) );
	    mnuEdit.add( XComponentBuilder.getInstance().buildMenuItem(pasteAction) );
	    mnuEdit.addSeparator();
	    mnuEdit.add( XComponentBuilder.getInstance().buildMenuItem(selectAllAction) );
	    mnuEdit.addSeparator();
	    mnuEdit.add( XComponentBuilder.getInstance().buildMenuItem(findAction) );
	    mnuEdit.add( XComponentBuilder.getInstance().buildMenuItem(findAgainAction) );

	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(reloadAction) );
	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(refreshAction) );
	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(stopAction) );
	    mnuView.addSeparator();
	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(pageSourceAction) );
		mnuView.addSeparator();
	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(backAction) );
	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(forwardAction) );
	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(homeAction) );
		mnuView.addSeparator();
	    mnuView.add( XComponentBuilder.getInstance().buildMenuItem(new JavaConsoleAction()) );

	    mnuHistory.add( XComponentBuilder.getInstance().buildMenuItem(historyAction) );
	    mnuHistory.add( XComponentBuilder.getInstance().buildMenuItem(new SearchHistoryAction()) );
		mnuHistory.addSeparator();
	    mnuHistory.add( XComponentBuilder.getInstance().buildMenuItem(new ClearHistoryAction()) );

        mnuDocumentSelector = XComponentBuilder.getInstance().buildCheckBoxMenuItem(new DocumentSelectorAction());
		mnuBookmarkPane = XComponentBuilder.getInstance().buildCheckBoxMenuItem(new BookmarkPaneAction());
		mnuHistoryPane = XComponentBuilder.getInstance().buildCheckBoxMenuItem(new HistoryPaneAction());

		mnuNavigationToolBar = XComponentBuilder.getInstance().buildCheckBoxMenuItem(new NavigationToolBarAction());
		mnuLocationToolBar = XComponentBuilder.getInstance().buildCheckBoxMenuItem(new LocationToolBarAction());
		mnuPersonalToolBar = XComponentBuilder.getInstance().buildCheckBoxMenuItem(new PersonalToolBarAction());
		mnuStatusBar = XComponentBuilder.getInstance().buildCheckBoxMenuItem(new StatusBarAction());

        mnuOptions.add(mnuDocumentSelector);
        mnuOptions.add(mnuBookmarkPane);
        mnuOptions.add(mnuHistoryPane);
		mnuOptions.addSeparator();
        mnuOptions.add(mnuNavigationToolBar);
        mnuOptions.add(mnuLocationToolBar);
        mnuOptions.add(mnuPersonalToolBar);
        mnuOptions.add(mnuStatusBar);
		mnuOptions.addSeparator();
	    mnuOptions.add( XComponentBuilder.getInstance().buildMenuItem(optionsAction) );
		mnuOptions.addSeparator();
	    mnuOptions.add( XComponentBuilder.getInstance().buildMenuItem(setAsHomeAction) );

	    mnuHelp.add( XComponentBuilder.getInstance().buildMenuItem(new HelpContentAction()) );
	    mnuHelp.add( XComponentBuilder.getInstance().buildMenuItem(new AcknowledgementAction()) );
	    mnuHelp.add( XComponentBuilder.getInstance().buildMenuItem(new XBrowserHomeAction()) );
		mnuHelp.addSeparator();
	    mnuHelp.add( XComponentBuilder.getInstance().buildMenuItem(new AboutAction()) );

		mnuWindow.add( XComponentBuilder.getInstance().buildMenuItem(tileCascadeAction) );
	    mnuWindow.add( XComponentBuilder.getInstance().buildMenuItem(tileHorizontalAction) );
	    mnuWindow.add( XComponentBuilder.getInstance().buildMenuItem(tileVerticalAction) );
		mnuWindow.addSeparator();
	    mnuWindow.add( XComponentBuilder.getInstance().buildMenuItem(restoreAllAction) );
	    mnuWindow.add( XComponentBuilder.getInstance().buildMenuItem(minimizeAllAction) );
		mnuWindow.addSeparator();
	    mnuWindow.add( XComponentBuilder.getInstance().buildMenuItem(closeAction) );
	    mnuWindow.add( XComponentBuilder.getInstance().buildMenuItem(closeAllAction) );
		mnuWindow.addSeparator();
		mnuWindow.add(mnuOpenDocuments);

	    mnubarMain.add( Box.createGlue() );
    	mnubarMain.add( XComponentBuilder.getInstance().buildSignButton(new XBrowserSignAction()) );

		return mnubarMain;
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

    private void registerListeners()
    {
		cmbURL.addComboBoxListener( new XURLComboBoxListener() {
			public void urlActivated(String url)
			{
				showInActiveDocument(url);
			}

			public void comboBoxPopupRequested(int x, int y)
			{
				comboBoxPopup.show(cmbURL,x,y);
			}
		});

		addFrimbleListener( new FrimbleAdapter() {
			public void frimbleClosing(FrimbleEvent e)
			{
				exit();
			}
		});

		XProjectConfig.getInstance().addPropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
	String prop_name = evt.getPropertyName();
	Object new_value = evt.getNewValue();

		if( prop_name.equals("ActiveLNF") || prop_name.equals("ActiveTheme") )
			//updateLookAndFeel()
			;
		else if( prop_name.equals("DocumentSelector") )
		{
			mnuDocumentSelector.setState( ((XViewTool)new_value).isVisible() );
			updateViews();
		}
		else if( prop_name.equals("BookmarkPane") )
		{
			mnuBookmarkPane.setState( ((XViewTool)new_value).isVisible() );
			updateViews();
		}
		else if( prop_name.equals("HistoryPane") )
		{
			mnuHistoryPane.setState( ((XViewTool)new_value).isVisible() );
			updateViews();
		}
		else if( prop_name.equals("ContentStyle") )
			setContentStyle( ((Integer)new_value).intValue() );
		else if( prop_name.equals("ContentTabPlacement") )
			contentView.setContentTabPlacement( ((Integer)new_value).intValue() );
		else if( prop_name.equals("NavigationToolBar") )
		{
		boolean state = ((Boolean)new_value).booleanValue();

			mnuNavigationToolBar.setState(state);

			pnlNorth.remove(tlbNavigation);
			if( state==true )
				pnlNorth.add(tlbNavigation, BorderLayout.NORTH);

			validate();
		}
		else if( prop_name.equals("PersonalToolBar") )
		{
		boolean state = ((Boolean)new_value).booleanValue();

			mnuPersonalToolBar.setState(state);

			pnlNorth.remove(tlbPersonal);
			if( state==true )
				pnlNorth.add(tlbPersonal, BorderLayout.SOUTH);

			validate();
		}
		else if( prop_name.equals("LocationToolBar") )
		{
		boolean state = ((Boolean)new_value).booleanValue();

			mnuLocationToolBar.setState(state);

			pnlNorth.remove(pnlLocationBar);
			if( state==true )
				pnlNorth.add(pnlLocationBar, BorderLayout.CENTER);

			validate();
		}
		else if( prop_name.equals("StatusBar") )
		{
		boolean state = ((Boolean)new_value).booleanValue();

			mnuStatusBar.setState(state);

			getContentPane().remove(statusBar);
			if( state==true )
				getContentPane().add(statusBar, BorderLayout.SOUTH);

			validate();
		}
		else if( prop_name.equals("HomeURL") )
			homeAction.putValue(Action.SHORT_DESCRIPTION,new_value.toString());
	}

    /*public void setVisible(boolean b)
    {
    	if( b==true )
        {
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = getSize();

        	setLocation( (screen_size.width-size.width)/2, (screen_size.height-size.height)/2 );
        }

        super.setVisible(b);
    }*/

    ///////////////// XIBrowser Interface /////////////////////
    public void openInActiveDocument(XBookmark bookmark)
    {
		showInActiveDocument( bookmark.getHRef() );
	}

    public void openInNewDocument(XBookmark bookmark)
    {
		createNewDocument();
		showInActiveDocument( bookmark.getHRef() );
	}

    public void openInActiveDocument(XHistoryData history_data)
    {
		showInActiveDocument( history_data.getLocation() );
	}

    public void openInNewDocument(XHistoryData history_data)
    {
		createNewDocument();
		showInActiveDocument( history_data.getLocation() );
	}

    public void showInActiveDocument(String url)
    {
    	if( activeDocument==null )
            createNewDocument();

        activeDocument.showURL(url);
    }

	public void showInNewDocument(String url)
	{
		createNewDocument();
		showInActiveDocument(url);
	}

	public XDocument getActiveDocument()
	{
		return activeDocument;
	}

	public void activateDocument(XDocument doc)
	{
		contentView.activateDocument(doc);
	}

    public void showPopupMenu(Component invoker, String hyper_link, int x, int y)
    {
	JPopupMenu popup = (hyper_link==null) ? rightPopup : hyperLinkPopup;

		hyperLink = hyper_link;
		popup.show(invoker,x,y);
    }
    ///////////////// XIBrowser Interface /////////////////////

    ///////////////// XDocumentListener Interface /////////////////////
    public void documentLoadingStarted(XDocument doc)
    {
		updateTitle();
		lstDocumentSelector.repaint();

        contentView.setTitleFor(doc, doc.getPageTitle());

    	mnuBackwardHistory.update();
		mnuForwardHistory.update();
    	updateComponents();
	}

    public void documentLoadingFinished(XDocument doc)
    {
	XHistoryData history_data = XHistoryData.buildNewHistoryData(doc.getPageCompletePath(), doc.getPageTitle());

		XHistoryManager.getInstance().addHistoryData(history_data);

		updateTitle();
		lstDocumentSelector.repaint();

        contentView.setTitleFor(doc, doc.getPageTitle());

    	mnuBackwardHistory.update();
		mnuForwardHistory.update();
    	updateComponents();
	}

    public void pageAddedToDocumentHistory(XDocument doc, String url)
    {
	XHistoryData history_data = XHistoryData.buildNewHistoryData(doc.getPageCompletePath(), doc.getPageTitle());

		XHistoryManager.getInstance().addHistoryData(history_data);

		if( doc==activeDocument )
			cmbURL.addURL(url);
	}
    ///////////////// XDocumentListener Interface /////////////////////

    ///////////////// XContentListener Interface /////////////////////
    public void showPopupMenu(Component invoker, int x, int y)
    {
		showPopupMenu(invoker, null, x, y);
	}

    public void documentActivated(XDocument doc)
    {
		mnuOpenDocuments.activateDocument(doc);
        lstDocumentSelector.setSelectedValue(doc,true);

    	setActiveDocument(doc);
        cmbURL.loadURLs(doc.getRenderer().getURLList(), doc.getRenderer().getCurrentURL());
    }

    public void documentAdded(XDocument doc)
    {
		doc.addDocumentListener(this);

    	setActiveDocument(doc);

        mnuOpenDocuments.addDocument(doc);
        lstDocumentSelector.addDocument(doc);
	}

    public void documentClosed(XDocument doc)
    {
		doc.removeDocumentListener(this);

		mnuOpenDocuments.removeDocument(doc);
        lstDocumentSelector.removeDocument(doc);

		cmbURL.removeAllURLs();
    	setActiveDocument(null);
    }
    ///////////////// XContentListener Interface /////////////////////

    private void buildActions()
    {
        backAction = new BackAction();
        forwardAction = new ForwardAction();
        reloadAction = new ReloadAction();
        homeAction = new HomeAction();
        stopAction = new StopAction();
        findAction = new FindAction();
        newPageAction = new NewPageAction();
        printAction = new PrintAction();
        printPreviewAction = new PrintPreviewAction();
        copyAction = new CopyAction();
        cutAction = new CutAction();
        pasteAction = new PasteAction();
        selectAllAction = new SelectAllAction();
        saveAsAction = new SaveAsAction();
        findAgainAction = new FindAgainAction();
        refreshAction = new RefreshAction();
        pageSourceAction = new PageSourceAction();
        setAsHomeAction = new SetAsHomeAction();
        optionsAction = new OptionsAction();
        closeAllAction = new CloseAllAction();
        closeAction = new CloseAction();
        tileCascadeAction = new TileCascadeAction();
        tileHorizontalAction = new TileHorizontalAction();
        tileVerticalAction = new TileVerticalAction();
		minimizeAllAction = new MinimizeAllAction();
		restoreAllAction = new RestoreAllAction();
        openInSameWindowAction = new OpenInSameWindowAction();
        openInNewWindowAction = new OpenInNewWindowAction();
		addToBookmarksAction = new AddToBookmarksAction();
		historyAction = new HistoryAction();
    }

    private void updateComponents()
    {
        backAction.setEnabled(activeDocument!=null && activeDocument.getRenderer().hasBackwardHistory());
        forwardAction.setEnabled(activeDocument!=null && activeDocument.getRenderer().hasForwardHistory());
        reloadAction.setEnabled(activeDocument!=null);

        if( activeDocument!=null )
        {
			backAction.putValue(Action.SHORT_DESCRIPTION, activeDocument.getRenderer().getPreviousPagePath());
			forwardAction.putValue(Action.SHORT_DESCRIPTION, activeDocument.getRenderer().getNextPagePath());
			reloadAction.putValue(Action.SHORT_DESCRIPTION, activeDocument.getRenderer().getCurrentURL());
		}
		else
		{
			backAction.putValue(Action.SHORT_DESCRIPTION, null);
			forwardAction.putValue(Action.SHORT_DESCRIPTION, null);
			reloadAction.putValue(Action.SHORT_DESCRIPTION, null);
		}

        pageSourceAction.setEnabled(activeDocument!=null && activeDocument.getRenderer().hasSource());
        closeAllAction.setEnabled(contentView!=null && contentView.hasDocument());

        stopAction.setEnabled(activeDocument!=null);
		findAction.setEnabled(activeDocument!=null);
        findAgainAction.setEnabled(activeDocument!=null);
        printAction.setEnabled(activeDocument!=null);
        printPreviewAction.setEnabled(activeDocument!=null);
        saveAsAction.setEnabled(activeDocument!=null);
        refreshAction.setEnabled(activeDocument!=null);
        setAsHomeAction.setEnabled(activeDocument!=null);
		closeAction.setEnabled(activeDocument!=null);

		updateTilings();
    }

    private void updateTilings()
	{
		tileCascadeAction.setEnabled(contentView!=null && contentView.hasDocument() && contentView.supportsTiling());
		tileHorizontalAction.setEnabled(contentView!=null && contentView.hasDocument() && contentView.supportsTiling());
		tileVerticalAction.setEnabled(contentView!=null && contentView.hasDocument() && contentView.supportsTiling());
		minimizeAllAction.setEnabled(contentView!=null && contentView.hasDocument() && contentView.supportsTiling());
		restoreAllAction.setEnabled(contentView!=null && contentView.hasDocument() && contentView.supportsTiling());
    }

    private void updateLookAndFeel()
    {
		/*SwingUtilities.updateComponentTreeUI(this);

		SwingUtilities.updateComponentTreeUI(rightPopup);
		SwingUtilities.updateComponentTreeUI(hyperLinkPopup);
		SwingUtilities.updateComponentTreeUI(comboBoxPopup);
		SwingUtilities.updateComponentTreeUI(mnuBookmarkQuickFile);

		SwingUtilities.updateComponentTreeUI(documentSelectorComponent);
		SwingUtilities.updateComponentTreeUI(bookmarkPaneComponent);
		SwingUtilities.updateComponentTreeUI(historyPaneComponent);

		SwingUtilities.updateComponentTreeUI(tlbNavigation);
		SwingUtilities.updateComponentTreeUI(tlbPersonal);
		SwingUtilities.updateComponentTreeUI(pnlLocationBar);
		SwingUtilities.updateComponentTreeUI(statusBar);*/
    }

    private void updateTitle()
    {
   		if( activeDocument!=null )
   			setTitle( XProjectConstants.PRODUCT_NAME+" - "+activeDocument.getPageTitle() );
   		else
   			setTitle( XProjectConstants.PRODUCT_NAME );
	}

    private void setContentStyle(int style)
    {
    XContentView old_content_view = contentView;

        if( old_content_view!=null )
	    	pnlContentView.remove(old_content_view.getComponent());

        contentView = XContentFactory.createContent(style, this);
		contentView.setContentTabPlacement(XProjectConfig.getInstance().getContentTabPlacement());
		pnlContentView.add(contentView.getComponent(), BorderLayout.CENTER);

        pnlContentView.validate();
        updateTilings();

        if( old_content_view!=null )
        {
            contentView.setDocuments( old_content_view.getDocuments() );
            old_content_view.closeAll(false);
	    }
    }

    private void updateViews()
    {
	XViewTool doc_sel = XProjectConfig.getInstance().getDocumentSelector();
	XViewTool bm_pane = XProjectConfig.getInstance().getBookmarkPane();
	XViewTool history_pane = XProjectConfig.getInstance().getHistoryPane();

        pnlCenter.removeAll();

        if( (doc_sel==null || !doc_sel.isVisible()) &&
        	(bm_pane==null || !bm_pane.isVisible()) &&
        	(history_pane==null || !history_pane.isVisible()) )
    		pnlCenter.add(pnlContentView, BorderLayout.CENTER);
		else
		{
    	JSplitPane spltMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true);
    	LinkedList page1 = new LinkedList();
    	LinkedList page2 = new LinkedList();
    	LinkedList page3 = new LinkedList();
    	Component page1_comp, page2_comp, page3_comp;

        	spltMain.setOneTouchExpandable(true);

			addToToolPage(page1, page2, page3, doc_sel, documentSelectorComponent);
			addToToolPage(page1, page2, page3, bm_pane, bookmarkPaneComponent);
			addToToolPage(page1, page2, page3, history_pane, historyPaneComponent);

			page1_comp = constructToolPage(page1);
			page2_comp = constructToolPage(page2);
			page3_comp = constructToolPage(page3);

	        spltMain.setRightComponent(pnlContentView);

		LinkedList pages = new LinkedList();

			if( page1_comp!=null )
				pages.add(page1_comp);

			if( page2_comp!=null )
				pages.add(page2_comp);

			if( page3_comp!=null )
				pages.add(page3_comp);

			if( pages.size()>1 )
			{
			JTabbedPane tabTools = new JTabbedPane();

				for( int i=0; i<pages.size(); i++ )
					tabTools.add(XComponentBuilder.getInstance().getProperty(this, "Pane", ""+(i+1)), (Component)pages.get(i));

		        spltMain.setLeftComponent(tabTools);
			}
			else
		        spltMain.setLeftComponent( (Component)pages.getFirst() );

	        spltMain.setDividerLocation(200);
    		pnlCenter.add(spltMain, BorderLayout.CENTER);
		}

        pnlCenter.validate();
        pnlCenter.repaint();
    }

    private void addToToolPage(LinkedList page1, LinkedList page2, LinkedList page3, XViewTool tool, Component tool_comp)
    {
		if( tool!=null && tool.isVisible() )
		{
			if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE1 )
				page1.add(tool_comp);
			else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE2 )
				page2.add(tool_comp);
			else if( tool.getPlacement()==XProjectConstants.TOOLS_PAGE3 )
				page3.add(tool_comp);
		}
	}

    private Component constructToolPage(LinkedList page_comps)
    {
		if( page_comps.isEmpty() )
			return null;

		if( page_comps.size()==1 )
			return( (Component)page_comps.getFirst() );
		else
		{
		JSplitPane splt = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true);

			splt.setOneTouchExpandable(true);
			splt.setTopComponent( (Component)page_comps.get(0) );

			if( page_comps.size()==2 )
				splt.setBottomComponent( (Component)page_comps.get(1) );
			else if( page_comps.size()==3 )
			{
			JSplitPane splt2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true);

				splt2.setOneTouchExpandable(true);
				splt2.setTopComponent( (Component)page_comps.get(1) );
				splt2.setBottomComponent( (Component)page_comps.get(2) );
		        splt2.setDividerLocation(120);

				splt.setBottomComponent(splt2);
			}

	        splt.setDividerLocation(250);
			return splt;
		}
	}

    private void showInActiveDocument(File file)
    {
	String str = "";

		try
		{
			str = file.toURL().toString();
		}
		catch( Exception e )
		{
			str = file.toString();
		}

		showInActiveDocument(str);
	}

    private void setActiveDocument(XDocument doc)
    {
    	activeDocument = doc;

    	mnuBackwardHistory.setDocument(doc);
		mnuForwardHistory.setDocument(doc);

		updateTitle();
		updateComponents();
    }

	private void exit()
    {
		XProjectConfig.getInstance().removePropertyChangeListener(this);

        setVisible(false);
        statusBar.stopTimer();

		XProjectConfig.getInstance().save();
		XBookmarkManager.getInstance().save();
		XHistoryManager.getInstance().save();
		//System.exit(0);
    }

	private void createNewDocument()
    {
    	contentView.createNewDocument();
    }

    private class BackAction extends XAction
    {
        public BackAction()
        {
            super(XBrowserLayout.this, "Back", null);
        }

        public void actionPerformed(ActionEvent e)
        {
    	    activeDocument.getRenderer().showPreviousPage();
        }
    }

    private class ForwardAction extends XAction
    {
        public ForwardAction()
        {
            super(XBrowserLayout.this, "Forward", null);
        }

        public void actionPerformed(ActionEvent e)
        {
    	    activeDocument.getRenderer().showNextPage();
        }
    }

    private class ReloadAction extends XAction
    {
        public ReloadAction()
        {
            super(XBrowserLayout.this, "Reload", KeyStroke.getKeyStroke(KeyEvent.VK_F5,-1));
        }

        public void actionPerformed(ActionEvent e)
        {
    	    activeDocument.getRenderer().reload();
        }
    }

    private class HomeAction extends XAction
    {
        public HomeAction()
        {
            super(XBrowserLayout.this, "Home", null);
        }

        public void actionPerformed(ActionEvent e)
        {
            showInActiveDocument(XProjectConfig.getInstance().getHomeURL());
        }
    }

    private class StopAction extends XAction
    {
        public StopAction()
        {
            super(XBrowserLayout.this, "Stop", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,-1));
        }

        public void actionPerformed(ActionEvent e)
        {
    	    activeDocument.getRenderer().stopRendering();
        }
    }

    private class FindAction extends XAction
    {
        public FindAction()
        {
            super(XBrowserLayout.this, "Find", KeyStroke.getKeyStroke(KeyEvent.VK_F,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
	        XFindLayout.getInstance().setVisible(true);
        }
    }

    private class NewPageAction extends XAction
    {
        public NewPageAction()
        {
            super(XBrowserLayout.this, "NewPage", KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
	    	createNewDocument();
        }
    }

    private class PrintAction extends XAction
    {
        public PrintAction()
        {
            super(XBrowserLayout.this, "Print", KeyStroke.getKeyStroke(KeyEvent.VK_P,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		    //activeDocument.printDocumentImmediately();
		    activeDocument.printDocument();
        }
    }

    private class PrintPreviewAction extends XAction
    {
        public PrintPreviewAction()
        {
            super(XBrowserLayout.this, "PrintPreview", null);
        }

        public void actionPerformed(ActionEvent e)
        {
            new XPrintPreviewLayout(activeDocument);
        }
    }

    private class CopyAction extends XAction
    {
        public CopyAction()
        {
            super(XBrowserLayout.this, "Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		Component focus_owner = getFocusOwner();

			if( focus_owner instanceof JTextComponent )
				((JTextComponent)focus_owner).copy();
			else if( activeDocument!=null )
				activeDocument.getRenderer().copy();
        }
    }

    private class CutAction extends XAction
    {
        public CutAction()
        {
            super(XBrowserLayout.this, "Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		Component focus_owner = getFocusOwner();

			if( focus_owner instanceof JTextComponent )
				((JTextComponent)focus_owner).cut();
        }
    }

    private class PasteAction extends XAction
    {
        public PasteAction()
        {
            super(XBrowserLayout.this, "Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		Component focus_owner = getFocusOwner();

			if( focus_owner instanceof JTextComponent )
				((JTextComponent)focus_owner).paste();
        }
    }

    private class SelectAllAction extends XAction
    {
        public SelectAllAction()
        {
            super(XBrowserLayout.this, "SelectAll", KeyStroke.getKeyStroke(KeyEvent.VK_A,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		Component focus_owner = getFocusOwner();

			if( focus_owner instanceof JTextComponent )
				((JTextComponent)focus_owner).selectAll();
			else if( activeDocument!=null )
				activeDocument.getRenderer().selectAll();
        }
    }

    private class OpenFileAction extends XAction
    {
        public OpenFileAction()
        {
            super(XBrowserLayout.this, "OpenFile", KeyStroke.getKeyStroke(KeyEvent.VK_I,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		JFileChooser file_chooser = XComponentBuilder.getInstance().getLoadSaveFileChooser();

			if( file_chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION )
			{
			File selected_file = file_chooser.getSelectedFile();

				if( (selected_file!=null) && (selected_file.exists()) )
					showInActiveDocument(selected_file);
			}
        }
    }

    private class OpenURLAction extends XAction
    {
        public OpenURLAction()
        {
            super(XBrowserLayout.this, "OpenURL", KeyStroke.getKeyStroke(KeyEvent.VK_U,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		String message = XComponentBuilder.getInstance().getProperty(XBrowserLayout.this, "OpenURLDescription");
		String title = XComponentBuilder.getInstance().getProperty(XBrowserLayout.this, "OpenURL");
		String url = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);

			if( url!=null )
			{
				try
				{
					new URL( url );
				}
				catch( Exception ex )
				{
					url = XProjectConstants.DEFAULT_PROTOCOL+url;
				}

				showInActiveDocument( url );
			}
        }
    }

    private class AcknowledgementAction extends XAction
    {
        public AcknowledgementAction()
        {
            super(XBrowserLayout.this, "Acknowledge", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	        showInActiveDocument( XBrowserLayout.class.getClassLoader().getResource("docs/Acknowledge.html").toString() );
        }
    }

    private class HelpContentAction extends XAction
    {
        public HelpContentAction()
        {
            super(XBrowserLayout.this, "HelpContent", KeyStroke.getKeyStroke(KeyEvent.VK_F1,-1));
        }

        public void actionPerformed(ActionEvent e)
        {
	        showInActiveDocument( XBrowserLayout.class.getClassLoader().getResource("docs/index.html").toString() );
        }
    }

    private class AboutAction extends XAction
    {
        public AboutAction()
        {
            super(XBrowserLayout.this, "About", KeyStroke.getKeyStroke(KeyEvent.VK_F12,-1));
        }

        public void actionPerformed(ActionEvent e)
        {
	        (new XAboutLayout()).setVisible(true);
        }
    }

    private class SaveAsAction extends XAction
    {
        public SaveAsAction()
        {
            super(XBrowserLayout.this, "Save", KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
		JFileChooser file_chooser = XComponentBuilder.getInstance().getLoadSaveFileChooser();
		int result = -1;

			file_chooser.setSelectedFile( new File(activeDocument.getPageTitle()) );

			if( file_chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION )
				activeDocument.getRenderer().saveContent( file_chooser.getSelectedFile() );
        }
    }

    private class ExitAction extends XAction
    {
        public ExitAction()
        {
            super(XBrowserLayout.this, "Exit", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			exit();
        }
    }

    private class FindAgainAction extends XAction
    {
        public FindAgainAction()
        {
            super(XBrowserLayout.this, "FindAgain", KeyStroke.getKeyStroke(KeyEvent.VK_F3,-1));
        }

        public void actionPerformed(ActionEvent e)
        {
		    XFindLayout.getInstance().findInDocument();
        }
    }

    private class RefreshAction extends XAction
    {
        public RefreshAction()
        {
            super(XBrowserLayout.this, "Refresh", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			activeDocument.getRenderer().refresh();
        }
    }

    private class PageSourceAction extends XAction
    {
        public PageSourceAction()
        {
            super(XBrowserLayout.this, "PageSource", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			if( activeDocument!=null )
				activeDocument.showPageSource();
        }
    }

    private class SetAsHomeAction extends XAction
    {
        public SetAsHomeAction()
        {
            super(XBrowserLayout.this, "SetAsHome", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	        XProjectConfig.getInstance().setHomeURL(activeDocument.getPageCompletePath());
        }
    }

    private class OptionsAction extends XAction
    {
        public OptionsAction()
        {
            super(XBrowserLayout.this, "Options", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		String current_path = (activeDocument!=null) ? activeDocument.getPageCompletePath() : "";

			XOptionsLayout.getInstance(current_path).setVisible(true);
        }
    }

    private class CloseAllAction extends XAction
    {
        public CloseAllAction()
        {
            super(XBrowserLayout.this, "CloseAll", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	    	contentView.closeAll(true);
        }
    }

    private class CloseAction extends XAction
    {
        public CloseAction()
        {
            super(XBrowserLayout.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	    	contentView.closeCurrentDocument();
        }
    }

    private class TileCascadeAction extends XAction
    {
        public TileCascadeAction()
        {
            super(XBrowserLayout.this, "TileCascade", KeyStroke.getKeyStroke(KeyEvent.VK_A,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
	    	contentView.tileCascade();
        }
    }

    private class TileHorizontalAction extends XAction
    {
        public TileHorizontalAction()
        {
            super(XBrowserLayout.this, "TileHorizontal", KeyStroke.getKeyStroke(KeyEvent.VK_H,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
	    	contentView.tileHorizontal();
        }
    }

    private class TileVerticalAction extends XAction
    {
        public TileVerticalAction()
        {
            super(XBrowserLayout.this, "TileVertical", KeyStroke.getKeyStroke(KeyEvent.VK_T,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
	    	contentView.tileVertical();
        }
    }

    private class MinimizeAllAction extends XAction
    {
        public MinimizeAllAction()
        {
            super(XBrowserLayout.this, "MinimizeAll", KeyStroke.getKeyStroke(KeyEvent.VK_M,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
	    	contentView.minimizeAll();
        }
    }

    private class RestoreAllAction extends XAction
    {
        public RestoreAllAction()
        {
            super(XBrowserLayout.this, "RestoreAll", KeyStroke.getKeyStroke(KeyEvent.VK_R,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
	    	contentView.restoreAll();
        }
    }

    private class GoAction extends XAction
    {
        public GoAction()
        {
            super(XBrowserLayout.this, "Go", null);
        }

        public void actionPerformed(ActionEvent e)
        {
            cmbURL.requestFocus();
            showInActiveDocument(cmbURL.getCurrentURL());
        }
    }

    private class OpenInSameWindowAction extends XAction
    {
        public OpenInSameWindowAction()
        {
            super(XBrowserLayout.this, "OpenInSameWindow", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			showInActiveDocument(hyperLink);
        }
    }

    private class OpenInNewWindowAction extends XAction
    {
        public OpenInNewWindowAction()
        {
            super(XBrowserLayout.this, "OpenInNewWindow", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	    	createNewDocument();
			showInActiveDocument(hyperLink);
        }
    }

    private class CopyHyperLinkAction extends XAction
    {
        public CopyHyperLinkAction()
        {
            super(XBrowserLayout.this, "CopyHyperLink", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		StringSelection str_sel = new StringSelection(hyperLink);

			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str_sel,str_sel);
        }
    }

    private class DocumentSelectorAction extends XAction
    {
        public DocumentSelectorAction()
        {
            super(XBrowserLayout.this, "DocumentSelector", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XViewTool tool = new XViewTool();

			tool.setVisible( mnuDocumentSelector.isSelected() );
			tool.setPlacement( XProjectConfig.getInstance().getDocumentSelector().getPlacement() );

			XProjectConfig.getInstance().setDocumentSelector(tool);
        }
    }

    private class BookmarkPaneAction extends XAction
    {
        public BookmarkPaneAction()
        {
            super(XBrowserLayout.this, "BookmarkPane", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XViewTool tool = new XViewTool();

			tool.setVisible( mnuBookmarkPane.isSelected() );
			tool.setPlacement( XProjectConfig.getInstance().getBookmarkPane().getPlacement() );

			XProjectConfig.getInstance().setBookmarkPane(tool);
        }
    }

    private class HistoryPaneAction extends XAction
    {
        public HistoryPaneAction()
        {
            super(XBrowserLayout.this, "HistoryPane", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XViewTool tool = new XViewTool();

			tool.setVisible( mnuHistoryPane.isSelected() );
			tool.setPlacement( XProjectConfig.getInstance().getHistoryPane().getPlacement() );

			XProjectConfig.getInstance().setHistoryPane(tool);
        }
    }

    private class NavigationToolBarAction extends XAction
    {
        public NavigationToolBarAction()
        {
            super(XBrowserLayout.this, "NavigationToolBar", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XProjectConfig.getInstance().setNavigationToolBar( mnuNavigationToolBar.isSelected() );
        }
    }

    private class LocationToolBarAction extends XAction
    {
        public LocationToolBarAction()
        {
            super(XBrowserLayout.this, "LocationToolBar", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XProjectConfig.getInstance().setLocationToolBar( mnuLocationToolBar.isSelected() );
        }
    }

    private class PersonalToolBarAction extends XAction
    {
        public PersonalToolBarAction()
        {
            super(XBrowserLayout.this, "PersonalToolBar", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XProjectConfig.getInstance().setPersonalToolBar( mnuPersonalToolBar.isSelected() );
        }
    }

    private class StatusBarAction extends XAction
    {
        public StatusBarAction()
        {
            super(XBrowserLayout.this, "StatusBar", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XProjectConfig.getInstance().setStatusBar( mnuStatusBar.isSelected() );
        }
    }

    private class CloseDocumentSelectorAction extends XAction
    {
        public CloseDocumentSelectorAction()
        {
            super(XBrowserLayout.this, "CloseDocumentSelector", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XViewTool tool = new XViewTool();

			tool.setVisible(false);
			tool.setPlacement( XProjectConfig.getInstance().getDocumentSelector().getPlacement() );

			XProjectConfig.getInstance().setDocumentSelector(tool);
        }
    }

    private class CloseBookmarkPaneAction extends XAction
    {
        public CloseBookmarkPaneAction()
        {
            super(XBrowserLayout.this, "CloseBookmarkPane", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XViewTool tool = new XViewTool();

			tool.setVisible(false);
			tool.setPlacement( XProjectConfig.getInstance().getBookmarkPane().getPlacement() );

			XProjectConfig.getInstance().setBookmarkPane(tool);
        }
    }

    private class CloseHistoryPaneAction extends XAction
    {
        public CloseHistoryPaneAction()
        {
            super(XBrowserLayout.this, "CloseHistoryPane", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		XViewTool tool = new XViewTool();

			tool.setVisible(false);
			tool.setPlacement( XProjectConfig.getInstance().getHistoryPane().getPlacement() );

			XProjectConfig.getInstance().setHistoryPane(tool);
        }
    }

    private class BookmarkQuickFileAction extends XAction
    {
        public BookmarkQuickFileAction()
        {
            super(XBrowserLayout.this, "BookmarkQuickFile", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		Component invoker = (Component)e.getSource();

			mnuBookmarkQuickFile.getPopupMenu().show(invoker,invoker.getWidth(),0);
			mnuBookmarkQuickFile.getPopupMenu().setInvoker(mnuBookmarkQuickFile);
        }
    }

    private class XBrowserSignAction extends XAction
    {
        public XBrowserSignAction()
        {
            super(XBrowserLayout.this, "ProductSign", null);
        }

        public void actionPerformed(ActionEvent e)
        {
            showInActiveDocument(XProjectConstants.PRODUCT_HOME_PAGE);
        }
    }

    private class XBrowserHomeAction extends XAction
    {
        public XBrowserHomeAction()
        {
            super(XBrowserLayout.this, "ProductHome", null);
        }

        public void actionPerformed(ActionEvent e)
        {
            showInActiveDocument(XProjectConstants.PRODUCT_HOME_PAGE);
        }
    }

    private class AddToBookmarksAction extends XAction
    {
        public AddToBookmarksAction()
        {
            super(XBrowserLayout.this, "AddBookmark", KeyStroke.getKeyStroke(KeyEvent.VK_D,Event.CTRL_MASK));
        }

        public void actionPerformed(ActionEvent e)
        {
			if( hyperLink!=null )
				XBookmarkPropertiesLayout.getInstance(new XBookmark(hyperLink), null).setVisible(true);
			else if( activeDocument!=null )
			{
			XBookmark bm = new XBookmark(activeDocument.getPageCompletePath());

				bm.setTitle(activeDocument.getPageTitle());
				XBookmarkPropertiesLayout.getInstance(bm, null).setVisible(true);
			}
        }
    }

    private class HistoryAction extends XAction
    {
        public HistoryAction()
        {
            super(XBrowserLayout.this, "History", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XHistoryLayout.getInstance().setVisible(true);
        }
    }

    private class SearchHistoryAction extends XAction
    {
        public SearchHistoryAction()
        {
            super(XBrowserLayout.this, "SearchHistory", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	        XFindLayout2.getInstance(XFindLayout2.SEARCH_HISTORY).setVisible(true);
        }
    }

    private class ClearHistoryAction extends XAction
    {
        public ClearHistoryAction()
        {
            super(XBrowserLayout.this, "ClearHistory", null);
        }

        public void actionPerformed(ActionEvent e)
        {
			XHistoryManager.getInstance().removeAllHistory();
        }
    }

    private class JavaConsoleAction extends XAction
    {
        public JavaConsoleAction()
        {
            super(XBrowserLayout.this, "JavaConsole", null);
        }

        public void actionPerformed(ActionEvent e)
        {
	        XJavaConsole.getInstance().setVisible(true);
        }
    }

// Attributes:
	private JPopupMenu rightPopup,hyperLinkPopup,comboBoxPopup;
    private JCheckBoxMenuItem mnuDocumentSelector,mnuBookmarkPane,mnuHistoryPane,mnuNavigationToolBar,mnuPersonalToolBar,mnuLocationToolBar,mnuStatusBar;
	private XOpenDocumentsMenu mnuOpenDocuments;

	private XBackForwardHistoryPopup mnuForwardHistory, mnuBackwardHistory;

	private JPanel pnlCenter = new JPanel(new BorderLayout());
	private JPanel pnlNorth = new JPanel( new BorderLayout() );
	private JPanel pnlContentView = new JPanel(new BorderLayout());
	private JToolBar tlbNavigation = null;
	private XPersonalToolBar tlbPersonal = new XPersonalToolBar();
	private JPanel pnlLocationBar = null;

    private XDocumentList lstDocumentSelector;
    private XBookmarkMenu mnuBookmarkQuickFile;
	private XStatusBar statusBar;
    private Component documentSelectorComponent, bookmarkPaneComponent, historyPaneComponent;
	private XURLComboBox cmbURL;

    private XDocument activeDocument = null;
    private Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
    private XContentView contentView = null;
    private String hyperLink = null;

	private XAction backAction,forwardAction,reloadAction,homeAction,stopAction,findAction,newPageAction,
			printAction,printPreviewAction,copyAction,cutAction,pasteAction,selectAllAction,saveAsAction,
			findAgainAction,refreshAction,pageSourceAction,setAsHomeAction,optionsAction,closeAllAction,
			closeAction,tileCascadeAction,tileHorizontalAction,tileVerticalAction,openInSameWindowAction,
			openInNewWindowAction,addToBookmarksAction,historyAction,minimizeAllAction,restoreAllAction;
}
