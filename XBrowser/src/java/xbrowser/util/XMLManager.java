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
package xbrowser.util;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;

import xbrowser.widgets.*;

// the following line is for IBM parser
//import com.ibm.xml.parser.Parser;

// the following 4 lines are for Sun parser
import com.sun.xml.parser.Resolver;
import com.sun.xml.tree.*;
import org.xml.sax.*;

public final class XMLManager
{
	public static String getNodeValue(Node node)
	{
		return( (node.getFirstChild()==null) ? "" : node.getFirstChild().getNodeValue() );
	}

	public static Document readDocument(String filename) throws IOException, SAXException
	{
	// if you are using the IBM parser, use the commented lines

	//    Parser parser = new Parser(filename);
	//    InputStream input = new FileInputStream(filename);
	//    Document doc = parser.readStream(input);
	//    input.close();
	//    return doc;

	// if you are using the Sun parser, use these lines
	InputSource input = Resolver.createInputSource(new File(filename));
	XmlDocument doc = XmlDocument.createXmlDocument(input,true);

		doc.getDocumentElement().normalize();
		return doc;
	}

	public static Node findNode(Node node, String name)
	{
		if( node.getNodeName().equals(name) )
			return node;

		if( node.hasChildNodes() )
		{
		NodeList list = node.getChildNodes();
		int size = list.getLength();

			for (int i = 0; i < size; i++)
			{
			Node found = findNode(list.item(i), name);

				if( found!=null )
					return found;
			}
		}

		return null;
	}

	public static String getNodeAttribute(Node node, String name)
	{
		if( node instanceof Element )
			return ((Element)node).getAttribute(name);

		return null;
	}

	public static void addDataNodeTo(XmlDocument xml_doc, ElementNode parent_node, String new_node_name, String new_node_data)
	{
	ElementNode node = (ElementNode)xml_doc.createElement(new_node_name);

		node.appendChild( xml_doc.createTextNode(new_node_data) );
		parent_node.appendChild(node);
	}
}
