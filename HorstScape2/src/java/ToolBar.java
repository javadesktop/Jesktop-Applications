import java.awt.*;

import javax.swing.*;

public class ToolBar extends Container
{
    JPanel m_buttonPanel;
    
    public ToolBar()
    {
        m_buttonPanel = new JPanel();
        m_buttonPanel.setBackground(SystemColor.control);
        setLayout(new GridBagLayout());
        Helper.addComponent(this, m_buttonPanel,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            1, 1,
                            0, 0,
                            new Insets(5,5,5,5), 0, 0,
                            1.0, 0.0);
    }
    
    public void paint(Graphics g)
    {
        Dimension d = getSize();
        g.setColor(SystemColor.control);
        g.fillRect(0,0,d.width,d.height);
        super.paint(g);
    }
    
    public void addButton(ImageButton btn)
    {
        m_buttonPanel.add(btn);
    }
}