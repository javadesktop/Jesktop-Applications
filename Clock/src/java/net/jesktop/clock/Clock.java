
/*****************************************************************************
 *
 * Unlicenced.  Work by Ravinder Singh and William Bebee
 *
 * The Initial Developer of the Original Code is Ravinder Singh.
 * Portions created by Ravinder Singh are
 * Copyright (C) 2000.  All Rights Reserved.
 *
 * It was originally coded as an applet:
 *
 *   Author: Ravinder Singh
 *   Description: A simple & cool analog clock
 *   Created in conjunction with Java Developers Kit v1.1.5
 *   Although it is coded with the 1.0 Java API in mind...
 *
 * Contributor(s):
 *
 * William Beebe: It was William Beebe who moved it to Java2D.
 *
 *
 *****************************************************************************/
package net.jesktop.clock;



import java.awt.*;

import javax.swing.*;

import java.util.*;

import org.jesktop.frimble.*;


/**
 * Class Clock
 *
 *
 * @author Ravinder Singh & William Beebe
 * @version $Revision: 1.1 $
 */
public class Clock extends JPanel implements FrimbleAware {

    private ClockDial clock_dial;
    private SimpleCalendar simple_calendar;

    /**
     * Method main
     *
     *
     * @param args
     *
     */
    public static void main(String[] args) {

        JFrame f = new JFrame("Clock");

        f.getContentPane().setLayout(new BorderLayout());

        Clock c = new Clock();

        f.getContentPane().add(c, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.init();
        c.start();
    }

    /**
     * Constructor Clock
     *
     *
     */
    public Clock() {

        setLayout(new BorderLayout());

        clock_dial = new ClockDial();

        add(clock_dial, BorderLayout.EAST);

        simple_calendar = new SimpleCalendar();

        add(simple_calendar, BorderLayout.CENTER);
    }

    /**
     * Method init
     *
     *
     */
    public void init() {
        clock_dial.init();
        simple_calendar.init();
    }

    /**
     * Method paint
     *
     *
     * @param g
     *
     */
    public void paint(Graphics g) {
        super.paint(g);
    }

    /**
     * Method run
     *
     *
     */
    public void run() {}

    /**
     * Method start
     *
     *
     */
    public void start() {
        clock_dial.start();
        simple_calendar.start();
    }

    /**
     * Method stop
     *
     *
     */
    public void stop() {}

    /**
     * Method update
     *
     *
     * @param g
     *
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Method setFrimble
     *
     *
     * @param frimble
     *
     */
    public void setFrimble(Frimble frimble) {

        frimble.addFrimbleListener(new FrimbleAdapter() {

            public void frimbleOpened(FrimbleEvent e) {
                init();
                start();
            }

            public void frimbleClosing(FrimbleEvent e) {

                stop();

                clock_dial = null;
            }
        });
    }
}
