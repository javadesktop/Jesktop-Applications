package calc;

// Written by Nilesh Raghuvnashi, no copyright

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.awt.Toolkit;

/**A Text Field that limits the number of characters entered in it.*/
public class LimitedTextField extends JTextField {
	/**The number of columns in the text field.*/
  private int limit;
 	/**If true indicates that the characters are to be converted to upper case. */
  private boolean upperCase;
 	/**Indicates whether the 'ALT' key is pressed.*/
  private boolean isAltPressed;
	/**Constructs a Text Field that limits the number of characters entered in it. */
  LimitedTextField(){
  }
/**Constructs a Text Field that limits the number of characters entered in it.
   *@param limit The maximum number of columns in the text field.*/
  LimitedTextField(int limit){
  	this(limit, false);
  }


	/**Constructs a Text Field that limits the number of characters entered in it.
	 *
	 * @param   limit  The maximum number of columns in the text field.
	 * @param   upper_case  If true indicates that the characters are to be converted to upper case.
	 */

  LimitedTextField(int limit, boolean upper_case){
  	upperCase = upper_case;
  	this.limit = limit;
		addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent fe){
				selectAll();
			}
			public void focusLost(FocusEvent fe){
				select(0,0);
				if(upperCase){
					setText(getText().toUpperCase());
				}
			}
		});
  }

/**Returns the Document Model for this Text Field.*/
protected Document createDefaultModel() {
	return new TextDocument();
}

/**The Document Model for the Text Field.*/
public class TextDocument extends PlainDocument{
	protected TextDocument(){
	}
 /**Function for inserting characters in the text field.
   *@param offset The offset at which the first character of the string is to be inserted.
   *@param str The string being inserted in the text field.
   *@param attr A collection of attributes of the Document.
	 *@exception  BadLocationException  Thrown on an attempt to reference
	 *																			a location that doesn't exist
   */
 public void insertString(int offset, String  str, AttributeSet attr)
	 throws BadLocationException {
     if (null == str)	return;
     if (limit >= (getLength() + str.length())) {
       super.insertString(offset, str, attr);
     }
   }
 }
}