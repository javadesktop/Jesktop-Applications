import java.awt.*;

import javax.swing.*;

class MainPanel extends Container
{
    boolean bBorder = false;
    
    MainPanel()
    {
    }
    
    MainPanel(boolean border)
    {
        bBorder = border;
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }
    
    public void paint(Graphics g)
    {
        Dimension d = getSize();
        g.setColor(SystemColor.control);
        g.fillRect(0,0,d.width, d.height);
        
        if (bBorder)
        {
            g.setColor(Color.black);
            g.drawRect(4,4,d.width-9, d.height-9);
        }
        super.paint(g);
    }
}