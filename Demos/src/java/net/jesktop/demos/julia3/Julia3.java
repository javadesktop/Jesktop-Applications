package net.jesktop.demos.julia3;



import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.lang.Math;

import java.applet.*;


/** Class juliagenerator is a thread that generates Juliafractals
 * @author Robert Olofsson
 * @version 3.0
 */
interface JuliaConsts {

  int WWIDTH =256;    // don't change these! as they are
  int HHEIGHT=256;    // incorrrectly used later - PH
  String    FASTER ="25% Faster";
  String    SLOWER ="25% Slower";
}


class JuliaGenerator extends Thread implements JuliaConsts {

  public int REPAINTDELAY=100;
  int        iterations  =80;              // can be changed if image is bigger
  int        pix[]       =new int[WWIDTH * HHEIGHT];    // used to store the julia
  int        pix2[]      =new int[WWIDTH * HHEIGHT];    // used to quick-clear pix
  int        sqrs[]      =new int[256];    // this gains a few percent in speed
  int        hw[]        =new int[256];    // start on which pixel on row
  int        re          =2;
  int        im          =4;

  // used to store my colors
  byte            redv[]  =new byte[iterations + 5];
  byte            greenv[]=new byte[iterations + 5];
  byte            bluev[] =new byte[iterations + 5];
  byte            alfas[] =new byte[iterations + 5];
  IndexColorModel cm;                      // Palette for my image
  Image           img;
  int             ingvar=0;                // counter for re and im
  Julia3          j3    =null;

  protected void incSpeed() {

    REPAINTDELAY=(int) (REPAINTDELAY * 0.75);

    if (REPAINTDELAY < 10) {
      REPAINTDELAY=10;
    }
  }

  protected void decSpeed() {
    REPAINTDELAY=(int) (REPAINTDELAY * 1.25);
  }

  /** constructor Takes a Julia3 applet so it knows where to
   * give the results (images).
   * @param j the Julia3-applet that should show the images this thread makes
   */
  JuliaGenerator(Julia3 j) {

    j3=j;

    // build the palette
    buildpalette(0, 0, 255, 255, 0, 0, 10, 0);
    buildpalette(0, 255, 0, 0, 0, 255, 30, 10);
    buildpalette(0, 0, 0, 0, 255, 0, 45, 40);

    // calculate outer bounds of fractal (need only be done once)
    // and build sqaure vector.
    for (int i=0; i < 128; i++) {
      hw[i]        =hw[255 - i]=(int) (128
                                       * Math.sin(Math.acos((128 - i)
                                         / 128.0)));
      sqrs[127 - i]=sqrs[128 + i]=i * i;
    }

    for (int i=0; i < WWIDTH * HHEIGHT; i++) {
      pix[i]=pix2[i]=0;
    }

    // create colormap and image
    cm =new IndexColorModel(8, iterations, redv, greenv, bluev);
    img=j3.createImage(new MemoryImageSource(WWIDTH, HHEIGHT, cm, pix, 0,
                                             HHEIGHT));
  }

  void buildpalette(int r2, int g2, int b2,        // to these values
                    int r, int g, int b,           // from these
                            int n, int start) {    // how many, starting from


    int m=n;

    for (int i=0; i < n; i++, m--) {
      redv[i + start]  =(byte) (r2 * i / n + r * m / n);
      greenv[i + start]=(byte) (g2 * i / n + g * m / n);
      bluev[i + start] =(byte) (b2 * i / n + b * m / n);
      alfas[i + start] =(byte) 127;
    }
  }

  /** this is the magic iterate function that calculates the value for
   * the given point (x,y).
   * @param x the x-cordinate
   * @param y the y-cordinate
   * @param iter the number of iterations we already have done.
   */
  int iterate(int x, int y, int iter) {

    int it;
    int nx, ny;
    int v=x - (y << 8);

    if ((it=pix[v + 127 * 257]) == 0) {    // do we have a previous value
      it=iterations;

      //  nop we dont
      if (iter < iterations) {
        ny=(x * y + im) >> 5;              // here we get errors in division
        it=1;

        if ((ny < 128) && (ny > -128)) {
          nx=(sqrs[x + 127] - sqrs[y + 127] + re) >> 6;

          if ((nx <= hw[ny + 127]) && (nx >= -hw[ny + 127])) {
            it+=iterate(nx, ny, iter + 1);
          }
        }
      }

      pix[v + 127 * 257]=pix[-v + 127 * 257]=it;
    }

    return (it == iterations)
           ? it - 1
           : it;
  }

