
/*****************************************************************************
 *                                                                           *
 *  This file is part of the BeanShell Java Scripting distribution.          *
 *  Documentation and updates may be found at http://www.beanshell.org/      *
 *                                                                           *
 *  BeanShell is distributed under the terms of the LGPL:                    *
 *  GNU Library Public License http://www.gnu.org/copyleft/lgpl.html         *
 *                                                                           *
 *  Patrick Niemeyer (pat@pat.net)                                           *
 *  Author of Exploring Java, O'Reilly & Associates                          *
 *  http://www.pat.net/~pat/                                                 *
 *                                                                           *
 *****************************************************************************/

/*
 * Author of this adaption from Beanshell source (s):
 * Paul Hammant
 */
package net.jesktop.beanshell;



import java.awt.*;

import java.beans.*;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import bsh.*;
import bsh.util.*;

import java.io.*;

import org.jesktop.frimble.*;


/**
 * Class WorkspaceEditor
 *
 *
 * @author Paul Hammant <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
 * @version $Revision: 1.1.1.1 $
 */
public class WorkspaceEditor implements ActionListener {

    Interpreter parentInterpreter;
    Frimble frimble;
    BeanShell shell;
    JConsole console;

    /**
     * Constructor WorkspaceEditor
     *
     *
     * @param shell
     * @param console
     * @param frimble
     * @param parentInterpreter
     * @param name
     *
     */
    public WorkspaceEditor(BeanShell shell, JConsole console, Frimble frimble,
                           bsh.Interpreter parentInterpreter, String name) {

        this.console = console;
        this.shell = shell;
        this.parentInterpreter = parentInterpreter;
        this.frimble = frimble;

        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem mi = new JMenuItem("New");

        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Open");

        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Save");

        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Close");

        mi.addActionListener(this);
        menu.add(mi);
        menubar.add(menu);

        menu = new JMenu("Evaluate");
        mi = new JMenuItem("Eval in Workspace");

        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Run in new Workspace");

        mi.addActionListener(this);
        menu.add(mi);
        menubar.add(menu);

        menu = makeFontMenu();

        //TODO
        menubar.add(menu);
        frimble.setJMenuBar(menubar);
    }

    protected void closing() {

        this.console = null;
        this.shell = null;
        this.parentInterpreter = null;
        this.frimble = null;
    }

    /**
     * Method open
     *
     *
     */
    public void open() {

        JFileChooser chooser = new JFileChooser();

        try {
            chooser.setCurrentDirectory(parentInterpreter
                .pathToFile((String) parentInterpreter.get("cwd")));

            int returnVal = frimble.showOpenDialog(chooser);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                FileReader reader = new FileReader(file);
                char[] ca = new char[(int) file.length()];
                reader.read(ca);
            }
	    } catch (IOException ioe) {
			ioe.printStackTrace();
        } catch (EvalError ee) {
            ee.printStackTrace();
        }
    }

    /**
     * Method save
     *
     *
     */
    public void save() {

        JFileChooser chooser = new JFileChooser();

        try {
            chooser.setCurrentDirectory(parentInterpreter
                .pathToFile((String) parentInterpreter.get("cwd")));

            int returnVal = frimble.showSaveDialog(chooser);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                FileWriter writer = new FileWriter(file);

                //writer.write( textarea.getText().toCharArray() );
                writer.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();            
        } catch (EvalError ee) {
            ee.printStackTrace();
        }
    }

    /**
     * Method actionPerformed
     *
     *
     * @param e
     *
     */
    public void actionPerformed(ActionEvent e) {

        String com = e.getActionCommand();

        if (com.equals("Close")) {
            try {
                frimble.setClosed(true);
            } catch (PropertyVetoException pve) {
                pve.printStackTrace();
            }
        } else if (com.equals("New")) {

            //textarea.setText("");
        } else if (com.equals("Open")) {
            open();
        } else if (com.equals("Save")) {
            save();
        } else if (com.equals("Eval in Workspace")) {

            // eval in parent global namespace
            //  try {
            //parentInterpreter.eval( textarea.getText() );
            //  } catch (EvalError ee) {
            //   ee.printStackTrace();
            // }
        }

        if (com.equals("Normal")) {
            setFont(console, 12);
        } else if (com.equals("Big")) {
            setFont(console, 16);
        } else if (com.equals("Bigger")) {
            setFont(console, 20);
        }
    }

    private JMenu makeFontMenu() {

        JMenu menu = new JMenu("Font");
        JMenuItem mi = new JMenuItem("Normal");

        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Big");

        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Bigger");

        mi.addActionListener(this);
        menu.add(mi);

        return menu;
    }

    /**
     * Method setFont
     *
     *
     * @param comp
     * @param ptsize
     *
     * @return
     *
     */
    Font setFont(Component comp, int ptsize) {

        Font font = comp.getFont();
        String f = font.getFamily();
        int s = font.getStyle();

        font = new Font(f, s, ptsize);

        comp.setFont(font);
        comp.validate();

        return font;
    }
}
