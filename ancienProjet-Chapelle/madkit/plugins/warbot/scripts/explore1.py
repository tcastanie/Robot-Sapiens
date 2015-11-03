# explore1.py - A Brain for simple Warbot Explorers, in Python.
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


groupName="warbot-"+self.getTeam()

def activate():
	print 'hello', groupName
	self.randomHeading()
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,"explorer",None)
	self.requestRole(groupName,"mobile",None)
	self.showUserMessage(1)

def end():
	print 'bye bye'

def doIt():
	action='nothing'
	if not self.isMoving():
		self.randomHeading()
	#print 'heading..', self.getHeading()
	percepts = self.getPercepts()
	#lst = [x.getPerceptType() for x in percepts]
	#print 'percepts=',lst, 'nb:', len(percepts)
	dist = self.getCoveredDistance()
	#self.setUserMessage('head:'+str(self.getHeading())+' dist:'+str(dist))
	self.setUserMessage(None)
	if len(percepts)>0:
		food = 0
		home = 0
		for p in percepts: #find the closest Hamburger... Miam
			if (not home and p.getPerceptType()=='Home' and p.getTeam()!=self.getTeam()):
				self.broadcast(groupName,"launcher","homeposition")
				self.setUserMessage('Enemy home!!')
				home = 1
			if (not food and p.getPerceptType()=='Food'):
				pmin=p
				food = 1
			if (p.getPerceptType()=='RocketLauncher' and p.getTeam()!=self.getTeam()):
				self.setUserMessage('Under attack..')
				self.broadcast(groupName,"launcher","help")
		if food:
			if self.distanceTo(pmin)<2: 	#if close enough
				self.eat(pmin)				#eat it
				return
			else:							#else go towards it
				self.setHeading(self.towards(pmin.getX(),pmin.getY()))
				self.move()
				return
	self.move()	#move if do not eat
		
	
