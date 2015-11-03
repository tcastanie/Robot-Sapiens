# Warbot - Launcher (C) 2003 - Guillaume Verger
from java.util import Random
from java.util import Vector
from java.lang import Math
from warbot.BPV_team import Point
from warbot.BPV_team import Assistance
from warbot.BPV_team import Cible
from warbot.BPV_team import Compteur
from warbot.BPV_team import Mobile
from warbot.BPV_team import Rocket
from warbot.BPV_team import Groupe

rnd = Random()

AIDE_EXPLORER = 1               # un explorateur voit un explorateur ennemi
AIDE_ROCKET = 2                    # un robot voit un tir ennemi et essaie de trouver d"ou vient le tir
AIDE_BASE_ENNEMIE = 3      # la base ennemie est decouverte
AIDE_LAUNCHER = 4              # un explorateur voit un launcher ennemi
AIDE_ATTAQUE = 5                  # un launcher voit un launcher ennemi
AIDE_BASE_AMIE = 6              # la base voit un launcher ennemi
	
groupName="warbot-"+self.getTeam()
coordonnees = Point()
base_ennemie = []
attenteRoquettes = Compteur(4)
synchro = Compteur(2)
ennemi_courant = Cible()
aide = Assistance()
offre = None
mon_groupe = Groupe()
liste_rockets = []
tour = 0

def getDeplX():
	alpha = self.getHeading() * Math.PI / 180
	return self.getCoveredDistance() * Math.cos(alpha)
	
def getDeplY():
	alpha = self.getHeading() * Math.PI / 180
	return self.getCoveredDistance() * Math.sin(alpha)
	
def prioriteType(type_unite):
	if type_unite == "Home":
		return 1
	elif type_unite == "Explorer":
		return 0
	elif type_unite == "RocketLauncher":
		return 2
	else:
		return -1
	
def activate():
	print "Essai HomeCrusher ", groupName
	self.randomHeading()
	coordonnees.setCoord(0,0)
	definirRoles()
	
def definirRoles():
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,"launcher",None)
	self.requestRole(groupName,"info",None)
	self.requestRole(groupName, "mobile", None)
	
def end():
	print "Essai HomeCrusher Mort"
	self.broadcast(groupName, "launcher", "FIN")

def monEtat():
	if ennemi_courant.existe() and ennemi_courant.getType() == "RocketLauncher":
		args = []
		args.append(str(ennemi_courant.getX() - coordonnees.getX()))
		args.append(str(ennemi_courant.getY() - coordonnees.getY()))
		args.append(str(ennemi_courant.getEnergy()))
		self.broadcast(groupName, "launcher", "HELP2", args)

def ecartMin(p):
	if p.getPerceptType() == "Obstacle":
		return 30
	elif p.getPerceptType() == "Rocket":
		return 30
	elif p.getTeam() != self.getTeam():
		return 70
	elif p.getPerceptType() == "Explorer":
		return 30
	elif aide.besoin() and aide.getVisee().distanceTo(coordonnees) < 200:
		return 100
	else:
		return 90

# repond 1 si p est sur le trajet d"une rocket de self vers la cible
def gene(cible, p):
	taille = 12
	if p.getPerceptType() == "Home":
		taille = 20
	# Distance par rapport au tir
	angle = self.towards(cible.getX(), cible.getY())
	angle = Math.PI * angle / 180
	t = Math.tan(angle)
	s = Math.sin(angle)
	c = Math.cos(angle)
	
	dist_x = (  p.getX() + t* p.getY()) / (c + s * t)
	dist_y = -p.getY()/c + t * dist_x
	#print self.getAddress().getName() + " --> " + str(dist_x) + " --- " + str(dist_y)
	return abs(dist_y) < taille and dist_x > 0 and dist_x< cible.distanceTo(Point())
	
	
