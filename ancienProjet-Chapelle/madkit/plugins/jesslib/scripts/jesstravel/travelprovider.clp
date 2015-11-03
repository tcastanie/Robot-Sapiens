;;
;; travelorgprovider.clp - The travel agency example in Jess version, simplified...
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


;; Note: providers know only the "client" role and not the senders of requests.


(deftemplate travel
    (slot from)
    (slot to)
    (slot price (default 0))
)


(defrule startup ""
     =>
	(bind ?name (str-cat "Air-" (gensym*)))
    (printout t "I am " ?name ", the best travel provider in the world" crlf)
    (assert (name ?name))
    (assert (travel (from Montpellier)(to Paris)(price (div (random) 40))))
    (assert (travel (from Paris)(to Montpellier)(price (div (random) 40))))
    (assert (travel (from Montpellier)(to Nancy)(price (div (random) 40))))
    (assert (travel (from Nancy)(to Montpellier)(price (div (random) 40))))
    (createGroup TRUE "Travel")
    (requestRole "Travel" "provider")
)


(defrule messageReception ""
    ?mess <- (ActMessage (sender ?sender)(action ?act) (content ?cont))
	=>
	(bind ?i (gensym*))
	(printout t ":: got a message " ?act " with content " ?cont crlf)
	(assert (message ?sender ?act ?i))
	(assert-string (str-cat "(message-content " ?i " " ?cont")"))
	(retract ?mess)
)

(defrule reponse ""
    ?trav <- (message ?client "requestTravel" ?i)
	?cont <- (message-content ?i ?f ?t)
    (travel (from ?f) (to ?t)(price ?p))
	(name ?name)
    =>
    (sendActMessage ?client proposal (str-cat ?name " " ?f " " ?t " " ?p))
    (printout t ?client "request a travel from " ?f " to " ?t crlf)
	(printout t "I propose a price of " ?p crlf)
    (retract ?trav)
	(retract ?cont)
)

(defrule gotContract ""
	?mess <- (message ?client "makeContract" ?i)
	?cmess <- (message-content ?i ?from ?to ?p)
	=>
	(printout t "YEAH I got the contract from " ?client crlf)
	(retract ?mess)
	(retract ?cmess)
)

(defglobal ?*time* = (time))
(set-reset-globals FALSE)
(reset)
(run)

(printout t "Elapsed time: " (integer (- (time) ?*time*)) crlf) 