
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
import java.io.*;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

/**
 * Diese Klasse stellt ein Fenster mit einem unformatierten Text dar.
 * Der Text wird in einem ScrollPane angezeigt.
 *
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @see FileContentWindow
 * @version $Id: PlainTextWindow.java,v 1.1.1.1 2002-01-11 08:49:04 paul-h Exp $
 */
public class PlainTextWindow extends FileContentWindow implements LanguageAware {

    private JTextArea jta;
    private JScrollPane scroller;
    private int i;
    private File file;

    public PlainTextWindow(File f) {
        super("TextViewer: " + f);
        if (OK) {
            file = f;
            jta = new JTextArea();
            jta.setFont(new Font("monospaced", Font.PLAIN, 12));
            jta.setLineWrap(true);
            jta.setWrapStyleWord(true);
            jta.setEditable(false);

            // TextArea füllen, zeilenweise
            i = 0;
            try {
                BufferedReader in = new BufferedReader(new FileReader(f));
                String line;
                String CRLF = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    i++;
                    jta.append(line + CRLF);
                }
                in.close();
            } catch (Exception e) {}

            // Position -> Zum Anfang des Fensters



            jta.setCaretPosition(1);
            statusBar.setText(f.length() + " Bytes, " + i + " " + ld.get(LanguageData.L_LINES));
            scroller = new JScrollPane(jta);
            mainPanel.add(scroller);
            this.setSize(new Dimension(800, 600));
            this.setVisible(true);
        }
    }

    /** Update bei Sprachänderung */
    public void changeLanguage(LanguageData l) {
        statusBar.setText(file.length() + " Bytes, " + i + " " + l.get(l.L_LINES));
    }

}
