# Warbot - Explorer - (C) 2002 - Jean Privat

from java.lang import Math
from java.util import Random

rnd = Random() # Generateur aleatoire

groupName="warbot-"+self.getTeam()


# La classe Mem stoque la Memoire de l'agent
class Mem:
  # Init
  def __init__(self):
    self.t = self.next = 0 # Temps, latence d'envoi de message
  # Traitement des messages
  def message(self, m):
    print "que veux-tu que je fasse de messages ?"
  # Envoi d'un message lie a un percept
  def send(self, agent, p):
    if self.next < self.t or self.next == self.t + 10:
      print 'send'
      agent.broadcast(groupName,'info',p.getPerceptType(),str(p.getX()),str(p.getY()))
      self.next = self.t + 10
  # Choix d'un cap a maintenir
  def go(self):
    self.but = rnd.nextDouble() * 360
    return self.but

# Creation de la memoire
mem = Mem()
#mem.t = 0
#mem.next = 0

def activate():
	print 'Explorer ', groupName
	self.setHeading(mem.go())
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,"explorer",None)

def end():
	print 'Explorer Mort'

def doIt():
	mem.t += 1

	if not self.isMoving():
	  self.setHeading(mem.go())

	percepts = self.getPercepts()
	# Vecteurs d'evitement
	aurax = auray = 0
	# nombre d'obstacles, explorers amis, rocketlauncher ennemis vus
	ob = expl = rl = 0
	# objectif choisi (seulement nouriture)
	pmin = 0
	for p in percepts:
	  d = self.distanceTo(p)+.0001
	  t = p.getPerceptType()
	  if d < 40:
	    aurax += p.getX()/d *(40-d)/150
	    auray += p.getY()/d *(40-d)/150
	  if p.getTeam() != self.getTeam():
	    if t=='Home' or t=='Explorer':
	      mem.send(self, p)
	    elif t=='RocketLauncher':
	      rl += 1
	      mem.send(self, p)
	      if d < 150:
		aurax += p.getX()/d *(150-d)/70
		auray += p.getY()/d *(150-d)/70
	    elif t=='Food' and (pmin==0 or d <= self.distanceTo(pmin)):
	      pmin=p
	    elif t=='Rocket':
	      if d < 150:
		aurax += p.getX()/d *(150-d)/150
		auray += p.getY()/d *(150-d)/150
	    elif t=='Obstacle':
	      ob += 1
	      if d < 150:
		aurax += p.getX()/d *(150-d)/150
		auray += p.getY()/d *(150-d)/150
	  elif t=='Explorer':
	    if d < 150:
	      aurax += p.getX()/d *(150-d)/150
	      auray += p.getY()/d *(150-d)/150
	      expl += 1

	# Gestion de la nouriture (non teste)
	if pmin != 0:
	  if self.distanceTo(pmin)<2:	  #if close enough
	    self.eat(pmin)			    #eat it
	    return
	  else: 						  #else go towards it
	    self.setHeading(self.towards(pmin.getX(),pmin.getY()))
	    self.move()
	    return

	# Calcul du vecteur de deplacement en fonction des vecteurs de force
	# et du cap a maintenir
	aurax = Math.cos(mem.but*Math.PI/180) - aurax
	auray = Math.sin(mem.but*Math.PI/180) - auray
	t = self.towards(aurax,auray)

	# Changement de l'objectif
	if ob > 3 or expl > 0 or rl > 2:
	  mem.but = t

	# Deplacement effectif
	self.setHeading(t)
	self.move()


