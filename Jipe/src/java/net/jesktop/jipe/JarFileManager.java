/**
 * JarFileManager.java - Java Jar file manager class for use with the Jipe
 * Project Copyright (c) 1996-1999 Steve Lawson.  E-Mail steve@e-i-s.co.uk
 * Latest version can be found at http://e-i-s.co.uk/jipe
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.jesktop.jipe;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import javax.swing.*;


public class JarFileManager extends JFrame implements WindowListener, ActionListener, ItemListener
{

    //
    ///////////////////////////////////////////////////////////////////////////
    // Others
    ///////////////////////////////////////////////////////////////////////////
    //

    Jipe jipe;
    java.awt.List filenamelist;
    JTextField jarname;
    JCheckBox compress = new JCheckBox();
    JCheckBox manifest = new JCheckBox();
    JLabel projectdirectory;
    String filename = "Jar File Manager";
    String directoryname = "";
    JMenuItem newItem = new JMenuItem("Create New Jar");
    JMenuItem openItem = new JMenuItem("List an Existing Jar");
    JMenuItem extractItem = new JMenuItem("Extract an Existing Jar");
    JMenuItem saveasItem = new JMenuItem("Save Jar As");
    JMenuItem changedirItem = new JMenuItem("Change Working Directory");
    JMenuItem quitItem = new JMenuItem("Close Jar File Manager");
    JMenuItem addItem = new JMenuItem("Add a New File");
    JMenuItem deleteItem = new JMenuItem("Delete Selected Files");
    JMenuItem addAllItem = new JMenuItem("Add all Files of Type");
    JMenuItem selectItem = new JMenuItem("Select All");
    JMenuItem deselectItem = new JMenuItem("De-Select All");
    public JarFileManager(Jipe parent, String title)
    {
        super(title);
        jipe = parent;
        filename = title;
        Font font = new Font("Dialog", Font.PLAIN, 10);
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu options = new JMenu("Options");
        JMenu compile = new JMenu("Compile");

        // FILE MENU LIST
        file = new JMenu("File");
        file.add(newItem);
        file.add(openItem);
        file.add(extractItem);
        file.add(saveasItem);
        file.addSeparator();
        file.add(changedirItem);
        file.addSeparator();
        file.add(quitItem);
        file.setFont(font);
        extractItem.setFont(font);
        changedirItem.setFont(font);
        newItem.setFont(font);
        openItem.setFont(font);
        saveasItem.setFont(font);
        quitItem.setFont(font);
        extractItem.addActionListener(this);
        changedirItem.addActionListener(this);
        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveasItem.addActionListener(this);
        quitItem.addActionListener(this);
        menubar.add(file);

        // OPTIONS LIST
        options = new JMenu("Options");
        options.add(addItem);
        options.add(addAllItem);
        options.add(deleteItem);
        options.addSeparator();
        options.add(selectItem);
        options.add(deselectItem);
        options.setFont(font);
        addItem.setFont(font);
        addAllItem.setFont(font);
        deleteItem.setFont(font);
        selectItem.setFont(font);
        deselectItem.setFont(font);
        menubar.add(options);
        deselectItem.addActionListener(this);
        selectItem.addActionListener(this);
        addItem.addActionListener(this);
        addAllItem.addActionListener(this);
        deleteItem.addActionListener(this);
        filenamelist = new java.awt.List(8, true);
        projectdirectory = new JLabel("Current Directory : ", Label.LEFT);
        compress = new JCheckBox("Store Only; Don't ZIP");
        manifest = new JCheckBox("Exclude manifest File");
        compress.setFont(font);
        manifest.setFont(font);
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        GridLayout grid = new GridLayout(0, 2, 5, 5);
        panel2.setLayout(grid);
        JLabel jLabel1;
        panel.add(jLabel1 = new JLabel("Jar File Name:", Label.LEFT));
        panel.add(jarname = new JTextField("", 15));
        panel.add(compress);
        panel.add(manifest);

        // panel.add(filenamelist);

        panel2.add(panel);
        panel2.add(filenamelist);
        filenamelist.setFont(font);
        jarname.setFont(font);
        jLabel1.setFont(font);
        projectdirectory.setFont(font);
        this.getContentPane().add("North", menubar);
        this.getContentPane().add("Center", panel2);
        this.getContentPane().add("South", projectdirectory);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        projectdirectory.setText(new File("").getAbsolutePath());
        filenamelist.addItemListener(this);
        setResizable(true);
        this.setSize(420, 270);
        setVisible(true);
        this.addWindowListener(this);

    }

    /**
     * WindowListener implementation
     */
    public void windowClosed(WindowEvent event)
    {
    }
    public void windowDeiconified(WindowEvent event)
    {
    }
    public void windowIconified(WindowEvent event)
    {
    }
    public void windowActivated(WindowEvent event)
    {
    }
    public void windowDeactivated(WindowEvent event)
    {
    }
    public void windowOpened(WindowEvent event)
    {
    }
    public void windowClosing(WindowEvent event)
    {
        Close();
    }
    public void itemStateChanged(ItemEvent e)
    {
        String newfile = filenamelist.getSelectedItem();
    }
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == deselectItem)
            deselectAll();
        if (source == selectItem)
            selectAll();
        if (source == addItem)
            AddFile();
        if (source == addAllItem)
            AddAllFiles();
        if (source == deleteItem)
            DeleteFiles();
        if (source == quitItem)
            Close();
        if (source == changedirItem)
            SetDirectory("Set Directory", "*.java");
        if (source == openItem)
            ListJarFiles();
        if (source == extractItem)
            ExtractJarFiles();
        if (source == saveasItem)
            SaveJarFiles();
        if (source == newItem)
            NewJarFile();
    }
    void Close()
    {
        this.setVisible(false);

        // jdt.jarfilemanager = null;

        this.dispose();
    }
    void NewJarFile()
    {
        filenamelist.removeAll();
        jarname.setText("");
    }
    void AddAllFiles()
    {
        FileDialog file_dialog = new FileDialog(
                                     this, "Select Directory to add Files", FileDialog.LOAD);
        file_dialog.setDirectory(directoryname);
        file_dialog.show();
        if (file_dialog.getFile() != null)
        {
            String str = file_dialog.getFile();
            for (int i = 0; i < str.length(); i++)
            {
                if (str.charAt(i) == '.')
                {
                    str = str.substring(i, str.length());
                    break;
                }
            }
            directoryname = file_dialog.getDirectory();
            projectdirectory.setText("Current Directory : " + directoryname);
            String[] files = new File(directoryname).list();
            String path = new File(directoryname).getAbsolutePath();
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].endsWith(str))
                {
                    try
                    {
                        filenamelist.remove(path + files[i]);
                    }
                    catch (Exception err)
                    {
                    }
                    filenamelist.add(path + files[i]);
                }
            }
        }
    }
    void AddFile()
    {
        FileDialog file_dialog = new FileDialog(
                                     this, "Add File", FileDialog.LOAD);
        file_dialog.setDirectory(directoryname);
        file_dialog.show();
        if (file_dialog.getFile() != null)
        {
            directoryname = file_dialog.getDirectory();
            projectdirectory.setText("Current Directory : " + directoryname);
            try
            {
                filenamelist.remove(file_dialog.getDirectory() +
                                    file_dialog.getFile());
            }
            catch (Exception err)
            {
            }
            filenamelist.add(
                file_dialog.getDirectory() + file_dialog.getFile());
        }
    }
    void DeleteFiles()
    {
        int i = 0;
        while (i <= filenamelist.getItemCount() - 1)
        {
            if (filenamelist.isIndexSelected(i) == true)
            {
                filenamelist.remove(i);
                i--;
            }
            i++;
        }
    }
    void deselectAll()
    {
        int i = 0;
        while (i <= filenamelist.getItemCount() - 1)
        {
            if (filenamelist.isIndexSelected(i) == true)
                filenamelist.deselect(i);
            i++;
        }
    }
    void selectAll()
    {
        int i = 0;
        while (i <= filenamelist.getItemCount() - 1)
        {
            if (filenamelist.isIndexSelected(i) == false)
                filenamelist.select(i);
            i++;
        }
    }
    void ListJarFiles()
    {
        FileDialog file_dialog = new FileDialog(
                                     this, "List Existing Jar File", FileDialog.LOAD);
        file_dialog.setDirectory(directoryname);
        file_dialog.setFile("*.jar");
        file_dialog.show();
        String filename = file_dialog.getFile();
        InputStream is;
        String str = null;
        if (file_dialog.getFile() != null)
            directoryname = file_dialog.getDirectory();
        if (file_dialog.getFile() != null)
            projectdirectory.setText("Current Directory : " + directoryname);
        if (file_dialog.getFile() != null)
        {
            filenamelist.removeAll();
            try
            {
                Process ps = Runtime.getRuntime().exec("jar tf  " +
                                                       directoryname + filename);
                is = ps.getInputStream();
                BufferedReader ds = new BufferedReader(new InputStreamReader(
                                                           is));
                jarname.setText(directoryname + filename);
                str = ds.readLine();
                if (str.endsWith(".MF") == false)
                    filenamelist.add(str);
                while (str != null)
                {
                    str = ds.readLine();
                    if (str != null && str.endsWith(".MF") == false)
                        filenamelist.add(str);

                    // filenamelist.add(str);

                }
                ds.close();
            }
            catch (Exception err)
            {
                projectdirectory.setText("Error " + err);
            }
        }
    }
    void ExtractJarFiles()
    {
        FileDialog file_dialog = new FileDialog(
                                     this, "Select Jar File to Extract", FileDialog.LOAD);
        file_dialog.setDirectory(directoryname);
        file_dialog.setFile("*.jar");
        file_dialog.show();
        String filename = file_dialog.getFile();
        if (file_dialog.getFile() != null)
            directoryname = file_dialog.getDirectory();
        if (file_dialog.getFile() != null)
            projectdirectory.setText("Current Directory : " + directoryname);
        if (file_dialog.getFile() != null)
        {
            try
            {
                Process ps = Runtime.getRuntime().exec("jar xf  " +
                                                       directoryname + filename);
            }
            catch (Exception err)
            {
                projectdirectory.setText("Error " + err);
            }
        }
    }
    void SaveJarFiles()
    {
        FileDialog file_dialog = new FileDialog(
                                     this, "Save Jar File As", FileDialog.SAVE);
        file_dialog.setDirectory(directoryname);
        file_dialog.setFile("*.jar");
        file_dialog.show();
        String temp = "jar cf";
        if (manifest.isSelected())
            temp = temp + "M";
        if (compress.isSelected())
            temp = temp + "0";
        String filename = file_dialog.getFile();
        if (file_dialog.getFile() != null)
            directoryname = file_dialog.getDirectory();
        if (file_dialog.getFile() != null)
            projectdirectory.setText("Current Directory : " + directoryname);
        if (file_dialog.getFile() != null)
        {
            temp = temp + " " + directoryname + filename + " ";
            int i = 0;
            while (i <= filenamelist.getItemCount() - 1)
            {
                temp = temp + filenamelist.getItem(i) + " ";
                i++;
            }
            try
            {
                Process ps = Runtime.getRuntime().exec(temp);
            }
            catch (Exception err)
            {
                projectdirectory.setText("Error " + err);
            }
        }
    }
    void SetDirectory(String title, String type)
    {
        FileDialog file_dialog = new FileDialog(this, title, FileDialog.LOAD);
        file_dialog.setFile(type);
        file_dialog.setDirectory(directoryname);
        file_dialog.setVisible(true);
        if (file_dialog.getFile() != null)
            directoryname = file_dialog.getDirectory();
        if (file_dialog.getFile() != null)
            projectdirectory.setText("Current Directory : " + directoryname);
    }
}

