(show-deftemplates)

(deffunction sendACLMessage(?x ?act ?content)
   (bind ?mess (new "madkit.messages.ACLMessage" ?act))
   (call ?mess setContent ?content)
   (sendMessage ?x ?mess) )


(defrule readACLMessage ""
    	?mess <- (ACLMessage (sender ?sender)(action "propose")(content ?p))
	=>
	(printout t "received proposal: " ?p crlf)	
	(printout t "computation: " (+ ?p 1) crlf)
	(retract ?mess)
)


;; testing
(foreach ?x (getAgentsWithRole "Jess" "member")
     (sendACLMessage ?x "propose" (new "java.lang.Integer" 100)))
