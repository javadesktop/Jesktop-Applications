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
package xbrowser.renderer.custom;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.applet.*;
import java.util.*;
import java.awt.print.*;

import xbrowser.*;
import xbrowser.widgets.*;
import xbrowser.renderer.*;
import xbrowser.renderer.event.*;

public class XCustomRenderer extends JEditorPane implements XRenderer, Printable
{
	public Component getComponent()
	{
		return this;
	}

    public XCustomRenderer()
    {
		setEditable(false);

		addHyperlinkListener( new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if( e.getEventType()==HyperlinkEvent.EventType.ACTIVATED )
				{
					if( e instanceof HTMLFrameHyperlinkEvent )
					{
					HTMLFrameHyperlinkEvent frame_hyperlink_event = (HTMLFrameHyperlinkEvent)e;

						processXHyperLink(frame_hyperlink_event.getURL().toString(), frame_hyperlink_event.getTarget(), frame_hyperlink_event.getSourceElement(), true);
					}
					else
						processXHyperLink(e.getURL().toString(), true);
				}
				else if( e.getEventType()==HyperlinkEvent.EventType.ENTERED )
				{
					currentFocusedLink = e.getURL().toString();
					rendererListener.hyperlinkEntered(currentFocusedLink);
				}
				else if( e.getEventType()==HyperlinkEvent.EventType.EXITED )
				{
				String last_entered_link = currentFocusedLink;

					currentFocusedLink = null;
					rendererListener.hyperlinkExited(last_entered_link);
				}
			}
		});

		addPropertyChangeListener( new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				if( e.getPropertyName().equals("page") )
					rendererListener.pageLoadingFinished(null);
			}
		});
    }

    public void addRendererListener(XRendererListener renderer_listener)
    {
		rendererListener = renderer_listener;
	}

    public void removeRendererListener(XRendererListener renderer_listener)
    {
		if( rendererListener==renderer_listener )
			rendererListener = null;
	}

    public void showURL(String url)
    {
		processXHyperLink(url, true);
	}

    private void processXHyperLink(String url, boolean add_to_document_history)
    {
		processXHyperLink(url, null, null, add_to_document_history);
	}

    private void processXHyperLink(final String url, final String target_frame, final Element source_elem, final boolean add_to_document_history)
    {
		destroying();

		if( url.equals(getCurrentURL()) && getDocument().getProperty(Document.StreamDescriptionProperty)!=null )
			return;

        loaderThread = new Thread() {
			public void run()
			{
		    URL prev_stream = null;

				try
				{
				boolean dont_add = !pageHistory.isEmpty() && pageHistory.getLast().equals(url);

					prev_stream = (URL)getDocument().getProperty(Document.StreamDescriptionProperty);
					currentURL = url;

					rendererListener.pageLoadingStarted();

					if( target_frame==null )
						setPage(url);
					else
					{
					HTMLFrameHyperlinkEvent frame_hyperlink_event = new HTMLFrameHyperlinkEvent(XCustomRenderer.this, HyperlinkEvent.EventType.ACTIVATED, new URL(url), target_frame);

						((HTMLDocument)getDocument()).processHTMLFrameHyperlinkEvent(frame_hyperlink_event);
					}

					urlList.add(url);

					if( add_to_document_history && !dont_add )
					{
					int size = pageHistory.size();

						if( currentPageIndex!=size-1 )
						{
							for( int i=currentPageIndex+1; i<size; i++ )
								pageHistory.remove( currentPageIndex+1 );
						}

						addToPageHistory(url);
						currentPageIndex++;
					}
				}
				catch( Exception e )
				{
					if( !isInterrupted() )
					{
						rendererListener.pageLoadingFinished(e);

						getDocument().putProperty(Document.StreamDescriptionProperty, prev_stream);
						currentURL = prev_stream.toString();
					}
				}

				loaderThread = null;
			}
		};

		loaderThread.start();
    }

	public String getPageTitle()
	{
    	if( getCurrentURL()==null )
        	return XProjectConstants.EMPTY_DOCUMENT;
        else if( getCurrentURL().trim().equals("") )
        	return XProjectConstants.UNTITLED_DOCUMENT;
        else
        {
		String title = (String)getDocument().getProperty(Document.TitleProperty);

			if( (title==null) || (title.equals("")) )
			{
				try
				{
				String file_name = (new URL(getCurrentURL())).getFile();

					if( file_name.equals("/") || file_name.equals("\\") || file_name.equals("") )
						title = XProjectConstants.UNTITLED_DOCUMENT;
					else
					{
					int slash_index = file_name.lastIndexOf("/");
					int backslash_index = file_name.lastIndexOf("\\");

						if( slash_index==-1 && backslash_index==-1 )
							title = file_name;
						else if( slash_index!=-1 )
							title = file_name.substring(slash_index+1);
						else
							title = file_name.substring(backslash_index+1);
					}
				}
				catch( Exception e )
				{
					title = XProjectConstants.UNTITLED_DOCUMENT;
				}
			}

			return title;
        }
	}

    public boolean hasSource()
    {
        return getContentType().equals("text/html");
    }

    public String getSource()
    {
		return getText();
	}

    public void reload()
    {
        if( getCurrentURL()!=null )
        {
			// Force to reload !!
			getDocument().putProperty(Document.StreamDescriptionProperty, null);
			processXHyperLink(getCurrentURL(), false);
		}
    }

    public void refresh()
    {
        repaint();
    }

    public void stopRendering()
    {
		if( loaderThread!=null )
		{
			loaderThread.interrupt();
			loaderThread = null;
		}

        if( myStream!=null )
        {
            try
            {
                myStream.skip( myStream.available() );
            }
            catch( Exception e )
            {
            }

            //myStream = null;
        }

		rendererListener.pageLoadingStopped();
    }

	public void destroying()
	{
	EditorKit kit = getEditorKit();

		stopRendering();
		stopPrinting();

		if( kit instanceof XHTMLEditorKit )
			((XHTMLEditorKit)kit).destroyAllApplets();
	}

    public void saveContent(File dest_file)
    {
    FileWriter file = null;

		try
        {
	    	file = new FileWriter( dest_file );
            write( file );
        }
        catch( Exception ex1 )
        {
        }
        finally
        {
        	try
            {
	        	file.close();
            }
            catch( Exception ex2 )
            {
            }
        }
    }

    public void showNextPage()
    {
        if( hasForwardHistory() )
        	processXHyperLink((String)pageHistory.get(++currentPageIndex), false);
    }

    public void showPreviousPage()
    {
        if( hasBackwardHistory() )
        	processXHyperLink((String)pageHistory.get(--currentPageIndex), false);
    }

    public boolean hasForwardHistory()
    {
    	return( currentPageIndex<pageHistory.size()-1 );
    }

    public boolean hasBackwardHistory()
    {
    	return( currentPageIndex>0 );
    }

    public String getNextPagePath()
    {
        if( hasForwardHistory() )
        	return( (String)pageHistory.get(currentPageIndex+1) );
        else
        	return null;
	}

    public String getPreviousPagePath()
    {
        if( hasBackwardHistory() )
        	return( (String)pageHistory.get(currentPageIndex-1) );
        else
        	return null;
	}

    public Iterator getForwardHistory()
    {
		if( hasForwardHistory() )
			return pageHistory.listIterator(currentPageIndex+1);
		else
			return null;
	}

    public Iterator getBackwardHistory()
    {
		if( hasBackwardHistory() )
			return pageHistory.subList(0, currentPageIndex).iterator();
		else
			return null;
	}

	public void showPageFromHistory(int index, int history_type)
	{
		currentPageIndex = (history_type==XRenderer.BACKWARD_HISTORY) ? index : currentPageIndex+index+1;
		processXHyperLink((String)pageHistory.get(currentPageIndex), false);
	}

    public String getPageCompletePath()
    {
    	return( (getCurrentURL()==null) ? "" : getCurrentURL() );
    }

    public String getCurrentFocusedLink()
    {
		return currentFocusedLink;
	}

    public String getCurrentURL()
    {
		return currentURL;
		//return( (getPage()==null) ? null : getPage().toString() );
	}

    public Iterator getURLList()
    {
		return urlList.iterator();
	}

    private void setSelection(int start_index, int end_index, boolean move_up)
    {
        if( move_up )
        {
            setCaretPosition(end_index);
            moveCaretPosition(start_index);
        }
        else
            select(start_index, end_index);
    }

	private boolean isSeparator(char ch)
	{
		for( int k=0; k<WORD_SEPARATORS.length; k++ )
		{
			if( ch==WORD_SEPARATORS[k] )
				return true;
		}

		return false;
	}

    private String getRenderedText()
    {
    Document doc = getDocument();
    String content = "";

        try
        {
            content = doc.getText(0, doc.getLength());
        }
        catch( Exception e )
        {
        }

        return content;
    }

    public void findNext(String find_phrase, boolean case_sensitive, boolean whole_word, boolean search_up) throws Exception
    {
    int pos = getCaretPosition();
    String search_data = getRenderedText();

        if( !case_sensitive )
        {
            search_data = search_data.toLowerCase();
            find_phrase = find_phrase.toLowerCase();
        }

        if( whole_word )
        {
            for( int k=0; k<WORD_SEPARATORS.length; k++ )
            {
                if( find_phrase.indexOf(WORD_SEPARATORS[k])>=0 )
                    throw new Exception(XComponentBuilder.getInstance().getProperty(this, "IllegalCharacter", ""+WORD_SEPARATORS[k]));
            }
        }

    int start = -1;
    int finish = -1;

        while( true )
        {
            if( search_up )
                start = search_data.lastIndexOf(find_phrase, pos-1);
            else
                start = search_data.indexOf(find_phrase, pos);

            if( start<0 )
				throw new Exception( XComponentBuilder.getInstance().getProperty(this, "FinishedSearching") );

            finish = start+find_phrase.length();

            if( whole_word )
            {
            boolean s1 = start>0;
            boolean b1 = s1 && !isSeparator(search_data.charAt(start-1));
            boolean s2 = finish<search_data.length();
            boolean b2 = s2 && !isSeparator(search_data.charAt(finish));

                if( b1 || b2 )    // Not a whole word
                {
                    if( search_up && s1 )    // Can continue up
                    {
                        pos = start;
                        continue;
                    }

                    if( !search_up && s2 )    // Can continue down
                    {
                        pos = finish;
                        continue;
                    }

                    // Found, but not a whole word, and we cannot continue
                    throw new Exception( XComponentBuilder.getInstance().getProperty(this, "FinishedSearching") );
                }
            }

            break;
        }

        setSelection(start, finish, search_up);
	}

    public void startPrinting(boolean print_immediately)
    {
    	prnJob = PrinterJob.getPrinterJob();
        prnJob.setPrintable(this);

        if( !print_immediately && !prnJob.printDialog() )
            return;

        try
        {
			rendererListener.pagePrintingStarted();
            prnJob.print();

			rendererListener.pagePrintingFinished(null);
        }
        catch( PrinterException e )
        {
			rendererListener.pagePrintingFinished(e);
        }

        prnJob = null;
	}

	public void stopPrinting()
	{
		if( prnJob!=null )
		{
			prnJob.cancel();
			prnJob = null;
		}

		loadingPreviews = false;
	}

    public void changePreviewScale(XPreviewContainer preview, int new_scale)
    {
	int w = (int)(pageWidth*new_scale/100);
	int h = (int)(pageHeight*new_scale/100);
	Component[] comps = preview.getComponents();

		for( int i=0; i<comps.length; i++ )
		{
			if( !(comps[i] instanceof XPagePreview) )
				continue;

			((XPagePreview)comps[i]).setScaledSize(w, h);
		}

		preview.doLayout();
		preview.getParent().getParent().validate();
	}

    public void loadPreviews(final XPreviewContainer preview, int scale) throws Exception
    {
    PrinterJob prn_job = PrinterJob.getPrinterJob();
    PageFormat page_format = prn_job.defaultPage();

        if( page_format.getHeight()==0 || page_format.getWidth()==0 )
            throw new Exception(XComponentBuilder.getInstance().getProperty(this, "PageSizeError"));

        pageWidth = (int)(page_format.getWidth());
        pageHeight = (int)(page_format.getHeight());

    final int w = (int)(pageWidth*scale/100);
    final int h = (int)(pageHeight*scale/100);
    int page_index = 0;

        loadingPreviews = true;

        try
        {
            while( loadingPreviews )
            {
            final BufferedImage img = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();

                g.setColor(Color.white);
                g.fillRect(0, 0, pageWidth, pageHeight);

				if( print(g, page_format, page_index)!=Printable.PAGE_EXISTS )
					break;

                System.gc();
				SwingUtilities.invokeAndWait( new Runnable() {
					public void run()
					{
						preview.add(new XPagePreview(w, h, img));
						preview.doLayout();
						preview.getParent().getParent().validate();
					}
				});

                page_index++;
            }
        }
        catch (PrinterException e)
        {
	        throw new Exception(XComponentBuilder.getInstance().getProperty(this, "PreviewError"));
        }
        finally
        {
	        loadingPreviews = false;
		}
	}

    private void addToPageHistory(String url)
    {
		pageHistory.add(url);
		if( pageHistory.size()>MAX_HISTORY_LENGTH )
			pageHistory.remove(0);

		rendererListener.pageAddedToHistory(url);
	}

	void showDocument(URL url, String target)
	{
		if( target.equalsIgnoreCase("_parent") || target.equalsIgnoreCase("_top") || target.equalsIgnoreCase("_blank") )
			XBrowser.getBrowser().showInNewDocument(url.toString());
		else
			processXHyperLink(url.toString(), true);
	}

    void showAppletStatusInternal(String status)
    {
		rendererListener.showAppletStatus(status);
	}

	void showAppletLifeCycle(Applet applet, int status)
	{
		rendererListener.showAppletLifeCycle(applet, status);
	}

    public int print(Graphics pg, PageFormat page_format, int page_index) throws PrinterException
    {
        rendererListener.renderingPage(page_index);

        pg.translate((int)page_format.getImageableX(),(int)page_format.getImageableY());

    int page_width = (int)page_format.getImageableWidth();
    int page_height = (int)page_format.getImageableHeight();

        pg.setClip(0, 0, page_width, page_height);

        // Only do this once per print
        if( printView == null )
        {
        View root = getUI().getRootView(this);

            if( getContentType().equals("text/html") )
                printView = new XPrintView(getDocument().getDefaultRootElement().getElement(1), root, page_width, page_height);
            else
                printView = new XPrintView(getDocument().getDefaultRootElement(), root, page_width, page_height);
        }

        System.gc();
        if( printView.paintPage(pg, page_height, page_index) )
            return PAGE_EXISTS;
        else
        {
			rendererListener.renderingFinished();
            printView = null;
            return NO_SUCH_PAGE;
        }
    }

	protected InputStream getStream(URL page) throws IOException
	{
		myStream = new XInputStream(super.getStream(page));
		return myStream;
	}

	private class XInputStream extends FilterInputStream
	{
		public XInputStream(InputStream in)
		{
			super(in);

			try
			{
				entireSize = available();
			}
			catch( Exception e )
			{
			}
		}

		public int read(byte b[], int off, int len) throws IOException
		{
		int result = super.read(b, off, len);

			if( result!=-1 )
			{
				readSize += result;
				rendererListener.pageLoadingProgress(readSize, entireSize);
			}

			return result;
		}

		public void close() throws IOException
		{
			rendererListener.pageLoadingFinished(null);
		}

	// Attributes:
		private long entireSize = 0;
		private long readSize = 0;
	}

	private class XPrintView extends BoxView
	{
		public XPrintView(Element elem, View root, int w, int h)
		{
			super(elem, Y_AXIS);

			setParent(root);
			setSize(w, h);
			layout(w, h);
		}

		public boolean paintPage(Graphics g, int page_height, int page_index)
		{
			if( page_index>pageIndex )
			{
				firstOnPage = lastOnPage + 1;
				if( firstOnPage>=getViewCount() )
					return false;
				pageIndex = page_index;
			}

		int min = getOffset(Y_AXIS, firstOnPage);
		int max = min + page_height;
		Rectangle rc = new Rectangle();

			for( int i = firstOnPage; i<getViewCount(); i++ )
			{
				rc.x = getOffset(X_AXIS, i);
				rc.y = getOffset(Y_AXIS, i);
				rc.width = getSpan(X_AXIS, i);
				rc.height = getSpan(Y_AXIS, i);

				if( rc.y+rc.height>max )
					break;

				lastOnPage = i;
				rc.y -= min;
				paintChild(g, rc, i);
			}

			return true;
		}

	// Attributes:
		private int firstOnPage = 0;
		private int lastOnPage = 0;
		private int pageIndex = 0;
	}

	private class XPagePreview extends JPanel
	{
		public XPagePreview(int w, int h, Image source)
		{
			m_w = w;
			m_h = h;
			m_source= source;
			m_img = m_source.getScaledInstance(m_w, m_h,Image.SCALE_SMOOTH);
			m_img.flush();
			setBackground(Color.white);
			setBorder(new MatteBorder(1, 1, 2, 2, Color.black));
		}

		public void setScaledSize(int w, int h)
		{
			m_w = w;
			m_h = h;
			m_img = m_source.getScaledInstance(m_w, m_h,Image.SCALE_SMOOTH);
			repaint();
		}

		public Dimension getPreferredSize()
		{
		Insets ins = getInsets();

			return new Dimension(m_w+ins.left+ins.right,m_h+ins.top+ins.bottom);
		}

		public Dimension getMaximumSize()
		{
			return getPreferredSize();
		}

		public Dimension getMinimumSize()
		{
			return getPreferredSize();
		}

		public void paint(Graphics g)
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(m_img, 0, 0, this);
			paintBorder(g);
		}

	// Attributes:
		private int m_w;
		private int m_h;
		private Image m_source;
		private Image m_img;
	}

	static
	{
  		javax.swing.JEditorPane.registerEditorKitForContentType("text/html", "xbrowser.renderer.custom.XHTMLEditorKit", XCustomRenderer.class.getClassLoader());
	}

// Attributes:
	private static final char[] WORD_SEPARATORS = {' ', '\t', '\n',
				'\r', '\f', '.', ',', ':', '-', '(', ')', '[', ']', '{',
				'}', '<', '>', '/', '|', '\\', '\'', '\"'};

	private XInputStream myStream = null;
	private XRendererListener rendererListener = null;
    private String currentFocusedLink = null;
    private String currentURL = null;
    private XPrintView printView = null;
    private PrinterJob prnJob = null;
    private boolean loadingPreviews = false;

    private int pageWidth;
    private int pageHeight;

    private static final int MAX_HISTORY_LENGTH = 40;
    private int currentPageIndex = -1;
    private LinkedList pageHistory = new LinkedList();
    private LinkedList urlList = new LinkedList();
	private Thread loaderThread = null;
}
