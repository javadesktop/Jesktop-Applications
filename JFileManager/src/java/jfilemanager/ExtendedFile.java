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
import java.text.*;
import java.math.BigDecimal;
import javax.swing.ImageIcon;

/**
 * Eine Klasse, die einige nützliche zusätzliche Dateioperationen zur Verfügung
 * stellt.
 * @author Thorsten Jens
 * @see java.io.File
 * @version $Id: ExtendedFile.java,v 1.1.1.1 2002-01-11 08:48:53 paul-h Exp $ 
 */
public class ExtendedFile {

    private static final int KB = 1024;
    private static final int MB = KB * KB;

    /** Datei ist eine ZIP- oder JAR-Datei */
    public static final int ZIP = 1;
    /** Datei ist eine HTML-Datei */
    public static final int HTML = 2;
    /** Datei ist eine von Java direkt unterstützte Bilddatei (jpg, gif) */
    public static final int IMAGE = 3;
    /** Datei ist eine Nur-Text-Datei (ASCII-Text, Sourcecode, etc.)*/
    public static final int PLAIN = 4;
    /** Datei ist keine der unterstützen Typen */
    public static final int OTHER = -1;

    /** Die Icons für die entsprechenden Dateitypen.*/
    public static final ImageIcon[] icons = {
        new ImageIcon(MainWindow.class.getResource("folder.gif")), // Ordner
        new ImageIcon(MainWindow.class.getResource("xfm_z.gif")), // ZIP
        new ImageIcon(MainWindow.class.getResource("html.gif")), // HTML
        new ImageIcon(MainWindow.class.getResource("image.gif")), // IMAGE
        new ImageIcon(MainWindow.class.getResource("plain.gif")) // PLAIN
    };

    /**
     * Leerer Konstruktor, da keine Instanzenmethoden
     */
    public ExtendedFile() {}

    /**
     * private, wird nur von Methoden in dieser Klasse aufgerufen werden.
     *  Name trotzdem selbsterklärend. :-)
     *
     */
    private static String getLowercaseExtensionString(File f) {
        return getExtensionString(f).toLowerCase();
    }

    /**
     * Liefert den (hintersten) Dateisuffix einer Datei.
     *
     * @return Dateisuffix der Datei, ohne führenden Punkt (Bsp.: "<b>zip</b>",
     *  "<b>html</b>"
     * @param f Die zu untersuchende Datei.
     * @see #getExtensionType
     *
     */
    public static String getExtensionString(File f) {
        String tmp = f.getName();
        return tmp.substring(tmp.lastIndexOf(".") + 1, tmp.length());
    }


    public static int getExtensionType(File f) {
        String tmp = getLowercaseExtensionString(f);
        int retVal = OTHER;
        if (tmp.equals("zip") || tmp.equals("jar")) {
            retVal = ZIP;
        } else if (tmp.equals("htm") || tmp.equals("html")) {
            retVal = HTML;
        } else if (tmp.equals("gif") || tmp.equals("jpg") || tmp.equals("jpeg") || tmp.equals("jpe")) {
            retVal = IMAGE;
        } else if (tmp.equals("txt") || tmp.equals("c") || tmp.equals("java")) {
            retVal = PLAIN;
        }
        return retVal;
    }

    /**
     * Liefert eine "schöne" Darstellung der Größe einer Datei.
     * @params f Die zu untersuchende Datei
     * @returns Ein String, der die Dateigröße in Bytes, und je nach Größe der
     * Datei, ihre Größe in Kilobytes oder Megabytes in Klammern.
     */
    public static String getBeautifulFileSize(File f) {
        String retVal = new String();
        long size = f.length();
        BigDecimal bd;
        double s = size;
        NumberFormat nf = NumberFormat.getNumberInstance();
        retVal = nf.format(size) + " Bytes"; // Bytes werden immer dargestellt

        // Jetzt wird entschieden, ob zusätzlich MB oder KB dargestellt wird.
        if (size > MB) {
            bd = new BigDecimal(s / MB);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            retVal = retVal + " (" + bd.toString() + " MB)";
        } else if (size > KB) {
            bd = new BigDecimal(s / KB);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            retVal = retVal + " (" + bd.toString() + " KB)";
        }
        return retVal;
    };

    /**
     * @params f Die zu untersuchende Datei
     * @returns <code>true</code>, wenn die Datei anzeigbar ist, sonst <code>false</code>.
     */
    static public boolean isViewable(File f) {
        return (getExtensionType(f) > 0);
    };


    /**
     * Liefert das Icon (16x16) zu einer Datei. Zur Verwendung in der FileTable.
     *
     * @see FileTable
     * @params f Die Datei, für die das Icon gesucht wird.
     * @returns Gibt das passende Icon zurück oder <code>null</code>, falls die
     * Datei nicht unterstützt wird.
     */
    static public ImageIcon getIcon(File f) {
        if (f.isDirectory()) {
            return icons[0];
        } else {
            int type = getExtensionType(f);
            return (type > 0) ? icons[type] : null;
        }
    }

}
