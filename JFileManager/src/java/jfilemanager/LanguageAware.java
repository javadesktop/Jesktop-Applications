package jfilemanager;

/**
 * Ein Klasse die LanguageAware ist, verf�gt �ber Methoden zur
 * dynamischen �nderung von Labels oder �hnlichem, wenn eine neue Sprache
 * ausgew�hlt wird.
 *
 * @see LanguageData
 * @author Thorsten Jens
 * @version $Id: LanguageAware.java,v 1.1.1.1 2002-01-11 08:48:57 paul-h Exp $
 */
public interface LanguageAware {

    /**
     * Die Methode wird aufgerufen, wenn die Sprache ge�ndert werden soll.
     	* @params l Ein LanguageData-Objekt, das die neue Sprachdefinition
      *           enth�lt.
      * @see LanguageData
      */
    public void changeLanguage(LanguageData l);

}
