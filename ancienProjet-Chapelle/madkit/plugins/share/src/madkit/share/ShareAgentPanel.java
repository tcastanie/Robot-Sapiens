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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import madkit.TreeTools.DirEntry;
import madkit.kernel.AgentAddress;

class ServerPanel extends JPanel implements TreeSelectionListener {

    DirEntry entry;
    AgentAddress address;
    AbstractShareAgent agent;
    ShareRemoteTree tree;
    ShareAgentPanel agentPanel;
    JButton downloadButton;
    JButton closeButton;
    JButton refreshButton;
    JButton viewButton;
    TreePath selection;

    JPanel optionPanel;
    JPanel downloadPanel;
    JPanel refreshPanel;
    JPanel viewPanel;
    JPanel closePanel;
    JPanel infoPanel;


    public ServerPanel(ShareAgentPanel _agentPanel, DirEntry _entry, AbstractShareAgent _agent ,AgentAddress _address){
	agentPanel = _agentPanel;
	entry = _entry;
	address = _address;
	agent = _agent;
	setLayout(new BorderLayout());
	optionPanel = new JPanel();
	downloadPanel = new JPanel();
	refreshPanel = new JPanel();
	viewPanel = new JPanel();
	closePanel = new JPanel();
	infoPanel = new JPanel();
	optionPanel.setLayout(new GridLayout(5,1));
	downloadPanel.setLayout(new FlowLayout());
	refreshPanel.setLayout(new FlowLayout());
	viewPanel.setLayout(new FlowLayout());
	closePanel.setLayout(new FlowLayout());
	infoPanel.setLayout(new FlowLayout());
	downloadButton = new JButton("Download");
	downloadButton.setToolTipText("Download selected file or directory");
	downloadButton.setEnabled(false);
	downloadButton.addActionListener(new ServerPanel_download_actionListenerAdapter(this));
	closeButton = new JButton("Close");
	closeButton.setToolTipText("Close current server panel");
	closeButton.addActionListener(new ServerPanel_close_actionListenerAdapter(this));
	refreshButton = new JButton("Refresh");
	refreshButton.setToolTipText("Refresh current server panel");
	refreshButton.addActionListener(new ServerPanel_refresh_actionListenerAdapter(this));
	viewButton = new JButton("View Incoming");
	viewButton.setToolTipText("Open your incoming directory with an explorer");
	viewButton.addActionListener(new ServerPanel_view_actionListenerAdapter(this));
	Calendar date = Calendar.getInstance();
	JLabel infoDate = new JLabel(" Updated : "+date.HOUR_OF_DAY+":"+date.MINUTE+":"+date.SECOND);
	downloadPanel.add(downloadButton);
	refreshPanel.add(refreshButton);
	viewPanel.add(viewButton);
	closePanel.add(closeButton);
	infoPanel.add(infoDate);
	optionPanel.add(downloadPanel);
	optionPanel.add(refreshPanel);
	optionPanel.add(viewPanel);
	optionPanel.add(closePanel);
	optionPanel.add(infoDate);
	tree = new ShareRemoteTree(entry, agent, address);
	tree.getTree().addTreeSelectionListener(this);
	add(tree, BorderLayout.CENTER);
	add(optionPanel, BorderLayout.EAST);
    }

    public void close_action(ActionEvent e){
	agentPanel.removeServerPanel();
    }

    public void download_action(ActionEvent e){
	//System.out.println("Class ShareAgentPanel : download");
	tree.getPath(selection.toString());
	tree.send();
    }

    public void refresh_action(ActionEvent e){
	agentPanel.removeServerPanel();
	agentPanel.addServerPanel(entry,address);
    }

    public void view_action(ActionEvent e){
	final JFrame frame = new JFrame("Explorer");
        frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {frame.dispose();}
	    });

        frame.setContentPane(new ExplorerPanel( agent.madkitDirectory+File.separator+agent.groupName+File.separator+"Incoming"));
        frame.pack();
        frame.setVisible(true);
    }

    public void valueChanged(TreeSelectionEvent e){
	selection = tree.getTree().getSelectionPath();
	if (selection == null) downloadButton.setEnabled(false);
	else downloadButton.setEnabled(true);
    }

}

