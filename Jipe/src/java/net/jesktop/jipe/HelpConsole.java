//compare test

/**
 * HelpConsole.java - Help class for use with the Jipe Project Copyright (c)
 * 1996-1999 Steve Lawson.  E-Mail steve@e-i-s.co.uk Latest version can be
 * found at http://e-i-s.co.uk/jipe
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
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.jesktop.jipe;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.io.*;
import java.net.*;


/** HelpConsole class to display Help files within a Jipe Editor Window.
 *
 */
public class HelpConsole extends JPanel
{
    /**      *
     */
    Jipe jipe;
    /**      *
     */
    JEditorPane helpConsole;
    /**      */
    public HelpConsole(Jipe parent)
    {
        helpConsole = new JEditorPane();
        showPage("docs/intro.htm");
        helpConsole.addHyperlinkListener(createHyperLinkListener());
        jipe.tabbedPane2.add(new JScrollPane(helpConsole), "- Help -");
        jipe.tabbedPane2.setSelectedIndex(jipe.tabbedPane2.getTabCount() - 1);
        jipe.children.addElement((Object)this);
        jipe.statusBar.msgline.setText("Help Window");
        jipe.window.add(jipe.windowitem = new JMenuItem("- Help -"));
        jipe.windowitem.setFont(new Font("dialog", Font.PLAIN, 11));
        jipe.windowitem.addActionListener(parent);
    }
    /** Add a listener so that we can detect when somebody has clicked on a link.
     *
     */
    public HyperlinkListener createHyperLinkListener() {
        return new HyperlinkListener() {
                   /** Try to open new a new page when a link has be selected.
                    *
                    * @param e
                    */
                   public void hyperlinkUpdate(HyperlinkEvent e) {
                       if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                           if (e instanceof HTMLFrameHyperlinkEvent) {
                               ((HTMLDocument)helpConsole.getDocument()).processHTMLFrameHyperlinkEvent(
                                   (HTMLFrameHyperlinkEvent)e);
                           } else {
                               try {
                                   helpConsole.setPage(e.getURL());
                               } catch (IOException ioe) {
                                   System.out.println("IOE: " + ioe);
                               }
                           }
                       }
                   }
               };
    }

    /** Display the HTML page.
     *
     * @param page
     */
    public void showPage(String page)
    {
        helpConsole.setEditable(false);
        URL pageURL = getClass().getResource(page);
        try
        {
            helpConsole.setPage(pageURL);
        }
        catch (java.io.IOException ioe)
        {
            System.out.println("IOExecption while loading page.");
        }
    }

    /** Close the Help Window, and tidy up Jipe Editor.
     *
     */
    public void close(){
        String str;
        for (int i = 0; i < jipe.window.getItemCount(); i++)
        {
            if (jipe.window.getItem(i) == null)
            {
                continue;
            }
            str = jipe.window.getItem(i).getText();
            if (str.equals("- Help -"))
                jipe.window.remove(i);
        }
        jipe.children.removeElement((Object)this);
        jipe.tabbedPane2.removeTabAt(jipe.tabbedPane2.indexOfTab("- Help -"));
    }
}

