 package madkit.explorer;

import madkit.TreeTools.*;
import java.awt.*;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;


import java.io.*;
import java.net.URL;



/**
 * <p>Explorer Agent Interface </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>University of Montpellier II</p>
 * @authors A.Khammal , C.Marty , X.Lorca
 * @version 1.0
 **/

public class ExplorerGUI extends JRootPane {

	public static int ICONSIZE=64;
	
	ExplorerLauncher ag;

	ExplorerActionDrag explorerActionDrag;

    String initialPath = System.getProperty("madkit.dir"); //"/";

	private Box pToolbar;
	
//	Undo Helpers
	protected UndoManager undoManager = new UndoManager();
//	protected ExplorerActionRedo  explorerActionRedo;
//	protected ExplorerActionUndo  explorerActionUndo;

	    
    JPanel contentPane;
    JPanel status;
    JButton backward;    
    JTextField textField;
    JMenuBar jMenuBar;
	JMenu jMenuFile;
//	Brd le 7/04/04
	JMenu jMenuEdit;
	JMenu jMenuDisplay;
//
  	JButton bParent;
  	JButton bForward;
	JButton bHome;
//	
	JButton bUndo;
	JButton bRedo;
	JButton bCut;
	JButton bCopy;
	JButton bPast;
	String actionToolBar;
	
    JMenu jMenuHelp;
    JMenu jMenuOptions;

//	Brd le 7/04/04
//  Menu File
	JMenuItem jMenuFileNew;
	JMenuItem jMenuFileLink;
	JMenuItem jMenuFileDeleteFolder;	
	JMenuItem jMenuFileFind;
	JMenuItem jMenuFileCreateFolder;
//

	JMenuItem jMenuFileExit;
//	Menu Edit	
	JMenuItem jMenuEditUndo;
	JMenuItem jMenuEditRedo;
	JMenuItem jMenuEditCut;
	JMenuItem jMenuEditCopy;
	JMenuItem jMenuEditPast;
	JMenuItem jMenuEditSelectAll;
	JMenuItem jMenuEditReverseSelect;
  
//	Menu Display	
	JMenuItem jMenuDisplayDetails;
//	JMenuItem jMenuDisplayList;
//	JMenuItem jMenuDisplayDetails;
//	JMenuItem jMenuDisplayReOrganizedBy;

//	Menu Help	
	JMenuItem jMenuHelpHelp;
	JMenuItem jMenuHelpAbout;

//	Menu Option
    JMenuItem jMenuHiddenFiles;
    JSplitPane splitPane;
    //ExplorerTree tree;
    MadkitExplorerTree tree;
    MadkitExplorer explorer;	

    
    public ExplorerGUI(ExplorerLauncher _ag){
	ag = _ag;

	jMenuBar = new JMenuBar();
//	Menu File
	jMenuFile = new JMenu();
//	Brd le 7/04/04
	jMenuFileNew = new JMenuItem();
	jMenuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
	jMenuFileNew.setName("new");

	jMenuFileLink = new JMenuItem();
	jMenuFileLink.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
	jMenuFileLink.setName("link");

	jMenuFileDeleteFolder = new JMenuItem();	
	jMenuFileDeleteFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
	jMenuFileDeleteFolder.setName("deletefolder");

	jMenuFileFind = new JMenuItem();
	jMenuFileFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
	jMenuFileFind.setName("find");

	jMenuFileCreateFolder = new JMenuItem();
	jMenuFileCreateFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, Event.CTRL_MASK));
	jMenuFileCreateFolder.setName("createfolder");
//
    jMenuFileExit = new JMenuItem();
    
    

//	Brd le 7/04/04
//	Menu Edit
    jMenuEdit = new JMenu();

	jMenuEditUndo = new JMenuItem();	
	jMenuEditUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK));
    
	jMenuEditRedo = new JMenuItem();
	jMenuEditRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK));
	jMenuEditCut = new JMenuItem();
	jMenuEditCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
	jMenuEditCopy = new JMenuItem();
	jMenuEditCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
	jMenuEditPast = new JMenuItem();
	jMenuEditPast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
	jMenuEditSelectAll = new JMenuItem();
	jMenuEditSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
	jMenuEditReverseSelect = new JMenuItem();

	
//	Menu Display
    jMenuDisplay = new JMenu();
	jMenuDisplayDetails = new JMenuItem();
