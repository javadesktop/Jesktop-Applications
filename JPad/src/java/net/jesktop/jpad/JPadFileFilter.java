/*
 * JPadFileFilter.java Sat Jan 20 22:54:02 GMT+05:30 2001
 *
 * Software Designed and Developed by HariKrishna
 * Can be contacted at hari_8@yahoo.com
 *
 * The Author grants you ('Licensee') a non-exclusive, royalty free,
 * license to use, modify and redistribute this software in source and
 * binary code form, provided that this license notice appear on all
 * copies of this software.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR 'AS IS' WITHOUT A WARRANTY OF
 * ANY KIND. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE SOFTWARE
 * OR ITS DERIVATIVES. IN NO EVENT THE AUTHOR WILL BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES.
 *
 * The Author encourages wide distribution of this software for personal or
 * commercial use, provided the above license notice remains intact.
 */

package net.jesktop.jpad;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.filechooser.FileFilter;

public class JPadFileFilter extends FileFilter {

  private static String TYPE_UNKNOWN = "Type Unknown";
  private static String HIDDEN_FILE = "Hidden File";
  private Hashtable filters = null;
  private String description = null;
  private String fullDescription = null;
  private boolean useExtensionsInDescription = true;

  public JPadFileFilter() {
    this((String) null, (String) null);
  }
  public JPadFileFilter(String extension) {
    this(extension,null);
  }
  public JPadFileFilter(String extension, String description) {
    this(new String[] {extension}, description);
  }
  public JPadFileFilter(String[] filters) {
    this(filters, null);
  }
  public JPadFileFilter(String[] filters, String description) {
    this.filters = new Hashtable(filters.length);
    for (int i = 0; i < filters.length; i++)
    addExtension(filters[i]);
    setDescription(description);
  }
  public boolean accept(File f) {
    if(f != null) {
      if(f.isDirectory())
        return true;
      String extension = getExtension(f);
      if(extension != null && filters.get(getExtension(f)) != null)
      return true;
    }
    return false;
  }
  public String getExtension(File f) {
    if(f != null) {
      String filename = f.getName();
      int i = filename.lastIndexOf('.');
      if(i>0 && i<filename.length()-1)
      return filename.substring(i+1).toLowerCase();
    }
    return null;
  }
  public void addExtension(String extension) {
    if(filters == null)
      filters = new Hashtable(5);
    filters.put(extension.toLowerCase(), this);
    fullDescription = null;
    return;
  }
  public String getDescription() {
    if(fullDescription == null) {
      if(description == null || isExtensionListInDescription()) {
        fullDescription = description==null ? "(" : description + " (";
        Enumeration extensions = filters.keys();
        if(extensions != null) {
          fullDescription += "." + (String) extensions.nextElement();
          while (extensions.hasMoreElements())
            fullDescription += ", " + (String) extensions.nextElement();
        }
        fullDescription += ")";
      }
      else
        fullDescription = description;
    }
    return fullDescription;
  }
  public void setDescription(String description) {
    this.description = description;
    fullDescription = null;
  }
  public void setExtensionListInDescription(boolean b) {
    useExtensionsInDescription = b;
    fullDescription = null;
  }
  public boolean isExtensionListInDescription() {
    return useExtensionsInDescription;
  }
}