/*
 * 18:46:33 25/03/99
 *
 * ToUpperCase.java
 * Copyright (C) 1999 Romain Guy
 *
 * This free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.jesktop.jipe;

import javax.swing.text.Element;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

import org.gjt.sp.jedit.textarea.*;

public class ToUpperCase
{

  public ToUpperCase(JEditTextArea textArea)
  {
    textArea.beginCompoundEdit();
    String selection = textArea.getSelectedText();
    if (selection != null)
    {
      textArea.setSelectedText(selection.toUpperCase());
    } else {
      Document doc = textArea.getDocument();
      try
      {
        Element map = doc.getDefaultRootElement();
        int count = map.getElementCount();
        for (int i = 0; i < count; i++)
        {
          Element lineElement = map.getElement(i);
          int start = lineElement.getStartOffset();
          int end = lineElement.getEndOffset() - 1;
          end -= start;
          int tabSize = textArea.getTabSize();
          String text = textArea.getText(start, end).toUpperCase();
          doc.remove(start, end);
          doc.insertString(start, text, null);
        }
      } catch (BadLocationException ble) { }
    }
    textArea.endCompoundEdit();
  }
}

// End of ToUpperCase.java
