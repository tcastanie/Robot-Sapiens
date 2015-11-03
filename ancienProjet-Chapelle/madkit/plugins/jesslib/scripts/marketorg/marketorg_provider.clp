;;
;; marketorg_provider.clp - A provider in the agency example, written in Jess. 
;;
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

;; Note: this agent Works exactly as the provider agent written in Java 
;; Thus you may use it with java version of providers.

(deffunction sendACLMessage(?x ?act ?content)
   (bind ?mess (new "madkit.lib.messages.ACLMessage" ?act))
   (call ?mess setContent (call ?content toString))
   (sendMessage ?x ?mess) )
        

(defrule startup1 ""
     =>
        (bind ?t (random))
        (if (> ?t 32000)
         then
                (assert (competence train))
         else
                (assert (competence plane)) )
        
        (assert (price (div (random) 100)))
)

(defrule startup2 ""
        (competence ?c)
        => 
        (createGroup TRUE "travel" "travel-providers")
        (requestRole "travel" "travel-providers" (str-cat ?c "-provider"))
        (println (str-cat "Ticket: " ?c))
)

(defrule bidResponse ""
    ?mess <- (ACLMessage (sender ?sender)(action "REQUEST-FOR-BID"))
        (price ?p)
        =>
        (sendACLMessage ?sender "BID" (new "java.lang.Integer" ?p)) ;; could be simpler to pass an int as a string...
        (retract ?mess)
)

(defrule gotContract ""
    ?mess <- (ACLMessage (sender ?sender)
        (action "MAKE-CONTRACT")
        (content ?cont))
        =>
        (printout t "Received contract: " ?cont crlf)
        (createGroup TRUE "travel" ?cont)
        (requestRole "travel" ?cont "service")
)

(defrule validate ""
    ?mess <- (ACLMessage (sender ?sender)
        (action "VALIDATE"))
        (price ?p)
        =>
        (printout t "Validating contract" crlf)
        (sendACLMessage ?sender "ACCEPT-CONTRACT" (new "java.lang.Integer" ?p))
)

(reset)
(run)
