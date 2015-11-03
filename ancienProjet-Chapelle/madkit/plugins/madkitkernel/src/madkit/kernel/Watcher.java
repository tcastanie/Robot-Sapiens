/*
* Watcher.java - Kernel: the kernel of MadKit
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

/** This class defines a generic watcher agent. It holds a collection of probes to explore agents' internal properties.

    @author Fabien Michel (MadKit 3.0 05/09/01) and 2.0 (Overlooker).
    @author Olivier Gutknecht version 1.0
    @since MadKit 2.0
    @version 3.0
*/

public abstract class Watcher extends AbstractAgent
{
	private Collection probes = new HashSet(7);
	
final synchronized public void addProbe(Probe p)
{
	if(currentKernel.addOverlooker(getAddress(), p, null))
		probes.add(p);
}

final synchronized public void addProbe(Probe p, Object accessCard)
{
	if(currentKernel.addOverlooker(getAddress(), p, accessCard))
		probes.add(p);
}
    
final synchronized public void removeProbe(Probe p)
{
	if(probes.remove(p))
		currentKernel.removeOverlooker(p);
}

final public Probe[] getProbes()
{
	return(Probe[]) probes.toArray(new Probe[0]);
}

final synchronized public void removeAllProbes()
{
	for (Iterator i = probes.iterator();i.hasNext();)
	{
		currentKernel.removeOverlooker((Overlooker) i.next());
		i.remove();
	}
}

public void end()
{
	removeAllProbes();
}

/** @deprecated since MadKit 2.1 : update is now useless as the Probe's agents variable is updated automatically*/
public void update(){}
}
