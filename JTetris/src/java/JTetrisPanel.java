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

public class JTetrisPanel extends JPanel
{

private JTetris tetrisPanel=new JTetris();

public JTetrisPanel() {
    setLayout(new BorderLayout());
        System.out.println("JTetrisPanel..");
}

public static void main(String args[])
{
    System.out.println("Starting JTetris...");


    MYFrame frame=new MYFrame("Java Tetris");

    JTetrisApplet applet=new JTetrisApplet();

    frame.setSize(300,410);

    frame.getContentPane().add(applet,BorderLayout.CENTER);

    applet.init();
    applet.start();


    frame.setVisible(true);

    //applet.focus();

}


public void init()
{
  add(tetrisPanel, BorderLayout.CENTER);
  focus();
  System.out.println("JTetrisPanel.init()");
}


public void focus()
{
 tetrisPanel.requestFocus();
}

}



class MYFrame extends JFrame
{
 public MYFrame(String title)
 {
        super(title);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
                System.exit(0);
            }
        });
 }
}


class JTetris extends JPanel implements KeyListener, ActionListener, ItemListener
{
    private  final int MAXPIECE=6;
    private  Board gameBoard,previewBoard;

    private  ShapeDescriptor
                    square,tee,Lright,Lleft,Zright,Zleft,Ishape;
    private  ShapeDescriptor thisShape,previewShape;
    private  Timer timer;
    private  JButton buttonStart,buttonPause;

    private  ButtonGroup btg;
    private  JRadioButton jrbMedium,jrbFast,jrbSlow;

    private  JCheckBox jcbObstructions;

    private  int blockSize=15;

    private  boolean gameActive=false;
    private  boolean gameOver=false;
    private  boolean showGamePaused=false;
    private  boolean gamePaused=false;
    private  boolean gameObstructions=false;

    private  boolean nextPiece=false;
    private  boolean pieceDone=false;
    private  int linesCleared=0;


