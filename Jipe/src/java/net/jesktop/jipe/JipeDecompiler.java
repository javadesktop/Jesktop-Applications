/**
 * JipeDecompiler.java - JipeDecompiler Class for Jipe project.
 * This routine will ask for a filename to open, then use an external decompiler
 * to display its source code within the jipe window.
 * Copyright (c) 1996-2000 Steve Lawson.  E-Mail
 * steve@e-i-s.co.uk Latest version found at http://e-i-s.co.uk/jipe
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
 *  along with this program; if not, write to the free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.jesktop.jipe;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

//import org.gjt.sp.jedit.syntax.*;
import org.gjt.sp.jedit.textarea.*;

import org.jesktop.frimble.*;

public class JipeDecompiler extends JDialog

{
    JEditTextArea editor;
    Jipe jipe;
    Process process;

    protected static JipeDecompiler makeJipeDecompiler(Jipe jipe, Frimble frimble, JEditTextArea text) {
    Component contained = frimble.getFrimbleContained();
    if (contained instanceof JFrame) {
      return new JipeDecompiler(jipe, (JFrame) contained, text);
    } else {
      return new JipeDecompiler(jipe, null, text);
    }
  }

    public JipeDecompiler(Jipe parent, JFrame frame, JEditTextArea text){
        super(frame, "Decompiler", true);
        jipe=parent;
        editor=text;
        String classfilename=BrowseName();

        String separator = System.getProperty("file.separator");
        try
        {
            process = Runtime.getRuntime().exec(parent.props.DECOMPILER + " " +classfilename);
            process.getOutputStream().close();
            ReaderThread r1 = new ReaderThread(process.getInputStream());
            r1.start();

            Thread waitThread = new Thread(new Runnable() {

                                               public void run()
                                               {
                                                   _wait();
                                               }
                                           });
            waitThread.start();

        }
        catch (Exception err)
        {
            jipe.jipeconsole.jipeConsole.append("Error could not open browser " + err + System.getProperty("line.separator"));
        }

    }

    String BrowseName()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a class file ");
        fileChooser.setFileFilter(new ClassFilter());
        int selected = fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file != null && selected == JFileChooser.APPROVE_OPTION)
            return (file.getPath());
        else
            return null;
    }
    private void _wait()
    {
        for (; ; )
        {
            try
            {
                int x = process.waitFor();
                break;
            }
            catch (InterruptedException ex)
            {
                jipe.statusBar.msgline.setText("Error " + ex.toString());
            }
        }
    }
    private class ReaderThread extends Thread
    {
        private InputStream stream;
        private byte[] buf = new byte[80];
        ReaderThread(InputStream stream)
        {
            this.stream = stream;
            setDaemon(true);
        }
        public void run()
        {
            for (; ; )
            {
                try
                {
                    int b = stream.read(buf);
                    if (b > 0){
                        //                        jipe.errorModel.addElement(new String(buf, 0, b));
                        editor.setSelectedText(new String(buf, 0, b));
                    }
                    else if (b < 0)
                        break;
                }
                catch (IOException ex)
                {
                    jipe.statusBar.msgline.setText("Error " + ex.toString());
                    break;
                }
            }
        }
    }
}
