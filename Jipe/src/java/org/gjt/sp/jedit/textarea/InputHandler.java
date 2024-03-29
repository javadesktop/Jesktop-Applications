/*
 * InputHandler.java - Manages key bindings and executes actions
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

package org.gjt.sp.jedit.textarea;

import javax.swing.text.*;
import javax.swing.JPopupMenu;
import java.awt.event.*;
import java.awt.Component;
import java.util.*;

/**
 * An input handler converts the user's key strokes into concrete actions.
 * It also takes care of macro recording and action repetition.<p>
 *
 * This class provides all the necessary support code for an input
 * handler, but doesn't actually do any key binding logic. It is up
 * to the implementations of this class to do so.
 *
 * @author Slava Pestov
 * @version $Id: InputHandler.java,v 1.1.1.1 2002-01-11 08:51:24 paul-h Exp $
 * @see org.gjt.sp.jedit.textarea.DefaultInputHandler
 */
public abstract class InputHandler extends KeyAdapter
{
	/**
	 * If this client property is set to Boolean.TRUE on the text area,
	 * the home/end keys will support 'smart' BRIEF-like behaviour
	 * (one press = start/end of line, two presses = start/end of
	 * viewscreen, three presses = start/end of document). By default,
	 * this property is not set.
	 */
	public static final String SMART_HOME_END_PROPERTY = "InputHandler.homeEnd";

	public static final ActionListener BACKSPACE = new backspace();
	public static final ActionListener BACKSPACE_WORD = new backspace_word();
	public static final ActionListener DELETE = new delete();
	public static final ActionListener DELETE_WORD = new delete_word();
	public static final ActionListener END = new end(false);
	public static final ActionListener DOCUMENT_END = new document_end(false);
	public static final ActionListener SELECT_END = new end(true);
	public static final ActionListener SELECT_DOC_END = new document_end(true);
	public static final ActionListener INSERT_BREAK = new insert_break();
	public static final ActionListener INSERT_TAB = new insert_tab();
	public static final ActionListener HOME = new home(false);
	public static final ActionListener DOCUMENT_HOME = new document_home(false);
	public static final ActionListener SELECT_HOME = new home(true);
	public static final ActionListener SELECT_DOC_HOME = new document_home(true);
	public static final ActionListener NEXT_CHAR = new next_char(false);
	public static final ActionListener NEXT_LINE = new next_line(false);
	public static final ActionListener NEXT_PAGE = new next_page(false);
	public static final ActionListener NEXT_WORD = new next_word(false);
	public static final ActionListener SELECT_NEXT_CHAR = new next_char(true);
	public static final ActionListener SELECT_NEXT_LINE = new next_line(true);
	public static final ActionListener SELECT_NEXT_PAGE = new next_page(true);
	public static final ActionListener SELECT_NEXT_WORD = new next_word(true);
	public static final ActionListener OVERWRITE = new overwrite();
	public static final ActionListener PREV_CHAR = new prev_char(false);
	public static final ActionListener PREV_LINE = new prev_line(false);
	public static final ActionListener PREV_PAGE = new prev_page(false);
	public static final ActionListener PREV_WORD = new prev_word(false);
	public static final ActionListener SELECT_PREV_CHAR = new prev_char(true);
	public static final ActionListener SELECT_PREV_LINE = new prev_line(true);
	public static final ActionListener SELECT_PREV_PAGE = new prev_page(true);
	public static final ActionListener SELECT_PREV_WORD = new prev_word(true);
	public static final ActionListener REPEAT = new repeat();

	// Default action
	public static final ActionListener INSERT_CHAR = new insert_char();

	private static Hashtable actions;

