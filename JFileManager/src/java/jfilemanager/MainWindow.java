package jfilemanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.awt.Dimension;
import java.util.*;
import java.beans.*;
import org.jesktop.frimble.*;

/**
 * Das Haupt-GUI. Beinhaltet je zwei FileTables und Detailviews, Menüleiste, usw.
 *
 * $Id: MainWindow.java,v 1.1.1.1 2002-01-11 08:49:02 paul-h Exp $
 *
 * @see FileTable
 * @see DetailView
 * @author Thorsten Jens
 * @author Kai Benjamins
 * @version $Id: MainWindow.java,v 1.1.1.1 2002-01-11 08:49:02 paul-h Exp $
 */
public class MainWindow extends JFrimble implements LanguageAware {
    public static final String JFM_VERSION = "$Id: MainWindow.java,v 1.1.1.1 2002-01-11 08:49:02 paul-h Exp $";
    boolean firstTime = true;
    LanguageData ld = new LanguageData();
    JPanel contentPane;
    boolean linkDetailViews = false;
    boolean leftTable=false;

    BorderLayout borderLayout1 = new BorderLayout();
    JToolBar mainToolBar = new JToolBar();
    JButton searchButton = new JButton();
    JPanel statusBarPanel = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JLabel leftStatusBar = new JLabel();
    JLabel rightStatusBar = new JLabel();
    JSplitPane mainSplitPane = new JSplitPane();
    JSplitPane leftSplitPane = new JSplitPane();
    JSplitPane rightSplitPane = new JSplitPane();
    DetailView leftDetailView = new DetailView();
    DetailView rightDetailView = new DetailView();

    FileTable leftFileTable = new FileTable(leftDetailView, ld);
    FileTable rightFileTable = new FileTable(rightDetailView, ld);

    JPanel leftHelpPanel = new JPanel(new BorderLayout());
    JPanel rightHelpPanel = new JPanel(new BorderLayout());

    JToolBar leftToolBar = new JToolBar();
    JToolBar rightToolBar = new JToolBar();

    JButton leftBackButton = new JButton();
    JButton rightBackButton = new JButton();
    JButton leftDelButton = new JButton();
    JButton rightDelButton = new JButton();
    JButton leftHomeButton = new JButton();
    JButton rightHomeButton = new JButton();
    JButton leftCopyButton = new JButton();
    JButton leftMoveButton = new JButton();
    JButton rightCopyButton = new JButton();
    JButton rightMoveButton = new JButton();

    // Kai: Das Füllen mach ich unten!
    JComboBox leftComboBox = new JComboBox();
    JComboBox rightComboBox = new JComboBox();
    JButton infoButton = new JButton();
    JButton exitButton = new JButton();
    JButton prefsButton = new JButton();
    JToggleButton linkSizesButton = new JToggleButton();

    JPopupMenu popup = new JPopupMenu();
    JMenuItem popupItem;

