package net.jesktop.demos.jdksupport;



import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

import java.lang.reflect.*;


/**
 * Class SwingSet2Demo
 *
 *
 * @author Paul Hammant <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
 * @version $Revision: 1.1.1.1 $
 */
public class SwingSet2Demo extends JPanel {

    /**
     * Constructor SwingSet2Demo
     *
     *
     */
    public SwingSet2Demo() {

        this.setLayout(new BorderLayout());

        try {
            Class cl = Class.forName("SwingSet2");
            Constructor[] con = cl.getConstructors();
            JPanel pnl = (JPanel) con[0].newInstance(new Object[]{ null });

            this.add(pnl, BorderLayout.CENTER);
        } catch (ClassNotFoundException cnfe) {
            addErrPanel("ClassNotFoundException");
        } catch (InstantiationException iae) {
            addErrPanel("InstantiationException");
        } catch (IllegalAccessException iae) {
            addErrPanel("InstantiationException");
        } catch (InvocationTargetException ite) {
            addErrPanel("InstantiationException");
        }
    }

    private void addErrPanel(String reason) {
        this.add(new JLabel(" SwingSet2 jar not found in classpath: " + reason),
                 BorderLayout.CENTER);
    }
}
