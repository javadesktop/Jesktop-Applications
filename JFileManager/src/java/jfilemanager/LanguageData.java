package jfilemanager;

import java.util.Properties;
import java.util.Enumeration;
import java.io.*;

/**
 * LanguageData enthält die Methoden, um eine angepasste Sprachdatei für JFileManager zu erstellen.
 * Diese Dateien enthalten allen Text der Menüeintrage, Label usw. als Übersetzung.
 *
 * @author Thorsten Jens
 * @author Kai Benjamins
 * @since 0.1
 *
 * @version $Id: LanguageData.java,v 1.1.1.1 2002-01-11 08:48:59 paul-h Exp $ 
 */
public class LanguageData {

    // Konstanten

    // - Rückgabewerte von store
    public final static int STORE_OK = 0;
    public final static int STORE_ERR_NONAME = 1;
    public final static int STORE_ERR_IOEXCEPTION = 2;
    public final static int STORE_ERR_FILEEXISTS = 3;


    /**
     * Array, bei dem der Rückgabewert von <code>store()</code>, als Index
     * eingesetzt, einen String mit der Fehlermeldung zurückgibt.
     * @see #store
     */
    public final static String[] storeErrors = {"Kein Fehler",
            "Kein Name angegeben",
            "IOException",
            "Date existiert bereits"};

    private final static File baseDir = new File(System.getProperty("user.home") +
                                        System.getProperty("file.separator") +
                                        ".jfilemanager" + System.getProperty("file.separator"));


    public final static int LOAD_FILE_NOT_FOUND = 1;
    public final static int LOAD_OK = 0;

    // - Die einzelnen
    public final static String L_NAME = "NAME";
    public final static String L_SIZE = "SIZE";
    public final static String L_DATE = "DATE";
    public final static String L_FILE = "FILE";
    public final static String L_EXIT = "EXIT";
    public final static String L_ATTRIB = "ATTRIB";
    public final static String L_HELP = "HELP";
    public final static String L_VIEW = "VIEW";
    public final static String L_EXEC = "EXEC";
    public final static String L_DIR = "DIR";
    public final static String L_ABOUT = "ABOUT";
    public final static String L_OPTIONS = "OPTIONS";
    public final static String L_ENTRIES = "ENTRIES";
    public final static String L_LINES = "LINES";
    public final static String L_SEARCH_FOR_FILE = "SEARCHFORFILE";
    public final static String L_VIEW_SOURCE = "VIEWSOURCE";
    public final static String L_LANGUAGE = "LANGUAGE";
    public final static String L_FILE_ASSOCIATIONS = "FILEASSOCIATIONS";
    public final static String L_FILENAME = "FILENAME";
    public final static String L_STARTSEARCH = "STARTSEARCH";
    public final static String L_PREFERENCES = "PREFERENCES";
    public final static String L_DIRBACK = "DIRBACK";
    public final static String L_HOMEDIR = "HOMEDIR";
    public final static String L_DELETE_FILES = "DELETEFILES";
    public final static String L_COPY_FILES = "COPYFILES";
    public final static String L_MOVE_FILES = "MOVEFILES";
    public final static String L_SELECT_LANGUAGE = "SELECTLANGUAGE";
    public final static String L_LINK_DETAIL_SIZES = "LINKDETAILSIZES";
    public final static String L_OTHER_OPTIONS = "OTHEROPTIONS";
    public final static String L_OK = "OK";
    public final static String L_CANCEL = "CANCEL";
    public final static String L_ERROR = "ERROR";
    public final static String L_FILE_ERROR = "FILEERROR";
    public final static String L_SEARCH_WHERE = "SEARCHWHERE";
    public final static String L_FILES_FOUND = "FILESFOUND";
    public final static String L_NO_FILENAME = "NOFILENAME";
    public final static String L_NO_PERMISSION = "NOPERMISSION";
    public final static String L_COPY_DIALOG = "COPYDIALOG";
    public final static String L_COPY_DIALOG_COPYTO = "COPYDIALOGCOPYTO";
    public final static String L_COPY_DIALOG_COPY = "COPYDIALOGCOPY";
    public final static String L_MOVE_DIALOG = "MOVEDIALOG";
    public final static String L_MOVE_DIALOG_MOVETO = "MOVEDIALOGMOVETO";
    public final static String L_MOVE_DIALOG_MOVE = "MOVEDIALOGMOVE";
    public final static String L_FILE2 = "FILE";
    public final static String L_FILE_EXISTS_OVERWRITE = "FILEEXISTSOVERWRITE";
    public final static String L_FILE_EXISTS = "FILEEXISTS";
    public final static String L_FILES_COPIED = "FILESCOPIED";
    public final static String L_COPY_ERROR = "COPYERROR";
    public final static String L_DONE = "DONE";
    public final static String L_FILES_MOVED = "FILESMOVED";
    public final static String L_MOVE_ERROR = "MOVEERROR";
    public final static String L_RENAME_ERROR = "RENAMEERROR";
    public final static String L_NO_WRITE_PERMISSION = "NOWRITEPERMISSION";
    public final static String L_DELETE_DIALOG = "DELETEDIALOG";
    public final static String L_DELETE = "DELETE";
    public final static String L_DELETE_ERROR = "DELETEERROR";
    public final static String L_FILES_DELETED = "FILESDELETED";
    public final static String L_OPEN_WITH = "OPENWITH";
    public final static String L_FILE3 = "FILE3";
    public final static String L_BROWSE = "BROWSE";
    public final static String L_EXEC_ERROR = "EXECERROR";
    // Felder

