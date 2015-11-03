package madkit.share;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;

import madkit.TreeTools.DirEntry;
import madkit.TreeTools.Entry;
import madkit.TreeTools.GenericTreeNode;
import madkit.TreeTools.LocalTree;
import madkit.TreeTools.RemoteTree;


/**
 * <p>Share Agent Interface </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>University of Montpellier II</p>
 * @authors A.Khammal , W.Abdallah , C.Marty , X.Lorca , L.Nivaggioni
 * @version 1.2
 **/

public class SenderAgentPanel extends AgentPanel {

    File file;   
    JPanel centerPanel;
    JPanel buttonAddPanel;
    JPanel buttonRemPanel;
    JPanel lTreePanel;
    JPanel vTreePanel;
    JPanel groupPane;
    JPanel rootPane;
    JPanel vPane;
    JButton add;
    JButton rem;
    JButton validGroup;
    JButton validRoot;
    JLabel statusBar;
    JLabel separator;
    JLabel group;
    RemoteTree vTree;
    LocalTree lTree;
    TitledBorder titledBorder1;
    TitledBorder titledBorder2;
    TitledBorder titledBorder3;
    TitledBorder titledBorder4;
    ImageIcon leftButtonIcon;
    ImageIcon rightButtonIcon;
    ImageIcon logoIcon;
    String lSelect;
    int vSelect;
    JPanel mainPane;
    JTabbedPane tabbedPane;
    URL url;
    JTextField groupField;
    JTextField rootField;
    //JComboBox rootCombo;

    JMenuItem jMenuSaveSender;
    JMenuItem jMenuLoadSender;

    Vector listDir = new Vector();
   