    public JTetris()
    {
        timer= new Timer(300,this);

/*      addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
                System.exit(0);
            }
        });
*/
      addKeyListener(this);

      this.setSize(300, 400);
//    this.setTitle("JTetris");
//    this.setVisible(true);
      this.requestFocus();
//      this.getContentPane().setLayout(null);
      this.setLayout(null);

      buttonStart=new JButton("New Game");
      buttonPause=new JButton("Pause");
System.out.println("JTetrisPanel..2");

      jcbObstructions=new JCheckBox("Obstructions");

      btg=new ButtonGroup();

      jrbFast=new JRadioButton("Fast",false);
      jrbMedium=new JRadioButton("Medium",true);
      jrbSlow=new JRadioButton("Slow",false);


      btg.add(jrbFast);btg.add(jrbMedium);btg.add(jrbSlow);


//      this.getContentPane().add(buttonStart);
      this.add(buttonStart);
      buttonStart.setSize(95,20);
      buttonStart.setLocation(190,310);
      buttonStart.setVisible(true);

      this.add(buttonPause);
      buttonPause.setSize(95,20);
      buttonPause.setLocation(190,290);
      buttonPause.setVisible(false);

      this.add(jcbObstructions);
      jcbObstructions.setSize(100,20);
      jcbObstructions.setLocation(190,270);

//      this.getContentPane().add(jrbFast);
      this.add(jrbFast);
      jrbFast.setSize(80,20);
      jrbFast.setLocation(190,200);
//      jrbFast.setVisible(true);

//      this.getContentPane().add(jrbMedium);
      this.add(jrbMedium);
      jrbMedium.setLocation(190,220);
      jrbMedium.setSize(80,20);
//      jrbMedium.setVisible(true);

//      this.getContentPane().add(jrbSlow);
     this.add(jrbSlow);
      jrbSlow.setLocation(190,240);
      jrbSlow.setSize(80,20);
//      jrbSlow.setVisible(true);

      jrbFast.addItemListener(this);
      jrbMedium.addItemListener(this);
      jrbSlow.addItemListener(this);

//      this.getContentPane().setVisible(true);
//      this.setVisible(true);

      buttonStart.addActionListener(this);
      buttonPause.addActionListener(this);

      jcbObstructions.addItemListener(this);
        //Boards
        gameBoard=new Board(2,1,21,10);
        previewBoard=new Board(13,6,5,5);

        //ShapeDescriptor square
        square=new ShapeDescriptor(4,Color.red);
        square.Points=new ShapePoint[]{new ShapePoint(0,0),
                                    new ShapePoint(0,-1),
                                    new ShapePoint(1,0),
                                    new ShapePoint(1,-1)
                                    };
        square.x=7;
        square.y=4;

        //ShapeDescriptor Lright
        Lright=new ShapeDescriptor(4,Color.green);
        Lright.Points=new ShapePoint[]{new ShapePoint(0,0),
                                    new ShapePoint(0,-1),
                                    new ShapePoint(0,1),
                                    new ShapePoint(1,1)
                                    };
        Lright.x=7;
        Lright.y=4;


        //ShapeDescriptor Lleft
        Lleft=new ShapeDescriptor(4,Color.blue);
        Lleft.Points=new ShapePoint[]{new ShapePoint(0,0),
                                    new ShapePoint(0,-1),
                                    new ShapePoint(0,1),
                                    new ShapePoint(-1,1)
                                    };
        Lleft.x=7;
        Lleft.y=4;

        //ShapeDescriptor Zleft
        Zleft=new ShapeDescriptor(4,Color.yellow);
        Zleft.Points=new ShapePoint[]{new ShapePoint(0,0),
                                      new ShapePoint(-1,-1),
                                      new ShapePoint(0,-1),
                                      new ShapePoint(1,0)
                                      };
        Zleft.x=7;
        Zleft.y=4;


         //ShapeDescriptor Zright
        Zright=new ShapeDescriptor(4,Color.orange);
        Zright.Points=new ShapePoint[]{new ShapePoint(0,0),
                                      new ShapePoint(-1,0),
                                      new ShapePoint(1,-1),
                                      new ShapePoint(0,-1)
                                      };
        Zright.x=7;
        Zright.y=4;

        //ShapeDescriptor Ishape

        Ishape=new ShapeDescriptor(3,Color.pink);
        Ishape.Points=new ShapePoint[]{new ShapePoint(0,0),
                                      new ShapePoint(0,-1),
                                      new ShapePoint(0,1),
                                     };
        Ishape.x=7;
        Ishape.y=4;


        previewShape=pickRandomShape();
        previewShape.x=15;previewShape.y=8;


        gameActive=false;

        repaint();

    }


 ShapeDescriptor pickRandomShape()
    {
      switch ((int)(Math.random()*MAXPIECE))
      {
        case 0:
           return new ShapeDescriptor(square);
        case 1:
           return new ShapeDescriptor(Lleft);
        case 2:
           return new ShapeDescriptor(Lright);
        case 3:
            return new ShapeDescriptor(Zleft);
        case 4:
          return new ShapeDescriptor(Zright);
        case 5:
         return new ShapeDescriptor(Ishape);
        }

        return null;

    }


    void addObstructions()
    {
      int n,numObstructions;

      ShapeDescriptor s;

      numObstructions=(int)(Math.random()*6) + 2;
      n=0;

      Graphics g=this.getGraphics();

      while (n<numObstructions)
       {
          s=pickRandomShape();
          s.color=Color.white;
          s.y=(int)(Math.random()*gameBoard.row + gameBoard.top + 8);
          s.x=(int)(Math.random()*gameBoard.col+gameBoard.left);

          if (drawShapeInsideBoard(s,g)) n++;
      }

      g.dispose();
    }


