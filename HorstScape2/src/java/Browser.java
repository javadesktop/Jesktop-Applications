import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import horst.webwindow.*;
import horst.webwindow.event.*;
import org.jesktop.frimble.*;


/**
 * A demonstration web browser highlighting the capabilites
 * of the WebWindow Java HTML rendering comoponent.
 *
 * @author Horst Heistermann
 * @version 1.0
 */
public class Browser extends JFrimble
implements FrimbleListener, ActionListener, ChangeListener,
ItemListener, HTMLPaneStatusListener, LinkListener, KeyListener
{
    // Main menu items
    JMenuItem fontItem;
    JMenuItem encodingItem;
    JMenuItem proxyItem;
    JMenuItem findItem;
    JMenuItem findNextItem;
    JMenuItem unLoadItem;
    JMenuItem openItem;
    JMenuItem closeItem;
    JMenuItem readerItem;
    JMenuItem aboutItem;

    // Main menu checkbox items
    JCheckBoxMenuItem renderWhileParsingItem;
    JCheckBoxMenuItem enableFramesItem;
    JCheckBoxMenuItem iFramesItem;
    JCheckBoxMenuItem progressiveDisplayItem;
    JCheckBoxMenuItem sizableItem;
    JCheckBoxMenuItem appletsItem;
    JCheckBoxMenuItem tooltipItem;
    JCheckBoxMenuItem imagesItem;
    JCheckBoxMenuItem debugMessagesItem;

    // Popup menu items
    JMenuItem reloadPopup;
    JMenuItem findPopup;
    JMenuItem openPopup;
    JMenuItem searchPopup;

    ImageButton open;
    ImageButton back;
    ImageButton forward;
    ImageButton home;
    ImageButton reload;
    ImageButton search;
    ImageButton print;
    ImageButton stop;
    ImageButton about;

    StatusPanel m_statusPanel;
    LocationPanel m_location;
    ToolBar m_toolbar;
    HTMLPane m_htmlPane;
    WebWindow m_webwindow;

    String imgDir = "d:\\webwindow\\awtoutput\\images\\";

    public Browser() {
		this("images/");
	}

    public Browser(String imageDirectory)
    {
        imgDir = imageDirectory;
        m_webwindow = new WebWindow(false, new JScrollBar(JScrollBar.HORIZONTAL), new JScrollBar(JScrollBar.VERTICAL));
        m_htmlPane = m_webwindow.getHTMLPane();
        m_htmlPane.addHTMLPaneStatusListener(this);
        m_htmlPane.addLinkListener(this);
        createPopupMenu();
        createMenu();
        createToolBar();
        createLocationPanel();
        m_statusPanel = new StatusPanel(imgDir);
        m_statusPanel.m_zoomSlider.addChangeListener(this);
        layoutBrowser();
        addFrimbleListener(this);
        setTitle("WebWindow Browser");
    }

    /**
     * Lays out the browser GUI controls.
     */
    void layoutBrowser()
    {
        MainPanel topPanel = new MainPanel();
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.add("Center", m_toolbar);
        topPanel.add("South", m_location);

        MainPanel browserPanel = new MainPanel(true);
        browserPanel.setLayout(new GridBagLayout());
        Helper.addComponent(browserPanel, m_webwindow,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            1, 1,
                            0, 0,
                            new Insets(5,5,5,5), 0, 0,
                            1.0, 1.0);

        getContentPane().setLayout(new BorderLayout(0,0));
        getContentPane().add("North", topPanel);
        getContentPane().add("Center", browserPanel);
        getContentPane().add("South", m_statusPanel);
        setSize(500,500);
		//centerFrame(this.getOwnerFrame());
    }

    /**
     * Creates the panel showing the location.
     */
    void createLocationPanel()
    {
        ImageButton img = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "World.gif"));
        img.bCanHighlight = false;
        m_location = new LocationPanel(img);
        m_location.textField.addKeyListener(this);
    }

    /**
     * Creates the toolbar.
     */
    void createToolBar()
    {
        m_toolbar = new ToolBar();
        m_toolbar.addButton(open = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "Open.gif"), "Open"));
        m_toolbar.addButton(back =  new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "Left.gif"), "Back"));
        m_toolbar.addButton(forward =new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "Right.gif"), "Forward"));
        m_toolbar.addButton(home = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "Home.gif"), "Home"));
        m_toolbar.addButton(reload = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "RotCCDown.gif"), "Reload"));
        m_toolbar.addButton(search = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "Search.gif"), "Search"));
        m_toolbar.addButton(stop = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "Stop.gif"),"Stop"));
        m_toolbar.addButton(print = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "print.gif"),"Print"));
        m_toolbar.addButton(about = new ImageButton(Helper.getImage(this.getFrimbleContained(), imgDir + "Help.gif"),"About"));

        open.addActionListener(this);
        back.addActionListener(this);
        forward.addActionListener(this);
        home.addActionListener(this);
        reload.addActionListener(this);
        search.addActionListener(this);
        stop.addActionListener(this);
        print.addActionListener(this);
        about.addActionListener(this);
    }

    /**
     * Creates the web browser menu.
     */
    void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();

		debugMessagesItem = new JCheckBoxMenuItem("Show Debug Messages");
		iFramesItem = new JCheckBoxMenuItem("IFRAMES Enabled");
		iFramesItem.setState(true);
		renderWhileParsingItem = new JCheckBoxMenuItem("Render While Parsing");
		renderWhileParsingItem.setState(true);
		enableFramesItem = new JCheckBoxMenuItem("Frames Enabled");
		enableFramesItem.setState(true);
		progressiveDisplayItem = new JCheckBoxMenuItem("Progressive Image Display");
		progressiveDisplayItem.setState(true);
		sizableItem = new JCheckBoxMenuItem("Always Sizable Frames");
		sizableItem.setState(false);
		tooltipItem = new JCheckBoxMenuItem("Enable Tool Tips");
		tooltipItem.setState(false);
		appletsItem = new JCheckBoxMenuItem("Load Applets");
		appletsItem.setState(true);
		imagesItem = new JCheckBoxMenuItem("Load Images");
		imagesItem.setState(true);
		fontItem = new JMenuItem("Set Font...");
		fontItem.setEnabled(false);
		encodingItem = new JMenuItem("Character Encoding...");
		proxyItem = new JMenuItem("Proxy...");
		findItem = new JMenuItem("Find...");
		findItem.setEnabled(false);
		findNextItem = new JMenuItem("Find Next   F3");
		findNextItem.setEnabled(false);
        unLoadItem = new JMenuItem("Unload Page");
        unLoadItem.setEnabled(false);
        openItem = new JMenuItem("Open...");
        closeItem = new JMenuItem("Close");
        aboutItem = new JMenuItem("About...");

        JMenu fileItem = new JMenu("File");
            fileItem.add(openItem);
            fileItem.add(unLoadItem);
            fileItem.add(closeItem);

		JMenu editMenu = new JMenu("Edit");
		    editMenu.add(encodingItem);
		    editMenu.add(proxyItem);
		    editMenu.add(findItem);
		    editMenu.add(findNextItem);
		    editMenu.add(iFramesItem);
		    editMenu.add(renderWhileParsingItem);
		    editMenu.add(enableFramesItem);
		    editMenu.add(progressiveDisplayItem);
		    editMenu.add(sizableItem);
		    editMenu.add(tooltipItem);
		    editMenu.add(appletsItem);
		    editMenu.add(imagesItem);

		JMenu aboutMenu = new JMenu("Help");
		    aboutMenu.add(aboutItem);

		// Register for action events
		iFramesItem.addItemListener(this);
		renderWhileParsingItem.addItemListener(this);
		enableFramesItem.addItemListener(this);
		fontItem.addActionListener(this);
		encodingItem.addActionListener(this);
		proxyItem.addActionListener(this);
		findItem.addActionListener(this);
		findNextItem.addActionListener(this);
		progressiveDisplayItem.addItemListener(this);
		debugMessagesItem.addItemListener(this);
		sizableItem.addItemListener(this);
		tooltipItem.addItemListener(this);
		appletsItem.addItemListener(this);
		imagesItem.addItemListener(this);
		unLoadItem.addActionListener(this);
		openItem.addActionListener(this);
		closeItem.addActionListener(this);
		aboutItem.addActionListener(this);

		menuBar.add(fileItem);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);

		setJMenuBar(menuBar);
    }

    /**
     * Creates the popup menu.
     */
    void createPopupMenu()
    {
        JPopupMenu popup = new JPopupMenu();

        reloadPopup = new JMenuItem("Reload");
        reloadPopup.addActionListener(this);
        popup.add(reloadPopup);

        findPopup = new JMenuItem("Find");
        findPopup.addActionListener(this);
        popup.add(findPopup);

        openPopup = new JMenuItem("Open...");
        openPopup.addActionListener(this);
        popup.add(openPopup);

        searchPopup = new JMenuItem("Search...");
        searchPopup.addActionListener(this);
        popup.add(searchPopup);

        m_webwindow.setJPopupMenu(popup);
    }

    /**
    * Since the HTMLWindow is a lightweight component and this Frame
    * is a heavyweight component. The frame will erase the background and the
    * HTMLWindow will flicker because of this. I override the update method
    * so the background is not erased and thus the flickering will not occur.
    */
    //public void update(Graphics g)
    //{
    //    paint(g);
    //}

