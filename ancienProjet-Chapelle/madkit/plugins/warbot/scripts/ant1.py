# Ant.py - An ant like brain of a Warbot robot, in Python.
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

# Note: this agent shows some properties in a Panel.


from javax.swing import JButton, JPanel, JLabel, JFrame
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

def makeAttrPanel(key,value):
	makeGrid(1,2,JLabel(key),JLabel(value))

class PythonAgentDisplay(JFrame):
	headingPanel = JLabel('0')
	actionPanel=JLabel('Nothing')
	perceptsPanel=JLabel('')
	energyPanel=JLabel('0')
	myAgent=None

	dispAttr=makeGrid(4,2,\
		JLabel('heading'),headingPanel,\
		JLabel('energy'),energyPanel,\
		JLabel('action'),actionPanel,\
		JLabel('percepts'),perceptsPanel)

	def updateData(self,a,p):
		self.headingPanel.setText(str(self.myAgent.getHeading()))
		self.energyPanel.setText(str(self.myAgent.getEnergyLevel()))
		self.actionPanel.setText(a)
		if (p != ''):
			self.perceptsPanel.setText(p)

	def __init__(self,thisAgent):
		self.setTitle("PythonAgent")
		self.myAgent=thisAgent
		self.getContentPane().setLayout(BorderLayout())
		self.getContentPane().add("Center",self.dispAttr)
		self.pack()
		self.show()


display = PythonAgentDisplay(self)



def activate():
	print 'hello'
	self.randomHeading()
	display.updateData('Nothing','')

def end():
	display.dispose()

def doIt():
	action='nothing'
	if not self.isMoving():
		self.randomHeading()
	#print 'heading..', self.getHeading()
	percepts = self.getPercepts()
	#lst = [x.getPerceptType() for x in percepts]
	#print 'percepts=',lst, 'nb:', len(percepts)
	if len(percepts)>0:
		pmin = percepts[0]
		for p in percepts[1:]: #find the closest Hamburger... Miam
			if (self.distanceTo(p) <= self.distanceTo(pmin)) and\
				(p.getPerceptType()=='Food'):
				pmin=p
		if pmin.getPerceptType()=='Food':
			if self.distanceTo(pmin)<2: 	#if close enough
				self.eat(pmin)				#eat it
				display.updateData('Eating',pmin.getPerceptType())
				return
			else:							#else go towards it
				self.setHeading(self.towards(pmin.getX(),pmin.getY()))
				self.move()
				display.updateData('Miam',pmin.getPerceptType())
				return
		else:
			display.updateData('Moving',pmin.getPerceptType())
	self.move()	#move if do not eat
	display.updateData('Moving','')
		
	
