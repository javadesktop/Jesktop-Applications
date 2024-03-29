/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package net.jesktop.apps.decorators.oyoaha;



import org.jesktop.config.ConfigHelper;
import org.jesktop.config.ConfigManager;
import org.jesktop.config.ObjConfiglet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;


/**
 * Class OyoahaDecorator allows the choosing of theme/lnfs from the OYOAHA lnf.
 *
 *
 * @author <a href="mailto:Paul_Hammant@yahoo.com">Paul Hammant</a> May 2001.
 * @version V1.0
 */
public class OyoahaDecoratorConfig extends JPanel
        implements ObjConfiglet, ActionListener {

    private final OyoahaCheckBox[] checkboxes = new OyoahaCheckBox[] {
        new OyoahaCheckBox("Metal / Blue #1", "themes/blue1.theme"),
        new OyoahaCheckBox("Metal / Font #2", "themes/font2.theme"),
        new OyoahaCheckBox("Metal / Green #1", "themes/green1.theme"),
        new OyoahaCheckBox("Metal / Pink #1", "themes/pink1.theme"),
        new OyoahaCheckBox("Metal / Purple #1", "themes/purple1.theme"),
        new OyoahaCheckBox("Anidaisy", "otms/anidaisy.otm"),
        new OyoahaCheckBox("Slushy", "otms/slushy.otm"),
        new OyoahaCheckBox("Tgang", "otms/tgang.otm")
    };
    private Config config;
    private ConfigManager configManager;

    /**
     * Constructor TestDecoratorConfig
     *
     *
     */
    public OyoahaDecoratorConfig(ConfigManager configManager) {
        this.configManager = configManager;

        this.setLayout(new FlowLayout());

        for (int f = 0; f < checkboxes.length; f++) {
            this.add(checkboxes[f]);
            checkboxes[f].addActionListener(this);
        }
    }

    /**
     * Method getConfig
     *
     *
     * @return
     *
     */
    public Object getConfig() {
        return config;
    }

    /**
     * Method setConfig
     *
     *
     * @param o
     *
     */
    public void setConfig(Object o) {

        config = (Config) o;

        if (o == null) {
            config = new Config();
        }

        for (int f = 0; f < checkboxes.length; f++) {
            if (config.oyoahaConfigFile.equals(checkboxes[f].fileName)) {
                checkboxes[f].setSelected(true);

                return;
            }

            checkboxes[0].setSelected(true);

            return;
        }
    }

    /**
     * Method actionPerformed
     *
     *
     * @param ae
     *
     */
    public void actionPerformed(ActionEvent ae) {

        for (int f = 0; f < checkboxes.length; f++) {
            OyoahaCheckBox ocb = (OyoahaCheckBox) ae.getSource();

            if (ocb != checkboxes[f]) {
                checkboxes[f].setSelected(false);
            } else {
                config.oyoahaConfigFile = ocb.fileName;

                configManager.notifyUpdated(this);
            }
        }
    }

    public void propertyChange( PropertyChangeEvent event ) {
        if (ConfigHelper.isConfigPropChange(event) && event.getNewValue() != config ) {
            setConfig(event.getNewValue());
        }
    }

    /**
     * Class OyoahaCheckBox
     *
     *
     * @author Paul Hammant <a href="mailto:Paul_Hammant@yahoo.com">Paul_Hammant@yahoo.com</a>
     * @version $Revision: 1.2 $
     */
    private class OyoahaCheckBox extends JCheckBox {

        String fileName;

        private OyoahaCheckBox(String optionName, String fileName) {

            super(optionName);

            this.fileName = fileName;
        }
    }
}
