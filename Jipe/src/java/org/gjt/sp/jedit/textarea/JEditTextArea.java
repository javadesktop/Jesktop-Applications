/*
 * JEditTextArea.java - jEdit's text component
 * Copyright (C) 1999, 2000 Slava Pestov
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

import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;
import org.gjt.sp.jedit.syntax.*;
import net.jesktop.jipe.*;

/**
 * jEdit's text area component. It is more suited for editing program
 * source code than JEditorPane, because it drops the unnecessary features
 * (images, variable-width lines, and so on) and adds a whole bunch of
 * useful goodies such as:
 * <ul>
 * <li>More flexible key binding scheme
 * <li>Supports macro recorders
 * <li>Rectangular selection
 * <li>Bracket highlighting
 * <li>Syntax highlighting
 * <li>Command repetition
 * <li>Block caret can be enabled
 * </ul>
 * It is also faster and doesn't have as many problems. It can be used
 * in other applications; the only other part of jEdit it depends on is
 * the syntax package.<p>
 *
 * To use it in your app, treat it like any other component, for example:
 * <pre>JEditTextArea ta = new JEditTextArea();
 * ta.setTokenMarker(new JavaTokenMarker());
 * ta.setText("public class Test {\n"
 *     + "    public static void main(String[] args) {\n"
 *     + "        System.out.println(\"Hello World\");\n"
 *     + "    }\n"
 *     + "}");</pre>
 *
 * @author Slava Pestov
 * @version $Id: JEditTextArea.java,v 1.1.1.1 2002-01-11 08:51:29 paul-h Exp $
 */
public class JEditTextArea extends JComponent
{
  /**
   * Adding components with this name to the text area will place
   * them left of the horizontal scroll bar. In jEdit, the status
   * bar is added this way.
   */
  public static String LEFT_OF_SCROLLBAR = "los";

  /**
  *Adding a boolean which keeps tabs on amended documents
  */
  public boolean dirty;

  /**
  *Adding a boolean which keeps tabs on autoindent
  */
  public boolean autoindent;


  /**
   *Add a int to keep track of an editors tab size
   */

  public int tabSize;


  /**
   * Creates a new JEditTextArea with the default settings.
   */
  public JEditTextArea()
  {
    this(TextAreaDefaults.getDefaults());
  }

  /**
   * Creates a new JEditTextArea with the specified settings.
   * @param defaults The default settings
   */
  public JEditTextArea(TextAreaDefaults defaults)
  {
    // Enable the necessary events
    enableEvents(AWTEvent.KEY_EVENT_MASK);

    // Initialize some misc. stuff
    painter = new TextAreaPainter(this,defaults);
    gutter = new Gutter(this, defaults);
    documentHandler = new DocumentHandler();
    listenerList = new EventListenerList();
    caretEvent = new MutableCaretEvent();
    bracketLine = bracketPosition = -1;
    blink = true;
    lineSegment = new Segment();

    // Initialize the GUI
    setLayout(new ScrollLayout());
    add(LEFT,gutter);
    add(CENTER,painter);
    add(RIGHT,vertical = new JScrollBar(JScrollBar.VERTICAL));
    add(BOTTOM,horizontal = new JScrollBar(JScrollBar.HORIZONTAL));

    // Add some event listeners
    vertical.addAdjustmentListener(new AdjustHandler());
    horizontal.addAdjustmentListener(new AdjustHandler());
    painter.addComponentListener(new ComponentHandler());
    painter.addMouseListener(new MouseHandler());
    painter.addMouseMotionListener(new DragHandler());
    addFocusListener(new FocusHandler());

    // Load the defaults
    setInputHandler(defaults.inputHandler);
    editable = defaults.editable;
    caretVisible = defaults.caretVisible;
    caretBlinks = defaults.caretBlinks;
    electricScroll = defaults.electricScroll;

    popup = defaults.popup;

    setDocument(new SyntaxDocument());

    // We don't seem to get the initial focus event?
    focusedComponent = this;
  }

  /**
   * Return true if area content has changed, false otherwise.
   */

  public boolean isDirty()
  {
    return dirty;
  }

  /**
   * Called when the content of the area has changed.
   */

  public void setDirty()
  {
    dirty = true;
  }

  /**
   * Called after having saved or created a new document to ensure
   * the content isn't 'dirty'.
   */

  public void clean()
  {
    dirty = false;
  }


  /**
   * Call to set AutoIndent boolean
   */

  public void setAutoIndentOn()
  {
    autoindent=true;
  }

  public void setAutoIndentOff(){
    autoindent=false;
  }

  public boolean isAutoIndent()
  {
    return autoindent;
  }

  public void setTabSize(int size)
  {
    tabSize=size;
  }

  public int getTabSize()
  {
    return tabSize;
  }

  /**
   * Return the state of the softtab check menu item.
   * This is necessary to know if tabs have to be replaced
   * by whitespaces.
   */

  public static boolean getSoftTab()
  {
    return false;
  }

  /**
   * Returns if this component can be traversed by pressing
   * the Tab key. This returns false.
   */
  public final boolean isManagingFocus()
  {
    return true;
  }

  /**
   * Returns 0,0 for split pane compatibility.
   */
  public final Dimension getMinimumSize()
  {
    return new Dimension(0,0);
  }

  /**
   * Returns the object responsible for painting this text area.
   */
  public final TextAreaPainter getPainter()
  {
    return painter;
  }

  /**
   * Returns the gutter to the left of the text area or null if the gutter
   * is disabled
   */
  public final Gutter getGutter()
  {
    return gutter;
  }


      private CompoundEdit compoundEdit;

    /**
   * Used for ReplaceAll.
   * This merges all text changes made between the beginCompoundEdit()
   * and the endCompoundEdit() calls into only one undo event.
   */

  public void beginCompoundEdit()
  {
    beginCompoundEdit(true);
  }

  public void beginCompoundEdit(boolean cursorHandle)
  {
    if (compoundEdit == null)
    {
      compoundEdit = new CompoundEdit();
    }
//SL    if (cursorHandle) waitingCursor(true);
  }

  /**
   * See beginCompoundEdit().
   */

  public void endCompoundEdit()
  {
    endCompoundEdit(true);
  }

  public void endCompoundEdit(boolean cursorHandle)
  {
    if (compoundEdit != null)
    {
      compoundEdit.end();
  //    if (compoundEdit.canUndo())
  //      undo.addEdit(compoundEdit);
      compoundEdit = null;
    }
//SL    if (cursorHandle) waitingCursor(false);
  }



  /**
   * Returns the input handler.
   */
  public final InputHandler getInputHandler()
  {
    return inputHandler;
  }

  /**
   * Sets the input handler.
   * @param inputHandler The new input handler
   */
  public void setInputHandler(InputHandler inputHandler)
  {
    this.inputHandler = inputHandler;
  }

  /**
   * Returns true if the caret is blinking, false otherwise.
   */
  public final boolean isCaretBlinkEnabled()
  {
    return caretBlinks;
  }

  /**
   * Toggles caret blinking.
   * @param caretBlinks True if the caret should blink, false otherwise
   */
  public void setCaretBlinkEnabled(boolean caretBlinks)
  {
    this.caretBlinks = caretBlinks;
    if(!caretBlinks)
      blink = false;

    painter.invalidateSelectedLines();
  }

