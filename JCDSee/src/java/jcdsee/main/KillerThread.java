package jcdsee.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.jesktop.frimble.*;

/**
 * Used to terminate started JFrames
 */
public class KillerThread extends FrimbleAdapter {
  private int totCount;

  /** Constructor
   */
  public KillerThread() { totCount = 0; }

  /**
   * Listens for close window events
   * and exits when all started windows are closed
   */
  public synchronized void frimbleClosing(FrimbleEvent e) {
    if (--totCount == 0)
      System.exit(0);
  }

  /**
   * Adds a window to wait for
   */
  public void addWindow() { ++totCount; }
}
