
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;


/**
 * jar file compression class
 */
public class MyJarCompressor extends MyCompressor {

    private JarOutputStream mJos;
    private int miCurrentCount;

    /**
     * Constructor MyJarCompressor
     *
     *
     * @param jarfl
     *
     */
    public MyJarCompressor(File jarfl) {

        super(jarfl);

        miCurrentCount = 0;
    }

    /**
     * open new jar file
     */
    public void open() throws Exception {

        try {
            mJos = new JarOutputStream(new FileOutputStream(mCompressedFile));

            mObserverCont.start();
            mObserverCont.setCount(0);
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());

            throw ex;
        }
    }

    /**
     * set compression level
     */
    protected void setCompressionLevel(int level) {
        mJos.setLevel(level);
    }

    /**
     * add a new file
     */
    protected void addFile(File newEntry, String name) {

        // if directory return
        if (newEntry.isDirectory()) {
            return;
        }

        try {
            JarEntry je = new JarEntry(name);

            mJos.putNextEntry(je);

            FileInputStream fis = new FileInputStream(newEntry);
            byte fdata[] = new byte[1024];
            int readCount = 0;

            while ((readCount = fis.read(fdata)) != -1) {
                mJos.write(fdata, 0, readCount);
            }

            fis.close();
            mJos.closeEntry();
            mObserverCont.setNext(je);
            mObserverCont.setCount(++miCurrentCount);
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        }
    }

    /**
     * close the newly created jar file
     */
    public void close() {

        try {
            mJos.finish();
            mJos.close();
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        } finally {
            mObserverCont.end();
        }
    }
}
