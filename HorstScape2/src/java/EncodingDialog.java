import horst.webwindow.*;

import java.awt.*;
import java.awt.event.*;

public class EncodingDialog extends Dialog
implements ActionListener
{
    Panel m_controls;
    TextField m_name;
    Button m_ok;
    Button m_cancel;
    HTMLPane m_renderer;
    
    public EncodingDialog(Frame parent, String title, HTMLPane renderer)
    {
        super(parent, title);
        m_renderer = renderer;
        add(createControls());
        setSize(250,120);
        centerDialog(this);
    }
    
    void centerDialog(Dialog f)
    {
		Dimension d1 = f.getSize();
		Dimension d2 = getToolkit().getScreenSize();
		int frameWidth = d1.width;
		int frameHeight = d1.height;
		int screenWidth = d2.width;
		int screenHeight = d2.height;
		int xPos = Math.max((screenWidth - frameWidth)/2, 0);
		int yPos = Math.max((screenHeight - frameHeight)/2, 0);
		f.setLocation(xPos, yPos);
    }
    
    public Panel createControls()
    {
        String name = m_renderer.getCharacterEncoding();
        m_name = new TextField(40);
        if (name != null)
            m_name.setText(name);
        
        m_ok = new Button("OK");
        m_cancel = new Button("Cancel");
        
        m_ok.addActionListener(this);
        m_cancel.addActionListener(this);
        
        Panel p = new Panel();
        p.setLayout(new GridBagLayout());
        
        addComponent(p, new Label("Encoding:"),
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        1, 1,
        0, 0,
        new Insets(5,5,5,5), 0, 0,
        0.0, 0.0);
        
        addComponent(p, m_name,
        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        1, 1,
        1, 0,
        new Insets(5,5,5,5), 0, 0,
        1.0, 0.0);
        
        Panel p2 = new Panel();
        p2.add(m_ok);
        p2.add(m_cancel);
        
        addComponent(p, p2,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        1, 2,
        0, 1,
        new Insets(5,5,5,5), 0, 0,
        1.0, 1.0);
        
        return p;
    }
    
    protected void addComponent(Container con, Component comp,
        int anchor, int fill,
        int gridheight, int gridwidth,
        int gridx, int gridy,
        Insets insets, int ipadx, int ipady,
        double weightx, double weighty)
    {
        GridBagLayout gridbag = (GridBagLayout) con.getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = anchor; c.fill = fill; c.gridheight = gridheight;
        c.gridwidth = gridwidth; c.gridx = gridx; c.gridy = gridy;
        c.insets = insets; c.ipadx = ipadx; c.ipady = ipady;
        c.weightx = weightx; c.weighty = weighty;
        gridbag.setConstraints(comp,  c);
        con.add(comp);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == m_cancel)
        {
            dispose();
        }
        else if (e.getSource() == m_ok)
        {
            m_renderer.setCharacterEncoding(m_name.getText());
            dispose();
        }
    }
}