    //Construct the frame
    public SenderAgentPanel(AbstractServerAgent _ag) {

	super(_ag);
	
	statusBar = new JLabel(" ");
	separator = new JLabel(" ");
	buttonAddPanel = new JPanel();
	buttonRemPanel = new JPanel();
	centerPanel = new JPanel(new GridLayout(7,1));
	lTreePanel = new JPanel(new BorderLayout());
	vTreePanel = new JPanel(new BorderLayout());
	mainPane = new JPanel(new BorderLayout());
	rootField = new JTextField("/");
	file = new File(rootField.getText());
	//file = new File("/");
	lTree = new LocalTree(file);
	vTree = new RemoteTree(new DirEntry());
	lSelect = null;
	vSelect = -1;
	jMenuSaveSender = new JMenuItem();
	jMenuLoadSender = new JMenuItem();
	groupPane = new JPanel();
	rootPane = new JPanel();
	vPane =  new JPanel();
	group = new JLabel("Group Name : ");
	groupField = new JTextField(((AbstractServerAgent)ag).groupName);
	/*====================
	File[] allRoots = file.listRoots();
	String[] rootTab = new String[allRoots.length];
	for(int i=0;i<allRoots.length;i++){
		rootTab[i]= ((File)allRoots[i]).getAbsolutePath();
	}
	rootCombo = new JComboBox(rootTab);
	if(((String)(rootCombo.getItemAt(0))).equals("A:\\"))
		rootCombo.setSelectedIndex(1);	 
	====================*/
	rootField = new JTextField("/");
	tabbedPane = new JTabbedPane();
	url = this.getClass().getResource("/images/kde/right.gif");
	if (url != null) leftButtonIcon = new ImageIcon(url);
	else{System.out.println("erreur image : /images/kde/right.gif");}
	url = this.getClass().getResource("/images/kde/left.gif");
	if (url != null) rightButtonIcon = new ImageIcon(url);
	else{System.out.println("erreur image : /images/kde/left.gif");}
	url = this.getClass().getResource("/images/share/sender1.png");
	if (url != null) logoIcon = new ImageIcon(url);
	else{System.out.println("erreur image : /images/share/sender1.png");}
	rem = new JButton("Rem",rightButtonIcon);
	rem.setToolTipText("Click on this button to remove the selected directory from the share directory");
	rem.setEnabled(false);
	add = new JButton("Add",leftButtonIcon);
	validGroup = new JButton("ok");	
	validGroup.setToolTipText("Click on this button to validate your group");
	validGroup.setPreferredSize(new Dimension(40, 20));
	validRoot = new JButton("ok");
	validRoot.setToolTipText("Click on this button to validate your root directory");
	validRoot.setPreferredSize(new Dimension(40, 20));
	add.setVerticalTextPosition(AbstractButton.CENTER);
        add.setHorizontalTextPosition(AbstractButton.LEADING);
	add.setToolTipText("Click on this button to add the selected directory to the share directory");
	add.setEnabled(false);
	enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	try {
	    jbInit();
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }

    //Component initialization
    private void jbInit() throws Exception  {
	titledBorder1 = new TitledBorder("");
	titledBorder2 = new TitledBorder("");
	titledBorder3 = new TitledBorder("");
	titledBorder4 = new TitledBorder("");
	jMenuBar.setActionMap(null);
	jMenuSaveSender.setText("Save server as ...");
	jMenuSaveSender.setToolTipText("Save a virtual tree for a next usage");
	jMenuSaveSender.addActionListener(new SenderAgentPanel_jMenuSaveSender_ActionAdapter(this));
	jMenuLoadSender.setText("Load server");
	jMenuSaveSender.setToolTipText("Load a virtual tree");
	jMenuLoadSender.addActionListener(new SenderAgentPanel_jMenuLoadSender_ActionAdapter(this));
	jMenuFile.addSeparator();
	jMenuFile.add(jMenuLoadSender);
	jMenuFile.add(jMenuSaveSender);
	lTree.getTree().addTreeSelectionListener(new SenderAgentPanel_LTree_treeSelectionListenerAdapter(this));
	vTree.getTree().addTreeSelectionListener(new SenderAgentPanel_VTree_treeSelectionListenerAdapter(this));
	add.addActionListener(new SenderAgentPanel_add_actionListenerAdapter(this));
	rem.addActionListener(new SenderAgentPanel_rem_actionListenerAdapter(this));
	validGroup.addActionListener(new SenderAgentPanel_validGroup_actionListenerAdapter(this));
	validRoot.addActionListener(new SenderAgentPanel_validRoot_actionListenerAdapter(this));
	buttonRemPanel.add(rem);
	buttonAddPanel.add(add);
	groupPane.setLayout(new FlowLayout());
	groupPane.add(group, null);
	groupPane.add(groupField, null);
	groupPane.add(validGroup,null);
	groupField.setColumns(10);
	rootPane.setLayout(new FlowLayout());
	rootPane.add(new JLabel("root : "),null);
	rootPane.add(rootField,null);
	//rootPane.add(rootCombo,null);
	rootPane.add(validRoot,null);
	rootField.setColumns(5);
	vPane.setLayout(new FlowLayout());
	vPane.add(new JLabel("Shared Directories"),null);
	lTreePanel.add(lTree, BorderLayout.CENTER);
	vTreePanel.add(vTree, BorderLayout.CENTER);
	vTreePanel.add(vPane, BorderLayout.NORTH);
	lTreePanel.add(rootPane, BorderLayout.NORTH);
	mainPane.add(lTreePanel, BorderLayout.WEST);
	mainPane.add(groupPane, BorderLayout.NORTH);
	mainPane.add(vTreePanel, BorderLayout.EAST);
	centerPanel.add(new JLabel(" "));
	centerPanel.add(new JLabel(" "));
	centerPanel.add(buttonAddPanel);
	centerPanel.add(buttonRemPanel);
	centerPanel.add(new JLabel(" "));
	centerPanel.add(new JLabel(" "));
	centerPanel.add(new JLabel(" "));
	mainPane.add(centerPanel, BorderLayout.CENTER);
	mainPane.add(statusBar, BorderLayout.SOUTH);
	tabbedPane.addTab("Sender", logoIcon, mainPane, "Sender Agent");
	contentPane.add(tabbedPane, BorderLayout.CENTER);
 	lTree.setPreferredSize(new Dimension(200,100));
 	vTree.setPreferredSize(new Dimension(200,100));
    }

    
    public Entry getEntry() {return vTree.getRootFile();}

    public void add_action(ActionEvent e){
		//System.out.println("Un add va etre effectue avec : "+lSelect);
		listDir.add(new String(lSelect));
		DirEntry dir = new DirEntry(lSelect);
		vTree.addNode(dir);
		File temp = new File(lSelect);
		((AbstractServerAgent)getAgent()).getData().addDir(temp.getAbsolutePath());
    }
    
    public void rem_action(ActionEvent e){
		//System.out.println("Un rem va etre effectue avec : "+vSelect);
	       	listDir.remove(vSelect);
		vTree.remNode(vSelect);
		vSelect = -1;
		rem.setEnabled(false);
    }
       
    public void validGroup_action(ActionEvent e){
	((AbstractServerAgent)getAgent()).changeGroup(groupField.getText());
   }

    public void validRoot_action(ActionEvent e){
	lTreePanel.removeAll();
	lTree = new LocalTree(new File(rootField.getText()));
	//LTree = new LocalTree(new File((String)rootCombo.getSelectedItem()));
	lTree.getTree().addTreeSelectionListener(new SenderAgentPanel_LTree_treeSelectionListenerAdapter(this));
	lSelect = null;
	lTree.setPreferredSize(new Dimension(200,100));
	lTreePanel.add(lTree, BorderLayout.CENTER);
	lTreePanel.add(rootPane, BorderLayout.NORTH);
	lTreePanel.validate();
	mainPane.validate();
	repaint();	
    }

     public void jMenuFileExit_actionPerformed(ActionEvent e) {
	((AbstractServerAgent)getAgent()).sendMessage(((AbstractServerAgent)getAgent()).getAddress(),new KillMessage());
    }

    public void VTree_treeSelection(TreeSelectionEvent e){
	//System.out.println("Selection du Virtual Tree: "+VTree.getPath((e.getPath()).getPath()));
	GenericTreeNode node = (GenericTreeNode)vTree.getTree().getLastSelectedPathComponent();
	if (node == null) return;
	if (!vTree.getRootNode().isNodeChild(node)){
	    vSelect = -1;
	    rem.setEnabled(false);
	}
	else{
	    int index = node.getParent().getIndex(node);
	    //System.out.println("Index du Noeud: "+index);
	    vSelect = index;
	    rem.setEnabled(true);
	}
    }

    public void LTree_treeSelection(TreeSelectionEvent e){
	//System.out.println("Selection du Local Tree: "+LTree.getPath((e.getPath()).getPath()));
	GenericTreeNode node = (GenericTreeNode)lTree.getTree().getLastSelectedPathComponent();
	if (node == null) return;
	if (node.isLeaf()) {
	    //System.out.println("Un fichier a ete selectionne");
	    lSelect = null;
	    add.setEnabled(false);
	}
	else {
	    //System.out.println("Un repertoire a ete selectionne");
	    //System.out.println(LTree.getPath(node.getPath()));
	    //LSelect = LTree.getPath(node.getPath());
	    lSelect = lTree.getPath((e.getPath()).getPath());
	    add.setEnabled(true);
	}
    }
    
     public void jMenuLoadSender_actionPerformed(ActionEvent e) {
    	JFileChooser chooser = new JFileChooser();
    	chooser.setCurrentDirectory(new File( ((AbstractServerAgent)getAgent()).madkitDirectory+File.separator+"share"));
    	int returnVal = chooser.showOpenDialog(this);
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
    		try{
    			listDir.clear();
    			File f = new File((chooser.getSelectedFile()).getAbsolutePath());
			RandomAccessFile raf = new RandomAccessFile(f,"r");
			String element;
			int cut;
			while ( (element = raf.readLine()) != null ){
				cut = element.indexOf(" ** ");
				listDir.add(element.substring(0,cut));
				element = element.substring(cut+4);
			}
			raf.close();
		}
		catch(IOException ioe){System.out.println("(Client) Can't read share.init");}
		vTreePanel.removeAll();
		vTree = new RemoteTree(new DirEntry());
		vTree.getTree().addTreeSelectionListener(new SenderAgentPanel_VTree_treeSelectionListenerAdapter(this));
		vTreePanel.add(vTree, BorderLayout.CENTER);
		vTreePanel.add(vPane, BorderLayout.NORTH);
		vTree.setPreferredSize(new Dimension(200,100));	
		vSelect = -1;	
		for(int i=0;i<listDir.size();i++){
			DirEntry dir = new DirEntry((String)listDir.elementAt(i));
			vTree.addNode(dir);
			File temp = new File((String)listDir.elementAt(i));
			((AbstractServerAgent)getAgent()).getData().addDir(temp.getAbsolutePath());
		}
		mainPane.validate();
		repaint();				
    	}
    }
     public void jMenuSaveSender_actionPerformed(ActionEvent e) {
    	JFileChooser chooser = new JFileChooser();
    	chooser.setCurrentDirectory(new File( ((AbstractServerAgent)getAgent()).madkitDirectory+File.separator+"share"));
    	int returnVal = chooser.showSaveDialog(this);
    	if(returnVal == JFileChooser.APPROVE_OPTION) {
    		File f = new File((chooser.getSelectedFile()).getAbsolutePath());
	    	try{
	    		f.createNewFile();
	    		FileOutputStream settings = new FileOutputStream (f);
			OutputStreamWriter w = new OutputStreamWriter(settings);
			for(int i=0;i<listDir.size();i++)
				w.write(listDir.elementAt(i)+" ** \n");
			w.close();
			settings.close();
		}
	    	catch(IOException ioe){System.out.println("(Server) Can't write FileSender");}	
    	}
    }
}

