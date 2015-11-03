# Warbot - Base - (C) 2002 - Jean Privat

from java.lang import Math
from java.util import Random

rnd = Random()

groupName='warbot-'+self.getTeam()

class Mem:
  # Init
  def __init__(self):
    self.t = 0 # temps
    self.next = 0 # latence d'envoi de parole
    self.mmin = 0 # agent le plus proche
    self.dmin = 0 # sa proximite
    self.trouille = 0 # le facteur de trouille
    self.touche = 0 # touche ou non ?

  # Gestion des messages
  def message(self, m):
    x = float(m.getArg1()) + m.getFromX()
    y = float(m.getArg2()) + m.getFromY()
    d = Math.sqrt(x*x+y*y)
    a = m.getAct()
    if a=='Me':
      return 1/(d + 1)
    elif a=='RocketLauncher':
      if d>200 and d<400 and mem.trouille < mem.t:
	mem.trouille = mem.t

mem = Mem()
#mem.t = 0
#mem.next = 0
#mem.mmin = 0
#mem.dmin = 0
#mem.trouille = 0
#mem.touche = 0

def activate():
	print 'Base', groupName
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,'info',None)
	self.requestRole(groupName,'info',None)

def end():
	print 'Base morte'
	if mem.trouille > 0:
	  self.broadcast(groupName,'launcher','Next','0','0')

def doIt():
	mem.t += 1

	if self.getShot():
	  mem.touche += 1

	# Reception des messages et idenification du RL le plus proche
	while not self.isMessageBoxEmpty():
	  m = self.readMessage()
	  d = mem.message(m)
	  if d > mem.dmin:
	    mem.mmin = m.getSender()
	    mem.dmin = d
	  #print d,mem.dmin,mem.mmin

	if mem.t<mem.next:
	  return

	c = mem.touche # nombre de mechants
	t = 0 # nombre de gentils
	pmin = 0 # RL le plus proche
	percepts = self.getPercepts()
	for p in percepts:
	  if p.getPerceptType()=='RocketLauncher':
	    if p.getTeam()==self.getTeam():
	      t += 1
	    else:
	      self.broadcast(groupName,'info','RocketLauncher',str(p.getX()),str(p.getY()))
	      c += 1
	      mem.next = mem.t + 10
	      if pmin == 0 or self.distanceTo(p) < self.distanceTo(pmin):
		pmin = p
	  if p.getPerceptType()=='Explorer' and p.getTeam()!=self.getTeam():
	      self.broadcast(groupName,'info','Explorer',str(p.getX()),str(p.getY()))
	      mem.next = mem.t + 10

	if c>0:
	  if pmin == 0:
	    x = y = '0'
	  else:
	    x = str(pmin.getX())
	    y = str(pmin.getY())
	  if mem.trouille > 0:
	    mem.trouille = 0
	    self.broadcast(groupName,'launcher','Next','0','0')
	    c = 3
	  if c>1:
	    self.broadcast(groupName,'launcher','Help3',x,y)
	  else:
	    self.broadcast(groupName,'launcher','Help2',x,y)
	  mem.next = mem.t + 10
	elif t==0:
	  self.broadcast(groupName,'launcher','Ask','0','0')
	  if mem.mmin == 0:
	    if mem.trouille == 0:
	      mem.trouille = mem.t + 200
	  elif mem.t > mem.trouille + 200:
	    self.send(mem.mmin,'Help3','0','0')
	  elif mem.t > mem.trouille + 100:
	    self.send(mem.mmin,'Help2','0','0')
	  elif mem.t > mem.trouille:
	    self.send(mem.mmin,'Help','0','0')
	  mem.next = mem.t + 10
	elif mem.trouille > 0:
	  mem.trouille = 0
	  self.broadcast(groupName,'launcher','Next','0','0')
	mem.mmin = 0
	mem.dmin = 0
	mem.touche = 0