    //Construct the frame
    public MainWindow() {
        try {
            jbInit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception  {
        rightStatusBar.setText("");
        leftStatusBar.setText ("");

        //PopUp-Menu initialisieren...
        popupItem = new JMenuItem(ld.get(ld.L_OPEN_WITH));
        popupItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
              popupItem_actionPerformed(e);
              }
            }

        );
        popup.add(popupItem);
        // ...für die linke JTable
        leftFileTable.table.addMouseListener(new MouseAdapter(){
              public void mousePressed(MouseEvent e) {
                showPopup(e);
                }
              public void mouseReleased(MouseEvent e){
                showPopup(e);
                }
              private void showPopup(MouseEvent e){
                if (e.isPopupTrigger()) {
                  leftFileTable.table.clearSelection();
                  leftFileTable.table.addRowSelectionInterval(leftFileTable.table.rowAtPoint(e.getPoint()),leftFileTable.table.rowAtPoint(e.getPoint()));
                  leftTable=true;
                  popup.show(e.getComponent(), e.getX(), e.getY());
                  }
              }
          }
        );
        // ...für die rechte JTable
        rightFileTable.table.addMouseListener(new MouseAdapter(){
              public void mousePressed(MouseEvent e) {
                showPopup(e);
                }
              public void mouseReleased(MouseEvent e){
                showPopup(e);
                }
              private void showPopup(MouseEvent e){
                if (e.isPopupTrigger()) {
                  rightFileTable.table.clearSelection();
                  rightFileTable.table.addRowSelectionInterval(rightFileTable.table.rowAtPoint(e.getPoint()),leftFileTable.table.rowAtPoint(e.getPoint()));
                  leftTable=false;
                  popup.show(e.getComponent(), e.getX(), e.getY());
                  }
              }
          }
        );

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(800, 600));
        this.setTitle("JFileManager " + JFM_VERSION);

        searchButton.setIcon(new ImageIcon(MainWindow.class.getResource("find24.gif")));
        searchButton.setRolloverEnabled(true);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                               searchButton_actionPerformed(e);
                                           }
                                       }
                                      );
        statusBarPanel.setLayout(gridLayout1);
        gridLayout1.setColumns(2);
        mainSplitPane.setLeftComponent(leftSplitPane);
        mainSplitPane.setOneTouchExpandable(true);
        leftSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        leftSplitPane.setOneTouchExpandable(true);
        leftSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                                                    public void propertyChange(PropertyChangeEvent e) {
                                                        leftSplitPane_propertyChange(e);
                                                    }
                                                }
                                               );
        rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setOneTouchExpandable(true);
        rightSplitPane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        rightSplitPane_propertyChange(e);
                    }
                }
                                                );
        rightBackButton.setIcon(new ImageIcon(MainWindow.class.getResource("back16.gif")));
        rightBackButton.setToolTipText(ld.get(ld.L_DIRBACK));
        rightBackButton.addActionListener(new java.awt.event.ActionListener() {
                                              public void actionPerformed(ActionEvent e) {
                                                  rightBackButton_actionPerformed(e);
                                              }
                                          }
                                         );
        rightHomeButton.setIcon(new ImageIcon(MainWindow.class.getResource("home16.gif")));
        rightHomeButton.setToolTipText(ld.get(ld.L_HOMEDIR));
        rightHomeButton.addActionListener(new java.awt.event.ActionListener() {
                                              public void actionPerformed(ActionEvent e) {
                                                  rightHomeButton_actionPerformed(e);
                                              }
                                          }
                                         );

        rightDelButton.setIcon(new ImageIcon(MainWindow.class.getResource("delete16.gif")));
        rightDelButton.setToolTipText(ld.get(ld.L_DELETE_FILES));
        rightDelButton.addActionListener(new java.awt.event.ActionListener() {

                                             public void actionPerformed(ActionEvent e) {
                                                 rightDelButton_actionPerformed(e);
                                             }
                                         }
                                        );

        leftDelButton.setIcon(new ImageIcon(MainWindow.class.getResource("delete16.gif")));
        leftDelButton.setToolTipText(ld.get(ld.L_DELETE_FILES));
        leftDelButton.addActionListener(new java.awt.event.ActionListener() {

                                            public void actionPerformed(ActionEvent e) {
                                                leftDelButton_actionPerformed(e);
                                            }
                                        }
                                       );



        leftBackButton.setIcon(new ImageIcon(MainWindow.class.getResource("back16.gif")));
        leftBackButton.setToolTipText(ld.get(ld.L_DIRBACK));
        leftBackButton.addActionListener(new java.awt.event.ActionListener() {

                                             public void actionPerformed(ActionEvent e) {
                                                 leftBackButton_actionPerformed(e);
                                             }
                                         }
                                        );
        leftHomeButton.setIcon(new ImageIcon(MainWindow.class.getResource("home16.gif")));
        leftHomeButton.setToolTipText(ld.get(ld.L_HOMEDIR));
        leftHomeButton.addActionListener(new java.awt.event.ActionListener() {

                                             public void actionPerformed(ActionEvent e) {
                                                 leftHomeButton_actionPerformed(e);
                                             }
                                         }
                                        );

        leftComboBox.addActionListener(new java.awt.event.ActionListener() {

                                           public void actionPerformed(ActionEvent e) {
                                               leftComboBox_actionPerformed(e);
                                           }
                                       }
                                      );
        rightComboBox.addActionListener(new java.awt.event.ActionListener() {

                                            public void actionPerformed(ActionEvent e) {
                                                rightComboBox_actionPerformed(e);
                                            }
                                        }
                                       );

        //Copy-Button initialisieren
        leftCopyButton.setIcon(new ImageIcon(MainWindow.class.getResource("Copy16.gif")));
        leftCopyButton.setToolTipText(ld.get(ld.L_COPY_FILES));
        leftCopyButton.addActionListener(new java.awt.event.ActionListener() {

                                             public void actionPerformed(ActionEvent e) {
                                                 leftCopyButton_actionPerformed(e);
                                             }
                                         }
                                        );

        rightCopyButton.setIcon(new ImageIcon(MainWindow.class.getResource("Copy16.gif")));
        rightCopyButton.setToolTipText(ld.get(ld.L_COPY_FILES));
        rightCopyButton.addActionListener(new java.awt.event.ActionListener() {

                                              public void actionPerformed(ActionEvent e) {
                                                  rightCopyButton_actionPerformed(e);
                                              }
                                          }
                                         );

        // Move-Button initialisieren
        leftMoveButton.setIcon(new ImageIcon(MainWindow.class.getResource("Move16.gif")));
        leftMoveButton.setToolTipText(ld.get(ld.L_MOVE_FILES));
        leftMoveButton.addActionListener(new java.awt.event.ActionListener() {

                                             public void actionPerformed(ActionEvent e) {
                                                 leftMoveButton_actionPerformed(e);
                                             }
                                         }
                                        );

        rightMoveButton.setIcon(new ImageIcon(MainWindow.class.getResource("Move16.gif")));
        rightMoveButton.setToolTipText(ld.get(ld.L_MOVE_FILES));
        rightMoveButton.addActionListener(new java.awt.event.ActionListener() {

                                              public void actionPerformed(ActionEvent e) {
                                                  rightMoveButton_actionPerformed(e);
                                              }
                                          }
                                         );


        /*
                contentPane.addComponentListener(new java.awt.event.ComponentAdapter() {

              public void componentResized(ComponentEvent e) {
                contentPane_componentResized(e);
              }
            });
        */
        infoButton.setIcon(new ImageIcon(MainWindow.class.getResource("information24.gif")));
    infoButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        infoButton_actionPerformed(e);
      }
    });
        exitButton.setIcon(new ImageIcon(MainWindow.class.getResource("stop24.gif")));
        exitButton.addActionListener(new java.awt.event.ActionListener() {

                                         public void actionPerformed(ActionEvent e) {
                                             exitButton_actionPerformed(e);
                                         }
                                     }
                                    );
        prefsButton.setIcon(new ImageIcon(MainWindow.class.getResource("preferences24.gif")));
        prefsButton.addActionListener(new java.awt.event.ActionListener() {

                                          public void actionPerformed(ActionEvent e) {
                                              prefsButton_actionPerformed(e);
                                          }
                                      }
                                     );

        linkSizesButton.addActionListener(new java.awt.event.ActionListener() {

                                              public void actionPerformed(ActionEvent e) {
                                                  linkSizesButton_actionPerformed(e);
                                              }
                                          }
                                         );
        linkSizesButton.setIcon(new ImageIcon(MainWindow.class.getResource("linkSizes.gif")));
        linkSizesButton.setMargin(new Insets(0, 0, 0, 0));
        linkSizesButton.setMaximumSize(new Dimension(31, 31));
        linkSizesButton.setMinimumSize(new Dimension(31, 31));
        contentPane.add(mainToolBar, BorderLayout.NORTH);

        exitButton.setToolTipText(ld.get(ld.L_EXIT));
        infoButton.setToolTipText(ld.get(ld.L_HELP));
        prefsButton.setToolTipText(ld.get(ld.L_PREFERENCES));
        searchButton.setToolTipText(ld.get(ld.L_SEARCH_FOR_FILE));
        linkSizesButton.setToolTipText(ld.get(ld.L_LINK_DETAIL_SIZES));

        mainToolBar.setMargin(new Insets(3, 3, 3, 3));
        mainToolBar.add(exitButton, null);
        mainToolBar.addSeparator();
        mainToolBar.add(searchButton, null);
        mainToolBar.addSeparator();
        mainToolBar.add(prefsButton, null);
        mainToolBar.addSeparator();
        mainToolBar.add(linkSizesButton, null);
        mainToolBar.add(Box.createHorizontalGlue());
        mainToolBar.add(infoButton, null);
        mainToolBar.setFloatable(false);

        contentPane.add(statusBarPanel, BorderLayout.SOUTH);
        statusBarPanel.add(leftStatusBar, null);
        statusBarPanel.add(rightStatusBar, null);

        leftToolBar.add(leftBackButton);
        leftToolBar.add(leftHomeButton);
        leftToolBar.add(leftDelButton);
        leftToolBar.add(leftCopyButton);
        leftToolBar.add(leftMoveButton);
        leftToolBar.addSeparator();
        leftToolBar.add(leftComboBox);

        rightToolBar.add(rightBackButton);
        rightToolBar.add(rightHomeButton);
        rightToolBar.add(rightDelButton);
        rightToolBar.add(rightCopyButton);
        rightToolBar.add(rightMoveButton);
        rightToolBar.addSeparator();
        rightToolBar.add(rightComboBox);

        if (System.getProperty("os.name").equals("Linux") || System.getProperty("os.name").equals("Solaris")) {
            File root = new File("/");
            File rootdirs[] = root.listFiles();
            leftComboBox.addItem("/");
            rightComboBox.addItem("/");
            for (int i=0; i < rootdirs.length; i++) {
                if ((rootdirs[i]).isDirectory()) {
                    leftComboBox.addItem((rootdirs[i]).toString());
                    rightComboBox.addItem((rootdirs[i]).toString());
                   }
                }
                String tmp = leftFileTable.returnHomePath();
                leftComboBox.addItem(tmp);
                rightComboBox.addItem(tmp);
                leftComboBox.setSelectedItem(tmp);
                rightComboBox.setSelectedItem(tmp);
                tmp = null;
        } else {
            File roots[] = File.listRoots();
            for (int i=0; i < roots.length; i++) {
                leftComboBox.addItem((roots[i]).toString());
                rightComboBox.addItem((roots[i]).toString());
            }
            leftComboBox.setSelectedItem(leftFileTable.returnHomePath());
            rightComboBox.setSelectedItem(rightFileTable.returnHomePath());
        }


        //Erzeugt zwei ActionListener zur Anzeige des aktuellen Pfades
        leftFileTable.table.addMouseListener(new MouseAdapter() {
                                                 public void mouseClicked(MouseEvent e) {
                                                     setDetailViewStatusLabel();
                                                 }
                                             }
                                            );

        rightFileTable.table.addMouseListener(new MouseAdapter() {
                                                  public void mouseClicked(MouseEvent e) {
                                                      setDetailViewStatusLabel();
                                                  }
                                              }
                                             );

        contentPane.add(mainSplitPane, BorderLayout.CENTER);
        mainSplitPane.add(leftSplitPane, JSplitPane.LEFT);
        leftHelpPanel.add(leftFileTable, BorderLayout.CENTER);
        leftHelpPanel.add(leftToolBar, BorderLayout.NORTH);
        leftSplitPane.add(leftHelpPanel, JSplitPane.TOP);
        leftSplitPane.add(leftDetailView, JSplitPane.BOTTOM);


        mainSplitPane.add(rightSplitPane, JSplitPane.RIGHT);
        rightHelpPanel.add(rightFileTable, BorderLayout.CENTER);
        rightHelpPanel.add(rightToolBar, BorderLayout.NORTH);
        rightSplitPane.add(rightHelpPanel, JSplitPane.TOP);
        rightSplitPane.add(rightDetailView, JSplitPane.BOTTOM);

        mainSplitPane.setDividerLocation((this.getWidth()/2)-2*(mainSplitPane.getDividerSize()));

        //        System.err.println("field height: " + leftDetailView.getPreferredHeight());

        rightSplitPane.setDividerLocation(mainSplitPane.getPreferredSize().height - rightDetailView.getPreferredHeight() - 30);
        leftSplitPane.setDividerLocation(mainSplitPane.getPreferredSize().height - rightDetailView.getPreferredHeight() - 30);

        firstTime = false;
    }

    //File | Exit action performed
    public void exitButton_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    /*
    //Help | About action performed
    public void helpAbout_actionPerformed(ActionEvent e) {}
*/