class SenderAgentPanel_add_actionListenerAdapter implements java.awt.event.ActionListener {
    SenderAgentPanel adaptee;

    SenderAgentPanel_add_actionListenerAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e){
	adaptee.add_action(e);
    }
}

class SenderAgentPanel_rem_actionListenerAdapter implements java.awt.event.ActionListener {

    SenderAgentPanel adaptee;

    SenderAgentPanel_rem_actionListenerAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    
    public void actionPerformed(ActionEvent e){
	adaptee.rem_action(e);
    }
}

class SenderAgentPanel_validGroup_actionListenerAdapter implements java.awt.event.ActionListener {

    SenderAgentPanel adaptee;

    SenderAgentPanel_validGroup_actionListenerAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    
    public void actionPerformed(ActionEvent e){
	adaptee.validGroup_action(e);
    }
}

class SenderAgentPanel_validRoot_actionListenerAdapter implements java.awt.event.ActionListener {

    SenderAgentPanel adaptee;

    SenderAgentPanel_validRoot_actionListenerAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    
    public void actionPerformed(ActionEvent e){
	adaptee.validRoot_action(e);
    }
}

class SenderAgentPanel_LTree_treeSelectionListenerAdapter implements javax.swing.event.TreeSelectionListener {
    SenderAgentPanel adaptee;

    SenderAgentPanel_LTree_treeSelectionListenerAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void valueChanged(TreeSelectionEvent e){
	adaptee.LTree_treeSelection(e);
    }
}

class SenderAgentPanel_VTree_treeSelectionListenerAdapter implements javax.swing.event.TreeSelectionListener {
    SenderAgentPanel adaptee;

    SenderAgentPanel_VTree_treeSelectionListenerAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void valueChanged(TreeSelectionEvent e){
	adaptee.VTree_treeSelection(e);
    }
}
class SenderAgentPanel_jMenuSaveSender_ActionAdapter implements ActionListener {
    SenderAgentPanel adaptee;
    
    SenderAgentPanel_jMenuSaveSender_ActionAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuSaveSender_actionPerformed(e);
    }
}    
class SenderAgentPanel_jMenuLoadSender_ActionAdapter implements ActionListener {
    SenderAgentPanel adaptee;
    
    SenderAgentPanel_jMenuLoadSender_ActionAdapter(SenderAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuLoadSender_actionPerformed(e);
    }
}    
