/*
* Hamburger.java -Warbot: robots battles in MadKit
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

public class Hamburger extends Entity
{
    static protected ImageIcon hamburgerGif=null;

    public Hamburger(WarbotEnvironment theWorld,String name,int radius,int energy,int detectingRange)
    {
        super(theWorld,name,"",radius,energy,detectingRange);
    }

    public Hamburger(){}

    ImageIcon getImage()
    {
        return hamburgerGif;
    }

    void  createDefaultImage()
    {
        if(hamburgerGif==null)
            hamburgerGif = new ImageIcon(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("warbot/kernel/images/"+team+name+".gif")).getScaledInstance(radius*2,radius*2,Image.SCALE_SMOOTH));
    }

	public Percept makePercept(double dx, double dy, double d){
	   Percept p = new Food(this,dx,dy,d);
	   return p;
	}

    void doAction(){}

}
