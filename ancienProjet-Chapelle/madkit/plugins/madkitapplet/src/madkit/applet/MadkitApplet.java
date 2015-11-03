/*
 * KernelApplet.java - Created on Jan 18, 2004
 * 
 * Copyright (C) 1998-2004 Fabien Michel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */



package madkit.applet;

import madkit.kernel.*;
import java.applet.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import java.io.*;

/** KernelApplet is the main MadKit starting point in an applet version of madkit MAS.

  @author Fabien Michel */
  
class KernelApplet extends AbstractMadkitBooter 
{
	public static String VERSION = "v 3.0 17/01/04";
	static String theName = "MadKit Applet " + VERSION;

	protected KernelApplet(MadkitApplet applet){
		super(true,false,null,null,false);
		applet.theKernel=this;
	}

 protected void init(boolean isg, boolean ipnumeric, String initFile, 
 					String ipaddress, boolean network){
        setBooter(this);

	  setProgress("Starting micro-kernel...");
      isGraphics = isg;
      if (ipaddress == null)
	  theKernel = new Kernel("Kernel",ipnumeric);
      else
	  theKernel = new Kernel("Kernel",true, ipaddress);
      // new graphic shell architecture
      if (isGraphics)
	  {
	      guis = new Hashtable();
	      theKernel.registerGUI(this);
	  }
   }
	
void launchAgent(String ag, String name)
{
	try
	{
	        Class agentclass = Class.forName(ag);
 		System.err.println(ag+" "+agentclass);
		Agent a = null;
		a = (Agent)(agentclass.newInstance());
		launchAgent(a,name);
	}
	catch (ClassNotFoundException ex)
	{
		System.err.println("Agent class < "+ag+" > does not exist");
	}
	catch (Exception ccex)
	{
		System.err.println("Agent launch exception:"+ccex);
	}
}

void launchAgent(Agent a, String name)
{
	theKernel.launchAgent(a,name,this,true);
	try
	{
		Thread.currentThread().sleep(50);
	}
	catch(Exception e)
	{
		System.err.println("can't make a pause "+e);
	}
}

}

