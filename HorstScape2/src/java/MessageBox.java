import java.awt.*;
import java.awt.event.*;

public class MessageBox extends Dialog implements ActionListener
{
	protected Label lblMessage = new java.awt.Label();
	protected Button butOK = new java.awt.Button();

    public MessageBox(Frame parent, String szTitle, String szMsg)
    {
		super(parent, true);

		setLayout(new GridBagLayout());
		setFont(new Font("SansSerif", Font.PLAIN, 12));
		setResizable(false);
		lblMessage.setText(szMsg);
		    lblMessage.setAlignment(java.awt.Label.CENTER);
		    addComponent(this, lblMessage, 
		                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
		                            1, 1, 
		                            0, 0,
		                            new Insets(5, 10, 5, 10), 0, 0,
		                            1.0, 0);
		    
        butOK.setLabel("  OK  ");
		    addComponent(this, butOK, 
		                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
		                            1, 1, 
		                            0, 1,
		                            new Insets(5, 0, 10, 0), 10, 0,
		                            0.5, 0);

		butOK.addActionListener(this);
        setTitle(szTitle);
    }
    
    public void setMessageText(String szMsg)
    {
        lblMessage.setText(szMsg);
    }

	public void setVisible(boolean b)
	{
	    if (b) {
            // set the proper size
            int width = getFontMetrics(getFont()).stringWidth(lblMessage.getText());
            setSize(Math.max(200, width + 40), 110);

		    Rectangle bounds = getParent().getBounds();
		    Rectangle abounds = getBounds();

		    setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
			    bounds.y + (bounds.height - abounds.height)/2);
		}
		super.setVisible(b);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == butOK)
    	    setVisible(false);
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
}
