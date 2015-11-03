from java.lang import Math
from java.util import Random

# active le mode verbeaux
debug = 1

# rayon moyen du cercle d'attaque
#taille_parabole = 160
security_radius = 160

# nombre de tours à attendre avant de lancer une exploration massive
time_before_explore = 300

# nombre de tours à attendre après une attaque avant de se replier en défense
time_before_retreat = 80

###############################################################################
# fonctions standard

def d(str):
    if debug:
        self.println('[DEBUG] '+str)

def s(str):
    self.println(str)

def distance(x, y, point=[0,0]):
	try:
		return Math.sqrt((x-point[0]) * (x-point[0]) + (y-point[1]) * (y-point[1]))
	except:
		return -1

###############################################################################
# moi

class Own:
	def __init__(this):
		# la liste des killers sous mon commandement
		this._killers = []
		this._explore_ttl = None
		this.behavior = None
		this._enemy_explorer = []
		this._enemy_killer = []
		this._enemy_home = []

	## gestion de la liste des combattants
	def addKiller(this, killer):
		this._killers.append(killer)
		this.behavior.newKiller(killer)
	def remKiller(this, killer):
		try:
			this._killers.remove(killer)
		except:
			s('soldat mort inconnu')
	def getKillerByName(this, name):
		#d(str(len(this._killers))+' bots dans la liste')
		for k in this._killers:
			if k.getName() == name:
				return k
			else:
				pass
				#d(' '+name+' != '+k.getName())
		return None
	def arePositionsKnown(this):
		for k in this._killers:
			if not k.getX():
				return 0
		return 1

	## sélection du comportement à adopter selon les événements
	def sendPositions(this):
		if this._enemyX:
			d('ennemi')
			this.setPositions(this._enemyX, this._enemyY, 1)
		#~ elif this._enemy_baseX:
			#~ d('base')
			#~ this.setPositions(this._enemy_baseX, this._enemy_baseY, 1)
		elif this.getPositionStable():
			d('cache')
			this.sendPositionsFromCache()
		else:
			d('defense')
			this.positionDefense()

	## 
	def addEnemyExplorer(this, p):
		this._enemy_explorer.append(p)
	def addEnemyKiller(this, p):
		this._enemy_killer.append(p)
	def addEnemyHome(this, p):
		this._enemy_home.append(p)
		this._enemy_baseX = p[0]
		this._enemy_baseY = p[1]
	def getEnemyExplorers(this):
		return this._enemy_explorer
	def getEnemyKillers(this):
		return this._enemy_killer
	def getEnemyHomes(this):
		#d('il y a '+str(len(this._enemy_home))+' bases ennemies en vue')
		return this._enemy_home
	def flush(this):
		this._enemy_explorer = []
		this._enemy_killer = []
		this._enemy_home = []

	def setBehavior(this, behav):
		if this.behavior != behav:
			this.behavior = behav
			this.behavior.join()

class Killer:
	def __init__(this, address):
		this._address = address
		this._X = None
		this._Y = None
	def getName(this):
		return this._address.getName()
	def setPosition(this, x, y):
		this._X = x
		this._Y = y
	def getX(this):
		return this._X
	def getY(this):
		return this._Y
	def getAddress(this):
		return this._address
	def distance(this, point):
		return distance(this._X, this._Y, point)

class Behavior:
	def __init__(this, me):
		this.me = me
	def move(this):
		pass
	def attack(this):
		pass
	def newKiller(this, killer):
		pass
	def join(this):
		pass
	def leave(this):
		pass
	def recvMail(this, msg):
		pass

class Positionnable:
	def getHalfCircle(this, num, radius, x, y):
		if num == 1:
			return [[x,y]]
		axe = self.towards(x, y)
		rayon = radius * (1 - Math.exp(-num/3))
		offset = Math.PI / (num-1)
		points = []
		for i in range(num):
			X = rayon*Math.cos(i*offset+Math.PI/2+axe*Math.PI/180)+x
			Y = rayon*Math.sin(i*offset+Math.PI/2+axe*Math.PI/180)+y
			points.append([X,Y])
		return points

	def getCircle(this, num, radius):
		if num == 0: 
			return None
		if num == 1:
			return [[50,50]]
		rayon = radius * (1 - Math.exp(-num/3))
		offset = 2*Math.PI / num
		points = []
		for i in range(num):
			X = rayon*Math.cos(i*offset)
			Y = rayon*Math.sin(i*offset)
			points.append([X,Y])
		return points

	def resolve(this, botList, points):
		bots=[]
		for b in botList:
			bots.append(b)
		res = []
		while bots:
			mins={}
			ibot = 0
			while ibot < len(bots):
				ipoint=0
				min = None
				while ipoint < len(points):
					# recherche du point min pour ce bot
					if not min or bots[ibot].distance(points[ipoint]) < bots[ibot].distance(min):
						min = points[ipoint]
					ipoint += 1
				mins[bots[ibot]] = min
				ibot += 1
			max = None
			for bot in mins.keys():
				if not max or bot.distance(mins[bot]) > max[0].distance(max[1]):
					max = [bot,mins[bot]]
			res.append(max)
			bots.remove(max[0])
			points.remove(max[1])
		return res

