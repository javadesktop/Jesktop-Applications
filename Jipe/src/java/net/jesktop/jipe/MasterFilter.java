package net.jesktop.jipe;

import java.io.*;

public class MasterFilter implements FilenameFilter {
    private String extension;
    public MasterFilter(String e) {
        this.extension = e.substring(1);
    }
    public boolean accept(File dir, String fileName){
        if ((fileName.indexOf(".")== -1)||(fileName.endsWith(extension)))
            return true;
        else
            return false;
    }
}
