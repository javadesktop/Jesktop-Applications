
/*
 *  AlexWarp image-warping program, copyright 1996 by Alex Rosen
 *  (mail@axlrosen.net). You can do whatever you want with this code, as long
 *  as you include a link to my home page (http://www.axlrosen.net/)
 *  with it, and/or with whatever you create from it.
 *
 *  AlexWarp is a port to Java of a Mac-based program I wrote. All the code in
 *  the ImageWarper class was taken straight from the Mac, with a little
 *  search-and-replace work thrown in.
 */
package net.jesktop.demos.alexwarp;



import java.awt.*;

import javax.swing.ImageIcon;

import java.awt.event.*;

//import java.applet.*;
import javax.swing.*;

import java.awt.image.*;

import java.net.*;


/**
 * Class AlexWarp
 *
 *
 * @author
 * @version %I%, %G%
 */
public class AlexWarp extends JPanel
        implements Runnable, MouseListener, MouseMotionListener,
                   ActionListener {

  Thread       mStartup  =null;                    // initialization thread
  ImageWarper  mWarper   =null;                    // warping thread
  String       mImageName=null;                    // filename of the image
  Image        mImage    =null;                    // the image to be warped
  int          mPixels[], mOldPixels[];            // pixel data from images
  int          mWidth, mHeight;                    // dimensions of warp image
  String       mStatus    ="";                     // status message
  Point        mFromPoint =null, mToPoint=null;    // for clicking & dragging
  boolean      mReady     =false;                  // ready to warp
  boolean      mCanUndo   =false;                  // can user select undo/redo?
  boolean      mRedo      =false;    // if mCanUndo, is this an undo or a redo?
  JButton      mUndoButton=null;                   // the undo/redo button
  GraphicPanel graphicPanel;

  //      final int               kHOffset = 30, kVOffset = 35;                   // offset of image in applet
  final int kHOffset=0, kVOffset=0;                // offset of image in jpanel

  /**
   * Constructor AlexWarp
   *
   *
   */
  public AlexWarp() {

    this.setLayout(new BorderLayout());

    graphicPanel=new GraphicPanel();

    this.add(graphicPanel, BorderLayout.CENTER);
    this.setPreferredSize(new Dimension(320, 320));
  }

  /**
   * Method run
   *
   *
   */
  public void run() {

    Initialize();

    // need to keep a thread going to update status while warping
    // (this won't work from the ImageWarper thread for some reason)
    while (mStartup != null) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {}

      if (!mReady) {

        // if warping, add a period on the end of the status message
        mStatus+=".";
      }
    }
  }

  void Initialize() {

    graphicPanel.addMouseListener(this);
    graphicPanel.addMouseMotionListener(this);

    mStatus ="Loading image...";
    mReady  =false;
    mCanUndo=false;
    mRedo   =false;

    mUndoButton.disable();

    // get warp image & dimensions
    //mImage = Toolkit.getDefaultToolkit().getImage( mImageName );
    //while ( (mWidth=mImage.getWidth(this)) < 0 )                                          // this is not the way we're really
    //try { Thread.sleep(100); } catch( InterruptedException e ) {} // supposed to do this. oh well, it works.
    //              while ( (mHeight=mImage.getHeight(this)) < 0 )
    //              try { Thread.sleep(100); } catch( InterruptedException e ) {}
    // get the pixel data from the image
    mPixels   =new int[mWidth * mHeight];
    mOldPixels=new int[mWidth * mHeight];

    PixelGrabber grabber=new PixelGrabber(mImage, 0, 0, mWidth, mHeight,
                                          mPixels, 0, mWidth);
    boolean      done   =false;

    do {

      // grab pixels for 500 msec
      try {
        done=grabber.grabPixels(500);
      } catch (InterruptedException e) {
        mStatus="AlexWarp interrupted.";

        return;
      }

      // update status message
      mStatus+=".";
    } while (!done);

    if ((grabber.status() & ImageObserver.ABORT) != 0) {
      mStatus="AlexWarp interrupted.";

      return;
    }

    mReady =true;
    mStatus="Ready for warping. Click and drag in the image to warp it.";

    graphicPanel.repaint();
  }

  // called by ImageWarper thread when warping is complete
  void DoneWithWarping() {

    // the new picture is now in mPixels - create a new Image from it
    mImage=createImage(new MemoryImageSource(mWidth, mHeight, mPixels, 0,
                                             mWidth));

    // set button to "undo" state
    mUndoButton.enable();
    mUndoButton.setLabel("Undo");

    mRedo   =false;
    mCanUndo=true;

    graphicPanel.repaint();

    // all done, ready for the next one!
    mReady =true;
    mWarper=null;
    mStatus="Ready for warping. Click and drag in the image to warp it.";
  }

  /**
   * Method start
   *
   *
   */
  public void start() {

    if (mImage == null)    // have we initialized yet?
    {

      // add buttons
      JPanel bPnl=new JPanel(new FlowLayout());

      this.add(bPnl, BorderLayout.NORTH);

      JButton b=new JButton("Undo");

      b.addActionListener(this);
      bPnl.add(b);
      b.disable();

      mUndoButton=b;
      b          =new JButton("Stop");

      b.addActionListener(this);
      bPnl.add(b);

      b=new JButton("Reset");

      b.addActionListener(this);
      bPnl.add(b);

      ClassLoader cl =(ClassLoader) this.getClass().getClassLoader();
      URL url = cl.getResource("net/jesktop/demos/alexwarp/bill.gif");
      ImageIcon      ii =new ImageIcon(url);

      mImage =ii.getImage();
      mWidth =ii.getIconWidth();
      mHeight=ii.getIconHeight();

      //     saveButton.setIcon(new ImageIcon(url));
      // get name of the image we're supposed to warp
      //              mImageName =  "c:\\dist\\bill.gif"; // default name
      // start initialization thread
      mStartup=new Thread(this);

      mStartup.start();
    }
  }

  /**
   * Method stop
   *
   *
   */
  public void stop() {

    if (mStartup != null) {
      mStartup.stop();
    }

    mStartup=null;

    if (mWarper != null) {
      mWarper.stop();
    }

    mWarper=null;
  }

  /**
   * Method actionPerformed
   *
   *
   * @param ae
   *
   */
  public void actionPerformed(ActionEvent ae) {

    if (ae.getActionCommand().equals("Undo")) {

      // user click on undo button
      int temp[]=mPixels;

      mPixels   =mOldPixels;
      mOldPixels=temp;
      mImage    =createImage(new MemoryImageSource(mWidth, mHeight, mPixels,
              0, mWidth));
      mRedo     =!mRedo;

      mUndoButton.setLabel(mRedo
                           ? "Redo"
                           : "Undo");
      graphicPanel.repaint();
    } else if (ae.getActionCommand().equals("Stop")) {

      // user click on stop button
      if (mWarper != null) {
        mWarper.stop();

        mWarper =null;
        mReady  =true;
        mCanUndo=false;

        mUndoButton.disable();
        graphicPanel.repaint();

        mStatus="Ready for warping. Click and drag in the image to warp it.";
      }
    } else if (ae.getActionCommand().equals("Reset")) {

      // user click on reset button
      if (mWarper != null) {
        mWarper.stop();

        mWarper=null;
      }

      if (mStartup != null) {
        mStartup.stop();

        mStartup=new Thread(this);

        mStartup.start();
      }
    }
  }

  // user begins to click-n-drag

  /**
   * Method mouseDown
   *
   *
   * @param x
   * @param y
   *
   * @return
   *
   */
  public boolean mouseDown(int x, int y) {

    if (!PointInImage(x, y) ||!mReady) {
      return false;
    }

    mFromPoint=new Point(x, y);

    return true;
  }

  // user continues dragging

  /**
   * Method mouseDrag
   *
   *
   * @param x
   * @param y
   *
   * @return
   *
   */
  public boolean mouseDrag(int x, int y) {

    if (mFromPoint == null) {
      return false;
    }

    // set mToPoint, so that paint will draw it
    if ((mToPoint == null) ||!mToPoint.equals(mFromPoint)) {
      mToPoint=ClipToImage(mFromPoint, x, y);

      graphicPanel.repaint();
    }

    return true;
  }

  // user is done dragging - start warping!

  /**
   * Method mouseUp
   *
   *
   * @param x
   * @param y
   *
   * @return
   *
   */
  public boolean mouseUp(int x, int y) {

    if (mFromPoint == null) {
      return false;
    }

    mReady =false;
    mStatus="Warping...";

    // swap new & old pixels; old pixels are saved in case of undo, new pixels will receive new image
    int temp[]=mOldPixels;

    mOldPixels=mPixels;
    mPixels   =temp;

    // start warp thread
    Point clipPoint=ClipToImage(mFromPoint, x, y);

    mWarper=new ImageWarper(
      this, mOldPixels, mPixels, mWidth, mHeight,
      new Point(clipPoint.x - kHOffset, clipPoint.y - kVOffset),
      new Point(mFromPoint.x - kHOffset, mFromPoint.y - kVOffset));

    mWarper.start();

    mFromPoint=mToPoint=null;

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
   * Method mousePressed
   *
   *
   * @param e
   *
   */
  public void mousePressed(MouseEvent e) {
    mouseDown(e.getX(), e.getY());
  }

  /**
   * Method mouseReleased
   *
   *
   * @param e
   *
   */
  public void mouseReleased(MouseEvent e) {
    mouseUp(e.getX(), e.getY());
  }

  /**
   * Method mouseDragged
   *
   *
   * @param e
   *
   */
  public void mouseDragged(MouseEvent e) {
    mouseDrag(e.getX(), e.getY());
  }

  /**
   * Method mouseMoved
   *
   *
   * @param e
   *
   */
  public void mouseMoved(MouseEvent e) {}

  // is this point inside the image area?
  boolean PointInImage(int x, int y) {
    return ((x >= kHOffset) && (x < kHOffset + mWidth) && (y >= kVOffset)
            && (y < kVOffset + mHeight));
  }

  // clip line to the image area. returns a point that contains replacements for x & y
  Point ClipToImage(Point from, int x, int y) {

    int dx=x - from.x, dy=y - from.y;

    if (dx == 0) {
      if (y < kVOffset) {
        y=kVOffset;
      }

      if (y >= kVOffset + mHeight) {
        y=kVOffset + mHeight - 1;
      }
    } else if (dy == 0) {
      if (x < kHOffset) {
        x=kHOffset;
      }

      if (x >= kHOffset + mWidth) {
        x=kHOffset + mWidth - 1;
      }
    } else {
      double slope=(double) dy / dx;

      if (x < kHOffset) {
        x=kHOffset;
        y=from.y + (int) (slope * (x - from.x));
      }

      if (x >= kHOffset + mWidth) {
        x=kHOffset + mWidth - 1;
        y=from.y + (int) (slope * (x - from.x));
      }

      if (y < kVOffset) {
        y=kVOffset;
        x=from.x + (int) ((y - from.y) / slope);
      }

      if (y >= kVOffset + mHeight) {
        y=kVOffset + mHeight - 1;
        x=from.x + (int) ((y - from.y) / slope);
      }
    }

    return new Point(x, y);
  }

  private class GraphicPanel extends JPanel {

    /**
     * Method getPreferredSize
     *
     *
     * @return
     *
     */
    public Dimension getPreferredSize() {
      return new Dimension(mWidth, mHeight);
    }

    /**
     * Method getMinimumSize
     *
     *
     * @return
     *
     */
    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    /**
     * Method paintComponent
     *
     *
     * @param g
     *
     */
    public void paintComponent(Graphics g) {

      // draw image
      if (mImage != null) {
        g.drawImage(mImage, kHOffset, kVOffset, this);
      }

      // if user is dragging, draw line
      if ((mFromPoint != null) && (mToPoint != null)) {
        g.setColor(Color.red);
        g.drawLine(mFromPoint.x, mFromPoint.y, mToPoint.x, mToPoint.y);
        g.setColor(Color.black);
      }
    }
  }
}


/*--- Formatted in Sun Java Convention Style on Fri, Nov 24, '00 ---*/


/*------ Formatted by Jindent 3.11 trial --- http://www.jindent.de ------*/
