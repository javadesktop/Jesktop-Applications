/****************************************************************
*              XBrowser  -  eXtended web Browser                *
*                                                               *
*           Copyright (c) 2000-2001  Armond Avanes              *
*     Refer to ReadMe & License files for more information      *
*                                                               *
*                                                               *
*                      By: Armond Avanes                        *
*          Armond@Neda.net     &    ArnoldX@MailCity.com        *
*          http://www.geocities.com/xa_arnold/index.html        *
*****************************************************************/
package xbrowser.widgets;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import xbrowser.widgets.event.*;

public class XWorkbook extends JTabbedPane
{
	public XWorkbook()
	{
		super();
		addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
		    	updateSelectedTab();
			}
		});

		frameListener = new InternalFrameAdapter() {
			public void internalFrameActivated(InternalFrameEvent e)
			{
				setSelectedComponent( (Component)frames_Components.get(e.getSource()) );
			}

			public void internalFrameClosing(InternalFrameEvent e)
			{
			Component comp = (Component)frames_Components.get(e.getSource());

				if( workbookListener!=null )
					workbookListener.componentRemoved(comp);

				remove(comp);
			}
		};

		dtp = new XDesktopPane();
	}

	public void addWorkbookListener(XWorkbookListener listener)
	{
		workbookListener = listener;
	}

	public void removeWorkbookListener(XWorkbookListener listener)
	{
		if( workbookListener==listener )
			workbookListener = null;
	}

    public void updateUI()
    {
		super.updateUI();

        if( dtp!=null )
        	SwingUtilities.updateComponentTreeUI(dtp);
	}

	public void insertTab(String title, Icon icon, Component component, String tip, int index) throws IllegalArgumentException
	{
	JInternalFrame  jif = new JInternalFrame(title, true, true,	true, true);
	JPanel pnl = new JPanel(new BorderLayout());

		frames_Components.put(jif,component);
		components_Frames.put(component,jif);

		panels_Components.put(pnl,component);
		components_Panels.put(component,pnl);

		super.insertTab(title, icon, pnl, tip, index);

		if( dtp.getSize().width==0 )
			dtp.setSize(getSize());

		jif.getContentPane().setLayout( new BorderLayout() );
		jif.getContentPane().add(component,BorderLayout.CENTER);
		jif.addInternalFrameListener(frameListener);
		dtp.addInternalFrame(jif);
	}

    public Component getSelectedComponent()
    {
	Component sel_comp = super.getSelectedComponent();
	Component main_sel_comp = ( (sel_comp!=null) ? (Component)panels_Components.get(sel_comp) : null );

		return( (main_sel_comp!=null) ? main_sel_comp : sel_comp );
	}

    public void setSelectedComponent(Component c)
    {
	Component comp = (Component)components_Panels.get(c);

		super.setSelectedComponent( (comp!=null) ? comp : c );
	}

	public Component getMainComponentAt(int index)
	{
		return( (Component)panels_Components.get( getComponentAt(index) ) );
	}

    public int indexOfComponent(Component component)
    {
	Component comp = (Component)components_Panels.get(component);

		return super.indexOfComponent( (comp!=null) ? comp : component );
    }

    public void setTitleAt(int index, String title)
    {
    Component comp = (Component)panels_Components.get( (JPanel)getComponentAt(index) );
    JInternalFrame jif = (JInternalFrame)components_Frames.get(comp);

		jif.setTitle(title);

		super.setTitleAt(index,title);
	}

    private void updateSelectedTab()
    {
		// activate the frame corresponding to the
		// currently selected tab

	Component comp = getSelectedComponent();

		if( comp!=null )
		{
			try
			{
				((JInternalFrame)components_Frames.get(comp)).setSelected(true);
			}
			catch( Exception e )
			{
			}
		}

		// move the desktop pane to the selected page
	Container old_parent = dtp.getParent();
	Container new_parent = (Container) super.getSelectedComponent();

		if( old_parent!=null )
			old_parent.remove(dtp);

		if( new_parent!=null )
			new_parent.add(dtp);

		validate();
	}

    public void removeTabAt(int index)
    {
    JPanel pnl = (JPanel)getComponentAt(index);
    Component comp = (Component)panels_Components.remove(pnl);
    JInternalFrame jif = (JInternalFrame)components_Frames.remove(comp);

    	frames_Components.remove(jif);
    	components_Panels.remove(comp);

    	dtp.remove(jif);

    	super.removeTabAt(index);
    	updateSelectedTab();
	}

	public void tileCascade()
	{
		dtp.tileCascade();
	}

	public void tileHorizontal()
	{
		dtp.tileHorizontal();
	}

	public void tileVertical()
	{
		dtp.tileVertical();
	}

    public void minimizeAll()
    {
		dtp.minimizeAll();
	}

    public void restoreAll()
    {
		dtp.restoreAll();
	}

// Attributes:
	private XDesktopPane dtp;
	private InternalFrameListener frameListener;
	private XWorkbookListener workbookListener;

	private Map frames_Components = new HashMap();
	private Map components_Frames = new HashMap();
	private Map components_Panels = new HashMap();
	private Map panels_Components = new HashMap();
}
