// Authour Yang Kok Wah & Paul Hammant
// Date May 26, 2001
// JTetris.java

import java.awt.*;
import javax.swing.*;

public class JTetrisLauncher extends JPanel
{

  public JTetrisLauncher() {
      this.setLayout(new BorderLayout());
      this.setSize(300,410);
      JTetrisPanel jtp = new JTetrisPanel();
      this.add(jtp, BorderLayout.CENTER);
      jtp.init();
      //jta.focus();
  }
  public Dimension getPreferredSize() {
      return new Dimension(300,410);
  }
  public Dimension getMinimumSize() {
      return getPreferredSize();
  }
  public Dimension getMaximumSize() {
      return getPreferredSize();
  }


}