	static
	{
		actions = new Hashtable();
		actions.put("backspace",BACKSPACE);
		actions.put("backspace-word",BACKSPACE_WORD);
		actions.put("delete",DELETE);
		actions.put("delete-word",DELETE_WORD);
		actions.put("end",END);
		actions.put("select-end",SELECT_END);
		actions.put("document-end",DOCUMENT_END);
		actions.put("select-doc-end",SELECT_DOC_END);
		actions.put("insert-break",INSERT_BREAK);
		actions.put("insert-tab",INSERT_TAB);
		actions.put("home",HOME);
		actions.put("select-home",SELECT_HOME);
		actions.put("document-home",DOCUMENT_HOME);
		actions.put("select-doc-home",SELECT_DOC_HOME);
		actions.put("next-char",NEXT_CHAR);
		actions.put("next-line",NEXT_LINE);
		actions.put("next-page",NEXT_PAGE);
		actions.put("next-word",NEXT_WORD);
		actions.put("select-next-char",SELECT_NEXT_CHAR);
		actions.put("select-next-line",SELECT_NEXT_LINE);
		actions.put("select-next-page",SELECT_NEXT_PAGE);
		actions.put("select-next-word",SELECT_NEXT_WORD);
		actions.put("overwrite",OVERWRITE);
		actions.put("prev-char",PREV_CHAR);
		actions.put("prev-line",PREV_LINE);
		actions.put("prev-page",PREV_PAGE);
		actions.put("prev-word",PREV_WORD);
		actions.put("select-prev-char",SELECT_PREV_CHAR);
		actions.put("select-prev-line",SELECT_PREV_LINE);
		actions.put("select-prev-page",SELECT_PREV_PAGE);
		actions.put("select-prev-word",SELECT_PREV_WORD);
		actions.put("repeat",REPEAT);
		actions.put("insert-char",INSERT_CHAR);
	}

	/**
	 * Returns a named text area action.
	 * @param name The action name
	 */
	public static ActionListener getAction(String name)
	{
		return (ActionListener)actions.get(name);
	}

	/**
	 * Returns the name of the specified text area action.
	 * @param listener The action
	 */
	public static String getActionName(ActionListener listener)
	{
		Enumeration enum = getActions();
		while(enum.hasMoreElements())
		{
			String name = (String)enum.nextElement();
			ActionListener _listener = getAction(name);
			if(_listener == listener)
				return name;
		}
		return null;
	}

	/**
	 * Returns an enumeration of all available actions.
	 */
	public static Enumeration getActions()
	{
		return actions.keys();
	}

	/**
	 * Adds the default key bindings to this input handler.
	 * This should not be called in the constructor of this
	 * input handler, because applications might load the
	 * key bindings from a file, etc.
	 */
	public abstract void addDefaultKeyBindings();

	/**
	 * Adds a key binding to this input handler.
	 * @param keyBinding The key binding (the format of this is
	 * input-handler specific)
	 * @param action The action
	 */
	public abstract void addKeyBinding(String keyBinding, ActionListener action);

	/**
	 * Removes a key binding from this input handler.
	 * @param keyBinding The key binding
	 */
	public abstract void removeKeyBinding(String keyBinding);

	/**
	 * Removes all key bindings from this input handler.
	 */
	public abstract void removeAllKeyBindings();

	/**
	 * Grabs the next key typed event and invokes the specified
	 * action with the key as a the action command.
	 * @param action The action
	 */
	public void grabNextKeyStroke(ActionListener listener)
	{
		grabAction = listener;
	}

	/**
	 * Returns if repeating is enabled. When repeating is enabled,
	 * actions will be executed multiple times. This is usually
	 * invoked with a special key stroke in the input handler.
	 */
	public boolean isRepeatEnabled()
	{
		return repeat;
	}

	/**
	 * Enables repeating. When repeating is enabled, actions will be
	 * executed multiple times. Once repeating is enabled, the input
	 * handler should read a number from the keyboard.
	 */
	public void setRepeatEnabled(boolean repeat)
	{
		this.repeat = repeat;
		if(!repeat)
			repeatCount = 0;
	}

	/**
	 * Returns the number of times the next action will be repeated.
	 */
	public int getRepeatCount()
	{
		return (repeat ? Math.max(1,repeatCount) : 1);
	}

	/**
	 * Sets the number of times the next action will be repeated.
	 * @param repeatCount The repeat count
	 */
	public void setRepeatCount(int repeatCount)
	{
		this.repeatCount = repeatCount;
	}

	/**
	 * Returns the action used to handle text input.
	 */
	public ActionListener getInputAction()
	{
		return inputAction;
	}