//	jMenuDisplayList = new JMenuItem();
//	jMenuDisplayDetails = new JMenuItem();
//	jMenuDisplayReOrganizedBy = new JMenuItem();
//

	jMenuHelp = new JMenu();
	jMenuHelpHelp = new JMenuItem();
	jMenuHelpAbout = new JMenuItem();
	
	jMenuOptions = new JMenu();
	jMenuHiddenFiles = new JMenuItem();
	status = new JPanel(new BorderLayout());
	try {
	    jbInit();
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }


    private void jbInit() throws Exception {

//ici
		textField = new JTextField(50);
		textField.setMargin(new Insets(2,2,2,2));
		textField.addActionListener(new ExplorerGUI_textField_ActionAdapter(this));
    	String path = initialPath;
    	// tree = new ExplorerTree(new File(path));
		tree = new MadkitExplorerTree(new File(path));

    	tree.setBackground(Color.white);
		tree.getTree().addTreeSelectionListener(new ExplorerGUI_tree_treeSelectionListenerAdapter(this));
		explorer = new MadkitExplorer(ag, tree,(AbstractFileNode)tree.getLocalRoot(),path,ICONSIZE,textField);
		explorer.setNode((AbstractFileNode)(tree.getLocalRoot()));
//ici
//
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,tree, explorer);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(175);
	
   
		backward = new JButton(new ImageIcon(loadImage("/images/backward.png")));
		backward.setMargin(new Insets(2,2,2,2));
		backward.setName("Backward");
		backward.setToolTipText("Backward");
		backward.addActionListener(new ExplorerActions(this));
//	status.add(backward, BorderLayout.WEST);
		bParent = new JButton(new ImageIcon(loadImage("/images/parent.png")));
		bParent.setMargin(new Insets(0,0,0,0));
		bParent.setName("Parent");
		bParent.setToolTipText("Parent Directory");
		bParent.addActionListener(new ExplorerActions(this));

		bForward = new JButton(new ImageIcon(loadImage("/images/forward.png")));
		bForward.setMargin(new Insets(2,2,2,2));
		bForward.setName("forward");
		bForward.setToolTipText("forward");
		bForward.addActionListener(new ExplorerActions(this));

//
  		bHome = new JButton(new ImageIcon(loadImage("/images/home.png")));
  		bHome.setMargin(new Insets(0,0,0,0));
  		bHome.setName("Home");
  		bHome.setToolTipText("Home Directory");
  		bHome.addActionListener(new ExplorerActions(this));

//
		bUndo = new JButton(new ImageIcon(loadImage("/images/undo.png")));
		bUndo.setMargin(new Insets(0,0,0,0));
		bUndo.setToolTipText("Undo");
		bUndo.setName("Undo");
		bUndo.setEnabled(false);
		bUndo.addActionListener(new ExplorerActions(this));

   
		bRedo = new JButton(new ImageIcon(loadImage("/images/redo.png")));
 		bRedo.setMargin(new Insets(0,0,0,0));
		bRedo.setToolTipText("Redo");
		bRedo.setName("Redo");
		bRedo.setEnabled(false);
		bRedo.addActionListener(new ExplorerActions(this));


		bCut = new JButton(new ImageIcon(loadImage("/images/cut.png")));
		bCut.setMargin(new Insets(0,0,0,0));
		bCut.setToolTipText("Cut");
		bCut.setName("Cut");
		bCut.setEnabled(true);
		bCut.addActionListener(new ExplorerActions(this));
		

		bCopy = new JButton(new ImageIcon(loadImage("/images/copy.png")));
		bCopy.setMargin(new Insets(0,0,0,0));
		bCopy.setToolTipText("Copy");
		bCopy.setName("Copy");
		bCopy.setEnabled(true);
		bCopy.addActionListener(new ExplorerActions(this));


		bPast = new JButton(new ImageIcon(loadImage("/images/past.png")));
		bPast.setMargin(new Insets(0,0,0,0));
		bPast.setToolTipText("Past");
		bPast.setName("Past");
		bPast.setEnabled(false);
		bPast.addActionListener(new ExplorerActions(this));
    
		JToolBar toolBar = new JToolBar();
		
		toolBar.add(bHome);
		toolBar.add(backward);
		toolBar.add(bForward);
		toolBar.add(bParent);
		toolBar.add(bUndo);
		toolBar.add(bRedo);
		toolBar.add(bCut);
		toolBar.add(bCopy);
		toolBar.add(bPast);
		
		status.add(toolBar,BorderLayout.WEST);
		status.add(textField, BorderLayout.EAST);
	
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.lightGray);
		contentPane.add(splitPane,BorderLayout.CENTER);
		contentPane.add(status,BorderLayout.NORTH);

		
