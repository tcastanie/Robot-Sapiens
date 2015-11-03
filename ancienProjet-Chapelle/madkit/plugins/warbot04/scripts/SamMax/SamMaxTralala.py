# Warbot - Explorer - Neil est roi l'as du pecan, leumas lojuop

from java.lang import Math
from java.util import Random

    
#--------------TRAITEMENT PERCEPTS------------------
# Une classe qui les encode (pour 1 tour...)
class PerceptBag:
    def __init__ (self, team):
        # pour les ennemis
        self.my_team = team
        self.reset ()

    def reset (self):
        self.attackers = { } ; self.explorers = { }
        self.homes = { } ; self.friends = { }
        self.bases = { }
        self.obstacles = { } ; self.rockets = { }
        self.nb_ennemies = (0,0,0) # cache le nb d'ennemis (1)
        self.nearest = None
        
    def update (self, perc):
        self.reset ()
        if len(perc)>0:
            self.nearest=perc[0]
            for p in perc:
                p_type = p.getPerceptType ()
                p_team = p.getTeam ()
                if p_team != self.my_team:
                    if p_type == 'RocketLauncher':
                        self.attackers [p] = p.getDistance ()
                    elif p_type == 'Home':
                        self.homes [p] = p.getDistance ()
                    elif p_type == 'Explorer':
                        self.explorers [p] = p.getDistance ()
                    elif p_type == 'Obstacle':
                        self.obstacles [p] = p.getDistance ()
                    elif p_type == 'Rocket':
                        self.rockets [p] = p.getDistance ()
                    else:
                        toterm ("OBJET INCONNU "+p_type)
                else:
                    if p_type == 'Home':
                        self.bases [p] = p.getDistance ()
                    else:
                        self.friends [p] = p.getDistance ()
        self.nb_ennemies = self._ennemy_count ()

    def nearest_wall():
        n=self.obstacles.items()[0][0]
        for t in self.obstacles.items():
            if (t[0].getDistance() < n.getDistance()):
                n=t[0]
            
    def nearest_foe():
        n=self.attackers.items()[0][0]
        for t in self.attackers.items():
            if (t[0].getDistance() < n.getDistance()):
                n=t[0]

    # (1) Workaround pour un bug qui n'existe peut-être plus
    def _ennemy_count (self):
       return (len (self.attackers),
                len (self.explorers),
                len (self.homes))
        
rnd = Random() # Generateur aleatoire
groupName = 'sAMetmAX-' +  self.getTeam ()
#journaliser les messages : 0/1
log = 1
#println sur la console : 0/1
console = 1
#cette globale est initialisee a chaque tour a
#une liste de percepts vus par l'agent
percepts = PerceptBag (self.getTeam ())
#celle ci contient ce qu'il faut pour calculer
#le mouvement courant

#methode qui filtre l'affichage
def toterm (msg):
    if console == 1:
        self.println (msg)

#appellee au posage de l'agent
def activate ():
    self.createGroup (0, groupName, None, None)
    self.requestRole (groupName, 'explorer', None)
    self.randomHeading()
    toterm ("Journalist activé...")

#appelee quand le robot est casse
def end ():
    toterm ("Journalist dead.")

#fonction principale appelee a chaque tour de jeu
def doIt ():
    percepts.update(self.getPercepts())
    p=percepts.nearest
    #changements de direction eventuels
    if (p!=None):
        type=p.getPerceptType()
        if (type=='Obstacle'):
            if (p.getDistance() < 20):
                ping_pong_with_noise(p)
            elif (p.getDistance <10):
                self.randomHeading()
        else:
            compute_heading(p,'flee')
            
        for p in percepts.attackers.items():
            toterm("HEY!!!!!!!!!!! JE PARLE")
            self.broadcast (groupName, 'killer', 'Attack',
                    [p[0].getPerceptType(),
                    str(p[0].getX ()),
                    str(p[0].getY ())])
        for p in percepts.explorers.items():
            toterm("HEY!!!!!!!!!!! JE PARLE")
            self.broadcast (groupName, 'killer', 'Attack',
                    [p[0].getPerceptType(),
                    str(p[0].getX ()),
                    str(p[0].getY ())])
        for p in percepts.homes.items():
            toterm("HEY!!!!!!!!!!! JE PARLE")
            self.broadcast (groupName, 'killer', 'Attack',
                    [p[0].getPerceptType(),
                    str(p[0].getX ()),
                    str(p[0].getY ())])
    self.move()

