# Warbot - Explorer - Neil est roi l'as du pecan, leumas lojuop

from java.lang import Math
from java.util import Random

    
#--------------TRAITEMENT PERCEPTS------------------
# Une classe qui les encode (pour 1 tour...)
class PerceptBag:
    def __init__ (self, team):
        # pour les ennemis
        self.my_team = team
        self.attackers = { }
        self.explorers = { }
        self.homes = { }
        self.friends = { }
        self.obstacles = { }
        self.nb_ennemies = (0,0,0) # cache le nb d'ennemis

    def update (self, perc):
        self.attackers = { }
        self.explorers = { }
        self.homes = { }
        self.obstacles = { }
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
            else:
                self.friends [p] = p.getDistance ()
        self.nb_ennemies = self._ennemy_count ()

    def _ennemy_count (self):
       return (len (self.attackers),
                len (self.explorers),
                len (self.homes))

#--------------TRAITEMENT MOUVEMENTS------------------
# Cette classe contient ce qu'il faut pour calculer
# les prochains mouvements
class MovementMemory:
    def __init__(self):
        self.direction="EAST"
        self.distance_to_go=0
        self.valuable_distance=0
        self.last_heading=0
        self.orthogonal_error=0
        self.vertical_policy=1 #1 descente, -1 remonte
        self.last_horizontal_dir="EAST"
        
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
mvt_mem = MovementMemory()

#methode qui filtre l'affichage
def toterm (msg):
    if console == 1:
        self.println (msg)

#converti les degres en radians
def deg2pi (deg):
    return deg / 180 * Math.PI

def activate ():
    self.createGroup (0, groupName, None, None)
    self.requestRole (groupName, 'explorer', None)
    self.setHeading(0)
    toterm ("Journalist activé...")

#appelee quand le robot est casse
def end ():
    toterm ("Journalist dead.")

#idee generale: on laboure le pre en faisant les
#sillons d'ouest en est
#pour forcer ce comportement en gardant les dispersions
#on met des forces enormes au bon point cardinal
#pour les decalages entre sillons, on maintient
#une distance parcourue qu'on compare avec une borne

def doIt ():
    update_distances()
    percepts.update(self.getPercepts())
    if should_change_dir():
        change_dir()
    compute_heading()
    toterm(mvt_mem.direction+" Heading:"+str(self.getHeading())+" ach:"+str(mvt_mem.valuable_distance)+"/"+str(mvt_mem.distance_to_go))
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
    mvt_mem.last_heading=self.getHeading()

#donne la valeur de l'angle correspondant a un point cardinal
def deg_val(d):
    v=None
    if (d=="EAST"):
        v=0
    elif (d=="WEST"):
        v=180
    elif (d=="NORTH"):
        v=270
    elif (d=="SOUTH"):
        v=90
    return(v)

#donne l'oppose d'un point cardinal
def opposite(d):
    o=None
    if (d=="EAST"):
        o="WEST"
    elif (d=="WEST"):
        o="EAST"
    elif (d=="NORTH"):
        o="SOUTH"
    elif (d=="SOUTH"):
        o="NORTH"
    return(o)

#met a jour la distance orthogonale parcourue par rapport au dernier sillon
#notee valuable_distance
#ainsi que la distance parallele notee orthogonal_error
#n'a de sens que pendant un changement de sillon
def update_distances():
    covered=self.getCoveredDistance()
    last_angle=mvt_mem.last_heading - deg_val(mvt_mem.direction)
    toterm("la"+str(last_angle)+"lh"+str(mvt_mem.last_heading)+"di"+str(deg_val(mvt_mem.direction)))
    ortho_increment=covered*Math.sin(last_angle)
    dist_increment=covered*Math.cos(last_angle)
    mvt_mem.valuable_distance += dist_increment
    mvt_mem.orthogonal_error += ortho_increment

#change la direction et met a jour la distance orthogonale a parcourir en cas de chgt de sillon
def change_dir():
    range=130
    if (mvt_mem.direction=="EAST"):
        #pour changer de sillon, on doit s'eloigner perpendiculairement
        #d'une certaine distance notee distance_to_go
        #ici c'est 2 fois le rayon de vision + ou - l'ecart
        #du tracteur par rapport au sillon precedent, a la fin
        #du dernier tour, noté orthogonal_error
        mvt_mem.distance_to_go=(2*range)+mvt_mem.orthogonal_error
        if (mvt_mem.vertical_policy==1):
            mvt_mem.direction="SOUTH"
        else:
            mvt_mem.direction="NORTH"
            
    elif (mvt_mem.direction=="WEST"):
        mvt_mem.distance_to_go=(2*range)+mvt_mem.orthogonal_error
        if (mvt_mem.vertical_policy==1):
            mvt_mem.direction="SOUTH"
        else:
            mvt_mem.direction="NORTH"
        
    elif (mvt_mem.direction=="NORTH"):
        #cas ou on a touché le haut
        if (mvt_mem.valuable_distance < mvt_mem.distance_to_go ):
            mvt_mem.vertical_policy=1
        mvt_mem.distance_to_go=None
        mvt_mem.direction=opposite(mvt_mem.last_horizontal_dir)
        mvt_mem.last_horizontal_dir=mvt_mem.direction
        
    elif (mvt_mem.direction=="SOUTH"):
        #cas ou on a touché le bas
        if  (mvt_mem.valuable_distance < mvt_mem.distance_to_go ):
            mvt_mem.vertical_policy=-1
        mvt_mem.distance_to_go=None
        mvt_mem.direction=mvt_mem.direction=opposite(mvt_mem.last_horizontal_dir)
        mvt_mem.last_horizontal_dir=mvt_mem.direction
        
    mvt_mem.orthogonal_error=0
    mvt_mem.valuable_distance=0
    mvt_mem.last_heading=deg_val(mvt_mem.direction)
        
