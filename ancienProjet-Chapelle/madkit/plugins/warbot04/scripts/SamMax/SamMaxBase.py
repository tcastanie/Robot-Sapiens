# Warbot - Base - (C) 2004 - Samuel Poujol, Aurélien Campéas

from java.lang import Math
from java.util import Random

groupName = 'sAMetmAX-' +  self.getTeam ()
my_team = self.getTeam ()
priority = 1

# println sur la console : 0/1
console = 1

# Utilitaire
def toterm (msg):
    if console == 1:
        self.println (msg)


def shout_attack (agent):
    toterm ("Ennemy !")
    self.broadcast (groupName, 'killer', 'Attack',
                    [agent.getPerceptType (),
                     str(agent.getX ()),
                     str(agent.getY ()),])    


def doIt ():
    perc = self.getPercepts ()
    for p in perc:
        p_type = p.getPerceptType ()
        if p_type == 'Obstacle' or p_type == 'Rocket' :
            continue
        p_team = p.getTeam ()
        if p_team != my_team:
            shout_attack (p)
     #vider la bal...
    while not self.isMessageBoxEmpty ():
        msg = self.nextMessage ()


def activate ():
    self.createGroup (0, groupName, None, None)
    self.requestRole (groupName, 'killer', None)
    
    toterm ("Base activée... Groupe "+groupName)


def end ():
    toterm ("Mort de la base")

