
/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included  with this distribution in *
 * the LICENSE file.                                                         *
 *****************************************************************************/
package ranab.gui;



import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * this is a generic class to show about dialog
 */
public class MyAboutDialog extends JDialog implements SwingConstants, ActionListener {

    private String mstMessage;
    private String mstButtonText;
    private Icon mImgIcon;
    private int miButtonAlignment;
    private String mstTextAlignment;
    private Font mFont;
    private Color mBgColor;
    private Color mFgColor;
    private Component mParent;

    /**
     * instantiate about dialog box
     */
    public MyAboutDialog(Frame parent, boolean modal) {

        super(parent, modal);

        mParent = parent;
        mstMessage = null;
        mImgIcon = null;
        mstButtonText = "Ok";
        miButtonAlignment = FlowLayout.CENTER;
        mstTextAlignment = BorderLayout.SOUTH;
        mFont = null;
        mBgColor = null;
        mFgColor = null;
    }

    /**
     * set button alignment
     */
    public void setButtonAlignment(int align) {

        if ((align != FlowLayout.LEFT) || (align != FlowLayout.CENTER)
                || (align != FlowLayout.RIGHT)) {
            throw new IllegalArgumentException("Not a valid horizontal alignment");
        }

        miButtonAlignment = align;
    }

    /**
     * set text alignment
     */
    public void setTextAlignment(String align) {

        if ((!align.equals(BorderLayout.NORTH)) || (!align.equals(BorderLayout.SOUTH))
                || (!align.equals(BorderLayout.EAST)) || (!align.equals(BorderLayout.WEST))) {
            throw new IllegalArgumentException("Not a valid alignment");
        }

        mstTextAlignment = align;
    }

    /**
     * set button text
     */
    public void setButtonText(String txt) {
        mstButtonText = txt;
    }

    /**
     * set about text message
     */
    public void setText(String msg) {
        mstMessage = msg;
    }

    /**
     * set image icon
     */
    public void setImage(String img) {
        mImgIcon = MyGuiUtil.createImageIcon(img);
    }

    /**
     * set text font
     */
    public void setFont(Font fnt) {
        mFont = fnt;
    }

    /**
     * set background color
     */
    public void setBackgroundColor(Color color) {
        mBgColor = color;
    }

    /**
     * set foreground color
     */
    public void setForegroundColor(Color color) {
        mFgColor = color;
    }

    /**
     * set title
     */
    public void setTitle(String title) {

        if (title != null) {
            super.setTitle(title);
        }
    }

    /**
     * get text area component
     */
    private JLabel getTextArea() {

        if (mstMessage == null) {
            return null;
        }

        JLabel txtLab = new JLabel();

        if (mFont != null) {
            txtLab.setFont(mFont);
        }

        if (mBgColor != null) {
            txtLab.setBackground(mBgColor);
        }

        if (mFgColor != null) {
            txtLab.setForeground(mFgColor);
        }

        txtLab.setText(mstMessage);
        txtLab.setHorizontalAlignment(SwingConstants.CENTER);

        return txtLab;
    }

    /**
     * get button panel
     */
    private JPanel getButtonPanel() {

        JButton btn = new JButton(mstButtonText);

        btn.addActionListener(this);

        JPanel pane = new JPanel();

        pane.setLayout(new FlowLayout(miButtonAlignment));
        pane.add(btn);

        return pane;
    }

    /**
     * get top panel
     */
    private JPanel getTopPanel() {

        JPanel topPane = new JPanel();

        topPane.setLayout(new BorderLayout());

        JLabel txtLab = getTextArea();

        if ((txtLab == null) && (mImgIcon == null)) {
            return null;
        }

        if (txtLab == null) {
            topPane.add(new JLabel(mImgIcon), BorderLayout.CENTER);

            return topPane;
        }

        if (mImgIcon == null) {
            topPane.add(txtLab, BorderLayout.CENTER);

            return topPane;
        }

        topPane.add(new JLabel(mImgIcon), BorderLayout.CENTER);
        topPane.add(txtLab, mstTextAlignment);

        return topPane;
    }

    /**
     * show about dialog
     */
    public void display() {

        getContentPane().setLayout(new BorderLayout());

        JPanel topPane = getTopPanel();
        JPanel buttonPanel = getButtonPanel();

        if (topPane == null) {
            getContentPane().add(buttonPanel, BorderLayout.CENTER);
        } else {
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            getContentPane().add(topPane, BorderLayout.CENTER);
        }

        pack();
        setLocationRelativeTo(mParent);
        setVisible(true);
    }

    /**
     * button action listener
     */
    public void actionPerformed(ActionEvent l) {
        close();
    }

    /**
     * Closes the dialog box.
     */
    private void closeDialog(WindowEvent evt) {
        close();
    }

    /**
     * close dialog window
         */
    public void close() {
        setVisible(false);
        dispose();
    }
}
