/*
 * 20:35:36 21/05/99
 *
 * WingComment.java
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

import javax.swing.text.BadLocationException;

import org.gjt.sp.jedit.textarea.*;

public class WingComment
{

  public WingComment(JEditTextArea textArea)
  {

    textArea.beginCompoundEdit();
    String commentStart = "/*";
    String commentEnd = "*/";
    if (commentStart == null || commentEnd == null)
      return;
    commentStart = commentStart + ' ';
    commentEnd = ' ' + commentEnd;
    try
    {
      textArea.getDocument().insertString(textArea.getSelectionStart(), commentStart, null);
      textArea.getDocument().insertString(textArea.getSelectionEnd(), commentEnd, null);
    } catch(BadLocationException ble) { }
    textArea.setCaretPosition(textArea.getCaretPosition());
    textArea.endCompoundEdit();
  }
}

// End of WingComment.java
