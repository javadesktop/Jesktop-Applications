package net.jesktop.demos.jdksupport;



import javax.swing.*;

import java.awt.*;

import java.util.*;

import java.lang.reflect.*;

import java.io.*;


//import org.pje.ApplicationInfo;

/**
 * Class JavaSoundDemo
 *
 *
 * @author Paul Hammant <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
 * @version $Revision: 1.1.1.1 $
 */
public class JavaSoundDemo extends JPanel {

    /**
     * Constructor JavaSoundDemo
     *
     *
     */
    public JavaSoundDemo() {

        this.setLayout(new BorderLayout());

        try {
            Class cl = Class.forName("JavaSound");
            Constructor[] con = cl.getConstructors();
            JPanel pnl = (JPanel) con[0].newInstance(new Object[]{ "null" });

            //      Object[] { appInfo.getProperty( "fullJavaMediaPath", null ) });
            this.add(pnl, BorderLayout.CENTER);
        } catch (ClassNotFoundException cnfe) {
            addErrPanel("ClassNotFoundException");
        } catch (InstantiationException iae) {
            addErrPanel("InstantiationException");
        } catch (IllegalAccessException iae) {
            addErrPanel("IllegalAccessException");
        } catch (InvocationTargetException ite) {
            addErrPanel("InvocationTargetException");
        }

        //this.add(new SwingSet2(null), BorderLayout.CENTER);
    }

    private void addErrPanel(String reason) {
        add(new JLabel(" JavaSound jar not found in classpath: " + reason), BorderLayout.CENTER);
    }
}
