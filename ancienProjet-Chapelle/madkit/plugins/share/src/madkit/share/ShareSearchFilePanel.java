package madkit.share;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import madkit.kernel.AgentAddress;


class ShareSearchFilePanel extends JFrame implements ActionListener {
    
    AbstractShareAgent shareAgent;
    SmallSearchFileAgent ssfa;
    String fileName;
    SearchTable searchTable;

    JPanel mainPanel;
    JPanel northPanel;
    JPanel southPanel;
    JPanel titlePane;
    JPanel searchPane;
    JPanel resultPane;
    JPanel downloadPane;
    JPanel closePane;
    JTextField searchTextField;
    //JTextField groupTextField;
    JButton searchButton;
    JButton closeButton;
    JButton stopButton;
    JButton downloadButton;

    public String getFileName() {return fileName;}
   // public String getGroup() {return groupTextField.getText();}

    public ShareSearchFilePanel(AbstractShareAgent s){
	shareAgent = s;
	fileName="";
	setResizable(false);
	setTitle("SHARE SEARCH FILE");	
	setSize(650,650);	
	setLocation(300,300);

	searchTable = new SearchTable(new Vector());

	mainPanel = new JPanel(new BorderLayout());
	northPanel = new JPanel(new GridLayout(3,1));
	southPanel = new JPanel(new GridLayout(2,1));

	titlePane = new JPanel();
	searchPane = new JPanel();
	resultPane = new JPanel(new BorderLayout());
	downloadPane = new JPanel();
	closePane = new JPanel();
	searchTextField = new JTextField(10);
	//groupTextField = new JTextField(10);
	searchButton = new JButton("search");
	closeButton = new JButton("close");
	downloadButton = new JButton("download");
	stopButton = new JButton("stop research");
	stopButton.setEnabled(false);

	searchButton.addActionListener(this);
	closeButton.addActionListener(this);
	downloadButton.addActionListener(this);
	stopButton.addActionListener(this);

	titlePane.add(new JLabel("=== SHARE SEARCH FILE ==="));
	searchPane.add(new JLabel("file "));
	searchPane.add(searchTextField);
	//searchPane.add(new JLabel(" group "));
	//searchPane.add(groupTextField);
	searchPane.add(searchButton);
	resultPane.add(searchTable);
	downloadPane.add(downloadButton);
	downloadPane.add(stopButton);
	closePane.add(closeButton);

	northPanel.add(titlePane);
	northPanel.add(searchPane);
	northPanel.add(new JLabel("  "));

	southPanel.add(downloadPane);
	southPanel.add(closePane);

	mainPanel.add(northPanel,BorderLayout.NORTH);
	mainPanel.add(resultPane,BorderLayout.CENTER);
	mainPanel.add(southPanel,BorderLayout.SOUTH);
	mainPanel.add(new JLabel("  "),BorderLayout.EAST);
	mainPanel.add(new JLabel("  "),BorderLayout.WEST);

	getContentPane().add(mainPanel);

	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e){
		    shareAgent.killAgent(ssfa);
		    setVisible(false);
		    dispose();
		}
	    });
    }

    public void display(){
	setVisible(true);
    }	

    public void actionPerformed(ActionEvent e){
	if(e.getSource() == searchButton){
	    fileName = searchTextField.getText();
	    searchButton.setEnabled(false);
	    stopButton.setEnabled(true);
	    searchButton_action(fileName);
	}
	if(e.getSource() == closeButton){
	    shareAgent.killAgent(ssfa);
	    setVisible(false);
	    dispose();
	}
	if(e.getSource() == downloadButton){
	    downloadButton_action();
	}
	if(e.getSource() == stopButton){
	    shareAgent.killAgent(ssfa);
	    searchButton.setEnabled(true);
	    stopButton.setEnabled(false);
	}
    }
  
    public void searchButton_action(String fileName){
	if(fileName.equals("")){
	    JOptionPane.showMessageDialog(this,
					  "Please, enter a name file.",
					  "File Name",
					  JOptionPane.ERROR_MESSAGE);
	    searchButton.setEnabled(true);
	    stopButton.setEnabled(false);
	}
	else{
	    updateResultTable(new Vector());
	    ssfa = new SmallSearchFileAgent(shareAgent,this);
	    shareAgent.launchAgent(ssfa,"smallSearchFileAgent"+fileName,false);
	}
    }

    public void downloadButton_action(){
	if(searchTable.getSelectedRow() != -1){
	    AgentAddress address = ((SearchedFile)((searchTable.getVector()).elementAt(searchTable.getSelectedRow()))).getServerAddress();
	    String virtualPath = ((SearchedFile)((searchTable.getVector()).elementAt(searchTable.getSelectedRow()))).getVirtualPath();
	    shareAgent.sendMessage( address,new FicMessage(virtualPath));
	}	
    }

    public void updateResultTable(Vector v){
	resultPane.removeAll();
	searchTable = new SearchTable(v);
	resultPane.add(searchTable);
	resultPane.validate();
	mainPanel.validate();
    }

}