///////////////////////////////////////////////////////////////////////////////
// ItemListener
///////////////////////////////////////////////////////////////////////////////

    public void itemStateChanged(ItemEvent e)
    {
        Object src  = e.getSource();
        if (src == renderWhileParsingItem)
            m_htmlPane.setRenderWhileParsing(renderWhileParsingItem.getState());
        else if (src == progressiveDisplayItem)
            m_htmlPane.setProgressiveImageDisplay(progressiveDisplayItem.getState());
        else if (src == tooltipItem)
            m_htmlPane.setToolTips(tooltipItem.getState());
        else if (src == appletsItem)
            m_htmlPane.setLoadApplets(appletsItem.getState());
        else if (src == imagesItem)
            m_htmlPane.setLoadImages(imagesItem.getState());
    }

///////////////////////////////////////////////////////////////////////////////
// ActionListener
///////////////////////////////////////////////////////////////////////////////

    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if (src == encodingItem)
        {
            EncodingDialog dlg = new EncodingDialog(this.getOwnerFrame(), "Character Encoding", m_htmlPane);
            dlg.show();
        }
        else if (src == findNextItem)
        {
            findNext();
        }
        else if (src == print)
        {
            ((HTMLRenderer)m_htmlPane).printPage();
        }
        else if (src == findItem || src == findPopup)
        {
            FindTextDialog dlg = new FindTextDialog(this.getOwnerFrame(), "Find Text", m_webwindow);
            dlg.setVisible(true);
        }
        else if (src == stop)
        {
            m_htmlPane.stop();
        }
        else if (src == reload || src == reloadPopup)
        {
            m_htmlPane.reload();
        }
        else if (src == openItem || src == open || src ==openPopup)
        {
            openFile();
        }
        else if (src == back)
        {
            m_htmlPane.back();
        }
        else if (src == forward)
        {
            m_htmlPane.forward();
        }
        else if (src == home)
        {
            m_htmlPane.loadPage(Helper.getURL("http://www.yahoo.com"));
        }
        else if (src == search || src == searchPopup)
        {
            m_htmlPane.loadPage(Helper.getURL("http://www.google.com"));
        }
        else if (src == unLoadItem)
        {
            m_htmlPane.unLoadPage();
            m_htmlPane.repaint();
        }
        else if (src == unLoadItem)
            m_htmlPane.unLoadPage();
        else if (src == closeItem)
        {
            dispose();
        }
        else if (src == proxyItem)
        {
            ProxyDialog dlg = new ProxyDialog(this.getOwnerFrame(), "Proxy Settings");
            dlg.show();
        }
        else if (src == aboutItem || src == about)
        {
            MessageBox dlg = new MessageBox(this.getOwnerFrame(), "About the WebWindow", "WebWindow Demo Application (Beta Version) For information email: hheister@earthlink.net");
            dlg.setVisible(true);
        }
    }