class Defense(Behavior, Positionnable):

	def __init__(this, me):
		Behavior.__init__(this, me)
		this._first_time = 1
		this._cached = 0
		this._join_direction = None
		this._join_ttl = 0
		this._join_points = []
		this._explore_ttl = 0

	def move(this):
		if not this._first_time and this._explore_ttl:
			this._explore_ttl -=1
			if not this._explore_ttl % 50:
				d('explore: '+str(this._explore_ttl))
			if not this._explore_ttl:
				this.me.setBehavior(exploring)
				return
		if this._join_ttl:
			this._join_ttl -= 1
			if this._join_ttl:
				return
			else:
				sumx = sumy = 0
				for p in this._join_points:
					sumx += p[0]
					sumy += p[1]
				this._join_direction = self.towards(sumx/len(this._join_points), sumy/len(this._join_points))
				this._first_time = 0
		if not this._cached and len(this.me._killers) > 0:
			if this._first_time:
				for pos in this.resolve(this.me._killers, this.getCircle(len(this.me._killers), security_radius)):
					k = this.me.getKillerByName(pos[0].getName())
					k.setPosition(pos[1][0], pos[1][1])
				this._cached = 1
			else:
				x = Math.cos(this._join_direction*Math.PI/180) * security_radius
				y = Math.sin(this._join_direction*Math.PI/180) * security_radius
				for pos in this.resolve(this.me._killers, this.getHalfCircle(len(this.me._killers), security_radius, x, y)):
					k = this.me.getKillerByName(pos[0].getName())
					k.setPosition(pos[1][0], pos[1][1])
				this._cached = 1
		this.sendCache()

	def join(this):
		s('COMPORTEMENT DEFENSE')
		this._explore_ttl = time_before_explore 
		if not this._join_direction:
			self.broadcast(self.getTeam(), 'killers', 'where_are_you')
			this._join_ttl = 2

	def sendCache(this):
		for k in this.me._killers:
			self.send(k.getAddress(), 'move', str(k.getX()), str(k.getY()))

	def newKiller(this, killer):
		this._cached = 0

	def recvMail(this, msg):
		d('receiving (defending) '+msg.getAct())
		act = msg.getAct()
		if act == 'my_position':
			this._join_points.append([msg.getFromX(), msg.getFromY()])
		elif act == 'dead':
			this._cached = 0
		elif act == 'killer_detected' or act == 'explorer_detected' or act == 'home_detected':
			this.me.setBehavior(attacking)

class Attacking(Behavior, Positionnable):
	def __init__(this, me):
		Behavior.__init__(this, me)
		# coordonnées de la base ou de l'ennemi ciblé
		this._enemy_baseX = this._enemy_baseY = this._enemyX = this._enemyY = None
		this._retreat_ttl = 0

	def findTarget(this):
		if len(this.me._killers) == 0:
			return
		# trouver la meilleure cible
		for list in [this.me.getEnemyKillers, this.me.getEnemyHomes]:
			lower = None
			for e in list():
				#d('ennemi: '+str(e[0])+','+str(e[1]))
				if e[0] and (not lower or lower[2] > e[2]):
					lower = e
			if lower:
				this.setEnemyPosition(lower[0], lower[1])
				this.me.flush()
				return
		if this._enemy_baseX:
			this.setEnemyPosition(this._enemy_baseX, this._enemy_baseY)

	def attack(this):
		this.findTarget()
		if this._retreat_ttl == time_before_retreat:
			self.broadcast(self.getTeam(), 'killers', 'attack', str(this._enemyX), str(this._enemyY))

	def move(this):
		if this._retreat_ttl:
			this._retreat_ttl -= 1
			if not this._retreat_ttl:
				this.me.setBehavior(defensing)
				return
		if len(this.me._killers) == 0 or not this._enemyX:
			return
		x = Math.cos(self.towards(this._enemyX, this._enemyY)*Math.PI/180) * security_radius
		y = Math.sin(self.towards(this._enemyX, this._enemyY)*Math.PI/180) * security_radius
		for pos in this.resolve(this.me._killers, this.getHalfCircle(len(this.me._killers), security_radius, x, y)):
			k = this.me.getKillerByName(pos[0].getName())
			#k.setPosition(pos[1][0], pos[1][1])
			self.send(k.getAddress(), 'move', str(pos[1][0]), str(pos[1][1]))


	def join(this):
		s('COMPORTEMENT ATTAQUE')

	def setEnemyPosition(this, x, y):
		this._enemyX = x
		this._enemyY = y
		this._retreat_ttl = time_before_retreat
		#d('nouvelle cible')

	def recvMail(this, msg):
		#d('receiving (attacking) '+msg.getAct())
		pass


	#~ def setEnemyBasePosition(this, x, y):
		#~ this._enemy_baseX = x
		#~ this._enemy_baseY = y



