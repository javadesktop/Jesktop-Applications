package net.jesktop.demos.jdksupport;



import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

import java.lang.reflect.*;

import org.jesktop.frimble.*;


/**
 * Class StylePadDemo
 *
 *
 * @author Paul Hammant <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
 * @version $Revision: 1.1.1.1 $
 */
public class StylePadDemo extends JPanel implements FrimbleAware {

    private Frimble frimble;

    /**
     * Constructor StylePadDemo
     *
     *
     */
    public StylePadDemo() {}

    private void addErrPanel(String reason) {
        this.add(new JLabel(" SylePad jar not found in classpath: " + reason),
                 BorderLayout.CENTER);
    }

    /**
     * Method getMinimumSize
     *
     *
     * @return
     *
     */
    public Dimension getMinimumSize() {
        return new Dimension(600, 480);
    }

    // Javadocs will automatically import from interface.

    /**
     * Method setFrimble
     *
     *
     * @param frimble
     *
     */
    public void setFrimble(Frimble frimble) {

        this.frimble = frimble;

        this.setLayout(new BorderLayout());

        try {
            Class cl = Class.forName("StylePad");
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
}
