/*
 * 23:17:37 20/03/99
 *
 * RemoveSpaces.java
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

public class RemoveSpaces
{
  public RemoveSpaces(JEditTextArea textArea)

  {
    textArea.beginCompoundEdit();
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
        String text = doRemove(textArea.getText(start, end));
  if (text != null)
  {
          doc.remove(start, end);
          doc.insertString(start, text, null);
  }
      }
      textArea.endCompoundEdit();
    } catch (BadLocationException ble) { }
  }

  private String doRemove(String in)
  {
    int off = in.length() - 1;
    if (off < 0) return null;
    StringBuffer buf = new StringBuffer();
    while (in.charAt(off) == ' ')
      if (--off < 0) break;
    for (int i = 0; i < off + 1; i++)
      buf.append(in.charAt(i));
    return buf.toString();
  }
}

// End of RemoveSpaces.java
