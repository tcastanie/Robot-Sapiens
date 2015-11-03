package madkit.share;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Vector;
import java.util.HashMap;

import javax.swing.*;
import java.awt.*;

import madkit.TreeTools.DirEntry;
import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;


class PluginUpdatePanel extends JPanel implements ActionListener {

//	DirEntry entry;
	AgentAddress address;
	PluginShareAgent agent;
//	ShareRemoteTree tree;
	PluginShareAgentPanel agentPanel;
	JButton downloadButton;
	JButton refreshButton;
//	JButton viewButton;
//	JButton closeButton;
	//TreePath selection;

	JPanel optionPanel;
//	JPanel downloadPanel;
//	JPanel refreshPanel;
//	JPanel viewPanel;
//	JPanel closePanel;
	JPanel infoPanel;
	

	UpdatePluginTable updatePluginTable;

	public PluginUpdatePanel(PluginShareAgentPanel _agentPanel, Vector stockUpdatedPlugin, PluginShareAgent _agent ,AgentAddress _address){
	agentPanel = _agentPanel;
	address = _address;
	agent = _agent;
	setLayout(new BorderLayout());
	optionPanel = new JPanel();
//	downloadPanel = new JPanel();
//	refreshPanel = new JPanel();
//	viewPanel = new JPanel();
//	closePanel = new JPanel();
	infoPanel = new JPanel();
	optionPanel.setLayout(new GridLayout(3,1));
//	downloadPanel.setLayout(new FlowLayout());
//	refreshPanel.setLayout(new FlowLayout());
//	viewPanel.setLayout(new FlowLayout());
//	closePanel.setLayout(new FlowLayout());
	infoPanel.setLayout(new FlowLayout());
	downloadButton = addButton("Download",
						"/toolbarButtonGraphics/media/StepForward24.gif",
						"Download and update selected plugins", this);
//	downloadButton.setEnabled(false);
//	downloadButton.addActionListener(new PluginServerPanel_download_actionListenerAdapter(this));
//	closeButton = new JButton("Close");
//	closeButton.setToolTipText("Close current server panel");
//	closeButton.addActionListener(new PluginServerPanel_close_actionListenerAdapter(this));
	refreshButton = addButton("Refresh",
						"/toolbarButtonGraphics/general/Refresh24.gif",
						"Refresh the list of plugins to update", this);
//	refreshButton.addActionListener(new PluginServerPanel_refresh_actionListenerAdapter(this));
//	viewButton = new JButton("View Incoming");
//	viewButton.setToolTipText("Open your incoming directory with an explorer");
//	viewButton.addActionListener(new PluginServerPanel_view_actionListenerAdapter(this));
	Calendar date = Calendar.getInstance();
	JLabel infoDate = new JLabel(" Updated : "+date.HOUR_OF_DAY+":"+date.MINUTE+":"+date.SECOND);

	infoPanel.add(infoDate);
	optionPanel.add(downloadButton);
	optionPanel.add(refreshButton);
	optionPanel.add(infoDate);

	updatePluginTable = new UpdatePluginTable(stockUpdatedPlugin, agent);
	//tree = new ShareRemoteTree(entry, agent, address);
	//tree.getTree().addTreeSelectionListener(this);
	add(updatePluginTable, BorderLayout.CENTER);
	add(optionPanel, BorderLayout.EAST);
	}
	
	void updateTable(Vector update){
		 updatePluginTable.updateTable(update);
	 }
	 
	 public void actionPerformed(ActionEvent e){
	 	String c = e.getActionCommand();
	 	if (c.equals("Download")){
	 		//System.out.println("updating...");
			((UpdatePluginTableModel)updatePluginTable.getTableModel()).sendRequestPlugin(agent);
	 	} else if (c.equals("Refresh")){
			//System.out.println("Refreshing...");
			agent.checkPlugins(new AgentAddress[]{address});
	 	}
	 }
	 
	JButton addButton(String name, String imageName, String toolTipTex, ActionListener listener) {
		   JButton b;
		   if ((imageName == null) || (imageName.equals(""))) {
			   b = new JButton(name);
			   b.setActionCommand(name);
		   }
		   else {
			   ImageIcon i=null;
			   java.net.URL u = this.getClass().getResource(imageName);
			   if (u != null)
				   i = new ImageIcon (u);

			   if ((i != null) && (i.getImage()!=null))
				   b = new JButton(i);
			   else
				   b = new JButton(name);
			   b.setActionCommand(name);
		   }

		   b.setToolTipText(toolTipTex);
		   b.setMargin(new Insets(0,0,0,0));
		   b.addActionListener(listener);
		   return b;
	   }

//	public void close_action(ActionEvent e){
//			agentPanel.removePluginServerPanel();
//	}

//	public void download_action(ActionEvent e){
//	//System.out.println("Class ShareAgentPanel : download");
//	tree.getPath(selection.toString());
//	tree.send();
//	}

//	public void refresh_action(ActionEvent e){
//	agentPanel.removePluginServerPanel();
//	agentPanel.addServerPanel(address);
//	}

//	public void view_action(ActionEvent e){
//	final JFrame frame = new JFrame("Explorer");
//		frame.addWindowListener(new WindowAdapter() {
//		public void windowClosing(WindowEvent e) {frame.dispose();}
//		});
//
//		frame.setContentPane(new ExplorerPanel( agent.madkitDirectory+File.separator+agent.groupName+File.separator+"Incoming"));
//		frame.pack();
//		frame.setVisible(true);
//	}

//	public void valueChanged(TreeSelectionEvent e){
//	selection = tree.getTree().getSelectionPath();
//	if (selection == null) downloadButton.setEnabled(false);
//	else downloadButton.setEnabled(true);
//	}

} 