def eviteRockets(percepts):
	global coordonnees
	global liste_rockets
	
	evite_rockets = Point()
	nb_rockets = 0
	
	nouvelles_rockets = []
	for p in percepts:
		pos = Point()
		pos.setCoord(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY())
		c = Rocket(pos)
		nouvelles_rockets.append(c)

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
				if r2.getCoord().distanceTo(nouv_pos) < 0.0001:
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
				if abs(dst - 10) < 0.0001:
					#print "Distance : ", str(abs(dst - 10))
					r.setCoord(r2.getX(), r2.getY())
					rockets.append(r)
					trouve = 1
					nouvelles_rockets.remove(r2)
				else:
					i += 1
	
	
	plus_proche = 100
	roc = None
	cy = 0
	for r in rockets:
		s = (r.getNextCoord().getY() - r.getY())/10
		c = (r.getNextCoord().getX() - r.getX())/10
		if c != 0:
			c = 0.0000001
		t = s/c
		x = r.getX() - coordonnees.getX()
		y = r.getY() - coordonnees.getY()
		
		centre_x = -( x + t* y) / (c + s * t)
		centre_y = -y/c - t * centre_x
		centre = Point()
		centre.setCoord(x, y)
		distance = centre.distanceTo(Point())
		#Si je suis sur la trajectoire
		if (centre_x > 0) and abs(centre_y) < 14:
			nb_rockets += 1
			mult = (100 - distance)/100
			if centre_y >= 0:
				evite_rockets.setCoord(evite_rockets.getX() + mult*y, evite_rockets.getY() - mult*x)
			else:
				evite_rockets.setCoord(evite_rockets.getX() - mult*y, evite_rockets.getY() + mult*x)
			if (not r.getShootee()) and (plus_proche > centre_x):
				roc = r
				plus_proche = centre_x
		
			
	
	liste_rockets = rockets
	for r in nouvelles_rockets:
		liste_rockets.append(r)
	
	if nb_rockets > 0:
		direct = self.getHeading()*Math.PI/ 180
		but_virtuel = Point()
		but_virtuel.setCoord(2*Math.cos(direct),2*Math.sin(direct))
		
		self.setHeading(self.towards(but_virtuel.getX() + evite_rockets.getX(),but_virtuel.getY() + evite_rockets.getY()))
	
	if attenteRoquettes.pret() and roc !=None:
#		dist = 14
#		impact = 0
		dx = (roc.getNextCoord().getX() - roc.getX())/10
		dy = (roc.getNextCoord().getY() - roc.getY())/10
		vise = Point()
		#print "TIR:"
		a = 0
		b = 100
		while b - a > 1:
			m = int ((a + b) / 2)
			vise.setCoord(roc.getX() + m*dx,roc.getY() + m*dy)
			if coordonnees.distanceTo(vise) > m + 14:
				a = m
			else:
				b = m
		roc.setShootee(1)
		vise.setCoord(roc.getX() + m*dx,roc.getY() + m*dy)
		return vise
	else:
		return None

# Renvoie 1 si on a tiré
# 0 si on n"a pas tiré -> roquette pas prete ouquelqu"un entre robot et sa cible
def tirer(cible_abs): # cible est un Point()
	if not attenteRoquettes.pret():
		return 0
	cible = Point(cible_abs.getX() - coordonnees.getX(), cible_abs.getY() - coordonnees.getY())
	percepts = self.getPercepts()
	qqn_devant = 0
	i = 0
	nb_percepts = len(percepts)
	while not qqn_devant and i < nb_percepts:
		p = percepts[i]
		if p.getTeam() == self.getTeam():
			qqn_devant = gene(cible, p)
		i += 1
		
	if not qqn_devant:
		self.launchRocket(self.towards(cible.getX(), cible.getY()))
		attenteRoquettes.decrementer()
		return 1
	else:
		return 0
	
def baseEnnemieConnue(b) :
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
	
def eviteAmis(percepts):

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
	#                       rotation du repere de l"angle de direction courant
	direction = self.getHeading()
	angle = Math.PI * direction / 180
	t = Math.tan(angle)
	s = Math.sin(angle)
	c = Math.cos(angle)
		
	for p in liste_obstacles:
	
		# centre_x, centre_y : centre de l"obstacle dans le repere
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
		
