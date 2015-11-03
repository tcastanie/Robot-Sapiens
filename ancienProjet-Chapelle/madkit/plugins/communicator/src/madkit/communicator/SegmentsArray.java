/*
* SegmentsArray.java - Communicator: the connection module of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel, Thomas Cahuzac
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
package madkit.communicator;

import java.util.*;

class SegmentsArray extends java.lang.Thread
{
	private Hashtable receivedPackets;
	private Hashtable receptionDates;
	private TransportLayer agent;

	public SegmentsArray(TransportLayer pere)
	{
		agent = pere;
		receivedPackets = new Hashtable(agent.communicatorSize());
		receptionDates = new Hashtable(agent.communicatorSize());
	}

	public Vector get(Integer packetNumber)
	{
		return (Vector)receivedPackets.get(packetNumber);
	}

	public void remove(Integer packetNumber)
	{
		receivedPackets.remove(packetNumber);
		receptionDates.remove(packetNumber);
	}

	public void put(Integer packetNumber, Vector messageSegments)
	{
		receivedPackets.put(packetNumber, messageSegments);
		receptionDates.put(packetNumber, new Long(System.currentTimeMillis()));
	}

	public void run()
	{
		while (true)
		{
			try
			{
				sleep(agent.communicatorGarbage());
			}
			catch(InterruptedException e)
			{
				System.err.println("Problem when the segmentsArray has fallen asleep");
			}

			if (receivedPackets.size() != 0)
			{
				Integer theKey;
				Long theDate;

				for (Enumeration messageSegments = receivedPackets.keys(); messageSegments.hasMoreElements();)
				{
					theKey = (Integer)messageSegments.nextElement();
					theDate = (Long)receptionDates.get(theKey);
					if ((theDate.longValue() + agent.communicatorTime()) < System.currentTimeMillis())
					{
						remove(theKey);
						System.err.println("A Message segment has been lost : Message trashed");
					}
				}
			}
		}
	}
}
