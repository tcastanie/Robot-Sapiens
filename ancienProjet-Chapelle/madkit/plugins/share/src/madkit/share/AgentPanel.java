package madkit.share;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import madkit.kernel.Agent;

/**
 * <p>Share Agent Interface </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>University of Montpellier II</p>
 * @authors A.Khammal , W.Abdallah , C.Marty , X.Lorca , L.Nivaggioni
 * @version 1.2
 **/

public class AgentPanel extends JRootPane {

    Agent ag;
    JPanel contentPane;
    JMenuBar jMenuBar;
    JMenu jMenuFile;
    JMenu jMenuHelp;
    JMenuItem jMenuFileExit;
    JMenuItem jMenuHelpAbout;

    public Agent getAgent(){
	return ag;
    }
    
    public void setAgent(Agent _ag){
	ag = _ag;
    }
    
    public AgentPanel(Agent _ag){
	ag = _ag;
	jMenuBar = new JMenuBar();
	jMenuFile = new JMenu();
	jMenuFileExit = new JMenuItem();
	jMenuHelp = new JMenu();
	jMenuHelpAbout = new JMenuItem();
	try {
	    jbInit();
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	contentPane = (JPanel) this.getContentPane();
	contentPane.setLayout(new BorderLayout());
	contentPane.setPreferredSize(new Dimension(600,400));
	contentPane.setBackground(Color.lightGray);
	jMenuFile.setText("File");
	jMenuFileExit.setText("Exit");
	jMenuFileExit.addActionListener(new AgentPanel_jMenuFileExit_ActionAdapter(this));
	jMenuHelp.setText("Help");
	jMenuHelpAbout.setText("About");
	jMenuHelpAbout.addActionListener(new AgentPanel_jMenuHelpAbout_ActionAdapter(this));
	jMenuBar.setActionMap(null);
	jMenuFile.add(jMenuFileExit);
	jMenuHelp.add(jMenuHelpAbout);
	jMenuBar.add(jMenuFile);
	jMenuBar.add(jMenuHelp);
	this.setJMenuBar(jMenuBar);
    }
    
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    	JOptionPane.showMessageDialog(this,"Madkit - Share \nUniversity of Montpellier II \nauthors A.Khammal , W.Abdallah , C.Marty , X.Lorca , L.Nivaggioni \nversion 2.5", "INFORMATION",JOptionPane.INFORMATION_MESSAGE);
    }
    
    //File | Exit action performed
    public void jMenuFileExit_actionPerformed(ActionEvent e) {
	System.exit(0);
    }
}

class AgentPanel_jMenuFileExit_ActionAdapter implements ActionListener {
    AgentPanel adaptee;
    
    AgentPanel_jMenuFileExit_ActionAdapter(AgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuFileExit_actionPerformed(e);
    }
}    
class AgentPanel_jMenuHelpAbout_ActionAdapter implements ActionListener {
    AgentPanel adaptee;
    
    AgentPanel_jMenuHelpAbout_ActionAdapter(AgentPanel adaptee) {
	this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
	adaptee.jMenuHelpAbout_actionPerformed(e);
    }
}  
