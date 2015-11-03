package madkit.system;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.kernel.AgentAddress;

import madkit.utils.graphics.JTextAreaOutputStream;


class MadkitOutputGUI extends JPanel implements ActionListener{
	protected PrintStream out;
	protected JTextAreaOutputStream outStream;
	protected JButton clear, save;
	protected JTextArea outputArea;
	protected MadkitOutput myAgent;
	
	public MadkitOutputGUI(MadkitOutput _ag){
		super();
		myAgent = _ag;
		JScrollPane scrollArea = new JScrollPane();
		outputArea = new JTextArea(6,90);
		scrollArea.setSize(300,140);
		scrollArea.getViewport().add(outputArea);
		outStream = new JTextAreaOutputStream(outputArea);
		out = new PrintStream(outStream, true);
		
		System.setOut(out);
		System.setErr(out);
		
		setLayout(new BorderLayout());
		add(scrollArea, BorderLayout.CENTER);
		
		JPanel pane = new JPanel();
		save = new JButton("Save as...");
		save.addActionListener(this);
		pane.add(save);
		clear = new JButton("Clear output");
		clear.addActionListener(this);
		pane.add(clear);
		
		add(pane, BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		if (o == clear){
			outStream.clear();
		}
		if (o == save){
			JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
			fc.setDialogType(JFileChooser.SAVE_DIALOG);
			fc.setSelectedFile(new File("Output.txt"));
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try{
					FileWriter writer = new FileWriter(fc.getSelectedFile());
					writer.write(outputArea.getText());
					writer.flush();
				}catch(Exception ee){ ee.printStackTrace(System.err); }
			}
		}
	}
}

public class MadkitOutput extends Agent {

  MadkitOutputGUI gui;
  boolean living=true;
  
  public void activate(){
	  createGroup(true,"public","system",null,null);
	  AgentAddress[] aglst = getAgentsWithRole("public","system","output");
	  if (aglst.length > 0)
	  	living = false;
	  else
	  	requestRole("public","system","output",null);
  }
	
  public void initGUI(){
	  gui = new MadkitOutputGUI(this);
	  setGUIObject(gui);
  }

  public void live(){
	while(living){
	  Message m = waitNextMessage();
	}
  }

}