def lireMessages():
	global offre

	# Lecture des messages
	liste_messages = [] # liste_messages = liste des messages de demande d"assistance
	while not self.isMessageBoxEmpty():
		message = self.readMessage()
		if message.getSender() != self.getAddress():
			if message.getAct() == "INFO-QG":
				x_absolu = message.getFromX() + float(message.getArgN(1)) + coordonnees.getX()
				y_absolu = message.getFromY() + float(message.getArgN(2)) + coordonnees.getY()
				b = Point(x_absolu, y_absolu)
				if (not baseEnnemieConnue(b)):
					base_ennemie.append(b)
			elif (message.getAct() == "FIN"):
				if (aide.getAdresseDemande() == message.getSender().getName()):
					aide.finAssistance()
					self.broadcast(groupName, "launcher", "ETAT")
					self.broadcast(groupName, "explorer", "ETAT")
				if offre == message.getSender():
					offre = None
			if message.getAct() == "ROCKET-FIN" and offre == message.getSender():
				offre = None
			elif (message.getAct() == "ETAT"):
				monEtat()
			elif message.getAct() == "FIN-QG":
				x = message.getFromX() + float(message.getArg1()) + coordonnees.getX()
				y = message.getFromY() + float(message.getArg2()) + coordonnees.getY()
				b = Point(x,y)
				baseEnnemieSuppr(b)
				aide.finAssistance()
			elif message.getAct() == "ROCKET-OFFRE":
				if offre == None:
					offre = message.getSender()
				elif offre != message.getSender():
					self.send(message.getSender(), "ROCKET-NON")
			else: # il ne reste que les messages de demande d"aide
				liste_messages.append(message)
			
	
	
	
	for message in liste_messages:
		act = message.getAct()
		if act == "HELP0" or act == "HELP1" or act == "HELP2" or act == "HELP3" or act == "ROCKET":
			x = coordonnees.getX() + message.getFromX()+ float(message.getArg1())
			y = coordonnees.getY() + message.getFromY()+ float(message.getArg2())
			energy = int(message.getArgN(3))
			if message.getAct() == "ROCKET":
				aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_ROCKET,3, tour)
			elif message.getAct() == "HELP0":
				if base_ennemie == []:
					aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_EXPLORER,3, tour)
			elif (message.getAct() == "HELP1"):
				aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_LAUNCHER,3, tour)
			elif message.getAct() == "HELP2":
				aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_ATTAQUE,3, tour)
			elif (message.getAct() == "HELP3"):
				aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_BASE_AMIE,3, tour)
			

	
