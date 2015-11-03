/*
* BeeEnvironment.java - DynamicBees, a demo for the probe and watcher mechanisms
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

import madkit.kernel.ReferenceableAgent;
import madkit.kernel.Watcher;
/**
  @version 2.0
  @author Fabien MICHEL 01/02/2001*/
public class BeeEnvironment extends Watcher implements ReferenceableAgent
{
    int screenWidth=300;
    int screenHeight=300;
    int queenMaxAcc;
    int queenMaxVel;
    int beeMaxAcc;
    int beeMaxVel;
    BeeEnumerator beeEnumerator;
    QueenBeeEnumerator queenEnumerator;

    public void setWidth (int add){screenWidth = add;}
    public int getWidth(){return screenWidth;}
    public void setHeight (int add){screenHeight = add;}
    public int getHeight(){return screenHeight;}
    public void setQueenAcceleration (int add){queenMaxAcc = add;}
    public int getQueenAcceleration(){return queenMaxAcc;}
    public void setQueenVelocity (int add){queenMaxVel = add;}
    public int getQueenVelocity(){return queenMaxVel;}
    public void setBeeAcceleration(int add){beeMaxAcc = add;}
    public int getBeeAcceleration(){return beeMaxAcc;}
    public void setBeeVelocity (int add){beeMaxVel = add;}
    public int getBeeVelocity(){return beeMaxVel;}
    

public BeeEnvironment()
{
    queenMaxAcc=5;
    queenMaxVel=12;
    beeMaxAcc=3;
    beeMaxVel=9;
}
public BeeEnvironment(int width,int height)
{
    this();	
    screenWidth=width;
    screenHeight=height;
}

public void updateQueenBees(QueenBee[] qb)
{
	for (int i=0;i<qb.length;i++)
		if (qb[i].beeWorld != this)
			qb[i].setEnvironment(this);
	if(qb.length>0)
	{
		Bee[] bees = beeEnumerator.getBees();
		for (int i=0;i<bees.length;i++)
			bees[i].setQueenBee( qb[((int) (Math.random()*qb.length))] );
	}
}

public void updateBees(Bee[] b)
{
	QueenBee[] qb = queenEnumerator.getQueenBees();
	for (int i=0;i<b.length;i++)
		if (b[i].beeWorld != this)
		{
			b[i].setEnvironment(this);
			if (qb.length>0)
				b[i].setQueenBee( qb[((int) (Math.random()*qb.length))] );
		}
}

public void activate()
{
	createGroup(false,"bees",null,null);
	requestRole("bees","bee environment");
	beeEnumerator=new BeeEnumerator("bees","bee",this);
    	queenEnumerator=new QueenBeeEnumerator("bees","queen bee",this);
	addProbe(queenEnumerator);
	addProbe(beeEnumerator);
}

public void end()
{
	disposeMyGUI();
   	super.end();
}
	

}
