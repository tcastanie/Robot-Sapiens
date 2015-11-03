/*
* Rocket.java -Warbot: robots battles in MadKit
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

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Rocket extends MovableEntity
{
	protected static ImageIcon rocketGif=null;

    protected int power = 200;

	public Rocket(WarbotEnvironment env,double direction)
	{
		super(env,"rocket","",1,150,0);
		setSpeed(1);
		setHeading(direction);
		action = MOVE;
        //System.out.println("Rocket created..");
	}

	public Rocket()
	{
		setSpeed(1);
        setHeading(90);
		action = MOVE;
        setEnergy(150);
	}

	public Percept makePercept(double dx, double dy, double d){
	   Percept p = super.makePercept(dx,dy,d);
	   p.setPerceptType("Rocket");
	   return p;
	}

    ImageIcon getImage()
    {
        return rocketGif;
    }

    void  createDefaultImage()
    {
        if(rocketGif==null)
            rocketGif = new ImageIcon(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("warbot/kernel/images/"+team+name+".gif")).getScaledInstance(radius*2,radius*2,Image.SCALE_SMOOTH));
    }

    public void doIt(){
        super.doIt();
        if(energy > 0)
            energy--;
    }

    void doAction()
    {
        if(energy>0)
        {
            Entity e = myWorld.authorizeMove(this,newX(),newY());
            if (e!=null)
            {
                e.getMissileShot(power);
                doPhysicalMove();
                delete();
                return;
            }
            else
            {
                moving=true;
                doPhysicalMove();
            }
        }
        else
            delete();
    }


    void update() {
    }


    void getMissileShot(int value)
    {
        delete();
    }


}
