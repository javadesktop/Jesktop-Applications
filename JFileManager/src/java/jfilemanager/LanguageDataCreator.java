package jfilemanager;

import java.io.*;
import java.util.Enumeration;

/**
 * Ein einfaches Konsolenprogramm zur Erstellung von Definitionsdateien f�r die
 * LanguageData-Klasse.
 *
 * @see LanguageData
 * @author Thorsten Jens
 * @version $Id: LanguageDataCreator.java,v 1.1.1.1 2002-01-11 08:48:59 paul-h Exp $ 
 */
class LanguageDataCreator {

	/**
  	 * Main() macht hier alles, Sprache einlesen und speichern.
   	 *
   	 */
    public static void main(String[] args) {
        BufferedReader con = new BufferedReader(new InputStreamReader(System.in));
        String lang = new String();
        String key = new String();
        String translation = new String();

        System.out.println("LanguageDataCreator v0.2 [2001-02-14]");
        System.out.println("Dieses Programm geh�rt zum JFileManager Paket");
        System.out.println("von Thorsten Jens und Kai Benjamins.");
        System.out.println();

        System.out.println("Folgende Sprachen sind bereits vorhanden:");
        LanguageData ldtmp = new LanguageData();
        String[] strtmp = ldtmp.getAvailableLanguages();
        for (int i = 0; i < strtmp.length; i++) {
						System.out.println(" " + (i + 1) + ". " + strtmp[i]);
        }
	ldtmp = null;
	strtmp = null;

        System.out.println();
        System.out.println("Wenn sie eine neue Sprachdatei f�r den JFileManager erstellen m�chten,");
        System.out.println("geben sie jetzt bitte den Namen der Sprache ein. Falls nicht, dr�cken sie");
        System.out.println("einfach die <RETURN> Taste, und dieses Programm wird beendet.");
        System.out.println();
        System.out.print("Name der Sprache: ");
        try {
            lang = con.readLine().trim(); // Sofort trim() aufrufen, um umgebende Leerzeichen abzuscheiden.
        }
        catch (IOException iox) {
            System.err.println("Autsch, Exception beim readLine. Das Programm wird beendet.");
            System.err.println("Bitte informieren sie die Autoren von diesem Fehler.");
            System.exit(-1);
        }
        if (lang.equals("")) {
            System.exit(-1);
        }

        checkExistance(lang);

        LanguageData data = new LanguageData(lang);
        System.out.println();
        System.out.println("Eine neue Sprachdatei f�r die Sprache \"" + lang +"\" wird erstellt.");
        System.out.println("Als n�chstes werden ihnen die zu �bersetzenden Begriffe einzeln vorgestellt,");
        System.out.println("die englische Originalversion wird vorgegeben. Falls sie einen Begriff nicht");
        System.out.println("�bersetzen k�nnen oder wollen, dr�cken sie einfach die <RETURN> Taste. In diesem");
        System.out.println("Fall wird der englische Text �bernommen.");
        System.out.println();

        for (Enumeration e = data.propertyNames(); e.hasMoreElements();) {
            key = (String)e.nextElement();
            System.out.println("Original: \"" + data.get(key) + "\"");
            System.out.print("�bersetzung: ");
            try {
                translation = con.readLine().trim();
            } catch (IOException iox) {
                System.err.println("Autsch, Exception beim readLine. Das Programm wird beendet.");
                System.err.println("Bitte informieren sie die Autoren von diesem Fehler.");
                System.exit(-1);
            }
            if (translation.equals("")) {
                System.out.println(" --Der alte Name wird weiterhin benutzt.");
            } else {
                System.out.println(" --Neuer Name \"" + translation + "\" angenommen.");
                data.set(key, translation);
            }
            System.out.println();
        }
        // Alle fertig, speichern?
        System.out.println();
        System.out.println("Es sind jetzt alle �bersetzungen eingegeben.");
        System.out.println("Jetzt k�nnen sie die Sprachdatei speichern, m�chten sie das tun?");
        do {
            System.out.print("Dr�cken sie dann bitte <J> f�r Ja, sonst <N> f�r Nein: ");
            try {
                key =  con.readLine().toLowerCase().trim();
            } catch(IOException iox) {
                System.err.println("Autsch, Exception beim readLine. Das Programm wird beendet.");
                System.err.println("Bitte informieren sie die Autoren von diesem Fehler.");
                System.exit(-1);
            }
        } while (!(key.equals("j")) && !(key.equals("n")));
        if (key.equals("j")) {
            int errorCode = data.store();
            if (errorCode == LanguageData.STORE_OK) {
                System.out.println("Datei wurde gespeichert.");
            } else {
                System.err.println("Datei konnte nicht gespeichert werden.");
                System.err.println("Wurde JFileManager korrekt installiert? Erl�uterungen");
                System.err.println("zur Installation finden sich im Benutzerhandbuch.");
            }
        } else {
            System.out.println("Datei wurde nicht gespeichert.");
        }
    }

    private static void checkExistance(String str) {
	LanguageData ldtmp = new LanguageData();
        String[] strtmp = ldtmp.getAvailableLanguages();

        for (int i = 0; i < strtmp.length; i++) {
		if (strtmp[i].equals(str)) {
        		System.err.println("Diese Sprache existiert bereits. Bitte w�hlen sie einen anderen Namen.");
             		System.exit(-1);
        	}
        }
    }

}
