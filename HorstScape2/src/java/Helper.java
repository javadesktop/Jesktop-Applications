import java.awt.*;
import java.net.*;
import javax.swing.*;
public class Helper
{
    public static URL getURL(String src)
    {
        URL u = null;
        try
        {
            u = new URL(src);
        }
        catch(MalformedURLException e)
        {
        }
        return u;
    }


    public static void addComponent(Container con, Component comp,
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

    private static URL getResourceURL(String rscName) {
        java.net.URLClassLoader cl = (java.net.URLClassLoader) Helper.class.getClassLoader();
        return cl.findResource(rscName);
    }

    public static Image getImage(Component c, String s)
    {
		System.out.println("s="+s);
		ImageIcon ii = new ImageIcon(getResourceURL(s));
		return ii.getImage();
        //Image i = Toolkit.getDefaultToolkit().getImage(s);
        //if (i != null)
        //    return waitForImage(c, i);
        //return null;
    }

    static Image waitForImage(Component c, Image i)
    {
        MediaTracker tracker = new MediaTracker(c);
        tracker.addImage(i, 0);
	    try
	    {
	        tracker.waitForID(0);
	        if (tracker.isErrorAny())
	            return null;
	    }
	    catch (InterruptedException e)
	    {
	    }
	    return i;
    }
}