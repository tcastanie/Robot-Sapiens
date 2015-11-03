# Warbot - Explorer - (C) 2002 - Jean Privat

from java.lang import Math
from java.util import Random
from warbot.BPV_team import Point
from warbot.BPV_team import Rocket
from warbot.BPV_team import Cible
from warbot.BPV_team import Compteur

rnd=Random()

class CibleObs:
	# Init
	def __init__(self, p):
		self.obj = p
		self.deja_vu = 0
	def getX(self):
		return self.obj.getX()
	def getY(self):
		return self.obj.getY()
	def getCoord(self):
		p = Point(self.getX(), self.getY())
		return p
	def distanceTo(self, p):
		x = self.obj.getX() - p.getX()
		y = self.obj.getY() - p.getY()
		return Math.sqrt(x*x+ y*y)
	def getEnergy(self):
		return self.obj.getEnergy()
	def getType(self):
		if self.obj != None:
			return self.obj.getPerceptType()
		return ''
	def setVuParAutre(self, b):
		self.deja_vu = b
	def getVuParAutre(self):
		return self.deja_vu


groupName="warbot-"+self.getTeam()
coordonnees = Point()
objectif = Point()
adr_objectif = None
base_ennemie = []
synchro = Compteur(5)
cibles_observees = []
moi = 0
launcher_vu = 0
liste_autres = []
liste_rockets = []

def activate():
	global moi
	print 'Explorer ', groupName
	self.randomHeading()
	moi = len(self.getAgentsWithRole(groupName, 'explorer'))
	self.broadcast(groupName, 'explorer', 'NUM', str(moi))
	definirRoles()
	
def definirRoles():
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,"explorer",None)
	self.requestRole(groupName, 'info', None)
	self.requestRole(groupName,'mobile',None)
	
def end():
	global moi
	print 'Explorer Mort'
	self.broadcast(groupName, 'explorer', 'FIN-NUM', str(moi))
	self.broadcast(groupName, 'launcher', 'FIN')
		

def monEtat():
	global liste_autres
	global cibles_observees
	
	for c in cibles_observees:
		if not c.getVuParAutre():
			args = []
			args.append(str(c.getX()))
			args.append(str(c.getY()))
			args.append(str(c.getEnergy()))
			if c.getType() == 'RocketLauncher':
				self.broadcast(groupName, 'mobile', 'HELP1', args)
			elif c.getType() == 'Explorer':
				self.broadcast(groupName, 'mobile', 'HELP0', args)

def baseEnnemieConnue(b):
	global base_ennemie
	trouve = 0
	for base in base_ennemie:
		if base.distanceTo(b) < 10:
			trouve = 1
	return trouve

def baseEnnemieSuppr(b) :
	global base_ennemie
	nouv_liste = []
	for base in base_ennemie:
		if base.distanceTo(b) > 10:
			nouv_liste.append(base)
	base_ennemie = nouv_liste

def ecartMin(p):
	if p.getPerceptType() == 'Obstacle':
		return 20
	elif p.getPerceptType() == 'Rocket':
		return 30
	elif p.getTeam() != self.getTeam():
		if p.getPerceptType() == 'RocketLauncher':
			return 120
		else:
			return 5
	elif p.getPerceptType() == 'Explorer' :
		return 154
	elif cibles_observees != []:
		return 12
	else:
		return 0

def estMonSuperieur(adr):
	global liste_autres
	trouve = 0
	i = 0
	nb_autres = len(liste_autres)
	while not trouve and i < nb_autres:
		trouve = liste_autres[i] == adr
		i = i + 1
	return trouve
	