//	Menu File
		jMenuFile.setText("File");
//	Brd le 7/04/04
		jMenuFileNew.setText("New");
//	jMenuFileNew.addActionListener(new ExplorerGUI_jMenuFileNew_ActionAdapter(this));
		jMenuFileNew.addActionListener(new ExplorerActions(this));
		jMenuFileLink.setText("Create a link");
		jMenuFileLink.addActionListener(new ExplorerGUI_jMenuFileLink_ActionAdapter(this));
		jMenuFileDeleteFolder.setText("Delete Folder");
		jMenuFileDeleteFolder.addActionListener(new ExplorerGUI_jMenuFileDeleteFolder_ActionAdapter(this));

		jMenuFileFind.setText("Find");
//	jMenuFileFind.addActionListener(new ExplorerGUI_jMenuFileFind_ActionAdapter(this));
		jMenuFileFind.addActionListener(new ExplorerFindFile(this));
	
		jMenuFileCreateFolder.setText("Create Folder");
		jMenuFileCreateFolder.addActionListener(new ExplorerActions(this));

//	Menu Edit
		jMenuEdit.setText("Edit");
		jMenuEditUndo.setText("Undo");
		jMenuEditUndo.addActionListener(new ExplorerActions(this));
		jMenuEditUndo.setEnabled(false);

		jMenuEditRedo.setText("Redo");
		jMenuEditRedo.addActionListener(new ExplorerActions(this));
		jMenuEditRedo.setEnabled(false);
	
		jMenuEditCut.setText("Cut");
		jMenuEditCut.addActionListener(new ExplorerActions(this));
		jMenuEditCut.setEnabled(true);
	
		jMenuEditCopy.setText("Copy");
		jMenuEditCopy.addActionListener(new ExplorerActions(this));
		jMenuEditCopy.setEnabled(true);
	
		jMenuEditPast.setText("Past");
		jMenuEditPast.addActionListener(new ExplorerActions(this));
		jMenuEditPast.setEnabled(false);
	
		jMenuEditSelectAll.setText("Select All");
		jMenuEditSelectAll.addActionListener(new ExplorerGUI_jMenuEditSelectAll_ActionAdapter(this));
		jMenuEditSelectAll.setEnabled(false);
	
		jMenuEditReverseSelect.setText("Reverse Select");
		jMenuEditReverseSelect.addActionListener(new ExplorerGUI_jMenuEditReverseSelect_ActionAdapter(this));
		jMenuEditReverseSelect.setEnabled(false);
	
	//	Menu Display
		jMenuDisplay.setText("Display");
		jMenuDisplayDetails.setText("Details");
		jMenuDisplayDetails.setEnabled(true);
		jMenuDisplayDetails.addActionListener(new ExplorerDetails(this));
    	jMenuFileExit.setText("Exit");
		jMenuFileExit.addActionListener(new ExplorerGUI_jMenuFileExit_ActionAdapter(this));
		jMenuHelp.setText("?");
		jMenuHelpHelp.setText("Help - Documentation");
		jMenuHelpHelp.addActionListener(new ExplorerGUI_jMenuHelpHelp_ActionAdapter(this));
		jMenuHelpAbout.setText("About Madkit - Explorer");
		jMenuHelpAbout.addActionListener(new ExplorerGUI_jMenuHelpAbout_ActionAdapter(this));
		jMenuOptions.setText("Options");
		jMenuHiddenFiles.setText("show/hide hidden files");
		jMenuHiddenFiles.addActionListener(new ExplorerGUI_jMenuHiddenFiles_ActionAdapter(this));
		jMenuBar.setActionMap(null);
//	Brd le 7/04/04
//	Menu file
		jMenuFile.add(jMenuFileNew);
		jMenuFile.add(jMenuFileLink);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuFileFind);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuFileCreateFolder);
		jMenuFile.add(jMenuFileDeleteFolder);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuFileExit);
//	Menu Edit

		jMenuEdit.add(jMenuEditUndo);
		jMenuEdit.add(jMenuEditRedo);
		jMenuEdit.addSeparator();

		jMenuEdit.add(jMenuEditCut);
		jMenuEdit.add(jMenuEditCopy);
		jMenuEdit.add(jMenuEditPast);
		jMenuEdit.addSeparator();

		jMenuEdit.add(jMenuEditSelectAll);
		jMenuEdit.add(jMenuEditReverseSelect);
