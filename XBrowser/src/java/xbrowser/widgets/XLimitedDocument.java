/****************************************************************
*              XBrowser  -  eXtended web Browser                *
*                                                               *
*           Copyright (c) 2000-2001  Armond Avanes              *
*     Refer to ReadMe & License files for more information      *
*                                                               *
*                                                               *
*                      By: Armond Avanes                        *
*          Armond@Neda.net     &    ArnoldX@MailCity.com        *
*          http://www.geocities.com/xa_arnold/index.html        *
*****************************************************************/
package xbrowser.widgets;

import javax.swing.text.*;

public class XLimitedDocument extends PlainDocument
{
	public XLimitedDocument(char[] valid_chars)
	{
		validChars = valid_chars;
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		if( isValid(str) )
			super.insertString(offs, str, a);
		else
			java.awt.Toolkit.getDefaultToolkit().beep();
	}

	private boolean isValid(String str)
	{
		for( int i=0; i<str.length(); i++ )
		{
			if( !isValidChar(str.charAt(i)) )
				return false;
		}

		return true;
	}

	private boolean isValidChar(char ch)
	{
		for( int i=0; i<validChars.length; i++ )
		{
			if( validChars[i]==ch )
				return true;
		}

		return false;
	}

// Attributes:
	private char[] validChars;
}
