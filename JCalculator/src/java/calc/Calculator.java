package calc;

// Written by Nilesh Raghuvnashi, no copyright

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
/**  Class which implements the calculator. */
public class Calculator extends JPanel{
/** Holds the result of the calculation.*/
    private double arg = 0;
/** Holds the value stored in the memory.*/
    private double mem = 0;
/** Holds the operator to be applied.*/
    private String op = "=";
/** If true,indicates that '%' button is clicked.*/
    private boolean percent = false;
/** If true,indicates the start of a new operation.*/
    private boolean clear = true;
/** If true,indicates that a new number is being entered.*/
    private boolean start = true;
/** *Tells whether to allow deletion of the last digit of the displayed number.*/
    private boolean allowBack = true;
/** If false, indicates that the equal sign is clicked.*/
		private boolean oprFlag = true;

 // Component Declaration
/** Textfield used to display the numbers entered.*/
    private LimitedTextField display;
/** Textfield which indicates the status of memory.*/
    private JTextField memory;
/** Button for entering  "<<" (BACK).*/
    private MyButton buttonBack;
/** Button for entering  "CE".*/
    private MyButton buttonCE;
/** Button for entering  "C".*/
    private MyButton buttonC;
/** Button for entering  "MC".*/
    private MyButton buttonMC;
/** Button for entering digit "7".*/
    private MyButton button7;
/** Button for entering digit "8".*/
    private MyButton button8;
/** Button for entering digit "9".*/
    private MyButton button9;
/** Button for entering  "/".*/
    private MyButton buttonSlash;
/** Button for entering  "sqrt".*/
    private MyButton buttonSqrt;
/** Button for entering  "MR".*/
    private MyButton buttonMR;
/** Button for entering digit "4".*/
    private MyButton button4;
/** Button for entering digit "5".*/
    private MyButton button5;
/** Button for entering digit "6".*/
    private MyButton button6;
/** Button for entering  "*".*/
    private MyButton buttonStar;
/** Button for entering  "%".*/
    private MyButton buttonPercent;
/** Button for entering  "MS".*/
    private MyButton buttonMS;
/** Button for entering digit "1".*/
    private MyButton button1;
/** Button for entering digit "2".*/
    private MyButton button2;
/** Button for entering digit "3".*/
    private MyButton button3;
/** Button for entering  "-".*/
    private MyButton buttonMinus;
/** Button for entering  "1/x".*/
    private MyButton buttonReciprocal;
/** Button for entering  "M+".*/
    private MyButton buttonMPlus;
/** Button for entering digit "0".*/
    private MyButton button0;
/** Button for entering  "+/-".*/
    private MyButton buttonSign;
/** Button for entering digit ".".*/
    private MyButton buttonDot;
/** Button for entering  "+".*/
    private MyButton buttonPlus;
/** Button for entering  "=".*/
    private MyButton buttonEqual;
/** Insets for all the buttons.*/
    private Insets insets = new Insets(2,2,2,2);
/** Handles the operations to be taken on button clicks.*/
    private ButtonHandler buttonHandler = new ButtonHandler();
/** Handles the operations to be taken on key presses.*/
    private MyKeyListener keyListener = new MyKeyListener();

// End of Component Declaration

