/*
gosling.java
Copyright (C) 2001 Dave Seaton
dave@goose24.org
http://www.goose24.org

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package net.jesktop.gosling;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 * Gosling is a free help system for Java applications.  It is meant to be
 * simple to use with respect to the programmer and also to the end user.
 * <p>
 * When adding gosling to your application, you will need to pass it two
 * URL object, specifying the locations of two HTML 3.2 files - a "home" page and
 * a "contents" page.  How you design these HTML files is up to you, but I
 * recommend that the contents page look something like table of contents of a
 * book.  (You could also make it look like the index to a book).
 * <p>
 * The gosling help window has 2 tabs, the "Help" tab and the "Contents" tab.
 * The home page is initially loaded into the help tab, while the contents page
 * is loaded into the contents tab.  As the user navigates through your help
 * pages, those pages load in the help tab.  The contents page remains static -
 * a familiar point of reference to the user.
 * <p>
 * One thing to note is when the user chooses "Exit Help" from the menu, or
 * closes the window, it does not dispose the gosling.  Instead, it "hides"
 * the window, so the next time the user clicks on help in your application,
 * you need only call <code>setVisible( true )</code> on the gosling to show
 * it again.  This saves time and gives the appearance that the window comes
 * up very fast.
 * <p>
 * @author Dave Seaton
 * @version 1.0.1
 */
public class gosling extends org.jesktop.frimble.JFrimble implements HistoryEventListener
{
    public static final String VERSION = "1.0.1";

    private JToolBar tools;
    private JMenuBar menuBar;
    private JTabbedPane tabs;
    private JScrollPane helpScroller;
    private JScrollPane tocScroller;
    private JEditorPane helpPage;
    private JEditorPane tocPage;

    private homeAction homea;
    private backAction backa;
    private forwardAction forwarda;
    private exitAction exita;

    private URL home;
    private goslingHistory history;

    /**
     * Constructs a new gosling window with the specified title, icon, home page,
     * and contents page.
     *
     * @param title a title for the window
     * @param icon an icon for the window
     * @param home the <code>URL</code> of the home page
     * @param toc the <code>URL</code> of the contents page
     */
    public gosling( String title, Image icon, URL home, URL toc )
    {
        super( title );
        setIconImage( icon );

        homea = new homeAction( "Home", loadIcon( "Home24.gif", "Home" ));
        backa = new backAction( "Back", loadIcon( "Back24.gif", "Back" ));
        backa.setEnabled( false );
        forwarda = new forwardAction( "Forward", loadIcon( "Forward24.gif", "Forward" ));
        forwarda.setEnabled( false );
        exita = new exitAction( "Exit Help", loadIcon( "Stop24.gif", "Exit" ));

        Container cp = getContentPane();

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( KeyEvent.VK_F );
        fileMenu.add( exita );

        JMenu navMenu = new JMenu( "Navigate" );
        navMenu.setMnemonic( KeyEvent.VK_N );
        navMenu.add( homea );
        navMenu.add( backa );
        navMenu.add( forwarda );

        menuBar.add( fileMenu );
        menuBar.add( navMenu );

        tools = new JToolBar( title );
        tools.add( homea );
        tools.add( backa );
        tools.add( forwarda );

        helpPage = new JEditorPane();
        helpPage.setEditable( false );

        helpPage.addHyperlinkListener(new HyperlinkListener()
        {
            public void hyperlinkUpdate(HyperlinkEvent e)
            {
                try
                {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                    {
                        helpPage.setPage(e.getURL());
                        history.visit( e.getURL());
                        tabs.setSelectedComponent( helpScroller );
                    }
                }
                catch (IOException ioe)
                {
                    JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Invalid link.", "Error", JOptionPane.ERROR_MESSAGE );
                    try
                    {
                        helpPage.setPage( getHome());
                        history.clear();
                        tabs.setSelectedComponent( helpScroller );
                    }
                    catch( IOException ioe2 )
                    {
                        JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Invalid Home Page.", "Error", JOptionPane.ERROR_MESSAGE );
                    }
                }
        }});

        try
        {
            helpPage.setPage( home );
            this.home = home;
        }
        catch( IOException e )
        {
            JOptionPane.showMessageDialog( this.getOwnerFrame(), "Invalid Home Page.", "Error", JOptionPane.ERROR_MESSAGE );
        }

        tocPage = new JEditorPane();
        tocPage.setEditable( false );

        tocPage.addHyperlinkListener(new HyperlinkListener()
        {
            public void hyperlinkUpdate(HyperlinkEvent e)
            {
                try
                {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                    {
                        helpPage.setPage(e.getURL());
                        history.visit( e.getURL());
                        tabs.setSelectedComponent( helpScroller );
                    }
                }
                catch (IOException ioe)
                {
                    JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Invalid link.", "Error", JOptionPane.ERROR_MESSAGE );
                    try
                    {
                        helpPage.setPage( getHome());
                        history.clear();
                        tabs.setSelectedComponent( helpScroller );
                    }
                    catch( IOException ioe2 )
                    {
                        JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Invalid Home Page.", "Error", JOptionPane.ERROR_MESSAGE );
                    }
                }
        }});

        try
        {
            tocPage.setPage( toc );
        }
        catch( IOException e )
        {
            JOptionPane.showMessageDialog( this.getOwnerFrame(), "Invalid Contents Page.", "Error", JOptionPane.ERROR_MESSAGE );
        }

        history = new goslingHistory( home );
        history.addHistoryEventListener( this );

        helpScroller = new JScrollPane( helpPage );
        tocScroller = new JScrollPane( tocPage );

        tabs = new JTabbedPane();
        tabs.add( "Help", helpScroller );
        tabs.add( "Contents", tocScroller );

        cp.setLayout( new BorderLayout());
        cp.add( tools, BorderLayout.NORTH );
        cp.add( tabs, BorderLayout.CENTER );

        setJMenuBar( menuBar );

        addFrimbleListener( new FrimbleHandler());

        setLocation( 200, 100 );
        setSize( 400, 600 );
    }