	/**
	 * Sets the action used to handle text input.
	 * @param inputAction The new input action
	 */
	public void setInputAction(ActionListener inputAction)
	{
		this.inputAction = inputAction;
	}
	
	/**
	 * Returns the macro recorder. If this is non-null, all executed
	 * actions should be forwarded to the recorder.
	 */
	public InputHandler.MacroRecorder getMacroRecorder()
	{
		return recorder;
	}

	/**
	 * Sets the macro recorder. If this is non-null, all executed
	 * actions should be forwarded to the recorder.
	 * @param recorder The macro recorder
	 */
	public void setMacroRecorder(InputHandler.MacroRecorder recorder)
	{
		this.recorder = recorder;
	}

	/**
	 * Executes the specified action, repeating and recording it as
	 * necessary.
	 * @param listener The action listener
	 * @param source The event source
	 * @param actionCommand The action command
	 */
	public void executeAction(ActionListener listener, Object source,
		String actionCommand)
	{
		// create event
		ActionEvent evt = new ActionEvent(source,
			ActionEvent.ACTION_PERFORMED,
			actionCommand);

		// don't do anything if the action is a wrapper
		// (like EditAction.Wrapper)
		if(listener instanceof Wrapper)
		{
			listener.actionPerformed(evt);
			return;
		}

		// remember old values, in case action changes them
		boolean _repeat = repeat;
		int _repeatCount = getRepeatCount();

		// execute the action
		if(listener instanceof InputHandler.NonRepeatable)
			listener.actionPerformed(evt);
		else
		{
			for(int i = 0; i < _repeatCount; i++)
				listener.actionPerformed(evt);
		}

		// do recording. Notice that we do no recording whatsoever
		// for actions that grab keys
		if(grabAction == null)
		{
			if(recorder != null)
			{
				if(!(listener instanceof InputHandler.NonRecordable))
				{
					if(_repeatCount != 1)
						recorder.actionPerformed(REPEAT,String.valueOf(_repeatCount));

					recorder.actionPerformed(listener,actionCommand);
				}
			}

			// If repeat was true originally, clear it
			// Otherwise it might have been set by the action, etc
			if(_repeat)
				setRepeatEnabled(false);
		}
	}

	/**
	 * Returns the text area that fired the specified event.
	 * @param evt The event
	 */
	public static JEditTextArea getTextArea(EventObject evt)
	{
		if(evt != null)
		{
			Object o = evt.getSource();
			if(o instanceof Component)
			{
				// find the parent text area
				Component c = (Component)o;
				for(;;)
				{
					if(c instanceof JEditTextArea)
						return (JEditTextArea)c;
					else if(c == null)
						break;
					if(c instanceof JPopupMenu)
						c = ((JPopupMenu)c)
							.getInvoker();
					else
						c = c.getParent();
				}
			}
		}

		// this shouldn't happen
		System.err.println("BUG: getTextArea() returning null");
		System.err.println("Report this to Slava Pestov <sp@gjt.org>");
		return null;
	}

	// protected members
	protected ActionListener inputAction = INSERT_CHAR;
	protected ActionListener grabAction;
	protected boolean repeat;
	protected int repeatCount;
	protected InputHandler.MacroRecorder recorder;

	/**
	 * If a key is being grabbed, this method should be called with
	 * the appropriate key event. It executes the grab action with
	 * the typed character as the parameter.
	 */
	protected void handleGrabAction(KeyEvent evt)
	{
		// Clear it *before* it is executed so that executeAction()
		// resets the repeat count
		ActionListener _grabAction = grabAction;
		grabAction = null;

		char keyChar = evt.getKeyChar();
		int keyCode = evt.getKeyCode();

		String arg;

		if(keyChar != KeyEvent.VK_UNDEFINED)
			arg = String.valueOf(keyChar);
		else if(keyCode == KeyEvent.VK_TAB)
			arg = "\t";
		else if(keyCode == KeyEvent.VK_ENTER)
			arg = "\n";
		else
			arg = "\0";

		executeAction(_grabAction,evt.getSource(),arg);
	}