	/** Initializes all the components.
	 *  Adds all the components to the panel.
	 *  Calls the InitialPositionSet() method.
	 */
    public Calculator(){
	      setLayout(null);
        // Component Initialization
        display = new LimitedTextField(32);
        display.setText("0");
        display.setForeground(Color.black);
        display.setBackground(Color.white);
        display.setFont(new Font("Dialog",Font.PLAIN,12));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.addKeyListener(keyListener);

        memory = new JTextField();
        memory.setForeground(Color.black);
        memory.setBackground(Color.lightGray);
        memory.setFont(new Font("Dialog",Font.BOLD,15));
        memory.setEditable(false);
        memory.addKeyListener(keyListener);
        Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        memory.setBorder(border);

        buttonBack = new MyButton("<<");
        buttonBack.setMargin(insets);
        buttonBack.setFont(new Font("Dialog",Font.BOLD,15));
        buttonBack.setForeground(Color.red.darker().darker());
        buttonBack.addActionListener(buttonHandler);
        buttonBack.addKeyListener(keyListener);

        buttonCE = new MyButton("CE");
        buttonCE.setMargin(insets);
        buttonCE.setFont(new Font("Dialog",Font.BOLD,15));
        buttonCE.setForeground(Color.red.darker().darker());
        buttonCE.addActionListener(buttonHandler);
        buttonCE.addKeyListener(keyListener);

        buttonC = new MyButton("C");
        buttonC.setMargin(insets);
        buttonC.setFont(new Font("Dialog",Font.BOLD,15));
        buttonC.setForeground(Color.red.darker().darker());
        buttonC.addActionListener(buttonHandler);
        buttonC.addKeyListener(keyListener);

        buttonMC = new MyButton("MC");
        buttonMC.setMargin(insets);
        buttonMC.setFont(new Font("Dialog",Font.BOLD,15));
        buttonMC.setForeground(Color.red);
        buttonMC.addActionListener(buttonHandler);
        buttonMC.addKeyListener(keyListener);

        button7 = new MyButton("7");
        button7.setMargin(insets);
        button7.setFont(new Font("Dialog",Font.BOLD,12));
        button7.setForeground(Color.blue);
        button7.addActionListener(buttonHandler);
        button7.addKeyListener(keyListener);

        button8 = new MyButton("8");
        button8.setMargin(insets);
        button8.setFont(new Font("Dialog",Font.BOLD,12));
        button8.setForeground(Color.blue);
        button8.addActionListener(buttonHandler);
        button8.addKeyListener(keyListener);

        button9 = new MyButton("9");
        button9.setMargin(insets);
        button9.setFont(new Font("Dialog",Font.BOLD,12));
        button9.setForeground(Color.blue);
        button9.addActionListener(buttonHandler);
        button9.addKeyListener(keyListener);

        buttonSlash = new MyButton("/");
        buttonSlash.setMargin(insets);
        buttonSlash.setFont(new Font("Dialog",Font.BOLD,15));
        buttonSlash.setForeground(Color.red);
        buttonSlash.addActionListener(buttonHandler);
        buttonSlash.addKeyListener(keyListener);

        buttonSqrt = new MyButton("sqrt");
        buttonSqrt.setMargin(insets);
        buttonSqrt.setFont(new Font("Dialog",Font.BOLD,12));
        buttonSqrt.setForeground(Color.blue.darker());
        buttonSqrt.addActionListener(buttonHandler);
        buttonSqrt.addKeyListener(keyListener);

        buttonMR = new MyButton("MR");
        buttonMR.setMargin(insets);
        buttonMR.setFont(new Font("Dialog",Font.BOLD,15));
        buttonMR.setForeground(Color.red);
        buttonMR.addActionListener(buttonHandler);
        buttonMR.addKeyListener(keyListener);

        button4 = new MyButton("4");
        button4.setMargin(insets);
        button4.setFont(new Font("Dialog",Font.BOLD,12));
        button4.setForeground(Color.blue);
        button4.addActionListener(buttonHandler);
        button4.addKeyListener(keyListener);

        button5 = new MyButton("5");
        button5.setMargin(insets);
        button5.setFont(new Font("Dialog",Font.BOLD,12));
        button5.setForeground(Color.blue);
        button5.addActionListener(buttonHandler);
        button5.addKeyListener(keyListener);

        button6 = new MyButton("6");
        button6.setMargin(insets);
        button6.setFont(new Font("Dialog",Font.BOLD,12));
        button6.setForeground(Color.blue);
        button6.addActionListener(buttonHandler);
        button6.addKeyListener(keyListener);

        buttonStar = new MyButton("*");
        buttonStar.setMargin(insets);
        buttonStar.setFont(new Font("Dialog",Font.BOLD,15));
        buttonStar.setForeground(Color.red);
        buttonStar.addActionListener(buttonHandler);
        buttonStar.addKeyListener(keyListener);

        buttonPercent = new MyButton("%");
        buttonPercent.setMargin(insets);
        buttonPercent.setFont(new Font("Dialog",Font.BOLD,15));
        buttonPercent.setForeground(Color.blue.darker());
        buttonPercent.addActionListener(buttonHandler);
        buttonPercent.addKeyListener(keyListener);

        buttonMS = new MyButton("MS");
        buttonMS.setMargin(insets);
        buttonMS.setFont(new Font("Dialog",Font.BOLD,15));
        buttonMS.setForeground(Color.red);
        buttonMS.addActionListener(buttonHandler);
        buttonMS.addKeyListener(keyListener);

        button1 = new MyButton("1");
        button1.setMargin(insets);
        button1.setFont(new Font("Dialog",Font.BOLD,12));
        button1.setForeground(Color.blue);
        button1.addActionListener(buttonHandler);
        button1.addKeyListener(keyListener);

        button2 = new MyButton("2");
        button2.setMargin(insets);
        button2.setFont(new Font("Dialog",Font.BOLD,12));
        button2.setForeground(Color.blue);
        button2.addActionListener(buttonHandler);
        button2.addKeyListener(keyListener);

        button3 = new MyButton("3");
        button3.setMargin(insets);
        button3.setFont(new Font("Dialog",Font.BOLD,12));
        button3.setForeground(Color.blue);
        button3.addActionListener(buttonHandler);
        button3.addKeyListener(keyListener);

        buttonMinus = new MyButton("-");
        buttonMinus.setMargin(insets);
        buttonMinus.setFont(new Font("Dialog",Font.BOLD,15));
        buttonMinus.setForeground(Color.red);
        buttonMinus.addActionListener(buttonHandler);
        buttonMinus.addKeyListener(keyListener);

        buttonReciprocal = new MyButton("1/x");
        buttonReciprocal.setMargin(insets);
        buttonReciprocal.setFont(new Font("Dialog",Font.BOLD,15));
        buttonReciprocal.setForeground(Color.blue.darker());
        buttonReciprocal.addActionListener(buttonHandler);
        buttonReciprocal.addKeyListener(keyListener);

        buttonMPlus = new MyButton("M+");
        buttonMPlus.setMargin(insets);
        buttonMPlus.setFont(new Font("Dialog",Font.BOLD,15));
        buttonMPlus.setForeground(Color.red);
        buttonMPlus.addActionListener(buttonHandler);
        buttonMPlus.addKeyListener(keyListener);

        button0 = new MyButton("0");
        button0.setMargin(insets);
        button0.setFont(new Font("Dialog",Font.BOLD,12));
        button0.setForeground(Color.blue);
        button0.addActionListener(buttonHandler);
        button0.addKeyListener(keyListener);

        buttonSign = new MyButton("+/-");
        buttonSign.setMargin(insets);
        buttonSign.setFont(new Font("Dialog",Font.BOLD,15));
        buttonSign.setForeground(Color.blue);
        buttonSign.addActionListener(buttonHandler);
        buttonSign.addKeyListener(keyListener);

        buttonDot = new MyButton(".");
        buttonDot.setMargin(insets);
        buttonDot.setFont(new Font("Dialog",Font.BOLD,15));
        buttonDot.setForeground(Color.blue);
        buttonDot.addActionListener(buttonHandler);
        buttonDot.addKeyListener(keyListener);

        buttonPlus = new MyButton("+");
        buttonPlus.setMargin(insets);
        buttonPlus.setFont(new Font("Dialog",Font.BOLD,15));
        buttonPlus.setForeground(Color.red);
        buttonPlus.addActionListener(buttonHandler);
        buttonPlus.addKeyListener(keyListener);

        buttonEqual = new MyButton("=");
        buttonEqual.setMargin(insets);
        buttonEqual.setFont(new Font("Dialog",Font.BOLD,15));
        buttonEqual.setForeground(Color.red);
        buttonEqual.addActionListener(buttonHandler);
        buttonEqual.addKeyListener(keyListener);

        // End of Component Initialization

        // Add()s
        add(display);
        add(memory);
        add(buttonBack);
        add(buttonCE);
        add(buttonC);
        add(buttonMC);
        add(button7);
        add(button8);
        add(button9);
        add(buttonSlash);
        add(buttonSqrt);
        add(buttonMR);
        add(button4);
        add(button5);
        add(button6);
        add(buttonStar);
        add(buttonPercent);
        add(buttonMS);
        add(button1);
        add(button2);
        add(button3);
        add(buttonMinus);
        add(buttonReciprocal);
        add(buttonMPlus);
        add(button0);
        add(buttonSign);
        add(buttonDot);
        add(buttonPlus);
        add(buttonEqual);
        // End of Add()s
        setBounds(0,0,234,200);
        initialPositionSet();
        setToolTips();
    } // End of Constructor