///////////////////////////////////////////////////////////////////////////////
// FrimbleListener
///////////////////////////////////////////////////////////////////////////////

    public void frimbleClosing( FrimbleEvent event ){dispose();}
    public void frimbleOpened( FrimbleEvent event ){}
    public void frimbleIconified( FrimbleEvent event ){}
    public void frimbleDeiconified( FrimbleEvent event ){}
    public void frimbleClosed( FrimbleEvent event ){this.dispose();}
    public void frimbleActivated( FrimbleEvent event ){}
    public void frimbleDeactivated( FrimbleEvent event ){}

///////////////////////////////////////////////////////////////////////////////
// HTMLPaneStatusListener
///////////////////////////////////////////////////////////////////////////////

    public boolean statusChanged(HTMLPaneStatusEvent evt)
    {
        if (evt.getID() == HTMLPaneStatus.PAGE_LOADED)
        {
            URL u = evt.getURL();
            if (u != null)
            {
                setPageName(u.toString());
                m_location.setLocation(u.toString());
            }
            unLoadItem.setEnabled(true);
            fontItem.setEnabled(true);
            findItem.setEnabled(true);
            findNextItem.setEnabled(true);
            m_htmlPane.repaint();
            m_statusPanel.setLabel("");
            m_statusPanel.m_bShowReadingData = false;
        }
        else if (evt.getID() == HTMLPaneStatus.APPLET_STATUS_MSG)
        {
            String s = evt.getAppletStatusMessage();
            m_statusPanel.setLabel(s);
        }
        else if (evt.getID() == HTMLPaneStatus.CONNECTING)
        {
            String s = "Connecting to " + evt.getURL() + "...";
            m_statusPanel.setLabel(s);
            m_statusPanel.m_bShowReadingData = true;
        }
        else if (evt.getID() == HTMLPaneStatus.PARSING)
        {
            String s = "Reading content from " + evt.getURL() + "...";
            m_statusPanel.setLabel(s);
            m_statusPanel.m_bShowReadingData = true;
        }
        else if (evt.getID() == HTMLPaneStatus.ERROR_OCCURRED)
        {
            m_statusPanel.setLabel("Error occured!");
            m_statusPanel.m_bShowReadingData = false;
        }
        return true;
    }

