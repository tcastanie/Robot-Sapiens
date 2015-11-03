from java.lang import Math
from java.util import Random
rnd=Random()

# Active le mode verbeux
debug = 1

##################################################################

class Own:
	def __init__(this):
		this._target = None
		this._base = 0
		this._direction_ttl = 0
		this._dest_x = None
		this._dest_y = None
	def toggleSens(this):
		this.sens *= -1
	def getSens(this):
		return this.sens
	def getTeam(this):
		return self.getTeam()
	def setTarget(this, x, y):
		if this._target:
			this._target.seen(x, y)
		else:
			this._target = Target(x, y)
	def update(this):
		if this._target:
			if not this._target.update():
				this._target = None
				# à enlever
				self.setHeading(rnd.nextDouble()*180)
	def getTarget(this):
		return this._target
	def setHome(this, x, y):
		if not this._base:
			self.broadcast(this.getTeam(), 'thebigone', 'base_detected', str(x), str(y))
			this._base = 1
	def setDestination(this, x, y):
		this._dest_x = x
		this._dest_y = y


class Target:
	def __init__(this, x, y):
		this.seen(x, y)
	def seen(this, x, y):
		this._x = x
		this._y = y
		this._backward = 1
		this._timer = 3
		self.broadcast(my.getTeam(), 'thebigone', 'killer_detected', [str(this._x), str(this._y), '4999'])
	def towards(this):
		angle = self.towards(this._x, this._y)
		if this._backward:
			angle += 180
		return angle
	def update(this):
		if this._timer == 0:
			if this._backward:
				this._backward = 0
				this._timer = 10
			else:
				return 0
		else:
			this._timer -= 1
		return 1

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

#########################################################
# fonctions appelées par madkit

def activate():
	self.createGroup(0, my.getTeam(), 'L\'equipe qui en veut', None)
	self.requestRole(my.getTeam(), 'explo', None)
	#self.requestRole(my.getTeam(), 'killers', None)
	#self.broadcast(self.getTeam(), 'thebigone', 'hello')
	self.setHeading(0)

def end():
	pass

def processEnv():
	if my._direction_ttl:
		my._direction_ttl -= 1
	percepts = self.getPercepts()
	best = None
	rocket = None
	for p in percepts:
		if p.getTeam() != my.getTeam():
			if p.getPerceptType() == 'RocketLauncher': # or i.getPerceptType() == 'Explorer':
				if not best or distance(p.getX(), p.getY()) < distance(best.getX(), best.getY()):
					best = p
			elif p.getPerceptType() == 'Home':
				my.setHome(p.getX(), p.getY())
		if p.getPerceptType() == 'Rocket' or p.getTeam() != my.getTeam():
			if not rocket or distance(p.getX(), p.getY()) < distance(rocket.getX(), rocket.getY()) and Math.abs(self.towards(p.getX(), p.getY()))-Math.abs(self.getHeading()) < 20:
				rocket = p
	if not best:
		return
	my.setTarget(p.getX(), p.getY())
	# évitement des rockets et des mechants !
	if rocket and not my._direction_ttl:
		self.setHeading(self.towards(rocket.getX(), rocket.getY())+120)
		my._direction_ttl = 30

## lecture/reponse des emails :)
def readMailbox():
	while not self.isMessageBoxEmpty():
		msg = self.readMessage()
		if msg.getAct() == 'where_are_you':
			self.send(msg.getSender(), 'my_position')
		elif msg.getAct() == 'move':
			my.setDestination(float(msg.getArg1())+msg.getFromX(), float(msg.getArg2())+msg.getFromY())
		elif msg.getAct() == 'explore':
			my._dest_x = None

def setDirection():
	if my._direction_ttl:
		return
	if my._dest_x:
		self.setHeading(self.towards(my._dest_x, my._dest_y))
	elif not self.isMoving():
		self.setHeading(rnd.nextDouble()*360)

def doIt():
	readMailbox()
	processEnv()
	setDirection()
	self.move()

my=Own()
