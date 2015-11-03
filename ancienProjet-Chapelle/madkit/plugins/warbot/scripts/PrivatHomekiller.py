# Warbot - Launcher (C) 2002 - Jean Privat

from java.lang import Math
from java.util import Random

# Generateur aleatoire
rnd = Random()

# Un But est un objectif a ateindre
class But:
  # Init
  def __init__(self, tox, toy, ttl=0, pri=1):
    self.x = tox # abscisse du but
    self.y = toy # ordonnee d but
    self.ttl = ttl # duree de vie (time to live)
    self.pri = pri # priorite
    self.nb = 0 # nombre d'agents sur ce but

  # Distance entre un but et un point
  def distxy(self, ox, oy):
    return Math.sqrt(Math.pow(self.x-ox,2)+Math.pow(self.y-oy,2))

  # Distance entre deux buts
  def dist(self, but):
    return self.distxy(but.x, but.y)

  # Utilise pour l'affichage d'un but (debug)
  def str(self):
    if self.x=='abs':
      return '(abs ' + str(int(self.y)) + ')'
    else:
      return '(' + str(int(self.x)) + ',' + str(int(self.y)) + ' t' + str(int(self.ttl)) + ' p' + str(self.pri) + ' n' + str(self.nb)+ ')'

groupName="warbot-"+self.getTeam()
borneVie = 2000 # limite de lachete

class Mem:
  # Init
  def __init__(self, a):
    self.origin = But(0,0) # position par rapport a point de depart, donc (0,0) au debut
    self.buts = [] # liste de buts
    self.iMove = 0 # indicateur de volonte de deplacement
    self.wait = -1 # indicateur de latence de tir (1 coup tous les 3)
    self.t = self.next = self.fuite = 0 # temps, latence d'envoi de message, latence de fuite
    self.pbak = self.priminbak = 0 # ancien objectif et sa valeur
    self.agent = a # Reflexivite ;)

  # Ajout d'un but dans la liste
  def addBut(self, but):
    # Recherche d'un but proche
    f = 0 # but proche trouve ?
    for b in self.buts:
      if b.dist(but) < 50:
	# si trouve alors misa a jour
	f = 1
	if b.ttl < but.ttl:
	  b.x = but.x
	  b.y = but.y
	  b.ttl = but.ttl
	if b.pri < but.pri:
	  b.x = but.x
	  b.y = but.y
	  b.pri = but.pri
    if f == 0:
      # sinon ajout du but
      self.buts.append(but)

  # Choix du meilleur but
  def newBut(self):
    bmin = 0
    for b in self.buts:
      bd = b.dist(self.origin)/b.pri
      if b.nb == 0:
	bd *= 2
      elif b.nb <= 3:
	bd /= b.nb
      elif b.nb > 3:
	bd *= b.nb / 2
      if (bmin == 0 or cd>bd) and b.ttl > self.t:
	bmin = b
	cd = bd
    # si pas de meilleur but, alors continuer tout droit
    if bmin == 0:
      bmin = But('abs',self.toBut(self.current))