  /** the thread that runs the whole time.
   *  It generates an image and then gives it to the applet.
   */
  public void run() {

    while (true) {

      // give the system some time to copy the vector to the image
      try {
        Thread.sleep(REPAINTDELAY);
      } catch (InterruptedException e) {}

      // beefore we clear pix to start a new image
      // this is the fastes way I know of
      System.arraycopy(pix2, 0, pix, 0, WWIDTH * HHEIGHT);

      //update new re and new im part....
      // change these to other functions to get diffrent julias
      re=(int) (1.5 * 2048 * Math.cos(0.11 * ingvar));
      im=(int) (1.5 * 1024 * Math.sin(0.14 * (ingvar++) + 0.4711));

      // get value
      // 128 represents 2.0 on the axis
      // x,y
      // -127,128---------128,128
      //     |    screen     |
      //     |  cordinates   |
      //-127,-127---------128,-127
      // by starting from the inner parts I have a bigger chans to fill in
      // more of the julia in one row.
      for (int y=0; y < 128; y++) {
        for (int x=-hw[y + 127]; x < hw[y + 127] + 1; x++) {
          iterate(x, y, 0);
        }
      }

      img=j3.createImage(new MemoryImageSource(WWIDTH, HHEIGHT, cm, pix, 0,
                                               HHEIGHT));

      j3.setImage(img);
    }
  }
}

/** Class Julia3 is a framework that shows juliafractals.
 * @author Robert Olofsson
 * @version 3.0
 */
public class Julia3 extends JPanel
        implements JuliaConsts, MouseListener, ActionListener {

  JuliaGenerator julia         =null;
  boolean        juliasuspended=false;
  JPopupMenu     popup;
  Image          img=null;

  /**
   * Constructor Julia3
   *
   *
   */
  public Julia3() {

    this.setPreferredSize(new Dimension(WWIDTH, HHEIGHT));
    this.addMouseListener(this);

    popup=new JPopupMenu("Julia3 Speed Control");

    JMenuItem mi=new JMenuItem(FASTER);

    mi.addActionListener(this);
    popup.add(mi);

    mi=new JMenuItem(SLOWER);

    mi.addActionListener(this);
    popup.add(mi);
  }

  /** simple startup handler
   */
  public void init() {

    julia=new JuliaGenerator(this);

    julia.start();
  }

  /** restart the applet, and the generator
   */
  public void start() {

    if (julia == null) {
      julia=new JuliaGenerator(this);

      julia.start();
    }
  }

  /** stop the generator so the applet behaves.
   */
  public void stop() {

    julia.stop();

    julia=null;
  }

  /** Dont clear background since we are showing a full image.
   *
   * @param g
   */
  public void update(Graphics g) {
    paint(g);
  }

  /**  draw the image that holds the current fractal.
   *
   * @param g
   */
  public void paint(Graphics g) {

    if (img != null) {
      g.drawImage(img, 0, 0, Color.black, this);
    }
  }

  /** Sets the image (fractal) to show.
   * @param img the new image of the fractal
   */
  public void setImage(Image img) {

    this.img=img;

    repaint();
  }

  /** handle this to suspend generation
   * @param e the event that happend.
   * @param x the x-cordinate of the event.
   * @param y the y-cordinate of the event.
   *
   * @return
   */
  public boolean mouseDown(Event e, int x, int y) {

    if (juliasuspended) {
      julia.resume();
    } else {
      julia.suspend();
    }

    juliasuspended=!juliasuspended;

    return true;
  }

  /**
   * Method mouseClicked
   *
   *
   * @param e
   *
   */
  public void mouseClicked(MouseEvent e) {}

  /**
   * Method mousePressed
   *
   *
   * @param e
   *
   */
  public void mousePressed(MouseEvent e) {
    popup.show(this, e.getX(), e.getY());
  }

  /**
   * Method mouseReleased
   *
   *
   * @param e
   *
   */
  public void mouseReleased(MouseEvent e) {}

  /**
   * Method mouseEntered
   *
   *
   * @param e
   *
   */
  public void mouseEntered(MouseEvent e) {}

  /**
   * Method mouseExited
   *
   *
   * @param e
   *
   */
  public void mouseExited(MouseEvent e) {}

  /**
   * Method actionPerformed
   *
   *
   * @param e
   *
   */
  public void actionPerformed(ActionEvent e) {

    if (e.getActionCommand().equals(FASTER)) {
      julia.incSpeed();
    } else if (e.getActionCommand().equals(SLOWER)) {
      julia.decSpeed();
    }
  }
}


/*--- Formatted in Sun Java Convention Style on Fri, Nov 24, '00 ---*/


/*------ Formatted by Jindent 3.11 trial --- http://www.jindent.de ------*/