///////////////////////////////////////////////////////////////////////////////
// LinkListener
///////////////////////////////////////////////////////////////////////////////

    public void mouseEnteredLink(LinkEvent evt)
    {
        m_statusPanel.setLabel(evt.getLink());
    }

    public void mouseExitedLink(LinkEvent evt)
    {
        m_statusPanel.setLabel("");
    }

    public boolean linkClicked(LinkEvent evt)
    {
        return true;
    }

///////////////////////////////////////////////////////////////////////////////
// KeyListener
///////////////////////////////////////////////////////////////////////////////

    public void keyPressed(KeyEvent ev)
    {
        if (ev.getKeyCode() == KeyEvent.VK_ENTER)
        {
            URL u = Helper.getURL(m_location.textField.getText());
            if (u != null)
            {
                m_htmlPane.loadPage(u);
                m_location.setLocation(u.toString());
            }
        }
        else if (ev.getKeyCode() == KeyEvent.VK_F3)
        {
            findNext();
        }
    }

    public void keyReleased(KeyEvent ev) {}
    public void keyTyped(KeyEvent ev) {}

///////////////////////////////////////////////////////////////////////////////
// ChangeListener
///////////////////////////////////////////////////////////////////////////////

    public void stateChanged(ChangeEvent e)
    {
        m_htmlPane.setZoom((double)((double)m_statusPanel.m_zoomSlider.getValue()/100.0));
        m_htmlPane.repaint();
    }

///////////////////////////////////////////////////////////////////////////////
// Helpers
///////////////////////////////////////////////////////////////////////////////

    /**
     * Finds the next occurance of the last searched and found string.
     */
    void findNext()
    {
        int pos = m_webwindow.findNext();
        if (pos == -1)
        {
            MessageBox dlg = new MessageBox(this.getOwnerFrame(), "Find Text", "Text Not Found!");
            dlg.setVisible(true);
        }
    }

    /**
    * Opens a file.
    */
    void openFile()
    {
        FileDialog fileDialog = new FileDialog(this.getOwnerFrame());
	    fileDialog.setMode(FileDialog.LOAD);
	    fileDialog.show();

	    String file = fileDialog.getFile();
	    if (file == null)
		    return;
        String dir = fileDialog.getDirectory();
        dir = getInternetFormat(dir);
        file = getInternetFormat(file);
        URL u = Helper.getURL("file:/" + dir + file);
        if (u != null)
            m_htmlPane.loadPage(u);
    }

    /**
     * Centers the browser in the screen.
     *
     * @param f
     */
    void centerFrame(Frame f)
    {
		Dimension d1 = f.getSize();
		Dimension d2 = this.getOwnerFrame().getToolkit().getScreenSize();
		int frameWidth = d1.width;
		int frameHeight = d1.height;
		int screenWidth = d2.width;
		int screenHeight = d2.height;
		int xPos = Math.max((screenWidth - frameWidth)/2, 0);
		int yPos = Math.max((screenHeight - frameHeight)/2, 0);
		f.setLocation(xPos, yPos);
    }

    public static final String getInternetFormat(String str)
    {
        String s = "";
        for (int i = 0; i < str.length(); i++)
        {
        	char c = str.charAt(i);
        	if (c == '\\')
        	    s += "/";
        	else
        	    s += c;
        }
        return s;
    }

    /**
     * Sets the current page name in the title bar.
     *
     * @param name Page name
     */
    public void setPageName(String name)
    {
        setTitle(name + " - WebWindow Browser");
    }

    /**
     * Sets the location in the location panel.
     *
     * @param txt
     */
    public void setLocation(String txt)
    {
        m_location.setLocation(txt);
        setPageName(txt);
    }

    public static void main(String args[])
    {
        Browser browser;
        browser = new Browser(args[0]);
        browser.show();
    }
}

