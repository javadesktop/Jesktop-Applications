/**
 * PropertiesManager.java - Sets properties within the Jipe Environment.  Copyright
 * (c) 1996-2000 Steve Lawson.  E-Mail steve@e-i-s.co.uk Latest version can be
 * found at http://e-i-s.co.uk/jipe
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.jesktop.jipe;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class PropertiesManager
{

    Jipe jipe;
    private static Properties p;
    static String JAVA;
    static String BROWSER;
    static String APPLETVIEWER;
    static String DEBUG;
    static String COMPILER;
    static String DECOMPILER;
    static String CLASSPATH;
    static String OUTPUTDIR;
    static String FONTSTYLE;
    static String CONSOLEFONT;
    static String RFILES;
    static String RPROJECTS;
    static Color CONSOLEBCOLOUR, CONSOLEFCOLOUR;
    static int CONSOLEFONTSIZE,FONTSIZE,TABSIZE,XPOS,YPOS,XSIZE,YSIZE,DIV1,DIV2,DIV3;
    static boolean AUTOINDENT,SHOWLEFTPANEL,SHOWSTATUSBAR,SHOWBOTTOMPANEL,SHOWTOOLBAR1,SHOWTOOLBAR2;
    public static int[] CCOLOR;
    public static int[] JCOLOR;
    public static int[] HCOLOR;
    public static int[] JSCOLOR;
    public static int[] JSPCOLOR;

    static String LOOKANDFEEL;
    static String JAVAOPTIONSTRING;
    static String JAVADOCS;
    String path1,path2,path3,path4,path5,path6,path7, path8;
    static String CColors,JColors,JSColors,JSPColors,HTMLColors;
    StringParser parser2,parser3;
    public PropertiesManager(Jipe parent)
    {
        jipe = parent;
        try
        {
            p = new Properties();
            String userdir = System.getProperty("user.home");
            userdir = userdir + System.getProperty("file.separator") +
                      "jipe.properties";
            File file = new File(userdir);
            if (file.exists())
                p.load(new FileInputStream(userdir));
            else
            {
                InputStream is = getClass().getResourceAsStream(
                                     "resources/jipe.properties");
                p.load(is);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

        String vers = p.getProperty("VERSION");
    System.out.println("Jipe version "+vers);
        if (vers==null || vers.compareTo("0.93") < 0)
        {
          System.out.println("Your jipe.properties file in "+System.getProperty("user.home")+
          "\nisn't compatible with this version of Jipe.\nYou need to delete this file for Jipe to work again.");
          System.exit(0);
      }

        JAVA = p.getProperty("JAVA");
        BROWSER = p.getProperty("BROWSER");
        APPLETVIEWER = "appletviewer";
        COMPILER = p.getProperty("COMPILER");
        DEBUG=p.getProperty("DEBUG");
        DECOMPILER=p.getProperty("DECOMPILER");
        JAVADOCS=p.getProperty("JAVADOCS");
        CLASSPATH=p.getProperty("CLASSPATH");
        OUTPUTDIR=p.getProperty("OUTPUTDIR");
        JAVAOPTIONSTRING=p.getProperty("OPTIONSTRING");
        FONTSTYLE = p.getProperty("FONT");
        FONTSIZE = Integer.parseInt(p.getProperty("FONTSIZE"));
        CONSOLEFONT = p.getProperty("CONSOLEFONT");
        CONSOLEFONTSIZE = Integer.parseInt(p.getProperty("CONSOLEFONTSIZE"));
        CONSOLEFCOLOUR=parseColor(p.getProperty("CONSOLEFCOLOUR"));
        CONSOLEBCOLOUR=parseColor(p.getProperty("CONSOLEBCOLOUR"));

        TABSIZE = Integer.parseInt(p.getProperty("TABSIZE"));
        AUTOINDENT = setState(p.getProperty("AUTOINDENT"));
        LOOKANDFEEL = p.getProperty("LOOKFEEL");
        XPOS = Integer.parseInt(p.getProperty("XPOS"));
        YPOS = Integer.parseInt(p.getProperty("YPOS"));
        XSIZE = Integer.parseInt(p.getProperty("XSIZE"));
        YSIZE = Integer.parseInt(p.getProperty("YSIZE"));
        DIV1 = Integer.parseInt(p.getProperty("DIV1"));
        DIV2 = Integer.parseInt(p.getProperty("DIV2"));
        DIV3 = Integer.parseInt(p.getProperty("DIV3"));
        CColors = p.getProperty("CCOLORS");
        JColors = p.getProperty("JCOLORS");
        JSColors = p.getProperty("JSCOLORS");
        JSPColors = p.getProperty("JSPCOLORS");
        HTMLColors = p.getProperty("HTMLCOLORS");
        CCOLOR = new int[10];
        JCOLOR = new int[10];
        HCOLOR = new int[10];
        JSCOLOR = new int[10];
        JSPCOLOR = new int[10];
        parser2 = new StringParser();
        CCOLOR = parser2.getColorCode(CColors);
        JCOLOR = parser2.getColorCode(JColors);
        JSCOLOR = parser2.getColorCode(JSColors);
        HCOLOR = parser2.getColorCode(HTMLColors);
        JSPCOLOR = parser2.getColorCode(JSPColors);
        SHOWLEFTPANEL=setState(p.getProperty("SHOWLEFTPANEL"));
    SHOWSTATUSBAR=setState(p.getProperty("SHOWSTATUSBAR"));
    SHOWBOTTOMPANEL=setState(p.getProperty("SHOWBOTTOMPANEL"));
    SHOWTOOLBAR1=setState(p.getProperty("SHOWTOOLBAR1"));
    SHOWTOOLBAR2=setState(p.getProperty("SHOWTOOLBAR2"));
        RFILES=p.getProperty("RFILES");
        RPROJECTS=p.getProperty("RPROJECTS");


    }

    /**
     * Sets the state of a given boolean switch to true or false
     * @param vers - The String value of boolean that need to be converted
     */

     boolean setState(String vers)
     {
    if(vers.equals("true"))return true;
      else return false;
     }

    /**
     * Converts a color object to its hex value. The hex value
     * prefixed is with #, for example #ff0088.
     * @param c The color object
     */

    public static String getColorHexString(Color c)
    {
        String colString = Integer.toHexString(c.getRGB() & 0xffffff);
        return "#000000".substring(0, 7 - colString.length()).concat(colString);
    }

    /**
     * Converts a hex color value prefixed with #, for example #ff0088.
     * @param name The color value
     */

    public static Color parseColor(String name)
    {
        if (name == null)
            return Color.black;
        else if (name.startsWith("#"))
        {
            try
            {
                return Color.decode(name);
            }
            catch (NumberFormatException nfe)
            {
                return Color.black;
            }
        }
        return Color.black;
    }

    void SaveOptions()
    {
        try
        {
            String userdir = System.getProperty("user.home");
            userdir = userdir + System.getProperty("file.separator") +
                      "jipe.properties";
            FileWriter fs = new FileWriter(userdir);
            String separator = System.getProperty("line.separator");
            fs.write("# Jipe Properties." + separator);
            fs.write("# Steve Lawson 22-10-00" + separator);
            fs.write("VERSION=0.93"+ separator + separator);
            if((System.getProperty("file.separator")).equals("\\")){
                StringParser parser=new StringParser();
                path8=parser.ModifyPath(DECOMPILER);
                fs.write("DECOMPILER "+path8 + separator);
                path7=parser.ModifyPath(JAVADOCS);
                fs.write("JAVADOCS "+path7 + separator);
                path6=parser.ModifyPath(DEBUG);
                fs.write("DEBUG "+path6 + separator);
                path5=parser.ModifyPath(JAVA);
                fs.write("JAVA "+path5 + separator);
                path4=parser.ModifyPath(BROWSER);
                fs.write("BROWSER "+path4 + separator);
                path3=parser.ModifyPath(COMPILER);
                fs.write("COMPILER "+path3 + separator);
                path2=parser.ModifyPath(CLASSPATH);
                fs.write("CLASSPATH "+path2 + separator);
                path1=parser.ModifyPath(OUTPUTDIR);
                fs.write("OUTPUTDIR "+path1 + separator);

            }
            else {
                fs.write("DECOMPILER " + DECOMPILER + separator);
                fs.write("JAVADOCS " + JAVADOCS + separator);
                fs.write("DEBUG " + DEBUG + separator);
                fs.write("JAVA " + JAVA + separator);
                fs.write("BROWSER=" + BROWSER + separator);
                fs.write("COMPILER " + COMPILER + separator);
                fs.write("CLASSPATH " + CLASSPATH + separator);
                fs.write("OUTPUTDIR " + OUTPUTDIR + separator);
            }
            fs.write("OPTIONSTRING "+JAVAOPTIONSTRING + separator);
            fs.write(separator + "#Set Font Style" + separator);
            fs.write(
                "##Valid values are normally - SansSerif, Dialog, Serif, Monospaced or DialogInput" +
                separator);
            fs.write("FONT=" + FONTSTYLE + separator);
            fs.write("FONTSIZE=" + FONTSIZE + separator);
            fs.write("TABSIZE=" + TABSIZE + separator);
      fs.write("AUTOINDENT="+AUTOINDENT + separator);
            fs.write("# Console Options" + separator);
            fs.write("CONSOLEFONT="+CONSOLEFONT+separator);
            fs.write("CONSOLEFONTSIZE="+CONSOLEFONTSIZE+separator);
            fs.write("CONSOLEFCOLOUR="+getColorHexString(CONSOLEFCOLOUR)+separator);
            fs.write("CONSOLEBCOLOUR="+getColorHexString(CONSOLEBCOLOUR)+separator+separator);

            fs.write("#Colors for Syntax highlighting"+ separator);
            parser3 = new StringParser();
            fs.write("CCOLORS="+ parser3.makeString(CCOLOR)+separator);
            fs.write("JCOLORS="+ parser3.makeString(JCOLOR)+separator);
            fs.write("JSCOLORS="+parser3.makeString(JSCOLOR)+separator);
            fs.write("HTMLCOLORS="+ parser3.makeString(HCOLOR)+separator);
            fs.write("JSPCOLORS="+ parser3.makeString(JSPCOLOR)+separator+separator);

            fs.write("#Set the Look & Feel" + separator);
            fs.write(
                "#Valid values are normally - Metal, CDE/Motif, or Windows" +
                separator);
            fs.write("LOOKFEEL=" + LOOKANDFEEL + separator);
            fs.write("XPOS="+ jipe.getLocation().x + separator);
            fs.write("YPOS="+ jipe.getLocation().y + separator);
            fs.write("XSIZE="+ jipe.getSize().width + separator);
            fs.write("YSIZE="+ jipe.getSize().height + separator);
            fs.write("DIV1="+ jipe.sp.getDividerLocation()+separator);
            if(SHOWLEFTPANEL==true)fs.write("DIV2="+ jipe.sp2.getDividerLocation()+separator);
            else fs.write("DIV2="+ DIV2 +separator);
            if(SHOWBOTTOMPANEL==true)fs.write("DIV3="+ jipe.sp3.getDividerLocation()+separator+ separator);
            else fs.write("DIV3="+ DIV3 +separator);
            fs.write("SHOWLEFTPANEL="+SHOWLEFTPANEL+separator);
            fs.write("SHOWSTATUSBAR="+SHOWSTATUSBAR+separator);
            fs.write("SHOWBOTTOMPANEL="+SHOWBOTTOMPANEL+separator);
            fs.write("SHOWTOOLBAR1="+SHOWTOOLBAR1+separator);
            fs.write("SHOWTOOLBAR2="+SHOWTOOLBAR2+separator+ separator);
            if((System.getProperty("file.separator")).equals("\\")){
                StringParser parser=new StringParser();
            fs.write("RFILES="+ parser.ModifyPath(RFILES) + separator);
            fs.write("RPROJECTS="+ parser.ModifyPath(RPROJECTS) + separator);
        }
            else{
              fs.write("RFILES="+ RFILES + separator);
            fs.write("RPROJECTS="+ RPROJECTS + separator);
        }

            fs.close();
        }
        catch (Exception err){}
    }
}