def doIt():	
	global offre
	global tour
	tour = tour + 1
	attendre = 0
	coordonnees.setX(coordonnees.getX() + getDeplX())
	coordonnees.setY(coordonnees.getY() + getDeplY())
	
	# Time to live decremente
	aide.decrementer()
	
	if aide.besoin() and coordonnees.distanceTo(aide.getCoord()) <= 90 :
		aide.finAssistance()
	
	#Rechargement des roquettes?
	if not attenteRoquettes.pret():
		attenteRoquettes.decrementer()
	
	lireMessages()
	
	if synchro.pret():
		monEtat()
	synchro.decrementer()
	
	base_cible = Point()
	if base_ennemie != [] :
		for b in base_ennemie:
			if aide.setAssistance(coordonnees, self.getName(), b.getX(), b.getY(), 10000, AIDE_BASE_ENNEMIE,3, tour):
				base_cible = b
	
	# Deplacement normal
	if aide.besoin():
		direct = self.towards(aide.getVisee().getX() - coordonnees.getX(), aide.getVisee().getY() -coordonnees.getY())
		if coordonnees.distanceTo(aide.getCoord()) > 150 or (aide.besoin() > AIDE_LAUNCHER or aide.besoin()  == AIDE_BASE_ENNEMIE): 
			self.setHeading(direct)
		else:
			self.setHeading(180 + direct)

	# BaryCentre des unites ennemies proches
	centre = Point() 
	percepts = self.getPercepts()
	nb_unites = 0
	
	ennemi_perdu = ennemi_courant.existe()
	qg_ennemi_visible = not (base_cible.estConnu() and base_cible.distanceTo(coordonnees) <= 100)
	for p in percepts:
		team = p.getTeam()
		type_percept = p.getPerceptType()
		if ((type_percept =="Home") or (type_percept =="RocketLauncher") or (type_percept == "Explorer")) and team!=self.getTeam():
			if p.getPerceptType() == "Home":
				base_abs = Point(p.getX() +coordonnees.getX(), p.getY() +coordonnees.getY())
				if not baseEnnemieConnue(base_abs):
					base_ennemie.append(base_abs)
					self.broadcast(groupName, "info", "INFO-QG", str(p.getX()), str(p.getY()))
				qg_ennemi_visible = qg_ennemi_visible or base_abs.distanceTo(base_cible) <= 10
			coord = Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY())
			energie = p.getEnergy()
			if not ennemi_courant.existe() :
				ennemi_courant.setCible(type_percept, coord, energie, tour)
				#self.broadcast(groupName, "launcher", "FIN")
				args = [str(p.getX()), str(p.getY()), str(p.getEnergy())]
				if type_percept == "RocketLauncher":
					self.broadcast(groupName, "launcher", "HELP2", args)
			elif ennemi_courant.egal(type_percept, coord) :
				ennemi_courant.setCible(type_percept, coord, energie, tour)
				ennemi_perdu = 0
			elif (ennemi_courant.getEnergy() > p.getEnergy()) and prioriteType(ennemi_courant.getType()) <= prioriteType(p.getPerceptType()):
				ennemi_courant.setCible(type_percept, coord, energie, tour)
				ennemi_perdu = 0
			
	if base_cible.estConnu() and not qg_ennemi_visible:
		baseEnnemieSuppr(base_cible)
		self.broadcast(groupName, "mobile", "FIN-QG", str(base_cible.getX() - coordonnees.getX()), str(base_cible.getY() - coordonnees.getY()))
		
		
