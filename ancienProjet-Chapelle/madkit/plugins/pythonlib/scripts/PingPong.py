#
# PingPong.py - The famous Ping-Pong agent (of Ol. Gutknecht) in its Python version.
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
import java.util

def activate():
    self.println("PythonAgent ping pong activated")
        # self.pause(1000)
    self.println("looking for a ping pong group")
    if self.isGroup("ping-pong"):
        self.println("Yeah I am coming")
        # self.joinGroup("ping-pong")
    else :
        self.println("I create a new group")
        self.createGroup(1,"ping-pong",None,None)
    self.requestRole("ping-pong","player")

def live():
    other = None
    self.println("looking for a ping pong partner")
    while (other == None) :
        v = self.getAgentsWithRole("ping-pong","player")
    # self.println("OK1")
        for x in v :
	        self.println(x.toString())
	        if not x.equals(self.getAddress()) :
	            other = x
        # self.println(v);
        self.pause(1000)
    self.println("other is : " + other.toString())
    self.sendMessage(other, StringMessage("Balle"))
    for y in range(0,5):
        ans = self.waitNextMessage()
        self.println("OK, my turn " + java.util.Date().toString())
        self.pause(1000)
        self.sendMessage(other, StringMessage(ans.getString()))

def end():
    self.println("PythonAgent ping pong ended : " + self.getName())


