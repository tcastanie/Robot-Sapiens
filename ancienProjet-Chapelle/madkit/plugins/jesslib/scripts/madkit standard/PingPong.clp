;;
;; PingPong.clp - The famous Ping-Pong agent (of Ol. Gutknecht) in its Jess version.
;;
;; Copyright (C) 2000 Jacques Ferber
;;
;; This program is free software; you can redistribute it and/or
;; modify it under the terms of the GNU General Public License
;; as published by the Free Software Foundation; either version 2
;; of the License, or any later version.
;;
;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;;
;; You should have received a copy of the GNU General Public License
;; along with this program; if not, write to the Free Software
;; Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.


(defrule startup ""
     =>
	 (printout t "Looking for a Ping-Pong group..." crlf)
	 (if (isGroup "ping-pong")
	  then
		(println "Yeah ! I join")
      else
		(println "Nope ! I create one")
    	(createGroup TRUE "ping-pong"))
	(requestRole "ping-pong" "player")
	(assert (lookingForPartner))
	(assert (nth 5)) ;; plays 5 times...
)

(defrule findPartner ""
	?f <- (lookingForPartner)
	=>
	(bind ?lst (getAgentsWithRole "ping-pong" "player"))
	(retract ?f)
	(if (> (length$ ?lst) 1)
		then
			(foreach ?x ?lst
				(if (neq ?x (getAddress (me)))
			      then
					(sendStringMessage ?x "balle")
					(return) ))
		else
			(assert (lookingForPartner))
			(printout t "waiting for a partner..." crlf)
			(pause 1000))
)


(defrule playing2 ""   
    ?nplay <- (nth ?n&~0)
	?mess <- (StringMessage (sender ?sender))
	=>
	(println "GEE ! My turn...")
	(pause 1000)
	(sendStringMessage ?sender "balle")
	(retract ?nplay)
	(assert (nth (- ?n 1)))
	(retract ?mess)
)

(defrule stop-playing ""   
    ?nplay <- (nth 0)
	=>
	(println "I am fed up of playing.. I stop")
	(retract ?nplay)
)

(reset)
(run)