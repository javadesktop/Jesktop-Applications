import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.URL;


public class StatusBar extends Panel
{
    private Label label;
    
    public StatusBar()
    {
        label = new Label("orst");
        
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 1.0;
        c.weighty = 0.0;
   	    c.gridwidth = GridBagConstraints.REMAINDER;//1;
 	    c.gridheight = 1;
        c.insets = new Insets(0, 2, 0, 2);
        g.setConstraints(label, c);
        setLayout(g);
        label.setBackground(Color.yellow);
        add(label);
    }
    
    public void setText(String txt)
    {
       //label.setText(" " + txt);
    }
}