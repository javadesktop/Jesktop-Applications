/*
 * JPadFileView.java Sat Jan 20 22:54:14 GMT+05:30 2001
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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;
import java.net.URL;


public class JPadFileView extends FileView {
  private Hashtable icons = new Hashtable(5);
  private Hashtable fileDescriptions = new Hashtable(5);
  private Hashtable typeDescriptions = new Hashtable(5);

  public JPadFileView(ClassLoader cl) {
    super();
    putIcon("jxt", new ImageIcon(getResourceURL("resources/JPadJxt.jpg")));
    putIcon("txt", new ImageIcon(getResourceURL("resources/JPadJxt.jpg")));
  }

    private URL getResourceURL(String rscName) {
    java.net.URLClassLoader cl = (java.net.URLClassLoader) this.getClass().getClassLoader();
      return cl.findResource(rscName);
  }

  public String getName(File f) {
    return null;
  }

  public void putDescription(File f, String fileDescription) {
    fileDescriptions.put(fileDescription, f);
  }

  public String getDescription(File f) {
    return (String) fileDescriptions.get(f);
  }

  public void putTypeDescription(String extension, String typeDescription) {
    typeDescriptions.put(typeDescription, extension);
  }
  public void putTypeDescription(File f, String typeDescription) {
    putTypeDescription(getExtension(f), typeDescription);
  }
  public String getTypeDescription(File f) {
    return (String) typeDescriptions.get(getExtension(f));
  }
  public String getExtension(File f) {
    String name = f.getName();
    if(name != null) {
      int extensionIndex = name.lastIndexOf('.');
      if(extensionIndex < 0)
        return null;
      return name.substring(extensionIndex+1).toLowerCase();
    }
    return null;
  }
  public void putIcon(String extension, Icon icon) {
    icons.put(extension, icon);
  }
  public Icon getIcon(File f) {
    Icon icon = null;
    String extension = getExtension(f);
    if(extension != null)
      icon = (Icon) icons.get(extension);
    return icon;
  }
  public Boolean isHidden(File f) {
    String name = f.getName();
    if(name != null && !name.equals("") && name.charAt(0) == '.')
      return Boolean.TRUE;
    else
      return Boolean.FALSE;
  }
  public Boolean isTraversable(File f) {
    if(f.isDirectory())
      return Boolean.TRUE;
    else
      return Boolean.FALSE;
  }
}