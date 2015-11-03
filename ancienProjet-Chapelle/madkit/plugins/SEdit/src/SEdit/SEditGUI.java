package SEdit;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;
    
//import SEdit.*;
//import SEdit.Formalisms.*;
//import SEdit.Formalisms.StateTransition.*;
//import SEdit.Formalisms.Bric.*;
//import SEdit.Formalisms.Petri.*;
//import SEdit.Formalisms.Object.*;
//import SEdit.Formalisms.Automaton.*;
//import SEdit.Formalisms.Aalaadin.*;
//import SEdit.Formalisms.DataFlow.*;


public class SEditGUI extends JRootPane implements ActionListener 
{
   Hashtable formalisms = new Hashtable();
    
    SEditAgent myAgent;
    
  private static boolean installFileName=false;
  public static void setInstallFileName(boolean f) {installFileName = f;}
  public static boolean getInstallFileName() {return installFileName;}
  
      public static void showClipBoard() {
	  //new CommentEditor(null,"Contenu du presse-papier",getClipBoard());
  }
   
  
  JTree scrollingTree;
  DefaultMutableTreeNode loadedRoot;  
  DefaultMutableTreeNode preloadedRoot;  
  DefaultMutableTreeNode realRoot;
  JMenuItem metalMenuItem;
  

    
    void quit(){
	int        result;
	result = JOptionPane.showConfirmDialog(this, 
					       "Do you really want to quit SEdit?", 
					       "Quit",
					       JOptionPane.YES_NO_OPTION,
					       JOptionPane.QUESTION_MESSAGE,
					       null);
	if(result == JOptionPane.YES_OPTION)
	    System.exit(0);
	
  }
  	
 // fait doublon avec celui de StructureFrame
 void addTool(JToolBar toolBar, String name, String icon) {
     // Ol FIXME: this one is bogus too. We shouldn't use a
     // "images/xxx.gif" template (formalisms defined as jar, loaded
     // from the web, with jpg or png instead of gif, etc...
     java.net.URL u = ClassLoader.getSystemResource(icon);
     JButton b = null;
     u = this.getClass().getResource(icon);
     if (u != null){
     	b = (JButton) toolBar.add(new JButton(new ImageIcon(u)));
     } else
     	b=(JButton) toolBar.add(new JButton(name));
     b.setToolTipText(name);
     b.setActionCommand(name);
     b.addActionListener(this);
 }


  // utilise par StructureFrame
  public static void addMenuItem(ActionListener al, JMenu m, String label, String command, int key) {
  		addMenuItem(al, m, label, command, key, -1);
   }
   
  public  static  void addMenuItem(ActionListener al, JMenu m, String label, String command, int key, int ckey) {
        JMenuItem menuItem;
        if (key > 0)
	  		menuItem = new JMenuItem(label,key);//Ol. Was MenuShortCut
        else
        	 menuItem = new JMenuItem(label);
        m.add(menuItem);
		// System.err.println("added"+label);
	
        menuItem.setActionCommand(command);
        menuItem.addActionListener(al);
        if (ckey > 0) {
        	if (ckey != KeyEvent.VK_DELETE)
        		menuItem.setAccelerator(KeyStroke.getKeyStroke(ckey, Event.CTRL_MASK, false));
        	else
        		menuItem.setAccelerator(KeyStroke.getKeyStroke(ckey, 0, false));
        }
   }

 public static void addRadioButtonMenuItem(ActionListener al, JMenu m, String label, String command, boolean b, ButtonGroup g)
  {
       JRadioButtonMenuItem menuItem;
       menuItem = new JRadioButtonMenuItem(label);
        m.add(menuItem);
        menuItem.setActionCommand(command);
        menuItem.addActionListener(al);
		menuItem.setSelected(b);
		g.add(menuItem);
  }
  

  public void actionPerformed(ActionEvent e) {
        				command(e.getActionCommand());
  }
  
