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

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

import xbrowser.renderer.event.*;

public class XAppletView extends ComponentView
{
	public XAppletView(Element elem, XHTMLEditorKit.XAppletContext applet_context)
	{
		super(elem);

		retrieveAppletAttributes();
		appletContext = applet_context;

		documentBase = ((HTMLDocument)getDocument()).getBase();
		try
		{
			if( codebaseParam==null )
				codeBase = new URL(documentBase, ".");
			else
				codeBase = new URL(documentBase, codebaseParam.endsWith("/") ? codebaseParam : codebaseParam+"/");
		}
		catch( Exception e )
		{
			codeBase = documentBase;
		}

		buildClassLoader();
	}

	public Applet getApplet()
	{
		return myApplet;
	}

	public void destroyApplet()
	{
		appletContainer.destroyApplet();
	}

	private void buildClassLoader()
	{
	LinkedList url_list = new LinkedList();

		url_list.add(codeBase);

		if( archiveParam!=null )
		{
		StringTokenizer tokenizer = new StringTokenizer(archiveParam, ",;");

			while( tokenizer.hasMoreTokens() )
			{
				try
				{
					url_list.add( new URL(codeBase, tokenizer.nextToken()) );
				}
				catch( Exception e )
				{
					e.printStackTrace();
				}
			}
		}

	URL[] urls = new URL[url_list.size()];
	Iterator it = url_list.iterator();
	int i = 0;

		while( it.hasNext() )
			urls[i++] = (URL)it.next();

		myClassLoader = new URLClassLoader(urls);
	}

	private void retrieveAppletAttributes()
	{
	AttributeSet attr = getElement().getAttributes();

		altParam = getAttribute(attr, HTML.Attribute.ALT);
		archiveParam = getAttribute(attr, HTML.Attribute.ARCHIVE);
		codeParam = getAttribute(attr, HTML.Attribute.CODE);
		codebaseParam = getAttribute(attr, HTML.Attribute.CODEBASE);
		heightParam = getAttribute(attr, HTML.Attribute.HEIGHT);
		hspaceParam = getAttribute(attr, HTML.Attribute.HSPACE);
		nameParam = getAttribute(attr, HTML.Attribute.NAME);
		vspaceParam = getAttribute(attr, HTML.Attribute.VSPACE);
		widthParam = getAttribute(attr, HTML.Attribute.WIDTH);

		if( codeParam!=null && codeParam.endsWith(".class") )
			codeParam = codeParam.substring(0, codeParam.length()-6);
	}

	private String getAttribute(AttributeSet attr, HTML.Attribute attr_name)
	{
	String result = (String)attr.getAttribute(attr_name);

		return( result==null ? result : result.trim() );
	}

	protected Component createComponent()
	{
		if( appletContainer!=null )
			return appletContainer;

		appletContainer = new XAppletContainer();

		return appletContainer;
	}

	private void printDebugInfo()
	{
	URL[] urls = myClassLoader.getURLs();

		System.out.println("--- ClassLoader search path ---");
		for( int i=0; i<urls.length; i++ )
			System.out.println(urls[i]);
		System.out.println("-------------------------------");

		System.out.println("--- Applet Attributes & Parameters ---");

	AttributeSet attr = getElement().getAttributes();
	Enumeration enum = attr.getAttributeNames();

		while( enum.hasMoreElements() )
		{
		Object key = enum.nextElement();
		Object value = attr.getAttribute(key);

			System.out.println(key+" = "+value);
		}
		System.out.println("--------------------------------------");
	}

	private class XAppletContainer extends JPanel
	{
		public XAppletContainer()
		{
			super( new BorderLayout() );

		Dimension size = null;

			setBackground(Color.lightGray);
			try
			{
				size = new Dimension(Integer.parseInt(widthParam), Integer.parseInt(heightParam));
			}
			catch( Exception ex )
			{
				size = new Dimension(50, 50);
			}

			setSize(size);
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
		}

		public void addNotify()
		{
			super.addNotify();

			appletContext.showAppletLifeCycle(null, XRendererListener.STARTING_APPLET_LOAD);
			try
			{
				myApplet = (Applet)Class.forName(codeParam, true, myClassLoader).newInstance();
			}
			catch( Exception e )
			{
				appletContext.showAppletLifeCycle(null, XRendererListener.APPLET_LOAD_FAILED);
				return;
			}

			appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_LOAD_SUCCESS);

			appletThread = new XAppletThread();
			myApplet.setStub(new XAppletStub());
			myApplet.setSize( getSize() );

			startApplet();
		}