#    print bmin.str(), '-', self.current.str()
    # Si le but courant change
    if bmin != self.current:
      # avertir la liberation de l'ancien but
      if self.current.x != 'abs':
	self.agent.broadcast(groupName,"launcher",'Leave', str(self.current.x-self.origin.x),str(self.current.y-self.origin.y))
      # avertir la reservation du nouveau but
      if bmin.x != 'abs':
	self.agent.broadcast(groupName,"launcher",'Take', str(bmin.x-self.origin.x),str(bmin.y-self.origin.y))
      self.current = bmin

  # Choix d'un but aleatoire (direction aleatoire)
  def go(self):
    self.current = But('abs',rnd.nextDouble() * 360)
    return self.current.y

  # Supression d'un but (et de tous les vieux buts au passage)
  def delBut(self, x ,y):
    for b in self.buts[:]:
      if b.distxy(x,y)<4 or b.ttl<self.t:
	self.buts.remove(b)

  # Modification du nombre d'agents sur un but
  def takeBut(self, x, y, val):
    for b in self.buts:
      if b.distxy(x,y)<4:
	b.nb += val

  # Gestion des messages
  def message(self, m):
    x = float(m.getArg1()) + m.getFromX() + self.origin.x
    y = float(m.getArg2()) + m.getFromY() + self.origin.y
    a = m.getAct()
    if a=='Home':
      self.addBut(But(x,y,self.t+10000))
    elif a=='Explorer':
      self.addBut(But(x,y,self.t+15,0.3))
    elif a=='RocketLauncher':
      self.addBut(But(x,y,self.t+50,2))
    elif a=='Help':
      self.addBut(But(x,y,self.t+60,5))
    elif a=='Help2':
      self.addBut(But(x,y,self.t+60,20))
    elif a=='Help3':
      self.addBut(But(x,y,self.t+100,100))
    elif a=='Ask':
      mem.agent.send(m.getSender(),'Me','0','0')
    elif a=='Next':
      self.delBut(x,y)
    elif m.getSender() != self.agent.getAddress():
      if a=='Take':
	self.takeBut(x,y,1)
      elif a=='Leave':
	self.takeBut(x,y,-1)

  # Terminaison du but courant et choix d'un autre
  def nextBut(self):
    if self.current.x != 'abs':
      self.delBut(self.current.x, self.current.y)
    #if self.buts.count(self.current)>0:
    #  self.buts.remove(self.current)
    self.newBut()

  # Calcul de la direction d'un but
  def toBut(self, but):
    if but.x == 'abs':
      return but.y
    a = but.x - self.origin.x
    b = but.y - self.origin.y
    if a == 0 and b == 0:
      return rnd.nextDouble() * 360;
    if b < 0:
      return 180*Math.asin(a / Math.sqrt(Math.pow(a,2)+Math.pow(b,2)))/Math.PI+270
    else:
      return 180*Math.acos(a / Math.sqrt(Math.pow(a,2)+Math.pow(b,2)))/Math.PI

  # Envoi d'un message lie a un percept
  def send(self , p):
    if self.next < self.t or self.next == self.t + 10:
      self.agent.broadcast(groupName,'info',p.getPerceptType(), str(p.getX()),str(p.getY()))
      self.next = self.t + 10

mem = Mem(self)
#mem.origin = But(0,0)
#mem.buts = []
#mem.iMove = 0
#mem.wait = -1
#mem.t = mem.next = mem.fuite = 0
#mem.pbak = mem.priminbak = 0
#mem.agent = self

def activate():
	print 'Homekiller III', groupName
	self.setHeading(mem.go())
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,'launcher',None)
	self.requestRole(groupName,'info',None)

def end():
	print 'Homekiller III Mort'
	# Liberation du but courrant
	if mem.current.x != 'abs':
	  self.broadcast(groupName,"launcher",'Leave', str(mem.current.x-mem.origin.x),str(mem.current.y-mem.origin.y))