    //public void setVisible( boolean b )
    //{
    //  if( b )
    //  {
    //      setState( Frame.NORMAL );
    //  }
///
    //  super.setVisible( b );
    //}

    /**
     * Notifies the gosling that the history status has changed and it should adjust its buttons accordingly.
     *
     * @param he a <code>HistoryEvent</code>
     */
    public void historyStatusChanged( HistoryEvent he )
    {
        backa.setEnabled( he.isBackAvailable());
        forwarda.setEnabled( he.isForwardAvailable());
    }

    private URL getHome()
    {
        return home;
    }

    private ImageIcon loadIcon( String name, String description )
    {
        URL url = ClassLoader.getSystemResource( "gosling_images/" + name );
        if( url != null )
            return new ImageIcon( url, description );
        return null;
    }

    private class FrimbleHandler extends org.jesktop.frimble.FrimbleAdapter
    {
        public void frimbleClosing( org.jesktop.frimble.FrimbleEvent e )
        {
            gosling.this.setVisible( false );
        }
    }

    private class homeAction extends AbstractAction
    {
        public homeAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_H ));
            putValue( Action.SHORT_DESCRIPTION, "Home" );
        }

        public void actionPerformed( ActionEvent ae )
        {
            try
            {
                helpPage.setPage( getHome());
                history.visit( getHome());
                tabs.setSelectedComponent( helpScroller );
            }
            catch( IOException ioe )
            {
                JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Invalid Home Page.", "Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    private class backAction extends AbstractAction
    {
        public backAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_B ));
            putValue( Action.SHORT_DESCRIPTION, "Back" );
        }

        public void actionPerformed( ActionEvent ae )
        {
            URL temp = history.goBack();
            if( temp != null )
                try
                {
                    helpPage.setPage( temp );
                    tabs.setSelectedComponent( helpScroller );
                }
                catch( IOException e )
                {
                    JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Corrupted Page History.", "Error", JOptionPane.ERROR_MESSAGE );

                    try
                    {
                        helpPage.setPage( getHome());
                        history.clear();
                        tabs.setSelectedComponent( helpScroller );
                    }
                    catch( IOException ioe2 )
                    {
                        JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Invalid Home Page.", "Error", JOptionPane.ERROR_MESSAGE );
                    }
                }
        }
    }

    private class forwardAction extends AbstractAction
    {
        public forwardAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_F ));
            putValue( Action.SHORT_DESCRIPTION, "Forward" );
        }

        public void actionPerformed( ActionEvent ae )
        {
            URL temp = history.goForward();
            if( temp != null )
                try
                {
                    helpPage.setPage( temp );
                    tabs.setSelectedComponent( helpScroller );
                }
                catch( IOException e )
                {
                    JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Corrupted Page History.", "Error", JOptionPane.ERROR_MESSAGE );

                    try
                    {
                        helpPage.setPage( getHome());
                        history.clear();
                        tabs.setSelectedComponent( helpScroller );
                    }
                    catch( IOException ioe2 )
                    {
                        JOptionPane.showMessageDialog( gosling.this.getOwnerFrame(), "Invalid Home Page.", "Error", JOptionPane.ERROR_MESSAGE );
                    }
                }
        }
    }

    private class exitAction extends AbstractAction
    {
        public exitAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_X ));
            putValue( Action.SHORT_DESCRIPTION, "Exit" );
        }

        public void actionPerformed( ActionEvent ae )
        {
            gosling.this.setVisible( false );
        }
    }
}