//	Menu Display
		jMenuDisplay.add(jMenuDisplayDetails);
//	jMenuDisplay.add(jMenuDisplayList);
//	jMenuDisplay.add(jMenuDisplayDetails);
//	jMenuDisplay.add(jMenuDisplayReOrganizedBy);

//


//	Menu Help
		jMenuHelp.add(jMenuHelpHelp);
		jMenuHelp.add(jMenuHelpAbout);
//	Menu Options
		jMenuOptions.add(jMenuHiddenFiles);
		jMenuBar.add(jMenuFile);
//	Brd le 7/04/04
		jMenuBar.add(jMenuEdit);
		jMenuBar.add(jMenuDisplay);
//
		jMenuBar.add(jMenuOptions);
		jMenuBar.add(jMenuHelp);
	
		this.setJMenuBar(jMenuBar);
		
		explorerActionDrag = new ExplorerActionDrag ();
	
		ExplorerBackForWard.init(this);	
		ExplorerActions.actionToolBar = "init";		
		traitBackFor();

    }

    private Image loadImage(String imagePath) {
	URL url;
	url = this.getClass().getResource(imagePath);
	if (url != null) return Toolkit.getDefaultToolkit().getImage(url);
	return null;
    }

	//About action performed
	public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
		String firstMessage = "Madkit - Explorer \nauthors A.Khammal ,C.Marty , X.Lorca, J.Ferber \nversion 3.0";
		String SecondMessage = "INFORMATION";
		ExplorerOptionPane.showMessage(firstMessage, SecondMessage);

//		JOptionPane.showMessageDialog(this,"Madkit - Explorer \nauthors A.Khammal ,C.Marty , X.Lorca, J.Ferber \nversion 3.0", "INFORMATION",JOptionPane.INFORMATION_MESSAGE);
	}
	//About action performed
	public void jMenuHelpHelp_actionPerformed(ActionEvent e) {
		String firstMessage = "help on line";
		String SecondMessage = "INFORMATION";
		ExplorerOptionPane.showMessage(firstMessage, SecondMessage);
	}
	

	//File | New action performed
	public void jMenuFileLink_actionPerformed(ActionEvent e) 
	{
//		ag.killExplorerAgent();
	}

	//File | New action performed
	public void jMenuFileDeleteFolder_actionPerformed(ActionEvent e) 
	{
		String pathToExplore = explorer.absolutePath.intern();
		String arg1 = "Enter the Name of the Folder you want to DELETE  ";
		String arg2 = "DELETE FOLDER  ";

		//		name+File.separatorChar);
		String name = pathToExplore+File.separatorChar;
		String nameEntered  = ExplorerOptionPane.askForName(name,arg1,arg2);
		File fileEntered  = new File(nameEntered);
	 
		if (fileEntered.isDirectory())
		{
			arg1 = "ARE YOU SURE YOU WANT TO DELETE THIS FOLDER " + nameEntered + " ?";
			arg2 = "DELETE FOLDER  ";
			int request = ExplorerOptionPane.yesNo(arg1,arg2);
			if (request == JOptionPane.YES_OPTION)
			{
				boolean delet = fileEntered.delete();
				if (delet)
				{
					System.out.println("Delete OK");			
					explorer.read();
				}
				else
					System.out.println("Delete NOT OK");
			}
		}else
		{
/*			
			JOptionPane.showMessageDialog(null,  
			"MadKit cannot execute this action,This Folder or this File " 
				+ fileEntered 
				+ " Doesn't exist or is not a directory");
*/
			arg1 = null;
			arg2 = "MadKit cannot execute this action,This Folder or this File " 
					+ fileEntered 
					+ " Doesn't exist or is not a directory";
			ExplorerOptionPane.showMessage(arg1,arg2);
			
			nameEntered  = ExplorerOptionPane.askForName(pathToExplore,arg1,arg2);
			fileEntered  = new File(nameEntered);

		}

	}
