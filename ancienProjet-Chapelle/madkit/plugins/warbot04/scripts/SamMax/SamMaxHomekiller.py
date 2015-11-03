from java.lang import Math
from java.util import Random

groupName = 'sAMetmAX-' +  self.getTeam ()
rnd = Random ()
# println sur la console : 0/1
console = 0

# Utilitaires
def toterm (msg):
    if console:
        self.println (msg)

def whine (msg):
    toterm ("BUG : " + msg)

# ...pour l'environnement
def initenv ():
    env = { }

def setenv (var, val):
    env [var] = val

def getenv (var):
    if env.has_key (var):
        return env [var]
    else:
        whine ("variable N'EXISTE PAS dans ENV !-]")
        return None

def immobile ():
    return not self.isMoving ()

#--------------TRAITEMENT MESSAGES------------------

# Ugly hack (dans une classe, les méthodes de l'agent
# sont hors de portée à cause du shadowing de 'self')
isMsgBoxEmpty = self.isMessageBoxEmpty
nextMsg = self.readMessage

# Une classe qui les encode (pour 1 tour...)
class Messages:
    def __init__ (self):
        self.tg_attackers = [ ]
        self.tg_explorers = [ ]
        self.tg_homes = [ ]
        self.help_wanted = { }

    def update (self):
        self.__init__ ()
        while not isMsgBoxEmpty ():
            message = nextMsg ()
            act = message.getAct ()
            x = message.getFromX ()
            y = message.getFromY ()
            content = message.getContent ()

            if act == 'Attack':
                # contenu : 'type', 'x', 'y'
                atype = content [0]
                coord = (float (content [1]) + x,
                         float (content [2]) + y)
                if atype == 'Home':
                    self.tg_homes.append (coord)
                elif atype == 'Explorer':
                    self.tg_explorers.append (coord)
                elif atype == 'RocketLauncher':
                    self.tg_attackers.append (coord)
                else:
                    whine ("type de cible inconnu !")
            else:
                whine ("type de message inconnu !")

    def help_base (self):
        return len (self.help_wanted) > 0

    def kill_home (self):
        return len (self.tg_homes) > 0

    def attack_ennemy (self):
        return (len (self.tg_attackers) +
                len (self.tg_explorers) +
                len (self.tg_homes)) > 0
    
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
        self.bases = { } ; self.obstacles = { } 
        self.nb_ennemies = (0,0,0) # cache le nb d'ennemis (1)

    def update (self, perc):
        self.reset ()
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
                if p_type == 'Home':
                    self.bases [p] = p.getDistance ()
                else:
                    self.friends [p] = p.getDistance ()
        self.nb_ennemies = self._ennemy_count ()

    # (1) Workaround pour un bug qui n'existe peut-être plus
    def _ennemy_count (self):
       return (len (self.attackers),
                len (self.explorers),
                len (self.homes))


# ----------------------------------------------------------
# ----------------- LES TACHES -----------------------------
# ----------------------------------------------------------
# Toutes les tâches/continuations possibles

#----------------EXPLORATION------------------------
def init_explore ():
    initenv ()
    explore_loop ()

def explore_loop ():
    # calculer la prochaine transition (décision locale)    
    nb_ennemies = percepts.nb_ennemies
    if nb_ennemies  == (0,0,0):
        gm.tasks.add_task ('explore_loop', [])
    else:
        if nb_ennemies [0] > 0 or nb_ennemies [2] > 0:
            return gm.interrupt_goal ('A')
        else:
            return gm.interrupt_goal ('K')
        
    # choisir une direction et se déplacer
    leftist = getenv ('leftist')
    if immobile ():
        if leftist:
            self.setHeading (self.getHeading () - 15)
        else:
            self.setHeading (self.getHeading () + 15)
    self.move ()

# Opérations sur des angles

def add_angles (a1, a2):
    a = a1 + a2
    if a > 359:
        return a - 360
    elif a < 0:
        return 360 - a
    return a

def diff_angles (a1, a2):
    return round_angles (max (a1, a2) - min (a1, a2))

def round_angles (angle):
    if angle > 180:
        return abs (angle - 360)
    return angle