	public Dimension getPreferredSize() {
		return new Dimension(235,205);
	}
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}


	/** Sets the bounds of all the components appropriately.
	 *
	 */
	private void initialPositionSet(){
    // initialPositionSet()
    display.setBounds(2,2,228,24);
    memory.setBounds(2,32,36,30);
    buttonBack.setBounds(108,32,40,30);
    buttonCE.setBounds(150,32,40,30);
    buttonC.setBounds(192,32,40,30);
    buttonMC.setBounds(2,72,36,30);
    button7.setBounds(44,72,36,30);
    button8.setBounds(82,72,36,30);
    button9.setBounds(120,72,36,30);
    buttonSlash.setBounds(158,72,36,30);
    buttonSqrt.setBounds(196,72,36,30);
    buttonMR.setBounds(2,104,36,30);
    button4.setBounds(44,104,36,30);
    button5.setBounds(82,104,36,30);
    button6.setBounds(120,104,36,30);
    buttonStar.setBounds(158,104,36,30);
    buttonPercent.setBounds(196,104,36,30);
    buttonMS.setBounds(2,136,36,30);
    button1.setBounds(44,136,36,30);
    button2.setBounds(82,136,36,30);
    button3.setBounds(120,136,36,30);
    buttonMinus.setBounds(158,136,36,30);
    buttonReciprocal.setBounds(196,136,36,30);
    buttonMPlus.setBounds(2,168,36,30);
    button0.setBounds(44,168,36,30);
    buttonSign.setBounds(82,168,36,30);
    buttonDot.setBounds(120,168,36,30);
    buttonPlus.setBounds(158,168,36,30);
    buttonEqual.setBounds(196,168,36,30);
  }// End of initialPositionSet()


	/** Sets ToolTips for all the buttons, indicating their keyboard equivalents and
	 *  the operations they perform.
	 *
	 */
	private void setToolTips(){
    // setToolTips()
		/** The tooltip message for buttons other than those for digits.*/
	String tip = new String();
	/**The tooltip message for buttons corresponding to digits.*/
	String numTip = "Puts this number in the calculator display.\nKeyboard equivalent = 0-9";
	tip = "Deletes the last digit of the\ndisplayed number.\nKeyboard equivalent = BACKSPACE";
    buttonBack.setToolTipText(tip);
    tip = "Clears the displayed number.\nKeyboard equivalent = DEL";
    buttonCE.setToolTipText(tip);
    tip = "Clears the current calculation.\nKeyboard equivalent = ESC";
    buttonC.setToolTipText(tip);
    tip = "Clears any number stored in memory.\nKeyboard equivalent = CTRL+L";
    buttonMC.setToolTipText(tip);
    button7.setToolTipText(numTip);
    button8.setToolTipText(numTip);
    button9.setToolTipText(numTip);
    tip = "Divides.\nKeyboard equivalent = /";
    buttonSlash.setToolTipText(tip);
    tip = "Calculates the square root of the\ndisplayed number.\nKeyboard equivalent = @";
    buttonSqrt.setToolTipText(tip);
    tip = "Recalls the number stored in memory.\nThe number remains in memory.\n";
    tip += "Keyboard equivalent = CTRL+R";
    buttonMR.setToolTipText(tip);
    button4.setToolTipText(numTip);
    button5.setToolTipText(numTip);
    button6.setToolTipText(numTip);
    tip = "Multiplies.\nKeyboard equivalent = *";
    buttonStar.setToolTipText(tip);
    tip = "Displays the result of multiplication as a\npercentage. Enter one number, click *,";
    tip += "\nclick the second number, and then click\n%.If you use any operator other than *,";
    tip += "\ncalculator assumes that you meant *, and\nmultiplies the numbers.\n";
    tip += "Keyboard equivalent = %";
    buttonPercent.setToolTipText(tip);
    tip = "Stores the displayed number in memory.\nKeyboard equivalent = CTRL+M";
    buttonMS.setToolTipText(tip);
    button1.setToolTipText(numTip);
    button2.setToolTipText(numTip);
    button3.setToolTipText(numTip);
    tip = "Subtracts.\nKeyboard equivalent = -";
    buttonMinus.setToolTipText(tip);
    tip = "Calculates the reciprocal of the\ndisplayed number.\nKeyboard equivalent = r";
    buttonReciprocal.setToolTipText(tip);
    tip = "Adds the displayed number to any\nnumber in memory.\nKeyboard equivalent = CTRL+P";
    buttonMPlus.setToolTipText(tip);
    button0.setToolTipText(numTip);
    tip = "Changes the sign of the\ndisplayed number.\nKeyboard equivalent = F9";
    buttonSign.setToolTipText(tip);
    tip = "Inserts a decimal point.\nKeyboard equivalent = . or ,";
    buttonDot.setToolTipText(tip);
    tip = "Adds.\nKeyboard equivalent = +";
    buttonPlus.setToolTipText(tip);
    tip = "Performs any operation on the previous\ntwo numbers. To repeat the last\n";
    tip += "operation, click again.\nKeyboard equivalent = ENTER";
    buttonEqual.setToolTipText(tip);
	}// End of setToolTips()