    private String name;
    private Properties items;

    // Konstruktoren

    /**
     * Standardkonstruktor ohne Parameter. Initialisiert die lokalen Vaiablen. Der Name
     * der Sprache muss noch durch die Funktion setName() gesetzt werden.
     * @see #setName
     */
    public LanguageData() {
        this.name = new String();
        defaultInit();
    }


    /**
     * Dieser Konstruktor erhält als Parameter die Sprache.
     *
     * @param s Der Name der Sprache, die diese Instanz darstellt.
     */
    public LanguageData(String s) {
        this.name = s;
        defaultInit();
    }


    // Methoden

    /**
     * Setzt alle Felder auf Standardwerte. Die Standardwerte sind die englischen
     * Entprechungen der einzelnen zu ersetzenden Felder. Wird nur vom Konstruktor
     * aufgerufen -> private
     */
    private void defaultInit() {
        items = new Properties();
				if (!baseDir.exists()) {
      		if (installMe()) {
						System.err.println("OK, let's go!");
          } else {
            System.err.println("Hmm. Error during creation of config-directory :-((");
            System.exit(-1);
          }
      	}

     		load("Deutsch");
    }


    /**
     * Setzt den Namen der Sprache. Dies muss geschehen (entweder durch diese Methode
     * oder den geeigneten Konstruktor), bevor die Sprache gespeichert werden kann.
     *
     * @param s Der Name der Sprache
     * @see #LanguageData(String)
     */
    protected void setName(String s) {
        // Sprache setzen
        this.name = s;
    }


    /**
     * Liefert die Anzahl der Keys im Objekt zurück.
     *
     * @returns die Anzahl der Keys.
     */
    protected int getNumOfKeys() {
        return items.size();
    }


    /**
     * Verbindet einen Identifikationsstring mit dem Namen in der jeweiligen Sprache, z.B.<br>
     * <code>set(LanguagaData.L_FILENAME, "Dateiname");</code><br>
     * Der Identifikationsstring sollte eine der L_* Konstaten von LanguageData sein.
     *
     * @param identifier lsefj
     *
     */
    protected void set(String identifier, String value) {
        items.setProperty(identifier, value);
    }

    /**
     * Liefert den zum String gehörenden Wert zurück.
     *
     * @param identifier Eine der L_* Konstanten
     * @returns den Wert des <code>identifier</code>s oder "UNDEFINED", falls der
     * <code>identifier</code> keinen gültigen Wert darstellt. Wenn der <code>identifier</code>
     * nicht explizit mit <code>set()</code> initialisiert oder aus einer Date geladen wurde,
     * werden die Standard-Werte verwendet (Standard-Sprache ist Englisch).
     * @see #set
     *
     */
    protected String get(String identifier) {
        // liefert "UNDEFINED", falls die Property nicht exsitiert
        return items.getProperty(identifier, "UNDEFINED");
    }