def eviteObstacles(percepts):

	dist1 = dist2 = 500
	taille_robot = 20
	liste_obstacles = []
	for p in percepts:
		centre = Point()
		centre.setCoord(p.getX(), p.getY())
		liste_obstacles.append(centre)
		
		        			
	# on dessine 2 droite paralleles a la direction
	# qui partent des bords du robot -> d1 : y = 12 et d2 : y = -12
	# Dans nouveau repere : origine = self
	#                       rotation du repere de l'angle de direction courant
	direction = self.getHeading()
	angle = Math.PI * direction / 180
	t = Math.tan(angle)
	s = Math.sin(angle)
	c = Math.cos(angle)
		
	for p in liste_obstacles:
	
		# centre_x, centre_y : centre de l'obstacle dans le repere
		centre_x = (  p.getX() + t* p.getY()) / (c + s * t)
		centre_y = -p.getY()/c + t * centre_x

		# savoir quelle droite prendre
		if centre_x > 0:
			if centre_y >= 0 and centre_y <= 2*taille_robot:
				y = centre_y - taille_robot
				dist1 = min(dist1,-Math.sqrt(taille_robot*taille_robot - y*y) + centre_x)
			elif centre_y < 0 and centre_y >= -(2*taille_robot):
				y = centre_y + taille_robot
				dist2 = min(dist2,-Math.sqrt(taille_robot*taille_robot - y*y) + centre_x)

	if min(dist1, dist2) <= 100 and abs(dist1 - dist2) > 2:
		if dist1 < dist2:
			direction += 100/dist1
		else:
			direction -= 100/dist2
	
		self.setHeading(direction)	

	
def miseAJourMouvement():
	# Mouvement normal et mise a jour des coordonnees
	alpha = self.getHeading() * Math.PI / 180
	depl_x = 2*Math.cos(alpha)
	depl_y = 2*Math.sin(alpha)
	if not self.isMoving():
		self.randomHeading()
	else:
		coordonnees.setCoord(coordonnees.getX() + depl_x, coordonnees.getY() + depl_y)
	
	