  /**
   * Returns true if the caret is visible, false otherwise.
   */
  public final boolean isCaretVisible()
  {
    return (!caretBlinks || blink) && caretVisible;
  }

  /**
   * Sets if the caret should be visible.
   * @param caretVisible True if the caret should be visible, false
   * otherwise
   */
  public void setCaretVisible(boolean caretVisible)
  {
    this.caretVisible = caretVisible;
    blink = true;

    painter.invalidateSelectedLines();
  }

  /**
   * Blinks the caret.
   */
  public final void blinkCaret()
  {
    if(caretBlinks)
    {
      blink = !blink;
      painter.invalidateSelectedLines();
    }
    else
      blink = true;
  }

  /**
   * Returns the number of lines from the top and button of the
   * text area that are always visible.
   */
  public final int getElectricScroll()
  {
    return electricScroll;
  }

  /**
   * Sets the number of lines from the top and bottom of the text
   * area that are always visible
   * @param electricScroll The number of lines always visible from
   * the top or bottom
   */
  public final void setElectricScroll(int electricScroll)
  {
    this.electricScroll = electricScroll;
  }

  /**
   * Updates the state of the scroll bars. This should be called
   * if the number of lines in the document changes, or when the
   * size of the text are changes.
   */
  public void updateScrollBars()
  {
    if(vertical != null && visibleLines != 0)
    {
      vertical.setValues(firstLine,visibleLines,0,getLineCount());
      vertical.setUnitIncrement(2);
      vertical.setBlockIncrement(visibleLines);
    }

    int width = painter.getWidth();
    if(horizontal != null && width != 0)
    {
      horizontal.setValues(-horizontalOffset,width,0,width * 5);
      horizontal.setUnitIncrement(painter.getFontMetrics()
        .charWidth('w'));
      horizontal.setBlockIncrement(width / 2);
    }
  }

  /**
   * Returns the line displayed at the text area's origin.
   */
  public final int getFirstLine()
  {
    return firstLine;
  }

  /**
   * Sets the line displayed at the text area's origin without
   * updating the scroll bars.
   */
  public void setFirstLine(int firstLine)
  {
    if(firstLine == this.firstLine)
      return;
    int oldFirstLine = this.firstLine;
    this.firstLine = firstLine;
    if(firstLine != vertical.getValue())
      updateScrollBars();
    painter.repaint();
    gutter.repaint();
  }

  /**
   * Returns the number of lines visible in this text area.
   */
  public final int getVisibleLines()
  {
    return visibleLines;
  }

  /**
   * Recalculates the number of visible lines. This should not
   * be called directly.
   */
  public final void recalculateVisibleLines()
  {
    if(painter == null)
      return;
    int height = painter.getHeight();
    int lineHeight = painter.getFontMetrics().getHeight();
    int oldVisibleLines = visibleLines;
    visibleLines = height / lineHeight;
    updateScrollBars();
  }

  /**
   * Returns the horizontal offset of drawn lines.
   */
  public final int getHorizontalOffset()
  {
    return horizontalOffset;
  }

  /**
   * Sets the horizontal offset of drawn lines. This can be used to
   * implement horizontal scrolling.
   * @param horizontalOffset offset The new horizontal offset
   */
  public void setHorizontalOffset(int horizontalOffset)
  {
    if(horizontalOffset == this.horizontalOffset)
      return;
    this.horizontalOffset = horizontalOffset;
    if(horizontalOffset != horizontal.getValue())
      updateScrollBars();
    painter.repaint();
  }

  /**
   * A fast way of changing both the first line and horizontal
   * offset.
   * @param firstLine The new first line
   * @param horizontalOffset The new horizontal offset
   * @return True if any of the values were changed, false otherwise
   */
  public boolean setOrigin(int firstLine, int horizontalOffset)
  {
    boolean changed = false;
    int oldFirstLine = this.firstLine;

    if(horizontalOffset != this.horizontalOffset)
    {
      this.horizontalOffset = horizontalOffset;
      changed = true;
    }

    if(firstLine != this.firstLine)
    {
      this.firstLine = firstLine;
      changed = true;
    }

    if(changed)
    {
      updateScrollBars();
      painter.repaint();
      gutter.repaint();
    }

    return changed;
  }

  /**
   * Ensures that the caret is visible by scrolling the text area if
   * necessary.
   * @return True if scrolling was actually performed, false if the
   * caret was already visible
   */
  public boolean scrollToCaret()
  {
    int line = getCaretLine();
    int lineStart = getLineStartOffset(line);
    int offset = Math.max(0,Math.min(getLineLength(line) - 1,
      getCaretPosition() - lineStart));

    return scrollTo(line,offset);
  }

  /**
   * Ensures that the specified line and offset is visible by scrolling
   * the text area if necessary.
   * @param line The line to scroll to
   * @param offset The offset in the line to scroll to
   * @return True if scrolling was actually performed, false if the
   * line and offset was already visible
   */
  public boolean scrollTo(int line, int offset)
  {
    // visibleLines == 0 before the component is realized
    // we can't do any proper scrolling then, so we have
    // this hack...
    if(visibleLines == 0)
    {
      setFirstLine(Math.max(0,line - electricScroll));
      return true;
    }

    int newFirstLine = firstLine;
    int newHorizontalOffset = horizontalOffset;

    if(line < firstLine + electricScroll)
    {
      newFirstLine = Math.max(0,line - electricScroll);
    }
    else if(line + electricScroll >= firstLine + visibleLines)
    {
      newFirstLine = (line - visibleLines) + electricScroll + 1;
      if(newFirstLine + visibleLines >= getLineCount())
        newFirstLine = getLineCount() - visibleLines;
      if(newFirstLine < 0)
        newFirstLine = 0;
    }

    int x = offsetToX(line,offset);
    int width = painter.getFontMetrics().charWidth('w');

    if(x < 0)
    {
      newHorizontalOffset = Math.min(0,horizontalOffset
        - x + width + 5);
    }
    else if(x + width >= painter.getWidth())
    {
      newHorizontalOffset = horizontalOffset +
        (painter.getWidth() - x) - width - 5;
    }

    return setOrigin(newFirstLine,newHorizontalOffset);
  }

  /**
   * Converts a line index to a y co-ordinate.
   * @param line The line
   */
  public int lineToY(int line)
  {
    FontMetrics fm = painter.getFontMetrics();
    return (line - firstLine) * fm.getHeight()
      - (fm.getLeading() + fm.getMaxDescent());
  }

  /**
   * Converts a y co-ordinate to a line index.
   * @param y The y co-ordinate
   */
  public int yToLine(int y)
  {
    FontMetrics fm = painter.getFontMetrics();
    int height = fm.getHeight();
    return Math.max(0,Math.min(getLineCount() - 1,
      y / height + firstLine));
  }