#rebondissement a la arkanoid
def ping_pong_with_noise(wall):
    xw=wall.getX()
    yw=wall.getY()
    maxnoise=30
    noise=(rnd.nextDouble()*maxnoise)-(maxnoise/2)
    toterm(str(noise))
    curdir=self.getHeading()
    if (xw>0):
        if (yw>0):
            nudir=(curdir-90+noise)%360
        else:
            nudir=(curdir+90+noise)%360
    else:
        if (yw>0):
            nudir=(curdir+90+noise)%360
        else:
            nudir=(curdir-90+noise)%360
    self.setHeading(nudir)
    

# calculer l'orientation pour fuir ou attacker
# avec le super modele de forces
# target : ennemi cible
# mode : { 'attack', 'flee'}
def compute_heading (target, mode):
    # composantes X et Y du vecteur direction final
    X = Y = 0
    for it, dist in percepts.friends.items ():
        #               percept, dist, scale, theta, p
        X = X + vector_x (it, dist, 50, 60, 2)
        Y = Y + vector_y (it, dist, 50, 60, 2)
    for it, dist in percepts.bases.items ():
        # Les bases sont boguées : repère sur le coin
        # supérieur gauche
        X = X + bug_vector_x (it, dist, 50, 60, 1)
        Y = Y + bug_vector_y (it, dist, 50, 60, 1)
    for it, dist in percepts.attackers.items ():
        # devrait être négligeable...
        X = X + vector_x (it, dist, 30, 70, 2)
        Y = Y + vector_y (it, dist, 30, 70, 2)
    for it, dist in percepts.explorers.items ():
        X = X + vector_x (it, dist, 20, 60, 1)
        Y = Y + vector_y (it, dist, 20, 60, 1)
    for it, dist in percepts.homes.items ():
        X = X + bug_vector_x (it, dist, 50, 70, 1)
        Y = Y + bug_vector_y (it, dist, 50, 70, 1)
    for it, dist in percepts.obstacles.items ():
        radius = it.getRadius ()
        X = X + vector_x (it, dist, 30+radius, 50, 2)
        Y = Y + vector_y (it, dist, 30+radius, 50, 2)
        
    # pour la cible
    if mode == 'flee':
        offset = 180
        u = 12 # alors, plus fort ou plus faible ?
    else:
        offset = 0
        u = 8
    X = X + Math.cos (deg2pi (offset + self.towards (target.getX(), target.getY()))) * u * target.getDistance ()
    Y = Y + Math.sin (deg2pi (offset + self.towards (target.getX(), target.getY()))) * u * target.getDistance ()
    if immobile ():
        toterm ("immobile")
    self.setHeading (self.towards (X, Y))

#converti les degres en radians
def deg2pi (deg):
    return deg / 180 * Math.PI

def vector_x (percept, dist, scale, theta, p): 
    return Math.cos (deg2pi (180 + self.towards (percept.getX(), percept.getY()))) * theta / Math.pow ((dist / scale), p)

def vector_y (percept, dist, scale, theta, p): 
    return Math.sin (deg2pi (180 + self.towards (percept.getX(), percept.getY()))) * theta / Math.pow ((dist / scale), p)

# Compenser le bug Madkit : redéfinit le centre de l'objet
def bug_vector_x (percept, dist, scale, theta, p):
    r = percept.getRadius () / 2
    return Math.cos (deg2pi (180 + self.towards (percept.getX()+r, percept.getY()+r))) * theta / Math.pow ((dist / scale), p)

def bug_vector_y (percept, dist, scale, theta, p):
    r = percept.getRadius () / 2    
    return Math.sin (deg2pi (180 + self.towards (percept.getX()+r, percept.getY()+r))) * theta / Math.pow ((dist / scale), p)

def immobile ():
    return not self.isMoving ()
    
def processInbox ():    
    pass
 
