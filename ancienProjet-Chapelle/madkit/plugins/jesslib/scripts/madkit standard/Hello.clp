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
	(println "Hello I'm an agent !" crlf)
	(assert (social-goal "myCommunity" "myGroup" "myRole"))
)

(defrule enteringGroup ""
	?goal <- (social-goal ?com ?group ?role)
	=>
	(bind ?r (createGroup TRUE ?com ?group ))
	(if (= ?r 1)
		then
		   (requestRole ?com ?group ?role)
		   (retract ?goal)
	)
)

;; add rules for handling messages here:
(defrule receivingStringMessage ""
    ?mess <- (StringMessage (sender ?sender)(string ?str))
        =>
        (printout t "Received message: " ?str " from " ?sender crlf)
        (retract ?mess)
)


(reset)
(run)
