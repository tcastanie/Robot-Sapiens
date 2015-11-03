# LaunchGuiLayout2.py - A script to show how several agents may be seen together in a
#					   same window.
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
from agents.toolagents import AgentGuiLayout, JavaLauncherPanel, PythonLauncherPanel
from agents.marketorg import Broker, Client, Provider
from madkit.lib.messages import StringMessage
from javax.swing import JButton, JPanel
from java.awt import FlowLayout, GridLayout, BorderLayout
    
def makeFlow(*elts):
    p = JPanel()
    p.setLayout(FlowLayout())
    for b in elts:
        p.add(b)
    return p

def makeGrid(i,j,*elts):
    p = JPanel()
    p.setLayout(GridLayout(i,j))
    for b in elts:
        p.add(b)
    return p

def makeBorder(el):
    p = JPanel()
    p.setLayout(BorderLayout())
    p.add("Center",el)
    return p



class TestLayout(AgentGuiLayout):
	def activate(self):
	    print "Display gui layout"
	    if not self.isGroup("test"):
	        self.createGroup(1,"test",None,None)
	    self.requestRole("test","bigMember",None)


	def initGUI(self):
		disp = makeBorder(JavaLauncherPanel(self,'agents.python.EditPythonAgent', 'Python Editor'))
		d2=makeGrid(1,0,PythonLauncherPanel(self,'scripts/pythonfiles/PingPong.py','ping',1),\
				JavaLauncherPanel(self,'agents.jess.JessPingPongAgent','pong',1),\
				JavaLauncherPanel(self,'agents.marketorg.Broker','Broker',1),\
				JavaLauncherPanel(self,'agents.marketorg.Client','Client',1))
		disp.add("North",d2)	
		d3=makeGrid(1,0,JavaLauncherPanel(self,'agents.marketorg.Provider','One',1),\
				JavaLauncherPanel(self,'agents.marketorg.Provider','Two',1),\
				JavaLauncherPanel(self,'agents.marketorg.Provider','Three',1),\
				JavaLauncherPanel(self,'agents.marketorg.Provider','Four',1))
		disp.add("South",d3)	
		self.setGUIObject(disp)
        
    
	def end(self):
	    print "Tcho boys..."

a = TestLayout()
launch(a,"Super visualisateur")