def angle_in_range (angle, inf, sup):
    if inf > sup:
        return (angle <= sup and angle >=0) or \
               (angle >= inf and angle <=360)
    return angle >= inf and angle <= sup


#----------------ATTAQUE----------------------------
def init_attack ():
    #choisir un ennemi, se battre jusqu'à la mort
    initenv ()
    setenv ('shoot_time', 3)
    setenv ('evasion-timeout', 4)
    attack_loop ()

def deg2pi (deg):
    return deg / 180 * Math.PI

def vector_x (percept, dist, scale, theta, p): 
    return Math.cos (deg2pi (180 + self.towards (percept.getX(), percept.getY()))) * theta / Math.pow ((dist / scale), p)

def vector_y (percept, dist, scale, theta, p): 
    return Math.sin (deg2pi (180 + self.towards (percept.getX(), percept.getY()))) * theta / Math.pow ((dist / scale), p)

# Compenser le bug Madkit : redéfinit le centre de l'objet pour les Bases
def bug_vector_x (percept, dist, scale, theta, p):
    r = percept.getRadius () / 2
    return Math.cos (deg2pi (180 + self.towards (percept.getX()+r, percept.getY()+r))) * theta / Math.pow ((dist / scale), p)

def bug_vector_y (percept, dist, scale, theta, p):
    r = percept.getRadius () / 2    
    return Math.sin (deg2pi (180 + self.towards (percept.getX()+r, percept.getY()+r))) * theta / Math.pow ((dist / scale), p)


# calculer l'orientation
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
        X = X + bug_vector_x (it, dist, 60, 70, 2)
        Y = Y + bug_vector_y (it, dist, 60, 70, 2)
    for it, dist in percepts.attackers.items ():
        # devrait être négligeable...
        X = X + vector_x (it, dist, 30, 70, 2)
        Y = Y + vector_y (it, dist, 30, 70, 2)
    for it, dist in percepts.explorers.items ():
        X = X + vector_x (it, dist, 20, 60, 1)
        Y = Y + vector_y (it, dist, 20, 60, 1)
    for it, dist in percepts.homes.items ():
        X = X + bug_vector_x (it, dist, 60, 70, 2)
        Y = Y + bug_vector_y (it, dist, 60, 70, 2)
    for it, dist in percepts.obstacles.items ():
        radius = it.getRadius ()
        X = X + vector_x (it, dist, 50+radius, 60, 2)
        Y = Y + vector_y (it, dist, 50+radius, 60, 2)
        
    # pour la cible
    if mode == 'flee':
        offset = 180
        u = 12 # on s'arrache viiiiite !!
    else:
        offset = 0
        u = 8
    X = X + Math.cos (deg2pi (offset + self.towards (target.getX(), target.getY()))) * u * target.getDistance ()
    Y = Y + Math.sin (deg2pi (offset + self.towards (target.getX(), target.getY()))) * u * target.getDistance ()
    if immobile ():
        toterm ("immobile")
    self.setHeading (self.towards (X, Y))


# choisir la meilleure cible (ie la plus proche)
def choose_best_target (target_type):
    if target_type == 'explorer':
        candidates = percepts.explorers
    elif target_type == 'attacker':
        candidates = percepts.attackers
    else:
        candidates = percepts.homes

    assert (len (candidates) > 0)

    best = (0, 999999, 4000)
    for k, d in candidates.items ():
        if d < best [1]:
            best = (k, d, 0)

    # annonce publique...
    agent = best [0]
    self.broadcast (groupName, 'killer', 'Attack',
                    [agent.getPerceptType (),
                    str(agent.getX ()),
                    str(agent.getY ()),])
                    
    return (best[0], best[1])

# Tirer !
# ...sauf si on nique un copain...

def shoot (tx, ty):
    # shooooot !
    time = getenv ('shoot_time')
    if time > 1 and can_shoot (tx, ty):
        setenv('shoot_time', 0)
        self.launchRocket (self.towards (tx, ty))
        return 1
    else:
        time = time + 1
        setenv ('shoot_time', time)
        return 0

# Permis de tirer...

def can_shoot (tx, ty):
    s_angle = self.towards (tx, ty)
    for fr, dist in percepts.friends.items ():
        fr_angle = self.towards (fr.getX(), fr.getY())
        if in_cone (s_angle, fr_angle, fr.getDistance (), fr.getRadius ()):
            return 0
    return 1

