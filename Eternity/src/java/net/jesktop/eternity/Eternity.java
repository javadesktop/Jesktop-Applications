package net.jesktop.eternity;

/*
Eternity.java - a text editor
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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.*;

import net.jesktop.gosling.*;

public class Eternity extends org.jesktop.frimble.JFrimble
{
    public static final String VERSION = "0.3.2";

    JToolBar tools;
    JMenuBar menuBar;
    JDesktopPane desk;
    JFileChooser chooser;
    JLabel statusBar;

    newAction newa;
    openAction opena;
    closeAction closea;
    saveAction savea;
    saveAsAction saveasa;
    exitAction exita;
    cutAction cuta;
    copyAction copya;
    pasteAction pastea;
    findAction finda;
    findAgainAction findagaina;
    replaceAction replacea;
    gotoAction gotoa;
    selectAllAction selecta;
    statusAction statusa;
    toolsAction toolsa;
    aboutAction abouta;
    helpAction helpa;

    editorHandler edithandle;

    eternityReplace replace;
    eternityAbout about;
    gosling help;

    public Eternity()
    {
        super( "Eternity" );

        setIconImage( loadIcon( "twistedEternity16.jpg", "" ).getImage());

        setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );

        chooser = new JFileChooser();

        Container cp = getContentPane();

        ImageIcon blank = loadIcon( "Blank24.gif", "Blank" );

        newa = new newAction( "New", loadIcon( "New24.gif", "New" ));
        opena = new openAction( "Open", loadIcon( "Open24.gif", "Open" ));
        closea = new closeAction( "Close", blank );
        savea = new saveAction( "Save", loadIcon( "Save24.gif", "Save" ));
        saveasa = new saveAsAction( "Save As", loadIcon( "SaveAs24.gif", "Save As" ));
        exita = new exitAction( "Exit", blank );
        cuta = new cutAction( "Cut" , loadIcon( "Cut24.gif", "Cut" ));
        copya = new copyAction( "Copy", loadIcon( "Copy24.gif", "Copy" ));
        pastea = new pasteAction( "Paste", loadIcon( "Paste24.gif", "Paste" ));
        finda = new findAction( "Find", loadIcon( "Find24.gif", "Find" ));
        findagaina = new findAgainAction( "Find Again", loadIcon( "FindAgain24.gif", "Find Again" ));
        replacea = new replaceAction( "Replace", loadIcon( "Replace24.gif", "Replace" ));
        gotoa = new gotoAction( "Goto Line", blank );
        selecta = new selectAllAction( "Select All", blank );
        statusa = new statusAction( "Status Bar", null );
        toolsa = new toolsAction( "Tool Bar", null );
        abouta = new aboutAction( "About", blank );
        helpa = new helpAction( "Help", loadIcon( "Help24.gif", "Help" ));

        tools = new JToolBar( "Eternity Tools" );
        tools.add( newa );
        tools.add( opena );
        tools.add( savea );
        tools.add( saveasa );
        tools.addSeparator();
        tools.add( cuta );
        tools.add( copya );
        tools.add( pastea );
        tools.addSeparator();
        tools.add( finda );
        tools.add( findagaina );
        tools.add( replacea );
        tools.addSeparator();
        tools.add( helpa );

        JLabel logo = new JLabel( loadIcon( "twistedEternity24.jpg", "" ));
        logo.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(),
                                                                             BorderFactory.createLoweredBevelBorder()));
        tools.addSeparator();
        tools.add( Box.createHorizontalGlue());
        tools.add( Box.createVerticalGlue());
        tools.add( logo );

        JMenuItem jmi; //need to do all this accelerator stuff because of bug 4304129

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( KeyEvent.VK_F );
        jmi = fileMenu.add( newa );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_N, Event.CTRL_MASK ));
        jmi = fileMenu.add( opena );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, Event.CTRL_MASK ));
        jmi = fileMenu.add( closea );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_W, Event.CTRL_MASK ));
        fileMenu.addSeparator();
        jmi = fileMenu.add( savea );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, Event.CTRL_MASK ));
        fileMenu.add( saveasa );
        fileMenu.addSeparator();
        fileMenu.add( exita );

        JMenu editMenu = new JMenu( "Edit" );
        editMenu.setMnemonic( KeyEvent.VK_E );
        jmi = editMenu.add( cuta );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_X, Event.CTRL_MASK ));
        jmi = editMenu.add( copya );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, Event.CTRL_MASK ));
        jmi = editMenu.add( pastea );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_P, Event.CTRL_MASK ));
        editMenu.addSeparator();
        jmi = editMenu.add( finda );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F, Event.CTRL_MASK ));
        jmi = editMenu.add( findagaina );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_G, Event.CTRL_MASK ));
        jmi = editMenu.add( replacea );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_R, Event.CTRL_MASK ));
        editMenu.addSeparator();
        jmi = editMenu.add( gotoa );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L, Event.CTRL_MASK ));
        jmi = editMenu.add( selecta );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, Event.CTRL_MASK ));

        JMenu winMenu = new JMenu( "Window" );
        winMenu.setMnemonic( KeyEvent.VK_W );

        JCheckBoxMenuItem statusm = new JCheckBoxMenuItem( statusa );
        statusm.setSelected( true );
        JCheckBoxMenuItem toolsm = new JCheckBoxMenuItem( toolsa );
        toolsm.setSelected( true );

        winMenu.add( toolsm );
        winMenu.add( statusm );

        JMenu helpMenu = new JMenu( "Help" );
        helpMenu.setMnemonic( KeyEvent.VK_H );
        jmi = helpMenu.add( helpa );
        jmi.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_H, Event.CTRL_MASK ));
        helpMenu.addSeparator();
        helpMenu.add( abouta );

        menuBar.add( fileMenu );
        menuBar.add( editMenu );
        menuBar.add( winMenu );
        menuBar.add( helpMenu );

        desk = new JDesktopPane();
        desk.setDragMode( JDesktopPane.OUTLINE_DRAG_MODE );

        statusBar = new JLabel( "Welcome to Eternity.");
        statusBar.setIcon( loadIcon( "twistedEternity16.jpg", "Welcome" ));
        statusBar.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(),
                                                                                    BorderFactory.createLoweredBevelBorder()));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new BorderLayout());
        mainPanel.add( desk, BorderLayout.CENTER );
        mainPanel.add( tools, BorderLayout.NORTH );

        cp.setLayout( new BorderLayout());
        cp.add( statusBar, BorderLayout.SOUTH );
        cp.add( mainPanel, BorderLayout.CENTER );

        setJMenuBar( menuBar );

        edithandle = new editorHandler();
        setEnabledActions( false );

        addFrimbleListener( new FrimbleHandler());

        setLocation( 100, 100 );
        setSize( 800, 600 );

        replace = new eternityReplace( Eternity.this.getOwnerFrame(), "Replace" );

        about = new eternityAbout( Eternity.this.getOwnerFrame(), "About" );

        URL home = ClassLoader.getSystemResource( "help/home.html" );
        URL contents = ClassLoader.getSystemResource( "help/contents.html" );
        help = new gosling( "Eternity Help", loadIcon( "twistedEternity16.jpg", "" ).getImage(), home, contents );
    }

    private void exitApplication()
    {
        int choice = JOptionPane.showConfirmDialog( Eternity.this.getOwnerFrame(), "Really Exit?",
                           "Really Exit?", JOptionPane.YES_NO_OPTION );

        if( choice == JOptionPane.YES_OPTION )
        {
            this.setVisible( false );
            this.dispose();
            //System.exit( 0 );
        }
    }

    private ImageIcon loadIcon( String name, String description )
    {
        URL url = this.getClass().getClassLoader().getResource( "images/" + name );
        if( url != null )
            return new ImageIcon( url, description );
        return null;
    }

    private void setEnabledActions( boolean enabled )
    {
        closea.setEnabled( enabled );
        savea.setEnabled( enabled );
        saveasa.setEnabled( enabled );
        cuta.setEnabled( enabled );
        copya.setEnabled( enabled );
        pastea.setEnabled( enabled );
        finda.setEnabled( enabled );
        findagaina.setEnabled( enabled );
        replacea.setEnabled( enabled );
        gotoa.setEnabled( enabled );
        selecta.setEnabled( enabled );
    }

    private String getNiceTime()
    {
        return DateFormat.getTimeInstance().format( new Date());
    }

    private void updateStatus( String action, Icon icon )
    {
        statusBar.setText( action + " at " + getNiceTime());
        statusBar.setIcon( icon );
    }

    private class FrimbleHandler extends org.jesktop.frimble.FrimbleAdapter
    {
        public void frimbleClosing( org.jesktop.frimble.FrimbleEvent e )
        {
            exitApplication();
        }
    }

    private class newAction extends AbstractAction
    {
        public newAction( String name, Icon icon )
        {
            super( name, icon );
            Object[] keys = getKeys();
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_N ));
            putValue( Action.SHORT_DESCRIPTION, "New" );
        }

        public void actionPerformed( ActionEvent e )
        {
            eternityEdit ee = new eternityEdit( "Untitled", true, true, true, true );
            ee.addInternalFrameListener( edithandle );
            ee.setBounds( 10, 10, 600, 400 );
            ee.setVisible( true );
            desk.add( ee, JLayeredPane.DEFAULT_LAYER );
            try
            {
                ee.setSelected( true );
                setEnabledActions( true );
            }
            catch( java.beans.PropertyVetoException pve )
            {
                    JOptionPane.showMessageDialog( Eternity.this.getOwnerFrame(), "Error", "Whoa! Beans are vetoing properties!",
                                                           JOptionPane.ERROR_MESSAGE );
                    exitApplication();
            }
        }
    }

    private class openAction extends AbstractAction
    {
        public openAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_O ));
            putValue( Action.SHORT_DESCRIPTION, "Open" );
        }

        public void actionPerformed( ActionEvent e )
        {
            int retVal = chooser.showOpenDialog( Eternity.this.getOwnerFrame() );

            if( retVal == JFileChooser.APPROVE_OPTION )
            {
                File file = chooser.getSelectedFile();

                try
                {
                    FileInputStream fis = new FileInputStream( file );
                    byte[] b = new byte[fis.available()];
                    fis.read( b );
                    fis.close();

                    eternityEdit ee = new eternityEdit( file.getAbsolutePath(), true, true, true, true );
                    ee.addInternalFrameListener( edithandle );
                    ee.setBounds( 10, 10, 600, 400 );
                    ee.setText( new String( b ));
                    ee.setVisible( true );
                    desk.add( ee, JLayeredPane.DEFAULT_LAYER );
                    try
                    {
                        ee.setSelected( true );
                        setEnabledActions( true );
                        updateStatus( "Opened " + file.getAbsolutePath(), loadIcon( "Open16.gif", "Opened" ));
                    }
                    catch( java.beans.PropertyVetoException pve )
                    {
                        JOptionPane.showMessageDialog( Eternity.this.getOwnerFrame(), "Error",
                                                            "Whoa! Beans are vetoing properties!",
                                                            JOptionPane.ERROR_MESSAGE );
                        exitApplication();
                    }
                }
                catch( IOException ioe )
                {
                    JOptionPane.showMessageDialog( Eternity.this.getOwnerFrame(), "Error", "Couldn't open.",
                                       JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    private class closeAction extends AbstractAction
    {
        public closeAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_C ));
            putValue( Action.SHORT_DESCRIPTION, "Close" );
        }

        public void actionPerformed( ActionEvent e )
        {
            desk.getSelectedFrame().dispose();
        }
    }

    private class saveAction extends AbstractAction
    {
        public saveAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_S ));
            putValue( Action.SHORT_DESCRIPTION, "Save" );
        }

        public void actionPerformed( ActionEvent e )
        {
            String fname = desk.getSelectedFrame().getTitle();

            if( fname != "Untitled" )
            {
                File file = new File( desk.getSelectedFrame().getTitle());
                try
                {
                    FileOutputStream fos = new FileOutputStream( file );
                    fos.write(((eternityEdit) desk.getSelectedFrame()).getText().getBytes());
                    fos.close();

                    desk.getSelectedFrame().setTitle( file.getAbsolutePath());
                    updateStatus( "Saved " + file.getAbsolutePath(), loadIcon( "Save16.gif", "Saved" ));
                }
                catch( IOException ioe )
                {
                    JOptionPane.showMessageDialog( Eternity.this.getOwnerFrame(), "Error", "Couldn't save.",
                                                            JOptionPane.ERROR_MESSAGE );
                }
            }
            else
            {
                saveasa.actionPerformed( e );
            }
        }
    }

    private class saveAsAction extends AbstractAction
    {
        public saveAsAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_A ));
            putValue( Action.SHORT_DESCRIPTION, "Save As" );
        }

        public void actionPerformed( ActionEvent e )
        {
            int retVal = chooser.showSaveDialog( Eternity.this.getOwnerFrame() );

            if( retVal == JFileChooser.APPROVE_OPTION )
            {
                File file = chooser.getSelectedFile();

                try
                {
                    FileOutputStream fos = new FileOutputStream( file );
                    fos.write(((eternityEdit) desk.getSelectedFrame()).getText().getBytes());
                    fos.close();

                    desk.getSelectedFrame().setTitle( file.getAbsolutePath());
                    updateStatus( "Saved " + file.getAbsolutePath(), loadIcon( "SaveAs16.gif", "Saved As" ));
                }
                catch( IOException ioe )
                {
                    JOptionPane.showMessageDialog( Eternity.this.getOwnerFrame(), "Error", "Couldn't save.",
                                                            JOptionPane.ERROR_MESSAGE );
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

        public void actionPerformed( ActionEvent e )
        {
            exitApplication();
        }
    }

    private class cutAction extends AbstractAction
    {
        public cutAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_T ));
            putValue( Action.SHORT_DESCRIPTION, "Cut" );
        }

        public void actionPerformed( ActionEvent e )
        {
            ((eternityEdit) desk.getSelectedFrame()).cut();
        }
    }

    private class copyAction extends AbstractAction
    {
        public copyAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_C ));
            putValue( Action.SHORT_DESCRIPTION, "Copy" );
        }

        public void actionPerformed( ActionEvent e )
        {
            ((eternityEdit) desk.getSelectedFrame()).copy();
        }
    }

    private class pasteAction extends AbstractAction
    {
        public pasteAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_V ));
            putValue( Action.SHORT_DESCRIPTION, "Paste" );
        }

        public void actionPerformed( ActionEvent e )
        {
            ((eternityEdit) desk.getSelectedFrame()).paste();
        }
    }

    private class findAction extends AbstractAction
    {
        public findAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_F ));
            putValue( Action.SHORT_DESCRIPTION, "Find" );
        }

        public void actionPerformed( ActionEvent e )
        {
            String temp = JOptionPane.showInputDialog( Eternity.this.getOwnerFrame(), "Find", "Find",
                JOptionPane.PLAIN_MESSAGE );
            if( temp != null )
            {
                ((eternityEdit) desk.getSelectedFrame()).find( temp );
            }
        }
    }

    private class findAgainAction extends AbstractAction
    {
        public findAgainAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_D ));
            putValue( Action.SHORT_DESCRIPTION, "Find Again" );
        }

        public void actionPerformed( ActionEvent e )
        {
            if(((eternityEdit) desk.getSelectedFrame()).isFindAgainReady())
            {
                ((eternityEdit) desk.getSelectedFrame()).findAgain();
            }
            else
            {
                finda.actionPerformed( e );
            }
        }
    }

    private class replaceAction extends AbstractAction
    {
        public replaceAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_R ));
            putValue( Action.SHORT_DESCRIPTION, "Replace" );
        }

        public void actionPerformed( ActionEvent e )
        {
            replace.setLocationRelativeTo( Eternity.this.getOwnerFrame() );
            replace.show();
            String[] terms = replace.getReplaceTerms();
            if( terms != null )
                ((eternityEdit) desk.getSelectedFrame()).replace( terms );
        }
    }

    private class gotoAction extends AbstractAction
    {
        public gotoAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_G ));
            putValue( Action.SHORT_DESCRIPTION, "Goto Line" );
        }

        public void actionPerformed( ActionEvent e )
        {
            try
            {
                String temp = JOptionPane.showInputDialog( Eternity.this.getOwnerFrame(), "Goto Line Number", "Goto Line Number",
                    JOptionPane.PLAIN_MESSAGE );
                if( temp != null )
                {
                    int line = Integer.parseInt( temp );
                    ((eternityEdit) desk.getSelectedFrame()).gotoLine( line );
                }
            }
            catch( NumberFormatException nfe )
            {
                JOptionPane.showMessageDialog( Eternity.this.getOwnerFrame(), "Invalid line number", "Error",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    private class selectAllAction extends AbstractAction
    {
        public selectAllAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_A ));
            putValue( Action.SHORT_DESCRIPTION, "Select All" );
        }

        public void actionPerformed( ActionEvent ae )
        {
            ((eternityEdit) desk.getSelectedFrame()).selectAll();
        }
    }

    private class statusAction extends AbstractAction
    {
        public statusAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_S ));
            putValue( Action.SHORT_DESCRIPTION, "Toggle Status Bar" );
        }

        public void actionPerformed( ActionEvent e )
        {
            statusBar.setVisible( ! statusBar.isVisible());
        }
    }

    private class toolsAction extends AbstractAction
    {
        public toolsAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_T ));
            putValue( Action.SHORT_DESCRIPTION, "Toggle Tool Bar" );
        }

        public void actionPerformed( ActionEvent e )
        {
            tools.setVisible( ! tools.isVisible());
        }
    }

    private class aboutAction extends AbstractAction
    {
        public aboutAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_A ));
            putValue( Action.SHORT_DESCRIPTION, "About Eternity" );
        }

        public void actionPerformed( ActionEvent e )
        {
            about.setLocationRelativeTo( Eternity.this.getOwnerFrame() );
            about.show();
        }
    }

    private class helpAction extends AbstractAction
    {
        public helpAction( String name, Icon icon )
        {
            super( name, icon );
            putValue( Action.MNEMONIC_KEY, new Integer( KeyEvent.VK_H ));
            putValue( Action.SHORT_DESCRIPTION, "Help" );
        }

        public void actionPerformed( ActionEvent e )
        {
            help.setVisible( true );
        }
    }

    private class editorHandler extends InternalFrameAdapter
    {
        public void internalFrameClosed( InternalFrameEvent ife )
        {
            JInternalFrame[] frames = desk.getAllFrames();
            if( frames.length == 0 )
                setEnabledActions( false );
        }

        public void internalFrameIconified( InternalFrameEvent ife )
        {
            if( desk.getSelectedFrame() == null )
                setEnabledActions( false );
        }

        public void internalFrameDeiconified( InternalFrameEvent ife )
        {
            setEnabledActions( true );
        }
    }

    public static void main( String[] args )
    {
        Eternity e = new Eternity();
        e.setVisible( true );
    }
}