public class ShareAgentPanel extends AgentPanel  {

    AbstractShareAgent ag;

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
    JMenu jMenuOption;
    JMenuItem jMenuSettings;
    JMenuItem jMenuUpdatedFile;
    JMenuItem jMenuSearchFile;
    JMenuItem jMenuViewExplorer;

    public ShareAgentPanel(AbstractShareAgent _ag) {

	super(_ag);
	ag = (AbstractShareAgent) getAgent();
	role = ag.serverRoleName;
	serverList = new ShareList(new String[20],_ag,new AgentAddress[20]);

	//Menu
	jMenuBar1 = new JMenuBar();
  	jMenuFile = new JMenu();
  	jMenuFileExit = new JMenuItem();
  	jMenuHelp = new JMenu();
  	jMenuHelpAbout = new JMenuItem();
  	jMenuOption = new JMenu();
  	jMenuSettings = new JMenuItem();
  	jMenuUpdatedFile = new JMenuItem();
	jMenuSearchFile = new JMenuItem();
	jMenuViewExplorer= new JMenuItem();
	jMenuSearchFile= new JMenuItem();

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
    	jMenuOption.setToolTipText("");
    	jMenuOption.setText("Option");
    	jMenuSettings.setToolTipText("");
   		jMenuSettings.setText("Program Settings...");
   		jMenuUpdatedFile.setToolTipText("");
    	jMenuUpdatedFile.setText("View Updated Files...");
		jMenuSearchFile.setText("Search file...");
		jMenuViewExplorer.setText("View Incoming directory...");
    	jMenuFile.add(jMenuFileExit);
    	jMenuHelp.add(jMenuHelpAbout);
    	jMenuBar1.add(jMenuFile);
    	jMenuBar1.add(jMenuOption);
    	jMenuBar1.add(jMenuHelp);
   		this.setJMenuBar(jMenuBar1);
   		jMenuOption.add(jMenuSettings);
    	jMenuOption.addSeparator();
    	jMenuOption.add(jMenuUpdatedFile);
    	jMenuOption.add(jMenuViewExplorer);
    	jMenuOption.addSeparator();
		jMenuOption.add(jMenuSearchFile);


    	jMenuFileExit.addActionListener(new AgentPanel_jMenuFileExit_ActionAdapter(this));
    	jMenuHelpAbout.addActionListener(new ShareAgentPanel_jMenuHelpAbout_ActionAdapter(this));
    	jMenuUpdatedFile.addActionListener(new ShareAgentPanel_jMenuUpdatedFile_ActionAdapter(this));
    	jMenuSettings.addActionListener(new ShareAgentPanel_jMenuSettings_ActionAdapter(this));
		jMenuSearchFile.addActionListener(new ShareAgentPanel_jMenuSearchFile_ActionAdapter(this));
		jMenuViewExplorer.addActionListener(new ShareAgentPanel_jMenuViewExplorer_ActionAdapter(this));

		contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(550,350));
		contentPane.setBackground(Color.lightGray);
		groupPane.setLayout(new FlowLayout());
		groupPane.add(group, null);
		groupPane.add(groupField, null);
		groupField.setColumns(15);
		serverListButton.addActionListener(new ShareAgentPanel_list_actionListenerAdapter(this));
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
	 	tabbedPane.addTab(ag.groupName, logo, mainPane, "Share Agent");
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
		serverList = new ShareList(aginfos, (AbstractShareAgent)getAgent() ,aglist);
		//((AbstractShareAgent)getAgent()).checkPlugins(aglist);
    }

    public void addServerPanel(DirEntry entry, AgentAddress serverAddress){
    	url = this.getClass().getResource("/images/share/sender1.png");
		if (url != null) logo = new ImageIcon(url);
		JPanel pane = new ServerPanel(this, entry, (AbstractShareAgent)getAgent(), serverAddress);
		tabbedPane.addTab(serverAddress.getKernel().getHost().toString(), logo, pane, "Server Agent");
		repaint();
    }

    public void removeServerPanel(){
    	tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
    	repaint();
    }

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
 	listPane.validate();
 	mainPane.validate();
    }

    public void jMenuFileExit_actionPerformed(ActionEvent e) {
	((AbstractShareAgent)getAgent()).sendMessage(((AbstractShareAgent)getAgent()).getAddress(),new KillMessage());
    }

     public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
	JOptionPane.showMessageDialog(this,"Madkit - Share \nUniversity of Montpellier II \nauthors A.Khammal , W.Abdallah , C.Marty , X.Lorca , L.Nivaggioni \nversion 2.5", "INFORMATION",JOptionPane.INFORMATION_MESSAGE);
    }

     public void jMenuUpdatedFile_actionPerformed(ActionEvent e) {
	((AbstractShareAgent)getAgent()).showUpdateTable();
    }

    public void jMenuSearchFile_actionPerformed(ActionEvent e) {
	ShareSearchFilePanel searchPanel = new ShareSearchFilePanel(ag);
	searchPanel.display();
    }

    public void jMenuSettings_actionPerformed(ActionEvent e) {
    	ShareOptionPanel option = new ShareOptionPanel(this);
    }
     public void jMenuViewExplorer_actionPerformed(ActionEvent e) {
	 final JFrame frame = new JFrame("Explorer");
	 frame.addWindowListener(new WindowAdapter() {
		 public void windowClosing(WindowEvent e) {frame.dispose();}
	     });

	 frame.setContentPane(new ExplorerPanel( ((AbstractShareAgent)getAgent()).madkitDirectory+File.separator+ag.groupName+File.separator+"Incoming"));
	 frame.pack();
	 frame.setVisible(true);
     }
}

