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
package xbrowser.screen;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import xbrowser.widgets.*;
import xbrowser.bookmark.io.*;
import xbrowser.bookmark.*;
import xbrowser.*;

public class XImportExportBookmarksLayout extends XFrame
{
	public static XImportExportBookmarksLayout getInstance(int type, XBookmarkFolder base_folder)
	{
		if( instance==null )
			instance = new XImportExportBookmarksLayout();

		instance.setType(type);
		instance.setBaseFolder(base_folder);

		return instance;
	}

	public static XImportExportBookmarksLayout getInstance()
	{
		return getInstance(IMPORT_BOOKMARKS, null);
	}

	private XImportExportBookmarksLayout()
	{
		getContentPane().setLayout( new BorderLayout(5,5) );

        getContentPane().add(getCenterPanel(),BorderLayout.CENTER);
        getContentPane().add(getButtonsPanel(),BorderLayout.SOUTH);

        setIconImage( XComponentBuilder.getInstance().buildImageIcon(this, "image.FrameIcon").getImage() );
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        registerListeners();

        pack();

    Dimension size = getSize();

        size.height = 280;
        setSize(size);
        setResizable(false);
	}

    private void registerListeners()
    {
	ItemListener item_listener = new ItemListener() {
			public void itemStateChanged(ItemEvent e)
			{
				if( rdoImport.isSelected() )
					setType(IMPORT_BOOKMARKS);
				else
					setType(EXPORT_BOOKMARKS);
			}
		};

		rdoImport.addItemListener(item_listener);
		rdoExport.addItemListener(item_listener);
	}

	private void addToContainer(Component comp,Container container,GridBagLayout gridbag,GridBagConstraints constraints,int grid_width,double weight_x)
	{
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
		container.add(comp);
	}

    private JPanel getCenterPanel()
    {
    JPanel pnl = new JPanel( new BorderLayout() );
    JPanel pnl_import_export = new JPanel( new GridLayout(2,1) );
    ButtonGroup grp_import_export = new ButtonGroup();
    ButtonGroup grp_format = new ButtonGroup();
    GridBagLayout gridbag1 = new GridBagLayout();
    GridBagConstraints constraints1 = new GridBagConstraints();
    GridBagLayout gridbag2 = new GridBagLayout();
    GridBagConstraints constraints2 = new GridBagConstraints();
    JPanel pnl_north = new JPanel( new BorderLayout() );
    JPanel pnl_center = new JPanel(gridbag1);
    JPanel pnl_south = new JPanel(gridbag2);
    Iterator serializers = XBookmarkSerializer.getAvailableSerializers();

    	grp_import_export.add(rdoImport);
    	grp_import_export.add(rdoExport);
    	pnl_import_export.add(rdoImport);
    	pnl_import_export.add(rdoExport);
    	pnl_import_export.setBorder( new TitledBorder(XComponentBuilder.getInstance().getProperty(this, "Bookmarks")) );

		while( serializers.hasNext() )
		{
		XBookmarkSerializer ser = (XBookmarkSerializer)serializers.next();
		JRadioButton rdo = XComponentBuilder.getInstance().buildRadioButton(this, "Format2", ser.getName(), "(."+ser.getFormat()+")");

			rdo.putClientProperty("XSerializer", ser);
			serializerComponents.add(rdo);
			grp_format.add(rdo);
	    	pnlFormat.add(rdo);

	    	if( ser==XBookmarkSerializer.getDefaultSerializer() )
		    	rdo.setSelected(true);
		}

        pnl_north.add(pnl_import_export,BorderLayout.WEST);
        pnl_north.add(pnlFormat,BorderLayout.CENTER);

        constraints1.fill = GridBagConstraints.HORIZONTAL;
        constraints1.insets = new Insets(0,5,0,5);
		addToContainer(XComponentBuilder.getInstance().buildLabel(this, "BaseFolder"),pnl_center,gridbag1,constraints1,1,0);
		addToContainer(cmbBaseFolder,pnl_center,gridbag1,constraints1,GridBagConstraints.REMAINDER,1);

        constraints2.fill = GridBagConstraints.HORIZONTAL;
        constraints2.insets = new Insets(0,5,0,5);
		addToContainer(lblImportExportIO,pnl_south,gridbag2,constraints2,1,0);
		addToContainer(txfImportExportIO,pnl_south,gridbag2,constraints2,GridBagConstraints.RELATIVE,1);
		addToContainer(XComponentBuilder.getInstance().buildButton(new BrowseAction()),pnl_south,gridbag2,constraints2,GridBagConstraints.REMAINDER,0);

        pnl.add(pnl_north,BorderLayout.NORTH);
        pnl.add(pnl_center,BorderLayout.CENTER);
        pnl.add(pnl_south,BorderLayout.SOUTH);

    	return pnl;
	}

    private JPanel getButtonsPanel()
    {
    JPanel pnl_main = new JPanel();
    JPanel pnl_buttons = new JPanel();
	JButton btn_import_export = XComponentBuilder.getInstance().buildButton(importExportAction);

        pnl_main.setLayout(new FlowLayout());
        pnl_buttons.setLayout(new GridLayout(1,2,15,5));
        pnl_buttons.add(btn_import_export);
        pnl_buttons.add(XComponentBuilder.getInstance().buildButton(new CloseAction()));
        pnl_main.add(pnl_buttons);

        getRootPane().setDefaultButton(btn_import_export);

        return pnl_main;
	}

