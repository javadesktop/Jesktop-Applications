
/**
 * JipeMethodFinder.java - SpeedBar window.  Copyright (c) 1996-2000 Steve Lawson.  E-Mail
 * steve@e-i-s.co.uk Latest version found at http://e-i-s.co.uk/jipe
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.jesktop.jipe;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;


public class JipeMethodFinder implements ListSelectionListener{
    Jipe jipe;
    public JList results;
    public Vector positions;

    public JipeMethodFinder(Jipe parent){
        jipe=parent;
        results = new JList();
        results.setVisibleRowCount(10);
        results.addListSelectionListener(this);
        positions = new Vector();
        results.setFont(new Font(jipe.props.FONTSTYLE, 0, jipe.props.FONTSIZE));
    }
    public void removeList()
    {
        positions.removeAllElements();
        results.setListData(positions);
    }
    protected void parse()
    {
        try
        {
            positions.removeAllElements();
            Vector data = new Vector();
            Element map =
                jipe.activeChild.textarea.getDocument().getDefaultRootElement();
            int lines = map.getElementCount();
            for (int i = 0; i < lines; i++)
            {
                Element lineElement = map.getElement(i);
                int start = lineElement.getStartOffset();
                String lineString = jipe.activeChild.textarea.getText(start,
                                    lineElement.getEndOffset() - start - 1);
                if (isMethod(lineString))
                {
                    String splitstring = SplitString(lineString);
                    data.addElement(splitstring);

                    // data.addElement(lineString.substring(
                    //     getMethodStartIndex(), getMethodEndIndex()));

                    positions.addElement(jipe.activeChild.textarea.getDocument().createPosition
                                         (start + getMethodStartIndex()));
                }
            }
            results.setListData(data);
        }
        catch (BadLocationException ble)
        {
        }
    }

    /**
     * Methods indexes
     */
    private int startIndex;
    private int endIndex;

    /**
     * Braces indexex
     */
    private int parenthesisOpen = -1;
    private int parenthesisClose = -1;

    /**
     * Several booleans needed to determine if our 'method' can be displayed
     */
    private boolean isInComment;
    private boolean isString;
    private boolean isVariable;
    private boolean isInstruction;
    private boolean oneWord;
    private int getMethodStartIndex()
    {
        return startIndex;
    }
    private int getMethodEndIndex()
    {
        return endIndex;
    }
    private boolean isMethod(String data)
    {

        // Used to determine the method start index
        isVariable = isInstruction = oneWord = false;
        parenthesisOpen = parenthesisClose = -1;
        boolean isIndented = false;
        startIndex = endIndex = 0;

        // Current character
        char c;
        char comment = '\0';

        // Last parsed char
        char lastChar = '\0';

        // Index of the comment, if one, on current line See below its usage
        int commentIndex = -1;
out:
        for (int i = 0; i < data.length(); i++)
        {
            switch (c = data.charAt(i))
            {
            case ' ':
            case '\t':                               // needed to
                // remove blanks
                // spaces and
                // ensure we
                if (!isIndented && !isString && !isInComment) // got
                    // at
                    // least
                    // one
                    // word
                    // before
                    // parenthesis
                    startIndex++;
                else if ((i < data.length() - 1) &&
                         data.charAt(i + 1) != '(')
                    oneWord = true;
                lastChar = ' ';
                break;
            case '\"':                               // the user may
                // have declared
                // methods in a
                // String
                if (isInComment)
                    break;                           // we have to
                // avoid these
                // 'declares'
                if (!isIndented)
                    isIndented = true;
                if (!isString)
                    isString = true;
                else
                    isString = false;
                lastChar = '\"';
                break;
            case '/':                                // coders often
                // place
                // method(s) in
                // comments
                if (!isIndented)
                    isIndented = true;               // we have to
                // avoid'em too
                if (isString)
                    break;
                if (lastChar == '/')
                {
                    if (isInComment)
                        break;
                    isInComment = true;
                    commentIndex = i;
                    comment = '/';
                    break out;
                }
                else if (lastChar == '*')
                    isInComment = false;
                else
                    lastChar = '/';
                break;
            case '*':                                // if we
                // encounter
                // this, the
                // comment might
                // ends
                if (!isIndented)
                    isIndented = true;               // here...
                if (isString)
                    break;
                if (lastChar == '/')
                {
                    if (isInComment)
                        break;
                    isInComment = true;
                    commentIndex = i;
                }
                else
                    lastChar = '*';
                break;
            case '(':                                // a method
                // declare is
                // made of a
                // opening
                // parethesis
                if (!isInComment && !isString)       // and of a
                    // closing one
                    parenthesisOpen = i;
                lastChar = '(';
                break;
            case ')':                                // that's what
                // I've just said
                if (!isInComment && !isString)
                    if (parenthesisClose == -1)
                        parenthesisClose = i;
                lastChar = ')';
                break;
            case '=':
            case '?':
            case '!':
            case ':':                                // these chars
                // NEVER appear
                // in a method
                // declare
                if (!isInComment && !isString)
                    isVariable = true;
                lastChar = c;
                break;
            case ';':
            case '|':
            case '&':                                // these chars
                // NEVER appear
                // in a method
                // declare
                if (!isInComment && !isString)
                    isInstruction = true;
                lastChar = ';';
                break;
            default:
                if (!isIndented)
                    isIndented = true;
                if (isString || isInComment)
                    break;
                lastChar = c;
                break;
            }
        }
        if (commentIndex == -1)
            commentIndex = data.length();

        // our String must be out of a String, out of a comment, contain at
        // least one word before the parenthesis, have an opening and a
        // closing parenthesis and the opening parenthesis must be placed
        // before the closing one
        if (!isInComment && !isString && !isVariable && !isInstruction &&
                oneWord && parenthesisOpen != -1 && parenthesisClose != -1 &&
                parenthesisOpen < parenthesisClose && parenthesisClose <=
                commentIndex)
        {

            // Silly hack !! To FIX #################### necessary because
            // methods declares really look like those blocks code
            if (data.indexOf("if") == -1 && data.indexOf("catch") == -1 && data.indexOf
                    ("new") == -1 && data.indexOf("switch") == -1 && data.indexOf(
                        "while") == -1)
            {
                endIndex = parenthesisClose + 1;
                if (comment == '/')
                    isInComment = false;
                return true;
            }
        }
        if (comment == '/')
            isInComment = false;
        return false;
    }
    private String SplitString(String stringname)
    {
        String s = null;
        int pos = stringname.indexOf('(');
        if (pos != -1)
            s = stringname.substring(0, pos);
        StringTokenizer t = new StringTokenizer(s, " ");
        while (t.hasMoreTokens())
            s = t.nextToken().trim();
        return s;
    }
    public void valueChanged(ListSelectionEvent evt)
    {
        if (results.isSelectionEmpty() || evt.getValueIsAdjusting() ||
                jipe.activeChild == null)
            return;
        Position pos = (Position)positions.elementAt(
                           results.getSelectedIndex());
        Element map =
            jipe.activeChild.textarea.getDocument().getDefaultRootElement();
        Element lineElement = map.getElement(map.getElementIndex(
                                                 pos.getOffset()));
        if (lineElement == null)
            return;
        jipe.activeChild.textarea.select(lineElement.getStartOffset(),
                                         lineElement.getEndOffset() - 1);
    }

}