package net.jesktop.demos.julia3;



import java.awt.*;

import javax.swing.*;

import java.util.*;

import org.jesktop.frimble.*;


/**
 * Class Julia3Wrapper
 *
 *
 * @author
 * @version %I%, %G%
 */
public class Julia3Wrapper extends JPanel implements FrimbleAware {

  Julia3 julia3;

  /**
   * Constructor Julia3Wrapper
   *
   *
   */
  public Julia3Wrapper() {

    julia3=new Julia3();

    this.add(julia3, BorderLayout.CENTER);
  }

  // Javadocs will automatically import from interface.
  public void setFrimble(Frimble f) {

    f.addFrimbleListener(new FrimbleAdapter() {

      public void frimbleOpened(FrimbleEvent e) {
        julia3.init();
        julia3.start();
      }

      public void frimbleClosing(FrimbleEvent e) {

        julia3.stop();

        julia3=null;
      }
    });
  }
}


/*--- Formatted in Sun Java Convention Style on Fri, Nov 24, '00 ---*/


/*------ Formatted by Jindent 3.11 trial --- http://www.jindent.de ------*/