  /**
   * Converts an offset in a line into an x co-ordinate.
   * @param line The line
   * @param offset The offset, from the start of the line
   */
  public int offsetToX(int line, int offset)
  {
    TokenMarker tokenMarker = getTokenMarker();

    /* Use painter's cached info for speed */
    FontMetrics fm = painter.getFontMetrics();

    getLineText(line,lineSegment);

    int segmentOffset = lineSegment.offset;
    int x = horizontalOffset;

    /* If syntax coloring is disabled, do simple translation */
    if(tokenMarker == null)
    {
      lineSegment.count = offset;
      return x + Utilities.getTabbedTextWidth(lineSegment,
        fm,x,painter,0);
    }
    /* If syntax coloring is enabled, we have to do this because
     * tokens can vary in width */
    else
    {
      Token tokens = tokenMarker.markTokens(lineSegment,line);

      Toolkit toolkit = painter.getToolkit();
      Font defaultFont = painter.getFont();
      SyntaxStyle[] styles = painter.getStyles();

      for(;;)
      {
        byte id = tokens.id;
        if(id == Token.END)
        {
          return x;
        }

        if(id == Token.NULL)
          fm = painter.getFontMetrics();
        else
          fm = styles[id].getFontMetrics(defaultFont);

        int length = tokens.length;

        if(offset + segmentOffset < lineSegment.offset + length)
        {
          lineSegment.count = offset - (lineSegment.offset - segmentOffset);
          return x + Utilities.getTabbedTextWidth(
            lineSegment,fm,x,painter,0);
        }
        else
        {
          lineSegment.count = length;
          x += Utilities.getTabbedTextWidth(
            lineSegment,fm,x,painter,0);
          lineSegment.offset += length;
        }
        tokens = tokens.next;
      }
    }
  }

  /**
   * Converts an x co-ordinate to an offset within a line.
   * @param line The line
   * @param x The x co-ordinate
   */
  public int xToOffset(int line, int x)
  {
    TokenMarker tokenMarker = getTokenMarker();

    /* Use painter's cached info for speed */
    FontMetrics fm = painter.getFontMetrics();

    getLineText(line,lineSegment);

    char[] segmentArray = lineSegment.array;
    int segmentOffset = lineSegment.offset;
    int segmentCount = lineSegment.count;

    int width = horizontalOffset;

    if(tokenMarker == null)
    {
      for(int i = 0; i < segmentCount; i++)
      {
        char c = segmentArray[i + segmentOffset];
        int charWidth;
        if(c == '\t')
          charWidth = (int)painter.nextTabStop(width,i)
            - width;
        else
          charWidth = fm.charWidth(c);

        if(painter.isBlockCaretEnabled())
        {
          if(x - charWidth <= width)
            return i;
        }
        else
        {
          if(x - charWidth / 2 <= width)
            return i;
        }

        width += charWidth;
      }

      return segmentCount;
    }
    else
    {
      Token tokens = tokenMarker.markTokens(lineSegment,line);

      int offset = 0;
      Toolkit toolkit = painter.getToolkit();
      Font defaultFont = painter.getFont();
      SyntaxStyle[] styles = painter.getStyles();

      for(;;)
      {
        byte id = tokens.id;
        if(id == Token.END)
          return offset;

        if(id == Token.NULL)
          fm = painter.getFontMetrics();
        else
          fm = styles[id].getFontMetrics(defaultFont);

        int length = tokens.length;

        for(int i = 0; i < length; i++)
        {
          char c = segmentArray[segmentOffset + offset + i];
          int charWidth;
          if(c == '\t')
            charWidth = (int)painter.nextTabStop(width,offset + i)
              - width;
          else
            charWidth = fm.charWidth(c);

          if(painter.isBlockCaretEnabled())
          {
            if(x - charWidth <= width)
              return offset + i;
          }
          else
          {
            if(x - charWidth / 2 <= width)
              return offset + i;
          }

          width += charWidth;
        }

        offset += length;
        tokens = tokens.next;
      }
    }
  }

  /**
   * Converts a point to an offset, from the start of the text.
   * @param x The x co-ordinate of the point
   * @param y The y co-ordinate of the point
   */
  public int xyToOffset(int x, int y)
  {
    int line = yToLine(y);
    int start = getLineStartOffset(line);
    return start + xToOffset(line,x);
  }

  /**
   * Returns the document this text area is editing.
   */
  public final SyntaxDocument getDocument()
  {
    return document;
  }

  /**
   * Sets the document this text area is editing.
   * @param document The document
   */
  public void setDocument(SyntaxDocument document)
  {
    if(this.document == document)
      return;
    if(this.document != null)
      this.document.removeDocumentListener(documentHandler);
    this.document = document;

    document.addDocumentListener(documentHandler);
    documentHandlerInstalled = true;

    select(0,0);
    updateScrollBars();
    painter.repaint();
    gutter.repaint();
  }

  /**
   * Returns the document's token marker. Equivalent to calling
   * <code>getDocument().getTokenMarker()</code>.
   */
  public final TokenMarker getTokenMarker()
  {
    return document.getTokenMarker();
  }

  /**
   * Sets the document's token marker. Equivalent to caling
   * <code>getDocument().setTokenMarker()</code>.
   * @param tokenMarker The token marker
   */
  public final void setTokenMarker(TokenMarker tokenMarker)
  {
    document.setTokenMarker(tokenMarker);
    getDocumentStyle(tokenMarker);
  }
  /**
   * Returns the Style Type of the document.
   * <code>getStyle()</code>.
   * @param tokenMarker The token marker
   */
                public final void getDocumentStyle(TokenMarker tokenMarker)
                {
  if(tokenMarker .getClass().getName().endsWith("JavaTokenMarker"))painter.setStyles(SyntaxUtilities.getJSyntaxStyles());
  else if(tokenMarker .getClass().getName().endsWith("HTMLTokenMarker"))painter.setStyles(SyntaxUtilities.getHTMLSyntaxStyles());
  else if(tokenMarker .getClass().getName().endsWith("CTokenMarker"))painter.setStyles(SyntaxUtilities.getCSyntaxStyles());
  else if(tokenMarker .getClass().getName().endsWith("JSPTokenMarker"))painter.setStyles(SyntaxUtilities.getJSPSyntaxStyles());
  }

  /**
   * Returns the length of the document. Equivalent to calling
   * <code>getDocument().getLength()</code>.
   */
  public final int getDocumentLength()
  {
    return document.getLength();
  }

  /**
   * Returns the number of lines in the document.
   */
  public final int getLineCount()
  {
    return document.getDefaultRootElement().getElementCount();
  }

  /**
   * Returns the line containing the specified offset.
   * @param offset The offset
   */
  public final int getLineOfOffset(int offset)
  {
    return document.getDefaultRootElement().getElementIndex(offset);
  }

  /**
   * Returns the start offset of the specified line.
   * @param line The line
   * @return The start offset of the specified line, or -1 if the line is
   * invalid
   */
  public int getLineStartOffset(int line)
  {
    Element lineElement = document.getDefaultRootElement()
      .getElement(line);
    if(lineElement == null)
      return -1;
    else
      return lineElement.getStartOffset();
  }

  /**
   * Returns the end offset of the specified line.
   * @param line The line
   * @return The end offset of the specified line, or -1 if the line is
   * invalid.
   */
  public int getLineEndOffset(int line)
  {
    Element lineElement = document.getDefaultRootElement()
      .getElement(line);
    if(lineElement == null)
      return -1;
    else
      return lineElement.getEndOffset();
  }

  /**
   * Returns the length of the specified line.
   * @param line The line
   */
  public int getLineLength(int line)
  {
    Element lineElement = document.getDefaultRootElement()
      .getElement(line);
    if(lineElement == null)
      return -1;
    else
      return lineElement.getEndOffset()
        - lineElement.getStartOffset() - 1;
  }

