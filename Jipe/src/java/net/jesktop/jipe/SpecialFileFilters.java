package net.jesktop.jipe;

import java.io.*;


abstract class SpecialFileFilters extends javax.swing.filechooser.FileFilter
{
    public String getSuffix(File f)
    {
        String s = f.getPath();
        String suffix = null;
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1)
            suffix = s.substring(i + 1).toLowerCase();
        return suffix;
    }
    public boolean accept(File f)
    {
        return f.isDirectory();
    }
}