//
   
  	//File | Exit action performed
  	public void jMenuFileExit_actionPerformed(ActionEvent e) {
  		ag.killExplorerAgent();
  	}




	//Edit | select all action performed
	public void jMenuEditSelectAll_actionPerformed(ActionEvent e) {
//			ag.killExplorerAgent();
	}

	//Edit | Reverse Select action performed
	public void jMenuEditReverseSelect_actionPerformed(ActionEvent e) {
//			ag.killExplorerAgent();
	}


	//Display| List action performed
	public void jMenuDisplayList_actionPerformed(ActionEvent e) {
//			ag.killExplorerAgent();
	}


	//Display | Reorganized by action performed
	public void jMenuDisplayReorganizedBy_actionPerformed(ActionEvent e) {
//			ag.killExplorerAgent();
	}
    

    public void tree_treeSelection(TreeSelectionEvent e) {
    	GenericTreeNode node = (GenericTreeNode)tree.getTree().getLastSelectedPathComponent();
		if (node == null) return;
		explorer.setNode((AbstractFileNode)node);
		String selection = tree.getPath((e.getPath()).getPath());
		explorer.setAbsolutePath(selection);
		traitBackFor();
		explorer.read();
		explorerActionDrag.execTree(this);
    }
    

    public void setRootPath(String path){
    	System.out.println("Setting root path:"+path);
		File f = new File(path);
		if (f.exists()){
			tree.reInstallRoot(f);
			explorer.setNode((AbstractFileNode)tree.getLocalRoot());
			explorer.setAbsolutePath(path);
//			ici
			traitBackFor();
			explorer.read();
		}
    }


    public void jMenuHiddenFiles_actionPerformed(ActionEvent e){
		boolean bool = explorer.getHidden();
		explorer.setHidden(!bool);
		tree.setHidden(!bool);
		validate();
    }   

// Tester si on a sélectionner un fichier    
    public void mousePressed(MouseEvent e)    
    {
    	
		System.out.println("mousePressed a été sélectionné");
   }


//traitement forward et backward
	public void traitBackFor()
	{
		try
		{
			if (ExplorerActions.actionToolBar.equals("backward") ||
					ExplorerActions.actionToolBar.equals("forward"))
						ExplorerActions.actionToolBar ="init";
			else
			{
				ExplorerBackForWard.vectorGuiPos = ExplorerBackForWard.vectorGuiPos + 1;		
				ExplorerBackForWard.vectorSelection.add(ExplorerBackForWard.vectorGuiPos,explorer.getAbsolutePath());
				ExplorerBackForWard.vectorNode.add(ExplorerBackForWard.vectorGuiPos,explorer.node);
				backward.setEnabled(true);			
			}

		}catch (Exception ex)
		{
		}


	}


}

class ExplorerGUI_tree_treeSelectionListenerAdapter implements javax.swing.event.TreeSelectionListener {
    ExplorerGUI adaptee;

    ExplorerGUI_tree_treeSelectionListenerAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
    }
    public void valueChanged(TreeSelectionEvent e){
	adaptee.tree_treeSelection(e);
    }
}


class ExplorerGUI_jMenuFileLink_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;
    
	ExplorerGUI_jMenuFileLink_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuFileLink_actionPerformed(e);
	}
}


class ExplorerGUI_jMenuFileDeleteFolder_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;
    
	ExplorerGUI_jMenuFileDeleteFolder_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuFileDeleteFolder_actionPerformed(e);
	}
}



class ExplorerGUI_jMenuFileExit_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;
    
	ExplorerGUI_jMenuFileExit_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuFileExit_actionPerformed(e);
	}
}
    

class ExplorerGUI_jMenuHelpAbout_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;
    
	ExplorerGUI_jMenuHelpAbout_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuHelpAbout_actionPerformed(e);
	}
}  

class ExplorerGUI_jMenuHelpHelp_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;
    
	ExplorerGUI_jMenuHelpHelp_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuHelpHelp_actionPerformed(e);
	}
}    
    
    
class ExplorerGUI_jMenuEditSelectAll_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;
    
	ExplorerGUI_jMenuEditSelectAll_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuEditSelectAll_actionPerformed(e);
	}
}  
    
class ExplorerGUI_jMenuEditReverseSelect_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;
    
	ExplorerGUI_jMenuEditReverseSelect_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuEditReverseSelect_actionPerformed(e);
	}
}  


class ExplorerGUI_jMenuHiddenFiles_ActionAdapter implements ActionListener {
    
    ExplorerGUI adaptee;

    ExplorerGUI_jMenuHiddenFiles_ActionAdapter(ExplorerGUI adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuHiddenFiles_actionPerformed(e);
    }
}  

class ExplorerGUI_textField_ActionAdapter implements ActionListener {
	ExplorerGUI adaptee;

	ExplorerGUI_textField_ActionAdapter(ExplorerGUI adaptee) {
		this.adaptee = adaptee;
	}
	
	public void actionPerformed(ActionEvent e) {
		String path = adaptee.textField.getText();
		adaptee.setRootPath(path);
		
	}
	
}

