// Authour Yang Kok Wah
// Date May 11, 2001
// JTetris.java

//import java.awt.*;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class JTetrisApplet extends JApplet
{

  JTetrisPanel jtp;
  public JTetrisApplet() {
      this.getContentPane().setLayout(new BorderLayout());
      jtp = new JTetrisPanel();
  }

public void init()
{
  getContentPane().add(jtp, BorderLayout.CENTER);
  jtp.init();
}


}




