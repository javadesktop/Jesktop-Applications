package net.jesktop.eternity;

/*
eternityAbout.java - an about screen for the Eternity text editor
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
import java.net.URL;
import javax.swing.*;

/**
 * About dialog for the Eternity Editor.  The dialog specifies version, copyright, and license info.
 *
 * @author Dave Seaton
 */
public class eternityAbout extends JDialog implements ActionListener
{
    JButton ok;
    JButton license;

    /**
     * Construct a new about dialog with specified owner and title.
     *
     * @param owner the <code>Frame</code> that owns this dialog
     * @param title a title for the dialog
     */
    public eternityAbout( Frame owner, String title )
    {
        super( owner, title, true );

        Container cp = getContentPane();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets( 5, 5, 5, 5 );

        cp.setLayout( gb );

        // First, add the logo image
        URL url = ClassLoader.getSystemResource( "images/twistedEternity.jpg" );
        if( url != null )
        {
            c.gridx = 0;
            c.gridy = 0;
            JLabel logo = new JLabel( new ImageIcon( url ));
            gb.setConstraints( logo, c );
            cp.add( logo );
        }

        c.gridx = 1;
        c.gridy = 0;

        // Add the about dialog text
        JLabel info = new JLabel( "<html><font color=#000000>Eternity v" + Eternity.VERSION +
                                            "<br>Copyright 2000, 2001<br> Dave Seaton" +
                                            "<br>dave@goose24.org" +
                                            "<br>www.goose24.org/software/" +
                                            "<br><br>Eternity uses gnu.regexp" +
                                            "<br>Copyright 1998-2001<br> Wes Biggs" +
                                            "<br>www.cacas.org/~wes/java/</font></html>");

        gb.setConstraints( info, c );
        cp.add( info );

        /* Add some buttons - "Ok" will be used to hide the dialog, "License Info" should open a new dialog
            with licensing information */
        ok = new JButton( "Ok" );
        ok.addActionListener( this );
        license = new JButton( "License Info" );
        license.addActionListener( this );

        // Add it all to the dialog
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new FlowLayout( 5 ));
        buttonPanel.add( ok );
        buttonPanel.add( license );

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        gb.setConstraints( buttonPanel, c );
        cp.add( buttonPanel );
        pack();
    }

    /**
     * Responds to button actions.  If the user clicks "Ok", this will hide the dialog.  If
     * "License Info" is clicked, a new dialog will be displayed with licensing information.
     *
     * @param ae an <code>ActionEvent</code> representing a button click
     */
    public void actionPerformed( ActionEvent ae )
    {
        if( ae.getSource() == ok )
            setVisible( false );
        if( ae.getSource() == license )
        {
            // Here are the licensing terms.
            String terms = new String( "Eternity is free software; you can redistribute it and/or\n" +
            "modify it under the terms of the GNU General Public License\n" +
            "as published by the Free Software Foundation; either version 2\n" +
            "of the License, or (at your option) any later version.\n\n" +

            "This program is distributed in the hope that it will be useful,\n" +
            "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
            "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
            "GNU General Public License for more details.\n\n" +

                "Eternity uses the Gosling help system (also licensed under the\n" +
                "GPL) and the gnu.regexp package, licensed under the Lesser GNU\n" +
                "Public License.\n\n" +

            "You should have received a copy of the GNU General Public License\n" +
                "and the Lesser GNU Public License in the JAR file along with\n" +
            "this program; if not, write to the Free Software Foundation, Inc.,\n" +
            "59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.\n\n" +

                "The icon images are provided by Sun Microsystems under their own\n" +
                "license, which can also be found in the JAR file." );

            JOptionPane.showMessageDialog( this, terms, "Eternity License (GPL)",
                                                        JOptionPane.INFORMATION_MESSAGE );
        }
    }
}