	/**
	 * If an action implements this interface, it should not be repeated.
	 * Instead, it will handle the repetition itself.
	 */
	public interface NonRepeatable {}

	/**
	 * If an action implements this interface, it should not be recorded
	 * by the macro recorder. Instead, it will do its own recording.
	 */
	public interface NonRecordable {}

	/**
	 * For use by EditAction.Wrapper only.
	 * @since jEdit 2.2final
	 */
	public interface Wrapper {}

	/**
	 * Macro recorder.
	 */
	public interface MacroRecorder
	{
		void actionPerformed(ActionListener listener,
			String actionCommand);
	}

	public static class backspace implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(textArea.getSelectionStart()
			   != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}
			else
			{
				int caret = textArea.getCaretPosition();
				if(caret == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				try
				{
					textArea.getDocument().remove(caret - 1,1);
				}
				catch(BadLocationException bl)
				{
					bl.printStackTrace();
				}
			}
		}
	}

	public static class backspace_word implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int start = textArea.getSelectionStart();
			if(start != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
				return;
			}

			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			int caret = start - lineStart;

			String lineText = textArea.getLineText(textArea
				.getCaretLine());

			if(caret == 0)
			{
				if(lineStart == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				caret--;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = TextUtilities.findWordStart(lineText,
					caret-1,noWordSep);
			}

			try
			{
				textArea.getDocument().remove(
						caret + lineStart,
						start - (caret + lineStart));
			}
			catch(BadLocationException bl)
			{
				bl.printStackTrace();
			}
		}
	}

	public static class delete implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(textArea.getSelectionStart()
			   != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}
			else
			{
				int caret = textArea.getCaretPosition();
				if(caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				try
				{
					textArea.getDocument().remove(caret,1);
				}
				catch(BadLocationException bl)
				{
					bl.printStackTrace();
				}
			}
		}
	}

	public static class delete_word implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int start = textArea.getSelectionStart();
			if(start != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
				return;
			}

			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			int caret = start - lineStart;

			String lineText = textArea.getLineText(textArea
				.getCaretLine());

			if(caret == lineText.length())
			{
				if(lineStart + caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				caret++;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = TextUtilities.findWordEnd(lineText,
					caret+1,noWordSep);
			}

			try
			{
				textArea.getDocument().remove(start,
					(caret + lineStart) - start);
			}
			catch(BadLocationException bl)
			{
				bl.printStackTrace();
			}
		}
	}

	public static class end implements ActionListener
	{
		private boolean select;

		public end(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			int caret = textArea.getCaretPosition();

			int lastOfLine = textArea.getLineEndOffset(
				textArea.getCaretLine()) - 1;
			int lastVisibleLine = textArea.getFirstLine()
				+ textArea.getVisibleLines();
			if(lastVisibleLine >= textArea.getLineCount())
			{
				lastVisibleLine = Math.min(textArea.getLineCount() - 1,
					lastVisibleLine);
			}
			else
				lastVisibleLine -= (textArea.getElectricScroll() + 1);

			int lastVisible = textArea.getLineEndOffset(lastVisibleLine) - 1;
			int lastDocument = textArea.getDocumentLength();

			if(caret == lastDocument)
			{
				textArea.getToolkit().beep();
				return;
			}
			else if(!Boolean.TRUE.equals(textArea.getClientProperty(
				SMART_HOME_END_PROPERTY)))
				caret = lastOfLine;
			else if(caret == lastVisible)
				caret = lastDocument;
			else if(caret == lastOfLine)
				caret = lastVisible;
			else
				caret = lastOfLine;

			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class document_end implements ActionListener
	{
		private boolean select;

		public document_end(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			if(select)
				textArea.select(textArea.getMarkPosition(),
					textArea.getDocumentLength());
			else
				textArea.setCaretPosition(textArea
					.getDocumentLength());
		}
	}

	public static class home implements ActionListener
	{
		private boolean select;

		public home(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			int caret = textArea.getCaretPosition();

			int firstLine = textArea.getFirstLine();

			int firstOfLine = textArea.getLineStartOffset(
				textArea.getCaretLine());
			int firstVisibleLine = (firstLine == 0 ? 0 :
				firstLine + textArea.getElectricScroll());
			int firstVisible = textArea.getLineStartOffset(
				firstVisibleLine);

			if(caret == 0)
			{
				textArea.getToolkit().beep();
				return;
			}
			else if(!Boolean.TRUE.equals(textArea.getClientProperty(
				SMART_HOME_END_PROPERTY)))
				caret = firstOfLine;
			else if(caret == firstVisible)
				caret = 0;
			else if(caret == firstOfLine)
				caret = firstVisible;
			else
				caret = firstOfLine;

			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class document_home implements ActionListener
	{
		private boolean select;

		public document_home(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			if(select)
				textArea.select(textArea.getMarkPosition(),0);
			else
				textArea.setCaretPosition(0);
		}
	}

	public static class insert_break implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}
//			try{
//			if(textArea.getText().endsWith("\t}"))textArea.getDocument().remove(textArea.getCaretPosition()-2,1);
//			} catch(BadLocationException bl) { }
			textArea.setSelectedText("\n");
			if(textArea.isAutoIndent())Indent.indent(textArea);  //Added by SL
			
			
		}
	}

	public static class insert_tab implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			textArea.overwriteSetSelectedText("\t");
		}
	}

	public static class next_char implements ActionListener
	{
		private boolean select;

		public next_char(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			if(caret == textArea.getDocumentLength())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					caret + 1);
			else
				textArea.setCaretPosition(caret + 1);
		}
	}

	public static class next_line implements ActionListener
	{
		private boolean select;

		public next_line(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();

			if(line == textArea.getLineCount() - 1)
			{
				textArea.getToolkit().beep();
				return;
			}

			int magic = textArea.getMagicCaretPosition();
			if(magic == -1)
			{
				magic = textArea.offsetToX(line,
					caret - textArea.getLineStartOffset(line));
			}

			caret = textArea.getLineStartOffset(line + 1)
				+ textArea.xToOffset(line + 1,magic + 1);
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
			textArea.setMagicCaretPosition(magic);
		}
	}

	public static class next_page implements ActionListener
	{
		private boolean select;

		public next_page(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int lineCount = textArea.getLineCount();
			int firstLine = textArea.getFirstLine();
			int visibleLines = textArea.getVisibleLines();
			int line = textArea.getCaretLine();

			firstLine += visibleLines;

			if(firstLine + visibleLines >= lineCount - 1)
				firstLine = lineCount - visibleLines;

			textArea.setFirstLine(firstLine);

			int caret = textArea.getLineStartOffset(
				Math.min(textArea.getLineCount() - 1,
				line + visibleLines));
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class next_word implements ActionListener
	{
		private boolean select;

		public next_word(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			caret -= lineStart;

			String lineText = textArea.getLineText(textArea
				.getCaretLine());

			if(caret == lineText.length())
			{
				if(lineStart + caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				caret++;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = TextUtilities.findWordEnd(lineText,caret + 1,noWordSep);
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					lineStart + caret);
			else
				textArea.setCaretPosition(lineStart + caret);
		}
	}

	public static class overwrite implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			textArea.setOverwriteEnabled(
				!textArea.isOverwriteEnabled());
		}
	}

	public static class prev_char implements ActionListener
	{
		private boolean select;

		public prev_char(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			if(caret == 0)
			{
				textArea.getToolkit().beep();
				return;
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					caret - 1);
			else
				textArea.setCaretPosition(caret - 1);
		}
	}

	public static class prev_line implements ActionListener
	{
		private boolean select;

		public prev_line(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();

			if(line == 0)
			{
				textArea.getToolkit().beep();
				return;
			}

			int magic = textArea.getMagicCaretPosition();
			if(magic == -1)
			{
				magic = textArea.offsetToX(line,
					caret - textArea.getLineStartOffset(line));
			}

			caret = textArea.getLineStartOffset(line - 1)
				+ textArea.xToOffset(line - 1,magic + 1);
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
			textArea.setMagicCaretPosition(magic);
		}
	}

	public static class prev_page implements ActionListener
	{
		private boolean select;

		public prev_page(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int firstLine = textArea.getFirstLine();
			int visibleLines = textArea.getVisibleLines();
			int line = textArea.getCaretLine();

			if(firstLine < visibleLines)
				firstLine = visibleLines;

			textArea.setFirstLine(firstLine - visibleLines);

			int caret = textArea.getLineStartOffset(
				Math.max(0,line - visibleLines));
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class prev_word implements ActionListener
	{
		private boolean select;

		public prev_word(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			caret -= lineStart;

			String lineText = textArea.getLineText(textArea
				.getCaretLine());

			if(caret == 0)
			{
				if(lineStart == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				caret--;
			}
			else
			{
				String noWordSep = (String)textArea.getDocument().getProperty("noWordSep");
				caret = TextUtilities.findWordStart(lineText,caret - 1,noWordSep);
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					lineStart + caret);
			else
				textArea.setCaretPosition(lineStart + caret);
		}
	}

	public static class repeat implements ActionListener,
		InputHandler.NonRecordable
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			textArea.getInputHandler().setRepeatEnabled(true);
			String actionCommand = evt.getActionCommand();
			if(actionCommand != null)
			{
				textArea.getInputHandler().setRepeatCount(
					Integer.parseInt(actionCommand));
			}
		}
	}

	public static class insert_char implements ActionListener,
		InputHandler.NonRepeatable
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			String str = evt.getActionCommand();
			int repeatCount = textArea.getInputHandler().getRepeatCount();

			if(textArea.isEditable())
			{
				StringBuffer buf = new StringBuffer();
				for(int i = 0; i < repeatCount; i++)
					buf.append(str);
				textArea.overwriteSetSelectedText(buf.toString());
			}
			else
			{
				textArea.getToolkit().beep();
			}
		}
	}
}

