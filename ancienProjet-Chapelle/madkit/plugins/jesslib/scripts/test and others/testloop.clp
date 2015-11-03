;;-----------------------------------------------------
;;
;;		TestLoop
;;		
;;		Rules R1 and R2 perform a loop. Messages
;;		are read during the loop.
;;		Rules MessageXXX process the incoming messages.
;;
;;-------------------------------------------------------

(createGroup TRUE "testloop")
(requestRole "testloop" "performer")

(assert (ok))

(defrule R1 ""
	?ok <- (ok)
	=>
	(retract ?ok)
	(pause 500)
	(printout t ".")
	(readMessages)
	(assert (ko))
)

(defrule R2 ""
	?ko <- (ko)
	=>
	(retract ?ko)
	(pause 500)
	(printout t "-")
	(readMessages)
	(assert (ok))
)


(defrule MessageReading "Reads messages and do nothing"
	(declare (salience 100))
	?m <- (ActMessage (sender ?sender)(action ?act&:(not (member$ ?act (create$ "halt" "eval"))))(content ?cont))
	=>
	(printout t  "got a message : " ?act " with content: " ?cont crlf)
	(retract ?m)
)

(defrule MessageHalting "Halt the loop when receiving such a message"
	(declare (salience 100))
	?m <- (ActMessage (sender ?sender)(action "halt") (content ?cont))
	=>
	(printout t  "got a halt message! " crlf)
	(retract ?m)
	(halt)
)

(defrule MessageEvaluating "Evaluate the content of the incoming message"
	(declare (salience 100))
	?m <- (ActMessage (sender ?sender)(action "eval") (content ?cont))
	=>
	(printout t  "got an eval message! " crlf)
	(retract ?m)
	(printout t "=> " (eval ?cont) crlf)
)

(defrule EvalStringMessage "Evaluate the content of a StringMessage"
	(declare (salience 100))
	?m <- (StringMessage (sender ?sender)(string ?cont))
	=>
	(printout t  "got a StringMessage! " crlf)
	(retract ?m)
	(printout t "=s=> " (eval ?cont) crlf)
)
