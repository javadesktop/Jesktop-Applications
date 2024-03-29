/*
 * DefaultInputHandler.java - Default implementation of an input handler
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

import javax.swing.KeyStroke;
import java.awt.event.*;
import java.awt.Toolkit;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * The default input handler. It maps sequences of keystrokes into actions
 * and inserts key typed events into the text area.
 * @author Slava Pestov
 * @version $Id: DefaultInputHandler.java,v 1.1.1.1 2002-01-11 08:51:19 paul-h Exp $
 */
public class DefaultInputHandler extends InputHandler
{
	/**
	 * Creates a new input handler with no key bindings defined.
	 */
	public DefaultInputHandler()
	{
		bindings = currentBindings = new Hashtable();
	}

	/**
	 * Creates a new input handler with the same set of key bindings
	 * as the one specified. Note that both input handlers share
	 * a pointer to exactly the same key binding table; so adding
	 * a key binding in one will also add it to the other.
	 * @param copy The input handler to copy key bindings from
	 */
	public DefaultInputHandler(DefaultInputHandler copy)
	{
		bindings = currentBindings = copy.bindings;
	}

	/**
	 * Sets up the default key bindings.
	 */
	public void addDefaultKeyBindings()
	{
		addKeyBinding("BACK_SPACE",BACKSPACE);
		addKeyBinding("C+BACK_SPACE",BACKSPACE_WORD);
		addKeyBinding("DELETE",DELETE);
		addKeyBinding("C+DELETE",DELETE_WORD);

		addKeyBinding("ENTER",INSERT_BREAK);
		addKeyBinding("TAB",INSERT_TAB);

		addKeyBinding("INSERT",OVERWRITE);

		addKeyBinding("HOME",HOME);
		addKeyBinding("END",END);
		addKeyBinding("S+HOME",SELECT_HOME);
		addKeyBinding("S+END",SELECT_END);
		addKeyBinding("C+HOME",DOCUMENT_HOME);
		addKeyBinding("C+END",DOCUMENT_END);
		addKeyBinding("CS+HOME",SELECT_DOC_HOME);
		addKeyBinding("CS+END",SELECT_DOC_END);

		addKeyBinding("PAGE_UP",PREV_PAGE);
		addKeyBinding("PAGE_DOWN",NEXT_PAGE);
		addKeyBinding("S+PAGE_UP",SELECT_PREV_PAGE);
		addKeyBinding("S+PAGE_DOWN",SELECT_NEXT_PAGE);

		addKeyBinding("LEFT",PREV_CHAR);
		addKeyBinding("S+LEFT",SELECT_PREV_CHAR);
		addKeyBinding("C+LEFT",PREV_WORD);
		addKeyBinding("CS+LEFT",SELECT_PREV_WORD);
		addKeyBinding("RIGHT",NEXT_CHAR);
		addKeyBinding("S+RIGHT",SELECT_NEXT_CHAR);
		addKeyBinding("C+RIGHT",NEXT_WORD);
		addKeyBinding("CS+RIGHT",SELECT_NEXT_WORD);
		addKeyBinding("UP",PREV_LINE);
		addKeyBinding("S+UP",SELECT_PREV_LINE);
		addKeyBinding("DOWN",NEXT_LINE);
		addKeyBinding("S+DOWN",SELECT_NEXT_LINE);

		addKeyBinding("C+ENTER",REPEAT);
	}

	/**
	 * Adds a key binding to this input handler. The key binding is
	 * a list of white space separated key strokes of the form
	 * <i>[modifiers+]key</i> where modifier is C for Control, A for Alt,
	 * or S for Shift, and key is either a character (a-z) or a field
	 * name in the KeyEvent class prefixed with VK_ (e.g., BACK_SPACE)
	 * @param keyBinding The key binding
	 * @param action The action
	 */
	public void addKeyBinding(String keyBinding, ActionListener action)
	{
	        Hashtable current = bindings;

		StringTokenizer st = new StringTokenizer(keyBinding);
		while(st.hasMoreTokens())
		{
			KeyStroke keyStroke = parseKeyStroke(st.nextToken());
			if(keyStroke == null)
				return;

			if(st.hasMoreTokens())
			{
				Object o = current.get(keyStroke);
				if(o instanceof Hashtable)
					current = (Hashtable)o;
				else
				{
					o = new Hashtable();
					current.put(keyStroke,o);
					current = (Hashtable)o;
				}
			}
			else
				current.put(keyStroke,action);
		}
	}

