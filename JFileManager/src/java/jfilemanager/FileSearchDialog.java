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
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Dimension;
import java.io.File;
import java.util.Vector;
import org.jesktop.frimble.*;

/**
 * Diese Klasse stellt einen generischen Dialog zur Suche nach
 * Dateien zur Verfügung.
 *
 * @version $Id: FileSearchDialog.java,v 1.1.1.1 2002-01-11 08:48:54 paul-h Exp $
 *
 * @see FileSearchThread
 * @author Thorsten Jens
 * @author Kai Benjamins
 */
public class FileSearchDialog extends JFrimble implements LanguageAware {

    // Components
    private JPanel contentPane;
    private JTextField fileNameField;
    private JComboBox startDir;
    private JLabel fileNameDesc;
    private JLabel comboBoxDesc;
    private JPanel enterGroupPanel;
    private JLabel statusBar;
    private JButton startSearch;
    private JList fileList;
    private JPanel leftGroup, midGroup, rightGroup;
    private JScrollPane scroller;
    private LanguageData ld;
    private JMenuItem popUp;
    private JPopupMenu popMenu;
    private FileSearchThread searchThread;

    // Misc. Fields
    private int numOfFiles;

    /**
     * Der Default-Konstruktor. Die Referenz auf das LanguageData-Objekt
     * liefert die aktuell eingestellte Sprache. Alle anderen Funtionen sind
     * <code>private</code>.
     * @see LanguageData
     */
    public FileSearchDialog(LanguageData ld) {
        super();
        setTitle(ld.get(ld.L_SEARCH_FOR_FILE));
        this.ld = ld;
        contentPane = (JPanel)this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        popMenu = new JPopupMenu();
        popUp = new JMenuItem(ld.get(ld.L_OPEN_WITH));
        popUp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
              popupItem_actionPerformed(e);
              }
            });
        popMenu.add(popUp);

        fileList = new JList();
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fileList.addMouseListener(new MouseAdapter(){
              public void mousePressed(MouseEvent e) {
                showPopup(e);
                }
              public void mouseReleased(MouseEvent e){
                showPopup(e);
                }
              private void showPopup(MouseEvent e){
                if (e.isPopupTrigger()) {
                  popMenu.show(e.getComponent(), e.getX(), e.getY());
                  }
              }
          }
        );

        enterGroupPanel = new JPanel();
        enterGroupPanel.setLayout(new BorderLayout(5, 5));

        leftGroup = new JPanel(new GridLayout(2, 2));
        midGroup = new JPanel(new GridLayout(2, 2));
        rightGroup = new JPanel(new GridLayout(2, 2));

        statusBar = new JLabel(" ");
        fileNameDesc = new JLabel(ld.get(ld.L_FILENAME) + ": ");
        comboBoxDesc = new JLabel(ld.get(ld.L_SEARCH_WHERE) + ": ");
        startSearch = new JButton(ld.get(ld.L_STARTSEARCH));

        fileNameField = new JTextField("");

        /*
         * Set up the combobox with the the proper directories
         */
        startDir = new JComboBox();
        startDir.setEditable(true);
        if (System.getProperty("os.name").equals("Linux") || System.getProperty("os.name").equals("Solaris")) {
            File root = new File("/");
            File rootdirs[] = root.listFiles();
            startDir.addItem("/");
            for (int i=0; i < rootdirs.length; i++) {
                if ((rootdirs[i]).isDirectory()) {
                    startDir.addItem((rootdirs[i]).toString());
                }
            }
        } else {
            File roots[] = File.listRoots();
            for (int i=0; i < roots.length; i++) {
                startDir.addItem((roots[i]).toString());
            }
        }
        startDir.addItem(System.getProperty("user.home"));
        startDir.setSelectedItem(System.getProperty("user.home"));

        startSearch.setIcon(new ImageIcon(MainWindow.class.getResource("find16.gif")));
        startSearch.addActionListener(new java.awt.event.ActionListener() {
                                          public void actionPerformed(ActionEvent e) {
                                              searchButton_actionPerformed(e);
                                          }
                                      }
                                     );

        scroller = new JScrollPane(fileList);

        leftGroup.add(fileNameDesc);
        midGroup.add(fileNameField);
        leftGroup.add(comboBoxDesc);
        midGroup.add(startDir);


        enterGroupPanel.add(leftGroup, BorderLayout.WEST);
        enterGroupPanel.add(midGroup, BorderLayout.CENTER);
        enterGroupPanel.add(startSearch, BorderLayout.SOUTH);
        enterGroupPanel.setBorder(BorderFactory.createEmptyBorder(4, 4 , 4, 4));

        contentPane.add(enterGroupPanel, BorderLayout.NORTH);
        contentPane.add(scroller, BorderLayout.CENTER);
        contentPane.add(statusBar, BorderLayout.SOUTH);

        this.setSize(new Dimension(500, 400));
        this.setVisible(true);
    }


    void searchButton_actionPerformed(ActionEvent e) {
        Vector result = new Vector();
        String dir = (String)startDir.getSelectedItem();
        if (fileNameField.getText() != null && !fileNameField.getText().trim().equals("")) {
          File test = new File(dir);
          if (test.canRead()) {
//            result = findFileMain(new File(dir), fileNameField.getText(), result);
              searchThread = new FileSearchThread(this.getFrimbleContained(), fileList, result, fileNameField.getText(),
                                new File(dir), statusBar, ld, startSearch);
            searchThread.start();
            startSearch.setEnabled(false);
          } else {
            JOptionPane.showMessageDialog(this.getFrimbleContained(), ld.get(ld.L_NO_PERMISSION),
              ld.get(ld.L_ERROR), JOptionPane.ERROR_MESSAGE);
          }
        } else {
          JOptionPane.showMessageDialog(this.getFrimbleContained(), ld.get(ld.L_NO_FILENAME),
             ld.get(ld.L_ERROR), JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Gibt das aktuelle Verzeichnis und die Rekursionstiefe auf
     * <code>System.err</code> aus.
     *
     * @param dir Das aktuelle Verzeichnis
     * @param depth Die Rekursionstiefe
     *
     * @see #MAX_DEPTH
     */
    void printUpdate(File dir, int depth) {
        //      statusBar.setText(depth + dir.getPath());
        //      statusBar.repaint();
        System.err.println(depth + " " + dir.getPath());
    }


    public void changeLanguage(LanguageData l) {
      fileNameDesc.setText(l.get(l.L_FILENAME) + ": ");
      comboBoxDesc.setText(l.get(l.L_SEARCH_WHERE) + ": ");
      startSearch.setText(l.get(l.L_STARTSEARCH));
    }


    private void popupItem_actionPerformed(ActionEvent e){
      Vector v = new Vector(1);
      v.add(new File((String)fileList.getSelectedValue()));
      OpenWithWindow runProg = new OpenWithWindow(v, ld);
      v = null;
    }

//  TODO - kill search thread on close - Paul H
//    protected void processWindowEvent(WindowEvent e) {
//        super.processWindowEvent(e);
//        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//            searchThread.pleaseStop();
//            this.dispose();
//        }
//    }
}