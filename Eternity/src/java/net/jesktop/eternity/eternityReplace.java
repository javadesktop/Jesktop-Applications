package net.jesktop.eternity;

/*
eternityReplace.java - a replace dialog for the Eternity text editor
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
import javax.swing.*;

/**
 * A dialog to get information for the replace command.
 *
 * @author Dave Seaton
 * @version 1.0
 */
public class eternityReplace extends JDialog implements ActionListener
{
    JButton ok;
    JButton cancel;

    JLabel find;
    JLabel replace;

    JTextField findField;
    JTextField replaceField;

    String[] terms;

    /**
     * Constructs a new dialog with a specified owner and title.
     *
     * @param owner the <code>Frame</code> that owns the dialog
     * @param title the dialog's title
     */
    public eternityReplace( Frame owner, String title )
    {
        super( owner, title, true );

        Container cp = getContentPane();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        cp.setLayout( gb );

        c.insets = new Insets( 5, 15, 5, 5 );

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = c.WEST;
        find = new JLabel( "Replace" );
        find.setForeground( Color.black );
        gb.setConstraints( find, c );
        cp.add( find );

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = c.CENTER;
        findField = new JTextField( 20 );
        gb.setConstraints( findField, c );
        cp.add( findField );

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = c.WEST;
        replace = new JLabel( "with" );
        replace.setForeground( Color.black );
        gb.setConstraints( replace, c );
        cp.add( replace );

        c.gridx = 0;
        c.gridy = 3;
        c.anchor = c.CENTER;
        replaceField = new JTextField( 20 );
        replaceField.addActionListener( this );
        gb.setConstraints( replaceField, c );
        cp.add( replaceField );

        JPanel buttons = new JPanel();
        buttons.setLayout( new FlowLayout( 5 ));
        ok = new JButton( "Ok" );
        ok.addActionListener( this );
        buttons.add( ok );
        cancel = new JButton( "Cancel" );
        cancel.addActionListener( this );
        buttons.add( cancel );

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        gb.setConstraints( buttons, c );
        cp.add( buttons );
        pack();
    }

    /**
     * Handler to respond to button clicks.  Should also respond if user hits 'Enter' in the replace
     * field.  This allows user to be more efficient, since he doesn't have to reach for the mouse.
     *
     * @param ae an <code>ActionEvent</code> representing a button click
     */
    public void actionPerformed( ActionEvent ae )
    {
        if( ae.getSource() == ok || ae.getSource() == replaceField )
        {
            if( findField.getText().equals( "" ))
            {
                JOptionPane.showMessageDialog( this, "You must enter something to replace.", "Error",
                    JOptionPane.ERROR_MESSAGE );
                return;
            }
            terms = new String[2];
            terms[0] = findField.getText();
            terms[1] = replaceField.getText();
            setVisible( false );
        }
        else
        {
            terms = null;
            setVisible( false );
        }
    }

    /**
     * Returns the terms for the replace operation that the user entered.
     *
     * @return an array of <code>String</code> objects, where [0] is the find and [1] is the replace
     */
    public String[] getReplaceTerms()
    {
        return terms;
    }
}