def in_cone (a1, a2, d2, radius):
    # bug madkit ;-) parfois on est DANS l'objet...
    if d2 <= radius:
        d2 = radius + 1
    delta = Math.asin (radius / d2) + 15
    cone_sup = add_angles (a2, delta)
    cone_inf = add_angles (a2, -delta)
    rule = angle_in_range (a1, cone_inf, cone_sup)
    return rule 
    
    
# Fuite, honteuse certes, mais...

def flee (target):
    if len (percepts.attackers) > 0:
        target = choose_best_target ('attacker') [0]
    compute_heading (target, 'flee')
    # on peut shooter également, y a pas de raison
    tx = target.getX ()
    ty = target.getY ()
    if not shoot (tx, ty):
        self.move ()

    timeout = getenv ('evasion-timeout')
    if timeout  > 0:
        gm.tasks.add_task ('flee', [target])
    else:
        setenv ('evasion-timeout', 4)
        gm.tasks.add_task ('attack_loop', [])
    if not self.getShot ():
        setenv ('evasion-timeout', timeout - 1)
    else:
        setenv ('evasion-timeout', timeout + 4)
        

def attack_loop ():
    # réexaminer la situation, prévoir la suite...
    attackers, explorers, homes = percepts.nb_ennemies
    if attackers == 0:
        if homes == 0:
            if explorers == 0:
                if bal.attack_ennemy ():
                    return find_ennemy ()
                else:
                    # ne restent que des cendres fumantes...
                    return gm.resume_previous_goal ()
            else:
                target, dist = choose_best_target ('explorer') 
        else:
            target, dist = choose_best_target ('home')
    else:
        target, dist = choose_best_target ('attacker')        
    #    return gm.interrupt_goal ('K')

    # diagnostic : on est outnumbered -> cassos !
    if (attackers > 0) \
           or self.getShot ():
        return flee (target)

    # d'abord tirer (si on peut), on discute après
    tx = target.getX()
    ty = target.getY()
    if not shoot (tx, ty): 
        compute_heading (target, 'attack')
        self.move ()

    gm.tasks.add_task ('attack_loop', [])

# Un wrapper pour les ennemis qui sont pas dans
# nos percepts (on a reçu leurs coordonnées par
# la poste)
class PseudoTarget:
    def __init__ (self, x, y):
        self.x = x
        self.y = y

    def getX (self):
        return self.x

    def getY (self):
        return self.y

    def getDistance (self):
        return 100

def distance (x, y):
    return Math.sqrt (x*x + y*y)

def choose_distant_ennemy ():
    ennemies = bal.tg_attackers
    # hola ! et si l'ennemi a fui ?
    if len (ennemies) == 0:
        return None

    target = ennemies [0]
    dist = distance (target[0], target[1])
    for e in ennemies:
        d = distance (e[0], e[1])
        if d < dist:
            dist = d
            target = e
    return target

def find_ennemy ():
    # décision locale
    attackers, explorers, homes = percepts.nb_ennemies
    if attackers > 0:
        return attack_loop ()

    # ok on continue
    target = choose_distant_ennemy ()
    if not target: # a pu ! un home ?
        return find_home ()

    pseudo_target = PseudoTarget (target[0], target[1])
    compute_heading (pseudo_target, 'attack')
    self.move ()
    gm.tasks.add_task ('find_ennemy', [])


#--------------ATTAQUE HOME ENNEMI-----------------
def init_kill_home ():
    initenv ()
    setenv ('shoot_time', 2)
    find_home ()

def choose_distant_home ():
    homes = bal.tg_homes
    # si y a pu d'ennemi, faut pas s'en faire
    if len (homes) == 0:
        return None
    
    target = homes [0]
    dist = distance (target[0], target[1])
    for e in homes:
        d = distance (e[0], e[1])
        if d < dist:
            dist = d
            target = e
    return target

