package jfilemanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import javax.swing.border.EtchedBorder;

/**
 * DetailView ist ein kleines JPanel, das Informationen über Dateien anzeigt.
 * Ausserdem können einige Dateien über einen View-Button in einem <b>FileContentWindow</b>
 * geöffnet werden.
 *
 * @see ExtendedFile
 * @see FileContentWindow
 * @author Thorsten Jens
 * @author Kai Benjamins
 * @version $id$ 
 */
public class DetailView extends JPanel implements LanguageAware {

    private JPanel dataPanel;
    private JPanel labelPanel;
    private JPanel fieldPanel;
    private JPanel statusPanel;

    private JLabel nameLabel;
    private JLabel sizeLabel;
    private JLabel dateLabel;
    private JLabel attribLabel;
    private JLabel statusLabel;

    private JTextField nameField;
    private JTextField sizeField;
    private JTextField dateField;
    private JTextField attribField;

    private JButton viewButton;
    private JButton execButton;

    private EtchedBorder statusPanelBorder;
    private Font font;

    private LanguageData ld;

    private File file;

    public DetailView() {
        // LanguageData initialisieren
        ld = new LanguageData();

        // Create the default font
        font = new Font("sansserif", Font.PLAIN, 12);

        // Initialize the components
        nameLabel = new JLabel(ld.get(LanguageData.L_NAME) + ": ");
        nameLabel.setFont(font);
        nameField = new JTextField();
        nameField.setEditable(false);
        nameField.setBackground(Color.white);
        nameField.setFont(font);

        sizeLabel = new JLabel(ld.get(LanguageData.L_SIZE) + ": ");
        sizeLabel.setFont(font);
        sizeField = new JTextField();
        sizeField.setEditable(false);
        sizeField.setBackground(Color.white);
        sizeField.setFont(font);
        sizeField.setHorizontalAlignment(SwingConstants.RIGHT);

        dateLabel = new JLabel(ld.get(LanguageData.L_DATE) + ": ");
        dateLabel.setFont(font);
        dateField = new JTextField();
        dateField.setEditable(false);
        dateField.setBackground(Color.white);
        dateField.setFont(font);
        dateField.setHorizontalAlignment(SwingConstants.RIGHT);

        attribLabel = new JLabel(ld.get(LanguageData.L_ATTRIB) + ": ");
        attribLabel.setFont(font);
        attribField = new JTextField();
        attribField.setEditable(false);
        attribField.setBackground(Color.white);
        attribField.setFont(font);

        statusPanelBorder=new EtchedBorder(1);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(font);

        viewButton = new JButton(ld.get(LanguageData.L_VIEW));
        viewButton.setEnabled(false);
        //        execButton = new JButton(ld.get(LanguageData.L_EXEC));
        //        execButton.setEnabled(false);


        // Set up the panels
        // 1. the labels
        labelPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        labelPanel.add(nameLabel, null);
        labelPanel.add(sizeLabel, null);
        labelPanel.add(dateLabel, null);
        labelPanel.add(attribLabel, null);

        // 2. the fields
        fieldPanel = new JPanel(new GridLayout(4, 1, 0, 0));
        fieldPanel.add(nameField, null);
        fieldPanel.add(sizeField, null);
        fieldPanel.add(dateField, null);
        fieldPanel.add(attribField, null);

        //3. the status
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(statusPanelBorder);
        statusPanel.add(statusLabel,BorderLayout.WEST);

        // 4. the containers
        dataPanel = new JPanel(new BorderLayout());
        dataPanel.add(labelPanel, BorderLayout.WEST);
        dataPanel.add(fieldPanel, BorderLayout.CENTER);
        dataPanel.add(viewButton, BorderLayout.EAST);

        this.setLayout(new BorderLayout());
        this.add(dataPanel, BorderLayout.CENTER);
        this.add(statusPanel, BorderLayout.SOUTH);


        viewButton.addActionListener(new java.awt.event.ActionListener() {
                                         public void actionPerformed(ActionEvent e) {
                                             openExternalWindow();
                                         }
                                     }
                                    );

    }