	/**
	 * Removes a key binding from this input handler. This is not yet
	 * implemented.
	 * @param keyBinding The key binding
	 */
	public void removeKeyBinding(String keyBinding)
	{
		throw new InternalError("Not yet implemented");
	}

	/**
	 * Removes all key bindings from this input handler.
	 */
	public void removeAllKeyBindings()
	{
		bindings.clear();
	}

	/**
	 * Handle a key pressed event. This will look up the binding for
	 * the key stroke and execute it.
	 */
	public void keyPressed(KeyEvent evt)
	{
		int keyCode = evt.getKeyCode();
		int modifiers = evt.getModifiers();

		if(keyCode == KeyEvent.VK_CONTROL ||
			keyCode == KeyEvent.VK_SHIFT ||
			keyCode == KeyEvent.VK_ALT ||
			keyCode == KeyEvent.VK_META)
			return;

		if((modifiers & ~KeyEvent.SHIFT_MASK) != 0
			|| evt.isActionKey()
			|| keyCode == KeyEvent.VK_BACK_SPACE
			|| keyCode == KeyEvent.VK_DELETE
			|| keyCode == KeyEvent.VK_ENTER
			|| keyCode == KeyEvent.VK_TAB
			|| keyCode == KeyEvent.VK_ESCAPE)
		{
			if(grabAction != null)
			{
				handleGrabAction(evt);
				return;
			}

			KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode,
				modifiers);
			Object o = currentBindings.get(keyStroke);
			if(o == null)
			{
				// Don't beep if the user presses some
				// key we don't know about unless a
				// prefix is active. Otherwise it will
				// beep when caps lock is pressed, etc.
				if(currentBindings != bindings)
				{
					Toolkit.getDefaultToolkit().beep();
					// F10 should be passed on, but C+e F10
					// shouldn't
					repeatCount = 0;
					repeat = false;
					evt.consume();
				}
				currentBindings = bindings;
				return;
			}
			else if(o instanceof ActionListener)
			{
				currentBindings = bindings;

				executeAction(((ActionListener)o),
					evt.getSource(),null);

				evt.consume();
				return;
			}
			else if(o instanceof Hashtable)
			{
				currentBindings = (Hashtable)o;
				evt.consume();
				return;
			}
		}
	}

	/**
	 * Handle a key typed event. This inserts the key into the text area.
	 */
	public void keyTyped(KeyEvent evt)
	{
		int modifiers = evt.getModifiers();
		char c = evt.getKeyChar();
		if(c != KeyEvent.CHAR_UNDEFINED &&
			(modifiers & KeyEvent.ALT_MASK) == 0)
		{
			if(c >= 0x20 && c != 0x7f)
			{
				KeyStroke keyStroke = KeyStroke.getKeyStroke(
					Character.toUpperCase(c));
				Object o = currentBindings.get(keyStroke);

				if(o instanceof Hashtable)
				{
					currentBindings = (Hashtable)o;
					return;
				}
				else if(o instanceof ActionListener)
				{
					currentBindings = bindings;
					executeAction((ActionListener)o,
						evt.getSource(),
						String.valueOf(c));
					return;
				}

				currentBindings = bindings;

				if(grabAction != null)
				{
					handleGrabAction(evt);
					return;
				}

				// 0-9 adds another 'digit' to the repeat number
				if(repeat && Character.isDigit(c))
				{
					setRepeatCount(repeatCount * 10
						+ (c - '0'));
					return;
				}

				executeAction(inputAction,evt.getSource(),
					String.valueOf(evt.getKeyChar()));

				repeatCount = 0;
				repeat = false;
			}
		}
	}

	/**
	 * Converts a string to a keystroke. The string should be of the
	 * form <i>modifiers</i>+<i>shortcut</i> where <i>modifiers</i>
	 * is any combination of A for Alt, C for Control, S for Shift
	 * or M for Meta, and <i>shortcut</i> is either a single character,
	 * or a keycode name from the <code>KeyEvent</code> class, without
	 * the <code>VK_</code> prefix.
	 * @param keyStroke A string description of the key stroke
	 */
	public static KeyStroke parseKeyStroke(String keyStroke)
	{
		if(keyStroke == null)
			return null;
		int modifiers = 0;
		int index = keyStroke.indexOf('+');
		if(index != -1)
		{
			for(int i = 0; i < index; i++)
			{
				switch(Character.toUpperCase(keyStroke
					.charAt(i)))
				{
				case 'A':
					modifiers |= InputEvent.ALT_MASK;
					break;
				case 'C':
					modifiers |= InputEvent.CTRL_MASK;
					break;
				case 'M':
					modifiers |= InputEvent.META_MASK;
					break;
				case 'S':
					modifiers |= InputEvent.SHIFT_MASK;
					break;
				}
			}
		}
		String key = keyStroke.substring(index + 1);
		if(key.length() == 1)
		{
			char ch = Character.toUpperCase(key.charAt(0));
			if(modifiers == 0)
				return KeyStroke.getKeyStroke(ch);
			else
				return KeyStroke.getKeyStroke(ch,modifiers);
		}
		else if(key.length() == 0)
		{
			System.err.println("Invalid key stroke: " + keyStroke);
			return null;
		}
		else
		{
			int ch;

			try
			{
				ch = KeyEvent.class.getField("VK_".concat(key))
					.getInt(null);
			}
			catch(Exception e)
			{
				System.err.println("Invalid key stroke: "
					+ keyStroke);
				return null;
			}

			return KeyStroke.getKeyStroke(ch,modifiers);
		}
	}

	// private members
	private Hashtable bindings;
	private Hashtable currentBindings;
}

