/*
* RocketLauncher.java -Warbot: robots battles in MadKit
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

import java.awt.Point;


public class RocketLauncher extends BasicBody
{
	final static protected int maximumRocket = 50;
	protected int rocketNb=300;
	final protected static int SHOOT=10; //ACTION DESCRIPTION
	final protected static int BUILD_ROCKET=11; //ACTION DESCRIPTION
	private double rocketDirection=0;

public RocketLauncher(WarbotEnvironment env,Brain b,String team)
{
	super(env,b,"rocket launcher",team,15,8000,45);
	setSpeed(5);
}

public RocketLauncher()
{
    super();
	setDetectingRange(45);
	setSpeed(5);
	maximumEnergy=8000;
}

public Percept makePercept(double dx, double dy, double d){
	   Percept p = super.makePercept(dx,dy, d);
	   p.setPerceptType("RocketLauncher");
	   return p;
}

////////////////////////////////////     PARTIAL BODY INTERFACE 2 function
public void launchRocket(double direction)
{
	rocketDirection=direction;
	action=SHOOT;
}

public void buildRocket()
{
	action = BUILD_ROCKET;
}

public int getRocketNb(){ return rocketNb;}

int rocketWaitMax=3;
int rocketWait=rocketWaitMax;

//////////////////////////////////////  internal methods
void tryShoot()
{
	if((rocketNb>0) && (rocketWait <= 0))
	{
		rocketNb--;
		//Rocket r=new Rocket(myWorld,rocketDirection);

        getStructure().getAgent().doCommand(new SEdit.NewNodeCommand("Rocket",new Point(0,0)));
        Rocket r = (Rocket)((WarbotStructure)getStructure()).getLastEntity();
        r.setHeading(rocketDirection);
        r.setXY( (radius+r.getRadius()+1)*r.getCosAlpha()+x,(radius+r.getRadius()+1)*r.getSinAlpha()+y);

        rocketWait=rocketWaitMax; // waiting time to be able to shoot again
		//System.err.println(toString());
		/*System.err.println();
		System.err.println(toString());
		System.err.println("xmoi="+ x +" ymoi= "+y);
		System.err.println("x="+ ((radius*2+r.getRadius()+1)*r.getCosAlpha()+x)+" y= "+( (radius*2+r.getRadius()+1)*r.getSinAlpha()+y));
		System.err.println();*/
		//myWorld.addEntity(r);
	}
}

void tryBuildRocket()
{
	rocketNb++;
	if(rocketNb > maximumRocket)
		rocketNb = maximumRocket;
}

/////////////////////////////////	ACTION MECHANISM

void doIt()
{
	super.doIt();
    rocketWait--;
    if (rocketWait < 0) rocketWait=0;
}

void doAction()
{
    //if (getBrain() != null) getBrain().println("trying: " + ACTIONS[action]);
	switch(action)
	{
		case SHOOT:tryShoot();break;
  		case BUILD_ROCKET:tryBuildRocket();break;
        default:
        //System.out.println("try to do super : "+action);
        super.doAction();
	}
}

}