def find_home ():
    # décision locale
    attackers, explorers, homes = percepts.nb_ennemies
    if attackers > 0 or homes > 0: # trouvé !
        return attack_loop () # hmmmm....

    # ok on continue
    target = choose_distant_home ()
    if not target: # pu personne :)
        return gm.resume_previous_goal ()

    pseudo_target = PseudoTarget (target [0], target[1])
    compute_heading (pseudo_target, 'attack')
    self.move ()
    gm.tasks.add_task ('find_home', [])
    

# ----------------PLAN----------------------------
# La liste des tâches de l'Agent pour un tour

class TaskList:
    def __init__ (self):
        #task == ('name', [args])
        self.tasks = []
        self.alltasks = {
            'init_explore' : init_explore,
            'explore_loop' : explore_loop,
            'init_attack' : init_attack,
            'attack_loop' : attack_loop,
            'flee' : flee,
            'find_ennemy' : find_ennemy,
            'init_kill_home' : init_kill_home,
            'find_home' : find_home,
            }

    def add_task (self, task_name, args):
        assert (self.alltasks.has_key (task_name))
        self.tasks.append ((task_name, args))

    def has_next (self):
        return len (self.tasks) != 0
        
    def execute_next (self):
        assert (len (self.tasks) != 0)
        todo = self.tasks.pop ()
        toterm ("exec'ing task ["+str(todo)+"]")
        apply (self.alltasks [todo [0]], todo [1])

# Gestionnaire des Buts et de l'Engagement !-)

def top (stack):
    return stack [len (stack) - 1]

class GoalManager:
    def __init__ (self):
        self.goals = ['E']
        # priorités initiales
        self.priority = { 'E':1, 'A':3, 'D':5, 'K':9 }
        # objectifs futurs (de type 'K' en particulier)
        self.future_goals = []
        self.tasks = TaskList ()

    # but atteint/interrompu
    #  a) revenir au but 'inférieur'
    #  b) initialiser ce but
    def resume_previous_goal (self):
        self.goals.pop (len (self.goals) - 1)
        self.tasks = TaskList ()
        if len (self.goals) == 0:
            self.goals.append ('E')
        self.init_goal ()

    # interruption de but -> nouveau but
    def interrupt_goal (self, new_goal):
        goal_idx = len (self.goals) - 1
        self.goals [goal_idx] = new_goal
        self.tasks = TaskList ()
        self.init_goal ()

    # méthode principale
    def resume_action (self):
        # fonction de décision/transition
        new_goal = self.select_goal ()
        curr_goal = top (self.goals)

        # changé / pas changé ?
        if new_goal != curr_goal:
            self.interrupt_goal (new_goal)

        # exécuter le plan courant
        curr_tasks = self.tasks
        self.tasks = TaskList ()
        while curr_tasks.has_next ():
            curr_tasks.execute_next ()

    # sélection du but courant
    # 1) calcul de situation
    # 2) maintien/changement de but courant
    def select_goal (self):
        curr_goal = top (self.goals)
        curr_prio = self.priority [curr_goal]
        
        # si on est en vadrouille, tout est simple
        if curr_goal == 'E':
            if bal.attack_ennemy ():
                curr_goal = 'A'
        
        return curr_goal

    # un but a été choisi, démarrer le plan associé
    def init_goal (self):
        curr_goal = top (self.goals)
        if curr_goal == 'E':
            init_explore ()
        elif curr_goal == 'A':
            init_attack ()
        else:
            whine ("BUT inconnu !")
            
#-------------------MAINLOOP------------------------
# Objets globaux
# gestionnaire de buts
gm = GoalManager ()
# percepts
percepts = PerceptBag (self.getTeam ())
# boîte aux lettres
bal = Messages ()
# environnement
env = { }

def doIt ():
    percepts.update (self.getPercepts ())
    bal.update ()
    gm.resume_action ()

def activate ():
    self.createGroup (0, groupName, None, None)
    self.requestRole (groupName, 'killer', None)
    gm.tasks.add_task ('init_explore', [])
    setenv ('leftist', 1) # tourne-à-gauche
    if rnd.nextDouble () * 3 > 1:
        toterm ("chiraquien !")
        setenv ('leftist', 0)
    toterm ("RocketLauncher activé... Groupe "+groupName)

def end ():
    self.broadcast (groupName, 'killer', 'Dying',
                    self.getName ())
    toterm ("Terminaison du RocketLauncher...")