  /**
   * Returns the entire text of this text area.
   */
  public String getText()
  {
    try
    {
      return document.getText(0,document.getLength());
    }
    catch(BadLocationException bl)
    {
      bl.printStackTrace();
      return null;
    }
  }

  /**
   * Sets the entire text of this text area.
   */
  public void setText(String text)
  {
    try
    {
      document.beginCompoundEdit();
      document.remove(0,document.getLength());
      document.insertString(0,text,null);
    }
    catch(BadLocationException bl)
    {
      bl.printStackTrace();
    }
    finally
    {
      document.endCompoundEdit();
    }
  }

  /**
   * Returns the specified substring of the document.
   * @param start The start offset
   * @param len The length of the substring
   * @return The substring, or null if the offsets are invalid
   */
  public final String getText(int start, int len)
  {
    try
    {
      return document.getText(start,len);
    }
    catch(BadLocationException bl)
    {
      bl.printStackTrace();
      return null;
    }
  }

  /**
   * Copies the specified substring of the document into a segment.
   * If the offsets are invalid, the segment will contain a null string.
   * @param start The start offset
   * @param len The length of the substring
   * @param segment The segment
   */
  public final void getText(int start, int len, Segment segment)
  {
    try
    {
      document.getText(start,len,segment);
    }
    catch(BadLocationException bl)
    {
      bl.printStackTrace();
      segment.offset = segment.count = 0;
    }
  }

  /**
   * Returns the text on the specified line.
   * @param lineIndex The line
   * @return The text, or null if the line is invalid
   */
  public final String getLineText(int lineIndex)
  {
    int start = getLineStartOffset(lineIndex);
    return getText(start,getLineEndOffset(lineIndex) - start - 1);
  }

  /**
   * Copies the text on the specified line into a segment. If the line
   * is invalid, the segment will contain a null string.
   * @param lineIndex The line
   */
  public final void getLineText(int lineIndex, Segment segment)
  {
    int start = getLineStartOffset(lineIndex);
    getText(start,getLineEndOffset(lineIndex) - start - 1,segment);
  }

  /**
   * Returns the selection start offset.
   */
  public final int getSelectionStart()
  {
    return selectionStart;
  }

  /**
   * Returns the offset where the selection starts on the specified
   * line.
   */
  public int getSelectionStart(int line)
  {
    if(line == selectionStartLine)
      return selectionStart;
    else if(rectSelect)
    {
      Element map = document.getDefaultRootElement();
      int start = selectionStart - map.getElement(selectionStartLine)
        .getStartOffset();

      Element lineElement = map.getElement(line);
      int lineStart = lineElement.getStartOffset();
      int lineEnd = lineElement.getEndOffset() - 1;
      return Math.min(lineEnd,lineStart + start);
    }
    else
      return getLineStartOffset(line);
  }

  /**
   * Returns the selection start line.
   */
  public final int getSelectionStartLine()
  {
    return selectionStartLine;
  }

  /**
   * Sets the selection start. The new selection will be the new
   * selection start and the old selection end.
   * @param selectionStart The selection start
   * @see #select(int,int)
   */
  public final void setSelectionStart(int selectionStart)
  {
    select(selectionStart,selectionEnd);
  }

  /**
   * Returns the selection end offset.
   */
  public final int getSelectionEnd()
  {
    return selectionEnd;
  }

  /**
   * Returns the offset where the selection ends on the specified
   * line.
   */
  public int getSelectionEnd(int line)
  {
    if(line == selectionEndLine)
      return selectionEnd;
    else if(rectSelect)
    {
      Element map = document.getDefaultRootElement();
      int end = selectionEnd - map.getElement(selectionEndLine)
        .getStartOffset();

      Element lineElement = map.getElement(line);
      int lineStart = lineElement.getStartOffset();
      int lineEnd = lineElement.getEndOffset() - 1;
      return Math.min(lineEnd,lineStart + end);
    }
    else
      return getLineEndOffset(line) - 1;
  }

  /**
   * Returns the selection end line.
   */
  public final int getSelectionEndLine()
  {
    return selectionEndLine;
  }

  /**
   * Sets the selection end. The new selection will be the old
   * selection start and the bew selection end.
   * @param selectionEnd The selection end
   * @see #select(int,int)
   */
  public final void setSelectionEnd(int selectionEnd)
  {
    select(selectionStart,selectionEnd);
  }

  /**
   * Returns the caret position. This will either be the selection
   * start or the selection end, depending on which direction the
   * selection was made in.
   */
  public final int getCaretPosition()
  {
    return (biasLeft ? selectionStart : selectionEnd);
  }

  /**
   * Returns the caret line.
   */
  public final int getCaretLine()
  {
    return (biasLeft ? selectionStartLine : selectionEndLine);
  }

  /**
   * Returns the mark position. This will be the opposite selection
   * bound to the caret position.
   * @see #getCaretPosition()
   */
  public final int getMarkPosition()
  {
    return (biasLeft ? selectionEnd : selectionStart);
  }

  /**
   * Returns the mark line.
   */
  public final int getMarkLine()
  {
    return (biasLeft ? selectionEndLine : selectionStartLine);
  }

  /**
   * Sets the caret position. The new selection will consist of the
   * caret position only (hence no text will be selected)
   * @param caret The caret position
   * @see #select(int,int)
   */
  public final void setCaretPosition(int caret)
  {
    select(caret,caret);
  }

  /**
   * Selects all text in the document.
   */
  public final void selectAll()
  {
    select(0,getDocumentLength());
  }

  /**
   * Moves the mark to the caret position.
   */
  public final void selectNone()
  {
    select(getCaretPosition(),getCaretPosition());
  }

  /**
   * Selects from the start offset to the end offset. This is the
   * general selection method used by all other selecting methods.
   * The caret position will be start if start &lt; end, and end
   * if end &gt; start.
   * @param start The start offset
   * @param end The end offset
   */
  public void select(int start, int end)
  {
    int newStart, newEnd;
    boolean newBias;
    if(start <= end)
    {
      newStart = start;
      newEnd = end;
      newBias = false;
    }
    else
    {
      newStart = end;
      newEnd = start;
      newBias = true;
    }

    if(newStart < 0 || newEnd > getDocumentLength())
    {
      throw new IllegalArgumentException("Bounds out of"
        + " range: " + newStart + "," +
        newEnd);
    }

    // If the new position is the same as the old, we don't
    // do all this crap, however we still do the stuff at
    // the end (clearing magic position, scrolling)
    if(newStart != selectionStart || newEnd != selectionEnd
      || newBias != biasLeft)
    {
      updateBracketHighlight(end);

      int newStartLine = getLineOfOffset(newStart);
      int newEndLine = getLineOfOffset(newEnd);

      painter.invalidateLineRange(selectionStartLine,selectionEndLine);
      painter.invalidateLineRange(newStartLine,newEndLine);

      document.addUndoableEdit(new CaretUndo(
        selectionStart,selectionEnd,
        newStart,newEnd));

      selectionStart = newStart;
      selectionEnd = newEnd;
      selectionStartLine = newStartLine;
      selectionEndLine = newEndLine;
      biasLeft = newBias;

      fireCaretEvent();
    }

    // When the user is typing, etc, we don't want the caret
    // to blink
    blink = true;
    caretTimer.restart();

    // Disable rectangle select if selection start = selection end
    if(selectionStart == selectionEnd)
      rectSelect = false;

    // Clear the `magic' caret position used by up/down
    magicCaret = -1;

    scrollToCaret();
  }

