/**
 * JipeConsole.java - Console shell to allow users to run external commands
 * Copyright (c) 1996-2000 Steve Lawson.  E-Mail
 * steve@e-i-s.co.uk Latest version can be found at http://e-i-s.co.uk/jipe
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

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class JipeConsole
{

    Jipe jipe;
    JTextArea jipeConsole;
    private Process process;
    private Thread waitThread;
    String jipeConsoleCommand="";

    /** SunOS prompt: csdlyon% */
    public static final int SUNOS_PROMPT = 0;
    /** Linux prompt: guy@csdlyon$ */
    public static final int LINUX_PROMPT = 1;
    /** DOS prompt: /export/home/guy > */
    public static final int DOS_PROMPT = 2;

    private int promptType;

    public JipeConsole(Jipe parent){
        jipe = parent;
        //        GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        jipeConsole = new JTextArea();
        if(System.getProperty("os.name").startsWith("Linux"))promptType=LINUX_PROMPT;
        else if(System.getProperty("os.name").startsWith("Windows"))promptType=DOS_PROMPT;
        else promptType=SUNOS_PROMPT;
        jipeConsole.setFont(new Font(jipe.props.CONSOLEFONT, 0, jipe.props.CONSOLEFONTSIZE));
        jipeConsole.setBackground(jipe.props.CONSOLEBCOLOUR);
        jipeConsole.setForeground(jipe.props.CONSOLEFCOLOUR);
        jipeConsole.setCaretColor(jipe.props.CONSOLEFCOLOUR);
        displayPrompt();
        jipeConsole.addKeyListener(new KeyListener(){
                                       public void keyPressed(KeyEvent e){
                                           jipeConsole.setCaretPosition(jipeConsole.getText().length());
                                           int key=e.getKeyCode();
                                           int modifiers = e.getModifiers();
                                           char c = e.getKeyChar();
                                           switch(key){

                                           case KeyEvent.VK_BACK_SPACE:     //int number 8
                                           case KeyEvent.VK_DELETE:            //int number 127
                                               if(jipeConsoleCommand.trim().length()<1) {
                                                   jipeConsoleCommand="";
                                                   e.consume();
                                               }
                                               else if(jipeConsoleCommand!="")jipeConsoleCommand=jipeConsoleCommand.substring(0,jipeConsoleCommand.length()-1);
                                               break;
                                           case KeyEvent.VK_ENTER:
                                               String errorCommand=jipeConsoleCommand;
                                               try{
                                                   if(jipeConsoleCommand.trim().length()>1){
                                                       if(jipeConsoleCommand.endsWith("cls")) {
                                                           jipeConsole.setText("");
                                                           displayPrompt();
                                                       }
                                                       else RunCommand(jipeConsoleCommand);
                                                   }
                                               }
                                               catch(IOException ioe){
                                                   jipeConsole.append("Unknown command: "+errorCommand);
                                               }
                                               e.consume();
                                               break;
                                           case KeyEvent.VK_UP:
                                           case KeyEvent.VK_DOWN:
                                           case KeyEvent.VK_LEFT:
                                           case KeyEvent.VK_RIGHT:
                                           case KeyEvent.VK_TAB:
                                               e.consume();
                                               break;
                                           }
                                           if (c >=0x20 && c<=0x7e)jipeConsoleCommand+=e.getKeyChar();
                                       }
                                   public void keyReleased(KeyEvent e){}
                                       public void keyTyped(KeyEvent e){}
                                   });
    }
    public void RunCommand(String cmdline)
    throws IOException
    {
        jipeConsole.append(System.getProperty("line.separator"));
        String OSNAME=System.getProperty("os.name");
        if(OSNAME.startsWith("Windows NT") || OSNAME.startsWith("Windows 2000"))cmdline="cmd /c "+cmdline;
        else if(OSNAME.startsWith("Windows"))cmdline="command.com /c "+cmdline;
        process = Runtime.getRuntime().exec(cmdline);
        process.getOutputStream().close();

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
        waitThread.start();
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
                jipeConsole.append(ex.toString());
            }
        }
        displayPrompt();
    }

    public static String getUserDirectory()
    {
        return System.getProperty("user.dir");
    }

    public void displayPrompt()
    {
        String prompt = new String("> ");
        switch (promptType)
        {
        case SUNOS_PROMPT:
            try
            {
                prompt = InetAddress.getLocalHost().getHostName() + "% ";
            } catch (UnknownHostException uhe) { }
            break;
        case LINUX_PROMPT:
            try
            {
                prompt = System.getProperty("user.name") + "@" +
                         InetAddress.getLocalHost().getHostName() + "$ ";
            } catch (UnknownHostException uhe) { }
            break;
    case DOS_PROMPT: default:
            prompt = getUserDirectory() + ">";
            break;
        }
        jipeConsole.append(prompt);
        jipeConsole.setCaretPosition(jipeConsole.getText().length());
        jipeConsoleCommand="";
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
                    if (b > 0)
                        jipeConsole.append(new String(buf, 0, b));
                    else if (b < 0)
                        break;
                }
                catch (IOException ex)
                {
                    jipeConsole.append(ex.toString());
                    break;
                }
            }
        }
    }
}

