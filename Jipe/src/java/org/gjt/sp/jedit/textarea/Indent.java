/*
 * 20:01:42 02/11/99
 *
 * Indent.java - By Slava Pestov, Improved by Romain Guy
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

package org.gjt.sp.jedit.textarea;

import javax.swing.text.Element;
import javax.swing.text.BadLocationException;

public class Indent
{
  private static String openBrackets;
  private static String closeBrackets;

  public static boolean indent(JEditTextArea textArea)
  {
    textArea.beginCompoundEdit(false);
    int tabSize = textArea.getTabSize();
    openBrackets = "{"; //textArea.getProperty("indentOpenBrackets"); Modified by SL
    closeBrackets = "}"; //textArea.getProperty("indentCloseBrackets"); Modified by SL
    if (openBrackets == null)
      openBrackets = "";
    if (closeBrackets == null)
      closeBrackets = "";
    Element map = textArea.getDocument().getDefaultRootElement();
    int index = map.getElementIndex(textArea.getCaretPosition());
    if (index == 0)
    {
      textArea.endCompoundEdit(false);
      return false;
    }
    Element lineElement = map.getElement(index);
    Element prevLineElement = null;
    int prevStart = 0;
    int prevEnd = 0;
    while (--index >= 0)
    {
      prevLineElement = map.getElement(index);
      prevStart = prevLineElement.getStartOffset();
      prevEnd = prevLineElement.getEndOffset();
      if (prevEnd - prevStart > 1) break;
    }
    if (prevLineElement == null)
    {
      textArea.endCompoundEdit(false);
      return false;
    }

    try
    {
      int start = lineElement.getStartOffset();
      int end = lineElement.getEndOffset();
      String line = textArea.getDocument().getText(start, end - start);
      String prevLine = textArea.getDocument().getText(prevStart, prevEnd - prevStart);

      char c;

      int ij;
      boolean prevLineStart = true;
      int prevLineIndent = 0;
      int prevLineBrackets = 0;

      for (ij = 0; ij < prevLine.length(); ij++)
      {
        if (!prevLineStart) break;
        switch (c = prevLine.charAt(ij))
        {
          case ' ':
            prevLineIndent++;
            break;
          case '\t':
            prevLineIndent += (tabSize - (prevLineIndent % tabSize));
            break;
          default:
            prevLineStart = false;
            if (closeBrackets.indexOf(c) != -1)
              //prevLineBrackets = Math.max(prevLineBrackets - 1, 0); Removed by SL 15/07/00
              prevLineBrackets--;
            else if (openBrackets.indexOf(c) != -1)
              prevLineBrackets++;
            break;
        }
      }

      if (ij != prevLine.length() - 1)
      {
        boolean prevLineEnd = true;
        for (int i = prevLine.length() - 1; i > 0; i--)
        {
          if (!prevLineEnd) break;
          switch (c = prevLine.charAt(i))
          {
            case ' ': case '\t': case '\n':
              break;
            default:
              prevLineEnd = false;
              if (openBrackets.indexOf(c) != -1)
                prevLineBrackets++;
              break;
          }
        }
      }

      boolean lineStart = true;
      int lineIndent = 0;
      int lineWidth = 0;
      int lineBrackets = 0;
      int lineOpenBrackets = 0;

      for (int i = 0; i < line.length(); i++)
      {
        if (!lineStart) break;
        switch (c = line.charAt(i))
        {
          case ' ':
            lineIndent++;
            lineWidth++;
            break;
          case '\t':
            lineIndent += (tabSize - (lineIndent % tabSize));
            lineWidth++;
            break;
          default:
            lineStart = false;
            if (closeBrackets.indexOf(c) != -1)
            {
              if (lineOpenBrackets != 0)
                lineOpenBrackets--;
              else
                lineBrackets--;
            } else if (openBrackets.indexOf(c) != -1)
              lineOpenBrackets++;
            break;
        }
      }

      prevLineIndent += (prevLineBrackets + lineBrackets) * tabSize;

      if (lineIndent >= prevLineIndent)
      {
        textArea.endCompoundEdit(false);
        return false;
      }

      textArea.getDocument().remove(start, lineWidth);
      String indent;
      if (textArea.getSoftTab())
        indent = net.jesktop.jipe.JipeUtilities.createWhiteSpace(prevLineIndent);
      else
        indent = doTabs(prevLineIndent, tabSize);
      textArea.getDocument().insertString(start, indent, null);
      textArea.endCompoundEdit(false);
      return true;
    } catch(BadLocationException bl) { }
      textArea.endCompoundEdit(false);
    return false;
  }

  private static String doTabs(int length, int tabSize)
  {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < length / tabSize; i++)
      buf.append('\t');
    for (int i = 0; i < length % tabSize; i++)
      buf.append(' ');
    return buf.toString();
  }
}

// End of Indent.java
