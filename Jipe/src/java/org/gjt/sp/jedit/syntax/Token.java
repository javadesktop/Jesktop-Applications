/*
 * Token.java - Generic token
 * Copyright (C) 1998, 1999 Slava Pestov
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

/**
 * A linked list of tokens. Each token has three fields - a token
 * identifier, which is a byte value that can be looked up in the
 * array returned by <code>SyntaxDocument.getColors()</code>
 * to get a color value, a length value which is the length of the
 * token in the text, and a pointer to the next token in the list.
 *
 * @author Slava Pestov
 * @version $Id: Token.java,v 1.1.1.1 2002-01-11 08:51:17 paul-h Exp $
 */
public class Token
{
	/**
	 * Normal text token id. This should be used to mark
	 * normal text.
	 */
	public static final byte NULL = 0;

	/**
	 * Comment 1 token id. This can be used to mark a comment.
	 */
	public static final byte COMMENT1 = 1;

	/**
	 * Comment 2 token id. This can be used to mark a comment.
	 */
	public static final byte COMMENT2 = 2;

	
	/**
	 * Literal 1 token id. This can be used to mark a string
	 * literal (eg, C mode uses this to mark "..." literals)
	 */
	public static final byte LITERAL1 = 3;

	/**
	 * Literal 2 token id. This can be used to mark an object
	 * literal (eg, Java mode uses this to mark true, false, etc)
	 */
	public static final byte LITERAL2 = 4;

	/**
	 * Label token id. This can be used to mark labels
	 * (eg, C mode uses this to mark ...: sequences)
	 */
	public static final byte LABEL = 5;

	/**
	 * Keyword 1 token id. This can be used to mark a
	 * keyword. This should be used for general language
	 * constructs.
	 */
	public static final byte KEYWORD1 = 6;

	/**
	 * Keyword 2 token id. This can be used to mark a
	 * keyword. This should be used for preprocessor
	 * commands, or variables.
	 */
	public static final byte KEYWORD2 = 7;

	/**
	 * Keyword 3 token id. This can be used to mark a
	 * keyword. This should be used for data types.
	 */
	public static final byte KEYWORD3 = 8;

	/**
	 * Operator token id. This can be used to mark an
	 * operator. (eg, SQL mode marks +, -, etc with this
	 * token type)
	 */
	public static final byte OPERATOR = 9;

	/**
	 * Invalid token id. This can be used to mark invalid
	 * or incomplete tokens, so the user can easily spot
	 * syntax errors.
	 */
	public static final byte INVALID = 10;

	/**
	 * The total number of defined token ids.
	 */
	public static final byte ID_COUNT = 11;

	/**
	 * The first id that can be used for internal state
	 * in a token marker.
	 */
	public static final byte INTERNAL_FIRST = 100;

	/**
	 * The last id that can be used for internal state
	 * in a token marker.
	 */
	public static final byte INTERNAL_LAST = 126;

	/**
	 * The token type, that along with a length of 0
	 * marks the end of the token list.
	 */
	public static final byte END = 127;

	/**
	 * The length of this token.
	 */
	public int length;

	/**
	 * The id of this token.
	 */
	public byte id;

	/**
	 * The next token in the linked list.
	 */
	public Token next;

	/**
	 * Creates a new token.
	 * @param length The length of the token
	 * @param id The id of the token
	 */
	public Token(int length, byte id)
	{
		this.length = length;
		this.id = id;
	}

	/**
	 * Returns a string representation of this token.
	 */
	public String toString()
	{
		return "[id=" + id + ",length=" + length + "]";
	}
}

/*
 * ChangeLog:
 * $Log: not supported by cvs2svn $
 * Revision 1.0  2000-07-15 11:24:40+01  steve_lawson
 * Initial revision
 *
 * Revision 1.0  2000-06-29 21:50:45+01  steve_lawson
 * Initial revision
 *
 * Revision 1.12  1999/12/13 03:40:30  sp
 * Bug fixes, syntax is now mostly GPL'd
 *
 * Revision 1.11  1999/06/05 00:22:58  sp
 * LGPL'd syntax package
 *
 * Revision 1.10  1999/04/22 06:03:26  sp
 * Syntax colorizing change
 *
 * Revision 1.9  1999/04/19 05:38:20  sp
 * Syntax API changes
 *
 * Revision 1.8  1999/04/01 04:13:00  sp
 * Bug fixing for 1.5final
 *
 * Revision 1.7  1999/03/13 08:50:39  sp
 * Syntax colorizing updates and cleanups, general code reorganizations
 *
 * Revision 1.6  1999/03/12 23:51:00  sp
 * Console updates, uncomment removed cos it's too buggy, cvs log tags added
 *
 */
