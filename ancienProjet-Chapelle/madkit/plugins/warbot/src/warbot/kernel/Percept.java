/*
* Percept.java -Warbot: robots battles in MadKit
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
import madkit.kernel.AgentAddress;

/**In warbot, everything is only Percept for a Brain. the informations given by the Percept are all relative to the position
of Body of the Brain*/
public class Percept
{
	private Entity ent;
	int radius;
	double x,y;
	int energy=0;
	String perceptType="Entity";
	boolean moving=false;
	String team="";

	double distance=0.0;
    AgentAddress agent;


	Percept(Entity e,double xcor,double ycor, double d)
	{
		ent=e;
		x=xcor;
		y=ycor;
		distance=d;

		radius=e.getRadius();
		team =e.getTeam();
		energy=e.getEnergy();
		distance = d-radius;
	}

	void setEnergy(int v){energy = v;}
	void setMoving(boolean b){moving = b;}
    void setAgent(AgentAddress ag) {agent = ag;}

	protected Entity getEntity(){return ent;}
/**returns the relative distance to the perceived entity*/
	public double getDistance(){return distance;}

/**returns the relative x coordinate of the perceived entity*/
	public double getX(){return x;}
/**returns the relative y coordinate of the perceived entity*/
	public double getY(){return y;}
/**returns the radius of the perceived entity*/
	public int getRadius(){return radius;}
/**returns the team of the Percept detected (if any)*/
	public String getTeam(){  return team;}
/**returns the energy of the Percept detected (if any)*/
	public int getEnergy(){return energy;}
/**returns the AgentAddress of the perceived body (if any)*/
	public AgentAddress getAgent(){  return agent;}

/**returns a label which represents the type of the perceived entity
 */
   public String getPerceptType(){return perceptType;}
   public void setPerceptType(String s){perceptType = s;}
}
