package jcdsee.help;

import java.util.Vector;
import java.net.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

/**
 * ScrollPane for displaying web pages.
 *
 */
public class WebScrollPane extends JScrollPane {

    // Text field
    private JTextPane WebTextPane = new JTextPane() {
	protected InputStream getStream(URL page) throws IOException {
	    return page.openStream();
	}
    };
    
    // Vector with history info
    private Vector historyVect;
    private int histStop = 0;
    private int histCurr = -1;
    
    /**
     * Constructor
     */
    public WebScrollPane() {
	super();

	// Initialize history vector
	historyVect = new Vector(1);

	// Initialize WebTextPane
	WebTextPane.setToolTipText("");
	WebTextPane.setEditorKit( new HTMLEditorKit() );
	WebTextPane.setEditable(false);
	WebTextPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
	    public void hyperlinkUpdate(HyperlinkEvent e) {
	        WebTextPane_hyperlinkUpdate(e);
	    }
	});
	
	// Add WebTextPane to the scroll pane
	this.getViewport().add(WebTextPane, null);
    }

    // Public Methods

    /**
     * Go to a specific web-page. The current page is added to history.
     *
     * @param url String with the URL.
     */
    public void gotoPage( String url ) throws IOException, MalformedURLException {
	gotoPage( new URL( url ) );
    }
   
    /**
     * Go to a specific web-page. The current page is added to history.
     *
     * @param url the URL.
     */    
    public void gotoPage( URL url ) throws IOException {
	// Change page
	changePage( url );
	
	// Check if the same page as current
	if( histCurr >= 0 ) {
	    if( ((URL)historyVect.get( histCurr )).equals(url) )
		return;
	}
	 
	// Add to history vector
	histCurr++;

	if( histCurr >= historyVect.size() )
	    historyVect.add( new URL( url.toString() ) );
	else
	    historyVect.set( histCurr, new URL( url.toString() ) );
	
	histStop = histCurr;
    }

    
    /**
     * Get the current page URL. If no page has been loaded, this method will return null.
     *
     * @return the current URL. If no page has been loaded, this will be null.
     */
    public URL getPage() {
	URL url = null;
	
	try {
	  url = new URL( ((URL)historyVect.get(histCurr)).toString() );
	} catch( MalformedURLException e ) { }
	
	return url;
    }

    
    /**
     * Go to prevoius page in history.
     */    
    public void prevPage() throws IOException {
	// Check if previous page available
	if( histCurr > 0 ) {
	    // Move one page back
	    histCurr--;
	    changePage( (URL)historyVect.get(histCurr) );
	}
    }

    /**
     * Go to next page in history.
     */    
    public void nextPage() throws IOException {
	// Check if next page available
	if( histCurr < histStop ) {
	    // Move one page forward
	    histCurr++;
	    changePage( (URL)historyVect.get(histCurr) );
	}
    }

    // Private methods
 
    private void changePage( URL url ) throws IOException {
	WebTextPane.setPage( url );
    }
    
    private void WebTextPane_hyperlinkUpdate(HyperlinkEvent e) {
	// Hyper link event - check if interesting
	if( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
	    try {
		gotoPage( e.getURL() );
	    }
	    catch( IOException ioe ) { }
	} 
    }
}

 