    void startNewGame()
    {
        //Boards
        gameBoard=new Board(2,1,21,10);

        if (gameObstructions) addObstructions();

        linesCleared=0;

        thisShape=previewShape;

        thisShape.x=gameBoard.left+gameBoard.col/2;
        thisShape.y=gameBoard.top+1;


        previewShape=pickRandomShape();
        previewShape.x=15;previewShape.y=8;


        gameActive=true;
        gameOver=false;
        showGamePaused=true;

        repaint();

//        this.requestFocus();

        pauseGame(false);
        //timer.start();

    }


public void pauseGame(boolean status)
{
  gamePaused=status;
  if (status)
   {
     buttonPause.setText("Continue");
     gameActive=false;
     timer.stop();
   }
  else
   {
     buttonPause.setText("Pause");
     gameActive=true;
     timer.start();
    }
 repaint();
 this.requestFocus();
}

public void paint(Graphics g)
{

 buttonPause.setVisible(showGamePaused);

 super.paint(g);
 g.drawString(new String("Next Piece"),200,80);
 if (gameBoard!=null) drawBoard(gameBoard,g);
 if (thisShape!=null) drawShape(thisShape,g);
 if ((previewBoard!=null) && (previewShape!=null))
    showPreviewPiece(g);

 showScore(g);
 showInstructions(g);
 showGameOverMessage(g);
 showPauseGameMessage(g);
 if (gameOver) drawShapeNoChecking(thisShape,g);

}

void showPauseGameMessage(Graphics g)
{
 if (gamePaused && buttonPause.isVisible())
  {
   g.setColor(Color.white);
   g.setFont(new Font("SanSerif",Font.BOLD,18));
   g.drawString("GAME PAUSED",40,140);
  }

}


void showScore(Graphics g)
{
 g.setColor(this.getBackground());
 g.fillRect(200,30-20,100,20);
 g.setColor(Color.black);
 g.drawString("Score : " + linesCleared,200,30);
}

void showInstructions(Graphics g)
{
 g.setColor(this.getBackground());
 g.fillRect(30,380-45,100,45);
 g.setColor(Color.red);

 g.setFont(new Font("SanSerif",Font.BOLD,11));
 g.drawString("INSTRUCTIONS",30,350);
 g.drawLine(30,350,110,350);

 g.setColor(Color.black);
 g.setFont(new Font("SanSerif",Font.PLAIN,10));
 g.drawString("Left>move left   Up>rotate   Right>move right",30,365);
 g.drawString("Click on Applet to get Keyboard focus",30,380);
}




void showPreviewPiece(Graphics g)
{
  drawBoard(previewBoard,g);
  drawShapeNoChecking(previewShape,g);
}


boolean checkPieceDone()
{
 return pieceDone;
}


boolean checkPointInsideBoard(ShapePoint s,int x,int y)
{
  int col=s.xOff+x-gameBoard.left,row=s.yOff+y-gameBoard.top;
  if ((row>=gameBoard.row) ||
      (col>=gameBoard.col) ||
      (col<0))
     return false;
  return true;
}

boolean checkPointOK(ShapePoint s,int x,int y)
{
  int col=s.xOff+x-gameBoard.left,row=s.yOff+y-gameBoard.top;
  if ((row>=gameBoard.row) ||
      (col>=gameBoard.col) ||
      (col<0))
     return false;
  return gameBoard.cells[row][col].avail;
}


void updateBoard(ShapeDescriptor s,boolean erase)
{
  int r,c;
  for (int i=0;i<s.numPoints;i++)
    {
     r=s.y+s.Points[i].yOff-gameBoard.top;
     c=s.x+s.Points[i].xOff-gameBoard.left;

     gameBoard.cells[r][c].avail=erase;
     if (erase)
       gameBoard.cells[r][c].color=Color.black;
     else
       gameBoard.cells[r][c].color=s.color;
    }
}


public void actionPerformed(ActionEvent e)
{
  //    this.requestFocus();
      if (nextPiece)
      {
        nextPiece=false;
        pieceDone=false;

        thisShape=new ShapeDescriptor(previewShape);
        thisShape.x=gameBoard.left+gameBoard.col/2;
        thisShape.y=gameBoard.top+1;


        previewShape=pickRandomShape();
        previewShape.x=15;previewShape.y=8;

        Graphics g=this.getGraphics();

        showPreviewPiece(g);
        showScore(g);


        boolean ret=true;

        if (thisShape!=null)
          ret=drawShape(thisShape,g);

        if (!ret)
         {
        //  gameActive=false;
        //  timer.stop();
         //gameOver(g);
          gameOver=true;
         // g.dispose();
          pauseGame(true);
         // showGameOverMessage(g);
          showGamePaused=false;
          repaint();
//        drawShapeNoChecking(thisShape,g);
          return;
         }

        g.dispose();
        return;
      }

      if (gameActive)
      {
       Graphics g=this.getGraphics();
       clearShape(thisShape,g);

       int y=thisShape.y;
       thisShape.y++;

       if (! drawShape(thisShape,g))
        {
          thisShape.y=y;
          drawShape(thisShape,g);
          pieceDone=true;
        };

       g.dispose();

       if (checkPieceDone())
       {
         while (true)
         {
          int line=checkLine();
          if (line==-1) break;
          clearLine(line);
          linesCleared++;
         }

         nextPiece=true;

         return;
       }

      }

      if (e.getSource().equals(buttonStart))
       {


           timer.stop();
      //    int ret= JOptionPane.showConfirmDialog
      //      (this,new String("Start New Game?"),new String("Confirm"),
      //      JOptionPane.YES_NO_OPTION);

      //    this.requestFocus();

       //   if (ret==0)
       //    {
            startNewGame();
       //     return;
       //     }
       //   timer.start();

       }

       if (e.getSource().equals(buttonPause))
        { if (buttonPause.getText()=="Pause")
            pauseGame(true);
          else
            pauseGame(false);
        }
}


void rotateShape(ShapeDescriptor s)
{
 ShapeDescriptor test=new ShapeDescriptor(s);

 for (int i=0;i<test.numPoints;i++)
  {

   int temp=test.Points[i].xOff;
   test.Points[i].xOff=test.Points[i].yOff;
   test.Points[i].yOff=(-1)*temp;

   if (!checkPointOK(test.Points[i],test.x,test.y))
     return;
  }


  for (int i=0;i<s.numPoints;i++)
   {
    s.Points[i].xOff=test.Points[i].xOff ;
    s.Points[i].yOff=test.Points[i].yOff;
   }
}

//void gameOver(g)
//{
  // JOptionPane.showMessageDialog(this,new String("Game Over"));
//}

void showGameOverMessage(Graphics g)
{
// g.setColor(Color.black);
// g.fillRect(50,150-30,130,30);

 if (gameOver)
  {
   g.setColor(Color.white);
   g.setFont(new Font("SanSerif",Font.BOLD,18));
   g.drawString("GAME OVER",55,140);
  }
}


void drawShapeNoChecking(ShapeDescriptor s,Graphics g)
{
    g.setColor(s.color);
    for (int i=0;i<s.numPoints;i++)
      drawPoint(s.Points[i],s.x,s.y,g);
}


boolean drawShapeInsideBoard(ShapeDescriptor s,Graphics g)
{
   for (int i=0;i<s.numPoints;i++)
   if (! checkPointInsideBoard(s.Points[i],s.x,s.y))
     return false;

    g.setColor(s.color);
    for (int i=0;i<s.numPoints;i++)
      drawPoint(s.Points[i],s.x,s.y,g);

    updateBoard(s,false);

   return true;

}

boolean drawShape(ShapeDescriptor s,Graphics g)
{
  for (int i=0;i<s.numPoints;i++)
   if (! checkPointOK(s.Points[i],s.x,s.y))
     return false;

    g.setColor(s.color);
    for (int i=0;i<s.numPoints;i++)
      drawPoint(s.Points[i],s.x,s.y,g);

    updateBoard(s,false);

   return true;
}


//return line number of line to clear
int checkLine()
{
  boolean lineFound=true;
  for (int r=gameBoard.row-1;r>=0;r--)
   {
     lineFound=true;
     for(int c=0;c<gameBoard.col;c++)
       if (gameBoard.cells[r][c].avail)
         {
           lineFound=false;
           break;
         }
     if(lineFound) return r;
    }

  return -1; //no line to clear;
}


void clearLine(int line)
{
  for (int r=line;r>0;r--)
     for(int c=0;c<gameBoard.col;c++)
       gameBoard.cells[r][c]=gameBoard.cells[r-1][c];
  for(int c=0;c<gameBoard.col;c++)
    gameBoard.cells[0][c]=new Cell();

  Graphics g=this.getGraphics();
  if (gameBoard!=null) drawBoard(gameBoard,g);
  g.dispose();
}


void drawPoint(ShapePoint p,int sx,int sy,Graphics g)
{
    g.draw3DRect((sx+p.xOff)*blockSize,(sy+p.yOff)*blockSize,blockSize,blockSize,true);
    g.fill3DRect((sx+p.xOff)*blockSize,(sy+p.yOff)*blockSize,blockSize,blockSize,true);
}


void clearShape(ShapeDescriptor s,Graphics g)
{

   g.setColor(Color.black);
    for (int i=0;i<s.numPoints;i++)
      drawPoint(s.Points[i],s.x,s.y,g);

   updateBoard(s,true);
}


void drawBoard(Board b,Graphics g)
{
  for (int r=0;r<b.row;r++)
    for (int c=0;c<b.col;c++)
     drawCell(b.cells[r][c],b.left,b.top,r,c,g);
}

void drawCell(Cell c,int sx,int sy,int rowno,int colno,Graphics g)
{
  g.setColor(c.color);
  g.draw3DRect((colno+sx)*blockSize,(rowno+sy)*blockSize,
                blockSize,blockSize,true);
  g.fill3DRect((colno+sx)*blockSize,(rowno+sy)*blockSize,
                blockSize,blockSize,true);
}

public void keyPressed(KeyEvent e) {
    if (!gameActive) return;

    Graphics g=this.getGraphics();

    clearShape(thisShape,g);

    int x=thisShape.x,y=thisShape.y;

    int key=e.getKeyCode();
    switch (key)
    {
      case KeyEvent.VK_LEFT:
       thisShape.x--; break;
      case KeyEvent.VK_RIGHT:
       thisShape.x++; break;
      case KeyEvent.VK_SPACE:
      case KeyEvent.VK_DOWN:
         fastDrop(thisShape);break;
      case KeyEvent.VK_UP:
       rotateShape(thisShape);
       break;

      default:break;
    }

    if (!drawShape(thisShape,g))
     {
      thisShape.x=x;
      thisShape.y=y;
      drawShape(thisShape,g);
     }
     else
     {
        nextPiece=false;
        pieceDone=false;
        };

    g.dispose();

}


void fastDrop(ShapeDescriptor s)
{
 int row=s.y-gameBoard.top;
 int y=s.y;

 for(int r=row;r<gameBoard.row;r++)
  {
    y=s.y;
    s.y++;
    for(int i=0;i<s.numPoints;i++)
      if (!checkPointOK(s.Points[i],s.x,s.y))
        {
          s.y=y;
          return;
        }

  }
}

public void keyReleased(KeyEvent parm1) {
}

public void keyTyped(KeyEvent parm1) {
}

public void itemStateChanged(ItemEvent e) {

    if (e.getSource() instanceof JRadioButton)
    {

        timer.stop();

        if (jrbFast.isSelected())
         timer.setDelay(150);
        if (jrbMedium.isSelected())
         timer.setDelay(300);
        if (jrbSlow.isSelected())
         timer.setDelay(500);

        if (gameActive) timer.start();

        this.requestFocus();

    }

    if (e.getSource().equals(jcbObstructions))
    {
      if (jcbObstructions.isSelected())
        gameObstructions=true;
      else
        gameObstructions=false;

    }
  }
}