    /**
     * Liefert alle verfügbaren Keys zurück.
     *
     * @returns Ein <code>java.util.Enumeration</code> Objekt, mit dem alle Keys durchiteriert
     * werden können.
     */
    protected Enumeration propertyNames() {
        return items.propertyNames();
    }


    /**
     * Speichert die Daten als java.util.Properties-Datei. Der Name wird zusammengesetzt
     * aus dem Namen der Sprache (siehe setName) und der Endung ".langdata". Das Standard-
     * Verzeichnis für die Datei ist $USER/.jfilemanager/, wobei $USER für das Verzeichnis
     * steht, das von <code>System.getProperty("user.home")</code> zurückgeliefert wird.
     *
     * @returns Gibt das Ergebnis des Schreibens zurück, durch storeErrors[] kann die
     * Ursache festgestellt werden.
     *
     * @see #setName(String)
     * @see #storeErrors
     */
    protected int store() {
        if ((this.name.equals("")) || (this.name == null)) {
            return STORE_ERR_NONAME;
        }
        //        File fileToStore = new File(System.getProperty("user.home") +
        //                                    System.getProperty("file.separator") +
        //                                    ".jfilemanager" + System.getProperty("file.separator") +
        //                                    this.name + ".langdata");
        File fileToStore = new File(baseDir, this.name + ".langdata");

        if (fileToStore.exists()) {
            return STORE_ERR_FILEEXISTS;
        }
        try {
            this.items.store(new FileOutputStream(fileToStore), "LanguageData Datei für JFileManager" );
        } catch(IOException ioe) {
            return STORE_ERR_IOEXCEPTION;
        }
        return STORE_OK;
    }