	public void setBaseFolder(XBookmarkFolder base_folder)
	{
		cmbBaseFolder.setSelectedItem(base_folder);
	}

	public void setType(int type)
	{
		if( type==IMPORT_BOOKMARKS )
		{
			setTitle(XComponentBuilder.getInstance().getProperty(this, "Import.Title"));
			pnlFormat.setBorder( new TitledBorder(XComponentBuilder.getInstance().getProperty(this, "ImportAs")) );
			lblImportExportIO.setText(XComponentBuilder.getInstance().getProperty(this, "ImportFrom"));
			importExportAction.putValue(Action.NAME, "Import");

			if( !rdoImport.isSelected() )
				rdoImport.setSelected(true);
		}
		else if( type==EXPORT_BOOKMARKS )
		{
			setTitle(XComponentBuilder.getInstance().getProperty(this, "Export.Title"));
			pnlFormat.setBorder( new TitledBorder(XComponentBuilder.getInstance().getProperty(this, "ExportAs")) );
			lblImportExportIO.setText(XComponentBuilder.getInstance().getProperty(this, "ExportTo"));
			importExportAction.putValue(Action.NAME, "Export");

			if( !rdoExport.isSelected() )
				rdoExport.setSelected(true);
		}
	}

    private class CloseAction extends XAction
    {
        public CloseAction()
        {
            super(XImportExportBookmarksLayout.this, "Close", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		    setVisible(false);
        }
    }

    private class ImportExportAction extends XAction
    {
        public ImportExportAction()
        {
            super(XImportExportBookmarksLayout.this, "ImportExport", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		String result_msg, result_title;
		int result_type;

			try
			{
			Iterator it = serializerComponents.iterator();

				while( it.hasNext() )
				{
				JRadioButton rdo = (JRadioButton)it.next();

					if( rdo.isSelected() )
					{
					XBookmarkSerializer ser = (XBookmarkSerializer)rdo.getClientProperty("XSerializer");
					XBookmarkFolder bm_folder = cmbBaseFolder.getSelectedBookmark();

						if( rdoImport.isSelected() )
							ser.importFrom(txfImportExportIO.getText().trim(), bm_folder);
						else
							ser.exportTo(txfImportExportIO.getText().trim(), bm_folder);

						break;
					}
				}

				result_msg = XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "EmportExportSuccessful");
				result_title = XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "Successful");
				result_type = JOptionPane.INFORMATION_MESSAGE;
			}
			catch( /*IO*/Exception ex1 )
			{
				result_msg = XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "EmportExportError");
				result_title = XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "Error");
				result_type = JOptionPane.ERROR_MESSAGE;
			}
			/*catch( Exception ex2 )
			{
				result_msg = ex2.getMessage();
				result_title = XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "Error");
				result_type = JOptionPane.ERROR_MESSAGE;
			}*/

			JOptionPane.showMessageDialog(null, result_msg, result_title, result_type);
        }
    }

    private class BrowseAction extends XAction
    {
        public BrowseAction()
        {
            super(XImportExportBookmarksLayout.this, "Browse", null);
        }

        public void actionPerformed(ActionEvent e)
        {
		JFileChooser file_chooser = XComponentBuilder.getInstance().getImportExportFileChooser();
		Iterator it = serializerComponents.iterator();
		int result = -1;

			while( it.hasNext() )
			{
			JRadioButton rdo = (JRadioButton)it.next();

				if( rdo.isSelected() )
				{
				XBookmarkSerializer ser = (XBookmarkSerializer)rdo.getClientProperty("XSerializer");
				String format = XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "Format", ser.getName());

					file_chooser.setFileSelectionMode(ser.hasSingleFileStructure() ? JFileChooser.FILES_ONLY : JFileChooser.DIRECTORIES_ONLY);

					if( rdoImport.isSelected() )
					{
						file_chooser.setDialogTitle(XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "ImportAsFormat", format));
						result = file_chooser.showOpenDialog(null);
					}
					else
					{
						file_chooser.setDialogTitle(XComponentBuilder.getInstance().getProperty(XImportExportBookmarksLayout.this, "ExportAsFormat", format));
						result = file_chooser.showSaveDialog(null);
					}
					break;
				}
			}

			if( result==JFileChooser.APPROVE_OPTION )
				txfImportExportIO.setText( file_chooser.getSelectedFile().toString() );
        }
    }

// Attributes:
	private static XImportExportBookmarksLayout instance = null;

	public static final int IMPORT_BOOKMARKS = 70;
	public static final int EXPORT_BOOKMARKS = 71;

	private JRadioButton rdoImport = XComponentBuilder.getInstance().buildRadioButton(this, "ImportBookmarks");
	private JRadioButton rdoExport = XComponentBuilder.getInstance().buildRadioButton(this, "ExportBookmarks");

	private LinkedList serializerComponents = new LinkedList();

	private JLabel lblImportExportIO = new JLabel("");
	private JTextField txfImportExportIO = new JTextField(25);
	private XBookmarkFolderComboBox cmbBaseFolder = new XBookmarkFolderComboBox();
	private XAction importExportAction = new ImportExportAction();

	private JPanel pnlFormat = new JPanel( new GridLayout(4,1) );
}