class Exploration(Behavior):
	def __init__(this, me):
		Behavior.__init__(this, me)
		this._sent = 0
	def move(this):
		if not this._sent:
			self.broadcast(self.getTeam(), 'killers', 'explore')
			this._sent = 1
	def newKiller(this, killer):
		self.send(killer.getAddress(), 'explore')
	def recvMail(this, msg):
		act = msg.getAct()
		#d('receiving '+act)
		if act == 'killer_detected' or act == 'explorer_detected' or act == 'home_detected':
			this.me.setBehavior(attacking)
	def join(this):
		s('COMPORTEMENT EXPLORATION')

# fonctions pour moi

def readMailbox():
	while not self.isMessageBoxEmpty():
		msg = self.readMessage()
		my.behavior.recvMail(msg)
		act = msg.getAct()
		d(' !!receiving '+act)
		if act == 'my_position':
			k = my.getKillerByName(msg.getSender().getName())
			if not k:
				d('HOULA, réponse "my_position" dun killer non repertorié!')
				continue
			k.setPosition(msg.getFromX(), msg.getFromY())
		elif act == 'hello':
			#s('hello recu ('+msg.getSender().getName()+')')
			k = Killer(msg.getSender())
			my.addKiller(k)
			k.setPosition(msg.getFromX(), msg.getFromY())
		elif act == 'dead':
			k = my.getKillerByName(msg.getSender().getName())
			my.remKiller(k)
		elif act == 'explorer_detected':
			my.addEnemyExplorer([float(msg.getArgN(1))+msg.getFromX(), float(msg.getArgN(2))+msg.getFromY(), int(msg.getArgN(3))])
		elif act == 'killer_detected':
			my.addEnemyKiller([float(msg.getArgN(1))+msg.getFromX(), float(msg.getArgN(2))+msg.getFromY(), int(msg.getArgN(3))])
		elif act == 'base_detected':
			my.addEnemyHome([float(msg.getArgN(1))+msg.getFromX(), float(msg.getArgN(2))+msg.getFromY(), int(msg.getArgN(3))])


def analyseEnv():
	for p in self.getPercepts():
		if p.getTeam() != self.getTeam():
			if p.getPerceptType() == 'Explorer':
				my.addEnemyExplorer([p.getX(), p.getY(), p.getEnergy()])
			elif p.getPerceptType() == 'RocketLauncher':
				my.addEnemyKiller([p.getX(), p.getY(), p.getEnergy()])
			elif p.getPerceptType() == 'Home':
				my.addEnemyHome([p.getX(), p.getY(), p.getEnergy()])

# fonctions pour madkit :

def activate():
	self.createGroup(0, self.getTeam(), 'L\'equipe qui en veut', None)
	self.requestRole(self.getTeam(), 'thebigone', None)
	killers = self.getAgentsWithRole(self.getTeam(), 'killers')
	for a in killers:
		k = Killer(a)
		self.send(a, 'where_are_you')
		my.addKiller(k)
	


def end():
	self.broadcast(self.getTeam(), 'killers', 'explore')
	s('good game')

def doIt():
	analyseEnv()
	readMailbox()

	# positionnement de base (attente passive)
	if my.arePositionsKnown():
		my.behavior.attack()
		my.behavior.move()

my = Own()
defensing = Defense(my)
attacking = Attacking(my)
exploring = Exploration(my)
my.behavior = exploring
