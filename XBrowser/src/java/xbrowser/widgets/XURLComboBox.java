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
import java.awt.event.*;
import java.net.*;
import java.beans.*;

import xbrowser.*;
import xbrowser.util.*;
import xbrowser.widgets.event.*;

public class XURLComboBox extends JComboBox implements FocusListener, ItemListener, KeyListener, MouseListener
{
    public XURLComboBox()
    {
        super();
    	setEditable(true);

        editor = (JTextField)getEditor().getEditorComponent();
        registerListeners();

        addPropertyChangeListener( new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				if( e.getPropertyName().equals("editor") && e.getNewValue()!=null )
				{
					unregisterListeners();
					editor = (JTextField)((ComboBoxEditor)e.getNewValue()).getEditorComponent();
					registerListeners();
				}
			}
		});
    }

    public void addComboBoxListener(XURLComboBoxListener listener)
    {
    	this.listener = listener;
	}

    public void removeComboBoxListener(XURLComboBoxListener listener)
    {
		if( this.listener==listener )
    		this.listener = null;
	}

    private void registerListeners()
    {
		editor.addKeyListener(this);
		editor.addFocusListener(this);
		editor.addMouseListener(this);
        addItemListener(this);
    }

    private void unregisterListeners()
    {
		editor.removeKeyListener(this);
		editor.removeFocusListener(this);
		editor.removeMouseListener(this);
        removeItemListener(this);
    }

    public void addURL(String url)
    {
		if( url==null )
			return;

        unregisterListeners();

        removeItem(url);
        insertItemAt(url, 0);
        setSelectedItem(url);

        //if( getItemCount()>MAX_MEM_LEN )
        //    removeItemAt( getItemCount()-1 );

        registerListeners();
    }

    public void loadURLs(java.util.Iterator url_list,String active_url)
    {
        unregisterListeners();

		if( getItemCount()>0 )
		    removeAllItems();

		while( url_list.hasNext() )
			addItem( url_list.next() );

		if( active_url!=null && getItemCount()>0 )
			setSelectedItem(active_url);

        registerListeners();
    }

    public void removeAllURLs()
    {
		if( getItemCount()>0 )
	        removeAllItems();
    }

    public String getCurrentURL()
    {
    String sel_item = editor.getText();

        try
        {
        	new URL( sel_item );
        }
        catch( Exception e )
        {
            sel_item = XProjectConstants.DEFAULT_PROTOCOL+sel_item;
        }

       	editor.setText(sel_item);
       	return sel_item;
	}

   	private String decorateURL(KeyEvent e, String str)
   	{
	boolean alt = (e.getModifiers() & InputEvent.ALT_MASK)!=0;
	boolean ctrl = (e.getModifiers() & InputEvent.CTRL_MASK)!=0;
	boolean shft = (e.getModifiers() & InputEvent.SHIFT_MASK)!=0;
	XDomainCompletion domain_completion = XProjectConfig.getInstance().getDomainCompletion(alt, ctrl, shft);
	StringBuffer url_str = new StringBuffer(str);
	String host, postfix, prefix, new_host;
	int host_start_index = -1;
	URL url = null;

		try
		{
			url = new URL( str );
		}
		catch( Exception ex )
		{
			editor.setText(str);
			return str;
		}

		host = url.getHost();
		host_start_index = str.indexOf(host);

		if( domain_completion!=null )
		{
			prefix = domain_completion.getPrefix();
			postfix = domain_completion.getPostfix();
		}
		else
		{
			prefix = "";
			postfix = "";
		}

		if( !host.startsWith(prefix) )
			new_host = prefix+host;
		else
			new_host = host;

		if( !host.endsWith(postfix) )
			new_host += postfix;

		url_str.replace( host_start_index, host_start_index+host.length(), new_host );
		str = url_str.toString();

       	editor.setText(str);
       	return str;
	}

   	public void keyPressed(KeyEvent e)
    {
        if( e.getKeyCode()==KeyEvent.VK_ENTER )
        {
        String url = decorateURL(e, getCurrentURL());

            addURL(url);

	    	if( listener!=null )
	    		listener.urlActivated(url);
	    }
    }

   	public void keyReleased(KeyEvent e)
    {
    char ch = e.getKeyChar();

        if( ch==KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch) )
            return;

    int pos = editor.getCaretPosition();
    String str = editor.getText();

        if( str.length()==0 )
            return;

        for( int i=0; i<getItemCount(); i++ )
        {
        String item = getItemAt(i).toString();

            if( item.startsWith(str) )
            {
                editor.setText(item);
                editor.setCaretPosition(item.length());
                editor.moveCaretPosition(pos);
                break;
            }
        }
    }

   	public void keyTyped(KeyEvent e) {}

   	public void itemStateChanged(ItemEvent e)
    {
        if( e.getStateChange()!=ItemEvent.SELECTED )
            return;

        if( ((DefaultComboBoxModel)getModel()).getIndexOf(getEditor().getItem())!=-1 )
        {
        String url = getCurrentURL();

            addURL(url);

	    	if( listener!=null )
	    		listener.urlActivated(url);
	    }
    }

   	public void focusGained(FocusEvent e)
    {
        getEditor().selectAll();
    }

	public void focusLost(FocusEvent e) {}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e)
	{
		if( e.isPopupTrigger() && listener!=null )
			listener.comboBoxPopupRequested(e.getX(), e.getY());
	}

// Attributes:
    private static final int MAX_MEM_LEN = 30;
    private XURLComboBoxListener listener;
    private JTextField editor;
}
