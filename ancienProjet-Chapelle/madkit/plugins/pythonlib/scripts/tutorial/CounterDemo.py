# CounterAgent.py - An agent that counts, in Python.
#
# Copyright (C) 2002 Jacques Ferber
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

from madkit.kernel import Agent
from madkit.kernel import StringMessage

class Counter(Agent):
  def activate(self):
    self.println("je compte")
    self.createGroup(1,'demo.counter',None,None)
    self.requestRole('demo.counter','counter',None)
    
        
  def live(self):
    n = 0
    counting = 0
    while (1) : #never stops
        m = self.nextMessage()
        if (m == None):
            if counting:
                self.println('counting: '+str(n))
                n = n+1
            self.pause(1000)
        elif (m.getString()=='start'):
            counting=1
        elif (m.getString()=='stop'):
            counting=0
            

            
a = Counter()
launch(a,"count")  


