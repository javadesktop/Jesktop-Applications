import java.awt.*;
import java.awt.event.*;

public class ProxyDialog extends Dialog
implements ActionListener
{
    public Panel m_controls;
    public Checkbox m_useProxy;
    public TextField m_host;
    public TextField m_port;
    public Button m_ok;
    public Button m_cancel;
    
    
    public ProxyDialog(Frame parent, String title)
    {
        super(parent, title);
        add(createControls());
        setSize(300,180);
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
        String proxySet = (String)System.getProperties().get("proxySet");
        String host = (String)System.getProperties().get("proxyHost");
        String port = (String)System.getProperties().get("proxyPort");
        m_useProxy = new Checkbox("Use Proxy");
        if (proxySet != null && proxySet.equals("true"))
            m_useProxy.setState(true);
        m_host = new TextField(40);
        if (host != null)
            m_host.setText(host);
        m_port = new TextField(40);
        if (port != null)
            m_port.setText(port);
        m_ok = new Button("OK");
        m_cancel = new Button("Cancel");
        
        m_ok.addActionListener(this);
        m_cancel.addActionListener(this);
        
        Panel p = new Panel();
        p.setLayout(new GridBagLayout());
        
        addComponent(p, m_useProxy,
        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        1, 2,
        0, 0,
        new Insets(5,5,5,5), 0, 0,
        0.0, 0.0);
        
        addComponent(p, new Label("Host:"),
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        1, 1,
        0, 1,
        new Insets(5,5,5,5), 0, 0,
        0.0, 0.0);
        
        addComponent(p, m_host,
        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        1, 1,
        1, 1,
        new Insets(5,5,5,5), 0, 0,
        1.0, 0.0);
        
        addComponent(p, new Label("Port:"),
        GridBagConstraints.WEST, GridBagConstraints.NONE,
        1, 1,
        0, 2,
        new Insets(5,5,5,5), 0, 0,
        0.0, 0.0);
        
        addComponent(p, m_port,
        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
        1, 1,
        1, 2,
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
            if (m_useProxy.getState())
            {
                System.getProperties().put("proxySet", "true");
                System.getProperties().put("proxyHost", m_host.getText());
                System.getProperties().put("proxyPort", m_port.getText());
            }
            else
                System.getProperties().put("proxySet", "false");
            dispose();
        }
    }
}