/*
 * ChangeLog:
 * $Log: not supported by cvs2svn $
 * Revision 1.0  2000-07-15 11:24:32+01  steve_lawson
 * Initial revision
 *
 * Revision 1.0  2000-06-29 21:50:39+01  steve_lawson
 * Initial revision
 *
 * Revision 1.22  2000/03/18 05:45:25  sp
 * Complete word overhaul, various other changes
 *
 * Revision 1.21  2000/02/15 07:44:30  sp
 * bug fixes, doc updates, etc
 *
 * Revision 1.20  2000/02/10 08:32:51  sp
 * Bug fixes, doc updates
 *
 * Revision 1.19  2000/01/28 00:20:58  sp
 * Lots of stuff
 *
 * Revision 1.18  2000/01/21 00:35:29  sp
 * Various updates
 *
 * Revision 1.16  1999/12/24 01:20:20  sp
 * Bug fixing and other stuff for 2.3pre1
 *
 * Revision 1.15  1999/12/19 11:14:29  sp
 * Static abbrev expansion started
 *
 * Revision 1.14  1999/12/13 03:40:30  sp
 * Bug fixes, syntax is now mostly GPL'd
 *
 * Revision 1.13  1999/12/03 23:48:10  sp
 * C+END/C+HOME, LOADING BufferUpdate message, misc stuff
 *
 * Revision 1.12  1999/11/21 07:59:30  sp
 * JavaDoc updates
 *
 * Revision 1.11  1999/11/21 03:40:18  sp
 * Parts of EditBus not used by core moved to EditBus.jar
 *
 * Revision 1.10  1999/11/21 01:20:31  sp
 * Bug fixes, EditBus updates, fixed some warnings generated by jikes +P
 *
 * Revision 1.9  1999/11/19 08:54:52  sp
 * EditBus integrated into the core, event system gone, bug fixes
 *
 * Revision 1.8  1999/11/16 08:21:20  sp
 * Various fixes, attempt at beefing up expand-abbrev
 *
 * Revision 1.7  1999/11/09 10:14:34  sp
 * Macro code cleanups, menu item and tool bar clicks are recorded now, delete
 * word commands, check box menu item support
 *
 * Revision 1.6  1999/10/24 02:06:41  sp
 * Miscallaneous pre1 stuff
 *
 * Revision 1.5  1999/10/05 10:55:29  sp
 * File dialogs open faster, and experimental keyboard macros
 *
 */