  /**
   * Returns the selected text, or null if no selection is active.
   */
  public final String getSelectedText()
  {
    if(selectionStart == selectionEnd)
      return null;

    if(rectSelect)
    {
      // Return each row of the selection on a new line

      Element map = document.getDefaultRootElement();

      int start = selectionStart - map.getElement(selectionStartLine)
        .getStartOffset();
      int end = selectionEnd - map.getElement(selectionEndLine)
        .getStartOffset();

      // Certain rectangles satisfy this condition...
      if(end < start)
      {
        int tmp = end;
        end = start;
        start = tmp;
      }

      StringBuffer buf = new StringBuffer();
      Segment seg = new Segment();

      for(int i = selectionStartLine; i <= selectionEndLine; i++)
      {
        Element lineElement = map.getElement(i);
        int lineStart = lineElement.getStartOffset();
        int lineEnd = lineElement.getEndOffset() - 1;
        int lineLen = lineEnd - lineStart;

        lineStart = Math.min(lineStart + start,lineEnd);
        lineLen = Math.min(end - start,lineEnd - lineStart);

        getText(lineStart,lineLen,seg);
        buf.append(seg.array,seg.offset,seg.count);

        if(i != selectionEndLine)
          buf.append('\n');
      }

      return buf.toString();
    }
    else
    {
      return getText(selectionStart,
        selectionEnd - selectionStart);
    }
  }

  /**
   * Replaces the selection with the specified text.
   * @param selectedText The replacement text for the selection
   */
  public void setSelectedText(String selectedText)
  {
    if(!editable)
    {
      throw new InternalError("Text component"
        + " read only");
    }

    document.beginCompoundEdit();

    try
    {
      if(rectSelect)
      {
        Element map = document.getDefaultRootElement();

        int start = selectionStart - map.getElement(selectionStartLine)
          .getStartOffset();
        int end = selectionEnd - map.getElement(selectionEndLine)
          .getStartOffset();

        // Certain rectangles satisfy this condition...
        if(end < start)
        {
          int tmp = end;
          end = start;
          start = tmp;
        }

        int lastNewline = 0;
        int currNewline = 0;

        for(int i = selectionStartLine; i <= selectionEndLine; i++)
        {
          Element lineElement = map.getElement(i);
          int lineStart = lineElement.getStartOffset();
          int lineEnd = lineElement.getEndOffset() - 1;
          int rectStart = Math.min(lineEnd,lineStart + start);

          document.remove(rectStart,Math.min(lineEnd - rectStart,
            end - start));

          if(selectedText == null)
            continue;

          currNewline = selectedText.indexOf('\n',lastNewline);
          if(currNewline == -1)
            currNewline = selectedText.length();

          document.insertString(rectStart,selectedText
            .substring(lastNewline,currNewline),null);

          lastNewline = Math.min(selectedText.length(),
            currNewline + 1);
        }

        if(selectedText != null &&
          currNewline != selectedText.length())
        {
          int offset = map.getElement(selectionEndLine)
            .getEndOffset() - 1;
          document.insertString(offset,"\n",null);
          document.insertString(offset + 1,selectedText
            .substring(currNewline + 1),null);
        }
      }
      else
      {
        document.remove(selectionStart,
          selectionEnd - selectionStart);
        if(selectedText != null)
        {
          document.insertString(selectionStart,
            selectedText,null);
        }
      }
    }
    catch(BadLocationException bl)
    {
      bl.printStackTrace();
      throw new InternalError("Cannot replace"
        + " selection");
    }
    // No matter what happends... stops us from leaving document
    // in a bad state
    finally
    {
      document.endCompoundEdit();
    }

    setCaretPosition(selectionEnd);
  }

  /**
   * Returns true if this text area is editable, false otherwise.
   */
  public final boolean isEditable()
  {
    return editable;
  }

  /**
   * Sets if this component is editable.
   * @param editable True if this text area should be editable,
   * false otherwise
   */
  public final void setEditable(boolean editable)
  {
    this.editable = editable;
  }

  /**
   * Returns the right click popup menu.
   */
  public final JPopupMenu getRightClickPopup()
  {
    return popup;
  }

  /**
   * Sets the right click popup menu.
   * @param popup The popup
   */
  public final void setRightClickPopup(JPopupMenu popup)
  {
    this.popup = popup;
  }

  /**
   * Returns the `magic' caret position. This can be used to preserve
   * the column position when moving up and down lines.
   */
  public final int getMagicCaretPosition()
  {
    return magicCaret;
  }

  /**
   * Sets the `magic' caret position. This can be used to preserve
   * the column position when moving up and down lines.
   * @param magicCaret The magic caret position
   */
  public final void setMagicCaretPosition(int magicCaret)
  {
    this.magicCaret = magicCaret;
  }

  /**
   * Similar to <code>setSelectedText()</code>, but overstrikes the
   * appropriate number of characters if overwrite mode is enabled.
   * @param str The string
   * @see #setSelectedText(String)
   * @see #isOverwriteEnabled()
   */
  public void overwriteSetSelectedText(String str)
  {
    // Don't overstrike if there is a selection
    if(!overwrite || selectionStart != selectionEnd)
    {
      setSelectedText(str);
      return;
    }

    // Don't overstrike if we're on the end of
    // the line
    int caret = getCaretPosition();
    int caretLineEnd = getLineEndOffset(getCaretLine());
    if(caretLineEnd - caret <= str.length())
    {
      setSelectedText(str);
      return;
    }

    document.beginCompoundEdit();

    try
    {
      document.remove(caret,str.length());
      document.insertString(caret,str,null);
    }
    catch(BadLocationException bl)
    {
      bl.printStackTrace();
    }
    finally
    {
      document.endCompoundEdit();
    }
  }

  /**
   * Returns true if overwrite mode is enabled, false otherwise.
   */
  public final boolean isOverwriteEnabled()
  {
    return overwrite;
  }

  /**
   * Sets if overwrite mode should be enabled.
   * @param overwrite True if overwrite mode should be enabled,
   * false otherwise.
   */
  public final void setOverwriteEnabled(boolean overwrite)
  {
    this.overwrite = overwrite;
    painter.invalidateSelectedLines();
  }

  /**
   * Returns true if the selection is rectangular, false otherwise.
   */
  public final boolean isSelectionRectangular()
  {
    return rectSelect;
  }

  /**
   * Sets if the selection should be rectangular.
   * @param overwrite True if the selection should be rectangular,
   * false otherwise.
   */
  public final void setSelectionRectangular(boolean rectSelect)
  {
    this.rectSelect = rectSelect;
    painter.invalidateSelectedLines();
  }

  /**
   * Returns the position of the highlighted bracket (the bracket
   * matching the one before the caret)
   */
  public final int getBracketPosition()
  {
    return bracketPosition;
  }

  /**
   * Returns the line of the highlighted bracket (the bracket
   * matching the one before the caret)
   */
  public final int getBracketLine()
  {
    return bracketLine;
  }

  /**
   * Adds a caret change listener to this text area.
   * @param listener The listener
   */
  public final void addCaretListener(CaretListener listener)
  {
    listenerList.add(CaretListener.class,listener);
  }

  /**
   * Removes a caret change listener from this text area.
   * @param listener The listener
   */
  public final void removeCaretListener(CaretListener listener)
  {
    listenerList.remove(CaretListener.class,listener);
  }