public class PluginShareAgentPanel extends AgentPanel  {

	PluginShareAgent ag;
	

	HashMap panelsMap=new HashMap(); 

	//public JDesktopPane desktop;
	JPanel mainPane;
	JPanel listPane;
	JPanel groupPane;
	JPanel buttonServerListPane;
	JPanel listPanel;
	JTabbedPane tabbedPane;
	JLabel status;
	JLabel separator;
	JLabel group;
	ImageIcon logo;
	URL url;
	JButton serverListButton;
	JTextField groupField;
	ShareList serverList;
	String role;

	//Menu
	JMenuBar jMenuBar1;
	JMenu jMenuFile;
	JMenuItem jMenuFileExit;
	JMenu jMenuHelp;
	JMenuItem jMenuHelpAbout;
//	JMenu jMenuOption;
	JMenuItem jMenuUpdatedFile;
//	JMenuItem jMenuSettings;
//	JMenuItem jMenuSearchFile;
//	JMenuItem jMenuViewExplorer;

	public PluginShareAgentPanel(PluginShareAgent _ag) {

	super(_ag);
	ag = (PluginShareAgent) getAgent();
	role = ag.serverRoleName;
	serverList = new ShareList(new String[20],_ag,new AgentAddress[20]);

	//Menu
	jMenuBar1 = new JMenuBar();
	jMenuFile = new JMenu();
	jMenuFileExit = new JMenuItem();
	jMenuHelp = new JMenu();
	jMenuHelpAbout = new JMenuItem();
//	jMenuOption = new JMenu();
	jMenuUpdatedFile = new JMenuItem();
//	jMenuSettings = new JMenuItem();
//	jMenuSearchFile = new JMenuItem();
//	jMenuViewExplorer= new JMenuItem();
//	jMenuSearchFile= new JMenuItem();

	//desktop = new JDesktopPane();
	mainPane = new JPanel();
	listPane = new JPanel();
	listPanel = new JPanel();
	groupPane = new JPanel();

	buttonServerListPane = new JPanel();
	tabbedPane = new JTabbedPane();
	status = new JLabel(" ");
	separator = new JLabel("       ");
	group = new JLabel("Group Name : ");
	serverListButton = new JButton("Server List");
	groupField = new JTextField(ag.groupName);
	url = this.getClass().getResource("/images/share/share.png");
	if (url != null) logo = new ImageIcon(url);
	else{System.out.println("erreur image : /images/share/share.png");}
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		//Menu
		jMenuFile.setText("File");
		jMenuFileExit.setText("Exit");
		jMenuHelp.setText("Help");
		jMenuHelpAbout.setText("About");
//		jMenuOption.setToolTipText("");
//		jMenuOption.setText("Option");
//		jMenuSettings.setToolTipText("");
//		jMenuSettings.setText("Program Settings...");
//		jMenuUpdatedFile.setToolTipText("");
//		jMenuUpdatedFile.setText("View Updated Files...");
//		jMenuSearchFile.setText("Search file...");
//		jMenuViewExplorer.setText("View Incoming directory...");
		jMenuFile.add(jMenuFileExit);
		jMenuHelp.add(jMenuHelpAbout);
		jMenuBar1.add(jMenuFile);
//		jMenuBar1.add(jMenuOption);
		jMenuBar1.add(jMenuHelp);
		this.setJMenuBar(jMenuBar1);
//		jMenuOption.add(jMenuSettings);
//		jMenuOption.addSeparator();
//		jMenuOption.add(jMenuUpdatedFile);
//		jMenuOption.add(jMenuViewExplorer);
//		jMenuOption.addSeparator();
//		jMenuOption.add(jMenuSearchFile);


		jMenuFileExit.addActionListener(new AgentPanel_jMenuFileExit_ActionAdapter(this));
		jMenuHelpAbout.addActionListener(new PluginShareAgentPanel_jMenuHelpAbout_ActionAdapter(this));
		jMenuUpdatedFile.addActionListener(new PluginShareAgentPanel_jMenuUpdatedFile_ActionAdapter(this));
//		jMenuSettings.addActionListener(new PluginShareAgentPanel_jMenuSettings_ActionAdapter(this));
//		jMenuSearchFile.addActionListener(new PluginShareAgentPanel_jMenuSearchFile_ActionAdapter(this));
//		jMenuViewExplorer.addActionListener(new PluginShareAgentPanel_jMenuViewExplorer_ActionAdapter(this));

		contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(450,250));
		contentPane.setBackground(Color.lightGray);
		groupPane.setLayout(new FlowLayout());
		groupPane.add(group, null);
		groupPane.add(groupField, null);
		groupField.setColumns(15);
		serverListButton.addActionListener(new PluginShareAgentPanel_list_actionListenerAdapter(this));
		buttonServerListPane.setLayout(new FlowLayout());
		buttonServerListPane.add(serverListButton,null);
		listPane.setPreferredSize(new Dimension(200,100));
		listPane.setLayout(new BorderLayout());
		listPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		listPanel.setBackground(Color.white);
		listPanel.add(serverList, null);
		listPane.add(listPanel,BorderLayout.CENTER);
		listPane.add(buttonServerListPane, BorderLayout.NORTH);
		mainPane.setLayout(new BorderLayout());
		mainPane.add(new JLabel("               "), BorderLayout.EAST);
		mainPane.add(listPane, BorderLayout.CENTER);
		mainPane.add(status, BorderLayout.SOUTH);
		mainPane.add(new JLabel("               "), BorderLayout.WEST);
		tabbedPane.addTab(ag.groupName, logo, mainPane, "Plugin Share Agent");
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		contentPane.add(groupPane, BorderLayout.NORTH);
	}


	

	public void initList(){
		String groupName = groupField.getText();
		AgentAddress[] aglist = getAgent().getAgentsWithRole(groupName,role);
		if (aglist.length==0) JOptionPane.showMessageDialog(this,"Could'nt find server","Error",JOptionPane.ERROR_MESSAGE);
		String[] aginfos = new String[aglist.length];
		for(int i=0;i<aginfos.length;i++) {
			aginfos[i]=aglist[i].getKernel().toString();
		}
		serverList = new ShareList(aginfos, (PluginShareAgent)getAgent() ,aglist);
		//((PluginShareAgent)getAgent()).checkPlugins(aglist);
	}

