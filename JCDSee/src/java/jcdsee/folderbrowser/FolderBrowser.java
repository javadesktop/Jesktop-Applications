
package jcdsee.folderbrowser;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import jcdsee.resources.*;

/**
 * Implements a graphical folder browser
 */
public class FolderBrowser extends JPanel 
{
  protected JTree  m_tree;
  protected DefaultTreeModel m_model;

  // event list for listeners 
  protected EventListenerList listeners;
  
  /**
   * Constructor
   */
  public FolderBrowser() {
    setLayout(new BorderLayout());
    
    DefaultMutableTreeNode top 
      = new DefaultMutableTreeNode(new IconData(Resources.ICON_COMPUTER, 
						    null, 
						"Computer"));
    
    DefaultMutableTreeNode node;
    File[] roots = File.listRoots();
    for (int k=0; k<roots.length; k++) {
	  node = new DefaultMutableTreeNode(
					    new IconData(Resources.ICON_DISK, 
							 null, 
							 new FileNode(roots[k])));
	  top.add(node);
	  node.add( new DefaultMutableTreeNode(new Boolean(true)));
    }
    
    // add a home dir node
    File homeDir = new File(System.getProperty( "user.home" ));
    node = new DefaultMutableTreeNode(new IconData(Resources.ICON_DISK, 
						       null, 
						   new FileNode(homeDir)));
    top.add(node);
    node.add( new DefaultMutableTreeNode(new Boolean(true)));
    
    
    m_model = new DefaultTreeModel(top);
    
    m_tree = new JTree(m_model);
    m_tree.getSelectionModel().setSelectionMode(
						TreeSelectionModel.SINGLE_TREE_SELECTION);
    m_tree.putClientProperty("JTree.lineStyle", "Angled");
    TreeCellRenderer renderer = new IconCellRenderer();
    m_tree.setCellRenderer(renderer);
    
    m_tree.addTreeExpansionListener(new DirExpansionListener());
    m_tree.addTreeSelectionListener(new DirSelectionListener());
    
    m_tree.setShowsRootHandles(true); 
    m_tree.setEditable(false);
    
    JScrollPane s = new JScrollPane();
    s.getViewport().add(m_tree);
    add(s, BorderLayout.CENTER);
    
    listeners = new EventListenerList();
    
  }
  
  /** Adds a directory change listener
   * @param DirectoryEventListener the class that want to listen
   * @return void
   */
  public void addDirectoryChangeListener(DirectoryEventListener l) {
    listeners.add(DirectoryEventListener.class, l);
  }

  /** Removes a directory change listener
   * @param DirectoryEventListener the class that want to be removed
   * @return void
   */
  public void removeDirectoryChangeListener(DirectoryEventListener l) {
	listeners.remove(DirectoryEventListener.class, l);
  }
  
  // fire event to listeners
  protected void fireDirectoryChanged(DirectoryEvent e) {
    // Guaranteed to return a non-null array
    Object[] audience = listeners.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
	for (int i = audience.length-2; i>=0; i-=2) {
	  if (audience[i] == DirectoryEventListener.class) {
	    ((DirectoryEventListener)audience[i+1]).directoryChanged(e);
	  }              
	}
    }      
  
  protected DefaultMutableTreeNode getTreeNode(TreePath path) {
    return (DefaultMutableTreeNode) (path.getLastPathComponent());
  }
  
  protected FileNode getFileNode(DefaultMutableTreeNode node) {
    if (node == null)
      return null;
    Object obj = node.getUserObject();
    if (obj instanceof IconData)
      obj = ((IconData)obj).getObject();
    if (obj instanceof FileNode)
      return (FileNode)obj;
    else
      return null;
  }
  
  // Make sure expansion is threaded and updating the tree model
  // only occurs within the event dispatching thread.
  class DirExpansionListener implements TreeExpansionListener
  {
    // make sync. to avoid problems with impatient users...
    public synchronized void treeExpanded(TreeExpansionEvent event) {
      final DefaultMutableTreeNode node = getTreeNode(event.getPath());
      final FileNode fnode = getFileNode(node);
      
      Thread runner = new Thread() {
		    public void run() {
		      if (fnode != null && fnode.expand(node)) {
			Runnable runnable = new Runnable() {
			  public void run() {
			    m_model.reload(node);
			  }
			};
			SwingUtilities.invokeLater(runnable);
			}
		    }
      };
      runner.start();
    }
    
    public void treeCollapsed(TreeExpansionEvent event) {}
  }
  
  class DirSelectionListener implements TreeSelectionListener
  {
    // make sync. to avoid problems with impatient users...
    public synchronized void valueChanged(TreeSelectionEvent event) {
      DefaultMutableTreeNode node = getTreeNode(event.getPath());
      FileNode fnode = getFileNode(node);
      if (fnode != null)
	fireDirectoryChanged(new DirectoryEvent(this, fnode.getFile()));
    }
  }
  
  
  
}

class IconCellRenderer extends JLabel implements TreeCellRenderer
{
  protected Color m_textSelectionColor;
  protected Color m_textNonSelectionColor;
  protected Color m_bkSelectionColor;
  protected Color m_bkNonSelectionColor;
  protected Color m_borderSelectionColor;
  
  protected boolean m_selected;
  
