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

import java.awt.datatransfer.*;

public class XObjectSelection implements Transferable, ClipboardOwner
{
    public XObjectSelection(Object data)
    {
        this.data = data;
    }

    public DataFlavor[] getTransferDataFlavors()
    {
		return (DataFlavor[])flavors.clone();
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (int i = 0; i < flavors.length; i++)
        {
			if (flavors[i].equals(flavor))
				return true;
		}

		return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
    {
		if( isDataFlavorSupported(flavor) )
	    	return data;
		else
	    	throw new UnsupportedFlavorException(flavor);
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents)
    {
    }

// Attributes:
	public static DataFlavor localObjectFlavor = null;
    private static DataFlavor[] flavors = null;
	static
	{
		try
		{
			localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
			flavors = new DataFlavor[] {localObjectFlavor};
		}
		catch( Exception e )
		{
			flavors = new DataFlavor[] {};
		}
	}

    private Object data;
}