  /**
   * Deletes the selected text from the text area and places it
   * into the clipboard.
   */
  public void cut()
  {
    if(editable)
    {
      copy();
      setSelectedText("");
    }
  }

  /**
   * Places the selected text into the clipboard.
   */
  public void copy()
  {
    if(selectionStart != selectionEnd)
    {
      Clipboard clipboard = getToolkit().getSystemClipboard();

      String selection = getSelectedText();

      int repeatCount = inputHandler.getRepeatCount();
      StringBuffer buf = new StringBuffer();
      for(int i = 0; i < repeatCount; i++)
        buf.append(selection);

      clipboard.setContents(new StringSelection(buf.toString()),null);
    }
  }

  /**
   * Inserts the clipboard contents into the text.
   */
  public void paste()
  {
    if(editable)
    {
      Clipboard clipboard = getToolkit().getSystemClipboard();
      try
      {
        // The MacOS MRJ doesn't convert \r to \n,
        // so do it here
        String selection = ((String)clipboard
          .getContents(this).getTransferData(
          DataFlavor.stringFlavor))
          .replace('\r','\n');

        int repeatCount = inputHandler.getRepeatCount();
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < repeatCount; i++)
          buf.append(selection);
        selection = buf.toString();
        setSelectedText(selection);
      }
      catch(Exception e)
      {
        getToolkit().beep();
        System.err.println("Clipboard does not"
          + " contain a string");
      }
    }
  }

  /**
   * Returns the status bar component (which was added with a name
   * of LEFT_OF_SCROLLBAR).
   */
  public Component getStatus()
  {
    return ((ScrollLayout)getLayout()).leftOfScrollBar;
  }

  /**
   * Called by the AWT when this component is added to a parent.
   * Adds document listener.
   */
  public void addNotify()
  {
    super.addNotify();

    if(!documentHandlerInstalled)
    {
      documentHandlerInstalled = true;
      document.addDocumentListener(documentHandler);
    }
  }

  /**
   * Called by the AWT when this component is removed from it's parent.
   * This clears the pointer to the currently focused component.
   * Also removes document listener.
   */
  public void removeNotify()
  {
    super.removeNotify();
    if(focusedComponent == this)
      focusedComponent = null;

    if(documentHandlerInstalled)
    {
      document.removeDocumentListener(documentHandler);
      documentHandlerInstalled = false;
    }
  }

  /**
   * Forwards key events directly to the input handler.
   * This is slightly faster than using a KeyListener
   * because some Swing overhead is avoided.
   */

  public void processKeyEvent(KeyEvent evt)
  {
    if(inputHandler == null)
      return;
      if(!isDirty())setDirty();

    switch(evt.getID())
    {
    case KeyEvent.KEY_TYPED:
      inputHandler.keyTyped(evt);
      break;
    case KeyEvent.KEY_PRESSED:
      inputHandler.keyPressed(evt);
      break;
    case KeyEvent.KEY_RELEASED:
      inputHandler.keyReleased(evt);
      break;
    }
  }

  // package-private members
  Segment lineSegment;

  // protected members
  protected static String CENTER = "center";
  protected static String RIGHT = "right";
  protected static String LEFT = "left";
  protected static String BOTTOM = "bottom";

  protected static JEditTextArea focusedComponent;
  protected static Timer caretTimer;

  protected TextAreaPainter painter;

  protected Gutter gutter;

  protected JPopupMenu popup;

  protected EventListenerList listenerList;
  protected MutableCaretEvent caretEvent;

  protected boolean caretBlinks;
  protected boolean caretVisible;
  protected boolean blink;

  protected boolean editable;

  protected int firstLine;
  protected int visibleLines;
  protected int electricScroll;

  protected int horizontalOffset;

  protected JScrollBar vertical;
  protected JScrollBar horizontal;
  protected boolean scrollBarsInitialized;

  protected InputHandler inputHandler;
  protected SyntaxDocument document;
  protected DocumentHandler documentHandler;
  protected boolean documentHandlerInstalled;

  protected int selectionStart;
  protected int selectionStartLine;
  protected int selectionEnd;
  protected int selectionEndLine;
  protected boolean biasLeft;

  protected int bracketPosition;
  protected int bracketLine;

  protected int magicCaret;
  protected boolean overwrite;
  protected boolean rectSelect;

  protected void fireCaretEvent()
  {
    Object[] listeners = listenerList.getListenerList();
    for(int i = listeners.length - 2; i >= 0; i--)
    {
      if(listeners[i] == CaretListener.class)
      {
        ((CaretListener)listeners[i+1]).caretUpdate(caretEvent);
      }
    }
  }

  protected void updateBracketHighlight(int newCaretPosition)
  {
    if(!painter.isBracketHighlightEnabled())
      return;

    if(bracketLine != -1)
      painter.invalidateLine(bracketLine);

    if(newCaretPosition == 0)
    {
      bracketPosition = bracketLine = -1;
      return;
    }

    try
    {
      int offset = TextUtilities.findMatchingBracket(
        document,newCaretPosition - 1);
      if(offset != -1)
      {
        bracketLine = getLineOfOffset(offset);
        bracketPosition = offset - getLineStartOffset(bracketLine);

        if(bracketLine != -1)
          painter.invalidateLine(bracketLine);
        return;
      }
    }
    catch(BadLocationException bl)
    {
      bl.printStackTrace();
    }

    bracketLine = bracketPosition = -1;
  }

  protected void documentChanged(DocumentEvent evt)
  {
    DocumentEvent.ElementChange ch = evt.getChange(
      document.getDefaultRootElement());
    int count;
    if(ch == null)
      count = 0;
    else
      count = ch.getChildrenAdded().length -
        ch.getChildrenRemoved().length;

    int line = getLineOfOffset(evt.getOffset());
    if(count == 0)
    {
      painter.invalidateLine(line);
    }
    // do magic stuff
    else if(line < firstLine)
    {
      setFirstLine(firstLine + count);
    }
    // end of magic stuff
    else
    {
      painter.invalidateLineRange(line,firstLine + visibleLines);
      gutter.repaint();
      updateScrollBars();
    }
  }

  // private members

  // for event handlers only
  private int clickCount;

  class ScrollLayout implements LayoutManager
  {
    public void addLayoutComponent(String name, Component comp)
    {
      if(name.equals(CENTER))
        center = comp;
      else if(name.equals(RIGHT))
        right = comp;
      else if(name.equals(LEFT))
        left = comp;
      else if(name.equals(BOTTOM))
        bottom = comp;
      else if(name.equals(LEFT_OF_SCROLLBAR))
        leftOfScrollBar = comp;
    }

    public void removeLayoutComponent(Component comp)
    {
      if(center == comp)
        center = null;
      else if(right == comp)
        right = null;
      else if(left == comp)
        left = null;
      else if(bottom == comp)
        bottom = null;
      else
        leftOfScrollBar = null;
    }

    public Dimension preferredLayoutSize(Container parent)
    {
      Dimension dim = new Dimension();
      Insets insets = getInsets();
      dim.width = insets.left + insets.right;
      dim.height = insets.top + insets.bottom;

      Dimension leftPref = left.getPreferredSize();
      dim.width += leftPref.width;
      Dimension centerPref = center.getPreferredSize();
      dim.width += centerPref.width;
      dim.height += centerPref.height;
      Dimension rightPref = right.getPreferredSize();
      dim.width += rightPref.width;
      Dimension bottomPref = bottom.getPreferredSize();
      dim.height += bottomPref.height;

      return dim;
    }

    public Dimension minimumLayoutSize(Container parent)
    {
      Dimension dim = new Dimension();
      Insets insets = getInsets();
      dim.width = insets.left + insets.right;
      dim.height = insets.top + insets.bottom;

      Dimension leftPref = left.getMinimumSize();
      dim.width += leftPref.width;
      Dimension centerPref = center.getMinimumSize();
      dim.width += centerPref.width;
      dim.height += centerPref.height;
      Dimension rightPref = right.getMinimumSize();
      dim.width += rightPref.width;
      Dimension bottomPref = bottom.getMinimumSize();
      dim.height += bottomPref.height;

      return dim;
    }

    public void layoutContainer(Container parent)
    {
      Dimension size = parent.getSize();
      Insets insets = parent.getInsets();
      int itop = insets.top;
      int ileft = insets.left;
      int ibottom = insets.bottom;
      int iright = insets.right;

      int rightWidth = right.getPreferredSize().width;
      int leftWidth = left.getPreferredSize().width;
      int bottomHeight = bottom.getPreferredSize().height;
      int centerWidth = size.width - leftWidth - rightWidth -
        ileft - iright;
      int centerHeight = size.height - bottomHeight - itop -
        ibottom;

      left.setBounds(
        ileft,
        itop,
        leftWidth,
        centerHeight);

      center.setBounds(
        ileft + leftWidth,
        itop,
        centerWidth,
        centerHeight);

      right.setBounds(
        ileft + leftWidth + centerWidth,
        itop,
        rightWidth,
        centerHeight);

      if(leftOfScrollBar != null)
      {
        Dimension dim = leftOfScrollBar.getPreferredSize();
        leftOfScrollBar.setBounds(ileft,
          itop + centerHeight,
          dim.width,
          bottomHeight);
        ileft += dim.width;
      }

      bottom.setBounds(
        ileft,
        itop + centerHeight,
        size.width - rightWidth - ileft - iright,
        bottomHeight);
    }

    Component center;
    Component left;
    Component right;
    Component bottom;
    Component leftOfScrollBar;
  }

  static class CaretBlinker implements ActionListener
  {
    public void actionPerformed(ActionEvent evt)
    {
      if(focusedComponent != null
        && focusedComponent.hasFocus())
        focusedComponent.blinkCaret();
    }
  }

  class MutableCaretEvent extends CaretEvent
  {
    MutableCaretEvent()
    {
      super(JEditTextArea.this);
    }

    public int getDot()
    {
      return getCaretPosition();
    }

    public int getMark()
    {
      return getMarkPosition();
    }
  }

  class AdjustHandler implements AdjustmentListener
  {
    public void adjustmentValueChanged(final AdjustmentEvent evt)
    {
      if(!scrollBarsInitialized)
        return;

      // If this is not done, mousePressed events accumilate
      // and the result is that scrolling doesn't stop after
      // the mouse is released
      SwingUtilities.invokeLater(new Runnable() {
        public void run()
        {
          if(evt.getAdjustable() == vertical)
            setFirstLine(vertical.getValue());
          else
            setHorizontalOffset(-horizontal.getValue());
        }
      });
    }
  }

  class ComponentHandler extends ComponentAdapter
  {
    public void componentResized(ComponentEvent evt)
    {
      recalculateVisibleLines();
      scrollBarsInitialized = true;
    }
  }

  class DocumentHandler implements DocumentListener
  {
    public void insertUpdate(DocumentEvent evt)
    {
      documentChanged(evt);

      int offset = evt.getOffset();
      int length = evt.getLength();

      int newStart;
      int newEnd;

      boolean change = false;

      if(selectionStart > offset || (selectionStart
        == selectionEnd && selectionStart == offset))
      {
        change = true;
        newStart = selectionStart + length;
      }
      else
        newStart = selectionStart;

      if(selectionEnd >= offset)
      {
        change = true;
        newEnd = selectionEnd + length;
      }
      else
        newEnd = selectionEnd;

      if(change)
        select(newStart,newEnd);
      else
        updateBracketHighlight(getCaretPosition());
    }

    public void removeUpdate(DocumentEvent evt)
    {
      documentChanged(evt);
      int offset = evt.getOffset();
      int length = evt.getLength();

      int newStart;
      int newEnd;

      boolean change = false;

      if(selectionStart > offset)
      {
        change = true;

        if(selectionStart > offset + length)
          newStart = selectionStart - length;
        else
          newStart = offset;
      }
      else
        newStart = selectionStart;

      if(selectionEnd > offset)
      {
        change = true;

        if(selectionEnd > offset + length)
          newEnd = selectionEnd - length;
        else
          newEnd = offset;
      }
      else
        newEnd = selectionEnd;

      if(change)
        select(newStart,newEnd);
      else
        updateBracketHighlight(getCaretPosition());
    }

    public void changedUpdate(DocumentEvent evt)
    {
    }
  }

  class DragHandler implements MouseMotionListener
  {
    public void mouseDragged(MouseEvent evt)
    {
      if(popup != null && popup.isVisible())
        return;

      setSelectionRectangular((evt.getModifiers()
        & InputEvent.CTRL_MASK) != 0);

      switch(clickCount)
      {
      case 1:
        doSingleDrag(evt);
        break;
      case 2:
        doDoubleDrag(evt);
        break;
      case 3:
        doTripleDrag(evt);
        break;
      }
    }

    public void mouseMoved(MouseEvent evt) {}

    private void doSingleDrag(MouseEvent evt)
    {
      select(getMarkPosition(),xyToOffset(
        evt.getX(),evt.getY()));
    }

    private void doDoubleDrag(MouseEvent evt)
    {
      int markLine = getMarkLine();
      int markLineStart = getLineStartOffset(markLine);
      int markLineLength = getLineLength(markLine);
      int mark = getMarkPosition() - markLineStart;

      int line = yToLine(evt.getY());
      int lineStart = getLineStartOffset(line);
      int lineLength = getLineLength(line);
      int offset = xToOffset(line,evt.getX());

      String lineText = getLineText(line);
      String markLineText = getLineText(markLine);
      String noWordSep = (String)document.getProperty("noWordSep");

      if(markLineStart + mark > lineStart + offset)
      {
        if(offset != 0 && offset != lineLength)
        {
          offset = TextUtilities.findWordStart(
            lineText,offset,noWordSep);
        }

        if(markLineLength != 0)
        {
          mark = TextUtilities.findWordEnd(
            markLineText,mark,noWordSep);
        }
      }
      else
      {
        if(offset != 0 && lineLength != 0)
        {
          offset = TextUtilities.findWordEnd(
            lineText,offset,noWordSep);
        }

        if(mark != 0 && mark != markLineLength)
        {
          mark = TextUtilities.findWordStart(
            markLineText,mark,noWordSep);
        }
      }
      select(markLineStart + mark,lineStart + offset);
    }

    private void doTripleDrag(MouseEvent evt)
    {
      int mark = getMarkLine();
      int mouse = yToLine(evt.getY());
      int offset = xToOffset(mouse,evt.getX());
      if(mark > mouse)
      {
        mark = getLineEndOffset(mark) - 1;
        if(offset == getLineLength(mouse))
          mouse = getLineEndOffset(mouse) - 1;
        else
          mouse = getLineStartOffset(mouse);
      }
      else
      {
        mark = getLineStartOffset(mark);
        if(offset == 0)
          mouse = getLineStartOffset(mouse);
        else
          mouse = getLineEndOffset(mouse) - 1;
      }
      select(mark,mouse);
    }
  }

  class FocusHandler implements FocusListener
  {
    public void focusGained(FocusEvent evt)
    {
      setCaretVisible(true);
      focusedComponent = JEditTextArea.this;
    }

    public void focusLost(FocusEvent evt)
    {
      setCaretVisible(false);
      focusedComponent = null;
    }
  }

  class MouseHandler extends MouseAdapter
  {
        private void doRightClick(MouseEvent evt)
        {
            int x = evt.getX();
            int y = evt.getY();

//            JextFrame view = ((JextTextArea) JEditTextArea.this).getJextParent();
                net.jesktop.jipe.Editor view=null;

            if (view != null)
            {
                Dimension viewSize = view.getSize();
                Point viewLocation = view.getLocationOnScreen();
                Insets viewInsets  = view.getInsets();

                Point tapLocation = painter.getLocationOnScreen();
                Dimension popupSize  = popup.getSize();

                if ((tapLocation.x + x + popupSize.width) >
                    (viewLocation.x + viewSize.width - viewInsets.right))
                  x -= popupSize.width;

                if ((tapLocation.y + y + popupSize.height) >
                    (viewLocation.y + viewSize.height - viewInsets.bottom))
                  y = (viewLocation.y + viewSize.height - viewInsets.bottom)
                      - (tapLocation.y + popupSize.height);
            }
            popup.show(painter,x,y);
        }

    public void mousePressed(MouseEvent evt)
    {
      requestFocus();

      // Focus events not fired sometimes?
      setCaretVisible(true);
      focusedComponent = JEditTextArea.this;

      if((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0
        && popup != null)
      {
        doRightClick(evt);
        return;
      }

      int line = yToLine(evt.getY());
      int offset = xToOffset(line,evt.getX());
      int dot = getLineStartOffset(line) + offset;

      clickCount = evt.getClickCount();
      switch(clickCount)
      {
      case 1:
        doSingleClick(evt,line,offset,dot);
        break;
      case 2:
        // It uses the bracket matching stuff, so
        // it can throw a BLE
        try
        {
          doDoubleClick(evt,line,offset,dot);
        }
        catch(BadLocationException bl)
        {
          bl.printStackTrace();
        }
        break;
      case 3:
        doTripleClick(evt,line,offset,dot);
        break;
      }
    }

    private void doSingleClick(MouseEvent evt, int line,
      int offset, int dot)
    {
      if((evt.getModifiers() & InputEvent.SHIFT_MASK) != 0)
      {
        rectSelect = (evt.getModifiers() & InputEvent.CTRL_MASK) != 0;
        select(getMarkPosition(),dot);
      }
      else
        setCaretPosition(dot);
    }

    private void doDoubleClick(MouseEvent evt, int line,
      int offset, int dot) throws BadLocationException
    {
      // Ignore empty lines
      if(getLineLength(line) == 0)
        return;

      try
      {
        int bracket = TextUtilities.findMatchingBracket(
          document,Math.max(0,dot - 1));
        if(bracket != -1)
        {
          int mark = getMarkPosition();
          // Hack
          if(bracket > mark)
          {
            bracket++;
            mark--;
          }
          select(mark,bracket);
          return;
        }
      }
      catch(BadLocationException bl)
      {
        bl.printStackTrace();
      }

      // Ok, it's not a bracket... select the word
      String lineText = getLineText(line);
      String noWordSep = (String)document.getProperty("noWordSep");
      if(offset == getLineLength(line))
        offset--;

      int wordStart = TextUtilities.findWordStart(lineText,offset,noWordSep);
      int wordEnd = TextUtilities.findWordEnd(lineText,offset+1,noWordSep);

      int lineStart = getLineStartOffset(line);
      select(lineStart + wordStart,lineStart + wordEnd);
    }

    private void doTripleClick(MouseEvent evt, int line,
      int offset, int dot)
    {
      select(getLineStartOffset(line),getLineEndOffset(line)-1);
    }
  }

  class CaretUndo extends AbstractUndoableEdit
  {
    private int start;
    private int end;
    private int newStart;
    private int newEnd;

    CaretUndo(int start, int end, int newStart, int newEnd)
    {
      this.start = start;
      this.end = end;

      this.newStart = newStart;
      this.newEnd = newEnd;
    }

    public boolean isSignificant()
    {
      return false;
    }

    public String getPresentationName()
    {
      return "caret move";
    }

    public void undo() throws CannotUndoException
    {
      super.undo();

      select(start,end);
    }

    public boolean addEdit(UndoableEdit edit)
    {
      if(edit instanceof CaretUndo)
      {
        CaretUndo cedit = (CaretUndo)edit;
//        start = cedit.start;
//        end = cedit.end;
//        newStart = cedit.start;
//        newEnd = cedit.end;
        cedit.die();

        return true;
      }
      else
        return false;
    }

    public String toString()
    {
      return getPresentationName() + "[start="
        + start + ",end=" + end + "]";
    }
  }

  static
  {
    caretTimer = new Timer(500,new CaretBlinker());
    caretTimer.setInitialDelay(500);
    caretTimer.start();
  }
}

