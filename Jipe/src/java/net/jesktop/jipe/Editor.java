/**
 * Editor.java - Editor class for use with the Jipe Project Copyright (c)
 * 1996-1999 Steve Lawson.  E-Mail steve@e-i-s.co.uk Latest version can be
 * found at http://e-i-s.co.uk/jipe
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package net.jesktop.jipe;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.undo.*;
import java.net.URL;
import java.util.*;

import org.gjt.sp.jedit.syntax.*;
import org.gjt.sp.jedit.textarea.*;


public class Editor extends JTextArea
{
    private JSFormatter jsbeautifier;
    private CompoundEdit compoundEdit;
    protected static int num_windows = 0;
    protected static int window_num = 0;
    JEditTextArea textarea;
    protected UndoableEditListener undoHandler = new UndoHandler();

    /**
    * UndoManager that we add edits to.
    */
    protected UndoManager undo = new UndoManager();

    File file;
    String filename;
    String title;
    static boolean dontopen = false;
    String path;
    Jipe jipe;
    public Editor(Jipe parent, String str, File importfile)
    {
        super(str);
        jipe = parent;

        if(importfile!=null)file = importfile;
        else file=null;
        textarea=new JEditTextArea();
        textarea.setBackground(Color.white);
        textarea.getPainter().setFont(new Font(jipe.props.FONTSTYLE, 0, jipe.props.FONTSIZE));
        setTabSize(getTabSize());
        textarea.setEditable(true);
        if (str.startsWith("Html"))
            setDocumentStyle(".html");
        else if (str.startsWith("Java"))
            setDocumentStyle(".java");
        else if (str.startsWith("Untitled"))
            setDocumentStyle("");
        jsbeautifier=new JSFormatter();
        textarea.setRightClickPopup(jipe.popup);
        textarea.addCaretListener(new CaretHandler());
        textarea.getDocument().addUndoableEditListener(undoHandler);
        if(jipe.props.AUTOINDENT)textarea.setAutoIndentOn();
          else textarea.setAutoIndentOff();

    }

    public String getTitle()
    {
        return (title);
    }
    public String setTitle(String setTitle)
    {
        title = setTitle;
        return title;
    }

    void SetActiveWindow()
    {
        jipe.activeChild = this;
        jipe.statusBar.msgline.setText("Current Window : " + this.getTitle());
        if (jipe.activeChild.getTitle().endsWith(".java"))
            jipe.speedbar.parse();
        Find.editor = textarea;
    }
    public void setDirtyTitle(){
        jipe.tabbedPane2.setForegroundAt(jipe.tabbedPane2.getSelectedIndex(), Color.red.darker());
    }
    void load(String filename)
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            FileReader fis = new FileReader(file);
            BufferedReader br = new BufferedReader(fis);
            String str = br.readLine();
            while (str != null)
            {
                sb.append(str);
                sb.append("\n");
                str = br.readLine();
            }
            fis.close();
            setDocumentStyle(file.getName());
            setTitle(file.getName());
            textarea.setText(sb.toString());
        }
        catch (Exception err)
        {
        }
    }

    void close()
    {
        int selectedValue = 1;
        if (textarea.isDirty() == true)
            selectedValue = SaveConfirm(
                                this.getTitle(), "File not Saved, Save Now?");
        if (selectedValue == 0)
        {
            if (file != null)
                SaveFile(false);
            else
                SaveFile(true);
            selectedValue = 1;
        }
        if (selectedValue == 1)
        {
            String str;
            for (int i = 0; i < jipe.window.getItemCount(); i++)
            {
                if (jipe.window.getItem(i) == null)
                {
                    continue;
                }
                str = jipe.window.getItem(i).getText();
                if (str == this.getTitle())
                    jipe.window.remove(i);
            }
            jipe.children.removeElement((Object)this);
            jipe.tabbedPane2.removeTabAt(jipe.tabbedPane2.indexOfTab(
                                             this.getTitle()));
            if (jipe.children.isEmpty())
            {
                jipe.statusBar.msgline.setText("No current Window Set");
                jipe.activeChild = null;
                jipe.speedbar.removeList();
            }
            else
                jipe.GetWindow(new File(jipe.tabbedPane2.getTitleAt(
                                            jipe.tabbedPane2.getSelectedIndex())));
        }
    }
    public void SaveFile(boolean saveas)
    {
        boolean saveAs=saveas;
        if(file==null)saveAs=true;
        JFileChooser fileChooser = new JFileChooser(jipe.CURRENTDIR);
        if (saveAs)
        {
            fileChooser.addChoosableFileFilter(new TextFilter());
            fileChooser.addChoosableFileFilter(new HtmlFilter());
            fileChooser.addChoosableFileFilter(new JspFilter());
            fileChooser.setFileFilter(new JavaFilter());

            int selected = fileChooser.showSaveDialog(this);
            if (selected == JFileChooser.APPROVE_OPTION){
                file = fileChooser.getSelectedFile();
                fileChooser.setCurrentDirectory(file);
                jipe.CURRENTDIR=fileChooser.getCurrentDirectory();
            }
            else
                return;
        }
        try
        {
            jipe.statusBar.msgline.setText("Saving Document " + file.getName());
            FileWriter fos = new FileWriter(file.getPath());
            fos.write(this.textarea.getText());
            fos.close();
            jipe.statusBar.msgline.setText( file.getName()+" Saved.");
            textarea.clean();
            jipe.tabbedPane2.setForegroundAt(jipe.tabbedPane2.getSelectedIndex(), Color.black);

        }
        catch (Exception err)
        {
            jipe.statusBar.msgline.setText("Error : " + err);
        }
        if (saveAs){

            for (int i = 0; i < jipe.window.getItemCount(); i++)
            {

                if (jipe.window.getItem(i)==null)
                {
                    continue;
                }
                if (this.getTitle().equals(jipe.window.getItem(i).getText()))
                    jipe.window.remove(i);
            }
            jipe.children.removeElement((Object)this);
            jipe.tabbedPane2.setTitleAt(
                jipe.tabbedPane2.getSelectedIndex(), file.getName());
            setTitle(file.getName());
            jipe.addChild(jipe, this);
        }
    }
    int SaveConfirm(String confirmTitle, String confirmText)
    {
        int value;
        value = JOptionPane.showConfirmDialog(
                    this, confirmText, confirmTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return value;
    }

    public void Goto()
    {
        String nameExtension = JOptionPane.showInputDialog("Goto Line:");

        if(nameExtension==null)System.out.println("");
        if ((nameExtension!=null)&&(nameExtension.trim().length() >0))
        {
            try
            {
                int lineno = Integer.valueOf(nameExtension).intValue() - 1;
                System.out.println(""+lineno );
                if (lineno <= textarea.getLineCount() && lineno>0)
                    gotoLine(lineno);
            }
            catch (NumberFormatException e)
            {
            }
        }
    }
    public void gotoLine(int line)
    {

        Element elemen3 = textarea.getDocument().getDefaultRootElement();
        Element elemen4 = elemen3.getElement(line);
        textarea.select(
            ((int)elemen4.getStartOffset()), elemen4.getEndOffset());
    }
    public int getTabSize()
    {
        int tabSize = jipe.props.TABSIZE;
        return tabSize;
    }

    /**
     * See getTabSize().
     *
     * @param size The new tab size (in amount of spaces)
     */
    public void setTabSize(int size)
    {
        try
        {
            ((PlainDocument)textarea.getDocument()).putProperty(((PlainDocument)textarea.getDocument()).tabSizeAttribute, new Integer
                    (size));
            textarea.setTabSize(size);
        }
        catch (NumberFormatException nfe)
        {
            ((PlainDocument)textarea.getDocument()).putProperty(((PlainDocument)textarea.getDocument()).tabSizeAttribute, new Integer
                    (4));
            textarea.setTabSize(4);
        }
    }

    public void indent()
    {
        try{
            JSFormatter format = new JSFormatter();
            format.setBracketBreak(true);
            format.beautifier.setSpaceIndentation(getTabSize());
            format.init();
            Document doc = textarea.getDocument();
            Element map = doc.getDefaultRootElement();

            String line;
            int i = 0, start, end;
            StringBuffer buf = new StringBuffer(doc.getLength());
            try
            {
                while (true)
                {
                    while (!format.hasMoreFormattedLines())
                    {
                        if (i > map.getElementCount()) throw new NullPointerException();
                        Element lineElement = map.getElement(i);
                        start = lineElement.getStartOffset();
                        end = lineElement.getEndOffset() - 1;
                        end -= start;
                        line = textarea.getText(start, end);
                        if (line == null) throw new NullPointerException();
                        format.formatLine(line);
                        i++;
                    }
                    while (format.hasMoreFormattedLines())
                        buf.append(format.nextFormattedLine() + '\n');
                }
            } catch (NullPointerException npe) { }

            doc.remove(0, doc.getLength());
            doc.insertString(0, buf.toString(), null);

            format.summerize();
            while (format.hasMoreFormattedLines())
                doc.insertString(doc.getLength(), format.nextFormattedLine() + '\n', null);
        } catch (BadLocationException ble) { }
    }
    public void setDocumentStyle(String str)
    {
        if (str.toLowerCase().endsWith(".java") || str.toLowerCase().endsWith(
                    ".jav")) textarea.setTokenMarker(new JavaTokenMarker());
        else if (str.toLowerCase().endsWith(".htm") || str.toLowerCase().endsWith
                 (".html"))
            textarea.setTokenMarker(new HTMLTokenMarker());
        else if (str.toLowerCase().endsWith(".jsp"))
            textarea.setTokenMarker(new JSPTokenMarker());
        else if (str.toLowerCase().endsWith(".c") || str.toLowerCase().endsWith
                 (".cpp"))
            textarea.setTokenMarker(new CTokenMarker());
        else
            textarea.setTokenMarker(new TextTokenMarker());
    }
    class UndoHandler implements UndoableEditListener
    {

        /**
         * Messaged when the Document has created an edit, the edit is added
         * to <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(UndoableEditEvent e)
        {
            undo.addEdit(e.getEdit());

            // undoAction.update();
            // redoAction.update();

        }
    }
    void redoAction()
    {
        try
        {
            undo.redo();
        }
        catch (CannotRedoException ex)
        {
            System.out.println("Unable to redo: " + ex);
            ex.printStackTrace();
        }
    }
    void undoAction()
    {
        try
        {
            undo.undo();
        }
        catch (CannotUndoException ex)
        {
            System.out.println("Unable to undo: " + ex);
            ex.printStackTrace();
        }
    }
    class CaretHandler implements CaretListener
    {
        public void caretUpdate(CaretEvent evt)
        {
            jipe.statusBar.updateStatus(textarea);
            if(textarea.isDirty() && jipe.tabbedPane2.getForegroundAt(jipe.tabbedPane2.getSelectedIndex())!=Color.red.darker())jipe.tabbedPane2.setForegroundAt(jipe.tabbedPane2.getSelectedIndex(), Color.red.darker());

        //    if(jipe.props.AUTOINDENT)textarea.setAutoIndentOn();
       //       else textarea.setAutoIndentOff();
        }
    }
}