/** Handles the key events for the calculator. */
    private class MyKeyListener extends KeyAdapter {
    	public void keyPressed(KeyEvent evt) {
	      if (evt.getKeyCode() == evt.VK_1 || evt.getKeyCode() == evt.VK_NUMPAD1 ) button1.doClick();
	      else if (evt.getKeyCode() == evt.VK_2 && !evt.isShiftDown() || evt.getKeyCode() == evt.VK_NUMPAD2) button2.doClick();
	      else if (evt.getKeyCode() == evt.VK_3 || evt.getKeyCode() == evt.VK_NUMPAD3) button3.doClick();
	      else if (evt.getKeyCode() == evt.VK_4 || evt.getKeyCode() == evt.VK_NUMPAD4) button4.doClick();
	      else if (evt.getKeyCode() == evt.VK_5 && !evt.isShiftDown() || evt.getKeyCode() == evt.VK_NUMPAD5) button5.doClick();
	      else if (evt.getKeyCode() == evt.VK_6 || evt.getKeyCode() == evt.VK_NUMPAD6) button6.doClick();
	      else if (evt.getKeyCode() == evt.VK_7 || evt.getKeyCode() == evt.VK_NUMPAD7) button7.doClick();
	      else if (evt.getKeyCode() == evt.VK_8 && !evt.isShiftDown() || evt.getKeyCode() == evt.VK_NUMPAD8) button8.doClick();
	      else if (evt.getKeyCode() == evt.VK_9 || evt.getKeyCode() == evt.VK_NUMPAD9) button9.doClick();
	      else if (evt.getKeyCode() == evt.VK_0 || evt.getKeyCode() == evt.VK_NUMPAD0) button0.doClick();
	      else if (evt.getKeyCode() == evt.VK_DECIMAL || evt.getKeyCode() == evt.VK_COMMA || evt.getKeyCode() == evt.VK_PERIOD) buttonDot.doClick();
	      else if (evt.getKeyCode() == evt.VK_ENTER){buttonEqual.doClick(); evt.consume();}
	      else if (evt.getKeyCode() == evt.VK_ADD || evt.getKeyCode() == evt.VK_EQUALS && evt.isShiftDown()) buttonPlus.doClick();
	      else if (evt.getKeyCode() == evt.VK_SUBTRACT || evt.getKeyCode() == evt.VK_UNDERSCORE && evt.isShiftDown()) buttonMinus.doClick();
	      else if (evt.getKeyCode() == evt.VK_MULTIPLY || evt.getKeyCode() == evt.VK_8 && evt.isShiftDown()) buttonStar.doClick();
	      else if (evt.getKeyCode() == evt.VK_DIVIDE || evt.getKeyCode() == evt.VK_SLASH) buttonSlash.doClick();
	      else if (evt.getKeyCode() == evt.VK_BACK_SPACE) buttonBack.doClick();
	      else if (evt.getKeyCode() == evt.VK_DELETE) buttonCE.doClick();
	      else if (evt.getKeyCode() == evt.VK_ESCAPE) buttonC.doClick();
	      else if (evt.getKeyCode() == evt.VK_2 && evt.isShiftDown()) buttonSqrt.doClick();
	      else if (evt.getKeyCode() == evt.VK_R && !evt.isControlDown()) buttonReciprocal.doClick();
	      else if (evt.getKeyCode() == evt.VK_L && evt.isControlDown()) buttonMC.doClick();
	      else if (evt.getKeyCode() == evt.VK_R && evt.isControlDown()) buttonMR.doClick();
	      else if (evt.getKeyCode() == evt.VK_M && evt.isControlDown()) buttonMS.doClick();
	      else if (evt.getKeyCode() == evt.VK_P && evt.isControlDown()) buttonMPlus.doClick();
	      else if (evt.getKeyCode() == evt.VK_F9) buttonSign.doClick();
	      else if (evt.getKeyCode() == evt.VK_5 && evt.isShiftDown()) buttonPercent.doClick();
      }
    }

