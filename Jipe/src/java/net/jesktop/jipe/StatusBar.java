/**
 * StatusBar.java - Main Class for Jipe.  Copyright (c) 1996-2000 Steve Lawson.  E-Mail
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
import javax.swing.text.*;
import javax.swing.*;

import org.gjt.sp.jedit.textarea.*;

public class StatusBar extends JPanel
{

    static JLabel msgline;
    static JLabel lineIndicator;

    public StatusBar()
    {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 2.5;
        setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 2.0;
        msgline = new JLabel("Jipe - (c) 1996-2000 Steve Lawson ");
        //        msgline.setBorder(BorderFactory.createLoweredBevelBorder());
        msgline.setHorizontalAlignment(JLabel.LEFT);
        msgline.setFont(new Font("dialog", 0, 11));
        gridbag.setConstraints(msgline, c);
        c.weightx = 0.5;
        lineIndicator = new JLabel();
        //        lineIndicator.setBorder(BorderFactory.createLoweredBevelBorder());
        lineIndicator.setHorizontalAlignment(JLabel.RIGHT);
        lineIndicator.setFont(new Font("dialog", 0, 11));
        gridbag.setConstraints(lineIndicator, c);
        setBorder(BorderFactory.createLoweredBevelBorder());
        add(((Component)msgline));
        add(lineIndicator);
    }

    /**
     * Update status label which displays informations about caret's position.
     * @param textArea The text area which caret status has to be updated
     */

    public void updateStatus(JEditTextArea textarea)
    {
        int off = textarea.getCaretPosition();

        Element map =textarea.getDocument().getDefaultRootElement();
        int currLine = map.getElementIndex(off);

        Element lineElement = map.getElement(currLine);
        int start = lineElement.getStartOffset();
        int end = lineElement.getEndOffset();
        int numLines = map.getElementCount();

        lineIndicator.setText("Line: " + (currLine + 1) +
                              "/" + numLines + " Column: "+ (off - start + 1) + ":" + (end - start) + " - " + (((currLine + 1) * 100) / numLines) + "%");
    }


}