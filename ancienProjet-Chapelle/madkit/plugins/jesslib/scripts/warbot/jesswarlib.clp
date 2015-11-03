;;--------------------------------------------------------------------------------
;;
;;	A JessFile to implement the basic operation of JessAgents for Warbot
;;
;;
;;--------------------------------------------------------------------------------
;;	(C) 1999 - J.Ferber
;;--------------------------------------------------------------------------------

(deftemplate percept
    (slot type)
	(slot x)
	(slot y)
    (slot radius)
	(slot energy)
	(slot team)
)

(define move()
	(call self move))

;; 
;; last rules fired, clean the house
;; at the end of an action
(defrule cleaning-percepts
    (declare (salience -100))
    ?x <- (percept )
    =>
	; (printout t "percepts cleaned" crlf)
	(retract ?x)
)

(defrule cleaning-done-tasks
    (declare (salience -100))
    ?x <- (do-it)
    =>
	(retract ?x)
)

;; if nothing is done, move (more of less randomly...)
(defrule move-around
	(declare (salience -50))
	?d <- (do-it)
   => 
	(move-randomly)	;; move once
	(retract ?d)
	(printout t ":: moving " crlf)
)

;; move to food if some food has been found
;; priority standard!!
(defrule moving-to-food
	(percept 
	   (type SEdit.Formalisms.World.Food)
	   (direction ?d))
	?x <- (do-it)
   => 
	(setdir ?d)
	(move)	;; move once
	(retract ?x)
	(printout t ":: heading " ?d crlf)
)

;; avoiding agents ...
(defrule avoiding-laserWarrior
	(declare (salience 40))
	(percept
		(type Warbot.LaserWarrior)
		(direction ?d)
		(entity ?e&:(isEnnemy ?e))
	)
	?x <- (do-it)
	=>
	;(setdir (mod (+ ?d 180) 360))
	(setdir (mod (+ ?d 180) 360))
	(move) ;; move once
	(retract ?x)
	(printout t ":: avoiding LaserWarrior" crlf)
)

(defrule avoiding-MissileLauncher
	(declare (salience 42))
	(percept
		(type Warbot.MissileLauncher)
		(direction ?d))
	?x <- (do-it)
	=>
	(setdir (mod (+ ?d 180) 360))
	(move) ;; move once
	(retract ?x)
	(printout t ":: avoiding MissileLauncher" crlf)
)


;; shooting missiles
(defrule shooting-missile
	(declare (salience 80))
	(percept
		(type Warbot.Missile)
		(entity ?e))
	(do-it)
	=>
	(printout t ":: shooting missile" crlf)
	(shootLaser ?e)
)

(deffunction isEnnemy (?e)
	(printout t "isEnnemy " ?e crlf)
	(call (me) isEnnemy ?e))

(deffunction shootLaser (?e)
	(call (me) shootLaser ?e))

(deffunction move-randomly ()
	(call (me) randomMove))




  