    protected String[] getAvailableLanguages() {
        //        File baseDir = new File(System.getProperty("user.home") +
        //                                System.getProperty("file.separator") +
        //                                ".jfilemanager" + System.getProperty("file.separator"));
        String[] ret = baseDir.list(new FilenameFilter() {
                                        public boolean accept(File f, String name) {
                                            return name.endsWith(".langdata");
                                        }
                                    }
                                   );
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ret[i].substring(0, ret[i].indexOf(".langdata"));
        }
        return ret;
    }


    private boolean installMe() {
	    String langFileData[] = {"#LanguageData Datei für JFileManager\n#Fri Feb 02 13:57:04 GMT+01:00 2001\n" +
				"EXEC=Execute\nEXIT=Exit\nMOVEFILES=Move or rename file(s)\nFILEASSOCIATIONS=File associations\n" +
				"HELP=Help\nENTRIES=Entries\nCANCEL=Cancel\nDATE=Date\nFILE=File\nSEARCHFORFILE=Search for file\n" +
				"ERROR=Error\nOK=Ok\nPREFERENCES=Preferences\nNAME=Name\nVIEWSOURCE=View sourcecode\nLANGUAGE=Language\n" +
				"SELECTLANGUAGE=Select language\nDELETEFILES=Delete file(s)\nATTRIB=Attributes\nCOPYFILES=Copy files(s)\n" +
				"LINES=Lines\nDIR=Directory\nSTARTSEARCH=Start search\nFILENAME=Filename\nOPTIONS=Options\nABOUT=About\n" +
				"FILEERROR=Error opening file\nVIEW=View\nOTHEROPTIONS=Other options\nSIZE=Size\nHOMEDIR=Home directory\n" +
				"DIRBACK=Directory back\nLINKDETAILSIZES=Link sizes of Detail Views\nFILESFOUND=files found\nSEARCHWHERE=Search where\n" +
    		"NOFILENAME=No filename given!\nNOPERMISSION=No read permission\nCOPYDIALOG=Copy file(s)\nCOPYDIALOGCOPY=copy\nCOPYDIALOGCOPYTO=to\n" +
                "MOVEDIALOG=Move files(s)\nMOVEDIALOGMOVE=move\nMOVEDIALOGMOVETO=to\nFILE=File\nFILEEXISTSOVERWRITE=exists! Overwrite?\nCOPYERROR=could not be copied\n"+
                "DONE=Done!\nFILEEXISTS=File exists!\nFILESCOPIED=File(s) copied\nFILESMOVED=File(s) moved\nMOVEERROR=could not be moved!\nRENAMEERROR=Select only one file to rename!\n"+
                "NOWRITEPERMISSION=No write permission\nDELETEDIALOG=Delete file(s)\nDELETE=Do you want to delete the following file(s)?\nDELETEERROR=could not be deleted!\n"+
                "FILESDELETED=File(s) deleted\nOPENWITH=open with\nBROWSE=Browse\nFILE3=Datei\nEXECERROR=could not be executed.\n",
     	  "#LanguageData Datei für JFileManager\n#Fri Feb 02 13:57:04 GMT+01:00 2001\n" +
  			"EXEC=Ausf\u00FChren\nEXIT=Beenden\nMOVEFILES=Datei(en) umbenennen oder verschieben\nFILEASSOCIATIONS=Dateiassoziationen\n" +
  			"HELP=Hilfe\nENTRIES=Eintr\u00E4ge\nCANCEL=Abbrechen\nDATE=Datum\nFILE=Datei\nSEARCHFORFILE=Datei suchen\nERROR=Fehler\n" +
		 	  "OK=Ok\nPREFERENCES=Einstellungen\nNAME=Name\nVIEWSOURCE=Quellcode ansehen\nLANGUAGE=Sprache\nSELECTLANGUAGE=Sprache ausw\u00E4hlen\n" +
  			"DELETEFILES=Datei(en) l\u00F6schen\nATTRIB=Attribute\nCOPYFILES=Date(en) kopieren\nLINES=Zeilen\nDIR=Verzeichnis\n" +
  			"STARTSEARCH=Suche starten\nFILENAME=Dateiname\nOPTIONS=Optionen\nABOUT=\u00DCber\nFILEERROR=Fehler beim \u00D6ffnen der Datei\n" +
  			"VIEW=Ansehen\nOTHEROPTIONS=Andere Optionen\nSIZE=Gr\u00F6\u00DFe\nHOMEDIR=Heimatverzeichnis\nDIRBACK=Verzeichnis zur\u00FCck\n" +
  			"LINKDETAILSIZES=Gr\u00F6\u00DFe der Detailanzeigen immer gleich\nSEARCHWHERE=Wo suchen\nFILESFOUND=Dateien gefunden\n" +
     		"NOFILENAME=Kein Dateiname angegeben!\nNOPERMISSION=Keine Leseberechtigung\nCOPYDIALOG=Datei(en) kopieren\nCOPYDIALOGCOPY=Kopieren\nCOPYDIALOGCOPYTO=nach\n"+
                "MOVEDIALOG=Datei(en) verschieben\nMOVEDIALOGMOVE=Verschieben\nMOVEDIALOGMOVETO=nach\nFILE=Die Datei\nFILEEXISTSOVERWRITE=existiert bereits! Überschreiben?\n"+
                "COPYERROR=konnte nicht kopiert werden!\nDONE=Fertig!\nFILEEXISTS=Datei existiert!\nFILESCOPIED=Datei(en) kopiert\nFILESMOVED=Datei(en) verschoben\n"+
                "MOVEERROR= konnte nicht verschoben werden!\nRENAMEERROR=Beim Umbenennen darf nur eine Datei markiert sein!\nDELETEDIALOG=Datei(en) löschen\nDELETE=Sollen die folgenden Dateien gelöscht werden?\nDELETEERROR=konnte nicht gelöscht werden!\n"+
                "NOWRITEPERMISSION=Keine Schreibberechtigung\nFILESDELETED=Datei(en) gelöscht\nOPENWITH=öffnen mit\nBROWSE=Durchsuchen\nFILE3=File\nEXECERROR=konnte nicht ausgeführt werden.\n",
				"#LanguageData Datei für JFileManager\n#Mon Feb 19 01:02:13 GMT+01:00 2001\nMOVEDIALOG=Dateien schuiven\nOTHEROPTIONS=ander optie\n" +
				"ATTRIB=Attributen\nSELECTLANGUAGE=Kies Taal\nNOPERMISSION=Geen Lezberechtiging\nBROWSE=Doorzoeken\nDELETEDIALOG=Dateien delgen\n" +
				"SEARCHFORFILE=Dateien zoeken\nDELETEFILES=Dateien delgen\nDATE=Datum\nENTRIES=Tekeningen\nDELETEERROR=kun niet uitgeblusst worden!\n" +
				"DELETE=Zullen de aanstaand Dateien uitgeblussen worden?\nSEARCHWHERE=Waar zoeken\nPREFERENCES=Instellingen\nDIRBACK=Catalogus terug\n" +
				"FILEEXISTS=Datei bestaad!\nMOVEDIALOGMOVETO=naar\nFILEASSOCIATIONS=Dateiassociatie\nCOPYDIALOGCOPY=Kopieren\nLANGUAGE=Taal\n" +
				"VIEW=Aanzien\nMOVEFILES=Dateien benoemen of uiteen schuiven\nOK=Ok\nEXIT=Be\u2030indigen\nMOVEERROR=kun niet uiteen geschuivt worden!\n" +
				"DIR=Register\nCOPYDIALOGCOPYTO=naar\nNOFILENAME=Geen Dateinaam aangegeven!\nCOPYFILES=Dateien kopieren\nCOPYERROR=kun niet kopiert worden!\n" +
				"MOVEDIALOGMOVE=Uiteen schuiven\nSIZE=Grootte\nSTARTSEARCH=Start zoektocht\nCOPYDIALOG=Dateien kopieren\nABOUT=Over\n" +
				"LINKDETAILSIZES=Grootte van de detailaangifte altijd gelijk\nNOWRITEPERMISSION=Geen schrijvpermissie\nDONE=Klaar!\nVIEWSOURCE=Bekijk kwelcode\n" +
				"FILESDELETED=Dateien gedelgt\nHOMEDIR=Homedirectory\nNAME=Naam\nERROR=Fout\nFILENAME=Dateinaam\nEXECERROR=kun niet bewerkstelligt worden.\n" +
				"CANCEL=Afbreken\nOPENWITH=Openmaken met\nLINES=Lijnen\nFILESMOVED=Dateien uiteen geschuivt\nFILE=De Datei\nFILESCOPIED=Dateien kopiert\n" +
				"FILESFOUND=Dateien gevonden\nRENAMEERROR=Aan het benoemen mag maar een datei aangeduid worden!\nFILEEXISTSOVERWRITE=bestaad al! Overschrijven?\n" +
				"EXEC=Uitvoeren\nHELP=Help\nFILE3=Datei\nFILEERROR=Fout bij het openen von de Datei\nOPTIONS=Opties\n"};

      File langFile[] = new File[3];
      langFile[0] = new File(baseDir, "English.langdata");
      langFile[1] = new File(baseDir, "Deutsch.langdata");
      langFile[2] = new File(baseDir, "Nederlands.langdata");
      PrintWriter output;

			System.err.println("Hey! I haven't run yet. Let's install me.");
      if (baseDir.mkdir()) {
				for (int i = 0; i < langFile.length; i++) {
      		try {
          	langFile[i].createNewFile();
           	output = new PrintWriter(new FileWriter(langFile[i]));
            output.println(langFileData[i]);
            output.close();
            System.out.println("Successfully installed" + langFile[i]);
          } catch (Exception ex) {
            System.err.println("Ooops. Exception during creation of \"" + langFile[i] + "\".");
						ex.printStackTrace(System.err);
            return false;
          }
      	}
      } else {
        System.err.println("Ooops. I could not create the directory \".jfilemanager\".");
        return false;
      }
      return true;
    }

    /**
     * Lädt eine neue Sprache aus einer Datei und setzt alle Variablen
     * entsprechend.
     *
     * @param s Der Name der Sprache <i>(ohne ".langdata"!)</i>
     * @return entweder <code>LOAD_FILE_NOT_FOUND</code> oder <code>LOAD_OK</code>
     */
    protected int load(String s) {
        File fileToLoad = new File(baseDir, s + ".langdata");
        if (!fileToLoad.exists()) {
            return LOAD_FILE_NOT_FOUND;
        } else {
            try {
                this.items.load(new FileInputStream(fileToLoad));
            } catch (IOException ioe) {
                return LOAD_FILE_NOT_FOUND;
            }
            return LOAD_OK;
        }
    }
}
