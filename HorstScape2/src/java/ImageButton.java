import java.awt.*;
import java.awt.event.*;

public class ImageButton extends Component 
implements MouseListener
{
    static Color brightColor = Color.decode("#dcdcdc");
	static Color darkColor = brightColor.darker().darker();
    Font m_font = new Font("Arial", Font.BOLD, 12);
    String m_text;
    Image m_image = null;
    boolean m_bPressed = false;
    ActionEvent m_ae = new ActionEvent(this, 0, "");
    ActionListener m_al = null;
    int padding;
    boolean bHighlight = false;
    boolean bCanHighlight = true;
    
    public ImageButton()
    {
        addMouseListener(this);
        padding = 5;
    }
    
    public ImageButton(Image image)
    {
        this(image, null);
        padding = 0;
    }
    
    public ImageButton(Image image, String txt)
    {
        this();
        m_text = txt;
        m_image = image;
        padding = 5;
    }
    
    public void setImage(Image img)
    {
        m_image = img;
    }
    
    public void addActionListener(ActionListener al)
    {
        m_al = al;
    }
    
    public void removeActionListener(ActionListener al)
    {
        if (m_al == al) 
            m_al = null;
    }
    
    public Dimension getPreferredSize()
    {
        Dimension d = new Dimension(0,0);
        if (m_image != null)
        {
            d.width = Math.max(0, m_image.getWidth(null)) + 2*padding;
            d.height = Math.max(0, m_image.getHeight(null)) + 2*padding;
        }
        if (m_text != null)
        {
            FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(m_font);
            d.height += fm.getHeight();
            d.width = Math.max(d.width, fm.stringWidth(m_text) + 2*padding);
        }
        return d;
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }
    
    public void update(Graphics g)
    {
        Dimension dim = getSize();
        g.setColor(SystemColor.control);
        g.fillRect(0,0,dim.width,dim.height);
        if (m_image != null) 
        {
            int width = m_image.getWidth(this);
            int height = m_image.getHeight(this);
            int xOffset = (int) ((dim.width - 2*padding) - width) / 2;
            g.drawImage(m_image, padding + xOffset, padding, this);
        } 
        g.setFont(m_font);
        g.setColor(Color.blue);
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(g.getFont());
        int yPos = padding + fm.getHeight();
        if (m_image != null)
            yPos += m_image.getHeight(this);
        if (m_text != null)
        {
            int xOffset = ((dim.width - 2*padding) - fm.stringWidth(m_text))/2;
            g.drawString(m_text, padding +xOffset, yPos);
        }
        
        if (bCanHighlight && bHighlight)
        {
            if (m_bPressed)
                g.setColor(darkColor);
            else
                g.setColor(brightColor);
            g.drawLine(0,0,dim.width-1, 0);
           // g.drawLine(0,0,dim.width-2, 0);
            g.drawLine(0,0,0, dim.height-1);
           // g.drawLine(1,0,1, dim.height-1);
            
            if (m_bPressed)
                g.setColor(brightColor);
            else
                g.setColor(darkColor);
            g.drawLine(dim.width-1,0,dim.width-1,dim.height-1);
            //g.drawLine(dim.width-2,0,dim.width-2,dim.height-1);
            g.drawLine(0,dim.height-1,dim.width-1, dim.height-1);
            //g.drawLine(0,dim.height-2,dim.width-1, dim.height-2);
        }
    }
    
    public void paint(Graphics g)
    {
        update(g);
    }
    
    public void mouseClicked(MouseEvent e)
    {
        m_bPressed = false;
        repaint();
    }

    public void mouseEntered(MouseEvent e)
    {
        bHighlight = true;
        repaint();
    }
    
    public void mouseExited(MouseEvent e)
    {
        bHighlight = false;
        if (m_bPressed) 
        {
            m_bPressed = false;
        }
        m_bPressed = false;
        repaint();
    }

    public void mousePressed(MouseEvent e)
    {
        m_bPressed = true;
        repaint();
    }

    public void mouseReleased(MouseEvent e)
    {
        boolean bPressed = m_bPressed;
        m_bPressed = false;
        repaint();
        if (m_al != null && bPressed)
            m_al.actionPerformed(m_ae);
    }
}
