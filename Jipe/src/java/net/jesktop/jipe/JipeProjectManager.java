/**
 * ProjectDialog.java - Sets project properties within the Jipe Environment.  Copyright
 * (c) 1996-1999 Steve Lawson.  E-Mail steve@e-i-s.co.uk Latest version can be
 * found at http://e-i-s.co.uk/jipe
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
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class JipeProjectManager extends JDialog
{
    static String PROJECTNAME, PROJECTMAIN,SOURCEDIR,CLASSDIR,BUILDSCRIPT;
    boolean OPENPROJECT,SAVEPROJECT;
    static String name;
    static JTextField projectname;
    static JTextField projectmain;
    static JTextField classpath;
    static JTextField classdirectory;
    static JTextField buildscript;
    protected static Vector projectchildren = null;
    DefaultMutableTreeNode projecttreenode;
    TreeSelectionModel selectionModel;
    DefaultTreeModel model;
    static DefaultMutableTreeNode root;
    JTree projecttree;
    JFileChooser fileChooser;

    Container container;
    Jipe jipe;
    JipeProjectManager(Jipe child)
    {
        jipe = child;
        container = this.getContentPane();
        projectchildren = new Vector(4, 4);
        root = new DefaultMutableTreeNode("Root");
        projecttree = new JTree(root);
        model = (DefaultTreeModel)projecttree.getModel();
        selectionModel = projecttree.getSelectionModel();
        projecttree.setRootVisible(false);
        projecttree.setShowsRootHandles(false);
        JScrollPane projectScrollPane=new JScrollPane(projecttree);
        jipe.tabbedPane.addTab("Project", projectScrollPane);

        selectionModel.setSelectionMode(
            TreeSelectionModel.SINGLE_TREE_SELECTION);

        model.addTreeModelListener(new TreeModelListener() {

                                       public void treeNodesInserted(TreeModelEvent e)
                                       {
                                           showInsertionOrRemoval(e, " added to ");
                                       }
                                       public void treeNodesRemoved(TreeModelEvent e)
                                       {
                                           showInsertionOrRemoval(e, " removed from ");
                                       }
                                       private void showInsertionOrRemoval(TreeModelEvent e, String s)
                                       {
                                           Object[] parentPath = e.getPath();
                                           int[] indexes = e.getChildIndices();
                                           Object[] children = e.getChildren();
                                           Object parent = parentPath[parentPath.length - 1];
                                           jipe.statusBar.msgline.setText("File: " + s);
                                       }
                                       public void treeNodesChanged(TreeModelEvent e)
                                       {
                                       }
                                       public void treeStructureChanged(TreeModelEvent e)
                                       {
                                       }
                                   });
        projecttree.addMouseListener(new MouseAdapter(){
                                         public void mousePressed(MouseEvent e)
                                         {
                                             JTree t = (JTree)e.getSource();
                                             int row = t.getRowForLocation(e.getX(), e.getY());

                                             TreePath path = t.getPathForLocation(e.getX(), e.getY());
                                             TreeNode treenode = null;
                                             File file = null;
                                             String nodeName = null;
                                             if (e.getClickCount() == 2)
                                             {
                                                 if (row != -1 && t == projecttree)
                                                 {
                                                     treenode = (TreeNode)path.getLastPathComponent();
                                                     nodeName = treenode.toString();
                                                     if (treenode.isLeaf())
                                                     {
                                                         file=(File)projectchildren.elementAt(row-1);
                                                         if (jipe.GetWindow(file) == false)
                                                             jipe.loadChild(nodeName, file);
                                                     }
                                                 }
                                             }
                                         }
                                     });
    }

    protected void addToProject()
    {
        if (projecttreenode != null)
        {
            int index = projecttreenode.getChildCount();
            File[] files=getAddChildFileName();
            if(files!=null){

                for(int i=0;i<files.length;i++){
                    projectchildren.addElement((Object)files[i]);
                    MutableTreeNode newNode = new DefaultMutableTreeNode(files[i].getName());
                    if(newNode!=null){
                        model.insertNodeInto(newNode, projecttreenode, index);
                        SAVEPROJECT=true;
                    }
                }
            }
        }
    }
    protected void removeFromProject()
    {
        if (projecttreenode != null)
        {
            TreePath path = selectionModel.getSelectionPath();
            if (path.getPathCount() == 1)
            {
                jipe.statusBar.msgline.setText("Can't remove root node!");
                return;
            }
            if (path.getPathCount() == 2)
            {
                // removeProject();
                jipe.statusBar.msgline.setText("Select ' Close Project ' option from menubar");
                return;
            }
            if (path != null)
            {
                MutableTreeNode node =
                    (MutableTreeNode)path.getLastPathComponent();
                if (node.isLeaf()){
                    projectchildren.removeElementAt(selectionModel.getLeadSelectionRow()-1);
                    model.removeNodeFromParent(node);
                    SAVEPROJECT=true;
                }
            }
        }
    }
    protected void ProjectProperties()
    {

        Font font = new Font("Dialog", Font.PLAIN, 11);
        setFont(font);
        setBackground(SystemColor.control);
        JPanel panel = new JPanel();
        GridLayout grid = new GridLayout(0, 2, 4, 0);
        panel.setBorder(
            BorderFactory.createTitledBorder(""));
        panel.setLayout(grid);
        panel.add(new JLabel("Project Name : ", JLabel.CENTER));
        panel.add(projectname = new JTextField("", 15));
        panel.add(new JLabel("Main Class/Html File : ", JLabel.CENTER));
        panel.add(projectmain = new JTextField("", 15));
        panel.add(new JLabel("ClassPath : ", JLabel.CENTER));
        panel.add(classpath = new JTextField("", 15));
        panel.add(new JLabel("Class Directory : ", JLabel.CENTER));
        panel.add(classdirectory = new JTextField("", 15));
        panel.add(new JLabel("Build Script : ", JLabel.CENTER));
        panel.add(buildscript = new JTextField("", 15));
        getContentPane().add("North", panel);

        if(PROJECTNAME!=null){
            projectname.setText(PROJECTNAME);
            projectmain.setText(PROJECTMAIN);
            classpath.setText(SOURCEDIR);
            classdirectory.setText(CLASSDIR);
            buildscript.setText(BUILDSCRIPT);
        }

        final JOptionPane pane= new JOptionPane(
                                    panel, // message
                                    JOptionPane.PLAIN_MESSAGE, // messageType
                                    JOptionPane.OK_CANCEL_OPTION); // optionType


        JDialog dialog = pane.createDialog(
                             this, // parentComponent
                             "Project Properties"); // title

        dialog.setResizable(false);
        dialog.pack();
        dialog.show();

        Integer value=(Integer)pane.getValue();
        OPENPROJECT=true;
        if(value.intValue()==JOptionPane.OK_OPTION)UpdateReferences();
        if(value.intValue()==JOptionPane.CANCEL_OPTION)OPENPROJECT=false;


    }

    protected void OpenNewProject(File projectFile)
    {
        if (projecttreenode != null)CloseProject();
        if (projecttreenode == null){
            if(projectFile==null)return;
            try
            {
                jipe.statusBar.msgline.setText("Loading Project.");
                FileReader fis = new FileReader(projectFile.getPath());
                BufferedReader br = new BufferedReader(fis);

                PROJECTNAME= br.readLine();
                projecttreenode = new DefaultMutableTreeNode(PROJECTNAME);
                PROJECTMAIN = br.readLine();
                SOURCEDIR = br.readLine();
                CLASSDIR = br.readLine();
                BUILDSCRIPT = br.readLine();
                String str = br.readLine();
                while (str != null)
                {
                    File file=new File(str);
                    projectchildren.addElement((Object)file);
                    projecttreenode.add(new DefaultMutableTreeNode(file.getName()));
                    str = br.readLine();
                }
                root.add(projecttreenode);
                model.reload(root);
                jipe.statusBar.msgline.setText("Project Loaded.");
                fis.close();
            }
            catch (Exception err)
            {
                jipe.statusBar.msgline.setText("Error : " + err);
            }
        }
    }

    protected void OpenNewProject()
    {
        if (projecttreenode != null)CloseProject();
        if (projecttreenode == null){
            File projectFile = getFileName("Open Project",3);
            if(projectFile==null)return;
            try
            {
                jipe.statusBar.msgline.setText("Loading Project.");

                            if(jipe.props.RPROJECTS.indexOf(projectFile.getAbsolutePath())<0){
        jipe.props.RPROJECTS=projectFile.getAbsolutePath()+","+jipe.props.RPROJECTS;
                jipe.createRecentProjectsList();
            }
                FileReader fis = new FileReader(projectFile.getPath());
                BufferedReader br = new BufferedReader(fis);

                PROJECTNAME= br.readLine();
                projecttreenode = new DefaultMutableTreeNode(PROJECTNAME);
                PROJECTMAIN = br.readLine();
                SOURCEDIR = br.readLine();
                CLASSDIR = br.readLine();
                BUILDSCRIPT = br.readLine();
                String str = br.readLine();
                while (str != null)
                {
                    File file=new File(str);
                    projectchildren.addElement((Object)file);
                    projecttreenode.add(new DefaultMutableTreeNode(file.getName()));
                    str = br.readLine();
                }
                root.add(projecttreenode);
                model.reload(root);
                jipe.statusBar.msgline.setText("Project Loaded.");
                fis.close();
            }
            catch (Exception err)
            {
                jipe.statusBar.msgline.setText("Error : " + err);
            }
        }
    }
    protected void CloseProject()
    {
        if(SAVEPROJECT)SaveConfirm("Save Project", "Project not Saved, Save Now?");
        else {
            this.dispose();
            jipe.tabbedPane.removeTabAt(0);
            jipe.project=null;
        }
    }
    protected void NewProject()
    {
        if (projecttreenode != null)CloseProject();
        if (projecttreenode == null){
            ProjectProperties();
            if(OPENPROJECT){
                projecttreenode = new DefaultMutableTreeNode(PROJECTNAME);
                root.add(projecttreenode);
                model.reload(root);
                MutableTreeNode newNode = new DefaultMutableTreeNode(PROJECTNAME+".project");
                model.insertNodeInto(newNode, projecttreenode, 0);
                jipe.addChild(jipe,PROJECTNAME+".project",null);
            }
        }
    }
    protected void SaveProject()
    {
        Vector x=new Vector();
        int i=0;
        while (i < projectchildren.size())
        {
            File tempfile=(File)projectchildren.elementAt(i);
            x.addElement(tempfile.getPath());
            i++;
        }

        File file = getFileName("Save Project",1);
        TreePath selPath;
        TreeNode node;

        if (file==null)return;
        else
        {
            String filename=file.getPath();
            if(!filename.endsWith(".prj"))filename=filename+".prj";
            try
            {
                FileWriter fs = new FileWriter(filename);
                fs.write(PROJECTNAME);
                fs.write(System.getProperty("line.separator"));
                fs.write(PROJECTMAIN);
                fs.write(System.getProperty("line.separator"));
                fs.write(SOURCEDIR);
                fs.write(System.getProperty("line.separator"));
                fs.write(CLASSDIR);
                fs.write(System.getProperty("line.separator"));
                fs.write(BUILDSCRIPT);
                fs.write(System.getProperty("line.separator"));
                i = 0;
                Enumeration e=x.elements();
                while(e.hasMoreElements())
                    fs.write(e.nextElement() + System.getProperty("line.separator"));
                fs.close();
                SAVEPROJECT=false;
            }
            catch (Exception err)
            {
            }
        }
    }
    public void SaveConfirm(String confirmTitle, String confirmText)
    {
        int value = JOptionPane.showConfirmDialog(
                        getContentPane(), confirmText, confirmTitle, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (value)
        {
        case JOptionPane.YES_OPTION:
            SaveProject();
        case JOptionPane.NO_OPTION:
            this.dispose();
            jipe.tabbedPane.removeTabAt(0);
            jipe.project=null;
        case JOptionPane.CANCEL_OPTION:
        }
    }

    protected File getFileName(String title,int type){
        fileChooser = new JFileChooser(jipe.CURRENTDIR);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle(title);
        fileChooser.addChoosableFileFilter(new ProjectFilter());
        int selected=0;
        if(type==1)selected = fileChooser.showSaveDialog(container);
        else selected = fileChooser.showOpenDialog(container);
        if (selected == JFileChooser.APPROVE_OPTION){
            jipe.CURRENTDIR=fileChooser.getCurrentDirectory();
            return fileChooser.getSelectedFile();
        }
        else if (selected == JFileChooser. CANCEL_OPTION)
            return null;
        return null;
    }

    protected File[] getAddChildFileName()
    {
        fileChooser = new JFileChooser(jipe.CURRENTDIR);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setDialogTitle("Add Files to Project");
        fileChooser.addChoosableFileFilter(new TextFilter());
        fileChooser.addChoosableFileFilter(new JspFilter());
        fileChooser.addChoosableFileFilter(new HtmlFilter());
        fileChooser.addChoosableFileFilter(new JavaFilter());
        fileChooser.addChoosableFileFilter(new ProjectFilter());
        fileChooser.setFileFilter(new JavaFilter());
        int selected = fileChooser.showOpenDialog(container);

        if (selected == JFileChooser.APPROVE_OPTION){
            jipe.CURRENTDIR=fileChooser.getCurrentDirectory();
            return fileChooser.getSelectedFiles();
        }
        else if (selected == JFileChooser. CANCEL_OPTION)
            return null;
        return null;
    }

    public void UpdateReferences(){
        PROJECTNAME=projectname.getText().trim();
        PROJECTMAIN=projectmain.getText().trim();
        SOURCEDIR=classpath.getText().trim();
        CLASSDIR=classdirectory.getText().trim();
        BUILDSCRIPT=buildscript.getText().trim();
        SAVEPROJECT=true;
    }
}

