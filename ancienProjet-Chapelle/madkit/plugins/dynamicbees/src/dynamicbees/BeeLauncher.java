/*
* BeeLauncher.java - DynamicBees, a demo for the probe and watcher mechanisms
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel
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
package dynamicbees;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Kernel;

/**
  @version 2.0
  @author Fabien MICHEL 01/02/2001*/
public class BeeLauncher extends madkit.kernel.Agent
{
	int delay = 9;
	int beenumber = 2000;
	int height=600, width=800;
	List agentsList = new ArrayList();
	List queensList = new ArrayList();
	List beesList = new ArrayList();
	boolean active=false;
	boolean withSwing=false;
	boolean randomMode=true;
	BeeScheduler beeScheduler;
	BeeEnvironment beeEnvironment;
	BeeWorldViewer beeViewer;
	


public BeeLauncher(){}

public void setWidth (int add){width = add; beeEnvironment.setWidth(add);}
public int getWidth(){return width;}
public void setHeight (int add){height = add; beeEnvironment.setHeight(add);} 
public int getHeight(){return height;}
public int getDelay() {return delay;}
public void setDelay(int  v)
{
	this.delay = v;
	if (beeScheduler!=null)
		beeScheduler.delay=delay;
}
public void setBeenumber(int  v) {this.beenumber = v;}
public int getBeenumber() {return beenumber;}
public boolean getActive() {return active;}
public void setActive(boolean  v) {this.active = v;}
public boolean getwithSwing() {return withSwing;}
public void setwithSwing(boolean  v) {this.withSwing = v;}

public synchronized void launchBees(int numberOfBees)
{
	for (int i =0;i<numberOfBees;i++)
	{
		Bee b = new Bee();
		beesList.add(b);
		if ((i+1)%5000==0) System.err.println(""+(i+1)+" bees launched");
		launchAgent(b,"Bee", false);
	}
}	

public synchronized void launchQueens(int numberOfQueens)
{
	for (int i =0;i<numberOfQueens;i++)
	{
		QueenBee qb = new QueenBee();
		queensList.add(qb);
		launchAgent(qb ,"queen bee",false);
		if ((i+1)%1000==0) System.err.println(""+(i+1)+" bees launched");
	}
}	

public synchronized void killBees(boolean queen,int number)
{
	List l;
	int j = 0;
	if(queen)
		l = queensList;
	else
		l = beesList;
	for(Iterator i = l.iterator();i.hasNext() && j < number;j++)
	{
		AbstractAgent a = (AbstractAgent) i.next();
		if(a != null)
		{
			i.remove();
			killAgent(a);
		}
		else
			break;
	}
}
		
		

public void activate()
{
	System.err.println("Launching simulation...");
	createGroup(true,"bees",null,null);
	requestRole("bees","launcher");
	while(active != true)
	{
		exitImmediatlyOnKill();
		pause(100);
	}
	QueenBee qb = new QueenBee();
	queensList.add(qb);
	launchAgent(qb ,"queen bee",true);
	//long timeL = System.currentTimeMillis();
	launchBees(beenumber);

	beeScheduler=new BeeScheduler(delay);
	agentsList.add(beeScheduler);
	beeEnvironment=new BeeEnvironment(width,height);
	agentsList.add(beeEnvironment);
	beeViewer=new BeeWorldViewer(withSwing,width,height);
	agentsList.add(beeViewer);
	
	//System.err.println("launching time : "+(System.currentTimeMillis()-timeL));
	launchAgent(beeEnvironment,"bee world",false);
	launchAgent(beeViewer,"viewer",true);
	launchAgent(beeScheduler,"bee scheduler",false);
	println("activated");
}

public void live()
{
	int speed=0;
	while(true)
	{
		exitImmediatlyOnKill();
		pause(500 + (int) (Math.random()*2000) );
		if(randomMode)
		{
			if(Math.random()<.8)
			{
				if(Math.random()<.5)
				{
					if(queensList.size()>1)
						if(queensList.size()>7)
							killBees(true, (int) (Math.random()*7) + 1);
						else
							killBees(true, (int) (Math.random()*2) + 1);
				}
				else if(queensList.size() < 10)
					launchQueens((int) (Math.random()*2) + 1);
			}
			else
				if(Math.random()<.5)
				{
					if(beesList.size() < 5000)
						launchBees((int) (Math.random()*25) + 1);
				}
				else
					killBees(false, (int) (Math.random()*25) + 1);
		}		
	}
} 

public void end()
{
	System.err.println("simulation ending");
	/*killAgent(beeViewer);
	killAgent(beeScheduler);
	killAgent(beeEnvironment);*/
	pause(200);
	int j=0;
	//long timeL = System.currentTimeMillis();
	for (Iterator i =agentsList.iterator(); i.hasNext();)
	{
		AbstractAgent a = (AbstractAgent) i.next();
		killAgent(a);
	}
	for (Iterator i =queensList.iterator(); i.hasNext();)
	{
		AbstractAgent a = (AbstractAgent) i.next();
		killAgent(a);
	}
	for (Iterator i =beesList.iterator(); i.hasNext();)
	{
		AbstractAgent a = (AbstractAgent) i.next();
		//if ((++j)%1000==0) System.out.println(""+j+" bees killed");
		killAgent(a);
	}
	//System.err.println("killing time : "+(System.currentTimeMillis()-timeL));
	pause(1000);
	Kernel.debugString();
	System.gc();
}
    
public void initGUI()
{
	setGUIObject(new BeeLauncherGUI(this));
}
    
      
}