class ShareAgentPanel_list_actionListenerAdapter implements java.awt.event.ActionListener {
    ShareAgentPanel adaptee;

    ShareAgentPanel_list_actionListenerAdapter(ShareAgentPanel adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e){
	adaptee.list_action(e);
    }
}

class ShareAgentPanel_jMenuHelpAbout_ActionAdapter implements ActionListener {
    ShareAgentPanel adaptee;

   ShareAgentPanel_jMenuHelpAbout_ActionAdapter(ShareAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuHelpAbout_actionPerformed(e);
    }
}

class ShareAgentPanel_jMenuUpdatedFile_ActionAdapter implements ActionListener {
    ShareAgentPanel adaptee;

   ShareAgentPanel_jMenuUpdatedFile_ActionAdapter(ShareAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuUpdatedFile_actionPerformed(e);
    }
}

class ShareAgentPanel_jMenuSettings_ActionAdapter implements ActionListener {
    ShareAgentPanel adaptee;

   ShareAgentPanel_jMenuSettings_ActionAdapter(ShareAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuSettings_actionPerformed(e);
    }
}

class ShareAgentPanel_jMenuSearchFile_ActionAdapter implements ActionListener {
    ShareAgentPanel adaptee;

    ShareAgentPanel_jMenuSearchFile_ActionAdapter(ShareAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuSearchFile_actionPerformed(e);
    }
}
class ShareAgentPanel_jMenuViewExplorer_ActionAdapter implements ActionListener {
    ShareAgentPanel adaptee;

    ShareAgentPanel_jMenuViewExplorer_ActionAdapter(ShareAgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuViewExplorer_actionPerformed(e);
    }
}



class ServerPanel_close_actionListenerAdapter implements java.awt.event.ActionListener {
    ServerPanel adaptee;

    ServerPanel_close_actionListenerAdapter(ServerPanel adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e){
	adaptee.close_action(e);
    }
}

class ServerPanel_download_actionListenerAdapter implements java.awt.event.ActionListener {
    ServerPanel adaptee;

    ServerPanel_download_actionListenerAdapter(ServerPanel adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e){
	adaptee.download_action(e);
    }
}

class ServerPanel_refresh_actionListenerAdapter implements java.awt.event.ActionListener {
    ServerPanel adaptee;

    ServerPanel_refresh_actionListenerAdapter(ServerPanel adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e){
	adaptee.refresh_action(e);
    }
}
class ServerPanel_view_actionListenerAdapter implements java.awt.event.ActionListener {
    ServerPanel adaptee;

    ServerPanel_view_actionListenerAdapter(ServerPanel adaptee) {
	this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e){
	adaptee.view_action(e);
    }
}
