import java.awt.*;

import javax.swing.*;

public class LocationPanel extends Container 
{ 
    JTextField textField; 
    
    public LocationPanel(ImageButton img) 
    { 
        setBackground(SystemColor.control);  
        setLayout(new GridBagLayout()); 
      
        MyLabel label = new MyLabel(); 
        label.setText("Location:");
        textField = new JTextField("http://www.yahoo.com"); 
      
        Helper.addComponent(this, label,
                          GridBagConstraints.WEST, GridBagConstraints.NONE,
                          1, 1,
                          0, 0,
                          new Insets(5,5,5,5), 0, 0,
                          0.0, 0.0);
        Helper.addComponent(this, img,
                          GridBagConstraints.WEST, GridBagConstraints.NONE,
                          1, 1,
                          1, 0,
                          new Insets(5,5,5,5), 0, 0,
                          0.0, 0.0);
        Helper.addComponent(this, textField,
                          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                          1, 1,
                          2, 0,
                          new Insets(5,5,5,5), 0, 0,
                          1.0, 0.0);
    } 

    public void setLocation(String txt)
    {
        textField.setText(txt);
    }
    
    public void paint(Graphics g)
    {
        Dimension d = getSize();
        g.setColor(SystemColor.control);
        g.fillRect(0,0,d.width, d.height);
        super.paint(g);
    }
} 
