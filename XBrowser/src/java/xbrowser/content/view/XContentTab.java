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
package xbrowser.content.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

import xbrowser.content.*;
import xbrowser.content.event.*;
import xbrowser.doc.*;
import xbrowser.widgets.*;
import xbrowser.*;

public class XContentTab implements XContentView
{
	public XContentTab(XContentListener listener)
    {
    	this.listener = listener;
		tabbedPane = new JTabbedPane() {
			public String getToolTipText(MouseEvent e)
			{
				if( e==null )
					return null;

			int index = getUI().tabForCoordinate(this,e.getX(),e.getY());

				if( index!=-1 )
				{
				XDocument doc = (XDocument)page_Doc.get( getComponentAt(index) );

					return doc.getPageCompletePath();
				}
				else
					return null;
			}
		};
		ToolTipManager.sharedInstance().registerComponent(tabbedPane);

    	tabImage = XComponentBuilder.getInstance().buildImageIcon(this, "image.Tab");
		registerListeners();
    }

    private void registerListeners()
    {
        tabbedPane.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
			Component comp = tabbedPane.getSelectedComponent();

				if( comp!=null )
				{
				XDocument doc = (XDocument)page_Doc.get(comp);

					if( doc!=null )
						listener.documentActivated(doc);
				}
			}
		});

        tabbedPane.addMouseListener( new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				if( e.isPopupTrigger() )
				{
				int x = e.getX();
				int y = e.getY();
				int index = tabbedPane.getUI().tabForCoordinate(tabbedPane,x,y);

					if( index!=-1 )
						listener.showPopupMenu(tabbedPane,x,y);
				}
			}
		});
	}

    private JPanel createNewPage(XDocument doc)
    {
    JPanel page = new JPanel();

    	page.setLayout(new BorderLayout());
		page.add(doc.getComponent(), BorderLayout.CENTER);

        page_Doc.put(page,doc);
        doc_Page.put(doc,page);

		tabbedPane.addTab(doc.getPageTitle(), tabImage, page);
        tabbedPane.setSelectedComponent(page);

        return page;
	}

   	public XDocument createNewDocument()
    {
    XDocument doc = XDocumentFactory.createNewDocument();

		listener.documentAdded(doc);
    	createNewPage(doc);
        return doc;
    }

    public boolean hasDocument()
    {
		return( !doc_Page.isEmpty() );
	}

    public Iterator getDocuments()
    {
    	return doc_Page.keySet().iterator();
    }

    public void setDocuments(Iterator docs)
    {
    	closeAll(true);
        while( docs.hasNext() )
    		createNewPage( (XDocument)docs.next() );
    }

    public void activateDocument(XDocument doc)
    {
    JPanel page = (JPanel)doc_Page.get(doc);

        if( page!=null )
            tabbedPane.setSelectedComponent(page);
    }

    private void closeDocument(JPanel page,boolean inform)
    {
    XDocument doc = (XDocument)page_Doc.remove(page);

    	doc_Page.remove(doc);

        if( inform )
        {
	    	doc.closingDocument();
	    	listener.documentClosed(doc);
		}
    }

    public void closeCurrentDocument()
    {
    JPanel page = (JPanel)tabbedPane.getSelectedComponent();

    	if( page!=null )
        {
            closeDocument(page,true);
        	tabbedPane.remove(page);
        }
    }

    public void closeAll(boolean inform)
    {
    int size = tabbedPane.getTabCount();
    JPanel page = null;

    	for( int i=0; i<size; i++ )
        {
    		page = (JPanel)tabbedPane.getComponentAt(0);
            closeDocument(page,inform);
        	tabbedPane.remove(page);
        }
    }

    public boolean supportsTiling()
    {
        return false;
    }

    public void tileCascade()
    {
	}

    public void tileHorizontal()
    {
	}

    public void tileVertical()
    {
	}

    public void minimizeAll()
    {
	}

    public void restoreAll()
    {
	}

    public Component getComponent()
    {
    	return tabbedPane;
    }

    public void setTitleFor(XDocument doc, final String title)
    {
    final int index = tabbedPane.indexOfComponent((JPanel)doc_Page.get(doc));

        if( index!=-1 )
    	    tabbedPane.setTitleAt( index, title);
    }

    public void setContentTabPlacement(int place)
    {
		try
		{
        	tabbedPane.setTabPlacement(place);
		}
		catch( Exception e )
		{
		}
    }

// Attributes:
    private XContentListener listener = null;
    private Map page_Doc = new HashMap();
    private Map doc_Page = new HashMap();

	private ImageIcon tabImage = null;
	private JTabbedPane tabbedPane;
}
