package net.jesktop.demos.alexwarp;



import java.awt.*;

import javax.swing.*;

import java.util.*;

import org.jesktop.frimble.*;


/**
 * Class AlexWarpWrapper
 *
 *
 * @author
 * @version %I%, %G%
 */
public class AlexWarpWrapper extends JPanel implements FrimbleAware {

  AlexWarp alexWarp;

  /**
   * Constructor AlexWarpWrapper
   *
   *
   */
  public AlexWarpWrapper() {

    System.out.println("alexwrapper created");

    alexWarp=new AlexWarp();

    this.add(alexWarp, BorderLayout.CENTER);
  }

  // Javadocs will automatically import from interface.
  public void setFrimble(Frimble f) {

    System.out.println("frimble set");
    f.addFrimbleListener(new FrimbleAdapter() {

      public void frimbleOpened(FrimbleEvent e) {

        //alexWarp.init();
        alexWarp.start();
      }

      public void frimbleClosing(FrimbleEvent e) {

        alexWarp.stop();

        alexWarp=null;
      }
    });
  }
}


/*--- Formatted in Sun Java Convention Style on Fri, Nov 24, '00 ---*/


/*------ Formatted by Jindent 3.11 trial --- http://www.jindent.de ------*/