#indique si on doit changer de direction
#on doit le faire quand on termine un sillon
#ou bien qu'on a fini de s'en ecarter et qu'on est pret
#a en commencer un autre
def should_change_dir():
    toterm("should_change_dir called")
    should_change = 0
    if (mvt_mem.direction=="EAST"):
        walls = percepts.obstacles.items()
        pos=0
        taille=len(walls)
        while((pos < taille) and not(should_change)):
            toterm(str([pos,walls[pos][0].getX(),walls[pos][0].getY()]))
            if ((abs(walls[pos][0].getY()) < 30) and (walls[pos][0].getX()>0)):
                should_change=1
            pos += 1
    elif (mvt_mem.direction=="WEST"):
        walls = percepts.obstacles.items()
        pos=0
        taille=len(walls)
        while((pos < taille) and not(should_change)):
            toterm(str([pos,walls[pos][0].getX(),walls[pos][0].getY()]))
            if ((abs(walls[pos][0].getY()) < 30) and (walls[pos][0].getX()<0)):
                should_change=1
            pos += 1
    elif (mvt_mem.direction=="SOUTH"):
        if (mvt_mem.valuable_distance >= mvt_mem.distance_to_go):
            should_change = 1
        else:
            walls = percepts.obstacles.items()
            pos=0
            taille=len(walls)
            while((pos < taille) and not(should_change)):
                toterm(str([pos,walls[pos][0].getX(),walls[pos][0].getY()]))
                if ((abs(walls[pos][0].getX()) < 30) and ( walls[pos][0].getY() > 0)):
                    should_change=1
                pos += 1
    elif (mvt_mem.direction=="NORTH"):
        if (mvt_mem.valuable_distance >= mvt_mem.distance_to_go):
            should_change = 1
        else:
            walls = percepts.obstacles.items()
            pos=0
            taille=len(walls)
            while((pos < taille) and not(should_change)):
                toterm(str([pos,walls[pos][0].getX(),walls[pos][0].getY()]))
                if ((abs(walls[pos][0].getX()) < 30) and ( walls[pos][0].getY() < 0)):
                    should_change=1
                pos += 1
    toterm("return " +str(should_change))
    return(should_change)
     
#calcule la prochaine direction
#en prenant en compte les forces mystiques
#qui orchestrent l'exploration
#et attirent vers un pint cardinal
def compute_heading ():
    if (mvt_mem.direction=="EAST"):
        x_modifier=2000
        y_modifier=0
    elif (mvt_mem.direction=="WEST"):
        x_modifier=-2000
        y_modifier=0
    elif (mvt_mem.direction=="SOUTH"):
        x_modifier=0
        y_modifier=+2000
    elif (mvt_mem.direction=="NORTH"):
        x_modifier=0
        y_modifier=-1000
        
    # on construit une liste de TOUS les objets  répulsifs
    repulsives = percepts.attackers.items () + \
                 percepts.explorers.items () + \
                 percepts.homes.items () + \
                 percepts.friends.items () + \
                 percepts.obstacles.items ()

    # valeurs magiques
    theta = 140
    scale = 25
    p = 2
    u = 30
    # composantes X et Y du vecteur direction final
    X = 0
    Y = 0
    if repulsives:
        for it, dist in repulsives:
            X = X + Math.cos (deg2pi (180 + self.towards (it.getX(), it.getY()))) * theta / Math.pow (dist/scale, p)
            Y = Y + Math.sin (deg2pi (180 + self.towards (it.getX(), it.getY()))) * theta / Math.pow (dist/scale, p)
        
    toterm ("AFTER REPULSION:"+str(X) +" "+ str(Y))
    X = X + Math.cos (deg2pi ( self.getHeading() ))*u
    Y = Y + Math.sin (deg2pi ( self.getHeading() ))*u
    toterm ("AFTER ATTRACTION:"+str(X) +" "+ str(Y))
    X+=x_modifier
    Y+=y_modifier 
    toterm ("NEW HEADING : "+str(self.towards (X, Y)))
    self.setHeading (self.towards (X, Y))     
 
