/*
* SynchroScheduler.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.kernel;



import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;



/** This class is the non threaded version of a Scheduler.
*  Don't forget to make it implementing the ReferenceableAgent interface if you plan to schedule it with activators.
*
*    @author Fabien MICHEL
*    @version 1.0 05/09/01
*    @since MadKit 3.1
*/



public abstract class SynchroScheduler extends AbstractAgent

{

	private Collection activators = new HashSet(7);

	

final synchronized protected void addActivator(Activator a)

{

	if(currentKernel.addOverlooker(getAddress(),a,null))

		activators.add(a);

}

    

final synchronized protected void addActivator(Activator a, Object accessCard)

{

	if(currentKernel.addOverlooker(getAddress(), a, accessCard))

		activators.add(a);

}

    

final synchronized protected void removeActivator(Activator a)

{

	if(activators.remove(a))

		currentKernel.removeOverlooker(a);

}



final protected Activator[] getActivators()

{

	return(Activator[]) activators.toArray(new Activator[0]);

}



final synchronized protected void removeAllActivators()

{

	for (Iterator i = activators.iterator();i.hasNext();)

	{

		currentKernel.removeOverlooker((Overlooker) i.next());

		i.remove();

	}

}



public void end()

{

	removeAllActivators();

}



}

