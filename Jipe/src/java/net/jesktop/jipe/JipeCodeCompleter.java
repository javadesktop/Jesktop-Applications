/**
 * JipeCodeCompleter.java - CodeCompleter Class for Jipe project.  Copyright (c) 1996-2000 Steve Lawson.  E-Mail
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
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

//import org.gjt.sp.jedit.syntax.*;
import org.gjt.sp.jedit.textarea.*;
import org.jesktop.frimble.*;


public class JipeCodeCompleter extends JDialog //implements ActionListener

{
    JEditTextArea editor;

    JComboBox jcombo;

    protected static JipeCodeCompleter makeJipeCodeCompleter(Jipe jipe, Frimble frimble, JEditTextArea text) {
    Component contained = frimble.getFrimbleContained();
    if (contained instanceof JFrame) {
      return new JipeCodeCompleter(jipe, (JFrame) contained, text);
    } else {
      return new JipeCodeCompleter(jipe, null, text);
    }
  }


    public JipeCodeCompleter(Jipe parent, JFrame frame, JEditTextArea text){
        super(frame, "Code Complete", true);
        editor=text;
        JPanel pane = new JPanel(new BorderLayout());
        Object[] defaultList={
            "addActionListener()","addItemListener()","addMenu()","addMouseListener()","addWindowListener()","getContentPane()",
            "implements","import","JButton","JCheckBox","JComboBox","JEditor","JFrame","JLabel","JList","JMenuItem","JOptionPane",
            "JPanel","JPopupMenu","package","public","String"
        };
        jcombo=new JComboBox(defaultList);
        jcombo.setMaximumRowCount(5);

        addKeyListener(new KeyAdapter() {
                           public void keyReleased(KeyEvent ke) {
                               if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                                   setSelectedText();
                               }
                               if(ke.getKeyCode()==KeyEvent.VK_ESCAPE)close();
                           }
                       });

        addMouseListener(new MouseAdapter() {
                             public void mouseClicked(MouseEvent me) {
                                 if (me.getClickCount() == 2) {
                                     setSelectedText();
                                     me.consume();
                                 }
                             }
                         });

        pane.add(jcombo);
        getContentPane().add(pane);
        int off = editor.getCaretPosition();
        Element map =editor.getDocument().getDefaultRootElement();
        int line = map.getElementIndex(off);
        int start = map.getStartOffset();
        int x = editor.offsetToX(line,off);
        int y = editor.lineToY(line);
        setLocation(editor.getLocationOnScreen().x+x, editor.getLocationOnScreen().y + y);
        pack();
        setVisible(true);
        jcombo.grabFocus();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    public void close(){
        this.dispose();
    }
    public void setSelectedText()
    {
        int start = editor.getSelectionStart();
        editor.setSelectedText((String)jcombo.getSelectedItem());
        this.dispose();
    }
}