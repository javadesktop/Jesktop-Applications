import java.awt.*;
import java.net.URL;

import javax.swing.*;

public class StatusPanel extends JPanel 
implements Runnable
{ 
    Image [] m_images;
    ImageButton m_img;
    MyLabel linkLabel;
    MyLabel memoryLabel;
    MyLabel usedLabel;
    MyLabel totalLabel;
    MainPanel linkPanel; 
    MainPanel memoryPanel;
    JSlider m_zoomSlider;
    
    boolean m_bShowReadingData;
    
    public StatusPanel(String imgDir)
    { 
        setBackground(SystemColor.control);
        m_images = new Image[3];
        m_images[0] = Helper.getImage((Component)this, imgDir + "1.gif");
        m_images[1] = Helper.getImage((Component)this, imgDir + "2.gif");
        m_images[2] = Helper.getImage((Component)this, imgDir + "3.gif");
       // m_images[3] = Helper.getImage((Component)this, imgDir + "4.gif");
        m_img = new ImageButton(m_images[0], "");
        m_img.bCanHighlight = false;
        
        new Thread(new ConnectionThread()).start();
       
        memoryLabel = new MyLabel("Memory: ");
        linkLabel = new MyLabel(); 
        
        linkPanel = new MainPanel(); 
        linkPanel.setLayout(new GridBagLayout()); 
        
        Helper.addComponent(linkPanel, linkLabel,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            1, 1,
                            0, 0,
                            new Insets(0,0,0,0), 0, 0,
                            1.0, 0.0);
        Helper.addComponent(linkPanel, m_img,
                            GridBagConstraints.EAST, GridBagConstraints.NONE,
                            1, 1,
                            1, 0,
                            new Insets(0,0,0,0), 0, 0,
                            0.0, 0.0);                    
        m_zoomSlider = new JSlider(1,200, 100);
        m_zoomSlider.setPreferredSize(new Dimension(140,14));
        memoryPanel = new MainPanel();
        //memoryPanel.bBorder = true;
        usedLabel = new MyLabel();
        usedLabel.m_textColor = Color.red;
        totalLabel = new MyLabel();
        totalLabel.m_textColor = Color.blue;
        memoryPanel.setLayout(new GridBagLayout()); 
        Helper.addComponent(memoryPanel, memoryLabel,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            1, 1,
                            0, 0,
                            new Insets(0,0,0,0), 0, 0,
                            0.0, 0.0);
        Helper.addComponent(memoryPanel, usedLabel,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            1, 1,
                            1, 0,
                            new Insets(0,0,0,0), 0, 0,
                            0.0, 0.0);
        Helper.addComponent(memoryPanel, totalLabel,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            1, 1,
                            2, 0,
                            new Insets(0,0,0,0), 0, 0,
                            0.0, 0.0);                            
        Helper.addComponent(memoryPanel, new MyLabel("Zoom:"),
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            1, 1,
                            3, 0,
                            new Insets(0,10,0,0), 0, 0,
                            0.0, 0.0);                    
        Helper.addComponent(memoryPanel, m_zoomSlider,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            1, 1,
                            4, 0,
                            new Insets(0,0,0,0), 0, 0,
                            1.0, 0.0);
        setLayout(new GridBagLayout()); 
        Helper.addComponent(this, linkPanel,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            1, 1,
                            0, 0,
                            new Insets(5,5,5,5), 0, 0,
                            1.0, 0.0);
        Helper.addComponent(this, memoryPanel,
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                            1, 1,
                            0, 1,
                            new Insets(0,0,0,0), 0, 0,
                            1.0, 0.0);
        Thread t = new Thread(this);
        t.start();
    } 
    
    public void run()
    {
        while (true)
        {
            try 
            {
                long free = Runtime.getRuntime().freeMemory();
                long total = Runtime.getRuntime().totalMemory();
                long used = total - free;
                String s = " Used: " + used/1024 + "Kbytes";
                usedLabel.setText(s);
                s = " Total: " + total/1024 + "KBytes";
                totalLabel.setText(s);
                Thread.currentThread().sleep(500);
            }
            catch (Exception e)
            {
            }
        }
    }
    
    public void paint(Graphics g)
    {
        Dimension d = getSize();
        g.setColor(SystemColor.control);
        g.fillRect(0,0,d.width, d.height);
        super.paint(g);
    }

    void setLabel(String s) 
    { 
        linkLabel.setText(s);
    } 
    
    class ConnectionThread implements Runnable
    {
        public void run()
        {
            int idx = 0;
            while (true)
            {
                try 
                {
                    if (m_bShowReadingData)
                    {
                        m_img.setImage(m_images[idx]);
                        m_img.repaint();
                        if (idx == (m_images.length - 1))
                            idx = 0;
                        else idx++;
                    }
                    else
                    {
                        if (idx != 0)
                        {
                            idx = 0;
                            m_img.setImage(m_images[idx]);
                            m_img.repaint();
                        }
                    }
                    Thread.currentThread().sleep(200);
                }
                catch (Exception e)
                {
                }
            }
        }
    }
} 
