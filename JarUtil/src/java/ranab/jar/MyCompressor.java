
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.io.*;

import ranab.util.*;


/**
 * This is tha abstract base class of all the file
 * compressors.
 */
public abstract class MyCompressor {

    protected File mCompressedFile;
    protected MyJarObserverContainer mObserverCont;

    /**
     * constructor
     * @file fl the new compressed file
     */
    public MyCompressor(File fl) {
        mCompressedFile = fl;
        mObserverCont = new MyJarObserverContainer();
    }

    /**
     * add observer
     */
    public void addObserver(MyJarObserver obsr) {
        mObserverCont.addObserver(obsr);
    }

    /**
     * remove observer
     */
    public void removeObserver(MyJarObserver obsr) {
        mObserverCont.removeObserver(obsr);
    }

    /**
     * add a file/directory
     */
    public void addFile(File file, boolean recursion, boolean pathInfo, int level) {
        addFile(file, file.getParentFile(), recursion, pathInfo, true, level);
    }

    /**
     * actual work horse
     */
    private void addFile(File file, File parent, boolean recursion, boolean pathInfo,
                         boolean firstTime, int level) {

        // if not directory - write now
        if (!file.isDirectory()) {

            // get entry name
            String filePath = file.getAbsolutePath();
            String dirPath = parent.getAbsolutePath();
            String entryName = file.getName();

            if (pathInfo && filePath.startsWith(dirPath)) {
                entryName = filePath.substring(dirPath.length());
            }

            setCompressionLevel(level);
            addFile(file,
                    MyStringUtils.replaceString(entryName, System.getProperty("file.separator"),
                                                "/"));
        } else if (firstTime || recursion) {
            File fileList[] = file.listFiles();

            for (int i = 0; i < fileList.length; i++) {
                addFile(fileList[i], parent, recursion, pathInfo, false, level);
            }
        }
    }

    /**
     * open a new compressed file
     */
    abstract public void open() throws Exception;

    /**
     * add a file
     *
     * @param file the file to be added
     * @param name the entry name (usually the filename)
     */
    abstract protected void addFile(File file, String name);

    /**
     * set the compression level
     */
    abstract protected void setCompressionLevel(int level);

    /**
     * close the compressed file
     */
    abstract public void close();

    /**
     * get the compressed filename
     */
    public String toString() {
        return mCompressedFile.getAbsolutePath();
    }
}
