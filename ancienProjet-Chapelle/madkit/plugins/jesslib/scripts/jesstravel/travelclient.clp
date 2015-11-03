;;
;; travelclient.clp - The travel agency example in Jess version, simplified...
;; Copyright (C) 1999-2002 Jacques Ferber
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


;; Note: in this version there should have only one client!!


(defglobal ?*nbids* = 1)

(deftemplate goalTravel
	(slot from)
	(slot to)
	(slot price-max (default 0))
)

(deftemplate bid
	(slot provider)
	(slot provider-name)
	(slot from)
	(slot to)
	(slot price)
)

(deftemplate chosen-bid
	(slot provider)
	(slot provider-name)
	(slot from)
	(slot to)
	(slot price)
)

(defrule startup ""
	=>
	(assert (init))
	(printout t "the show is starting.." crlf)
)

(defrule init ""
	?i <- (init)
	(not (chosen-bid (provider ?)(from ?from)(to ?to)(price ?p1)))
	 =>
  	(printout t "I would like a Montpellier - Paris ticket" crlf)
    (assert (goalTravel (from Montpellier)(to Paris)(price-max 2000)))
    (createGroup TRUE "Travel")
	(requestRole "Travel" "client")
	(retract ?i)
)

(defrule cleaningFirst ""
	(init)
	?c <- (chosen-bid (provider ?)(from ?from)(to ?to)(price ?p1))
	 =>
  	(retract ?i)
)

(defrule messageReception ""
    ?mess <- (ActMessage (sender ?sender)(action ?act) (content ?cont))
	=>
	(bind ?i (gensym*))
	(printout t ":: message " ?act " with content " ?cont crlf)
	(assert (message ?sender ?act ?i))
	(assert-string (str-cat "(message-content " ?i " " ?cont")"))
	(retract ?mess)
)

(defrule askForTravel ""
    (goalTravel (from ?from)(to ?to)(price-max ?q))
   =>
	(bind ?lst (getAgentsWithRole "Travel" "provider"))
	(bind ?n (length$ ?lst))
	(assert (n-reception ?n))
	(assert (waiting-answer)) ;; we should add a task number...
	(foreach ?x ?lst
		(printout t "je demande a " ?x " un trajet " ?from "-" ?to crlf)
		(sendActMessage ?x requestTravel (str-cat ?from " " ?to)))
)

(defrule getBid ""
	(waiting-answer)
    (goalTravel (from ?from)(to ?to)(price-max ?q))
	?prop <- (message ?bidder "proposal" ?i)
	?prop2 <- (message-content ?i ?name ?from ?to ?p1)
    ?nrecpt <- (n-reception ?n&~0)
	=>
	(printout t "got a bid of " ?p1 " from " ?name crlf)
	(retract ?nrecpt)
	(assert (n-reception (- ?n 1)))
	(assert (bid (provider ?bidder)(provider-name ?name) (from ?from)(to ?to)(price ?p1)))
	(assert (chosen-bid (provider-name "unknown") (from ?from)(to ?to)(price ?q)))
	(retract ?prop)
	(retract ?prop2)
)

(defrule selection ""
	(waiting-answer)
	(goalTravel (from ?from)(to ?to))
	(n-reception 0)
	?prop-chosen <- (chosen-bid (provider ?)(from ?from)(to ?to)(price ?p1))
	?prop1 <- (bid (provider ?x)(provider-name ?name)(from ?from)(to ?to)(price ?p2&:(< ?p2 ?p1)))
 =>
	(printout t "selection phase..." crlf)
    (retract ?prop1)
	(modify ?prop-chosen (provider ?x)(provider-name ?name)(price ?p2))
)

(defrule decision ""
	?w <- (waiting-answer)
	?goal <- (goalTravel (from ?from)(to ?to))
	?nrecpt <- (n-reception 0)
	(chosen-bid (provider ?y)(provider-name ?yname)(from ?from)(to ?to)(price ?p1))
	(not (bid (provider ?x)(provider-name ?xname) (from ?from)(to ?to)(price ?p2&:(< ?p2 ?p1))))
 =>
	(printout t "the winner is ..." crlf)
    (retract ?goal)
	(retract ?nrecpt)
	(printout t "provider : " ?yname ", with a price : " ?p1 crlf)
	(sendActMessage ?y makeContract (str-cat ?from " " ?to " " ?p1))
	(assert (finished))
    (retract ?w)
)

(defrule getRidOfBids ""
	(finished)
	?b <- (bid (provider ?x)(from ?from)(to ?to)(price ?p2))
	=>
	(retract ?b)
)

(defrule finished ""
	?f <- (finished)
	(not (bid (provider ?x) (from ?from)(to ?to)(price ?p2)))
	=>
	(retract ?f)
)
	

(defglobal ?*time* = (time))
(set-reset-globals FALSE)
(reset)
;(run)

(printout t "Elapsed time: " (integer (- (time) ?*time*)) crlf)