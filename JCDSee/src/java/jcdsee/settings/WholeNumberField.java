package jcdsee.settings;

import javax.swing.*; 
import javax.swing.text.*;  
import java.awt.Toolkit; 
import java.text.NumberFormat; 
import java.text.ParseException; 
import java.util.Locale; 

/** A textfield implementation that only allows the
 * user to enter positive natural numbers. The textfield
 * beeps if an incorrect keystroke is made.
 */
public class WholeNumberField extends JTextField { 
  private Toolkit toolkit; 
  private NumberFormat integerFormatter; 
  public WholeNumberField(int value, int columns) { 
    super(columns); 
    toolkit = Toolkit.getDefaultToolkit(); 
    integerFormatter = NumberFormat.getNumberInstance(Locale.US); 
    integerFormatter.setParseIntegerOnly(true); 
    setValue(value); 
  } 

  /** Returns the integer value entered in the textfield
   * @return the integer value
   */
  public int getValue() { 
    int retVal = 0; 

    try { 
      retVal = integerFormatter.parse(getText()).intValue(); 
    } catch (ParseException e) { 
      // This should never happen because insertString allows 
      // only properly formatted data to get in the field. 
      toolkit.beep(); 
    }
    
    return retVal; 
  }

  /** Sets the textfield value
   * @param value the new value
   */
  public void setValue(int value) { 
    setText(integerFormatter.format(value)); 
  }

  protected Document createDefaultModel() { 
    return new WholeNumberDocument(); 
  }
 
  protected class WholeNumberDocument extends PlainDocument { 
    
    public void insertString(int offs, String str, AttributeSet a)  
	 throws BadLocationException { 
	   
	   char[] source = str.toCharArray(); 
	   char[] result = new char[source.length]; 
	   
	   int j = 0; 
	   for (int i = 0; i < result.length; i++) { 
	     if (Character.isDigit(source[i])) 
	       result[j++] = source[i]; 
	     else { 
	       toolkit.beep(); 
	     } 
	   } 
	   super.insertString(offs, new String(result, 0, j), a); 
    } 
  } 
} 
