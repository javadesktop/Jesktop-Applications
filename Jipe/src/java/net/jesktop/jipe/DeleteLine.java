/*
 * 23:50:09 20/03/99
 *
 * DeleteLine.java
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

import javax.swing.text.BadLocationException;

import org.gjt.sp.jedit.textarea.*;

public class DeleteLine
{
  public DeleteLine(JEditTextArea textArea)
  {

    try
    {
      Element map = textArea.getDocument().getDefaultRootElement();
      Element lineElement = map.getElement(map.getElementIndex(textArea.getCaretPosition()));
      int start = lineElement.getStartOffset();
      int end = lineElement.getEndOffset();
      if (end == textArea.getDocument().getLength() + 1) end--;
      textArea.getDocument().remove(start, end - start);
      // parent.updateStatus(textArea);
    } catch (BadLocationException ble) { }
  }
}

// End of DeleteLine.java
