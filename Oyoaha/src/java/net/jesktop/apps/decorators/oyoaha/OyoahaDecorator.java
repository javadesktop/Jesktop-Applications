/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package net.jesktop.apps.decorators.oyoaha;

import org.jesktop.frimble.Frimble;
import org.jesktop.config.*;
import org.jesktop.launchable.*;
import org.jesktop.Decorator;

import javax.swing.*;

import java.net.URL;

import java.util.Properties;

import com.oyoaha.swing.plaf.oyoaha.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * Class OyoahaDecorator allows the choosing of theme/lnfs from the OYOAHA lnf.
 *
 *
 * @author <a href="mailto:Paul_Hammant@yahoo.com">Paul Hammant</a> May 2001.
 * @version V1.0
 */
public class OyoahaDecorator implements Decorator, ObjConfigurable, PropertyChangeListener {

    private Config config = new Config();
    private OyoahaLookAndFeel lnf;

    /**
     * Constructor OyoahaDecorator
     *
     *
     */
    public OyoahaDecorator() {}

    /**
     * Refer Decrorator java docs.
     */
    public void setConfig(Object obj) {

        lnf = new OyoahaLookAndFeel();

        if (obj == null) {
            config = new Config();

            doDecoration();
        } else {
            try {
                config = (Config) obj;
            } catch (ClassCastException cce) {
                config = new Config();
            }

            doDecoration();
        }
    }

    /**
     * Method end
     *
     *
     */
    public void end() {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {}
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
            String fName = config.oyoahaConfigFile;
            URL url = this.getClass().getClassLoader().getResource(fName);

            if (fName.startsWith("themes.")) {
                lnf.setCurrentTheme(url);
            } else {
                lnf.setOyoahaTheme(url);
            }

            UIManager.setLookAndFeel(lnf);
        } catch (UnsupportedLookAndFeelException uslnfe) {

            //TODO throws?
            System.out.println("UnsupportedLookAndFeelException " + uslnfe.getMessage());
        }
    }
    
    public void propertyChange( PropertyChangeEvent event ) {
        setConfig(event.getNewValue());
    }    
    
}