//	public void addServerPanel(DirEntry entry, AgentAddress serverAddress){
//		url = this.getClass().getResource("/images/share/sender1.png");
//		if (url != null) logo = new ImageIcon(url);
//		JPanel pane = new PluginServerPanel(this, entry, (PluginShareAgent)getAgent(), serverAddress);
//		tabbedPane.addTab(serverAddress.getKernel().getHost().toString(), logo, pane, "Server Agent");
//		repaint();
//	}
	
	public void addServerPanel(AgentAddress serverAddress){
			url = this.getClass().getResource("/images/share/sender1.png");
			if (url != null) logo = new ImageIcon(url);
			JPanel pane = new PluginUpdatePanel(this, new Vector(),(PluginShareAgent)getAgent(), serverAddress);
			KernelAddress kernel = serverAddress.getKernel();
			panelsMap.put(kernel.toString(), pane);
			
			tabbedPane.addTab(serverAddress.getKernel().getHost().toString(), logo, pane, "Plugin Server");
			revalidate();
			ag.checkPlugins(new AgentAddress[]{serverAddress});
		}
		
	public void updatePlugin(AgentAddress serverAddress, Vector stockUpdatedPlugin){
		System.out.println("updating plugin from: "+serverAddress+" ,with:"+stockUpdatedPlugin);
		PluginUpdatePanel panel = (PluginUpdatePanel) this.panelsMap.get(serverAddress.getKernel().toString());
		if (panel != null){
			System.out.println("updating panel");
			panel.updateTable(stockUpdatedPlugin);
			panel.repaint();
		}	
	}

//	public void removePluginServerPanel(){
//		tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
//		repaint();
//	}

	public void list_action(ActionEvent e){
		listPanel.removeAll();
		listPane.removeAll();
		listPane.validate();
		mainPane.validate();
		listPane.add(buttonServerListPane, BorderLayout.NORTH);
		listPanel.setBackground(Color.white);
		initList();
		listPanel.add(serverList,null);
		listPane.add(listPanel, BorderLayout.CENTER);
		//listPane.validate();
		mainPane.validate();
	}

	public void jMenuFileExit_actionPerformed(ActionEvent e) {
	((PluginShareAgent)getAgent()).sendMessage(((PluginShareAgent)getAgent()).getAddress(),new KillMessage());
	}

	 public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
	JOptionPane.showMessageDialog(this,"Madkit - Share\nMadKit Team \nauthors: A.Khammal , W.Abdallah , C.Marty,\n X.Lorca , S. Lara, J. Ferber \nversion 2.1", "INFORMATION",JOptionPane.INFORMATION_MESSAGE);
	}

	 public void jMenuUpdatedFile_actionPerformed(ActionEvent e) {
	((PluginShareAgent)getAgent()).showUpdateTable();
	}

	public void jMenuSearchFile_actionPerformed(ActionEvent e) {
	ShareSearchFilePanel searchPanel = new ShareSearchFilePanel(ag);
	searchPanel.display();
	}

