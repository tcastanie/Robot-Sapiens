/*
* PythonCommandCenter.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package turtlekit.kernel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import madkit.kernel.Activator;
import madkit.kernel.SynchroScheduler;
import madkit.kernel.Utils;
import madkit.simulation.activators.TurboMethodActivator;
import madkit.utils.agents.AbstractEditorPanel;

import org.python.core.PyException;
import org.python.util.PythonInterpreter;


/**
  *
  @author Fabien MICHEL
  @date 20/02/2002
  @version 1.1

*/

public class PythonCommandCenter extends SynchroScheduler implements ActionListener
{
	public AbstractEditorPanel display;
	protected PythonInterpreter interp;
	TurboMethodActivator viewersDoIt, updateDisplay;
	Activator turtles;
	String simulationGroup;
	final Map turtleThreads = new HashMap(),frames = new HashMap();

public PythonCommandCenter(String group)
{
    	try
    	{
    		Class python = Utils.loadClass("org.python.util.PythonInterpreter");
    		interp = (org.python.util.PythonInterpreter) python.newInstance();
		simulationGroup = group;
	}
	catch(Exception e)	{	}

}

public PythonInterpreter getInterpret(){return interp;}

public void println(String s)
{
	display.stdout().println(s);
}

public void initGUI()
{
	display = new CommandEditorPanel(this,getInterpret());
	setGUIObject(display);
}


public void activate()
{
	try
	{
		interp.exec("import sys");
		interp.set("self",this);
		interp.exec("def askTurtles(s):self.turtleMethod(s)");
		interp.exec("def crt(x):self.createTurtle(x)");
		interp.exec("def clearPatch():self.clearPatch()");
		interp.exec("def makeProcedure(p):self.makeProcedure(p)");
	}
	catch(PyException e)
	{
		println("Python error while activating");
	}
	println("Select a pyturtle script or type some turtle commands using python interpreter like this");
	println("for i in range (0,20):");
	println("\taskTurtles(\"fd(1)\")");
	viewersDoIt = new TurboMethodActivator("display",simulationGroup,"viewer");
	addActivator(viewersDoIt );
	updateDisplay = new TurboMethodActivator("displayOff",simulationGroup,"world");
	addActivator(updateDisplay);
	turtles = new Activator(simulationGroup,"turtle");
	addActivator(turtles);
	interp.set("allTurtles",turtles.getCurrentAgentsList());
	interp.exec("def clearT():\n\tfor i in allTurtles:\n\t\ti.die()");
	interp.exec("def clearAll():clearPatch(),clearT()");
	interp.exec("def addTurtle(t):self.addTurtle(t)");
	interp.exec("def killTurtle(x):\n\tfor i in allTurtles:\n\t\tif i.mySelf()==x:\n\t\t\ti.die()");
}

public void evaluation(String act, String content)
{
	//System.err.println("list = "+turtles.getCurrentAgentsList());
	interp.set("allTurtles",turtles.getCurrentAgentsList());
  	println("<< ControlMessage: " + act);
  	try
  	{
  		if (act.equals("dir"))
  			interp.exec("print dir()");
        	else if (act.equals("eval"))
        	{
        		//System.err.println("exec : "+content);
  			interp.exec(content);
			println("OK");
	    	}
  	}
  	catch (PyException re)
  	{
		println("Python error doing " + act);
		re.printStackTrace(System.err);
	}
}

public final void turtleMethod(String s)
{
     	//System.err.println("executing : "+s+" on "+turtles.getCurrentAgentsList());
	//Collection all = turtles.getCurrentAgentsList();
	String execution="";
	StringTokenizer st = new StringTokenizer(s);
	execution+=" i."+st.nextToken();
	while (st.hasMoreTokens())
	{
		execution+=", i."+st.nextToken();
	}
	//interp.set("allTurtles",turtles.getCurrentAgentsList());
	try
	{
		interp.exec("for i in allTurtles:\n\ttry:\n\t\t"+execution+"\n\texcept:\n\t\tprint i.toString() + \"can't do "+execution+"\"");
	}
	catch(Exception e)
	{
		System.err.println("python error "+e);
	}
	viewersDoIt.execute();
	updateDisplay.execute();
}

public void createTurtle(int x)
{
	TurtleEnvironment env = (TurtleEnvironment) updateDisplay.getAgentNb(0);
	for(int i = 0;i<x;i++)
	{
		Turtle tmp = new Turtle();
		env.addAgent(tmp);
		tmp.home();
		tmp.randomHeading();
		tmp. setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));
	}
	viewersDoIt.execute();
	interp.set("allTurtles",turtles.getCurrentAgentsList());
}

