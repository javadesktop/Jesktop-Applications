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
 * @version $Id: JFileManager.java,v 1.2 2004-07-25 13:01:50 paul-h Exp $
 * @see MainWindow
 */
public class JFileManager extends MainWindow{
    boolean packFrame = false;

    //Construct the application
    public JFileManager() {
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            super.pack();
        } else {
            super.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = super.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        super.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        super.setVisible(true);
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