//	public void jMenuSettings_actionPerformed(ActionEvent e) {
//		ShareOptionPanel option = new ShareOptionPanel(this);
//	}
	
//	 public void jMenuViewExplorer_actionPerformed(ActionEvent e) {
//	 final JFrame frame = new JFrame("Explorer");
//	 frame.addWindowListener(new WindowAdapter() {
//		 public void windowClosing(WindowEvent e) {frame.dispose();}
//		 });
//
//	 frame.setContentPane(new ExplorerPanel( ((PluginShareAgent)getAgent()).madkitDirectory+File.separator+ag.groupName+File.separator+"Incoming"));
//	 frame.pack();
//	 frame.setVisible(true);
//	 }
}

class PluginShareAgentPanel_list_actionListenerAdapter implements java.awt.event.ActionListener {
	PluginShareAgentPanel adaptee;

	PluginShareAgentPanel_list_actionListenerAdapter(PluginShareAgentPanel adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e){
	adaptee.list_action(e);
	}
}

class PluginShareAgentPanel_jMenuHelpAbout_ActionAdapter implements ActionListener {
	PluginShareAgentPanel adaptee;

   PluginShareAgentPanel_jMenuHelpAbout_ActionAdapter(PluginShareAgentPanel adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuHelpAbout_actionPerformed(e);
	}
}

class PluginShareAgentPanel_jMenuUpdatedFile_ActionAdapter implements ActionListener {
	PluginShareAgentPanel adaptee;

   PluginShareAgentPanel_jMenuUpdatedFile_ActionAdapter(PluginShareAgentPanel adaptee) {
	this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
	adaptee.jMenuUpdatedFile_actionPerformed(e);
	}
}

//class PluginShareAgentPanel_jMenuSettings_ActionAdapter implements ActionListener {
//	PluginShareAgentPanel adaptee;
//
//   PluginShareAgentPanel_jMenuSettings_ActionAdapter(PluginShareAgentPanel adaptee) {
//	this.adaptee = adaptee;
//	}
//	public void actionPerformed(ActionEvent e) {
//	adaptee.jMenuSettings_actionPerformed(e);
//	}
//}

//class PluginShareAgentPanel_jMenuSearchFile_ActionAdapter implements ActionListener {
//	PluginShareAgentPanel adaptee;
//
//	PluginShareAgentPanel_jMenuSearchFile_ActionAdapter(PluginShareAgentPanel adaptee) {
//	this.adaptee = adaptee;
//	}
//	public void actionPerformed(ActionEvent e) {
//	adaptee.jMenuSearchFile_actionPerformed(e);
//	}
//}
//class PluginShareAgentPanel_jMenuViewExplorer_ActionAdapter implements ActionListener {
//	PluginShareAgentPanel adaptee;
//
//	PluginShareAgentPanel_jMenuViewExplorer_ActionAdapter(PluginShareAgentPanel adaptee) {
//	this.adaptee = adaptee;
//	}
//	public void actionPerformed(ActionEvent e) {
//	adaptee.jMenuViewExplorer_actionPerformed(e);
//	}
//}

/*

class PluginServerPanel_close_actionListenerAdapter implements java.awt.event.ActionListener {
	PluginServerPanel adaptee;

	PluginServerPanel_close_actionListenerAdapter(PluginServerPanel adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e){
	adaptee.close_action(e);
	}
}

class PluginServerPanel_download_actionListenerAdapter implements java.awt.event.ActionListener {
	PluginServerPanel adaptee;

	PluginServerPanel_download_actionListenerAdapter(PluginServerPanel adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e){
	adaptee.download_action(e);
	}
}

class PluginServerPanel_refresh_actionListenerAdapter implements java.awt.event.ActionListener {
	PluginServerPanel adaptee;

	PluginServerPanel_refresh_actionListenerAdapter(PluginServerPanel adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e){
	adaptee.refresh_action(e);
	}
}
class PluginServerPanel_view_actionListenerAdapter implements java.awt.event.ActionListener {
	PluginServerPanel adaptee;

	PluginServerPanel_view_actionListenerAdapter(PluginServerPanel adaptee) {
	this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e){
	adaptee.view_action(e);
	}
}
*/