public void addTurtle(Turtle t)
{
	TurtleEnvironment env = (TurtleEnvironment) updateDisplay.getAgentNb(0);
	env.addAgent(t);
	t.setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));
	t.home();
	t.randomHeading();
	viewersDoIt.execute();
	interp.set("allTurtles",turtles.getCurrentAgentsList());
}

public void clearT()
{
	for(Iterator i = turtles.getAgentsIterator();i.hasNext();)
		((Turtle) i.next()).die();
}

public void clearPatch()
{
	TurtleEnvironment env = (TurtleEnvironment) updateDisplay.getAgentNb(0);
	Turtle tmp = new Turtle();
	env.addAgent(tmp,0,0);
	for(int i = 0;i < env.x;i++)
	{
		tmp.setX(i);
		for(int j = 0;j < env.y;j++)
		{
			tmp.setY(j);
			tmp.setPatchColor(Color.black);
		}
	}
	tmp.die();
}

public void makeProcedure(String s)
{
if(! frames.containsKey(s))
{
	final String s2 = s;
	JFrame	j = new JFrame(s+" procedure");
	frames.put(s,j);
	j.setLocation((int) (Math.random()*500),(int) (Math.random()*500));
	final JButton b = new JButton(s);
	j.addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			frames.remove(s2);
			ProcedureThread tThread = (ProcedureThread) turtleThreads.remove(s2);
			if(tThread != null)
				tThread.alive = false;
		}
	});
	j.setSize(300,100);
	j.getContentPane().add(b);
	j.getContentPane().setLayout(new GridLayout(2,1));
	b.addActionListener(this);
	final JCheckBox forever = new JCheckBox("Forever");
	//CheckBoxListener myListener = new CheckBoxListener();
	forever.addItemListener(new ItemListener(){
			    public void itemStateChanged(ItemEvent e) {
		       		if(e.getStateChange() == ItemEvent.SELECTED)
			        	b.setText(b.getText()+" forever");
			        else
			        {
			       		StringTokenizer st = new StringTokenizer(b.getText());
					b.setText(st.nextToken());
				}
			}}
		);
	j.getContentPane().add(forever);
	j.show();
}
}

public void actionPerformed(ActionEvent e)
{
	StringTokenizer st = new StringTokenizer(e.getActionCommand());
	JButton jb = (JButton) e.getSource();
	final String command = st.nextToken();
	if(turtleThreads.containsKey(command))
	{
		ProcedureThread tThread = (ProcedureThread) turtleThreads.remove(command);
		tThread.alive = false;
		jb.setBackground(Color.lightGray);
	}
	else if(st.hasMoreTokens())
	{
		if(! turtleThreads.containsKey(command))
		{
			ProcedureThread tThread = new ProcedureThread(command,this);
			turtleThreads.put(command, tThread);
			tThread.start();
			jb.setBackground(Color.green);
		}
		else
		{
			ProcedureThread tThread = (ProcedureThread) turtleThreads.remove(command);
			tThread.alive = false;
			jb.setBackground(Color.lightGray);
		}
	}
	else
	{	//evaluation("eval",e.getActionCommand());
		//interp.set("allTurtles",turtles.getCurrentAgentsList());
		ProcedureThread tThread = new ProcedureThread(command,this);
		tThread.start();
		tThread.alive = false;
	}
}

public void executeProcedure(String s)
{
	interp.set("allTurtles",turtles.getCurrentAgentsList());
	interp.exec(s+"()");
	//System.err.println("executing "+s+"!!!");
	/*viewersDoIt.execute();
	updateDisplay.execute();*/
}

class ProcedureThread extends Thread
{
	String procedure;
	boolean alive=true;
	PythonCommandCenter interp;

	public ProcedureThread(String s, PythonCommandCenter interp)
	{
		procedure = s;
		this.interp = interp;
	}

	public void run()
	{
		interp.executeProcedure(procedure);
		while(alive)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(Exception e){}
			interp.executeProcedure(procedure);
		}
	}
}

public void end()
{
	for(Iterator i = frames.values().iterator();i.hasNext();)
		((JFrame) i.next()).dispose();
	for(Iterator i = turtleThreads.values().iterator();i.hasNext();)
		((ProcedureThread) i.next()).alive=false;
	super.end();
}



}