/*
 * JavaScriptTokenMarker.java - JavaScript token marker
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

import javax.swing.text.Segment;

/**
 * JavaScript token marker.
 *
 * @author Slava Pestov
 * @version $Id: JavaScriptTokenMarker.java,v 1.1.1.1 2002-01-11 08:51:13 paul-h Exp $
 */
public class JavaScriptTokenMarker extends CTokenMarker
{
	public JavaScriptTokenMarker()
	{
		super(false,false,getKeywords());
	}

	public static KeywordMap getKeywords()
	{
		if(javaScriptKeywords == null)
		{
			javaScriptKeywords = new KeywordMap(false);
			javaScriptKeywords.add("function",Token.KEYWORD2);
			javaScriptKeywords.add("var",Token.KEYWORD2);
			javaScriptKeywords.add("else",Token.KEYWORD2);
			javaScriptKeywords.add("for",Token.KEYWORD2);
			javaScriptKeywords.add("if",Token.KEYWORD2);
			javaScriptKeywords.add("in",Token.KEYWORD2);
			javaScriptKeywords.add("new",Token.KEYWORD2);
			javaScriptKeywords.add("return",Token.KEYWORD2);
			javaScriptKeywords.add("while",Token.KEYWORD2);
			javaScriptKeywords.add("with",Token.KEYWORD2);
			javaScriptKeywords.add("break",Token.KEYWORD2);
			javaScriptKeywords.add("case",Token.KEYWORD2);
			javaScriptKeywords.add("continue",Token.KEYWORD2);
			javaScriptKeywords.add("default",Token.KEYWORD2);
			javaScriptKeywords.add("false",Token.KEYWORD3);
			javaScriptKeywords.add("this",Token.KEYWORD3);
			javaScriptKeywords.add("true",Token.KEYWORD3);
		}
		return javaScriptKeywords;
	}

	// private members
	private static KeywordMap javaScriptKeywords;
}

/*
 * ChangeLog:
 * $Log: not supported by cvs2svn $
 * Revision 1.0  2000-07-15 11:24:37+01  steve_lawson
 * Initial revision
 *
 * Revision 1.0  2000-06-29 21:50:46+01  steve_lawson
 * Initial revision
 *
 * Revision 1.4  2000/01/29 10:12:43  sp
 * BeanShell edit mode, bug fixes
 *
 * Revision 1.3  1999/12/13 03:40:29  sp
 * Bug fixes, syntax is now mostly GPL'd
 *
 * Revision 1.2  1999/06/05 00:22:58  sp
 * LGPL'd syntax package
 *
 * Revision 1.1  1999/03/13 09:11:46  sp
 * Syntax code updates, code cleanups
 *
 */
