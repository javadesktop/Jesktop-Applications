/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package net.jesktop.apps.decorators.skinlf;



import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import org.jesktop.Decorator;
import org.jesktop.frimble.Frimble;
import org.jesktop.launchable.LaunchableTarget;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


/**
 * Class SkinLFDecorator allows the choosing of theme/lnfs from the L2FProd.com lnf which
 * supports KDE and GTK themes.
 *
 *
 * @author <a href="mailto:Paul_Hammant@yahoo.com">Paul Hammant</a> May 2001.
 * @version V1.0
 */
public class SkinLFDecorator implements Decorator {

    final static String theTheme = "@Theme@";
    private String skinDir = "SkinLnF" + File.separator;

    /**
     * Constructor SkinLFDecorator
     *
     *
     */
    public SkinLFDecorator() {
        unpackThemePack(theTheme.toLowerCase());
        doDecoration();
    }

    /**
     * Method end
     *
     *
     */
    public void end() {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refer Decrorator java docs.
     */
    public void decorate(Frimble frimble, LaunchableTarget launchableTarget) {

        String targetName = "default";

        if (launchableTarget != null) {

            // for uninstalled apps, for the moment launchableTarget is null.
            targetName = launchableTarget.getTargetName();
        }

        frimble.pack();
    }

    private void doDecoration() {

        try {
            Skin skin = SkinLookAndFeel.loadThemePack(skinDir + theTheme.toLowerCase()
                                                      + "themepack.zip");

            SkinLookAndFeel.setSkin(skin);

            /**
             * Using custom look and feel with Swing is currently buggy
             * because Swing is (most of the time) loaded from the system classpath
             * and call to Class.forName use the system class loader
             * see http://developer.java.sun.com/developer/bugParade/bugs/4155617.html
             * for more information on this bug and workaround
             */

            /**
             * First don't use setLookAndFeel(String) but setLookAndFeel(LookAndFeel)
             */
            SkinLookAndFeel lnf = new SkinLookAndFeel();

            UIManager.setLookAndFeel(lnf);

            /**
             * Then, specify your own ClassLoader to work around these bugs
             * Calls to UIManager.getUI() will use the lnf classloader
             * see javax.swing.UIDefaults#getUI method to understand how this works.
             */
            UIManager.getLookAndFeelDefaults().put("ClassLoader",
                                                   lnf.getClass().getClassLoader());
        } catch (UnsupportedLookAndFeelException uslnfe) {

            //TODO throws?
            System.out.println("UnsupportedLookAndFeelException " + uslnfe.getMessage());
        } catch (Exception e) {

            //TODO throws? // sucky forced catch from SkinL&F
            System.out.println("Unknown Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void unpackThemePack(String themePack) {

        // new File(skinDir).mkdir();
        try {
            File outputFile = new File(skinDir + themePack + "themepack.zip");

            outputFile.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(outputFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            URL url = this.getClass().getClassLoader().getResource("themes/" + themePack
                                                                   + "themepack.zip");
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            byte[] block = new byte[2048];
            int bytes = bis.read(block);

            while (bytes != -1) {
                bos.write(block, 0, bytes);

                bytes = bis.read(block);
            }

            bis.close();
            bos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
