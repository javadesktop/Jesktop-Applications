
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



import bsh.EvalError;
import bsh.Interpreter;
import bsh.util.JConsole;
import org.jesktop.api.DesktopKernel;
import org.jesktop.frimble.Frimble;
import org.jesktop.frimble.FrimbleAware;

import javax.swing.*;
import java.awt.*;


/**
 * Class BeanShell
 *
 *
 * @author Paul Hammant <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
 * @version $Revision: 1.2 $
 */
public class BeanShell extends JPanel implements FrimbleAware {

    Frimble frimble;
    JConsole jc;
    Interpreter interpreter;
    WorkspaceEditor wse;
    Thread thread;

    /**
     * Constructor BeanShell
     *
     *
     */
    public BeanShell(DesktopKernel desktopKernel) throws EvalError {
        interpreter.set("jesktop-desktopKernel", desktopKernel);

        this.setPreferredSize(new Dimension(600, 480));

        jc = new JConsole();

        this.setLayout(new BorderLayout());
        this.add(jc, BorderLayout.CENTER);

        interpreter = new Interpreter(jc);
    }

    /**
     * Method setFrimble
     *
     *
     * @param frimble
     *
     */
    public void setFrimble(Frimble frimble) {

        this.frimble = frimble;
        wse = new WorkspaceEditor(this, jc, frimble, interpreter, "BeanShell-WorkspaceEditor");
        thread = new Thread(interpreter);

        thread.start();
    }

    protected void closing() {

        thread.interrupt();

        thread = null;
        jc = null;
        interpreter = null;

        wse.closing();

        wse = null;
    }
}