/*
 * ChangeLog:
 * $Log: not supported by cvs2svn $
 * Revision 1.0  2000-07-15 11:24:34+01  steve_lawson
 * Initial revision
 *
 * Revision 1.0  2000-06-29 21:50:40+01  steve_lawson
 * Initial revision
 *
 * Revision 1.47  2000/03/20 03:42:55  sp
 * Smoother syntax package, opening an already open file will ask if it should be
 * reloaded, maybe some other changes
 *
 * Revision 1.46  2000/03/14 06:22:25  sp
 * Lots of new stuff
 *
 * Revision 1.45  2000/02/27 00:39:51  sp
 * Misc changes
 *
 * Revision 1.44  2000/02/15 07:44:30  sp
 * bug fixes, doc updates, etc
 *
 * Revision 1.43  2000/02/04 05:50:27  sp
 * More gutter updates from mike
 *
 * Revision 1.42  2000/02/01 06:12:33  sp
 * Gutter added (still not fully functional)
 *
 * Revision 1.41  2000/01/29 03:27:20  sp
 * Split window functionality added
 *
 * Revision 1.40  2000/01/29 01:56:51  sp
 * Buffer tabs updates, some other stuff
 *
 * Revision 1.39  2000/01/28 00:20:58  sp
 * Lots of stuff
 *
 * Revision 1.38  2000/01/21 00:35:29  sp
 * Various updates
 *
 * Revision 1.37  2000/01/14 04:23:50  sp
 * 2.3pre2 stuff
 *
 * Revision 1.36  1999/12/13 03:40:30  sp
 * Bug fixes, syntax is now mostly GPL'd
 *
 * Revision 1.35  1999/12/06 00:06:15  sp
 * Bug fixes
 *
 * Revision 1.34  1999/12/03 23:48:11  sp
 * C+END/C+HOME, LOADING BufferUpdate message, misc stuff
 *
 */
