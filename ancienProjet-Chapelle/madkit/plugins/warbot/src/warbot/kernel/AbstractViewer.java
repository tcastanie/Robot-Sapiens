/*
* AbstractViewer.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import java.awt.Component;
import java.awt.Graphics;

import madkit.kernel.AbstractAgent;
import madkit.kernel.ReferenceableAgent;
import madkit.kernel.Watcher;
import madkit.simulation.probes.ReflexiveProbe;

abstract class AbstractViewer extends Watcher implements ReferenceableAgent
{
	private EnvironmentProbe envProbe;

	protected Environment2D world;
	protected Entity[] entities;
	protected Component onScreen;
	protected boolean show = true;
	protected boolean swing = false;
	protected String simulationGroup;


    int nEntities=0;
    public int getNumberEntities(){
        return nEntities;
    }

    int nBasicBodies=0;
    public int getNumberAgents(){
        return nBasicBodies;
    }

void setShow (boolean add){show = add;}
boolean getShow(){return show;}

AbstractViewer(String theGroup){simulationGroup=theGroup;}

abstract void paintEntities(Graphics g);
void updateWorldData(Environment2D env){world=env;}
abstract public void observe();

public void activate()
{
	if (! swing) ((AwtGridCanvas)onScreen).initialisation();
	envProbe=new EnvironmentProbe("world",simulationGroup,"environment",this);
	addProbe(envProbe, WarbotIdentifier.password);
	requestRole(simulationGroup,"observer", WarbotIdentifier.password);
}

protected void checkMail()
{
	if ( nextMessage() != null)
	    envProbe.initialize();
}


}

final class EnvironmentProbe extends ReflexiveProbe
{
	private AbstractViewer myViewer;

EnvironmentProbe(String property, String group, String role,AbstractViewer viewer)
{
	super(group,role,property);
	myViewer=viewer;
}

public void update(AbstractAgent theAgent, boolean added)
{
	super.update(theAgent,added);
	if(added)
		myViewer.updateWorldData((Environment2D)getObject(theAgent));
}

public void initialize()
{
	super.initialize();
	if(numberOfAgents()>0)
		myViewer.updateWorldData((Environment2D)getObject(getAgentNb(0)));
}

}
