from java.lang import Math
from java.util import Random
rnd = Random()

# Active le mode verbeux
debug = 1

# portee d'une rocket: 200
# vitesse d'une roquette: 10

# borne d'energie minimale jusqu'ou on peut construire des nouvelles rockets
retreat_limit = 1000

# combien de tours continue-t-on à tirer sans que plus personne n'ait vu la cible
default_blind_time = 4

# distance maxi à laquelle on tire sur une cible
max_shoot_distance = 200

# nombre de tours à attendre pour recharger l'arme
reload_round = 2

# distance de sécurité pour ne pas tirer sur ses amis (normalement 0 est bon mais pas si les amis se "jettent" sur les rockets :p)
security_distance = 4

# distance pour prendre en compte l'évitement
redirect_distance = 20

##################################################################
# fonctions communes

def d(str):
	if debug:
		self.println('[DEBUG] '+str)

def s(str):
	self.println(str)

def distance(x, y):
	try:
		return Math.sqrt(x * x + y * y)
	except:
		return -1

def isMasqued(x, y):
	# on ne tire pas si un #@! de copain se trouve devant (verification des percepts seulement ?)
	for p in self.getPercepts():
		if p.getTeam() == self.getTeam() and p.getPerceptType() != 'Rocket':
			if distance(p.getX(), p.getY()) > distance(x, y):
				continue
			a = self.towards(p.getX(), p.getY())*Math.PI/180
			b = self.towards(x, y)
			r = p.getRadius() + security_distance
			cos = Math.cos(a - Math.PI/2)
			sin = Math.sin(a - Math.PI/2)
			inf = self.towards(p.getX() + r*cos, p.getY() + r*sin)
			sup = self.towards(p.getX() - r*cos, p.getY() - r*sin)
			#~ d('ene: x: '+str(x)+' y: '+str(y)+' A: '+str(b)+' a: '+str(b*Math.PI/180))
			#~ d('     s: '+str(p.getX() + r*cos)+':'+str(p.getY() + r*sin)+' t: '+str(p.getX() - r*cos)+':'+str(p.getY() - r*sin))
			#~ d('ami: x: '+str(p.getX())+' y: '+str(p.getY())+' A: '+str(a*180/Math.PI)+' a: '+str(a)+' r: '+str(r))
			#~ d('ne pas tirer ene(A) entre '+str(inf)+' et '+str(sup))
			#~ d('angles relatifs: ami: '+str((sup - inf) % 360)+' enemy: '+str((b - inf) % 360 ))
			if ((b - inf) % 360 ) > ((sup - inf) % 360):
				#~ d('boom!')
				continue
			#~ d('un ami me cache la cible!')
			return 1
	return 0

##################################################################
# mes données

class Own:
	def __init__(this):
		# viseur
		this._target = None
		# cible donnée sur ordre
		this._t = Target()
		# notre destination
		this._destX = 0
		this._destY = 0
		# "main lourde" sur la détente
		this._blind_time = 0
		# compteur pour recharger l'arme
		this._shoot_time = 0
		# mode exploration
		this._explore = 0

	def canIShoot(this):
		if self.getRocketNumber() < 1 and self.getEnergy() > retreat_limit:
			self.buildRocket()
			return 0
		if this._shoot_time > 0:
			this._shoot_time -= 1
			return 0
		return 1

	## sélectionne une cible:
	## - celle ordonnée si à portée + non cachée
	## - la plus faible parmi les visibles non cachées
	def getTarget(this):
		
		# si on a une cible principale
		if this._t.x and distance(this._t.x, this._t.y) < max_shoot_distance and not isMasqued(this._t.x, this._t.y):
				this._target = this._t
				return 1

		# sinon
		list = TargetList()
		for p in self.getPercepts():
			if p.getTeam() != self.getTeam() and p.getPerceptType() == 'RocketLauncher':
				list.add(p)

		# sélection de la 1ere cible visible non cachée
		for p in list.get():
			if not isMasqued(p.getX(), p.getY()):
				this._target = Target()
				this._target.setPosition(p.getX(), p.getY())
				return 1

		# c'est mal fait mais: on recommence pour les explorer
		list = TargetList()
		for p in self.getPercepts():
			if p.getTeam() != self.getTeam() and p.getPerceptType() == 'Explorer':
				list.add(p)

		# sélection de la 1ere cible visible non cachée
		for p in list.get():
			if not isMasqued(p.getX(), p.getY()):
				this._target = Target()
				this._target.setPosition(p.getX(), p.getY())
				return 1

		# y'en n'a pas
		return 0

	## on a reçu l'ordre d'abattre cette cible
	def setTarget(this, x, y):
		this._t.setPosition(x, y)
		this._blind_time = default_blind_time

	## pan!
	def shootTarget(this):
		direction = this._target.getAim()
		self.launchRocket(direction)
		this._shoot_time = reload_round

	def setDestination(this, x, y):
		this._destX = x
		this._destY = y
		d('set destination to '+str(x)+':'+str(y))

	## règle la trajectoire pour le tour courant + décale la trajectoire si obstacle ami
	def setTrajectoire(this):
		if this._explore:
			if not self.isMoving() or self.getHeading() == 0:
				self.randomHeading()
			return
		decalx = decaly = 0
		for p in self.getPercepts():
			type = p.getPerceptType()
			dist = p.getDistance()
			if dist < redirect_distance and p.getTeam() == self.getTeam() and (type == 'Home' or type == 'RocketLauncher' or type == 'Explorer'):
				decalx += p.getX() / dist * (70-dist) / 120
				decaly += p.getY() / dist * (70-dist) / 120
				#~ d('distance: '+str(dist))
		#~ d('direction originelle: '+str(self.towards(this._destX, this._destY)))
		#~ d('direction modifiee: '+str(self.towards(Math.cos(self.towards(this._destX, this._destY))-decalx, Math.sin(self.towards(this._destX, this._destY))-decaly)))
		self.setHeading(self.towards(Math.cos(self.towards(this._destX, this._destY)*Math.PI/180)-decalx, Math.sin(self.towards(this._destX, this._destY)*Math.PI/180)-decaly))

		#~ dirt = self.towards(this._destX, this._destY)
		#~ for p in self.getPercepts():
			#~ type = p.getPerceptType()
			#~ dist = p.getDistance()
			#~ dirp = self.towards(p.getX(), p.getY())
			#~ diff = dirp - dirt
			#~ if (dist < redirect_distance and p.getTeam() == self.getTeam() and (type == 'Home' or type == 'RocketLauncher' or type == 'Explorer')):

				#~ d('testons')
				#~ if diff < 0 and diff > -45:
					#~ self.setHeading(dirp+45)
				#~ elif diff > 0 and diff < 45:
					#~ self.setHeading(dirp-45)
				#~ else:
					#~ d('eh non')
					#~ continue
				#~ d('trajctoire modifiee')
				#~ return

				#~ and  or (dirp - dirt) % 360 < 45:
				#~ if (dirp - dirt) % 360 < 45 and (dirp - dirt) % 360 < 0:
					#~ self.setHeading(45 + dirp)
				#~ else:
					#~ self.setHeading(45 - dirp)
				#~ return
		#~ self.setHeading(dirt)