public class MadkitApplet extends JApplet implements ActionListener
{
	JTextField nameField,agentField;
	KernelApplet theKernel;
	String[] agentsnames,agentsclasses;
	String projectName;
	JButton c;
	
public void init()
{
	agentsnames = getParameter("agentsnames").split(",");
	agentsclasses = getParameter("agentsclasses").split(",");
	projectName=getParameter("projectName");
	setSize(600,400);
	getContentPane().setBackground(Color.black);
	getContentPane().setLayout(new BorderLayout(10,10));

	JPanel launchArea = new JPanel();
	launchArea.setOpaque(true);
	JButton b;
	launchArea.setLayout(new BorderLayout());
	launchArea.setBackground(Color.white);

	JPanel infoPanel = new JPanel();
	infoPanel.setLayout(new GridLayout(5,1,2,2));
	JLabel tmp = new JLabel(projectName+" is using "+KernelApplet.theName,JLabel.CENTER);
	tmp.setBackground(Color.white);
	infoPanel.add( tmp);//new JLabel(KernelApplet.theName,JLabel.CENTER));
	infoPanel.add( new JLabel("quick launch menu OR an agent entry OR via a script",JLabel.CENTER));
	JPanel flowPanel = new JPanel();

	flowPanel.add(new JLabel("     Class name                              ",JLabel.CENTER));
	flowPanel.add(new JLabel("             Agent name              ",JLabel.CENTER));
	infoPanel.add(flowPanel);

	flowPanel = new JPanel();

	nameField = new JTextField (25);
	nameField.setText(agentsclasses[0]);
	flowPanel.add(nameField);

	agentField = new JTextField (10);
	agentField .setText(agentsnames[0]);
	flowPanel.add(agentField );

	flowPanel.add(b = new JButton("Launch agent"));
	infoPanel.add(flowPanel);


	flowPanel = new JPanel();
	flowPanel.add(new JLabel("     ",JLabel.CENTER));
	infoPanel.add(flowPanel);


	//launchArea.add("North", new JLabel(KernelApplet.theName,JLabel.CENTER));
	launchArea.add("Center", infoPanel);
	launchArea.add("South", c = new JButton("Launch the "+projectName+" Application script "));
	b.addActionListener(this);
	c.addActionListener(this);

	try
	{
		java.net.URL url = this.getClass().getResource("madkit_small.jpg");
		getContentPane().add("Center",new JLabel(new ImageIcon(getToolkit().getImage(url)),JLabel.CENTER));
	}
	catch (Exception e)
	{
		//System.err.println("Image access exception"+e);
	}

	getContentPane().add("South", launchArea);

	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;

	Container contentPane = getContentPane();

	//Create the menu bar.
	menuBar = new JMenuBar();
	setJMenuBar(menuBar);



	//Build the first menu.
	menu = new JMenu("Quick launch");
	menu.setMnemonic(KeyEvent.VK_L);
	menu.getAccessibleContext().setAccessibleDescription("Launch agents");
	menuBar.add(menu);

	//agents to launch
	submenu = new JMenu(projectName+ " project");
	submenu.setMnemonic(KeyEvent.VK_P);
	for(int i = 0;i<agentsnames.length;i++)
		submenu.add(createMenuItem(agentsnames[i], i,agentsclasses[i]));

	menu.add(submenu);
	menu.addSeparator();


	submenu = new JMenu("demo agents");
	submenu.setMnemonic(KeyEvent.VK_D);
	submenu.add(createMenuItem("PingPong", KeyEvent.VK_A, "madkit.demos.PingPong"));

	menu.add(submenu);

	menu.addSeparator();

	submenu = new JMenu("turtlekit");
	submenu.setMnemonic(KeyEvent.VK_T);
	submenu.add(createMenuItem("termites", KeyEvent.VK_G, "turtlekit.simulations.termites.TermiteLauncher"));
	submenu.add(createMenuItem("diffusion", KeyEvent.VK_H, "turtlekit.simulations.diffusion.DiffusionTest"));
	submenu.add(createMenuItem("game of life", KeyEvent.VK_I, "turtlekit.simulations.gameoflife.GameOfLife"));
	submenu.add(createMenuItem("gas experiment", KeyEvent.VK_J, "turtlekit.simulations.gas.GasExperiment"));
	submenu.add(createMenuItem("prey predator", KeyEvent.VK_K, "turtlekit.simulations.hunt.HuntLauncher"));
	submenu.add(createMenuItem("soccer", KeyEvent.VK_L, "turtlekit.simulations.soccer.Soccer"));
	submenu.add(createMenuItem("gravity", KeyEvent.VK_M, "turtlekit.simulations.gravity.Gravity"));
	submenu.add(createMenuItem("creation", KeyEvent.VK_O, "turtlekit.simulations.tests.Creation"));
	submenu.add(createMenuItem("mosquitoes", KeyEvent.VK_6, "turtlekit.simulations.tests.Mosquitoes"));
	submenu.add(createMenuItem("ovni", KeyEvent.VK_Q, "turtlekit.simulations.tests.OvniLauncher"));
	submenu.add(createMenuItem("walkers", KeyEvent.VK_R, "turtlekit.simulations.tests.Walkers"));
	submenu.add(createMenuItem("virus", KeyEvent.VK_U, "turtlekit.simulations.virus.Epidemic"));

	menu.add(submenu);

	menu.addSeparator();

	submenu = new JMenu("simulations");
	submenu.setMnemonic(KeyEvent.VK_S);
	submenu.add(createMenuItem("BeeLauncher", KeyEvent.VK_V,"dynamicbees.BeeLauncher"));

	menu.add(submenu);

	menu.addSeparator();

	submenu = new JMenu("System agents");
	submenu.setMnemonic(KeyEvent.VK_D);

	//submenu.add(createMenuItem("AgentLister", KeyEvent.VK_W, "madkit.system.AgentLister"));
	//submenu.add(createMenuItem("EditorAgent", KeyEvent.VK_X, "madkit.system.EditorAgent"));
	submenu.add(createMenuItem("GroupObserver", KeyEvent.VK_Y, "madkit.system.GroupObserver"));
	//submenu.add(createMenuItem("MinimalChatAgent", KeyEvent.VK_Z,"madkit.system.MinimalChatAgent"));

	menu.add(submenu);

	menu.addSeparator();

	submenu = new JMenu("travel agents");
	submenu.setMnemonic(KeyEvent.VK_E);

	submenu.add(createMenuItem("Broker", KeyEvent.VK_1, "marketorg.Broker"));
	submenu.add(createMenuItem("Client", KeyEvent.VK_2, "marketorg.Client"));
	submenu.add(createMenuItem("Provider", KeyEvent.VK_3, "marketorg.Provider"));

	menu.add(submenu);

	menu.addSeparator();

	submenu = new JMenu("network");
	submenu.setMnemonic(KeyEvent.VK_N);

	submenu.add(createMenuItem("Communicator", KeyEvent.VK_4, "madkit.netcomm.NetAgent"));
	//submenu.add(createMenuItem("OutputRerouter", KeyEvent.VK_5, "madkit.system.OutputRerouter"));
	//submenu.add(createMenuItem("Migrator", KeyEvent.VK_5, "agents.network.demo.Migrator"));
	//submenu.add(createMenuItem("SuperPingPong", KeyEvent.VK_6, "agents.network.demo.SuperPingPong"));
	//submenu.add(createMenuItem("Ball", KeyEvent.VK_7, "agents.network.demo.Ball"));

	menu.add(submenu);
	//theKernel.launchAgent("madkit.desktop2.DesktopAgent","agentName");


}

/*    public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());

    }*/

    public JMenuItem createMenuItem(String title, int key, String description)
    {
    	JMenuItem menuItem = new JMenuItem(title, key);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(description);
        menuItem.addActionListener(this);
        return menuItem;
    }

public void actionPerformed(ActionEvent e)
{
	String className = nameField.getText();
	String agentName = agentField.getText();
	if(e.getSource() instanceof JMenuItem)
	{
		JMenuItem source = (JMenuItem)(e.getSource());
		className = source.getAccessibleContext().getAccessibleDescription();
		theKernel.launchAgent(className,source.getText());
	}
	else if(e.getSource() == c)
	{
		for(int i = 0;i<agentsnames.length;i++)
			theKernel.launchAgent(agentsclasses[i],agentsnames[i]);
	}
	else if (agentName.equals(""))
		theKernel.launchAgent(className,className);
	else
		theKernel.launchAgent(className,agentName);
}



  public void start()
    {
	new KernelApplet(this);
   }
}
