
//
// Mondrian - applet that draws modern art
//
//  Written by: Richie Bielak    5/16/1996
//      http://www.netlabs.net/hp/richieb
//
//  -- This program maybe freely copied --
//  -- Just please keep my name in.     --
//
//
package net.jesktop.demos.mondrian;



import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


/**
 * Class Mondrian Ported to PJE and Swing by Paul Hammant 24th July 2000
 *
 *
 * @author Richie Bielak
 * @version %I%, %G%
 */
public class Mondrian extends JPanel {

  /**
   * Constructor Mondrian
   *
   *
   */
  public Mondrian() {

    this.setPreferredSize(new Dimension(300, 200));
    this.addMouseListener(new MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        repaint();
      }
    });
  }

  /**
   * Method paintComponent
   *
   *
   * @param g
   *
   */
  public void paintComponent(Graphics g) {

    int pic_width =this.getWidth();
    int pic_height=this.getHeight();

    g.setColor(Color.white);
    g.fillRect(1, 1, pic_width, pic_height);
    mondrian_vertical(g, 1, 1, pic_width, pic_height, 4);
  }

  /**
   * Method mondrian_vertical
   *
   *
   * @param g
   * @param x
   * @param y
   * @param width
   * @param height
   * @param depth
   *
   */
  public void mondrian_vertical(Graphics g, int x, int y, int width,
                                int height, int depth) {

    int middle;

    if (depth > 0) {

      // split in two
      middle=(int) (Math.random() * width);

      // Draw dividing line
      g.setColor(Color.black);
      g.fillRect(x + middle, y, depth * 2, height);

      // Handle sub-rectangles
      mondrian_horizontal(g, x, y, middle, height, depth - 1);
      mondrian_horizontal(g, x + middle + depth * 2, y, width - middle,
                          height, depth - 1);
    } else {

      // Fill in with random color
      if (Math.random() < .2) {
        setRandomColor(g);
        g.fillRect(x, y, width, height);
      }
    }
  }

  /**
   * Method mondrian_horizontal
   *
   *
   * @param g
   * @param x
   * @param y
   * @param width
   * @param height
   * @param depth
   *
   */
  public void mondrian_horizontal(Graphics g, int x, int y, int width,
                                  int height, int depth) {

    int middle;

    if (depth > 0) {
      middle=(int) (Math.random() * height);

      g.setColor(Color.black);
      g.fillRect(x, y + middle, width, depth * 2);

      // Deal with the sub-rectangles
      mondrian_vertical(g, x, y + middle + depth * 2, width, height - middle,
                        depth - 1);
      mondrian_vertical(g, x, y, width, middle, depth - 1);
    } else {
      if (Math.random() < .2) {
        setRandomColor(g);
        g.fillRect(x, y, width, height);
      }
    }
  }

  /**
   * Method setRandomColor
   *
   *
   * @param g
   *
   */
  public void setRandomColor(Graphics g) {

    double x;

    x=Math.random();

    if (x < .3) {
      g.setColor(Color.blue);
    } else if (x < .6) {
      g.setColor(Color.red);
    } else {
      g.setColor(Color.yellow);
    }
  }
}


/*--- Formatted in Sun Java Convention Style on Fri, Nov 24, '00 ---*/


/*------ Formatted by Jindent 3.11 trial --- http://www.jindent.de ------*/
