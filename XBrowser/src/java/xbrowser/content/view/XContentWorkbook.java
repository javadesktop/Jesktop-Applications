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

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import xbrowser.content.*;
import xbrowser.content.event.*;
import xbrowser.doc.*;
import xbrowser.widgets.*;
import xbrowser.widgets.event.*;
import xbrowser.*;

public class XContentWorkbook implements XContentView
{
	public XContentWorkbook(XContentListener listener)
    {
    	this.listener = listener;
		workbook = new XWorkbook() {
			public String getToolTipText(MouseEvent e)
			{
				if( e==null )
					return null;

			int index = getUI().tabForCoordinate(this,e.getX(),e.getY());

				if( index!=-1 )
				{
				XDocument doc = (XDocument)page_Doc.get( getMainComponentAt(index) );

					return doc.getPageCompletePath();
				}
				else
					return null;
			}
		};
		ToolTipManager.sharedInstance().registerComponent(workbook);

    	tabImage = XComponentBuilder.getInstance().buildImageIcon(this, "image.Tab");
		registerListeners();
    }

    private void registerListeners()
    {
        workbook.addChangeListener( new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
			Component comp = workbook.getSelectedComponent();

				if( comp!=null )
				{
				XDocument doc = (XDocument)page_Doc.get(comp);

					if( doc!=null )
						listener.documentActivated(doc);
				}
			}
		});

        workbook.addMouseListener( new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				if( e.isPopupTrigger() )
				{
				int x = e.getX();
				int y = e.getY();
				int index = workbook.getUI().tabForCoordinate(workbook,x,y);

					if( index!=-1 )
						listener.showPopupMenu(workbook,x,y);
				}
			}
		});

		workbook.addWorkbookListener( new XWorkbookListener() {
			public void componentRemoved(Component comp)
			{
			XDocument doc = (XDocument)page_Doc.remove(comp);

				doc_Page.remove(doc);
				doc.closingDocument();

				listener.documentClosed(doc);
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

		workbook.addTab(doc.getPageTitle(),tabImage,page);
        workbook.setSelectedComponent(page);

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
            workbook.setSelectedComponent(page);
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
    JPanel page = (JPanel)workbook.getSelectedComponent();

    	if( page!=null )
        {
            closeDocument(page,true);
        	workbook.remove(page);
        }
    }

    public void closeAll(boolean inform)
    {
    int size = workbook.getTabCount();
    JPanel page = null;

    	for( int i=0; i<size; i++ )
        {
    		page = (JPanel)workbook.getMainComponentAt(0);
            closeDocument(page,inform);
        	workbook.remove(page);
        }
    }

    public boolean supportsTiling()
    {
        return true;
    }

    public void tileCascade()
    {
		workbook.tileCascade();
	}

    public void tileHorizontal()
    {
		workbook.tileHorizontal();
	}

    public void tileVertical()
    {
		workbook.tileVertical();
	}

    public void minimizeAll()
    {
		workbook.minimizeAll();
	}

    public void restoreAll()
    {
		workbook.restoreAll();
	}

    public Component getComponent()
    {
    	return workbook;
    }

    public void setTitleFor(XDocument doc,String title)
    {
    int index = workbook.indexOfComponent((JPanel)doc_Page.get(doc));

        if( index!=-1 )
    	    workbook.setTitleAt( index, title);
    }

    public void setContentTabPlacement(int place)
    {
		try
		{
	        workbook.setTabPlacement(place);
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
	private XWorkbook workbook;
}
