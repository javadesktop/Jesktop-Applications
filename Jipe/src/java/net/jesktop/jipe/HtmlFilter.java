// HtmlFilter.java

package net.jesktop.jipe;

import java.io.*;


/** FileFilter class to display .htm and .html files only
 * in a FileChooser dialog.
 *
 */
class HtmlFilter extends SpecialFileFilters
{

    /**      */
    public boolean accept(File f)
    {
        boolean accept = super.accept(f);
        if (!accept)
        {
            String suffix = getSuffix(f);
            if (suffix != null)
                accept = suffix.startsWith("htm");
        }
        return accept;
    }
    /** Return the text for this type of filter.
     *
     */
    public String getDescription()
    {
        return "HTML Files(*.htm *.html)";
    }
}

