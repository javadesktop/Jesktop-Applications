
/**
 * Title:        JFileManager<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins<p>
 * Company:      <p>
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */
package jfilemanager;

import javax.swing.*;
import java.util.*;
import java.io.File;
import java.awt.*;
import java.awt.event.WindowEvent;
import org.jesktop.frimble.*;

/**
 * Vaterklasse für mehrere Klassen, die verschiedene Dateiarten anzeigen. Stellt einige
 * allgemeine Elemente dar wie z.B. eine Statusleiste. Ausserdem wird über eine statische
 * Variable die Anzahl der Fenster kontrolliert.
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: FileContentWindow.java,v 1.1.1.1 2002-01-11 08:48:53 paul-h Exp $
 */
public class FileContentWindow extends JFrimble implements LanguageAware {

    protected static int windowCount = 0;
    protected static boolean OK = true;
    /** Die maximale Anzahl offener Viewer-Fenster (=20) */
    public static final int MAX_WINDOWS = 20;
    private static final int X = 600;
    private final static int Y = 600;

    private Vector children;

    protected JPanel mainPanel;
    protected JLabel statusBar;

    protected LanguageData ld;

    public FileContentWindow(String title) {
        super();
        setTitle(title);
        if (windowCount < MAX_WINDOWS) {
            OK = true;
            windowCount++;
            //            System.err.println("FileContentWindow.windowCount: " + windowCount);

            ld = new LanguageData();
            mainPanel=new JPanel(new BorderLayout());
            statusBar = new JLabel();
            mainPanel.add(statusBar, BorderLayout.SOUTH);
            this.getContentPane().add(mainPanel);
            this.validate();
            //            this.setSize(X, Y);
            //            this.setVisible(true);
        }
        else {
            JOptionPane.showMessageDialog(this.getFrimbleContained(), "Too many open windows!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            OK = false;
        }
    }


    /** Update bei Sprachänderung */
    public void changeLanguage(LanguageData l) {
        // Keine Labels, keine Änderung.
    }




}