		private void startApplet()
		{
			if( appletThread==null || appletThread.isActive() )
				return;

			add(BorderLayout.CENTER, myApplet);

			appletThread.initializeApplet();
			appletThread.startApplet();
		}

		public void destroyApplet()
		{
			//remove(myApplet);
			appletThread.finalizeApplet();
			//appletThread = null;
		}
	}

	private class XAppletStub implements AppletStub
	{
		public void appletResize(int width, int height)
		{
			if( myApplet!=null && width>0 && height>0 )
				myApplet.resize(width, height);
		}

		public AppletContext getAppletContext()
		{
			return appletContext;
		}

		public URL getCodeBase()
		{
			return codeBase;
		}

		public URL getDocumentBase()
		{
			return documentBase;
		}

		public String getParameter(String param_name)
		{
		Object param_value = getElement().getAttributes().getAttribute(param_name);

			return( (param_value==null) ? null : param_value.toString() );
		}

		public boolean isActive()
		{
			return appletThread.isActive();
		}
	}

	private class XAppletThread extends Thread
	{
		public boolean isActive()
		{
			return active;
		}

		public boolean isInitialized()
		{
			return initialized;
		}

		public void initializeApplet()
		{
			start();
			doAction(INIT);
		}

		public void startApplet()
		{
			doAction(START);
		}

		public void finalizeApplet()
		{
			doAction(STOP);
			doAction(DESTROY);
		}

		public synchronized void doAction(Integer type)
		{
			synchronized( jobList )
			{
				jobList.addLast(type);
			}
		}

		private void doInitAction()
		{
			if( !initialized && !active )
			{
				appletContext.showAppletLifeCycle(myApplet, XRendererListener.STARTING_APPLET_INIT);

				try
				{
					myApplet.init();
					initialized = true;
					appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_INIT_SUCCESS);
				}
				catch( Exception e )
				{
					e.printStackTrace();
					appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_INIT_FAILED);
				}
			}
		}

		private void doStartAction()
		{
			if( initialized && !active )
			{
				appletContext.showAppletLifeCycle(myApplet, XRendererListener.STARTING_APPLET_START);

				try
				{
					myApplet.start();
					active = true;

					myApplet.addMouseListener( new MouseAdapter() {
						public void mouseEntered(MouseEvent e)
						{
							appletContext.showStatus(myApplet.getAppletInfo());
						}

						public void mouseExited(MouseEvent e)
						{
							appletContext.showStatus(null);
						}
					});

					appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_START_SUCCESS);
				}
				catch( Exception e )
				{
					e.printStackTrace();
					appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_START_FAILED);
				}
			}
		}

		private void doStopAction()
		{
			appletContext.showAppletLifeCycle(myApplet, XRendererListener.STARTING_APPLET_STOP);
			active = false;

			try
			{
				myApplet.stop();
				appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_STOP_SUCCESS);
			}
			catch( Exception e )
			{
				e.printStackTrace();
				appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_STOP_FAILED);
			}
		}

		private void doDestroyAction()
		{
			appletContext.showAppletLifeCycle(myApplet, XRendererListener.STARTING_APPLET_DESTROY);

			try
			{
				myApplet.destroy();
				appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_DESTROY_SUCCESS);
			}
			catch( Exception e )
			{
				e.printStackTrace();
				appletContext.showAppletLifeCycle(myApplet, XRendererListener.APPLET_DESTROY_FAILED);
			}
			myApplet = null;
		}

		public void run()
		{
		Integer status = null;

			while( true )
			{
				try
				{
					sleep(500);
				}
				catch( Exception e )
				{
				}

				synchronized( jobList )
				{
					if( !jobList.isEmpty() )
						status = (Integer)jobList.removeFirst();
					else
						continue;
				}

				if( status==INIT )
					doInitAction();
				else if( status==START )
					doStartAction();
				else if( status==STOP )
					doStopAction();
				else if( status==DESTROY )
				{
					doDestroyAction();
					return;
				}
			}
		}

	// Attributes:
		private boolean	active = false;
		private boolean	initialized = false;

		private LinkedList jobList = new LinkedList();

		private final Integer INIT = new Integer(0);
		private final Integer START = new Integer(1);
		private final Integer STOP = new Integer(2);
		private final Integer DESTROY = new Integer(3);
	}

	// Attributes:
	private String							altParam, archiveParam, codeParam, codebaseParam, heightParam, hspaceParam, nameParam, vspaceParam, widthParam, objectParam;
	private Applet							myApplet = null;
	private XHTMLEditorKit.XAppletContext   appletContext = null;
	private XAppletThread					appletThread = null;
	private URL								documentBase, codeBase;

	private URLClassLoader		myClassLoader = null;
	private XAppletContainer	appletContainer = null;
}
