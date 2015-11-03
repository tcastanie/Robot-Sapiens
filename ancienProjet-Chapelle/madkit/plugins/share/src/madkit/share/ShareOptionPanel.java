package madkit.share;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


class ShareOptionPanel extends JFrame implements ActionListener{
    
    ShareAgentPanel shareAgentPanel;
    ShareAgent shareAgent;
    JPanel mainPanel;
    JPanel titlePane;
    JPanel updatePane;
    JPanel sizePane;
    JPanel timeoutPane;
    JPanel buttonPane;
    
    JLabel updateLabel;
    JLabel sizeLabel;
    JLabel timeoutLabel;
    
    JComboBox updateCombo;
    JComboBox timeoutCombo;
    JComboBox sizeCombo;
    
    JButton saveButton;
    JButton quitButton;
    
    public ShareOptionPanel(ShareAgentPanel s){
	shareAgentPanel=s;
	shareAgent = (ShareAgent)((ShareAgentPanel)shareAgentPanel).getAgent();
	setResizable(false);
	setTitle("SHARE SETTINGS");	
	setSize(400,300);	
	setLocation(300,300);

	String[] updateTab = {" yes "," no "};
	updateCombo = new JComboBox(updateTab);
	String[] sizeTab = {" 100Ko "," 200Ko "," 300Ko "," 400Ko "," 500Ko "," 1024Ko (1Mo)"," 2048Ko (2Mo)"," 4096Ko (4Mo)"};
	sizeCombo = new JComboBox(sizeTab); 
	String[] timeoutTab = {" 30s "," 1 min "," 1 min 30 s "," 2 min "," 2 min 30 s "," 3 min "," 3 min 30 s "," 4 min "," 4 min 30 s "," 5 min "};
	timeoutCombo = new JComboBox(timeoutTab);
	sizeCombo.setMaximumRowCount(4);
	timeoutCombo.setMaximumRowCount(4);
		
	mainPanel = new JPanel(new GridLayout(6,1));
	titlePane = new JPanel();
	updatePane = new JPanel();
	sizePane = new JPanel();
	timeoutPane = new JPanel();
	buttonPane = new JPanel();
	
	titlePane.add(new JLabel("=== SHARE SETTINGS ==="));
	updateLabel = new JLabel("Activate updating file mode :");
	sizeLabel = new JLabel("Choose default paquet size :");
	timeoutLabel = new JLabel("Choose default timeout :");
	
	updatePane.add(updateLabel);
	sizePane.add(sizeLabel);
	timeoutPane.add(timeoutLabel);
	updatePane.add(updateCombo);
	sizePane.add(sizeCombo);
	timeoutPane.add(timeoutCombo);
	
	saveButton = new JButton("SAVE");
	quitButton = new JButton("QUIT"); 
	saveButton.addActionListener(this);
	quitButton.addActionListener(this);
	buttonPane.add(saveButton);
	buttonPane.add(quitButton);
	
	mainPanel.add(titlePane);
	//mainPanel.add(updatePane);
	mainPanel.add(new JLabel(" "));
	mainPanel.add(sizePane);
	mainPanel.add(timeoutPane);
	mainPanel.add(new JLabel(" "));
	mainPanel.add(buttonPane);
	
	getContentPane().add(mainPanel);
	display();	
    } 
    
    public void display(){
	int timeout = shareAgent.getTimeout();
	int maxSize = shareAgent.getMaxSize();
	
	if(timeout==30) timeoutCombo.setSelectedIndex(0);
	if(timeout==60) timeoutCombo.setSelectedIndex(1);
	if(timeout==90) timeoutCombo.setSelectedIndex(2);
	if(timeout==120) timeoutCombo.setSelectedIndex(3);
	if(timeout==150) timeoutCombo.setSelectedIndex(4);
	if(timeout==180) timeoutCombo.setSelectedIndex(5);
	if(timeout==210) timeoutCombo.setSelectedIndex(6);
	if(timeout==240) timeoutCombo.setSelectedIndex(7);
	if(timeout==270) timeoutCombo.setSelectedIndex(8);
	if(timeout==300) timeoutCombo.setSelectedIndex(9);
	
	if(maxSize==1048576/10) sizeCombo.setSelectedIndex(0);		
	if(maxSize==(1048576/10)*2) sizeCombo.setSelectedIndex(1);	
	if(maxSize==(1048576/10)*3) sizeCombo.setSelectedIndex(2);	
	if(maxSize==(1048576/10)*4) sizeCombo.setSelectedIndex(3);	
	if(maxSize==1048576/2) sizeCombo.setSelectedIndex(4);	
	if(maxSize==1048576) sizeCombo.setSelectedIndex(5);	
	if(maxSize==1048576*2) sizeCombo.setSelectedIndex(6);	
	if(maxSize==1048576*4) sizeCombo.setSelectedIndex(7);	
	
	setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
	if(e.getSource() == saveButton){
	    String size = (String)sizeCombo.getSelectedItem();
	    if(size.equals(" 100Ko ")) shareAgent.setMaxSize(1048576/10);
	    if(size.equals(" 200Ko ")) shareAgent.setMaxSize((1048576/10)*2);
	    if(size.equals(" 300Ko ")) shareAgent.setMaxSize((1048576/10)*3);
	    if(size.equals(" 400Ko ")) shareAgent.setMaxSize((1048576/10)*4);
	    if(size.equals(" 500Ko ")) shareAgent.setMaxSize(1048576/2);
	    if(size.equals(" 1024Ko (1Mo)")) shareAgent.setMaxSize(1048576);
	    if(size.equals(" 2048Ko (2Mo)")) shareAgent.setMaxSize(1048576*2);
	    if(size.equals(" 4096Ko (4Mo)")) shareAgent.setMaxSize(1048576*4);
	    
	    String timeout = (String)timeoutCombo.getSelectedItem();
	    if(timeout.equals(" 30s ")) shareAgent.setTimeout(30);
	    if(timeout.equals(" 1 min ")) shareAgent.setTimeout(60);
	    if(timeout.equals(" 1 min 30 s ")) shareAgent.setTimeout(90);
	    if(timeout.equals(" 2 min ")) shareAgent.setTimeout(120);
	    if(timeout.equals(" 2 min 30 s ")) shareAgent.setTimeout(150);
	    if(timeout.equals(" 3 min ")) shareAgent.setTimeout(180);
	    if(timeout.equals(" 3 min 30 s ")) shareAgent.setTimeout(210);
	    if(timeout.equals(" 4 min ")) shareAgent.setTimeout(240);
	    if(timeout.equals(" 4 min 30 s ")) shareAgent.setTimeout(270);
	    if(timeout.equals(" 5 min ")) shareAgent.setTimeout(300);
	    
	    String settingsSharePath = shareAgent.madkitDirectory+File.separator+"share"+File.separator+"share.ini";
	    File f = new File(settingsSharePath);
	    try{
		f.createNewFile();
		FileOutputStream settings = new FileOutputStream (f);
		OutputStreamWriter w = new OutputStreamWriter(settings);
		w.write("timeout = "+shareAgent.getTimeout()+" ** "+"maxSize = "+shareAgent.getMaxSize()+" ** ");
		w.close();
		settings.close();
	    }
	    catch(IOException ioe){System.out.println("(Client) Can't write share.init");}
	    setVisible(false);
	}
	
	
	if(e.getSource() == quitButton){
	    setVisible(false);
	}
    }	
    
    public void close_action(ActionEvent e){
	setVisible(false);
    }
}