/*
 * ChangeLog:
 * $Log: not supported by cvs2svn $
 * Revision 1.0  2000-07-15 11:24:35+01  steve_lawson
 * Initial revision
 *
 * Revision 1.0  2000-06-29 21:50:36+01  steve_lawson
 * Initial revision
 *
 * Revision 1.21  2000/03/18 05:45:25  sp
 * Complete word overhaul, various other changes
 *
 * Revision 1.20  2000/02/12 03:56:58  sp
 * 2.3pre5 stuff
 *
 * Revision 1.19  1999/12/19 11:14:29  sp
 * Static abbrev expansion started
 *
 * Revision 1.18  1999/12/13 03:40:30  sp
 * Bug fixes, syntax is now mostly GPL'd
 *
 * Revision 1.17  1999/12/03 23:48:10  sp
 * C+END/C+HOME, LOADING BufferUpdate message, misc stuff
 *
 * Revision 1.16  1999/11/16 08:21:20  sp
 * Various fixes, attempt at beefing up expand-abbrev
 *
 * Revision 1.15  1999/11/09 10:14:34  sp
 * Macro code cleanups, menu item and tool bar clicks are recorded now, delete
 * word commands, check box menu item support
 *
 * Revision 1.14  1999/10/26 07:43:59  sp
 * Session loading and saving, directory list search started
 *
 * Revision 1.13  1999/10/24 02:06:41  sp
 * Miscallaneous pre1 stuff
 *
 * Revision 1.12  1999/10/17 04:16:28  sp
 * Bug fixing
 *
 * Revision 1.11  1999/10/10 06:38:45  sp
 * Bug fixes and quicksort routine
 *
 * Revision 1.10  1999/10/06 08:39:46  sp
 * Fixes to repeating and macro features
 *
 * Revision 1.9  1999/10/05 10:55:29  sp
 * File dialogs open faster, and experimental keyboard macros
 *
 * Revision 1.8  1999/10/05 04:43:58  sp
 * Minor bug fixes and updates
 *
 * Revision 1.7  1999/10/04 06:13:52  sp
 * Repeat counts now supported
 *
 * Revision 1.6  1999/09/30 12:21:05  sp
 * No net access for a month... so here's one big jEdit 2.1pre1
 *
 */
