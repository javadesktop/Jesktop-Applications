package jfilemanager;

/**
 * Ein Klasse die LanguageAware ist, verfügt über Methoden zur
 * dynamischen Änderung von Labels oder ähnlichem, wenn eine neue Sprache
 * ausgewählt wird.
 *
 * @see LanguageData
 * @author Thorsten Jens
 * @version $Id: LanguageAware.java,v 1.1.1.1 2002-01-11 08:48:57 paul-h Exp $
 */
public interface LanguageAware {

    /**
     * Die Methode wird aufgerufen, wenn die Sprache geändert werden soll.
     	* @params l Ein LanguageData-Objekt, das die neue Sprachdefinition
      *           enthält.
      * @see LanguageData
      */
    public void changeLanguage(LanguageData l);

}
