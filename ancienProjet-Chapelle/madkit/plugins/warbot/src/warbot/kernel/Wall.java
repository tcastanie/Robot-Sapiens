/*
* Wall.java -Warbot: robots battles in MadKit
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


public class Wall extends Entity
{
//protected Image wallGif=null;// = (Toolkit.getDefaultToolkit().getImage("src/madkit/models/warbot/images/hamburger.gif")).getScaledInstance(10,10,Image.SCALE_SMOOTH);

    public Wall(WarbotEnvironment theWorld,String name,int radius,int energy)
    {
        super(theWorld,name,"",radius,energy,0);
        /*if (wallGif == null)
            /*wallGif= */createDefaultImage();//"src/madkit/models/warbot/images/"+team+name+".gif");*/

    }

    public Wall(){
		   super();
	}

	public Percept makePercept(double dx, double dy, double d){
	   Percept p = super.makePercept(dx,dy,d);
	   p.setPerceptType("Obstacle");
	   return p;
	}

    void doAction(){}

    void getMissileShot(int value) // walls cannot be desctructed...
	{
    }
}
