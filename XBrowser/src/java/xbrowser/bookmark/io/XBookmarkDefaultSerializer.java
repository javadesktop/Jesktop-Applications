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
package xbrowser.bookmark.io;

import java.io.*;
import java.util.*;
import javax.swing.plaf.*;
import org.w3c.dom.*;

import xbrowser.*;
import xbrowser.util.*;
import xbrowser.bookmark.*;

// the following line is for IBM parser
//import com.ibm.xml.parser.Parser;

// the following 4 lines are for Sun parser
import com.sun.xml.parser.Resolver;
import com.sun.xml.tree.*;
import org.xml.sax.*;

public class XBookmarkDefaultSerializer extends XBookmarkSerializer
{
	XBookmarkDefaultSerializer()
	{
	}

	public String getName()
	{
		return XProjectConstants.PRODUCT_NAME;
	}

	public String getFormat()
	{
		return "xml";
	}

	public boolean hasSingleFileStructure()
	{
		return true;
	}

	public void importFrom(String file_name, XBookmarkFolder root_folder) throws Exception
	{
	Node node = XMLManager.findNode(XMLManager.readDocument(file_name),"xbookmarks").getNextSibling().getFirstChild();

		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("xfolder") )
					loadBookmarkFolder(node,root_folder);
				else if( node.getNodeName().equals("xbookmark") )
					loadBookmark(node,root_folder);
			}

			node = node.getNextSibling();
		}
	}

	private void loadBookmarkFolder(Node node, XBookmarkFolder parent) throws DOMException
	{
	XBookmarkFolder bm_folder = new XBookmarkFolder();
	Node temp_node = node;

		temp_node = node.getFirstChild();
		while( temp_node!=null )
		{
			if( temp_node instanceof Element )
			{
				if( temp_node.getNodeName().equals("title") )
					bm_folder.setTitle( XMLManager.getNodeValue(temp_node) );
				else if( temp_node.getNodeName().equals("desc") )
					bm_folder.setDescription( XMLManager.getNodeValue(temp_node) );
				else if( temp_node.getNodeName().equals("creationdate") )
					bm_folder.setCreationDate( buildDate(temp_node) );
				else if( temp_node.getNodeName().equals("modificationdate") )
					bm_folder.setModificationDate( buildDate(temp_node) );
				else if( temp_node.getNodeName().equals("personalfolder") )
					bm_folder.setPersonalFolder(true);
			}

			temp_node = temp_node.getNextSibling();
		}

		parent.addBookmark(bm_folder);

		temp_node = node.getFirstChild();
		while( temp_node!=null )
		{
			if( temp_node instanceof Element )
			{
				if( temp_node.getNodeName().equals("xfolder") )
					loadBookmarkFolder(temp_node,bm_folder);
				else if( temp_node.getNodeName().equals("xbookmark") )
					loadBookmark(temp_node,bm_folder);
			}

			temp_node = temp_node.getNextSibling();
		}
	}

	private void loadBookmark(Node node, XBookmarkFolder parent) throws DOMException
	{
	XBookmark bookmark = new XBookmark( XMLManager.getNodeAttribute(node,"href") );

		node = node.getFirstChild();
		while( node!=null )
		{
			if( node instanceof Element )
			{
				if( node.getNodeName().equals("title") )
					bookmark.setTitle( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("desc") )
					bookmark.setDescription( XMLManager.getNodeValue(node) );
				else if( node.getNodeName().equals("creationdate") )
					bookmark.setCreationDate( buildDate(node) );
				else if( node.getNodeName().equals("modificationdate") )
					bookmark.setModificationDate( buildDate(node) );
			}

			node = node.getNextSibling();
		}

		parent.addBookmark(bookmark);
	}

	private Date buildDate(Node node) throws DOMException
	{
	Date date = null;

        try
        {
	        date = df.parse( XMLManager.getNodeValue(node) );
		}
		catch( Exception e )
		{
			date = new Date();
		}

		return date;
	}

	public void exportTo(String file_name, XBookmarkFolder root_folder) throws Exception
	{
	XmlDocument xml_doc = new XmlDocument();
	OutputStream out = new FileOutputStream(file_name);
	ElementNode root = (ElementNode)xml_doc.createElement("xbookmarks");

		xml_doc.appendChild(root);
		//xml_doc.setDoctype(null,file_name+".dtd",null);
		xml_doc.setDoctype(null, null, dtdContent);

		//// Saving Bookmarks
	Iterator bookmarks = root_folder.getBookmarks();

		while( bookmarks.hasNext() )
			saveBookmarksImpl( (XAbstractBookmark)bookmarks.next(), xml_doc, root);
		//// Bookmarks Saved

		root.normalize();
		xml_doc.write(out);
		out.flush();
		out.close();
	}

	private void saveBookmarksImpl(XAbstractBookmark abs_bm, XmlDocument xml_doc, ElementNode parent_node)
	{
		if( abs_bm instanceof XBookmark )
			saveBookmark( (XBookmark)abs_bm, xml_doc, parent_node);
		else if( abs_bm instanceof XBookmarkFolder )
			saveBookmarkFolder( (XBookmarkFolder)abs_bm, xml_doc, parent_node);
	}

	private void saveBookmark(XBookmark bookmark, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement("xbookmark");

		node.setAttribute("href", bookmark.getHRef() );

		XMLManager.addDataNodeTo(xml_doc, node, "title", bookmark.getTitle());
		XMLManager.addDataNodeTo(xml_doc, node, "desc", bookmark.getDescription());
		XMLManager.addDataNodeTo(xml_doc, node, "creationdate", df.format(bookmark.getCreationDate()));
		XMLManager.addDataNodeTo(xml_doc, node, "modificationdate", df.format(bookmark.getModificationDate()));

		parent_node.appendChild(node);
	}

	private void saveBookmarkFolder(XBookmarkFolder bm_folder, XmlDocument xml_doc, ElementNode parent_node)
	{
	ElementNode node = (ElementNode)xml_doc.createElement("xfolder");

		XMLManager.addDataNodeTo(xml_doc, node, "title", bm_folder.getTitle());
		XMLManager.addDataNodeTo(xml_doc, node, "desc", bm_folder.getDescription());
		XMLManager.addDataNodeTo(xml_doc, node, "creationdate", df.format(bm_folder.getCreationDate()));
		XMLManager.addDataNodeTo(xml_doc, node, "modificationdate", df.format(bm_folder.getModificationDate()));

		if( bm_folder.isPersonalFolder() )
			node.appendChild( (ElementNode)xml_doc.createElement("personalfolder") );

		parent_node.appendChild(node);

	Iterator bookmarks = bm_folder.getBookmarks();

		while( bookmarks.hasNext() )
			saveBookmarksImpl( (XAbstractBookmark)bookmarks.next(), xml_doc, node);
	}

// Attributes:
	private String dtdContent = "<!ELEMENT xbookmarks ((xfolder | xbookmark)*, (xbookmark | xfolder)*)>"+"\n"+
								"<!ELEMENT xfolder (title, desc, creationdate, modificationdate, personalfolder?, (xfolder | xbookmark)*, (xbookmark | xfolder)*)>"+"\n"+
								"<!ELEMENT xbookmark (title, desc, creationdate, modificationdate)>"+"\n"+
								"<!ELEMENT title (#PCDATA)>"+"\n"+
								"<!ELEMENT desc (#PCDATA)>"+"\n"+
								"<!ELEMENT creationdate (#PCDATA)>"+"\n"+
								"<!ELEMENT modificationdate (#PCDATA)>"+"\n"+
								"<!ELEMENT personalfolder EMPTY>"+"\n"+
								"<!ATTLIST xbookmark href CDATA #REQUIRED>";
}
