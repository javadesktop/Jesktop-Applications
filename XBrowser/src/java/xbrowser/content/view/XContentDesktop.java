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

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import xbrowser.content.*;
import xbrowser.content.event.*;
import xbrowser.doc.*;
import xbrowser.widgets.*;
import xbrowser.*;

public class XContentDesktop implements XContentView
{
	public XContentDesktop(XContentListener listener)
    {
    	this.listener = listener;
    }

    private void createNewPage(XDocument doc)
    {
    JInternalFrame page = new JInternalFrame(doc.getPageTitle(),true,true,true,true);

    	page.getContentPane().setLayout(new BorderLayout());
		page.getContentPane().add(doc.getComponent(), BorderLayout.CENTER);

        page.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        page.addInternalFrameListener(internalFrameListener);

        page_Doc.put(page,doc);
        doc_Page.put(doc,page);

		desktopPane.addInternalFrame(page);
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
    JInternalFrame page = (JInternalFrame)doc_Page.get(doc);

        if( page!=null )
        {
            try
            {
                page.setSelected(true);
            }
            catch( Exception e )
            {
            }
        }
    }

    private void closeDocument(JInternalFrame page,boolean inform)
    {
    XDocument doc = (XDocument)page_Doc.remove(page);

    	doc_Page.remove(doc);

        if( inform )
        {
	    	doc.closingDocument();
    		listener.documentClosed(doc);
		}

    	desktopPane.getDesktopManager().closeFrame(page);
    }

    public void closeCurrentDocument()
    {
    JInternalFrame[] frames = desktopPane.getAllFrames();

    	for( int i=0; i<frames.length; i++ )
        {
        	if( frames[i].isSelected() )
            {
	            closeDocument(frames[i],true);
                break;
            }
        }
    }

    public void closeAll(boolean inform)
    {
    JInternalFrame[] frames = desktopPane.getAllFrames();

    	for( int i=0; i<frames.length; i++ )
            closeDocument(frames[i],inform);
    }

    public Component getComponent()
    {
    	return desktopPane;
    }

    public void setTitleFor(XDocument doc,String title)
    {
    JInternalFrame jif = (JInternalFrame)doc_Page.get(doc);

        if( jif!=null )
            jif.setTitle(title);
    }

    public void setContentTabPlacement(int place)
    {
    }

    public boolean supportsTiling()
    {
        return true;
    }

    public void tileCascade()
    {
		desktopPane.tileCascade();
	}

    public void tileHorizontal()
    {
		desktopPane.tileHorizontal();
	}

    public void tileVertical()
    {
		desktopPane.tileVertical();
	}

    public void minimizeAll()
    {
		desktopPane.minimizeAll();
	}

    public void restoreAll()
    {
		desktopPane.restoreAll();
	}

    private class XInternalFrameListener extends InternalFrameAdapter
    {
		public void internalFrameActivated(InternalFrameEvent e)
		{
		XDocument doc = (XDocument)page_Doc.get(e.getSource());

			if( doc!=null )
				listener.documentActivated(doc);
		}

		public void internalFrameClosing(InternalFrameEvent e)
		{
		XDocument doc = (XDocument)page_Doc.remove(e.getSource());

			doc_Page.remove(doc);
	    	doc.closingDocument();

			listener.documentClosed(doc);
		}
	};

// Attributes:
    private XContentListener listener = null;
    private Map page_Doc = new HashMap();
    private Map doc_Page = new HashMap();
	private XDesktopPane desktopPane = new XDesktopPane();
	private XInternalFrameListener internalFrameListener = new XInternalFrameListener();
}