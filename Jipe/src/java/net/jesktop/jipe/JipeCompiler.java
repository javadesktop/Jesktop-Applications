/**
 * Compiler.java - Compile for use with the Jipe Project.  Copyright (c)
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

/* changes by Zsolt Rizsanyi
*/
package net.jesktop.jipe;

import java.io.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

/** Class to handle all of the compiling methods for Jipe.
 *
 */
public class JipeCompiler
{
    /** Holds the name of files  */
    String name;
    /** Holds the full path to compiler  */
    String compilerName;
    /** The compilation process   */
    Process process;
    /** holds the parent were the progress messages, and errors are sent back  */
    Jipe jipe;
    /** whether the last compile was successful  */
    boolean error=false;

    /** Construct the compiler class */
    JipeCompiler(Jipe parent)
    {
        jipe = parent;
        jipe.compileErrorPanel.addListSelectionListener(new ListSelectionListener() {
            /**                          */
            public void valueChanged(ListSelectionEvent e)
            {
                String str = jipe.compileErrorPanel.getSelectedValue().toString();
                str = str.trim();
                int start = 0;
                int end = 0;
                if (str.indexOf(jipe.activeChild.getTitle())>=0 || jipe.props.COMPILER.indexOf("jikes")>=0){
                    if (jipe.props.COMPILER.endsWith("jikes.exe") || jipe.props.COMPILER.endsWith
                            ("jikes"))
                        start = -1;
                    else
                        for (int i = 4; i < str.length(); i++)
                            if (str.charAt(i) == ':')
                            {
                                start = i;
                                break;
                            }
                    for (int i = start + 1; i < str.length(); i++)
                        if (str.charAt(i) == ':' || str.charAt(i) == '.')
                        {
                            end = i;
                            break;
                        }
                    String tmp = str.substring(start + 1, end);
                    try
                    {
                        int line = Integer.parseInt(tmp);
                        jipe.activeChild.gotoLine(line - 1);
                    }
                    catch (NumberFormatException nfe)
                    {
                    }
                }
            }
        });

    }

    /** Compiles the file (or files) specified with {@param path}, with the
     *  compiler {@param compilername}
     */

    public void compile(String compilername, String path){
        jipe.statusBar.msgline.setText("");
        System.err.println("Lets compile with:"+compilername+", and file:"+path);
        InputStream is;
        name = path;
        compilerName = compilername;
        try{
            String command = compilerName + " " + name;
            jipe.compilerErrors.addElement(command);
            process = Runtime.getRuntime().exec(command);
            process.getOutputStream().close();
            jipe.statusBar.msgline.setText("Compiling " + name +" Please Wait !!!");
            error = false;
            ReaderThread r1 = new ReaderThread(process.getInputStream());
            r1.start();
            ReaderThread r2 = new ReaderThread(process.getErrorStream());
            r2.start();


            Thread waitThread = new Thread(new Runnable() {
                public void run()
                {
                    _wait();
                }
            });
            waitThread.setDaemon(true);//it is important, if the compiler locks down
                //a loop and doesn't want to exit

            waitThread.start();

        }catch (IOException ex){
            jipe.statusBar.msgline.setText("Error " + ex);
        }
    }

    /** This checks the result, when the the process ends  */
    private void _wait()
    {
        try
        {
            int x = process.waitFor();
            if (x==0){
                jipe.compilerErrors.addElement("Compilation successful");
                jipe.statusBar.msgline.setText("Compiled OK.");
            }else{
                jipe.compilerErrors.addElement("There were errors in the compilation");
                jipe.statusBar.msgline.setText("Compiling Errors.");
                //the scrollpane is in the tabbedpane so that should be selected
                jipe.tabbedPane3.setSelectedComponent(jipe.compileErrorPanel.getParent().getParent());
            }
        }
        catch (InterruptedException ex)
        {
            jipe.compilerErrors.addElement("Compilation iterrupted");
            jipe.statusBar.msgline.setText("Error " + ex.toString());
        }
    }

    private class ReaderThread extends Thread
    {
        /**          */
        private InputStream stream;

        /**          */
        ReaderThread(InputStream stream)
        {
            this.stream = stream;
            setDaemon(true);
        }
        /**          */
        public void run()
        {
            try
            {
                BufferedReader ds = new BufferedReader(new InputStreamReader(stream));
                String  str;
                while ((str = ds.readLine())!= null){
                    //jipe.tabbedPane3.setSelectedIndex(1);
                    jipe.compilerErrors.addElement(str);
                }
                ds.close();
            }
            catch (IOException ex)
            {
                jipe.statusBar.msgline.setText("Error " + ex.toString());
                return;
            }
        }
    }
}