#	if ennemi_courant.existe():
#		self.setUserMessage("TIR")
#	else:
#		self.setUserMessage(None)

	if ennemi_perdu:
		self.broadcast(groupName, "launcher", "FIN")
		self.broadcast(groupName, "launcher", "ETAT")
		self.broadcast(groupName, "explorer", "ETAT")
		self.setHeading(self.towards(ennemi_courant.getX() - coordonnees.getX(), ennemi_courant.getY() - coordonnees.getY()))
		ennemi_courant.delCible()
		
	elif (ennemi_courant.existe()):
		coord = ennemi_courant.viser(coordonnees)
		if tirer(coord):
			return

	
		
	# Manoeuvres d"evitement
	gentils = Point()
	mechants = Point()
	nb_mechants = 0
	nb_gentils = 0	
	evite = Point()
	amis = Vector()
	tenaille = Point()
	
	#	self.getName()
	liste_amis = []
	liste_rockets = []
	if aide.besoin():
		dist_aide = aide.getVisee().distanceTo(coordonnees)
	else:
		dist_aide = 1000
	for p in percepts:
		dist = self.distanceTo(p)
		if p.getPerceptType() == "RocketLauncher" and p.getTeam() == self.getTeam():
			liste_amis.append(p)
			r = Mobile(coordonnees.getX() + p.getX(), coordonnees.getY() + p.getY())
			amis.add(r)
			if aide.besoin() == AIDE_LAUNCHER :
				p_ami = Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY())
				dist_ami = p_ami.distanceTo(aide.getVisee())
				if dist_aide > 200 and dist_ami > dist_aide+20:
					attendre = 1
		if p.getPerceptType() == "Rocket":
			liste_rockets.append(p)
		if p.getPerceptType() == "Home" and p.getTeam() == self.getTeam():
			liste_amis.append(p)
		ecart_min = ecartMin(p)
		if dist > 0 and dist < ecart_min:
			mult = (ecart_min - dist)/ecart_min
			if p.getPerceptType() == "Obstacle" or (p.getTeam() == self.getTeam()):
				gentils.setCoord(gentils.getX() - p.getX()/dist *mult, gentils.getY() - p.getY()/dist*mult)
				nb_gentils +=1
			else:
				nb_mechants += 1
				mechants.setCoord(mechants.getX() - p.getX()/dist*mult, mechants.getY() - p.getY()/dist*mult)
				
	mon_groupe.amisVus(amis)

	if attendre:
		self.setHeading(self.getHeading() + 180)


	rocket_dangereuse = eviteRockets(liste_rockets)
	
	tirer_loin = aide.besoin() and aide.getVisee().distanceTo(coordonnees) < 200
	# Si on voit une rocket sans voir d"ennemi
	if rocket_dangereuse != None and not tirer_loin:
		d = coordonnees.distanceTo(rocket_dangereuse)
		# Estimation de la pos de l"ennemi : a 150 de distance
		x = (rocket_dangereuse.getX() - coordonnees.getX()) / d * 150
		y = (rocket_dangereuse.getY() - coordonnees.getY())/ d * 150
		args = [str(x), str(y), str(10000)]
		self.broadcast(groupName, "launcher", "ROCKET", args)
		#Demande a un explorateur de venir voir s"il y a quelqu"un
		if offre == None:
			self.broadcast(groupName, "explorer", "ROCKET-REQ",str(x),str(y))
		else:
			self.send(offre, "ROCKET-OK", str(x), str(y))
	
	if rocket_dangereuse != None and attenteRoquettes.pret():
		if tirer(rocket_dangereuse):
			return
	
	if tirer_loin:
		pos = Point()
		pos.setCoord(aide.getVisee().getX(), aide.getVisee().getY())
		if tirer(pos):
			return
	
	
	# Besoin de construire des rockets?
	seuil_rocket = 50
	if tirer_loin:
		seuil_rocket = 10
	
	if (self.getRocketNumber() < seuil_rocket):
	#	self.setUserMessage("Reconstruction Rockets")
		self.buildRocket()
		return
	#else:
	self.setUserMessage(self.getTeam())
	
	if mon_groupe.nbVus() != 0 :
		centre = mon_groupe.barycentre()
		if not aide.besoin():
			if coordonnees.distanceTo(centre) >60:
				direct = self.getHeading()*Math.PI/ 180
				but_virtuel = Point()
				but_virtuel.setCoord(35*Math.cos(direct),35*Math.sin(direct))
				self.setHeading(self.towards(but_virtuel.getX() + centre.getX()-coordonnees.getX(),but_virtuel.getY() + centre.getY() - coordonnees.getY() ))
			formation = mon_groupe.vecteurMoyen()
			direct = self.getHeading()*Math.PI/ 180
			but_virtuel = Point()
			but_virtuel.setCoord(1*Math.cos(direct),1*Math.sin(direct))
			self.setHeading(self.towards(but_virtuel.getX() + formation.getX() ,but_virtuel.getY() + formation.getY()))
		

	eviteAmis(liste_amis)

	if nb_gentils > 0:
		direct = self.getHeading()*Math.PI/ 180
		but_virtuel = Point()
		if aide.besoin() and aide.getVisee().distanceTo(coordonnees) < 200:
			but_virtuel.setCoord(Math.cos(direct),Math.sin(direct))
		else:
			but_virtuel.setCoord(5*Math.cos(direct),5*Math.sin(direct))
		self.setHeading(self.towards(but_virtuel.getX() + gentils.getX(),but_virtuel.getY() + gentils.getY() ))

	if nb_mechants > 0:
		direct = self.getHeading()*Math.PI /180
		but_virtuel = Point()
		but_virtuel.setCoord(2*Math.cos(direct),2*Math.sin(direct))
		self.setHeading(self.towards(but_virtuel.getX() + mechants.getX(),but_virtuel.getY() + mechants.getY() ))
	
	
	self.move()
