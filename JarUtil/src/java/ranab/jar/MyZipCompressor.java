
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

import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;


/**
 * Zip file compressor class.
 */
public class MyZipCompressor extends MyCompressor {

    private ZipOutputStream mZos;
    private int miCurrentCount;

    /**
     * Constructor MyZipCompressor
     *
     *
     * @param zipfl
     *
     */
    public MyZipCompressor(File zipfl) {

        super(zipfl);

        miCurrentCount = 0;
    }

    /**
     * create a new zip file
     */
    public void open() throws Exception {

        try {
            mZos = new ZipOutputStream(new FileOutputStream(mCompressedFile));

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
        mZos.setLevel(level);
    }

    /**
     * add a new entry in the zip file
     */
    protected void addFile(File newEntry, String name) {

        // if directory return
        if (newEntry.isDirectory()) {
            return;
        }

        try {
            ZipEntry ze = new ZipEntry(name);

            mZos.putNextEntry(ze);

            FileInputStream fis = new FileInputStream(newEntry);
            byte fdata[] = new byte[512];
            int readCount = 0;

            while ((readCount = fis.read(fdata)) != -1) {
                mZos.write(fdata, 0, readCount);
            }

            fis.close();
            mZos.closeEntry();
            mObserverCont.setNext(ze);
            mObserverCont.setCount(++miCurrentCount);
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        }
    }

    /**
     * closed the newly created zip file
     */
    public void close() {

        try {
            mZos.finish();
            mZos.close();
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        } finally {
            mObserverCont.end();
        }
    }
}
