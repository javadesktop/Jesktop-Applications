/*
 * SyntaxUtilities.java - Utility functions used by syntax colorizing
 * Copyright (C) 1999 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
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

package org.gjt.sp.jedit.syntax;

import javax.swing.text.*;
import java.awt.*;

/**
 * Class with several utility functions used by jEdit's syntax colorizing
 * subsystem.
 *
 * @author Slava Pestov
 * @version $Id: SyntaxUtilities.java,v 1.1.1.1 2002-01-11 08:51:16 paul-h Exp $
 */
public class SyntaxUtilities
{
  /**
   * Checks if a subregion of a <code>Segment</code> is equal to a
   * string.
   * @param ignoreCase True if case should be ignored, false otherwise
   * @param text The segment
   * @param offset The offset into the segment
   * @param match The string to match
   */
  public static boolean regionMatches(boolean ignoreCase, Segment text,
              int offset, String match)
  {
    int length = offset + match.length();
    char[] textArray = text.array;
    if(length > text.offset + text.count)
      return false;
    for(int i = offset, j = 0; i < length; i++, j++)
    {
      char c1 = textArray[i];
      char c2 = match.charAt(j);
      if(ignoreCase)
      {
        c1 = Character.toUpperCase(c1);
        c2 = Character.toUpperCase(c2);
      }
      if(c1 != c2)
        return false;
    }
    return true;
  }

  /**
   * Checks if a subregion of a <code>Segment</code> is equal to a
   * character array.
   * @param ignoreCase True if case should be ignored, false otherwise
   * @param text The segment
   * @param offset The offset into the segment
   * @param match The character array to match
   */
  public static boolean regionMatches(boolean ignoreCase, Segment text,
              int offset, char[] match)
  {
    int length = offset + match.length;
    char[] textArray = text.array;
    if(length > text.offset + text.count)
      return false;
    for(int i = offset, j = 0; i < length; i++, j++)
    {
      char c1 = textArray[i];
      char c2 = match[j];
      if(ignoreCase)
      {
        c1 = Character.toUpperCase(c1);
        c2 = Character.toUpperCase(c2);
      }
      if(c1 != c2)
        return false;
    }
    return true;
  }

  /**
   * Returns the default style table. This can be passed to the
   * <code>setStyles()</code> method of <code>SyntaxDocument</code>
   * to use the default syntax styles.
   */
  public static SyntaxStyle[] getDefaultSyntaxStyles()
  {
    SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

/*    styles[Token.COMMENT1] = new SyntaxStyle(new Color(0x009900),true,false);
    styles[Token.COMMENT2] = new SyntaxStyle(new Color(0x009900),true,false);
    styles[Token.KEYWORD1] = new SyntaxStyle(new Color(0x0000ff),false,true);
    styles[Token.KEYWORD2] = new SyntaxStyle(new Color(0xff9900),false,false);
    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(0xff0000),false,false);
    styles[Token.LITERAL1] = new SyntaxStyle(new Color(0x9999ff),false,false);
    styles[Token.LITERAL2] = new SyntaxStyle(new Color(0x650099),false,true);
    styles[Token.LABEL] = new SyntaxStyle(new Color(0xcc00cc),false,true);
    styles[Token.OPERATOR] = new SyntaxStyle(new Color(0xffc800),false,true);
    styles[Token.INVALID] = new SyntaxStyle(new Color(0xff9900),false,true); */

    styles[Token.COMMENT1] = new SyntaxStyle(Color.black,true,false);
    styles[Token.COMMENT2] = new SyntaxStyle(new Color(0x990033),true,false);
    styles[Token.KEYWORD1] = new SyntaxStyle(Color.black,false,true);
    styles[Token.KEYWORD2] = new SyntaxStyle(Color.magenta,false,false);
    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(0x009600),false,false);
    styles[Token.LITERAL1] = new SyntaxStyle(new Color(0x650099),false,false);
    styles[Token.LITERAL2] = new SyntaxStyle(new Color(0x650099),false,true);
    styles[Token.LABEL] = new SyntaxStyle(new Color(0x990033),false,true);
    styles[Token.OPERATOR] = new SyntaxStyle(Color.black,false,true);
    styles[Token.INVALID] = new SyntaxStyle(Color.red,false,true);