class ShapePoint
{
  int xOff ;
  int yOff ;

  ShapePoint(int x,int y)
   {
     xOff=x;
     yOff=y;
   }

  ShapePoint()
   {
     this(0,0);
    }

}


class ShapeDescriptor
{
   int numPoints;
   ShapePoint[] Points;
   int ID;
   int x;
   int y;
   Color color;

   ShapeDescriptor(ShapeDescriptor s)
   {
     this(s.numPoints,s.color);
     for (int i=0;i<s.numPoints;i++)
      {
      Points[i].xOff=s.Points[i].xOff;
      Points[i].yOff=s.Points[i].yOff;
      }
     this.x=s.x;
     this.y=s.y;

   }

   ShapeDescriptor(int n,Color c)
   {
      Points=new ShapePoint[n];
      numPoints=n;
      color=c;
      for (int i=0;i<n;i++)
       Points[i]=new ShapePoint();
  }
}


class Cell
{
  boolean avail;
  Color color;

  Cell()
   {
     avail=true;
     color=Color.black;
   }
}

class Board
{
  int col;
  int row;
  int top;
  int left;
  Cell[][] cells;

  Board(int left,int top,int row,int col)
   {
     this.left=left;
     this.top=top;
     this.row=row;
     this.col=col;

     cells=new Cell[row][col];
     for (int r=0;r<row;r++)
      for (int c=0;c<col;c++)
        cells[r][c]=new Cell();

   }

}




