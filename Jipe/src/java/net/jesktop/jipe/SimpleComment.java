/*
 * 20:35:36 21/05/99
 *
 * SimpleComment.java
 * Copyright (C) 1999 Romain Guy
 * Portions copyright (C) 1998-2000 Slava Pestov
 * 15/07/00 - Modified by Steve Lawson to make compatible with Jipe Project.
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

import javax.swing.text.*;
import javax.swing.text.BadLocationException;

import org.gjt.sp.jedit.textarea.*;



public class SimpleComment
{


  public SimpleComment(JEditTextArea textArea)
  {
    String comment = "//";
    if (comment == null)
      return;
    comment = comment + ' ';

    Document doc = textArea.getDocument();

    int selectionStart = textArea.getSelectionStart();
    int selectionEnd = textArea.getSelectionEnd();
    Element map = doc.getDefaultRootElement();
    int startLine = map.getElementIndex(selectionStart);
    int endLine = map.getElementIndex(selectionEnd);

    textArea.beginCompoundEdit();

    try
    {
      doc.insertString(selectionStart, comment, null);
      for (int i = startLine + 1; i <= endLine; i++)
        doc.insertString(map.getElement(i).getStartOffset(), comment, null);
    } catch(BadLocationException ble) { }
    textArea.setCaretPosition(textArea.getCaretPosition());
    textArea.endCompoundEdit();
  }
}

// End of SimpleComment.java