  public IconCellRenderer() {
    super();

    m_textSelectionColor = UIManager.getColor(
					      "Tree.selectionForeground");
    m_textNonSelectionColor = UIManager.getColor(
						 "Tree.textForeground");
    m_bkSelectionColor = UIManager.getColor(
					    "Tree.selectionBackground");
    m_bkNonSelectionColor = UIManager.getColor(
					       "Tree.textBackground");
    m_borderSelectionColor = UIManager.getColor(
						"Tree.selectionBorderColor");
    setOpaque(false);
  }

  // part of the cell renderer interface
  public Component getTreeCellRendererComponent(JTree tree, 
						Object value, 
						boolean sel, 
						boolean expanded, 
						boolean leaf, 
						int row, 
						boolean hasFocus)        
  {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    Object obj = node.getUserObject();
    setText(obj.toString());
	
    if (obj instanceof Boolean)
      setText("Retrieving data...");

    if (obj instanceof IconData) {
      IconData idata = (IconData)obj;
      if (expanded)
	setIcon(idata.getExpandedIcon());
      else
	setIcon(idata.getIcon());
    }
    else
      setIcon(null);

    setFont(tree.getFont());
    setForeground(sel ? m_textSelectionColor : 
		  m_textNonSelectionColor);
    setBackground(sel ? m_bkSelectionColor : 
		  m_bkNonSelectionColor);
    m_selected = sel;
    return this;
  }
  
  // part of the cell renderer interface
  public void paintComponent(Graphics g) {
    Color bColor = getBackground();
    Icon icon = getIcon();

    g.setColor(bColor);
    int offset = 0;
    if(icon != null && getText() != null) 
      offset = (icon.getIconWidth() + getIconTextGap());
    g.fillRect(offset, 0, getWidth() - 1 - offset,
	       getHeight() - 1);
      
    if (m_selected) {
      g.setColor(m_borderSelectionColor);
      g.drawRect(offset, 0, getWidth()-1-offset, getHeight()-1);
    }
    super.paintComponent(g);
  }
}

// stores icon info
class IconData
{
  protected Icon   m_icon;
  protected Icon   m_expandedIcon;
  protected Object m_data;

  public IconData(Icon icon, Object data) {
    m_icon = icon;
    m_expandedIcon = null;
    m_data = data;
  }
  
  public IconData(Icon icon, Icon expandedIcon, Object data) {
    m_icon = icon;
    m_expandedIcon = expandedIcon;
    m_data = data;
  }

  public Icon getIcon() { return m_icon; }
  
  public Icon getExpandedIcon() { 
    return m_expandedIcon!=null ? m_expandedIcon : m_icon;
  }
  
  public Object getObject() { return m_data; }
  
  public String toString() { return m_data.toString(); }
}

// a class for storing file info in the tree
class FileNode
{
  protected File m_file;
  
  public FileNode(File file) { m_file = file; }
  
  public File getFile() { return m_file; }

  public String toString() {
    return m_file.getName().length() > 0 ? m_file.getName() :
      m_file.getPath();
  }
  
  public boolean expand(DefaultMutableTreeNode parent) {
    DefaultMutableTreeNode flag =
      (DefaultMutableTreeNode)parent.getFirstChild();
    if (flag==null)      // No flag
      return false;
    Object obj = flag.getUserObject();
    
    // added by martin, always refresh dir list
    //if (!(obj instanceof Boolean))
    //  return false;      // Already expanded

    parent.removeAllChildren();  // Remove Flag

    File[] files = listFiles();
    if (files == null)
      return true;

    Vector v = new Vector();

    for (int k=0; k<files.length; k++) {
      File f = files[k];
      if (!(f.isDirectory()))
	continue;

      FileNode newNode = new FileNode(f);
      
      boolean isAdded = false;
      for (int i=0; i<v.size(); i++) {
	FileNode nd = (FileNode)v.elementAt(i);
	if (newNode.compareTo(nd) < 0) {
	  v.insertElementAt(newNode, i);
	  isAdded = true;
	  break;
	}
      }
      if (!isAdded)
	v.addElement(newNode);
    }

    for (int i=0; i<v.size(); i++) {
      FileNode nd = (FileNode)v.elementAt(i);
      IconData idata 
	= new IconData(Resources.ICON_FOLDER, 
		       Resources.ICON_EXPANDEDFOLDER, 
		       nd);
	    
      DefaultMutableTreeNode node = 
	new DefaultMutableTreeNode(idata);
      parent.add(node);
              
      if (nd.hasSubDirs())
	node.add(new DefaultMutableTreeNode( 
					    new Boolean(true) ));
    }
    return true;
  }

  public boolean hasSubDirs() {
    // do not recurse to check
    // if under directories have directories
    
    // 	File[] files = listFiles();
    // 	if (files == null)
    // 	    return false;
    // 	for (int k=0; k<files.length; k++) {
    // 	    if (files[k].isDirectory())
    // 		return true;
    // 	}
    return true;
  }
    
  public int compareTo(FileNode toCompare) { 
    return  m_file.getName().compareToIgnoreCase(
						 toCompare.m_file.getName() ); 
  }

  protected File[] listFiles() {
    if (!m_file.isDirectory())
      return null;
    try {
      return m_file.listFiles();
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null, 
				    "Error reading directory "+m_file.getAbsolutePath(),
				    "Warning", JOptionPane.WARNING_MESSAGE);
      return null;
    }
  }
}
