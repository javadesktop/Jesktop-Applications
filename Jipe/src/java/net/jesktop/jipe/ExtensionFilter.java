package net.jesktop.jipe;

import java.io.*;

public class ExtensionFilter implements FilenameFilter {
    private String extension;
    public ExtensionFilter(String e) {
        this.extension = e.substring(1);
    }
    public boolean accept(File dir, String fileName){
        return fileName.endsWith(extension);
    }
}
