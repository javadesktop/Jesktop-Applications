
/**
 * Title:        JFileManager<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Thorsten Jens, Kai Benjamins<p>
 * Company:      <p>
 * @author Thorsten Jens, Kai Benjamins
 * @version 1.0
 */
package jfilemanager;

import java.io.File;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;

/**
 * Diese Klasse stellt ein Fenster mit einem einzigen Bild darin dar.
 * Es werden einige Maßnahmen getroffen, damit das Fenster trotz großen Bildes
 * nicht größer als die Bildscirmauflösung wird.
 *
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @see FileContentWindow
 * @version $Id: ImageWindow.java,v 1.1.1.1 2002-01-11 08:48:57 paul-h Exp $
 */
public class ImageWindow extends FileContentWindow implements LanguageAware {

    private JScrollPane img;
    private ImageIcon icon;
    private int width = 0, height = 0;

    /**
     * Der Default-Konstruktor, &ouml;fnnet das Fenster.
     *
     * @param f Der absolute Pfad zur Bild-Datei.
     */
    public ImageWindow(File f) {
        super("ImageViewer: " + f.toString());
        icon = new ImageIcon(f.getAbsolutePath());
        statusBar.setText(icon.getIconWidth() + "x" + icon.getIconHeight() + " Pixels");

        // Fenstergröße ermitteln
        // Breite
        if ((icon.getIconWidth() + 25) > Toolkit.getDefaultToolkit().getScreenSize().width) {
            width = 400;
        } else {
            width = icon.getIconWidth() + 20;
        }
        // Höhe
        if ((icon.getIconHeight() + 35)> Toolkit.getDefaultToolkit().getScreenSize().height) {
            height = 400;
        } else {
            height = icon.getIconHeight() + statusBar.getPreferredSize().height + 30;
        }
        this.setSize(new Dimension(width, height));
        img = new JScrollPane(new JLabel("", icon, JLabel.CENTER));
        mainPanel.add(img);
        this.setVisible(true);
    }

    public void changeLanguage(LanguageData l) {
        // Keine Label, keine Änderung
    }



}