//   TODO - still do exitButton_actionPerformed() - Paul H
//    protected void processWindowEvent(WindowEvent e) {
//        super.processWindowEvent(e);
//        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//            exitButton_actionPerformed(null);
//        }
//    }

    void leftComboBox_actionPerformed(ActionEvent e) {
        if(firstTime==false)
            leftFileTable.setRootTable((String)leftComboBox.getSelectedItem());
        setDetailViewStatusLabel();
    }

    void rightComboBox_actionPerformed(ActionEvent e) {
        if(firstTime==false)
            rightFileTable.setRootTable((String)rightComboBox.getSelectedItem());
        setDetailViewStatusLabel();
    }

    void leftHomeButton_actionPerformed(ActionEvent e) {
        leftComboBox.setSelectedItem(leftFileTable.returnHomePath());
        leftFileTable.setHomeTable();
        setDetailViewStatusLabel();
    }

    void rightHomeButton_actionPerformed(ActionEvent e) {
        rightComboBox.setSelectedItem(rightFileTable.returnHomePath());
        rightFileTable.setHomeTable();
        setDetailViewStatusLabel();
    }

    void setDetailViewStatusLabel() {
        leftDetailView.setStatusLabel(leftFileTable.getActualPath());
        rightDetailView.setStatusLabel(rightFileTable.getActualPath());
    }

    void leftBackButton_actionPerformed(ActionEvent e) {
        leftFileTable.setParentDirectoryTable();
        setDetailViewStatusLabel();
    }

    void rightBackButton_actionPerformed(ActionEvent e) {
        rightFileTable.setParentDirectoryTable();
        setDetailViewStatusLabel();
    }

    void searchButton_actionPerformed(ActionEvent e) {
        FileSearchDialog fsd = new FileSearchDialog(ld);
    }

    void leftCopyButton_actionPerformed(ActionEvent e) {
        if(leftFileTable.getSelectedFiles().size()>0) {
            CopyFileWindow copy = new CopyFileWindow(leftFileTable.getSelectedFiles(),rightFileTable.getActualPath(), rightFileTable,ld);
        }
    }

    void rightCopyButton_actionPerformed(ActionEvent e) {
        if(rightFileTable.getSelectedFiles().size()>0) {
            CopyFileWindow copy = new CopyFileWindow(rightFileTable.getSelectedFiles(),leftFileTable.getActualPath(), leftFileTable,ld);
        }
    }

    void leftMoveButton_actionPerformed(ActionEvent e) {
        if(leftFileTable.getSelectedFiles().size()>0) {
            MoveFileWindow copy = new MoveFileWindow(leftFileTable.getSelectedFiles(),rightFileTable.getActualPath(),leftFileTable, rightFileTable,ld);
        }
    }

    void rightMoveButton_actionPerformed(ActionEvent e) {
        if(rightFileTable.getSelectedFiles().size()>0) {
            MoveFileWindow move = new MoveFileWindow(rightFileTable.getSelectedFiles(),leftFileTable.getActualPath(),rightFileTable,leftFileTable,ld);
        }
    }

    void leftDelButton_actionPerformed(ActionEvent e) {
        if(leftFileTable.getSelectedFiles().size()>0) {
            DeleteFileWindow delete = new DeleteFileWindow(leftFileTable.getSelectedFiles(),leftFileTable,ld);
        }
    }

    void rightDelButton_actionPerformed(ActionEvent e) {
        if(rightFileTable.getSelectedFiles().size()>0) {
            DeleteFileWindow delete = new DeleteFileWindow(rightFileTable.getSelectedFiles(), rightFileTable,ld);
        }
    }

    void menuFileSearch_actionPerformed(ActionEvent e) {
        FileSearchDialog fsd = new FileSearchDialog(ld);
    }

    void contentPane_componentResized(ComponentEvent e) {
        if (!firstTime) {
            rightSplitPane.setDividerLocation(mainSplitPane.getPreferredSize().height - rightDetailView.getPreferredHeight() - 30);
            leftSplitPane.setDividerLocation(mainSplitPane.getPreferredSize().height - rightDetailView.getPreferredHeight() - 30);
        }
        /*      if (firstTime == false) {
                int leftpos = contentPane.getHeight() - leftSplitPane.getDividerLocation();
                System.out.println("l: " + leftpos);
                int rightpos = contentPane.getHeight() - leftSplitPane.getDividerLocation();
                System.out.println("r: " + rightpos);
                super.processComponentEvent(e);
                rightSplitPane.setDividerLocation(mainSplitPane.getPreferredSize().height - rightDetailView.getPreferredHeight() - 30);
                leftSplitPane.setDividerLocation(mainSplitPane.getPreferredSize().height - rightDetailView.getPreferredHeight() - 30);

                leftSplitPane.setDividerLocation(leftpos);
                rightSplitPane.setDividerLocation(rightpos);
              }
              leftSplitPane.resetToPreferredSizes();
        */
    }

    void prefsButton_actionPerformed(ActionEvent e) {
        Object o;
        String newLanguage;
        String[] availableLanguages = ld.getAvailableLanguages();
        //    int numLangs = availableLanguages.length;

        o = JOptionPane.showInputDialog(
                this.getFrimbleContained(),
                ld.get(ld.L_SELECT_LANGUAGE),
                ld.get(ld.L_OPTIONS),
                JOptionPane.QUESTION_MESSAGE,
                null,
                availableLanguages,
                availableLanguages[0]);
        if (o != null) {
            newLanguage = (String)o;
            if (ld.load(newLanguage) != ld.LOAD_FILE_NOT_FOUND) {
                changeLanguage(ld);
            } else {
                JOptionPane.showMessageDialog(
                    this.getFrimbleContained(),
                    ld.get(ld.L_FILE_ERROR) + " \"" + newLanguage + ".langdata\"",
                    ld.get(ld.L_ERROR),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void changeLanguage(LanguageData l) {
        // DetailViews
        leftDetailView.changeLanguage(l);
        rightDetailView.changeLanguage(l);

        popupItem.setText(ld.get(ld.L_OPEN_WITH));
        // Toolbar
        exitButton.setToolTipText(l.get(l.L_EXIT));
        infoButton.setToolTipText(l.get(l.L_HELP));
        prefsButton.setToolTipText(l.get(l.L_PREFERENCES));
        searchButton.setToolTipText(l.get(l.L_SEARCH_FOR_FILE));
        linkSizesButton.setToolTipText(l.get(l.L_LINK_DETAIL_SIZES));

        // kleine Toolbars
        leftCopyButton.setToolTipText(ld.get(ld.L_COPY_FILES));
        rightCopyButton.setToolTipText(ld.get(ld.L_COPY_FILES));
        leftMoveButton.setToolTipText(ld.get(ld.L_MOVE_FILES));
        rightMoveButton.setToolTipText(ld.get(ld.L_MOVE_FILES));
        leftDelButton.setToolTipText(ld.get(ld.L_DELETE_FILES));
        rightDelButton.setToolTipText(ld.get(ld.L_DELETE_FILES));
        leftHomeButton.setToolTipText(ld.get(ld.L_HOMEDIR));
        rightHomeButton.setToolTipText(ld.get(ld.L_HOMEDIR));
        leftBackButton.setToolTipText(ld.get(ld.L_DIRBACK));
        rightBackButton.setToolTipText(ld.get(ld.L_DIRBACK));

    }


    void leftSplitPane_propertyChange(PropertyChangeEvent e) {
        if (linkDetailViews) {
            rightSplitPane.setDividerLocation(leftSplitPane.getDividerLocation());
        }
    }

    void rightSplitPane_propertyChange(PropertyChangeEvent e) {
        if (linkDetailViews) {
            leftSplitPane.setDividerLocation(rightSplitPane.getDividerLocation());
        }
    }

    void linkSizesButton_actionPerformed(ActionEvent e) {
        linkDetailViews = linkSizesButton.isSelected();
    }

    void popupItem_actionPerformed(ActionEvent e){
      if(leftTable){
        if(leftFileTable.getSelectedFiles().size()==1){
          OpenWithWindow runProg = new OpenWithWindow(leftFileTable.getSelectedFiles(),ld);
        }
      }else{
        if(rightFileTable.getSelectedFiles().size()==1){
          OpenWithWindow runProg = new OpenWithWindow(rightFileTable.getSelectedFiles(),ld);
        }
      }
    }

    void infoButton_actionPerformed(ActionEvent e) {
      //String NL = System.getProperty("line.separator");
      JOptionPane.showMessageDialog(this.getFrimbleContained(),
                    "JFileManager, Version " + JFM_VERSION+"\n\n" +
                    "Geschrieben von:\n"  + "Thorsten Jens <thojens@gmx.de>\n" +
                    "Kai Benjamins <kaiben@gmx.net>\n\n" +
                    "Die Niederländische Sprachdatei wurde erstellt von\n" +
                    "Hendrik Schwarz <h@ndrik.de>\n\n" +
                    "Bei Fragen lesen Sie bitte das mitgelieferte Benutzerhandbuch\n" +
                    "(Manual.pdf) oder wenden sich per Email an die Autoren,\n"  +
                    "Feedback ist willkommen!\n" +
                    "Alle verwendeten Icons sind frei verfügbar oder wurden von\n" +
                    "den Autoren selbst erstellt.",
                    ld.get(ld.L_ABOUT) + " JFileManager" + JFM_VERSION,
                    JOptionPane.INFORMATION_MESSAGE);
  }


}
