/*
 * 16:43:35 17/11/99
 *
 * RightIndent.java
 * Copyright (C) 1999 Romain Guy
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

import javax.swing.text.Element;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

import org.gjt.sp.jedit.textarea.*;

public class RightIndent
{


  public RightIndent(JEditTextArea textArea)
  {
    Document doc = textArea.getDocument();
    textArea.beginCompoundEdit();
    try
    {
      int tabSize = textArea.getTabSize();
      boolean noTabs = textArea.getSoftTab();
      Element map = textArea.getDocument().getDefaultRootElement();
      int start = map.getElementIndex(textArea.getSelectionStart());
      int end = map.getElementIndex(textArea.getSelectionEnd());
      for (int i = start; i <= end; i++)
      {
        Element lineElement = map.getElement(i);
        int lineStart = lineElement.getStartOffset();
        String line = doc.getText(lineStart, lineElement.getEndOffset() - lineStart - 1);
        int whiteSpace = JipeUtilities.getLeadingWhiteSpace(line);
        int whiteSpaceWidth = JipeUtilities.getLeadingWhiteSpaceWidth(line, tabSize) + tabSize;
        doc.remove(lineStart, whiteSpace);
        doc.insertString(lineStart, JipeUtilities.createWhiteSpace(whiteSpaceWidth,
                   (noTabs ? 0 : tabSize)), null);
      }
    } catch(BadLocationException ble) {
      ble.printStackTrace();
    }
    textArea.endCompoundEdit();
  }
}

// End of RightIndent.java
