
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
import java.io.File;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;

/**
 * Ein Fenster, das eine HTML-Datei interpretiert anzeigt. Das Fenster hat
 * aussederm einen Buuton, mit dem man sich den Quelltext der Seite in einem
 * Extra-Fenster ansehen kann.
 *
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @see FileContentWindow
 * @version $Id: HTMLWindow.java,v 1.1.1.1 2002-01-11 08:48:56 paul-h Exp $
 */
public class HTMLWindow extends FileContentWindow implements LanguageAware {

    private JScrollPane scrollPane;
    private JEditorPane htmlPane;
    private JButton button;


    private final File file;

    /**
     * Default-Konstruktor. &Ouml;ffnet das Fenster und zeigt die Datei an.
     *
     * @param f Die HTML-Datei
     */
    public HTMLWindow(File f) {
        super("HTMLViewer: " + f.toString());
        file = f;
        if (OK) {
            htmlPane = new JEditorPane();
            htmlPane.setEditable(false);
            try {
                htmlPane.setPage(f.toURL());
            } catch(Exception ie) {
                System.err.println(ie.toString());
            }

            button = new JButton(ld.get(LanguageData.L_VIEW_SOURCE));
            button.addActionListener(new java.awt.event.ActionListener() {
                                         public void actionPerformed(ActionEvent e) {
                                             PlainTextWindow ptw = new PlainTextWindow(file);
                                         }
                                     }
                                    );
            scrollPane=new JScrollPane(htmlPane);
            mainPanel.add(scrollPane,BorderLayout.CENTER);
            mainPanel.add(button, BorderLayout.NORTH);
        }
        this.setSize(new Dimension(800, 600));
        this.setVisible(true);
    }


    public void changeLanguage(LanguageData l) {
        button.setText(l.get(l.L_VIEW_SOURCE));
    }

}