def doIt():
	mem.t += 1
	if mem.iMove:
	  if not self.isMoving():
	    self.setHeading(mem.go())
	  else:
	    # Calcul de la nouvelle position
	    mem.origin.x += Math.cos(self.getHeading()*Math.PI/180)*2
	    mem.origin.y += Math.sin(self.getHeading()*Math.PI/180)*2
	    #if mem.t % 15 == 0:
	      #print len(mem.buts),mem.origin.str(),'->',mem.current.str()
	mem.iMove = 0

	# Latence de tir
	if mem.wait>=0:
	  mem.wait = mem.wait - 1

	# Lecture des messages
	if not self.isMessageBoxEmpty():
	  while not self.isMessageBoxEmpty():
	    mem.message(self.readMessage())
	  if mem.fuite == 0:
	    mem.newBut()

	percepts = self.getPercepts()
	c = 1 # Coequipiers - Ennemis (rockerLauncher)
	pmin = primin = 0 # Objectif et sa valeur
	aurax = auray = 0 # Vecteurs d'evitement des mechants
	auraxt = aurayt = 0 # Vecteurs d'evitement des gentils (team)
	for p in percepts:
	  pri = 0 # Interet du percept courant
	  d = self.distanceTo(p)+0.0001
	  t = p.getPerceptType()
	  if p.getTeam()!=self.getTeam():
	    if t=='Home':
	      mem.send(p)
	      pri=1
	      if d < 80:
		aurax += p.getX()/d
		auray += p.getY()/d
	    elif t=='RocketLauncher':
	      mem.send(p)
	      pri=4
	      c -= 1
	      if d < 70 or mem.fuite > 0:
		aurax += p.getX()/d
		auray += p.getY()/d
	    elif t=='Explorer':
	      mem.send(p)
	      pri=3
	      if d < 70:
		aurax += p.getX()/d
		auray += p.getY()/d
	    elif t=='Food':
	      pri=2
	    # Mise a jour de l'objectif en fonction de la priorite et de son energie
	    if pri>0 and (pri>primin or (pri==primin and p.getEnergy() < pmin.getEnergy())):
	      pmin=p
	      primin=pri
	  else:
	    if d < 70:
	      auraxt += p.getX()/d *(70-d)/120
	      aurayt += p.getY()/d *(70-d)/120
	    if t=='RocketLauncher':
	      c += 1

	# Si pas d'objectif
	if pmin == 0:
	  mem.pbak = mem.priminbak = 0
	  # Si but atteind
	  if mem.current.x != 'abs' and mem.current.dist(mem.origin) < 70:
	    self.broadcast(groupName,"launcher","Next",str(mem.current.x-mem.origin.x),str(mem.current.y-mem.origin.y))
	    print "but ok ", mem.current.str()
	elif primin==2: #food (non teste)
	  mem.priminbak = primin
	  d = self.distanceTo(pmin)+0.0001
	  if d<2:     #if close enough
	    self.eat(pmin)			    #eat it
	    return
	  else: 						  #else go towards it
	    self.setHeading(self.towards(pmin.getX(),pmin.getY()))
	    self.move()
	    return
	else: # Tir sur auelaue chose
	  x = pmin.getX()
	  y = pmin.getY()
	  # Inference de tir (ou prediction a la louche)
	  if primin == mem.priminbak:
	    xx = x - mem.pbak.getX()
	    yy = y - mem.pbak.getY()
	    if xx*xx+yy*yy <= 50:
	      d = self.distanceTo(pmin)/25+0.0001
	      if primin == 4:
		d /= 4
	      x += xx*d*d
	      y += yy*d*d
	  mem.pbak = pmin
	  mem.priminbak = primin
	  if mem.wait<=0 and mem.fuite==0:
#	     self.broadcast(groupName,"launcher","Help",str(x),str(y))
	    self.launchRocket(self.towards(x,y))
	    mem.wait = 3
	    return

	# Construction des roquettes si nombre faible
	if self.getRocketNumber() < 50 and rnd.nextDouble()*50>self.getRocketNumber():
	  self.buildRocket()
	  return

	# Cas de fuite
	if self.getRocketNumber() < 10: # plus de balles
	  mem.fuite = mem.t + 30
	elif c < 0: # plein de mechant
	  mem.fuite = mem.t + 20
#	 elif self.getShot(): # touche
 #	   mem.fuite = mem.t + 10
	elif mem.fuite > 0 and mem.t > mem.fuite: # fuite finie
	    mem.fuite = 0
	    if self.getEnergyLevel() > borneVie:
	      mem.newBut()
	    else:
	      mem.nextBut()

	# Calcul du vecteur de but
	b = mem.toBut(mem.current)
	x = Math.cos(b*Math.PI/180)
	y = Math.sin(b*Math.PI/180)

	# les laches vont dans l'autre sens
	if mem.fuite > 0:
	  x = -x*2
	  y = -y*2

	# si du team est detecte
	if auraxt != 0 or aurayt != 0:
	  if mem.current.x == 'abs':
	    # si pas de but, changement de cap
	    mem.current.y = self.towards(x-auraxt,y-aurayt)
	  else:
	    # si la priorite est forte, on se sert les coudes
	    if mem.current.pri > 1:
	      x *= 3
	      y *= 3
	    # calcul de la nouvelle direction
	    b = self.towards(x-auraxt,y-aurayt)
	    x = Math.cos(b*Math.PI/180)
	    y = Math.sin(b*Math.PI/180)
	    #print 'b(',x,y,')	bb(',xx,yy,')  aura(',aurax,auray,')  t(',auraxt,aurayt,')'

	# les laches ont plus peur
	if mem.fuite > 0:
	  x = x/3
	  y = y/3

	# Deplacement effectif
	self.setHeading(self.towards(x-aurax,y-auray))
	self.move()
	mem.iMove = 1