    return styles;
  }

  /**
   * Returns the JS style table. This can be passed to the
   * <code>setStyles()</code> method of <code>SyntaxDocument</code>
   * to use the default syntax styles.
   */
  public static SyntaxStyle[] getJScriptSyntaxStyles()
  {
    SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

    styles[Token.COMMENT1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[0]),true,false);
    styles[Token.COMMENT2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[1]),true,false);
    styles[Token.KEYWORD1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[2]),false,true);
    styles[Token.KEYWORD2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[3]),false,false);
    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[4]),false,false);
    styles[Token.LITERAL1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[5]),false,false);
    styles[Token.LITERAL2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[6]),false,true);
    styles[Token.LABEL] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[7]),false,true);
    styles[Token.OPERATOR] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[8]),false,true);
    styles[Token.INVALID] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSCOLOR[9]),false,true);
    return styles;
  }

  /**
   * Returns the C style table. This can be passed to the
   * <code>setStyles()</code> method of <code>SyntaxDocument</code>
   * to use the default syntax styles.
   */
  public static SyntaxStyle[] getCSyntaxStyles()
  {
    SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

    styles[Token.COMMENT1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[0]),true,false);
    styles[Token.COMMENT2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[1]),true,false);
    styles[Token.KEYWORD1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[2]),false,true);
    styles[Token.KEYWORD2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[3]),false,false);
    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[4]),false,false);
    styles[Token.LITERAL1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[5]),false,false);
    styles[Token.LITERAL2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[6]),false,true);
    styles[Token.LABEL] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[7]),false,true);
    styles[Token.OPERATOR] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[8]),false,true);
    styles[Token.INVALID] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.CCOLOR[9]),false,true);
    return styles;
  }
  /**
   * Returns the J style table. This can be passed to the
   * <code>setStyles()</code> method of <code>SyntaxDocument</code>
   * to use the default syntax styles.
   */
  public static SyntaxStyle[] getJSyntaxStyles()
  {
    SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

    styles[Token.COMMENT1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[0]),true,false);
    styles[Token.COMMENT2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[1]),true,false);
    styles[Token.KEYWORD1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[2]),false,true);
    styles[Token.KEYWORD2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[3]),false,false);
    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[4]),false,false);
    styles[Token.LITERAL1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[5]),false,false);
    styles[Token.LITERAL2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[6]),false,true);
    styles[Token.LABEL] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[7]),false,true);
    styles[Token.OPERATOR] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[8]),false,true);
    styles[Token.INVALID] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JCOLOR[9]),false,true);
    return styles;
  }
  /**
   * Returns the HTML style table. This can be passed to the
   * <code>setStyles()</code> method of <code>SyntaxDocument</code>
   * to use the default syntax styles.
   */
  public static SyntaxStyle[] getHTMLSyntaxStyles()
  {
    SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

    styles[Token.COMMENT1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[0]),true,false);
    styles[Token.COMMENT2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[1]),true,false);
    styles[Token.KEYWORD1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[2]),false,true);
    styles[Token.KEYWORD2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[3]),false,false);
    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[4]),false,false);
    styles[Token.LITERAL1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[5]),false,false);
    styles[Token.LITERAL2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[6]),false,true);
    styles[Token.LABEL] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[7]),false,true);
    styles[Token.OPERATOR] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[8]),false,true);
    styles[Token.INVALID] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.HCOLOR[9]),false,true);
    return styles;
  }
  /**
   * Returns the JSP style table. This can be passed to the
   * <code>setStyles()</code> method of <code>SyntaxDocument</code>
   * to use the default syntax styles.
   */
  public static SyntaxStyle[] getJSPSyntaxStyles()
  {
    SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

    styles[Token.COMMENT1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[0]),true,false);
    styles[Token.COMMENT2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[1]),true,false);
    styles[Token.KEYWORD1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[2]),false,true);
    styles[Token.KEYWORD2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[3]),false,false);
    styles[Token.KEYWORD3] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[4]),false,false);
    styles[Token.LITERAL1] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[5]),false,false);
    styles[Token.LITERAL2] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[6]),false,true);
    styles[Token.LABEL] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[7]),false,true);
    styles[Token.OPERATOR] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[8]),false,true);
    styles[Token.INVALID] = new SyntaxStyle(new Color(net.jesktop.jipe.PropertiesManager.JSPCOLOR[9]),false,true);
    return styles;
  }

  /**
   * Paints the specified line onto the graphics context. Note that this
   * method munges the offset and count values of the segment.
   * @param line The line segment
   * @param tokens The token list for the line
   * @param styles The syntax style list
   * @param expander The tab expander used to determine tab stops. May
   * be null
   * @param gfx The graphics context
   * @param x The x co-ordinate
   * @param y The y co-ordinate
   * @return The x co-ordinate, plus the width of the painted string
   */
  public static int paintSyntaxLine(Segment line, Token tokens,
    SyntaxStyle[] styles, TabExpander expander, Graphics gfx,
    int x, int y)
  {
    Font defaultFont = gfx.getFont();
    Color defaultColor = gfx.getColor();

    int offset = 0;
    for(;;)
    {
      byte id = tokens.id;
      if(id == Token.END)
        break;

      int length = tokens.length;
      if(id == Token.NULL)
      {
        if(!defaultColor.equals(gfx.getColor()))
          gfx.setColor(defaultColor);
        if(!defaultFont.equals(gfx.getFont()))
          gfx.setFont(defaultFont);
      }
      else
        styles[id].setGraphicsFlags(gfx,defaultFont);

      line.count = length;
      x = Utilities.drawTabbedText(line,x,y,gfx,expander,0);
      line.offset += length;
      offset += length;

      tokens = tokens.next;
    }

    return x;
  }

  // private members
  private SyntaxUtilities() {}
}

/*
 * ChangeLog:
 * $Log: not supported by cvs2svn $
 * Revision 1.0  2000-07-15 11:24:39+01  steve_lawson
 * Initial revision
 *
 * Revision 1.0  2000-06-29 21:50:44+01  steve_lawson
 * Initial revision
 *
 * Revision 1.9  1999/12/13 03:40:30  sp
 * Bug fixes, syntax is now mostly GPL'd
 *
 * Revision 1.8  1999/07/08 06:06:04  sp
 * Bug fixes and miscallaneous updates
 *
 * Revision 1.7  1999/07/05 04:38:39  sp
 * Massive batch of changes... bug fixes, also new text component is in place.
 * Have fun
 *
 * Revision 1.6  1999/06/07 06:36:32  sp
 * Syntax `styling' (bold/italic tokens) added,
 * plugin options dialog for plugin option panes
 *
 * Revision 1.5  1999/06/05 00:22:58  sp
 * LGPL'd syntax package
 *
 * Revision 1.4  1999/04/19 05:38:20  sp
 * Syntax API changes
 *
 * Revision 1.3  1999/04/02 02:39:46  sp
 * Updated docs, console fix, getDefaultSyntaxColors() method, hypersearch update
 *
 * Revision 1.2  1999/03/27 02:46:17  sp
 * SyntaxTextArea is now modular
 *
 * Revision 1.1  1999/03/13 09:11:46  sp
 * Syntax code updates, code cleanups
 *
 */
