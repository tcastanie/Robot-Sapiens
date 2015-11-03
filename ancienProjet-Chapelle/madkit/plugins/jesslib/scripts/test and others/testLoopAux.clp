(createGroup TRUE "testloop")
(requestRole "testloop" "leader")

(deffunction sendEval (?value)
	(foreach ?x (getAgentsWithRole "testloop" "performer")
		(printout t "j'envoie un message à " ?x crlf)
		(sendActMessage ?x "eval" ?value)))

(deffunction sendHalt ()
	(foreach ?x (getAgentsWithRole "testloop" "performer")
		(printout t "j'envoie un message à " ?x crlf)
		(sendActMessage ?x "halt" "terminato")))


(deffunction sendInfo (?value)
	(foreach ?x (getAgentsWithRole "testloop" "performer")
		(printout t "j'envoie un message à " ?x crlf)
		(sendActMessage ?x "info" "?value")))

(deffunction sendString (?value)
	(foreach ?x (getAgentsWithRole "testloop" "performer")
		(printout t "j'envoie un string message à " ?x crlf)
		(sendStringMessage ?x ?value)))


; (sendInfo "salut les mecs!")
; (sendEval "(+ 30 40)")
; (sendHalt)
; (sendString "(* 3 4)")