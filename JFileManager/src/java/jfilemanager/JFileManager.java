package jfilemanager;

import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Die "main"-Klasse, tut nichts anderes als die Klasse f&uuml;r das GUI (MainWindow) aufzurufen.
 *
 * @since 0.1
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: JFileManager.java,v 1.1.1.1 2002-01-11 08:48:57 paul-h Exp $
 * @see MainWindow
 */
public class JFileManager {
    boolean packFrame = false;

    //Construct the application
    public JFileManager() {
      // TODO - thisis the wrong class to launch - Paul H
        MainWindow frame = new MainWindow();
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    //Main method
    public static void main(String[] args) {
        try {
            // Wir nehmen *immer* das Metal-LnF.
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        new JFileManager();
    }
}
