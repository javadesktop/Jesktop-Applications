import java.awt.*;
import java.awt.event.*;

import horst.webwindow.*;

public class FindTextDialog extends Dialog
implements ActionListener
{
    public Panel m_controls;
    public TextField m_text;
    public Button m_ok;
    public Button m_cancel;
    WebWindow m_htmlWindow;
    Frame m_frame;
    
    public FindTextDialog(Frame parent, String title, WebWindow w)
    {
        super(parent, title);
        add(createControls());
        setSize(300,130);
        m_htmlWindow = w;
        m_frame = parent;
        center();
    }
    
    void center()
    {
		Dimension d1 = getSize();
		Dimension d2 = getToolkit().getScreenSize();
		int frameWidth = d1.width;
		int frameHeight = d1.height;
		int screenWidth = d2.width;
		int screenHeight = d2.height;
		int xPos = Math.max((screenWidth - frameWidth)/2, 0);
		int yPos = Math.max((screenHeight - frameHeight)/2, 0);
		setLocation(xPos, yPos);
    }
    
    Panel createControls()
    {
        m_text = new TextField(40);
        m_ok = new Button("OK");
        m_cancel = new Button("Cancel");
        
        m_ok.addActionListener(this);
        m_cancel.addActionListener(this);
        
        Panel p = new Panel();
        p.setLayout(new GridBagLayout());
        
        addComponent(p, new Label("Text:"),
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        1, 1,
        0, 0,
        new Insets(5,5,5,5), 0, 0,
        0.0, 0.0);
        
        addComponent(p, m_text,
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
        0, 3,
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
            String text = m_text.getText();
            if (text != null)
            {
                int pos = m_htmlWindow.find(text);
                if (pos == -1)
                {
                    MessageBox dlg = new MessageBox(m_frame, "Find Text", "Text Not Found!");
                    dlg.setVisible(true);
                }
            }
            dispose();
        }
    }
}