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
package xbrowser.history;

import java.io.*;
import java.util.*;
import java.text.*;
import org.w3c.dom.*;

import xbrowser.*;
import xbrowser.util.*;

// the following line is for IBM parser
//import com.ibm.xml.parser.Parser;

// the following 4 lines are for Sun parser
import com.sun.xml.parser.Resolver;
import com.sun.xml.tree.*;
import org.xml.sax.*;

final class XHistorySerializer
{
	public void loadHistory(String file_name, XHistoryManager history_manager) throws Exception
	{
	Node node = XMLManager.findNode(XMLManager.readDocument(file_name),"xhistory").getNextSibling().getFirstChild();

		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("xhistorydata") )
					loadHistoryData(node,history_manager);
			}

			node = node.getNextSibling();
		}
	}

	private void loadHistoryData(Node node, XHistoryManager history_manager) throws DOMException
	{
	XHistoryData history_data = new XHistoryData( XMLManager.getNodeAttribute(node,"location") );

		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("title") )
					history_data.setTitle( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("firstvisited") )
					history_data.setFirstVisited( buildDate(node) );
				else if( node.getNodeName().equals("lastvisited") )
					history_data.setLastVisited( buildDate(node) );
				else if( node.getNodeName().equals("expiration") )
					history_data.setExpiration( buildDate(node) );
				else if( node.getNodeName().equals("visitcount") )
					history_data.setVisitCount( buildInteger(node) );
			}

			node = node.getNextSibling();
		}

		history_manager.addHistoryData(history_data);
	}

	private Date buildDate(Node node) throws DOMException
	{
	Date date = new Date();

        try
        {
	        date = df.parse( XMLManager.getNodeValue(node) );
		}
		catch( Exception e )
		{
		}

		return date;
	}

	private int buildInteger(Node node) throws DOMException
	{
	int i = 0;

        try
        {
	        i = new Integer(XMLManager.getNodeValue(node)).intValue();
		}
		catch( Exception e )
		{
		}

		return i;
	}

	public void saveHistory(String file_name, XHistoryManager history_manager) throws Exception
	{
	XmlDocument xml_doc = new XmlDocument();
	OutputStream out = new FileOutputStream(file_name);
	ElementNode root = (ElementNode)xml_doc.createElement("xhistory");

		xml_doc.appendChild(root);
		//xml_doc.setDoctype(null,file_name+".dtd",null);
		xml_doc.setDoctype(null, null, dtdContent);

		//// Saving History
	Iterator history = history_manager.getHistory();

		while( history.hasNext() )
			saveHistoryData( (XHistoryData)history.next(), xml_doc, root);
		//// History Saved

		root.normalize();
		xml_doc.write(out);
		out.flush();
		out.close();
	}

	private void saveHistoryData(XHistoryData history_data, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement("xhistorydata");

		node.setAttribute("location", history_data.getLocation() );

		XMLManager.addDataNodeTo(xml_doc, node, "title", history_data.getTitle());
		XMLManager.addDataNodeTo(xml_doc, node, "firstvisited", df.format(history_data.getFirstVisited()));
		XMLManager.addDataNodeTo(xml_doc, node, "lastvisited", df.format(history_data.getLastVisited()));
		XMLManager.addDataNodeTo(xml_doc, node, "expiration", df.format(history_data.getExpiration()));
		XMLManager.addDataNodeTo(xml_doc, node, "visitcount", ""+history_data.getVisitCount());

		parent_node.appendChild(node);
	}

// Attributes:
	private DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
	private String dtdContent = "<!ELEMENT xhistory (xhistorydata*)>"+"\n"+
								"<!ELEMENT xhistorydata (title, firstvisited, lastvisited, expiration, visitcount)>"+"\n"+
								"<!ELEMENT title (#PCDATA)>"+"\n"+
								"<!ELEMENT firstvisited (#PCDATA)>"+"\n"+
								"<!ELEMENT lastvisited (#PCDATA)>"+"\n"+
								"<!ELEMENT expiration (#PCDATA)>"+"\n"+
								"<!ELEMENT visitcount (#PCDATA)>"+"\n"+
								"<!ATTLIST xhistorydata location CDATA #REQUIRED>";
}