class BeeLauncherGUI extends JPanel implements ActionListener
{
	JButton launchSimu,launchQueen,launchBee,launchViewer,randomMode;
	BeeLauncher myLauncher;
	
	BeeLauncherGUI(BeeLauncher myLauncher)
	{
		launchSimu = addNewButton("Launch simulation");
		randomMode = addNewButton("Random launch Off");
		launchQueen = addNewButton("Launch a queen");
		launchBee = addNewButton("Launch a bee");
		launchViewer = addNewButton("Launch a viewer");
		this.myLauncher = myLauncher;
		JSlider simulationSpeed = new JSlider(JSlider.HORIZONTAL, 0, 19 , 11);
		simulationSpeed.addChangeListener(new SliderListener());
		simulationSpeed.setMajorTickSpacing(5);
		simulationSpeed.setPaintTicks(true);
		simulationSpeed.setPaintLabels(false);
		simulationSpeed.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
		add(simulationSpeed);
	}
	
	public JButton addNewButton(String name)
	{
		JButton jb = new JButton(name);
		add(jb);
		jb.addActionListener(this);
		return jb;
	}
		
	
    public void actionPerformed(ActionEvent e)
    {
	Object s=e.getSource();
	if(s==launchSimu)
		myLauncher.setActive(true);
	else if(s==randomMode)
	{
		if(randomMode.getText().equals("Random launch Off"))
		{
			randomMode.setText("Random launch On");
			myLauncher.randomMode = false;
		}
		else
		{
			randomMode.setText("Random launch Off");
			myLauncher.randomMode = true;
		}
	}
	else if(s==launchQueen)
	{
		QueenBee qb = new QueenBee();
		myLauncher.queensList.add(qb);
		myLauncher.launchAgent(qb ,"queen bee",true);
	}
	else if(s==launchBee)
		myLauncher.launchBees(1);
	else if(s==launchViewer)
	{
		BeeWorldViewer v = new BeeWorldViewer(myLauncher.withSwing,myLauncher.width,myLauncher.height);
		myLauncher.agentsList.add(0,v);
		myLauncher.launchAgent(v,"viewer",true);
	}
    }			
	
    class SliderListener implements ChangeListener {
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
		myLauncher.setDelay( 20 - (int)source.getValue());
	    }
	}
    }

}