/** Handles the button press events for the calculator.*/
    class ButtonHandler implements ActionListener{
      public void actionPerformed(ActionEvent evt){
        String str = evt.getActionCommand();
        if("1"==str) button1_Action(str);
        else if ("2" == str) button1_Action(str);
        else if ("3" == str) button1_Action(str);
        else if ("4" == str) button1_Action(str);
        else if ("5" == str) button1_Action(str);
        else if ("6" == str) button1_Action(str);
        else if ("7" == str) button1_Action(str);
        else if ("8" == str) button1_Action(str);
        else if ("9" == str) button1_Action(str);
        else if ("0" == str) button1_Action(str);
        else if ("." == str) button1_Action(str);
        else if ("-" == str) buttonOpr_Action(str);
        else if ("+" == str) buttonOpr_Action(str);
        else if ("*" == str) buttonOpr_Action(str);
        else if ("/" == str) buttonOpr_Action(str);
        else if ("=" == str) buttonOpr_Action(str);
        else if ("<<" == str) buttonBack_Action(str);
        else if ("CE" == str) buttonCE_Action(str);
        else if ("C" == str) buttonC_Action(str);
        else if ("sqrt" == str) buttonOpr_Action(str);
        else if ("1/x" == str) buttonOpr_Action(str);
        else if ("MC" == str) buttonMC_Action(str);
        else if ("MR" == str) buttonMC_Action(str);
        else if ("MS" == str) buttonMC_Action(str);
        else if ("M+" == str) buttonMC_Action(str);
        else if ("+/-" == str) buttonSign_Action(str);
        else if ("%" == str) buttonOpr_Action(str);
      }
   }

 // Event Handling Routines

 /** Event handling routine called when digits from 0 to 9 or '.' is entered.*/
    private void button1_Action(String str){
  		oprFlag = true;
      if(clear){
        start = true;
        op = "=";
        arg = 0;
        clear = false;
      }
      if (start){
        if("." == str && -1 == display.getText().indexOf('.')){
          display.setText("0" + str);
        }
        else {
          if("." == str){
          	str = "0"+str;
          }
          display.setText(str);
        }
      }
      else
        if(str == "."){
          if(display.getText().indexOf('.')==-1){
            display.setText(display.getText() + str);
          }
        	else{
          	Toolkit.getDefaultToolkit().beep();
        	}
        }
        else{
          display.setText(display.getText() + str);
        }
      if ("0" != str) {
        start = false;
      }
      allowBack = true;
    }

  /** Event handling routine called when operator '+/-' is entered.*/
    private void buttonSign_Action(String str){
   		oprFlag = true;
      if(display.getText().length()>0){
        if((Character.isDigit(display.getText().charAt(0)) || display.getText().charAt(0) == '-') && (display.getText().charAt(0)!='0' || display.getText().indexOf('.')!=-1)){
          if(-1 == display.getText().indexOf('-')){
            display.setText("-"+display.getText());
          }
	        else{
            display.setText(display.getText().substring(1,display.getText().length()));
	        }
        }
        else{
          Toolkit.getDefaultToolkit().beep();
        }
      }
      else{
  	  	Toolkit.getDefaultToolkit().beep();
      }
    }

 /** Event handling routine called when '+','-','*','/','sqrt',1/x','=','%' is entered.*/
    private void buttonOpr_Action(String str){
      if("sqrt" == str){
        display.setText(Double.toString(Math.sqrt(Double.parseDouble(display.getText()))));
      }
      else if("1/x" == str){
        display.setText(Double.toString(1/Double.parseDouble(display.getText())));
      }
      else{
        if(str == "%"){
           percent = true;
        }
       	if(oprFlag || "=" == str){
         	calculate(Double.parseDouble(display.getText()));
       		oprFlag = false;
       	}
      }
      start = true;
      allowBack = false;
      if("sqrt" != str && "1/x" != str && "%" != str) {
        op = str;
      }
      clear = false;
    }

 /** Event handling routine called when '<<'(BACK) is pressed.*/
    private void buttonBack_Action(String str){
    	oprFlag = true;
      if(allowBack){
        if(display.getText().length()==1){
          display.setText("0");
          start = true;
          allowBack = false;
        }
        else{
        	display.setText(display.getText().substring(0,display.getText().length()-1));
        }
      }
      else{
        Toolkit.getDefaultToolkit().beep();
      }
    }

 /** Event handling routine called when 'CE' is pressed.*/
    private void buttonCE_Action(String str) {
    	oprFlag = true;
      display.setText("0");
      start = true;
    }

 /** Event handling routine called when 'C' is pressed.*/
    private void buttonC_Action(String str){
    	oprFlag = true;
      display.setText("0");
      start = true;
      op = "=";
      arg = 0;
    }

 /** Event handling routine called when 'MC','MR','MS','M+' is pressed.*/
    private void buttonMC_Action(String str){
    	oprFlag = true;
      if("MC" == str){
        mem = 0;
        memory.setText("");
      }
      else if("MS" == str){
        mem = Double.parseDouble(display.getText());
        if (mem != 0){
          memory.setText("  M");
        }
        start = true;
      }
      else if("MR" == str){
        display.setText(Double.toString(mem));
        start = true;
        clear = true;
      }
      else{
        mem += Double.parseDouble(display.getText());
        start = false;
      }
      allowBack = false;
    }

    // End of Event Handling Routines
	/**Performs the operations such as addition,substraction,multiplication,
    *division,modulo and displays the result.*/
    private void calculate(double n){
      if(!percent){
        if ("+" == op) arg += n;
        else if ("-" == op) arg -= n;
        else if ("*" == op) arg *= n;
        else if ("/" == op) arg /= n;
        else if ("%" == op) arg %= n;
        else if ("=" == op) arg = n;
      }
      else{
        arg *= n/100;
        percent = false;
        op = "=";
      }
      display.setText("" + arg);
   }


   /** A nonfocusable JButton.*/
   private class MyButton extends JButton{

   	/** Creates a nonfocusable JButton.*/
   	public MyButton(){
   	}

   	/** Creates a nonfocusable JButton with the specified text.*/
   	public MyButton(String text){
   		super(text);
   	}

   	/**Returns false so that the button cannot gain focus on tab key press.
   	  *@return false so that the button cannot gain focus on tab key press.
			*/
   	public boolean isFocusTraversable(){
   		return false;
   	}

   	/**Creates a tooltip for this button.
   	  *@return A MultiLineToolTip for this button.
   	  */
   	public JToolTip createToolTip() {
      MultiLineToolTip tip = new MultiLineToolTip();
      tip.setComponent(this);
      return tip;
    }
   }

   public static void main(String[] args){
   	JFrame frame = new JFrame("JCalculator");
   	frame.getContentPane().add(new Calculator());
   	frame.setVisible(true);
   	frame.pack();
   	frame.show();
   }
} // End of Class Calculator