    /**
     * &Ouml;ffnet ein neues Fenster, das die gew&auml;hlte Datei anzeigt, sofern der Dateityp
     * unterst&uuml;tzt wird. Dient ausserdem als <code>ActionListener</code> für
     * den <i>Anzeigen</i>-Button.
     *
     * @return Gibt zurück, ob des Dateityp unterstützt wird.
     * @see ExtendedFile#getExtensionType
     *
     */
    public boolean openExternalWindow() {
        boolean retVal = true;
        if (file != null && file.isFile()) {
            switch(ExtendedFile.getExtensionType(file)) {
            case ExtendedFile.ZIP:
                ZipListWindow zlw = new ZipListWindow(file);
                break;
            case ExtendedFile.HTML:
                HTMLWindow htmlw = new HTMLWindow(file);
                break;
            case ExtendedFile.IMAGE:
                ImageWindow imw = new ImageWindow(file);
                break;
            case ExtendedFile.PLAIN:
                PlainTextWindow ptw = new PlainTextWindow(file);
                break;
            default:
                retVal = false;
                break;
            }
        } else {
            retVal = false;
        }
        return retVal;
    }


    /**
     * Gibt die Informationen über eine Datei im Panel aus.
     *
     * @see #showInfo(File)
     * @returns Gibt <code><b>true</b></code> zurück, wenn die Aktion
     * erfolgreich war, und <code><b>false</b></code>, falls Fehler aufgetreten sind.
     * @param s Ein Dateiname. Der String wird in ein <code>java.io.File</code>
     * konvertiert und dann die Methode showInfo(File) aufgerufen.
     */
    public boolean showInfo(String s) {
        File f = new File(s);
        if (f.exists()) {
            return this.showInfo(f);
        } else {
            return false;
        }
    }

    /**
     * Gibt die Informationen über eine Datei im Panel aus. Angezeigt werden:
     * <ol>
     * 	<li>Dateiname</li>
     * 	<li>Größe der Datei</li>
     * 	<li>Datum der letzten Änderung</li>
     * 	<li>Dateiattribute</li>
     * </ol>
     *	Dabei wird teilweise auf die Methoden der Klasse <b>ExtendedFile</b>
     *  zurückgegriffen.
     *
     * @see ExtendedFile
     * @see #showInfo(File)
     * @returns Gibt <code><b>true</b></code> zurück, wenn die Aktion
     * erfolgreich war, und <code><b>false</b></code>, falls Fehler aufgetreten sind
     * (z.B. Datei nicht gefunden).
    */
    public boolean showInfo(File f) {
        if (f.exists()) {
            this.file = f;
            this.nameField.setText(f.getName());
            this.nameField.setCaretPosition(JTextField.LEFT);
            this.sizeField.setText(ExtendedFile.getBeautifulFileSize(f));
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            this.dateField.setText(df.format(new Date(f.lastModified())));
            this.attribField.setText((f.isDirectory() ? "D" : "-") +
                                     (f.canRead() ? "R" : "-") +
                                     (f.canWrite() ? "W" : "-") +
                                     (f.isHidden() ? "H" : "-"));
            viewButton.setEnabled(ExtendedFile.isViewable(f) && f.canRead());
            return true;
        } else {
            return false;
        }
    }


    public int getPreferredHeight() {
        return nameField.getPreferredSize().height +
               sizeField.getPreferredSize().height +
               dateField.getPreferredSize().height +
               attribField.getPreferredSize().height +
               statusLabel.getPreferredSize().height +
               ((System.getProperty("os.name").toLowerCase().indexOf("win") > 0) ? 20 : 5);
    }

		/**
	   * Zur &Auml;nderung des statusLabels (zeigt den aktuellen Pfad).
  	 *
     * @param path der aktuelle Pfad als String
   	 */
    public void setStatusLabel(String path) {
        statusLabel.setText(path);
    }

    /**
     * Update bei Sprachänderung
     */
    public void changeLanguage(LanguageData l) {
        nameLabel.setText(l.get(l.L_NAME) + ": ");
        sizeLabel.setText(l.get(l.L_SIZE) + ": ");
        dateLabel.setText(l.get(l.L_DATE) + ": ");
        attribLabel.setText(l.get(l.L_ATTRIB) + ": ");
        viewButton.setText(l.get(l.L_VIEW));
    }
}
