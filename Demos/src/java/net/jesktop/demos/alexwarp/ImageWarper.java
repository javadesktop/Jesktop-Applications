package net.jesktop.demos.alexwarp;



import java.awt.*;
import java.awt.event.*;

import java.applet.*;

import javax.swing.JPanel;

import java.awt.image.*;


/*
 *  ImageWarper is the class that does the actual warping. Give it two
 *  pixels buffers - one with the original image, and one to hold the new
 *  image. It calls DoneWithWarping to let you know when it's all done.
 */

/**
 * Class ImageWarper
 *
 *
 * @author
 * @version %I%, %G%
 */
public class ImageWarper extends Thread {

  AlexWarp mAlexWarp;
  Point    mFromPoint, mToPoint;
  int      mFromPixels[], mToPixels[];
  int      mWidth, mHeight;    // width & height of warp image

  ImageWarper(AlexWarp j, int fromPixels[], int toPixels[], int w, int h,
              Point fromPoint, Point toPoint) {

    mAlexWarp  =j;
    mFromPixels=fromPixels;
    mToPixels  =toPixels;
    mFromPoint =fromPoint;
    mToPoint   =toPoint;
    mWidth     =w;
    mHeight    =h;
  }

  // warp the pixels, then notify the applet

  /**
   * Method run
   *
   *
   */
  public void run() {
    WarpPixels();
    mAlexWarp.DoneWithWarping();
  }

  // warp mFromPixels into mToPixels
  void WarpPixels() {

    int       dx=mToPoint.x - mFromPoint.x, dy=mToPoint.y - mFromPoint.y,
              dist=(int) Math.sqrt(dx * dx + dy * dy) * 2;
    Rectangle r =new Rectangle();
    Point     ne=new Point(0, 0), nw=new Point(0, 0), se=new Point(0, 0),
              sw=new Point(0, 0);

    // copy mFromPixels to mToPixels, so the non-warped parts will be identical
    System.arraycopy(mFromPixels, 0, mToPixels, 0, mWidth * mHeight);

    if (dist == 0) {
      return;
    }

    // warp northeast quadrant
    SetRect(r, mFromPoint.x - dist, mFromPoint.y - dist, mFromPoint.x,
            mFromPoint.y);
    ClipRect(r, mWidth, mHeight);
    SetPt(ne, r.x, r.y);
    SetPt(nw, r.x + r.width, r.y);
    SetPt(se, r.x, r.y + r.height);
    SetPt(sw, mToPoint.x, mToPoint.y);
    WarpRegion(r, nw, ne, sw, se);

    // warp nortwest quadrant
    SetRect(r, mFromPoint.x, mFromPoint.y - dist, mFromPoint.x + dist,
            mFromPoint.y);
    ClipRect(r, mWidth, mHeight);
    SetPt(ne, r.x, r.y);
    SetPt(nw, r.x + r.width, r.y);
    SetPt(se, mToPoint.x, mToPoint.y);
    SetPt(sw, r.x + r.width, r.y + r.height);
    WarpRegion(r, nw, ne, sw, se);

    // warp southeast quadrant
    SetRect(r, mFromPoint.x - dist, mFromPoint.y, mFromPoint.x,
            mFromPoint.y + dist);
    ClipRect(r, mWidth, mHeight);
    SetPt(ne, r.x, r.y);
    SetPt(nw, mToPoint.x, mToPoint.y);
    SetPt(se, r.x, r.y + r.height);
    SetPt(sw, r.x + r.width, r.y + r.height);
    WarpRegion(r, nw, ne, sw, se);

    // warp southwest quadrant
    SetRect(r, mFromPoint.x, mFromPoint.y, mFromPoint.x + dist,
            mFromPoint.y + dist);
    ClipRect(r, mWidth, mHeight);
    SetPt(ne, mToPoint.x, mToPoint.y);
    SetPt(nw, r.x + r.width, r.y);
    SetPt(se, r.x, r.y + r.height);
    SetPt(sw, r.x + r.width, r.y + r.height);
    WarpRegion(r, nw, ne, sw, se);
  }

  // warp a quadrilateral into a rectangle (double-secret magic code!)
  void WarpRegion(Rectangle fromRect, Point nw, Point ne, Point sw,
                  Point se) {

    int    dx   =fromRect.width, dy=fromRect.height;
    double invDX=1.0 / dx, invDY=1.0 / dy;

    for (int a=0; a < dx; a++) {
      double aa     =a * invDX;
      double x1     =ne.x + (nw.x - ne.x) * aa;
      double y1     =ne.y + (nw.y - ne.y) * aa;
      double x2     =se.x + (sw.x - se.x) * aa;
      double y2     =se.y + (sw.y - se.y) * aa;
      double xin    =x1;
      double yin    =y1;
      double dxin   =(x2 - x1) * invDY;
      double dyin   =(y2 - y1) * invDY;
      int    toPixel=fromRect.x + a + fromRect.y * mWidth;

      for (int b=0; b < dy; b++) {
        if (xin < 0) {
          xin=0;
        }

        if (xin >= mWidth) {
          xin=mWidth - 1;
        }

        if (yin < 0) {
          yin=0;
        }

        if (yin >= mHeight) {
          yin=mHeight - 1;
        }

        int pixelValue=mFromPixels[(int) xin + (int) yin * mWidth];

        mToPixels[toPixel]=pixelValue;
        xin               +=dxin;
        yin               +=dyin;
        toPixel           +=mWidth;
      }
    }
  }

  void ClipRect(Rectangle r, int w, int h) {

    if (r.x < 0) {
      r.width+=r.x;
      r.x    =0;
    }

    if (r.y < 0) {
      r.height+=r.y;
      r.y     =0;
    }

    if (r.x + r.width >= w) {
      r.width=w - r.x - 1;
    }

    if (r.y + r.height >= h) {
      r.height=h - r.y - 1;
    }
  }

  // SetRect and SetPt are Mac OS functions. I wrote my own versions here
  // so I didn't have to rewrite too much of the code.
  void SetRect(Rectangle r, int left, int top, int right, int bottom) {

    r.x     =left;
    r.y     =top;
    r.width =right - left;
    r.height=bottom - top;
  }

  void SetPt(Point pt, int x, int y) {
    pt.x=x;
    pt.y=y;
  }
}


/*--- Formatted in Sun Java Convention Style on Fri, Nov 24, '00 ---*/


/*------ Formatted by Jindent 3.11 trial --- http://www.jindent.de ------*/
