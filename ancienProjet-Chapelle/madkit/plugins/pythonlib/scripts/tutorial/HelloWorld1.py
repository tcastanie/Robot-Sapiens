# HelloWorld.py - A HelloWorldAgent in Python.
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

class HelloWorld1(Agent):
  def activate(self):
    self.println("Hello World")
    
        
  def live(self):
    n = 6
    while (n > 0) :
        self.println('I am OK : '+str(n))
        self.pause(1000)
        n = n-1

  def end(self):
    self.println('I am dead, arghhh')
            
a = HelloWorld1()
launch(a,"toto")  


