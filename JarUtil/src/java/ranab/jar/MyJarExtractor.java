
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
public abstract class MyJarExtractor implements Runnable {

    protected File mDir;
    protected File mJarFile;
    protected MyJarObserverContainer mObserverCont;
    protected boolean mbIsPauseRequest = false;
    protected boolean mbIsStopRequest = false;

    /**
     * Constructor MyJarExtractor
     *
     *
     * @param jarFile
     * @param dir
     *
     */
    public MyJarExtractor(File jarFile, File dir) {

        mDir = dir;
        mJarFile = jarFile;
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
     * Extract an entry from the zip file.
     */
    public void extract(ZipFile jf, ZipEntry ze) throws Exception {

        File fl = new File(mDir, ze.toString());

        // directory create it
        if (ze.isDirectory()) {
            fl.mkdirs();

            return;
        }

        File par = new File(fl.getParent());

        if (!par.exists()) {
            par.mkdirs();
        }

        // file decompres it
        FileOutputStream fos = new FileOutputStream(fl);

        extract(jf, ze, fos);
        fos.close();
    }

    /**
     * write ZipEntry into OutputStream
     */
    public static void extract(ZipFile jf, ZipEntry ze, OutputStream out) throws Exception {

        InputStream is = jf.getInputStream(ze);
        byte buff[] = new byte[1024];
        int cnt = -1;

        while ((cnt = is.read(buff)) != -1) {
            out.write(buff, 0, cnt);
        }

        is.close();
    }

    /**
     * stop decompression
     */
    public void stop() {
        mbIsStopRequest = true;
    }

    /**
     * Method isStopped
     *
     *
     * @return
     *
     */
    public boolean isStopped() {
        return mbIsStopRequest;
    }

    /**
     * pause decompression
     */
    public void pause() {
        mbIsPauseRequest = true;
    }

    /**
     * Method isPaused
     *
     *
     * @return
     *
     */
    public boolean isPaused() {
        return mbIsPauseRequest;
    }

    /**
     * resume decompression
     */
    public void resume() {
        mbIsPauseRequest = false;
    }

    /**
     * get jar file name
     */
    public String toString() {
        return mJarFile.getAbsolutePath();
    }

    /**
     * start decompression
     */
    public abstract void extract();
}
