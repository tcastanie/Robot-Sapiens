/*
* BrainActivator.java -Warbot: robots battles in MadKit
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Activator;

class BrainActivator extends Activator
    {
        private List currentBodies;
        private List bodiesActing = new ArrayList();	//in case we want to shuffle it

    BrainActivator(String group)
    {
        super(group,"brain");
        currentBodies = new ArrayList();
    }

    public void initialize()
    {
        currentBodies.clear();
        for (Iterator i = getAgentsIterator();i.hasNext();)
            // JF Modifs : currentBodies.add(Proxy.getInvocationHandler( ((Brain)i.next()).getBody()) );
            currentBodies.add(((Brain)i.next()).getBody());
    }

    public void update(AbstractAgent theAgent,boolean added)
    {
        if(added)
            // JF Modifs: currentBodies.add( Proxy.getInvocationHandler( ((Brain)theAgent).getBody()) );
            currentBodies.add(((Brain)theAgent).getBody());
        else
            // JF Modifs: currentBodies.remove( Proxy.getInvocationHandler( ((Brain)theAgent).getBody()) );
            currentBodies.remove(((Brain)theAgent).getBody());
    }

    synchronized void executeBrains()
    {
        List bodiesActing = new ArrayList();	//in case we want to shuffle it
        //bodiesActing.clear();
        for(ListIterator i = currentBodies.listIterator();i.hasNext();)
        {
            BasicBody body = (BasicBody) i.next();
            if( body != null && body.getBrain() != null && body.willAct())
            {
                body.action = Entity.NULL;
				body.createPerception();
                body.getBrain().doIt();
				//body.doIt();
                bodiesActing.add(body);
            }
        }
        for(Iterator i = bodiesActing.iterator();i.hasNext();)
            ((Entity) i.next()).doIt();
    }

    synchronized void executeBodies()
    {
        for(Iterator i = bodiesActing.iterator();i.hasNext();)
            ((Entity) i.next()).doIt();
    }


}
