
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
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import jfilemanager.FileTable;
import javax.swing.border.*;
import org.jesktop.frimble.*;

/**
 * Vaterklasse für mehrere Klassen, die verschiedene Dateioperationen zur Verfügung stellen.
 * Hierzu gehören Kopieren, Verschieben, Umbenennen und Löschen.
 * Außerdem wird in dieser Klasse sichergestellt, daß jeweils nur ein Fenster geöffnet ist.
 * @author Kai Benjamins
 * @author Thorsten Jens
 * @version $Id: FileOperationsWindow.java,v 1.1.1.1 2002-01-11 08:48:54 paul-h Exp $
 */

public class FileOperationsWindow extends JFrimble implements LanguageAware{
    Dimension Size;
    JPanel mainPanel, contentPanel, contentLabelPanel, contentFieldPanel, buttonPanel, progressPanel;
    JButton okButton, cancelButton;
    JLabel copy, copyFile, copyTo, progress;
    JTextArea progressList;
    JTextField copyFileTo;
    JProgressBar operationProgress;
    JScrollPane scrollpane;
    Font font;
    private static int windowCount = 0;
    protected static boolean OK = true;

    public FileOperationsWindow(String title){
        super();
        setTitle(title);
        if(windowCount==0) {
            windowCount++;
            //            System.err.println("FileOperationsWindow.windowCount: " + windowCount);
            OK = true;
            font = new Font("sansserif", Font.PLAIN, 12);

            contentLabelPanel = new JPanel(new GridLayout(2,1,2,2));
            copy = new JLabel();
            copyTo = new JLabel();
            contentLabelPanel.add(copy);
            contentLabelPanel.add(copyTo);

            contentFieldPanel = new JPanel(new GridLayout(2,1,10,10));
            copyFile = new JLabel();
            copyFile.setFont(font);
            copyFile.setVerticalAlignment(SwingConstants.BOTTOM);

            copyFileTo = new JTextField();
            contentFieldPanel.add(copyFile);
            contentFieldPanel.add(copyFileTo);

            contentPanel = new JPanel(new BorderLayout());
            contentPanel.add(contentLabelPanel,BorderLayout.WEST);
            contentPanel.add(contentFieldPanel,BorderLayout.CENTER);

            progressPanel = new JPanel(new BorderLayout());
            Border loweredbevel = BorderFactory.createLoweredBevelBorder();
            progressList=new JTextArea(4,20);
            progressList.setEditable(false);

            scrollpane=new JScrollPane(progressList);
            scrollpane.setBorder(loweredbevel);
            progressPanel.add(scrollpane,BorderLayout.CENTER);

            buttonPanel = new JPanel(new FlowLayout());
            okButton = new JButton("OK");
            cancelButton = new JButton("");
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                                               public void actionPerformed(ActionEvent e) {
                                                   cancelButton_actionPerformed(e);
                                               }
                                           }
                                          );

            mainPanel=new JPanel(new BorderLayout());
            mainPanel.add(contentPanel, BorderLayout.NORTH);
            mainPanel.add(progressPanel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            this.getContentPane().add(mainPanel);
            this.validate();
            this.setSize(new Dimension(380,220));
            this.setLocation(250,250);
            //            this.show();
            this.setVisible(true);

        } else {
            OK = false;
            dispose();
        }
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        //        this.dispose();
        //        windowCount--;
        closeWin();
    }

    void closeWin() {
        this.dispose();
        windowCount--;
        //        System.err.println("FileOperationsWindow.windowCount: " + windowCount);
    }

    public void changeLanguage(LanguageData l) {

    }


}
