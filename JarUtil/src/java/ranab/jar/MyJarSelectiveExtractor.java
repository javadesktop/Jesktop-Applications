
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.jar.JarFile;


/**
 * This is JAR file extractor class. It extracts the JAR file
 * in a separate thread. Where we are passing a MyJarObserver
 * object to track the current status of this decompression.
 */
public class MyJarSelectiveExtractor extends MyJarExtractor {

    private ZipEntry mEntry[];

    /**
     * Constructor MyJarSelectiveExtractor
     *
     *
     * @param entry
     * @param jarFile
     * @param dir
     *
     */
    public MyJarSelectiveExtractor(ZipEntry entry[], File jarFile, File dir) {

        super(jarFile, dir);

        mEntry = entry;
    }

    /**
     * invoke a new thread and start decompression
     */
    public void extract() {

        Thread th = new Thread(this);

        th.start();
    }

    /**
     * Method run
     *
     *
     */
    public void run() {

        mbIsPauseRequest = false;
        mbIsStopRequest = false;

        mObserverCont.start();

        try {
            ZipFile jf = new JarFile(mJarFile);

            mObserverCont.setCount(mEntry.length);

            for (int i = 0; i < mEntry.length; i++) {

                // check request
                while (mbIsPauseRequest && (!mbIsStopRequest)) {
                    Thread.sleep(100);
                }

                if (mbIsStopRequest) {
                    return;
                }

                extract(jf, mEntry[i]);
                mObserverCont.setNext(mEntry[i]);
            }
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        } finally {
            mbIsStopRequest = true;

            mObserverCont.end();
        }
    }

    /**
     * get jar file name
     */
    public String toString() {
        return mJarFile.getAbsolutePath();
    }
}
