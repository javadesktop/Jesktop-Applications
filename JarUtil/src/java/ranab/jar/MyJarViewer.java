
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.jar;



import java.io.File;

import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;


/**
 * view a jar file - display all the entries.
 */
public class MyJarViewer implements Runnable {

    private File mJarFile;
    private MyJarObserverContainer mObserverCont;

    /**
     * Constructor MyJarViewer
     *
     *
     * @param jarFile
     *
     */
    public MyJarViewer(File jarFile) {
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
     * invoke a new thread
     */
    public void view() {

        Thread th = new Thread(this);

        th.start();
    }

    /**
     * thread starting point - open the jar file
     */
    public void run() {

        mObserverCont.start();

        try {
            JarFile jf = new JarFile(mJarFile);

            mObserverCont.setCount(jf.size());

            Enumeration en = jf.entries();

            while (en.hasMoreElements()) {
                JarEntry je = (JarEntry) en.nextElement();

                mObserverCont.setNext(je);
            }
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        } finally {
            mObserverCont.end();
        }
    }

    /**
     * get the jar filename
     */
    public String toString() {
        return mJarFile.getAbsolutePath();
    }
}
