
/*****************************************************************************
 *
 * Unlicenced. Work by Ravinder Singh and William Beebe
 *
 * The Initial Developer of the Original Code is Ravinder Singh.
 * Portions created by Ravinder Singh are
 * Copyright (C) 2000.  All Rights Reserved.
 *
 * It was originally coded as an applet:
 *
 *   Original Author: Ravinder Singh
 *   Description: A simple & cool analog clock
 *   Created in conjunction with Java Developers Kit v1.1.5
 *   Although it is coded with the 1.0 Java API in mind...
 *
 * Contributor(s):
 *
 * William Beebe: Moved to JDK 1.2 coding.
 *                Very little left of Singh's, except the algorythms.
 *
 *
 *****************************************************************************/
package net.jesktop.clock;



import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import javax.swing.*;

import java.util.*;


/**
 * Class ClockDial
 *
 *
 * @author Ravinder Singh & William Beebe
 * @version $Revision: 1.2 $
 */
public class ClockDial extends JPanel implements Runnable {

    private Thread thread;
    private Image buffer, clock_face;
    private Graphics2D gbuff;
    private Line2D.Double line2D = new Line2D.Double();
    private Stroke basic_stroke = new BasicStroke(2);
    private Font clock_number_font = new Font("Helvetica", Font.BOLD, 14);
    private Date date = new Date();
    private Calendar calendar = new GregorianCalendar();
    private Color background_color, clock_face_color, clock_border_color, outline_color,
                  hand_fill_color;
    private int one_second = 1000, x, y, seconds, minutes, hours, center, xpoly[] = new int[4],
                ypoly[] = new int[4];
    private char[] date_string = new char[8], numString = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

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
     * Constructor ClockDial
     *
     *
     */
    public ClockDial() {

        date_string[2] = ':';
        date_string[5] = ':';
    }

    public Dimension getPreferredSize() {
        return new Dimension(150, 135);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * Method init
     *
     *
     */
    public void init() {

        x = getSize().width;
        y = 135; //getSize().height;
        center = x / 2;
        background_color = Color.decode("0x66000080");
        clock_face_color = Color.decode("0x000099");
        clock_border_color = Color.decode("0x6600ff");
        outline_color = Color.decode("0xcceeff");
        hand_fill_color = Color.decode("0x9999ff");

        create_face();

        buffer = createImage(x, y);
        gbuff = (Graphics2D) buffer.getGraphics();

        gbuff.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
        gbuff.setFont(clock_number_font);
    }

    /**
     * Method paint
     *
     *
     * @param g
     *
     */
    public void paint(Graphics g) {

        if (gbuff == null) {
            return;
        }

        gbuff.setColor(background_color);
        gbuff.fillRect(0, 0, x, y);
        date.setTime(System.currentTimeMillis());
        calendar.setTime(date);

        seconds = calendar.get(Calendar.SECOND);
        minutes = calendar.get(Calendar.MINUTE);
        hours = calendar.get(Calendar.HOUR_OF_DAY);

        gbuff.drawImage(clock_face, center - 55, 5, this);
        gbuff.setColor(outline_color);

        date_string[0] = numString[hours / 10];
        date_string[1] = numString[hours % 10];
        date_string[3] = numString[minutes / 10];
        date_string[4] = numString[minutes % 10];
        date_string[6] = numString[seconds / 10];
        date_string[7] = numString[seconds % 10];

        gbuff.drawChars(date_string, 0, 8, center - 32, 130);
        draw_hands(hours % 12 * 30 + (minutes / 2), 30, 7);
        draw_hands(minutes * 6, 40, 5);
        gbuff.setColor(outline_color);

        Stroke save_stroke = gbuff.getStroke();
        double degrees = seconds * 6;

        gbuff.setStroke(basic_stroke);
        line2D.setLine(center, 60, center + (Math.sin(Math.toRadians(degrees)) * 40),
                       60 - (Math.cos(Math.toRadians(degrees)) * 40));
        gbuff.draw(line2D);
        gbuff.setStroke(save_stroke);
        g.drawImage(buffer, 0, 0, this);
    }

    /**
     * Method draw_hands
     *
     *
     * @param degrees
     * @param len
     * @param thick
     *
     */
    void draw_hands(int degrees, int len, int thick) {

        int deg1 = (degrees + 45) % 360, deg2 = (degrees + 315) % 360;

        xpoly[0] = center;
        xpoly[1] = (int) (center + ((Math.sin(Math.toRadians(deg1)) * thick)));
        xpoly[2] = (int) (center + ((Math.sin(Math.toRadians(degrees)) * len)));
        xpoly[3] = (int) (center + ((Math.sin(Math.toRadians(deg2)) * thick)));
        ypoly[0] = 60;
        ypoly[1] = (int) (60 - ((Math.cos(Math.toRadians(deg1)) * thick)));
        ypoly[2] = (int) (60 - ((Math.cos(Math.toRadians(degrees)) * len)));
        ypoly[3] = (int) (60 - ((Math.cos(Math.toRadians(deg2)) * thick)));

        gbuff.setPaint(hand_fill_color);
        gbuff.fillPolygon(xpoly, ypoly, 4);
        gbuff.setPaint(clock_border_color);
        gbuff.drawPolygon(xpoly, ypoly, 4);
    }

    /**
     * Method create_face
     *
     *
     */
    void create_face() {

        clock_face = createImage(110, 110);
        gbuff = (Graphics2D) clock_face.getGraphics();

        gbuff.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
        gbuff.setPaint(background_color);
        gbuff.fillRect(0, 0, getSize().width, getSize().height);
        gbuff.setPaint(clock_face_color);

        Ellipse2D.Double e = new Ellipse2D.Double(5, 5, 100, 100);

        gbuff.fill(e);
        gbuff.setPaint(clock_border_color);

        Stroke s = gbuff.getStroke();

        gbuff.setStroke(basic_stroke);
        gbuff.draw(e);
        gbuff.setColor(outline_color);

        for (int i = 0; i < 360; i += 6) {
            double x1, x2, y1, y2;
            int big = ((i % 90) == 0 ? 2 : 0);

            if (i % 30 == 0) {
                x1 = 55 + ((48 - big) * Math.sin(Math.toRadians(i)));
                y1 = 55 - ((48 - big) * Math.cos(Math.toRadians(i)));
                x2 = 55 + ((52 + big) * Math.sin(Math.toRadians(i)));
                y2 = 55 - ((52 + big) * Math.cos(Math.toRadians(i)));

                line2D.setLine(x1, y1, x2, y2);
                gbuff.draw(line2D);
            } else {
                x1 = 55 + (50 * Math.sin(Math.toRadians(i)));
                y1 = 55 - (50 * Math.cos(Math.toRadians(i)));

                line2D.setLine(x1, y1, x1, y1);
                gbuff.draw(line2D);
            }
        }

        gbuff = null;
    }

    /**
     * Method run
     *
     *
     */
    public void run() {

        long time = System.currentTimeMillis();

        while (thread != null) {
            try {
                time += one_second;

                thread.sleep(Math.max(0, time - System.currentTimeMillis()));
            } catch (InterruptedException e) {}

            repaint();
        }
    }

    /**
     * Method start
     *
     *
     */
    public void start() {

        if (thread == null) {
            thread = new Thread(this);

            thread.start();
        }
    }

    /**
     * Method stop
     *
     *
     */
    public void stop() {
        thread = null;
    }

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
}
