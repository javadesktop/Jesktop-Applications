package net.jesktop.eternity;

/*
eternityEdit.java - the editing frame of the Eternity text editor
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

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import gnu.regexp.*;

/**
 * An editor frame for the Eternity editor.  This frame contains all the information and operations for
 * a single document.
 *
 * @author Dave Seaton
 * @version 1.0
 */
public class eternityEdit extends JInternalFrame
{
    JScrollPane scroller;
    JTextArea texteditor;

    RESyntax syntax;  // So we don't have to create a new RESyntax for every operation.
    RE expression;  // hold on to the last find operation to make find again quicker

    /**
     * Constructs a new eternity editing frame.
     *
     * @param title the title for the frame
     * @param resizable whether the frame can be resized
     * @param closable whether the frame can be closed
     * @param maximizable whether the frame can be maximized
     * @param iconifiable whether the frame can be iconified
     */
    public eternityEdit( String title, boolean resizable, boolean closable, boolean maximizable,
                boolean iconifiable )
    {
        super( title, resizable, closable, maximizable, iconifiable );
        init();
    }

    /**
     * Initialize the editing frame.
     */
    private void init()
    {
        texteditor = new JTextArea( );
        texteditor.setFont( new Font( "Monospaced", Font.PLAIN, 12 ));

        texteditor.getDocument().putProperty( PlainDocument.tabSizeAttribute, new Integer( 3 ));

        scroller = new JScrollPane( texteditor );

        setContentPane( scroller );

        //workaround for JDK Bug, ID# 4345625
        addInternalFrameListener( new InternalFrameAdapter()
                                                {
                                                    public void internalFrameActivated( InternalFrameEvent ife )
                                                    {
                                                        texteditor.requestFocus();
                                                        FocusEvent fe = new FocusEvent( texteditor,
                                                                                                    FocusEvent.FOCUS_GAINED, true );
                                                        texteditor.dispatchEvent( fe );
                                                    }
                                                }
                                        );

        // If the editor is run on windows, the default line separator is \r.  Since we always work with
        // \n, we want our regular expressions to use \n so they can do beginning and end of line matches
        syntax = new RESyntax( RESyntax.RE_SYNTAX_PERL5 );
        syntax.setLineSeparator( "\n" );
    }

    /**
     * Sets the text in the editing frame.  Any text already there will be lost.
     *
     * @param text the new text for the editing frame to display
     */
    public void setText( String text )
    {
        texteditor.setText( text );
    }

    /**
     * Gets the text from the editing frame as a <code>String</code>.
     *
     * @return the text in the editing frame
     */
    public String getText()
    {
        return texteditor.getText();
    }

    /**
     * Performs the cut operation on the hilighted text in this editing frame.
     */
    public void cut()
    {
        texteditor.cut();
    }

    /**
     * Performs the copy operation on the hilighted text in this editing frame.
     */
    public void copy()
    {
        texteditor.copy();
    }

    /**
     * Performs the paste operation on the hilighted text in this editing frame.
     */
    public void paste()
    {
        texteditor.paste();
    }

    /**
     * Performs the select all operation on the hilighted text in this editing frame.
     */
    public void selectAll()
    {
        texteditor.requestFocus();
        texteditor.selectAll();
    }

    /**
     * Performs the goto line operation on the hilighted text in this editing frame.
     *
     * @param line the number of the line to move the cursor to
     */
    public void gotoLine( int line )
    {
        --line; //because we are 0-based
        try
        {
            texteditor.requestFocus();
            texteditor.setCaretPosition( texteditor.getLineEndOffset( line ));
            texteditor.moveCaretPosition( texteditor.getLineStartOffset( line ));
        }
        catch( BadLocationException ble )
        {
            JOptionPane.showMessageDialog( this, "Invalid line number: " +
                ( line + 1 ), "Error", JOptionPane.ERROR_MESSAGE );
        }
        catch( IllegalArgumentException iae )
        {
            JOptionPane.showMessageDialog( this, "Invalid line number: " +
                ( line + 1 ), "Error", JOptionPane.ERROR_MESSAGE );
        }
    }

    /**
     * Performs the find operation on the hilighted text in this editing frame.  The find operation should
     * find the next occurance of the regular expression given and hilight it, leaving the cursor at the
     * end of the search string.
     *
     * @param searchString the string to find - this string is treated as a regular expression
     */
    public void find( String searchString )
    {
        try
        {
            expression = new RE( searchString, RE.REG_MULTILINE, syntax );

            // once we have compiled a new RE, we can treat this operation like a find again operation
            // the mechanics are the same; don't let the name confuse you
            findAgain();
        }
        catch( REException ree )
        {
            JOptionPane.showMessageDialog( this, "Regular expression error.", "Error",
                JOptionPane.ERROR_MESSAGE );
        }
    }

    /**
     * Returns <code>true</code> if the editor has past find data and can perform a find again
     * operation.
     *
     * @return <code>true</code> if a find again operation can be performed
     */
    public boolean isFindAgainReady()
    {
        return ( expression != null );
    }

    /**
     * Repeats the last find operation, or returns if there was no previous find operation.
     * <p>
     * Actually, what this just does a find operation with a previously compiled regular expression.
     * So you don't have to actually have done a find, as long as <code>expression</code> is not
     * <code>null</code>.
     */
    public void findAgain()
    {
        if( isFindAgainReady())
        {
            REMatch match = expression.getMatch( texteditor.getText(), texteditor.getCaretPosition());

            if( match != null )
            {
                texteditor.requestFocus();
                texteditor.setCaretPosition( match.getStartIndex());
                texteditor.moveCaretPosition( match.getEndIndex());
            }
            else
            {
                JOptionPane.showMessageDialog( this, "Search string not found.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    /**
     * Performs the replace operation on the hilighted text in this editing frame.  The replace operation
     *  should find the next occurance of the regular expression given in the first term and replace it
     * with the string in the second term.
     *
     * @param terms an array of strings with the find string in [0] and the replace string in [1]
     */
    public void replace( String[] terms )
    {
        try
        {
            RE expression = new RE( terms[0], RE.REG_MULTILINE, syntax );  // use multi-line mode

            REMatch match;
            int choice;

            do
            {
                match = expression.getMatch( texteditor.getText(), texteditor.getCaretPosition());

                if( match != null )
                {
                    texteditor.requestFocus();
                    texteditor.setCaretPosition( match.getStartIndex());
                    texteditor.moveCaretPosition( match.getEndIndex());

                    choice = JOptionPane.showConfirmDialog( this, "Replace this match?", "Replace",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE );

                    switch( choice )
                    {
                        case JOptionPane.YES_OPTION:
                            texteditor.replaceRange( terms[1], match.getStartIndex(), match.getEndIndex());
                            break;
                        case JOptionPane.NO_OPTION:
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            match = null;
                            break;
                    }
                }
            } while( match != null );
        }
        catch( REException ree )
        {
            JOptionPane.showMessageDialog( this, "Regular Expression Error", "Error",
                JOptionPane.ERROR_MESSAGE );
            return;
        }
    }
}