  SEditGUI(SEditAgent a)
    {
	myAgent =a;
	
	/** initalise le look and feel en prenant par defaut celui de la plate-forme */
	/*	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception exc2) {
	    System.err.println("Look and feel inconnu: " + exc2);
	    }*/
  
  
	//  setSize(200, 240); // Ol: added a pack() instead 
	getContentPane().setLayout(new BorderLayout(10,10));

	JPanel up = new JPanel();
	up.setLayout(new BorderLayout());
	JMenuBar menubar=new JMenuBar();


	JToolBar toolBar = new JToolBar();

	addTool(toolBar, "open", "/toolbarButtonGraphics/general/Open24.gif");
	//	addTool(toolBar, "scheme");
	addTool(toolBar, "quit", "/toolbarButtonGraphics/general/Stop24.gif");
	
	toolBar.addSeparator();
	
	up.add("South",toolBar);


	getContentPane().add("North",up);
	  	
	JMenu menuFile=new JMenu("File");
	menuFile.setMnemonic('f');
	menubar.add(menuFile);
	addMenuItem(this, menuFile, "Open...", "open", KeyEvent.VK_O, KeyEvent.VK_O);
	//	addMenuItem(this, menuFile, "Editeur Scheme", "scheme", -1);
	//	addMenuItem(this, menuFile, "Importer modeles", "oldOpen", -1,KeyEvent.VK_M);
	//	addMenuItem(this, menuFile, "Afficher presse papier", "showClipBoard", -1);
	addMenuItem(this, menuFile, "Quit", "quit", KeyEvent.VK_Q, KeyEvent.VK_Q);
	
	getRootPane().setJMenuBar(menubar);
  
	JMenu menuFormalisms=new JMenu("Formalisms");
	menubar.add(menuFormalisms);
	addMenuItem(this, menuFormalisms, "Add a formalism...", "loadFormalism", -1);
	addMenuItem(this, menuFormalisms, "Add a formalism from Web...", "urlFormalism", -1);
	
	// Options Menu
	/*	JMenu options = (JMenu) menubar.add(new JMenu("Options"));
		options.setMnemonic('p');
		ButtonGroup group = new ButtonGroup();
		addRadioButtonMenuItem(this, options, "Look & Feel Multi-plateforme","crosslook",false,group);
		addRadioButtonMenuItem(this, options, "Look & Feel Windows","winlook",false,group);
		addRadioButtonMenuItem(this, options, "Look & Feel Motif","motiflook",false,group);
	*/
	
	JMenu menuHelp=new JMenu("Help");
	menubar.add(menuHelp);
	addMenuItem(this, menuHelp, "About", "about", -1);
	  addMenuItem(this, menuHelp, "Help", "help", -1);
	//  addMenuItem(this, menuHelp, "look and feel", "look and feel", -1);
	
	
	/** l'initialisation de l'editeur Scheme associe, pour l'instant Kawa */
	
	
	realRoot =   new DefaultMutableTreeNode("Formalisms");
	preloadedRoot =   new DefaultMutableTreeNode("Preloaded");
	realRoot.add(preloadedRoot);
		
	scrollingTree = new JTree(realRoot);
	//scrollingTree.setSize(200,300);
	
	scrollingTree.setRootVisible(false);
	JScrollPane treepane = new JScrollPane();
	treepane.getViewport().add(scrollingTree);
	getContentPane().add("Center",treepane);
    
	MouseListener ml = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    int selRow = scrollingTree.getRowForLocation(e.getX(), e.getY());
		    TreePath selPath = scrollingTree.getPathForLocation(e.getX(), e.getY());
		    if (selRow != -1) 
			if(e.getClickCount() == 2) 
			    if (((TreeNode)selPath.getLastPathComponent()).isLeaf())
				{
				    Object o = formalisms.get(selPath.getLastPathComponent());
				    if (o!=null)
					myAgent.startEditor((String)o);
				    
				    //(((TreeNode)selPath.getLastPathComponent()).toString());
				}
		}
		
	    };
	scrollingTree.addMouseListener(ml);
	
	
	//show();
      
}
  	
    public void addPreloaded(String name, String desc)
    {
	
      DefaultMutableTreeNode level1 = 
	  new DefaultMutableTreeNode(desc);
      formalisms.put(level1, name);
      preloadedRoot.add(level1);
      scrollingTree.repaint();
      
    }
    


  public void updateFormalismList(String name, String desc)
  {
    DefaultTreeModel model = (DefaultTreeModel)scrollingTree.getModel();
    
    if (loadedRoot==null)
      {
	loadedRoot =   new DefaultMutableTreeNode("Added");
	model.insertNodeInto(loadedRoot, realRoot,
			    model.getChildCount(realRoot));
      }
    DefaultMutableTreeNode level1 = new DefaultMutableTreeNode(desc);
    formalisms.put(level1, name);
    model.insertNodeInto(level1, loadedRoot,
			      model.getChildCount(loadedRoot)
			      );
    //    scrollingTree.hide();
    // scrollingTree.invalidate();
    //  scrollingTree.show();
    scrollingTree.repaint();
    
  }

   void command(String a)
   { if (a.equals("quit"))
   	quit();
     else if (a.equals("open"))
	 {	
	     
	     JFileChooser fd = new JFileChooser(System.getProperty("madkit.dir")+"/plugins/SEdit/scripts");
	     int retval = fd.showOpenDialog(null);
	     if (retval != -1)	{
		 if (fd.getSelectedFile()!=null)
		     {
			 String fileDir = fd.getSelectedFile().getParent()+File.separator;
			 String fileName = fd.getSelectedFile().getName();
			 myAgent.openFile(fileDir+File.separator+fileName);
		     }
	     }
	     
	 }
   //   else if (a.equals("scheme"))
   // 	myAgent.startSchemeEditor();
   	 else if (a.equals("showClipBoard"))
   	 	showClipBoard();
     else if (a.equals("help"))
       {
	 HelpBox hb = new HelpBox("doc/index.html");
	 
       }
   else if (a.equals("about"))
       { 
	   new AboutBox();
       } 
    else if (a.equals("loadFormalism"))
{
	    JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
	    int retval = fd.showOpenDialog(null);
	    if (retval != -1)	{
		if (fd.getSelectedFile()!=null)
		     {		
			 String fileDir = fd.getSelectedFile().getParent()+File.separator;
			 String fileName = fd.getSelectedFile().getName();
			 myAgent.loadFormalism(fileDir+fileName);
		     }
	    }
	    

}    else if (a.equals("urlFormalism"))
{
    String url;
    url = JOptionPane.showInputDialog(this, 
				      "URL to a SEdit formalism",
				      "URL Formalism",
				      JOptionPane.PLAIN_MESSAGE);
    myAgent.urlFormalism(url);
}    else if (a.equals("look and feel"))
    	System.out.println("look and feel: " + UIManager.getLookAndFeel().getName());
     else
     	System.out.println("command: " + a);
   }


/* Switch the between the Windows, Motif, Mac, and the Metal Look and Feel
     */
    class ToggleUIListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
		}
    }
}