class TargetList:
	def __init__(this):
		this._list = []
	def add(this, target):
		i=0
		while i < len(this._list) and this._list[i].getEnergy() < target.getEnergy():
			i += 1
		this._list.insert(i, target)
	def get(this):
		return this._list

class Target:
	def __init__(this):
		this.oldx = None
		this.oldy = None
		this.x = None
		this.y = None
	def setPosition(this, x, y):
		this.oldx = this.x
		this.oldy = this.y
		this.x = x
		this.y = y
	def getAim(this):
		if not this.x:
			return None
		return self.towards(this.x, this.y)
		#~ if not this.oldx:
			#~ d('simple aiming')
			#~ return self.towards(this.x, this.y)
		## smart aiming
		#~ a1 = self.towards(this.oldx, this.oldy)
		#~ a2 = self.towards(this.x, this.y)
		#~ return (a2 - distance(this.x, this.y) * (a1 - a2) / 10)

## fonctions pour moi

## met à jour quelques variables
def newRound():
	if my._blind_time > 0: 
		my._blind_time -= 1
	else:
		# la cible a expiré
		my._t.x = None

## reporte toutes les unités ennemies visibles au chef
def analyseEnv():
	for p in self.getPercepts():
		if p.getTeam() != self.getTeam():
			if p.getPerceptType() == 'Home':
				self.broadcast(self.getTeam(), 'thebigone', 'base_detected', [str(p.getX()), str(p.getY()), str(p.getEnergy())])
			elif p.getPerceptType() == 'RocketLauncher':
				self.broadcast(self.getTeam(), 'thebigone', 'killer_detected', [str(p.getX()), str(p.getY()), str(p.getEnergy())])
			elif p.getPerceptType() == 'Explorer':
				self.broadcast(self.getTeam(), 'thebigone', 'explorer_detected', [str(p.getX()), str(p.getY()), str(p.getEnergy())])

## lecture/reponse des emails :)
def readMailbox():
	while not self.isMessageBoxEmpty():
		msg = self.readMessage()
		if msg.getAct() == 'where_are_you':
			self.send(msg.getSender(), 'my_position')
		elif msg.getAct() == 'move':
			my.setDestination(float(msg.getArg1())+msg.getFromX(), float(msg.getArg2())+msg.getFromY())
			my._explore = 0
		elif msg.getAct() == 'attack':
			my.setTarget(float(msg.getArg1())+msg.getFromX(), float(msg.getArg2())+msg.getFromY())
			my._explore = 0
		elif msg.getAct() == 'explore':
			my._explore = 1
			self.randomHeading()

# fonctions appelées par madkit

def activate():
	self.createGroup(0, self.getTeam(), 'L\'equipe qui en veut', None)
	self.requestRole(self.getTeam(), 'killers', None)
	self.broadcast(self.getTeam(), 'thebigone', 'hello')
	self.setHeading(0)
	self._explore = 1

def end():
	self.broadcast(self.getTeam(), 'thebigone', 'dead')

def doIt():
	newRound()
	analyseEnv()
	readMailbox()
	if my.canIShoot() and my.getTarget():
		my.shootTarget()
	else:
		my.setTrajectoire()
		self.move()

my = Own()