def doIt():
	global moi
	global cibles_observees
	global launcher_vu
	global liste_rockets
	global adr_objectif
	
	miseAJourMouvement()
	
	
	# Phase de detection
	percepts = self.getPercepts()
	
	liste_obs = []
	
	# Detection des ennemis
	zero_cible = (cibles_observees == [])
	nouvelles_cibles = []
	cible_launcher = 0
	for p in percepts:
		# On trouve le QG ennemi
		if p.getPerceptType() == 'Home':
			liste_obs.append(p)
			if p.getTeam() != self.getTeam():
				b = Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY())
				if not baseEnnemieConnue(b):
					base_ennemie.append(b)
					self.broadcast(groupName, 'info', 'INFO-QG', str(p.getX()), str(p.getY()))
		# On entre dans la zone de detection d'un rocket-launcher
		elif p.getPerceptType() == 'RocketLauncher' and p.getTeam() != self.getTeam():
			if not launcher_vu:
				self.broadcast(groupName, 'explorer', 'LAUNCHER_VU')
			launcher_vu = 1
			cible_launcher = 1
			c = CibleObs( p)
			nouvelles_cibles.append(c)
		elif p.getPerceptType() == 'Explorer' and p.getTeam() != self.getTeam() :
			c = CibleObs( p)
			nouvelles_cibles.append(c)
			liste_obs.append(p)
		elif p.getPerceptType() == 'RocketLauncher' and p.getTeam() == self.getTeam():
			liste_obs.append(p)
	
	if adr_objectif != None:
		if coordonnees.distanceTo(objectif) < 110:# or cible_launcher:
			objectif.setConnu(0)
			self.send(adr_objectif, 'ROCKET-FIN')
			adr_objectif = None
		else:
			self.setHeading( self.towards(objectif.getX() - coordonnees.getX(), objectif.getY() - coordonnees.getY()))
	
		
	# Mise a jour des cibles vues par plusieurs explorateurs
	nb_vus_par_autre = 0
	longueur = len(cibles_observees)
	for c in nouvelles_cibles:
		pos = Point()
		pos.setCoord(c.getX(), c.getY())
		trouve = 0
		i = 0
		while not trouve and i < longueur:
			c_anc = cibles_observees[i]
			if c_anc.getVuParAutre() and pos.distanceTo(c_anc.getCoord()) < 10:
				trouve = 1
				c.setVuParAutre(c_anc.getVuParAutre() - 1)
				nb_vus_par_autre += 1
			i = i+1
	
	cibles_observees = nouvelles_cibles
	
	nb_ennemis = 0
	centre = Point()
	for c in cibles_observees :
		#print str(c.distanceTo(Point()))
		if nb_ennemis < 5 and c.getType() == 'RocketLauncher' and not c.getVuParAutre():
			mult = 130 - c.distanceTo(Point())
			centre.setCoord(centre.getX() + c.getX()*mult, centre.getY() +  c.getY()*mult)
			nb_ennemis += 1
		elif c.getType() == 'RocketLauncher':
			mult =  200- c.distanceTo(Point())
			centre.setCoord(centre.getX() + c.getX()*mult, centre.getY() +  c.getY()*mult)
			nb_ennemis += 1
	
	if synchro.pret():
		monEtat()
	elif zero_cible and cibles_observees != []:
		monEtat()
	synchro.decrementer()
	
	#self.setUserMessage(str(len(cibles_observees)))
		
	if nb_ennemis > 0:
		centre.setCoord(centre.getX()/nb_ennemis, centre.getY()/ nb_ennemis)
		repuls = 180
		self.setHeading(repuls + self.towards(centre.getX(),centre.getY()))
		
		
	# Evitement des obstacles divers
	evite = Point()
	evite_rockets = Point()
	nb_rockets = 0
	nb_obs = 0
	nb_murs = 0
	nouvelles_rockets = []
	#print ''
	for p in percepts:
		if p.getPerceptType() == 'Rocket':
			pos = Point()
			pos.setCoord(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY())
			c = Rocket(pos)
			nouvelles_rockets.append(c)
		ecart_min = ecartMin(p)
		d = self.distanceTo(p)
		if  d < ecart_min:
			if d > 0:
				mult = (ecart_min - d)/ecart_min
				evite.setCoord(evite.getX() - mult*p.getX()/d, evite.getY() - mult*p.getY()/d )
				nb_obs = nb_obs + 1
				if p.getPerceptType() == 'Obstacle':
					nb_murs = nb_murs + 1
	
	
	# Evitement des rockets en prenant en compte leur deplacement
	rockets = []
	for r in liste_rockets:
		lg = len(nouvelles_rockets)
		i = 0
		trouve = 0
		if r.getDejaVu():
			nouv_pos = r.getNextCoord()
			while not trouve and i < lg:
				r2 = nouvelles_rockets[i]
				if r2.getCoord().distanceTo(nouv_pos) < 0.5:
					r.setCoord(r2.getX(), r2.getY())
					rockets.append(r)
					trouve = 1
					nouvelles_rockets.remove(r2)
				else:
					i += 1
		else:
			while not trouve and i < lg:
				r2 = nouvelles_rockets[i]
				dst = r2.getCoord().distanceTo(r.getCoord())
				if abs(dst - 4) < 0.5:
					r.setCoord(r2.getX(), r2.getY())
					rockets.append(r)
					trouve = 1
					nouvelles_rockets.remove(r2)
				else:
					i += 1
	
	
	for r in rockets:
		s = (r.getNextCoord().getY() - r.getY())/4
		c = (r.getNextCoord().getX() - r.getX())/4
		t = s/c
		x = r.getX() - coordonnees.getX()
		y = r.getY() - coordonnees.getY()
		
		centre_x = ( x + t* y) / (c + s * t)
		centre_y = -y/c + t * centre_x
		centre = Point()
		centre.setCoord(x, y)
		distance = centre.distanceTo(Point())
		#Si je suis sur la trajectoire
		if distance < 80 and centre_x < 0 and abs(centre_y < 16):
			nb_rockets += 1
			mult = (80 - distance)/80
			if centre_y >= 0:
				evite_rockets.setCoord(evite_rockets.getX() + mult*y, evite_rockets.getY() - mult*x)
			else:
				evite_rockets.setCoord(evite_rockets.getX() - mult*y, evite_rockets.getY() + mult*x)
		#print str(int(centre_x*1000) / 1000), str(int(centre_y * 1000) / 1000)
		
	
	liste_rockets = rockets
	for r in nouvelles_rockets:
		liste_rockets.append(r)
	
	#print ''
	#for r in liste_rockets:
	#	print r.getX(), r.getY()
	
	eviteObstacles(liste_obs)
	
	if nb_rockets > 0:
		direct = self.getHeading()*Math.PI/ 180
		but_virtuel = Point()
		but_virtuel.setCoord(2*Math.cos(direct),2*Math.sin(direct))
		
		self.setHeading(self.towards(but_virtuel.getX() + evite_rockets.getX(),but_virtuel.getY() + evite_rockets.getY()))
	
	if nb_obs > 0:
		direct = self.getHeading()*Math.PI/ 180
		but_virtuel = Point()
		but_virtuel.setCoord(1*Math.cos(direct),1*Math.sin(direct))
		
		self.setHeading(self.towards(but_virtuel.getX() + evite.getX(),but_virtuel.getY() + evite.getY() ))
	
		
	
		
	# Reception des messages
	while not self.isMessageBoxEmpty():
		message = self.readMessage()
		# Message de position de la base
		if message.getAct() == 'POS':
			coordonnees.setCoord(-message.getFromX(), -message.getFromY())
		elif message.getAct() == 'INFO-QG':
			x_absolu = message.getFromX() + float(message.getArgN(1)) + coordonnees.getX()
			y_absolu = message.getFromY() + float(message.getArgN(2)) + coordonnees.getY()
			b = Point(x_absolu, y_absolu)
			if (not baseEnnemieConnue(b)):
				base_ennemie.append(b)
		elif message.getAct() == 'FIN-NUM' :
			if int(message.getArg1()) < moi :
				moi = moi - 1
		elif message.getAct() == 'NUM':
			liste_autres.append(message.getSender())
		elif message.getAct() == 'FIN-QG':
			x = message.getFromX() + float(message.getArg1()) + coordonnees.getX()
			y = message.getFromY() + float(message.getArg2()) + coordonnees.getY()
			b = Point(x,y)
			baseEnnemieSuppr(b)
		elif message.getAct() == 'LAUNCHER_VU':
			launcher_vu = 1
		elif message.getAct() == 'ROCKET-REQ':
			if adr_objectif == None and not cible_launcher:
				self.send(message.getSender(), 'ROCKET-OFFRE')
				adr_objectif = message.getSender()
				dest = Point()
				dest.setCoord(message.getFromX() + coordonnees.getX() + float(message.getArg1()), message.getFromY() + coordonnees.getY() + float(message.getArg2()))
				objectif.setCoord (dest.getX(), dest.getY())
		elif message.getAct() == 'ROCKET-OK' and adr_objectif == message.getSender():
			dest = Point()
			dest.setCoord(message.getFromX() + coordonnees.getX() + float(message.getArg1()), message.getFromY() + coordonnees.getY() + float(message.getArg2()))
			objectif.setCoord (dest.getX(), dest.getY())
		elif message.getAct() == 'ROCKET-NON':
			if adr_objectif == message.getSender():
				adr_objectif = None
		elif estMonSuperieur(message.getSender()) and (message.getAct() == 'HELP0' or message.getAct() == 'HELP1'):
			x =  message.getFromX()+ float(message.getArg1())
			y =  message.getFromY()+ float(message.getArg2())
			pos = Point()
			pos.setCoord(x, y)
			for c in cibles_observees:
				if pos.distanceTo(c.getCoord()) <= 10:
					c.setVuParAutre(6) # 6 tours avant fin ttl
			
	self.move()     			
	                
	
